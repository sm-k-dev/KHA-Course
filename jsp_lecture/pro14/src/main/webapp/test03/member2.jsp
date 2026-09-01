<%@page import="sec01.ex01.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	// 순서1. member2.jsp 요청한 한글 문자 인코딩 방식 UTF-8 설정
	request.setCharacterEncoding("utf-8");
%>

<%-- 순서2. HashMap, ArrayList 배열 생성 (액션태그 사용) --%>
<jsp:useBean id="membersList" class="java.util.ArrayList" scope="page" />
<jsp:useBean id="membersMap" class="java.util.HashMap" scope="page" />
<%
	// 순서3. ArrayList 배열에 MemberVO객체 2쌍을 생성해서 추가
	membersList.add(new MemberVO("ki", "4321", "기성용", "ki@test.com"));
	membersList.add(new MemberVO("son", "1234", "손흥민", "son@test.com"));
	
	// 순서4. HashMap 배열에 key, value 를 한 쌍의 형태로 묶어서 박지성 정보를 바인딩(저장)
	membersMap.put("id", "park2");
	membersMap.put("pwd", "4321");
	membersMap.put("name", "박지성");
	membersMap.put("email", "park2@test.com");
	
	// 순서5. HashMap 배열에 key, value를 한 쌍의 형태로 묶어서 바로 위 ArrayList 배열 자체를 바인딩
	membersMap.put("List", membersList);
%>
<%--
	순서6. <c:set>태그를 이용해 HashMap에 바인딩(저장)된 ArrayList에 접근하기 위해 사용하기 편리한 이름으로 변수 설정
		참고. 아래 membersList 변수명으로 저장된 ArrayList배열 메모리 자체를 EL 태그 내부에 작성해 사용 할 수 있다.
--%>
<c:set var="memberslist" value="${pageScope.membersMap.List}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 출력창</title>
</head>
<body>
	<table border="1" align="center">
		<tr align=center bgcolor="#99ccff">
			<td width="20%"><b>아이디</b></td>
			<td width="20%"><b>비밀번호</b></td>
			<td width="20%"><b>이름</b></td>
			<td width="20%"><b>이메일</b></td>
		</tr>
		<%--
			HashMap 배열에 저장된 박지성에 대한 문자열 정보들을 EL 태그로 얻어 출력
			작성 방법: 먼저 page 내장객체에 접근하기 위해 EL 태그 문법에서 제공하는 pageScope 를 사용하고
					그리고 HashMap 배열을 꺼내오기 위해 pageScope.membersMap을 작성하고
					마지막으로 박지성에 대한 문자열 값을 얻어오기 위해 pageScope.memberMap 키 작성해서 최종 얻어 각각 출력
		--%>
		<tr align="center">
			<td>${pageScope.membersMap.id}</td>
			<td>${pageScope.membersMap.pwd}</td>
			<td>${			membersMap.name}</td>
			<td>${			membersMap.email}</td>
		</tr>
		<%-- membersMap.key -> membersMap.List 를 작성하면 key와 저장된 ArrayList배열을 HashMap에서 꺼내 옵니다.
			 그런데 우리는 위  c:set 태그로  memberslist변수를 선언하고  HashMap배열에서 꺼내온 ArrayList배열 자체 주소를 저장 해 놓았습니다.
			 그러므로 아래 처럼 memberlist[index]를 작성 하면  ArrayList배열 내부의 index위치 칸에 저장된 MemberVO객체를 꺼내 올수 있습니다.
		 --%>	
 
		<%--
			위에 만들어진 ArrayList배열의 0 index 위치칸에 저장된 첫번째 MemberVO객체를 얻고,
			얻은 MemberVO객체의 각 변수의 값을 얻어 EL 태그로 출력
			EL 태그는 자바의 for문과 함께 쓸 수 없다. 후에 c:forEach를 사용할 것이다.
		--%>
		<tr align="center">
			<td>${memberslist[0].id}</td>
			<td>${memberslist[0].pwd}</td>
			<td>${memberslist[0].name}</td>
			<td>${memberslist[0].email}</td>
		</tr>
		<tr align="center">
			<td>${memberslist[1].id}</td>
			<td>${memberslist[1].pwd}</td>
			<td>${memberslist[1].name}</td>
			<td>${memberslist[1].email}</td>
		</tr>
	</table>
</body>
</html>