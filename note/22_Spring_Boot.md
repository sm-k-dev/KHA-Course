# Spring Boot·웹 API

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `10_SpringBoot_3_4`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — Spring Framework · Boot · 자동 설정

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

Spring Framework는 애플리케이션 개발 기반 기능을 제공하고 Spring Boot는 Spring 애플리케이션의 설정·실행을 간편하게 만드는 도구와 관례를 제공한다.

### 2. 용어를 구분해야 하는 이유

Spring Boot가 Spring과 별개의 DI 프레임워크라는 말은 틀리다. 자동 설정이 모든 설정을 대신해 주는 것도 아니다.

### 3. 새로운 상황에 적용

웹 스타터를 추가했을 때 웹 관련 Bean이 조건에 따라 자동 구성되는 이유를 설명한다.

### 4. 면접 질문과 답변 기준

**질문:** Spring Framework · Boot · 자동 설정의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** Spring Boot는 Spring Framework를 기반으로 자동 설정과 스타터, 실행 지원 등을 제공해 애플리케이션 구성을 단순화합니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. Spring과 Spring Boot

**정의** — Spring Framework는 DI·AOP·웹·데이터 접근 등을 제공하는 프레임워크이고 Spring Boot는 자동 설정·스타터·실행 편의로 Spring 애플리케이션 구성을 단순화한다.

**목적과 책임** — 반복 설정을 줄이고 실행 가능한 서비스를 빠르게 구성한다.

**구별해야 할 개념 / 흔한 오해** — Spring Boot는 Spring Framework를 대체하는 별개 프레임워크가 아니다.

**실행·처리 흐름** — 스타터 의존성 → 자동 설정 조건 평가 → 컨텍스트 생성 → 서버 기동

**직접 구현·검증** — 자동 설정이 적용된 Bean과 사용자 정의 Bean을 비교. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Spring과 Spring Boot의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

Spring Framework는 DI·AOP·MVC 등의 기반 기능을 제공하고 Spring Boot는 자동 설정·스타터·실행 환경 등을 통해 Spring 애플리케이션 구성을 간소화한다. Boot가 Spring을 대체하는 별개의 웹 프레임워크는 아니다. 자동 설정은 조건에 따라 적용되며 명시 설정으로 재정의할 수 있다.

### 직접 읽고 구현하는 독립 예제

```java
@SpringBootApplication
public class DemoApplication {
 public static void main(String[] args){SpringApplication.run(DemoApplication.class,args);}
}
```


### 실행 검증 체크포인트

실행 로그에서 내장 서버 시작 여부와 Bean 구성을 확인한다.

### 스스로 작성할 변형 실습

@SpringBootApplication이 포함하는 메타 애너테이션을 조사한다.

### 면접 답변의 기준 문장

> Spring Boot는 Spring 기반 애플리케이션의 설정·실행을 간소화하는 프로젝트입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“Spring Framework는 DI·AOP·웹·데이터 접근 등을 제공하는 프레임워크이고 Spring Boot는 자동 설정·스타터·실행 편의로 Spring 애플리케이션 구성을 단순화한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“Spring Boot는 Spring Framework를 대체하는 별개 프레임워크가 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“Spring과 Spring Boot의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. 검증·예외·API 계약

**정의** — 요청 검증은 외부 입력이 API 계약을 만족하는지 확인하고 예외 처리는 실패를 일관된 HTTP 응답으로 변환하는 책임을 갖는다.

**목적과 책임** — 잘못된 입력을 조기에 거절하고 호출자에게 안정적인 오류 형식을 제공한다.

**구별해야 할 개념 / 흔한 오해** — @Valid는 모든 비즈니스 규칙 검사를 대체하지 않는다.

**실행·처리 흐름** — 요청 역직렬화 → 제약 검증 → 서비스 → 예외 매핑 → 오류 JSON

**직접 구현·검증** — 빈 제목과 중복 이메일에 대해 서로 다른 오류를 반환. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Bean Validation과 업무 검증은 어떻게 다른가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

입력 검증은 요청 값이 계약과 도메인 규칙을 만족하는지 확인하는 과정이다. Bean Validation은 필드 수준 검증에 유용하지만 모든 업무 규칙을 대체하지 않는다. 예외 처리는 오류를 일관된 응답으로 변환하되 내부 구현 정보를 숨겨야 한다.

