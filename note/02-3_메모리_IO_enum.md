# Java 02-3: 메모리·I/O·enum

> 이 파트는 [02장 Java 허브](02_Java_기초와객체지향.md)의 일부입니다. 이전: [02-2 컬렉션·예외·제네릭](02-2_컬렉션_예외_제네릭.md).

## 6. 메모리 영역 (Stack·Heap)과 GC 개념

- **메서드 영역:** 클래스 정보와 static 변수.
- **스택:** 메서드를 호출할 때마다 프레임이 쌓이고(LIFO), 지역 변수와 참조 변수 자체가 여기 있다. 메서드가 끝나면 프레임이 사라진다.
- **힙:** `new`로 만든 객체와 배열이 있다. 어떤 참조도 가리키지 않는 객체는 가비지 컬렉터(GC)가 회수한다.
- **확인:** `Box b = new Box(7);`에서 `b`는 스택에, `Box` 객체는 힙에 있다. 메서드가 끝나 `b`가 사라져도, 다른 참조가 남아 있는 객체는 힙에 그대로 남는다.
- **면접 기준:** "참조 변수는 스택, 객체는 힙에 생성되고, 참조가 없어진 객체는 GC가 회수합니다."

### 6-1. 메모리 문제를 직접 재현하기 (2026-11-23 보강, JDK 25에서 확인함)

> 아래 코드는 힙 최대 크기를 64MB로 제한해 실행했다(`-Xmx64m`). `held` 값은 실행마다 조금 달라질 수 있다.

```java
import java.util.ArrayList;
import java.util.List;

public class MemoryDemo {
    static class Box {
        final int v;
        Box(int v) { this.v = v; }
    }

    static int depth = 0;
    static void recurse() { depth++; recurse(); }   // 스택 프레임이 계속 쌓인다

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        System.out.println("max heap(MB)=" + rt.maxMemory() / (1024 * 1024));

        // 1) 힙: 참조가 남아 있는 객체는 GC가 회수하지 않는다
        Box keep = new Box(1);
        for (int i = 0; i < 3_000_000; i++) {
            new Box(i);                       // 참조를 버린 객체 → GC 대상
        }
        System.out.println("keep.v=" + keep.v + " (alive)");

        // 2) 스택: 재귀가 너무 깊으면 StackOverflowError
        try {
            recurse();
        } catch (StackOverflowError e) {
            System.out.println("StackOverflowError, depth>1000=" + (depth > 1000));
        }

        // 3) 힙 부족: 참조를 계속 쥐고 있으면 OutOfMemoryError
        List<byte[]> hold = new ArrayList<>();
        try {
            while (true) hold.add(new byte[1024 * 1024]);
        } catch (OutOfMemoryError e) {
            int held = hold.size();
            hold.clear();                     // 참조를 놓아야 아래 출력 문자열을 만들 메모리가 생긴다
            System.out.println("OutOfMemoryError, held(MB)=" + held);
        }
    }
}
```

```powershell
javac -d out MemoryDemo.java
java -Xmx64m -cp out MemoryDemo
```

**실행 결과 (확인함)**

```
max heap(MB)=64
keep.v=1 (alive)
StackOverflowError, depth>1000=true
OutOfMemoryError, held(MB)=31
```

**GC 로그 보기 (확인함)**

```powershell
java -Xmx64m -Xlog:gc -cp out MemoryDemo
```

실행하면 `Pause Young`와 `Pause Full` 같은 줄이 나온다. 예: `Pause Full (G1 Compaction Pause) 63M->1M(8M)`은 힙이 63MB에서 1MB로 줄었다는 뜻이다. 실행 한 번에 13번 정도 GC가 일어났다.

**세 가지 문제를 구별하기**

| 상황 | 원인 | 나타나는 신호 |
|---|---|---|
| 스택 오버플로 | 메서드 호출이 끝나지 않고 재귀로 쌓임 | `StackOverflowError` |
| 힙 부족 | 객체를 참조한 채로 계속 쌓아 둠 | `OutOfMemoryError: Java heap space` |
| GC 과다 | 짧은 시간에 객체를 많이 만듦 | 응답 지연, GC 로그가 자주 나옴 |

**핵심**: GC는 "참조가 없는 객체"만 회수한다. 그래서 Java에서도 참조를 오래 쥐고 있으면 메모리 누수가 생긴다. 캐시 맵에 항목을 계속 넣기만 하고 지우지 않는 것이 대표적인 예다.

**면접 기준**: "힙에 객체가 쌓이고 참조가 없어진 객체를 GC가 회수합니다. 힙이 부족하면 OutOfMemoryError가 나고, 재귀가 깊으면 StackOverflowError가 납니다. 참조를 계속 쥐면 GC가 회수하지 못해 누수가 생깁니다."

## 6-2. 입출력(I/O)과 인터페이스 default 메서드 (2026-11-23 보강, JDK 25에서 확인함)

### 파일 읽기·쓰기와 try-with-resources

