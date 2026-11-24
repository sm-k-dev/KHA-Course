# AJAX·REST API

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `09_AJAX_HTTP_JSON`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — AJAX · Fetch · REST · JSON

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

AJAX는 페이지 전체를 새로고침하지 않고 서버와 비동기 통신하는 방식이며, `fetch`는 웹 플랫폼의 요청 API다. REST는 아키텍처 스타일, JSON은 데이터 표현 형식이다.

### 2. 용어를 구분해야 하는 이유

JSON을 사용한다고 REST API가 되는 것은 아니다. `fetch`의 Promise는 HTTP 404/500 응답만으로 항상 reject되지 않는다.

### 3. 새로운 상황에 적용

`fetch("/api/users/99")` 응답의 `response.ok`를 검사하지 않고 `response.json()`만 호출할 때 생길 문제를 설명한다.

### 4. 면접 질문과 답변 기준

**질문:** AJAX · Fetch · REST · JSON의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** AJAX는 비동기 통신 방식이고 REST는 자원을 다루는 설계 원칙이며 JSON은 메시지 데이터 형식입니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. AJAX·JSON·REST

**정의** — AJAX는 페이지 전체를 새로 불러오지 않고 비동기 HTTP 요청으로 데이터를 교환하는 웹 개발 기법이고 JSON은 데이터 직렬화 형식이다. REST는 자원과 표현, 일관된 인터페이스 등의 제약을 갖는 아키텍처 스타일이다.

**목적과 책임** — 프론트와 서버가 명확한 API 계약으로 통신하게 한다.

**구별해야 할 개념 / 흔한 오해** — AJAX ≠ JSON ≠ REST. REST API는 반드시 JSON만 쓰는 것도 아니다.

**실행·처리 흐름** — fetch 요청 → HTTP 라우팅 → JSON 직렬화 → 응답 → DOM 갱신

**직접 구현·검증** — GET /tasks, POST /tasks 요청·응답 계약 설계. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — AJAX와 REST API, JSON의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

AJAX는 페이지 전체 이동 없이 비동기 HTTP 요청으로 화면 일부를 갱신하는 기법이다. JSON은 데이터 교환 형식이며 REST는 자원·표현·통일 인터페이스 등 아키텍처 제약을 다룬다. JSON을 사용한다고 자동으로 REST API가 되는 것은 아니다.

### 직접 읽고 구현하는 독립 예제

```js
const res=await fetch("/api/tasks");
if(!res.ok) throw new Error(`HTTP ${res.status}`);
const tasks=await res.json();
console.log(tasks.length);
```


### 실행 검증 체크포인트

Network 탭에서 요청과 JSON 응답을 확인한다.

### 스스로 작성할 변형 실습

자원 URI /tasks/1과 동사형 URI /getTask를 비교한다.

### 면접 답변의 기준 문장

> AJAX는 비동기 요청 기법, JSON은 표현 형식, REST는 아키텍처 스타일입니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“AJAX는 페이지 전체를 새로 불러오지 않고 비동기 HTTP 요청으로 데이터를 교환하는 웹 개발 기법이고 JSON은 데이터 직렬화 형식이다. REST는 자원과 표현, 일관된 인터페이스 등의 제약을 갖는 아키텍처 스타일이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“AJAX ≠ JSON ≠ REST. REST API는 반드시 JSON만 쓰는 것도 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“AJAX와 REST API, JSON의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. 상태 코드·오류 응답

**정의** — HTTP 상태 코드는 요청 처리 결과의 범주와 의미를 표현한다. 2xx 성공, 4xx 클라이언트 측 문제, 5xx 서버 측 문제를 나타낸다.

**목적과 책임** — 호출자가 재시도·수정·사용자 안내를 결정하게 돕는다.

**구별해야 할 개념 / 흔한 오해** — fetch는 404·500만으로 Promise를 reject하지 않는다. response.ok 확인이 필요하다.

**실행·처리 흐름** — 응답 수신 → 상태 검사 → 성공 데이터/오류 본문 처리

**직접 구현·검증** — 400·401·403·404·500 응답을 의도적으로 발생시키고 처리. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 401과 403, 400과 500의 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## REST CRUD 한 번에 보기: 201·200·404·204 (2026-11-23 보강, MockMvc로 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**REST 규칙 표**

