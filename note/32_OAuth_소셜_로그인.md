# OAuth 2.0과 소셜 로그인: 위임, 인가 코드, PKCE

> 중요도: **A** · 이 장의 목표: OAuth 2.0의 네 역할과 인가 코드 흐름을 설명하고, PKCE 계산을 직접 검증하며, 소셜 로그인에서 생기는 보안 실수를 막는다.

> 2026-11-23 추가 장 (로드맵 M08 "Spring Security & OAuth" 항목). 이 장 3절의 PKCE 계산 코드를 실행해 RFC 7636 예제 값과 일치함을 확인했다. **Spring Security의 `oauth2Login` 설정과 실제 카카오·구글 연동은 실행하지 않았다.** 해당 부분은 설정 구조만 설명한다.

---

## 1. OAuth 2.0은 무엇을 위임하는가

**정의** — OAuth 2.0은 **사용자의 비밀번호를 넘기지 않고**, 제3자 앱에 제한된 권한(scope)만 빌려주는 **인가(authorization) 프로토콜**이다.

**인증과 인가의 차이**

| 구분 | 질문 | 예시 |
|---|---|---|
| 인증 (Authentication) | 너는 누구인가? | 로그인 |
| 인가 (Authorization) | 너는 무엇을 해도 되는가? | 사진 목록 읽기 권한 |

OAuth 2.0 자체는 인가 프로토콜이다. 사용자가 누구인지 알려주는 **인증**은 그 위에 **OpenID Connect(OIDC)**를 얹어서 한다. 소셜 로그인이라고 부르는 것 대부분은 OAuth 2.0 + OIDC다.

### 1.1 네 역할

| 역할 | 뜻 | 예시 |
|---|---|---|
| Resource Owner | 데이터의 주인 (사용자) | 나 |
| Client | 사용자 대신 데이터를 요청하는 앱 | 우리 서비스 |
| Authorization Server | 로그인과 동의를 처리하고 토큰을 발급 | 카카오, 구글 |
| Resource Server | 토큰을 검사하고 데이터를 제공 | 카카오 사용자 정보 API |

---

## 2. 인가 코드 흐름 (Authorization Code Flow)

서버가 있는 웹 앱에서 쓰는 표준 흐름이다.

```
사용자 브라우저          우리 서버(Client)            인가 서버
     | 1. 로그인 클릭         |                         |
     |----------------------->| 2. 리다이렉트 (client_id, redirect_uri, scope, state, code_challenge)
     |<-----------------------|------------------------>|
     | 3. 로그인·동의 화면 (인가 서버 도메인)           |
     |------------------------------------------------->|
     | 4. redirect_uri?code=...&state=...               |
     |----------------------->| 5. code + code_verifier + client_secret 으로 토큰 요청
     |                        |------------------------>|
     |                        | 6. access_token (+ id_token)
     |                        |<------------------------|
     | 7. 로그인 세션 발급     |                         |
```

**핵심 원칙**

- 토큰은 **브라우저 주소창에 나오지 않는다.** 주소창에는 짧은 일회용 **인가 코드**만 나온다.
- 코드를 토큰으로 바꾸는 단계는 **서버끼리** 통신한다.
- `state`는 CSRF 방지용 난수다. 시작할 때 만든 값과 돌아온 값이 같아야 한다.

---

## 3. PKCE: 코드 가로채기를 막는다

**왜 필요한가** — 모바일 앱이나 SPA는 `client_secret`을 안전하게 숨길 수 없다. 공격자가 인가 코드를 가로채면 토큰을 얻을 수 있다.

**PKCE(Proof Key for Code Exchange, RFC 7636)**는 다음과 같이 동작한다.

1. 클라이언트가 무작위 `code_verifier`를 만든다.
2. `code_challenge = BASE64URL(SHA-256(code_verifier))`를 계산해 인가 요청에 보낸다.
3. 토큰 요청에는 원본 `code_verifier`를 보낸다.
4. 인가 서버는 원본을 다시 해시해서 처음 받은 challenge와 비교한다.

가로챈 코드만으로는 verifier를 알 수 없어서 토큰을 받을 수 없다.

### 3.1 검증 코드

RFC 7636 부록 B의 예제 값으로 계산을 확인한다.

```java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PkceCheck {
    static String challenge(String verifier) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(verifier.getBytes(StandardCharsets.US_ASCII));
        // PKCE는 패딩(=)이 없는 URL-safe Base64를 쓴다
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    public static void main(String[] args) throws Exception {
        String verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
        String expected = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM";
        String actual = challenge(verifier);
        System.out.println("challenge = " + actual);
        System.out.println("일치: " + actual.equals(expected));
    }
}
```

실행 결과: `일치: true`

**주의**: 표준 Base64를 쓰면 `+`, `/`, `=`가 섞여 URL에서 깨진다. `getUrlEncoder().withoutPadding()`을 써야 한다.

---

## 4. 스프링 시큐리티 설정 구조 (미실행)

Spring Boot 4 기준 소셜 로그인은 `oauth2Login()`으로 연결한다. 아래는 구조 설명용이며 실제 키와 도메인이 필요하다.

```yaml
# application.yml (값은 환경 변수로 주입한다. 파일에 직접 쓰지 않는다)
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: openid, profile, email
```

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**").permitAll()
                        .anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
