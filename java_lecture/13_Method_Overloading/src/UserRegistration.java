//==========================================================================
// ★ 문제. 메소드 오버로딩 - 사용자 등록(UserRegistration) 클래스 만들기 ★
//--------------------------------------------------------------------------
//	✅ 메소드 오버로딩이란?
//  	같은 이름을 가진 메소드를 매개변수의 자료형이나 개수에 따라
//  	여러 개 정의하는 것입니다.
//
//	예제. 사용자 정보를 입력받는 메소드를 오버로딩하여 다양한 정보를
//      처리할 수 있도록 만들어 보세요.
//
// 요구사항
//	1. 사용자 이름과 이메일을 입력받아 사용자 정보를 등록하는 메소드
//	2. 사용자 이름, 이메일, 나이를 입력받아 사용자 정보를 등록하는 메소드
//	3. 사용자 이름, 이메일, 나이, 전화번호를 입력받아 사용자 정보를 등록하는 메소드
//	4. 각 메소드는 등록된 사용자 정보를 출력하는 기능을 포함합니다.
//
// 아래 주석의 지시대로 코드를 직접 작성하면 됩니다.
//==========================================================================

public class UserRegistration { // 사용자 한사람의 정보를 저장해 놓고 제공하는 설계도(클래스)

	// [1] 사용자 한사람의 정보들을 저장할 변수들 선언
	// - String 타입 3개 : name(이름), email(이메일), phone(전화번호)
	// - int 타입 1개 : age(나이)
	// 힌트: String은 한 줄에 콤마(,)로 이어서 선언할 수 있다.
	// String name, email, phone;

	String	name, email, phone;
	int		age;

	// [참고] 기본생성자는 컴파일러(javac.exe)에 의해 자동으로 만들어져 있다.
	// 그런데 우리 개발자 눈에는 보이지 않는다.
	// public UserRegistration() {} <- 안 적어도 자동 존재!

	// [2] 사용자 등록 메소드 ① - 이름, 이메일을 매개변수로 전달받아 저장하는 메소드
	// 메소드명 : registerUser
	// 반환타입 : void
	// 매개변수 : String name, String email
	// 기능 순서:
	// - this.name 에 매개변수 name 저장
	// - this.email 에 매개변수 email 저장
	// - this.age 는 0으로 저장 (나이를 전달받지 않았으므로)
	// - this.phone 은 null로 저장 (전화번호를 전달받지 않았으므로)
	// - 마지막에 printInfo(); 메소드 호출 (등록된 정보 출력용)

	public void registerUser ( String name, String email ) {
		this.name = name;
		this.email = email;
		this.age = 0;
		this.phone = null;
		
		printInfo();
	}

	// [3] 사용자 등록 메소드 ② - 이름, 이메일, "나이"까지 전달받는 오버로딩
	// 메소드명 : registerUser (★①과 이름이 같아야 오버로딩!★)
	// 매개변수 : String name, String email, int age
	// 기능 순서:
	// - this.name / this.email / this.age 에 매개변수 값 저장
	// - this.phone 은 null로 저장
	// - 마지막에 printInfo(); 호출

	public void registerUser ( String name, String email, int age ) {
		this.name = name;
		this.email = email;
		this.age = age;
		this.phone = null;
		
		printInfo();
	}

	// [4] 사용자 등록 메소드 ③ - 이름, 이메일, 나이, "전화번호"까지 전달받는 오버로딩
	// 메소드명 : registerUser
	// 매개변수 : String name, String email, int age, String phone
	// 기능 순서:
	// - this.name / this.email / this.age / this.phone 에 매개변수 값 저장
	// - 마지막에 printInfo(); 호출

	public void registerUser ( String name, String email, int age, String phone ) {
		this.name = name;
		this.email = email;
		this.age = age;
		this.phone = phone;
		
		printInfo();
	}

	// [5] 사용자 정보 출력하는 메소드
	// 메소드명 : printInfo
	// 반환타입 : void, 매개변수 없음
	// 기능 순서:
	// 1) "사용자 정보 : " 출력
	// 2) "이름 : " + this.name 출력
	// 3) "이메일 : " + this.email 출력
	// 4) ★조건 출력★ 나이가 저장되어 있을 때만(this.age != 0)
	// "나이 : " + this.age 출력
	// (요약: 사용자의 나이가 저장되어 있지 않다면? -> 출력 생략)
	// 5) ★조건 출력★ 전화번호가 저장되어 있을 때만(this.phone != null)
	// "전화번호 : " + this.phone 출력
	// (요약: 사용자의 전화번호가 저장되어 있지 않다면? -> 출력 생략)
	// 6) System.out.println(); 으로 한줄 줄바꿈 출력

	public void printInfo () {
		System.out.println("사용자 정보: ");
		System.out.println("이름: " + this.name );
		System.out.println("이메일: " + this.email );
		if ( this.age != 0 ) System.out.println("나이: " + this.age );
		if ( this.phone != null) System.out.println("전화번호: " + this.phone );
		System.out.println();
	}

	public static void main(String[] args) {

		// [6] UserRegistration 클래스 설계도 하나를 이용해 객체 메모리 생성
		// 변수명 : userRegistration
		// 힌트 : 클래스자료형 참조변수 = new 생성자();

		UserRegistration userRegistration = new UserRegistration();

		// [7] 오버로딩된 registerUser 메소드들을 각각 호출해서 사용해 보자

		// 1.이름과 이메일 등록
		// -> "홍길동", "hong@example.com" 전달 (매개변수 2개짜리 ①번 호출됨)

		userRegistration.registerUser("홍길동", "hong@example.com");

		// 2.이름, 이메일, 나이 등록 <- 이미 저장된 객체 변수의 값들을 변경하기 위함
		// -> "김철수", "kim@example.com", 25 전달 (매개변수 3개짜리 ②번 호출됨)

		userRegistration.registerUser("김철수", "kim@example.com", 25);

		// 3.이름, 이메일, 나이, 전화번호 등록 <- 이미 저장된 객체 변수의 값들을 변경하기 위함
		// -> "이영희", "lee@example.com", 30, "010-1234-5678" 전달
		// (매개변수 4개짜리 ③번 호출됨)
		userRegistration.registerUser("이영희", "lee@example.com", 30, "010-1234-5678");
	}

}

/*
 * ===== 정답 코드 작성 시 예상 실행 결과 =====
 * 
 * 사용자 정보 : 이름 : 홍길동 이메일 : hong@example.com
 * 
 * 사용자 정보 : 이름 : 김철수 이메일 : kim@example.com 나이 : 25
 * 
 * 사용자 정보 : 이름 : 이영희 이메일 : lee@example.com 나이 : 30 전화번호 : 010-1234-5678
 * 
 * ★확인 포인트★ - 홍길동은 나이/전화번호가 출력되지 않는다 (age=0, phone=null 이므로) - 김철수는 나이만 출력된다
 * (phone=null 이므로) - 이영희는 4개 정보가 모두 출력된다 - 같은 이름 registerUser 인데 매개변수 개수에 따라
 * 자바가 알아서 ①②③ 중 맞는 메소드를 골라 호출한다 = 메소드 오버로딩!
 */
