# Java 버전 차이: 8, 17, 21, 25를 어떻게 구분해 익히나

> 중요도: **B** · 이 장의 목표: Java 8부터 25까지 문법과 API가 어디서 달라지는지 구분하고, 실무에서 "이 코드는 몇 버전 기준인가"를 판단하고, 8 호환이 필요할 때 컴파일로 확인한다.

> 2026-11-23 추가 부록 (02장 Java 기초를 배운 뒤 읽는다). 아래 예제는 **JDK 25.0.4 (Corretto)** 에서 컴파일과 실행을 확인했다. `--release 8`, `--release 17` 컴파일 결과도 함께 확인했다. Spring Boot의 최소 Java 버전은 공식 문서에서 다시 확인한다.

---

## 1. 학습 방법: 버전 하나를 기준으로 삼는다

- 실습 기준은 **JDK 21(LTS)** 또는 **JDK 25**로 정한다. 이 교재는 25로 검증한다.
- 문법의 큰 줄기(클래스, 상속, 인터페이스, 컬렉션, 제네릭, 람다, 스트림, `Optional`, 예외)는 Java 8부터 있다. 버전과 상관없이 먼저 익힌다.
- 새 기능(`var`, `record`, `switch` 패턴 등)은 **도입 버전을 함께 외운다.** 예: "record는 16부터"
- 학원이나 회사가 쓰는 버전은 `java -version`과 빌드 파일로 확인한다.

```powershell
java -version
```

빌드 파일에서 목표 버전을 지정하는 위치:

- Maven: `pom.xml`의 `<maven.compiler.release>21</maven.compiler.release>`
- Gradle: `build.gradle`의 `java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }`

---

## 2. 버전별 기능 표

| 기능 | 도입 버전 | Java 8에서 쓰던 방식 |
|---|---|---|
| `List.of`, `Set.of`, `Map.of` | 9 | `Arrays.asList`, `Collections.unmodifiableList` |
| `var` (지역 변수 타입 추론) | 10 | 타입을 직접 씀 |
| 텍스트 블록 `"""` | 15 | 문자열 `+`로 이어 붙임 |
| `record` (데이터 클래스) | 16 | getter, `equals`, `hashCode`, `toString`을 직접 작성 |
| `instanceof` 패턴 매칭 | 16 | `instanceof` 뒤에 형 변환을 따로 함 |
| `sealed` 클래스·인터페이스 | 17 | 상속을 막을 수 없음 (`final`로 하나씩 막음) |
| `switch` 표현식 `->`, `yield` | 14 | `switch` 문과 `break` |
| `switch` 패턴 매칭 | 21 | `instanceof` 체인 |
| 가상 스레드 | 21 | 플랫폼 스레드와 스레드 풀 |

**실무 기준**: Spring Boot 3은 Java 17 이상이 필요하다. 따라서 Spring 프로젝트에서 8을 쓰는 경우는 레거시 유지보수뿐이다.

---

## 3. 8 호환 코드 (버전과 무관하게 동작)

```java
import java.util.*;

public class Java8Style {
    static class Point {
        private final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        int getX() { return x; }
        int getY() { return y; }
        @Override public String toString() { return "Point[x=" + x + ", y=" + y + "]"; }
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("kim", "lee");
        Map<String, Integer> ages = new HashMap<>();
        ages.put("kim", 30);
        names.stream().map(String::toUpperCase).forEach(System.out::println);
        System.out.println(new Point(1, 2) + " " + ages);
    }
}
```

**실행 결과 (JDK 25에서 확인함)**

```
KIM
LEE
Point[x=1, y=2] {kim=30}
```

**8 호환 컴파일 확인 (확인함)**

```powershell
javac -Xlint:-options --release 8 Java8Style.java
```

결과: 오류 없이 컴파일된다(종료 코드 0). `-Xlint:-options`를 빼면 "source value 8 is obsolete" 경고가 나온다. 경고는 옵션 문제이지 코드 문제가 아니다.

---

## 4. 최신 문법 모음 (21 기준)

```java
import java.util.*;

public class ModernFeatures {
    record Point(int x, int y) {}                              // 16: 데이터 클래스

    sealed interface Shape permits Circle, Square {}           // 17: 상속 제한
    record Circle(double r) implements Shape {}
    record Square(double s) implements Shape {}

    static double area(Shape s) {
        return switch (s) {                                    // 21: 패턴 매칭 switch
            case Circle c -> Math.PI * c.r() * c.r();
            case Square q -> q.s() * q.s();
        };
    }

    public static void main(String[] args) throws Exception {
        var names = List.of("kim", "lee");                     // var 10, List.of 9
        System.out.println(names);

        Object o = "hello";
        if (o instanceof String str && str.length() > 3) {     // 16: instanceof 패턴
            System.out.println(str.toUpperCase());
        }

        // 15: 텍스트 블록
        String json = """
                {"name": "kim"}
                """;
        System.out.print(json);

        System.out.println(new Point(1, 2));
        System.out.println(area(new Square(2)));

        // 21: 가상 스레드
        try (var ex = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            ex.submit(() -> System.out.println("virtual thread"));
        }
    }
}
```

