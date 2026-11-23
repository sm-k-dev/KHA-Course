# SQL과 데이터 조회

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `90_심화_참고과목/SQL`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — 0행 · 0 · NULL · COUNT

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

`SELECT * ... WHERE id=99`가 0행이면 **결과 행이 존재하지 않는 것**이다. `SELECT COUNT(*) ...`는 조건에 맞는 행이 없어도 **숫자 0을 담은 결과 행 하나**를 반환한다.

### 2. 용어를 구분해야 하는 이유

`NULL`은 특정 열 값의 부재·미상을 나타내는 표식이지, 숫자 0이나 빈 문자열, 조회 결과 0행과 같지 않다.

### 3. 새로운 상황에 적용

`SELECT COUNT(*) FROM users WHERE id=99`와 `SELECT id FROM users WHERE id=99`의 반환 행 수와 값의 차이를 설명한다.

### 4. 면접 질문과 답변 기준

**질문:** 0행 · 0 · NULL · COUNT의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** 조건에 맞는 행이 없는 일반 조회는 결과가 0행입니다. 집계 함수 COUNT(*)는 일치 행 수를 계산하므로 0을 포함한 결과 행을 반환할 수 있습니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. SELECT 논리적 처리 순서

**정의** — SQL은 선언적으로 원하는 결과를 표현하며, 조회를 이해할 때 FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT의 논리적 순서를 참고할 수 있다.

**목적과 책임** — 조건·집계·정렬·결과 제한을 분리해 표현한다.

**구별해야 할 개념 / 흔한 오해** — 논리적 순서는 물리적 실행 계획과 같지 않다.

**실행·처리 흐름** — 테이블 결합 → 행 필터 → 그룹화 → 그룹 필터 → 결과 투영 → 정렬

**직접 구현·검증** — WHERE와 HAVING의 조건을 바꾸어 결과 차이 확인. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — WHERE와 HAVING의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

SQL은 선언형 언어다. SELECT 문장은 작성 순서대로 실행된다고 단정할 수 없다. 논리적 평가 단계는 FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → DISTINCT → ORDER BY → LIMIT 등으로 이해할 수 있지만 실제 물리적 실행 순서는 옵티마이저가 재배치한다.

### 직접 읽고 구현하는 독립 예제

```sql
SELECT department_id, COUNT(*) AS cnt
FROM employee
WHERE department_id IS NOT NULL
GROUP BY department_id
HAVING COUNT(*) >= 2
ORDER BY cnt DESC;
```


### 실행 검증 체크포인트

WHERE는 행 필터, HAVING은 그룹 필터임을 작은 데이터로 확인한다.

### 스스로 작성할 변형 실습

SELECT 별칭을 WHERE에서 바로 쓰지 못하는 일반적 이유를 설명한다.

### 면접 답변의 기준 문장

> SQL의 논리적 처리 순서는 결과의 의미를 이해하기 위한 모델이고 물리 실행은 최적화될 수 있습니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“SQL은 선언적으로 원하는 결과를 표현하며, 조회를 이해할 때 FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT의 논리적 순서를 참고할 수 있다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“논리적 순서는 물리적 실행 계획과 같지 않다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“WHERE와 HAVING의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. INNER JOIN·LEFT JOIN

**정의** — JOIN은 관련된 행을 결합하는 연산이며 INNER JOIN은 조건에 맞는 조합, LEFT JOIN은 왼쪽 행을 보존하고 미일치 오른쪽 값을 NULL로 채운다.

**목적과 책임** — 테이블 사이의 관계를 조회 결과로 결합한다.

**구별해야 할 개념 / 흔한 오해** — LEFT JOIN 뒤 WHERE에서 오른쪽 열의 NULL을 제거하면 의도치 않게 미일치 행이 사라질 수 있다.

**실행·처리 흐름** — 왼쪽 행 × 매칭 후보 → ON 판정 → 외부 조인 미일치 보존 → WHERE

**직접 구현·검증** — 게시글과 댓글 수를 조회하되 댓글 없는 글도 표시. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — JOIN 조건을 ON에 둘 때와 WHERE에 둘 때 결과가 달라지는 예는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

