/*
 * 스토리. 도서관 책 한 권을 관리 한다.
 * 		대출 여부를 두고, 이미 대출 중이면 다시 대출 할 수 없다.
 * */

// 1단계: 현실의 도서 - "자바의 정석", "수학의 정석" 객체 모델링 ( 데이터와 기능 추출)
// 	- 데이터: 제목(title), 저자(author), 대출여부(isRented)
//	- 기능: 대출(rent), 반납(returnBook), 상태 출력 (printStatus)

// 2단계: 현실의 도서를 추상화해서 도서 설계도(class) 설계
public class BookTest {
	
	// 클래스 변수 선언
	String title;		// 책 제목
	String author;		// 책 저자
	boolean isRented;	// 대출 여부
	
	// 클래스 메소드 선언
	/*
	 * 메소드명: rent
	 * 기능: 대출 중이 아니면 대출 처리하고 완료 메세지 출력
	 * 		이미 대출 중이면 "이미 대출 중입니다." 출력
	 * */
	void rent() {
		
		if ( !isRented ) { // ! 는 논리부정 연산자
			isRented = true;
			System.out.println( title + " 도서가 대출 되었습니다.");
		} else {
			System.out.println(title + " 도서는 이미 대출 중입니다.");
		}
	}
	
	/*
	 * 메소드명: returnBook
	 * 기능: isRented를 false로 바꾸고 반납 완료 메세지 출력
	 * */
	void returnBook() {
		isRented = false;
		System.out.println(title + " 도서의 반납이 완료 되었습니다.");
	}
	
	/*
	 * 메소드명: printStatus
	 * 기능: 제목, 저자, 대출여부를 한 줄로 출력한다.
	 * */
	void printStatus() {
		System.out.println("제목: " + title + " / 저자 : " + author + " / 대출여부: " + (isRented ? "대출중, 현재 서고에 없습니다." : "현재 서고에 있습니다. 대출 가능"));
	}

	public static void main(String[] args) {
		// 3단계: new 연산자로 객체 메모리 생성 후 사용
		
		// 순서1+2. 참조변수선언 + 객체메모리 생성
		BookTest b = new BookTest();
		
		// 순서3. 객체 변수값 설정
		b.title = "자바의 정석"; 		// . 은 접근연산자
		b.author = "남궁성";
		b.isRented = false;
		
		// 순서4. 객체 메소드 호출해서 렌트도 하고 반납도 하자
		b.rent();
		b.rent();
		b.returnBook();
	}

}
