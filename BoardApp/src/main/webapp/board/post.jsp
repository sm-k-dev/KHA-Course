<%@ page contentType="text/html; charset=UTF-8" %>
<%-- post.jsp (View · 자바코드) : 글쓰기 폼 → /board/writeProc.do --%>
<%
    // 폼 action에 붙일 컨텍스트 경로(프로젝트명)
    String cpath = request.getContextPath();
%>
<html lang="ko">
<head><title>JSPBoard</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div style="text-align:center">
<br><br>
<table style="width:80%; margin:0 auto">
 <tr><td style="background:#84F399; height:25px; text-align:center">글쓰기</td></tr>
</table>
<br>
<table style="width:80%; margin:0 auto">
<%-- 입력값을 writeProc.do 로 POST 전송 --%>
<form name="post" method="post" action="<%=cpath%>/board/writeProc.do">
 <tr>
  <td style="text-align:center">
   <table style="width:100%; margin:0 auto">
    <tr><td style="width:10%">성 명</td>    <td style="width:90%"><input type="text" name="name" size="10" maxlength="8"></td></tr>
    <tr><td style="width:10%">E-Mail</td>   <td style="width:90%"><input type="text" name="email" size="30" maxlength="30"></td></tr>
    <tr><td style="width:10%">홈페이지</td> <td style="width:90%"><input type="text" name="homepage" size="40" maxlength="30"></td></tr>
    <tr><td style="width:10%">제 목</td>    <td style="width:90%"><input type="text" name="subject" size="50" maxlength="30"></td></tr>
    <tr><td style="width:10%">내 용</td>    <td style="width:90%"><textarea name="content" rows="10" cols="50"></textarea></td></tr>
    <tr><td style="width:10%">비밀 번호</td><td style="width:90%"><input type="password" name="pass" size="15" maxlength="15"></td></tr>
    <tr><td colspan="2"><hr></td></tr>
    <tr>
     <td colspan="2">
       <input type="submit" value="등록">&nbsp;&nbsp;
       <input type="reset" value="다시쓰기">&nbsp;&nbsp;
       <input type="button" value="목록" onClick="location.href='<%=cpath%>/board/list.do'">
     </td>
    </tr>
   </table>
  </td>
 </tr>
</form>
</table>
</div>
</body>
</html>
