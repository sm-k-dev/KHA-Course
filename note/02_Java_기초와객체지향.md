# Java · 객체지향

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `05_Java`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — 참조 변수 · 클래스 · 객체 · 업캐스팅

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

`List<String> names = new ArrayList<>();`에서 `List<String>`은 변수의 **선언 타입**, `names`는 **참조 변수**, `ArrayList`는 **구현 클래스**, `new ArrayList<>()`는 **객체 생성 표현식**이다. 실행 결과 만들어지는 객체의 실제 클래스는 `ArrayList`다.

### 2. 용어를 구분해야 하는 이유

`List`가 객체로 생성되었다는 말은 틀리다. `List`는 인터페이스이며, `ArrayList` 객체의 참조를 `List` 타입 변수에 대입하는 것이 업캐스팅이다. `List`는 순서가 있으며 중복을 허용한다.

### 3. 새로운 상황에 적용

`List<String> b = names; names = new LinkedList<>();` 뒤에는 `b`가 원래 `ArrayList`를 참조한다. 변수의 재대입은 기존 객체를 비우지 않는다.

### 4. 면접 질문과 답변 기준

**질문:** 참조 변수 · 클래스 · 객체 · 업캐스팅의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** 참조 변수의 선언 타입은 컴파일 시 사용할 수 있는 멤버를 결정하고, 실제 객체의 클래스는 오버라이딩된 인스턴스 메서드의 실행 구현을 결정합니다. `List`는 인터페이스이므로 직접 생성하지 않고 구현 클래스의 객체를 참조합니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. JDK·JRE·JVM

**정의** — JVM은 바이트코드를 실행하는 가상 머신이고, JRE는 실행 환경, JDK는 컴파일러 등 개발 도구를 포함하는 개발 키트다.

**목적과 책임** — JVM은 실행과 메모리 관리, javac는 소스 컴파일을 담당한다.

**구별해야 할 개념 / 흔한 오해** — Java 언어와 JVM은 같지 않다. Java 소스가 직접 CPU 명령으로 실행되는 것도 아니다.

**실행·처리 흐름** — 소스(.java) → javac → 바이트코드(.class) → 클래스 로딩·검증 → JVM 실행

**직접 구현·검증** — javac와 java로 Hello.java를 컴파일·실행하고 클래스 파일 생성 위치를 확인. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — JDK와 JVM의 차이와 Java 프로그램의 실행 과정을 설명하라.

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

JDK는 개발·빌드 도구 묶음, JVM은 바이트코드 실행 엔진, JRE는 실행에 필요한 구성의 개념이다. Java 9 이후에는 독립 JRE 배포가 항상 제공되는 것은 아니다. JVM은 클래스 로딩·검증, 실행, 가비지 컬렉션 등을 담당한다. `javac`와 `java`는 역할이 다르다.

### 직접 읽고 구현하는 독립 예제

```java
public class Hello {
  public static void main(String[] args) { System.out.println("hello"); }
}
```
```bash
javac Hello.java
javap -c Hello
java Hello
```


### 실행 검증 체크포인트

`Hello.class` 생성, `javap` 바이트코드, `hello` 출력 확인. `java Hello.java` 소스 파일 실행 기능과 일반 컴파일 흐름을 혼동하지 않는다.

### 스스로 작성할 변형 실습

클래스패스가 잘못되었을 때 발생하는 오류를 재현하고 원인을 설명한다.

### 면접 답변의 기준 문장

> JVM은 바이트코드를 실행하는 가상 머신이고 JDK는 컴파일러 등 개발 도구를 포함합니다. 소스는 보통 바이트코드로 컴파일된 뒤 JVM에서 실행됩니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“JVM은 바이트코드를 실행하는 가상 머신이고, JRE는 실행 환경, JDK는 컴파일러 등 개발 도구를 포함하는 개발 키트다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“Java 언어와 JVM은 같지 않다. Java 소스가 직접 CPU 명령으로 실행되는 것도 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“JDK와 JVM의 차이와 Java 프로그램의 실행 과정을 설명하라.”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. 클래스·객체·참조

**정의** — 클래스는 객체의 상태와 동작을 정의하는 타입이고 객체는 실행 중 생성된 인스턴스다. 참조 변수는 객체 자체가 아니라 객체를 참조하는 값을 가진다.

