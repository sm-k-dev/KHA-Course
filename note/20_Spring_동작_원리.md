# Spring 동작 원리: 리플렉션, 애노테이션, 프록시, AOP

> 중요도: **A** · 이 장의 목표: 애노테이션이 무엇을 하는지 리플렉션으로 설명하고, 프록시가 메서드 호출을 가로채는 구조를 만든 뒤, `@Transactional`이 같은 클래스 안의 호출에서 적용되지 않는 이유를 설명한다.

> 2026-11-23 추가 장 (로드맵 1순위 "Spring 동작 원리(프록시·AOP·애노테이션·리플렉션)" 항목, 19장 Spring Core의 심화). 리플렉션, 애노테이션, JDK 동적 프록시 예제는 JDK 25에서 실행해 확인했다. **Spring 컨테이너에서 `@Transactional`이 실제로 어떻게 동작하는지는 Spring 프로젝트를 띄워 확인하지 않았다.** 해당 부분은 원리 설명으로만 다룬다.

---

## 1. 애노테이션은 설정을 코드에 붙이는 표시다

**정의** — 애노테이션은 클래스, 메서드, 필드에 붙이는 메타데이터다. 그 자체로는 아무 일도 하지 않는다. **누군가 읽어서 동작을 바꿀 때** 의미가 생긴다.

```java
@Retention(RetentionPolicy.RUNTIME)   // 실행 중에도 읽을 수 있게 한다
@Target(ElementType.METHOD)
@interface Audit {
    String value();
}
```

- `@Retention(RUNTIME)`이 없으면 컴파일 후 사라져서 리플렉션으로 읽을 수 없다.
- Spring의 `@Component`, `@Transactional`, `@Autowired`도 이런 식으로 런타임에 읽힌다.

---

## 2. 리플렉션: 실행 중에 클래스와 메서드를 읽는다

```java
Method m = OrderService.class.getMethod("place", String.class);
Audit a = m.getAnnotation(Audit.class);
System.out.println(a.value());          // 애노테이션 값
Object result = m.invoke(new OrderService(), "book");  // 메서드 호출
```

Spring은 빈을 만들 때 클래스를 읽고, 애노테이션이 붙은 메서드를 찾아서 그에 맞는 처리를 끼워 넣는다.

---

### 2-1. 커스텀 애노테이션 만들고 리플렉션으로 읽기 (2026-11-23 보강, JDK 25에서 확인함)

`@Transactional`, `@GetMapping` 같은 애노테이션도 같은 방식으로 만들어졌다. 직접 만들어 보면 "애노테이션은 데이터일 뿐이고, 읽는 코드가 동작을 정한다"는 점이 분명해진다.

```java
import java.lang.annotation.*;
import java.lang.reflect.Method;

public class CustomAnn {
    @Retention(RetentionPolicy.RUNTIME)     // 실행 중에도 읽을 수 있게 유지
    @Target(ElementType.METHOD)              // 메서드에만 붙일 수 있다
    @interface Audit {
        String action();
        int level() default 1;
    }

    static class OrderService {
        @Audit(action = "cancel", level = 2)
        public void cancel() { System.out.println("cancel called"); }
        public void view() { System.out.println("view called"); }
    }

    public static void main(String[] args) throws Exception {
        for (Method m : OrderService.class.getDeclaredMethods()) {
            Audit a = m.getAnnotation(Audit.class);
            if (a != null) {
                System.out.println(m.getName() + " action=" + a.action() + " level=" + a.level());
                m.invoke(new OrderService());
            } else {
                System.out.println(m.getName() + " (no @Audit)");
            }
        }
    }
}
```

**실행 결과 (확인함)**

```
cancel action=cancel level=2
cancel called
view (no @Audit)
```

**읽는 법**
- `@Retention(RUNTIME)`을 빼면 `getAnnotation`이 `null`을 돌려준다. 컴파일 후 애노테이션이 사라지기 때문이다.
- `getDeclaredMethods()`의 순서는 JVM에 따라 다를 수 있다. 결과를 순서에 기대면 안 된다.
- Spring은 이 원리로 `@Transactional`이나 `@Cacheable`이 붙은 메서드를 찾고, 프록시로 감싼다.

**면접 기준**: "애노테이션은 메타데이터이고, `RUNTIME` 보존 정책이 있어야 리플렉션으로 읽을 수 있습니다. Spring은 이 정보를 읽어 프록시를 만듭니다."