**실행 결과 (JDK 25에서 확인함)**

```
[kim, lee]
HELLO
{"name": "kim"}
Point[x=1, y=2]
4.0
virtual thread
```

**주의**: 텍스트 블록 여는 `"""` 뒤에는 주석을 쓸 수 없다. 주석은 윗줄에 쓴다. (실습 중 이 오류를 확인했다.)

---

## 5. 버전 경계 확인 (확인함)

같은 파일을 낮은 버전으로 컴파일해 어디서 막히는지 본다.

```powershell
javac -Xlint:-options --release 8 ModernFeatures.java
```

결과 (JDK 25에서 확인함): `records are not supported in -source 8`, `sealed classes are not supported in -source 8` 등 오류가 나온다. 이 결과는 "이 코드는 8에서 컴파일되지 않는다"는 근거로 쓸 수 있다.

```powershell
javac -Xlint:-options --release 17 ModernFeatures.java
```

결과: `switch` 패턴 매칭만 `-source 17`에서 막힌다. 이 결과는 "패턴 switch는 21부터"라는 근거가 된다.

**기억할 방법**: 낮은 `--release`로 컴파일해서 오류가 나는 줄이 곧 그 버전에 없는 기능이다.

---

## 6. 옛 코드를 읽는 법

| 옛 코드에서 보이는 것 | 의미 |
|---|---|
| `new Comparator<String>() { ... }` (익명 클래스) | 람다가 없던 시절 (8 이전 또는 람다 미사용) |
| `for (int i = 0; ...)` 로만 반복 | 스트림 도입 전 |
| `javax.servlet.*`, `javax.persistence.*` | Java EE 시절 패키지. Spring Boot 3 이상은 `jakarta.*` |
| `Arrays.asList(...)`, `Collections.unmodifiableList(...)` | `List.of` 이전 방식 |
| `String s = ...; s += "..."` 긴 문자열 | 텍스트 블록 이전 방식 |

**규칙**: 옛 코드는 기능을 그대로 두고 변환하지 않는다. 새 코드를 쓸 때만 새 문법으로 바꾼다. 리팩터링은 테스트가 있을 때 한다.

---

## 7. 면접 답변 구조

"Java 버전 차이를 아는가"라는 질문에는 다음 순서로 답한다.

1. 정의: "버전마다 문법과 API가 추가된다."
2. 예시: "16에서 `record`가 들어와 데이터 클래스 보일러플레이트를 줄였다."
3. 실무 연결: "Spring Boot 3은 17 이상이 필요해서, 프로젝트 버전을 먼저 확인한다."
4. 경험: 실제로 쓴 버전과 기능을 한 줄로 말한다.

---

## 8. 흔한 실수

| 실수 | 결과 | 고치는 방법 |
|---|---|---|
| 버전을 확인하지 않고 최신 문법을 씀 | 팀 빌드 실패 | `release` 또는 toolchain을 프로젝트에 명시 |
| `javax.*`와 `jakarta.*`를 섞음 | 컴파일 또는 실행 오류 | Spring Boot 버전에 맞는 패키지 사용 |
| `var`를 필드에 씀 | 컴파일 오류 | `var`는 지역 변수에만 쓴다 |
| 텍스트 블록에 주석을 씀 | 컴파일 오류 | 주석은 윗줄에 쓴다 |
| 8 호환이 필요한데 `--release`를 확인하지 않음 | 운영 환경에서 실행 불가 | `javac --release 8`로 미리 확인 |

---

## 9. 실습 과제

1. 본인 PC의 `java -version`을 확인하고 결과를 적는다.
2. 3절의 `Java8Style.java`를 `javac --release 8`로 컴파일하고 종료 코드가 0인지 확인한다.
3. 4절의 `ModernFeatures.java`를 `--release 8`과 `--release 17`로 각각 컴파일하고, 오류 메시지가 어떤 기능을 가리키는지 적는다.
4. 프로젝트의 `pom.xml` 또는 `build.gradle`에서 Java 버전 설정 위치를 찾는다.
5. 옛 코드 한 덩어리(예: 익명 클래스)를 람다로 바꿔 보고 결과가 같은지 확인한다.

---

## 단원 통과 기준

- [ ] `var`, `record`, 텍스트 블록, `sealed`, 패턴 switch의 도입 버전을 말할 수 있다.
- [ ] `javac --release N`으로 버전 호환을 확인할 수 있다.
- [ ] 옛 코드(익명 클래스, `javax.*`)를 보고 어느 시대 코드인지 구별할 수 있다.
- [ ] 프로젝트의 Java 버전을 빌드 파일에서 확인할 수 있다.
- [ ] "Java 8과 21의 차이"를 정의 → 예시 → 실무 연결 순서로 설명할 수 있다.
