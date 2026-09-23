package com.board.dao;

import java.sql.Connection;         // DB와의 "연결 통로"
import java.sql.PreparedStatement;  // SQL을 미리 준비해두고 실행하는 객체 (? 자리표시자 사용)
import java.sql.ResultSet;          // SELECT 결과를 담아오는 "결과표(커서)"
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;        // 커넥션풀. 미리 만들어 둔 연결들을 빌려주는 창고.

import com.board.dto.BoardDto;

/**
 * ============================================================
 *  BoardDAO  (Data Access Object · DB 전담 계층)  =  MVC의 "Model" 일부
 * ============================================================
 *  [이 클래스가 하는 일]
 *   - 오직 "DB 작업"만 한다. (SQL 실행 → 결과를 DTO로 변환해서 반환)
 *   - 비밀번호 검증·시간 세팅 같은 "판단(업무규칙)"은 여기 두지 않는다. → Service 담당.
 *   - 규칙: DAO는 "시키는 대로 SQL만 실행하는 손발" 이라고 생각하면 쉽다.
 *
 *  [JDBC 5단계]  ★ DB 연동의 기본 뼈대
 *   ① Connection 얻기      : ds.getConnection()  (커넥션풀에서 빌림)
 *   ② SQL 준비             : con.prepareStatement(sql)
 *   ③ 값 바인딩            : pstmt.setXxx(순번, 값)   ← ? 자리에 값 끼우기
 *   ④ 실행                 : executeQuery(SELECT) / executeUpdate(INSERT·UPDATE·DELETE)
 *   ⑤ 자원 반납            : rs·pstmt·con close()   ← 반드시! 안 하면 커넥션 고갈
 *
 *  [JNDI + 커넥션풀(DataSource) 이란?]
 *   - DB 연결은 만들 때마다 비용이 크다. 그래서 톰캣이 미리 연결 여러 개를 만들어
 *     "커넥션풀(창고)"에 넣어둔다. 필요할 때 빌리고 → 다 쓰면 반납한다.
 *   - 그 창고 위치 정보는 META-INF/context.xml 에 등록되어 있고,
 *     JNDI(이름으로 찾기)로 "jdbc/jspbeginner" 라는 이름을 검색해 가져온다.
 *
 *  [PreparedStatement 를 쓰는 이유]  ★ 보안
 *   - 값을 문자열로 이어붙이지 않고 ? 자리표시자에 setString 으로 넣으면
 *     SQL Injection(악의적 SQL 삽입) 공격을 막을 수 있다.
 * ============================================================
 */
public class BoardDAO {

    private DataSource ds;   // 커넥션풀(창고) 참조를 보관해 둘 변수

    /**
     * 생성자 : 이 DAO 객체가 처음 만들어질 때 딱 1번 실행된다.
     *          → 톰캣의 커넥션풀(DataSource)을 JNDI로 찾아서 ds 에 저장해 둔다.
     */
    public BoardDAO() {
        try {
            // 1) 톰캣 환경정보에 접근하는 출입문 객체 생성
            Context init = new InitialContext();
            // 2) "jdbc/jspbeginner" 라는 이름으로 등록된 커넥션풀을 검색해서 가져옴
            //    ("java:comp/env/" 는 이 웹앱 전용 이름공간을 뜻하는 고정 접두어)
            ds = (DataSource) init.lookup("java:comp/env/jdbc/jspbeginner");
        } catch (Exception err) {
            System.out.println("BoardDAO 생성자 오류: " + err);
        }
    }