### 직접 읽고 구현하는 독립 예제

```java
record CreateUserRequest(@NotBlank String name,@Email String email) {}
@PostMapping("/users") ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest req){
 return ResponseEntity.status(201).build();
}
```


### 실행 검증 체크포인트

빈 이름과 잘못된 이메일로 400 응답이 생성되는지 확인한다. 검증 의존성 및 예외 처리 설정을 준비한다.

### 스스로 작성할 변형 실습

동일 이메일 등록 같은 업무 규칙은 어디에서 검사해야 하는지 설명한다.

### 면접 답변의 기준 문장

> API 검증은 입력 계약을 지키고 예외 처리는 실패를 일관된 응답으로 변환합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“요청 검증은 외부 입력이 API 계약을 만족하는지 확인하고 예외 처리는 실패를 일관된 HTTP 응답으로 변환하는 책임을 갖는다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“@Valid는 모든 비즈니스 규칙 검사를 대체하지 않는다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“Bean Validation과 업무 검증은 어떻게 다른가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. 테스트 계층

**정의** — 단위 테스트는 작은 구성 요소의 동작을 격리해 확인하고 통합 테스트는 구성 요소 간 연결·설정·실제 데이터 접근을 함께 검증한다.

**목적과 책임** — 변경으로 인한 회귀를 빠르게 발견한다.

**구별해야 할 개념 / 흔한 오해** — 테스트 통과가 모든 운영 환경 문제의 부재를 보장하지 않는다.

**실행·처리 흐름** — 요구사항 → 테스트 사례 → 실행 → 실패 분석 → 수정 → 재실행

**직접 구현·검증** — Service 단위 테스트와 API 통합 테스트 각각 1개 작성. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 단위 테스트와 통합 테스트의 목적은?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## 폼 제출과 검증 오류 표시: @ModelAttribute, @Valid, BindingResult (2026-11-23 보강, MockMvc로 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**흐름**: 브라우저가 폼을 `POST`로 보내면 Spring이 폼 값을 객체에 채우고(`@ModelAttribute`), 검증 규칙(`@Valid`)을 적용한다. 실패한 항목은 `BindingResult`에 모인다. 컨트롤러는 이 결과를 보고 다시 폼을 보여 주거나 저장한다.

```java
public static class SignupForm {
    @NotBlank(message = "이름을 입력하세요")
    private String name;
    @Min(value = 0, message = "나이는 0 이상이어야 합니다")
    private int age;
    // getter, setter 생략
}

@PostMapping("/signup")
@ResponseBody
public String signup(@Valid @ModelAttribute("form") SignupForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
        return "invalid: " + result.getFieldErrors().stream()
                .map(e -> e.getField() + "=" + e.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));
    }
    return "ok: " + form.getName() + " " + form.getAge();
}
```

**실행 결과 (확인함)**

```
bad   -> 200 invalid: age=0 이상이어야 합니다, name=이름을 입력하세요
good  -> 200 ok: kim 30
```

- `name=` 빈 값과 `age=-1`을 보냈더니 두 항목의 메시지가 모두 돌아왔다. 검증 실패 시 `BindingResult`가 항목별 오류를 가졌다.
- 정상 값은 `ok`로 처리됐다.

**설명 시 주의**: 이 예제는 Spring Boot 전체가 아니라 MockMvc standalone 설정으로 확인했다. 검증 엔진(Hibernate Validator)은 EL(표현 언어) 구현이 필요한데, 이 PC에는 EL jar가 없어서 `ParameterMessageInterpolator`로 바꿔 연결했다. Spring Boot 프로젝트에서는 `spring-boot-starter-validation`이 이 설정을 자동으로 맞춘다. 실제 프로젝트에서는 이 연결 코드를 쓰지 않는다.

**흔한 실수**
- `BindingResult`를 `@Valid` 바로 뒤에 두지 않는다: `@Valid` 객체 바로 다음 인자여야 오류가 `BindingResult`에 담긴다. 사이에 다른 인자가 있으면 검증 실패가 예외로 나간다.
- 검증 메시지를 템플릿에서 출력하지 않는다: Thymeleaf에서는 `th:errors`나 `th:field`로 항목별 오류를 화면에 보여 준다(24장).