INNER JOIN은 결합 조건을 만족하는 행만 남기고 LEFT JOIN은 왼쪽 행을 보존하며 오른쪽에 매칭이 없으면 NULL로 채운다. LEFT JOIN 뒤 WHERE에서 오른쪽 테이블 컬럼에 NULL 불허 조건을 걸면 의도와 달리 미매칭 행이 사라질 수 있다. 1:N JOIN은 행 수를 늘릴 수 있어 페이징과 집계에서 주의해야 한다.

### 직접 읽고 구현하는 독립 예제

```sql
SELECT d.id, e.id AS employee_id
FROM department d LEFT JOIN employee e ON e.department_id=d.id;
-- 부서별 직원 수
SELECT d.id, COUNT(e.id) FROM department d
LEFT JOIN employee e ON e.department_id=d.id GROUP BY d.id;
```


### 실행 검증 체크포인트

직원이 없는 부서가 남고 COUNT(e.id)는 0이 되는지 확인한다.

### 스스로 작성할 변형 실습

ON과 WHERE에 e.id 조건을 각각 두고 결과 차이를 설명한다.

### 면접 답변의 기준 문장

> INNER JOIN은 조건에 맞는 행을, LEFT JOIN은 왼쪽 행을 모두 유지하는 결합입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“JOIN은 관련된 행을 결합하는 연산이며 INNER JOIN은 조건에 맞는 조합, LEFT JOIN은 왼쪽 행을 보존하고 미일치 오른쪽 값을 NULL로 채운다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“LEFT JOIN 뒤 WHERE에서 오른쪽 열의 NULL을 제거하면 의도치 않게 미일치 행이 사라질 수 있다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“JOIN 조건을 ON에 둘 때와 WHERE에 둘 때 결과가 달라지는 예는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. NULL·3값 논리

**정의** — NULL은 값이 없거나 알려지지 않았음을 나타내는 표시이며 SQL 조건은 TRUE·FALSE·UNKNOWN의 세 값을 고려한다.

**목적과 책임** — 미정 값의 표현과 조건 판정에 영향을 준다.

**구별해야 할 개념 / 흔한 오해** — NULL = NULL은 TRUE가 아니다. IS NULL을 사용해야 한다. COUNT(*)와 COUNT(열)의 결과가 다를 수 있다.

**실행·처리 흐름** — NULL 비교 → UNKNOWN → WHERE는 TRUE 행만 유지

**직접 구현·검증** — NULL이 있는 데이터에서 COUNT(*)와 COUNT(column)를 비교. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — SQL에서 NULL을 = 연산자로 검사하면 왜 안 되는가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

NULL은 값이 없거나 알려지지 않았음을 표현하는 표지로 0이나 빈 문자열과 다르다. SQL 비교 결과는 TRUE·FALSE·UNKNOWN이 될 수 있으며 WHERE는 TRUE인 행만 통과시킨다. NULL 검사에는 IS NULL을 사용한다. 집계 함수 COUNT(column)는 NULL을 제외한다.

### 직접 읽고 구현하는 독립 예제

```sql
SELECT id FROM employee WHERE department_id IS NULL;
SELECT COUNT(*) AS rows_all, COUNT(department_id) AS assigned
FROM employee;
```


### 실행 검증 체크포인트

NULL을 가진 행이 첫 질의에 나타나고 두 COUNT 결과가 다를 수 있음을 확인한다.

### 스스로 작성할 변형 실습

NOT IN 목록에 NULL이 포함될 때 결과가 왜 예상과 달라질 수 있는지 실험한다.

### 면접 답변의 기준 문장

> NULL은 일반 값이 아니며 비교에 UNKNOWN이 발생할 수 있어 IS NULL 등 별도 처리가 필요합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“NULL은 값이 없거나 알려지지 않았음을 나타내는 표시이며 SQL 조건은 TRUE·FALSE·UNKNOWN의 세 값을 고려한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“NULL = NULL은 TRUE가 아니다. IS NULL을 사용해야 한다. COUNT(*)와 COUNT(열)의 결과가 다를 수 있다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“SQL에서 NULL을 = 연산자로 검사하면 왜 안 되는가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 4. 검색·페이징

