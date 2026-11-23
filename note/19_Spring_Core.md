# Spring Framework 핵심

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `10_SpringBoot_3_4`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — 객체 · Spring Bean · DI · IoC

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

객체는 프로그램 실행 중 존재하는 인스턴스이고, Spring Bean은 Spring 컨테이너가 생성하거나 등록해 관리하는 객체다. DI는 객체가 필요한 의존성을 외부에서 제공받는 방식이다.

### 2. 용어를 구분해야 하는 이유

`new`로 만든 모든 객체가 Spring Bean은 아니다. `@Component`는 컴포넌트 스캔 등록 방식이고 `@Bean`은 설정 메서드로 등록하는 방식이다.

### 3. 새로운 상황에 적용

생성자 주입된 `UserService`와 직접 `new UserService(...)`한 객체가 어떤 컨테이너 관리 차이를 가지는지 설명한다.

### 4. 면접 질문과 답변 기준

**질문:** 객체 · Spring Bean · DI · IoC의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** Spring은 객체의 생성·연결·생명주기를 컨테이너에서 관리할 수 있습니다. 의존성 주입은 객체가 직접 의존 대상을 결정하지 않게 해 결합을 줄입니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. IoC·DI·Bean

**정의** — IoC는 객체 생성·연결·생명주기 등의 제어가 애플리케이션 코드 바깥의 컨테이너로 이동하는 설계 원리다. DI는 객체의 의존성을 외부에서 제공하는 방식이고 Bean은 Spring 컨테이너가 관리하는 객체다.

**목적과 책임** — 구성·교체·테스트를 쉽게 하고 객체 간 결합을 조절한다.

**구별해야 할 개념 / 흔한 오해** — IoC ≠ DI ≠ Bean. 모든 Java 객체가 Bean인 것은 아니다.

**실행·처리 흐름** — 컨테이너 초기화 → Bean 정의 탐색 → 생성·의존성 연결 → 사용

**직접 구현·검증** — 생성자 주입으로 결제 수단 교체 예제 구현. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — IoC와 DI의 관계, Bean의 정확한 의미는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

IoC는 객체 생성·호출 등 제어의 일부가 애플리케이션 코드 외부의 프레임워크로 이동하는 원칙이다. DI는 의존 객체를 외부에서 제공하는 구체적 설계 기법이다. Spring Bean은 Spring IoC 컨테이너가 생성·관리하는 객체다. Bean과 일반 Java 객체는 동일 개념이 아니다.

### 직접 읽고 구현하는 독립 예제

```java
@Service
class PaymentService {
 private final PaymentRepository repository;
 PaymentService(PaymentRepository repository){this.repository=repository;}
}
```


### 실행 검증 체크포인트

Spring 컨텍스트에서 PaymentService Bean과 의존 Bean이 주입되는지 확인한다.

### 스스로 작성할 변형 실습

생성자 주입과 필드 주입의 테스트 용이성을 비교한다.

### 면접 답변의 기준 문장

> IoC는 제어 역전의 원칙, DI는 의존 객체 제공 방식, Bean은 Spring이 관리하는 객체입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“IoC는 객체 생성·연결·생명주기 등의 제어가 애플리케이션 코드 바깥의 컨테이너로 이동하는 설계 원리다. DI는 객체의 의존성을 외부에서 제공하는 방식이고 Bean은 Spring 컨테이너가 관리하는 객체다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“IoC ≠ DI ≠ Bean. 모든 Java 객체가 Bean인 것은 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“IoC와 DI의 관계, Bean의 정확한 의미는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. Spring MVC·DispatcherServlet

**정의** — DispatcherServlet은 Spring MVC의 프런트 컨트롤러 서블릿으로 요청을 받아 적절한 핸들러에 전달하고 결과 처리를 조정한다.

**목적과 책임** — 공통 웹 요청 처리 파이프라인을 구성한다.

**구별해야 할 개념 / 흔한 오해** — DispatcherServlet이 비즈니스 로직을 직접 수행하는 것은 아니다.

**실행·처리 흐름** — HTTP → Filter → DispatcherServlet → HandlerMapping/Adapter → Controller → 응답

**직접 구현·검증** — 컨트롤러 매핑을 바꾸고 요청 흐름을 로그로 추적. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — DispatcherServlet과 Controller는 어떻게 다른가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

DispatcherServlet은 Spring MVC의 프런트 컨트롤러로 요청을 받아 HandlerMapping을 통해 핸들러를 찾고 HandlerAdapter를 통해 실행한다. 이후 반환값은 ViewResolver 또는 메시지 컨버터 등으로 처리된다. 모든 요청이 JSP를 거치는 것은 아니다.

### 직접 읽고 구현하는 독립 예제

```java
@RestController
@RequestMapping("/api")
class HealthController {
 @GetMapping("/health") Map<String,String> health(){return Map.of("status","UP");}
}
```


