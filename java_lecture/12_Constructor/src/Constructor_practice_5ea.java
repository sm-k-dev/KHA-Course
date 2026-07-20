/*
	★ 생성자(Constructor) 만들기 연습 5개 ★
	
	[공통 규칙]
	1. 생성자 이름은 반드시 클래스 이름과 동일하게 작성한다.
	2. 생성자는 반환타입(void, int 등)을 절대 작성하지 않는다.
	3. 기본 생성자 + 매개변수가 있는 생성자를 각각 만들어 본다.
	4. new 생성자() 호출 시 자동으로 단 한번만 실행된다.
	
	[연습 목차]
	연습1. 학생(Student) 클래스      - 기본
	연습2. 은행계좌(BankAccount)     - 기본
	연습3. 책(Book) 클래스           - 매개변수 3개
	연습4. 핸드폰(Phone) 클래스      - 생성자 3개 오버로딩
	연습5. 피자(Pizza) 클래스        - 생성자 안에서 계산까지
*/
//=========================================================================
// 연습1. 학생(Student) 클래스
//-------------------------------------------------------------------------
// [문제] 
//  - 객체변수: name(이름, String), age(나이, int)
//  - 기본 생성자: name="무명", age=0 으로 초기화
//  - 매개변수 생성자: 이름과 나이를 전달받아 초기화
//=========================================================================
class Student{	
	//[1] 객체 변수 정의 
	String name;   //학생 이름
	int age;       //학생 나이 
	
	//[2] 기본 생성자 정의 
	public Student() {
		name = "무명";
		age = 0;
	}
	
	//[3] 매개변수가 있는 생성자 정의 
	public Student(String name_, int age_) {
					//   "홍길동",     20		
		name = name_;
//		     = "홍길동";
		     
		age = age_;
//			= 20;
	}
}
//=========================================================================
//연습2. 은행계좌(BankAccount) 클래스
//-------------------------------------------------------------------------
//[문제]
//- 객체변수: owner(예금주, String), balance(잔액, int)
//- 기본 생성자: owner="미지정", balance=0
//- 매개변수 생성자: 예금주와 첫 입금액을 전달받아 초기화
//=========================================================================
class BankAccount{
	//[1] 객체 변수 정의
	String owner;     //예금주 이름
	int   balance;    //계좌 잔액
	
	//[2] 기본 생성자 정의
	public BankAccount() {
		owner = "미지정";
		balance = 0;
	}
	
	//[3] 매개변수가 있는 생성자 정의
	public BankAccount(String owner_,  int balance_) {
						// "김길동"   ,      50000
		
		owner = owner_;
		//    = "김길동";
		
		balance = balance_;
		//      = 50000;
		
	}
}

public class Constructor_practice_5ea {

	public static void main(String[] args) {
		//---------------------------------------------
		//연습1 확인. 학생 객체 만들기 
		//--------------------------------------------
		System.out.println("===== 연습1. Student =====");
		
		//기본생성자로 Student 클래스로 객체 생성
		Student   s1 = new Student();
		/*
			[ 0x12 ]  =  --------- 0x12 객체 메모리 주소 ------
			
							//객체 변수 메모리들
							String name; [ "무명" ];  //학생 이름
							int age;     [  0    ];  //학생 나이
							
						  -----------------------------------
		 */
	    System.out.println(s1.name); //"무명"
	    System.out.println(s1.age);  //0
		
	    //매개변수가 작성되어 있는 생성자로 객체 생성
	    Student  s2 = new Student("홍길동", 20);
		/*
			[ 0x13 ]  =  --------- 0x13 객체 메모리 주소 ------
			
							String name; [ "홍길동" ];
							int age;     [  20     ];
							
						  -----------------------------------
		*/		
		System.out.println(s2.name); //"홍길동"
		System.out.println(s2.age);  //20
		
		//---------------------------------------------------
		//연습2 확인. 은행 계좌 객체 만들기 
		//---------------------------------------------------
		System.out.println("==== 연습2.  BankAccount ====");
		
		BankAccount  acc1 = new BankAccount();
		System.out.println(acc1.owner);  //"미지정"
		System.out.println(acc1.balance); //0
		
		BankAccount acc2 = new BankAccount("김길동", 50000);
		System.out.println(acc2.owner);   //"김길동"
		System.out.println(acc2.balance); //50000
		
		
		
	}

}
