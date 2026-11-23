# 서버 사이드 템플릿: Thymeleaf와 Mustache 문법

> 중요도: **A** · 이 장의 목표: 서버가 데이터를 넣어 HTML을 만드는 원리를 이해하고, Thymeleaf와 Mustache의 기본 문법(값 출력, 이스케이프, 반복, 조건)을 직접 쓸 수 있다.

> 2026-11-23 추가 장. 학원 선생님이 Spring Boot 풀스택에서 꼭 알아야 한다고 하신 내용이다. 아래 문법 예제는 Thymeleaf 3.1.3과 JMustache 1.16으로 실행해서 5개 검증이 모두 통과했다. **Spring과 연결된 부분(컨트롤러, 폼 바인딩, `th:action`, `th:field`)은 Spring Boot 환경이 없어서 미검증이다.**

---

## 1. 서버 사이드 렌더링이란

**정의** — 서버가 데이터를 HTML 틀(템플릿)에 넣어서 완성된 HTML을 브라우저에 보내는 방식이다.

**흐름**
1. 브라우저가 `/posts`를 요청한다.
2. 컨트롤러가 DB에서 글 목록을 읽어 모델에 담는다.
3. 템플릿 엔진이 HTML 틀에 모델 값을 채운다.
4. 완성된 HTML을 브라우저에 보낸다.

**React와의 차이**
- 서버 렌더링: 처음부터 완성된 HTML을 받는다. 첫 화면이 빠르고, 검색 엔진이 내용을 읽기 쉽다.
- React: 빈 HTML을 받고, 브라우저가 API를 호출해서 화면을 그린다.

**면접 기준:** "서버 렌더링은 서버가 데이터를 HTML 틀에 채워서 보내는 방식입니다. React는 브라우저가 데이터를 받아 화면을 그립니다."

---

## 2. Thymeleaf

**정의** — HTML 속성으로 동작을 적는 Java 템플릿 엔진이다. `th:` 속성을 붙인 HTML 파일은 브라우저에서 직접 열어도 화면 모양이 보인다("자연 템플릿").

### 2-1. 값 출력: `th:text`와 `th:utext`

```java
Context ctx = new Context();
ctx.setVariable("title", "<b>안녕</b>");
engine.process("<p th:text=\"${title}\">x</p><p th:utext=\"${title}\">y</p>", ctx);
```

- `th:text`는 **이스케이프**한다. 결과는 `&lt;b&gt;안녕&lt;/b&gt;`이라서 태그가 글자로 보인다.
- `th:utext`는 이스케이프하지 않는다. 결과는 `<b>안녕</b>`이라서 태그가 해석된다.
- **검증 완료.** 사용자 입력은 항상 `th:text`로 출력한다. `th:utext`에 사용자 입력을 넣으면 XSS가 된다(20장).

### 2-2. 반복: `th:each`

```java
ctx.setVariable("items", List.of("사과", "배"));
engine.process("<ul><li th:each=\"item : ${items}\" th:text=\"${item}\">-</li></ul>", ctx);
```

- **검증 완료:** `li`가 2개 만들어지고 각각 사과, 배가 들어간다.
- 같은 요소를 반복하고, 그 요소 안에서 `item` 변수를 쓴다.

### 2-3. 조건: `th:if`와 `th:unless`

```java
ctx.setVariable("items", List.of());
engine.process("<p th:if=\"${items.isEmpty()}\">비어 있음</p><p th:unless=\"${items.isEmpty()}\">있음</p>", ctx);
```

- **검증 완료:** 목록이 비어 있으면 "비어 있음"만 나오고, "있음"은 나오지 않는다.

### 2-4. 기타 자주 쓰는 속성

| 속성 | 뜻 | 예 |
|---|---|---|
| `th:href` | 링크 주소 | `th:href="@{/posts/{id}(id=${post.id})}"` |
| `th:src` | 이미지 주소 | `th:src="@{/img/logo.png}"` |
| `th:class` | 클래스 지정 | `th:class="${active} ? 'on' : 'off'"` |
| `th:attr` | 임의의 속성 | `th:attr="data-id=${post.id}"` |
| `th:block` | 태그 없이 묶기 | 반복 안에서 여러 요소를 묶을 때 |

- **`@{...}`**: URL을 만드는 표현식. 컨텍스트 경로를 자동으로 붙인다.
- **`${...}`**: 변수 표현식. 모델에 담긴 값을 꺼낸다.

### 2-5. Spring과 함께 쓸 때 (미검증)

```java
@GetMapping("/posts")
String list(Model model) {
    model.addAttribute("posts", postService.findAll());
    return "posts/list";            // src/main/resources/templates/posts/list.html 을 찾는다
}
```

```html
<tr th:each="post : ${posts}">
  <td th:text="${post.title}">제목</td>
  <td><a th:href="@{/posts/{id}(id=${post.id})}">보기</a></td>
</tr>
```

- **컨트롤러는 뷰 이름만 반환한다.** `@RestController`는 객체를 JSON으로 보내고, `@Controller`는 뷰 이름으로 템플릿을 찾는다. 이 차이를 헷갈리면 안 된다(15장).
- **폼 바인딩 (`th:object`, `th:field`, `th:action`):** 폼 값을 객체에 자동으로 채우는 기능이다. Spring 환경에서 확인해야 한다(미검증).

