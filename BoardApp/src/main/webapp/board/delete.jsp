<%@ page contentType="text/html; charset=UTF-8" %>
<%-- delete.jsp (View · 자바코드) : 삭제 비밀번호 확인 → /board/deleteProc.do --%>
<%
    // 컨텍스트 경로(프로젝트명)
    String cpath = request.getContextPath();
    // Controller가 담아준 "삭제할 글번호"를 꺼낸다. (Integer → int 자동 언박싱)
    int num = (Integer) request.getAttribute("num");
%>
<html lang="ko">
<head><title>JSPBoard</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
<script>
    function check() {
        if (document.form.pass.value == "") {
            alert("패스워드를 입력하세요.");
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
<table style="width:50%; margin:0 auto">
 <tr><td style="background:#dddddd; height:21px; text-align:center">삭제하려면 비밀번호를 입력해 주세요.</td></tr>
</table>

<%-- 비밀번호를 deleteProc.do 로 POST 전송 --%>
<form name="form" method="post" action="<%=cpath%>/board/deleteProc.do">
<%-- 삭제할 글번호(숨김) --%>
<input type="hidden" name="num" value="<%=num%>" />
<table style="width:70%; margin:0 auto">
 <tr>
  <td style="text-align:center">
   <table style="margin:0 auto; width:91%">
    <tr><td style="text-align:center"><input type="password" name="pass" size="17" maxlength="15"></td></tr>
    <tr><td><hr style="color:#eeeeee"></td></tr>
    <tr>
     <td style="text-align:center">
       <input type="button" value="삭제완료" onClick="check()">
       <input type="reset" value="다시쓰기">
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
