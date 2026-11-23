# Servlet·JSP

> 중요도: **A** · 이 장의 목표: 정의를 정확히 말하고, 역할·경계·흐름을 설명한 뒤 작은 예제를 직접 구현한다.

**기존 통합 원고의 참고 위치:** `07_Servlet;08_JSP`의 `01_정독용_교재.md` (압축파일 내부 기준). 이 문서는 원본을 복사하지 않고 핵심을 새로 선별·재서술했다.


## 학습 전 필수 관문 — Servlet · JSP · forward · redirect

> **학습 순서:** 아래 정의를 읽고 → 굵은 용어를 자기 말로 풀고 → 틀린 문장을 반박하고 → 예제를 설명한 다음 → 답안을 가리고 면접처럼 말한다. 비유만 이해한 상태에서는 다음으로 넘어가지 않는다.

### 1. 정확한 정의

Servlet은 HTTP 요청을 처리하는 Java 서버 컴포넌트이고 JSP는 서버에서 응답 콘텐츠를 만드는 뷰 기술이다. **forward**는 서버 내부 전달, **redirect**는 클라이언트에 새 요청을 지시하는 응답이다.

### 2. 용어를 구분해야 하는 이유

forward는 브라우저가 새 URL로 재요청하는 동작이 아니다. redirect는 원래 요청의 request 속성을 자동으로 이어받지 않는다.

### 3. 새로운 상황에 적용

POST 저장 후 redirect를 하면 브라우저가 새 GET을 수행할 수 있다. forward와 비교해 주소창·요청 횟수·request 속성의 차이를 설명한다.

### 4. 면접 질문과 답변 기준

**질문:** Servlet · JSP · forward · redirect의 차이를 정의와 동작을 포함해 설명해 보세요.

**답변 기준:** forward는 서버 내부에서 같은 요청을 다른 자원에 전달하고, redirect는 클라이언트가 새로운 요청을 하도록 응답하는 방식입니다.

### 5. 정답을 가리고 설명하기

1. 위 정의에서 핵심 명사 세 개를 골라 각각 뜻을 설명한다.
2. 위의 **틀린 주장**이 왜 틀렸는지 반례를 든다.
3. 위 상황의 **실행 결과·처리 주체·변경되는 값**을 분리해 설명한다.
4. 답변 기준을 보지 않고 **정의 1문장 → 차이 1문장 → 예시 1문장**으로 말한다.
5. 다음 날 같은 질문에 답하지 못하면 예제가 아니라 **정의**부터 다시 확인한다.

---

## 1. Servlet·컨테이너·생명주기

**정의** — Servlet은 서버에서 HTTP 등 요청을 처리하는 Java 컴포넌트이고 서블릿 컨테이너는 생명주기·요청 라우팅·스레드 등 실행 환경을 관리한다.

**목적과 책임** — Servlet은 요청 처리 로직, Tomcat 같은 컨테이너는 인스턴스와 호출을 관리한다.

**구별해야 할 개념 / 흔한 오해** — Servlet은 요청마다 반드시 새로 생성되지 않는다. 공유 필드에 요청별 상태를 저장하면 경쟁 문제가 생긴다.

**실행·처리 흐름** — 요청 → 필터 → 컨테이너 매핑 → service/doGet/doPost → 응답

**직접 구현·검증** — 동시 요청 카운터를 인스턴스 필드에 두었을 때 위험 설명. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Servlet과 Servlet 컨테이너는 각각 무엇인가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

Servlet은 Java 웹 요청을 처리하는 서버 측 컴포넌트 규약이다. Servlet 컨테이너는 인스턴스 생성·초기화·요청 디스패치·종료를 관리한다. 하나의 서블릿 인스턴스가 여러 요청 스레드에서 사용될 수 있어 요청별 상태를 인스턴스 필드에 두면 위험하다.

### 직접 읽고 구현하는 독립 예제

```java
@WebServlet("/ping")
public class PingServlet extends HttpServlet {
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
  resp.setContentType("text/plain;charset=UTF-8"); resp.getWriter().write("pong");
 }
}
```


### 실행 검증 체크포인트

컨테이너에 배포한 뒤 GET /ping의 응답을 확인한다. 위 예제는 Servlet API 버전에 맞는 import가 필요하다.

### 스스로 작성할 변형 실습