| 동작 | 요청 | 성공 응답 | 없을 때 |
|---|---|---|---|
| 메모 작성 | `POST /api/notes` | 201 Created | - |
| 목록 조회 | `GET /api/notes` | 200 OK | - |
| 한 건 조회 | `GET /api/notes/{id}` | 200 OK | 404 Not Found |
| 수정 | `PUT /api/notes/{id}` | 200 OK | 404 Not Found |
| 삭제 | `DELETE /api/notes/{id}` | 204 No Content | 404 Not Found |

```java
// 실행 시 -parameters 옵션이 필요하다 (아래 "주의" 참고)
package com.example.notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class NoteApi {

    record Note(Long id, String title, String content) {}
    record AddNoteRequest(String title, String content) {}
    record UpdateNoteRequest(String title, String content) {}

    @RestController
    @RequestMapping("/api/notes")
    static class NoteController {
        private final Map<Long, Note> store = new ConcurrentHashMap<>();
        private final AtomicLong seq = new AtomicLong();

        @PostMapping
        ResponseEntity<Note> add(@RequestBody AddNoteRequest req) {
            Note a = new Note(seq.incrementAndGet(), req.title(), req.content());
            store.put(a.id(), a);
            return ResponseEntity.status(HttpStatus.CREATED).body(a);      // 201 Created
        }

        @GetMapping
        List<Note> findAll() {
            return new ArrayList<>(store.values());                       // 200 OK
        }

        @GetMapping("/{id}")
        ResponseEntity<Note> findOne(@PathVariable Long id) {
            Note a = store.get(id);
            return a == null ? ResponseEntity.notFound().build()           // 404 Not Found
                             : ResponseEntity.ok(a);                       // 200 OK
        }

        @PutMapping("/{id}")
        ResponseEntity<Note> update(@PathVariable Long id, @RequestBody UpdateNoteRequest req) {
            Note old = store.get(id);
            if (old == null) return ResponseEntity.notFound().build();
            Note updated = new Note(id, req.title(), req.content());
            store.put(id, updated);
            return ResponseEntity.ok(updated);                            // 200 OK
        }

        @DeleteMapping("/{id}")
        ResponseEntity<Void> delete(@PathVariable Long id) {
            if (store.remove(id) == null) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();                    // 204 No Content
        }
    }

    public static void main(String[] args) throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new NoteController()).build();

        MvcResult created = mvc.perform(post("/api/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"hello\",\"content\":\"first post\"}"))
                .andReturn();
        System.out.println("POST    -> " + created.getResponse().getStatus() + " " + created.getResponse().getContentAsString());
        System.out.println("GET     -> " + mvc.perform(get("/api/notes")).andReturn().getResponse().getStatus()
                + " " + mvc.perform(get("/api/notes")).andReturn().getResponse().getContentAsString());
        System.out.println("GET 1   -> " + mvc.perform(get("/api/notes/1")).andReturn().getResponse().getStatus());
        System.out.println("GET 999 -> " + mvc.perform(get("/api/notes/999")).andReturn().getResponse().getStatus());

        MvcResult updated = mvc.perform(put("/api/notes/1")
                        .contentType("application/json")
                        .content("{\"title\":\"hello v2\",\"content\":\"edited\"}"))
                .andReturn();
        System.out.println("PUT 1   -> " + updated.getResponse().getStatus() + " " + updated.getResponse().getContentAsString());
        System.out.println("DELETE 1   -> " + mvc.perform(delete("/api/notes/1")).andReturn().getResponse().getStatus());
        System.out.println("GET 1 after -> " + mvc.perform(get("/api/notes/1")).andReturn().getResponse().getStatus());
    }
}
```

필요한 jar: `spring-webmvc`, `spring-test`, `tomcat-embed-core`(서블릿 API), `jackson-databind` 등. 이 PC에서는 Jackson 3(`tools.jackson`)을 함께 썼다.

**실행 결과 (확인함)**

```
POST    -> 201 {"id":1,"title":"hello","content":"first post"}
GET     -> 200 [{"id":1,"title":"hello","content":"first post"}]
GET 1   -> 200
GET 999 -> 404
PUT 1   -> 200 {"id":1,"title":"hello v2","content":"edited"}
DELETE 1   -> 204
GET 1 after -> 404
```

