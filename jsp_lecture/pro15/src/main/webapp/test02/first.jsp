<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- JSTL 전체 라이브러리에 속한 core 라이브러리 태그 사용을 위해 외부 사이트에 태그 요청 --%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"  %> 

<% request.setCharacterEncoding("UTF-8"); %>
    
<%-- 톰캣 서버가 프로젝트 까지 찾아갈수 있는 컨텍스트 주소 경로 "/pro15" 얻어 변수에 저장 --%>    
<c:set  var="contextPath" value="${pageContext.request.contextPath}" />    
    <%--      "/pro15"   --%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first.jsp</title>
</head>
<body>
	
	<%--
		first.jsp 페이지에서는 다운로드 시킬 파일이름을 hidden 태그에 설정해
		result.jsp 페이지로 전송요청 한다.
	--%>
	<form action="result.jsp" method="post">
		<input type="hidden" name="param1" value="CS.md" >
		<input type="hidden" name="param2" value="OT.md" >
		
		<input type="submit" value="다운받을 파일명 전달">
	</form>
	
</body>
</html>