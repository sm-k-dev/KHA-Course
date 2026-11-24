# JPA·Hibernate·Spring Data JPA

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `10_SpringBoot_3_4`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — JPA · Hibernate · Repository · Optional

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

JPA는 Java 영속성 기술의 **명세**, Hibernate는 대표적인 **구현체**, Spring Data JPA는 리포지토리 추상화를 제공하는 프로젝트다. `Optional.empty()`는 Java API가 결과 부재를 표현하는 방법이다.

### 2. 용어를 구분해야 하는 이유

SQL 0행과 `Optional.empty()`는 같은 계층의 개념이 아니다. JPA의 `flush`는 변경을 DB에 동기화하는 과정이며, 항상 트랜잭션 commit을 뜻하지 않는다.

### 3. 새로운 상황에 적용

`findById(99L)`가 `Optional.empty()`를 반환할 때 DB 결과, 영속성 계층 반환 타입, HTTP 404 선택을 각각 구분한다.

### 4. 면접 질문과 답변 기준

**질문:** JPA · Hibernate · Repository · Optional의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** JPA는 명세, Hibernate는 구현체, Spring Data JPA는 데이터 접근 코드를 줄이는 추상화입니다. 조회 결과 부재의 표현은 사용하는 API 계약에 따라 달라집니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. JPA·Hibernate·Spring Data JPA

**정의** — JPA는 Java 객체와 관계형 데이터의 매핑·영속성 관리를 정의하는 명세이며 Hibernate는 대표 구현체, Spring Data JPA는 JPA 기반 Repository 구현을 편리하게 하는 프로젝트다.

**목적과 책임** — 객체 중심의 데이터 접근을 돕는다.

**구별해야 할 개념 / 흔한 오해** — JPA는 라이브러리 이름이 아니며 Spring Data JPA가 Hibernate 자체를 대체하지 않는다.

**실행·처리 흐름** — Repository 호출 → JPA EntityManager → Hibernate → JDBC → DB

**직접 구현·검증** — 같은 조회를 JDBC와 JPA로 구현해 SQL과 코드 차이 비교. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — JPA와 Hibernate, Spring Data JPA의 관계는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

JPA(현 Jakarta Persistence)는 Java ORM을 위한 표준 명세이며 Hibernate는 대표 구현체다. Spring Data JPA는 JPA 기반 Repository를 편리하게 구성하는 프로젝트다. ORM이 SQL을 없애는 것은 아니며 실제 SQL과 실행 계획을 이해해야 한다.

### 직접 읽고 구현하는 독립 예제

```java
@Entity
class Book { @Id @GeneratedValue Long id; String title; }
interface BookRepository extends JpaRepository<Book,Long> {}
```


### 실행 검증 체크포인트

엔티티 저장과 조회 시 SQL 로그를 확인한다.

### 스스로 작성할 변형 실습

JPA EntityManager와 Spring Data Repository의 역할을 비교한다.

### 면접 답변의 기준 문장

> JPA는 명세, Hibernate는 구현체, Spring Data JPA는 저장소 접근 추상화입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“JPA는 Java 객체와 관계형 데이터의 매핑·영속성 관리를 정의하는 명세이며 Hibernate는 대표 구현체, Spring Data JPA는 JPA 기반 Repository 구현을 편리하게 하는 프로젝트다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“JPA는 라이브러리 이름이 아니며 Spring Data JPA가 Hibernate 자체를 대체하지 않는다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“JPA와 Hibernate, Spring Data JPA의 관계는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 1-2. JPA Repository 기본 메소드: save, findById, deleteById, count (9_Spring_boot_JPA 01 기준, 확인함)

> 추가 근거: 9_Spring_boot_JPA `01 JPA 기초` 강의 자료(17쪽)를 읽고 보강했다. 아래 결과는 **Spring Data JPA 4.1.1 + Hibernate 7.4.5 + MySQL 8.0.21에서 `JpaRepositoryFactory`로 직접 실행한 결과**다(2026-11-24). 스프링 컨테이너, `DepartmentController`, `ModelMapper` 화면 흐름은 **미실행**이다.

```java
public interface DepartmentRepository extends JpaRepository<Department, Integer> {}
```

인터페이스만 선언하면 `findAll`, `findById`, `save`, `deleteById`, `count` 구현이 자동으로 생긴다.

| 호출 | 실행 결과 (확인함) |
|---|---|
| `save(d)`, `d.id == 0` (새 객체) | INSERT. 반환된 객체의 `id`가 `1`이 된다 |
| `save(d)`, `d.id != 0` (기존 객체, 이름 변경) | UPDATE. `count`는 그대로 `1`, 이름이 바뀌었다 |
| `save(d)`, 새 객체 두 번째 | `count = 2` |
| `findById(999)` (없는 id) | `Optional.empty()`. `isPresent() = false` |
| `findById(id)` (있는 id) | `isPresent() = true` |
| `deleteById(existing)` | 행 하나 삭제, `count` 감소 |
| `deleteById(999)` (없는 id) | **예외 없이 끝났다** (Spring Data 4.1.1에서 관찰함) |

**주의 (확인함):** 트랜잭션 없이 `save`를 호출하면 **아무것도 저장되지 않았다**(`count = 0`). 실제 스프링 앱에서는 리포지토리 메소드에 `@Transactional`이 붙어 있어서 생기지 않지만, 독립 실행에서는 `EntityManager`의 트랜잭션을 직접 열어야 한다.

**강의의 조회 후 처리 패턴 (미실행):** `Optional<Department> d = repo.findById(id)`를 받고, `d.isEmpty()`이면 `ResponseStatusException(HttpStatus.NOT_FOUND)`를 던져 404로 응답한다. 이 패턴은 Optional을 쓰는 이유(null 대신 빈 상자)를 보여 준다.

**강의 02의 ModelMapper 매핑 (ModelMapper 3.2.5로 확인함):** `Student student = modelMapper.map(studentEdit, Student.class)`에서 `studentEdit.departmentId = 2`는 `student.department.id = 2`로 채워졌다. `studentNo`, `name`, `sex`, `email` 같은 같은 이름의 필드도 함께 복사되었다. 실행은 getter/setter를 가진 DTO 두 개로 했다.

**강의의 물리 이름 설정 (미실행):** `spring.jpa.hibernate.naming.physical-strategy=...PhysicalNamingStrategyStandardImpl`을 쓰면 `studentNo`가 그대로 컬럼명이 된다. 기본 설정(스프링 부트)은 카멜 케이스를 스네이크 케이스(`student_no`)로 바꾼다는 강의 설명은 이 실행에서 확인하지 않았다.

**면접 기준:** "`save`는 식별자가 없거나 0이면 INSERT, 있으면 UPDATE다. `findById`는 결과 부재를 `Optional.empty()`로 돌려준다. 이 호출들은 트랜잭션 안에서 실행되어야 한다."

## 2. 영속성 컨텍스트·변경 감지

**정의** — 영속성 컨텍스트는 관리되는 엔티티의 동일성·상태를 추적하는 환경이며 변경 감지는 관리 엔티티의 상태 변경을 감지해 적절한 시점에 SQL 변경을 반영하는 메커니즘이다.

**목적과 책임** — 동일성 보장, 1차 캐시, 쓰기 지연·변경 추적 등을 지원한다.

**구별해야 할 개념 / 흔한 오해** — save를 호출할 때마다 즉시 SQL이 실행된다고 단정할 수 없다. flush와 commit은 다르다.

**실행·처리 흐름** — 트랜잭션 → 조회·관리 → 상태 변경 → flush → SQL → commit

**직접 구현·검증** — 트랜잭션 내부 엔티티 수정 후 UPDATE SQL 발생 시점 관찰. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 영속성 컨텍스트와 DB 트랜잭션은 어떻게 다른가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

영속성 컨텍스트는 EntityManager가 관리하는 엔티티 인스턴스와 동일성·변경 추적을 제공하는 단위다. 변경 감지는 관리 상태 엔티티의 변경을 감지해 flush 시 SQL 반영을 준비한다. flush와 commit은 동일하지 않다.

### 직접 읽고 구현하는 독립 예제

```java
@Transactional
public void rename(long id,String title){
 Book b=entityManager.find(Book.class,id);
 b.setTitle(title); // 관리 상태에서 변경 감지
}
```


### 실행 검증 체크포인트

트랜잭션 커밋 시 UPDATE가 나가는지 SQL 로그로 확인한다.