**목적과 책임** — 클래스는 구조를 정의하고 인스턴스는 개별 상태를 보유한다.

**구별해야 할 개념 / 흔한 오해** — 클래스 ≠ 객체, 참조 복사 ≠ 객체 복제. ==는 참조형에서 동일 객체 참조를 비교한다.

**실행·처리 흐름** — new → 객체 생성 → 생성자 초기화 → 참조 저장 → 메서드 호출

**직접 구현·검증** — 서로 다른 두 객체와 같은 객체를 가리키는 두 변수를 만들어 == 결과를 예측·검증. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 클래스와 객체, 참조 변수의 차이를 설명하라.

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

클래스는 객체를 생성하는 데 사용되는 타입 정의이고 객체는 런타임의 인스턴스다. 참조 변수에 객체의 상태가 통째로 저장되는 것은 아니다. `==`는 참조값의 동일성을 비교하며 `equals`는 클래스가 정의한 동등성 규약에 따라 비교한다.

### 직접 읽고 구현하는 독립 예제

```java
class Box { int value; Box(int v) { value = v; } }
Box a = new Box(7);
Box b = a;
Box c = new Box(7);
System.out.println(a == b); // true
System.out.println(a == c); // false
b.value = 9;
System.out.println(a.value); // 9
```


### 실행 검증 체크포인트

참조 복사와 객체 복제의 차이를 값 변경으로 확인한다. 위 문장은 `main` 메서드 안에 넣는다.

### 스스로 작성할 변형 실습

Box에 값 기반 `equals`를 구현하고 `==`와 비교한다.

### 면접 답변의 기준 문장

> 클래스는 타입을 정의하고 객체는 생성된 인스턴스입니다. 참조 변수는 객체를 가리키며 같은 객체를 참조하면 변경 결과를 공유합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“클래스는 객체의 상태와 동작을 정의하는 타입이고 객체는 실행 중 생성된 인스턴스다. 참조 변수는 객체 자체가 아니라 객체를 참조하는 값을 가진다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“클래스 ≠ 객체, 참조 복사 ≠ 객체 복제. ==는 참조형에서 동일 객체 참조를 비교한다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“클래스와 객체, 참조 변수의 차이를 설명하라.”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. 캡슐화·접근 제어

**정의** — 캡슐화는 상태와 관련 동작을 묶고, 외부에서 허용한 경로로만 상태를 변경하게 만드는 설계 원칙이다.

**목적과 책임** — 객체가 자신의 불변 조건을 지키도록 책임을 집중한다.

**구별해야 할 개념 / 흔한 오해** — private 필드와 getter/setter를 모두 만드는 것이 캡슐화의 완성은 아니다.

**실행·처리 흐름** — 외부 요청 → 공개 메서드 → 유효성 검사 → 내부 상태 변경

**직접 구현·검증** — 잔액이 음수가 되지 않도록 Account.withdraw를 구현하고 예외를 테스트. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 정보 은닉과 캡슐화는 어떻게 연결되는가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

캡슐화는 상태와 동작을 묶고 유효한 상태 변경 통로를 제공하는 설계 원칙이다. 정보 은닉은 내부 구현 세부사항의 노출을 제한하는 수단이다. `private` 필드와 무조건적인 setter는 불변 조건을 보장하지 않는다.

### 직접 읽고 구현하는 독립 예제

```java
class Account {
  private int balance;
  Account(int initial) { if(initial < 0) throw new IllegalArgumentException(); balance=initial; }
  void withdraw(int amount) {
    if(amount <= 0 || amount > balance) throw new IllegalArgumentException();
    balance -= amount;
  }
  int balance() { return balance; }
}
```


### 실행 검증 체크포인트

초기 잔액 음수, 0원 출금, 잔액 초과 출금이 거부되는지 확인한다.

### 스스로 작성할 변형 실습

동시에 여러 스레드가 출금하면 불변 조건만으로 충분한지 조사한다.

### 면접 답변의 기준 문장

> 캡슐화는 객체가 자신의 상태와 관련 동작을 관리하게 하여 외부가 유효하지 않은 상태를 만들지 못하게 하는 설계입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“캡슐화는 상태와 관련 동작을 묶고, 외부에서 허용한 경로로만 상태를 변경하게 만드는 설계 원칙이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“private 필드와 getter/setter를 모두 만드는 것이 캡슐화의 완성은 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“정보 은닉과 캡슐화는 어떻게 연결되는가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 4. 상속·인터페이스·다형성