**주의: `-parameters` 옵션**
처음 실행했을 때 다음 오류가 났다.

```
IllegalArgumentException: Name for argument of type [java.lang.Long] not specified, and parameter name information not available via reflection.
Ensure that the compiler uses the '-parameters' flag.
```

`@PathVariable Long id`처럼 이름을 생략하면 Spring이 메서드 매개변수 이름을 읽는데, 자바 컴파일러는 기본으로 이름을 남기지 않는다. `javac -parameters`로 컴파일하거나 `@PathVariable("id")`처럼 이름을 적으면 된다. Spring Boot의 Gradle·Maven 플러그인은 이 옵션을 기본으로 켜 둔다. 직접 `javac`로 컴파일할 때만 이 오류가 난다.

**읽는 법**
- 상태 코드는 결과를 알리는 약속이다. 새 자원은 201, 없는 자원은 404, 본문 없이 끝나는 삭제는 204다.
- 원 자료의 예제 앱은 `JpaRepository`를 쓰므로 `findById`가 없으면 `Optional.empty()`가 돌아온다. 그 뒤 `orElse(404)`로 바꾸는 방식이 같은 원리다(25장).
- 수정(PUT)은 자료에서 트랜잭션과 변경 감지로 처리한다([25-1 엔티티 상태 전이와 flush](25-1_JPA_기초와_Repository.md)). 조회 후 필드만 바꾸면 UPDATE가 자동으로 나간다.

**면접 기준**: "REST는 URL로 자원을 가리키고 HTTP 메서드로 동작을 나타냅니다. 생성은 201, 없는 자원은 404, 삭제는 204로 응답하고, 이 계약을 MockMvc 테스트로 확인합니다."

## 요청 값과 응답 본문: @RequestParam, ResponseEntity, HttpMessageConverter (2026-11-24 보강, MockMvc로 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

```java
@RestController
public static class QuizController {
    @GetMapping("/quiz")
    public ResponseEntity<?> quiz(@RequestParam int code) {   // ?code=1 의 값을 int로 꺼낸다
        switch (code) {
            case 1:
                return ResponseEntity.status(HttpStatus.CREATED).body("created by code 1");
            case 2:
                return ResponseEntity.ok(Map.of("code", 2, "message", "ok"));
            default:
                return ResponseEntity.badRequest().body("unknown code: " + code);
        }
    }
}
```

**실행 결과 (확인함)**

```
code=1 -> 201 type=text/plain;charset=ISO-8859-1 body=created by code 1
code=2 -> 200 type=application/json body={"message":"ok","code":2}
code=9 -> 400 type=text/plain;charset=ISO-8859-1 body=unknown code: 9
```

**읽는 법**
- `@RequestParam`은 주소의 `?code=1` 같은 **쿼리 값**을 메서드 인자로 가져온다. 값이 없거나 `int`로 바꿀 수 없으면 400 응답이 난다.
- `ResponseEntity`는 **상태 코드, 헤더, 본문**을 한꺼번에 정한다. `201`, `200`, `400`을 코드에서 고른다.
- **HttpMessageConverter**가 본문을 바꾼다. 본문이 **글자(String)**면 `text/plain`으로, **객체**(`Map`, DTO)면 Jackson이 JSON으로 바꿔 `application/json`으로 보낸다. 같은 컨트롤러라도 반환 타입에 따라 응답 형식이 달라진다.

**주의**
- 쿼리 값은 항상 글자로 들어온다. 숫자로 쓰려면 `int`로 선언해서 Spring이 바꾸게 하거나 직접 변환한다.
- 본문을 글자로 돌려주면서 JSON처럼 보이게 쓰면(`"{...}"`) 클라이언트가 `Content-Type`을 보고 잘못 읽을 수 있다. 객체를 돌려주고 JSON 변환을 맡긴다.

**면접 기준**: "`@RequestParam`으로 쿼리 값을 받고, `ResponseEntity`로 상태 코드와 본문을 정합니다. 본문이 객체면 HttpMessageConverter(Jackson)가 JSON으로 바꿔 보냅니다."