**정의** — OFFSET 기반 페이징은 앞선 행을 건너뛰고 일정 수를 조회하며, 키셋 페이징은 마지막으로 본 정렬 키 이후를 조회한다.

**목적과 책임** — 대량 결과를 나누어 제공하고 사용자 탐색을 돕는다.

**구별해야 할 개념 / 흔한 오해** — LIMIT/OFFSET은 안정적인 ORDER BY가 필요하며, 데이터 변경 중 중복·누락이 발생할 수 있다.

**실행·처리 흐름** — 검색 조건 → 정렬 키 확정 → 페이지 조건 → 조회 → 다음 페이지 정보

**직접 구현·검증** — created_at와 id 복합 정렬로 키셋 페이징 구현. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — OFFSET과 키셋 페이징의 장단점은?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

검색은 조건을 적용해 결과 집합을 좁히는 작업이고 페이징은 그 결과의 일부 구간을 제공하는 방식이다. OFFSET은 구현이 간단하지만 깊은 페이지에서 비효율적이고 데이터 삽입 중 중복·누락이 생길 수 있다. 키셋 페이징은 안정적인 정렬 키를 기준으로 다음 범위를 조회한다.

### 직접 읽고 구현하는 독립 예제

```sql
SELECT id, title FROM board
WHERE title LIKE CONCAT('%', ?, '%')
ORDER BY created_at DESC, id DESC LIMIT 20 OFFSET 40;
-- 커서 예: 이전 마지막 행의 created_at, id를 바인딩
SELECT id, title FROM board
WHERE (created_at, id) < (?, ?)
ORDER BY created_at DESC, id DESC LIMIT 20;
```


### 실행 검증 체크포인트

같은 정렬에서 페이지 간 중복이 없는지 테스트하고, ?는 PreparedStatement 바인딩으로 실행한다.

### 스스로 작성할 변형 실습

동일 created_at 값이 많은 데이터에서 id 보조 정렬이 왜 필요한지 설명한다.

### 면접 답변의 기준 문장

> 페이징은 정렬된 결과 일부를 조회하는 기법이며 OFFSET과 키셋 방식은 성능·탐색 경험이 다릅니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: 조회 결과의 모양을 먼저 예측하기

```sql
SELECT * FROM users WHERE id = 999;        -- 조건을 만족하는 행이 없다면 0행
SELECT COUNT(*) FROM users WHERE id = 999; -- GROUP BY가 없으므로 0이라는 값을 담은 1행
SELECT name FROM users WHERE id = 1;       -- 행 1개, name이 nullable이면 값이 NULL일 수 있음
```

**주의:** `SELECT`가 0행이면 JDBC에서 `ResultSet.next()`가 처음부터 `false`가 된다. 이는 `getString(...)`이 `null`을 반환한 상황과 다르다. 행이 존재하는데 SQL NULL을 읽으면 `getString`은 Java `null`을 돌려줄 수 있고, `getInt`는 SQL NULL일 때 0을 돌려줄 수 있으므로 **반드시 `wasNull()`로 구분**해야 한다.

```java
try (var ps = connection.prepareStatement("SELECT age FROM users WHERE id = ?")) {
    ps.setLong(1, 1L);
    try (var rs = ps.executeQuery()) {
        if (!rs.next()) {
            System.out.println("행 없음");
        } else {
            int age = rs.getInt("age");
            System.out.println(rs.wasNull() ? "나이 NULL" : "나이 " + age);
        }
    }
}
```

**전제:** `connection`은 이미 획득한 JDBC Connection이며, 예제 실행을 위해 `users.age`라는 nullable 정수 컬럼이 있어야 한다. JDBC의 `getInt`와 `wasNull`을 연달아 사용해야 직전 읽기 결과를 정확히 검사할 수 있다.

**실습:** `SELECT id`, `COUNT(*)`, `LEFT JOIN`의 매칭 실패를 각각 실행하고 **행 수/각 컬럼 값/Java 변환 결과** 세 칸으로 표를 작성하라. `LEFT JOIN`의 오른쪽 컬럼 NULL은 오른쪽 행이 없어서 생성된 NULL일 수도 있으므로 단순히 전체 결과가 0행인 것은 아니다.


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. 0 rows / 0 / NULL / COUNT

