# JavaScript 핵심 문법: 배열 고차 함수, 구조 분해, 클로저, this, 모듈, class

> 중요도: **A** · 이 장의 목표: 프론트엔드 코드를 읽고 쓰는 데 필요한 JavaScript 문법을 채운다. 배열을 `map`, `filter`, `reduce`로 다루고, 구조 분해와 스프레드를 쓰고, 클로저와 `this`의 동작을 설명하고, 모듈과 class를 구별한다.

> 2026-11-23 추가 장 (목표 점검 결과 보강: 12장 JavaScript는 값·DOM·Promise를 다뤘지만 배열 고차 함수, 구조 분해, 클로저, this, 모듈, class가 빠져 있었다). 아래 코드는 Node.js로 실행해 결과를 확인했다. 브라우저 전용 코드(DOM)는 12장을 본다.

---

## 1. 배열 고차 함수

**정의** — 고차 함수는 함수를 인자로 받거나 함수를 반환하는 함수다. 배열에서는 반복문 대신 `map`, `filter`, `reduce`를 쓴다.

```javascript
const orders = [
  { id: 1, amount: 1200, paid: true },
  { id: 2, amount: 800, paid: false },
  { id: 3, amount: 3400, paid: true },
];

const paidAmounts = orders
  .filter(o => o.paid)              // 조건에 맞는 것만
  .map(o => o.amount);              // 값을 바꿈
const total = paidAmounts.reduce((sum, a) => sum + a, 0);  // 하나로 모음

console.log(paidAmounts, total);    // [ 1200, 3400 ] 4600
```

| 메서드 | 결과 | 원본 변경 |
|---|---|---|
| `map` | 같은 길이의 새 배열 | 없음 |
| `filter` | 조건에 맞는 원소의 새 배열 | 없음 |
| `reduce` | 누적 한 개 값 | 없음 |
| `find` | 조건에 맞는 첫 원소 (없으면 `undefined`) | 없음 |
| `some` / `every` | 하나라도 / 모두 조건 만족 여부 | 없음 |
| `sort` | 같은 배열을 정렬 | **있음** |

**주의**: `sort`는 원본을 바꾼다. 원본을 보존하려면 `[...arr].sort(...)`처럼 복사한 뒤 정렬한다. `sort`의 기본 비교는 문자열 순서라서 숫자 정렬에는 비교 함수를 준다.

```javascript
[10, 9, 1].sort();                 // [1, 10, 9]  문자열 기준
[10, 9, 1].sort((a, b) => a - b);  // [1, 9, 10]
```

---

## 2. 구조 분해와 스프레드

```javascript
const user = { name: 'kim', address: { city: 'Seoul' }, role: 'admin' };

const { name, role = 'guest', address: { city } } = user;   // 구조 분해 + 기본값
console.log(name, role, city);                              // kim admin Seoul

const [first, ...rest] = [1, 2, 3];                         // 배열 분해 + 나머지
console.log(first, rest);                                   // 1 [ 2, 3 ]

const updated = { ...user, role: 'member' };                // 얕은 복사 후 일부만 변경
console.log(updated.role, user.role);                       // member admin
```

**주의**: 스프레드 복사는 **얕은 복사**다. 중첩 객체(`address`)는 같은 객체를 가리킨다. 깊은 변경이 필요하면 값을 하나씩 새로 만든다.

---

## 3. 클로저 (Closure)

**정의** — 함수가 자신이 만들어질 때의 바깥 변수를 기억하고 계속 쓰는 것이다.

```javascript
function makeCounter() {
  let count = 0;                     // 바깥 함수의 변수
  return () => ++count;              // 안쪽 함수가 count를 기억한다
}

const next = makeCounter();
console.log(next(), next(), next()); // 1 2 3

const other = makeCounter();
console.log(other());                // 1  (count는 함수마다 따로 있다)
```

