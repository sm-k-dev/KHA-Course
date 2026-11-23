# Node.js와 프론트엔드 도구: npm, Vite, 환경 변수, 간단한 서버

> 중요도: **A** · 이 장의 목표: Node.js가 프론트엔드 개발에서 맡는 역할을 설명하고, `npm`과 `package.json`으로 프로젝트를 관리하고, Vite로 빌드하고, 환경 변수가 빌드 결과에 어떻게 들어가는지 이해한다. 마지막으로 Node.js로 간단한 HTTP 서버를 띄워 API 응답을 확인한다.

> 2026-11-23 추가 장 (목표 점검 후 멘토 조언 반영: React 개발에 필요한 Node.js 도구 체인 보강). 아래 명령과 코드는 Node.js 26.13.1, npm 11.20.0, Vite 5.4.21에서 실행해 결과를 확인했다. Express·NestJS 같은 Node.js 백엔드 프레임워크는 다루지 않는다. 백엔드 주력은 Java·Spring이다.

---

## 1. Node.js는 프론트엔드에서 무슨 일을 하나

브라우저는 JavaScript를 실행하는 곳이고, Node.js는 브라우저 밖에서 JavaScript를 실행하는 환경이다. 프론트엔드 개발에서 Node.js는 다음 역할을 한다.

| 역할 | 예 |
|---|---|
| 패키지 관리 | React, TypeScript, Vite를 `npm`으로 설치 |
| 개발 서버 | `npm run dev`로 코드를 바꾸면 화면이 즉시 갱신 |
| 빌드 | 소스를 브라우저가 읽는 파일로 묶고 줄임 |
| 테스트와 검사 | 타입 검사, 린트, 단위 테스트 실행 |

**핵심**: 브라우저에서 돌아가는 최종 결과물에 Node.js가 들어가지 않는다. 개발하고 빌드하는 과정에서 Node.js가 필요하다.

---

## 2. npm과 package.json

`package.json`은 프로젝트의 설명서다. 의존성 목록과 실행 명령(scripts)을 담는다.

```json
{
  "name": "khac-front-demo",
  "version": "0.1.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "start-api": "node server.js"
  }
}
```

| 항목 | 뜻 |
|---|---|
| `dependencies` | 배포된 앱이 실행할 때 필요한 패키지 |
| `devDependencies` | 개발과 빌드에만 필요한 패키지 (Vite, TypeScript 등) |
| `scripts` | `npm run <이름>`으로 실행하는 명령 |
| `type: "module"` | 파일을 ES 모듈(`import`/`export`)로 해석 |

**규칙**
- `node_modules/`는 Git에 올리지 않는다. 다른 사람은 `npm install`로 같은 패키지를 받는다.
- `package-lock.json`은 올린다. 같은 버전을 재현하기 위해서다.
- 설치는 `npm install --save-dev vite@5`처럼 버전을 명시해도 된다. `npx`는 설치된 패키지의 실행 파일을 실행한다.

### 2.1 설치와 실행 확인

```bash
npm install --save-dev vite@5    # 개발 의존성으로 Vite 설치
npx vite --version               # vite/5.4.21 출력
```

**주의**: npm 11은 패키지 설치 스크립트(esbuild의 postinstall 등)를 기본으로 막는다. 실행 결과에 `install-scripts` 경고가 나올 수 있다. 이 장의 예제는 경고가 나도 빌드가 되었다. 경고를 해결하려면 `npm install-scripts approve <패키지>`를 검토한 뒤 허용한다. 아무 패키지나 허용하지 않는다.

---

## 3. Vite로 빌드하기

Vite는 개발 서버와 빌드 도구다. 개발 중에는 파일을 바꾸면 바로 반영하고, 빌드할 때는 `dist/` 폴더에 배포용 파일을 만든다.

`index.html`

```html
<!doctype html>
<html lang="ko">
  <head><meta charset="UTF-8"><title>demo</title></head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.js"></script>
  </body>
</html>
```

`src/main.js`

```javascript
const api = import.meta.env.VITE_API_URL;
const app = document.getElementById('app');
app.textContent = 'API: ' + api;
```

`npm run build`를 실행하면 `dist/index.html`과 `dist/assets/index-*.js`가 생긴다.

**실행 결과 (확인함)**: 빌드 성공, `dist/assets` 안의 JavaScript 파일 하나. 아래 4절에서 환경 변수가 이 파일에 어떻게 들어갔는지 확인한다.