## 브라우저에서 JSON 주고받기와 Bearer 토큰 보내기 (2026-11-24 보강, Node로 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**JSON 변환 두 가지**
- `JSON.stringify(객체)`: 객체를 JSON **문자열**로 바꾼다. 서버로 보낼 때 쓴다.
- `JSON.parse(문자열)`: JSON 문자열을 객체로 바꾼다. 서버 응답을 읽을 때 쓴다.

```javascript
const member = { name: '김', age: 30, tags: ['a', 'b'] };
const text = JSON.stringify(member);
// '{"name":"김","age":30,"tags":["a","b"]}'
JSON.parse(text);   // 원래 객체와 같은 값
```

**Bearer 토큰을 붙여 POST하기 (확인함)**

```javascript
async function post(token, obj) {
  const res = await fetch(base + '/api/members', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
    body: JSON.stringify(obj),     // 객체는 문자열로 바꿔서 보낸다
  });
  const data = await res.json();   // 응답 본문을 객체로 바꾼다
  return { status: res.status, data };
}
```

확인 결과 (Node에서 같은 구조의 서버를 띄움):
```
with token: { status: 201, data: { id: 42, name: '김', age: 30, tags: [ 'a', 'b' ] } }
wrong token: { status: 401, data: { error: 'unauthorized' } }
parse error: SyntaxError
```

**읽는 법**
- `Content-Type: application/json`은 "본문이 JSON이다"라는 표시다. 서버는 이 값을 보고 본문을 객체로 읽는다.
- `Authorization: Bearer <토큰>`은 토큰을 증명하는 자리다. 토큰이 틀리면 서버는 401을 돌려준다(32장의 재발급 흐름이 이 응답을 받는다).
- 응답이 JSON이 아니면(로그인 페이지 HTML 등) `res.json()`이 `SyntaxError`로 실패한다. 14장 해독표의 `Unexpected token '<'`가 이 경우다.

**토큰을 어디에 두나**
- `localStorage`에 두면 JavaScript로 쉽게 읽고 쓸 수 있다. 대신 XSS 공격으로 스크립트가 실행되면 토큰도 빼앗길 수 있다.
- `HttpOnly` 쿠키에 두면 JavaScript가 읽지 못해 XSS에 강하다. 대신 같은 사이트 요청에 쿠키가 자동으로 붙도록 설정(`credentials`)을 맞춰야 한다. 32장의 리프레시 토큰은 이 방식이다.

**면접 기준**: "브라우저에서 객체는 `JSON.stringify`로 보내고 응답은 `JSON.parse`(또는 `res.json()`)로 읽습니다. 인증 토큰은 `Authorization: Bearer`로 보내고, 저장은 XSS 위험을 고려해 `HttpOnly` 쿠키를 우선 검토합니다."

## Java 객체의 getter가 JSON 필드 이름을 정한다 (9_SpringBoot_Basic 02 기준, Jackson으로 확인함)

> 추가 근거: 9_Spring_boot_Basic `02 Spring MVC 기초`의 `Product` DTO와 `@RestController` 예를 읽고 보강했다. 아래는 **Jackson 3.1.5(`tools.jackson.databind.ObjectMapper`)로 직접 실행한 결과**다(2026-11-24). 스프링 서버를 띄운 실행(`/first/test3` 브라우저 확인)은 **미실행**이다.

```java
public class Product {
    String name;          // 필드
    int unitCost;
    public String getName() { return name; }        // JSON 이름: name
    public void setName(String name) { this.name = name; }
    public int getUnitCost() { return unitCost; }   // JSON 이름: unitCost
    public void setUnitCost(int unitCost) { this.unitCost = unitCost; }
}
```

| 대상 | 결과 (확인함) |
|---|---|
| `new Product("맥주", 2000)` | `{"name":"맥주","unitCost":2000}` |

- JSON 속성 이름은 **필드 이름이 아니라 `get…` 메소드 이름**에서 온다(`getUnitCost` → `unitCost`). 강의도 같은 설명이다.
- `@RestController`의 액션 메소드가 객체나 배열을 반환하면 이 변환이 자동으로 일어난다. 강의의 `test2`는 문자열 배열, `test4`는 객체 배열을 반환한다.
- 이 규칙 때문에 JSON에 넣지 말아야 할 값이 있으면 getter를 만들지 않거나 `@JsonIgnore`를 쓴다(순환 참조 절 참고).

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

