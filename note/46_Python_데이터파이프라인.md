# Python과 데이터 파이프라인: 수집, 변환, 적재를 한 번에 안전하게

> 중요도: **B** · 이 장의 목표: Java와 다른 Python의 기본 문법을 익히고, 공공데이터 API를 수집해 DB에 저장하는 ETL 파이프라인을 만든다. 재실행해도 중복이 생기지 않는 멱등성을 확인한다.

> 2026-11-23 추가 장 (로드맵 M16 "Python, 데이터 파이프라인, ETL/ELT, 재처리" 항목). **Python 3.12.10에서 2절 문법 예제와 5절 파이프라인을 실행해 결과가 기대값과 같음을 확인했다.** 파이프라인을 두 번 적재해도 행 수는 2였다.

---

## 1. 왜 Python인가

- 2장과 17장에서 쓴 Java는 **타입이 엄격하고 서버 애플리케이션**에 강하다.
- Python은 **데이터 수집, 변환, 분석, AI 라이브러리**에 강하다. 로드맵의 AI 트랙(37–52장)과 데이터 분석(47장)이 모두 Python을 쓴다.
- 문법은 짧고, 들여쓰기로 블록을 나눈다. 대신 타입 실수는 **실행할 때** 드러난다.

### 1.1 Java와 나란히 보기

| 주제 | Java | Python |
|---|---|---|
| 블록 | `{ }` | 들여쓰기 (4칸) |
| 변수 선언 | `int x = 1;` | `x = 1` |
| 타입 | 컴파일 시 검사 | 실행 시 검사 (타입 힌트는 선택) |
| 리스트 | `List<Integer>` | `list[int]` 또는 `[1, 2, 3]` |
| 딕셔너리 | `Map<String, Integer>` | `dict[str, int]` 또는 `{"a": 1}` |
| 예외 | `try / catch` | `try / except` |
| 패키지 | `pom.xml`, Gradle | `pip`, `venv`, `requirements.txt` |

---

## 2. 기본 문법

```python
# 리스트 컴프리헨션: 조건에 맞는 값만 변환해서 새 리스트를 만든다
prices = [1200, 0, 3400, None, 800]
valid = [p for p in prices if p is not None and p > 0]
print(valid)  # [1200, 3400, 800]

# dict 순회
stock = {"apple": 3, "pear": 0}
for name, qty in stock.items():
    print(name, qty)

# 타입 힌트: 실행에는 영향이 없지만 읽기와 도구 검사에 쓴다
def total(values: list[int]) -> int:
    return sum(values)
```

**주의**

- `None`은 Java의 `null`과 같다. `p > 0`을 먼저 쓰면 `None > 0`에서 `TypeError`가 난다. 그래서 `p is not None`을 앞에 둔다.
- 리스트를 기본 인자로 주면(`def f(items=[])`) 호출 사이에 값이 공유된다. 기본 인자에는 `None`을 쓴다.

---

## 3. 환경 분리: venv

```bash
python -m venv .venv
source .venv/bin/activate        # Windows PowerShell: .venv\Scripts\Activate.ps1
pip install requests
pip freeze > requirements.txt
```

프로젝트마다 `.venv`를 따로 만든다. 시스템 Python에 패키지를 섞으면 다른 프로젝트와 버전이 충돌한다. `.venv/`는 Git에 올리지 않는다.

---

## 4. 파이프라인의 세 단계

| 단계 | 하는 일 | 실패할 때 |
|---|---|---|
| Extract (수집) | API에서 JSON이나 XML을 받는다 | 타임아웃, 429(요청 제한), 응답 형식 변경 |
| Transform (변환) | 필드를 고르고 타입을 맞추고 잘못된 행을 걸러낸다 | 필드 누락, 날짜 형식 불일치 |
| Load (적재) | DB에 넣는다 | 같은 데이터가 두 번 들어감 (중복) |