인스턴스 필드에 request별 값을 보관하면 어떤 동시성 문제가 생기는지 설명한다.

### 면접 답변의 기준 문장

> 서블릿은 요청 처리 컴포넌트이고 컨테이너는 생명주기와 요청 전달을 관리합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“Servlet은 서버에서 HTTP 등 요청을 처리하는 Java 컴포넌트이고 서블릿 컨테이너는 생명주기·요청 라우팅·스레드 등 실행 환경을 관리한다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“Servlet은 요청마다 반드시 새로 생성되지 않는다. 공유 필드에 요청별 상태를 저장하면 경쟁 문제가 생긴다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“Servlet과 Servlet 컨테이너는 각각 무엇인가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 2. request·response·session

**정의** — request는 요청 데이터 접근 객체, response는 응답 작성 객체, session은 여러 요청에 걸쳐 서버 측 사용자별 상태를 연관짓는 메커니즘이다.

**목적과 책임** — 요청 범위와 세션 범위의 데이터를 구분한다.

**구별해야 할 개념 / 흔한 오해** — 쿠키는 브라우저에 저장·전송되는 데이터이고 세션은 서버 측 상태 관리 개념이다.

**실행·처리 흐름** — 브라우저 쿠키 전송 → 서버 세션 식별 → 상태 조회 → 응답

**직접 구현·검증** — 로그인 상태를 세션에 저장하고 로그아웃 시 무효화. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — 쿠키와 세션, request 속성의 생명주기를 비교하라.

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

request는 현재 HTTP 요청의 데이터와 속성, response는 HTTP 응답 작성 수단이다. session은 여러 요청에 걸친 사용자별 서버 측 상태를 관리하는 추상화다. 쿠키는 클라이언트가 보관·전송하는 작은 데이터로 세션 식별자 전달에 자주 사용된다.

### 직접 읽고 구현하는 독립 예제

```java
String name=req.getParameter("name");
req.setAttribute("message",name);
HttpSession session=req.getSession();
session.setAttribute("userId",123L);
```


### 실행 검증 체크포인트

다음 요청에서 request attribute는 사라지고 session attribute는 유지될 수 있음을 확인한다.

### 스스로 작성할 변형 실습

세션 만료, 로그아웃 시 invalidate, 쿠키 HttpOnly/Secure 설정을 조사한다.

### 면접 답변의 기준 문장

> request는 단일 요청, session은 여러 요청에 걸친 사용자 상태를 다룹니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“request는 요청 데이터 접근 객체, response는 응답 작성 객체, session은 여러 요청에 걸쳐 서버 측 사용자별 상태를 연관짓는 메커니즘이다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“쿠키는 브라우저에 저장·전송되는 데이터이고 세션은 서버 측 상태 관리 개념이다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“쿠키와 세션, request 속성의 생명주기를 비교하라.”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 3. forward·redirect·JSP

**정의** — forward는 서버 내부에서 같은 요청을 다른 리소스로 넘기는 동작이고 redirect는 클라이언트에 새 URL 요청을 유도하는 응답이다. JSP는 주로 서버 측 View 생성에 사용된다.

**목적과 책임** — Controller가 모델을 준비하고 JSP가 출력하도록 역할을 나눈다.

**구별해야 할 개념 / 흔한 오해** — forward는 브라우저 주소가 바뀌지 않고 request 속성을 전달할 수 있다. redirect는 새로운 요청이다.

**실행·처리 흐름** — Controller → request.setAttribute → forward → JSP 렌더 / redirect → 브라우저 새 GET

**직접 구현·검증** — POST 처리 후 redirect하는 PRG 패턴을 구현. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — forward와 redirect의 요청 횟수·주소·속성 차이는?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

forward는 서버 내부에서 같은 요청을 다른 자원으로 넘기며 브라우저 주소가 바뀌지 않는다. redirect는 3xx 응답으로 브라우저가 새 요청을 보내도록 한다. JSP는 서버에서 HTML 등을 생성하는 뷰 기술이며 비즈니스 로직을 집중시키는 계층이 아니다.

### 직접 읽고 구현하는 독립 예제

```java
req.setAttribute("items",items);
req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req,resp);
// 또는 PRG 패턴
resp.sendRedirect(req.getContextPath()+"/items");
```


### 실행 검증 체크포인트

