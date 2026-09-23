<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.board.dto.BoardDto" %>
<%-- update.jsp (View · 자바코드) : 수정 폼 → /board/updateProc.do --%>
<%
    // 컨텍스트 경로(프로젝트명)
    String cpath = request.getContextPath();
    // Controller가 담아준 "수정할 글"을 꺼낸다.
    BoardDto dto = (BoardDto) request.getAttribute("dto");
    // 폼에 채워 넣을 값들을 미리 꺼낸다. (null이면 빈 문자열로 대체)
    int num        = dto.getNum();                                        // 글 번호
    String name    = (dto.getName() != null) ? dto.getName() : "";        // 이름
    String email   = (dto.getEmail() != null) ? dto.getEmail() : "";      // 이메일
    String subject = (dto.getSubject() != null) ? dto.getSubject() : "";  // 제목
    String content = (dto.getContent() != null) ? dto.getContent() : "";  // 내용
%>
<html lang="ko">
<head><title>JSPBoard</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
<script>
    function check() {
        if (document.form.pass.value == "") {
            alert("수정을 위해 패스워드를 입력하세요.");
            document.form.pass.focus();
            return false;
        }
        document.form.submit();
    }
</script>
</head>
<body>
<div style="text-align:center">
<br><br>
<table style="width:460px; margin:0 auto">
  <tr><td style="background:#FF9018; height:21px; text-align:center">수정하기</td></tr>
</table>

<%-- 수정값을 updateProc.do 로 POST 전송 --%>
<form name="form" method="post" action="<%=cpath%>/board/updateProc.do">
<%-- 어떤 글을 수정할지 구분할 글번호(숨김) --%>
<input type="hidden" name="num" value="<%=num%>" />
<table style="width:70%; margin:0 auto">
 <tr>
  <td style="text-align:center">
   <table>
    <tr><td style="width:20%">성 명</td>
        <td style="width:80%"><input type="text" name="name" size="30" maxlength="20" value="<%=name%>"></td></tr>
    <tr><td style="width:20%">E-Mail</td>
        <td style="width:80%"><input type="text" name="email" size="30" maxlength="30" value="<%=email%>"></td></tr>
    <tr><td style="width:20%">제 목</td>
        <td style="width:80%"><input type="text" name="subject" size="50" maxlength="50" value="<%=subject%>"></td></tr>
    <tr><td style="width:20%">내 용</td>
        <td style="width:80%"><textarea name="content" rows="10" cols="50"><%=content%></textarea></td></tr>
    <tr><td style="width:20%">비밀 번호</td>
        <td style="width:80%"><input type="password" name="pass" size="15" maxlength="15"> 수정시에는 비밀번호가 필요합니다.</td></tr>
    <tr><td colspan="2"><hr></td></tr>
    <tr>
     <td colspan="2">
       <input type="button" value="수정완료" onClick="check()">
       <input type="reset" value="다시수정">
       <input type="button" value="뒤로" onClick="history.back()">
     </td>
    </tr>
   </table>
  </td>
 </tr>
</table>
</form>
</div>
</body>
</html>
