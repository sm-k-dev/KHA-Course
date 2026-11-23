# DB 마이그레이션 (Flyway): 스키마 변경을 버전으로 관리한다

> 중요도: **A** · 이 장의 목표: 스키마를 `ALTER`로 손으로 바꾸는 대신 버전 번호가 붙은 마이그레이션 파일로 관리하고, 여러 환경에서 같은 순서로 적용되게 한다.

> 2026-11-23 추가 장 (로드맵 1순위 "DB 마이그레이션" 항목). 아래 SQL 마이그레이션 파일은 MySQL 8.0에서 **버전 순서대로 실행**해 확인했다. Flyway 도구 자체(Maven 플러그인, 체크섬 검증)는 실행하지 않았다. 해당 부분은 동작 원리로만 설명한다.

---

## 1. 문제: 스키마가 환경마다 달라진다

| 상황 | 결과 |
|---|---|
| 개발 DB에서만 `ALTER TABLE`을 실행 | 운영 DB는 컬럼이 없어서 배포 후 에러 |
| 누가 어떤 변경을 했는지 기록이 없다 | 장애 때 원인을 추적할 수 없다 |
| 두 사람이 같은 테이블을 다르게 고친다 | 병합할 방법이 없다 |

**해결**: 스키마 변경을 **번호가 붙은 파일**로 기록하고, 도구가 **아직 적용되지 않은 파일만** 순서대로 실행한다.

---

## 2. Flyway의 기본 규칙

**정의** — Flyway는 마이그레이션 파일을 버전 순서로 실행하고, 어디까지 적용했는지를 `flyway_schema_history` 테이블에 기록하는 도구다.

- 파일 이름: `V<버전>__<설명>.sql` (예: `V2__add_user_email.sql`)
- 버전은 정수 또는 `1.1`처럼 점이 있는 숫자다. 두 밑줄 `__`이 버전과 설명을 나눈다.
- 한 번 적용된 파일은 **내용을 고치지 않는다.** 고치면 체크섬이 달라져 검증에서 실패한다.
- 잘못된 변경은 **새 버전 파일로 되돌린다.** 예전 파일을 지우거나 고치지 않는다.

---

## 3. 마이그레이션 예제

`V1__create_user.sql`

```sql
CREATE TABLE app_user (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL
);
```

`V2__add_user_email.sql`

```sql
ALTER TABLE app_user ADD COLUMN email VARCHAR(255) NULL;
CREATE UNIQUE INDEX uk_app_user_email ON app_user (email);
```

`V3__backfill_email.sql`

```sql
-- 기존 행에 임시 이메일을 채운다 (데이터 이관)
UPDATE app_user SET email = CONCAT('user', id, '@example.invalid') WHERE email IS NULL;
```

`V4__email_not_null.sql`

```sql
ALTER TABLE app_user MODIFY COLUMN email VARCHAR(255) NOT NULL;
```

**순서가 중요한 이유**: V2에서 컬럼을 `NULL`로 추가하고, V3에서 값을 채운 뒤, V4에서 `NOT NULL`로 바꾼다. 한 번에 `NOT NULL`로 추가하면 기존 행이 있을 때 실패한다. 이 "추가 → 이관 → 제약" 순서는 무중단 배포에서도 같다.

---

## 4. 검증 (MySQL 8.0)

파일을 순서대로 적용하고, 결과 스키마와 데이터를 확인했다. 임시 데이터베이스에서 실행하고 삭제했다.

```sql
CREATE DATABASE flyway_check;
USE flyway_check;
-- V1, V2, V3, V4 순서로 실행
-- 기존 행 2개를 V1 이후에 넣고 V2~V4를 실행한다
```

**확인 결과**

- 기존 행 2개에 `email`이 `user1@example.invalid`, `user2@example.invalid`로 채워졌다.
- V4 이후 `email`은 `NOT NULL`이다.
- 같은 V2 파일을 다시 실행하면 `Duplicate column name 'email'` 오류가 난다. Flyway는 이 상황을 막기 위해 이미 적용된 버전을 다시 실행하지 않는다.

---

## 5. 운영 규칙

1. 마이그레이션 파일은 **리뷰를 거쳐 Git에 올린 뒤**에만 적용한다.
2. 운영 DB에 적용된 파일은 수정하지 않는다. 되돌리기도 새 버전으로 한다.
3. 큰 테이블의 `ALTER`는 잠금 시간을 확인한다. 한 번에 바꾸지 말고 나눠서 실행한다.
4. JPA의 `ddl-auto=update`는 개발에서만 쓴다. 운영에서는 마이그레이션 도구가 스키마를 관리하게 하고, JPA는 `validate`로 맞는지만 확인한다.

---

## 6. 실습 과제

1. 위 V1부터 V4까지 MySQL 임시 DB에서 실행하고, `SHOW CREATE TABLE app_user`로 최종 스키마를 확인한다.
2. V4 파일을 `NULL` 허용으로 되돌리는 V5 파일을 작성한다. V4를 고치지 않는다.
3. "이미 적용된 V2 파일을 고쳤다"는 상황에서 Flyway가 무엇을 검사하고 어떤 오류를 낼지 한 문단으로 쓴다.

---

## 단원 통과 기준

- [ ] `ALTER`를 손으로 실행하는 방식의 문제 세 가지를 말할 수 있다.
- [ ] 버전 파일을 고치면 안 되는 이유와 되돌리기 방법을 설명할 수 있다.
- [ ] 컬럼을 `NOT NULL`로 추가할 때 "추가 → 이관 → 제약" 순서가 필요한 이유를 설명할 수 있다.
- [ ] JPA `ddl-auto`를 운영에서 쓰면 안 되는 이유를 설명할 수 있다.
