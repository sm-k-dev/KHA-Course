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

**출처**: 선생님 자료 `Additional_HandBook★/파이널 준비 점검 1~2교시 실습 노트.html`의 사건 1 해결 4가지(DTO 직접 조회, fetch join, `@EntityGraph`, batch size) 중 세 가지를 실제로 실행해 비교했다. DTO 조회는 위 "N+1 (SQL 수 세기)" 절과 같다.

**실험 조건**: 맛집 5곳, 맛집마다 리뷰 2개(총 10개). 맛집 목록을 읽고 모든 맛집의 리뷰 수를 센다.

| 방식 | 설정 | SQL 개수 (확인함) | 리뷰 수 |
|---|---|---|---|
| 기본 지연 로딩 | 아무것도 안 함 | **6** (목록 1 + 맛집마다 1) | 10 |
| batch size | `hibernate.default_batch_fetch_size = 10` | **2** (목록 1 + 리뷰 묶음 1) | 10 |
| EntityGraph | 목록을 읽을 때 `reviews`를 함께 지정 | **1** (조인으로 한 번에) | 10 |

```java
// EntityGraph: 목록 조회 때 리뷰까지 한 번에 가져온다
EntityGraph<Store> graph = em.createEntityGraph(Store.class);
graph.addAttributeNodes("reviews");
List<Store> stores = em.createQuery("select s from Store s", Store.class)
        .setHint("jakarta.persistence.fetchgraph", graph)
        .getResultList();

// batch size: 설정 한 줄 (로딩 시점에 여러 맛집의 리뷰를 IN 절로 묶는다)
cfg.setProperty("hibernate.default_batch_fetch_size", "10");
```

**읽는 법**
- **지연 로딩**: 리뷰를 실제로 쓰는 순간 맛집마다 SELECT가 나간다. 맛집 수가 늘면 SQL도 같이 늘어난다.
- **batch size**: 코드는 그대로 두고 설정만 바꾼다. 여러 맛집의 리뷰를 한 번에 가져와 SQL 수를 줄인다. 기존 코드를 고치기 어려울 때 쓴다.
- **EntityGraph**: 필요한 연관 데이터를 **목록 조회 시점에 명시**한다. SQL은 가장 적지만, 목록 쿼리마다 어떤 연관을 가져올지 정해야 한다.

**주의**
- 컬렉션(리뷰 목록)을 fetch join이나 EntityGraph로 가져오면서 **페이징**을 함께 쓰면 DB 페이징이 아니라 메모리에서 잘린다. 이때는 batch size나 2단계 조회를 쓴다.
- 리뷰 **개수만** 필요하면 위 방법들보다 DTO 집계 조회가 더 가볍다(위 "N+1 (SQL 수 세기)" 절).

**면접 기준**: "N+1은 목록 조회 뒤 연관 데이터를 항목마다 다시 조회하는 문제입니다. 해결은 필요한 값만 DTO로 집계하거나, 연관 데이터를 목록 조회 때 함께 가져오는 것(fetch join, EntityGraph)입니다. 설정으로는 batch size로 묶어 줄입니다."

### 컬렉션 연관관계: cascade와 orphanRemoval (2026-11-23 보강, Hibernate 7.4.5에서 확인함)

**출처**: 선생님 자료 `9_Spring_boot_JPA`의 `04 JPA collection-valued association` 강의 제목(컬렉션 연관관계)의 주제를 기준으로 예제를 만들어 확인했다. 강의 PDF는 사진이라서 글자를 옮기지 않았다.

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

### QueryDSL: 자바 코드로 조건을 조립하기 (2026-11-24 보강, 미실행)

**출처**: 선생님 자료 `FinalTeamProject☆/tutorial_project/shop-tutorial.html`(쇼핑몰 실습, 상품 검색 Repository)의 설정과 사용 방식을 정리했다. 이 PC에는 QueryDSL 라이브러리가 없어서 **실행하지 않았다(미실행)**. 아래 설정과 코드는 자료의 내용을 옮긴 것이고, 버전은 자료 기준이다. 실제 프로젝트에서는 사용하는 Spring Boot 버전에 맞는 QueryDSL 버전을 확인한다.

**왜 쓰나**: JPQL이나 `@Query` 문자열은 오타가 나도 컴파일이 되고, 실행해야 오류를 본다. QueryDSL은 엔티티 필드를 자바 코드로 가리키므로, 철자가 틀리면 **컴파일 단계에서** 잡힌다. 검색 조건이 여러 개일 때 조건을 조립하기도 쉽다.

**설정 (자료 기준, Gradle)**

```groovy
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'   // 컴파일할 때 Q 클래스(QItem 등)를 자동 생성
```

**핵심 개념**
- **Q 클래스**: 빌드할 때 엔티티마다 자동으로 만들어진다. `QItem.item.itemNm`처럼 엔티티 필드를 가리키는 객체다.
- **JPAQueryFactory**: 쿼리를 만드는 시작점이다. `EntityManager`로 생성한다.
- **BooleanExpression**: `where`에 넣는 조건 하나다. 메서드로 만들어 두면 여러 쿼리에서 재사용한다. 값이 `null`이면 조건을 무시하게 만들 수 있다(검색어가 비어 있을 때 전체 조회).