### 스스로 작성할 변형 실습

detach 이후 변경과 merge의 차이를 실험한다.

### 면접 답변의 기준 문장

> 영속성 컨텍스트는 엔티티를 관리하고 변경 감지는 관리 상태 변경을 SQL 반영 대상으로 추적합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“영속성 컨텍스트는 관리되는 엔티티의 동일성·상태를 추적하는 환경이며 변경 감지는 관리 엔티티의 상태 변경을 감지해 적절한 시점에 SQL 변경을 반영하는 메커니즘이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“save를 호출할 때마다 즉시 SQL이 실행된다고 단정할 수 없다. flush와 commit은 다르다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“영속성 컨텍스트와 DB 트랜잭션은 어떻게 다른가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. N+1·JOIN FETCH·페이징

**정의** — N+1은 연관 데이터 접근 과정에서 초기 조회 1회 외에 추가 조회가 반복되는 성능 문제다. fetch join은 연관 데이터를 한 쿼리로 조회할 수 있지만 컬렉션 fetch join과 페이징 조합에는 주의가 필요하다.

**목적과 책임** — SQL 발생 횟수·데이터 양·페이지 정확성을 함께 관리한다.

**구별해야 할 개념 / 흔한 오해** — 모든 연관관계를 EAGER로 바꾸는 것이 해결책은 아니다.

**실행·처리 흐름** — 목록 조회 → 연관 접근 → 추가 SQL 확인 → fetch 전략 조정

**직접 구현·검증** — 게시글 10건과 작성자 조회 SQL 수를 측정하고 개선. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — N+1은 왜 발생하고 어떻게 진단하는가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

N+1은 첫 조회 후 연관 데이터 접근으로 추가 조회가 반복되는 문제다. fetch join은 관련 데이터를 한 번에 가져올 수 있지만 컬렉션 fetch join과 DB 페이징을 결합하면 중복·메모리 페이징 문제가 생길 수 있다. DTO 프로젝션·배치 조회 등 대안을 비교한다.

### 직접 읽고 구현하는 독립 예제

```java
@Query("select b from Book b join fetch b.author where b.id = :id")
Optional<Book> findDetail(@Param("id") Long id);
```


### 실행 검증 체크포인트

SQL 로그에서 기본 조회와 fetch join의 쿼리 수를 비교한다.

### 스스로 작성할 변형 실습

1:N 컬렉션 조회에 페이징이 필요하면 ID 페이징+일괄 조회 설계를 제안한다.

### 면접 답변의 기준 문장

> N+1은 연관 데이터 접근으로 쿼리가 반복되는 현상이고 fetch join은 해결책 중 하나입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“N+1은 연관 데이터 접근 과정에서 초기 조회 1회 외에 추가 조회가 반복되는 성능 문제다. fetch join은 연관 데이터를 한 쿼리로 조회할 수 있지만 컬렉션 fetch join과 페이징 조합에는 주의가 필요하다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“모든 연관관계를 EAGER로 바꾸는 것이 해결책은 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“N+1은 왜 발생하고 어떻게 진단하는가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

### N+1 해결 비교: 지연 로딩, batch size, EntityGraph (2026-11-23 보강, Hibernate 7.4.5에서 SQL 수 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**실험 조건**: 작가 5명, 작가마다 책 2권(총 10개). 작가 목록을 읽고 모든 작가의 책 수를 센다.

| 방식 | 설정 | SQL 개수 (확인함) | 책 수 |
|---|---|---|---|
| 기본 지연 로딩 | 아무것도 안 함 | **6** (목록 1 + 작가마다 1) | 10 |
| batch size | `hibernate.default_batch_fetch_size = 10` | **2** (목록 1 + 책 묶음 1) | 10 |
| EntityGraph | 목록을 읽을 때 `books`를 함께 지정 | **1** (조인으로 한 번에) | 10 |

```java
// EntityGraph: 목록 조회 때 책까지 한 번에 가져온다
EntityGraph<Author> graph = em.createEntityGraph(Author.class);
graph.addAttributeNodes("books");
List<Author> authors = em.createQuery("select s from Author s", Author.class)
        .setHint("jakarta.persistence.fetchgraph", graph)
        .getResultList();

// batch size: 설정 한 줄 (로딩 시점에 여러 작가의 책을 IN 절로 묶는다)
cfg.setProperty("hibernate.default_batch_fetch_size", "10");
```

**읽는 법**
- **지연 로딩**: 책을 실제로 쓰는 순간 작가마다 SELECT가 나간다. 작가 수가 늘면 SQL도 같이 늘어난다.
- **batch size**: 코드는 그대로 두고 설정만 바꾼다. 여러 작가의 책을 한 번에 가져와 SQL 수를 줄인다. 기존 코드를 고치기 어려울 때 쓴다.
- **EntityGraph**: 필요한 연관 데이터를 **목록 조회 시점에 명시**한다. SQL은 가장 적지만, 목록 쿼리마다 어떤 연관을 가져올지 정해야 한다.

**주의**
- 컬렉션(책 목록)을 fetch join이나 EntityGraph로 가져오면서 **페이징**을 함께 쓰면 DB 페이징이 아니라 메모리에서 잘린다. 이때는 batch size나 2단계 조회를 쓴다.
- 책 **개수만** 필요하면 위 방법들보다 DTO 집계 조회가 더 가볍다(위 "N+1 (SQL 수 세기)" 절).

### 컬렉션 연관관계: cascade와 orphanRemoval (2026-11-23 보강, Hibernate 7.4.5에서 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**정의**
- **cascade**: 부모에 대한 작업(저장, 삭제)을 자식에게도 전달한다.
- **orphanRemoval**: 부모의 컬렉션에서 뺀 자식을 DB에서도 지운다.

```java
@Entity
static class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Item5> items = new ArrayList<>();
}

@Entity
static class Item5 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne(fetch = FetchType.LAZY) Order order;
}
```

**확인 결과 (확인함)**

```
1) items saved with parent: 2          ← 부모만 persist해도 자식 2개가 저장됐다
2) items after removing one from collection: 1   ← 컬렉션에서 뺀 자식이 DB에서 삭제됐다
3) items after deleting parent: 0, orders=0      ← 부모를 지우면 자식도 함께 지워졌다
```

**읽는 법**
- 자식 쪽 `order` 필드(주인)에 외래키가 있다. 컬렉션은 거울(`mappedBy`)이다(위 "연관관계의 주인" 절).
- `cascade`를 빼면 자식을 따로 `persist`해야 하고, 빼먹으면 자식이 저장되지 않는다.
- `orphanRemoval = true`를 빼면 컬렉션에서 뺀 자식은 외래키만 `null`이 되고 행은 남는다.

**주의**
- `cascade = ALL`은 삭제도 전달한다. 부모가 자식의 생명주기를 소유할 때만 쓴다. 여러 부모가 같은 자식을 공유하면 쓰지 않는다.
- `orphanRemoval`은 컬렉션에서 뺀 것을 지운다. 실수로 `remove`하면 데이터가 사라지므로 주의한다.

**면접 기준**: "컬렉션 연관관계에서 부모의 생명주기를 자식이 따르면 `cascade`를, 컬렉션에서 뺀 자식도 지우려면 `orphanRemoval`을 씁니다. 외래키는 자식(다) 쪽에 있습니다."

### 동적 검색: Specification과 distinct (2026-11-24 보강, MySQL 8.0과 Hibernate 7.4.5에서 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**구조**: 검색어 `kw`가 들어오면 조건을 **런타임에** 만든다. 제목에서 찾고, 답변 내용에서 찾고, 작성자 이름에서 찾는다. 답변과 작성자는 조인이 필요하다.

```java
private Specification<Question> search(String kw) {
    return new Specification<>() {
        @Override
        public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
            query.distinct(true);                                   // 같은 질문이 여러 번 나오지 않게 한다
            Join<Question, SiteUser> u = q.join("author", JoinType.LEFT);
            Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
            String p = "%" + kw + "%";
            return cb.or(cb.like(q.get("subject"), p),
                         cb.like(a.get("content"), p),
                         cb.like(u.get("username"), p));
        }
    };
}
```

**중복이 생기는 이유**: 답변이 여러 개인 질문은 조인하면 **답변 수만큼 행이 늘어난다.** 질문 1에 검색어가 맞는 답변이 2개 있으면 SQL 결과는 그 질문이 2행으로 나온다.

**확인 결과 (확인함)**

