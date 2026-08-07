/*
	주제 : 컬렉션 + 제네릭 응용 - 게시판 글 목록 만들기 (JSP 웹개발 핵심 패턴 미리보기)

	■ 이 예제를 배우는 이유 (JSP/Spring 연결 - 매우 중요!)

	   웹 게시판의 동작 순서는 다음과 같다.

	     1. DB의 게시판 테이블에서 글들을 조회한다 (글 1줄 = 행 1개)
	     2. 조회된 행 하나하나를 "자바 객체" 하나하나로 만든다        <- DTO 클래스
	     3. 그 객체들을 가변 배열에 순서대로 담는다                  <- ArrayList<DTO>
	     4. JSP 화면에서 배열을 반복문으로 돌며 <table>에 출력한다    <- 향상된 for문

	   즉 "내가 만든 클래스를 제네릭 타입으로 지정한 ArrayList"가
	   DB와 웹 화면 사이에서 데이터를 나르는 그릇이 된다.
	   JSP/Spring 수업 내내 이 패턴을 매일 쓰게 되므로 지금 확실히 익혀 두자.

	■ 용어 정리
	   DTO (Data Transfer Object) : 데이터를 담아 나르는 용도로만 만드는 클래스.
	       DB 테이블의 컬럼(열) 하나가 DTO의 멤버 변수 하나에 대응된다.
	       (현장에 따라 VO, Bean 이라고도 부른다)

	■ 지금까지와 달라지는 점
	   지금까지 제네릭 <> 안에는 String, Integer처럼 자바가 만들어 둔 클래스만 넣었다.
	   -> 이번에는 "내가 직접 만든 BoardDTO 클래스"를 넣는다!
	      List<BoardDTO> : BoardDTO 객체만 저장할 수 있는 가변 배열
*/
//─────────────────────────────────────────────────────────────
// 게시판 글 1건을 표현하는 DTO 클래스
//
// DB의 board 테이블                     BoardDTO 클래스
//   no    (글 번호 컬럼)      <->        int    no
//   title (제목 컬럼)         <->        String title
//   writer(작성자 컬럼)       <->        String writer
//   hit   (조회수 컬럼)       <->        int    hit
//
// -> 테이블의 행(row) 1개  =  BoardDTO 객체 1개
//─────────────────────────────────────────────────────────────

import java.util.ArrayList;
import java.util.List;

class BoardDTO {	
	//멤버변수 : DB 컬럼 하나가 변수 하나에 대응된다.
	//private :  외부에서 변수에 직접 접근 하지 못하게 막고 (정보은닉)
	//           아래의 getter 역할을 하는 메소드를 통해서만 값을 꺼내게 한다.(DTO 작성 표준)
	private	int		no;		//글 번호 
	private	String	title;	//글 제목
	private	String	writer;	//작성자
	private	int		hit;	//조회수
	
	//생성자 : 글 1건의 데이터 4개를 전달받아 멤버 변수에 저장하며 객체를 만든다.
	public BoardDTO(int no, String title, String writer, int hit) {
		super();
		this.no		= no;
		this.title	= title;
		this.writer	= writer;
		this.hit	= hit;
	}
	//getter 메소드들 : private 멤버 변수의 값을 외부( .jsp 화면 등)에서 꺼낼때 사용
	public int getNo() { return no; }
	public String getTitle() { return title; }
	public String getWriter() { return writer; }
	public int getHit() { return hit; }
	
	//글조회수를 1올리는 메소드 (상세보기를 할 때마다 호출된다)
	public void increaseHit() {
		hit = hit + 1;
	}	
}

public class BoardListExample {