**정의** — 상속은 기존 타입을 확장하는 관계이며 인터페이스는 구현체가 따라야 할 계약을 표현한다. 다형성은 동일한 타입의 참조로 서로 다른 구현 동작을 호출할 수 있는 성질이다.

**목적과 책임** — 호출부가 구체 구현에 덜 의존하게 만들고 변경 가능성을 높인다.

**구별해야 할 개념 / 흔한 오해** — 오버로딩은 매개변수 목록에 따른 메서드 구분, 오버라이딩은 상위 타입의 인스턴스 메서드 재정의다.

**실행·처리 흐름** — 인터페이스 참조 → 구현 객체 주입 → 런타임 실제 구현 메서드 호출

**직접 구현·검증** — NotificationSender 인터페이스와 ConsoleSender·EmailSender(모의 전송)를 구현. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 인터페이스와 추상 클래스, 오버로딩과 오버라이딩의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

상속은 하위 타입 관계를 만들며, 인터페이스는 구현체가 제공해야 할 계약을 표현한다. 다형성은 상위 타입 참조를 통해 실제 객체에 맞는 구현을 호출하는 성질이다. 상속을 단순 코드 재사용 수단으로 남용하면 결합이 커진다. 오버로딩은 컴파일 시그니처 선택, 오버라이딩은 런타임 동적 디스패치와 관련된다.

### 직접 읽고 구현하는 독립 예제

```java
interface Sender { void send(String message); }
class ConsoleSender implements Sender {
  public void send(String message) { System.out.println(message); }
}
class Notification {
  private final Sender sender;
  Notification(Sender sender) { this.sender=sender; }
  void notifyUser() { sender.send("완료"); }
}
// new Notification(new ConsoleSender()).notifyUser();
```


### 실행 검증 체크포인트

`Sender` 구현을 교체해도 `Notification` 코드를 수정하지 않는지 확인한다.

### 스스로 작성할 변형 실습

실패를 기록하는 FakeSender를 만들어 호출 횟수를 검증한다.

### 면접 답변의 기준 문장

> 다형성은 동일한 상위 타입을 통해 다양한 구현 객체를 사용할 수 있는 성질이며, 인터페이스는 그 계약을 정의합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“상속은 기존 타입을 확장하는 관계이며 인터페이스는 구현체가 따라야 할 계약을 표현한다. 다형성은 동일한 타입의 참조로 서로 다른 구현 동작을 호출할 수 있는 성질이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“오버로딩은 매개변수 목록에 따른 메서드 구분, 오버라이딩은 상위 타입의 인스턴스 메서드 재정의다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“인터페이스와 추상 클래스, 오버로딩과 오버라이딩의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 5. 예외·컬렉션·제네릭

**정의** — 예외는 정상 흐름을 방해하는 상황을 표현하는 객체이며, 컬렉션은 다수의 원소를 관리하는 자료구조 API, 제네릭은 타입 매개변수로 컴파일 시 타입 안정성을 높이는 기능이다.

**목적과 책임** — 예외는 실패를 전달하고, 컬렉션은 데이터 저장·탐색을 담당한다.

**구별해야 할 개념 / 흔한 오해** — List는 순서와 중복을 허용하고 Set은 중복을 허용하지 않으며 Map은 키-값 대응을 관리한다.

**실행·처리 흐름** — 입력 검증 → 컬렉션 처리 → 실패 시 적절한 예외 전달 → 호출자가 처리

**직접 구현·검증** — 중복 이메일을 Set으로 검사하고 잘못된 입력에 IllegalArgumentException을 발생. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — checked/unchecked 예외를 어떻게 구분하며 언제 사용하는가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

> 버전 참고: 이 장의 문법은 Java 8부터 있다. `var`, `record`, 텍스트 블록 같은 새 문법의 도입 버전과 8 호환 확인은 [98장 Java 버전 차이](98_Java_버전_차이.md)에서 다룬다.

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

