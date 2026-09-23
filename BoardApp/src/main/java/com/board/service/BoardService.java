package com.board.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.board.dao.BoardDAO;
import com.board.dto.BoardDto;

/**
 * ============================================================
 *  BoardService  (비즈니스 로직 계층)  =  MVC의 "Model" 일부
 * ============================================================
 *  [이 클래스가 하는 일]
 *   - Controller와 DAO "사이"에서 업무 규칙(판단/계산)을 처리한다.
 *       · 글쓴 시간(regdate) 세팅
 *       · 검색 컬럼 화이트리스트 검증 (SQL Injection 방어)
 *       · 수정/삭제 시 비밀번호 확인
 *       · 답변글의 pos/depth 계산 흐름 조율
 *
 *  [계층을 나누는 이유]  ★ 아주 중요
 *   - Controller : "무엇을 언제 할지 흐름 제어" (교통정리)
 *   - Service    : "무엇을 할지 = 업무 규칙"      (판단/두뇌)
 *   - DAO        : "어떻게 DB에 할지"            (손발)
 *   → 역할을 나누면, 나중에 화면이 바뀌어도 규칙 코드는 그대로 재사용 가능.
 *     (예: 웹 화면용 Controller, 모바일 API용 Controller 가 같은 Service를 공유)
 *
 *  [DAO와 Service의 경계]
 *   - "비밀번호가 맞나?" 같은 판단은 업무 규칙 → Service.
 *   - "select 해서 가져와" 같은 실행은 DB 작업 → DAO.
 * ============================================================
 */
public class BoardService {

    // Service는 DAO를 "도구"로 들고 있으면서 필요할 때 시킨다.
    private BoardDAO dao = new BoardDAO();

    // 검색을 허용할 컬럼만 미리 정해둔 목록(화이트리스트).
    //   → 사용자가 keyField 에 엉뚱한 값(악성 SQL 등)을 보내도 여기 없으면 무시.
    private static final List<String> ALLOWED_FIELDS =
            Arrays.asList("name", "subject", "content");

    /**
     * [목록] 검색 컬럼을 검증한 뒤 DAO에 목록 조회를 위임.
     *   - 허용되지 않은 검색 기준이면 → 검색을 취소하고 전체 목록을 반환.
     */
    public List<BoardDto> getBoardList(String keyField, String keyWord) {
        if (keyWord != null && !keyWord.isEmpty()) {
            // 검색어는 있는데 검색 컬럼이 허용 목록에 없으면 → 검색 무효화
            if (keyField == null || !ALLOWED_FIELDS.contains(keyField)) {
                keyWord = "";   // 빈 검색어로 만들어 전체 목록이 나오게 함
            }
        }
        return dao.getBoardList(keyField, keyWord);
    }

    /**
     * [글쓰기] 작성 시각을 지금 시각으로 찍은 뒤 저장.
     *   - "현재 시간을 언제 넣을지" 는 업무 규칙이므로 Service가 담당.
     *   - new Timestamp(System.currentTimeMillis()) : 현재 시각을 DB용 형식으로 변환.
     */
    public void write(BoardDto dto) {
        dto.setRegdate(new Timestamp(System.currentTimeMillis()));
        dao.insertBoard(dto);
    }

    /** [상세보기] 조회수 +1 하며 글을 가져온다. (DAO의 readBoard 사용) */
    public BoardDto read(int num) {
        return dao.readBoard(num);
    }

    /** [폼/검증용] 조회수 증가 없이 글을 가져온다. (수정폼·답변폼 표시용) */
    public BoardDto getBoard(int num) {
        return dao.getBoard(num);
    }

    /**
     * [수정] 비밀번호가 일치할 때만 수정한다.
     *  처리 순서:
     *   1) DB에 저장된 원본 글을 가져와 비밀번호 확인
     *   2) 사용자가 입력한 비번과 다르면 → false 반환(수정 안 함)
     *   3) 같으면 → dao.updateBoard 실행 후 true 반환
     * @return 성공 true / 비밀번호 불일치·글없음 false
     */
    public boolean update(BoardDto dto) {
        BoardDto stored = dao.getBoard(dto.getNum());   // 원본 글(비번 포함) 조회
        if (stored == null) return false;               // 글이 없으면 실패
        if (!stored.getPass().equals(dto.getPass())) {  // 저장된 비번 != 입력 비번
            return false;                               // → 수정 거부
        }
        dao.updateBoard(dto);                           // 비번 일치 → 수정 실행
        return true;
    }

    /**
     * [삭제] 비밀번호가 일치할 때만 삭제한다.
     * @return 성공 true / 비밀번호 불일치·글없음 false
     */
    public boolean delete(int num, String pass) {
        BoardDto stored = dao.getBoard(num);
        if (stored == null) return false;
        if (!stored.getPass().equals(pass)) {
            return false;
        }
        dao.deleteBoard(num);
        return true;
    }

    /**
     * [답변] 계층형 답변글 등록. 아래 4단계를 순서대로 조율한다.
     *   ① 부모글 조회 (부모의 pos/depth 를 알아야 하므로)
     *   ② 부모보다 pos 큰 글들을 +1 로 밀어내 답변글 자리 확보 (replyUpPos)
     *   ③ 답변글 dto 에 시간 + 부모의 pos/depth 를 담아둠
     *      (실제 +1 계산은 DAO의 replyBoard 안에서 수행)
     *   ④ 답변글 저장 (replyBoard)
     */
    public void reply(int parentNum, BoardDto replyDto) {
        BoardDto parent = dao.getBoard(parentNum);   // ① 부모글
        if (parent == null) return;                  // 부모 없으면 중단

        dao.replyUpPos(parent.getPos());             // ② 자리 확보

        replyDto.setRegdate(new Timestamp(System.currentTimeMillis())); // ③ 시간
        replyDto.setPos(parent.getPos());            //    부모 pos 전달(DAO에서 +1)
        replyDto.setDepth(parent.getDepth());        //    부모 depth 전달(DAO에서 +1)
        dao.replyBoard(replyDto);                    // ④ 저장
    }
}
