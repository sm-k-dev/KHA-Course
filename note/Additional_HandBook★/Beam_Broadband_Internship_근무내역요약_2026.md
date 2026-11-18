# Beam Broadband 인턴십 경험 인수인계서

> **목적:** 이 대화 세션을 삭제한 후에도 다른 ChatGPT 세션에 업로드하여 호주 IT 취업용 이력서, LinkedIn, 커버레터, 면접 STAR 답변을 작성할 수 있도록 실제 경험과 확인이 필요한 사항을 보존한다.
>
> **작성 기준:** 2026-11-13까지 이 대화에서 사용자가 직접 공유한 코드, 오류, 작업 설명 및 인턴십 관련 대화. **완료/해결/배포 여부가 확인되지 않은 작업은 완료했다고 표현하지 않는다.**

## 1. 인턴십 기본 정보

| 항목 | 내용 | 확인 상태 |
|---|---|---|
| 실습 회사/프로젝트 | Beam Broadband 관련 Wix–UISP–Stripe 시스템 | 프로젝트명 확인. 공식 고용주 법인명은 별도 확인 필요 |
| 직무명 | Software Developer Intern / Backend Development Intern 등 후보 | 공식 직함 확인 필요 |
| 시작일 | 2025-11-05 | 사용자 제공 |
| 종료 시점 | 2026년 2월 초 무렵 | 2026-02-09 전후 대화에서 '지난주 종료'라고 설명. 정확한 날짜 확인 필요 |
| 교육 과정 | Professional Year(PY) 인턴십 | 사용자 제공 |
| 멘토/회사 대표 | Dustin | 사용자 제공 |
| 근무지/근무 형태 | 미확인 | 이력서 작성 전 확인 |

### 한 문장 요약

호주 ISP(인터넷 서비스 제공업체) 관련 운영 환경에서 **Wix 회원가입/인증 경로와 UISP CRM, Stripe 결제 설정, Make.com 자동화, 이메일 온보딩**을 연계하는 JavaScript 기반 업무에 참여하고, API 인증 및 응답 지연 문제를 조사했다.

## 2. 시스템 아키텍처와 비즈니스 맥락

### 핵심 설계 원칙

- **Wix:** 회원가입 및 로그인 UI/인증 경로. Wix Member 계정은 사용하지만, Wix에 별도의 고객정보 데이터베이스를 구축하지 않았다고 사용자 명시.
- **UISP:** 고객정보의 기준 시스템(source of truth). 신규 고객 생성, 기존 고객 조회, Client Zone 인증 및 로그인 토큰 생성에 사용.
- **Stripe:** 결제 수단 설정 및 구독 결제 관련 기능.
- **Make.com:** 고객/요금제/계약 정보를 외부 자동화 웹훅으로 전달.
- **이메일:** 환영 이메일 및 고객의 대시보드 접근 안내. 사용자는 가입 이메일에 로그인 토큰이 포함된 대시보드 링크를 발송하는 흐름이라고 설명했지만, 공유된 signup 코드의 `sendWelcomeEmail()` 호출 인자에는 `dashboardUrl`이 보이지 않음. 실제 이메일 템플릿/백엔드에서 링크를 어떻게 구성했는지 확인 필요.

### 당시 공유된 가입 흐름 (백업 코드 기준)

1. Wix signup 페이지에서 이름, 이메일, 전화번호, 주소, 요금제, 계약 옵션 입력.
2. `checkEmailExists(email)`에서 Wix CRM Contacts 및 UISP 활성/보관 고객 조회.
3. `generateSecurePassword()`로 비밀번호 생성.
4. `registerWithPassword()`로 Wix Member 등록.
5. `createUispClientAndService()`에서 UISP 고객 조회/생성, Client Zone 로그인, 로그인 토큰 URL 생성, Stripe setup session 생성.
6. Make.com 웹훅에 `clientID`, 이메일, 요금제, 계약값 전달.
7. `sendWelcomeEmail()` 호출.
8. Stripe 결제 URL로 리다이렉트.

**중요:** 위 순서는 공유된 백업 코드의 흐름이며, 이후 제안된 리팩터링이 최종 운영 환경에 반영되었는지는 확인되지 않았다.

