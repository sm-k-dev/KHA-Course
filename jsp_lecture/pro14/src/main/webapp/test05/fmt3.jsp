<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- JSTL 중에서 core 태그들을 사용하기 위해 외부 주소로 요청 --%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- JSTL 중에서 fomatting 태그들을 사용하기 위해 외부 주소로 요청 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>    
<% request.setCharacterEncoding("UTF-8"); %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h4>로케일 설정</h4>
	
	<%--
		java.util 패키지에서 제공하는 Date클래스의 기본생성자를 호출해 객체를 생성해서 today변수에 저장한다.
		참고. Date 클래스의 기본생성자로 객체를 생성하면
			오늘 날짜와 시간값을 가지는 Date 클래스의 객체가 만들어 진다.
	--%>
	<c:set var="today" value="<%= new java.util.Date() %>" />
	한글로 설정: <fmt:setLocale value="ko_kr" />
	<fmt:formatNumber value="10000" type="currency" />
	<fmt:formatDate value="${today}" />
	<br>
	
	일어로 설정: <fmt:setLocale value="ja_JP" />
	<fmt:formatNumber value="10000" type="currency" />
	<fmt:formatDate value="${today}" />
	<br>
	
	영어로 설정: <fmt:setLocale value="en_US" />
	<fmt:formatNumber value="10000" type="currency" />
	<fmt:formatDate value="${today}" />
	<br>
</body>
</html>