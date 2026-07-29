package EX8;


//===============================================================
//6. BoardController 클래스 (사장)
// 클라이언트의 글 기능중 하나의 요청을 받아 Service 클래스(부장) 에게 넘긴다. 직접 일하지 않는다.
//===============================================================
public class BoardController {

	//HAS-A 관계 : Controller 가 Service 클래스를 포함하고 있는 관계 
	private BoardService service; //<----  new BoardServiceImpl(); 저장 될것이다.
	
	//생성자로 new BoardServiceImpl(); 부장객체 초기화
	public BoardController(BoardService  service) {
		this.service = service;
	}
	
	//5. 글 번호에 관한 글 한쌍의 정보 삭제요청을 클라이언트로부터 받아  new BoardServiceImpl 부장객체에게 넘겨서 처리하게 한다
	public void requestRemove(int boardId) {
		service.remove(boardId);
	}
	
	//4. 글 번호에 관한 글의 글내용 수정요청을 클라이언트로부터 받아   new BoardServiceImpl 부장객체에게 넘겨서 처리하게 한다
	public void requestModify(int boardId, String newContent) {
		service.modify(boardId, newContent);
	}
	
	//3. 글 한건 상세정보 요청을 클라이언트로 부터 받아 new BoardServiceImpl 부장객체에게 넘겨서 처리하게 한다
	public void requestDetail(int boardId) {        
		service.showDetail(boardId);
	}
		
	//2. 글 목록 요청을 클라이언트로 부터 받아 new BoardServiceImpl 부장객체에게 넘겨서 처리하게 한다
	public void requestList() {
		this.service.showList();
	}
	
	//1.새 게시글 등록 요청을 클라이언트로 부터 받아 new BoardServiceImpl부장객체에게  넘긴다
	public void requestRegister(Board  board) { //<- new Board(1, "첫 글", "내용 입니다", "홍길동" );
												//<- new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수")
												//<- new Board(3, "    ", "제목이 공백뿐입니다", "이영희")
		this.service.register(board);
	}
	
}