### 발견된 설계상 주의점

- 백업 `uisp.jsw`는 기존 UISP 고객을 발견하면 가입을 거부하는 대신 기존 `clientId`를 사용하여 다음 단계로 진행했다. **기존 이메일 가입 금지 요구와 불일치.**
- 백업 `signup.js`는 UISP 처리 전에 Wix Member를 생성하므로, UISP 단계 실패 시 Wix 계정만 남을 수 있다.
- UISP 인증 키, Client Zone 세션 토큰, 생성된 비밀번호, 토큰 URL을 콘솔에 출력하지 않도록 개선 필요.
- 공유된 Stripe Checkout URL은 session ID 뒤에 긴 고정 fragment가 붙은 형태였다. 동적 세션에 대해 URL 구성이 정확한지 검증 필요.
- `createUispClientAndService`라는 이름과 달리 공유된 코드에는 UISP **서비스 생성 API 호출이 명확히 보이지 않음**. '고객 및 서비스 생성 구현 완료'라고 이력서에 쓰기 전에 확인 필요.
- `contractValue.length`로 계약 개월 수를 판단하는 로직은 실제 데이터 타입과 비즈니스 규칙 검증이 필요.

## 3. 주요 업무 A — Wix 회원가입 및 고객 온보딩 연동

### 사용 기술

JavaScript, Wix Velo, Wix Backend `.jsw`, `wix-fetch`, `wix-secrets-backend`, `wix-crm-backend`, `wix-auth`, Wix Members 관련 백엔드, REST API, JSON, 비동기 `async/await`.

### 공유된 파일 및 역할

| 파일 | 확인된 역할 |
|---|---|
| `pages/Signup.js` 또는 signup 페이지 코드 | 입력 검증, 요금제 선택, 로딩 UI, 백엔드 호출, Make.com 전달, 환영 이메일 호출, Stripe 이동 |
| `backend/contactUtils.jsw` | Wix CRM 및 UISP 이메일 중복 조회; 활성/보관 고객 검색 |
| `backend/uisp.jsw` | UISP 고객 조회/생성, Client Zone 로그인 및 토큰 URL 생성, Stripe setup session 호출 |
| `backend/passwordGenerator.jsw` | 비밀번호 생성 함수 호출 확인; 내부 구현 미공유 |
| `backend/registerMember.jsw` | Wix Member 생성 함수 호출 확인; 내부 구현 미공유 |
| `backend/email.jsw` | 환영 이메일 발송 함수 호출 확인; 내부 구현 미공유 |

### 구현·수정·검토한 기능

- 가입 입력값 검증: 이름, 이메일 형식/중복, 호주 전화번호 형식, 주소, 요금제.
- 이메일 중복 검사: Wix Contacts 조회 후 UISP `/crm/api/v1.0/clients?email=...` 및 `?isArchived=1&email=...` 조회.
- UISP API 인증: Wix Secrets Manager의 `uispApiKey`를 가져와 `x-auth-token` 헤더 사용.
- 신규 고객 등록용 UISP payload 구성: 이름, 사용자명(이메일), 비밀번호, 주소, 연락처.
- Client Zone 로그인 및 `/client-zone/login-token` 호출로 대시보드 접근 URL 확보.
- UISP의 `/crm/online-payment/stripe/create-setup-session/{clientId}` 엔드포인트 호출.
- Make.com 웹훅 및 이메일 전송 연계.
- 로딩 오버레이와 제출 버튼 비활성화로 중복 클릭 방지.

### 중복 이메일에 대한 확정 요구사항

1. **UISP가 고객 데이터의 기준 시스템이다.**
2. **이미 UISP에 있는 이메일로 신규 가입이 진행되어서는 안 된다.**
3. 이메일 확인이 실패했을 때 '존재하지 않는다'고 간주하는 **fail-open 방식은 허용하지 않는 것이 요구사항에 부합**한다.
4. 기존 고객을 발견한 경우 신규 가입을 차단하고, 필요하면 별도 인증된 계정 복구/로그인 경로를 마련해야 한다.

## 4. 주요 업무 B — UISP API 응답 지연 및 504 오류 조사

### 실제 관찰된 오류

브라우저/프론트:

```text
Error: Request failed with status code 504
```

