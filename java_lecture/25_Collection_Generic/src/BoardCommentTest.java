import java.util.Map;       //Map인터페이스
import java.util.HashMap;   //Map부모인터페이스를 구현한 자식 HashMap클래스
import java.util.List;      //List인터페이스
import java.util.ArrayList; //List부모인터페이스를 구현한 자식 ArrayList클래스

//===================================================================
//[응용 문제] DB에서 조회한 글 1개 + 그 글에 달린 모든 댓글 출력하기
//===================================================================
//※ 이 문제는 순수 자바 문법만 사용합니다. (JSP, JDBC 사용 안함)
//
//[배경 설명]
//게시판에서 글 제목을 클릭하면 "상세보기 화면" 이 나옵니다.
//상세보기 화면에는 두 가지 데이터가 필요합니다.
//
//  1. 클릭한 글 1개의 정보          → DB 의 글 테이블에서 조회
//  2. 그 글에 달린 모든 댓글 정보    → DB 의 댓글 테이블에서 조회
//
//[DB 글 테이블 조회 결과 (가정) - 2번 글 1개]
//   no   |  title        |  writer   |  content              |  hit
//  ──────┼───────────────┼───────────┼───────────────────────┼──────
//   2    |  "과제 제출"    |  "이학생"  |  "3장 과제 제출합니다"   |  42
//
//[DB 댓글 테이블 조회 결과 (가정) - 2번 글에 달린 댓글 3개]
//   commentNo  |  boardNo  |  writer   |  content
//  ────────────┼───────────┼───────────┼──────────────────────
//   1          |  2        |  "김학생"  |  "저도 방금 제출했어요"
//   2          |  2        |  "박학생"  |  "기한이 언제까지인가요?"
//   3          |  2        |  "선생님"  |  "확인했습니다 수고했어요"
//
//자바에서 저장하는 구조
//  - 글 1개(행 1개)          → HashMap<String, Object> 1개
//  - 댓글 1개(행 1개)        → HashMap<String, Object> 1개
//  - 댓글 여러 개(행 여러 개) → ArrayList 배열에 댓글 HashMap 여러 개를 담음
//
//[실무 포인트 - 효율적인 데이터 관리]
//글 HashMap 과 댓글 ArrayList 를 따로따로 변수 2개로 들고 다니면
//데이터를 전달할 때마다 2개를 같이 전달해야 해서 불편하고 실수하기 쉽습니다.
//→ 그래서 실무에서는 글 HashMap 안에
//  "commentList" 라는 key 로 댓글 ArrayList 전체(주소)를 value 로 저장해서
//  『글 + 댓글』을 하나의 데이터 묶음으로 만들어 관리합니다.
//→ 이렇게 하면 HashMap 1개만 전달해도 글과 댓글이 전부 따라갑니다.
//
//[요구사항]
// 1단계 : 아래에 미리 만들어져 있는 selectBoard 메소드를 호출해서 (글번호 2 전달)
//         반환된 글 1개 HashMap 주소를
//         HashMap<String, Object> 타입 참조변수 board 에 저장하세요.
//
// 2단계 : board 에서 no, title, writer, content, hit 를 꺼내
//         아래 형태로 출력하세요.
//         (Object 로 반환되므로 다운캐스팅 해서 변수에 저장 후 출력할 것)
//
//         ==================== 글 상세보기 ====================
//         글번호 : 2
//         제목 : 과제 제출
//         작성자 : 이학생
//         내용 : 3장 과제 제출합니다
//         조회수 : 42
//
// 3단계 : 아래에 미리 만들어져 있는 selectCommentList 메소드를 호출해서 (글번호 2 전달)
//         반환된 댓글 목록 ArrayList 주소를
//         List<HashMap<String, Object>> 타입 참조변수 commentList 에 저장하세요.
//
// 4단계 : [실무 포인트]
//         board(글 HashMap) 안에 "commentList" 라는 key 로
//         commentList(댓글 ArrayList 주소)를 value 로 저장해서
//         『글 + 댓글』을 하나의 데이터 묶음으로 만드세요.
//
// 5단계 : board 에서 "commentList" key 로 댓글 ArrayList 를 다시 꺼내서
//         (다운캐스팅 필요!)
//         List<HashMap<String, Object>> 타입 참조변수 list 에 저장한 후
//         size 메소드를 사용해 아래 형태로 출력하세요.
//
//         ==================== 댓글 (3개) ====================
//
// 6단계 : for 반복문으로 list 에서 댓글 HashMap 을 하나씩 꺼내
//         commentNo, writer, content 를 아래 형태로 출력하세요.
//
//         1 | 김학생 : 저도 방금 제출했어요
//         2 | 박학생 : 기한이 언제까지인가요?
//         3 | 선생님 : 확인했습니다 수고했어요
//===================================================================

