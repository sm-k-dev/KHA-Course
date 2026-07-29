package EX8;

//[1] Board 클래스 
//- 게시글 한 건의 정보를 담는 설계도.  실무에서는 DTO 또는 VO역할을 하는 클래스라 부른다.
public class Board {

	private int id; // 게시글 글번호
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private String writer; // 작성자 이름

	//게시글 한 건의 정보를 초기화 시키는 생성자 
	public Board(int id, String title, String content, String writer) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.writer = writer;
	}

	//Getter역할을 하는 메소드 :  private 으로 만든 변수의 값을 외부로 제공하는 메소드 
	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getWriter() {
		return writer;
	}

	//Setter 역할을 하는 메소드  :  private 으로 만든 변수에 외부에서 매개변수로 전달한 새값으로 변경하는 메소드 
	//기능 : 글내용 변경
	public void setContent(String content) {
		this.content = content;
	}
}