Wix 측 메시지:

```text
WebMethod request timed-out after 59 seconds...
Did you forget to resolve a promise?
```

추가된 8초 타임아웃 진단 로그:

```text
[checkEmailExists] start {"email":"[test email]"}
[checkEmailExists] wix exists? {"existsInWix":false,"count":0}
[checkEmailExists] uisp(active) request failed fetch_timeout_8000ms
```

### 확인된 사실과 확인되지 않은 원인

**확인된 사실**

- 이메일 형식 오류나 특정 임시 이메일 도메인 차단이 핵심 증거는 아니었다.
- Wix 측 이메일 검증 WebMethod가 59초 내에 끝나지 않아 504를 반환했다.
- 8초 타임아웃 진단에서 UISP 활성 고객 조회가 8초 이내에 완료되지 않았다.
- 동료는 UISP API가 작동한다고 보고했다.
- 인증 헤더 없이 브라우저 또는 PowerShell로 API에 접근했을 때 HTTP 401이 반환되었다. 이는 **토큰을 넣지 않은 테스트**였으며 Wix에 저장된 실제 토큰의 유효성을 입증하거나 반증하지 않는다.

**아직 확인되지 않은 원인**

- UISP 애플리케이션 자체의 지연인지, Wix→UISP 네트워크/DNS/TLS/프록시/WAF 문제인지.
- Wix Secrets의 `uispApiKey`가 정상적으로 조회되는지, 실제 값이 유효한지.
- UISP active/archived 쿼리의 성능 및 반환 형식.
- Wix CRM 조회, `getSecret()`, UISP fetch 중 어느 단계가 가장 크게 지연되는지(추가 계측 필요).
- 동료가 테스트한 엔드포인트·인증 방식·네트워크·응답 시간과 Wix 호출 조건이 일치하는지.

### 수행한 진단/개선 시도

- 브라우저 개발자 도구 Console의 `Validation error` 및 error stack 확인.
- Wix Velo Logs에서 백엔드 요청 오류 및 타임아웃 확인.
- `console.log/error`를 통한 단계별 실행 흐름, 상태코드, 응답시간 기록 코드 검토.
- `Promise.race()` 기반 8초 타임아웃 도입/테스트. **주의: Promise.race는 호출자 대기를 종료할 뿐, 실제 네트워크 요청을 반드시 중단하지는 않음.**
- 활성/보관 고객 조회 병렬화, 캐시, 재시도, 15~20초 진단 타임아웃 등을 대안으로 논의. **실제 배포/효과는 미확인.**
- Wix Publish 후 재테스트했으나 504가 지속된 시점이 있었음.
- PowerShell의 `curl` 별칭이 `Invoke-WebRequest`로 해석되어 `-i` 오류가 발생하는 문제 확인; `curl.exe` 또는 PowerShell 문법 사용 안내.
- 토큰 없는 PowerShell 테스트에서 401을 확인했고, 이후 사용자가 실제 토큰을 넣지 않았음을 명확히 함.

### 이력서에 쓸 때 정확한 표현

- 적절: **Investigated API timeouts and authentication-related issues across Wix and UISP using application logs and HTTP request diagnostics.**
- 적절: **Introduced or tested timeout handling and diagnostic logging for backend API requests.** (실제 반영 상태 확인 후 선택)
- 부적절: **Resolved all UISP connectivity issues**, **eliminated 504 errors**, **fixed UISP server/database performance** — 해결 완료 증거 없음.

## 5. 주요 업무 C — Stripe 구독 청구일 정렬 작업

### 비즈니스 요구사항

- Stripe Subscriptions를 사용한 선불형 요금 청구 운영.
- 여러 구독의 결제/청구 기준일을 **매월 7일**로 정렬하는 작업.
- 이미 확정된 청구 기간을 임의로 재구성하지 않고, 중복 청구와 미납 고객 문제를 피하려는 요구.

### 논의한 기술적 접근