**사용 예 (자료 흐름을 옮긴 것, 미실행)**

```java
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {   // 클래스 이름 끝에 Impl이 있어야 인식된다
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 판매 상태가 없으면 null을 돌려주고, where는 null 조건을 무시한다
    private BooleanExpression sellStatusEq(ItemSellStatus status) {
        return status == null ? null : QItem.item.itemSellStatus.eq(status);
    }

    public List<Item> search(ItemSellStatus status) {
        return queryFactory.selectFrom(QItem.item)
                .where(sellStatusEq(status))
                .orderBy(QItem.item.id.desc())
                .offset(0).limit(10)
                .fetch();
    }
}
```

**읽는 법**
- `selectFrom(QItem.item)`은 `select … from item`이다. `where`, `orderBy`, `offset`, `limit`은 SQL의 같은 이름 절과 대응한다.
- `fetch()`는 목록을, `fetchOne()`은 한 건을 돌려준다. 개수는 `select(Wildcard.count)`로 따로 센다.
- `where` 안의 `null` 조건은 무시된다. 검색 화면에서 선택하지 않은 조건을 그대로 넘길 수 있다.

**주의**
- Q 클래스가 만들어지지 않으면 `QItem`을 찾을 수 없다는 컴파일 오류가 난다. IDE에서 빌드를 다시 하거나 annotation processing을 켠다(자료도 이 점을 강조한다).
- 복잡한 조인과 통계는 JPQL보다 읽기 어려울 수 있다. 팀이 함께 쓰는 규칙(메서드 이름, 조건 분리)을 정한다.

**면접 기준**: "QueryDSL은 엔티티 필드를 자바 코드로 쓰는 쿼리 도구입니다. Q 클래스가 컴파일 때 만들어져 오타를 컴파일 단계에서 잡고, 검색 조건은 BooleanExpression으로 나눠 조립합니다."

### 동적 검색: Specification과 distinct (2026-11-24 보강, MySQL 8.0과 Hibernate 7.4.5에서 확인함)

**출처**: 선생님 자료 `9_SpringBoot_practice/sbb_2026.10.21_v1.zip`의 `QuestionService.search(kw)`(제목·답변·작성자에서 검색어 찾기)를 바탕으로 정리했다. 원 코드는 `Specification`과 `distinct`를 쓴다.

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
- 답변 내용을 검색하면서 HTML을 허용하면 화면 출력에서 XSS가 생길 수 있다(99장). SBB는 Markdown을 HTML로 바꾸는 `commonmark`를 쓰므로, 원시 HTML 출력 옵션을 확인한다. 이 부분은 라이브러리가 PC에 없어서 실행하지 않았다(미실행).

**면접 기준**: "검색 조건이 선택적이면 Specification이나 QueryDSL로 조건을 조립합니다. 답변처럼 여러 행과 조인하면 중복이 생기므로 `distinct`로 질문 단위를 맞추고, 개수와 페이징이 맞는지 확인합니다."

## 4. Page·Slice·COUNT

**정의** — Page는 전체 건수·페이지 정보를 포함하는 결과 모델이고 Slice는 다음 구간 존재 여부 중심의 결과 모델이다.

**목적과 책임** — 검색·정렬·페이지 응답의 계약을 정의한다.

**구별해야 할 개념 / 흔한 오해** — Page는 전체 건수 계산 비용이 있을 수 있으며 Slice는 전체 페이지 수를 알 필요가 없는 UX에 유리하다.

**실행·처리 흐름** — 검색 조건 → content 조회 → 필요시 count 조회 → 응답 구성

**직접 구현·검증** — 검색 조건 2개 조합과 페이지 이동을 테스트. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Page와 Slice, OFFSET과 키셋의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## 연관관계의 주인과 mappedBy, 벌크 UPDATE 함정 (2026-11-23 보강)

**출처와 확인 범위**: 선생님 자료 `AI_Code_Defect★/JPA + AI 코드 검증 시험공부 자습서`의 주제 A·D 내용을 정리했다. 아래 측정값은 **자료에 적힌 결과**다. 이 PC에서 JPA 테스트를 다시 실행하지는 않았다(미실행).

### 외래키는 "다(N)" 쪽에 생긴다

```
store 테이블                 review 테이블
┌────┬─────────┐           ┌────┬────────┬──────────┐
│ id │ name    │           │ id │ content│ store_id │ ← 외래키(FK)
│ 1  │ 맛집 1  │◄──────────│ 1  │ 리뷰 1 │    1     │
│    │         │◄──────────│ 2  │ 리뷰 2 │    1     │
└────┴─────────┘           └────┴────────┴──────────┘
Store (1) ──────< Review (N)
```

```java
// Review.java: 주인 (외래키를 관리한다)
@ManyToOne
@JoinColumn(name = "store_id")
private Store store;

// Store.java: 거울 (읽기 전용, DB 컬럼을 만들지 않는다)
@OneToMany(mappedBy = "store")
private List<Review> reviews = new ArrayList<>();
```

