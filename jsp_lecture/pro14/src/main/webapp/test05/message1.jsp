<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"%>
<%--
	JSTL 전체 라이브러리에 속한 core, fmt 라이브러리 태그들을 사용하기 위해 요청 주소 작성
	fmt:setLocale - locale(언어) 지정
	fmt:bundle - resource 패키지의 프로퍼티 파일을 읽어온다.
	message - 프로퍼티 파일에 작성한 키(key)에 대한 값을 각각 출력한다.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- formatting 태그 주소 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL 다국어 기능</title>
</head>
<body>
	<%-- fmt:setLocale 태그를 이용해 표시할 locale(언어)를 영어로 지정 --%>
	<%-- <fmt:setLocale value="en_US" /> --%>
	
	<%-- fmt:setLocale 태그를 이용해 표시할 locale(언어)를 한글로 지정 --%>
	<fmt:setLocale value="ko_KR" />
	
	<h1>
		회원정보 <br><br>
		<%-- fmt:bundle 태그를 이용해 resource 패키지 아래의 member 프로퍼티 파일을 읽어온다. --%>
		<fmt:bundle basename="resource.member">
		이름: <fmt:message key="mem.name" /><br> <%-- fmt:message 태그의 key 속성에 프로퍼티 파일의 key를 지정하여 값(value)을 출력 --%>
		주소: <fmt:message key="mem.address" /><br>
		직업: <fmt:message key="mem.job" /><br>
		</fmt:bundle>
	</h1>
</body>
</html>