---

## 4. 환경 변수: 빌드 시점에 박힌다

`.env` 파일에 `VITE_`로 시작하는 변수만 브라우저 코드에 노출된다.

```
VITE_API_URL=http://localhost:8080
```

빌드하면 `import.meta.env.VITE_API_URL`이 **문자열 값으로 바뀌어 파일 안에 들어간다.** 그래서 두 가지를 기억한다.

1. 브라우저에 들어가는 값은 **모두 사용자에게 보인다.** 비밀번호, 서버 전용 키를 `VITE_` 변수로 두면 안 된다. (23장의 비밀 관리와 같은 원칙)
2. 값을 바꾸려면 **다시 빌드해야 한다.** 배포 후 `.env`를 고쳐도 이미 만든 파일은 바뀌지 않는다.

**검증 결과**: 빌드된 JavaScript 파일에서 `localhost:8080` 문자열이 한 번 발견되었다. 값이 파일 안에 들어간 것을 확인했다.

---

## 5. 간단한 HTTP 서버로 API 확인하기

프론트엔드가 호출할 API가 없을 때 Node.js로 임시 서버를 띄울 수 있다. 실제 API는 Spring Boot 서버가 담당하고, 이 예제는 **프론트엔드가 어떤 응답을 받는지 확인하는 용도**다.

`server.js`

```javascript
import http from 'node:http';

const server = http.createServer((req, res) => {
  if (req.url === '/api/hello') {
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ message: '안녕하세요', port: process.env.PORT ?? '8081' }));
  } else {
    res.writeHead(404);
    res.end();
  }
});

server.listen(Number(process.env.PORT ?? 8081), () => console.log('listening'));
```

실행 (PowerShell 기준):

```powershell
$env:PORT = '8081'
node server.js
```

다른 터미널에서 호출하면 다음 응답을 받는다.

```
{"message":"안녕하세요","port":"8081"}
```

**결과 읽기**
- `Content-Type`의 `charset=utf-8`을 빼면 한글이 깨질 수 있다(4장의 인코딩 설명).
- `process.env.PORT`로 포트를 바꾼다. 포트를 코드에 고정하지 않는다(23장의 설정 원칙).

---

## 6. 흔한 실수

| 실수 | 결과 | 고치는 방법 |
|---|---|---|
| `node_modules`를 Git에 올림 | 저장소가 커지고 충돌이 잦음 | `.gitignore`에 추가 |
| `.env`에 비밀 키를 넣고 `VITE_`를 붙임 | 브라우저에서 키가 보임 | 비밀은 서버에 두고 브라우저는 서버를 호출 |
| 환경 변수를 바꾸고 다시 빌드하지 않음 | 예전 주소로 호출 | 환경별로 빌드하거나 배포 시 주입 |
| 버전을 정하지 않고 설치 | 팀원마다 다른 결과 | `package-lock.json`을 커밋 |
| `npm install` 경고를 무시하고 스크립트를 모두 허용 | 악성 설치 스크립트 실행 위험 | 필요한 패키지만 검토 후 허용 |

---

## 7. 실습 과제

1. 임시 폴더에서 `npm install --save-dev vite@5`로 Vite를 설치하고 `npx vite --version`을 실행한다.
2. 3절의 `index.html`과 `src/main.js`를 만들고 `npm run build`로 `dist`를 생성한다.
3. `.env`의 `VITE_API_URL`을 바꾼 뒤 다시 빌드하고, 바뀐 값이 `dist` 파일에 들어갔는지 찾아본다.
4. 5절의 `server.js`를 실행하고 `/api/hello`를 호출해서 응답을 확인한다.
5. React 화면에서 이 API를 `fetch`로 호출하는 코드를 한 줄 쓴다. (27장과 29장 참고)

---

## 단원 통과 기준

- [ ] `dependencies`와 `devDependencies`, `scripts`의 역할을 설명할 수 있다.
- [ ] `node_modules`를 Git에 올리지 않고 `package-lock.json`을 올리는 이유를 설명할 수 있다.
- [ ] `npm run build`로 배포 파일을 만들 수 있다.
- [ ] `VITE_` 환경 변수가 빌드 결과에 들어가고 브라우저에 노출되는 이유를 설명할 수 있다.
- [ ] Node.js로 간단한 HTTP 서버를 띄워 JSON 응답을 확인할 수 있다.