예외는 실패를 호출 경계 너머로 전달하는 메커니즘이고, 컬렉션은 자료의 저장·탐색 계약을 제공한다. 제네릭은 타입 매개변수를 통해 컴파일 시 타입 안전성을 높인다. `List`는 순서 있는 시퀀스, `Set`은 중복 원소를 허용하지 않는 집합, `Map`은 키-값 매핑이다. checked 예외는 `Exception` 계열 중 `RuntimeException`을 제외한 컴파일 시 처리 의무가 있는 예외다.

### 직접 읽고 구현하는 독립 예제

```java
Set<String> emails = new HashSet<>();
void register(String email) {
  if(email == null || email.isBlank()) throw new IllegalArgumentException("email");
  if(!emails.add(email)) throw new IllegalStateException("duplicate");
}
```


### 실행 검증 체크포인트

동일 이메일 두 번 등록 시 두 번째 호출에서 예외가 발생한다. 코드의 `emails`와 메서드는 동일 클래스에 둔다.

### 스스로 작성할 변형 실습

HashSet과 ArrayList에서 중복 처리와 탐색 비용을 비교한다.

### 면접 답변의 기준 문장

> 제네릭은 타입 안정성을 높이고 컬렉션은 자료구조의 계약을 제공합니다. 예외는 정상 흐름 밖의 실패를 표현합니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: 값, 참조, null, Optional, 컬렉션

### 1. 값과 객체의 부재는 다르다

`int count = 0`은 **숫자 0이 존재한다**는 뜻이다. `User user = null`은 `user` 변수가 현재 객체를 참조하지 않는다는 뜻이다. `List<User> users = List.of()`는 **리스트 객체는 존재하지만 원소가 0개**라는 뜻이다. `Optional<User> user = Optional.empty()`는 Optional 객체가 **User 값의 부재**를 표현한다는 뜻이다. 이 네 상태를 같은 것으로 취급하지 않는다.

```java
record User(long id, String name) {}
int count = 0;
User absent = null;
List<User> noUsers = List.of();
Optional<User> maybeUser = Optional.empty();
System.out.println(count);                 // 0
System.out.println(absent == null);        // true
System.out.println(noUsers.size());        // 0
System.out.println(maybeUser.isPresent()); // false
```

**실행 전제:** `import java.util.*;`를 추가하고 `record` 선언은 클래스 내부 또는 최상위에, 나머지는 `main` 내부에 배치한다. Java 17 이상을 사용한다.

### 2. Optional은 무엇이고, 무엇이 아닌가

`Optional<T>`는 값 `T`가 있을 수도 없을 수도 있음을 표현하는 **Java 라이브러리의 컨테이너 타입**이다. SQL 타입도, DB가 전송하는 데이터 타입도 아니다. `Optional.of(value)`는 null을 거부하고, `ofNullable(value)`는 null이면 empty를 만들며, `empty()`는 부재를 나타낸다. `get()`은 비어 있으면 `NoSuchElementException`을 던지므로 `orElseThrow`, `map`, `ifPresent`를 목적에 맞게 사용한다.

```java
Optional<User> found = Optional.of(new User(1, "Kim"));
String name = found.map(User::name).orElse("이름 없음");
User required = found.orElseThrow(() -> new IllegalStateException("사용자 없음"));
Optional<User> missing = Optional.empty();
System.out.println(missing.orElse(null)); // null: 호출자가 선택한 변환 결과
```

`Optional.empty()`와 `null`은 동일한 객체가 아니다. `Optional` 자체를 null로 반환하면 API 계약의 취지를 해친다. 컬렉션을 반환하는 메서드는 대체로 `Optional<List<T>>`보다 빈 컬렉션을 반환하는 편이 단순하다. 엔티티 필드나 직렬화 DTO에 Optional을 무조건 쓰는 것도 권장되지 않는다.

### 3. 타입을 정확히 읽는 연습

- `User`: 사용자 객체를 참조할 것으로 기대하는 타입. 참조가 null일 가능성은 Java 타입만으로 표현되지 않는다.
- `Optional<User>`: 사용자 **한 명의 존재 여부**를 표현하는 API 계약.
- `List<User>`: 사용자 **0명 이상**을 표현한다. 0명이어도 리스트 객체는 존재한다.
- `long`: 원시 정수형. `null`을 담을 수 없다. `Long`은 참조형이라 null이 가능하다.

