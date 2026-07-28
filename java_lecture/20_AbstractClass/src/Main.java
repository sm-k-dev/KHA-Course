
/*
게시판 기능 구현에 관한  미완성 설계도(추상클래스) 만들기 
추상클래스명 : Board
질문1. 추상클래스는 무엇인가요?
답변1. 메소드 기능 구현이 완벽히 작성되지 않은 추상메소드를 포함하고 있는 미완성 클래스이며,
	  상속을 통해서만 자식클래스를 새롭게 만들 수 있게 도와 주는 클래스가 추상클래스 입니다.
*/
abstract class Board { 	
	/* 글제목, 글내용, 글작성자명, 글작성날짜 를 저장할 변수들 선언
		질문2. protected 키워드는 무엇인가요?
		답변2. 동일한 패키지나 상속받은 자식 클래스에서만 변수에 접근할수 있도록 하는 접근제어자 종류중 하나  */
	protected String title, content, author, date;	
	
	/*생성자 역할 : 자식클래스의 객체 메모리 생성시 글제목, 글내용, 글작성자명, 글작성날짜를 모두 초기화 */
	public Board(String title, String content, String author, String date) {
		//질문3. this 키워드는 왜 사용하나요?
		//답변3. 생성자의 매개변수이자 지역변수와 !!  객체변수의 이름이 같기떄문에
		//		객체 변수를 구분하기 위해 사용됨. this.객체변수명 = 매개변수명;
		this.title = title;
		this.content = content;
		this.author = author;
		this.date  = date;
	}
	
	/*일반메소드 역할 : 게시글 정보(글제목, 글작성자명, 글작성일, 글내용) 각각 출력*/
	public void printInfo() {
		System.out.println("글제목 : " + this.title);
		System.out.println("작성자 : " + this.author);
		System.out.println("작성일 : " + this.date);
		System.out.println("글내용 : " + this.content);
	}
	
	/* 추상메소드1
	   - 새 게시글을 게시판에 추가하는 이름을 가진 추상메소드 addPost선언   
	 	질문4. 추상메소드는 무엇인가요?
		답변4. 자식 클래스가 반드시 강제로 재구현해야 하는 메소드입니다.  
			  메소드의 구현부가 작성되지 않은 메소드 입니다.
			  추상메소드명으로 메소드 기능을 판단 하는 메소드입니다.
	 */
	public abstract  void addPost();
	/* 추상메소드2
	   - 게시판에 저장된 하나의 글정보를 수정하는 이름을 가진 추상메소드 updatePost선언
		 단! 수정할 글내용 하나를 매개변수로 전달받아서 수정할 내용으로 사용할수 있게 선언
		질문5. 추상메소드를 사용하는 이유가 무엇인가요?
		답변5. 상속받은 자식 클래스 마다!!!! 다른 방식으로 기능을 재구현 할 수 있도록 강제성을 주기 위해 추상메소드를 정의 해 놓습니다. 
	*/
	public abstract  void updatePost(String newContent);
	/* 추상메소드3
	   - 게시글 하나의 정보를 삭제하는 의미 있는 이름을 가진 deletePost선언
	     단! 삭제할 글번호 하나를 매개변수로 전달받아서 글번호에 해당하는 글정보(행) 하나만 삭제 할수 있게 선언
	*/
	public abstract void deletePost(int num);	
} //===============================================>  abstract class Board  추상클래스 끝

// 공지사항 게시판 설계도인  자식클래스 NoticeBoard 만들기
//단! 추상클래스 Board를 상속받아 만들기
class NoticeBoard  extends Board {

	/*=================생성자 ================================*/
	//새글제목, 새글내용,  새글작성자명,  새글작성날짜를 매개변수로 전달받아서 초기화 시키는 생성자
	public NoticeBoard(String title, String content, String author, String date) {
		super(title, content, author, date);
	}
	
	/*================= 메소드 오버라이딩 =====================*/
	@Override
	public void addPost() { // 새 게시글 하나 추가 기능
		System.out.println("공지사항 게시판에 새 글 하나를 추가 insert 했습니다.");
	}

	@Override
	public void updatePost(String newContent) { //게시판에 저장된 하나의 글정보를 수정하는 기능
		System.out.println("공지사항 게시판에 저장된 하나의 글정보를 수정 update 했습니다.");
	}

	@Override
	public void deletePost(int num) { //게시글 하나의 정보를 삭제하는 기능 
		System.out.println("공지사항 게시판의 글 하나 삭제 delete 했습니다.");
	}
	
}  //=======================================>  NoticeBoard 자식 클래스 끝

//일반 자유게시판  설계도인  자식클래스 GeneralBoard 만들기
//단! 추상 클래스 Board 를 상속받아 만들기
class GeneralBoard  extends Board {

	//생성자 
	//새글 제목, 글 내용, 글작성자, 글작성일 을 매개변수로 전달 받아..
	//위 Board추상클래스의 변수들을 초기화 하는 생성자.
	public GeneralBoard(String title, String content, String author, String date) {
		
		//질문6. super() 는 어떤 역할을 하나요?
		//답변6. 부모 추상클래스의 생성자를 강제로 호출할때 사용합니다.
		super(title, content, author, date);
	}

	//메소드 오버라이딩 
	@Override
	public void addPost() {
		System.out.println("일반 자유게시판에 새글 하나를 추가 했습니다.");
	}

	@Override
	public void updatePost(String newContent) {
		System.out.println("일반 자유게시판에 새로운 글 내용" + newContent + "으로 수정했습니다.");
	}

	@Override
	public void deletePost(int num) {
		System.out.println("일반 자유게시판에 저장된 " + num + "글번호를 이용해 글번호에 해당하는 글하나 삭제 했습니다.");
	}

} //==========================> GeneralBoard 자식 클래스 끝

public class Main {
	public static void main(String[] args) {
		
		//추상클래스 로 업캐스팅 가능?   가능 합니다.
		
		//업캐스팅을 이용해  일반 자유게시판 기능이 있는  GeneralBoard 자식클래스의 객체 생성
		Board      board   =  new GeneralBoard("일반 글", "이것은 일반 게시판의 글내용입니다", "홍길동", "2025-12-23");
				  
		//1. 글 하나의 정보 출력 하는 기능 사용
				   board.printInfo();
				   /*
						글제목 : 일반 글
						작성자 : 홍길동
						작성일 : 2025-12-23
						글내용 : 이것은 일반 게시판의 글내용입니다
					*/
		//2. 새로운 글 하나를 일반자유게시판에 추가하는 기능 사용
				   board.addPost();
				   //일반 자유게시판에 새글 하나를 추가 했습니다.
				   
		//3. 게시글 하나의 정보중 글내용을 수정하는 기능 사용
				   board.updatePost(" => 이것은 수정된 글내용입니다.");
				   //일반 자유게시판에 새로운 글 내용 => 이것은 수정된 글내용입니다.으로 수정했습니다.
				   
		//4. 게시글 하나의 정보 삭제 하는 기능 사용
				   board.deletePost(1);
				   //일반 자유게시판에 저장된 1글번호를 이용해 글번호에 해당하는 글하나 삭제 했습니다.
		//-----------------------------------------------------------------------
		 
		//연습 해보자.					 
							 
		//공지사항 게시판 기능이 있는 NoticeBoard 자식클래스의 객체 생성	
	}

}
