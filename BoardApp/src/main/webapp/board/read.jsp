<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.board.dto.BoardDto" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%-- read.jsp (View · 자바코드) : 글 상세보기 --%>
<%
    // 컨텍스트 경로(프로젝트명) 구하기
    String cpath = request.getContextPath();
    // Controller가 담아준 "글 1건"을 꺼낸다. (BoardDto는 제네릭이 아니라 경고 없음)
    BoardDto dto = (BoardDto) request.getAttribute("dto");
    // 목록 복귀·답변 링크에 함께 넘길 검색 조건
    String keyField = (String) request.getAttribute("keyField");
    String keyWord  = (String) request.getAttribute("keyWord");

    // 날짜 표시용 포맷터
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 화면에 뿌릴 값들을 미리 지역변수로 꺼내 둔다 (가독성)
    String name     = dto.getName();      // 작성자
    String email    = dto.getEmail();     // 이메일
    String homepage = dto.getHomepage();  // 홈페이지
    String subject  = dto.getSubject();   // 제목
    // 작성일시가 null이 아닐 때만 포맷 적용 (null이면 빈 문자열)
    String regdate  = (dto.getRegdate() != null) ? sdf.format(dto.getRegdate()) : "";
    // 내용의 줄바꿈(\n)을 <br>로 바꿔 화면에서 줄이 바뀌게 한다.
    String content  = (dto.getContent() != null) ? dto.getContent().replace("\n", "<br/>") : "";
    String ip       = dto.getIp();        // 작성 IP
    int count       = dto.getCount();     // 조회수
    int num         = dto.getNum();       // 글 번호(수정/삭제/답변 링크에 사용)
%>
<html lang="ko">
<head><title>JSPBoard</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<br><br>
<table style="margin:0 auto; width:70%">
 <tr><td style="background:#9CA2EE; height:25px; text-align:center">글읽기</td></tr>
 <tr>
  <td>
   <table style="width:100%">
    <tr>
     <td style="text-align:center; background:#dddddd; width:10%">이 름</td>
     <td style="background:#ffffe8"><%=name%></td>
     <td style="text-align:center; background:#dddddd; width:10%">등록날짜</td>
     <td style="background:#ffffe8"><%=regdate%></td>
    </tr>
    <tr>
     <td style="text-align:center; background:#dddddd; width:10%">메 일</td>
     <td style="background:#ffffe8"><%=email%></td>
     <td style="text-align:center; background:#dddddd; width:10%">홈페이지</td>
     <td style="background:#ffffe8">
        <%
            // 홈페이지가 입력된 경우에만 링크로 보여준다.
            if (homepage != null && !homepage.isEmpty()) {
        %>
            <a href="http://<%=homepage%>" target="_new">http://<%=homepage%></a>
        <%
            }
        %>
     </td>
    </tr>
    <tr>
     <td style="text-align:center; background:#dddddd">제 목</td>
     <td style="background:#ffffe8" colspan="3"><%=subject%></td>
    </tr>
    <tr>
     <td colspan="4"><%=content%></td>
    </tr>
    <tr>
     <td colspan="4" style="text-align:right">
        <%=ip%>로 부터 글을 남기셨습니다. / 조회수 : <%=count%>
     </td>
    </tr>
   </table>
  </td>
 </tr>
 <tr>
  <td style="text-align:center">
    <hr>
    <%-- 목록/수정/답변/삭제 링크. 모두 Controller의 *.do 로 이동 --%>
    [ <a href="<%=cpath%>/board/list.do?keyField=<%=keyField%>&keyWord=<%=keyWord%>">목 록</a> |
      <a href="<%=cpath%>/board/updateForm.do?num=<%=num%>">수 정</a> |
      <a href="<%=cpath%>/board/replyForm.do?num=<%=num%>&keyField=<%=keyField%>&keyWord=<%=keyWord%>">답 변</a> |
      <a href="<%=cpath%>/board/deleteForm.do?num=<%=num%>">삭 제</a> ]<br>
  </td>
 </tr>
</table>
</body>
</html>
