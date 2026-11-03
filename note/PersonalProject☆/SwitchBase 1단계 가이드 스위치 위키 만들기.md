# SwitchBase 1단계 가이드: 스위치 위키 만들기

Oct 22, 2026 · @Soon

## 개요

1단계가 끝나면 인터넷 주소 하나로 스위치 50개 이상을 보고, 걸러 보고, 관리자가 고칠 수 있다. 고칠 때마다 이전 내용이 이력으로 남아서, 2단계에서 "누구나 고치는 위키"로 넓힐 바닥이 된다.

**완료 기준**

- AWS 주소에서 스위치 목록, 상세, 필터가 동작한다
- `/admin`은 로그인한 관리자만 들어가고, 수정하면 `switch_revision`에 한 줄이 쌓인다
- 스위치 데이터 50개 이상이 CSV로 들어가 있다
- GitHub Actions가 초록색이다

**순서**: 각 단계 끝의 **확인**을 통과하면 커밋하고 다음으로 넘어간다. 0단계와 같은 방식이다.

1. 1-1 DB 테이블 늘리기 (V2 마이그레이션)
2. 1-2 엔티티와 리포지토리
3. 1-3 목록/상세 페이지
4. 1-4 검색 필터 (QueryDSL + htmx)
5. 1-5 관리자 등록/수정 (Spring Security + 이력)
6. 1-6 CSV로 스위치 넣기
7. 1-7 AWS 첫 배포 (Lightsail)

**이번 단계의 약속**

- 패키지 이름은 `com.switchbase.keyswitch`다. `switch`는 자바 예약어라 패키지 이름으로 못 쓴다.
- 엔티티는 0단계의 `ddl-auto: validate` 그대로 간다. 테이블은 항상 Flyway SQL로 먼저 만들고, 엔티티는 거기에 맞춘다.
- Lombok 없이 간다. getter를 직접 쓰는 게 처음엔 번거롭지만 무엇이 열려 있는지 눈에 보인다.
- 코드는 모두 이 가이드를 쓰면서 실제로 빌드하고 테스트를 돌려 확인했다.

## 1-1. DB 테이블 늘리기 (V2 마이그레이션)

위키에 필요한 테이블 두 개를 더하고, 기존 `keyboard_switch`에 칸 두 개를 붙인다.

- `switch_revision`: 저장할 때마다 그 순간의 스펙을 통째로 한 줄씩 남긴다. 위키의 "역사 보기"다.
- `spec_source`: 숫자를 어디서 가져왔는지. 출처 없는 숫자는 믿기 어렵다.
- `description`: 스위치 설명 글.
- `version`: 두 사람이 동시에 고칠 때 나중 사람 것이 앞 사람 것을 모르고 덮어쓰는 걸 막는 번호표다.

**규칙 하나**: 이미 적용된 `V1__...sql`은 절대 고치지 않는다. 바꾸고 싶으면 항상 새 번호 파일을 만든다. Flyway가 파일마다 체크섬을 기억하고 있어서, V1을 고치면 앱이 아예 안 켜진다.

`src/main/resources/db/migration/V2__add_revision_and_source.sql`:

```sql
ALTER TABLE keyboard_switch
    ADD COLUMN description TEXT,
    ADD COLUMN version     BIGINT NOT NULL DEFAULT 0;

CREATE INDEX ix_keyboard_switch_type ON keyboard_switch (switch_type);

CREATE TABLE switch_revision (
    id                  BIGSERIAL    PRIMARY KEY,
    switch_id           BIGINT       NOT NULL REFERENCES keyboard_switch (id) ON DELETE CASCADE,
    revision_no         INT          NOT NULL,
    manufacturer        VARCHAR(100) NOT NULL,
    model_name          VARCHAR(150) NOT NULL,
    switch_type         VARCHAR(20)  NOT NULL,
    actuation_force_g   NUMERIC(5,1),
    bottom_out_force_g  NUMERIC(5,1),
    pre_travel_mm       NUMERIC(3,1),
    total_travel_mm     NUMERIC(3,1),
    release_year        SMALLINT,
    description         TEXT,
    edit_summary        VARCHAR(200) NOT NULL,
    editor              VARCHAR(50)  NOT NULL,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uk_switch_revision_no UNIQUE (switch_id, revision_no)
);

CREATE TABLE spec_source (
    id          BIGSERIAL    PRIMARY KEY,
    switch_id   BIGINT       NOT NULL REFERENCES keyboard_switch (id) ON DELETE CASCADE,
    source_type VARCHAR(20)  NOT NULL,
    url         VARCHAR(500) NOT NULL,
    note        VARCHAR(200),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_spec_source_type CHECK (source_type IN ('OFFICIAL', 'RETAILER', 'REVIEW', 'COMMUNITY'))
);

CREATE INDEX ix_spec_source_switch ON spec_source (switch_id);
```

왜 이력을 JSON 한 칸이 아니라 스펙 칸을 그대로 복사해 두나: SQL로 "입력압이 언제 바뀌었나"를 바로 조회할 수 있고, 다음 단계에서 엔티티를 그대로 재사용할 수 있다. 면접에서 설명하기도 쉽다.

**확인**: 아직 엔티티가 없으니 앱을 켜도 된다. `./gradlew bootRun` 로그에 `now at version v2`가 보이면 성공이다. IntelliJ Database 창이나 `docker exec`로 `\dt`를 보면 테이블이 네 개(`flyway_schema_history` 포함)다.

```
docker exec -it switchbase-postgres-1 psql -U switchbase -c "\dt"
```

컨테이너 이름이 다르면 `docker ps`로 확인한다.

커밋: `feat(db): add switch revision and spec source tables`

## 1-2. 엔티티와 리포지토리

테이블 세 개에 맞춘 엔티티 세 개와 enum 두 개를 만든다. 핵심은 `SwitchSpec`이다. 스펙 칸 아홉 개를 record 하나로 묶어서 `KeyboardSwitch`와 `SwitchRevision`이 똑같이 끼워 쓴다(`@Embeddable`). 칸이 늘어도 한 곳만 고치면 된다.

```
src/main/java/com/switchbase/keyswitch/
├─ SwitchType.java
├─ SourceType.java
├─ SwitchSpec.java
├─ KeyboardSwitch.java
├─ SwitchRevision.java
├─ SpecSource.java
├─ KeyboardSwitchRepository.java
├─ SwitchRevisionRepository.java
└─ SpecSourceRepository.java
```

`SwitchType.java`: 화면에는 한글 이름을, DB에는 영문 상수를 쓴다.

```java
package com.switchbase.keyswitch;

public enum SwitchType {
    LINEAR("리니어"),
    TACTILE("택타일"),
    CLICKY("클리키");

    private final String label;

    SwitchType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
```

`SourceType.java`:

```java
package com.switchbase.keyswitch;

public enum SourceType {
    OFFICIAL("제조사 공식"),
    RETAILER("판매처"),
    REVIEW("리뷰"),
    COMMUNITY("커뮤니티");

    private final String label;

    SourceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
```

`SwitchSpec.java`: `@Column` 이름이 SQL의 칸 이름과 글자 하나까지 같아야 한다. `release_year`가 `SMALLINT`라서 자바도 `Short`를 쓴다. `Integer`로 쓰면 validate가 "타입이 다르다"며 앱을 멈춘다.

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

/**
 * 스위치의 "고칠 수 있는 정보" 묶음.
 * keyboard_switch와 switch_revision이 똑같이 이 묶음을 쓴다.
 */
@Embeddable
public record SwitchSpec(
        @Column(name = "manufacturer", nullable = false, length = 100)
        String manufacturer,

        @Column(name = "model_name", nullable = false, length = 150)
        String modelName,

        @Enumerated(EnumType.STRING)
        @Column(name = "switch_type", nullable = false, length = 20)
        SwitchType switchType,

        @Column(name = "actuation_force_g", precision = 5, scale = 1)
        BigDecimal actuationForceG,

        @Column(name = "bottom_out_force_g", precision = 5, scale = 1)
        BigDecimal bottomOutForceG,

        @Column(name = "pre_travel_mm", precision = 3, scale = 1)
        BigDecimal preTravelMm,

        @Column(name = "total_travel_mm", precision = 3, scale = 1)
        BigDecimal totalTravelMm,

        @Column(name = "release_year")
        Short releaseYear,

        @Column(name = "description", columnDefinition = "text")
        String description
) {
}
```

`KeyboardSwitch.java`: setter 대신 `changeSpec()` 하나만 연다. "스펙을 통째로 바꾼다"는 뜻이 이름에 드러난다.

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;

@Entity
@Table(name = "keyboard_switch")
public class KeyboardSwitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SwitchSpec spec;

    @Version
    private long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected KeyboardSwitch() {
        // JPA용
    }

    public KeyboardSwitch(SwitchSpec spec) {
        this.spec = spec;
    }

    public void changeSpec(SwitchSpec spec) {
        this.spec = spec;
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public SwitchSpec getSpec() { return spec; }
    public long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
```

`SwitchRevision.java`: 만들 때 스위치의 현재 스펙을 그대로 복사해 둔다. 이후에 스위치가 바뀌어도 이 줄은 안 바뀐다.

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/** 스위치를 저장할 때마다 그 순간의 스펙을 통째로 한 줄 남긴다. */
@Entity
@Table(name = "switch_revision")
public class SwitchRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "switch_id")
    private KeyboardSwitch keyboardSwitch;

    @Column(name = "revision_no", nullable = false)
    private int revisionNo;

    @Embedded
    private SwitchSpec spec;

    @Column(name = "edit_summary", nullable = false, length = 200)
    private String editSummary;

    @Column(name = "editor", nullable = false, length = 50)
    private String editor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected SwitchRevision() {
    }

    public SwitchRevision(KeyboardSwitch keyboardSwitch, int revisionNo, String editSummary, String editor) {
        this.keyboardSwitch = keyboardSwitch;
        this.revisionNo = revisionNo;
        this.spec = keyboardSwitch.getSpec();
        this.editSummary = editSummary;
        this.editor = editor;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public int getRevisionNo() { return revisionNo; }
    public SwitchSpec getSpec() { return spec; }
    public String getEditSummary() { return editSummary; }
    public String getEditor() { return editor; }
    public Instant getCreatedAt() { return createdAt; }
}
```

`SpecSource.java`:

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/** 스펙 숫자를 어디서 가져왔는지. 위키의 "출처" 칸. */
@Entity
@Table(name = "spec_source")
public class SpecSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "switch_id")
    private KeyboardSwitch keyboardSwitch;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "note", length = 200)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected SpecSource() {
    }

    public SpecSource(KeyboardSwitch keyboardSwitch, SourceType sourceType, String url, String note) {
        this.keyboardSwitch = keyboardSwitch;
        this.sourceType = sourceType;
        this.url = url;
        this.note = note;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public SourceType getSourceType() { return sourceType; }
    public String getUrl() { return url; }
    public String getNote() { return note; }
}
```

리포지토리 세 개. 메서드 이름만으로 Spring Data가 쿼리를 만들어 준다. `SpecManufacturer`처럼 `spec.` 안의 칸은 이름을 이어 붙여 부른다.

```java
package com.switchbase.keyswitch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyboardSwitchRepository extends JpaRepository<KeyboardSwitch, Long> {

    boolean existsBySpecManufacturerAndSpecModelName(String manufacturer, String modelName);
}
```

```java
package com.switchbase.keyswitch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwitchRevisionRepository extends JpaRepository<SwitchRevision, Long> {

    List<SwitchRevision> findByKeyboardSwitchIdOrderByRevisionNoDesc(Long switchId);

    int countByKeyboardSwitchId(Long switchId);
}
```

```java
package com.switchbase.keyswitch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecSourceRepository extends JpaRepository<SpecSource, Long> {

    List<SpecSource> findByKeyboardSwitchIdOrderByIdAsc(Long switchId);
}
```

**확인**: `./gradlew bootRun`이 에러 없이 뜨면 엔티티와 테이블이 딱 맞는다는 뜻이다. `ddl-auto: validate`가 앱이 켜질 때 칸 이름과 타입을 하나씩 대조해 주기 때문이다. `Schema validation: missing column`이나 `wrong column type encountered in column [...]`가 나오면 대괄호 안의 칸 이름을 보고 그 `@Column`을 SQL과 비교한다.

커밋: `feat(switch): add switch entities and repositories`

## 1-3. 목록/상세 페이지

`/switches`에서 목록을, `/switches/1`에서 상세(스펙, 출처, 수정 이력)를 보여준다. 검색은 다음 단계에서 붙이고, 여기서는 페이지 나누기까지만 한다.

