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
		HashMap<String, Object> board = selectBoard(2);


		//2단계 : board 에서 글 정보 꺼내 상세보기 출력 (다운캐스팅 필수)
		
		//board.get("no") → Object 타입으로 new Integer(2) 반환
		//→ (Integer) 다운캐스팅 → int 변수 저장(오토언박싱)
		int no = (Integer)board.get("no");			// 글 번호 
		
		//board.get("title") → Object 타입으로 "과제 제출" String 객체 반환
		//→ (String) 다운캐스팅 해야 String 변수에 저장 가능
		//※ 다운캐스팅 없이  String title = board.get("title");  → 컴파일 에러!
		String title = (String)board.get("title");   // 글제목 
		String writer = (String)board.get("writer"); // 작성자
		String content = (String)board.get("content"); //글 내용
		int hit = (Integer)board.get("hit");		   //글 조회수 
		
		System.out.println("================== 글 상세보기 ================");
		System.out.println("글번호 : " + no);   //글번호 : 2
		System.out.println("제목 : " + title); //제목 : 과제 제출
		System.out.println("작성자 : " + writer);//작성자 : 이학생
		System.out.println("내용 : " + content);//내용 : 3장 과제 제출합니다
		System.out.println("조회수 : " + hit);   //조회수 : 42
		


		//3단계 : selectCommentList 메소드 호출해서 (글번호 2 전달) 댓글 목록 ArrayList 주소 저장
		List<HashMap<String, Object>> commentList = selectCommentList(2);
		/*
		selectCommentList(2)
		→ "2번 글에 달린 댓글만 조회해 달라" 는 의미로 글번호 2 를 전달
		→ 댓글 3개(HashMap 3개)가 담긴 ArrayList 배열 주소를 반환 받음

		 [Stack]                     [Heap]
		┌──────────────┐    ┌─────────────────────────┐
		│ commentList  │───▶│ ArrayList (댓글 3개)      │
		└──────────────┘    │  0번 ─▶ 댓글1 HashMap    │
		                    │  1번 ─▶ 댓글2 HashMap    │
		                    │  2번 ─▶ 댓글3 HashMap    │
		                    └─────────────────────────┘
		*/		


		//4단계 : [실무 포인트] board 안에 "commentList" key 로 댓글 목록 통째로 저장
		/*
		왜 이렇게 하는가? (효율적인 데이터 관리)

		board 변수와 commentList 변수를 따로따로 2개 들고 다니면
		→ 다른 메소드에 전달할 때마다 2개를 같이 전달해야 하고
		→ 글은 전달했는데 댓글 전달을 깜빡하는 실수가 생긴다.

		글 HashMap 안에 댓글 ArrayList "주소"를 value 로 저장해 두면
		→ board 하나만 전달해도 글과 댓글이 전부 따라간다!
		→ 『글 + 댓글』이 하나의 데이터 묶음이 된다.
		*/
		board.put("commentList", commentList);
		/*
		put 이 가능한 이유
		→ board 의 value 타입은 Object 로 정해져 있다.
		→ ArrayList 객체도 결국 Object 의 자식이므로
		  ArrayList 배열의 "주소"가 Object 타입 value 자리에 업캐스팅되어 저장된다.
		※ 댓글 데이터가 복사되어 들어가는 것이 아니라 주소만 저장된다!

		[Heap]  4단계 실행 후 완성된 전체 구조 (실무 핵심 그림!)
		┌──────────────────────────────┐
		│ 글 HashMap (board)            │
		│──────────────────────────────│
		│ "no"          : 2            │
		│ "title"       : "과제 제출"    │
		│ "writer"      : "이학생"      │
		│ "content"     : "3장 과제..."  │
		│ "hit"         : 42           │
		│ "commentList" : 주소 ────────┼────┐
		└──────────────────────────────┘    │
		                                    ▼
		                     ┌─────────────────────────┐
		                     │ ArrayList (댓글 목록)      │
		                     │  0번 ─▶ 댓글1 HashMap     │
		                     │         {commentNo:1,   │
		                     │          writer:김학생,   │
		                     │          content:저도... }│
		                     │  1번 ─▶ 댓글2 HashMap     │
		                     │         {commentNo:2,   │
		                     │          writer:박학생,   │
		                     │          content:기한... }│
		                     │  2번 ─▶ 댓글3 HashMap     │
		                     │         {commentNo:3,   │
		                     │          writer:선생님,   │
		                     │          content:확인... }│
		                     └─────────────────────────┘
		- 글 HashMap 이 댓글 ArrayList 를 품고 있는 구조
		- board 주소 하나만 있으면 글도 꺼내고 댓글도 전부 꺼낼 수 있다!
		*/		
		

		//5단계 : board 에서 "commentList" key 로 댓글 목록 다시 꺼내 댓글 갯수 출력
		List<HashMap<String, Object>>  list  = (List<HashMap<String,Object>>)board.get("commentList");
		/*
		실행 순서
		1. board.get("commentList")
		   → key "commentList" 와 연결되어 저장된 value 반환
		   → 저장할 때 Object 타입으로 업캐스팅되어 저장했으므로
		     Object 타입으로 ArrayList 배열 "주소"가 반환된다.
		2. (List<HashMap<String, Object>>) 다운캐스팅
		   → Object 타입을 원래 타입인 List<HashMap<String, Object>> 로 되돌림
		   → 문자열은 (String), 정수는 (Integer) 로 되돌렸듯이
		     ArrayList 는 (List<HashMap<String, Object>>) 로 되돌린다. (원리는 똑같다!)
		3. list 참조변수에 ArrayList 배열 주소 저장

		※ 새로운 ArrayList 가 만들어지는 것이 아니다!
		  3단계의 commentList 와 5단계의 list 는
		  Heap 에 있는 "같은" ArrayList 배열 주소를 가리킨다.

		※ 이클립스에서 이 줄에 노란 밑줄(unchecked 경고)이 표시될 수 있다.
		  → 에러가 아니라 "다운캐스팅한 타입이 정말 맞는지 자바가 100% 확인할 수 없다" 는 알림일 뿐이다.
		  → 우리가 4단계에서 직접 ArrayList 를 저장했으므로 타입이 맞다는 것을 알고 있다. 무시하고 진행해도 된다.
		*/


		System.out.println("==================== 댓글 (3개) ======================");
		
		//6단계 : for 반복문으로 댓글 전체 출력
		//list.size() → 3 반환 → i 는 0, 1, 2 까지만 반복
		for(int i=0;   i<list.size();   i++) {
			
			//1. ArrayList 배열의 i번 index 칸에 저장된 댓글(HashMap) 주소를 꺼내 저장
			HashMap<String, Object>  comment = list.get(i);
			//i=0 → 댓글1 HashMap {commentNo=1, boardNo=2, writer=김학생, content=저도 방금 제출했어요}
			//i=1 → 댓글2 HashMap {commentNo=2, boardNo=2, writer=박학생, content=기한이 언제까지인가요?}
			//i=2 → 댓글3 HashMap {commentNo=3, boardNo=2, writer=선생님, content=확인했습니다 수고했어요}
			
			//2. 댓글(HashMap)에서 컬럼이름(key)으로 데이터(value) 꺼내기 (다운캐스팅 필수)
		 	int commentNo = (Integer)comment.get("commentNo");
		 	//i=0 → 1     i=1 → 2     i=2 → 3
		 	
		 	String commentWriter = (String)comment.get("writer");
		 	//i=0 → "김학생"     i=1 → "박학생"     i=2 → "선생님"
		 	
		 	String commentContent = (String)comment.get("content");
			//i=0 → "저도 방금 제출했어요"
			//i=1 → "기한이 언제까지인가요?"
			//i=2 → "확인했습니다 수고했어요"
		 	
		 	//3. 꺼낸 데이터들을 댓글 한 줄 형태로 출력
		 	System.out.println(commentNo + " | " + commentWriter + " | " + commentContent);
		 	/*
		 	1 | 김학생 : 저도 방금 제출했어요        <- 반복 1 (i=0)
		 	2 | 박학생 : 기한이 언제까지인가요?      <-  반복 2 (i=1)
		 	3 | 선생님 : 확인했습니다 수고했어요      <-  반복 3 (i=2)
			*/
		
		} //for
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