**면접 기준**: "폼 값은 `@ModelAttribute` 객체에 바인딩되고, `@Valid`로 규칙을 확인합니다. 실패한 항목은 `BindingResult`에 모여서 화면에 다시 보여 줍니다."

### 폼 검증 애노테이션: null과 빈 문자열은 다르게 처리된다 (9_ 자료 05 기준, Bean Validation 실행 확인)

> 추가 근거: 9_SpringBoot_Basic `05 spring form validation` 강의 자료(17쪽)의 `StudentEdit` 모델을 읽고 보강했다. 아래는 **Hibernate Validator 9.1.3을 직접 실행한 결과**다(2026-11-24). 스프링 웹 폼 화면 전체 흐름은 이 절 위의 MockMvc 예와 별개로 **미실행**이다.

```java
public static class StudentEdit {
    @NotEmpty @NotBlank @Size(min = 8, max = 12) public String studentNo;
    @NotEmpty @NotBlank @Pattern(regexp = "010-[0-9]{3,4}-[0-9]{4}",
                                 message = "휴대폰 번호를 입력하세요 예:010-000-0000") public String phone;
    @NotEmpty @Email public String email;
    @NotEmpty @NotBlank @Pattern(regexp = "남|여", message = "남, 여 중 하나를 입력하세요.") public String sex;
    @Min(value = 1, message = "학과를 선택하세요.") public int departmentId;
}
```

| 입력 | 나온 오류 (확인함) |
|---|---|
| 정상 값 (학번 9자리, 휴대폰 `010-2090-1421`, 메일, 성별 `여`, 학과 1) | 0건 |
| 잘못된 값 (학번 `123`, 이름 공백, 휴대폰 `0101234`, 메일 `not-an-email`, 성별 `기타`, 학과 `0`) | 7건: 학번 크기, 이름 공백, 이름 크기, 휴대폰 형식, 메일 형식, 성별 형식, 학과 최소값 |
| 모든 필드가 `null` (빈 객체) | 10건: `@NotEmpty`·`@NotBlank` 오류와 `@Min` 오류. `@Pattern`·`@Email`·`@Size` 오류는 **없다** |

**핵심 규칙 (확인함):** `@Pattern`, `@Email`, `@Size`는 값이 `null`이면 **통과**한다. 그래서 `null`을 막으려면 `@NotNull`·`@NotBlank`·`@NotEmpty`를 함께 붙인다. 강의 자료가 `@NotEmpty @NotBlank`를 같이 쓰는 이유가 이것이다. 빈 문자열 `""`은 `@NotEmpty`가 막고, 공백 `" "`은 `@NotBlank`가 막는다.

**메시지:** `message`를 생략하면 기본 메시지가 나온다. 한국어 기본 메시지는 설정 언어에 따라 바뀐다(위 실행에서 `공백일 수 없습니다`처럼 나옴).

**주의 (확인함):** Hibernate Validator는 EL 구현이 없으면 기본 메시지 보간에서 실패할 수 있다. 실행에서는 `ParameterMessageInterpolator`를 썼다. 스프링 부트 웹 앱에서는 의존성과 설정으로 자동 처리되므로 이 설정은 독립 실행에서만 필요하다.

**면접 기준:** "검증 애노테이션은 `null`을 통과시키는 것이 많다. 필수 값은 `@NotNull`, `@NotBlank`, `@NotEmpty` 중 하나로 명시하고, 형식은 `@Pattern`·`@Email`로 검사한다."

### 4-1. 빌드 파일 읽고 실행하기 (2026-11-23 보강)

> 확인 범위: Gradle 빌드 파일은 작성했고, 같은 과정을 `javac`·`jar`로 직접 실행해 확인했다. `gradle build` 자체는 이 환경에서 실행하지 못했다(아래 참고). Maven은 이 PC에 설치되어 있지 않아 미실행이다.

**Gradle 빌드 파일 (`build.gradle`)**

```groovy
plugins {
    id 'java'
    id 'application'
}

tasks.withType(JavaCompile).configureEach {
    options.release = 21          // 21 바이트코드로 컴파일 (JDK 25로 빌드해도 됨)
}

application {
    mainClass = 'app.App'        // java -jar 실행 시 시작 클래스
}
```