- Python Stripe SDK 사용. 개발 환경에서 `stripe` 패키지를 **14.1.0 → 14.3.0**으로 업그레이드한 기록이 있음.
- `SubscriptionSchedule.create(from_subscription=...)`를 통한 기존 구독 스케줄 기반 변경 구상.
- 현재 기간 유지 → trial bridge → 7일 기준 정상 청구라는 **3-phase 설계안**.
- `proration_behavior="none"`을 활용하는 방안.
- trial로 인한 청구 공백을 보정하기 위한 **구독별 one-off invoice** 생성 구상.
- 기존 Stripe Price를 사용한 `InvoiceItem` 생성으로 제품/가격 연결 유지 시도.
- `charge_automatically`, `hosted_invoice_url`, 실패 로그, `alignment_key`를 통한 중복 방지 로직 구상.
- 기존 schedule 연결 구독과 미납 인보이스 고객을 건너뛰는 안전장치 구상.
- `MODE=test/live`, `TARGET_SUB_IDS`, `CONFIRM`, `DRY_RUN` 등 실행 제어 옵션 논의.

### 중요한 상태/검증 주의사항

- 이 대화에는 **설계안과 코드 초안, SDK 업그레이드, 실행 오류**가 존재하지만, 운영 구독 전체에 성공적으로 적용되었다는 증거는 없다.
- 기존에 공유된 3-phase 코드가 Stripe API 14.3.0에서 실제로 정상 작동하는지, trial/phase 속성 및 invoice item 파라미터가 유효한지 **재검증 필요**.
- `DRY_RUN` 변수가 선언만 되고 실제 변경 요청을 막지 않는 초안이 있었으므로 실사용 전 반드시 수정해야 한다.
- 단순 `Invoice.list(limit=100)` 순회로는 모든 과거 invoice의 중복 청구를 보장할 수 없다. Stripe idempotency 및 영속적인 작업 상태 관리 검토 필요.
- 기존 `Invoice.pay()` 및 자동 청구 동작은 이중 결제 시도/실패 상태를 테스트 모드에서 확인해야 한다.
- 청구 시점과 서비스 제공 기간의 관계, trial로 인한 무료 기간, 호주 시간대 및 일광절약시간을 실제 정책과 대조해야 한다.

### 이력서 표현

- 적절: **Explored Stripe subscription billing-date alignment using subscription schedules, trial periods, and one-off invoices.**
- 구현·검증이 확인되면 강화 가능: **Developed a Python-based workflow to align subscription billing dates, incorporating duplicate-charge checks and payment-failure handling.**
- 확인 전 금지: **Successfully migrated all subscriptions to the 7th** 또는 **automated production billing for all customers**.

## 6. 주요 업무 D — 고객 로그인 링크, 이메일 및 결제 온보딩

### 코드에서 확인된 처리

- UISP Client Zone에 이메일/비밀번호로 로그인 요청.
- 반환된 `authenticationKey`를 사용해 `/crm/api/v1.0/client-zone/login-token` 호출.
- 반환된 URL을 대시보드 접근 링크로 사용할 수 있는 구조.
- UISP Stripe setup session을 만들고 결제 페이지로 이동하는 흐름.
- 가입 이후 Make.com 자동화 호출과 `sendWelcomeEmail()` 호출.

### 보안·운영 관점의 학습 포인트

- 비밀번호, 인증 키, 토큰 URL을 프론트 콘솔/백엔드 로그에 출력하지 않아야 함.
- 토큰 포함 링크의 만료, 일회성 여부, 이메일 오발송 위험 및 인증 정책 검증 필요.
- 백업 코드에는 `returnLoginURL`을 생성하지만 최종 반환값에는 포함하지 않고, 이메일 호출에도 URL을 직접 전달하지 않음. **실제 메일의 링크 전달 구현은 `email.jsw` 및 이메일 템플릿 확인 필요.**
- UISP에서 이미 존재하는 이메일을 발견했을 때 신규 가입을 막는 정책과, 기존 사용자에게 로그인 링크를 제공하는 정책은 분리되어야 함.

## 7. 기술 스택 및 도구

