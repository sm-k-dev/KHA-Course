# JPA 25-4: 로딩·운영·MyBatis

> 이 파트는 [25장 JPA 허브](25_JPA.md)의 일부입니다. 이전: [25-3 쿼리](25-3_쿼리.md).

## 4-4. 양방향 연관관계의 순환 참조: toString과 JSON

> 추가 근거: 9_Spring_boot_JPA `04 JPA collection-valued association` 강의 자료 19~22쪽(순환 참조, `@JsonIgnore`, Lombok `@ToString.Exclude`, StackOverflowError 메시지)을 읽고 같은 주제를 실행했다. 아래 두 실행은 **이 PC에서 확인함**(2026-11-24, Java 25, Jackson 3.1.5). Lombok `@Data`의 자동 `toString`은 **미실행**이고, 같은 동작을 손으로 쓴 `toString`으로 재현했다.

### 왜 생기나

`Department.students`가 `Student`를 담고, `Student.department`가 `Department`를 가리키면 객체 그래프가 원을 그린다. `toString()`이나 JSON 변환기가 연관 필드를 따라 들어가면 끝나지 않는다.

### 실행 1: toString 순환 (확인함)

```java
// Lombok @Data가 만드는 toString과 같은 동작을 직접 쓴 예
Department.toString() → "students=" + students       // Student.toString() 호출
Student.toString()    → "department=" + department   // Department.toString() 호출 → 무한 반복
```

결과: `StackOverflowError`가 발생한다. 강의 자료의 `StackOverflowError` 메시지와 같은 원인이다.

**해결:** 한쪽 연관 필드를 `toString`에서 빼거나, Lombok에서 `@ToString.Exclude`로 제외한다. 이 방식은 2026-11-24에 Lombok 1.18.46으로 실행해 확인했다(22장 Lombok 절).

### 실행 2: JSON 직렬화 (확인함)

| 상황 | 결과 (Jackson 3.1.5, `tools.jackson.databind.ObjectMapper`) |
|---|---|
| `Department` → `students` → `Student` → `department` 순환 | `StreamConstraintsException`: 중첩 깊이 500 초과 |
| `Student.department`에 `@JsonIgnore` | `{"name":"소프트웨어공학과","students":[{"name":"최하은"}]}` |

**해석:** 순환이 있으면 직렬화가 끝나지 않는다. Jackson 3는 깊이 500에서 멈추고 오류를 낸다(`StackOverflowError`로 끝나지 않는다). 역방향 필드에 `@JsonIgnore`를 붙이면 한쪽 방향만 나간다.

### 엔티티를 그대로 반환하면 안 되는 이유

- 엔티티를 JSON으로 바로 내보내면 **순환 참조**와 **지연 로딩 예외**(세션 밖에서 연관 접근)가 같이 생긴다.
- 실무에서는 응답 전용 DTO를 만들고, 필요한 필드만 옮긴다(DTO 조회는 [25-2 N+1 비교 절](25-2_연관관계와_N+1.md)에서 다뤘다. 응답 DTO 전용 실습은 이 장에 없음, 미실행).
- 이 절의 `@JsonIgnore`는 빠른 해결책이다. 응답 구조가 복잡해지면 DTO가 낫다.

**강의 04의 콘솔 로그 (강의 자료, 실행 확인과 같은 결과):** `department/list1`은 학과 조회 SQL 1번만 나갔고, `department/list2`에서 학과 4개를 돌며 `st.department`와 `students`를 쓰자 학생 조회 SQL이 학과 수만큼(4번) 나갔다. 위 4-5절의 "학과 3개 → 추가 3회"와 같은 원리다. 강의 04의 `list2.html`은 `th:each="dp : ${departments}"` 안에 `th:each="st : ${dp.students}"`를 중첩해서 쓴다. 이 화면은 미실행이다.

**면접 기준:** "양방향 연관관계는 객체 그래프가 순환하므로 `toString`과 JSON 직렬화에서 무한 반복이 생긴다. 한쪽에 `@JsonIgnore`를 붙이거나, 엔티티 대신 DTO로 응답한다."

## 4-5. 기본 로딩 전략: 단일 값은 EAGER, 컬렉션은 LAZY (9_JPA 02·04 기준, SQL 수 확인함)

