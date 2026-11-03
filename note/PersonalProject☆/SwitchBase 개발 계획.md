# SwitchBase 개발 계획

Oct 12, 2026 · @Soon

SwitchBase를 Spring Boot 모놀리스 하나로 만들고, 기획서의 1\~3단계를 먼저 완성한 뒤 AI 기능을 4단계로 얹는다. AI는 포트폴리오에서 보여줄 수 있는 방식으로 설계하되, 없어도 사이트가 돌아가는 부가 기능으로 둔다.

> **프로젝트 원칙: 모든 결정과 판단은 사람이 하고, AI는 활용할 뿐이다.** AI는 요약하고, 찾아주고, 제안하고, 표시한다. 게시, 삭제, 되돌리기, 차단 같은 결정은 항상 사람이 버튼을 누른다.

## 기술 스택

| 영역 | 선택 | 이유 |
| --- | --- | --- |
| 언어 / 프레임워크 | Java 25 (LTS), Spring Boot 4.x | 현재 LTS + 최신 Boot. Spring AI 2.0이 Boot 4 기반이다 |
| 빌드 | Gradle (Kotlin DSL) | 요즘 Spring 예제 기본값 |
| 화면 | Thymeleaf + htmx | Java 풀스택 학습 흐름과 맞고 Spring 하나로 끝난다. 위키 페이지가 검색에 잘 잡힌다. React는 나중에 JavaScript를 다시 공부할 때 검토 (확정) |
| DB | PostgreSQL + pgvector | 일반 데이터와 AI 유사도 검색을 DB 하나로 |
| 데이터 접근 | Spring Data JPA + QueryDSL | 필터가 많아서 동적 쿼리가 필요하다 |
| 마이그레이션 | Flyway | 스키마 변경 이력을 코드로 남긴다 |
| 인증 | Spring Security, 이메일 + 비밀번호 + 이메일 인증 | 기획서대로. SNS는 나중에 OAuth2로 추가 |
| AI | [Spring AI 2.0](https://spring.io/blog/2026/06/12/spring-ai-2-0-0-GA-available-now/) + Claude | 모델을 바꿔도 코드가 거의 안 바뀐다 |
| 테스트 | JUnit 5, Testcontainers (PostgreSQL) | 실제 DB로 통합 테스트 |
| 배포 | AWS (EC2에 jar + RDS PostgreSQL), GitHub Actions | 수업에서 배우는 AWS를 그대로 쓴다. Docker는 로컬 DB와 테스트에만 쓴다. 프리 티어로 월 비용을 낮게 유지한다 |

## 패키지 구조와 데이터 모델

기능별로 패키지를 나눈다. 나중에 기능을 더하거나 빼기 쉽고, 코드를 보는 사람도 구조를 바로 이해한다.

```
com.switchbase
├─ switches     스위치 위키 (문서, 버전, 출처)
├─ ratings      체감 평가, 집계
├─ builds       유저 빌드, 개조 태그
├─ sounds       타건음 샘플 (유튜브 임베드)
├─ reviews      후기, 도움됨
├─ collections  즐겨찾기 / 보유 / 써봄
├─ members      회원, 인증
├─ moderation   신고, 최근 변경
├─ ai           요약, 유사 스위치, 자연어 검색 (4단계)
└─ common       설정, 예외 처리
```

| 테이블 | 한 행이 뜻하는 것 | 주요 컬럼 |
| --- | --- | --- |
| switch | 스위치 1종의 현재 버전 | 제조사, 모델명, 타입, 규격, 압력·거리, 재질, 판매 상태, current\_revision\_id |
| switch\_revision | 위키 수정 1회 | switch\_id, 수정자, 스펙 스냅샷(JSON), 수정 요약, 상태(게시 / 검토 대기 / 되돌림) |
| spec\_source | 스펙 항목의 출처 | revision\_id, 항목명, URL, 제조사값 / 실측값 |
| build | 유저의 키보드 1대 | member\_id, 하우징 재질, 보강판, 마운트, 키캡 |
| rating | 스위치 1종을 빌드 1대에서 써본 평가 | switch\_id, member\_id, build\_id(선택), 소리 크기, 소리 성향, 타건감, 피로도, 별점, 윤활 여부 |
| mod\_tag / rating\_mod\_tag | 개조 태그와 평가의 연결 | 태그명, 별칭 |
| switch\_rating\_stats | 스위치별 집계 캐시 | 평균값, 표본 수, 갱신 시각 |
| sound\_sample | 타건음 영상 1개 | switch\_id, 유튜브 ID, 시작 시간, 채널, 녹음 조건 태그 |
| review | 짧은 후기 | switch\_id, member\_id, 본문, 추천 대상, 도움됨 수 |
| collection\_item | 즐겨찾기 / 보유 / 써봄 | member\_id, switch\_id, 상태 |
| member | 회원 | 이메일, 닉네임, 인증 여부, 역할 |
| report | 신고 1건 | 대상 종류/ID, 사유, 처리 상태 |

위키 버전은 Hibernate Envers 대신 switch\_revision을 직접 둔다. "이전 버전과 비교", "되돌리기", "검토 대기" 화면을 만들려면 버전이 도메인 데이터여야 하기 때문이다.

## 개발 단계별 할 일

기획서의 단계를 그대로 따른다. 각 단계가 끝날 때마다 배포해서 직접 써본다. 기간은 정하지 않는다.

### 0단계. 뼈대

- [ ] Spring Boot 프로젝트 생성, GitHub 저장소
- [ ] Docker Compose로 로컬 PostgreSQL
- [ ] Flyway 첫 마이그레이션, Testcontainers 테스트 하나
- [ ] GitHub Actions: 빌드 + 테스트
- [ ] 공통 레이아웃 (Thymeleaf), 에러 페이지

### 1단계. 나만의 스위치 위키

- [ ] switch, switch\_revision, spec\_source 엔티티
- [ ] 스위치 목록 / 상세 페이지
- [ ] 검색과 필터 (QueryDSL): 제조사, 타입, 규격, 압력 범위, 판매 상태
- [ ] 관리자 전용 등록/수정 화면 (이때부터 수정은 revision으로 저장)
- [ ] CSV 가져오기로 스위치 50\~100개 시딩
- [ ] 첫 배포 (AWS EC2 + RDS)

### 2단계. 커뮤니티 열기

- [ ] 회원가입, 이메일 인증, 로그인 (Spring Security)
- [ ] 유저 위키 수정, 버전 비교, 되돌리기, 최근 변경 목록
- [ ] 빌드 등록, 체감 평가 + 개조 태그 (자동완성)
- [ ] 집계: 평가 저장 시 이벤트로 switch\_rating\_stats 갱신, 표본 수 표시
- [ ] 후기, 내 컬렉션, 신고
- [ ] 개인정보 처리방침, 회원 탈퇴

### 3단계. 소리와 비교

- [ ] 타건음 샘플 등록 (유튜브 URL + 시작 시간 + 녹음 조건)
- [ ] A vs B 비교 화면: 스펙, 체감 평균(레이더 차트), 같은 채널 샘플 우선
- [ ] 입문자 가이드 + 감각 용어 사전
- [ ] 빌드 조건 필터로 체감 평균 좁혀 보기

### 4단계. AI

아래 AI 기능 계획 참고.

## AI 기능 계획

기획서의 AI 아이디어 3개에 자연어 검색을 더해, 여섯 개를 우선순위대로 넣는다. 모두 1\~3단계에서 쌓인 데이터를 재료로 쓰므로, 그 데이터가 있어야 의미가 있다.

| 순서 | 기능 | 방식 | 포트폴리오 포인트 |
| --- | --- | --- | --- |
| 1 | 후기 요약 | 후기가 N개 쌓일 때마다 배치로 요약을 새로 만들고 DB에 저장. 화면은 저장된 요약만 읽음 | 비용 통제(캐시, 배치), 요청이 아닌 이벤트 기반 생성 |
| 2 | 자연어 검색 | "조용하고 묵직한 50g 이하 리니어" → LLM이 필터 객체(Java record)로 변환 → 기존 QueryDSL 필터 재사용 | 구조화된 출력, 검증. LLM이 SQL을 직접 만들지 않게 설계 |
| 3 | 비슷한 느낌의 스위치 | 체감 평균 + 스펙을 숫자 벡터로 만들어 pgvector 코사인 검색. 다음에 후기 텍스트 임베딩을 섞음 | 벡터 검색. 처음엔 LLM 없이 동작하는 것도 설명 거리가 된다 |
| 4 | 취향 기반 추천 | 내가 높게 평가한 스위치 벡터의 평균으로 유사 검색, 안 써본 것만 | 3번 재사용 |
| 5 | 위키 수정 검토 보조 | 수정 전후 차이 + 출처를 LLM에 보내 "의심" 표시만. 최종 판단은 사람 | 사람이 최종 결정하는 AI 보조 설계 |
| 6 | 녹음 조건 태그 제안 | 유튜브 영상 설명란에서 보드·키캡·윤활 정보를 뽑아 태그 후보로 보여줌 | 비정형 텍스트 → 구조화 데이터 |

**모델**: 요약·검색 변환처럼 자주 부르는 건 저렴한 Claude Haiku 5.5, 검토 보조처럼 판단이 필요한 건 Claude Sonnet 5.5. Claude는 임베딩 모델을 제공하지 않으므로, 3번의 텍스트 임베딩은 Spring AI가 지원하는 별도 임베딩 모델을 쓴다.

**원칙**

- AI가 죽어도 사이트는 돌아간다: 타임아웃과 폴백(일반 검색, 요약 숨김)을 둔다
- AI가 만든 내용에는 "AI 요약" 같은 표시를 붙인다
- 월 비용 상한: 요청 수 제한, 결과 캐시, 사용량 로그
- 품질 테스트: 자연어 검색은 "입력 문장 → 기대 필터" 예제 20\~30개를 만들어 모델/프롬프트를 바꿀 때마다 돌린다
- API 키는 환경 변수로만 둔다

## 다른 프로젝트 연동 (NEXORA SentinelOps, 선택)

NEXORA는 팀 프로젝트이므로 이 연동은 SwitchBase의 필수 기능이 아니라 포트폴리오용 선택 기능이다. SwitchBase는 연동 없이도 완전히 동작한다. SentinelOps에는 이벤트 수신, 상태 확인, 인시던트 목록 REST API가 추가됐다.

SentinelOps는 오류 감지와 보안 감지를 함께 한다. SwitchBase의 오류와 보안 이벤트를 NEXORA의 SentinelOps로 보내고, 인시던트 감지와 관리는 SentinelOps에서 한다. 유저에게는 보이지 않는다. 포트폴리오에는 "직접 만든 인시던트 관리 시스템을 다른 서비스의 오류·보안 모니터링에 응용했다"로 쓴다.

**흐름**

1. SwitchBase에서 이벤트가 생긴다. 오류: 처리 안 된 예외, 5xx 응답, AI 호출 실패. 보안: 로그인 실패, 짧은 시간 안의 대량 가입, 위키 대량 수정·삭제, 신고 급증, 403/404 요청 폭증
2. SwitchBase가 이벤트를 JSON으로 SentinelOps API에 백그라운드 전송한다. 실패하면 몇 번 재시도하고, 그래도 안 되면 로그만 남긴다
3. SentinelOps가 감지 규칙에 따라 인시던트를 만든다
4. 운영자가 SentinelOps 화면에서 인시던트를 확인한다. 오류는 직접 고치고, 보안 조치인 계정 정지·IP 차단·수정 되돌리기는 SwitchBase 관리자 화면에서 직접 한다. 자동 차단은 하지 않는다

**NEXORA에 추가할 것** (지금은 JSP/Servlet 화면만 있고 REST API가 없다)

| 엔드포인트 | 하는 일 |
| --- | --- |
| POST /api/v1/events | 외부 서비스의 오류·보안 이벤트를 받아 (\`category\`: ERROR / SECURITY) 검증 후 저장하고 감지 규칙 실행 |
| GET /api/v1/incidents?source=switchbase | SwitchBase 관련 인시던트 목록 (SwitchBase 관리자 화면 요약용) |
| GET /api/v1/health | 연동 상태 확인 |

- 기존 .do 경로와 분리된 API 서블릿에서 JSON을 처리한다 (Jackson)
- 인증은 서비스 간 API 키(`X-Api-Key` 헤더). 키는 양쪽 모두 환경 변수로만 둔다
- 입력 검증과 공통 오류 응답 `{"code": ..., "message": ...}`
- 이벤트에 `source` 필드를 두어 어느 서비스에서 왔는지 구분한다. 나중에 다른 프로젝트도 붙일 수 있다

```json
{
  "source": "switchbase",
  "type": "LOGIN_FAILED",
  "occurredAt": "2026-10-12T14:50:00+09:00",
  "actor": { "memberId": 42, "ip": "203.0.113.7" },
  "detail": { "attempt": 5 }
}
```

**SwitchBase에 추가할 것**

- `SecurityEventPublisher` 인터페이스와 `SentinelOpsClient` 구현(Spring `@HttpExchange`), `@Async`로 전송해 유저 요청을 기다리게 하지 않는다
- `switchbase.sentinel.enabled=false`면 아무것도 안 하는 구현이 돈다. 기본값은 꺼짐
- 관리자 화면 "보안" 탭: 최근 인시던트 요약, SentinelOps 바로가기, 연동 상태(마지막 응답, 오류 수)
- 시점: 2단계(로그인·위키 수정이 생기는 시점) 이후

## 포트폴리오로 보여줄 것

기능 개수보다 "왜 이렇게 만들었는지"를 보여주는 게 힘이 있다. 만들면서 자연스럽게 남기면 되는 것들:

- 설계 결정 기록(ADR): 위키 버전을 직접 만든 이유, 빌드를 평가에 붙인 이유, LLM에게 SQL을 맡기지 않은 이유 같은 것을 한 페이지씩
- README: 한 줄 소개, 스크린샷, ERD, 실행 방법, 배포된 주소
- 테스트: Testcontainers 통합 테스트와 AI 검색 예제 테스트가 CI에서 도는 모습
- 실제 운영 숫자: 등록된 스위치 수, 평가 수, AI 월 비용
- 개인 스토리: "내가 필요해서 만든 사이트"라는 출발점 자체가 좋은 이야기다

## 정해야 할 것

- [x] 화면 방식: Thymeleaf + htmx로 확정
- [ ] 배포 위치와 월 비용 상한
- [ ] 도메인 이름
- [ ] 저장소 공개 여부 (포트폴리오용이면 공개 추천)
