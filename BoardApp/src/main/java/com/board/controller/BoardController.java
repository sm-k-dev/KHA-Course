package com.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;   // URL 매핑을 애노테이션으로 지정
import javax.servlet.http.HttpServlet;        // 서블릿의 부모 클래스
import javax.servlet.http.HttpServletRequest; // 요청 정보(파라미터 등)를 담은 객체
import javax.servlet.http.HttpServletResponse;// 응답을 만들어 내보내는 객체

import com.board.dto.BoardDto;
import com.board.service.BoardService;

/**
 * ============================================================
 *  BoardController  (Front Controller · MVC의 "C = Controller")
 * ============================================================
 *  [이 클래스가 하는 일]
 *   - 게시판의 모든 요청(*.do)을 이 서블릿 "하나"가 받는다. (= 프론트 컨트롤러 패턴)
 *   - 요청 주소를 보고 알맞은 작업으로 "분기(라우팅)" 한다.
 *   - 3가지 역할만 한다:
 *       ① 파라미터 수집  ② Service 호출(일 시키기)  ③ 어떤 화면(JSP)을 보여줄지 결정
 *   - DB·업무규칙은 절대 여기 두지 않는다. → Service/DAO 담당. (교통정리만!)
 *
 *  [서블릿(Servlet)이란?]
 *   - 톰캣(WAS) 위에서 실행되며 "요청을 받아 응답을 만드는" 자바 클래스.
 *   - HttpServlet 을 상속받고 doGet/doPost 를 재정의(override)해 사용한다.
 *
 *  [@WebServlet("*.do") 매핑을 쓴 이유]  ★ 이번 구조의 핵심
 *   - JSP를 webapp/board/ 폴더 아래에 두었다. (예: /board/list.jsp)
 *   - 만약 컨트롤러를 "/board/*" 로 매핑하면 JSP 요청까지 컨트롤러가 가로채
 *     forward → 다시 컨트롤러 → forward ... 무한루프에 빠진다.
 *   - 그래서 "확장자 매핑(*.do)"을 쓴다. JSP(*.jsp)와 겹치지 않으므로 안전.
 *
 *  [forward vs redirect]  ★ 단골 시험 포인트
 *   - forward : 서버 내부에서 제어를 넘김. request가 그대로 유지됨(주소 안 바뀜).
 *               → 화면 보여줄 때(목록/상세/폼) 사용. setAttribute 값 전달 가능.
 *   - redirect: 브라우저에게 "저 주소로 다시 요청해" 지시. 새 요청(주소 바뀜).
 *               → 글쓰기/수정/삭제 후 목록으로 보낼 때 사용.
 *                 (새로고침 시 중복 저장 방지 = PRG 패턴)
 * ============================================================
 */
@WebServlet("*.do")   // 끝이 .do 인 모든 요청을 이 서블릿이 처리
public class BoardController extends HttpServlet {

    private static final long serialVersionUID = 1L; // 직렬화 버전(서블릿 관례상 선언)

    // 컨트롤러가 부릴 Service 도구 (요청마다 새로 만들지 않고 하나를 재사용)
    private final BoardService service = new BoardService();

    // View(JSP)들이 있는 폴더 위치. forward 할 때 앞에 붙인다. (webapp/board/)
    private static final String VIEW = "/board/";

    /** GET 방식 요청(주소창 입력, 링크 클릭 등)이 오면 실행 → 공통 처리로 넘김 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    /** POST 방식 요청(폼 submit 등)이 오면 실행 → 공통 처리로 넘김 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    /**
     * 실제 분기 처리(라우팅).
     *   - 요청 주소(getServletPath)를 보고 알맞은 메소드를 호출한다.
     */
    private void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ── 한글 깨짐 방지 : 요청/응답 인코딩을 UTF-8 로 통일 ──
        //    (반드시 파라미터를 꺼내기 "전"에 설정해야 적용된다)
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // 요청 경로 추출 (예: "/board/list.do")
        String command = req.getServletPath();