**확인한 것 (JDK 25에서 실행함)**

```powershell
javac --release 21 -d out src\main\java\app\App.java
jar --create --file app.jar --main-class app.App -C out .
java -jar app.jar
javap -v out\app\App.class | findstr "major version"
```

- `java -jar app.jar`가 `Hello from Gradle build`를 출력했다.
- `major version: 65`가 나왔다. 클래스 파일 버전 65는 Java 21이다. 즉 `--release 21`이 실제로 바이트코드에 반영되었다.
- 이 순서가 Gradle `build`와 `jar` 태스크가 하는 일이다. 빌드 도구는 의존성 내려받기와 이 순서를 자동화한다.

**`gradle build`가 실패한 이유 (미실행)**: Gradle 9.8.2는 `gradle -v`로 실행된다. 그러나 `gradle --offline build`가 `java.io.IOException: Unable to establish loopback connection`으로 실패했다. 이 환경에서 Gradle이 내부 연결을 만들지 못하는 문제로 보인다. 원인을 확정하지 못했으니 다른 PC나 IntelliJ에서 `gradle build`를 한 번 실행해 확인한다.

**명령 대응표**

| 목적 | Maven | Gradle |
|---|---|---|
| 빌드와 jar 만들기 | `mvn package` | `gradle build` |
| 테스트 실행 | `mvn test` | `gradle test` |
| 의존성 트리 보기 | `mvn dependency:tree` | `gradle dependencies` |
| 빌드 결과 지우기 | `mvn clean` | `gradle clean` |
| 실행 | `java -jar target/app.jar` | `java -jar build/libs/app.jar` |

**Maven 설정 예 (`pom.xml`, 미실행)**