**변형 실습:** `findName(long id)`를 `Optional<String>` 반환으로 작성하고, 존재하는 ID/없는 ID 각각에 `map`, `orElseThrow`를 적용하라. 실패 상황을 테스트한 뒤 왜 `0`을 반환하면 안 되는지 설명하라.

**면접 질문:** “값이 없는 것과 값이 0인 것의 차이는?” “`Optional.empty()`와 `null`의 차이는?” “왜 목록 조회에는 빈 리스트를 반환하는가?”


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. List / ArrayList / 인스턴스 / 참조 변수

**흔한 오해:** “List는 인스턴스이고 ArrayList는 구현 객체다”

**정확한 구분:** List는 인터페이스, ArrayList는 이를 구현한 클래스다. new ArrayList<>()가 만든 것은 ArrayList 인스턴스이고 names는 그 객체를 가리키는 참조 변수다. 선언 타입 List와 런타임 클래스 ArrayList는 다를 수 있다.

**코드·명령 해석:**

```java
List<String> names = new ArrayList<>();
names.add("Kim");
System.out.println(names.get(0)); // Kim
```

**확인 방법:** List는 new List<>()로 직접 생성할 수 없다. ArrayList는 List 계약을 구현하므로 상위 타입 참조에 대입 가능하다.

**직접 구현:** ArrayList 대신 LinkedList로 바꾸고 공통 List 메서드만 쓰는 코드를 작성하라.

**면접 점검:** “List / ArrayList / 인스턴스 / 참조 변수”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. 객체 / 클래스 / 인스턴스 / 생성자

**흔한 오해:** “생성자는 객체다”

**정확한 구분:** 클래스는 객체의 구조와 동작을 정의하고 생성자는 객체 초기화에 참여하는 특별한 구성 요소다. 인스턴스는 특정 클래스로 생성된 객체를 강조하는 말이다. 참조 변수와 객체는 동일하지 않다.

**코드·명령 해석:**

```java
class User { String name; User(String name){ this.name=name; } }
User a = new User("A");
User b = a;
b.name = "B";
System.out.println(a.name); // B
```

**확인 방법:** a와 b는 서로 다른 참조 변수지만 같은 객체를 가리킨다. 객체 복사와 참조 복사는 다르다.

**직접 구현:** new User("C")를 하나 더 만들고 == 비교를 확인하라.

**면접 점검:** “객체 / 클래스 / 인스턴스 / 생성자”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. 오버로딩 / 오버라이딩 / 다형성

**흔한 오해:** “메서드 이름이 같으면 오버라이딩이다”

**정확한 구분:** 오버로딩은 같은 이름의 메서드를 매개변수 목록이 다르게 정의하는 것, 오버라이딩은 하위 클래스가 상위 타입의 인스턴스 메서드 구현을 재정의하는 것이다. 다형적 호출은 실제 객체의 재정의 메서드를 선택한다.

**코드·명령 해석:**

```java
class Animal { String sound(){return "?";} }
class Dog extends Animal { @Override String sound(){return "멍";} }
Animal animal = new Dog();
System.out.println(animal.sound()); // 멍
```

**확인 방법:** 변수 타입 Animal과 실제 객체 Dog를 분리해 설명한다.

**직접 구현:** Animal에 sound(int count)를 추가해 오버로딩과 오버라이딩을 함께 실험하라.

**면접 점검:** “오버로딩 / 오버라이딩 / 다형성”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 4. == / equals / hashCode

**흔한 오해:** “객체 내용 비교는 언제나 ==이다”

**정확한 구분:** 참조형 ==는 같은 객체를 참조하는지 비교한다. equals는 논리적 동등성을 표현하도록 재정의할 수 있다. equals가 같다고 판단하는 객체는 hashCode도 같아야 한다.

**코드·명령 해석:**

```java
String a = new String("hi");
String b = new String("hi");
System.out.println(a == b); // false
System.out.println(a.equals(b)); // true
```

**확인 방법:** HashSet에서 동등성 규약을 지키지 않는 사용자 정의 객체의 중복 처리 오류를 재현하라.

**직접 구현:** record UserKey(long id) {}를 HashSet에 두 번 추가해 결과를 확인하라.