### 실행 검증 체크포인트

GET /api/health에서 JSON 응답을 확인하고 요청 흐름을 그린다.

### 스스로 작성할 변형 실습

@Controller와 @RestController의 응답 처리 차이를 설명한다.

### 면접 답변의 기준 문장

> DispatcherServlet은 Spring MVC의 요청 진입점으로 핸들러 탐색·호출·응답 처리를 조정합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“DispatcherServlet은 Spring MVC의 프런트 컨트롤러 서블릿으로 요청을 받아 적절한 핸들러에 전달하고 결과 처리를 조정한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“DispatcherServlet이 비즈니스 로직을 직접 수행하는 것은 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“DispatcherServlet과 Controller는 어떻게 다른가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. AOP·트랜잭션 프록시

**정의** — AOP는 여러 모듈에 걸친 관심사를 분리해 적용하는 방법이며 Spring의 선언적 트랜잭션은 보통 프록시를 통한 메서드 호출을 가로채 경계를 만든다.

**목적과 책임** — 반복적인 트랜잭션·로깅 등의 공통 처리를 분리한다.

**구별해야 할 개념 / 흔한 오해** — 같은 객체 내부의 자기 호출은 일반적인 프록시 기반 @Transactional 적용을 우회할 수 있다.

**실행·처리 흐름** — 프록시 진입 → 트랜잭션 시작 → 실제 메서드 → 커밋/롤백

**직접 구현·검증** — 서비스의 내부 호출과 외부 호출에서 트랜잭션 차이 설명. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — @Transactional이 붙으면 항상 적용되는가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## 스캔 범위 밖의 클래스는 빈이 되지 않는다 (2026-11-23 보강, 확인함)

**출처**: 선생님 자료 `springboot4☆/2장_스프링 부트 4 시작하기_자습서.html`의 `@Component`·`@Service` 실습과 스캔 범위 설명을 바탕으로, 스캔 범위를 재현해 확인했다.

**정의**: 스프링은 시작할 때 지정한 **패키지와 그 하위 패키지**에서만 `@Component` 계열을 찾는다. `@SpringBootApplication`이 붙은 클래스의 패키지가 기준이다. 그 밖에 있는 클래스는 `@Component`가 붙어 있어도 빈으로 등록되지 않는다.

```java
// com/example/demo/PaymentGateway.java, OrderService.java (스캔 범위 안)
// com/example/other/OutsideBean.java (스캔 범위 밖)

try (var ctx = new AnnotationConfigApplicationContext("com.example.demo")) {
    OrderService order = ctx.getBean(OrderService.class);
    System.out.println(order.order("coffee", 1000));   // 결제가 성공하는 경우
    System.out.println(order.order("coffee", 0));      // 결제가 실패하는 경우
    try {
        ctx.getBean(OutsideBean.class);
    } catch (NoSuchBeanDefinitionException e) {
        System.out.println("outside bean not registered: NoSuchBeanDefinitionException");
    }
}
```

필요한 jar: `spring-context`, `spring-core`, `spring-beans`, `spring-aop`, `spring-expression`, `commons-logging`(Spring 7 기준으로 확인했다).

**실행 결과 (확인함)**: 첫 두 줄은 `coffee 주문 완료`와 `coffee 결제 실패`(한글 출력은 콘솔 인코딩에 따라 깨질 수 있다). 세 번째 줄은 `outside bean not registered: NoSuchBeanDefinitionException`이다.

**읽는 법**
- `PaymentGateway`의 `pay`는 가격이 0보다 클 때만 `true`를 돌려준다. 그래서 두 번째 주문은 결제에 실패했다. 자료의 "결제 실패로 바꿔 보기" 연습과 같은 원리다.
- Spring Boot 프로젝트에서 `OutsideBean`을 `com.example.other`에 두면 `NoSuchBeanDefinitionException`(빈을 찾을 수 없다)이 난다. 해결은 클래스를 `@SpringBootApplication`의 하위 패키지로 옮기거나, `@ComponentScan`으로 범위를 명시하는 것이다.

**면접 기준**: "`@Component` 계열 클래스는 `@SpringBootApplication`이 있는 패키지와 하위 패키지에서만 빈이 됩니다. 범위 밖 클래스는 빈을 찾을 수 없다는 오류가 납니다."

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

AOP는 여러 컴포넌트에 걸친 공통 관심사를 모듈화하는 방식이다. Spring의 선언적 트랜잭션은 일반적으로 프록시 기반 AOP로 메서드 호출 전후 트랜잭션 경계를 처리한다. 기본 프록시 모드에서 같은 객체 내부의 자기 호출은 트랜잭션 조언을 우회할 수 있다.

### 직접 읽고 구현하는 독립 예제

```java
@Service
class TransferService {
 @Transactional
 public void transfer(long from,long to,long amount){
  debit(from,amount); credit(to,amount);
 }
}
```