public class BoardCommentTest {

	//-------------------------------------------------------------------
	//DB의 글 테이블에서 글번호(no)에 해당하는 글 1개를 조회해 왔다고 가정하고
	//글 1개(행 1개)를 HashMap 으로 만들어 반환하는 메소드
	//※ 이 메소드는 수정하지 말고 그대로 사용하세요.
	//-------------------------------------------------------------------
	public static HashMap<String, Object> selectBoard(int no) {

		//글 1개(행 1개)를 저장할 HashMap 생성
		HashMap<String, Object> board = new HashMap<String, Object>();

		//글번호 2번 글의 정보 (DB 에서 조회해 왔다고 가정)
		board.put("no", no);
		board.put("title", "과제 제출");
		board.put("writer", "이학생");
		board.put("content", "3장 과제 제출합니다");
		board.put("hit", 42);

		//글 1개가 담긴 HashMap 주소 반환
		return board;
	}

	//-------------------------------------------------------------------
	//DB의 댓글 테이블에서 글번호(boardNo)에 달린 모든 댓글을 조회해 왔다고 가정하고
	//댓글 목록을 ArrayList<HashMap> 구조로 만들어 반환하는 메소드
	//※ 이 메소드는 수정하지 말고 그대로 사용하세요.
	//-------------------------------------------------------------------
	public static List<HashMap<String, Object>> selectCommentList(int boardNo) {

		//댓글 여러 개(행 여러 개)를 담을 ArrayList 배열 생성
		List<HashMap<String, Object>> commentList = new ArrayList<HashMap<String, Object>>();

		//댓글 1 : commentNo=1, writer="김학생", content="저도 방금 제출했어요"
		HashMap<String, Object> comment1 = new HashMap<String, Object>();
		comment1.put("commentNo", 1);
		comment1.put("boardNo", boardNo);
		comment1.put("writer", "김학생");
		comment1.put("content", "저도 방금 제출했어요");
		commentList.add(comment1);

		//댓글 2 : commentNo=2, writer="박학생", content="기한이 언제까지인가요?"
		HashMap<String, Object> comment2 = new HashMap<String, Object>();
		comment2.put("commentNo", 2);
		comment2.put("boardNo", boardNo);
		comment2.put("writer", "박학생");
		comment2.put("content", "기한이 언제까지인가요?");
		commentList.add(comment2);

		//댓글 3 : commentNo=3, writer="선생님", content="확인했습니다 수고했어요"
		HashMap<String, Object> comment3 = new HashMap<String, Object>();
		comment3.put("commentNo", 3);
		comment3.put("boardNo", boardNo);
		comment3.put("writer", "선생님");
		comment3.put("content", "확인했습니다 수고했어요");
		commentList.add(comment3);

		//댓글 목록이 담긴 ArrayList 배열 주소 반환
		return commentList;
	}

	public static void main(String[] args) {

		//1단계 : selectBoard 메소드 호출해서 (글번호 2 전달) 글 1개 HashMap 주소 저장



		//2단계 : board 에서 글 정보 꺼내 상세보기 출력 (다운캐스팅 필수)



		//3단계 : selectCommentList 메소드 호출해서 (글번호 2 전달) 댓글 목록 ArrayList 주소 저장



		//4단계 : [실무 포인트] board 안에 "commentList" key 로 댓글 목록 통째로 저장



		//5단계 : board 에서 "commentList" key 로 댓글 목록 다시 꺼내 댓글 갯수 출력



		//6단계 : for 반복문으로 댓글 전체 출력



	}//----- main
}//--- class

/*
 전체 출력 결과 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 
==================== 글 상세보기 ====================
글번호 : 2
제목 : 과제 제출
작성자 : 이학생
내용 : 3장 과제 제출합니다
조회수 : 42
==================== 댓글 (3개) ====================
1 | 김학생 : 저도 방금 제출했어요
2 | 박학생 : 기한이 언제까지인가요?
3 | 선생님 : 확인했습니다 수고했어요
*/


