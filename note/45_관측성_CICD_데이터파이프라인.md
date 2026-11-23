# 관측성, CI/CD, 데이터 파이프라인: 보고, 배포, 재처리

> 중요도: **A** · 이 장의 목표: 로그·메트릭·트레이스를 구별하고 지표를 고르는 기준(RED, USE)을 안다. CI/CD 단계와 배포·롤백 절차를 설명한다. 재실행해도 결과가 같은 ETL을 만들고 대조 검사로 누락을 찾는다.

> 2026-11-23 추가 장 (로드맵 M15·M16 항목, 보강). ETL 적재와 대조 예제는 JDK 25에서 실행해 결과를 확인했다(6절). **Prometheus, Grafana, Micrometer, GitHub Actions 예제는 실행 환경이 없어서 실행하지 않았다.** 설정 구조만 설명한다.

---

## 1. 관측성 (Observability)

**정의** — 시스템 내부 상태를 밖에서 보고 이해할 수 있는 능력이다. 세 가지 신호로 본다.

| 신호 | 무엇인가 | 예 |
|---|---|---|
| 로그 | 개별 사건 기록 | "주문 1001 결제 실패: 잔액 부족" |
| 메트릭 | 시간에 따른 수치 | 초당 요청 수, 오류율, p95 지연 시간 |
| 트레이스 | 한 요청이 여러 서비스를 지난 경로 | 게이트웨이 → 주문 서비스 → DB, 각 구간 시간 |

**구별해야 할 것** — 로그를 많이 남긴다고 관측성이 높은 것이 아니다. 무엇을 알고 싶은지 정하고, 그 답을 주는 신호를 남겨야 한다. 민감 정보(비밀번호, 개인정보)는 로그에 남기지 않는다(23장).

### 1.1 메트릭 종류

| 종류 | 뜻 | 예 |
|---|---|---|
| Counter | 계속 증가만 하는 값 | 누적 요청 수, 누적 실패 수 |
| Gauge | 오르내리는 현재 값 | 현재 DB 연결 수, 큐 길이 |
| Histogram | 값의 분포를 구간으로 나눠 센 것 | 응답 시간 구간별 요청 수 |

지연 시간은 평균보다 **Histogram으로 p95, p99**를 계산해서 본다(37장).

### 1.2 무엇을 측정할지: RED와 USE

- **RED (요청 중심 서비스):** Rate(초당 요청), Errors(실패 비율), Duration(응답 시간). API 서버에 쓴다.
- **USE (자원 중심):** Utilization(사용률), Saturation(대기 정도), Errors(에러). CPU, 메모리, 디스크, 커넥션 풀에 쓴다.

**예**: 주문 API 대시보드는 RED로 보고, 그 아래 DB 커넥션 풀은 USE로 본다. 응답이 느릴 때 어디를 볼지 순서가 정해진다.

### 1.3 도구 (구성 개념)

- **Prometheus:** 메트릭을 주기적으로 수집해 저장한다. 애플리케이션이 `/actuator/prometheus` 같은 경로에 수치를 노출하면 Prometheus가 가져간다.
- **Micrometer:** Spring Boot가 메트릭을 Prometheus 형식으로 바꾸는 계측 라이브러리다.
- **Grafana:** 메트릭을 대시보드로 보여 준다.
- **ELK (Elasticsearch, Logstash, Kibana):** 로그를 모아 검색하고 시각화한다.

```yaml
# application.yml (구성 구조 예시, 미실행)
management:
  endpoints:
    web:
      exposure:
        include: health, prometheus   # 외부에 노출할 엔드포인트만 고른다
```

**주의**: `/actuator/**`를 인증 없이 인터넷에 열면 내부 정보가 노출된다. 노출 범위를 최소로 하고, 내부망이나 인증 뒤에 둔다.

### 1.4 알림 설계

- 원인이 아니라 **사용자 영향**에 알린다. 예: "오류율 5% 초과 5분 지속"이 "CPU 80%"보다 낫다.
- 알림마다 담당자와 조치 문서를 연결한다. 조치가 없는 알림은 끈다.
- 알림이 너무 많으면 사람이 무시하게 된다(알림 피로).

**면접 기준:** "로그는 사건, 메트릭은 수치 추세, 트레이스는 요청 경로입니다. 셋을 함께 보면 어디서 느려졌는지 찾을 수 있습니다."

---

## 2. CI/CD 파이프라인

