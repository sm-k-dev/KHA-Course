# 람다, 스트림, Optional: 컬렉션을 선언형으로 다루기

> 중요도: **A** · 이 장의 목표: 함수형 인터페이스와 람다를 읽을 수 있고, 스트림의 중간 연산과 최종 연산을 구분하며, `Optional`로 null을 명시적으로 다룬다.

> 2026-11-23 추가 장 (로드맵 2순위 "람다·스트림·Optional 심화" 항목). 아래 Java 예제는 JDK 25에서 컴파일해 실행한 결과다. 항목 번호와 결과는 `Batch6.java` 기준으로 확인했다.

---

## 1. 람다 (Lambda)

**정의** — 메서드 하나를 가진 인터페이스(함수형 인터페이스)의 구현을 짧게 쓰는 문법이다.

```java
// 익명 클래스
Comparator<String> byLength1 = new Comparator<String>() {
    @Override
    public int compare(String a, String b) { return a.length() - b.length(); }
};

// 람다: 같은 동작을 한 줄로
Comparator<String> byLength2 = (a, b) -> a.length() - b.length();
```

**자주 쓰는 함수형 인터페이스**

| 인터페이스 | 시그니처 | 쓰임 |
|---|---|---|
| `Function<T, R>` | `T → R` | 변환 |
| `Predicate<T>` | `T → boolean` | 필터 조건 |
| `Consumer<T>` | `T → void` | 처리만 하는 작업 |
| `Supplier<T>` | `() → T` | 지연 생성 |

**주의** — 람다가 바깥 변수를 쓰면 그 변수는 **사실상 final**이어야 한다. 값을 바꾸려면 배열이나 객체로 감싸야 한다.

---

## 2. 스트림 (Stream)

**정의** — 컬렉션의 원소를 **선언형으로 변환하는 파이프라인**이다. 데이터를 저장하지 않고 연산만 연결한다.

```
원본 → 중간 연산(filter, map, sorted …) → 최종 연산(collect, count, sum …)
```

**핵심 규칙**

1. 중간 연산은 **지연 실행**된다. 최종 연산이 호출되기 전에는 아무 일도 하지 않는다.
2. 스트림은 **한 번만** 쓸 수 있다. 같은 스트림에 최종 연산을 두 번 하면 `IllegalStateException`이 난다.
3. 원본 컬렉션은 바뀌지 않는다.

```java
List<String> names = List.of("kim", "lee", "park", "choi");
List<String> result = names.stream()
        .filter(n -> n.length() > 3)     // 중간: 조건
        .map(String::toUpperCase)        // 중간: 변환
        .sorted()                        // 중간: 정렬
        .toList();                       // 최종: 리스트로 모으기
System.out.println(result);              // [CHOI, PARK]
```

### 2.1 `map`과 `flatMap`

```java
List<List<String>> nested = List.of(List.of("a", "b"), List.of("c"));
List<String> flat = nested.stream().flatMap(List::stream).toList();
System.out.println(flat); // [a, b, c]
```

`map`은 원소 하나를 하나로 바꾸고, `flatMap`은 여러 원소를 펼쳐서 하나의 스트림으로 합친다.

### 2.2 `reduce`와 `collect`

```java
int sum = List.of(1, 2, 3, 4).stream().reduce(0, Integer::sum);       // 10
Map<Integer, List<String>> byLen = Stream.of("a", "bb", "cc", "ddd")
        .collect(Collectors.groupingBy(String::length));              // {1=[a], 2=[bb, cc], 3=[ddd]}
```

### 2.3 주의: 스트림으로 바꾸면 안 되는 경우

- 중간에 `break`처럼 흐름을 끊어야 할 때는 `for`가 더 명확하다.
- 부수 효과(DB 저장, 파일 쓰기)를 `map` 안에서 하면 실행 순서가 눈에 안 보인다. 부수 효과는 `forEach`나 일반 반복문에 둔다.

---

## 3. Optional

**정의** — "값이 있을 수도 없을 수도 있다"를 타입으로 드러내는 컨테이너다.

```java
Optional<String> found = Optional.of("kim");
Optional<String> empty = Optional.empty();
```

### 3.1 쓰는 방법

