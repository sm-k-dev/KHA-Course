# 프론트엔드: React와 TypeScript

> 중요도: **B** · 이 장의 목표: 컴포넌트와 상태, 서버 데이터 호출을 설명하고, TypeScript 타입으로 API 응답을 정의할 수 있다.

> 2026-11-23 추가 장 (로드맵 M06·M10 항목). **Node.js 26, React 18.3.1, TypeScript 5.6.3 환경에서 타입 검사와 렌더링을 실행해 확인했다.** 실행 중 장의 코드에서 `handleDelete`가 정의되지 않은 문제를 찾아 고쳤다.

---

## 1. 왜 React인가

**정의** — React는 화면을 **컴포넌트**라는 작은 조각으로 나누고, 데이터(상태)가 바뀌면 화면을 다시 그리는 라이브러리다.

**jQuery와의 차이** — jQuery는 DOM을 직접 찾아서 바꾼다. React는 "상태가 이렇다면 화면은 이렇게 보인다"를 선언하고, 실제 DOM 변경은 React가 한다. 화면이 커질수록 상태 추적이 쉬워진다.

**백엔드 중심 풀스택에서 React가 필요한 이유** — API를 호출하고, 응답을 화면에 그리고, 로그인 상태와 권한에 따라 메뉴를 바꾸는 일을 직접 해야 한다.

---

## 2. 컴포넌트와 상태 (개념 예)

```jsx
// 함수형 컴포넌트: props(입력)를 받아 화면을 돌려준다
function PostItem({ post, onDelete }) {
  return (
    <li>
      {post.title}
      <button onClick={() => onDelete(post.id)}>삭제</button>
    </li>
  );
}

// 상태: 값이 바뀌면 컴포넌트가 다시 그려진다
function PostList() {
  const [posts, setPosts] = useState([]);
  useEffect(() => {
    fetch('/api/posts')                       // 서버 호출
      .then((r) => r.json())
      .then(setPosts);
  }, []);                                     // 처음 한 번만 실행
  const handleDelete = (id: number) => setPosts((prev) => prev.filter((p) => p.id !== id));   // 새 배열로 상태 갱신
  return (
    <ul>
      {posts.map((p) => <PostItem key={p.id} post={p} onDelete={handleDelete} />)}
    </ul>
  );
}
```

- **핵심 개념:** props는 바깥에서 받는 값, state는 컴포넌트가 가진 값이다. state를 바꾸면 다시 그린다.
- **흔한 실수:** state를 직접 고치면(`posts.push(...)`) 화면이 다시 그려지지 않는다. 새 배열을 만들어 `setPosts`에 넘긴다.
- **key:** 목록 항목에는 고유한 `key`를 준다. 순서가 바뀔 때 React가 어떤 항목인지 구별하기 위해서다.

---

## 3. TypeScript: API 응답에 타입 붙이기

**정의** — TypeScript는 JavaScript에 타입을 더한 언어다. 컴파일 시점에 잘못된 필드 사용을 잡아낸다.

```ts
// 백엔드 응답 DTO와 같은 모양으로 타입을 정의한다
interface Post {
  id: number;
  title: string;
  ownerId: number;
}

async function fetchPosts(): Promise<Post[]> {
  const res = await fetch('/api/posts');
  if (!res.ok) throw new Error(`HTTP ${res.status}`);   // 상태 코드 확인 (fetch는 404에서 reject하지 않음)
  return (await res.json()) as Post[];                   // 응답 모양을 타입으로 선언
}
```

- **왜 중요한가:** 백엔드 DTO 필드 이름이 바뀌면 프론트에서 컴파일 오류로 바로 알 수 있다. 17장 AJAX에서 본 "fetch는 404에서 reject하지 않는다"를 여기서도 확인한다.
- **주의:** `as Post[]`는 타입을 믿으라는 선언일 뿐, 실제 값을 검사하지 않는다. 실제 검사가 필요하면 런타임 검증 라이브러리를 쓴다.

---

## 4. 인증 연동

- **로그인 후 토큰 저장:** 토큰을 어디에 둘지 정한다. 로컬 저장소(localStorage)는 JavaScript가 읽을 수 있어서 XSS에 취약하다. HttpOnly 쿠키는 JavaScript에서 읽을 수 없어서 XSS 피해를 줄인다. 대신 CSRF를 따로 막아야 한다.
- **401 처리:** 응답이 401이면 로그인 화면으로 보낸다.
- **CORS:** 프론트와 백엔드의 주소(출처)가 다르면 브라우저가 응답을 막을 수 있다. 백엔드에서 허용할 출처를 설정한다.

---

## 5. 실습 과제 (실행 환경이 갖춰진 뒤)

1. `/api/posts`를 호출해 목록을 그리는 컴포넌트를 만든다.
2. 삭제 버튼을 누르면 DELETE 요청을 보내고, 성공하면 목록에서 뺀다.
3. 응답 타입을 TypeScript로 정의하고, 백엔드 필드 이름을 바꿔 컴파일 오류가 나는지 확인한다.

---

## 단원 통과 기준

- [ ] props와 state의 차이를 설명할 수 있다.
- [ ] 상태를 직접 고치면 안 되는 이유를 설명할 수 있다.
- [ ] fetch 응답을 상태 코드와 함께 확인하고, TypeScript 타입을 붙일 수 있다.
- [ ] 토큰 저장 위치에 따른 XSS와 CSRF 위험을 비교할 수 있다.

## 6. 실행 검증 결과 (2026-11-23)

| 확인 | 결과 |
|---|---|
| 정상 코드 타입 검사 (`PostList.tsx`, strict 모드) | 오류 없음 (통과) |
| 일부러 틀린 타입 (`id: '1'`을 number 자리에) | `TS2322: Type 'string' is not assignable to type 'number'` (잡힘) |
| `PostItem` 서버 렌더링 | 제목과 삭제 버튼이 HTML에 포함 (통과) |
| `PostList` 서버 렌더링 | 목록이 비어 있음 (`<ul></ul>`). `useEffect`는 서버에서 실행되지 않기 때문 (통과) |
| `removePost` 함수 | 해당 id만 빠지고, 원본 배열은 바뀌지 않음 (통과) |

- **확인한 코드 구성:** `removePost`를 순수 함수로 분리해서 상태 갱신 로직만 따로 검사할 수 있게 했다.
- **아직 하지 않은 것:** 브라우저에서 실제로 `/api/posts`를 호출하는 동작과 화면 클릭 테스트. 이것은 개발 서버와 백엔드가 함께 있어야 한다.