```xml
<properties>
    <java.version>21</java.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

**읽는 법**: Spring Boot 프로젝트에서 `spring-boot-starter-web` 한 줄을 넣으면 웹 서버와 JSON 변환 라이브러리가 함께 들어온다. 버전은 Spring Boot 부모 설정이 맞춰 준다. 의존성이 실제로 어떻게 들어오는지는 `mvn dependency:tree`나 `gradle dependencies`로 확인한다.

### 4-2. Lombok과 record (2026-11-23 보강, JDK 25에서 확인함)

Lombok은 getter, `equals`, `toString` 같은 반복 코드를 컴파일 때 만들어 준다. `record`는 언어 기능으로 같은 목적을 달성한다. 둘 다 실무에서 보이므로 차이를 알아야 한다.

```java
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class LombokDemo {
    @Getter @ToString @EqualsAndHashCode @Builder
    static class MemberDto {
        private final String name;
        private final int age;
    }

    record MemberRecord(String name, int age) {}

    public static void main(String[] args) {
        MemberDto a = MemberDto.builder().name("kim").age(30).build();
        MemberDto b = MemberDto.builder().name("kim").age(30).build();
        System.out.println(a);
        System.out.println(a.equals(b) + " " + a.getName());
        MemberRecord r1 = new MemberRecord("kim", 30);
        System.out.println(r1);
        System.out.println(r1.equals(new MemberRecord("kim", 30)) + " " + r1.name());
    }
}
```

**실행 방법 (확인함)**: Lombok jar(1.18.46)를 `-cp`와 `-processorpath`에 같이 지정해 컴파일한다. JDK 25에서 `sun.misc.Unsafe` 경고가 나오지만 결과는 정상이다.

```powershell
javac -cp lombok.jar -processorpath lombok.jar -d out LombokDemo.java
java -cp out LombokDemo
```

**실행 결과 (확인함)**

```
LombokDemo.MemberDto(name=kim, age=30)
true kim
MemberRecord[name=kim, age=30]
true kim
```

**선택 기준**

| 구분 | Lombok | record |
|---|---|---|
| 용도 | 일반 클래스, 엔티티, 가변 객체 | 값만 담는 불변 데이터 (DTO, 응답 객체) |
| 설정 | 라이브러리 의존성과 IDE 플러그인 필요 | JDK 16 이상이면 바로 사용 |
| JPA 엔티티 | `@NoArgsConstructor`와 함께 쓰고 상태 변경 메서드를 둔다 | 기본 생성자가 없어 엔티티로 쓰기 어렵다 |
| 디버깅 | 생성된 코드가 보이지 않아 혼란이 있을 수 있다 | 코드가 그대로 보인다 |

**면접 기준**: "응답용 DTO처럼 값만 담는 객체는 `record`를 쓰고, JPA 엔티티처럼 상태가 바뀌는 객체는 Lombok을 씁니다. 두 방식 모두 반복 코드를 줄입니다."

### 4-3. 빌드, 실행, 배포는 다른 단계다 (2026-11-24 보강, 선생님 자료 기반)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

| 단계 | 하는 일 | 결과물 | 예 |
|---|---|---|---|
| 빌드 | 소스와 설정, 라이브러리를 모아 컴파일하고 실행 가능한 결과물을 만든다 | `build/libs/app.jar` | `gradle build` |
| 실행 | 만들어진 결과물을 실행한다 | 실행 중인 프로세스 | `java -jar app.jar`, IntelliJ의 Run |
| 배포 | 실행할 결과물을 서버(EC2, Beanstalk 등)에 올린다 | 서버에서 돌아가는 서비스 | 44장 Beanstalk, 46장 CI/CD |

**구분할 것**
- 빌드는 **파일을 압축하는 것이 아니다.** 컴파일, 리소스 처리, 테스트(설정에 따라), jar 생성이 포함된다.
- 실행은 **내 PC에서 돌려 보는 것**이다. 빌드가 되었다고 실행이 된다는 뜻은 아니다. 설정 값이나 포트가 틀리면 실행이 실패한다.
- 배포는 **다른 컴퓨터에서 돌리는 것**이다. 같은 jar라도 서버의 환경 변수, 권한, 경로가 다르면 실패할 수 있다(43장의 함정 표).

**면접 기준**: "빌드는 소스를 실행 가능한 결과물로 만드는 단계이고, 실행은 그것을 돌려 보는 단계, 배포는 서버에 올려 서비스하는 단계입니다. 각 단계에서 실패 원인이 다르므로 나눠서 확인합니다."

### 4-4. Gradle 프로젝트 가져오기와 내보내기 (IntelliJ·STS, 2026-11-24 보강)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.
- [JetBrains, Gradle 도움말](https://www.jetbrains.com/help/idea/gradle.html)
- [JetBrains, 프로젝트 가져오기](https://www.jetbrains.com/help/idea/import-project-or-module-wizard.html)
- [JetBrains, Gradle 설정](https://www.jetbrains.com/help/idea/gradle-settings.html)
- [Spring Boot 레퍼런스, 애플리케이션 실행](https://docs.spring.io/spring-boot/reference/using/running-your-application.html)

**확인 범위**: 검색 결과의 요약만 읽었고 IDE 화면 조작은 이 PC에서 하지 않았다. 아래 단계는 **미실행**이다. 실제 버전에 맞게 원문에서 다시 확인한다.

**가져오기 (Import, 기존 Gradle 프로젝트를 IDE에서 열기)**
1. 빌드 파일(`build.gradle`)이 있는 폴더를 고른다. IDE는 그 폴더를 열고 Gradle과 동기화한다.
2. 이미 프로젝트가 열려 있다면 Gradle 도구 창에서 **Link Gradle Project**로 빌드 파일을 연결한다.
3. 같은 폴더에 Eclipse 설정과 Gradle 설정이 함께 있으면 IDE가 어느 쪽으로 열지 묻는다. Gradle을 고르면 빌드 스크립트를 실행하고 의존성을 내려받는다.
4. 프로젝트마다 Gradle 배포판과 JDK는 `Build, Execution, Deployment → Gradle` 설정에서 맞춘다.

**내보내기 (Export, 다른 사람이 같은 프로젝트를 받게 하기)**
- IDE 기능이라기보다 **저장소에 올릴 파일을 고르는 일**이다. `build/`, `.gradle/`, `.idea/`, `bin/` 같은 생성물은 빼고, `build.gradle`, `settings.gradle`, `gradlew`, `src/`를 올린다.
- 실행 결과물(jar)은 `gradle build`로 만든다(위 4-1절). 배포는 그 jar를 서버에 올리는 별도 단계다.
- 민감 정보(DB 비밀번호, API 키)는 공유 파일에 넣지 않는다. DB 데이터는 프로젝트 파일에 들어가지 않으므로 SQL 백업이나 초기 스크립트를 따로 전한다.
- Spring Boot 문서는 IDE가 프로젝트를 바로 읽지 못하면 빌드 플러그인으로 IDE 정보를 만들 수 있다고 안내한다. 대부분의 최신 IDE는 Gradle을 직접 지원한다.

**면접 기준**: "Gradle 프로젝트는 빌드 파일을 IDE에 연결해서 열고, 공유할 때는 소스와 빌드 설정만 올리고 생성물은 제외합니다. 결과물은 `gradle build`로 만들고, 배포는 그 결과를 서버에 올리는 단계입니다."

**STS(이클립스 기반) 가져오기와 자주 나는 오류** — 4-4절의 가져오기·내보내기 원칙을 STS 메뉴로 옮긴 것이다. (2026-11-24 보강, 선생님 자료 기반, 미실행)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**보내는 쪽 (Export)**: 4-4절의 내보내기 원칙과 같다. 빌드 파일, Gradle wrapper, 소스만 ZIP에 넣고, 민감 정보는 빼며, DB 데이터는 ZIP에 들어가지 않는다는 점을 함께 확인한다.

**받는 쪽 (Import)**
1. ZIP을 먼저 **폴더로 풀어** 둔다. ZIP 파일을 IDE에 바로 가져오지 않는다.
2. STS에서 `File → Import → Gradle → Existing Gradle Project`를 고르고, `build.gradle`이 있는 폴더를 지정한다.
3. 가져오기가 끝날 때까지 기다린다. 처음에는 라이브러리를 내려받아서 시간이 걸린다.
4. 프로젝트에서 `Gradle → Refresh Gradle Project`를 실행한다.

**실행 전 확인**: JDK 설정(프로젝트가 요구하는 Java 버전이 등록되어 있는지), Problems 뷰의 오류, 메인 클래스의 `Run As → Spring Boot App`, 콘솔의 포트(기본 8080)를 본다. DB를 쓰는 프로젝트라면 받는 PC에도 DB 서버, 데이터베이스, 테이블, 초기 데이터를 준비한다.

**자주 나는 오류 (자료 표를 옮김)**

| 증상 | 확인과 해결 |
|---|---|
| `Existing Gradle Project`가 보이지 않음 | Gradle 지원 플러그인과 STS 설치 구성을 확인한다 |
| Gradle 다운로드·동기화 실패 | 인터넷과 프록시 설정, 저장소 접근을 확인하고 Refresh를 다시 실행한다 |
| `Unsupported class file major version` | 프로젝트가 요구하는 Java 버전과 Gradle 버전의 호환성을 확인한다 |
| `Could not target platform` 또는 toolchain 오류 | `build.gradle`의 Java toolchain 버전과 PC에 설치된 JDK를 맞춘다 |
| 포트 8080이 이미 사용 중 | 기존 서버를 끄거나 `server.port=8081`처럼 다른 포트를 쓴다 |
| 실행은 되지만 데이터가 없음 | 소스와 ZIP만으로는 DB 데이터가 복사되지 않는다. SQL 백업이나 초기 스크립트를 준비한다 |

**연결해서 이해하기**: `Unsupported class file major version`은 19장 4-1절에서 확인한 클래스 파일 버전과 같은 문제다. `javap -v`의 `major version`이 65이면 Java 21 바이트코드다. 실행하는 JDK가 그보다 낮으면 이 오류가 난다.

**면접 기준**: "프로젝트를 옮길 때는 빌드 파일과 Gradle wrapper, 소스만 ZIP으로 보내고, 민감 정보와 DB 데이터는 따로 다룹니다. 받는 쪽은 폴더로 푼 뒤 Gradle 프로젝트로 가져오고, JDK와 toolchain을 맞춘 뒤 실행합니다."

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

단위 테스트는 작은 단위의 동작을 격리해 확인하고 통합 테스트는 실제 구성 요소 간 상호작용을 확인한다. 테스트 범위는 사용하는 프레임워크와 의존성에 따라 선택한다. Mock 테스트만으로 DB 제약·트랜잭션을 검증했다고 볼 수 없다.

### 직접 읽고 구현하는 독립 예제

```java
@Test void rejectNegative(){
 assertThrows(IllegalArgumentException.class,()->new Account(-1));
}
```


### 실행 검증 체크포인트

정상·경계·실패 케이스를 구분하고 테스트 이름을 요구사항 중심으로 작성한다.

### 스스로 작성할 변형 실습

@WebMvcTest와 @SpringBootTest가 검증하는 범위를 비교한다.

### 면접 답변의 기준 문장

> 단위 테스트는 좁은 책임을, 통합 테스트는 구성 요소 간 실제 협력을 검증합니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: 자동 설정과 API 응답은 서로 다른 책임

Spring Boot의 자동 설정은 클래스패스·프로퍼티·기존 Bean 등의 조건을 보고 기본 구성을 제공한다. 자동 설정이 DB의 0행을 HTTP 404로 **자동 번역하는 것은 아니다**. Repository 결과의 부재를 예외나 응답으로 해석하는 애플리케이션 로직이 필요하다.

```java
@RestControllerAdvice
class ApiErrorHandler {
  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<Map<String, String>> notFound(UserNotFoundException ex) {
    return ResponseEntity.status(404).body(Map.of("code", "USER_NOT_FOUND"));
  }
}
```

**실습:** `@WebMvcTest`에서 존재하지 않는 ID에 404가 반환되는지 확인한다. `@SpringBootTest`는 더 넓은 컨텍스트를 로드하므로 슬라이스 테스트와 구분한다. 실제 DB 연동은 별도의 통합 테스트로 검증한다.


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. Spring / Spring Boot / 내장 서버

**흔한 오해:** “Spring Boot는 Spring과 완전히 별개의 프레임워크다”

**정확한 구분:** Spring Framework는 DI·AOP·웹 등의 기반을 제공하고 Spring Boot는 자동 구성·스타터·실행 편의성을 더한다. 내장 서버는 웹 앱을 실행하는 선택 가능한 방식이다.

**코드·명령 해석:**

```java
@SpringBootApplication
public class App { public static void main(String[] args){ SpringApplication.run(App.class,args); } }
```

**확인 방법:** 어떤 의존성으로 웹 서버가 시작되는지 빌드 파일에서 확인한다.

**직접 구현:** spring-boot-starter-web 제거 시 웹 기능이 어떻게 달라지는지 확인하라.

**면접 점검:** “Spring / Spring Boot / 내장 서버”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. @Controller / @RestController / @ResponseBody

**흔한 오해:** “Controller는 JSON을 반환할 수 없다”

**정확한 구분:** @Controller는 MVC 핸들러로 보통 뷰 이름을 반환할 수 있다. @RestController는 @Controller와 @ResponseBody 의미를 합쳐 반환값을 응답 본문으로 직렬화한다.

**코드·명령 해석:**

```java
@RestController class PingController { @GetMapping("/ping") String ping(){return "pong";} }
```

**확인 방법:** 같은 문자열을 @Controller 메서드에서 반환했을 때 뷰 해석과 어떻게 달라지는지 확인한다.

**직접 구현:** JSON DTO를 반환하는 API와 HTML 뷰를 반환하는 컨트롤러를 나눠라.

**면접 점검:** “@Controller / @RestController / @ResponseBody”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. 설정값 / 환경 변수 / 비밀 정보

**흔한 오해:** “application.yml에 비밀번호를 적으면 운영에 적합하다”

**정확한 구분:** 설정 파일은 일반 구성에 유용하지만 자격 증명은 환경 변수나 비밀 관리 시스템 등으로 분리해야 한다. 프로파일은 환경별 구성 선택 수단이지 보안 경계 자체는 아니다.

**코드·명령 해석:**

```java
spring:
  datasource:
    password: ${DB_PASSWORD}