| 메서드 | 뜻 | 예 |
|---|---|---|
| `orElse(x)` | 없으면 `x` | `opt.orElse("guest")` |
| `orElseGet(s)` | 없으면 `s.get()` (지연 계산) | `opt.orElseGet(() -> loadDefault())` |
| `orElseThrow()` | 없으면 예외 | `opt.orElseThrow()` |
| `map(f)` | 있으면 변환 | `opt.map(String::length)` |
| `ifPresent(c)` | 있으면 처리 | `opt.ifPresent(System.out::println)` |

```java
String name = Optional.ofNullable(findName(1L))   // null이면 빈 Optional
        .map(String::trim)
        .orElse("guest");
```

### 3.2 안티패턴

- `opt.get()`을 `isPresent()` 검사 없이 호출하면 값이 없을 때 `NoSuchElementException`이 난다. `orElseThrow()`로 의도를 드러낸다.
- 필드나 메서드 인자에 `Optional`을 쓰지 않는다. 반환값에 쓰는 것이 기본 원칙이다.
- `orElse(makeExpensive())`는 값이 있어도 `makeExpensive()`를 호출한다. 비용이 크면 `orElseGet`을 쓴다.

---

## 4. 검증 코드

```java
import java.util.*;
import java.util.stream.*;

public class Batch6 {
    static String findName(long id) { return id == 1L ? "  kim  " : null; }

    public static void main(String[] args) {
        Comparator<String> byLen = (a, b) -> a.length() - b.length();
        System.out.println(Stream.of("ccc", "a", "bb").sorted(byLen).toList()); // [a, bb, ccc]

        List<String> names = List.of("kim", "lee", "park", "choi");
        System.out.println(names.stream().filter(n -> n.length() > 3)
                .map(String::toUpperCase).sorted().toList()); // [CHOI, PARK]

        System.out.println(List.of(List.of("a", "b"), List.of("c")).stream()
                .flatMap(List::stream).toList()); // [a, b, c]

        System.out.println(List.of(1, 2, 3, 4).stream().reduce(0, Integer::sum)); // 10
        System.out.println(Stream.of("a", "bb", "cc", "ddd")
                .collect(Collectors.groupingBy(String::length))); // {1=[a], 2=[bb, cc], 3=[ddd]}

        // 지연 실행: 최종 연산 전에는 map이 실행되지 않는다
        Stream<String> lazy = Stream.of("x").map(s -> { System.out.println("mapped " + s); return s; });
        System.out.println("before terminal");
        lazy.toList();

        // 스트림 재사용은 예외
        Stream<String> once = Stream.of("a");
        once.count();
        try { once.count(); } catch (IllegalStateException e) { System.out.println("reuse: IllegalStateException"); }

        System.out.println(Optional.ofNullable(findName(1L)).map(String::trim).orElse("guest")); // kim
        System.out.println(Optional.ofNullable(findName(2L)).map(String::trim).orElse("guest")); // guest
        try { Optional.empty().orElseThrow(); } catch (NoSuchElementException e) { System.out.println("empty: NoSuchElementException"); }

        // 람다가 바깥 값을 바꾸려면 배열로 감싼다
        int[] counter = {0};
        List.of(1, 2, 3).forEach(x -> counter[0] += x);
        System.out.println(counter[0]); // 6
    }
}
```

**실행 결과 (확인함)**

```
[a, bb, ccc]
[CHOI, PARK]
[a, b, c]
10
{1=[a], 2=[bb, cc], 3=[ddd]}
before terminal
mapped x
reuse: IllegalStateException
kim
guest
empty: NoSuchElementException
6
```

지연 실행 순서(`before terminal`이 먼저, `mapped x`가 나중)가 핵심이다.

---

## 단원 통과 기준

- [ ] 익명 클래스와 람다의 차이를 예로 설명할 수 있다.
- [ ] 중간 연산과 최종 연산을 구분하고, 지연 실행이 무엇인지 설명할 수 있다.
- [ ] `map`과 `flatMap`의 차이를 설명할 수 있다.
- [ ] `orElse`와 `orElseGet`의 차이, `opt.get()`의 위험을 설명할 수 있다.