**면접 점검:** “== / equals / hashCode”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“예외는 정상 흐름을 방해하는 상황을 표현하는 객체이며, 컬렉션은 다수의 원소를 관리하는 자료구조 API, 제네릭은 타입 매개변수로 컴파일 시 타입 안정성을 높이는 기능이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“List는 순서와 중복을 허용하고 Set은 중복을 허용하지 않으며 Map은 키-값 대응을 관리한다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“checked/unchecked 예외를 어떻게 구분하며 언제 사용하는가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.


## 6. 메모리 영역 (Stack·Heap)과 GC 개념

- **메서드 영역:** 클래스 정보와 static 변수.
- **스택:** 메서드를 호출할 때마다 프레임이 쌓이고(LIFO), 지역 변수와 참조 변수 자체가 여기 있다. 메서드가 끝나면 프레임이 사라진다.
- **힙:** `new`로 만든 객체와 배열이 있다. 어떤 참조도 가리키지 않는 객체는 가비지 컬렉터(GC)가 회수한다.
- **확인:** `Box b = new Box(7);`에서 `b`는 스택에, `Box` 객체는 힙에 있다. 메서드가 끝나 `b`가 사라져도, 다른 참조가 남아 있는 객체는 힙에 그대로 남는다.
- **면접 기준:** "참조 변수는 스택, 객체는 힙에 생성되고, 참조가 없어진 객체는 GC가 회수합니다."

### 6-1. 메모리 문제를 직접 재현하기 (2026-11-23 보강, JDK 25에서 확인함)

> 아래 코드는 힙 최대 크기를 64MB로 제한해 실행했다(`-Xmx64m`). `held` 값은 실행마다 조금 달라질 수 있다.

```java
import java.util.ArrayList;
import java.util.List;

public class MemoryDemo {
    static class Box {
        final int v;
        Box(int v) { this.v = v; }
    }

    static int depth = 0;
    static void recurse() { depth++; recurse(); }   // 스택 프레임이 계속 쌓인다

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        System.out.println("max heap(MB)=" + rt.maxMemory() / (1024 * 1024));

        // 1) 힙: 참조가 남아 있는 객체는 GC가 회수하지 않는다
        Box keep = new Box(1);
        for (int i = 0; i < 3_000_000; i++) {
            new Box(i);                       // 참조를 버린 객체 → GC 대상
        }
        System.out.println("keep.v=" + keep.v + " (alive)");

        // 2) 스택: 재귀가 너무 깊으면 StackOverflowError
        try {
            recurse();
        } catch (StackOverflowError e) {
            System.out.println("StackOverflowError, depth>1000=" + (depth > 1000));
        }

        // 3) 힙 부족: 참조를 계속 쥐고 있으면 OutOfMemoryError
        List<byte[]> hold = new ArrayList<>();
        try {
            while (true) hold.add(new byte[1024 * 1024]);
        } catch (OutOfMemoryError e) {
            int held = hold.size();
            hold.clear();                     // 참조를 놓아야 아래 출력 문자열을 만들 메모리가 생긴다
            System.out.println("OutOfMemoryError, held(MB)=" + held);
        }
    }
}
```

```powershell
javac -d out MemoryDemo.java
java -Xmx64m -cp out MemoryDemo
```

**실행 결과 (확인함)**

```
max heap(MB)=64
keep.v=1 (alive)
StackOverflowError, depth>1000=true
OutOfMemoryError, held(MB)=31
```

**GC 로그 보기 (확인함)**

```powershell
java -Xmx64m -Xlog:gc -cp out MemoryDemo
```

실행하면 `Pause Young`와 `Pause Full` 같은 줄이 나온다. 예: `Pause Full (G1 Compaction Pause) 63M->1M(8M)`은 힙이 63MB에서 1MB로 줄었다는 뜻이다. 실행 한 번에 13번 정도 GC가 일어났다.

**세 가지 문제를 구별하기**

| 상황 | 원인 | 나타나는 신호 |
|---|---|---|
| 스택 오버플로 | 메서드 호출이 끝나지 않고 재귀로 쌓임 | `StackOverflowError` |
| 힙 부족 | 객체를 참조한 채로 계속 쌓아 둠 | `OutOfMemoryError: Java heap space` |
| GC 과다 | 짧은 시간에 객체를 많이 만듦 | 응답 지연, GC 로그가 자주 나옴 |

