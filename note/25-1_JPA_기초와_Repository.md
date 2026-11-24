# JPA 25-1: 기초와 Repository

> 이 파트는 [25장 JPA 허브](25_JPA.md)의 일부입니다. 다음 파트: [25-2 연관관계와 N+1](25-2_연관관계와_N+1.md), [25-3 쿼리](25-3_쿼리.md), [25-4 로딩·운영·MyBatis](25-4_로딩_운영_MyBatis.md).

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

