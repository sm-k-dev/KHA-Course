# Spring Boot 22-3: 빌드·Lombok·Gradle

> 이 파트는 [22장 허브](22_Spring_Boot.md)의 일부입니다. 이전: [22-2](22-2_폼_검증_서버실행.md). 다음: [22-4 테스트 도구·템플릿](22-4_테스트도구_템플릿.md).

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

**`gradle build`와 `bootRun`이 실패한 이유 (미실행)**: Gradle 9.8.2는 `gradle -v`로 실행된다. 그러나 `gradle --offline build`가 `java.io.IOException: Unable to establish loopback connection`으로 실패했다. 2026-11-24에는 `gradle help`와 `gradle bootRun`도 같은 오류로 실패했다(`--no-daemon`, `--console=plain` 시도 포함). Gradle이 자기 내부 연결을 만들지 못하는 문제다. 소켓 바인딩 자체는 Java `ServerSocket`으로 성공했다. 이 환경에서 Gradle이 내부 연결을 만들지 못하는 문제로 보인다. 원인을 확정하지 못했으니 다른 PC나 IntelliJ에서 `gradle build`를 한 번 실행해 확인한다.

**명령 대응표**

| 목적 | Maven | Gradle |
|---|---|---|
| 빌드와 jar 만들기 | `mvn package` | `gradle build` |
| 테스트 실행 | `mvn test` | `gradle test` |
| 의존성 트리 보기 | `mvn dependency:tree` | `gradle dependencies` |
| 빌드 결과 지우기 | `mvn clean` | `gradle clean` |
| 실행 | `java -jar target/app.jar` | `java -jar build/libs/app.jar` |

**Maven 설정 예 (`pom.xml`, 2026-11-24 빌드 확인함)** — [22-2의 "실제 서버 실행" 절](22-2_폼_검증_서버실행.md)의 `pom.xml`과 같은 구조다.

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

### 검증 오류 문구 바꾸기: ValidationMessages.properties (9_ 05 기준, 확인함)

> 추가 근거: 9_SpringBoot_Basic `05 spring form validation` 강의의 3장(오류 문구 수정, 11~16쪽)을 읽고 보강했다. 아래는 **Hibernate Validator 9.1.3에 `ValidationMessages.properties`를 클래스패스에 두고 실행한 결과**다(2026-11-24). 스프링 웹 화면의 한글 깨짐 해결(IDE 인코딩 설정)은 미실행이다.

```properties
jakarta.validation.constraints.Size.message=크기가 {min} 이상 {max} 이하이어야 합니다
jakarta.validation.constraints.Email.message=이메일 주소가 바르지 않습니다
jakarta.validation.constraints.NotEmpty.message=필수 입력항목입니다
jakarta.validation.constraints.Min.message=값이 {value} 이상이어야 합니다
jakarta.validation.constraints.Max.message=값이 {value} 이하이어야 합니다
```

| 모델의 선언 | 출력 (확인함) |
|---|---|
| `@Size(min=8, max=12) studentNo` (값 `123`) | `크기가 8 이상 12 이하이어야 합니다` |
| `@Email email` (값 `x`) | `이메일 주소가 바르지 않습니다` |
| `@NotEmpty name` (값 `null`) | `필수 입력항목입니다` |
| `@Max(4) year` (값 `9`) | `값이 4 이하이어야 합니다` |
| `@Min(value=1, message="학과를 선택하세요") departmentId` (값 `0`) | `학과를 선택하세요` |
| `@Size(min=2, max=20, message="이름 길이가 {min} 이상 {max} 이하여야 합니다")` | `이름 길이가 2 이상 20 이하여야 합니다` |

**우선순위 (확인함):** 애노테이션에 직접 쓴 `message`가 `ValidationMessages.properties`보다 우선한다. 나머지는 properties의 기본 문구를 쓴다. `{min}`, `{max}`, `{value}`는 애노테이션 값으로 채워진다.

**주의:** 스프링 폼에서 한글 문구가 깨져 보이면 properties 파일 인코딩을 UTF-8로 맞춘다(강의의 IDE 설정). 이 실행은 파일을 UTF-8로 저장해서 깨지지 않았다.

