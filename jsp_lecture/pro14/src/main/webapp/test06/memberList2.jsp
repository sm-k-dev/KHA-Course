<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*, sec02.ex01.*" isELIgnored="false"%>
<%-- JSTL 중에서 core 태그들을 사용하기 위해 외부 주소로 요청 --%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- JSTL 중에서 fomatting 태그들을 사용하기 위해 외부 주소로 요청 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>    
<% request.setCharacterEncoding("UTF-8"); %> 
<% List<MemberVO> membersList = (List<MemberVO>)request.getAttribute("membersList"); %>

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
		<%
			if ( membersList == null || membersList.isEmpty() ) {
		%>
				<tr>
					<td colspan=5>
						<b>등록된 회원이 없습니다.</b>
					</td>
				</tr>
		<%
			} else {
				for ( MemberVO vo : membersList ) {	
		%>
					<tr alignt="center">
						<td><%=vo.getId()%></td>
						<td><%=vo.getPwd()%></td>
						<td><%=vo.getName()%></td>
						<td><%=vo.getEmail()%></td>
						<td><%=vo.getJoinDate()%></td>
					</tr>
		<%	} } %>
	</table>
</body>
</html>