브라우저 Network에서 redirect가 추가 요청을 발생시키는지 확인한다.

### 스스로 작성할 변형 실습

POST 후 새로고침 중복 제출을 줄이는 PRG 패턴을 설명한다.

### 면접 답변의 기준 문장

> forward는 서버 내부 전달이고 redirect는 클라이언트에게 새 요청을 지시합니다.


### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“forward는 서버 내부에서 같은 요청을 다른 리소스로 넘기는 동작이고 redirect는 클라이언트에 새 URL 요청을 유도하는 응답이다. JSP는 주로 서버 측 View 생성에 사용된다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“forward는 브라우저 주소가 바뀌지 않고 request 속성을 전달할 수 있다. redirect는 새로운 요청이다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“forward와 redirect의 요청 횟수·주소·속성 차이는?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.

## 4. Filter·Listener

**정의** — Filter는 서블릿 요청·응답 처리 전후의 공통 관심사를 다루는 체인 요소이며 Listener는 컨테이너 이벤트를 통지받는다.

**목적과 책임** — 인증 검사·인코딩·로깅 같은 횡단 관심사를 분리한다.

**구별해야 할 개념 / 흔한 오해** — Filter는 Controller와 동일 계층이 아니며 모든 예외를 처리하는 전역 장치도 아니다.

**실행·처리 흐름** — 요청 → Filter A → Filter B → Servlet → 응답 역순

**직접 구현·검증** — 5XX 응답을 기록하는 필터를 설계하고 예외 처리 경계 점검. 실행 결과를 캡처하거나 테스트 로그로 남기고, 예상과 실제 결과가 다르면 이유를 기록한다.

**면접 질문** — Filter와 Servlet, Spring Interceptor는 어떻게 다른가?

**30초 답변 연습** — 위 정의를 첫 문장으로 말하고, 목적·구별점·실제 예시 순서로 3~4문장으로 설명한다.

## 게시판의 답글 구조: pos와 depth (2026-11-23 보강, MySQL 8.0 로컬에서 확인함)

**출처**: 선생님 자료 `9_JSP_Board/tblboard게시판.txt`(v0~v3 단계별 실습)와 `Practice_project☆/MvcBoardTutorial.html`(답변 기능). 두 자료 모두 답글을 같은 방식으로 저장한다.

**정의**: 답글형 게시판은 글을 **시간순이 아니라 화면에 보일 순서**로 정렬해야 한다. 그래서 두 열을 둔다.
- `pos`: 정렬 순서. 작을수록 위에 보인다. 답글은 부모 바로 아래에 끼어든다.
- `depth`: 들여쓰기 깊이. 원글은 0, 답글은 1, 답글의 답글은 2.

**규칙**
1. **새 글**: 기존 글의 `pos`를 전부 +1 하고, 새 글을 `pos=0, depth=0`으로 넣는다. 새 글이 맨 위에 온다.
2. **답글**(부모 글 `P`): `pos`가 `P.pos`보다 큰 글을 전부 +1 해서 자리를 만든다. 답글은 `pos = P.pos + 1`, `depth = P.depth + 1`로 넣는다.
3. **목록**: `ORDER BY pos ASC`로 읽으면 답글이 부모 아래에 붙는다.

```sql
CREATE TABLE tblBoard (
  num INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(20), subject VARCHAR(50), content VARCHAR(4000),
  regdate DATETIME, count INT DEFAULT 0,
  pos INT DEFAULT 0, depth INT DEFAULT 0
);

-- 새 글
UPDATE tblBoard SET pos = pos + 1;
INSERT INTO tblBoard (name, subject, content, regdate, count, pos, depth) VALUES ('kim', 'A글', 'a', NOW(), 0, 0, 0);
UPDATE tblBoard SET pos = pos + 1;
INSERT INTO tblBoard (name, subject, content, regdate, count, pos, depth) VALUES ('lee', 'B글', 'b', NOW(), 0, 0, 0);

-- A글(num=1)에 답글
SET @ppos = (SELECT pos FROM tblBoard WHERE num = 1), @pdep = (SELECT depth FROM tblBoard WHERE num = 1);
UPDATE tblBoard SET pos = pos + 1 WHERE pos > @ppos;
INSERT INTO tblBoard (name, subject, content, regdate, count, pos, depth) VALUES ('park', 'A의 답글', 'r1', NOW(), 0, @ppos + 1, @pdep + 1);

-- B글(num=2)에 답글
SET @ppos = (SELECT pos FROM tblBoard WHERE num = 2), @pdep = (SELECT depth FROM tblBoard WHERE num = 2);
UPDATE tblBoard SET pos = pos + 1 WHERE pos > @ppos;
INSERT INTO tblBoard (name, subject, content, regdate, count, pos, depth) VALUES ('choi', 'B의 답글', 'r2', NOW(), 0, @ppos + 1, @pdep + 1);

SELECT CONCAT(REPEAT('  ', depth), subject) AS 목록, pos, depth FROM tblBoard ORDER BY pos ASC;
```