> 추가 근거: 9_Spring_boot_JPA `02 JPA single-valued association`과 `04 JPA collection-valued association`의 로딩 설명(eager/lazy)을 읽고 보강했다. 아래 SQL 수는 **Hibernate 통계(`prepareStatementCount`)로 센 값**이다(2026-11-24, Hibernate 7.4.5, MySQL 8.0.21). 학과 3개, 학생 4명(학과 1, 2에 2명씩 배정)으로 실행했다.

| 실행 | SQL 수 (확인함) | 해석 |
|---|---|---|
| `select d from Department d` (학과 3개) | **1** | 학과만 조회. 학생 목록은 가져오지 않았다 |
| 위에서 읽은 학과 3개의 `d.students`를 순회 | 추가 **3** | 컬렉션 접근 시점마다 학과 하나씩 학생을 조회했다 (LAZY, N+1) |
| `select s from Student s` (학생 4명) | **3** | 학생 1번 + 학과 2개를 따로 조회 (`@ManyToOne` 기본 EAGER) |
| 위 학생들의 `s.department.name` 접근 | 추가 **0** | 이미 EAGER로 채워져 있어서 추가 조회가 없다 |

**기본값 정리 (확인함 + 강의 설명):**
- `@ManyToOne`, `@OneToOne`은 기본이 EAGER다. 엔티티를 읽을 때 연관 객체를 같이 읽는다.
- `@OneToMany`, `@ManyToMany`는 기본이 LAZY다. 컬렉션을 실제로 쓸 때 읽는다.
- 강의 자료의 설정은 `@ManyToOne(fetch = FetchType.LAZY)`, `@OneToMany(mappedBy = "department", fetch = FetchType.EAGER)`처럼 바꿀 수 있다고 설명한다. 이 설정을 바꾼 실행은 **미실행**이다.

**장점과 단점 (강의 설명):** EAGER는 필요 없는 데이터까지 읽어서 낭비될 수 있고, LAZY는 필요할 때 추가 조회가 생겨서 시간이 든다. 어느 쪽이든 연관 조회가 항상 같이 필요한 것은 아니므로, 목록 화면에서 필요한 연관만 fetch join이나 DTO로 가져오는 방법(3장)을 함께 쓴다.

**면접 기준:** "단일 값 연관은 기본 EAGER, 컬렉션은 기본 LAZY입니다. 목록에서 컬렉션을 순회하면 N+1이 생기므로 fetch join이나 DTO 조회로 한 번에 가져옵니다. 설정 변경은 SQL 수로 확인합니다."

## 5. MyBatis와 JPA 비교 (국내 실무 대비)

- **JPA:** 객체 중심. 엔티티를 저장하고 조회하면 SQL을 자동으로 만든다. 변경 감지로 UPDATE가 자동으로 나간다.
- **MyBatis:** SQL 중심. 쿼리를 직접 작성하고 결과를 객체에 매핑한다. 쿼리를 정확히 통제할 수 있다.
- **쓰는 방식:** 단순 CRUD와 객체 관계는 JPA, 복잡한 통계나 조회 쿼리는 MyBatis를 섞어 쓰는 경우가 많다.
- **실습:** 같은 조회(예: 사용자별 주문 수)를 JPQL과 MyBatis 매퍼 XML로 각각 작성하고, 실행되는 SQL 로그를 비교한다.
- **면접 기준:** "JPA는 객체를 기준으로 SQL을 자동 생성하고, MyBatis는 SQL을 직접 작성해서 객체에 매핑합니다."

### MyBatis 사용법 실습 (2026-11-24, MyBatis 3.5.16 · MySQL 8.0.21로 확인함)

> 이 절은 Spring 없이 MyBatis 코어만 쓴 실행 결과다. `mybatis-spring-boot-starter`와 `@Mapper`를 쓰는 스프링 부트 연동은 아래 5-2절에서 확인했다. 공식 설정 방법은 MyBatis 공식 문서를 기준으로 하고, 이 장에서는 같은 동작을 직접 확인했다.

**구성 요소 (확인함)**