**활용**: 상태를 외부에 노출하지 않고 숨긴다. React의 `useState`나 이벤트 핸들러가 이 원리로 값을 기억한다.

**흔한 실수 (반복문과 `var`)**

```javascript
const fns = [];
for (var i = 0; i < 3; i++) fns.push(() => i);
console.log(fns.map(f => f()));      // [ 3, 3, 3 ]  var는 함수 단위 변수라서 모두 같은 i를 본다

const fns2 = [];
for (let j = 0; j < 3; j++) fns2.push(() => j);
console.log(fns2.map(f => f()));     // [ 0, 1, 2 ]  let은 반복마다 새 변수
```

---

## 4. `this`

**정의** — `this`는 함수를 **어떻게 호출했는지**에 따라 정해진다. 어디에 썼는지가 아니다.

| 호출 방식 | `this` |
|---|---|
| 객체의 메서드로 호출 `obj.method()` | `obj` |
| 일반 함수로 호출 `fn()` | 엄격 모드에서는 `undefined`, 아니면 전역 |
| 화살표 함수 | 자신을 감싼 바깥의 `this`를 그대로 씀 |

```javascript
const timer = {
  seconds: 5,
  tickLater() {
    setTimeout(() => console.log(this.seconds), 0);   // 화살표: 바깥의 this(timer)
  },
};
timer.tickLater();                                   // 5

const detached = timer.tickLater;
// detached()는 this가 달라져서 this.seconds 접근이 실패한다 (메서드를 떼어 낸 흔한 실수)
```

**실무 규칙**: 콜백에는 화살표 함수를 쓰면 `this`를 잃지 않는다. 메서드를 변수에 담아 넘길 때 `this` 손실이 생기니 주의한다.

---

## 5. 호이스팅과 `let`/`const`의 차이

- `var`와 함수 선언은 선언이 **위로 끌려 올라간다**(호이스팅). 값은 `undefined`로 먼저 존재한다.
- `let`과 `const`도 블록 시작에서 존재하지만 선언 줄에 닿기 전에는 쓸 수 없다. 이 구간을 **일시적 사각지대(TDZ)**라고 한다.

```javascript
console.log(a);          // undefined (var는 선언이 끌려 올라감)
var a = 1;

try { console.log(b); } catch (e) { console.log(e.name); }   // ReferenceError (TDZ)
let b = 2;
```

**실무 규칙**: 기본은 `const`, 값을 바꿔야 할 때만 `let`, `var`는 쓰지 않는다.

---

## 6. 모듈 (import / export)

```javascript
// mathUtil.js
export const add = (a, b) => a + b;
export default function multiply(a, b) { return a * b; }

// main.js
import multiply, { add } from './mathUtil.js';
console.log(add(2, 3), multiply(2, 3)); // 5 6
```

- `export`로 내보내고 `import`로 가져온다. 파일마다 자기 스코프를 가진다.
- Node.js와 브라우저 모두 ES 모듈을 쓴다. 파일 확장자 `.js`를 명시해야 하는 환경이 있다.

---

## 7. class와 프로토타입

```javascript
class Order {
  #secret = 'hidden';               // # 은 클래스 내부에서만 접근되는 private 필드
  static count = 0;

  constructor(id, amount) {
    this.id = id;
    this.amount = amount;
    Order.count++;
  }

  get label() { return `order-${this.id}`; }
  total(tax) { return this.amount * (1 + tax); }
}

const o = new Order(7, 1000);
console.log(o.label, o.total(0.1), Order.count);   // order-7 1100 1
console.log(o.secret);                              // undefined (private 필드는 밖에서 보이지 않는다)
```

**프로토타입**: class는 프로토타입 기반 상속을 쓰기 편하게 감싼 문법(문법 설탕)이다. 메서드는 인스턴스마다 복사되지 않고 프로토타입에 한 번만 있다. `Object.getPrototypeOf(o) === Order.prototype`이 참이다.