        // 경로에 따라 알맞은 기능으로 분기
        switch (command) {
            case "/board/list.do":        list(req, resp);       break; // 목록
            case "/board/read.do":        read(req, resp);       break; // 상세보기
            case "/board/write.do":       forward(req, resp, "post.jsp");  break; // 글쓰기 폼
            case "/board/writeProc.do":   writeProc(req, resp);  break; // 글쓰기 처리
            case "/board/updateForm.do":  updateForm(req, resp); break; // 수정 폼
            case "/board/updateProc.do":  updateProc(req, resp); break; // 수정 처리
            case "/board/deleteForm.do":  deleteForm(req, resp); break; // 삭제 폼
            case "/board/deleteProc.do":  deleteProc(req, resp); break; // 삭제 처리
            case "/board/replyForm.do":   replyForm(req, resp);  break; // 답변 폼
            case "/board/replyProc.do":   replyProc(req, resp);  break; // 답변 처리
            default:                      list(req, resp);       break; // 그 외 → 목록
        }
    }

    /* ========================= 목록 + 페이징 ========================= */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) 검색 파라미터 수집 (없으면 빈 문자열)
        String keyField = nvl(req.getParameter("keyField"));
        String keyWord  = nvl(req.getParameter("keyWord"));

        // 2) Service에 전체(또는 검색) 목록 요청
        List<BoardDto> all = service.getBoardList(keyField, keyWord);

        // 3) 페이징 계산  (화면에 몇 개씩/몇 페이지로 보여줄지)
        int numPerPage   = 5;   // 한 페이지에 보여줄 글 수
        int pagePerBlock = 3;   // 페이지 번호를 몇 개씩 묶어 보여줄지 (블럭)
        int totalRecord  = all.size();                                   // 전체 글 수
        int totalPage    = (int) Math.ceil((double) totalRecord / numPerPage); // 전체 페이지 수
        int totalBlock   = (int) Math.ceil((double) totalPage / pagePerBlock);  // 전체 블럭 수

        int nowPage  = parseInt(req.getParameter("nowPage"), 0);  // 현재 페이지(0부터)
        int nowBlock = parseInt(req.getParameter("nowBlock"), 0); // 현재 블럭(0부터)

        // 현재 페이지에 해당하는 글만 잘라내기 (subList: begin~end 범위)
        int begin = nowPage * numPerPage;
        int end   = Math.min(begin + numPerPage, totalRecord);
        List<BoardDto> pageList =
                (begin <= end && begin >= 0) ? all.subList(begin, end) : all.subList(0, 0);

        // 4) 화면(JSP)에서 쓸 값들을 request에 담기 (setAttribute → JSP에서 getAttribute)
        req.setAttribute("boardList",   pageList);
        req.setAttribute("totalRecord", totalRecord);
        req.setAttribute("totalPage",   totalPage);
        req.setAttribute("totalBlock",  totalBlock);
        req.setAttribute("nowPage",     nowPage);
        req.setAttribute("nowBlock",    nowBlock);
        req.setAttribute("numPerPage",  numPerPage);
        req.setAttribute("pagePerBlock",pagePerBlock);
        req.setAttribute("keyField",    keyField);
        req.setAttribute("keyWord",     keyWord);

        // 5) 목록 화면으로 forward (주소 안 바뀜, 담아둔 값 그대로 전달)
        forward(req, resp, "list.jsp");
    }

    /* ========================= 상세보기 ========================= */
    private void read(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int num = parseInt(req.getParameter("num"), 0);
        BoardDto dto = service.read(num);        // 조회수 +1 하며 글 가져오기
        req.setAttribute("dto", dto);            // 화면에 전달할 글
        req.setAttribute("keyField", nvl(req.getParameter("keyField"))); // 목록복귀용 검색조건
        req.setAttribute("keyWord",  nvl(req.getParameter("keyWord")));
        forward(req, resp, "read.jsp");
    }

    /* ========================= 글쓰기 처리 ========================= */
    private void writeProc(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        BoardDto dto = bindForm(req);            // 폼 값 → DTO로 포장
        dto.setIp(req.getRemoteAddr());          // IP는 폼이 아니라 서버가 직접 수집(위변조 방지)
        service.write(dto);                      // 저장 위임
        redirect(req, resp, "list.do");          // 저장 후 목록으로 redirect(새로고침 중복저장 방지)
    }

    /* ========================= 수정 ========================= */
    // 수정 "폼" 보여주기 : 기존 글 내용을 채워서 보여줘야 하므로 조회 후 forward
    private void updateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int num = parseInt(req.getParameter("num"), 0);
        req.setAttribute("dto", service.getBoard(num)); // 조회수 증가 X (폼 표시용)
        forward(req, resp, "update.jsp");
    }

    // 수정 "처리" : 비밀번호 확인 후 성공하면 목록, 실패하면 경고
    private void updateProc(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        BoardDto dto = bindForm(req);
        dto.setNum(parseInt(req.getParameter("num"), 0)); // 어떤 글을 수정할지 번호도 담기
        boolean ok = service.update(dto);                 // 비번 검증 + 수정
        if (ok) redirect(req, resp, "list.do");           // 성공 → 목록
        else    alertBack(req, resp, "입력하신 비밀번호가 올바르지 않습니다."); // 실패 → 경고
    }

    /* ========================= 삭제 ========================= */
    // 삭제 "폼"(비밀번호 입력창) 보여주기
    private void deleteForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("num", parseInt(req.getParameter("num"), 0));
        forward(req, resp, "delete.jsp");
    }

    // 삭제 "처리" : 비밀번호 확인 후 삭제
    private void deleteProc(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int num = parseInt(req.getParameter("num"), 0);
        String pass = nvl(req.getParameter("pass"));
        boolean ok = service.delete(num, pass);
        if (ok) redirect(req, resp, "list.do");
        else    alertBack(req, resp, "입력하신 비밀번호가 올바르지 않습니다.");
    }

    /* ========================= 답변 ========================= */
    // 답변 "폼" 보여주기 : 부모글 제목 등을 참고로 보여주기 위해 부모글 조회
    private void replyForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int num = parseInt(req.getParameter("num"), 0);
        req.setAttribute("dto", service.getBoard(num)); // 부모글(제목/내용)
        req.setAttribute("keyField", nvl(req.getParameter("keyField")));
        req.setAttribute("keyWord",  nvl(req.getParameter("keyWord")));
        forward(req, resp, "reply.jsp");
    }

    // 답변 "처리" : 부모글 번호와 함께 답변 저장
    private void replyProc(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int parentNum = parseInt(req.getParameter("num"), 0); // 부모글 번호
        BoardDto reply = bindForm(req);
        reply.setIp(req.getRemoteAddr());
        service.reply(parentNum, reply);
        redirect(req, resp, "list.do");
    }

    /* ========================= 공통 헬퍼 ========================= */

    /**
     * 폼 파라미터들을 꺼내 BoardDto 하나로 포장한다.
     *   - 글쓰기/수정/답변에서 공통으로 쓰이므로 메소드로 분리(중복 제거).
     */
    private BoardDto bindForm(HttpServletRequest req) {
        BoardDto dto = new BoardDto();
        dto.setName(nvl(req.getParameter("name")));
        dto.setEmail(nvl(req.getParameter("email")));
        dto.setHomepage(nvl(req.getParameter("homepage")));
        dto.setSubject(nvl(req.getParameter("subject")));
        dto.setContent(nvl(req.getParameter("content")));
        dto.setPass(nvl(req.getParameter("pass")));
        return dto;
    }

    /** forward : 서버 내부에서 JSP로 제어를 넘겨 화면을 그린다. (주소 유지) */
    private void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(VIEW + jsp).forward(req, resp);
    }

    /** redirect : 브라우저에게 새 주소로 다시 요청하라고 지시. (주소 변경 / 중복저장 방지) */
    private void redirect(HttpServletRequest req, HttpServletResponse resp, String cmd)
            throws IOException {
        //  contextPath = 프로젝트 이름(예: /BoardApp). 붙여줘야 정확한 경로가 됨.
        resp.sendRedirect(req.getContextPath() + "/board/" + cmd);
    }

    /** 비밀번호 오류 등: 자바스크립트로 경고창 띄우고 이전 페이지로 돌려보냄 */
    private void alertBack(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("<script>alert('" + msg + "'); history.back();</script>");
    }

    /** null 방지 : 값이 null이면 빈 문자열로 바꿔 반환 (NullPointerException 예방) */
    private String nvl(String s) { return (s == null) ? "" : s; }

    /** 문자열 → 정수 안전 변환 : 숫자가 아니면 예외 대신 기본값(def) 반환 */
    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
