<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.BoardDto" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--============================================================
  list.jsp (View · 순수 자바코드)
  - Controller가 request에 담아준 데이터를 화면에만 출력한다.
  - 아래 스크립틀릿(<% %>)의 자바 코드에 한 줄씩 주석을 달았다.
=============================================================--%>
<%
    // 웹앱의 컨텍스트 경로(=프로젝트명)를 구한다. 예) "/BoardApp"
    String cpath = request.getContextPath();

    // [노란 "!" 경고 제거] getAttribute()는 Object를 돌려주므로,
    //   List<BoardDto>로의 형변환은 '검증되지 않은 형변환(unchecked)' 경고를 낸다.
    //   바로 아래 @SuppressWarnings("unchecked") 로 그 경고만 안전하게 억제한다.
    @SuppressWarnings("unchecked")
    // Controller가 setAttribute("boardList", ...) 로 담아둔 "현재 페이지 글 목록"을 꺼낸다.
    List<BoardDto> boardList = (List<BoardDto>) request.getAttribute("boardList");

    // 전체 글 개수 (Integer 객체 → int 로 자동 언박싱)
    int totalRecord  = (Integer) request.getAttribute("totalRecord");
    // 전체 페이지 수
    int totalPage    = (Integer) request.getAttribute("totalPage");
    // 전체 블럭 수 (페이지 번호 묶음 개수)
    int totalBlock   = (Integer) request.getAttribute("totalBlock");
    // 현재 페이지 번호 (0부터 시작)
    int nowPage      = (Integer) request.getAttribute("nowPage");
    // 현재 블럭 번호 (0부터 시작)
    int nowBlock     = (Integer) request.getAttribute("nowBlock");
    // 한 블럭에 보여줄 페이지 개수
    int pagePerBlock = (Integer) request.getAttribute("pagePerBlock");
    // 검색 기준 컬럼(name/subject/content) — 없으면 빈 문자열
    String keyField  = (String) request.getAttribute("keyField");
    // 검색어 — 없으면 빈 문자열
    String keyWord   = (String) request.getAttribute("keyWord");

    // 작성일시(Timestamp)를 "yyyy-MM-dd HH:mm" 모양으로 바꿔줄 포맷터 생성
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>JSP Board (MVC)</title>
<link href="<%=cpath%>/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    function check() {
        if (document.search.keyWord.value == "") {
            alert("검색어를 입력하세요.");
            document.search.keyWord.focus();
            return;
        }
        document.search.submit();
    }
</script>
</head>
<body>
<div style="text-align:center"><br>
<h2>JSP Board <small>(MVC · Model2)</small></h2>

<table style="margin:0 auto; width:80%">
<tr>
    <td style="text-align:left">Total : <%=totalRecord%> Articles (
        <span style="color:red"><%=nowPage + 1%> / <%=(totalPage == 0 ? 1 : totalPage)%> Pages</span> )
    </td>
</tr>
</table>

<table style="margin:0 auto; width:80%">
<tr>
    <td style="text-align:center" colspan="2">
        <table style="width:100%">
            <tr style="text-align:center; background:#D0D0D0">
                <td> 번호 </td>
                <td> 제목 </td>
                <td> 이름 </td>
                <td> 날짜 </td>
                <td> 조회수 </td>
            </tr>
        <%
            // 목록이 비어 있으면(글이 하나도 없으면) 안내 문구 한 줄만 출력
            if (boardList == null || boardList.isEmpty()) {
        %>
            <tr><td colspan="5">등록된 글이 없습니다.</td></tr>
        <%
            // 글이 있으면 한 건(dto)씩 꺼내 반복 출력
            } else {
                for (BoardDto dto : boardList) {
                    // 답변 깊이(depth)만큼 공백을 만들어 "들여쓰기" 효과를 준다.
                    String indent = "";
                    // depth가 1이면 3칸, 2이면 6칸... (depth*3) 만큼 &nbsp; 반복
                    for (int i = 0; i < dto.getDepth() * 3; i++) indent += "&nbsp;";
        %>
            <tr>
                <%-- 글 번호 --%>
                <td style="text-align:center"><%=dto.getNum()%></td>
                <td>
                    <%-- 위에서 만든 들여쓰기 공백 출력 --%>
                    <%=indent%>
                    <%
                        // 답변글(depth>0)이면 화살표 아이콘을 붙인다.
                        if (dto.getDepth() > 0) {
                    %>
                        <img src="<%=cpath%>/img/re.gif" alt="답변">
                    <%
                        }
                    %>
                    <%-- 제목 클릭 → 상세보기(read.do). 검색조건도 함께 넘긴다. --%>
                    <a href="<%=cpath%>/board/read.do?num=<%=dto.getNum()%>&keyField=<%=keyField%>&keyWord=<%=keyWord%>">
                        <%=dto.getSubject()%>
                    </a>
                </td>
                <%-- 이름(클릭 시 메일 보내기) --%>
                <td style="text-align:center"><a href="mailto:<%=dto.getEmail()%>"><%=dto.getName()%></a></td>
                <%-- 작성일시(포맷 적용) --%>
                <td style="text-align:center"><%=sdf.format(dto.getRegdate())%></td>
                <%-- 조회수 --%>
                <td style="text-align:center"><%=dto.getCount()%></td>
            </tr>
        <%
                } // for 끝
            } // else 끝
        %>
        </table>
    </td>