```
src/main/java/com/switchbase/
├─ keyswitch/SwitchService.java
├─ keyswitch/SwitchController.java
└─ common/GlobalExceptionHandler.java
src/main/resources/templates/switches/
├─ list.html
└─ detail.html
```

`SwitchService.java`: 컨트롤러는 리포지토리를 직접 부르지 않고 서비스만 부른다. 클래스에 `readOnly = true`를 걸어 두고, 쓰기 메서드에만 따로 `@Transactional`을 붙인다(1-5).

```java
package com.switchbase.keyswitch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class SwitchService {

    private final KeyboardSwitchRepository switches;
    private final SwitchRevisionRepository revisions;
    private final SpecSourceRepository sources;

    public SwitchService(KeyboardSwitchRepository switches,
                         SwitchRevisionRepository revisions,
                         SpecSourceRepository sources) {
        this.switches = switches;
        this.revisions = revisions;
        this.sources = sources;
    }

    public Page<KeyboardSwitch> list(Pageable pageable) {
        return switches.findAll(pageable);
    }

    public KeyboardSwitch get(Long id) {
        return switches.findById(id)
                .orElseThrow(() -> new NoSuchElementException("스위치가 없습니다: " + id));
    }

    public List<SwitchRevision> revisionsOf(Long id) {
        return revisions.findByKeyboardSwitchIdOrderByRevisionNoDesc(id);
    }

    public List<SpecSource> sourcesOf(Long id) {
        return sources.findByKeyboardSwitchIdOrderByIdAsc(id);
    }
}
```

`SwitchController.java`: `?page=1`처럼 오는 값을 Spring이 `Pageable`로 바꿔 준다. 기본은 20개씩, 제조사·이름 순이다.

```java
package com.switchbase.keyswitch;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/switches")
class SwitchController {

    private final SwitchService switchService;

    SwitchController(SwitchService switchService) {
        this.switchService = switchService;
    }

    @GetMapping
    String list(@PageableDefault(size = 20, sort = {"spec.manufacturer", "spec.modelName"}) Pageable pageable,
                Model model) {
        model.addAttribute("page", switchService.list(pageable));
        return "switches/list";
    }

    @GetMapping("/{id}")
    String detail(@PathVariable Long id, Model model) {
        model.addAttribute("sw", switchService.get(id));
        model.addAttribute("sources", switchService.sourcesOf(id));
        model.addAttribute("revisions", switchService.revisionsOf(id));
        return "switches/detail";
    }
}
```

`common/GlobalExceptionHandler.java`: 없는 번호(`/switches/999`)로 들어오면 500 에러 대신 0단계에서 만든 에러 페이지를 404로 보여준다.

```java
package com.switchbase.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@ControllerAdvice
class GlobalExceptionHandler {

    // 없는 스위치 번호로 들어오면 500 대신 404 페이지
    @ExceptionHandler(NoSuchElementException.class)
    ModelAndView notFound() {
        ModelAndView mav = new ModelAndView("error", HttpStatus.NOT_FOUND);
        mav.addObject("status", 404);
        return mav;
    }
}
```

`templates/switches/list.html`: `th:fragment="results"`는 지금은 안 쓰지만 1-4에서 htmx가 이 부분만 갈아 끼울 때 쓴다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('스위치 목록')}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main>
    <h1>스위치</h1>

    <section id="results" th:fragment="results">
        <p class="muted" th:text="|${page.totalElements}개|">0개</p>
        <table class="switch-table" th:unless="${page.empty}">
            <thead>
            <tr><th>제조사</th><th>이름</th><th>종류</th><th>입력압</th><th>키 깊이</th></tr>
            </thead>
            <tbody>
            <tr th:each="sw : ${page.content}" th:with="s=${sw.spec}">
                <td th:text="${s.manufacturer}">Gateron</td>
                <td><a th:href="@{/switches/{id}(id=${sw.id})}" th:text="${s.modelName}">Yellow</a></td>
                <td th:text="${s.switchType.label}">리니어</td>
                <td th:text="${s.actuationForceG} ? |${s.actuationForceG}g| : '-'">50g</td>
                <td th:text="${s.totalTravelMm} ? |${s.totalTravelMm}mm| : '-'">4.0mm</td>
            </tr>
            </tbody>
        </table>
        <p th:if="${page.empty}">아직 스위치가 없어요.</p>

        <nav class="pager" th:if="${page.totalPages > 1}">
            <a th:if="${page.hasPrevious()}" th:href="@{/switches(page=${page.number - 1})}">이전</a>
            <span th:text="|${page.number + 1} / ${page.totalPages}|">1 / 3</span>
            <a th:if="${page.hasNext()}" th:href="@{/switches(page=${page.number + 1})}">다음</a>
        </nav>
    </section>
</main>
<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

`templates/switches/detail.html`: 수정 이력은 최신이 위로 온다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head(${sw.spec.modelName})}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main th:with="s=${sw.spec}">
    <p class="muted" th:text="${s.manufacturer}">Gateron</p>
    <h1 th:text="${s.modelName}">Yellow</h1>

    <table class="spec-table">
        <tr><th>종류</th><td th:text="${s.switchType.label}">리니어</td></tr>
        <tr><th>입력압</th><td th:text="${s.actuationForceG} ? |${s.actuationForceG}g| : '-'"></td></tr>
        <tr><th>바닥압</th><td th:text="${s.bottomOutForceG} ? |${s.bottomOutForceG}g| : '-'"></td></tr>
        <tr><th>입력 지점</th><td th:text="${s.preTravelMm} ? |${s.preTravelMm}mm| : '-'"></td></tr>
        <tr><th>키 깊이</th><td th:text="${s.totalTravelMm} ? |${s.totalTravelMm}mm| : '-'"></td></tr>
        <tr><th>출시</th><td th:text="${s.releaseYear} ?: '-'"></td></tr>
    </table>

    <p th:if="${s.description}" th:text="${s.description}"></p>

    <h2>출처</h2>
    <ul th:unless="${sources.empty}">
        <li th:each="src : ${sources}">
            <span th:text="${src.sourceType.label}">제조사 공식</span>
            <a th:href="${src.url}" th:text="${src.url}" rel="nofollow noopener" target="_blank"></a>
        </li>
    </ul>
    <p th:if="${sources.empty}" class="muted">아직 출처가 없어요.</p>

    <h2>수정 이력</h2>
    <ol class="revisions">
        <li th:each="r : ${revisions}">
            <strong th:text="|#${r.revisionNo}|">#1</strong>
            <span th:text="${#temporals.format(r.createdAt, 'yyyy-MM-dd HH:mm')}"></span>
            <span th:text="${r.editor}">admin</span>
            <span th:text="${r.editSummary}">처음 등록</span>
        </li>
    </ol>
</main>
<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

메뉴에서 들어갈 수 있게 `fragments/layout.html`의 header에 링크를 하나 더하고, CSS를 늘린다.

```html
<header th:fragment="header" class="site-header">
    <a th:href="@{/}" class="logo">SwitchBase</a>
    <nav>
        <a th:href="@{/switches}">스위치</a>
    </nav>
</header>
```

`static/css/main.css` 전체를 이걸로 바꾼다. 뒷부분은 다음 단계들에서 쓸 스타일까지 미리 넣어 둔 것이다.

```css
body { max-width: 960px; margin: 0 auto; padding: 0 16px; font-family: system-ui, sans-serif; }
.site-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 0; border-bottom: 1px solid #ddd; }
.site-header nav { display: flex; gap: 16px; align-items: center; }
.logo { font-weight: 700; text-decoration: none; color: inherit; }
.site-footer { margin-top: 48px; color: #888; font-size: 14px; }
.muted { color: #888; }
.error { color: #c0392b; margin: 0; }
.inline { display: inline; }
.filters { display: flex; flex-wrap: wrap; gap: 8px; margin: 16px 0; }
.filters input[type=number] { width: 90px; }
.switch-table, .spec-table { width: 100%; border-collapse: collapse; }
.switch-table th, .switch-table td, .spec-table th, .spec-table td { padding: 8px; border-bottom: 1px solid #eee; text-align: left; }
.spec-table th { width: 120px; color: #555; font-weight: 500; }
.pager { display: flex; gap: 16px; justify-content: center; margin: 16px 0; }
.switch-form { display: grid; gap: 8px; max-width: 480px; }
.switch-form label { display: grid; gap: 4px; }
```

**확인**: 아직 등록 화면이 없으니 IntelliJ Database 창(또는 `docker exec ... psql`)에서 직접 한 줄 넣어 본다.

```sql
INSERT INTO keyboard_switch (manufacturer, model_name, switch_type, actuation_force_g, total_travel_mm)
VALUES ('Gateron', 'Yellow', 'LINEAR', 50, 4.0);
```

`localhost:8090/switches`에 한 줄이 보이고, 이름을 누르면 상세가 뜨면 성공이다. `/switches/999`는 404 페이지가 떠야 한다. SQL로 넣은 줄이라 수정 이력은 비어 있는 게 정상이다.

커밋: `v`

## 1-4. 검색 필터 (QueryDSL + htmx)

이름 검색, 종류, 입력압 범위를 골라서 걸러 본다. 칸을 바꾸면 버튼을 안 눌러도 0.3초 뒤 표만 바뀐다. 조건이 몇 개 들어올지 모르는 검색은 메서드 이름 쿼리로는 감당이 안 돼서 QueryDSL을 쓴다.

### 의존성 추가

`build.gradle.kts`의 `dependencies { }` 안에 세 줄을 더한다. 원래 QueryDSL(`com.querydsl` 5.x)은 개발이 멈춰서 Spring Boot 4와 함께 쓸 때는 OpenFeign이 이어서 관리하는 판을 쓴다. 패키지 이름은 똑같이 `com.querydsl`이라 인터넷 예제도 그대로 통한다.

```kotlin
implementation("io.github.openfeign.querydsl:querydsl-jpa:7.7")
annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:7.7:jpa")
annotationProcessor("jakarta.persistence:jakarta.persistence-api")
```

IntelliJ 오른쪽 위의 코끼리 버튼(Load Gradle Changes)을 누른 뒤 `./gradlew compileJava`를 한 번 돌리면 `build/generated/sources/annotationProcessor/java/main/com/switchbase/keyswitch/` 아래에 `QKeyboardSwitch`, `QSwitchSpec` 같은 Q클래스가 생긴다. 엔티티를 보고 자동으로 만든 파일이라 직접 고치지도, 커밋하지도 않는다(`build/`는 이미 `.gitignore`에 있다).

### 코드

```
src/main/java/com/switchbase/
├─ common/QuerydslConfig.java
├─ keyswitch/SwitchSearch.java
└─ keyswitch/SwitchQueryRepository.java
```

`common/QuerydslConfig.java`:

```java
package com.switchbase.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QuerydslConfig {

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
```

`keyswitch/SwitchSearch.java`: 검색 칸 네 개. 주소창의 `?keyword=gat&type=LINEAR`를 Spring이 이 record에 채워 준다.

```java
package com.switchbase.keyswitch;

import java.math.BigDecimal;

/** 목록 화면의 검색 조건. 비어 있는 칸은 조건에서 빠진다. */
public record SwitchSearch(
        String keyword,
        SwitchType type,
        BigDecimal minForce,
        BigDecimal maxForce
) {
}
```

`keyswitch/SwitchQueryRepository.java`: 핵심은 조건 메서드가 값이 없으면 `null`을 돌려주는 것이다. QueryDSL은 `where()` 안의 `null`을 그냥 빼 주어서 `if`문을 쌓지 않아도 된다.

```java
package com.switchbase.keyswitch;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.switchbase.keyswitch.QKeyboardSwitch.keyboardSwitch;

@Repository
public class SwitchQueryRepository {

    private final JPAQueryFactory query;

    public SwitchQueryRepository(JPAQueryFactory query) {
        this.query = query;
    }

    public Page<KeyboardSwitch> search(SwitchSearch search, Pageable pageable) {
        BooleanExpression[] conditions = {
                keywordContains(search.keyword()),
                typeEq(search.type()),
                forceGoe(search.minForce()),
                forceLoe(search.maxForce())
        };

        List<KeyboardSwitch> content = query
                .selectFrom(keyboardSwitch)
                .where(conditions)
                .orderBy(keyboardSwitch.spec.manufacturer.asc(), keyboardSwitch.spec.modelName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(keyboardSwitch.count())
                .from(keyboardSwitch)
                .where(conditions)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    // 조건이 비어 있으면 null을 돌려준다. QueryDSL은 where 안의 null을 무시한다.
    private BooleanExpression keywordContains(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return keyboardSwitch.spec.manufacturer.containsIgnoreCase(keyword)
                .or(keyboardSwitch.spec.modelName.containsIgnoreCase(keyword));
    }

    private BooleanExpression typeEq(SwitchType type) {
        return type == null ? null : keyboardSwitch.spec.switchType.eq(type);
    }

    private BooleanExpression forceGoe(BigDecimal min) {
        return min == null ? null : keyboardSwitch.spec.actuationForceG.goe(min);
    }

    private BooleanExpression forceLoe(BigDecimal max) {
        return max == null ? null : keyboardSwitch.spec.actuationForceG.loe(max);
    }
}
```

