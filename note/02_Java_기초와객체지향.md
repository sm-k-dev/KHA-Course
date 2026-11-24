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

## 파트 안내

- [02-1 객체지향 기초](02-1_객체지향_기초.md): JDK·JVM, 클래스·객체·참조, 캡슐화, 상속·인터페이스·다형성
- [02-2 컬렉션·예외·제네릭](02-2_컬렉션_예외_제네릭.md): 예외, 제네릭, Collections 프레임워크
- [02-3 메모리·I/O·enum](02-3_메모리_IO_enum.md): 메모리 영역과 GC, 입출력과 default 메서드, enum·Iterator·String

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