```

**확인 방법:** DB_PASSWORD가 없을 때 실행 오류를 확인하고 안전한 배포 구성을 만든다.

**직접 구현:** 로컬/테스트/운영 설정을 구분하고 비밀 정보가 Git에 포함되지 않게 하라.

**면접 점검:** “설정값 / 환경 변수 / 비밀 정보”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“단위 테스트는 작은 구성 요소의 동작을 격리해 확인하고 통합 테스트는 구성 요소 간 연결·설정·실제 데이터 접근을 함께 검증한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“테스트 통과가 모든 운영 환경 문제의 부재를 보장하지 않는다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“단위 테스트와 통합 테스트의 목적은?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.


## 4. 빌드 도구와 운영 구성 개념

- **Maven·Gradle:** 의존성 내려받기, 컴파일, 테스트, 패키징(jar)을 자동화하는 빌드 도구다. Maven은 `pom.xml`, Gradle은 `build.gradle`에 의존성을 적는다.
- **실행 가능한 jar:** Spring Boot는 빌드 결과 jar 하나에 내장 서버가 들어 있어서, `java -jar`로 바로 실행할 수 있다.
- **설정 분리:** 프로파일(`application-prod.yml`)과 환경 변수로 DB 주소와 비밀번호를 코드 밖에서 넣는다.
- **앞단 웹 서버:** 운영에서는 Nginx를 앞에 두고 80/443 요청을 앱의 8080 포트로 넘기는 구성을 흔히 쓴다(리버스 프록시). 앱을 여러 개 띄우고 Nginx가 나눠 보내면 로드밸런싱이 된다.
- **면접 기준:** "Maven/Gradle로 빌드하고 jar 하나로 배포하며, 설정은 환경 변수로 분리하고, 앞단에 Nginx를 둬서 요청을 앱으로 전달합니다."

## 5. 테스트 도구: JUnit 5와 Mockito

- **JUnit 5:** 테스트를 실행하고 기대값과 비교하는 도구. `@Test`, `assertEquals`, `assertThrows`를 쓴다.
- **Mockito:** 의존하는 객체를 가짜(mock)로 바꿔서 한 클래스만 따로 테스트하게 한다.
- **예시:**

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repository;
    @InjectMocks UserService service;

    @Test void 없는_사용자면_예외() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.get(99L));
    }
}
```

