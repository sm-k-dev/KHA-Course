서블릿은 자바코드에 HTML, CSS, 자바스크립트 코드를 쓰는 것과 반대로
JSP (Java Server Page) 는 HTML, CSS와 자바스크립트에 자바코드를 쓰는 것

톰캣 컨테이너에서 JSP 변환 과정
    1. 변환 단계 (Translation Step): 컨테이너는 JSP 파일을 자바 파일로 변환 (이것이 서블릿) hello.jsp (이 파일에서 자바 코드만 빼내어서) → hello_jsp.java(변환된 이 파일이 서블릿)
    2. 컴파일 단계 (Compile Step): hello_jsp.java → hello_jsp.class
    3. 실행 단계 (Interpret Step): hello_jsp.class + HTML + CSS 실행

.jsp 파일엔 (서블릿의 형님, 톰캣서버(WAS)에 요청해야 한다)
    - HTML CSS JS jQuery
    - JAVA 기본문법
    을 작성 할 수 있다.

    C:\워크스페이스\.metadata\.plugins\org.eclipse.wst.server.core\tmp0 => 복제당한 tomcat 서버 폴더

driver 파일은 class 파일이랑 dbms를 연결해 주는 역할을 한다

jsp 내장객체 (내장변수) - request(HttpServletRequest), response, session(HttpSession), application(SevletContext), pageContext (ServletConfig)

VO = Bean

c:set
c:if
c:forEach
c:choose

fmt:formatDate
fmt:forNumber

fn:length()
fn:substring()
fn:split()
fn:contain()