**흔한 오해:** “조회 결과가 없으면 SQL이 0을 돌려준다”

**정확한 구분:** 일반 SELECT는 0행의 결과 집합을 돌려줄 수 있다. COUNT(*) 집계는 조건에 맞는 행이 없으면 0이라는 값을 가진 한 행을 돌려준다(그룹 없는 집계 기준). NULL은 셀 값의 부재다.

**코드·명령 해석:**

```sql
SELECT * FROM users WHERE id=-1;
SELECT COUNT(*) FROM users WHERE id=-1;
SELECT NULL AS value;
```

**확인 방법:** 각 SQL의 결과 행 수와 각 행의 값이 무엇인지 따로 기록한다.

**직접 구현:** COUNT(column)과 COUNT(*)가 NULL을 처리하는 방식도 비교하라.

**면접 점검:** “0 rows / 0 / NULL / COUNT”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. LEFT JOIN의 ON / WHERE

**흔한 오해:** “LEFT JOIN 뒤의 WHERE는 언제나 왼쪽 행을 유지한다”

**정확한 구분:** ON은 결합 조건이고 WHERE는 결합된 결과의 필터다. 오른쪽 테이블의 컬럼에 WHERE 조건을 걸면 NULL 확장 행이 제거되어 의도치 않게 INNER JOIN처럼 보일 수 있다.

**코드·명령 해석:**

```sql
SELECT u.id, o.id FROM users u LEFT JOIN orders o ON o.user_id=u.id AND o.status='PAID';
SELECT u.id, o.id FROM users u LEFT JOIN orders o ON o.user_id=u.id WHERE o.status='PAID';
```

**확인 방법:** 주문이 없는 사용자 한 명을 넣고 두 쿼리의 행 수를 비교한다.

**직접 구현:** 주문이 없는 사용자만 조회하는 IS NULL 조건을 작성하라.

**면접 점검:** “LEFT JOIN의 ON / WHERE”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. WHERE / HAVING / ORDER BY / LIMIT

**흔한 오해:** “HAVING은 WHERE와 완전히 같다”

**정확한 구분:** WHERE는 그룹화 전 행을 필터하고 HAVING은 그룹화된 결과를 필터한다. ORDER BY는 정렬, LIMIT는 반환 개수 제한이다. 실제 SQL 처리와 DB 엔진의 물리적 실행 계획은 구분해야 한다.

**코드·명령 해석:**

```sql
SELECT team_id, COUNT(*) AS cnt FROM users WHERE active=1 GROUP BY team_id HAVING COUNT(*) >= 2 ORDER BY cnt DESC LIMIT 5;
```

**확인 방법:** GROUP BY 없이 집계 열을 WHERE에 두면 왜 안 되는지 설명한다.

**직접 구현:** 동점 cnt가 생길 때 안정적인 정렬 기준을 추가하라.

**면접 점검:** “WHERE / HAVING / ORDER BY / LIMIT”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“OFFSET 기반 페이징은 앞선 행을 건너뛰고 일정 수를 조회하며, 키셋 페이징은 마지막으로 본 정렬 키 이후를 조회한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“LIMIT/OFFSET은 안정적인 ORDER BY가 필요하며, 데이터 변경 중 중복·누락이 발생할 수 있다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“OFFSET과 키셋 페이징의 장단점은?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.


## 4-1. 서브쿼리·CTE·윈도우 함수 (2026-11-23 보강, MySQL 8.0.21에서 확인함)

세 가지는 면접과 실무 쿼리에서 자주 나온다. 아래 데이터로 모두 실행해 확인했다.