	public static void main(String[] args) {
		//=============================================================
		//1. 글 목록을 담을 가변 배열 생성 : 제네릭에 "내가 만든 클래스명을 지정"
		//=============================================================
		
		//BoardDTO 객체만 저장할 수 있는 ArrayList 가변 배열 생성 (업캐스팅)
		//- <BoardDTO> 지정으로 add는 BoardDTO만 받고, get은 BoardDTO로 반환한다
		List<BoardDTO>  boardList = new ArrayList<BoardDTO>();
		
		//============================================================================================
		//2. 글 3건을 BoardDTO 객체로 만들어 ArrayList 배열에 추가해서 각각 저장 (DB에서 3행을 조회해 온 상황을 흉내)
		//============================================================================================
		
		//글 1건의 데이터가 저장된 class BoardDTO 의 객체를 생성해서 ArrayList배열에 저장
		boardList.add( new BoardDTO(1, "가입 인사드립니다", "kim", 5) );
		boardList.add( new BoardDTO(2, "자바 질문 있어요", "lee", 3) );
		boardList.add( new BoardDTO(3, "스터디 모집합니다", "park", 3) );
		
		//============================================================================================
		//3. 글 목록 전체 출력 ( JSP 에서 <table> 에 박복 출력하는 처리)
		//============================================================================================
		System.out.println("번호\t제목\t\t\t작성자\t조회수");
		System.out.println("------------------------------------------------------------------");
		
		// 향상된 for 문: boardList 참조변수에 저장된 ArrayList 배열에서 BoardDTO 객체를 하나씩 꺼내 dto 변수에 담아가며 반복
		for ( BoardDTO dto : boardList ) {
			
			// ArrayList 배열에서 꺼낸 BoardDTO 객체의 getter 메소드를 호출해 글 1건의 각 컬럼 값을 출력한다.
			System.out.println( dto.getNo() + "\t" + dto.getTitle() + "\t\t" + dto.getWriter() + "\t" + dto.getHit() );
			
		}
		System.out.println();
		
		//============================================================================================
		//4. 상세보기 : 글 번호로 글 1건 찾기 + 조회수 증가
		//				(게시판에서 제목을 클릭하면 일어나는 일을 흉내)
		//============================================================================================
		int	targetNo = 2; // 사용자가 클릭한 글 번호 (JSP에서는 파라미터로 전달받는 값)
		
		System.out.println("==== " + targetNo + "번 글 상세보기 ====");
		
		// ArrayList 배열을 돌면서 글 번호가 일치하는 BoardDTO 객체를 찾는다
		for ( BoardDTO dto : boardList ) { // [ 글번호1 BoardDTO, 글번호2 BoardDTO, 글번호3 BoardDTO ]
			
			// ArrayList 배열에서 꺼낸 BoardDTO 객체의 글번호가 찾는 글번호 2의 글과 같다면
			if ( dto.getNo() == targetNo ) {
				
				// 상세보기가 일어났으므로 글 조회수 1 증가
				dto.increaseHit(); // hit 인스턴스 변수 값 1 누적 해서 글 조회수 1 올릴 수 있다.
				
				// 2번 글번호의 글 상세 내용 출력
				System.out.println("제목 : " + dto.getTitle() );
				System.out.println("작성자 : " + dto.getWriter() );
				System.out.println("내용 : " + dto.getHit() );
			}
			
		}
		System.out.println();
		
		//============================================================================================
		//5. 수정이 진짜 반영되었는지 글목록 다시 출력해서 확인 <= DB의 테이블에 저장된 모든 글을 조회
		//============================================================================================
		System.out.println("==== 상세보기 후 목록 다시 출력 ====");
		System.out.println("번호\t제목\t\t\t작성자\t조회수");
		System.out.println("------------------------------------------------------------------");
		
		// 향상된 for 문: boardList 참조변수에 저장된 ArrayList 배열에서 BoardDTO 객체를 하나씩 꺼내 dto 변수에 담아가며 반복
		for ( BoardDTO dto : boardList ) {
			
			// ArrayList 배열에서 꺼낸 BoardDTO 객체의 getter 메소드를 호출해 글 1건의 각 컬럼 값을 출력한다.
			System.out.println( dto.getNo() + "\t" + dto.getTitle() + "\t\t" + dto.getWriter() + "\t" + dto.getHit() );
			
		}
		
		/*
		결론 1 : 제네릭 <> 안에는 내가 만든 클래스도 지정할 수 있다.
		         List<BoardDTO> = "게시판 글 객체만 담는 가변 배열"
		         -> DB의 행들을 객체로 만들어 나르는 웹개발 표준 패턴이다.

		결론 2 : DTO = DB 컬럼을 멤버 변수로 대응시킨 데이터 운반용 클래스.
		         private 변수 + 생성자 + getter 구조로 작성한다.

		결론 3 : 배열 칸에는 객체의 "주소"가 저장되므로,
		         꺼낸 객체를 수정하면 배열에서 다시 꺼내도 수정되어 있다. (같은 객체!)

		결론 4 : JSP 미리보기 - 곧 배울 코드에서는
		           request.setAttribute("list", boardList);   <- 서블릿이 배열을 화면에 전달
		         하면 JSP가 이 배열을 받아 반복문으로 <table>을 만든다.
		         오늘 만든 3번(목록 출력) 반복문이 바로 그 자리에 들어간다.
		 */	
	}

}