**면접 기준:** "Thymeleaf는 HTML 속성으로 반복과 조건을 쓰고, `th:text`는 이스케이프해서 출력합니다. 사용자 입력은 `th:utext`로 출력하지 않습니다."

---

## 3. Mustache

**정의** — 로직이 거의 없는 간단한 템플릿 문법이다. 중괄호로 값을 넣는다. 언어마다 같은 문법을 구현한 엔진이 있어서 여러 환경에서 같은 템플릿을 쓸 수 있다.

### 3-1. 값 출력: `{{ }}`와 `{{{ }}}`

```java
Map<String, Object> data = new HashMap<>();
data.put("name", "<i>x</i>");
data.put("raw", "<i>y</i>");
Mustache.compiler().compile("{{name}} / {{{raw}}}").execute(data);
```

- `{{name}}`은 이스케이프한다. 결과는 `&lt;i&gt;x&lt;/i&gt;`다.
- `{{{raw}}}`는 이스케이프하지 않는다. 결과는 `<i>y</i>`다.
- **검증 완료.** 결과는 `&lt;i&gt;x&lt;/i&gt; / <i>y</i>`다. 이중 중괄호를 기본으로 쓰고, 삼중 중괄호는 꼭 필요할 때만 쓴다.

### 3-2. 섹션: `{{#items}}` (반복과 조건을 함께)

```java
String tpl = "{{#items}}<li>{{.}}</li>{{/items}}{{^items}}없음{{/items}}";
Mustache.compiler().compile(tpl).execute(Map.of("items", List.of("a", "b")));   // <li>a</li><li>b</li>
Mustache.compiler().compile(tpl).execute(Map.of("items", List.of()));           // 없음
```

- **`{{#이름}}...{{/이름}}`**: 값이 있으면 블록을 반복하거나 보여 준다. 목록이면 항목마다 반복한다.
- **`{{^이름}}...{{/이름}}`**: 값이 없거나 비어 있을 때만 보여 준다(역섹션).
- **`{{.}}`**: 현재 항목 자체를 가리킨다. 문자열 목록에서 쓴다.
- **검증 완료.** 목록이 있으면 `li` 두 개가 만들어지고, 빈 목록이면 "없음"이 나온다.

### 3-3. Thymeleaf와의 차이

| 항목 | Thymeleaf | Mustache |
|---|---|---|
| 모양 | HTML 속성 (`th:each`) | 중괄호 (`{{#items}}`) |
| 미리 보기 | HTML 파일을 그대로 열어도 보임 | 중괄호가 그대로 보임 |
| 로직 | 표현식과 조건을 많이 쓸 수 있다 | 로직이 적다. 값과 반복, 조건만 |
| Spring 연결 | 기본 지원이 강하다 | Spring Boot에 스타터가 있다 (`spring-boot-starter-mustache`) |

### 3-4. Spring과 함께 쓸 때 (미검증)

- 파일 위치: `src/main/resources/templates/`, 확장자는 `.mustache`.
- 컨트롤러는 Thymeleaf와 같이 뷰 이름을 반환한다.
- 의존성이 있으면 템플릿이 자동으로 연결된다. 설정 값은 `application.yml`에서 바꾼다(미검증).

**면접 기준:** "Mustache는 `{{ }}`로 값을 넣고, `{{# }}`로 반복과 조건을 쓰는 간단한 문법입니다. 이중 중괄호는 이스케이프되므로 기본으로 씁니다."

---

## 4. 두 엔진 고르기와 보안

- **고르는 기준:** 팀이 쓰는 방식, 화면을 디자이너와 같이 보는지, 로직이 많은지. 학원 세미프로젝트는 Thymeleaf와 Mustache를 모두 썼다.
- **공통 원칙:** 사용자 입력은 항상 이스케이프해서 출력한다. XSS는 이 한 가지 실수에서 대부분 시작된다.
- **공통 원칙:** 화면에 비즈니스 로직을 넣지 않는다. 계산과 권한 검사는 서비스 계층에서 한다(12장).
- **React와 함께 쓸지:** 같은 프로젝트에서 서버 템플릿과 React를 섞으면 구조가 복잡해진다. 하나를 주로 쓰고 다른 하나는 비교로 이해한다.

---

## 5. 실습 과제

1. Thymeleaf로 글 목록 화면을 만든다. `th:each`로 목록을 반복하고, 글이 없으면 "글이 없습니다"를 `th:if`로 보여 준다.
2. 같은 화면을 Mustache로 다시 만든다. `{{#posts}}`와 `{{^posts}}`로 같은 결과를 낸다.
3. 제목에 `<script>` 태그를 넣어서 `th:text`와 `th:utext`의 결과 차이를 직접 확인한다.

---

## 단원 통과 기준

- [ ] 서버 렌더링과 React의 차이를 설명할 수 있다.
- [ ] `th:text`와 `th:utext`의 차이를 설명하고, 어느 쪽을 쓸지 판단할 수 있다.
- [ ] `th:each`와 `th:if`로 목록과 조건을 화면에 쓸 수 있다.
- [ ] Mustache의 `{{ }}`, `{{{ }}}`, `{{# }}`, `{{^ }}`를 구별할 수 있다.
- [ ] `@Controller`가 뷰 이름을 반환하고 `@RestController`가 JSON을 반환하는 차이를 설명할 수 있다.