ETL은 변환을 적재 전에 하고, ELT는 원본을 먼저 적재한 뒤 DB 안에서 변환한다. 소규모 파이프라인에서는 ETL이 디버깅하기 쉽다.

---

## 5. 최소 파이프라인 예제

공공데이터 API 대신 **JSON 문자열**을 입력으로 써서 네트워크 없이 흐름을 확인한다.

```python
import json
import sqlite3

RAW = """
[
  {"id": "A1", "name": "서울점", "sales": "1200"},
  {"id": "A2", "name": "부산점", "sales": "abc"},
  {"id": "A3", "name": "대구점", "sales": "800"}
]
"""

def extract(raw_text: str) -> list[dict]:
    return json.loads(raw_text)

def transform(rows: list[dict]) -> list[tuple[str, str, int]]:
    result = []
    for row in rows:
        try:
            sales = int(row["sales"])      # 문자열을 정수로
        except (KeyError, ValueError):
            continue                        # 잘못된 행은 건너뛴다 (기록은 남기는 것이 좋다)
        result.append((row["id"], row["name"], sales))
    return result

def load(conn: sqlite3.Connection, rows: list[tuple[str, str, int]]) -> None:
    # 같은 id가 있으면 덮어쓴다 (멱등성)
    conn.executemany(
        "INSERT INTO store (id, name, sales) VALUES (?, ?, ?) "
        "ON CONFLICT(id) DO UPDATE SET name = excluded.name, sales = excluded.sales",
        rows,
    )
    conn.commit()

if __name__ == "__main__":
    conn = sqlite3.connect(":memory:")
    conn.execute("CREATE TABLE store (id TEXT PRIMARY KEY, name TEXT, sales INTEGER)")
    rows = transform(extract(RAW))
    load(conn, rows)
    load(conn, rows)  # 두 번 실행해도 행 수가 같아야 한다
    print(conn.execute("SELECT COUNT(*) FROM store").fetchone()[0])  # 기대값: 2
```

**실행 결과:** `2` (확인함). A2는 `sales`가 숫자가 아니어서 건너뛰고, 두 번 적재해도 2행이다.

`ON CONFLICT ... DO UPDATE`는 SQLite 문법이다. MySQL에서는 `INSERT ... ON DUPLICATE KEY UPDATE`를 쓴다. 43장의 ETL 멱등성 설명과 같은 원리다.

---

## 6. 재처리 (Backfill)

파이프라인은 **같은 날짜를 여러 번 다시 돌릴 수 있어야** 한다. 원인:

- 수집 도중 실패해서 일부만 들어감
- 원본 데이터가 나중에 수정됨
- 변환 규칙을 고쳐서 과거 데이터를 다시 계산해야 함

재처리 규칙:

1. 실행 단위를 날짜(또는 배치 번호)로 나눈다.
2. 적재는 **덮어쓰기 또는 upsert**로 한다.
3. 실행 기록(시작, 끝, 행 수, 실패 사유)을 남긴다.

---

## 7. 실습 과제

1. 위 예제를 `pipeline.py`로 저장하고 `python pipeline.py`를 실행해 `2`가 나오는지 확인한다.
2. `RAW`에 잘못된 행을 하나 더 넣고, 행 수가 어떻게 바뀌는지 적는다.
3. `load`에서 `ON CONFLICT` 절을 빼고 두 번 실행해 행 수가 얼마가 되는지 확인한다.
4. 공공데이터포털에서 한 가지 API를 골라 응답 필드 세 개를 적는다. (인증키는 적지 않는다.)

---

## 단원 통과 기준

- [ ] Java와 Python의 블록, 타입, 예외 문법 차이를 세 가지 이상 말할 수 있다.
- [ ] venv를 만드는 이유를 설명할 수 있다.
- [ ] ETL의 세 단계에서 실패할 수 있는 지점을 각각 말할 수 있다.
- [ ] 같은 파이프라인을 두 번 실행해도 결과가 같아야 하는 이유를 설명할 수 있다.