### 서비스와 컨트롤러 고치기

`SwitchService`에 `SwitchQueryRepository`를 필드와 생성자 매개변수로 추가하고(`switchQuery`라는 이름으로), `list()`를 지우고 이 메서드를 넣는다. 전체 모양은 1-5에 한 번에 있다.

```java
public Page<KeyboardSwitch> search(SwitchSearch search, Pageable pageable) {
    return switchQuery.search(search, pageable);
}
```

`SwitchController`의 `list()`를 통째로 바꾼다. htmx가 보낸 요청에는 `HX-Request` 헤더가 붙어 있어서, 그때는 페이지 전체 대신 `results` 조각만 돌려준다. 정렬은 이제 QueryDSL이 하므로 `sort`는 빠진다.

```java
@GetMapping
String list(SwitchSearch search,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestHeader(value = "HX-Request", required = false) String htmx,
            Model model) {
    Page<KeyboardSwitch> page = switchService.search(search, pageable);
    model.addAttribute("search", search);
    model.addAttribute("page", page);
    model.addAttribute("types", SwitchType.values());

    // htmx가 보낸 요청이면 결과 표 부분만, 아니면 페이지 전체
    return htmx != null ? "switches/list :: results" : "switches/list";
}
```

import에 `org.springframework.data.domain.Page`와 `org.springframework.web.bind.annotation.RequestHeader`가 늘어난다. IntelliJ에서 빨간 글자에 Alt+Enter를 누르면 된다.

### 화면 고치기

`switches/list.html`의 `<h1>스위치</h1>` 바로 아래에 검색 폼을 넣는다.

```html
<!-- 입력이 바뀔 때마다 htmx가 GET /switches 를 보내고, #results 안만 갈아 끼운다 -->
<form class="filters" th:action="@{/switches}" method="get"
      hx-get="/switches" hx-target="#results" hx-swap="outerHTML" hx-push-url="true"
      hx-trigger="input changed delay:300ms, change">
    <input type="search" name="keyword" th:value="${search.keyword}" placeholder="제조사나 이름">
    <select name="type">
        <option value="">모든 종류</option>
        <option th:each="t : ${types}" th:value="${t}" th:text="${t.label}"
                th:selected="${t == search.type}"></option>
    </select>
    <input type="number" name="minForce" th:value="${search.minForce}" placeholder="최소 g" step="1">
    <input type="number" name="maxForce" th:value="${search.maxForce}" placeholder="최대 g" step="1">
    <noscript><button type="submit">검색</button></noscript>
</form>
```

그리고 `<nav class="pager">` 블록을 이걸로 바꾼다. 다음 페이지로 넘어가도 검색 조건이 유지되고, `hx-boost` 덕분에 표만 바뀐다. 빈 화면 문구도 `조건에 맞는 스위치가 없어요.`로 바꾸면 자연스럽다.

```html
<nav class="pager" th:if="${page.totalPages > 1}">
    <a th:if="${page.hasPrevious()}"
       th:href="@{/switches(keyword=${search.keyword}, type=${search.type}, minForce=${search.minForce}, maxForce=${search.maxForce}, page=${page.number - 1})}"
       hx-boost="true" hx-target="#results" hx-select="#results" hx-swap="outerHTML">이전</a>
    <span th:text="|${page.number + 1} / ${page.totalPages}|">1 / 3</span>
    <a th:if="${page.hasNext()}"
       th:href="@{/switches(keyword=${search.keyword}, type=${search.type}, minForce=${search.minForce}, maxForce=${search.maxForce}, page=${page.number + 1})}"
       hx-boost="true" hx-target="#results" hx-select="#results" hx-swap="outerHTML">다음</a>
</nav>
```

**확인**: `/switches`에서 검색창에 `gat`를 치면 새로고침 없이 표가 바뀌고, 주소창도 `?keyword=gat...`로 바뀐다. 그 주소를 복사해 새 탭에 붙여도 같은 결과가 나오면 성공이다. 개발자 도구 Network 탭을 보면 요청마다 `<section>` 조각만 오는 것도 보인다.

커밋: `*feat(switch): add switch search filters with QueryDSL*`

## 1-5. 관리자 등록/수정 (Spring Security + 이력)

`/admin/**`은 관리자 로그인을 해야만 들어갈 수 있고, 저장할 때마다 `switch_revision`에 한 줄이 쌓인다. 회원가입은 2단계에서 하고, 지금은 설정 파일에 적은 관리자 계정 하나만 쓴다.

### 의존성과 설정

`build.gradle.kts`에 추가한다. 두 번째 줄은 화면에서 "관리자일 때만 보여주기"를 할 때 쓴다.

```kotlin
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
testImplementation("org.springframework.boot:spring-boot-starter-security-test")
```

`application.yaml`의 `spring:` 아래(예를 들어 `jpa:`와 같은 줄 높이)에 추가한다. `${ADMIN_PASSWORD:admin1234}`는 "환경 변수 `ADMIN_PASSWORD`가 있으면 그 값, 없으면 `admin1234`"라는 뜻이다. 내 컴퓨터에서는 기본값을 쓰고, **서버에서는 반드시 환경 변수로 바꾼다**(1-7).

```yaml
  security:
    user:
      name: ${ADMIN_USERNAME:admin}
      password: ${ADMIN_PASSWORD:admin1234}
      roles: ADMIN
```

`common/SecurityConfig.java`: 관리자 주소만 잠그고 나머지는 누구나 본다. 로그인 화면은 Spring Security가 기본으로 주는 `/login`을 그대로 쓴다.

```java
package com.switchbase.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }
}
```

### 입력 폼과 검증

`keyswitch/SwitchForm.java`: 화면에서 들어오는 값을 받는 그릇이다. 엔티티에 바로 받지 않는 이유는, 화면에는 "무엇을 바꾨나요"처럼 DB 스펙에는 없는 칸이 있고, 검증 규칙도 화면 사정에 따라 바뀌기 때문이다. Thymeleaf의 `th:field`가 getter/setter를 찾아서 쓰므로 record가 아니라 클래스로 만든다. getter/setter는 IntelliJ에서 Alt+Insert → Getter and Setter로 한 번에 만들면 된다.

```java
package com.switchbase.keyswitch;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** 관리자 등록/수정 화면의 입력값. 검증 규칙은 여기에 모은다. */
public class SwitchForm {

    @NotBlank @Size(max = 100)
    private String manufacturer;

    @NotBlank @Size(max = 150)
    private String modelName;

    @NotNull
    private SwitchType switchType;

    @DecimalMin("10.0") @DecimalMax("200.0")
    private BigDecimal actuationForceG;

    @DecimalMin("10.0") @DecimalMax("200.0")
    private BigDecimal bottomOutForceG;

    @DecimalMin("0.1") @DecimalMax("9.9")
    private BigDecimal preTravelMm;

    @DecimalMin("0.1") @DecimalMax("9.9")
    private BigDecimal totalTravelMm;

    @Min(1980) @Max(2100)
    private Short releaseYear;

    @Size(max = 5000)
    private String description;

    @NotBlank(message = "무엇을 바꿨는지 한 줄 적어 주세요")
    @Size(max = 200)
    private String editSummary;

    @Size(max = 500)
    @Pattern(regexp = "^$|^https?://.+", message = "http:// 또는 https://로 시작해야 해요")
    private String sourceUrl;

    // 수정 화면을 연 순간의 버전. 저장할 때 DB 버전과 다르면 그 사이 누가 고친 것이다.
    private Long version;

    public static SwitchForm from(KeyboardSwitch sw) {
        SwitchSpec s = sw.getSpec();
        SwitchForm f = new SwitchForm();
        f.version = sw.getVersion();
        f.manufacturer = s.manufacturer();
        f.modelName = s.modelName();
        f.switchType = s.switchType();
        f.actuationForceG = s.actuationForceG();
        f.bottomOutForceG = s.bottomOutForceG();
        f.preTravelMm = s.preTravelMm();
        f.totalTravelMm = s.totalTravelMm();
        f.releaseYear = s.releaseYear();
        f.description = s.description();
        return f;
    }

    public SwitchSpec toSpec() {
        return new SwitchSpec(manufacturer.strip(), modelName.strip(), switchType,
                actuationForceG, bottomOutForceG, preTravelMm, totalTravelMm,
                releaseYear, description);
    }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public SwitchType getSwitchType() { return switchType; }
    public void setSwitchType(SwitchType switchType) { this.switchType = switchType; }
    public BigDecimal getActuationForceG() { return actuationForceG; }
    public void setActuationForceG(BigDecimal actuationForceG) { this.actuationForceG = actuationForceG; }
    public BigDecimal getBottomOutForceG() { return bottomOutForceG; }
    public void setBottomOutForceG(BigDecimal bottomOutForceG) { this.bottomOutForceG = bottomOutForceG; }
    public BigDecimal getPreTravelMm() { return preTravelMm; }
    public void setPreTravelMm(BigDecimal preTravelMm) { this.preTravelMm = preTravelMm; }
    public BigDecimal getTotalTravelMm() { return totalTravelMm; }
    public void setTotalTravelMm(BigDecimal totalTravelMm) { this.totalTravelMm = totalTravelMm; }
    public Short getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Short releaseYear) { this.releaseYear = releaseYear; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEditSummary() { return editSummary; }
    public void setEditSummary(String editSummary) { this.editSummary = editSummary; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
```

### 서비스: 저장할 때마다 이력 한 줄

`SwitchService.java` 전체를 이걸로 바꾼다(1-4의 `search()` 포함). 동시 수정 막기는 간단하다. 수정 화면을 열 때의 `version`을 폼에 숨겨 두었다가, 저장할 때 DB의 현재 `version`과 다르면 거절한다. 출처는 1단계에서는 모두 "제조사 공식"으로 저장하고, 종류 고르기는 2단계에서 더한다.

```java
package com.switchbase.keyswitch;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class SwitchService {

    private final KeyboardSwitchRepository switches;
    private final SwitchRevisionRepository revisions;
    private final SpecSourceRepository sources;
    private final SwitchQueryRepository switchQuery;

    public SwitchService(KeyboardSwitchRepository switches,
                         SwitchRevisionRepository revisions,
                         SpecSourceRepository sources,
                         SwitchQueryRepository switchQuery) {
        this.switches = switches;
        this.revisions = revisions;
        this.sources = sources;
        this.switchQuery = switchQuery;
    }

    public Page<KeyboardSwitch> search(SwitchSearch search, Pageable pageable) {
        return switchQuery.search(search, pageable);
    }

    public KeyboardSwitch get(Long id) {
        return switches.findById(id)
                .orElseThrow(() -> new NoSuchElementException("스위치가 없습니다: " + id));
    }

    public List<SwitchRevision> revisionsOf(Long id) {
        return revisions.findByKeyboardSwitchIdOrderByRevisionNoDesc(id);
    }

    public List<SpecSource> sourcesOf(Long id) {
        return sources.findByKeyboardSwitchIdOrderByIdAsc(id);
    }

    public boolean exists(String manufacturer, String modelName) {
        return switches.existsBySpecManufacturerAndSpecModelName(manufacturer, modelName);
    }

    @Transactional
    public KeyboardSwitch create(SwitchSpec spec, String editSummary, String editor, String sourceUrl) {
        KeyboardSwitch saved = switches.save(new KeyboardSwitch(spec));
        revisions.save(new SwitchRevision(saved, 1, editSummary, editor));
        addSource(saved, sourceUrl);
        return saved;
    }

    @Transactional
    public KeyboardSwitch update(Long id, Long expectedVersion, SwitchSpec spec,
                                 String editSummary, String editor, String sourceUrl) {
        KeyboardSwitch target = get(id);
        if (expectedVersion == null || target.getVersion() != expectedVersion) {
            throw new OptimisticLockingFailureException("다른 사람이 먼저 수정했습니다: " + id);
        }
        target.changeSpec(spec);
        int next = revisions.countByKeyboardSwitchId(id) + 1;
        revisions.save(new SwitchRevision(target, next, editSummary, editor));
        addSource(target, sourceUrl);
        return target;
    }

    private void addSource(KeyboardSwitch target, String sourceUrl) {
        if (StringUtils.hasText(sourceUrl)) {
            sources.save(new SpecSource(target, SourceType.OFFICIAL, sourceUrl.strip(), null));
        }
    }
}
```