| 파일 | 역할 |
|---|---|
| `StudentMapper.java` | 메소드 시그니처만 있는 인터페이스. 구현 클래스는 MyBatis가 만든다 |
| `StudentMapper.xml` | 같은 이름의 XML. 인터페이스 메소드마다 SQL을 쓴다. 클래스패스에서 짝으로 읽힌다 |
| `Configuration` + `SqlSessionFactory` | DataSource, 트랜잭션 방식, 매퍼 등록 |
| `SqlSession` | 요청 한 번에 쓰는 세션. `getMapper()`로 매퍼를 얻고 `commit`/`rollback`으로 끝낸다 |

**인터페이스와 XML의 연결 (확인함):** `cfg.addMapper(StudentMapper.class)`를 하면 MyBatis는 `StudentMapper.xml`을 찾아 `namespace="StudentMapper"`와 메소드 이름(`id`)으로 연결한다. XML의 `id`와 인터페이스 메소드 이름이 다르면 실행할 때 오류가 난다고 알려져 있으나, 이 실행에서는 일부러 어긋나게 하지 않아 확인하지 않았다(미확인).

```xml
<select id="findById" resultMap="studentWithDept">
    SELECT ... FROM student s JOIN department d ON s.department_id = d.id
    WHERE s.id = #{id}
</select>
```

**결과 매핑: `resultMap` (확인함)**
- 컬럼 이름과 필드 이름이 다르면 `resultMap`에서 `<result property="studentNo" column="student_no"/>`처럼 짝을 준다. 컬럼 별칭(`AS student_name`)으로 이름을 맞출 수도 있다.
- 단일 값 연관(`@ManyToOne`에 해당)은 `<association>`으로 묶는다. 실행 결과 `[소프트웨어공학과]`가 학생 객체 안에 채워졌다.
- 컬렉션 연관(`@OneToMany`에 해당)은 `<collection ofType="Student">`로 묶는다. 학과 1 → 학생 2명, 학과 2 → 학생 1명이 나왔다. 조인 결과 행 여러 개가 한 객체로 합쳐진다.

**동적 SQL: `<where>`와 `<if>` (확인함)**

| 호출 | 실행 결과 (확인함) |
|---|---|
| `search("김", null)` | 김숙홍, 김영우 (이름 포함 검색) |
| `search(null, 2)` | 박남기, 김영우 (학과 2) |
| `search(null, null)` | 4건 (조건 없음) |

`<where>`는 조건이 하나도 없으면 `WHERE`를 붙이지 않고, 앞쪽 `AND`를 자동으로 지운다. 실행된 SQL을 확인하면 `WHERE s.name LIKE CONCAT('%', ?, '%') ORDER BY s.id`처럼 조건이 있는 `<if>`만 남는다.

**`#{}`와 `${}`의 차이 (확인함)**
- `#{name}`: 값을 **바인딩 파라미터(`?`)** 로 보낸다. 값에 SQL 문법이 있어도 문자열로 취급된다.
- `${name}`: 값을 **SQL 문자열에 그대로 붙인다**. 테이블 이름처럼 바인딩할 수 없는 자리에만 쓴다.
- 같은 공격 문자열 `' OR '1'='1`을 넣었을 때 `${}` 검색은 **4건**(전체 행)이 나왔고, `#{}` 검색은 **0건**이었다. 30장의 SQL 인젝션 실험과 같은 결과다.
- 교육용 비교 예제만 `${}`를 쓴다. 실제 코드에서 `${}`에 사용자 입력을 넣지 않는다.

**INSERT와 생성된 키 (확인함)**
- `useGeneratedKeys="true" keyProperty="id"`를 쓰면 저장 후 `student.id`에 DB가 만든 번호가 들어간다(1, 2, 3, 4가 나왔다).
- `#{department.id}`처럼 중첩 속성을 쓰면 `Student.department` 객체의 `id`가 들어간다.

**UPDATE, DELETE의 영향 행 수 (확인함)**
- `int updated = mapper.updateName(1, "최하은2")`은 바뀐 행 수 `1`을 돌려준다.
- 영향 행 수는 실제로 바뀐 행 수다. 없는 `id`를 지울 때의 결과는 이 실행에서 확인하지 않았다(미확인). 결과 확인은 호출자가 한다.