### 실행 검증 체크포인트

두 번째 작업이 실패했을 때 첫 작업도 롤백되는지 통합 테스트한다. 메서드 예제는 저장소 연산 구현이 필요하다.

### 스스로 작성할 변형 실습

checked 예외 롤백 기본 규칙과 rollbackFor 옵션을 비교한다.

### 면접 답변의 기준 문장

> AOP는 횡단 관심사를 분리하고 Spring 트랜잭션은 프록시를 통해 메서드 경계를 관리할 수 있습니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: Bean, 객체, 의존성, 프록시

모든 Java 객체가 Spring Bean인 것은 아니다. Bean은 Spring IoC 컨테이너가 생성·등록·관리하는 객체다. DI는 필요한 의존 객체를 외부에서 제공하는 방식이고, `new`를 사용하면 무조건 잘못이라는 뜻은 아니다. 트랜잭션의 `@Transactional`은 일반적으로 Spring AOP 프록시를 통해 적용된다. 따라서 같은 객체 내부에서 `this.otherMethod()`를 호출하는 **자기 호출**은 프록시를 거치지 않아 기대한 트랜잭션 동작이 적용되지 않을 수 있다.

```java
@Service
class UserService {
  private final UserRepository repository;
  UserService(UserRepository repository) { this.repository = repository; }
  @Transactional(readOnly = true)
  public User find(long id) {
    return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
```

**전제:** `UserRepository`, `User`, 예외 타입과 Spring 의존성은 프로젝트에 정의되어 있어야 한다. `readOnly=true`는 쓰기를 물리적으로 반드시 차단하는 보안 장치가 아니다. DB·JPA 제공자에 따라 최적화 힌트와 플러시 정책 등에 영향을 준다.

**실습:** Bean을 생성자 주입한 경우와 직접 `new`한 경우를 비교하고, 어떤 객체에 프록시가 적용되는지 확인한다.


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. 객체 / Bean / Component

**흔한 오해:** “new로 만든 객체는 전부 Spring Bean이다”

**정확한 구분:** 객체는 Java 런타임 인스턴스다. Bean은 Spring IoC 컨테이너가 생성·등록·관리하는 객체다. @Component는 컴포넌트 스캔 등록 후보를 표시하는 어노테이션이며 모든 객체가 Bean은 아니다.

**코드·명령 해석:**

```java
@Component class MailSender {}
// new MailSender()는 그 자체로 컨테이너 등록을 뜻하지 않음
```

**확인 방법:** ApplicationContext.getBean으로 관리 객체를 조회하고 new 객체와 동일성 여부를 비교한다.

**직접 구현:** @Bean으로 등록한 객체와 @Component 객체의 등록 경로를 설명하라.

**면접 점검:** “객체 / Bean / Component”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. IoC / DI / Bean

**흔한 오해:** “IoC와 DI는 같은 단어다”

**정확한 구분:** IoC는 제어 흐름과 객체 구성의 주도권이 프레임워크 등 외부로 이동하는 설계 원리다. DI는 필요한 의존 객체를 외부에서 제공하는 구체적 방법 중 하나다. Bean은 컨테이너 관리 대상 객체다.

**코드·명령 해석:**

```java
class UserService {
  private final UserRepository repo;
  UserService(UserRepository repo){this.repo=repo;}
}
```

**확인 방법:** UserService가 구체 Repository를 new 하지 않는 이유를 설명한다.

**직접 구현:** 테스트에서 가짜 Repository를 생성자에 전달하라.

**면접 점검:** “IoC / DI / Bean”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. 싱글톤 / 스레드 안전

**흔한 오해:** “Spring Bean 기본 싱글톤이면 자동으로 스레드 안전하다”

**정확한 구분:** 싱글톤 스코프는 컨테이너당 하나의 Bean 인스턴스를 공유한다는 뜻이지 가변 필드의 동시 접근 안전을 보장하지 않는다. 요청별 상태는 지역 변수 등으로 다루어야 한다.

**코드·명령 해석:**

```java
@Service class Counter { private int count; void inc(){count++;} }
// 동시 호출 시 경쟁 조건 위험
```

**확인 방법:** 동시 호출에서 ++ 연산의 원자성을 검토한다.

**직접 구현:** 상태 없는 Service로 바꾸고 필요한 상태는 DB에 저장하라.

**면접 점검:** “싱글톤 / 스레드 안전”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“AOP는 여러 모듈에 걸친 관심사를 분리해 적용하는 방법이며 Spring의 선언적 트랜잭션은 보통 프록시를 통한 메서드 호출을 가로채 경계를 만든다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“같은 객체 내부의 자기 호출은 일반적인 프록시 기반 @Transactional 적용을 우회할 수 있다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“@Transactional이 붙으면 항상 적용되는가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