### 컨트롤러

`keyswitch/AdminSwitchController.java`: 검증에 걸리면 같은 화면을 에러 문구와 함께 다시 보여주고, 성공하면 상세 페이지로 보낸다(새로고침해도 두 번 저장되지 않게). `Principal`은 지금 로그인한 사람이다. 이력의 `editor`에 그 이름이 남는다.

```java
package com.switchbase.keyswitch;

import jakarta.validation.Valid;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin/switches")
class AdminSwitchController {

    private final SwitchService switchService;

    AdminSwitchController(SwitchService switchService) {
        this.switchService = switchService;
    }

    @ModelAttribute("types")
    SwitchType[] types() {
        return SwitchType.values();
    }

    @GetMapping("/new")
    String newForm(Model model) {
        model.addAttribute("form", new SwitchForm());
        return "admin/switch-form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute("form") SwitchForm form, BindingResult binding, Principal principal) {
        if (!binding.hasErrors() && switchService.exists(form.getManufacturer().strip(), form.getModelName().strip())) {
            binding.rejectValue("modelName", "duplicate", "이미 등록된 스위치예요");
        }
        if (binding.hasErrors()) {
            return "admin/switch-form";
        }
        KeyboardSwitch saved = switchService.create(
                form.toSpec(), form.getEditSummary(), principal.getName(), form.getSourceUrl());
        return "redirect:/switches/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", SwitchForm.from(switchService.get(id)));
        model.addAttribute("switchId", id);
        return "admin/switch-form";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id,
                  @Valid @ModelAttribute("form") SwitchForm form, BindingResult binding,
                  Principal principal, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("switchId", id);
            return "admin/switch-form";
        }
        try {
            switchService.update(id, form.getVersion(), form.toSpec(),
                    form.getEditSummary(), principal.getName(), form.getSourceUrl());
        } catch (OptimisticLockingFailureException e) {
            binding.reject("conflict", "그 사이 다른 사람이 먼저 고쳤어요. 새로고침 후 다시 해 주세요.");
            model.addAttribute("switchId", id);
            return "admin/switch-form";
        }
        return "redirect:/switches/" + id;
    }
}
```

### 화면

`templates/admin/switch-form.html`: 등록과 수정이 같은 화면을 쓴다. `switchId`가 있으면 수정이다. `th:action`을 쓴 폼에는 Spring Security가 CSRF 토큰(다른 사이트가 내 이름으로 폼을 보내는 걸 막는 표)을 자동으로 넣어 준다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head(${switchId} ? '스위치 수정' : '새 스위치')}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main>
    <h1 th:text="${switchId} ? '스위치 수정' : '새 스위치'">새 스위치</h1>

    <form th:object="${form}" method="post" class="switch-form"
          th:action="${switchId} ? @{/admin/switches/{id}(id=${switchId})} : @{/admin/switches}">
        <p class="error" th:if="${#fields.hasGlobalErrors()}" th:errors="*{global}"></p>
        <input type="hidden" th:field="*{version}">

        <label>제조사 <input th:field="*{manufacturer}" required></label>
        <p class="error" th:errors="*{manufacturer}"></p>

        <label>이름 <input th:field="*{modelName}" required></label>
        <p class="error" th:errors="*{modelName}"></p>

        <label>종류
            <select th:field="*{switchType}" required>
                <option value="">고르세요</option>
                <option th:each="t : ${types}" th:value="${t}" th:text="${t.label}"></option>
            </select>
        </label>
        <p class="error" th:errors="*{switchType}"></p>

        <label>입력압 (g) <input type="number" step="0.1" th:field="*{actuationForceG}"></label>
        <p class="error" th:errors="*{actuationForceG}"></p>
        <label>바닥압 (g) <input type="number" step="0.1" th:field="*{bottomOutForceG}"></label>
        <p class="error" th:errors="*{bottomOutForceG}"></p>
        <label>입력 지점 (mm) <input type="number" step="0.1" th:field="*{preTravelMm}"></label>
        <p class="error" th:errors="*{preTravelMm}"></p>
        <label>키 깊이 (mm) <input type="number" step="0.1" th:field="*{totalTravelMm}"></label>
        <p class="error" th:errors="*{totalTravelMm}"></p>
        <label>출시 연도 <input type="number" th:field="*{releaseYear}"></label>
        <p class="error" th:errors="*{releaseYear}"></p>

        <label>설명 <textarea th:field="*{description}" rows="4"></textarea></label>

        <label>출처 URL (선택) <input type="url" th:field="*{sourceUrl}" placeholder="https://"></label>
        <p class="error" th:errors="*{sourceUrl}"></p>

        <label>무엇을 바꿨나요 <input th:field="*{editSummary}" required placeholder="예: 공식 스펙표 기준으로 입력압 수정"></label>
        <p class="error" th:errors="*{editSummary}"></p>

        <button type="submit">저장</button>
    </form>
</main>
<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

`fragments/layout.html`의 `<html>` 태그에 `sec` 이름표를 더하고, header를 바꾼다. `sec:authorize` 안의 내용은 관리자에게만 보인다. `CSV` 링크는 1-6에서 만든다.

```html
<html lang="ko" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```

```html
<header th:fragment="header" class="site-header">
    <a th:href="@{/}" class="logo">SwitchBase</a>
    <nav>
        <a th:href="@{/switches}">스위치</a>
        <th:block sec:authorize="hasRole('ADMIN')">
            <a th:href="@{/admin/switches/new}">새 스위치</a>
            <a th:href="@{/admin/switches/import}">CSV</a>
            <form th:action="@{/logout}" method="post" class="inline">
                <button type="submit">로그아웃</button>
            </form>
        </th:block>
    </nav>
</header>
```

`switches/detail.html`도 `<html>` 태그에 같은 `xmlns:sec`를 더하고, `<h1>` 바로 아래에 수정 링크를 넣는다.

```html
<a sec:authorize="hasRole('ADMIN')" th:href="@{/admin/switches/{id}/edit(id=${sw.id})}">수정</a>
```

### 테스트

이번 단계부터 테스트가 제 역할을 한다. 이력이 쌓이는지, 늦게 저장하면 거절되는지, 필터가 맞는지, 관리자 문이 잠겨 있는지를 확인한다. 두 파일 모두 `src/test/java/com/switchbase/keyswitch/`에 둔다.

**먼저 할 것**: 0단계에서 Initializr가 만든 `test/java/com/switchbase/TestcontainersConfiguration.java`는 `public`이 아니다. 새 테스트는 한 칸 아래 패키지(`keyswitch`)에 있어서 이 클래스를 못 본다. 클래스 선언을 `public class TestcontainersConfiguration`으로 바꾼다.

`SwitchServiceTest.java`: `@Transactional`을 붙이면 테스트가 끝날 때 넣은 데이터가 되돌려져서, 0단계의 "테이블이 비어 있다" 테스트가 깨지지 않는다. `em.flush()`는 지금까지의 변경을 DB에 실제로 보내라는 뜻이다. 그래야 `version`이 올라간다.

```java
package com.switchbase.keyswitch;

import com.switchbase.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional  // 테스트가 끝나면 넣은 데이터를 되돌린다
class SwitchServiceTest {

    @Autowired
    SwitchService switchService;

    @Autowired
    EntityManager em;

    @Test
    void createWritesFirstRevision() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), "처음 등록", "tester", null);

        assertThat(switchService.revisionsOf(sw.getId()))
                .extracting(SwitchRevision::getRevisionNo)
                .containsExactly(1);
    }

    @Test
    void updateKeepsOldSpecInHistory() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), "처음 등록", "tester", null);
        em.flush();

        switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "55"), "입력압 수정", "tester",
                "https://example.com/spec");
        em.flush();

        var revisions = switchService.revisionsOf(sw.getId());
        assertThat(revisions).extracting(SwitchRevision::getRevisionNo).containsExactly(2, 1);
        assertThat(revisions.get(1).getSpec().actuationForceG()).isEqualByComparingTo("50");
        assertThat(switchService.get(sw.getId()).getSpec().actuationForceG()).isEqualByComparingTo("55");
        assertThat(switchService.sourcesOf(sw.getId())).hasSize(1);
    }

    @Test
    void staleVersionIsRejected() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), "처음 등록", "tester", null);
        em.flush();
        switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "55"), "먼저 고침", "a", null);
        em.flush();  // 여기서 version이 1이 된다

        assertThatThrownBy(() ->
                switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "60"), "늦게 고침", "b", null))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void searchFiltersByTypeAndForce() {
        switchService.create(spec("Light Linear", SwitchType.LINEAR, "35"), "등록", "tester", null);
        switchService.create(spec("Heavy Linear", SwitchType.LINEAR, "67"), "등록", "tester", null);
        switchService.create(spec("Heavy Tactile", SwitchType.TACTILE, "67"), "등록", "tester", null);

        Page<KeyboardSwitch> result = switchService.search(
                new SwitchSearch("test", SwitchType.LINEAR, new BigDecimal("60"), null), PageRequest.of(0, 20));

        assertThat(result.getContent())
                .extracting(sw -> sw.getSpec().modelName())
                .containsExactly("Heavy Linear");
    }

    private SwitchSpec spec(String model, SwitchType type, String force) {
        return new SwitchSpec("Test Co", model, type, new BigDecimal(force),
                null, null, null, null, null);
    }
}
```

`AdminAccessTest.java`: 브라우저 없이 요청을 보내 보는 MockMvc 테스트다. `@WithMockUser`는 "관리자로 로그인한 셈 치기"다.

```java
package com.switchbase.keyswitch;

import com.switchbase.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminAccessTest {

    @Autowired
    MockMvc mvc;

    @Test
    void anyoneCanSeeSwitchList() throws Exception {
        mvc.perform(get("/switches")).andExpect(status().isOk());
    }

    @Test
    void anonymousIsSentToLogin() throws Exception {
        mvc.perform(get("/admin/switches/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanOpenForm() throws Exception {
        mvc.perform(get("/admin/switches/new")).andExpect(status().isOk());
    }
}
```

`AutoConfigureMockMvc` import가 빨간색이면 `build.gradle.kts`에 `spring-boot-starter-webmvc-test`가 있는지 본다. 없으면 `testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")`를 더한다. Spring Boot 4에서는 이 클래스의 패키지가 3 버전과 달라서, 인터넷 예제의 import를 복사하면 안 맞는다.

**확인**

1. `./gradlew test`가 초록색이다(0단계 3개 + 새 테스트 7개).
2. 로그아웃 상태에서 `localhost:8090/admin/switches/new`로 가면 로그인 화면이 나온다. `admin` / `admin1234`로 로그인한다.
3. 스위치를 하나 등록하고, 상세에서 수정을 눌러 숫자를 바꾸면 수정 이력에 `#2`가 생긴다.
4. 수정 화면을 탭 두 개로 열고 차례로 저장하면 두 번째 탭에서 "그 사이 다른 사람이 먼저 고쳤어요"가 나온다.

1-3에서 SQL로 직접 넣은 줄은 이력이 없어서, 수정하면 `#1`부터 시작한다. 헷갈리면 그 줄은 지우고 화면으로 다시 등록해도 된다.

커밋 두 개로 나눌 수 있다: `feat(admin): add admin login and switch editing with revisions` 다음 `test(switch): add service and admin access tests`

## 1-5.5. 브랜드, 변형, 다른 이름 (V3)

데이터를 넣기 전에 이름 규칙을 정한다. 원칙은 **대표 이름 하나 + 다른 이름 여러 개**다.

| 문제 | 규칙 |
| --- | --- |
| 제조사 영어/한글 | 대표는 공식 영어 표기(`Gateron`). "게이트론"은 `manufacturer_alias`에 넣어 두면, 누가 한글로 입력해도 저장할 때 `Gateron`으로 바뀐다 |
| 스위치 별명 | `model_name`은 제조사 공식 표기 하나. "저월백", "저소음 월백" 같은 이름은 `switch_alias`에 여러 줄 |
| 콜라보 | `manufacturer` = 실제로 만든 회사, `brand` = 기획하거나 판매한 쪽. 혼자 낸 스위치는 `brand`를 비운다 |
| 이름은 같은데 무게가 다름 | 줄을 따로 만들고 variant(변형)에 45g, 55g처럼 적는다. 버전이 하나뿐이면 비운다. 같은 스위치 검사는 제조사 + 이름 + 변형으로 한다. 별명은 줄마다 따로 단다 |

