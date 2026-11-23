# JavaScript 비동기와 이벤트 루프: Promise, async/await, 실행 순서

> 중요도: **A** · 이 장의 목표: 이벤트 루프가 콜 스택, 마이크로태스크, 매크로태스크를 어떤 순서로 처리하는지 설명하고, Promise 체이닝과 async/await를 에러 처리까지 포함해 쓴다.

> 2026-11-23 추가 장 (로드맵 2순위 "JS 이벤트 루프·Promise 심화" 항목). 아래 코드는 Node.js로 실행해 결과를 확인했다. 브라우저에서는 DOM 이벤트 같은 태스크가 더해지지만 순서 규칙은 같다.

---

## 1. 왜 이벤트 루프가 필요한가

JavaScript는 **싱글 스레드**다. 한 번에 한 함수만 실행한다. 그래서 네트워크 응답이나 타이머를 기다리는 동안 다른 작업이 멈추면 안 된다. 이를 위해 기다리는 작업은 **환경(브라우저 또는 Node)**에 맡기고, 끝나면 콜백을 **큐**에 넣는다. 이벤트 루프는 콜 스택이 비었을 때 큐에서 하나씩 꺼내 실행한다.

```
콜 스택  ← 지금 실행 중인 함수
마이크로태스크 큐 ← Promise 콜백 (먼저 처리)
매크로태스크 큐 ← setTimeout, I/O 콜백
```

**규칙**

1. 콜 스택이 비면 마이크로태스크 큐를 **전부** 비운다.
2. 그다음 매크로태스크 하나를 실행하고, 다시 마이크로태스크를 전부 비운다.

---

## 2. 실행 순서 예제

```javascript
console.log('1 동기');
setTimeout(() => console.log('2 타이머'), 0);
Promise.resolve().then(() => console.log('3 마이크로태스크'));
console.log('4 동기');
```

**결과**: `1 동기` → `4 동기` → `3 마이크로태스크` → `2 타이머`

`setTimeout(…, 0)`이 바로 실행되지 않는 이유는 매크로태스크 큐에 들어가기 때문이다. Promise 콜백은 마이크로태스크라서 타이머보다 먼저 실행된다.

---

## 3. Promise

**정의** — 아직 끝나지 않은 비동기 작업의 결과를 나타내는 객체다. 상태는 `pending`, `fulfilled`, `rejected` 중 하나이고, 한 번 정해지면 바뀌지 않는다.

```javascript
function fetchUser(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (id > 0) resolve({ id, name: 'kim' });
      else reject(new Error('invalid id'));
    }, 10);
  });
}

fetchUser(1)
  .then(user => user.name)            // 값을 변환해 다음 then으로
  .then(name => console.log(name))    // kim
  .catch(err => console.error(err))   // 앞의 어느 단계에서든 실패하면 여기로
  .finally(() => console.log('done'));
```

**주의**

- `then` 안에서 `return`하지 않으면 다음 `then`은 `undefined`를 받는다.
- `then` 안에서 던진 예외는 `catch`로 간다. 하지만 `setTimeout` 콜백 안에서 던진 예외는 Promise 밖이라 `catch`로 잡히지 않는다.

---

## 4. async/await

`async` 함수는 항상 Promise를 반환한다. `await`는 Promise가 끝날 때까지 **그 함수만** 멈춘다. 이벤트 루프는 계속 돈다.

```javascript
async function main() {
  try {
    const user = await fetchUser(1);
    console.log(user.name);
    await fetchUser(-1);            // 여기서 reject되어 catch로 간다
    console.log('not reached');
  } catch (err) {
    console.log('caught:', err.message);
  }
}
main();
```

### 4.1 순차와 병렬

```javascript
// 순차: 합계 약 20ms
const a = await fetchUser(1);
const b = await fetchUser(2);

// 병렬: 약 10ms, 둘 다 시작한 뒤 기다린다
const [c, d] = await Promise.all([fetchUser(1), fetchUser(2)]);
```

`Promise.all`은 하나라도 실패하면 즉시 실패한다. 일부 실패를 허용하려면 `Promise.allSettled`를 쓴다.

### 4.2 흔한 실수

- `forEach` 안에서 `await`하면 기다리지 않는다. `for...of`를 쓴다.
- `await`를 빼먹으면 Promise 객체를 그대로 다룬다. `user.name`이 `undefined`가 된다.
- 에러를 잡지 않은 Promise는 Node에서 프로세스 경고 또는 종료를 일으킬 수 있다.

---

## 5. 검증 코드

```javascript
console.log('1 동기');
setTimeout(() => console.log('2 타이머'), 0);
Promise.resolve().then(() => console.log('3 마이크로태스크'));
console.log('4 동기');

function fetchUser(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (id > 0) resolve({ id, name: 'kim' });
      else reject(new Error('invalid id'));
    }, 10);
  });
}

async function main() {
  try {
    const user = await fetchUser(1);
    console.log(user.name);
    await fetchUser(-1);
    console.log('not reached');
  } catch (err) {
    console.log('caught:', err.message);
  }
  const [c, d] = await Promise.all([fetchUser(1), fetchUser(2)]);
  console.log(c.id, d.id);
}
main();
```

**실행 결과 (확인함)**

```
1 동기
4 동기
3 마이크로태스크
2 타이머
kim
caught: invalid id
1 2
```

---

## 6. 실습 과제

1. 검증 코드에 `queueMicrotask(() => console.log('5'))`를 추가하고 `3`과의 순서를 예측한 뒤 실행한다.
2. `fetchUser`를 5개 순차 호출과 `Promise.all` 병렬 호출로 각각 실행해 걸린 시간을 비교한다.
3. `forEach(async ...)`로 바꾸고 `await`가 기다리지 않는 것을 로그 순서로 확인한다.

---

## 단원 통과 기준

- [ ] 콜 스택, 마이크로태스크 큐, 매크로태스크 큐의 처리 순서를 설명할 수 있다.
- [ ] `setTimeout(…, 0)`이 Promise 콜백보다 늦게 실행되는 이유를 설명할 수 있다.
- [ ] `Promise.all`과 `Promise.allSettled`의 차이를 설명할 수 있다.
- [ ] `forEach` 안의 `await`가 순서를 보장하지 않는 이유를 설명할 수 있다.