| 조회 방식 | 결과 |
|---|---|
| SQL, `DISTINCT` 없음 | **4행** (질문 1이 2행, 나머지 각 1행) |
| SQL, `COUNT(DISTINCT q.id)` | **3개 질문** |
| Hibernate, `query.distinct(true)` 사용 | 3개 질문 |
| Hibernate, `distinct` 없이 엔티티 목록 조회 | 3개 질문 |

Hibernate의 엔티티 목록은 중복을 제거해서 `distinct`를 빼도 3개로 보였다. 그러나 **SQL 행은 4행**이다. 그래서 중복 문제는 **개수 계산과 페이징**에서 드러난다. 한 페이지에 10개를 보여 줄 때 중복 행이 섞이면 페이지 크기와 전체 개수가 틀어질 수 있다. 그래서 `distinct`를 명시한다.

**주의**
- `LEFT JOIN`을 써야 답변이 없는 질문도 검색된다. `INNER JOIN`이면 답변이 없는 질문이 빠진다.
- `kw`를 그대로 `LIKE`에 넣으면 `%`와 `_`가 특수 문자로 해석된다. 사용자가 입력한 값은 이스케이프하는 것이 안전하다.
- 답변 내용을 검색하면서 HTML을 허용하면 화면 출력에서 XSS가 생길 수 있다(99장). 실습 프로젝트는 Markdown을 HTML로 바꾸는 `commonmark`를 쓰므로, 원시 HTML 출력 옵션을 확인한다. 이 부분은 라이브러리가 PC에 없어서 실행하지 않았다(미실행).

**면접 기준**: "검색 조건이 선택적이면 Specification이나 QueryDSL로 조건을 조립합니다. 답변처럼 여러 행과 조인하면 중복이 생기므로 `distinct`로 질문 단위를 맞추고, 개수와 페이징이 맞는지 확인합니다."

## 4. Page·Slice·COUNT

**정의** — Page는 전체 건수·페이지 정보를 포함하는 결과 모델이고 Slice는 다음 구간 존재 여부 중심의 결과 모델이다.

**목적과 책임** — 검색·정렬·페이지 응답의 계약을 정의한다.

**구별해야 할 개념 / 흔한 오해** — Page는 전체 건수 계산 비용이 있을 수 있으며 Slice는 전체 페이지 수를 알 필요가 없는 UX에 유리하다.

**실행·처리 흐름** — 검색 조건 → content 조회 → 필요시 count 조회 → 응답 구성

**직접 구현·검증** — 검색 조건 2개 조합과 페이지 이동을 테스트. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Page와 Slice, OFFSET과 키셋의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 실습: Pageable로 페이지 나누기 (확인함, MySQL 8.0.21·Spring Data JPA 4.1.1)

> 추가 근거: 9_Spring_boot_JPA `03 JPA pagination` 강의 자료 2~3, 8~20쪽을 읽고 보강했다. 4~7쪽(프로젝트 생성과 설정 화면)은 읽지 않았다. 아래 값은 **학생 23명을 넣고 실행한 결과**다. 스프링 컨테이너와 웹 화면(`list.html`, `Pagination` 모델을 쓴 목록 화면)은 **미실행**이다.

```java
// 리포지토리는 JpaRepository를 상속만 한다. findAll(Pageable)은 자동으로 구현된다.
Page<Student> page = repo.findAll(PageRequest.of(pg - 1, sz, Sort.by(Sort.Direction.ASC, "id")));
```

| 호출 | 결과 (확인함) |
|---|---|
| `PageRequest.of(0, 10, id ASC)` (1쪽) | content 10건, `totalElements=23`, `totalPages=3`, `hasNext=true`, 첫 학번 `20150001`, 마지막 `20150010` |
| `PageRequest.of(2, 10, id ASC)` (3쪽) | content 3건, 첫 학번 `20150021`, `hasNext=false` |
| `PageRequest.of(5, 10)` (6쪽, 없는 페이지) | content 0건, `totalElements=23` (전체 건수는 그대로) |

**핵심 규칙:**
- `PageRequest`의 페이지 번호는 **0부터** 시작한다. 화면 번호(1부터)를 쓰려면 `pg - 1`을 넘긴다. 강의 자료의 `pagination.getPg() - 1`이 이 뜻이다.
- `Page`는 `getContent()`(현재 페이지 목록), `getTotalElements()`(전체 건수), `getTotalPages()`(전체 쪽 수), `hasNext()`를 준다.
- 없는 페이지를 요청하면 **오류가 아니라 빈 목록**이다. 전체 건수는 여전히 정확하다.

**강의 자료의 `Pagination` 모델 계산 (확인함):**

| 화면 쪽 `pg` | `getFirstRecordIndex()` = `(pg-1)*sz` | `getQueryString()` |
|---|---|---|
| 1 | 0 | `pg=1&sz=10` |
| 2 | 10 | `pg=2&sz=10` |
| 3 | 20 | `pg=3&sz=10` |

- 전체 쪽 수는 `ceil(전체 건수 / sz)`로 구하며, 23건이면 `ceil(2.3) = 3`이다.
- `getQueryString()`은 목록 화면에서 "수정 후 같은 쪽으로 돌아가기" 링크에 붙인다(강의 자료의 `redirect:list?` + `getQueryString()`).

**주의:** 강의 자료의 컨트롤러는 `page.getTotalElements()`를 `Pagination.recordCount`에 넣는다. `Page`는 전체 건수를 세는 count 쿼리가 함께 나가는 것이 일반적이다(이 실행에서는 SQL 로그를 확인하지 않아 미확인). 건수가 필요 없는 무한 스크롤에는 `Slice`가 더 가볍다(Slice는 미실행).

**페이지 번호 블록 (강의 `common.js`, Node에서 확인함):** 강의의 페이지 번호 계산 코드를 최소 DOM 스텁으로 실행했다. 데이터 1건부터 `Math.ceil(건수 / 쪽 크기)`로 쪽 수를 구하고, 번호는 10개씩 묶는다.

| 조건 | 출력 (확인함) |
|---|---|
| 300건, 15개씩, 3쪽 | `1 2 3(현재) 4 5 6 7 8 9 10 >` |
| 300건, 15개씩, 11쪽 | `< 11(현재) 12 … 20` (다음 블록 버튼 `>` 없음) |
| 47건, 15개씩, 2쪽 | `1 2(현재) 3 4` (쪽 수 4) |

위 결과는 강의 화면 캡처(3쪽에서 1~10과 `>`)와 같은 블록 구성이다. 강의 코드는 PDF 이미지에서 옮겨 적었으므로 오탈자 가능성은 있다.

**면접 기준:** "Spring Data의 `Pageable`은 페이지 번호를 0부터 세고, `Page`는 현재 쪽 내용과 전체 건수·쪽 수를 함께 준다. 없는 쪽은 빈 목록으로 응답한다."

## 연관관계의 주인과 mappedBy, 벌크 UPDATE 함정 (2026-11-23 보강)

**참고와 확인 범위**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

### 외래키는 "다(N)" 쪽에 생긴다

```
author 테이블                 book 테이블
┌────┬─────────┐           ┌────┬────────┬──────────┐
│ id │ name    │           │ id │ content│ author_id │ ← 외래키(FK)
│ 1  │ 작가 1  │◄──────────│ 1  │ 책 1 │    1     │
│    │         │◄──────────│ 2  │ 책 2 │    1     │
└────┴─────────┘           └────┴────────┴──────────┘
Author (1) ──────< Book (N)
```

```java
// Book.java: 주인 (외래키를 관리한다)
@ManyToOne
@JoinColumn(name = "author_id")
private Author author;

// Author.java: 거울 (읽기 전용, DB 컬럼을 만들지 않는다)
@OneToMany(mappedBy = "author")
private List<Book> books = new ArrayList<>();
```

- **주인**: `@JoinColumn`이 있는 쪽. 외래키 값을 바꿀 수 있다.
- **거울**: `mappedBy`가 있는 쪽. `mappedBy`의 값은 **상대 클래스의 필드 이름**(`author`)이다. 컬럼 이름(`author_id`)이 아니다.

**측정 결과 (자료 기록)**: 거울 쪽 `Author.books`에만 `add`하고 커밋하면 DB의 `book.author_id`는 `null`로 남는다. 저장하려면 **주인 쪽** `Book.author`에 값을 넣어야 한다.

### 기본 로딩 방식

| 필드 | JPA 기본 로딩 |
|---|---|
| `@ManyToOne` | EAGER |
| `@OneToMany` | LAZY |

