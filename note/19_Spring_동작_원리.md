# Spring 동작 원리: 리플렉션, 애노테이션, 프록시, AOP

> 중요도: **A** · 이 장의 목표: 애노테이션이 무엇을 하는지 리플렉션으로 설명하고, 프록시가 메서드 호출을 가로채는 구조를 만든 뒤, `@Transactional`이 같은 클래스 안의 호출에서 적용되지 않는 이유를 설명한다.

> 2026-11-23 추가 장 (로드맵 1순위 "Spring 동작 원리(프록시·AOP·애노테이션·리플렉션)" 항목, 18장 Spring Core의 심화). 리플렉션, 애노테이션, JDK 동적 프록시 예제는 JDK 25에서 실행해 확인했다. **Spring 컨테이너에서 `@Transactional`이 실제로 어떻게 동작하는지는 Spring 프로젝트를 띄워 확인하지 않았다.** 해당 부분은 원리 설명으로만 다룬다.

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