**트랜잭션 (확인함)**
- `factory.openSession(false)`는 자동 커밋을 끈 세션이다. 여러 변경을 모아서 `session.commit()`으로 한 번에 확정하거나, `rollback()`으로 되돌린다.
- 실행에서는 마지막에 `rollback()`을 호출했다.

**JPA와 비교해서 기억할 점**
- JPA는 엔티티 변경을 추적해서 SQL을 만든다. MyBatis는 **SQL을 직접 쓰고** 결과를 객체에 옮긴다.
- 복잡한 통계, 조인과 동적 조건이 많은 조회는 MyBatis가 SQL을 그대로 보여 줘서 튜닝과 검토가 쉽다.
- 객체 저장과 변경 추적은 JPA가 편하다. 두 기술을 같이 쓰는 프로젝트가 많다.

**면접 기준:** "MyBatis는 SQL을 개발자가 쓰고, 인터페이스 메소드와 XML의 `id`를 연결해 결과를 객체에 매핑합니다. 값은 `#{}`로 바인딩해서 SQL 인젝션을 막고, `${}`는 바인딩할 수 없는 식별자에만 씁니다."

### 5-2. 스프링 부트 연동: mybatis-spring-boot-starter와 @Mapper (2026-11-24, 확인함)

위 MyBatis 코어 실습은 Spring 없이 설정을 직접 했다. 이 절은 **Spring Boot 4.1.1**에서 `mybatis-spring-boot-starter` 4.0.0을 붙인 프로젝트 `mybatisapp`을 Maven으로 빌드해 서버를 띄운 결과다. 데이터베이스는 이 PC의 MySQL 8.0.21 `khac_mybatis_demo`를 썼다.

```java
@Mapper
public interface StudentMapper {

    // #{}는 값을 바인딩하므로 SQL 인젝션을 막는다.
    @Select("SELECT id, name, dept FROM mb_student WHERE dept = #{dept} ORDER BY id")
    List<Student> findByDept(@Param("dept") String dept);

    @Select("SELECT dept, COUNT(*) AS cnt FROM mb_student GROUP BY dept ORDER BY dept")
    List<DeptCount> countByDept();

    record Student(Long id, String name, String dept) {}
    record DeptCount(String dept, Long cnt) {}
}
```

```properties
# application.properties (발췌)
spring.sql.init.mode=always
mybatis.configuration.map-underscore-to-camel-case=true
```

`schema.sql`로 `mb_student` 테이블을 만들고 `data.sql`로 3행(CS 2명, MATH 1명)을 넣었다. `@Mapper` 인터페이스는 스타터가 구현체를 만들어 빈으로 등록하므로, 컨트롤러는 생성자로 주입만 받는다.

**확인 결과 (확인함)**

| 요청 | 응답 |
|---|---|
| `GET /students?dept=CS` | 200, 2행(`김하나`, `이둘`) |
| `GET /students?dept=MATH` | 200, 1행(`박셋`) |
| `GET /dept-counts` | 200, `[{"dept":"CS","cnt":2},{"dept":"MATH","cnt":1}]` |
| `GET /students?dept=' OR '1'='1` | 200, `[]` (값으로 바인딩되어 조건을 깨지 못함) |

- 한글은 응답 바이트를 UTF-8로 풀어 `김하나`, `이둘`, `박셋`을 확인했다(`Content-Type: application/json`).
- `cnt` 컬럼은 `record`의 `Long cnt`로 매핑되었다. `map-underscore-to-camel-case`는 이 예에서 필요하지 않았지만 켜 두었다.

**환경 메모**: Windows JVM에서 Tomcat NIO 루프백 문제가 같아서, 22장과 같은 NIO2 프로토콜 우회(`TomcatServletWebServerFactory` 빈)를 썼다. 이 우회는 실습 환경 한정이다.

**미실행 범위**: XML 매퍼(`*.xml`)와 `@Select` 방식을 섞은 동적 SQL(`<if>`, `<foreach>`), 트랜잭션 연동, 매퍼 XML의 `id` 불일치 오류는 이번에 확인하지 않았다. 강의의 JPA 화면과 MyBatis 화면 비교도 미실행이다.