`@ManyToOne`은 기본이 EAGER라서 여러 엔티티를 조회하면 연관 조회가 함께 늘 수 있다. N+1(3절)과 연결해서 기억한다.

### 벌크 UPDATE 뒤 1차 캐시가 옛 값을 가진다

`@Modifying` 쿼리(벌크 UPDATE)는 JPQL로 DB에 바로 실행된다. 영속성 컨텍스트(1차 캐시)에 이미 읽어 둔 엔티티는 갱신되지 않는다.

```java
@Modifying(clearAutomatically = true)   // 실행 후 1차 캐시를 비운다
@Query("update Customer c set c.point = c.point - :amount where c.id = :id and c.point >= :amount")
int usePoint(@Param("id") Long id, @Param("amount") int amount);
```

- 반환값 `int`는 바뀐 행 수다. 0이면 잔액 부족이다(33장의 조건부 UPDATE와 같은 원리).
- `clearAutomatically = true`를 빼면, 같은 트랜잭션에서 다시 읽어도 옛 값이 나올 수 있다.

**측정 결과 (자료 기록)**: 벌크 UPDATE 직후 이미 읽은 엔티티의 값은 옛 값(0)이고, DB는 1이었다. `clear` 후 다시 읽으면 새 값이 나왔다.

### 벌크 UPDATE 메소드: @Modifying과 @Transactional (9_JPA 06 기준, Spring Data JPA 4.1.1로 확인함)

강의 자료 `06 JPA query language`의 예는 리포지토리에 이렇게 선언한다.

```java
@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("UPDATE Sugang g SET g.grade = :after WHERE g.lecture.id = :lectureId AND g.grade = :before")
void updateGradeByLectureId(int lectureId, String before, String after);
```

- `@Modifying`이 없으면 Spring Data가 이 쿼리를 조회용으로 다루어 오류가 난다고 알려져 있다(이 실행에서는 확인하지 않음, 미확인). 이 쿼리는 행을 바꾸는 명령이다.
- `clearAutomatically = true`: 실행 뒤 영속성 컨텍스트를 비운다. 벌크 UPDATE가 DB를 바꾸어도 1차 캐시에는 옛 값이 남으므로, 이후 조회가 옛 값을 읽지 않게 한다.
- `flushAutomatically = true`: 실행 전에 쌓인 변경을 먼저 DB에 반영한다.
- 호출하는 쪽 메소드에는 `@Transactional`을 붙인다. 강의의 `SugangController`는 `@Transactional` 메소드 안에서 `updateGradeByLectureId`를 호출한다.
- **확인 결과 (JpaRepositoryFactory로 실행, 과목 3개 C0 → D+):**
  - `clearAutomatically = true`: 영향 행 3. 같은 `em`에서 이전에 읽은 객체의 성적은 `C0`였지만, clear 뒤 `em.find`로 다시 읽으니 `D+`였다.
  - `clearAutomatically = false`(기본값): 영향 행 3. 같은 객체 인스턴스(`sameInstance=true`)의 성적이 여전히 `C0`였다. 옛 값이 남는다.
- 트랜잭션은 이 실행에서 `EntityManager`로 직접 열었다. 스프링 컨테이너의 `@Transactional` 프록시는 쓰지 않았다.

### 자주 하는 오해

| 오해 | 실제 |
|---|---|
| 거울(`mappedBy`) 쪽에 `add`하면 저장된다 | 주인 쪽에 넣어야 외래키가 바뀐다 |
| `@ManyToOne`은 지연 로딩이다 | 기본은 EAGER다 |
| `mappedBy`에 컬럼 이름을 쓴다 | 상대 클래스의 필드 이름을 쓴다 |
| 외래키는 Author 테이블에 생긴다 | N 쪽(Book) 테이블에 생긴다 |
| 벌크 UPDATE 하면 읽어 둔 객체도 새 값이다 | 옛 값이 남는다. `clearAutomatically`를 쓴다 |

**면접 기준**: "외래키는 다(N) 쪽 테이블에 있고, 그 필드를 가진 쪽이 주인입니다. 반대쪽의 `mappedBy`는 읽기 전용 거울입니다. 벌크 UPDATE는 영속성 컨텍스트를 거치지 않으므로 `clearAutomatically`로 캐시를 비웁니다."

## 엔티티 상태 전이와 flush: 실제 SQL로 확인하기 (2026-11-23 보강, Hibernate 7.4.5에서 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**정의**
- **비영속**: `new`로 만든 객체. DB와 관계없다.
- **영속**: `persist`나 `find`로 영속성 컨텍스트에 들어간 객체. 변경을 추적한다.
- **준영속**: `detach`로 영속성 컨텍스트에서 분리된 객체. 바꿔도 추적하지 않는다.
- **flush**: 쌓인 변경을 DB로 보내는 작업이다. **commit과 다르다.** flush는 트랜잭션을 끝내지 않고, 1차 캐시도 비우지 않는다.

```java
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class JpaStates {
    @Entity(name = "Member5")
    static class Member {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        String name;

        Member() {}
        Member(String name) { this.name = name; }
    }

    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Member.class);
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/khac_jpa_demo");
        cfg.setProperty("hibernate.connection.username", "root");
        cfg.setProperty("hibernate.connection.password", System.getenv("MYSQL_PWD"));
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");
        cfg.setProperty("hibernate.show_sql", "true");

        try (SessionFactory sf = cfg.buildSessionFactory()) {
            EntityManager em = sf.createEntityManager();
            em.getTransaction().begin();

            System.out.println("== 1. persist (IDENTITY: INSERT right away)");
            Member m = new Member("kim");
            em.persist(m);
            System.out.println("== 2. find same id (1st level cache: no SELECT)");
            Member same = em.find(Member.class, m.id);
            System.out.println("same object=" + (same == m));

            System.out.println("== 3. change name without save, then commit (dirty checking: UPDATE)");
            m.name = "park";
            em.getTransaction().commit();

            em.getTransaction().begin();
            System.out.println("== 4. detach, change, commit (no UPDATE)");
            Member d = em.find(Member.class, m.id);
            em.detach(d);
            d.name = "lee";
            em.getTransaction().commit();
            em.close();

            EntityManager em2 = sf.createEntityManager();
            Member after = em2.find(Member.class, m.id);
            System.out.println("== 5. name in DB after detach change=" + after.name);
            em2.close();
        }
    }
}
```

**실행 결과 (확인함, `show_sql` 출력 중 핵심 줄만)**

```
== 1. persist (IDENTITY: INSERT right away)
Hibernate: insert into Member5 (name) values (?)
== 2. find same id (1st level cache: no SELECT)
same object=true
== 3. change name without save, then commit (dirty checking: UPDATE)
Hibernate: update Member5 set name=? where id=?
== 4. detach, change, commit (no UPDATE)
Hibernate: select m1_0.id,m1_0.name from Member5 m1_0 where m1_0.id=?
== 5. name in DB after detach change=park
```

**읽는 법**
- **1단계**: `IDENTITY` 전략은 DB가 id를 만들어야 하므로 `persist`할 때 INSERT가 바로 나간다. `SEQUENCE` 전략이라면 INSERT는 commit 때까지 모인다.
- **2단계**: 같은 id를 다시 찾으면 SELECT가 없다. 1차 캐시가 같은 객체를 돌려준다(`same object=true`).
- **3단계**: `save()`를 부르지 않았는데도 commit 때 UPDATE가 나갔다. 영속 상태의 객체는 스냅샷과 비교해서 바뀐 것만 자동으로 UPDATE한다(변경 감지).
- **4단계**: 준영속으로 만든 뒤에 바꾼 값은 DB에 나가지 않았다. 4단계의 SELECT는 `find`가 객체를 새로 읽은 것이다.
- **5단계**: DB에 남은 이름은 `park`이다. 준영속 상태에서 바꾼 `lee`는 저장되지 않았다.

**흔한 오해 정리**

| 오해 | 실제 |
|---|---|
| `persist`하면 항상 바로 INSERT | IDENTITY만 바로 나간다. SEQUENCE는 commit 때 모인다 |
| `flush`는 commit과 같다 | flush는 SQL만 보내고 트랜잭션은 계속된다 |
| 같은 id를 `find`하면 매번 SELECT | 1차 캐시에 있으면 SELECT가 없다 |
| 준영속 객체를 바꾸면 DB도 바뀐다 | 추적하지 않으므로 바뀌지 않는다. `merge`로 다시 붙여야 한다 |