**핵심**: GC는 "참조가 없는 객체"만 회수한다. 그래서 Java에서도 참조를 오래 쥐고 있으면 메모리 누수가 생긴다. 캐시 맵에 항목을 계속 넣기만 하고 지우지 않는 것이 대표적인 예다.

**면접 기준**: "힙에 객체가 쌓이고 참조가 없어진 객체를 GC가 회수합니다. 힙이 부족하면 OutOfMemoryError가 나고, 재귀가 깊으면 StackOverflowError가 납니다. 참조를 계속 쥐면 GC가 회수하지 못해 누수가 생깁니다."

## 6-2. 입출력(I/O)과 인터페이스 default 메서드 (2026-11-23 보강, JDK 25에서 확인함)

### 파일 읽기·쓰기와 try-with-resources

파일, 네트워크, DB 연결처럼 닫아야 하는 자원은 `try (...)` 괄호 안에 넣으면 블록이 끝날 때 자동으로 닫힌다. 예외가 나도 닫힌다.

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IoDemo {
    public static void main(String[] args) throws Exception {
        Path file = Files.createTempFile("khac", ".csv");
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write("id,name"); w.newLine();
            w.write("1,kim"); w.newLine();
            w.write("2,lee"); w.newLine();
        }
        try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int count = 0;
            while ((line = r.readLine()) != null) {
                if (count++ == 0) continue;          // 헤더 건너뛰기
                String[] cols = line.split(",");
                System.out.println(cols[0] + " -> " + cols[1]);
            }
        }
        List<String> all = Files.readAllLines(file, StandardCharsets.UTF_8);
        System.out.println("lines=" + all.size());
        try (InputStream in = Files.newInputStream(file)) {
            System.out.println("bytes=" + in.readAllBytes().length);
        }
        Files.delete(file);
        System.out.println("deleted=" + !Files.exists(file));
    }
}
```

**실행 결과 (확인함)**

```
1 -> kim
2 -> lee
lines=3
bytes=23
deleted=true
```

**읽는 법**
- `bytes=23`은 줄바꿈이 Windows에서 `\r\n`(2바이트)이라서 나온 값이다. Linux나 macOS에서는 `\n` 하나라서 20이 된다. 바이트 수는 OS에 따라 달라진다.
- `readAllLines`는 파일 전체를 메모리에 올린다. 큰 파일에는 `BufferedReader`로 한 줄씩 읽는다.
- 문자열은 `StandardCharsets.UTF_8`을 명시한다. 기본 인코딩에 기대면 OS마다 한글이 깨질 수 있다(4장의 인코딩 설명).

**면접 기준**: "파일과 스트림은 `try-with-resources`로 닫고, 큰 파일은 한 줄씩 읽고, 문자 인코딩은 명시합니다."

### 인터페이스의 default 메서드와 static 메서드

Java 8부터 인터페이스에 구현이 있는 메서드를 둘 수 있다. 기존 인터페이스에 메서드를 추가해도 구현체가 깨지지 않게 하려고 도입됐다.

```java
public class DefaultDemo {
    interface Notifier {
        void send(String msg);                                   // 구현해야 하는 추상 메서드
        default void sendAll(String... msgs) {                   // 기본 구현: 구현체가 바꾸지 않아도 된다
            for (String m : msgs) send(m);
        }
        static Notifier console() {                              // 인터페이스의 static 메서드
            return m -> System.out.println("[console] " + m);
        }
    }

    public static void main(String[] args) {
        Notifier n = Notifier.console();
        n.sendAll("a", "b");
        Notifier silent = m -> { };                              // send 하나뿐이라 람다로 만들 수 있다
        silent.sendAll("x");
        System.out.println("done");
    }
}
```

**실행 결과 (확인함)**

```
[console] a
[console] b
done
```

**읽는 법**: `send`만 구현하면 `sendAll`은 그대로 쓸 수 있다. 추상 메서드가 하나뿐인 인터페이스는 람다로 만들 수 있다(함수형 인터페이스). 같은 시그니처의 default 메서드를 가진 인터페이스를 두 개 구현하면, 구현 클래스가 직접 재정의해서 충돌을 해결해야 한다.

**면접 기준**: "default 메서드는 인터페이스에 기본 구현을 주는 기능이고, 기존 구현체를 깨지 않고 인터페이스를 확장하려고 도입됐습니다."

## 6-3. enum, Iterator, String과 StringBuilder (2026-11-23 보강, JDK 25에서 확인함)

### enum: 정해진 상수 묶음

`enum`은 고정된 값 목록을 타입으로 만든다. 상수마다 값과 메서드를 가질 수 있다.

```java
import java.util.*;