### Lombok 제외 설정과 JSON 필드 이름 (9_ JPA 04·02 기준, 확인함)

- `@Data`를 양방향 연관 엔티티에 붙이면 `toString`이 서로를 부른다. `@ToString.Exclude`와 `@EqualsAndHashCode.Exclude`를 연관 컬렉션에 붙이면 무한 반복이 사라진다. 실행 결과: `LombokCheck.Department(id=1, name=소프트웨어공학과)`로 출력되고 `equals`도 정상이었다(Lombok 1.18.46, Java 25). 순환 참조의 JSON 쪽은 [25-4 순환 참조와 JSON](25-4_로딩_운영_MyBatis.md)을 본다.
- JSON 속성 이름이 getter에서 정해지는 규칙과 실행 결과는 [18장 "Java 객체의 getter가 JSON 필드 이름을 정한다"](18_AJAX_REST.md) 절을 본다. 여기서는 중복하지 않는다.

### 9_ 자료 00·01 기준: 빌드 명령과 설정 (Maven 확인함, Gradle 미실행)

강의 자료 `00 maven과 gradle`과 `01 Spring Boot 시작하기`의 명령을 정리했다. Maven으로 `mvn package`를 실행해 jar를 만들고 서버를 띄운 것은 확인했다([22-2의 "실제 서버 실행" 절](22-2_폼_검증_서버실행.md)). Gradle 명령은 이 PC에서 실행하지 못해 미실행이다.

| 구분 | Maven (`pom.xml`) | Gradle (`build.gradle`) |
|---|---|---|
| 빌드 명령 | `./mvnw package` (Windows: `mvnw package`) | `./gradlew build` (Windows: `gradlew build`) |
| 결과 파일 | `target/<artifactId>-<version>.war` | `build/libs/<name>-<version>.war` |
| 설정 | `<packaging>war</packaging>`, `<version>0.0.1-SNAPSHOT</version>` | `version = '0.0.1-SNAPSHOT'` |
| 외부 설치 | 따로 설치하지 않아도 `mvnw`가 실행한다 | 따로 설치하지 않아도 `gradlew`가 실행한다 |

- `-plain.war`는 내장 톰캣이 없는 파일이다. 독립 톰캣에 배포할 때 쓴다. 일반 `.war`는 내장 톰캣이 있어서 `java -jar`로 바로 실행한다.
- 강의 설명: 옛 프로젝트는 대부분 Maven·MyBatis·JSP, 최근 프로젝트는 Gradle·JPA·Thymeleaf를 쓴다는 경향이다.
- 포트 충돌(`8080 port failed to start`)이 나면 `server.port=8088`처럼 `application.properties`에서 포트를 바꾼다. 설정 줄 끝에 공백이 있으면 실행 오류가 날 수 있다는 강의 주의도 있다.
- `pom.xml`에 빨간 오류가 뜨면 프로젝트 우클릭 → Maven → Update Project를 실행한다. Gradle은 Refresh Gradle Project를 실행한다.

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


## 4. 빌드 도구와 운영 구성 개념

- **Maven·Gradle:** 의존성 내려받기, 컴파일, 테스트, 패키징(jar)을 자동화하는 빌드 도구다. Maven은 `pom.xml`, Gradle은 `build.gradle`에 의존성을 적는다.
- **실행 가능한 jar:** Spring Boot는 빌드 결과 jar 하나에 내장 서버가 들어 있어서, `java -jar`로 바로 실행할 수 있다.
- **설정 분리:** 프로파일(`application-prod.yml`)과 환경 변수로 DB 주소와 비밀번호를 코드 밖에서 넣는다.
- **앞단 웹 서버:** 운영에서는 Nginx를 앞에 두고 80/443 요청을 앱의 8080 포트로 넘기는 구성을 흔히 쓴다(리버스 프록시). 앱을 여러 개 띄우고 Nginx가 나눠 보내면 로드밸런싱이 된다.
- **면접 기준:** "Maven/Gradle로 빌드하고 jar 하나로 배포하며, 설정은 환경 변수로 분리하고, 앞단에 Nginx를 둬서 요청을 앱으로 전달합니다."