**주의**
- `hibernate.show_sql`은 학습용이다. 운영에서는 끄고, 쿼리 수는 로그나 통계로 본다.
- 변경 감지는 **트랜잭션 안**에서만 동작한다. 서비스 메서드에 `@Transactional`을 붙이는 이유다(Spring에서는 자동으로 트랜잭션이 열린다).
- 여기서는 Spring 없이 Hibernate 네이티브 설정으로 실행했다. Spring Boot에서는 같은 동작을 `JpaRepository`와 `@Transactional`이 감싸 준다.

**면접 기준**: "영속 상태의 엔티티는 1차 캐시에 있고, 커밋 때 스냅샷과 비교해 UPDATE를 자동으로 만듭니다. 준영속 객체는 추적되지 않으므로 바꿔도 DB에 반영되지 않습니다. flush는 SQL을 보내는 작업이고 commit은 트랜잭션을 끝내는 작업입니다."

## 생성·수정 시간 자동 기록 (2026-11-23 보강, Hibernate 7.4.5에서 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**정의**
- **생성 시각**: 처음 저장될 때 한 번만 채운다.
- **수정 시각**: 저장·수정될 때마다 현재 시각으로 바꾼다.

```java
// Hibernate 네이티브 (확인함)
@CreationTimestamp
LocalDateTime createdAt;   // 처음 INSERT 때만 기록

@UpdateTimestamp
LocalDateTime updatedAt;   // INSERT와 UPDATE 때마다 기록
```

**실행 결과 (확인함)**

```
created: createdAt set=true, updatedAt set=true
createdAt unchanged=true
updatedAt changed=true
title=second
```

1초 쉬고 제목을 바꿔 commit하자, `createdAt`은 그대로이고 `updatedAt`만 새 시각으로 바뀌었다.

**Spring Data 방식 (미실행)**: 실제 프로젝트에서는 다음처럼 쓴다. 이 PC에서는 Spring Data 컨텍스트를 띄우지 않아 실행하지 않았다.

```java
@Entity
@EntityListeners(AuditingEntityListener.class)   // 이 엔티티에 감시 기능을 붙인다
public class Article {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

@SpringBootApplication
@EnableJpaAuditing                              // 감시 기능 전체를 켠다 (이것이 없으면 값이 null로 남는다)
public class BlogApplication { }
```

**자주 하는 실수**
- `@EnableJpaAuditing`을 빼면 `@CreatedDate`, `@LastModifiedDate`가 있어도 시간이 `null`로 남는다. 자료도 이 실수를 "자주 하는 실수"로 적었다.
- 시간 필드를 직접 `new LocalDateTime.now()`로 채우면 규칙이 흩어진다. 자동 기록 기능으로 한곳에서 처리한다.

**면접 기준**: "생성 시각은 `@CreatedDate`, 수정 시각은 `@LastModifiedDate`로 자동 기록합니다. Spring Data에서는 `@EnableJpaAuditing`을 켜야 동작하고, Hibernate는 `@CreationTimestamp`와 `@UpdateTimestamp`로 같은 일을 합니다."

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

Page는 전체 원소 수·총 페이지 수 등 메타데이터를 제공하고 Slice는 다음 구간 존재 여부 중심으로 제공한다. Page에서 COUNT 쿼리가 필요한 경우가 많지만 구현·최적화에 따라 생략될 수 있다. 검색 JOIN과 COUNT의 의미가 다를 수 있어 별도 countQuery가 필요할 수 있다.

### 직접 읽고 구현하는 독립 예제

```java
Pageable pageable=PageRequest.of(0,20,Sort.by("id").descending());
Page<Book> page=repository.findAll(pageable);
System.out.println(page.getTotalElements());
```


### 실행 검증 체크포인트

SQL 로그에서 조회 쿼리와 COUNT 쿼리를 구분한다.

### 스스로 작성할 변형 실습

무한 스크롤과 전체 페이지 번호 UI에서 각각 Page/Slice를 선택한다.

### 면접 답변의 기준 문장

> Page는 총량 정보, Slice는 다음 구간 여부 중심의 페이징 결과입니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: Optional<User>가 생기는 전체 과정

`Optional<User> result = userRepository.findById(99L);`에서 **DB가 Optional을 반환하는 것이 아니다.** SQL 조회는 0행일 수 있고, JPA 제공자가 이를 해석하며, Spring Data JPA의 `findById` API가 **단건 조회의 부재를 Optional.empty()로 표현하는 반환 계약**을 제공한다.

| 계층 | 결과가 없을 때 관찰하는 것 |
|---|---|
| SQL `SELECT ... WHERE id=99` | 결과 행 0개 |
| JDBC `ResultSet` | 첫 `next()`가 `false` |
| JPA `EntityManager.find(User.class, 99L)` | 엔티티를 찾지 못하면 `null` |
| Spring Data JPA `findById(99L)` | `Optional.empty()` |
| 서비스 `orElseThrow(...)` | 애플리케이션이 정한 예외 발생 |
| 웹 계층의 예외 처리기 | 정책에 따라 HTTP 404로 변환 가능 |

**예외:** JPA `getReference`는 조회 시점과 실패 시점이 다를 수 있고, `getSingleResult()`는 결과가 없으면 `NoResultException`을 던질 수 있다. 모든 조회 API가 null 또는 Optional을 반환한다고 일반화하지 않는다. `findAll()`은 일반적으로 0건이면 빈 리스트를 반환한다.

```java
Optional<User> maybe = userRepository.findById(99L);
if (maybe.isEmpty()) {
    System.out.println("조회 대상 없음");
}
User user = userRepository.findById(1L)
    .orElseThrow(() -> new UserNotFoundException(1L));
```

**실습:** H2 또는 MySQL 테스트 DB에 사용자 1명을 저장하고 `findById(1L)`, `findById(99L)`, `findAll()`을 실행한다. 반환 타입, `isPresent()`, 리스트 크기를 기록한다. SQL 로그로 실제 실행된 쿼리를 확인하되, 캐시와 영속성 컨텍스트에 따라 쿼리가 생략될 수 있음을 유의한다.

**면접 답변:** “조회 조건에 맞는 DB 행이 없다는 사실과 Java 메서드의 반환 타입은 별개입니다. Spring Data JPA의 `findById`는 존재하지 않는 엔티티를 `Optional.empty()`로 표현하도록 계약되어 있어 호출자가 부재를 명시적으로 처리할 수 있습니다.”


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. JPA / Hibernate / Spring Data JPA

**흔한 오해:** “JPA가 DB 연결을 직접 구현하는 제품이다”

**정확한 구분:** JPA는 Java 영속성 API 규약, Hibernate는 대표 구현체, Spring Data JPA는 JPA 기반 Repository 추상화를 제공하는 프로젝트다. 역할이 다른 세 층이다.

**코드·명령 해석:**

```java
interface UserRepository extends JpaRepository<User, Long> {}
```

**확인 방법:** Repository 호출이 JPA 구현을 거쳐 SQL로 이어지는 경로를 추적한다.

**직접 구현:** Spring Data 없이 EntityManager.find를 사용하는 코드를 작성하라.

**면접 점검:** “JPA / Hibernate / Spring Data JPA”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. SQL 0행 / EntityManager.find null / Optional.empty

**흔한 오해:** “DB가 Optional.empty()를 반환한다”

**정확한 구분:** DB는 SQL 결과 집합을 제공한다. EntityManager.find는 대상이 없으면 null을 반환하고, Spring Data JPA findById는 API 계약에 따라 Optional.empty()를 반환한다. 빈 목록 조회는 빈 List일 수 있다.

**코드·명령 해석:**

```java
Optional<User> result = repository.findById(99L);
User user = result.orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
```

**확인 방법:** 존재하는 ID와 없는 ID를 조회해 Optional.isPresent를 비교한다.

**직접 구현:** findAllByName에서 0건일 때 List 크기를 확인하고 findById와 비교하라.

**면접 점검:** “SQL 0행 / EntityManager.find null / Optional.empty”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. 영속성 컨텍스트 / 트랜잭션 / flush

**흔한 오해:** “save를 호출하면 즉시 COMMIT된다”

**정확한 구분:** 영속성 컨텍스트는 엔티티 상태와 동일성 등을 관리한다. flush는 변경 내용을 DB에 동기화하는 과정이고 commit은 트랜잭션 확정이다. flush가 곧 commit은 아니다.