HTTP 상태 코드는 응답 결과의 범주를 전달한다. 4xx는 요청 측 문제, 5xx는 서버 측 실패를 나타내지만 구체적 코드는 의미에 맞게 선택해야 한다. 401은 인증 필요·실패, 403은 권한 부족을 뜻한다. 오류 응답 본문은 일관된 계약을 유지하고 내부 스택 트레이스를 노출하지 않는다.

### 직접 읽고 구현하는 독립 예제

```json
{"code":"VALIDATION_ERROR","message":"제목을 입력하세요","fieldErrors":[{"field":"title","reason":"required"}]}
```


### 실행 검증 체크포인트

400, 401, 403, 404, 409, 500 시나리오를 표로 만들고 응답을 설계한다.

### 스스로 작성할 변형 실습

중복 가입 요청에 409를 사용할지 400을 사용할지 계약 관점에서 논의한다.

### 면접 답변의 기준 문장

> 상태 코드는 요청 처리 결과의 표준 의미를 전달하며 오류 본문은 세부 원인을 구조화합니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: 빈 JSON과 HTTP 상태 코드

`[]`는 JSON의 빈 배열이고 `null`은 JSON의 null 리터럴이며 `{}`는 빈 객체다. 세 가지는 구분된다. API 응답이 `200 []`인지 `404 {"code":"NOT_FOUND"}`인지는 서버가 정의한 **API 계약**이다. DB의 행 수와 1:1로 자동 결정되지 않는다.

```javascript
async function loadUsers() {
  const res = await fetch('/api/users');
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  const users = await res.json();
  if (!Array.isArray(users)) throw new Error('예상한 배열이 아님');
  return users; // []도 정상 결과
}
```

**실습:** 개발자 도구에서 status, headers, response body를 따로 확인한다. `204 No Content`에 무조건 `response.json()`을 호출하면 실패할 수 있다. CORS는 브라우저의 교차 출처 요청에 대한 정책이지 인증·인가를 대신하지 않는다.


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. AJAX / Fetch / JSON

**흔한 오해:** “AJAX는 JSON 포맷 이름이다”

**정확한 구분:** AJAX는 페이지 전체 새로고침 없이 서버와 비동기 통신하는 방식, fetch는 브라우저 API, JSON은 데이터 표현 형식이다. 서로 다른 층위다.

**코드·명령 해석:**

```text
const response = await fetch("/api/users");
if (!response.ok) throw new Error(String(response.status));
const users = await response.json();
```

**확인 방법:** fetch는 HTTP 404 자체만으로 Promise를 거부하지 않으므로 response.ok 확인이 필요하다.

**직접 구현:** 204 No Content 응답에 무조건 response.json()을 호출하면 어떻게 되는지 확인하라.

**면접 점검:** “AJAX / Fetch / JSON”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. REST / HTTP / JSON

**흔한 오해:** “JSON을 반환하면 자동으로 REST다”

**정확한 구분:** REST는 자원·표현·상태 없는 상호작용 등 제약을 가진 아키텍처 스타일이다. HTTP와 JSON은 구현에 자주 쓰이지만 JSON 자체가 REST를 보장하지 않는다.

**코드·명령 해석:**

```text
GET /users/3
POST /users
PATCH /users/3
DELETE /users/3
```

**확인 방법:** 동작 이름을 URL에 넣는 방식과 자원 중심 URL을 비교한다.

**직접 구현:** GET 요청으로 데이터를 삭제하면 어떤 의미론적 문제가 있는지 설명하라.

**면접 점검:** “REST / HTTP / JSON”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. CORS / 인증 / 네트워크 오류

**흔한 오해:** “CORS는 서버 간 요청을 차단한다”

**정확한 구분:** CORS는 브라우저가 다른 출처의 응답 접근을 제한하는 보안 메커니즘이다. 서버 간 HTTP 요청이나 인증/인가 정책과는 다른 문제다.

**코드·명령 해석:**

```text
Access-Control-Allow-Origin: https://client.example
```

**확인 방법:** 브라우저와 curl에서 같은 API 호출의 차이를 확인한다.

**직접 구현:** 쿠키를 동반하는 교차 출처 요청에 필요한 설정을 조사하라.

