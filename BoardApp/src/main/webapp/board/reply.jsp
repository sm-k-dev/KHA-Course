<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.board.dto.BoardDto" %>
<%-- reply.jsp (View · 자바코드) : 답변 폼 → /board/replyProc.do --%>
<%
    // 컨텍스트 경로(프로젝트명)
    String cpath = request.getContextPath();
    // Controller가 담아준 "부모글"을 꺼낸다. (제목을 Re: 로 이어붙이기 위함)
    BoardDto dto = (BoardDto) request.getAttribute("dto");
    // 답변 후 목록 복귀를 위해 검색조건도 함께 받는다.
    String keyField = (String) request.getAttribute("keyField");
    String keyWord  = (String) request.getAttribute("keyWord");
    // 부모글 번호(어느 글의 답변인지 구분)
    int num        = dto.getNum();
    // 부모글 제목(없으면 빈 문자열) → 답변 제목 기본값 "Re:부모제목" 에 사용
    String subject = (dto.getSubject() != null) ? dto.getSubject() : "";
%>
<html lang="ko">
<head><title>JSPBoard</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div style="text-align:center">
<br><br>
<table style="width:80%; margin:0 auto">
 <tr><td style="background:#84F399; height:25px; text-align:center">답변하기</td></tr>
</table>
<br>
<table style="width:80%; margin:0 auto">
<%-- 답변 내용을 replyProc.do 로 POST 전송 --%>
<form name="post" method="post" action="<%=cpath%>/board/replyProc.do">
<%-- 부모글 번호 + 검색조건(숨김) 전달 --%>
<input type="hidden" name="num"      value="<%=num%>">
<input type="hidden" name="keyField" value="<%=keyField%>">
<input type="hidden" name="keyWord"  value="<%=keyWord%>">
 <tr>
  <td style="text-align:center">
   <table style="width:100%; margin:0 auto">
    <tr><td style="width:10%">성 명</td>    <td style="width:90%"><input type="text" name="name" size="10" maxlength="8"></td></tr>
    <tr><td style="width:10%">E-Mail</td>   <td style="width:90%"><input type="text" name="email" size="30" maxlength="30"></td></tr>
    <tr><td style="width:10%">홈페이지</td> <td style="width:90%"><input type="text" name="homepage" size="40" maxlength="30"></td></tr>
    <tr><td style="width:10%">제 목</td>
        <%-- 부모 제목 앞에 "Re:" 를 붙여 기본값으로 채운다 --%>
        <td style="width:90%"><input type="text" name="subject" size="50" maxlength="30" value="Re:<%=subject%>"></td></tr>
    <tr><td style="width:10%">내 용</td>
        <td style="width:90%"><textarea name="content" rows="10" cols="50"></textarea></td></tr>
    <tr><td style="width:10%">비밀 번호</td><td style="width:90%"><input type="password" name="pass" size="15" maxlength="15"></td></tr>
    <tr><td colspan="2"><hr></td></tr>
    <tr>
     <td colspan="2">
       <input type="submit" value="등록">&nbsp;&nbsp;
       <input type="reset" value="다시쓰기">&nbsp;&nbsp;
       <input type="button" value="뒤로" onClick="history.back()">
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
