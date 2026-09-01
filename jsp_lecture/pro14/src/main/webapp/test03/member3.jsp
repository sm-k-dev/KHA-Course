<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- JSTL의 core 라이브러리 태그들을 사용하기 위해 외부 사이트에서 불러오는 taglib 디렉티브 태그 한 줄 작성
	prefix="c" 로 설정했기 때문에 JSTL 태그를 사용할때는 c: 으로 적는다.
	만약 다른 문자열로 설정했다면 설정한_문자열: 으로 적어야 한다. --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	// 순서1. member1.jsp 요청한 한글문자 인코딩 방식 utf-8 설정
	request.setCharacterEncoding("utf-8");
%>
<%--
	JSTL의 core 라이브러리에 속한 태그 중에서 c:set 태그
	- 변수 선언 하는 태그
	- c:set 태그 작성 방법
		<c:set var="선언할_변수명_작성" value="변수에_저장할_값" scope="선언한_변수를_바인딩할_내장객체종류중_하나" />
																page / reqeust / session / application
--%>
<%-- id 변수 선언 후 "hong" 저장하고, id 변수를 page 내장객체 영역에 바인딩 --%>
<c:set var="id" value="hong" scope="page" />
<c:set var="pwd" value="1234" scope="page" />
<c:set var="name" value="${홍길동}" scope="page" />
<c:set var="age" value="${22}" scope="page" />
<c:set var="height" value="${177}" scope="page" />

<%--
	c:remove 태그
	- c:remove 태그는 c:set 태그로 설정한 변수를 내장객체 영역에서 제거할 때 사용한다.
		JSP에서 내장객체 (page, request, session, application)영역에 저장된 변수를 제거할 때 사용하는
		자바코드의 removeAttribute("key"); 와 같다.
	- 작성문법
		c:remove var="삭제할변수명" scope="삭제할변수가_바인딩된_내장객쳐_영역명"
								참고: scope 속성의 값을 지정하지 않으면 모든 내장객체영역에 저장된 변수들이 삭제 된다.
--%>

<%--
	모든 내장객체 메모리 영역(page, request, session, application)내에 age라는 이름으로 저장된 변수들 모두 제거
--%>
<c:remove var="age" />

<%--
	page 내장객체 메모리 영역에 저장된(바인딩된) height 변수만 제거
--%>
<c:remove var="height" scope="page" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 출력창</title>
</head>
<body>
	<table align="center" border=1 >
		<tr align="center" bgcolor="lightgreen" >
			<td width="7%"><b>아이디</b></td>
			<td width="7%"><b>비밀번호</b></td>
			<td width="7%"><b>이름</b></td>
			<td width="7%"><b>나이</b></td>
			<td width="7%"><b>키</b></td>
		</tr>
		<%-- 표현언어 EL ${ } 태그로 page 내장객체 영역에 바인딩 된 변수에 바로 접근하여 값을 출력 합니다. --%>
		<tr align="center">
			<td>${id}</td>
			<td>${pwd}</td>
			<td>${name}</td>
			<td>${age}</td>
			<td>${height}</td>
		</tr>
	</table>
</body>
</html>