**면접 점검:** “CORS / 인증 / 네트워크 오류”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“HTTP 상태 코드는 요청 처리 결과의 범주와 의미를 표현한다. 2xx 성공, 4xx 클라이언트 측 문제, 5xx 서버 측 문제를 나타낸다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“fetch는 404·500만으로 Promise를 reject하지 않는다. response.ok 확인이 필요하다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“401과 403, 400과 500의 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.


## 4. JSON 직렬화 (Jackson)

- **직렬화:** 자바 객체를 JSON 문자열로 바꾸는 것. **역직렬화:** JSON 문자열을 자바 객체로 바꾸는 것.
- **Jackson:** Spring Boot에 기본으로 들어 있는 JSON 라이브러리다. `@RestController`가 객체를 반환하면 Jackson이 JSON으로 바꿔 응답한다.
- **직접 해 보기 (순수 Jackson):**

```java
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(new User(1, "Kim")); // {"id":1,"name":"Kim"}
User back = mapper.readValue(json, User.class);
```

- **실행 결과 (2026-11-23, Jackson 2.17.2):** 직렬화 결과 `{"id":1,"name":"Kim"}`, 역직렬화 후 원래 객체와 같음(true).
- **주의:** 응답 DTO에 비밀번호 같은 필드가 있으면 그대로 JSON으로 나간다. 응답 전용 DTO를 따로 만든다.
- **면접 기준:** "직렬화는 객체를 JSON으로 바꾸는 것이고, Spring에서는 Jackson이 @RestController의 반환값을 자동으로 바꿔 응답합니다."

### 4-1. Jackson 2와 3의 패키지 차이 (2026-11-23 보강, 확인함)

Jackson은 버전 3에서 패키지 이름이 바뀌었다. 옛 자료의 import 문과 새 자료의 import 문이 다른 이유다.

| 버전 | 패키지 | 확인 |
|---|---|---|
| Jackson 2 | `com.fasterxml.jackson.databind.ObjectMapper` | 위 4절에서 2.17.2로 확인함 |
| Jackson 3 | `tools.jackson.databind.ObjectMapper` | jackson-databind 3.1.5로 확인함 |

```java
import tools.jackson.databind.ObjectMapper;

public class Json3Demo {
    public record Member(long id, String name) {}

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Member(1L, "Kim"));
        System.out.println(json);                                   // {"id":1,"name":"Kim"}
        Member back = mapper.readValue(json, Member.class);
        System.out.println(back.equals(new Member(1L, "Kim")));    // true
    }
}
```

**실행 결과 (확인함)**: `{"id":1,"name":"Kim"}`, `true`. 필요한 jar는 `jackson-core`, `jackson-databind`, `jackson-annotations`다.

**실무 포인트**: 프로젝트가 어느 Jackson을 쓰는지는 Spring Boot 버전에 따라 다르다. 확인하려면 `mvn dependency:tree`나 `gradle dependencies`에서 `jackson`을 찾는다. 옛 자료를 따라 할 때 `com.fasterxml` import 오류가 나면 이 표를 먼저 본다.

## 5. OpenAPI 명세 (계약 먼저 쓰기)

- **정의:** REST API의 경로, 요청과 응답 모양, 상태 코드를 YAML이나 JSON 파일로 정리한 명세다. Swagger가 이 형식으로 문서를 만든다.
- **왜 먼저 쓰나:** 프론트와 백엔드가 같은 명세를 보고 동시에 일할 수 있다. 구현이 명세와 다르면 계약 테스트(37장)에서 잡는다.
- **작성 예 (일부):**

```yaml
paths:
  /api/posts/{id}:
    get:
      parameters:
        - name: id
          in: path
          required: true
          schema: { type: integer }
      responses:
        '200':
          description: 글 한 개
          content:
            application/json:
              schema:
                type: object
                properties:
                  id: { type: integer }
                  title: { type: string }
        '404':
          description: 글이 없음
```

- **확인 방법:** 명세에 적은 응답 필드 이름을 실제 응답과 비교한다. 다르면 명세와 구현 중 하나를 고친다.
- **면접 기준:** "API를 명세로 먼저 정하고, 구현이 명세를 지키는지 테스트로 확인합니다."