## 3. 프록시: 호출을 가로채는 대리 객체

**정의** — 프록시는 원래 객체와 같은 인터페이스를 가진 대리 객체다. 클라이언트는 프록시를 호출하고, 프록시는 **앞뒤에 부가 작업**(트랜잭션 시작, 로그, 권한 검사)을 한 뒤 원래 객체를 호출한다.

```
클라이언트 → 프록시 (트랜잭션 시작) → 실제 객체 → 프록시 (커밋 또는 롤백) → 클라이언트
```

Java 표준 `java.lang.reflect.Proxy`로 간단히 만들 수 있다. 인터페이스가 있어야 한다.

```java
interface Greeter { String greet(String name); }

Greeter real = name -> "hello " + name;
Greeter proxy = (Greeter) Proxy.newProxyInstance(
        Greeter.class.getClassLoader(),
        new Class<?>[]{ Greeter.class },
        (p, method, args) -> {
            System.out.println("before " + method.getName());
            Object r = method.invoke(real, args);
            System.out.println("after " + method.getName());
            return r;
        });
System.out.println(proxy.greet("kim"));
```

---

## 4. AOP: 부가 기능을 한곳에서 모은다

**정의** — AOP(관점 지향 프로그래밍)는 로그, 트랜잭션, 권한처럼 여러 곳에 반복되는 기능을 **관점(Aspect)**으로 분리해서 필요한 지점(Join Point)에 끼워 넣는 방식이다.

| 용어 | 뜻 | Spring 예 |
|---|---|---|
| Aspect | 부가 기능 묶음 | 트랜잭션 관리 |
| Join Point | 끼워 넣을 수 있는 지점 | 메서드 실행 |
| Pointcut | 실제로 끼워 넣을 지점 선택 | `@Transactional`이 붙은 메서드 |
| Advice | 끼워 넣는 코드 | 시작, 커밋, 롤백 |

Spring AOP는 대부분 **프록시 기반**이다. 빈으로 등록된 객체를 호출하면, 실제로는 프록시가 먼저 호출을 받는다.

---

## 5. `@Transactional`이 같은 클래스 안에서 안 먹는 이유

```java
@Service
public class OrderService {
    public void place(String item) {
        save(item);            // (1) 같은 객체의 메서드를 this로 호출
    }

    @Transactional
    public void save(String item) { ... }
}
```

**왜 안 되는가**

- 외부에서 `orderService.save()`를 호출하면 **프록시**를 거치므로 트랜잭션이 시작된다.
- 하지만 `place()` 안의 `save(item)`은 `this.save(item)`이다. `this`는 **프록시가 아니라 실제 객체**다. 프록시를 거치지 않아서 트랜잭션이 시작되지 않는다.

**해결 방법**

1. 메서드를 다른 빈으로 분리해서 프록시를 거치게 한다. (가장 깔끔하다)
2. 자기 자신을 주입받아 호출한다. (순환 참조 문제가 생길 수 있어 권장하지 않는다)
3. 호출 흐름을 바꿔 트랜잭션 경계를 바깥 메서드에 둔다.

**주의** — `private` 메서드, `final` 메서드, `static` 메서드에는 프록시 기반 AOP가 적용되지 않는다. 프록시가 오버라이드할 수 없기 때문이다.

---

## 6. 검증 코드 (JDK 기능만 사용)

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

public class ReflectionProxyCheck {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Audit { String value(); }

    public static class OrderService {
        @Audit("order")
        public String place(String item) { return "placed " + item; }
    }

    interface Greeter { String greet(String name); }

