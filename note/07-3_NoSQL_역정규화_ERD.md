# 관계형 DB 07-3: NoSQL·역정규화·ERD

> 이 파트는 [07장 허브](07_RDBMS.md)의 일부입니다. 이전: [07-2](07-2_트랜잭션_인덱스_격리.md).

## 관계형 DB와 NoSQL: 표인가 문서인가 (2026-11-23 보강, MySQL 8.0에서 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

| 구분 | 관계형 DB (RDB) | NoSQL (문서·키-값) |
|---|---|---|
| 구조 | 표(행·열), 기본키로 구분 | 문서(JSON 모양), 또는 키와 값 |
| 조회 | SQL로 조건을 걸어 찾는다 | 키로 찾거나 문서 내용으로 찾는다 |
| 스키마 | 표 구조를 미리 정한다 | 문서마다 필드가 달라도 된다 |
| 대표 제품 | MySQL, PostgreSQL, MariaDB | MongoDB, Redis |

**확인 결과 (MySQL 8.0, 임시 DB를 만들어 실행 후 삭제)**

```sql
CREATE TABLE memo_rdb (id INT PRIMARY KEY, title VARCHAR(50), author VARCHAR(20));
INSERT INTO memo_rdb VALUES (1,'buy milk','kim'),(2,'read book','lee');

CREATE TABLE memo_doc (id INT PRIMARY KEY, doc JSON);
INSERT INTO memo_doc VALUES
  (1, JSON_OBJECT('title','buy milk','author','kim','tags',JSON_ARRAY('home','food'))),
  (2, JSON_OBJECT('title','read book','author','lee','pages',320));

SELECT COUNT(*) FROM memo_rdb WHERE author='kim';                                          -- 1
SELECT COUNT(*) FROM memo_doc WHERE JSON_UNQUOTE(JSON_EXTRACT(doc,'$.author'))='kim';       -- 1
SELECT JSON_EXTRACT(doc,'$.pages') FROM memo_doc WHERE id=2;                                -- 320
SELECT JSON_EXTRACT(doc,'$.tags')  FROM memo_doc WHERE id=1;                                -- ["home", "food"]
```

**읽는 법**
- 두 방식 모두 `kim`을 찾는다. 차이는 **구조를 바꿀 때**다. `pages`는 2번 문서에만 있어도 저장된다. RDB라면 `ALTER TABLE`로 열을 먼저 추가해야 한다.
- MySQL의 `JSON` 열은 관계형 DB 안에서 문서를 쓰는 방법이다. 완전한 NoSQL과는 다르다. 필드가 자주 바뀌는 일부 데이터에만 쓴다.
- Redis 같은 키-값 저장소는 캐시나 세션처럼 빠른 키 조회에 쓴다(42장 캐시).

**선택 기준**: 데이터가 표 모양이고 관계(회원 ↔ 주문)가 많으면 RDB를 쓴다. 구조가 자주 바뀌고 문서 단위로 읽고 쓰면 NoSQL을 검토한다. 대부분의 백엔드 기본 선택은 RDB다(이 교재의 Spring 실습도 MySQL 기준).

**면접 기준**: "관계형 DB는 표와 SQL로 관계를 다루고 구조가 엄격합니다. NoSQL은 문서나 키-값으로 구조가 유연하고 대량 조회에 강점이 있지만, 관계와 일관성은 직접 챙겨야 합니다."


## 7. 역정규화 (Denormalization)

- **정의:** 정규화로 나눈 테이블을 조회 성능을 위해 일부러 다시 합치거나 중복을 두는 것이다.
- **언제:** 조회가 매우 많고 JOIN 비용이 문제가 될 때, 측정으로 확인한 뒤에만 한다. 먼저 인덱스로 해결할 수 있는지 본다.
- **대가:** 중복된 값을 함께 고쳐야 하므로 갱신 이상이 다시 생긴다. 갱신 경로를 한 곳으로 모은다.
- **예:** 주문 목록 화면에 고객 이름을 매번 JOIN하지 않고 주문 테이블에 복사해 둔다. 고객 이름이 바뀌면 어떻게 할지 정해 둔다.
- **면접 기준:** "역정규화는 측정한 조회 병목을 줄이려고 중복을 허용하는 선택이고, 갱신 이상을 어떻게 막을지 함께 설계합니다."

## 8. ERD (개체-관계 다이어그램) 그리기

- **정의:** 테이블(개체)과 테이블 사이의 관계를 그림으로 나타낸 것. 데이터 모델링의 기본 도구다.
- **요소:** 개체(테이블), 속성(컬럼), 식별자(기본 키), 관계(외래 키). 관계에는 **카디널리티**(1:1, 1:N, N:M)를 적는다.
- **예:** 부서 1개에는 직원이 여러 명 속한다 → 부서 `1` : 직원 `N`. 직원 테이블에 `department_id`(외래 키)를 둔다.
- **N:M은 중간 테이블로 푼다:** 학생과 과목의 N:M은 `수강(student_id, course_id)` 테이블로 바꾼다. 중간 테이블의 기본 키는 두 외래 키의 조합이다.
- **순서:** 개념 모델(업무 용어) → 논리 모델(속성과 관계) → 물리 모델(실제 테이블과 타입). 35장 UML 설명과 함께 본다.
- **실습:** 게시판 예제(사용자, 글, 댓글)의 ERD를 그리고, 그림에서 외래 키와 카디널리티를 SQL `CREATE TABLE`로 옮긴다.
- **면접 기준:** "ERD는 테이블과 관계를 그린 것입니다. 1:N은 N쪽에 외래 키를 두고, N:M은 중간 테이블로 나눕니다."
