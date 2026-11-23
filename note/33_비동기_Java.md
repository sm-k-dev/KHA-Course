# 비동기 Java: CompletableFuture와 ThreadLocal

> 중요도: **A** · 이 장의 목표: `CompletableFuture`로 여러 비동기 작업을 조합하고 예외를 처리하며, 실행 스레드를 지정하는 이유와 `ThreadLocal`의 누수 위험을 설명한다.

> 2026-11-23 추가 장 (로드맵 2순위 "비동기 Java" 항목, 32장 동시성 심화의 후속). 아래 예제는 JDK 25에서 실행해 결과를 확인했다.

---

## 1. `Future`의 한계와 `CompletableFuture`

`Future.get()`은 결과가 나올 때까지 **호출한 스레드를 막는다.** 여러 작업을 조합하기 어렵고, 콜백을 붙일 수 없다.

**`CompletableFuture`**는 다음을 할 수 있다.

- 결과가 나오면 다음 작업을 이어 붙인다 (`thenApply`, `thenCompose`)
- 여러 작업을 모두 기다리거나 하나만 기다린다 (`allOf`, `anyOf`)
- 예외를 한곳에서 처리한다 (`exceptionally`, `handle`)

```java
CompletableFuture<String> user = CompletableFuture.supplyAsync(() -> "kim");
CompletableFuture<String> greeting = user.thenApply(name -> "hello " + name);
System.out.println(greeting.join()); // hello kim (join은 막고 기다린다)
```

---

## 2. 조합

| 메서드 | 뜻 | 예 |
|---|---|---|
| `thenApply(f)` | 결과를 변환 (동기 변환) | `c.thenApply(String::length)` |
| `thenCompose(f)` | 결과로 **또 다른 비동기 작업**을 이어 붙임 | `userId → 주문 조회` |
| `thenCombine(other, f)` | 두 결과를 합침 | 사용자 + 잔액 |
| `allOf(…)` | 모두 끝날 때까지 기다림 | 여러 API 병렬 호출 |

**주의**: `thenApply` 안에서 또 `CompletableFuture`를 반환하면 `CompletableFuture<CompletableFuture<…>>`가 된다. 이때는 `thenCompose`를 쓴다.

---

## 3. 예외 처리

```java
CompletableFuture<Integer> safe = CompletableFuture
        .supplyAsync(() -> Integer.parseInt("abc"))
        .exceptionally(ex -> {
            System.out.println("failed: " + ex.getClass().getSimpleName());
            return -1;                       // 기본값으로 복구
        });
System.out.println(safe.join());             // -1
```

**주의**: 예외는 `CompletionException`으로 감싸져 전달된다. 원인을 보려면 `ex.getCause()`를 확인한다.

---

## 4. 실행 스레드 지정

`supplyAsync`는 기본적으로 `ForkJoinPool.commonPool()`을 쓴다. 네트워크나 DB를 기다리는 작업을 여기서 돌리면 공용 풀이 금방 막힌다. **I/O 작업은 전용 `Executor`를 넘긴다.**

```java
ExecutorService io = Executors.newFixedThreadPool(8);
CompletableFuture<String> fetched = CompletableFuture.supplyAsync(() -> "data", io);
io.shutdown();
```

---

## 5. ThreadLocal: 스레드마다 따로 가진 값

**정의** — `ThreadLocal`은 같은 변수 이름이라도 스레드마다 별도의 값을 가진다. 요청 ID, 로그인 사용자 같은 값을 요청 처리 흐름 안에서 전달할 때 쓴다.

**누수 위험**: 스레드 풀은 스레드를 재사용한다. 요청이 끝나면 `remove()`로 값을 지우지 않으면, 다음 요청이 이전 요청의 값을 보게 된다. 보안 사고로 이어질 수 있다.

```java
static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

void handle(String user) {
    CURRENT_USER.set(user);
    try {
        // 처리
    } finally {
        CURRENT_USER.remove();     // 반드시 지운다
    }
}
```

**비동기와의 관계**: `CompletableFuture`가 다른 스레드에서 실행되면 `ThreadLocal` 값은 **따라가지 않는다.** 그래서 비동기 경계를 넘을 때 값을 명시적으로 넘기거나, 요청 컨텍스트 전파 도구를 쓴다.

---

## 6. 검증 코드

```java
import java.util.*;
import java.util.concurrent.*;

public class AsyncCheck {
    static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        CompletableFuture<String> user = CompletableFuture.supplyAsync(() -> "kim");
        System.out.println(user.thenApply(n -> "hello " + n).join());            // hello kim

        CompletableFuture<String> order = user.thenCompose(
                u -> CompletableFuture.supplyAsync(() -> u + "-order-1"));
        System.out.println(order.join());                                        // kim-order-1

        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> 2);
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> 3);
        System.out.println(a.thenCombine(b, Integer::sum).join());              // 5

        CompletableFuture<Integer> safe = CompletableFuture
                .supplyAsync(() -> Integer.parseInt("abc"))
                .exceptionally(ex -> -1);
        System.out.println(safe.join());                                        // -1

        ExecutorService io = Executors.newFixedThreadPool(4);
        List<CompletableFuture<String>> calls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            final int id = i;
            calls.add(CompletableFuture.supplyAsync(() -> "item" + id, io));
        }
        CompletableFuture.allOf(calls.toArray(new CompletableFuture[0])).join();
        System.out.println(calls.stream().map(CompletableFuture::join).toList()); // [item1, item2, item3]
        io.shutdown();

        // ThreadLocal은 다른 스레드로 넘어가지 않는다
        CURRENT_USER.set("kim");
        String seenInOtherThread = CompletableFuture.supplyAsync(() -> String.valueOf(CURRENT_USER.get())).join();
        System.out.println("other thread sees: " + seenInOtherThread);       // null
        CURRENT_USER.remove();
    }
}
```

**실행 결과 (확인함)**

```
hello kim
kim-order-1
5
-1
[item1, item2, item3]
other thread sees: null
```

---

## 7. 실습 과제

1. `exceptionally` 대신 `handle((v, ex) -> …)`로 바꿔 성공과 실패를 같은 함수에서 처리한다.
2. 검증 코드에서 `io`를 쓰지 않고 `supplyAsync`만 쓴 경우와 쓴 경우를 비교해 스레드 이름을 출력한다.
3. `CURRENT_USER.remove()`를 빼고 스레드 풀에서 두 번 요청했을 때 두 번째 요청이 무엇을 보는지 확인한다.

---

## 단원 통과 기준

- [ ] `thenApply`와 `thenCompose`의 차이를 설명할 수 있다.
- [ ] `allOf`로 여러 비동기 작업을 기다리는 코드를 쓸 수 있다.
- [ ] I/O 작업에 전용 `Executor`를 쓰는 이유를 설명할 수 있다.
- [ ] `ThreadLocal`을 `remove()`하지 않으면 생기는 문제와 비동기 경계에서 값이 사라지는 이유를 설명할 수 있다.