**코드·명령 해석:**

```java
@Transactional void rename(User user){ user.setName("새이름"); }
// 관리 상태라면 변경 감지로 SQL 반영 가능
```

**확인 방법:** flush 이후 rollback하면 최종 데이터가 남는지 확인한다.

**직접 구현:** persist/flush/clear/find 순서에 따른 SQL 발생을 관찰하라.

**면접 점검:** “영속성 컨텍스트 / 트랜잭션 / flush”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 4. LAZY / N+1 / JOIN FETCH

**흔한 오해:** “LAZY면 SQL이 무조건 한 번만 나간다”

**정확한 구분:** LAZY는 연관 데이터의 로딩 시점을 미루는 전략이다. 목록 조회 후 각 항목의 연관 객체에 접근하면 N+1 문제가 발생할 수 있다. fetch join이나 DTO 조회 등으로 조절한다.

**코드·명령 해석:**

```java
SELECT u FROM User u JOIN FETCH u.team
```

**확인 방법:** SQL 로그에서 사용자 수 증가에 따른 쿼리 수를 측정한다.

**직접 구현:** 페이징과 컬렉션 fetch join을 함께 사용할 때의 주의점을 조사하라.

**면접 점검:** “LAZY / N+1 / JOIN FETCH”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“Page는 전체 건수·페이지 정보를 포함하는 결과 모델이고 Slice는 다음 구간 존재 여부 중심의 결과 모델이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“Page는 전체 건수 계산 비용이 있을 수 있으며 Slice는 전체 페이지 수를 알 필요가 없는 UX에 유리하다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“Page와 Slice, OFFSET과 키셋의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 4-2. 쿼리 메소드 이름으로 쿼리 만들기 (query creation)

> 추가 근거: 9_Spring_boot_JPA `05 JPA query creation` 강의 자료(23쪽)를 읽고 보강했다. 이름 규칙 해석은 **spring-data-commons 4.1.1의 `PartTree`를 직접 실행해 확인함**(2026-11-24). 실제 SQL 실행은 이 절에서 **미실행**이다.

### 정의

Spring Data JPA는 `JpaRepository`를 상속한 **인터페이스**에 선언한 메소드 이름을 읽어서 구현을 자동으로 만든다. 구현 클래스는 직접 쓰지 않는다. 메소드 이름이 곧 조회 조건, 정렬, 개수 제한이다.

### 예시 엔티티와 리포지토리

```java
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByStudentNo(String studentNo);                 // 0건 null, 1건 객체, 2건 이상 에러
    List<Student> findByName(String name);                     // 0건 빈 List
    List<Student> findByNameStartsWith(String name);           // name LIKE '김%'
    List<Student> findByDepartmentNameStartsWith(String name); // department.name LIKE ?
    List<Student> findByOrderByName();                         // 전체, name 오름차순
    List<Student> findByDepartmentIdOrderByNameDesc(int id);   // department.id = ? ORDER BY name DESC
    List<Student> findByStudentNoOrName(String studentNo, String name); // OR 조건
    int countByName(String name);                              // 건수
    void deleteByDepartmentId(int departmentId);               // 조건 삭제
}
```

### 실행 확인: 메소드 이름을 어떻게 나누는가 (PartTree)

`PartTree`는 메소드 이름을 주어와 조건으로 나누는 Spring Data의 파서다. 아래 코드를 실행해 결과를 얻었다.

```java
PartTree t = new PartTree(method, Student.class);
```

| 메소드 이름 | 해석 결과 (확인함) |
|---|---|
| `findByName` | `name` 단순 비교 |
| `findByNameStartsWith` | `name` STARTING_WITH |
| `findByDepartmentNameStartsWith` | `department.name` STARTING_WITH |
| `findByDepartmentIdOrderByNameDesc` | `department.id` 비교, 정렬 `name DESC` |
| `findByOrderByName` | 조건 없음, 정렬 `name ASC` |
| `countByStudentNoOrName` | 개수 조회, 조건 2개 OR |
| `findByNameOrStudentNo` | `name` OR `studentNo` |
| `findTop3ByOrderByIdDesc` | 개수 제한 3, 정렬 `id DESC` |
| `findByNameIsNull` | `name IS NULL` |
| `findByNameNotLike` | `name NOT LIKE` |
| `existsByName` | 존재 여부 조회 |
| `deleteByDepartmentId` | 삭제 조건 `department.id` |
| `findByNameAndSexIn` | `name` 비교 AND `sex IN` |
| `findByNameContaining` | `name` CONTAINING (`%값%`) |
| `findByDepartment_Name` | `department.name` (밑줄로 중첩 속성 구분) |
| `findByNamme` | **오류**: `No property 'namme' found for type 'Student'; Did you mean 'name'` |

### 이름 규칙으로 기억할 것

- **이름이 조건이다.** `find…By`, `count…By`, `exists…By`, `delete…By`의 `By` 뒤가 조건이고, 그 뒤 `OrderBy…`가 정렬이다.
- **속성 이름이 틀리면 시작할 때 에러가 난다.** 메소드 호출 때가 아니라 애플리케이션이 뜰 때 `PropertyReferenceException`이 나온다. 오타 메시지에 `Did you mean`이 함께 나온다.
- **중첩 속성은 연관 경로로 찾는다.** `departmentId`는 `Student.department`의 `id`를 가리킨다. 경로가 헷갈리면 `findByDepartment_Name`처럼 `_`로 구분한다.
- **파라미터 이름은 중요하지 않다. 순서가 중요하다.** 강의 자료는 `findByLastNameAndFirstName(String s1, String s2)`에서 첫 번째 값이 `lastName`, 두 번째 값이 `firstName`에 들어간다고 설명한다. 이 호출 동작은 실행하지 않았다(미실행).

### 키워드 표 (강의 자료 기준)

| 키워드 | 예 | 생성되는 조건 |
|---|---|---|
| And / Or | `findByLastNameAndFirstName` | AND / OR |
| Between | `findByStartDateBetween(d1, d2)` | `BETWEEN ? AND ?` |
| LessThan / GreaterThan | `findByAgeLessThan(int)` | `<` / `>` |
| LessThanEqual / GreaterThanEqual | `findByAgeGreaterThanEqual(int)` | `<=` / `>=` |
| After / Before | `findByStartDateAfter(d)` | 날짜 비교 |
| IsNull / IsNotNull | `findByAgeIsNull()` | `IS NULL` |
| Like / NotLike | `findByFirstNameLike(pattern)` | `LIKE` (`%`는 직접 넣는다) |
| StartingWith / EndingWith / Containing | `findByFirstNameStartingWith(name)` | `LIKE '값%'` / `'%값'` / `'%값%'` |
| OrderBy…Asc/Desc | `findByAgeOrderByLastNameDesc(int)` | 정렬 |
| Not | `findByLastNameNot(String)` | `<>` |
| In / NotIn | `findByAgeIn(Collection<Integer>)` | `IN (...)` / `NOT IN (...)` |
| True / False | `findByActiveTrue()` | `active = true` / `false` |
| Top / First | `findTop10ByOrderByAge()` | 앞 10건 (`LIMIT 10`) |

강의 자료의 SQL 대응표는 설명용이다. 실제 SQL 모양은 DB 방언과 Hibernate 버전에 따라 다르다. 위 표의 키워드 이름은 `PartTree`에서 해석 결과를 확인했다(`STARTING_WITH`, `CONTAINING`, `IS_NULL`, `NOT_LIKE`, `IN`, 정렬, `Top`·`First` 개수 제한). `Between`, `After`, `True/False` 등 나머지는 강의 자료 설명이며 이 PC에서 해석 실행은 하지 않았다.

### 반환 타입 규칙

| 반환 타입 | 결과 0건 | 1건 | 2건 이상 |
|---|---|---|---|
| `List<Student>` | 빈 List | 1개 List | 전부 List |
| `Student` | `null` | 객체 | 예외 (예외 종류는 미실행으로 확인하지 않음) |
| `Optional<Student>` | `Optional.empty()` | 객체가 든 Optional | 예외 |

**주의:** 반환 타입이 `Student`인데 조건이 여러 건을 가리키면 에러다. 조건을 더 좁히거나 `List`로 받는다. (에러 발생 시나리오는 실행하지 않았음, 미실행.)

### 개수, 삭제 메소드

