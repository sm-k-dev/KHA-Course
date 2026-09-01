<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
	c:set 태그를 이용해 변수를 선언하고 클라이언트가 login.jsp를 요청한 전제 URL 중에서 컨텍스트 주소를 얻어
	pageContext 내장 객체의 컨텍스트 이름을 변수 contextPath에 미리 설정
	
	방법: pageContext 내장객체의 request 변수를 호출하면 HttpServletRequest 객체 주소를 얻을 수 있다.
		그런 다음 HttpServletRequest 객체의 contextPath변수를 호출하면
		클라이언트가 login.jsp 를 최초로 요청한 전체 URL 중에서 컨텍스트 주소 ("/pro14")를 얻어 올 수 있다.
		
		URL: http://localhost:8181/pro14/test03/login.jsp
		컨텍스트주소: /pro14
--%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 창</title>
</head>
<body>
	<form action="result.jsp">
		아이디: <input type="text" size=20 /><br>
		비밀번호: <input type="password" size=20 /><br>
		<input type="submit" value="로그인" /> <input type="reset" value="다시입력" />
	</form>
	<br><br>
	<%--
		먼저 JSP 에서 <a> 태그를 이용해 다른 페이지로 이동하는 방법
		지금까지는 표현언어로 pageContext.request.contextPath 같은 긴 속성을 그대로 사용 했는데,
		<c:set>태그를 이용하면 긴 이름의 속성이나 변수를 줄여서 사용 할 수 있다.
	--%>
	<%--
		<a href="${pageContext.request.contextPath}/memberForm.html">회원등록하기</a>
	--%>
	<%--
		현재 로그인창에서 회원 가입 창으로 이동할 때 미리 c:set 태그를 이용해
		pageContext.request.contextPath 속성 이름을 contextPath로 줄여서 사용할 수 있다.
		복잡한 웹 페이지에서 속성 이름을 짧게 줄이면 코드의 가독성이 좋아진다.
	--%>
	<a href="${contextPath}/test03/memberForm.html">회원등록하기</a>
	<%
		/*
			자바코드를 작성해서 String contextPath2 변수를 하나 선언하고,
			클라이언트가 login.jsp를 요청한 전체 URL중에서 컨텍스트 주소("/pro14")를 반환받아 얻을 수도 있다.
		*/
		String contextPath2 = request.getContextPath();
	%>
	<a href="<%=contextPath2%>/test03/memberForm.html">회원등록하기</a>
</body>
</html>