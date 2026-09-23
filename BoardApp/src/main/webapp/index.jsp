<%@ page contentType="text/html; charset=UTF-8" %>
<%-- 진입점: 모든 요청은 Front Controller(*.do)를 거친다 --%>
<% response.sendRedirect(request.getContextPath() + "/board/list.do"); %>