- `countBy…`는 조건에 맞는 건수를 `int`/`long`으로 돌려준다.
- `deleteBy…`, `removeBy…`는 조건에 맞는 엔티티를 **하나씩 읽어서** 삭제한다. 대량 삭제에는 쓰지 않는다. 트랜잭션이 필요하다(`@Transactional`).
- `existsBy…`는 `boolean`을 돌려준다(`PartTree` 해석에서 exists 표시 확인).

### 연습 문제 (강의 자료 3장)

1. department id 값이 3, 4인 학생만 조회하기: `findByDepartmentIdIn(List<Integer> ids)`
2. 김씨 이거나 박씨인 학생 조회하기: `findByNameStartsWithOrNameStartsWith(String a, String b)`
3. 김씨 학생을 이름 오름차순으로 10명만 조회하기: `findTop10ByNameStartsWithOrderByNameAsc(String name)`
4. 김씨인 소프트웨어학생들만 이름 오름차순으로 조회하기: `findByNameStartsWithAndDepartmentNameOrderByNameAsc(String name, String dept)`

**확인 방법:** 연습 문제 1~4의 메소드 이름은 `PartTree`로 해석해 조건, 정렬, 개수 제한이 문제와 맞는지 확인했다(이름 해석만 확인, 실제 SQL과 결과는 미실행). 실제 실행은 Spring Boot 프로젝트에서 `findBy…`를 호출해 확인한다.

**면접 기준:** "Spring Data JPA는 메소드 이름을 파싱해서 조건, 정렬, 개수 제한을 만든다. 이름이 틀리면 애플리케이션 시작 때 실패하므로 빨리 발견된다. 복잡한 조건은 이름이 길어지므로 `@Query`나 Querydsl로 옮긴다."

## 4-3. JPQL: 테이블이 아니라 엔티티를 기준으로 쓰는 쿼리

> 추가 근거: 9_Spring_boot_JPA `06 JPA query language` 강의 자료의 JPQL 본문(19~32쪽)을 읽고 보강했다. 33~40쪽(`@Modifying`, `@Transactional`, JPQL 메소드 구현)도 읽었다. 2~18쪽은 프로젝트 설정과 엔티티 생성 화면이라 읽지 않았고, 41~42쪽(고급 SQL)은 08장 4-1절과 겹친다. 아래 쿼리는 **Hibernate 7.4.5와 MySQL 8.0.21에서 실행해 결과를 확인함**(2026-11-24, 임시 엔티티 `Department`·`Student`·`Professor`·`Lecture`·`Sugang`, 실습 DB `khac_jpql_demo`). 강의의 Spring 웹 화면(`/jpql`)은 미실행이다.

### 정의

JPQL은 SQL과 문법이 비슷하지만 **테이블과 컬럼이 아니라 엔티티와 필드**를 쓴다. `FROM Student s`는 `student` 테이블이 아니라 `Student` 엔티티 목록을 뜻하고, `s.department.name`처럼 점(`.`)으로 연관 객체를 따라간다. Hibernate가 이것을 SQL로 바꾼다.

### 실행 결과

| 번호 | JPQL (요지) | 결과 (확인함) |
|---|---|---|
| 1 | `SELECT d FROM Department d` | 학과 2개 |
| 2 | `SELECT s FROM Student s WHERE s.department.id = :id` | 학과 1의 학생 3명 |
| 3 | `SELECT s.studentNo, s.name, s.department.name FROM Student s` | 학생 4명 (학과명 포함) |
| 4 | `SELECT g.lecture.title FROM Student s JOIN s.sugangs g WHERE s.studentNo = '201532006'` | 자료구조, Java |
| 5 | `SELECT s FROM Student s WHERE SIZE(s.sugangs) > 1` | 수강 2과목 이상 학생 3명 |
| 6 | `SELECT s FROM Student s WHERE s.sugangs IS EMPTY` | 수강 0과목 학생 1명 |
| 7 | `... WHERE EXISTS (SELECT g FROM s.sugangs g WHERE g.lecture.professor.name = :name)` | 이몽룡 교수 과목을 하나라도 수강한 학생 3명 |
| 8 | `SELECT s.department.name, COUNT(s) FROM Student s GROUP BY s.department.name` | 소프트웨어공학과 3, 컴퓨터공학과 1 |
| 9 | `SELECT s.studentNo, s.name, SIZE(s.sugangs) FROM Student s ORDER BY SIZE(s.sugangs) DESC` | 수강 수 내림차순 4행 |
| 10 | `SELECT s FROM Student s JOIN s.sugangs g WHERE g.lecture.title = :t` | 자료구조 수강 학생 2명 |
| 11 | 같은 쿼리를 `?1` 위치 파라미터로 실행 (`setParameter(1, ...)`) | 2건 |
| 12 | `UPDATE Sugang g SET g.grade = 'D+' WHERE g.lecture.id = :lid AND g.grade = 'C0'` | 영향 행 수 2 |
| 13 | 12 실행 뒤 데이터베이스 과목의 성적 조회 | D+ 2건 |

**참고:** 2번 쿼리 결과는 `s.department.id`가 단일 값 연관이므로 SQL에서 조인 없이 외래키 컬럼 비교로 바뀐다. 4번·10번은 컬렉션 연관(`sugangs`)이라 **명시적 `JOIN`**이 필요하다. 이 차이가 강의의 핵심이다.

### 단일 값 연관과 컬렉션 연관의 경로 차이

- **단일 값 (`@ManyToOne`)**: `s.department.name`처럼 점으로 따라간다. 조인을 명시하지 않아도 된다(묵시적 조인).
- **컬렉션 (`@OneToMany`)**: `s.sugangs.grade`는 **오류**다. `sugangs`는 `List<Sugang>`이라 `grade`가 없다. 먼저 `JOIN s.sugangs g`로 원소를 별칭에 묶은 뒤 `g.grade`를 쓴다.
- 강의 자료의 SQL 대응은 `student s JOIN sugang g ON g.studentId = s.id JOIN lecture t ON g.lectureId = t.id`이다. JPQL에서는 연관 이름만 쓰고, 조인 조건은 매핑(`@JoinColumn`, `mappedBy`)이 담당한다.

### 집계, 정렬, 조건 함수

- `GROUP BY`는 SQL과 같다. 집계 결과는 `Object[]`로 받는다(실행 결과 8번).
- `SIZE(컬렉션)`은 원소 개수, `IS EMPTY`는 0개 여부, `EXISTS (서브쿼리)`는 조건 만족 여부다. 5, 6, 7번으로 확인했다.
- 강의 자료의 JPQL 함수 표(`CONCAT`, `LENGTH`, `LOWER`, `UPPER`, `SUBSTRING`, `TRIM`, `LOCATE`, `MOD`, `SQRT`, `ABS`, `CURRENT_DATE`, `INDEX`, `SIZE`)는 설명용이다. 이 중 `SIZE`만 실행해 확인했고, 나머지 함수는 미실행이다.

### UPDATE와 DELETE

- JPQL로 `INSERT`는 쓰지 않는다. 저장은 `save`(또는 `persist`)가 맡는다.
- 여러 행을 한 번에 바꿀 때는 `UPDATE` JPQL이 낫다. 12번에서 2행이 바뀌었다.
- **주의:** 벌크 UPDATE는 영속성 컨텍스트를 거치지 않는다. 같은 트랜잭션에서 이미 읽은 엔티티는 옛 값을 가질 수 있다(이 장의 "벌크 UPDATE 함정" 절 참고).
- Spring Data 메소드 이름으로도 삭제할 수 있다(`deleteByDepartmentId`). 이 경우 행을 하나씩 읽어서 지운다.

### `@Query`로 JPQL 메소드 구현

```java
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s.studentNo, s.name, SIZE(s.sugangs) FROM Student s ORDER BY SIZE(s.sugangs) DESC")
    List<Object[]> findSugangCount();                                  // 결과 행을 Object[]로 받는다

    @Query("SELECT s FROM Student s JOIN s.sugangs g WHERE g.lecture.title = ?1")
    List<Student> findByLectureTitle(String title);                    // ?1: 첫 번째 파라미터

    @Query("""
        SELECT s FROM Student s
        WHERE EXISTS (SELECT g FROM s.sugangs g
                      WHERE g.lecture.professor.id = ?1 OR g.lecture.professor.name = ?2)
        """)
    List<Student> findByProfessorIdOrName(int id, String name);       // 위치 파라미터 두 개

    @Query("""
        SELECT s FROM Student s
        WHERE EXISTS (SELECT g FROM s.sugangs g
                      WHERE g.lecture.professor.id = :id OR g.lecture.professor.name = :name)
        """)
    List<Student> findByProfessorIdOrNameNamed(@Param("id") int id, @Param("name") String name); // 이름 파라미터
}
```