| 범주 | 기술/도구 | 경험 수준/근거 |
|---|---|---|
| 언어 | JavaScript | Wix 프론트 및 백엔드 코드 작업/검토 |
| 언어 | Python | Stripe 구독 청구일 변경 스크립트 작업/테스트 |
| 플랫폼 | Wix Velo | 페이지 코드, `.jsw` 백엔드, 로그, Secrets Manager |
| 고객 관리 | UISP CRM / Client Zone API | 고객 조회·생성·인증·토큰 API 연계 |
| 결제 | Stripe / Stripe Python SDK | 구독 스케줄·invoice 설계 및 코드 작업 |
| 자동화 | Make.com webhook | 가입 정보 전달 코드 |
| 통신 | REST API, HTTP, JSON | GET/POST, headers, Basic Auth, API token |
| 디버깅 | Browser DevTools, Wix Velo Logs, PowerShell | 504/401/timeout 진단 |
| 개발 환경 | Windows PowerShell, Python venv, pip | 실제 명령 및 업그레이드 기록 |
| 보안 | Secrets Manager, API key, login token | 인증 비밀정보 및 접근 제어 다룸 |

**주의:** Java, Spring Boot, SentinelOps 등은 다른 학습/팀 프로젝트 경험으로, 이 인턴십에서 수행한 기술 스택과 섞지 않는다.

## 8. 문제 해결 사례 (면접 STAR 준비용)

### 사례 1 — 신규 고객 가입 시 이메일 검증 타임아웃

- **Situation:** 신규 가입 과정에서 이메일 중복 검사 실패가 발생했고, Wix WebMethod 504/59초 타임아웃이 관찰됨.
- **Task:** 기존 UISP 고객의 중복 가입을 막으면서 신규 고객의 정상 가입을 지원할 수 있도록 원인을 조사.
- **Action:** Wix 프론트 오류 스택과 Velo 로그를 확인하고, Wix CRM 조회와 UISP active 조회를 구분해 기록. 8초 제한 진단을 적용하고 PowerShell을 이용한 HTTP 인증 테스트 절차를 확인.
- **Result:** UISP 활성 고객 조회가 8초 내 완료되지 않는 현상을 구체적인 로그로 확인. **근본 원인 및 최종 해결 여부는 미확인.**
- **배운 점:** 외부 시스템 호출의 관측성, 타임아웃 정책, 인증 오류와 네트워크 오류의 구분, 중복 가입 방지를 위한 fail-closed 설계.

### 사례 2 — 구독 청구일 통일 요구 분석

- **Situation:** 선불형 구독의 청구일을 매월 7일로 통일해야 하는 요구.
- **Task:** 이미 존재하는 Stripe 구독 기간을 고려하면서 과금 누락·중복을 방지할 방안 탐색.
- **Action:** Stripe Subscription Schedule, trial bridge, one-off invoice, 기존 Price 사용, 미납 건 스킵, 실패 시 hosted invoice URL 등을 고려한 Python 워크플로를 설계/검토.
- **Result:** 설계안과 코드 초안, SDK 업그레이드 및 실행 오류 확인. **운영 적용 결과는 미확인.**
- **배운 점:** 결제 시스템의 상태 전이, idempotency, proration, 안전한 마이그레이션 및 테스트 모드 검증의 중요성.

### 사례 3 — 다중 서비스 고객 온보딩

- **Situation:** Wix를 인증 통로로 사용하고 UISP에 고객정보를 유지하는 ISP 운영 구조.
- **Task:** 고객 등록부터 UISP 대시보드 접근, 결제 설정, 이메일 안내까지 서비스 간 연결.
- **Action:** Wix Velo signup 및 backend 코드, UISP CRM/Client Zone API, Stripe setup session, Make.com webhook, welcome email 호출 흐름을 다룸.
- **Result:** 관련 통합 코드 및 동작 구조 확인. **각 단계의 운영 배포·성공률은 추가 확인 필요.**
- **배운 점:** 외부 API 통합, 순서에 따른 데이터 불일치 위험, 인증 비밀정보 관리, 실패 시 복구 설계.

## 9. 이력서용 영문 초안 (검증 가능한 수준으로 작성)

### Experience

**Software Developer Intern** *(official title to confirm)*  
**Beam Broadband** *(verify legal employer name)* | Australia *(city/remote to confirm)*  
**Nov 2025 – Feb 2026** *(verify exact end date)*