**정의** — 코드를 올리면 자동으로 빌드, 테스트, 배포까지 진행하는 흐름이다. CI(지속적 통합)는 병합할 때마다 빌드와 테스트를 돌리는 것이고, CD(지속적 배포)는 통과한 결과를 배포하는 것이다. (지속적 배포와 지속적 전달은 사람의 승인 단계 유무로 구별한다.)

**일반적인 흐름**
1. 개발자가 브랜치에 push한다.
2. 파이프라인이 빌드하고 단위 테스트, 통합 테스트를 돌린다.
3. 정적 분석과 보안 검사(36장)를 통과해야 한다.
4. PR이 승인되고 `main`에 병합되면 이미지를 만든다. 이미지 태그는 커밋 해시나 버전 번호로 붙인다.
5. 스테이징에 배포하고 확인한 뒤 운영에 배포한다.
6. 문제가 생기면 이전 버전으로 롤백한다.

### 2.1 GitHub Actions 구조 (미실행)

```yaml
# .github/workflows/ci.yml
name: ci
on:
  pull_request:
  push:
    branches: [main]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: ./gradlew test
      - name: 보안 점검
        run: ./gradlew dependencyCheckAnalyze
        env:
          NVD_API_KEY: ${{ secrets.NVD_API_KEY }}   # 비밀 값은 Secrets에서 주입
```

- `on`: 어떤 이벤트에서 실행할지
- `jobs`/`steps`: 실행할 단계 순서
- `${{ secrets.* }}`: 저장소 설정의 비밀 값을 읽는다. 파일에 값을 직접 쓰지 않는다.

**주의:** 포크한 PR이 비밀 값을 읽도록 허용하면 외부 코드가 비밀을 훔칠 수 있다. 기본 설정을 유지한다.

### 2.2 배포 전략

| 전략 | 방식 | 장점 | 단점 |
|---|---|---|---|
| 재생성 (Recreate) | 전부 내리고 새 버전 올림 | 단순 | 다운타임 발생 |
| 롤링 (Rolling) | 조금씩 교체 | 무중단에 가깝다 | 두 버전이 잠시 함께 돈다 |
| 블루/그린 | 새 환경을 만들고 한 번에 전환 | 빠른 롤백 | 환경이 두 배 필요 |
| 카나리 | 일부 사용자에게만 새 버전 | 문제를 작은 범위에서 발견 | 라우팅과 관측이 필요 |

Kubernetes의 롤링 업데이트는 47장에서 실행해 확인했다.

### 2.3 배포 체크리스트

- [ ] 마이그레이션이 이전 버전과 호환되는가 (26장: 추가 → 이관 → 제약 순서)
- [ ] 헬스 체크와 대시보드를 배포 중에 본다
- [ ] 롤백 명령과 담당자를 미리 적어 둔다
- [ ] 비밀 값이 설정 파일에 없고 환경 변수나 Secrets에 있다

---

## 3. 데이터 파이프라인과 ETL

**정의** — 원본 데이터를 가져오고(Extract), 정리하고 변환하고(Transform), 대상 저장소에 적재하는(Load) 흐름이다. ELT는 적재한 뒤 변환하는 순서다.

**핵심 원칙 1: 멱등성 (Idempotency)** — 같은 작업을 여러 번 돌려도 결과가 같아야 한다. 파이프라인은 실패하면 재실행되는데, 재실행 때마다 데이터가 중복되면 안 된다.

**핵심 원칙 2: 대조 (Reconciliation)** — 입력 수와 적재 수, 격리 수가 맞는지 확인한다. 맞지 않으면 누락이나 중복이 있다는 뜻이다.

**핵심 원칙 3: 격리 (Quarantine)** — 잘못된 행을 버리지 말고 따로 모아서 원인을 추적한다.

---

## 4. 재처리 설계

- 배치 ID와 상태(`대기`, `성공`, `실패`)를 기록해서 실패한 배치만 다시 돌릴 수 있게 한다.
- 격리 기록도 **배치 ID와 행 키 기준으로** 중복 없이 남긴다. 재실행하면 같은 격리 기록이 또 쌓이기 쉽다(6절 결과 참고).
- 원본 데이터 개수, 적재 개수, 격리 개수를 매번 비교하는 검사를 넣는다.
- 실패 알림은 "몇 건, 어느 배치, 어떤 원인"을 담는다.

---

## 5. 흔한 실수