**주의**: 부동소수점은 십진수를 정확히 나타내지 못한다. `0.1 + 0.2`는 `0.30000000000000004`가 된다. 금액 계산은 정수 최소 단위(원, 센트)로 하거나 `Decimal` 라이브러리를 쓴다.

---

## 8. 옵셔널 체이닝과 nullish 병합

```javascript
const res = { data: null };
console.log(res.data?.items?.length);   // undefined (중간에 null이어도 에러가 안 난다)
console.log(res.data?.items ?? []);     // [] (null과 undefined일 때만 기본값)
console.log(0 || 10, 0 ?? 10);          // 10 0  (||는 0도 걸러서 주의)
```

---

## 9. 검증 코드

```javascript
const orders = [
  { id: 1, amount: 1200, paid: true },
  { id: 2, amount: 800, paid: false },
  { id: 3, amount: 3400, paid: true },
];
const total = orders.filter(o => o.paid).map(o => o.amount).reduce((s, a) => s + a, 0);
console.log('total', total);

const user = { name: 'kim', address: { city: 'Seoul' }, role: 'admin' };
const { name, role = 'guest', address: { city } } = user;
console.log(name, role, city);
console.log([...[3, 1, 2]].sort((a, b) => a - b), [10, 9, 1].sort());

function makeCounter() { let count = 0; return () => ++count; }
const next = makeCounter();
console.log(next(), next(), next(), makeCounter()());

const fns = []; for (var i = 0; i < 3; i++) fns.push(() => i);
console.log(fns.map(f => f()));

const timer = { seconds: 5, tickLater() { setTimeout(() => console.log('timer', this.seconds), 0); } };
timer.tickLater();

class Order {
  #secret = 'hidden';
  static count = 0;
  constructor(id, amount) { this.id = id; this.amount = amount; Order.count++; }
  get label() { return `order-${this.id}`; }
  total(tax) { return this.amount * (1 + tax); }
}
const o = new Order(7, 1000);
console.log(o.label, Order.count, o.secret, Object.getPrototypeOf(o) === Order.prototype);

const data = { data: null };
console.log(data.data?.items?.length, data.data?.items ?? [], 0 || 10, 0 ?? 10);
```

**실행 결과 (Node.js 확인함)**

```
total 4600
kim admin Seoul
[ 1, 2, 3 ] [ 1, 10, 9 ]
1 2 3 1
[ 3, 3, 3 ]
timer 5
order-7 1 undefined true
undefined [] 10 0
```

---

## 10. 실습 과제

1. `orders`에서 미결제(`paid: false`) 금액의 합을 `filter`와 `reduce`로 구한다.
2. `makeCounter`를 두 번 실행한 카운터가 서로 독립적인지 확인하고, `let`을 `var`로 바꿨을 때 `for` 루프 결과가 달라지는 이유를 설명한다.
3. `timer.tickLater`를 `const f = timer.tickLater; f();`로 호출했을 때 무슨 일이 생기는지 확인한다. (에러 또는 `undefined`)
4. 금액 계산을 `Decimal` 없이 정수 원 단위로 바꾼다면 어떤 값을 저장할지 한 문장으로 적는다.

---

## 단원 통과 기준

- [ ] `map`, `filter`, `reduce`를 체이닝해서 데이터를 가공할 수 있다.
- [ ] `sort`가 원본을 바꾸는 이유와 숫자 정렬 비교 함수를 설명할 수 있다.
- [ ] 구조 분해와 기본값, 스프레드의 얕은 복사를 설명할 수 있다.
- [ ] 클로저가 상태를 기억하는 원리를 설명하고, `var`와 `let`의 반복문 차이를 설명할 수 있다.
- [ ] `this`가 호출 방식에 따라 정해지고, 화살표 함수에서는 바깥 `this`를 쓰는 이유를 설명할 수 있다.
- [ ] `export`/`import`와 class의 private 필드, `static`을 쓸 수 있다.