파일, 네트워크, DB 연결처럼 닫아야 하는 자원은 `try (...)` 괄호 안에 넣으면 블록이 끝날 때 자동으로 닫힌다. 예외가 나도 닫힌다.

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IoDemo {
    public static void main(String[] args) throws Exception {
        Path file = Files.createTempFile("khac", ".csv");
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write("id,name"); w.newLine();
            w.write("1,kim"); w.newLine();
            w.write("2,lee"); w.newLine();
        }
        try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int count = 0;
            while ((line = r.readLine()) != null) {
                if (count++ == 0) continue;          // 헤더 건너뛰기
                String[] cols = line.split(",");
                System.out.println(cols[0] + " -> " + cols[1]);
            }
        }
        List<String> all = Files.readAllLines(file, StandardCharsets.UTF_8);
        System.out.println("lines=" + all.size());
        try (InputStream in = Files.newInputStream(file)) {
            System.out.println("bytes=" + in.readAllBytes().length);
        }
        Files.delete(file);
        System.out.println("deleted=" + !Files.exists(file));
    }
}
```

**실행 결과 (확인함)**

```
1 -> kim
2 -> lee
lines=3
bytes=23
deleted=true
```

**읽는 법**
- `bytes=23`은 줄바꿈이 Windows에서 `\r\n`(2바이트)이라서 나온 값이다. Linux나 macOS에서는 `\n` 하나라서 20이 된다. 바이트 수는 OS에 따라 달라진다.
- `readAllLines`는 파일 전체를 메모리에 올린다. 큰 파일에는 `BufferedReader`로 한 줄씩 읽는다.
- 문자열은 `StandardCharsets.UTF_8`을 명시한다. 기본 인코딩에 기대면 OS마다 한글이 깨질 수 있다(4장의 인코딩 설명).

**면접 기준**: "파일과 스트림은 `try-with-resources`로 닫고, 큰 파일은 한 줄씩 읽고, 문자 인코딩은 명시합니다."

### 인터페이스의 default 메서드와 static 메서드

Java 8부터 인터페이스에 구현이 있는 메서드를 둘 수 있다. 기존 인터페이스에 메서드를 추가해도 구현체가 깨지지 않게 하려고 도입됐다.

```java
public class DefaultDemo {
    interface Notifier {
        void send(String msg);                                   // 구현해야 하는 추상 메서드
        default void sendAll(String... msgs) {                   // 기본 구현: 구현체가 바꾸지 않아도 된다
            for (String m : msgs) send(m);
        }
        static Notifier console() {                              // 인터페이스의 static 메서드
            return m -> System.out.println("[console] " + m);
        }
    }

    public static void main(String[] args) {
        Notifier n = Notifier.console();
        n.sendAll("a", "b");
        Notifier silent = m -> { };                              // send 하나뿐이라 람다로 만들 수 있다
        silent.sendAll("x");
        System.out.println("done");
    }
}
```

**실행 결과 (확인함)**

```
[console] a
[console] b
done
```

**읽는 법**: `send`만 구현하면 `sendAll`은 그대로 쓸 수 있다. 추상 메서드가 하나뿐인 인터페이스는 람다로 만들 수 있다(함수형 인터페이스). 같은 시그니처의 default 메서드를 가진 인터페이스를 두 개 구현하면, 구현 클래스가 직접 재정의해서 충돌을 해결해야 한다.

**면접 기준**: "default 메서드는 인터페이스에 기본 구현을 주는 기능이고, 기존 구현체를 깨지 않고 인터페이스를 확장하려고 도입됐습니다."

## 6-3. enum, Iterator, String과 StringBuilder (2026-11-23 보강, JDK 25에서 확인함)

### enum: 정해진 상수 묶음

`enum`은 고정된 값 목록을 타입으로 만든다. 상수마다 값과 메서드를 가질 수 있다.

```java
import java.util.*;

public class EnumIter {
    enum OrderStatus {
        PAID(1000), SHIPPED(0), CANCELED(0);

        private final int fee;
        OrderStatus(int fee) { this.fee = fee; }
        int fee() { return fee; }
        boolean isFinal() { return this == SHIPPED || this == CANCELED; }
    }

    public static void main(String[] args) {
        OrderStatus s = OrderStatus.valueOf("PAID");
        System.out.println(s + " " + s.fee() + " " + s.ordinal() + " " + s.isFinal());
        for (OrderStatus o : OrderStatus.values()) System.out.print(o.name() + " ");
        System.out.println();
        switch (s) {
            case PAID -> System.out.println("paid-case");
            default -> System.out.println("other");
        }
        System.out.println(OrderStatus.valueOf("SHIPPED").compareTo(OrderStatus.PAID));

        List<String> names = new ArrayList<>(List.of("kim", "lee", "park"));
        try {
            for (String n : names) {
                if (n.equals("kim")) names.remove(n);   // 첫 원소 제거 → CME
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException");
        }
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            if (it.next().equals("lee")) it.remove();   // iterator로 안전하게 제거
        }
        System.out.println(names);

        String a = "ab";
        String b = a.concat("c");
        System.out.println(a + " " + b);
        StringBuilder sb = new StringBuilder("ab");
        sb.append("c");
        System.out.println(sb + " " + sb.length());
    }
}
```

**실행 결과 (확인함)**

```
PAID 1000 0 false
PAID SHIPPED CANCELED 
paid-case
1
ConcurrentModificationException
[park]
ab abc
abc 3
```

**읽는 법**
- `ordinal()`은 선언 순서(0부터)다. 상수 순서를 바꾸면 값이 바뀌므로 **DB에는 `name()`을 저장하고 `ordinal()`은 저장하지 않는다.**
- 확장 for문으로 순회하는 도중 `list.remove()`를 호출하면 다음 `next()`에서 `ConcurrentModificationException`이 난다. 순회 중 제거는 `Iterator.remove()`나 `removeIf`를 쓴다.
- `String`은 불변이다. `concat`은 새 문자열을 돌려주고 원본은 그대로다. 반복문에서 `+=`로 문자열을 계속 이어 붙이면 객체가 많이 생기므로 `StringBuilder`를 쓴다.

**면접 기준**: "enum은 정해진 상수 집합을 타입으로 만든 것이고, DB에는 순서 값이 아니라 이름을 저장합니다. 순회 중 제거는 Iterator로 하고, 문자열을 반복해서 이어 붙일 때는 StringBuilder를 씁니다."