| 실수 | 결과 | 고치는 방법 |
|---|---|---|
| `INSERT`만 사용 | 재실행 때 같은 행이 두 번 들어감 | 고유 키로 upsert (`INSERT ... ON DUPLICATE KEY UPDATE`) |
| 잘못된 행을 조용히 버림 | 누락을 아무도 모름 | 격리 테이블에 저장하고 수치 확인 |
| 대조 없이 "성공"으로 표시 | 누락이 있어도 성공처럼 보임 | 입력 = 적재 + 격리를 검사 |
| 배치 실행 시각을 현재 시각으로 계산 | 재실행하면 결과가 달라짐 | 기준 날짜를 배치 인자로 받음 |

---

## 6. 검증 코드 (JDK 25에서 실행)

```java
import java.util.*;

public class EtlCheck {
    record Row(String id, String name) {}

    static final Map<String, Row> target = new LinkedHashMap<>();
    static final List<String> quarantine = new ArrayList<>();
    static final List<String> runLog = new ArrayList<>();

    // 한 배치를 적재하고, 실행 결과 수치를 돌려준다
    static int[] load(String batchId, List<Row> batch) {
        int valid = 0, overwritten = 0;
        for (Row r : batch) {
            if (r.id() == null || r.id().isBlank()) {
                quarantine.add(batchId + ": 빈 ID " + r.name());   // 잘못된 행은 격리
                continue;
            }
            valid++;
            if (target.containsKey(r.id())) overwritten++;          // 같은 키면 덮어쓰기(upsert)
            target.put(r.id(), r);
        }
        int bad = batch.size() - valid;
        runLog.add(batchId + " input=" + batch.size() + " valid=" + valid + " bad=" + bad);
        return new int[]{batch.size(), valid, bad, overwritten};
    }

    public static void main(String[] args) {
        List<Row> batch = List.of(
                new Row("1", "kim"), new Row("2", "lee"),
                new Row(" ", "broken"), new Row("1", "kim-updated"));

        int[] first = load("B-2026-11-23", batch);
        System.out.println("first run: input=" + first[0] + " valid=" + first[1] + " bad=" + first[2] + " overwritten=" + first[3]);
        System.out.println("stored=" + target.size() + " quarantine=" + quarantine);

        // 같은 배치를 다시 실행 (재처리)
        load("B-2026-11-23", batch);
        System.out.println("after rerun stored=" + target.size() + " name(1)=" + target.get("1").name());
        System.out.println("quarantine size=" + quarantine.size());

        // 대조: 입력 수 = 적재 시도 수 + 격리 수
        int input = first[0], valid = first[1], bad = first[2];
        System.out.println("reconcile: " + (input == valid + bad ? "ok" : "mismatch"));
        runLog.forEach(System.out::println);
    }
}
```

**실행 결과 (확인함)**

```
first run: input=4 valid=3 bad=1 overwritten=1
stored=2 quarantine=[B-2026-11-23: 빈 ID broken]
after rerun stored=2 name(1)=kim-updated
quarantine size=2
reconcile: ok
B-2026-11-23 input=4 valid=3 bad=1
B-2026-11-23 input=4 valid=3 bad=1
```

**결과 읽기**
- 재실행해도 `stored=2`로 대상 행은 늘지 않았다. 멱등성이 지켜졌다.
- 같은 ID가 두 번 들어오면 마지막 값(`kim-updated`)이 남았다.
- 하지만 `quarantine size`가 1에서 2로 늘었다. **격리 기록은 멱등하지 않다.** 4절의 규칙대로 배치 ID와 행 키로 중복을 막아야 한다.

---

## 7. 실습 과제

1. 6절 코드의 격리 기록을 `Set`으로 바꿔 재실행해도 `quarantine size`가 늘지 않게 고친다.
2. 배치 인자에 날짜를 넣어 두 번 실행해도 같은 결과가 나오는지 확인한다.
3. 본인 프로젝트에 배포 체크리스트 네 항목을 적용했을 때 빠진 항목을 한 가지 찾는다.
4. 주문 API의 RED 지표를 정하고, 각 지표가 어떤 알림으로 이어질지 한 문장씩 적는다.

---

## 단원 통과 기준

- [ ] 로그, 메트릭, 트레이스의 차이를 예로 설명할 수 있다.
- [ ] RED와 USE의 대상을 구분하고, 지연 시간을 평균 대신 p95로 보는 이유를 설명할 수 있다.
- [ ] CI와 CD의 차이를 설명하고, 파이프라인 단계를 순서대로 말할 수 있다.
- [ ] 롤링, 블루/그린, 카나리의 차이를 설명할 수 있다.
- [ ] 같은 배치를 두 번 돌려도 대상 데이터가 중복되지 않는 적재 방식을 구현할 수 있다.
- [ ] 격리 기록까지 멱등하게 만들어야 하는 이유를 설명할 수 있다.