검색은 띄어쓰기와 대소문자를 무시하고 제조사, 브랜드, 이름, 별칭, 제조사의 한글 표기를 모두 본다. "저 월백"으로 쳐도, "게이트론"으로 쳐도 찾는다. 별칭은 1단계에서는 수정 이력에 남지 않는다(매번 통째로 갈아 끼운다).

### V3 마이그레이션

`db/migration/V3__add_brand_and_aliases.sql`. 마지막 `INSERT`는 예시다. 제조사 한글 표기는 Soon이 정해서 고치거나 더한다. 나중에 더 넣고 싶으면 V4 파일에 `INSERT`만 쓰면 된다. `normalized`에는 띄어쓰기를 빼고 소문자로 바꾼 값을 넣는다.

```sql
-- 콜라보: manufacturer = 실제로 만든 회사, brand = 기획하거나 판매한 쪽 (혼자 낸 스위치는 비워 둔다)
ALTER TABLE keyboard_switch ADD COLUMN brand VARCHAR(100);
ALTER TABLE switch_revision ADD COLUMN brand VARCHAR(100);

-- 이름은 같은데 무게가 다른 버전 (Y3 45g, Y3 55g). 하나뿐이면 비워 둔다.
ALTER TABLE keyboard_switch ADD COLUMN variant VARCHAR(50);
ALTER TABLE switch_revision ADD COLUMN variant VARCHAR(50);

-- 같은 스위치 검사를 제조사 + 이름 + 변형으로 바꾼다. 변형이 비어 있는 것끼리도 같은 걸로 본다.
ALTER TABLE keyboard_switch DROP CONSTRAINT uk_keyboard_switch_name;
ALTER TABLE keyboard_switch ADD CONSTRAINT uk_keyboard_switch_name
    UNIQUE NULLS NOT DISTINCT (manufacturer, model_name, variant);

-- 스위치 하나의 다른 이름들 (저월백, 저소음 월백 ...)
CREATE TABLE switch_alias (
    id          BIGSERIAL    PRIMARY KEY,
    switch_id   BIGINT       NOT NULL REFERENCES keyboard_switch (id) ON DELETE CASCADE,
    alias       VARCHAR(150) NOT NULL,
    normalized  VARCHAR(150) NOT NULL,
    CONSTRAINT uk_switch_alias UNIQUE (switch_id, normalized)
);

CREATE INDEX ix_switch_alias_normalized ON switch_alias (normalized);

-- 제조사의 다른 표기 → 대표 이름 (게이트론 → Gateron)
CREATE TABLE manufacturer_alias (
    id            BIGSERIAL    PRIMARY KEY,
    manufacturer  VARCHAR(100) NOT NULL,
    alias         VARCHAR(100) NOT NULL,
    normalized    VARCHAR(100) NOT NULL,
    CONSTRAINT uk_manufacturer_alias UNIQUE (normalized)
);

INSERT INTO manufacturer_alias (manufacturer, alias, normalized) VALUES
    ('Gateron', '게이트론', '게이트론'),
    ('Cherry',  '체리',     '체리'),
    ('Kailh',   '카일',     '카일');
```

### 새 파일 (keyswitch 패키지)

`Names.java`: 이름 비교 규칙을 한 곳에 둔다.

```java
package com.switchbase.keyswitch;

import java.util.Locale;

/** 이름 비교용 규칙. 대소문자와 띄어쓰기를 무시한다. "저 월백" == "저월백" */
public final class Names {

    private Names() {
    }

    public static String normalize(String name) {
        return name.strip().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
    }
}
```

`SwitchAlias.java`:

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "switch_alias")
public class SwitchAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "switch_id")
    private KeyboardSwitch keyboardSwitch;

    @Column(name = "alias", nullable = false, length = 150)
    private String alias;

    @Column(name = "normalized", nullable = false, length = 150)
    private String normalized;

    protected SwitchAlias() {
    }

    public SwitchAlias(KeyboardSwitch keyboardSwitch, String alias) {
        this.keyboardSwitch = keyboardSwitch;
        this.alias = alias.strip();
        this.normalized = Names.normalize(alias);
    }

    public Long getId() { return id; }
    public String getAlias() { return alias; }
    public String getNormalized() { return normalized; }
}
```

`ManufacturerAlias.java`: 화면에서 고치지 않고 SQL로만 넣으니 생성자가 없다.

```java
package com.switchbase.keyswitch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 제조사의 다른 표기와 대표 이름의 짝. 데이터는 마이그레이션 SQL로 넣는다. */
@Entity
@Table(name = "manufacturer_alias")
public class ManufacturerAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer", nullable = false, length = 100)
    private String manufacturer;

    @Column(name = "alias", nullable = false, length = 100)
    private String alias;

    @Column(name = "normalized", nullable = false, length = 100)
    private String normalized;

    protected ManufacturerAlias() {
    }

    public Long getId() { return id; }
    public String getManufacturer() { return manufacturer; }
    public String getAlias() { return alias; }
}
```

리포지토리 두 개:

```java
package com.switchbase.keyswitch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwitchAliasRepository extends JpaRepository<SwitchAlias, Long> {

    List<SwitchAlias> findByKeyboardSwitchIdOrderByIdAsc(Long switchId);

    void deleteByKeyboardSwitchId(Long switchId);
}
```

```java
package com.switchbase.keyswitch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerAliasRepository extends JpaRepository<ManufacturerAlias, Long> {

    Optional<ManufacturerAlias> findByNormalized(String normalized);
}
```

### 고칠 파일

`SwitchSpec.java`: `manufacturer` 바로 다음에 `brand`를, `modelName` 바로 다음에 `variant`를 넣고, 맨 끝 `{ }` 안에 메서드 하나를 더한다. 순서가 중요하다. record는 칸 순서대로 생성자를 만든다.

```java
        @Column(name = "manufacturer", nullable = false, length = 100)
        String manufacturer,

        @Column(name = "brand", length = 100)
        String brand,

        @Column(name = "model_name", nullable = false, length = 150)
        String modelName,

        @Column(name = "variant", length = 50)
        String variant,
```

```java
) {

    public SwitchSpec withManufacturer(String manufacturer) {
        return new SwitchSpec(manufacturer, brand, modelName, variant, switchType, actuationForceG, bottomOutForceG,
                preTravelMm, totalTravelMm, releaseYear, description);
    }
}
```

`SwitchForm.java`: 칸 세 개와 메서드 두 개를 더하고, `from()`과 `toSpec()`을 바꾼다. import는 `java.util.Arrays`, `java.util.List`, `java.util.stream.Collectors`.

```java
    // modelName 위에
    @Size(max = 100)
    private String brand;

    // modelName 아래에. 같은 이름에 무게만 다른 버전이 있을 때만 (예: 45g)
    @Size(max = 50)
    private String variant;

    // editSummary 위에. 한 줄에 하나 (CSV에서는 | 로 구분)
    @Size(max = 2000)
    private String aliases;
```

```java
    public static SwitchForm from(KeyboardSwitch sw, List<SwitchAlias> aliases) {
        SwitchSpec s = sw.getSpec();
        SwitchForm f = new SwitchForm();
        f.version = sw.getVersion();
        f.manufacturer = s.manufacturer();
        f.brand = s.brand();
        f.modelName = s.modelName();
        f.variant = s.variant();
        f.switchType = s.switchType();
        f.actuationForceG = s.actuationForceG();
        f.bottomOutForceG = s.bottomOutForceG();
        f.preTravelMm = s.preTravelMm();
        f.totalTravelMm = s.totalTravelMm();
        f.releaseYear = s.releaseYear();
        f.description = s.description();
        f.aliases = aliases.stream().map(SwitchAlias::getAlias).collect(Collectors.joining("\n"));
        return f;
    }

    public SwitchSpec toSpec() {
        return new SwitchSpec(manufacturer.strip(), blankToNull(brand), modelName.strip(), blankToNull(variant), switchType,
                actuationForceG, bottomOutForceG, preTravelMm, totalTravelMm,
                releaseYear, description);
    }

    // 빈칸은 null로. 브랜드나 변형이 ""로 저장되면 "없음"과 구분이 안 된다.
    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.strip();
    }

    /** 별칭 칸을 줄바꿈이나 | 로 잘라 목록으로. 빈 줄은 버린다. */
    public List<String> aliasList() {
        if (aliases == null) {
            return List.of();
        }
        return Arrays.stream(aliases.split("[|\r\n]+"))
                .map(String::strip)
                .filter(a -> !a.isEmpty())
                .toList();
    }
```

`brand`, `variant`, `aliases`의 getter/setter도 Alt+Insert로 만든다.

`KeyboardSwitchRepository.java`: 있던 `exists...` 메서드를 지우고 아래 두 개로 바꾼다. 변형이 비어 있을 때(`IsNull`)와 있을 때를 따로 묻는다. SQL에서 `variant = null`은 항상 거짓이라서 하나로는 안 된다.

```java
    boolean existsBySpecManufacturerAndSpecModelNameAndSpecVariant(String manufacturer, String modelName, String variant);

    boolean existsBySpecManufacturerAndSpecModelNameAndSpecVariantIsNull(String manufacturer, String modelName);
```

`SwitchService.java` 전체를 이걸로 바꾼다. 바뀐 점은 세 가지다. `create()`/`update()`가 별칭 목록을 받고, 저장하기 전에 제조사 이름을 대표 이름으로 바꾸고, `exists()`는 대표 이름 + 이름 + 변형으로 비교한다.

```java
package com.switchbase.keyswitch;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SwitchService {

    private final KeyboardSwitchRepository switches;
    private final SwitchRevisionRepository revisions;
    private final SpecSourceRepository sources;
    private final SwitchAliasRepository aliases;
    private final ManufacturerAliasRepository manufacturerAliases;
    private final SwitchQueryRepository switchQuery;

    public SwitchService(KeyboardSwitchRepository switches,
                         SwitchRevisionRepository revisions,
                         SpecSourceRepository sources,
                         SwitchAliasRepository aliases,
                         ManufacturerAliasRepository manufacturerAliases,
                         SwitchQueryRepository switchQuery) {
        this.switches = switches;
        this.revisions = revisions;
        this.sources = sources;
        this.aliases = aliases;
        this.manufacturerAliases = manufacturerAliases;
        this.switchQuery = switchQuery;
    }

    public Page<KeyboardSwitch> search(SwitchSearch search, Pageable pageable) {
        return switchQuery.search(search, pageable);
    }

    public KeyboardSwitch get(Long id) {
        return switches.findById(id)
                .orElseThrow(() -> new NoSuchElementException("스위치가 없습니다: " + id));
    }

    public List<SwitchRevision> revisionsOf(Long id) {
        return revisions.findByKeyboardSwitchIdOrderByRevisionNoDesc(id);
    }

    public List<SpecSource> sourcesOf(Long id) {
        return sources.findByKeyboardSwitchIdOrderByIdAsc(id);
    }

    public List<SwitchAlias> aliasesOf(Long id) {
        return aliases.findByKeyboardSwitchIdOrderByIdAsc(id);
    }

    /** "게이트론"처럼 등록된 다른 표기면 대표 이름("Gateron")으로 바꾼다. 모르는 이름은 그대로. */
    public String canonicalManufacturer(String manufacturer) {
        return manufacturerAliases.findByNormalized(Names.normalize(manufacturer))
                .map(ManufacturerAlias::getManufacturer)
                .orElse(manufacturer.strip());
    }

    /** 제조사 + 이름 + 변형이 모두 같은 스위치가 있는지. 변형이 비어 있으면 변형 없는 것끼리 비교한다. */
    public boolean exists(String manufacturer, String modelName, String variant) {
        String official = canonicalManufacturer(manufacturer);
        if (variant == null || variant.isBlank()) {
            return switches.existsBySpecManufacturerAndSpecModelNameAndSpecVariantIsNull(official, modelName);
        }
        return switches.existsBySpecManufacturerAndSpecModelNameAndSpecVariant(official, modelName, variant.strip());
    }

    @Transactional
    public KeyboardSwitch create(SwitchSpec spec, List<String> aliasNames,
                                 String editSummary, String editor, String sourceUrl) {
        KeyboardSwitch saved = switches.save(new KeyboardSwitch(canonical(spec)));
        revisions.save(new SwitchRevision(saved, 1, editSummary, editor));
        replaceAliases(saved, aliasNames);
        addSource(saved, sourceUrl);
        return saved;
    }

    @Transactional
    public KeyboardSwitch update(Long id, Long expectedVersion, SwitchSpec spec, List<String> aliasNames,
                                 String editSummary, String editor, String sourceUrl) {
        KeyboardSwitch target = get(id);
        if (expectedVersion == null || target.getVersion() != expectedVersion) {
            throw new OptimisticLockingFailureException("다른 사람이 먼저 수정했습니다: " + id);
        }
        target.changeSpec(canonical(spec));
        int next = revisions.countByKeyboardSwitchId(id) + 1;
        revisions.save(new SwitchRevision(target, next, editSummary, editor));
        replaceAliases(target, aliasNames);
        addSource(target, sourceUrl);
        return target;
    }

    private SwitchSpec canonical(SwitchSpec spec) {
        return spec.withManufacturer(canonicalManufacturer(spec.manufacturer()));
    }

    // 별칭은 통째로 갈아 끼운다. "저 월백"과 "저월백"처럼 같은 이름은 하나만 남긴다.
    private void replaceAliases(KeyboardSwitch target, List<String> aliasNames) {
        aliases.deleteByKeyboardSwitchId(target.getId());
        aliases.flush();
        Set<String> seen = new HashSet<>();
        for (String name : aliasNames) {
            if (seen.add(Names.normalize(name))) {
                aliases.save(new SwitchAlias(target, name));
            }
        }
    }

    private void addSource(KeyboardSwitch target, String sourceUrl) {
        if (StringUtils.hasText(sourceUrl)) {
            sources.save(new SpecSource(target, SourceType.OFFICIAL, sourceUrl.strip(), null));
        }
    }
}
```

`aliases.flush()`는 "지우기를 먼저 DB에 보내라"는 뜻이다. 없으면 같은 별칭을 다시 넣을 때 지우기보다 넣기가 먼저 가서 중복 에러가 날 수 있다.

**`SwitchQueryRepository.java`**: `keywordContains()`를 바꾸고 import를 늘린다. `JPAExpressions`는 쿼리 안의 쿼리(서브쿼리)다. "이 스위치의 별칭 중에 맞는 게 있나"를 묻는다.

```java
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;