```

**redirect URI는 정확히 일치해야 한다.** 스프링 기본값은 `{baseUrl}/login/oauth2/code/{registrationId}`이다. 개발용 `localhost`와 운영 도메인을 콘솔에 둘 다 등록한다.

---

## 5. 소셜 로그인에서 흔한 실수

| 실수 | 결과 | 막는 방법 |
|---|---|---|
| `state` 검증 생략 | CSRF로 공격자 계정에 연결 | 요청마다 난수 `state`를 만들고 콜백에서 비교 |
| `client_secret`을 프런트에 노출 | 앱 위장 | 비밀 값은 서버 환경 변수에만 둔다 |
| access token을 로컬 스토리지에 저장 | XSS로 탈취 | HttpOnly 쿠키 세션 사용 |
| 이메일만으로 계정 연결 | 다른 제공자의 같은 이메일로 계정 탈취 | 제공자와 제공자 사용자 ID(`sub`) 조합으로 식별 |
| scope를 필요 이상으로 요청 | 동의 거부, 과도한 권한 | 필요한 scope만 요청 |

---

## 6. 로그인과 권한을 나눈다

소셜 로그인은 **사용자가 누구인지**(인증)를 해결한다. 그 사용자가 무엇을 할 수 있는지(인가, RBAC)는 우리 서비스가 정한다. 제공자에게서 받은 `ROLE_*` 값을 그대로 권한으로 쓰지 말고, 내부 권한 테이블에서 다시 계산한다. 자세한 RBAC 설계는 31장을 본다.

---

## 7. 실습 과제

1. `PkceCheck`를 직접 실행하고 `일치: true`가 나오는지 확인한다.
2. `verifier`를 한 글자만 바꿔 실행해 challenge가 어떻게 달라지는지 본다.
3. 카카오 또는 구글 개발자 콘솔에서 테스트 앱을 만들고, redirect URI가 어떻게 등록되는지 적어 본다. (실제 키는 노트에 적지 않는다.)

---

## 토큰 만료 후 재발급과 재요청 (2026-11-23 보강, Node로 확인함)

**출처**: 선생님 자료 `springboot4☆/11장_OAuth2로 로그인_로그아웃 구현하기.html`의 "화면 자바스크립트: 토큰 재발급과 요청 함수" 절. 액세스 토큰은 짧게 두고, 만료되면 리프레시 토큰으로 새 액세스 토큰을 받아 같은 요청을 한 번 더 보내는 흐름이다.

**흐름**
1. 액세스 토큰으로 API를 호출한다.
2. 서버가 만료된 토큰을 보고 **401**을 돌려준다.
3. 클라이언트는 리프레시 토큰이 있는지 확인한다. 있으면 `/api/token`에 요청해 새 액세스 토큰을 받는다.
4. 저장된 토큰을 새 값으로 바꾸고, **같은 요청을 한 번 더** 보낸다.
5. 재발급도 실패하면 로그인 화면으로 보낸다. 리프레시 토큰이 없으면 401을 그대로 돌려준다.

```javascript
// 클라이언트 (요약): 401이면 재발급 후 한 번만 재요청한다
async function httpRequest(url, options = {}, allowRetry = true) {
  const res = await fetch(base + url, {
    ...options,
    headers: { ...(options.headers || {}), Authorization: `Bearer ${accessToken}`, Cookie: cookies },
  });
  if (res.status === 401 && allowRetry && cookies.includes('refresh_token=')) {
    const r = await fetch(base + '/api/token', { method: 'POST', headers: { Cookie: cookies } });
    if (r.status === 200) {
      accessToken = (await r.json()).accessToken;   // 저장값을 새 액세스 토큰으로 교체
      return httpRequest(url, options, false);      // 재발급 후 같은 요청을 한 번 더
    }
  }
  return res;
}
```

서버 쪽은 `/api/token`이 리프레시 토큰을 확인하고 새 액세스 토큰을 돌려준다. `/api/**` 요청에 인증이 없으면 화면으로 이동시키지 않고 401을 준다(자료의 WebOAuthSecurityConfig).

**확인 결과 (Node 목 서버로 확인함)**: 만료 토큰으로 첫 호출 → 401 → 재발급 1회 → 같은 요청 200. 리프레시 쿠키가 없을 때는 재발급 없이 401이 그대로 나왔다.

```
first call status: 200 | reissued: 1 | token now: valid-2
body: [{"id":1,"title":"hello"}]
no refresh cookie status: 401 | reissued: 1
```

**주의: HttpOnly 쿠키는 JavaScript로 읽을 수 없다**
자료의 화면 코드는 `getCookie("refresh_token")`으로 쿠키를 읽어 `/api/token`에 넘긴다. 그런데 리프레시 토큰을 `HttpOnly`로 설정하면 JavaScript에서 쿠키 값을 읽을 수 없다. 이것이 자료 코드의 맹점이다. 이 방식이라면 쿠키를 `HttpOnly`로 두지 않거나, 재발급 요청을 `credentials: 'include'`로 보내서 브라우저가 쿠키를 자동으로 붙이게 한다. 보안을 위해서는 후자가 낫다. 반면 액세스 토큰을 로컬 스토리지에 두면 XSS로 탈취될 수 있다(위 "흔한 실수" 표).

**면접 기준**: "액세스 토큰은 짧게 두고, 401을 받으면 리프레시 토큰으로 재발급해서 같은 요청을 한 번만 다시 보냅니다. 리프레시 토큰은 HttpOnly 쿠키로 두고 JavaScript가 읽지 못하게 합니다."

## 단원 통과 기준

- [ ] OAuth 2.0의 네 역할을 예시와 함께 설명할 수 있다.
- [ ] 인가 코드가 토큰과 다른 이유를 설명할 수 있다.
- [ ] PKCE가 어떤 공격을 막는지 설명하고 `code_challenge` 계산을 손으로 쓸 수 있다.
- [ ] `state`의 역할과 검증 방법을 설명할 수 있다.
- [ ] 소셜 로그인 사용자를 이메일이 아니라 `sub`로 식별해야 하는 이유를 설명할 수 있다.
