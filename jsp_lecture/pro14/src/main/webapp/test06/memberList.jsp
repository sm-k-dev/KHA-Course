<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*, sec02.ex01.*" isELIgnored="false"%>
<%-- JSTL 중에서 core 태그들을 사용하기 위해 외부 주소로 요청 --%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- JSTL 중에서 fomatting 태그들을 사용하기 위해 외부 주소로 요청 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>    
<% request.setCharacterEncoding("UTF-8"); %> 

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
			<td width="7%"><b>이메일</b></td>
			<td width="7%"><b>가입일</b></td>
		</tr>
		<c:choose>
		<%--
			자바 코드 사용하지 않음
			ArrayList list = request.getAttribute("membersList");
		--%>
			<c:when test="${ membersList == null }">
				<tr>
					<td colspan=5>
						<b>등록된 회원이 없습니다.</b>
					</td>
				</tr>
			</c:when>
			<c:when test="${ membersList != null }"> <%-- 표현 언어에서 바인딩된 속성 이름으로 바로 접근 --%>
				<%-- membersList에 저장된 memberVO를 mem에 차례대로 가져와 속성 이름으로 출력 --%>
				<c:forEach var="mem" items="${ membersList }">
					<tr align="center">
						<td>${mem.id}</td>
						<td>${mem.pwd}</td>
						<td>${mem.name}</td>
						<td>${mem.email}</td>
						<td>${mem.joinDate}</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
</body>
</html>