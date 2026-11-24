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

### 2-1. 보안 필터는 컨트롤러보다 먼저 응답한다 (2026-11-23 보강, Spring Security 7.0.9에서 확인함)

Spring Security 필터는 `DispatcherServlet`보다 바깥의 서블릿 필터다. 그래서 인증이나 권한이 부족하면 컨트롤러가 실행되기 전에 응답이 끝난다. 20장 6-2에서 확인한 필터 순서와 같은 구조다.

```
요청 → Security 필터 체인 → (인증 실패: 401 응답 끝)
                         → (권한 부족: 403 응답 끝)
                         → DispatcherServlet → Interceptor → Controller
```

- **401 Unauthorized**: 누구인지 확인할 수 없다(로그인 필요, 인증 정보 없음).
- **403 Forbidden**: 누구인지는 알지만 권한이 없다.

**확인 결과 (실행함)**: `SecurityFilterChain`에 `/public/**`은 허용, `/admin/**`은 `ADMIN`만, 나머지는 로그인 필요를 설정하고 MockMvc로 요청했다. `httpBasic`을 켰다. 실행 환경은 Spring Security 7.0.9와 Spring Framework 7.0.9다.

```
public, no login -> 200
admin page, no login -> 401   ← 인증 정보가 없다
admin page, USER login -> 403  ← 로그인은 했지만 권한이 없다
admin page, ADMIN login -> 200
me, no login -> 401
me, USER login -> 200
```

컨트롤러 메서드는 401과 403 응답에서 실행되지 않았다. 필터가 먼저 막았다.

**면접 기준**: "Security 필터 체인은 컨트롤러 전에 인증과 인가를 검사하므로, 실패 요청은 컨트롤러에 도달하지 않고 401 또는 403으로 끝납니다."

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

### 3-1. JWT: 구조와 서명 검증을 직접 해 보기 (2026-11-23 보강, 확인함)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**정의**: JWT는 `헤더.페이로드.서명` 세 부분을 Base64URL로 이은 문자열이다. 서버가 비밀 키로 서명하므로, 클라이언트가 페이로드를 바꾸면 서명이 맞지 않는다.

```java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtCheck {
    static final byte[] SECRET = "demo-secret-key-at-least-32-bytes-long!!".getBytes(StandardCharsets.UTF_8);
    static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    static final Base64.Decoder B64D = Base64.getUrlDecoder();

    static String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET, "HmacSHA256"));
        return B64.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    static String issue(String payloadJson) throws Exception {
        String header = B64.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = B64.encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String data = header + "." + payload;
        return data + "." + sign(data);
    }

    // 서명 검증: 헤더.페이로드를 다시 서명해서 같은지 비교한다
    static boolean verify(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) return false;
        String expected = sign(parts[0] + "." + parts[1]);
        return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws Exception {
        String token = issue("{\"sub\":\"user1\",\"role\":\"USER\"}");
        System.out.println("parts=" + token.split("\\.").length);
        System.out.println("valid=" + verify(token));

        String[] p = token.split("\\.");
        String forgedPayload = B64.encodeToString("{\"sub\":\"user1\",\"role\":\"ADMIN\"}".getBytes(StandardCharsets.UTF_8));
        String forged = p[0] + "." + forgedPayload + "." + p[2];
        System.out.println("forged role valid=" + verify(forged));

        System.out.println("payload=" + new String(B64D.decode(p[1]), StandardCharsets.UTF_8));
    }
}
```

**실행 결과 (확인함)**

```
parts=3
valid=true
forged role valid=false
payload={"sub":"user1","role":"USER"}
```

**읽는 법**
- 서명이 맞으면 페이로드는 위조되지 않았다. 역할을 `ADMIN`으로 바꾸면 서명이 달라져 검증에 실패했다.
- **페이로드는 암호화가 아니다.** Base64URL로 인코딩만 된 것이라 누구나 읽을 수 있다. 비밀번호나 주민번호 같은 값을 넣지 않는다.
- 이 검증에는 **만료 시간(`exp`) 확인이 없다.** 실제 라이브러리는 `exp`와 `alg` 값도 검사한다. 직접 만들면 이 검사를 빼먹기 쉽다.
- 로그아웃은 JWT를 서버에서 지우기 어렵다. 그래서 짧은 만료 시간과 재발급(refresh token) 방식을 함께 쓴다.

**면접 기준**: "JWT는 헤더와 페이로드를 비밀 키로 서명한 토큰입니다. 서명이 맞으면 위조가 아니지만, 페이로드는 읽을 수 있으므로 민감 정보를 넣지 않고 만료 시간을 검사합니다."

