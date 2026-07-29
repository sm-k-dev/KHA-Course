package EX8;

//[2] BoardRepository 인터페이스 
//- 저장소가 지켜야할 규칙만 정한다.  실무에서  DAO 라고 부른다.
public interface BoardRepository {

	//상수. public static final 이 자동으로 붙는다.
	int MAX_SIZE = 100;

	//추상메소드. 글추가 기능
	//반환타입 boolean의 의미 : 글추가에 성공하면 true반환, 실패하면 false반환
	boolean insert(Board board);

	//추상메소드. 모든글 조회 기능
	//반환타입 Board[]의 의미 : 저장된 글 전체를 Board배열로 반환  
	Board[] selectAll();

	//추상메소드. 글 번호로 글 한건 정보를 조회 기능
	Board selectOne(int boardId);

	//추상메소드. 글 번호에 해당하는 글 한건 정보중 글내용 수정하는 기능
	boolean update(int boardId, String newContent);

	//추상메소드. 글 한건 삭제 하는 기능
	boolean delete(int boardId);

}