- Worked with Wix Velo and UISP CRM APIs to support customer registration, account validation, and onboarding workflows for an internet service provider.
- Contributed to JavaScript-based integrations connecting Wix member registration, UISP customer management, Stripe payment setup, and Make.com automation.
- Investigated backend API timeout and authentication issues using browser developer tools, Wix application logs, and HTTP request testing.
- Reviewed customer dashboard access workflows involving UISP Client Zone authentication and token-based login links.
- Explored Python and Stripe SDK approaches for aligning recurring subscription billing dates while considering invoice handling and duplicate-charge risks.

**위 bullet은 보수적 초안.** 실제로 독립 구현·배포한 기능이 확인되면 `Worked with`, `Contributed to`, `Explored`를 `Developed`, `Implemented`, `Delivered` 등으로 강화할 수 있다.

### LinkedIn용 짧은 소개

> Supported customer onboarding and billing integration workflows involving Wix Velo, UISP CRM, Stripe, and Make.com during an IT internship. Gained hands-on experience with REST API integration, authentication workflows, application logging, and troubleshooting backend request timeouts.

### ATS 키워드 후보

`JavaScript`, `Python`, `REST API`, `API Integration`, `Backend Development`, `Wix Velo`, `UISP CRM`, `Stripe`, `HTTP`, `JSON`, `Authentication`, `Debugging`, `Error Handling`, `Logging`, `Customer Onboarding`, `Payment Integration`, `Workflow Automation`.

## 10. 추가 확인 질문 (다음 세션에서 이력서 최종화 전)

1. 공식 회사명, 직함, 근무지(도시/원격 여부), 인턴십 종료일은 무엇인가?
2. Wix–UISP 회원가입 플로우 중 직접 구현·수정하여 **실제로 배포한 기능**은 어디까지인가?
3. 504 문제의 최종 원인은 밝혀졌는가? 해결했다면 변경사항과 결과는?
4. `uisp.jsw`의 서비스 생성 기능은 다른 코드/Make.com 시나리오에서 처리했는가?
5. 이메일의 로그인 토큰 링크는 `email.jsw` 또는 템플릿에서 어떻게 전달되었는가?
6. Stripe 청구일 변경 스크립트는 test/live 중 어디에서 성공적으로 실행했는가? 실제 변경된 구독 수는?
7. Make.com 연동의 실제 결과(서비스 생성, 계약 생성, 이메일 등)는 무엇인가?
8. 멘토 Dustin의 피드백이나 측정 가능한 개선 성과가 있었는가?
9. 코드 저장소, 커밋, 테스트 로그, 배포 기록 또는 스크린샷 등 검증 자료가 있는가?
10. 외부에 공개할 수 없는 고객정보·회사 내부 URL·API 구성은 무엇인가?

## 11. 다음 ChatGPT 세션에 전달할 프롬프트

```text
첨부한 Beam_Broadband_Internship_Handover_2026.md는 내 Professional Year 인턴십 경험 인수인계서야.
이 문서만을 근거로 호주 IT 취업용 이력서의 Work Experience를 작성해줘.
내 목표는 Backend Developer / Software Developer / API Integration 관련 직무야.
실제로 수행한 업무와 아직 확인되지 않은 제안·디버깅·미완료 작업을 구분하고,
성과를 임의로 만들어내지 말아줘. 필요하면 먼저 핵심 확인 질문을 해줘.
이력서는 영어로, 설명과 피드백은 한국어로 작성해줘.
```

## 12. 기록 보존 및 보안

- 이 파일에는 **실제 API 키, 비밀번호, 세션 토큰, 고객의 실명/이메일, 토큰 포함 URL**을 저장하지 않았다.
- 프로젝트의 실제 소스코드, 이메일 템플릿, Wix Logs, Stripe 테스트 결과가 있다면 별도 안전한 저장소에 보관할 것.
- 공개 이력서/LinkedIn에는 내부 API 도메인, 비밀정보, 실제 고객 데이터를 노출하지 말 것.
- 세션 삭제 후에는 이 문서를 새 대화에 업로드하면 된다. 다만 이 문서는 전체 원본 코드나 모든 로그를 완벽히 대체하지는 않는다.

---

*작성일: 2026-11-13 · 출처: 사용자 제공 인턴십 관련 대화 및 코드 · 문서 목적: 취업 서류용 사실 기반 인수인계*