public class EnumIter {
    enum OrderStatus {
        PAID(1000), SHIPPED(0), CANCELED(0);

        private final int fee;
        OrderStatus(int fee) { this.fee = fee; }
        int fee() { return fee; }
        boolean isFinal() { return this == SHIPPED || this == CANCELED; }
    }

    public static void main(String[] args) {
        OrderStatus s = OrderStatus.valueOf("PAID");
        System.out.println(s + " " + s.fee() + " " + s.ordinal() + " " + s.isFinal());
        for (OrderStatus o : OrderStatus.values()) System.out.print(o.name() + " ");
        System.out.println();
        switch (s) {
            case PAID -> System.out.println("paid-case");
            default -> System.out.println("other");
        }
        System.out.println(OrderStatus.valueOf("SHIPPED").compareTo(OrderStatus.PAID));

        List<String> names = new ArrayList<>(List.of("kim", "lee", "park"));
        try {
            for (String n : names) {
                if (n.equals("kim")) names.remove(n);   // 첫 원소 제거 → CME
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException");
        }
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            if (it.next().equals("lee")) it.remove();   // iterator로 안전하게 제거
        }
        System.out.println(names);

        String a = "ab";
        String b = a.concat("c");
        System.out.println(a + " " + b);
        StringBuilder sb = new StringBuilder("ab");
        sb.append("c");
        System.out.println(sb + " " + sb.length());
    }
}
```

**실행 결과 (확인함)**

```
PAID 1000 0 false
PAID SHIPPED CANCELED 
paid-case
1
ConcurrentModificationException
[park]
ab abc
abc 3
```

**읽는 법**
- `ordinal()`은 선언 순서(0부터)다. 상수 순서를 바꾸면 값이 바뀌므로 **DB에는 `name()`을 저장하고 `ordinal()`은 저장하지 않는다.**
- 확장 for문으로 순회하는 도중 `list.remove()`를 호출하면 다음 `next()`에서 `ConcurrentModificationException`이 난다. 순회 중 제거는 `Iterator.remove()`나 `removeIf`를 쓴다.
- `String`은 불변이다. `concat`은 새 문자열을 돌려주고 원본은 그대로다. 반복문에서 `+=`로 문자열을 계속 이어 붙이면 객체가 많이 생기므로 `StringBuilder`를 쓴다.

**면접 기준**: "enum은 정해진 상수 집합을 타입으로 만든 것이고, DB에는 순서 값이 아니라 이름을 저장합니다. 순회 중 제거는 Iterator로 하고, 문자열을 반복해서 이어 붙일 때는 StringBuilder를 씁니다."

## 7. Collections 프레임워크 한눈에 보기

- **Collection (인터페이스):** 여러 원소를 담는 큰 틀. 아래 둘로 나뉜다.
  - **List:** 순서가 있고 중복 허용. 구현체는 `ArrayList`(인덱스 조회 빠름), `LinkedList`(앞뒤 삽입 빠름).
  - **Set:** 중복 없음. 구현체는 `HashSet`(순서 없음), `TreeSet`(정렬됨).
- **Map (별도 인터페이스):** 키와 값의 쌍. 같은 키를 다시 넣으면 값이 덮어써진다. 구현체는 `HashMap`, `TreeMap`.
- **Collections (유틸리티 클래스):** 이름이 비슷하지만 다르다. `Collections.sort`, `Collections.unmodifiableList`처럼 컬렉션을 다루는 메서드 모음이다.
- **검증 완료:** `List`에 `1, 2, 2`를 넣으면 size 3, `Set`에 같은 값을 넣으면 size 2, `Map`에서 같은 키에 2를 넣으면 값이 2로 바뀐다.
- **면접 기준:** "List는 순서와 중복을 허용하고, Set은 중복을 허용하지 않으며, Map은 키로 값을 찾습니다. Collection은 인터페이스, Collections는 유틸리티 클래스입니다."
