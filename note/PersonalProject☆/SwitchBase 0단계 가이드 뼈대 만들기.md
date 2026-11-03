# SwitchBase 0단계 가이드: 뼈대 만들기

Oct 12, 2026 · @Soon

이 가이드를 끝내면 세 가지가 돼 있다. `./gradlew build`가 테스트까지 통과하고, `http://localhost:8080`에 SwitchBase 첫 화면이 뜨고, GitHub에 올리면 Actions가 초록불이 된다. 각 단계 끝의 **확인**이 통과하면 커밋하고 다음으로 넘어간다.

## 준비물

| 도구 | 확인 방법 | 비고 |
| --- | --- | --- |
| JDK 25 | `java -version` | Temurin 25 추천. IntelliJ에서 바로 내려받을 수도 있다 |
| Docker Desktop | `docker version` | 로컬 DB와 테스트 DB가 모두 Docker로 돈다. 실행 중이어야 한다 |
| IntelliJ IDEA | - | Community 버전으로 충분하다 |
| Git + GitHub 계정 | `git --version` | 6단계에서 쓴다 |

Gradle은 따로 설치하지 않는다. 프로젝트에 들어있는 `gradlew`가 알아서 내려받는다.

## 1. Spring Initializr로 프로젝트 생성

[start.spring.io](https://start.spring.io)에서 아래처럼 고르고 GENERATE를 누른다. 의존성은 직접 타이핑하지 말고 여기서 고르는 게 안전하다. Spring Boot 4는 모듈 이름이 3과 달라져서, 인터넷 예제를 복사하면 안 맞는 경우가 많다.

| 항목 | 값 |
| --- | --- |
| Project | Gradle - Kotlin |
| Language | Java |
| Spring Boot | 4.x 중 가장 최신 정식 버전 (SNAPSHOT, M, RC 제외) |
| Group | com.switchbase |
| Artifact | switchbase |
| Package name | `com.switchbase` (기본값 `com.switchbase.switchbase`에서 직접 고침) |
| Packaging | Jar |
| Java | 25 |

**Dependencies (ADD DEPENDENCIES에서 검색)**

| 의존성 | 왜 필요한가 |
| --- | --- |
| Spring Web | 웹 서버와 컨트롤러 |
| Thymeleaf | 화면 템플릿 |
| Validation | 입력 검증 (`@NotBlank` 등) |
| Spring Data JPA | DB 접근 |
| PostgreSQL Driver | PostgreSQL 연결 |
| Flyway Migration | 스키마 변경 이력 |
| Docker Compose Support | 앱을 켜면 로컬 DB도 같이 켜짐 |
| Testcontainers | 테스트용 진짜 PostgreSQL |
| Spring Boot DevTools | 코드 수정 시 자동 재시작 |

내려받은 zip을 풀고 IntelliJ에서 폴더를 연다 (File → Open). Gradle 동기화가 끝날 때까지 기다린다.

폴더에서 같이 생긴 파일 몇 개를 눈여겨본다.

- `build.gradle.kts`: 의존성 목록. 앞으로 버전이나 이름이 헷갈리면 여기가 정답이다
- `compose.yaml`: PostgreSQL 컨테이너 설정 (Docker Compose Support 덕분에 자동 생성)
- `src/test/java/.../TestcontainersConfiguration.java`: 테스트용 PostgreSQL 설정 (Testcontainers 덕분에 자동 생성)

**확인**: 터미널에서 `./gradlew compileJava`가 BUILD SUCCESSFUL. (Windows는 `gradlew.bat compileJava`)

여기서 `git init` 후 첫 커밋을 해둔다. `.gitignore`는 이미 들어 있다.

## 2. 로컬 PostgreSQL과 설정

Docker Compose Support가 있으면 `bootRun`할 때 Spring이 `compose.yaml`을 보고 DB를 대신 띄우고, 접속 정보도 알아서 연결한다. 그래서 `spring.datasource.url`을 직접 쓸 필요가 없다.

자동 생성된 `compose.yaml`을 아래처럼 바꾼다. 바뀜 점은 세 가지다: 이미지 버전을 17로 고정, DB 이름 정리, 데이터가 남도록 볼륨 추가.

```yaml
services:
  postgres:
    image: 'postgres:17'
    environment:
      - 'POSTGRES_DB=switchbase'
      - 'POSTGRES_USER=switchbase'
      - 'POSTGRES_PASSWORD=switchbase'
    ports:
      - '5432:5432'
    volumes:
      - 'switchbase-db:/var/lib/postgresql/data'

volumes:
  switchbase-db:
```

`latest` 대신 버전을 고정하는 이유: 어느 날 이미지가 새 메이저 버전으로 바뀌면 기존 데이터가 안 열릴 수 있다. 테스트도 같은 17을 쓴다 (4단계).

이제 `src/main/resources/application.properties`를 지우고 같은 자리에 `application.yaml`을 만든다.

```yaml
spring:
  application:
    name: switchbase
  jpa:
    hibernate:
      ddl-auto: validate   # 테이블은 Flyway만 만든다. JPA는 맞는지 검사만
    open-in-view: false   # DB 연결을 화면 렌더링까지 끌고 가지 않게
  docker:
    compose:
      lifecycle-management: start-only   # 앱을 꺼도 DB는 계속 켜둠 (재시작이 빨라짐)
```

**확인**: Docker Desktop을 켜고 `./gradlew bootRun`. 로그에 PostgreSQL 컨테이너가 뜨고 `Started SwitchbaseApplication`이 나오면 성공이다. 아직 화면이 없으니 `localhost:8080`은 에러 페이지가 뜨는 게 정상이다.

## 3. Flyway 첫 마이그레이션

Flyway는 `src/main/resources/db/migration` 폴더의 `V번호__설명.sql` 파일을 번호 순서대로 한 번씩만 실행한다. 실행한 기록은 DB의 `flyway_schema_history` 테이블에 남는다. 이미 실행된 파일은 절대 고치지 말고, 바꿀 게 생기면 `V2__...`를 새로 만든다.

테이블 이름은 개발 계획의 `switch` 대신 `keyboard_switch`로 한다. Java에서 `switch`가 예약어라서, 나중에 엔티티 이름(`KeyboardSwitch`)과 맞추는 게 덜 헷갈린다.

`src/main/resources/db/migration/V1__create_keyboard_switch.sql`:

```sql
CREATE TABLE keyboard_switch (
    id                  BIGSERIAL    PRIMARY KEY,
    manufacturer        VARCHAR(100) NOT NULL,
    model_name          VARCHAR(150) NOT NULL,
    switch_type         VARCHAR(20)  NOT NULL,
    actuation_force_g   NUMERIC(5,1),
    bottom_out_force_g  NUMERIC(5,1),
    pre_travel_mm       NUMERIC(3,1),
    total_travel_mm     NUMERIC(3,1),
    release_year        SMALLINT,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uk_keyboard_switch_name UNIQUE (manufacturer, model_name),
    CONSTRAINT ck_keyboard_switch_type CHECK (switch_type IN ('LINEAR', 'TACTILE', 'CLICKY'))
);
```

컬럼은 일부러 적게 둔다. 재질, 규격, 판매 상태 같은 나머지는 1단계에서 `V2`로 추가하면서 마이그레이션을 쌓는 연습을 한다.

**확인**: 다시 `./gradlew bootRun`. 로그에 `Migrating schema "public" to version "1 - create keyboard switch"`가 나오면 성공이다. IntelliJ Database 탭이나 DBeaver로 `localhost:5432` (switchbase / switchbase)에 접속해 테이블을 눈으로 보는 것도 좋다.

## 4. Testcontainers 테스트

테스트는 H2 같은 가짜 DB 대신, 테스트할 때만 잠깐 뜨는 진짜 PostgreSQL에서 돈다. 로컬 DB와 분리돼 있어서 테스트가 내 데이터를 건드리지 않는다.

먼저 자동 생성된 `src/test/java/com/switchbase/TestcontainersConfiguration.java`를 열어 이미지 이름만 바꾼다. import는 생성된 그대로 둔다 (Testcontainers 버전마다 패키지가 다르다).

```java
// 변경 전
new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
// 변경 후: 로컬과 같은 버전
new PostgreSQLContainer(DockerImageName.parse("postgres:17"));
```

그다음 같은 폴더에 `SchemaMigrationTest.java`를 만든다. 마이그레이션이 테이블을 만들었는지, 제약 조건이 제대로 걸렸는지 두 가지를 확인한다.

```java
package com.switchbase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SchemaMigrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Flyway가 keyboard_switch 테이블을 만든다")
    void createsKeyboardSwitchTable() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM keyboard_switch", Integer.class);

        assertThat(count).isZero();
    }

    @Test
    @DisplayName("스위치 타입은 LINEAR, TACTILE, CLICKY만 허용한다")
    void rejectsUnknownSwitchType() {
        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO keyboard_switch (manufacturer, model_name, switch_type) VALUES (?, ?, ?)",
                "Gateron", "Test", "SILENT"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
```

**확인**: Docker Desktop을 켜고 `./gradlew test`. 처음에는 이미지를 내려받느라 조금 걸린다. 테스트 3개(자동 생성된 `contextLoads` 포함)가 모두 통과하면 성공이다. 결과는 `build/reports/tests/test/index.html`에서 볼 수 있다.

일부러 한 번 깨뜨려 보는 것도 좋다. 마이그레이션의 CHECK 제약을 잠깐 지우고 돌리면 두 번째 테스트가 실패한다. 확인하고 되돌린다.

## 5. Thymeleaf 기본 레이아웃과 에러 페이지

모든 페이지가 같이 쓸 머리(head), 상단, 하단을 조각(fragment)으로 만들고, 각 페이지에서 끼워 넣는다. 파일은 다섯 개다.

```
src/main/resources/
├─ static/css/main.css
└─ templates/
   ├─ fragments/layout.html
   ├─ index.html
   └─ error.html
src/main/java/com/switchbase/common/HomeController.java
```

`templates/fragments/layout.html`: 공통 조각. htmx는 여기서 한 번만 불러오면 모든 페이지에서 쓸 수 있다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:fragment="head(title)">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title th:text="|${title} - SwitchBase|">SwitchBase</title>
    <link rel="stylesheet" th:href="@{/css/main.css}">
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.4/dist/htmx.min.js" defer></script>
</head>
<body>
<header th:fragment="header" class="site-header">
    <a th:href="@{/}" class="logo">SwitchBase</a>
</header>
<footer th:fragment="footer" class="site-footer">
    <p>결정은 사람이, AI는 도구로.</p>
</footer>
</body>
</html>
```

`templates/index.html`: 첫 화면. 조각을 `th:replace`로 끼워 넣는다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('홈')}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main>
    <h1>SwitchBase</h1>
    <p>Mechanical Keyboard Switches, structured by data and taste.</p>
</main>
<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

`templates/error.html`: 이 이름으로 두면 Spring Boot가 에러가 날 때 알아서 이 페이지를 보여준다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('오류')}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main>
    <h1 th:text="${status}">404</h1>
    <p>페이지를 찾을 수 없거나 문제가 생겼어요.</p>
    <a th:href="@{/}">홈으로</a>
</main>
</body>
</html>
```

`common/HomeController.java`:

```java
package com.switchbase.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    @GetMapping("/")
    String home() {
        return "index";   // templates/index.html
    }
}
```

`static/css/main.css`는 일단 아주 짧게 시작한다. 디자인은 나중에 얼마든지 바꾼다.

```css
body { max-width: 960px; margin: 0 auto; padding: 0 16px; font-family: system-ui, sans-serif; }
.site-header { padding: 16px 0; border-bottom: 1px solid #ddd; }
.logo { font-weight: 700; text-decoration: none; color: inherit; }
.site-footer { margin-top: 48px; color: #888; font-size: 14px; }
```

**확인**: `./gradlew bootRun` 후 `localhost:8080`에 첫 화면이 뜨고, `localhost:8080/nothing`에서 내가 만든 404 페이지가 뜨면 성공이다. 브라우저 개발자 도구 콘솔에서 `htmx`를 입력했을 때 객체가 나오면 htmx도 잘 불러온 것이다.

## 6. GitHub 저장소와 Actions

GitHub에 빈 저장소 `switchbase`를 만든다 (README, .gitignore 없이. 포트폴리오용이니 Public 추천). 그다음 프로젝트에 CI 설정 파일을 추가한다.

`.github/workflows/ci.yml`:

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '25'
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew build
```

GitHub의 ubuntu 러너에는 Docker가 있어서 Testcontainers 테스트가 그대로 돈다.

README도 한 줄이라도 넣어둔다. 포트폴리오의 첫인상이다.

```markdown
# SwitchBase

Mechanical Keyboard Switches, structured by data and taste.

> 모든 결정과 판단은 사람이 하고, AI는 활용할 뿐이다.

## 실행

Docker Desktop을 켜고 `./gradlew bootRun`
```

올리기:

```bash
git add .
git commit -m "0단계: 프로젝트 뼈대"
git branch -M main
git remote add origin https://github.com/<내아이디>/switchbase.git
git push -u origin main
```

**확인**: GitHub 저장소의 Actions 탭에서 CI가 초록색 체크가 되면 0단계 끝!

## 막혔을 때

| 증상 | 원인과 해결 |
| --- | --- |
| `Could not find a valid Docker environment` | Docker Desktop이 꺼져 있다. 켜고 다시 실행 |
| `Port 5432 is already allocated` / 포트 충돌 | PC에 PostgreSQL이 따로 설치돼 돌고 있다. 그걸 끄거나 `compose.yaml`의 포트를 `'5433:5432'`로 바꾼다 |
| `Schema validation: missing table` | JPA 엔티티는 있는데 마이그레이션이 없다. 테이블을 만드는 `V번호__...sql`을 추가 |
| `Validate failed: Migrations have failed validation` | 이미 실행된 마이그레이션 파일을 고쳤다. 되돌리고 새 번호 파일로. 0단계에서는 `docker compose down -v`로 DB를 통째로 지워도 된다 |
| CI에서 `./gradlew: Permission denied` | Windows에서 올리면 실행 권한이 빠진다. `git update-index --chmod=+x gradlew` 후 커밋 |
| `Unsupported class file major version` / Java 버전 오류 | IntelliJ의 Gradle JVM이 25가 아니다. Settings → Build Tools → Gradle → Gradle JVM을 25로 |
| 한글이 깨져 보임 | 템플릿 파일 인코딩을 UTF-8로 (IntelliJ 오른쪽 아래) |
| Port 8080 was already in use | Oracle XE 등 다른 프로그램이 8080을 쓰고 있다. application.yaml에 server.port: 8081을 추가하고 localhost:8081로 접속 |

그래도 안 되면 에러 메시지 전체를 이 스레드에 붙여넣어 주세요. 다음은 1단계 가이드(스위치 엔티티, 목록/상세 페이지)다.
