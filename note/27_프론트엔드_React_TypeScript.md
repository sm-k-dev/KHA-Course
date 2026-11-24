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

- **왜 중요한가:** 백엔드 DTO 필드 이름이 바뀌면 프론트에서 컴파일 오류로 바로 알 수 있다. 18장 AJAX에서 본 "fetch는 404에서 reject하지 않는다"를 여기서도 확인한다.
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

## 테스트: Vitest와 Testing Library (2026-11-23 보강, 확인함)

**정의**: Vitest는 Vite와 같은 설정을 쓰는 테스트 실행기다. Testing Library는 컴포넌트를 **사용자가 보는 방식**(버튼 이름, 화면 글자)으로 찾아 테스트하게 해 준다.

**설치 (확인함)**: 이 장의 예제는 vitest 5.1.1, React 18.3.1, `@testing-library/react` 16.3.3로 실행했다.

```powershell
npm install --save-dev vitest jsdom @testing-library/react @testing-library/dom react@18 react-dom@18 @vitejs/plugin-react
```

`vite.config.js`

```javascript
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  test: { environment: 'jsdom' },   // 브라우저 DOM을 흉내 내는 환경
});
```

`src/price.js`

```javascript
export function total(items) {
  return items.reduce((sum, item) => sum + item.amount, 0);
}
```

`src/Cart.jsx`

```jsx
import { useState } from 'react';

export function Cart({ initial = 0 }) {
  const [count, setCount] = useState(initial);
  return (
    <div>
      <p>count: {count}</p>
      <button onClick={() => setCount(count + 1)}>add</button>
    </div>
  );
}
```

`src/Cart.test.jsx`

```jsx
import { afterEach, describe, expect, it } from 'vitest';
import { cleanup, fireEvent, render, screen } from '@testing-library/react';
import { Cart } from './Cart';
import { total } from './price';

afterEach(cleanup);   // 테스트마다 화면을 정리한다

describe('total', () => {
  it('sums amounts', () => {
    expect(total([{ amount: 1200 }, { amount: 3400 }])).toBe(4600);
  });
});

describe('Cart', () => {
  it('increments the count when the button is clicked', () => {
    render(<Cart initial={1} />);
    expect(screen.getByText('count: 1')).toBeTruthy();
    fireEvent.click(screen.getByRole('button', { name: 'add' }));
    expect(screen.getByText('count: 2')).toBeTruthy();
  });
});
```

```powershell
npx vitest run
```

**실행 결과 (확인함)**: `Tests  2 passed (2)`

**실패 확인 (확인함)**: 기대값을 일부러 틀리게 쓰면 다음처럼 나온다.

```
FAIL  src/fail.test.js > wrong expectation on purpose
AssertionError: expected 1 to be 2 // Object.is equality
Tests  1 failed (1)
```

**읽는 법**
- `getByRole('button', { name: 'add' })`는 "이름이 add인 버튼"을 찾는다. 클래스 이름이나 DOM 구조에 기대지 않으므로 리팩터링에 덜 깨진다.
- `fireEvent.click`은 클릭 이벤트를 보낸다. 이후 화면이 바뀌었는지 `getByText`로 확인한다.
- `environment: 'jsdom'`이 없으면 `document`가 없어서 컴포넌트 테스트가 실패한다.

**면접 기준**: "순수 함수는 Vitest로 입력과 출력을 검증하고, 컴포넌트는 Testing Library로 사용자 동작(클릭, 화면 글자)을 기준으로 검증합니다."

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
- **브라우저 확인 (2026-11-24, 확인함):** Vite 개발 서버(6.3.5, React 18.3.1, TypeScript 5.6.3)와 Spring Boot 4.1.1 API(8092 포트)를 함께 띄우고, 내장 브라우저에서 확인했다.
  - 목록 화면: `GET /api/posts`가 Vite 프록시를 거쳐 응답하고 게시글 3개가 그려졌다.
  - 삭제: "삭제 2" 버튼을 누르자 화면에서 2번만 빠지고 "남은 게시글: 2개"가 되었다. 서버 목록도 1·3번만 남아 DELETE가 실제로 반영되었다.
  - 콘솔 오류: 정상 흐름에서는 없었다.
  - 서버 중단 시: 목록 요청이 500으로 실패하면 "목록 조회 실패: 500"이 화면에 나왔다.
  - **발견한 문제**: 목록 요청이 실패한 상태에서도 "남은 게시글: 0개"가 같이 보인다. 오류 상태에서는 개수를 숨기는 편이 정확하다. 이 장의 예제는 그대로 두고 여기에 적어 둔다.
  - 미실행: 로그인이 필요한 화면, 실제 사용자 흐름 전체, 다른 브라우저에서의 확인.