import static com.switchbase.keyswitch.QManufacturerAlias.manufacturerAlias;
import static com.switchbase.keyswitch.QSwitchAlias.switchAlias;
```

```java
    // 제조사, 브랜드, 이름, 별칭, 제조사의 다른 표기 중 하나라도 맞으면 찾는다.
    // 이름과 별칭은 띄어쓰기와 대소문자를 무시하고 비교한다.
    private BooleanExpression keywordContains(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        String compact = Names.normalize(keyword);
        StringExpression compactModelName = Expressions.stringTemplate(
                "replace(lower({0}), ' ', '')", keyboardSwitch.spec.modelName);

        return keyboardSwitch.spec.manufacturer.containsIgnoreCase(keyword.strip())
                .or(keyboardSwitch.spec.brand.containsIgnoreCase(keyword.strip()))
                .or(compactModelName.contains(compact))
                .or(JPAExpressions.selectOne()
                        .from(switchAlias)
                        .where(switchAlias.keyboardSwitch.eq(keyboardSwitch),
                                switchAlias.normalized.contains(compact))
                        .exists())
                .or(keyboardSwitch.spec.manufacturer.in(
                        JPAExpressions.select(manufacturerAlias.manufacturer)
                                .from(manufacturerAlias)
                                .where(manufacturerAlias.normalized.contains(compact))));
    }
```

같은 파일 `search()`의 정렬에 변형을 더해서, Y3가 변형 없는 것, 45g, 55g 순서로 나오게 한다.

```java
                .orderBy(keyboardSwitch.spec.manufacturer.asc(), keyboardSwitch.spec.modelName.asc(),
                        keyboardSwitch.spec.variant.asc().nullsFirst())