    /**
     * 자원 해제 : 빌려 쓴 rs → pstmt → con 을 "연 역순"으로 닫는다.
     *   - con.close() 는 실제로 연결을 끊는 게 아니라 "커넥션풀에 반납"하는 것.
     *   - 각 close 를 개별 try 로 감싸 하나가 실패해도 나머지는 닫히게 한다.
     */
    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {} // 풀에 반납
    }

    /**
     * ResultSet(결과표)의 "현재 한 행"을 → BoardDto 객체로 옮겨 담는 헬퍼.
     *   - 여러 메소드에서 똑같이 반복되는 코드라 하나로 묶어 중복을 제거했다.
     *   - rs.getString("컬럼명") : 그 컬럼의 값을 자료형에 맞게 꺼낸다.
     */
    private BoardDto mapRow(ResultSet rs) throws Exception {
        BoardDto dto = new BoardDto();
        dto.setNum(rs.getInt("num"));
        dto.setName(rs.getString("name"));
        dto.setEmail(rs.getString("email"));
        dto.setHomepage(rs.getString("homepage"));
        dto.setSubject(rs.getString("subject"));
        dto.setContent(rs.getString("content"));
        dto.setPass(rs.getString("pass"));
        dto.setCount(rs.getInt("count"));
        dto.setIp(rs.getString("ip"));
        dto.setRegdate(rs.getTimestamp("regdate"));
        dto.setPos(rs.getInt("pos"));
        dto.setDepth(rs.getInt("depth"));
        return dto;
    }

    /**
     * [목록] 전체 글 조회 (검색어 있으면 검색 결과) — pos 오름차순 정렬(답변글 순서 유지)
     * @param keyField 검색 기준 컬럼(name/subject/content) — Service가 화이트리스트로 검증함
     * @param keyWord  검색어
     * @return 글들을 담은 List (0건이면 빈 List)
     */
    public List<BoardDto> getBoardList(String keyField, String keyWord) {
        List<BoardDto> list = new ArrayList<BoardDto>();   // 여러 글을 담을 목록
        Connection con = null; PreparedStatement pstmt = null; ResultSet rs = null;
        try {
            con = ds.getConnection();                      // ① 연결 빌리기

            String sql;
            if (keyWord == null || keyWord.isEmpty()) {
                // 검색어가 없으면 → 전체 목록
                sql = "select * from tblBoard order by pos asc";
                pstmt = con.prepareStatement(sql);         // ② SQL 준비
            } else {
                // 검색어가 있으면 → 해당 컬럼 LIKE 검색
                //   keyField는 Service에서 name/subject/content 로만 제한(검증) → 인젝션 방지
                sql = "select * from tblBoard where " + keyField
                        + " like ? order by pos asc";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, "%" + keyWord + "%");    // ③ 첫 번째 ? 에 "%검색어%" 바인딩
            }

            rs = pstmt.executeQuery();                     // ④ SELECT 실행 → 결과표 받기
            while (rs.next()) {                             // 행이 있는 동안 반복(다음 행으로 커서 이동)
                list.add(mapRow(rs));                       // 한 행 → DTO 로 변환해 목록에 추가
            }
        } catch (Exception err) {
            System.out.println("getBoardList() 오류: " + err);
        } finally {
            close(con, pstmt, rs);                         // ⑤ 자원 반납 (성공/실패 무조건 실행)
        }
        return list;
    }

    /**
     * [글쓰기] 새 글 저장
     *   답변형 게시판 규칙 때문에 2단계로 처리한다.
     *   1) 기존 모든 글의 pos 를 +1 → 새 글이 맨 위(pos=0)로 오게 자리 확보
     *   2) 새 글을 pos=0, depth=0, count=0 으로 삽입
     */
    public void insertBoard(BoardDto dto) {
        Connection con = null; PreparedStatement pstmt = null;
        try {
            con = ds.getConnection();

            // 1) 기존 글 전체 pos +1 (새 글 자리 만들기)
            pstmt = con.prepareStatement("update tblBoard set pos = pos + 1");
            pstmt.executeUpdate();
            pstmt.close();   // 다음 SQL 준비 전에 현재 pstmt 닫기

            // 2) 새 글 삽입 (count/pos/depth 는 0 고정, 나머지는 ? 바인딩)
            String sql = "insert into tblBoard"
                    + "(name, email, homepage, subject, content, regdate, pass, count, ip, pos, depth) "
                    + "values(?,?,?,?,?,?,?,0,?,0,0)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getName());       // 1번째 ? ← 이름
            pstmt.setString(2, dto.getEmail());      // 2번째 ? ← 이메일
            pstmt.setString(3, dto.getHomepage());   // 3번째 ? ← 홈페이지
            pstmt.setString(4, dto.getSubject());    // 4번째 ? ← 제목
            pstmt.setString(5, dto.getContent());    // 5번째 ? ← 내용
            pstmt.setTimestamp(6, dto.getRegdate()); // 6번째 ? ← 작성시간
            pstmt.setString(7, dto.getPass());       // 7번째 ? ← 비밀번호
            pstmt.setString(8, dto.getIp());         // 8번째 ? ← IP  (count 뒤의 ip)
            pstmt.executeUpdate();                   // INSERT 실행
        } catch (Exception err) {
            System.out.println("insertBoard() 오류: " + err);
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * [상세보기용] 조회수 +1 한 뒤 해당 글을 반환.
     *   ※ 원본(Model1)의 getBoard()는 항상 조회수를 올려서,
     *     수정/삭제/답변 폼을 열 때도 조회수가 증가하는 버그가 있었다.
     *     그래서 "상세보기(readBoard)" 와 "폼/검증(getBoard)" 을 분리했다.
     */
    public BoardDto readBoard(int num) {
        Connection con = null; PreparedStatement pstmt = null; ResultSet rs = null;
        BoardDto dto = null;
        try {
            con = ds.getConnection();

            // 1) 조회수 1 증가
            pstmt = con.prepareStatement("update tblBoard set count = count + 1 where num=?");
            pstmt.setInt(1, num);
            pstmt.executeUpdate();
            pstmt.close();

            // 2) 글 1건 조회
            pstmt = con.prepareStatement("select * from tblBoard where num=?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if (rs.next()) dto = mapRow(rs);   // 결과가 있으면 DTO로 변환
        } catch (Exception err) {
            System.out.println("readBoard() 오류: " + err);
        } finally {
            close(con, pstmt, rs);
        }
        return dto;
    }

    /**
     * [폼/검증용] 조회수 증가 없이 글 1건 조회.
     *   - 수정폼·답변폼 표시, 비밀번호 검증 시 사용.
     */
    public BoardDto getBoard(int num) {
        Connection con = null; PreparedStatement pstmt = null; ResultSet rs = null;
        BoardDto dto = null;
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement("select * from tblBoard where num=?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if (rs.next()) dto = mapRow(rs);
        } catch (Exception err) {
            System.out.println("getBoard() 오류: " + err);
        } finally {
            close(con, pstmt, rs);
        }
        return dto;
    }

    /**
     * [수정] 이름·이메일·제목·내용만 변경 (비밀번호/IP/조회수 등은 그대로).
     *   - where num=? 조건이 반드시 있어야 "그 글만" 수정된다. (없으면 전체 수정 사고!)
     */
    public void updateBoard(BoardDto dto) {
        Connection con = null; PreparedStatement pstmt = null;
        try {
            con = ds.getConnection();
            String sql = "update tblBoard set name=?, email=?, subject=?, content=? where num=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getEmail());
            pstmt.setString(3, dto.getSubject());
            pstmt.setString(4, dto.getContent());
            pstmt.setInt(5, dto.getNum());     // 5번째 ? ← where 조건의 글번호
            pstmt.executeUpdate();
        } catch (Exception err) {
            System.out.println("updateBoard() 오류: " + err);
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * [삭제] 글번호에 해당하는 글 1건 삭제.
     *   - where num=? 조건 필수! (없으면 전체 삭제 대참사)
     */
    public void deleteBoard(int num) {
        Connection con = null; PreparedStatement pstmt = null;
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement("delete from tblBoard where num=?");
            pstmt.setInt(1, num);
            pstmt.executeUpdate();
        } catch (Exception err) {
            System.out.println("deleteBoard() 오류: " + err);
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * [답변 준비] 부모글보다 pos 가 큰 글들을 전부 +1.
     *   - 답변글이 부모 "바로 아래" 끼어들 빈 자리를 만들기 위함.
     *   - 예) 부모 pos=2 라면, pos 3,4,5... 인 글들을 4,5,6... 으로 밀어낸다.
     */
    public void replyUpPos(int parentPos) {
        Connection con = null; PreparedStatement pstmt = null;
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement("update tblBoard set pos = pos + 1 where pos > ?");
            pstmt.setInt(1, parentPos);
            pstmt.executeUpdate();
        } catch (Exception err) {
            System.out.println("replyUpPos() 오류: " + err);
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * [답변 저장] 답변글을 실제로 INSERT.
     *   - pos   = 부모pos + 1  → 부모 바로 아래에 위치
     *   - depth = 부모depth + 1 → 한 칸 더 들여쓰기(계층 표현)
     *   ※ 부모의 pos/depth 는 Service가 replyDto에 미리 담아 넘겨준다.
     */
    public void replyBoard(BoardDto dto) {
        Connection con = null; PreparedStatement pstmt = null;
        try {
            con = ds.getConnection();
            int pos = dto.getPos() + 1;     // 부모보다 한 단계 아래 순서
            int depth = dto.getDepth() + 1; // 부모보다 한 칸 더 들여쓰기

            String sql = "insert into tblBoard"
                    + "(name, email, homepage, subject, content, regdate, pass, count, ip, pos, depth) "
                    + "values(?,?,?,?,?,?,?,0,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getEmail());
            pstmt.setString(3, dto.getHomepage());
            pstmt.setString(4, dto.getSubject());
            pstmt.setString(5, dto.getContent());
            pstmt.setTimestamp(6, dto.getRegdate());
            pstmt.setString(7, dto.getPass());
            pstmt.setString(8, dto.getIp());
            pstmt.setInt(9, pos);       // 9번째 ? ← 계산된 pos
            pstmt.setInt(10, depth);    // 10번째 ? ← 계산된 depth
            pstmt.executeUpdate();
        } catch (Exception err) {
            System.out.println("replyBoard() 오류: " + err);
        } finally {
            close(con, pstmt, null);
        }
    }
}
