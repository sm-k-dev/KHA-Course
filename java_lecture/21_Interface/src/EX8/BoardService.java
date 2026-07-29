package EX8;

//===============================================================
//4. BoardService 인터페이스
// 업무 규칙을 정한다. 무엇을 검사하고 무엇을 출력할지 결정한다.
//===============================================================
public interface BoardService {

//추상메소드 역할 :  게시판이 제공하는 기능 목록
	
	//기능1. 새 게시글 등록
	void register(Board board);
	
	//기능2. 게시글 목록 보기 
	void showList();
	
	//기능3. 게시글 한건 보기 
	void showDetail(int boardId);
	
	//기능4. 게시글 수정
	void modify(int boardId, String newContent);
	
	//기능5. 게시글 삭제 
	void remove(int bardId);
	
	//default 메소드 - 구현부를 가질 수 있고, 구현 자식클래스에 그대로 상속되는 메소드
	//기능6. 위 다섯 기능 이 모두 같은 글제목 줄을 출력하게 구현
	default void printTitle(String title) {
		
		line(); //아래에 있는 private 메소드를 호출한다.
		
		System.out.println("[" + title + "]");  //제목을 대괄호로 감싸서 출력한다.
		
		line();  //구분선을 한번 더 출력하기 위해 아래의 line()메소드를 호출한다.
	}
	
	//private 메소드 
	//- 인터페이스 안에서만 사용되는 메소드 이고,  구현 자식클래스에 상속도지 않는다.
	//- default 메소드의 중복코드를 줄이는 용도의 메소드이다.
	private void line() {
		System.out.println("-------------------------------");
	}
	
	//static 메소드
	//- 객체 생성 없이 "인터페이스명.메소드명()" 형태로 호출해서 사용하는 메소드.
	//- 어느 자식 구현클래스에서든 똑같이 쓰이는 글 제목 검사 규칙을 이 메소드에 정의해 준다.
	static boolean  isVaildTitle(String title) {
									//"인터페이스 질문"
									//"    "
		
		//매개변수 title 로 받은 글제목이 없을 경우 먼저 확인한다.
		if(title == null) {
			return false;
		}
		//매개변수 title 로 글제목을 받았다면  trim() 메소드로 글제목에 양쪽공백을 제거후 
		//글제목이 없을 경우 한번더 확인한다.
		if(title.trim().length() == 0) {
			return false;
		}
		
		return true; //위 두 검사를 모두 통과하면 적합한 제목임을 알리자 
	}
	
} //------------> interface  Board 끝