### 3-2. 로그인·로그아웃 요청의 흐름 (Spring Boot 4.1.1 서버에서 확인함, 2026-11-24)

**참고**: 원본 자료와 출처, 확인 범위는 [부록 92](92_선별근거_원본연결.md)의 이 장 항목을 본다.

**확인 범위**: 강의의 SBB 프로젝트 전체가 아니라, 같은 흐름(`formLogin`, `logout`, CSRF 토큰, 세션 쿠키)을 최소 구성으로 만든 Boot 4.1.1 서버(`spring-boot-starter-security` 포함, 8091 포트)에서 HTTP 요청으로 확인했다. 브라우저 화면 조작은 하지 않았다. 계정(`kim`)은 실습용 임시 값이다.

| 순서 | 요청 | 결과 (확인함) |
|---|---|---|
| 1 | `GET /public` (로그인 없음) | 200 |
| 2 | `GET /me` (로그인 없음) | 302, `/login`으로 이동 |
| 3 | `POST /login` (잘못된 비밀번호, CSRF 포함) | 302, `/login?error` |
| 4 | `POST /login` (올바른 비밀번호, CSRF 포함) | 302, `/me`로 이동. 세션 쿠키 발급 |
| 5 | `GET /me` (세션 쿠키 포함) | 200, `로그인 사용자: kim` |
| 6 | `POST /logout` (유효한 CSRF 토큰) | 302, `/login?logout` |
| 7 | `GET /me` (로그아웃 후) | 302, `/login` |
| 8 | `GET /public` (로그아웃 후) | 200 |

**주의 (확인함):**
- 로그인에 성공하면 CSRF 토큰이 바뀐다. 로그인 전의 토큰을 로그아웃에 쓰면 **403**이 나온다. 로그아웃에는 현재 세션의 토큰을 써야 한다.
- 로그아웃은 `POST`만 받는다. 주소창에 `GET /logout`을 입력하는 방식은 막혀 있다.
- 세션 쿠키가 없으면 보호된 주소는 모두 로그인 화면으로 보낸다.

**면접 기준:** "폼 로그인은 CSRF 토큰을 확인하고 성공하면 세션을 만든다. 로그아웃은 `POST`와 현재 토큰으로만 세션을 지우고, 보호된 페이지는 인증 여부를 매 요청마다 확인한다."

**서버 시작 때 한 번**: `SecurityConfig.filterChain()`이 호출되어 보안 필터 체인을 등록한다. `formLogin`은 로그인 화면 주소와 성공 후 이동 주소를, `logout`은 로그아웃 주소와 세션 삭제 여부를 등록한다. `passwordEncoder()`와 `authenticationManager()`도 이때 객체가 된다.

**로그인 요청 (`POST /user/login`)**
1. 브라우저가 아이디와 비밀번호를 보낸다.
2. Security 필터(`UsernamePasswordAuthenticationFilter`)가 가로채서 `AuthenticationManager`에 넘긴다. 컨트롤러 메서드가 아니다.
3. 인증 관리자는 `UserDetailsService`(여기서는 `UserSecurityService`)의 `loadUserByUsername()`으로 DB에서 사용자를 찾는다.
4. `PasswordEncoder`가 입력 비밀번호와 저장된 해시를 비교한다.
5. 성공하면 인증 정보를 세션에 저장하고, 설정된 주소로 이동한다.

**로그인 후 화면**: 템플릿의 `sec:authorize="isAuthenticated()"`가 참이 되어 `[로그아웃]` 링크가 보인다. 세션에 인증 정보가 있기 때문이다.

**로그아웃 요청 (`POST /user/logout`)**: `LogoutFilter`가 처리하고, `invalidateHttpSession(true)`가 세션을 지운다. 세션 안의 로그인 정보도 함께 사라진다.

**자주 헷갈리는 점**
- 로그인 `POST`를 `UserController`에 만들면 안 된다. 보안 필터가 먼저 가로채므로 컨트롤러의 메서드는 실행되지 않는다. 자료의 퀴즈 1번이 이 점을 묻는다.
- `loadUserByUsername()`은 컨트롤러가 호출하지 않는다. `AuthenticationManager`가 호출한다.

**면접 기준**: "로그인 POST는 Security 필터가 가로채서 AuthenticationManager가 UserDetailsService로 사용자를 찾고 PasswordEncoder로 비밀번호를 검사합니다. 성공하면 세션에 인증 정보를 저장합니다."

## 4. 비밀번호 저장과 인증 토큰

- **비밀번호:** bcrypt나 argon2로 해시해서 저장한다. 평문이나 단순 SHA-256/MD5로 저장하지 않는다. (자세한 구분은 30장 암호화 실무 참고)
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