</tr>
<tr><td><br><br></td></tr>
<tr>
    <td style="text-align:left">Go to Page
    <%
        // [이전 블럭] 글이 있고, 현재 블럭이 첫 블럭이 아니면 "이전" 링크 표시
        if (totalRecord > 0 && nowBlock > 0) {
    %>
        <a href="<%=cpath%>/board/list.do?nowBlock=<%=nowBlock-1%>&nowPage=<%=(nowBlock-1)*pagePerBlock%>&keyField=<%=keyField%>&keyWord=<%=keyWord%>">이전 <%=pagePerBlock%>개</a> :::
    <%
        }
        // [페이지 번호] 현재 블럭에 속한 페이지 번호들을 하나씩 출력
        for (int cnt = 0; cnt < pagePerBlock; cnt++) {
            // 이번에 그릴 페이지 번호(사람이 보는 1부터의 번호)
            int pageNo = nowBlock * pagePerBlock + cnt + 1;
            // 전체 페이지 수를 넘으면 더 그리지 않고 멈춘다.
            if (pageNo > totalPage) break;
    %>
        <a href="<%=cpath%>/board/list.do?nowBlock=<%=nowBlock%>&nowPage=<%=nowBlock*pagePerBlock + cnt%>&keyField=<%=keyField%>&keyWord=<%=keyWord%>"><%=pageNo%></a>
    <%
        }
        // [다음 블럭] 다음 블럭이 더 있으면 "다음" 링크 표시
        if (totalBlock > nowBlock + 1) {
    %>
        ::: <a href="<%=cpath%>/board/list.do?nowBlock=<%=nowBlock+1%>&nowPage=<%=(nowBlock+1)*pagePerBlock%>&keyField=<%=keyField%>&keyWord=<%=keyWord%>">다음 <%=pagePerBlock%>개</a>
    <%
        }
    %>
    </td>
    <td style="text-align:right">
        <a href="<%=cpath%>/board/write.do">[글쓰기]</a>
        <a href="<%=cpath%>/board/list.do">[처음으로]</a>
    </td>
</tr>
</table>
<br>

<%-- 검색 폼 : 선택한 기준(keyField)과 검색어(keyWord)를 list.do로 POST 전송 --%>
<form action="<%=cpath%>/board/list.do" name="search" method="post">
    <table style="margin:0 auto; width:527px">
    <tr>
        <td style="text-align:center; vertical-align:bottom">
            <select name="keyField" size="1">
                <%-- 이전에 고른 검색 기준이 그대로 선택돼 보이도록 selected 처리 --%>
                <option value="name"    <%="name".equals(keyField)    ? "selected" : ""%>>이름</option>
                <option value="subject" <%="subject".equals(keyField) ? "selected" : ""%>>제목</option>
                <option value="content" <%="content".equals(keyField) ? "selected" : ""%>>내용</option>
            </select>
            <input type="text" size="16" name="keyWord" value="<%=keyWord%>">
            <input type="button" value="찾기" onClick="check()">
        </td>
    </tr>
    </table>
</form>
</div>
</body>
</html>
