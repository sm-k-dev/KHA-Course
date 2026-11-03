### 로그인 (스프링 시큐리티)

1. login.html에서 이메일(`name="username"`)과 비밀번호(`name="password"`)를 POST /login 으로 전송
2. DaoAuthenticationProvider 가 요청을 받음
3. `UserDetailService.loadUserByUsername(이메일)` 호출 → `UserRepository.findByEmail()` 로 회원 조회
4. DaoAuthenticationProvider 객체가 조회된 `User` 객체 (UserDetails 구현)의 `getPassword()`값과 입력 비밀번호를 `PasswordEncoder.matches()`로 비교
5. 성공 시 `/articles`로 이동, 실패 시 로그인 화면

### 회원 가입

1. signup.html 의 폼 값 (email, password)이 `AddUserRequest`에 채워져 POST /user 로 전달
2. `UserSerivce.save()`에서 