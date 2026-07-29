package EX8;


//===============================================================
//5. BoardServiceImpl 클래스 (부장)
// 업무 규칙의 실제 코드. 추상메소드 5개를 오버라이딩한다.
// default 메소드는 구현하지 않아도 상속받아 사용한다.
//===============================================================
public class BoardServiceImpl implements BoardService{

	//HAS-A 관계 : BoardServiceImpl객체가  
	//            MemoryBoardRepository 객체(새글 정보 저장 - 사원)를 변수에 저장하여 포함하는 관계
	private BoardRepository  repository;
	
	//생성자로 MemoryBoardRepository객체 초기화
	public BoardServiceImpl(BoardRepository  repository) {
		this.repository = repository;
	}
	
	//새 게시글 등록 메소드 
	@Override 
	public void register(Board board) { // <- new Board(1, "첫 글", "내용 입니다", "홍길동" );
										// <- new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수")
										// <- new Board(3, "    ", "제목이 공백뿐입니다", "이영희")
		
		//인터페이스로 부터 상속받은 default메소드를 호출해서 사용
		printTitle("게시글 등록");
		
		//static 메소드는 인터페이스명을 앞에 붙여 호출한다
		//글제목이 부적합 하면 아래 블록이 실행된다."인터페이스 질문"
		if(BoardService.isVaildTitle(board.getTitle()) == false) {			
			System.out.println("글 등록 실패 : 글 제목을 입력해야 합니다.");			
			return;  //register 메소드 종료 
		}
		
		//저장소(new MemoryBoardRepository())에 새글 정보 저장을 요청하고 저장 여부를 바로 판단한다
		// insert 메소드가 true를 돌려주면 성공이다.
		if( repository.insert(board)  ) {		
			System.out.println("새 글 등록 성공 : " +  board.getId() + "번 - " + board.getTitle() );
		}else {
			System.out.println("새 글 등록 실패 : 저장 공간이 가득 찼습니다.");
		}
		
	}//==============  register 메소드 끝 

	
	// 새글 목록 보기 
	@Override
	public void showList() {
		
		//인터페이스로 부터 상속받은 default메소드를 호출해서 사용
		printTitle("전체 목록");
		
		//저장소 (MemoryBoardRepository 객체)에서  추가된 새글 2건의 정보를 배열에 담아 받아옵니다
		Board[] boards = repository.selectAll();
		
		//boards배열의 길이가 0이면 저장된 글이 없다는 뜻이므로 출력해주자
		if(boards.length == 0) {
			System.out.println("등록된 글이 없습니다");
			return; //아래의 for반복문을 실행하지 않고   showList()메소드 종료 
		}
		
		//length 는 배열의 칸 수다.  여기서는 글 개수와 같다.
		System.out.println("총 " + boards.length + "건");
		
		//받아온 배열을 처음부터 끝까지 반복해서 추가된 글들을 얻어 옵니다.
		for(Board board :  boards) {
			
			//추가된 글의 정보를  반복해서 하나씩 출력
			System.out.println(board.getId() + " | " + board.getTitle() + " | " + board.getWriter());	
		}	
	}

	
	// 글 한건 상세 보기 
	@Override
	public void showDetail(int boardId) {
		printTitle("상세 보기");
		
		//매개변수 boardId로 받은 글번호 2 로 글을 찾아 받아옵니다.
		//방법. MemoryBoardRepository 객체의 selectOne(2); 메소드 호출해서 명령한다.
		Board found = repository.selectOne(boardId);
		          //= new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수");
				  //= null;
		
		
		//못 찾으면 null 이 들어 있다
		//확인 없이  found.getId()를 사용하면  실행중에 NullPointerException 오류가 발생한다
		if(found == null) {
			System.out.println(boardId + "번 글을 찾을 수 없습니다");
			return;
		}
		//찾으면 글의 정보를 항목별로 출력한다
		System.out.println("글번호 : " +  found.getId());
		System.out.println("제목  : " +  found.getTitle());
		System.out.println("작성자 : " +  found.getWriter());
		System.out.println("내용  : " +  found.getContent());
	}

	// 글 한건의 글내용 수정 
	@Override
	public void modify(int boardId, String newContent) {
		
		printTitle("글 수정");
		
		//저장소에 수정을 요청하고 그 결과를 반환 받아와 판단한다
		if(repository.update(boardId, newContent) ) {			
			System.out.println(boardId + "번 글을 수정했습니다.");			
		}else {			
			System.out.println(boardId + "번 글을 찾을 수 없습니다.");
		}
	}

	// 글 한건 정보 삭제
	@Override
	public void remove(int bardId) {
		printTitle("글 삭제");
		
		//저장소에 삭제를 요청하고 그 결과를 판단한다
		if(repository.delete(bardId)) {
			
			System.out.println(bardId + "번 글을 삭제 했습니다.");
			
		}else {//삭제 실패 했을 경우 
			System.out.println(bardId + "번 글을 찾을수 없습니다.");
		}
	}

}