**실행 결과 (확인함)**

```
목록        pos   depth
B글          0      0
  B의 답글   1      1
A글          2      0
  A의 답글   3      1
```

**읽는 법**: 답글이 부모 바로 아래에 붙고, 들여쓰기 깊이로 계층을 보여 준다. 새 글은 맨 위에 온다. 실행 결과는 최신 글(B글)이 위에 있는 것을 보여 준다.

**주의**
- 답글을 달 때 `pos`를 올리는 두 문장은 **한 트랜잭션**에 묶는다. 중간에 다른 요청이 끼어들면 순서가 깨진다(33장의 갱신 유실과 같은 원리).
- 이 설계는 글 수가 많아지면 `pos` 갱신이 많아진다. 대안으로 부모의 경로 문자열(`1/3/7`)이나 `parent_id`를 쓰는 방법이 있다.
- 자료의 `tblBoard` 원본에는 `content VARCHAR(100)`도 있었지만, 긴 본문을 위해 `VARCHAR(4000)`으로 적었다.

**면접 기준**: "답글형 게시판은 `pos`로 화면 순서를, `depth`로 들여쓰기를 저장합니다. 답글을 넣을 때는 부모보다 뒤의 글들을 밀어내고, 두 단계를 한 트랜잭션으로 묶습니다."

## JNDI 커넥션 풀: Tomcat의 context.xml에 DB 연결 맡기기 (2026-11-24 보강, 미실행)

**출처**: 선생님 자료 `Practice_project☆/CarProjectEdu-실습워크북.html`(CarProject 실습, `context.xml` 설정과 `lookup` 코드). 이 PC에서 Tomcat을 띄우지 않아 **미실행**이다. 설정과 흐름은 자료의 내용을 정리했다.

**무엇인가**: JNDI(Java Naming and Directory Interface)는 이름으로 객체를 찾는 기능이다. Tomcat은 `context.xml`에 DB 연결 풀(`DataSource`)을 이름으로 등록하고, 웹 앱은 그 이름으로 가져다 쓴다. 연결을 코드에 직접 만들지 않으므로 DB 주소와 비밀번호를 코드 밖에 둘 수 있다(23장).

**설정 (자료 기준, `META-INF/context.xml`)**

```xml
<Context>
  <Resource
    name="jdbc/jspbeginner"
    auth="Container"
    type="javax.sql.DataSource"
    driverClassName="com.mysql.cj.jdbc.Driver"
    username="root"
    password="(본인 DB 비밀번호)"
    maxTotal="50"
    maxIdle="10"
    maxWaitMillis="-1"
    url="jdbc:mysql://localhost:3306/rentcar_edu?serverTimezone=Asia/Seoul&amp;characterEncoding=UTF-8&amp;allowPublicKeyRetrieval=true&amp;useSSL=false"/>
</Context>
```

**JSP나 서블릿에서 가져오기 (자료 흐름)**

```java
InitialContext ic = new InitialContext();
DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/jspbeginner");
try (Connection con = ds.getConnection();
     PreparedStatement ps = con.prepareStatement("select * from car");
     ResultSet rs = ps.executeQuery()) {
    // 결과 처리
}   // try가 끝나면 연결이 풀로 반납된다
```

**주의: 비밀번호를 저장소에 올리지 않는다**
- 자료는 `context.xml.sample`을 저장소에 두고, 각자 `context.xml`을 만들어 자기 비밀번호를 적는다. 실제 `context.xml`은 `.gitignore`로 제외한다.
- 자료의 점검표도 "GitHub 창고에 `context.xml`이 없는가"를 확인하게 한다. 이 원칙은 23장의 비밀 관리와 같다.
- `lookup`의 이름(`java:comp/env/jdbc/jspbeginner`)이 `name` 값과 한 글자라도 다르면 `NamingException`이 난다.

