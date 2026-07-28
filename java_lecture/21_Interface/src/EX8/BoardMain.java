package EX8;

// [1] Board 클래스
//		- 게시글 한 건의 정보를 담는 설계도, 실무에서는 DTO 또는 VO 역할을 하는 클래스라 부른다
class Board {
	private int id;			// 게시글 글번호
	private String title;	// 게시글 제목
	private String content;	// 게시글 내용
	private String writer;	// 작성자 이름
	
	// 게시글 한 건의 정보를 초기화 시키는 생성자
	public Board(int id, String title, String content, String writer) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.writer = writer;
	}

	// Getter 역할을 하는 메소드: private 으로 만든 변수의 값을 외부로 제공하는 메소드
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getWriter() {
		return writer;
	}
	
	// Setter 역할을 하는 메소드: private 으로 만든 변수를 외부에서 매개변수를 전달 받은 새 값으로 변경하는 메소드
	public void setId(int id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	// 기능: 글 내용 변경
	public void setContent(String content) {
		this.content = content;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
}

// [2] BoardRepository 인터페이스
//		- 저장소가 지켜야 할 규칙만 정한다. 실무에서 DAO 라고 부른다.
interface BoardRepository {
	
	// 상수. public static final 이 자동으로 붙는다.
	int MAX_SIZE = 100;
	
	// 추상메소드. 글 추가 기능
	//		반환타입 boolean의 의미: 글 추가 성공 - true 반환, 글 추가 실패 - false 반환
	boolean insert(Board board);
	
	// 추상메소드. 모든 글 조회 기능
	//		반환타입 Board[]의 의미: 저장된 글 전체를 Board 배열로 반환 
	Board[] selectAll();
	
	// 추상메소드. 글 번호를 이용해서 글 한건의 정보를 조회 하는 기능
	Board selectOne(int boardId);
	
	// 추상메소드. 글 번호를 이용해서 글 한건의 정보를 수정 하는 기능
	boolean updateOne(int boardId, String newContent);
	
	// 추상메소드. 글 번호를 이용해서 글 한건의 정보를 삭제하는 기능
	boolean deleteOne(int boardId);
}

// [3] MemoryBoardRepository 클래스
//		- 배열(DB 공간으로 사용)에 저장하는 저장소. 추상메소드 5개를 오버라이딩 한다.
class MemoryBoardRepository implements BoardRepository {
	
	// 글을 여러 건을 담아 둘 배열. 크기는 인터페이스의 상수값 100을 사용
	private Board[] boards = new Board[MemoryBoardRepository.MAX_SIZE];
	
	// 실제로 채워진 칸의 갯수 저장할 변수 만들기
	private int count = 0;
	
	@Override
	public boolean insert(Board board) {
		return false;
	}

	@Override
	public Board[] selectAll() {
		return null;
	}

	@Override
	public Board selectOne(int boardId) {
		return null;
	}

	@Override
	public boolean updateOne(int boardId, String newContent) {
		return false;
	}

	@Override
	public boolean deleteOne(int boardId) {
		return false;
	}
}

public class BoardMain {

	public static void main(String[] args) {
		
	}

}