- **구분:** 이 테스트는 Repository를 가짜로 바꾼 단위 테스트다. DB까지 실제로 연결하면 통합 테스트다.
- **실행 결과 (2026-11-23):** JUnit 5 콘솔 실행에서 1개 테스트 통과. 단, JDK 25에서는 Mockito 5.11 + Byte Buddy 1.14 조합이 실패했다. Mockito 5.20 + Byte Buddy 1.17로 바꾸자 통과했다. 그래서 Mockito 버전은 JDK 버전에 맞춰야 한다.
- **경고:** 최신 Mockito는 실행 중 Java agent를 자동으로 붙이는데, 이 경고가 뜨면 build 설정에서 agent를 명시하라는 안내다.
- **면접 기준:** "단위 테스트는 Mockito로 의존 객체를 가짜로 바꿔서 한 클래스의 동작만 확인하고, 통합 테스트는 실제 DB와 함께 확인합니다."

## 6. 서버 사이드 템플릿: Thymeleaf와 Mustache (비교용)

- **정의:** 서버에서 HTML 화면을 만들 때 데이터를 채워 넣는 템플릿 엔진이다. 학원 세미프로젝트에서 Spring Boot와 함께 썼다(Boot + JPA + Thymeleaf 또는 Mustache).
- **Thymeleaf:** HTML 파일 그대로 열어도 보이는 "자연 템플릿"이다. `th:text="${post.title}"`처럼 속성으로 값을 넣는다. Spring과 잘 맞는다.
- **Mustache:** `{{title}}`처럼 중괄호로 값을 넣는 간단한 문법이다. 로직이 적고 언어에 덜 묶여 있다.
- **지금 우선순위:** 이 둘은 **비교용(인지)** 이다. 현재 프론트 학습은 React·TypeScript(27장)에 집중하고, 서버 렌더링 방식이 있다는 것만 이해하면 된다.
- **비교:** 서버 렌더링(Thymeleaf, Mustache)은 처음 화면을 서버가 만든다. React는 브라우저에서 데이터를 받아 화면을 그린다. 검색 엔진이나 첫 화면 속도가 중요하면 서버 렌더링이 유리한 경우가 있다.
- **면접 기준:** "Thymeleaf와 Mustache는 서버에서 HTML을 만드는 템플릿이고, React는 브라우저에서 화면을 그립니다. 프로젝트 성격에 따라 고릅니다."
