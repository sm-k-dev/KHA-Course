# 보안 프레임워크: Spring Security, RBAC, OWASP

> 중요도: **A** · 이 장의 목표: 인증과 인가를 구별하고, 역할 기반 접근 제어와 소유자 검사를 구현하고, OWASP 상위 위험을 예로 설명한다.

> 2026-11-23 추가 장 (로드맵 M08 항목). RBAC 권한 검사 로직은 `Batch2.java`에서 실행해 11개 검증이 통과했다. **Spring Security 설정 코드는 실행 환경이 없어서 미검증이다.**

---

## 1. 인증과 인가 (복습)

- **인증 (Authentication):** 너는 누구냐. 로그인, 토큰 확인.
- **인가 (Authorization):** 너는 이것을 해도 되느냐. 권한 확인.

인증에 성공해도 인가는 따로 검사해야 한다. 로그인만 했다고 모든 기능을 쓸 수 있으면 안 된다.

---

## 2. Spring Security의 구조 (개념)

**정의** — Spring Security는 요청이 Controller에 닿기 전에 필터 체인에서 인증과 인가를 검사하는 보안 프레임워크다.

**핵심 구성**
- **필터 체인:** 요청마다 인증 필터를 지나간다. 인증 정보는 `SecurityContext`에 저장된다.
- **인증 제공자 (AuthenticationProvider):** 아이디와 비밀번호를 확인한다. 비밀번호는 `PasswordEncoder`(예: bcrypt)로 비교한다.
- **인가 규칙:** 어떤 URL에 어떤 역할이 필요한지 정한다.

**설정 예 (미검증 — 실행 환경 없음)**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").hasRole("ADMIN")   // 관리자만
                .requestMatchers("/api/**").authenticated()          // 나머지 API는 로그인 필요
                .anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
```

- **주의:** 위 설정만으로 "다른 사람의 글을 읽지 못하게" 되지는 않는다. URL 단위 규칙은 역할만 확인한다. 소유자 검사는 서비스 계층에서 따로 해야 한다(아래 3절).

---

## 3. RBAC와 소유자 검사

**정의** — RBAC(Role-Based Access Control)는 사용자에게 역할을 주고, 역할에 권한을 묶어 접근을 정하는 방식이다.

**구분해야 할 것**
- **역할 기반 검사:** 관리자인가? 대부분의 기능에 쓴다.
- **소유자 검사 (IDOR 방지):** 이 글의 주인이 나인가? 번호만 바꿔서 남의 자원을 보는 공격을 막는다.

**구현 (검증 완료)**

```java
enum Role { USER, ADMIN }
enum Action { READ_OWN, READ_ANY, DELETE_ANY }
static final Map<Role, Set<Action>> PERMISSIONS = Map.of(
    Role.USER, EnumSet.of(Action.READ_OWN),
    Role.ADMIN, EnumSet.of(Action.READ_OWN, Action.READ_ANY, Action.DELETE_ANY)
);

static boolean canRead(User user, Post post) {
    if (user == null) return false;                                              // 인증 안 됨 (401)
    if (PERMISSIONS.get(user.role()).contains(Action.READ_ANY)) return true;     // 관리자
    return PERMISSIONS.get(user.role()).contains(Action.READ_OWN)
            && post.ownerId() == user.id();                                      // 소유자 검사 (IDOR 방지)
}

static boolean canDelete(User user, Post post) {
    if (user == null) return false;
    return PERMISSIONS.get(user.role()).contains(Action.DELETE_ANY);             // 일반 사용자는 false (403)
}
```

- **확인 (11개 중 RBAC 6개 통과):** 인증되지 않은 사용자는 읽을 수 없고, 일반 사용자는 자기 글만 읽고, 관리자는 모든 글을 읽고 지울 수 있다. 일반 사용자는 삭제할 수 없다.
- **401과 403:** 401은 "누구인지 모른다"(인증 실패), 403은 "알지만 권한이 없다"(인가 실패)다.

**면접 기준:** "RBAC는 역할에 권한을 묶는 방식입니다. 역할 검사만으로는 남의 자원 접근을 막지 못하니, 서비스에서 소유자 검사를 함께 합니다."

---

## 4. 비밀번호 저장과 인증 토큰

- **비밀번호:** bcrypt나 argon2로 해시해서 저장한다. 평문이나 단순 SHA-256/MD5로 저장하지 않는다. (자세한 구분은 29장 암호화 실무 참고)
- **세션 vs JWT:** 세션은 서버가 상태를 가지고, JWT는 토큰에 정보를 담아 서버가 상태를 갖지 않는다. JWT는 탈취되면 만료 전까지 쓸 수 있으므로 만료 시간을 짧게 두고, 갱신 토큰(Refresh Token)을 따로 쓴다.
- **CSRF:** 쿠키 기반 세션에서 외부 사이트가 사용자의 요청을 위조하는 공격이다. 상태를 바꾸는 요청에 CSRF 토큰을 검사한다. 토큰을 헤더로 주고받는 API에서는 위험이 달라진다.

---

## 5. OWASP Top 10에서 자주 보는 것

| 위험 | 예 | 방어 |
|---|---|---|
| 접근 통제 실패 | 번호만 바꿔 남의 글 조회 | 소유자 검사, 서버에서 매번 확인 |
| 암호화 실패 | 비밀번호 평문 저장 | bcrypt, 전송은 HTTPS |
| 인젝션 | SQL에 입력을 직접 이어 붙임 | PreparedStatement, 파라미터 바인딩 |
| 보안 설정 오류 | 기본 계정, 디버그 정보 노출 | 설정 점검, 오류 응답 최소화 |
| 취약한 구성 요소 | 오래된 라이브러리 | 의존성 점검, 업데이트 |

---

## 6. 실습 과제

1. `/api/admin/**`에 ADMIN만 접근하도록 설정하고, USER 계정으로 403이 나오는지 확인한다(미검증 항목).
2. 소유자 검사를 빼고 남의 글 번호로 요청해 보고, 그다음 검사를 넣어 막는다.

---

## 단원 통과 기준

- [ ] 인증과 인가의 차이를 예로 설명할 수 있다.
- [ ] 401과 403을 상황에 맞게 구별할 수 있다.
- [ ] 역할 검사만으로 부족한 이유(IDOR)를 설명하고 소유자 검사를 구현할 수 있다.
- [ ] 비밀번호 저장 방식과 JWT의 한계를 말할 수 있다.

## 7. 보안 아키텍처 (설계 관점)

- **심층 방어 (Defense in Depth):** 한 겹이 뚫려도 다음 겹이 막도록 여러 겹을 둔다. 네트워크 차단, 인증, 인가, 입력 검증, 암호화, 로그를 각각 둔다.
- **신뢰 경계:** 시스템 안에서 신뢰하는 영역과 신뢰하지 않는 영역의 경계를 그린다. 사용자 입력과 외부 응답은 경계 밖에 있다고 보고 검사한다.
- **최소 권한:** 서비스와 사용자에게 필요한 권한만 준다. DB 계정도 읽기만 필요한 서비스에는 읽기 권한만 준다.
- **설계 질문:** 이 데이터는 어디서 들어오고, 어디를 거쳐, 어디에 저장되며, 누가 볼 수 있는가? 각 구간마다 검사가 있는가?
- **면접 기준:** "신뢰 경계를 그리고 각 경계에 검사를 두며, 최소 권한과 심층 방어로 한 겹이 뚫려도 피해를 제한하도록 설계합니다."