    public static void main(String[] args) throws Exception {
        Method m = OrderService.class.getMethod("place", String.class);
        System.out.println("annotation = " + m.getAnnotation(Audit.class).value());
        System.out.println("invoke = " + m.invoke(new OrderService(), "book"));

        Greeter real = name -> "hello " + name;
        Greeter proxy = (Greeter) Proxy.newProxyInstance(
                Greeter.class.getClassLoader(),
                new Class<?>[]{ Greeter.class },
                (p, method, args2) -> {
                    System.out.println("before " + method.getName());
                    Object r = method.invoke(real, args2);
                    System.out.println("after " + method.getName());
                    return r;
                });
        System.out.println(proxy.greet("kim"));
    }
}
```

**실행 결과 (확인함)**

```
annotation = order
invoke = placed book
before greet
after greet
hello kim
```

---

### 6-2. Spring 요청 처리 흐름: Filter → Interceptor → Controller (2026-11-23 보강, 확인함)

**정의**: 요청은 서블릿 필터(Filter)를 거쳐 `DispatcherServlet`에 도착하고, 그 안에서 인터셉터(HandlerInterceptor)가 컨트롤러 전후에 끼어든다. 예외가 나면 `@RestControllerAdvice`가 응답을 만든다.

```
요청 → Filter(전) → DispatcherServlet → Interceptor.preHandle
     → Controller → Interceptor.postHandle
     → 뷰/응답 작성 → Interceptor.afterCompletion → Filter(후) → 응답