```

`AdminSwitchController.java`: 서비스를 부르는 세 곳에 별칭을 넘기고, 중복 검사에 변형을 더한다.

```java
// create() 위쪽 중복 검사
if (!binding.hasErrors() && switchService.exists(form.getManufacturer().strip(), form.getModelName().strip(), form.getVariant())) {

// create()
KeyboardSwitch saved = switchService.create(
        form.toSpec(), form.aliasList(), form.getEditSummary(), principal.getName(), form.getSourceUrl());

// editForm()
model.addAttribute("form", SwitchForm.from(switchService.get(id), switchService.aliasesOf(id)));

// update()
switchService.update(id, form.getVersion(), form.toSpec(), form.aliasList(),
        form.getEditSummary(), principal.getName(), form.getSourceUrl());
```

**`SwitchController.java`**: `detail()`에 한 줄.

```java
model.addAttribute("aliases", switchService.aliasesOf(id));
```

### 화면

`admin/switch-form.html`: "이름" 칸을 아래 줄들로 바꾸고, "설명" 칸 바로 위에 별칭 칸을 넣는다.

```html
<label>브랜드 (콜라보일 때만) <input th:field="*{brand}" placeholder="기획/판매한 쪽"></label>
<p class="error" th:errors="*{brand}"></p>

<label>이름 (제조사 공식 표기) <input th:field="*{modelName}" required></label>
<p class="error" th:errors="*{modelName}"></p>

<label>변형 (같은 이름에 무게만 다를 때) <input th:field="*{variant}" placeholder="예: 45g"></label>
<p class="error" th:errors="*{variant}"></p>
```

```html
<label>다른 이름 (한 줄에 하나) <textarea th:field="*{aliases}" rows="3" placeholder="저월백&#10;저소음 월백"></textarea></label>
<p class="error" th:errors="*{aliases}"></p>
```

`switches/detail.html`: 맨 위 `<head>`와 `<h1>`이 변형까지 보여 주게 바꾸고("Y3 (45g)"), `<h1>` 바로 아래에 다른 이름을, 스펙 표 맨 위에 브랜드 줄을 넣는다. `aliases.![alias]`는 "목록에서 alias만 뽑아라"는 뜻이다.

```html
<head th:replace="~{fragments/layout :: head(${sw.spec.variant} ? |${sw.spec.modelName} (${sw.spec.variant})| : ${sw.spec.modelName})}"></head>
```

```html
<h1 th:text="${s.variant} ? |${s.modelName} (${s.variant})| : ${s.modelName}">Yellow</h1>
```

```html
<p class="muted" th:unless="${aliases.empty}"
   th:text="|다른 이름: ${#strings.listJoin(aliases.![alias], ', ')}|">다른 이름: 저월백</p>
```

```html
<tr th:if="${s.brand}"><th>브랜드</th><td th:text="${s.brand}"></td></tr>
```

`switches/list.html`: 제조사 칸을 콜라보면 "TTC × 브랜드"로, 이름 칸을 변형이 있으면 "Y3 (45g)"로 보여 주고, 검색창 안내 문구를 바꾼다.

```html
<td th:text="${s.brand} ? |${s.manufacturer} × ${s.brand}| : ${s.manufacturer}">Gateron</td>
```

```html
<td><a th:href="@{/switches/{id}(id=${sw.id})}" th:text="${s.variant} ? |${s.modelName} (${s.variant})| : ${s.modelName}">Yellow</a></td>
```

```html
<input type="search" name="keyword" th:value="${search.keyword}" placeholder="제조사, 이름, 별명">
```

### 테스트

`SwitchServiceTest.java` 전체를 이걸로 바꾼다. 기존 테스트에 `List.of()`(별칭 없음)와 `SwitchSpec`의 변형 자리 `null`이 들어갔고, 별칭 검색, 한글 제조사 이름, 변형 테스트 세 개가 늘었다.

```java
package com.switchbase.keyswitch;

import com.switchbase.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional  // 테스트가 끝나면 넣은 데이터를 되돌린다
class SwitchServiceTest {

    @Autowired
    SwitchService switchService;

    @Autowired
    EntityManager em;

    @Test
    void createWritesFirstRevision() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), List.of(), "처음 등록", "tester", null);

        assertThat(switchService.revisionsOf(sw.getId()))
                .extracting(SwitchRevision::getRevisionNo)
                .containsExactly(1);
    }

    @Test
    void updateKeepsOldSpecInHistory() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), List.of(), "처음 등록", "tester", null);
        em.flush();

        switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "55"), List.of(), "입력압 수정", "tester",
                "https://example.com/spec");
        em.flush();

        var revisions = switchService.revisionsOf(sw.getId());
        assertThat(revisions).extracting(SwitchRevision::getRevisionNo).containsExactly(2, 1);
        assertThat(revisions.get(1).getSpec().actuationForceG()).isEqualByComparingTo("50");
        assertThat(switchService.get(sw.getId()).getSpec().actuationForceG()).isEqualByComparingTo("55");
        assertThat(switchService.sourcesOf(sw.getId())).hasSize(1);
    }

    @Test
    void staleVersionIsRejected() {
        KeyboardSwitch sw = switchService.create(spec("Yellow", SwitchType.LINEAR, "50"), List.of(), "처음 등록", "tester", null);
        em.flush();
        switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "55"), List.of(), "먼저 고침", "a", null);
        em.flush();  // 여기서 version이 1이 된다

        assertThatThrownBy(() ->
                switchService.update(sw.getId(), 0L, spec("Yellow", SwitchType.LINEAR, "60"), List.of(), "늦게 고침", "b", null))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void searchFiltersByTypeAndForce() {
        switchService.create(spec("Light Linear", SwitchType.LINEAR, "35"), List.of(), "등록", "tester", null);
        switchService.create(spec("Heavy Linear", SwitchType.LINEAR, "67"), List.of(), "등록", "tester", null);
        switchService.create(spec("Heavy Tactile", SwitchType.TACTILE, "67"), List.of(), "등록", "tester", null);

        Page<KeyboardSwitch> result = switchService.search(
                new SwitchSearch("test", SwitchType.LINEAR, new BigDecimal("60"), null), PageRequest.of(0, 20));

        assertThat(result.getContent())
                .extracting(sw -> sw.getSpec().modelName())
                .containsExactly("Heavy Linear");
    }

    @Test
    void searchFindsSwitchByAliasIgnoringSpaces() {
        switchService.create(spec("Silent Test White", SwitchType.LINEAR, "45"),
                List.of("저월백", "저소음 월백", "저 월백"), "등록", "tester", null);
        em.flush();

        assertThat(searchNames("저 월백")).containsExactly("Silent Test White");
        assertThat(searchNames("저소음월백")).containsExactly("Silent Test White");
        assertThat(searchNames("silenttest")).containsExactly("Silent Test White");
        // "저월백"과 "저 월백"은 같은 이름이라 하나만 남는다
        assertThat(switchService.aliasesOf(searchId("저월백"))).hasSize(2);
    }

    @Test
    void koreanManufacturerNameIsSavedAsOfficialName() {
        KeyboardSwitch sw = switchService.create(
                new SwitchSpec("게이트론", null, "Test Yellow", null, SwitchType.LINEAR, null, null, null, null, null, null),
                List.of(), "등록", "tester", null);
        em.flush();

        assertThat(sw.getSpec().manufacturer()).isEqualTo("Gateron");
        assertThat(switchService.exists("게이트론", "Test Yellow", null)).isTrue();
        assertThat(searchNames("게이트론")).contains("Test Yellow");
    }

    @Test
    void sameNameWithDifferentVariantsCanCoexist() {
        switchService.create(variantSpec("Y3", "45g", "45"), List.of(), "등록", "tester", null);
        switchService.create(variantSpec("Y3", "55g", "55"), List.of(), "등록", "tester", null);
        em.flush();

        assertThat(switchService.exists("Test Co", "Y3", "45g")).isTrue();
        assertThat(switchService.exists("Test Co", "Y3", "62g")).isFalse();
        assertThat(switchService.exists("Test Co", "Y3", null)).isFalse();
        assertThat(searchNames("y3")).hasSize(2);

        // 제조사 + 이름 + 변형이 모두 같으면 DB가 막는다
        assertThatThrownBy(() ->
                switchService.create(variantSpec("Y3", "45g", "45"), List.of(), "등록", "tester", null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private SwitchSpec variantSpec(String model, String variant, String force) {
        return new SwitchSpec("Test Co", null, model, variant, SwitchType.LINEAR, new BigDecimal(force),
                null, null, null, null, null);
    }

    private List<String> searchNames(String keyword) {
        return switchService.search(new SwitchSearch(keyword, null, null, null), PageRequest.of(0, 20))
                .getContent().stream().map(sw -> sw.getSpec().modelName()).toList();
    }

    private Long searchId(String keyword) {
        return switchService.search(new SwitchSearch(keyword, null, null, null), PageRequest.of(0, 20))
                .getContent().getFirst().getId();
    }

    private SwitchSpec spec(String model, SwitchType type, String force) {
        return new SwitchSpec("Test Co", null, model, null, type, new BigDecimal(force),
                null, null, null, null, null);
    }
}
```

**확인**

1. `bootRun` 로그에 `now at version v3`가 보인다. 이미 넣어 둔 스위치는 그대로 남고 브랜드와 변형만 비어 있다.
2. `.\gradlew test` 통과 (`SwitchServiceTest` 7개).
3. 관리자 화면에서 제조사를 "게이트론"으로 넣고 저장하면 상세에 `Gateron`으로 나온다.
4. 별칭을 넣은 스위치가 목록 검색창에서 별칭으로, 띄어쓰기를 바꿔 쳐도 찾아진다.
5. 관리자 화면에서 이름 Y3, 변형 45g와 55g를 따로 등록하면 둘 다 들어가고, 45g를 한 번 더 넣으면 "이미 등록된 스위치예요"가 나온다. 목록에는 "Y3 (45g)", "Y3 (55g)"로 보인다.

커밋: `feat(switch): add brand, variant and alias search`

## 1-6. CSV로 스위치 넣기

스위치 50\~100개를 한 개씩 화면으로 넣기는 힘들다. 관리자 화면에 CSV 올리기를 만들어서, 엑셀로 정리한 표를 한 번에 넣는다. 화면 등록과 똑같이 `SwitchService.create()`를 거치므로 이력(`CSV 일괄 등록`)과 출처도 같이 남는다. 서버에 배포한 뒤에도 같은 화면으로 넣으면 돼서, 서버 DB에 직접 접속할 일이 없다.

**데이터는 사람이 모은다.** 숫자는 제조사 공식 페이지나 스펙표에서 직접 확인하고, 그 주소를 `source_url`에 남긴다. 판매처마다 숫자가 다른 경우가 흔하고, 출처가 있어야 나중에 누가 맞는지 따질 수 있다. 모르는 칸은 비워 둔다. 틀린 숫자보다 빈칸이 낫다.

### CSV 형식

첫 줄은 제목 줄, 칸은 열두 개다(1-5.5에서 brand, variant, aliases가 늘었다). 예시는 형식만 보여 주는 것이니 숫자는 직접 확인해서 채운다. 빈칸도 쉼표는 그대로 둠야 칸 수가 맞는다.

```csv
manufacturer,brand,model_name,variant,switch_type,actuation_force_g,bottom_out_force_g,pre_travel_mm,total_travel_mm,release_year,aliases,source_url
Cherry,,MX Red,,LINEAR,45,,2.0,4.0,,적축,https://www.cherry.de/
Cherry,,MX Brown,,TACTILE,55,,2.0,4.0,,갈축,https://www.cherry.de/
Gateron,,Yellow,,LINEAR,50,,2.0,4.0,,황축|게이트론 황축,https://www.gateron.com/
```

| 칸 | 규칙 |
| --- | --- |
| manufacturer | 공식 영어 표기. "게이트론"처럼 manufacturer\_alias에 등록된 한글 표기는 저장할 때 자동으로 바뀐다 |
| brand | 콜라보일 때만. 아니면 빈칸 |
| variant | 같은 이름에 무게만 다를 때 (예: 45g). 버전이 하나뿐이면 빈칸. Y3 45g와 Y3 55g는 두 줄로 따로 적는다 |
| aliases | 다른 이름을 \| 로 구분 (예: 저월백\|저소음 월백). 없으면 빈칸 |
| `switch_type` | `LINEAR`, `TACTILE`, `CLICKY` 중 하나 (소문자도 됨) |
| 힘(g) | 10\~200 |
| 거리(mm) | 0.1\~9.9 |
| `release_year` | 1980\~2100, 모르면 빈칸 |
| 이름 | 쉼표(`,`)를 넣지 않는다 |

엑셀에서 저장할 때는 파일 형식을 \*\*CSV UTF-8(쉼표로 분리)\*\*로 고른다. 그냥 "CSV"를 고르면 한글 윈도우에서는 다른 인코딩으로 저장돼서 글자가 깨진다. 파일은 프로젝트의 `data/switches.csv`에 두고 커밋한다. 데이터도 Git으로 이력이 남는다.

### 코드

`keyswitch/SwitchCsvImporter.java`: 한 줄이 틀려도 멈추지 않고 나머지를 계속 넣은 뒤, 무엇이 몇 번째 줄에서 틀렸는지 보여준다. 이미 있는 스위치(제조사+이름+변형)는 건너뛰므로 같은 파일을 두 번 올려도 안전하다. 검증은 1-5의 `SwitchForm` 규칙을 그대로 쓴다.

```java
package com.switchbase.keyswitch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CSV 한 줄 = 스위치 하나.
 * manufacturer,brand,model_name,variant,switch_type,actuation_force_g,bottom_out_force_g,pre_travel_mm,total_travel_mm,release_year,aliases,source_url
 * aliases는 | 로 구분한다. 예: 저월백|저소음 월백
 * 이미 있는 스위치(제조사+이름+변형)는 건너뛴다. 한 줄이 틀려도 나머지는 계속 넣는다.
 */
@Component
public class SwitchCsvImporter {

    private static final int COLUMNS = 12;

    private final SwitchService switchService;
    private final Validator validator;

    public SwitchCsvImporter(SwitchService switchService, Validator validator) {
        this.switchService = switchService;
        this.validator = validator;
    }

    public record Result(int created, int skipped, List<String> errors) {
    }

    public Result importCsv(InputStream in, String editor) throws IOException {
        int created = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (lineNo == 1 || line.isBlank()) {
                    continue; // 첫 줄은 제목 줄
                }
                try {
                    SwitchForm form = parse(line);
                    Set<ConstraintViolation<SwitchForm>> violations = validator.validate(form);
                    if (!violations.isEmpty()) {
                        ConstraintViolation<SwitchForm> v = violations.iterator().next();
                        errors.add(lineNo + "번째 줄: " + v.getPropertyPath() + " " + v.getMessage());
                        continue;
                    }
                    if (switchService.exists(form.getManufacturer(), form.getModelName(), form.getVariant())) {
                        skipped++;
                        continue;
                    }
                    switchService.create(form.toSpec(), form.aliasList(), form.getEditSummary(), editor, form.getSourceUrl());
                    created++;
                } catch (NumberFormatException e) {
                    errors.add(lineNo + "번째 줄: 숫자 칸에 숫자가 아닌 값이 있어요");
                } catch (IllegalArgumentException e) {
                    errors.add(lineNo + "번째 줄: " + e.getMessage());
                }
            }
        }
        return new Result(created, skipped, errors);
    }

    private SwitchForm parse(String line) {
        String[] c = line.split(",", -1);
        if (c.length != COLUMNS) {
            throw new IllegalArgumentException("칸이 " + COLUMNS + "개여야 하는데 " + c.length + "개예요");
        }
        SwitchForm f = new SwitchForm();
        f.setManufacturer(c[0].strip());
        f.setBrand(c[1].strip());
        f.setModelName(c[2].strip());
        f.setVariant(c[3].strip());
        f.setSwitchType(type(c[4]));
        f.setActuationForceG(decimal(c[5]));
        f.setBottomOutForceG(decimal(c[6]));
        f.setPreTravelMm(decimal(c[7]));
        f.setTotalTravelMm(decimal(c[8]));
        f.setReleaseYear(c[9].isBlank() ? null : Short.valueOf(c[9].strip()));
        f.setAliases(c[10]);
        f.setSourceUrl(c[11].strip());
        f.setEditSummary("CSV 일괄 등록");
        return f;
    }

    private SwitchType type(String value) {
        try {
            return SwitchType.valueOf(value.strip().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("switch_type은 LINEAR, TACTILE, CLICKY 중 하나여야 해요");
        }
    }

    private BigDecimal decimal(String value) {
        return value.isBlank() ? null : new BigDecimal(value.strip());
    }
}
```

`AdminSwitchController`에 `SwitchCsvImporter`를 필드와 생성자 매개변수로 추가하고(`csvImporter`라는 이름으로), 메서드 두 개를 더한다. 올린 뒤에는 같은 화면으로 돌아가면서 결과를 한 번만 보여준다(flash attribute).

```java
@GetMapping("/import")
String importForm() {
    return "admin/switch-import";
}

@PostMapping("/import")
String importCsv(@RequestParam("file") MultipartFile file, Principal principal,
                 RedirectAttributes redirect) throws IOException {
    SwitchCsvImporter.Result result = csvImporter.importCsv(file.getInputStream(), principal.getName());
    redirect.addFlashAttribute("result", result);
    return "redirect:/admin/switches/import";
}
```

늘어난 import: `org.springframework.web.bind.annotation.RequestParam`, `org.springframework.web.multipart.MultipartFile`, `org.springframework.web.servlet.mvc.support.RedirectAttributes`, `java.io.IOException`.

`templates/admin/switch-import.html`: 파일을 보내는 폼은 `enctype="multipart/form-data"`가 꼭 있어야 한다.

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('CSV 등록')}"></head>
<body>
<header th:replace="~{fragments/layout :: header}"></header>
<main>
    <h1>CSV로 스위치 등록</h1>

    <div th:if="${result}" class="result">
        <p th:text="|새로 등록 ${result.created}개, 이미 있어서 건너뜀 ${result.skipped}개|"></p>
        <ul th:unless="${result.errors.empty}">
            <li class="error" th:each="e : ${result.errors}" th:text="${e}"></li>
        </ul>
    </div>

    <form th:action="@{/admin/switches/import}" method="post" enctype="multipart/form-data">
        <input type="file" name="file" accept=".csv" required>
        <button type="submit">올리기</button>
    </form>
    <p class="muted">첫 줄은 제목 줄이고, UTF-8로 저장한 파일이어야 해요.</p>
</main>
<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

**확인**: 위 예시 세 줄에 일부러 틀린 줄(`Bad,,Row,,LINEAR,abc,,,,,,`)을 하나 더해 올리면 "새로 등록 3개"와 "5번째 줄: 숫자 칸에 숫자가 아닌 값이 있어요"가 나온다. 같은 파일을 한 번 더 올리면 "건너뜀 3개"가 된다. 그다음 진짜 데이터 50개 이상을 올리고 `/switches`에서 필터와 페이지 넘기기를 다시 확인한다.

커밋: `feat(admin): add CSV import for switches` 다음 데이터만 따로 `chore(data): add initial switch dataset`

## 1-7. AWS 첫 배포 (Lightsail)

Lightsail은 AWS에서 서버 한 대를 월 고정 요금으로 빌리는 서비스다. 메모리 2GB짜리(월 12달러) 서버 한 대 안에서 앱(jar)과 DB(PostgreSQL 17)를 같이 돌린다. DB는 내 컴퓨터에서처럼 Docker로 띄우고, 서버 밖에서는 못 들어오게 막는다. 브라우저는 서버의 8090번 포트로 들어온다. 테이블은 앱이 켜질 때 Flyway가 알아서 만든다.

AWS 화면은 자주 바뀌어서 버튼 이름이 조금 다를 수 있다. 막히면 화면을 캡처해서 보내 주면 된다.

### 0) 요금 안전장치

- 오른쪽 위 지역을 \*\*서울(ap-northeast-2)\*\*로 맞춘다.
- Lightsail은 서버를 **중지해도 요금이 나간다.** 요금을 멈추려면 서버를 삭제해야 하고, 그러면 DB도 같이 지워진다.
- 고정 IP는 서버에 붙어 있는 동안은 무료고, 떼어 둔 채로 두면 요금이 붙는다.
- Billing → **Budgets**에서 월 15달러 예산을 만들고 이메일 알림을 켜 둔다. 12달러 외에 뭔가 새고 있으면 바로 알 수 있다.

### 1) jar 만들기 (내 컴퓨터)

```
./gradlew bootJar
```

`build/libs/`에 생긴 `switchbase-0.0.1-SNAPSHOT.jar`를 쓴다. `-plain.jar`가 같이 보이면 그건 실행이 안 되는 반쪽짜리다. Docker Compose Support는 개발용이라 jar에 들어가지 않는다. 그래서 서버에서는 DB 주소를 환경 변수로 알려줘야 한다.

### 2) Lightsail 서버 만들기

Lightsail 콘솔 → **Create instance**.

| 항목 | 값 |
| --- | --- |
| Instance location | Seoul (ap-northeast-2) |
| Platform | Linux/Unix |
| Blueprint | Operating System (OS) only → **Amazon Linux 2023** |
| SSH key pair | Change SSH key pair → Create new → 이름 `switchbase-key` → 내려받은 `.pem` 파일을 잘 보관 (다시 못 받는다) |
| Networking type | Dual-stack (IPv4가 있어야 휴대폰 LTE에서도 열린다) |
| Plan | **$12 USD** (2 GB 메모리, 2 vCPU, 60 GB SSD) |
| Instance name | `switchbase-app` |

만든 뒤 인스턴스를 눌러서 두 가지를 더 한다.

- **Networking** 탭 → IPv4 Firewall → **Add rule**: Application `Custom`, Protocol `TCP`, Port `8090`. 이게 없으면 브라우저에서 접속이 안 된다. 5432(DB)는 열지 않는다.
- 같은 탭에서 **Attach static IP**(또는 Create static IP)로 고정 IP를 붙인다. 안 붙이면 서버를 재부팅할 때마다 주소가 바뀐다. 아래에서 `<고정 IP>`는 이 주소다.

### 3) 서버 접속과 Java 설치

키 파일은 프로젝트 폴더(`C:\switchbase`) 밖, 예를 들어 `C:\keys\`에 둔다. 프로젝트 폴더에 두면 실수로 커밋될 수 있다. Windows는 키 파일 권한이 너무 널널하면 ssh가 거부하므로 PowerShell에서 한 번 잠근다.

```
cd C:\keys
icacls switchbase-key.pem /inheritance:r
icacls switchbase-key.pem /grant:r "$($env:USERNAME):(R)"
ssh -i switchbase-key.pem ec2-user@<고정 IP>
```

ssh가 잘 안 되면 Lightsail 인스턴스 화면의 **Connect using SSH** 버튼으로 브라우저 안에서 같은 터미널을 열 수 있다. 다만 5번의 jar 올리기는 키 파일이 있어야 한다.

접속되면 Java 25를 설치한다. 빌드한 Java 버전과 맞아야 한다.

```
sudo dnf install -y java-25-amazon-corretto-headless
java -version
```

`No match`가 나오면 `dnf search corretto`로 보이는 25 패키지 이름을 대신 쓴다.

### 4) DB 띄우기 (서버에서, Docker)

서버에 Docker를 깔고 PostgreSQL 17을 띄운다. 내 컴퓨터의 `compose.yaml`과 같은 이미지라 동작이 같다.

```
sudo dnf install -y docker
sudo systemctl enable --now docker
```

```
sudo docker run -d --name switchbase-db --restart unless-stopped \
  -e POSTGRES_DB=switchbase \
  -e POSTGRES_USER=switchbase \
  -e POSTGRES_PASSWORD='<DB 비밀번호>' \
  -v pgdata:/var/lib/postgresql/data \
  -p 127.0.0.1:5432:5432 \
  postgres:17
```

- `<DB 비밀번호>`는 새로 정해서 메모해 둔다. 6번에서 한 번 더 쓴다.
- `-p 127.0.0.1:5432:5432`는 "이 서버 안에서만 5432로 들어올 수 있다"는 뜻이다. 그래서 DB가 인터넷에 열리지 않는다.
- `-v pgdata:...`는 데이터를 컨테이너 밖에 보관한다. 컨테이너를 지웠다 다시 만들어도 데이터는 남는다.
- `--restart unless-stopped`라서 서버가 재부팅되면 DB도 같이 켜진다.

`sudo docker ps`에 `switchbase-db`가 `Up`으로 보이면 된다.

### 5) jar 올리기 (내 컴퓨터에서, 새 PowerShell 창)

```
scp -i C:\keys\switchbase-key.pem C:\switchbase\build\libs\switchbase-0.0.1-SNAPSHOT.jar ec2-user@<고정 IP>:~/app.jar
```

### 6) 비밀 값은 환경 변수 파일로 (서버에서)

DB 비밀번호와 관리자 비밀번호는 코드에도, GitHub에도 올리지 않는다. 서버에만 있는 파일에 적는다. Spring Boot는 `SPRING_DATASOURCE_URL` 같은 환경 변수를 `spring.datasource.url` 설정으로 알아듣는다.

```
sudo nano /etc/switchbase.env
```

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/switchbase
SPRING_DATASOURCE_USERNAME=switchbase
SPRING_DATASOURCE_PASSWORD=<4번에서 정한 DB 비밀번호>
ADMIN_USERNAME=<관리자 아이디>
ADMIN_PASSWORD=<다른 곳에서 안 쓰는 긴 비밀번호>
```

저장(Ctrl+O, Enter, Ctrl+X) 후 다른 사용자가 못 읽게 막는다.

```
sudo chmod 600 /etc/switchbase.env
```

### 7) 서비스로 등록해서 띄우기

ssh를 끊어도 계속 돌고, 서버가 재부팅되면 알아서 다시 켜지게 systemd 서비스로 등록한다.

```
sudo nano /etc/systemd/system/switchbase.service
```

```
[Unit]
Description=SwitchBase
After=network.target docker.service
Wants=docker.service

[Service]
User=ec2-user
EnvironmentFile=/etc/switchbase.env
ExecStart=/usr/bin/java -Xmx512m -jar /home/ec2-user/app.jar
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

`After`와 `Wants`는 Docker(그리고 DB)가 먼저 켜지게 하는 줄이다. 재부팅 직후 DB가 아직 준비 중이라 앱이 한 번 실패해도 `RestartSec=5` 때문에 5초 뒤 다시 시도한다.

```
sudo systemctl daemon-reload
sudo systemctl enable --now switchbase
journalctl -u switchbase -f
```

마지막 명령은 로그를 실시간으로 보여준다(나갈 때 Ctrl+C). `now at version v3`와 `Started SwitchbaseApplication`이 보이면 성공이다.

### 8) 다음부터 배포하기

코드를 고친 뒤에는 세 줄이면 된다. 1번(`bootJar`), 5번(`scp`)을 다시 하고 서버에서 재시작한다. DB는 그대로 두면 된다.

```
sudo systemctl restart switchbase
```

이 과정을 GitHub Actions가 대신 하게 만드는 것이 CD다. 손으로 몇 번 해 보고 어느 부분이 귀찮은지 알게 된 뒤에 자동화하는 게 좋다.

### 9) DB 백업 (가끔)

DB가 서버 안에 있어서, 서버가 망가지거나 지워지면 데이터도 같이 사라진다. 데이터를 많이 고친 날에는 서버에서 덤프를 떠서 내 컴퓨터로 가져온다.

```
sudo docker exec switchbase-db pg_dump -U switchbase switchbase > ~/backup.sql
```

```
scp -i C:\keys\switchbase-key.pem ec2-user@<고정 IP>:~/backup.sql C:\switchbase-backup\
```

두 번째 줄은 내 컴퓨터 PowerShell에서 실행한다. `C:\switchbase-backup` 폴더는 미리 만들어 둔다. Lightsail의 Snapshots 탭에서 서버를 통째로 찍어 둘 수도 있지만, 스냅샷은 따로 요금이 붙는다.

**확인**: 휴대폰 LTE로 `http://<고정 IP>:8090/switches`에 들어가진다. 서버 DB는 새로 만든 거라 처음엔 비어 있다. `/admin/switches/import`에서 `.env`에 적은 관리자 계정으로 로그인해 `switches_2026.11.03.csv`를 올리면 서버에도 스위치가 생긴다. `admin1234`로는 로그인이 **안 돼야** 한다.

지금은 `http`라서 로그인 비밀번호가 암호화되지 않고 오간다. 도메인을 연결하고 HTTPS를 붙이는 건 2단계 회원 기능 전에 한다. 그때까지는 다른 곳에서 쓰지 않는 비밀번호를 쓴다.

커밋할 코드는 없다. README에 배포 주소와 구성(Lightsail 한 대, 그 안에 jar와 Docker PostgreSQL 17)을 한 줄 적어 두면 좋다: `docs: add deployment info to README`

## 커밋 순서

한 단계가 끝날 때마다 커밋하고 바로 푸시한다. GitHub Actions가 매번 테스트를 돌려 주니 어느 단계에서 깨졌는지 바로 보인다.

| 단계 | 커밋 메시지 |
| --- | --- |
| 1-1 | `feat(db): add switch revision and spec source tables` |
| 1-2 | `feat(switch): add switch entities and repositories` |
| 1-3 | `feat(switch): add switch list and detail pages` |
| 1-4 | `feat(switch): add switch search filters with QueryDSL` |
| 1-5 | `feat(admin): add admin login and switch editing with revisions` |
| 1-5 | `test(switch): add service and admin access tests` |
| 1-5.5 | feat(switch): add brand, variant and alias search |
| 1-6 | `feat(admin): add CSV import for switches` |
| 1-6 | `chore(data): add initial switch dataset` |
| 1-7 | `docs: add deployment info to README` |

## 막힐 때

| 증상 | 원인과 해결 |
| --- | --- |
| `Validate failed: Migration checksum mismatch` | 이미 적용된 V1/V2 파일을 고쳤다. 원래대로 돌리고 바꾸고 싶은 내용은 V3 파일로 만든다. 로컬 DB는 지워도 되면 `docker compose down -v` 후 다시 켜도 된다 |
| `Schema validation: wrong column type ... [release_year]` | `releaseYear`를 `Integer`로 썼다. `Short`로 바꾼다 |
| `Schema validation: missing column [...]` | `@Column(name = ...)` 철자가 SQL과 다르다 |
| `QKeyboardSwitch`가 빨간색 | 엔티티를 바꾼 뒤 아직 컴파일을 안 했다. Gradle 새로고침 후 `./gradlew compileJava` |
| 모든 페이지가 로그인 화면으로 간다 | Security 의존성만 넣고 `SecurityConfig`를 안 만들었다. 있는데도 그러면 패키지가 `com.switchbase` 아래인지 본다 |
| 저장 버튼을 누르면 403 | 폼에 `th:action`이 아니라 `action`을 써서 CSRF 토큰이 빠졌다 |
| 검색하면 표 안에 표가 겹쳐 나온다 | 검색 폼에 `hx-swap="outerHTML"`이 빠졌다 |
| "수정" 링크가 로그아웃 상태에도 보인다 | `thymeleaf-extras-springsecurity6` 의존성이나 `xmlns:sec`가 빠졌다 |
| CSV 한글이 깨진다 | 엑셀에서 CSV UTF-8 형식으로 다시 저장한다 |
| 칸이 12개여야 하는데 9개예요 | 1-5.5 전의 옛 형식 CSV다. brand, variant, aliases 칸을 더한다. 일부 줄만 칸 수가 다르면 이름에 쉼표가 들어간 것이다 |
| duplicate key ... uk\_keyboard\_switch\_name | 제조사 + 이름 + 변형이 같은 스위치가 이미 있다. 무게가 다른 버전이면 변형 칸을 채운다 |
| `ssh: UNPROTECTED PRIVATE KEY FILE` | 1-7의 `icacls` 두 줄을 실행한다 |
| 서버 로그에 `Connection refused` / `timeout` (DB) | DB 컨테이너가 꺼져 있다. sudo docker ps로 보고, 없으면 sudo docker start switchbase-db |
| 서버 로그에 `database "switchbase" does not exist` | docker run에서 `POSTGRES_DB=switchbase`를 빠뜨렸다. 데이터가 아직 없으면 `sudo docker rm -f switchbase-db`, `sudo docker volume rm pgdata` 후 4번을 다시 한다 |
| 브라우저에서 서버 주소가 안 열린다 | Lightsail Networking 탭 방화벽에 8090 규칙이 없거나, 주소를 `https://`로 열었다 |
| `UnsupportedClassVersionError` | 서버 Java가 25보다 낮다 |

그 밖의 에러는 빨간 글자 첫 줄과 `Caused by:`가 들어간 줄을 복사해서 보내 주면 된다.

## 완료 체크리스트

- [x] 로컬에서 `now at version v2`가 보인다
- [x] `/switches`에서 검색, 종류, 입력압 필터와 페이지 넘기기가 된다
- [x] 관리자로 등록·수정하면 상세 페이지에 수정 이력이 쌓인다
- [x] `./gradlew test` 통과, GitHub Actions 초록색
- [ ] `data/switches.csv`에 출처가 달린 스위치 50개 이상
- [ ] AWS 주소에서 목록이 보이고, 기본 비밀번호로는 관리자 로그인이 안 된다
- [ ] AWS Budgets 알림이 켜져 있고, 서버에 고정 IP가 붙어 있다