**면접 기준**: "Tomcat JNDI는 커넥션 풀을 서버 설정에 두고 이름으로 가져옵니다. 비밀번호는 `context.xml`에 두되 저장소에는 샘플 파일만 올리고, 실제 파일은 제외합니다."

## 단원 통과 기준

- [ ] 각 용어를 자료 없이 30초 안에 정의한다.
- [ ] 유사 개념을 1개 이상 비교한다.
- [ ] 처리 흐름을 화살표로 그릴 수 있다.
- [ ] 실습을 직접 실행하고 결과를 기록했다.
- [ ] 예상 밖 결과 또는 예외 상황을 하나 이상 설명한다.

### 깊이 이해하기 — 개념의 경계와 설계 판단

Filter는 Servlet 요청·응답 처리 전후에 개입하는 체인 컴포넌트다. Listener는 컨테이너 이벤트를 관찰·처리한다. Spring MVC HandlerInterceptor는 DispatcherServlet 내부의 핸들러 처리 과정에 적용되므로 Filter와 적용 범위가 다르다.

### 직접 읽고 구현하는 독립 예제

```java
@WebFilter("/*")
public class TraceFilter implements Filter {
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain) throws IOException,ServletException {
  long start=System.nanoTime();
  try {chain.doFilter(req,res);} finally {System.out.println(System.nanoTime()-start);}
 }
}
```


### 실행 검증 체크포인트

성공·실패 요청에서 finally 로그가 남는지 확인한다.

### 스스로 작성할 변형 실습

Filter와 Spring Interceptor 중 인증·로깅 요구사항에 적합한 위치를 비교한다.

### 면접 답변의 기준 문장

> Filter는 서블릿 컨테이너 체인에서 요청·응답을 가로채는 컴포넌트입니다.


---

# 핵심 개념을 오해 없이 이해하기 위한 기초 연결

## 필수 기초 연결: 요청 속성, 세션, DB 결과는 서로 다른 저장소다

`request.setAttribute("user", user)`는 **현재 요청 객체의 속성**을 설정한다. `session.setAttribute(...)`는 같은 세션에 걸친 상태를 보관한다. DB 테이블의 행과는 다른 저장 위치와 수명을 갖는다. `request.getAttribute("user") == null`이라고 해서 DB에 사용자가 없다고 단정할 수 없다. 속성을 아예 설정하지 않았을 수도 있기 때문이다.

```java
// HttpServlet의 doGet 내부라는 전제
String raw = request.getParameter("id");
if (raw == null || !raw.matches("\\d+")) {
    response.sendError(400, "id가 필요합니다");
    return;
}
long id = Long.parseLong(raw);
// 실제 조회는 Service/Repository에 위임한다.
```

**주의:** 정규식 검사와 별개로 `Long.parseLong`은 범위를 벗어나는 숫자 문자열에서 예외를 던질 수 있다. 실무에서는 그 경우도 400으로 변환해야 한다. `forward`는 서버 내부에서 같은 요청을 다른 자원에 전달하고, `redirect`는 클라이언트에 새 요청을 유도한다. 따라서 redirect 뒤에는 일반적으로 기존 request 속성이 유지되지 않는다.

**실습:** 요청 속성에 사용자 이름을 넣고 JSP로 forward한 경우와 redirect한 경우를 비교한다. 쿠키, 세션, request scope를 같은 저장소로 설명하지 않는다.


---

## 개념 경계 정밀 보강 — 실제 코드에서 헷갈리는 지점


### 경계 1. Servlet / Servlet Container / Tomcat

**흔한 오해:** “Servlet이 HTTP 포트를 직접 연다”

**정확한 구분:** Servlet은 요청을 처리하는 Java 컴포넌트이고 Servlet Container는 생명주기·매핑·요청/응답 객체 등을 관리한다. Tomcat은 Servlet Container 기능을 제공한다.

**코드·명령 해석:**

```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {resp.getWriter().write("hi");}
}
```

**확인 방법:** 컨테이너가 URL을 서블릿에 매핑하고 여러 요청이 같은 서블릿 인스턴스를 공유할 수 있음을 확인한다.