```

**확인 방법**: 이 PC에서는 Java의 루프백 소켓이 막혀 실제 서버를 띄우지 못했다(34장 6-2). 그래서 `MockMvc`의 standalone 설정으로 같은 체인을 만들어 순서를 확인했다. 이 설정은 Spring Boot 전체 기동과 완전히 같지는 않다. 순서만 확인하는 용도다.

```java
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FlowDemo {
    static final List<String> log = new ArrayList<>();

    @RestController
    static class HelloController {
        @GetMapping("/hello")
        String hello() { log.add("controller"); return "hi"; }

        @GetMapping("/boom")
        String boom() { log.add("controller"); throw new IllegalStateException("boom"); }
    }

    static class LogInterceptor implements HandlerInterceptor {
        @Override public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object h) {
            log.add("interceptor.pre"); return true;
        }
        @Override public void postHandle(HttpServletRequest req, HttpServletResponse res, Object h, ModelAndView mv) {
            log.add("interceptor.post");
        }
        @Override public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object h, Exception ex) {
            log.add("interceptor.after");
        }
    }

    @RestControllerAdvice
    static class ErrorAdvice {
        @ExceptionHandler(IllegalStateException.class)
        ResponseEntity<String> handle(IllegalStateException e) {
            log.add("advice");
            return ResponseEntity.status(409).body("conflict: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Filter logFilter = (req, res, chain) -> {
            log.add("filter.before");
            chain.doFilter(req, res);
            log.add("filter.after");
        };

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new HelloController())
                .addFilters(logFilter)
                .addInterceptors(new LogInterceptor())
                .setControllerAdvice(new ErrorAdvice())
                .build();

        var ok = mvc.perform(get("/hello")).andReturn();
        System.out.println("/hello -> " + ok.getResponse().getStatus() + " " + ok.getResponse().getContentAsString());
        System.out.println("order: " + log);
        log.clear();

        var err = mvc.perform(get("/boom")).andReturn();
        System.out.println("/boom -> " + err.getResponse().getStatus() + " " + err.getResponse().getContentAsString());
        System.out.println("order: " + log);
    }
}
```

**실행 결과 (확인함)**

```
/hello -> 200 hi
order: [filter.before, interceptor.pre, controller, interceptor.post, interceptor.after, filter.after]
/boom -> 409 conflict: boom
order: [filter.before, interceptor.pre, controller, advice, interceptor.after, filter.after]
```

**읽는 법**
- `/hello`는 정상 흐름이라 `postHandle`이 실행된다.
- `/boom`은 컨트롤러에서 예외가 나서 `postHandle`이 빠졌다. 대신 `advice`가 응답을 만들었고, `afterCompletion`은 예외와 상관없이 실행됐다.
- 필터는 `DispatcherServlet`보다 바깥에 있다. 그래서 인증 같은 공통 처리를 필터나 Security 필터 체인에서 한다. 컨트롤러 기준의 공통 처리(예: 어떤 핸들러가 호출되는지 알아야 하는 것)는 인터셉터에서 한다.
- 실제 Spring Boot 앱에서는 필터를 `@Component`로, 인터셉터를 `WebMvcConfigurer.addInterceptors`로 등록한다. 이 설정 코드는 **미실행**이다.

**면접 기준**: "요청은 Filter를 먼저 거치고, DispatcherServlet이 핸들러를 고르고 Interceptor가 전후에 끼어듭니다. 예외는 ControllerAdvice가 응답으로 바꿉니다."

### 6-3. 한 요청이 DB까지 가는 길: GET /test 추적 (2026-11-24 보강, 선생님 자료 기반)

**출처**: 선생님 자료 `springboot4☆/SpringBoot4-chapter3-request-flow.html`의 14단계 추적을 정리했다. 원 자료는 실제 실행 로그를 함께 보여 준다. 이 PC에서는 Spring Boot 서버를 띄우지 않아서 전체 경로는 **미실행**이다. 각 구간은 교재의 검증된 예제와 연결해서 확인했다.

| 순서 | 무슨 일이 일어나나 | 어디서 확인했나 |
|---|---|---|
| 1 | `main`이 실행되고 스프링 컨테이너가 만들어진다 | 19장 (컨테이너와 빈) |
| 2 | `@Autowired`로 서비스와 저장소 객체가 연결된다 | 19장 (DI 검증) |
| 3 | `@Entity` 클래스를 읽어 테이블을 만든다 | 25장 (엔티티와 매핑) |
| 4 | `data.sql`로 초기 데이터를 넣는다 | 이 PC에서 미실행 |
| 5 | `GET /test` 주소에 맞는 메서드를 찾는다 | 20장 6-2 (DispatcherServlet 흐름) |
| 6 | 컨트롤러가 서비스에 업무를 맡긴다 | 17장 (계층 구조) |
| 7 | 저장소가 `findAll()`로 SQL `SELECT`를 보낸다 | 25장 (JPQL과 쿼리) |
| 8 | DB가 행 3줄을 돌려준다 | 07장 (SQL 결과) |
| 9 | 행을 `Member` 객체로 바꾼다 | 25장 (영속성 컨텍스트) |
| 10 | 목록이 서비스와 컨트롤러로 돌아간다 | 17장 (계층 구조) |
| 11 | 목록을 JSON 글자로 바꿔 응답한다 | 18장 (JSON과 REST, MockMvc 검증) |

**읽는 법**
- 요청은 **위에서 아래로 한 번** 지나간다. 시작할 때 만든 객체(빈)를 요청마다 재사용하고, 요청마다 새로 만드는 것은 영속성 컨텍스트(트랜잭션 단위)다.
- 컨트롤러는 DB를 직접 모른다. 서비스를 거쳐 저장소에 맡긴다. 계층을 나눠 두면 한 부분을 바꿔도 다른 부분이 덜 흔들린다.
- 응답 본문은 객체를 JSON으로 바꾼 결과다. 상태 코드와 헤더는 응답 객체가 정한다(18장).

**디버깅할 때 쓰는 질문**: 요청이 어느 단계에서 멈췄는지 먼저 찾는다. 404면 5단계(주소 매핑)를, 빈을 찾지 못했다는 오류면 2단계(스캔 범위, 19장)를, 빈 목록이면 4단계(초기 데이터)와 7단계(SQL)를 본다.

**면접 기준**: "요청은 DispatcherServlet이 컨트롤러 메서드로 연결하고, 컨트롤러 → 서비스 → 저장소 → DB 순서로 내려갔다가, 결과 객체가 JSON으로 바뀌어 올라옵니다. 시작할 때 빈을 만들어 두고 요청마다 재사용합니다."

## 7. 실습 과제

1. 검증 코드에서 `@Retention(RetentionPolicy.RUNTIME)`을 지우고 `getAnnotation`이 `null`을 반환하는지 확인한다.
2. 프록시의 `before`와 `after` 사이에 예외를 던지게 하고, 바깥에서 어떤 예외가 보이는지 적는다.
3. `OrderService` 예제를 Spring 프로젝트에 옮겨서 `place()`에서 `save()`를 호출할 때 트랜잭션 로그가 찍히는지 확인해 본다. (프로젝트가 있을 때만)

---

## 단원 통과 기준

- [ ] 애노테이션은 스스로 동작하지 않고, 리플렉션으로 읽혀야 의미가 생긴다는 것을 설명할 수 있다.
- [ ] 프록시가 호출 앞뒤에 끼어드는 구조를 그림으로 그릴 수 있다.
- [ ] `@Transactional`이 같은 클래스 안의 호출에서 적용되지 않는 이유를 설명하고, 해결 방법을 두 가지 이상 말할 수 있다.
- [ ] `private`과 `final` 메서드에 프록시 AOP가 적용되지 않는 이유를 설명할 수 있다.
