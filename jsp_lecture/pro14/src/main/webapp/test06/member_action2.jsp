<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*, sec02.ex01.*" isELIgnored="false"%>
<%--
	member_action.jsp는 화면 기능을 수행하지 않고
	데이터베이스 연동 기능만 수행한다.
	회원 정보를 추가한 후 다시 회원 정보를 조회하고,
	조회한 회원 정보를 request에 바인딩한 후 memberList.jsp로 포워딩 한다.
--%>

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
<jsp:useBean id="m" class="sec02.ex01.MemberVO" />
<jsp:setProperty property="*" name="m" />
<%
	MemberDAO memDAO = new MemberDAO();
	memDAO.addMember(m); // 회원 정보 추가
	List membersList = memDAO.listMembers(); // 회원 정보 조회
	request.setAttribute("membersList", membersList); // 조회한 회원정보를 request에 바인딩
%>
<%-- 다시 memberList.jsp로 포워딩 --%>
<%
	RequestDispatcher dispatcher = request.getRequestDispatcher("memberList2.jsp");
	dispatcher.forward(request, response);
%>
</body>
</html>