- **주인**: `@JoinColumn`이 있는 쪽. 외래키 값을 바꿀 수 있다.
- **거울**: `mappedBy`가 있는 쪽. `mappedBy`의 값은 **상대 클래스의 필드 이름**(`store`)이다. 컬럼 이름(`store_id`)이 아니다.

**측정 결과 (자료 기록)**: 거울 쪽 `Store.reviews`에만 `add`하고 커밋하면 DB의 `review.store_id`는 `null`로 남는다. 저장하려면 **주인 쪽** `Review.store`에 값을 넣어야 한다.

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

### 자주 하는 오해

| 오해 | 실제 |
|---|---|
| 거울(`mappedBy`) 쪽에 `add`하면 저장된다 | 주인 쪽에 넣어야 외래키가 바뀐다 |
| `@ManyToOne`은 지연 로딩이다 | 기본은 EAGER다 |
| `mappedBy`에 컬럼 이름을 쓴다 | 상대 클래스의 필드 이름을 쓴다 |
| 외래키는 Store 테이블에 생긴다 | N 쪽(Review) 테이블에 생긴다 |
| 벌크 UPDATE 하면 읽어 둔 객체도 새 값이다 | 옛 값이 남는다. `clearAutomatically`를 쓴다 |

**면접 기준**: "외래키는 다(N) 쪽 테이블에 있고, 그 필드를 가진 쪽이 주인입니다. 반대쪽의 `mappedBy`는 읽기 전용 거울입니다. 벌크 UPDATE는 영속성 컨텍스트를 거치지 않으므로 `clearAutomatically`로 캐시를 비웁니다."

## 엔티티 상태 전이와 flush: 실제 SQL로 확인하기 (2026-11-23 보강, Hibernate 7.4.5에서 확인함)

**출처**: 선생님 자료 `springboot4☆/5장_ORM_자습서.html`의 6~7절(영속성 컨텍스트, 엔티티 상태, flush)을 정리했다. 아래 코드는 Hibernate 7.4.5(JPA 3.2)와 MySQL 8.0으로 실행해, 어느 시점에 SQL이 나가는지 확인했다.

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

**출처**: 선생님 자료 `springboot4☆/5장_ORM_자습서.html`의 7장 화면 자료(`7장 블로그 화면 구성하기`)에 나오는 `@CreatedDate`, `@LastModifiedDate`, `@EnableJpaAuditing`을 정리했다. Spring Data의 방식은 **미실행**이다. 같은 동작을 Hibernate의 `@CreationTimestamp`·`@UpdateTimestamp`로 확인했다.

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

**Spring Data 방식 (미실행)**: 블로그 프로젝트에서는 다음처럼 쓴다. 이 PC에서는 Spring Data 컨텍스트를 띄우지 않아 실행하지 않았다.

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


## 5. MyBatis와 JPA 비교 (국내 실무 대비)

- **JPA:** 객체 중심. 엔티티를 저장하고 조회하면 SQL을 자동으로 만든다. 변경 감지로 UPDATE가 자동으로 나간다.
- **MyBatis:** SQL 중심. 쿼리를 직접 작성하고 결과를 객체에 매핑한다. 쿼리를 정확히 통제할 수 있다.
- **쓰는 방식:** 단순 CRUD와 객체 관계는 JPA, 복잡한 통계나 조회 쿼리는 MyBatis를 섞어 쓰는 경우가 많다.
- **실습:** 같은 조회(예: 사용자별 주문 수)를 JPQL과 MyBatis 매퍼 XML로 각각 작성하고, 실행되는 SQL 로그를 비교한다.
- **면접 기준:** "JPA는 객체를 기준으로 SQL을 자동 생성하고, MyBatis는 SQL을 직접 작성해서 객체에 매핑합니다."

## 6. Querydsl (타입 안전한 쿼리)

- **정의:** 자바 코드로 JPA 쿼리를 작성하는 라이브러리. 문자열 JPQL 대신 메서드 체인을 쓰므로 컴파일 시점에 오타를 잡는다.
- **왜 쓰나:** 조건이 많은 검색 화면에서 조건을 조합할 때 JPQL 문자열이 복잡해진다. Querydsl은 조건을 메서드로 조립한다.
- **예 (미검증 — Querydsl 설정 필요):**

```java
List<Post> result = queryFactory
    .selectFrom(post)
    .where(titleContains(keyword), authorIdEq(authorId))   // 값이 null인 조건은 자동으로 빠진다
    .orderBy(post.createdAt.desc(), post.id.desc())
    .fetch();
```

- **주의:** Q클래스를 생성하는 빌드 설정이 필요하다. 설정이 없으면 `QPost` 클래스를 찾을 수 없다.
- **면접 기준:** "Querydsl은 JPA 쿼리를 자바 코드로 타입 안전하게 조립하는 도구입니다. 조건부 검색에서 문자열 쿼리의 오류를 줄입니다."
