# 통합 사례 연구 — SQL 0행에서 Optional<User>, HTTP 404까지

이 문서는 **다른 계층의 용어를 섞지 않는 훈련**을 위한 독립 사례다. `0`, `NULL`, `null`, `Optional.empty()`, `[]`, `404`는 각각 다른 의미와 책임을 가진다.

## A. 출발점: DB에는 무엇이 있는가?

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  age INT NULL
);
INSERT INTO users(id,name,age) VALUES (1,'Kim',NULL);
SELECT * FROM users WHERE id=99;
SELECT COUNT(*) AS count FROM users WHERE id=99;
SELECT age FROM users WHERE id=1;
```

첫 SELECT는 **0행**이다. 둘째는 **한 행의 count=0**이다. 셋째는 **한 행의 age=NULL**이다. 0행, 숫자 0, 컬럼 NULL은 다르다.

## B. JDBC는 어떻게 구분하는가?

```java
try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM users WHERE id = ?")) {
  ps.setLong(1, 99L);
  try (ResultSet rs = ps.executeQuery()) {
    if (!rs.next()) {
      System.out.println("행 없음");
    } else {
      String name = rs.getString("name");
      System.out.println(name);
    }
  }
}
```

`rs.next()`는 다음 행이 있으면 true다. 0행일 때는 false다. DB가 `Optional`을 보내지 않는다. `getString`이 반환하는 Java null과 행 부재를 섞지 않는다.

## C. JPA와 Spring Data JPA의 API 계약

- `EntityManager.find(User.class, 99L)` → 엔티티가 없으면 null.
- `JpaRepository<User, Long>.findById(99L)` → Optional.empty().
- `JpaRepository<User, Long>.findAll()` → 0건이면 빈 List.
- JPQL 단일 결과 API는 부재 시 예외를 던질 수도 있다.

왜 다른가? **SQL이 아니라 각각의 Java API가 반환 방식을 정하기 때문**이다.

## D. 서비스와 HTTP의 책임

```java
@Service
public class UserService {
  private final UserRepository repository;
  public UserService(UserRepository repository) { this.repository = repository; }
  @Transactional(readOnly = true)
  public User get(long id) {
    return repository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
```

서비스는 “없는 사용자”를 도메인 예외로 해석한다. ControllerAdvice는 예외를 404로 변환할 수 있다. DB가 직접 HTTP 404를 생성하는 것은 아니다.

## E. 독립 실행 실습: 빈 값의 다섯 얼굴

**준비:** Java 17+, MySQL 또는 H2, Spring Boot + Spring Data JPA 프로젝트. JPA 부분을 실행하려면 Entity·Repository·DB 연결 설정이 필요하다.

1. SQL의 0행/COUNT 0/NULL을 직접 실행해 행 수와 컬럼 값을 기록한다.
2. JDBC `ResultSet.next()`로 0행을 확인한다.
3. Java에서 `null`, `List.of()`, `Optional.empty()`를 각각 출력한다.
4. `findById`로 존재/부재를 조회하고 `isPresent`를 검증한다.
5. 단건 404, 목록 200 `[]`, DB 장애 5xx를 별도 테스트한다.
6. 결과를 “DB 계층 / Java API 계층 / HTTP 계층” 표로 분류한다.

## F. 자가 진단 10문항과 정답

1. `SELECT * ... WHERE id=99`에서 매칭이 없으면? → 0행.
2. `SELECT COUNT(*) ... WHERE id=99`(GROUP BY 없음) 결과는? → 0 값을 담은 1행.
3. `SELECT NULL AS value` 결과는? → NULL 컬럼을 가진 1행.
4. `ResultSet.next()`의 의미는? → 다음 결과 행 존재 여부.
5. `ResultSet.getInt()`로 NULL을 읽으면? → 0을 반환할 수 있어 `wasNull()` 검사 필요.
6. `EntityManager.find`에서 엔티티가 없으면? → null.
7. Spring Data JPA `findById`에서 없으면? → Optional.empty().
8. `findAll`에서 없으면? → 빈 List.
9. SQL 0행이면 HTTP 404가 자동인가? → 아니다. 애플리케이션 정책이 결정한다.
10. `Optional.empty()`는 SQL NULL인가? → 아니다. Java API가 값의 부재를 표현한 것이다.

**30초 면접 답변:** “SQL에서 조회 결과가 없다는 것은 일반 SELECT의 결과 행이 0개라는 뜻입니다. 이 결과를 Java가 어떤 형태로 표현하는지는 API에 따라 달라집니다. JDBC에서는 ResultSet의 next로 행 존재를 확인하고, JPA EntityManager.find는 null, Spring Data JPA findById는 Optional.empty를 반환합니다. 서비스와 웹 계층이 부재를 해석해 예외나 HTTP 404로 변환할 수 있습니다.”