**직접 구현:** 서블릿 인스턴스 필드에 요청별 값을 저장하면 왜 위험한지 설명하라.

**면접 점검:** “Servlet / Servlet Container / Tomcat”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 2. forward / redirect / 요청 범위

**흔한 오해:** “forward는 브라우저에 새 URL을 요청시킨다”

**정확한 구분:** forward는 서버 내부에서 같은 요청/응답을 다음 자원으로 전달한다. redirect는 브라우저에 3xx 응답을 보내 새 요청을 유도한다. request attribute는 redirect 후 유지되지 않는다.

**코드·명령 해석:**

```java
req.setAttribute("user", user);
req.getRequestDispatcher("/WEB-INF/views/user.jsp").forward(req, resp);
// 또는 resp.sendRedirect(req.getContextPath()+"/users");
```

**확인 방법:** 주소창 변화, 요청 횟수, request attribute 유지를 비교한다.

**직접 구현:** POST 이후 새로고침 중복 제출을 피하는 PRG를 구현하라.

**면접 점검:** “forward / redirect / 요청 범위”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.


### 경계 3. 쿠키 / 세션 / HttpSession

**흔한 오해:** “세션 데이터 전체가 브라우저 쿠키에 저장된다”

**정확한 구분:** 일반적인 서버 세션 방식에서는 서버가 세션 상태를 저장하고 브라우저는 세션 식별자를 쿠키 등으로 전달한다. 쿠키는 클라이언트 저장/전송 수단이며 세션과 동일하지 않다.

**코드·명령 해석:**

```java
HttpSession session = req.getSession();
session.setAttribute("userId", 3L);
```

**확인 방법:** 개발자 도구 쿠키에 userId가 직접 들어가는지, 세션 ID만 들어가는지 확인한다.

**직접 구현:** 로그아웃에서 invalidate()를 사용하고 이전 세션 재사용을 확인하라.

**면접 점검:** “쿠키 / 세션 / HttpSession”의 각 용어를 각각 정의하고, 위 코드에서 어느 계층·시점·값에 해당하는지 60초 안에 설명해 보자.

### 정의를 읽고 넘어가지 않기 — 능동 확인

**1차: 정확한 문장 완성.** 다음 문장을 보지 않고 스스로 말한다. *“Filter는 서블릿 요청·응답 처리 전후의 공통 관심사를 다루는 체인 요소이며 Listener는 컨테이너 이벤트를 통지받는다.”* 문장 속 주요 용어를 각각 설명하지 못하면 정의를 외운 것이 아니라 읽은 것이다.

**2차: 경계 구분.** 다음 설명이 어떤 오해를 막는지 자기 말로 설명한다. *“Filter는 Controller와 동일 계층이 아니며 모든 예외를 처리하는 전역 장치도 아니다.”* 반례를 하나 만들어 본다.

**3차: 면접 재현.** *“Filter와 Servlet, Spring Interceptor는 어떻게 다른가?”*에 대해 **정의 → 왜 필요한가 → 무엇과 다른가 → 코드·상황 하나** 순서로 30~60초 말한다. 답을 읽지 않고 말한 뒤 원문과 대조한다.


## 5. EL과 JSTL (레거시 JSP 읽기용)

- **EL (Expression Language):** JSP 화면에서 `${user.name}`처럼 request, session의 속성을 꺼내 쓴다. 자바 코드(`<%= %>`)를 줄이기 위한 문법이다.
- **JSTL (JSP Standard Tag Library):** `<c:forEach>`, `<c:if>` 같은 태그로 반복과 조건을 화면에 쓴다.
- **선언 예:** `<%@ taglib prefix="c" uri="jakarta.tags.core" %>` (Jakarta EE 9 이후의 네임스페이스. 예전 `javax.servlet` 시절 URI와 다르다)
- **목표:** 새로 만들 때 쓰는 기술은 아니다. 과거 프로젝트나 레거시 코드의 JSP 화면을 읽고 수정할 수 있으면 충분하다.
- **면접 기준:** "EL은 속성 값을 화면에 꺼내는 표현식이고, JSTL은 반복과 조건을 태그로 쓰게 해 주는 라이브러리입니다."
- **검증 상태:** 아직 실행하지 않았다. 서블릿 컨테이너(Tomcat 등)가 있어야 확인할 수 있다.