```sql
CREATE TABLE customer (id INT PRIMARY KEY, name VARCHAR(20));
CREATE TABLE purchase (id INT PRIMARY KEY, customer_id INT, amount INT, bought_at DATE);
INSERT INTO customer VALUES (1,'kim'),(2,'lee'),(3,'park');
INSERT INTO purchase VALUES (1,1,1000,'2026-01-01'),(2,1,3000,'2026-02-01'),(3,2,500,'2026-01-15'),(4,2,700,'2026-03-01'),(5,1,200,'2026-03-05');

-- 서브쿼리: 평균보다 큰 구매
SELECT id, amount FROM purchase WHERE amount > (SELECT AVG(amount) FROM purchase);

-- IN 서브쿼리: 구매한 적 있는 고객
SELECT name FROM customer WHERE id IN (SELECT customer_id FROM purchase);

-- NOT EXISTS: 구매 없는 고객
SELECT c.name FROM customer c WHERE NOT EXISTS (SELECT 1 FROM purchase p WHERE p.customer_id = c.id);

-- CTE(WITH): 고객별 합계 후 정렬
WITH totals AS (SELECT customer_id, SUM(amount) AS total FROM purchase GROUP BY customer_id)
SELECT c.name, t.total FROM totals t JOIN customer c ON c.id = t.customer_id ORDER BY t.total DESC;

-- 윈도우 함수: 고객별 구매 순번과 누적합 (GROUP BY처럼 행을 합치지 않는다)
SELECT customer_id, bought_at, amount,
       ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY bought_at) AS seq,
       SUM(amount) OVER (PARTITION BY customer_id ORDER BY bought_at) AS running_total
FROM purchase ORDER BY customer_id, bought_at;

-- 윈도우 함수로 고객별 최신 구매 1건
SELECT customer_id, id, amount FROM (
  SELECT customer_id, id, amount, ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY bought_at DESC) AS rn
  FROM purchase) x WHERE rn = 1 ORDER BY customer_id;
```

**실행 결과 (확인함)**

| 쿼리 | 결과 |
|---|---|
| 평균(1080)보다 큰 구매 | id 2, amount 3000 |
| 구매한 고객 | kim, lee |
| 구매 없는 고객 | park |
| CTE 합계 | kim 4200, lee 1200 |
| 고객별 최신 구매 | 고객 1은 id 5(200), 고객 2는 id 4(700) |

윈도우 함수 결과 일부: 고객 1의 `running_total`은 1000 → 4000 → 4200으로 늘어난다.

**읽는 법**
- **서브쿼리**: 안쪽 결과를 바깥 조건에 쓴다. 상관 서브쿼리(`NOT EXISTS`)는 바깥 행마다 안쪽을 확인한다.
- **CTE**: `WITH`로 중간 결과에 이름을 붙여 쿼리를 읽기 쉽게 나눈다. 재사용이 편하지만 성능은 실행 계획으로 확인한다.
- **윈도우 함수**: `GROUP BY`는 행을 합치지만, `OVER (PARTITION BY ...)`는 행을 유지하면서 그룹 안에서 순번이나 누적을 구한다. `ROW_NUMBER`는 MySQL 8.0 이상에서 쓸 수 있다.
- `NOT IN`은 서브쿼리 결과에 NULL이 있으면 결과가 모두 비는 함정이 있다. `NOT EXISTS`가 더 안전하다(3절의 3값 논리와 연결).

**면접 기준**: "서브쿼리는 안쪽 결과를 조건에 쓰고, CTE는 중간 결과에 이름을 붙이며, 윈도우 함수는 행을 합치지 않고 그룹 안의 순번과 합계를 구합니다."

## 5. Oracle과 MySQL 차이 (곁다리 학습 대비)

| 항목 | MySQL | Oracle |
|---|---|---|
| 자동 번호 | AUTO_INCREMENT | SEQUENCE (12c 이후 IDENTITY도 가능) |
| 행 제한 | LIMIT 20 OFFSET 40 | FETCH FIRST 20 ROWS ONLY, OFFSET 40 ROWS (예전에는 ROWNUM) |
| NULL 대체 | IFNULL, COALESCE | NVL, COALESCE |
| 문자열 연결 | CONCAT() | 세로선 두 개(\|\|) 연산자 |
| 단일 행 조회 | FROM 생략 가능 | FROM DUAL 필요 (`SELECT 1 FROM DUAL`) |

- MySQL의 `SELECT 1 FROM DUAL`도 호환용으로 동작하지만, Oracle을 쓸 때는 FROM DUAL이 필요하다는 점만 기억하면 된다.
- 금융과 공공 쪽 공고에서는 Oracle을 요구하는 경우가 있으니, 위 차이를 한 번 정리해 두면 된다.