- `?1`, `?2`는 메소드 파라미터의 **순서**를 쓴다. `:name`처럼 이름을 쓰면 `@Param`으로 이름을 연결한다.
- 위 메소드들은 강의 자료 예이며, 이 PC에서는 Hibernate `Query` 호출로 1·10·11번과 같은 형태를 확인했다. Spring Data 리포지토리에서 `@Query` 메소드 호출은 미실행이다.
- 실행 중 `No argument for ordinal parameter '?1'`는 파라미터를 넘기지 않아 생기는 오류다(실험 중 확인함).

### 실행 중 나온 주의 로그 (첫 실행의 정상 메시지)

`hbm2ddl.auto=create`로 처음 실행하면 `alter table ... drop foreign key ...` 경고가 로그에 나올 수 있다. 테이블이 아직 없어서 생기는 경고이고, 스키마 생성은 끝까지 진행된다. 엔티티 이름을 지정하지 않으면 `JpqlMain$Lecture`처럼 내부 클래스 이름이 붙어 JPQL에서 엔티티를 찾지 못한다. 실습에서는 `@Entity(name = "Lecture")`처럼 이름을 명시했다.

**강의 06의 student2 스키마와 관계 (강의 자료):** `student`(departmentId), `department`, `professor`(departmentId), `lecture`(professorId), `sugang`(studentId, lectureId, grade). 학생-과목은 `Sugang`이 중간 테이블 역할을 한다. 위 실행은 같은 구조를 축소한 `khac_jpql_demo`에서 했다.

**"모든 과목을 수강한 학생" 쿼리 (강의 06의 #4·#5 예, 축소 데이터에서 확인함):** 강의는 `김진우` 학생이 수강한 과목을 모두 수강한 학생을 찾는다. 아래는 강의 쿼리를 그대로 옮긴 것이다. 강의 DB 컬럼은 `studentId`, `lectureId`처럼 카멜 케이스이고, 이 PC의 축소 DB는 `student_id`, `lecture_id`를 쓰므로 이름만 다르다. 실행에서는 `김진우`가 없어서 기준 학생을 `최하은`(id 1)으로 바꾸었다.

```sql
WITH lectures AS (
    SELECT s.id AS studentId, s.name AS studentName, e.id AS lectureId, e.title
    FROM student s
    JOIN sugang g ON g.studentId = s.id
    JOIN lecture e ON g.lectureId = e.id
)
SELECT s.id, s.name
FROM student s
WHERE NOT EXISTS (
    SELECT * FROM lectures
    WHERE studentName = '김진우'                      -- 기준 학생이 수강한 과목 중에서
      AND lectureId NOT IN (
          SELECT lectureId FROM lectures WHERE studentId = s.id  -- s가 수강하지 않은 과목이 있으면 제외
      )
);
```

결과: 기준 학생 자신만 나왔다. 다른 학생들은 기준 학생의 과목 중 하나 이상을 빠뜨려서 제외되었다(확인함). "전부 포함"을 `NOT EXISTS` + `NOT IN`으로 푸는 방식이다.

**면접 기준:** "JPQL은 테이블이 아니라 엔티티와 연관 경로를 쓰는 객체 기준 쿼리입니다. 단일 값 연관은 점으로 따라가고, 컬렉션 연관은 JOIN으로 원소를 별칭에 묶어야 합니다. 집계와 대량 수정도 가능하지만, 벌크 연산은 영속성 컨텍스트를 거치지 않는다는 점을 알고 써야 합니다."

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

**해결:** 한쪽 연관 필드를 `toString`에서 빼거나, Lombok에서 `@ToString.Exclude`로 제외한다(Lombok은 미실행).

### 실행 2: JSON 직렬화 (확인함)

| 상황 | 결과 (Jackson 3.1.5, `tools.jackson.databind.ObjectMapper`) |
|---|---|
| `Department` → `students` → `Student` → `department` 순환 | `StreamConstraintsException`: 중첩 깊이 500 초과 |
| `Student.department`에 `@JsonIgnore` | `{"name":"소프트웨어공학과","students":[{"name":"최하은"}]}` |

**해석:** 순환이 있으면 직렬화가 끝나지 않는다. Jackson 3는 깊이 500에서 멈추고 오류를 낸다(`StackOverflowError`로 끝나지 않는다). 역방향 필드에 `@JsonIgnore`를 붙이면 한쪽 방향만 나간다.

### 엔티티를 그대로 반환하면 안 되는 이유

- 엔티티를 JSON으로 바로 내보내면 **순환 참조**와 **지연 로딩 예외**(세션 밖에서 연관 접근)가 같이 생긴다.
- 실무에서는 응답 전용 DTO를 만들고, 필요한 필드만 옮긴다(DTO 조회는 3장 N+1 비교 절에서 다뤘다. 응답 DTO 전용 실습은 이 장에 없음, 미실행).
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

> 이 절은 Spring 없이 MyBatis 코어만 쓴 실행 결과다. `mybatis-spring-boot-starter`와 `@Mapper`를 쓰는 스프링 부트 연동은 **미실행**이다. 공식 설정 방법은 MyBatis 공식 문서를 기준으로 하고, 이 장에서는 같은 동작을 직접 확인했다.

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

## 6. Querydsl (타입 안전한 쿼리)

- **정의:** 자바 코드로 JPA 쿼리를 작성하는 라이브러리. 문자열 JPQL 대신 메서드 체인을 쓰므로 컴파일 시점에 오타를 잡는다.
- **왜 쓰나:** 조건이 많은 검색 화면에서 조건을 조합할 때 JPQL 문자열이 복잡해진다. Querydsl은 조건을 메서드로 조립한다.
- **설정 (Gradle, 튜토리얼 기준 버전 5.0.0):** `implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'`와 `annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'`를 넣는다. 빌드할 때 엔티티마다 `QBook`처럼 Q 클래스가 생성된다.

**확인 결과 (확인함, 직접 내려받은 Querydsl 5.0.0 jar와 MySQL 8.0)**: 엔티티 `Author`(1)와 `Book`(N)을 만들고 `javac`의 annotation processing으로 `QBook`, `QAuthor`를 생성했다. 조건이 `null`이면 `BooleanBuilder`가 그 조건을 무시하게 했다.

```java
static BooleanExpression titleContains(String keyword) {
    return keyword == null ? null : QBook.book.title.contains(keyword);
}
static BooleanExpression priceAtMost(Integer max) {
    return max == null ? null : QBook.book.price.loe(max);
}

BooleanBuilder where = new BooleanBuilder();
where.and(titleContains(keyword));
where.and(priceAtMost(max));
List<String> titles = new JPAQueryFactory(em)
        .select(QBook.book.title).from(QBook.book)
        .where(where)
        .orderBy(QBook.book.title.asc())
        .fetch();
```

```
1) all: [Java Basics, Java Concurrency, Spring Boot Guide]
2) title has 'Java': [Java Basics, Java Concurrency]
3) title 'Java' and price <= 25000: [Java Basics]
4) author kim only: [Java Basics, Spring Boot Guide]
5) all conditions: [Java Concurrency]
```

**실행에 필요한 jar (확인함)**: `querydsl-core`, `querydsl-jpa`(jakarta), `querydsl-apt`, `querydsl-codegen`, `codegen-utils`, `mysema-commons-lang 0.2.4`, `javapoet`, `classgraph`, `javax.inject`, `antlr4-runtime`. 빌드 도구를 쓰면 이 의존성은 자동으로 내려받는다. 직접 실행할 때는 `mysema-commons-lang`을 빠뜨리면 `NoClassDefFoundError`가 난다.

- **null 조건 무시:** 검색어가 없을 때 `null`을 돌려주면 `where`가 그 조건을 무시한다. 조건을 메서드로 나누는 이유가 이것이다. 자세한 조건 조합 예는 "동적 검색" 절을 본다.
- **면접 기준:** "Querydsl은 JPA 쿼리를 자바 코드로 타입 안전하게 조립하는 도구입니다. 빌드 때 생성된 Q 클래스가 엔티티 필드를 가리키므로 오타를 컴파일 단계에서 잡고, 조건이 없으면 null로 빼서 검색 조건을 조합합니다."


