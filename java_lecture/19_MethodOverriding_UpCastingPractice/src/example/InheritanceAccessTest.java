package example;

//==========================================================================
// ★ 문제지(초상세판). 상속(Inheritance) + 접근제어자(Access Modifier) 5문제 ★
//--------------------------------------------------------------------------
// - 각 빈칸의 주석은 "무엇을, 어떤 문법으로" 작성해야 하는지 순서대로 알려줍니다.
// - 주석의 지시를 위에서부터 한 줄씩 그대로 코드로 옮기면 완성됩니다.
// - 파일 맨 아래의 [예상 실행 결과]와 출력이 같아지면 정답입니다.
//==========================================================================


//==========================================================================
// 문제1. Person(부모) -> Student(자식)
//        핵심: private 변수는 자식 클래스에서도 직접 접근할 수 없다.
//              그래서 super(부모 생성자 호출)와 getter를 사용해야 한다.
//==========================================================================

class Person {

	//[1-1] 객체 변수 2개 선언
	//      선언 문법:  접근제어자  자료형  변수명;
	//      ① private 접근제어자, String 자료형, 변수명 name  (이름 저장용)
	//      ② private 접근제어자, int 자료형,    변수명 age   (나이 저장용)
	//      ※ private을 붙였으므로 이 두 변수는 Person 클래스의 { } 안에서만
	//        직접 접근할 수 있게 됩니다. (자식 클래스 Student에서도 직접 접근 불가)

	//>>> 여기에 변수 2개를 선언하세요.
	private String name;
	private int age;



	//[1-2] 생성자 작성
	//      생성자 문법:  public 클래스명(매개변수들) { 초기화 코드 }
	//      - 생성자 이름은 클래스 이름과 똑같이 Person 으로 작성 (반환타입 없음!)
	//      - 매개변수 2개: String name, int age
	//      - 생성자 내부 코드 2줄:
	//          this.name = name;   <- 객체의 name 변수에 매개변수 name 값 저장
	//          this.age = age;     <- 객체의 age 변수에 매개변수 age 값 저장
	//      - this.name = "객체가 소유한 변수",  name = "매개변수로 전달받은 값"
	//        이름이 같아 구분이 안 되므로 왼쪽에 this. 를 반드시 붙입니다.

	//>>> 여기에 생성자를 작성하세요.
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}


	//[1-3] getter 메소드 2개 작성
	//      getter 문법:  public 반환자료형 get변수명() { return this.변수명; }
	//      ① 메소드명 getName, 반환타입 String  -> return this.name;
	//      ② 메소드명 getAge,  반환타입 int     -> return this.age;
	//      ※ getter를 만드는 이유: name/age가 private이라 바깥에서 직접 못 읽으므로
	//        "값을 읽는 전용 public 메소드"를 대신 열어주는 것입니다.

	//>>> 여기에 getter 2개를 작성하세요.
	public String getName() {
		return this.name;
	}
	public int  getAge() {
		return this.age;
	}

	//[1-4] showInfo 메소드 작성
	//      문법:  public void showInfo() { 출력코드 }
	//      - 출력 코드 1줄:
	//        System.out.println("이름: " + this.name + ", 나이: " + this.age);
	//      ※ 같은 클래스(Person) 안이므로 private 변수를 this.name 으로
	//        직접 읽을 수 있습니다.

	//>>> 여기에 showInfo 메소드를 작성하세요.
	public void showInfo() {
		System.out.println("이름: " + this.name + ", 나이: " + this.age);
		// 여기는 Person 클래스 내부이므로 private 변수를 this.name 으로
		// 직접 읽을 수 있다. 문자열과 변수를 + 로 이어붙여 한 줄 출력.
	}
}
//=========================================================================================================

//[1-5] Student 클래스 전체 작성
//      상속 문법:  class 자식클래스명 extends 부모클래스명 { }
//      -> class Student extends Person {  으로 시작하세요.
class Student extends Person{

//      Student 내부에 작성할 것 3가지:
//
//      (1) 객체 변수 1개
//          private String major;    <- 전공 저장용
		private String major;
//
//      (2) 생성자
//          - 매개변수 3개: String name, int age, String major
//          - 생성자 내부 첫 줄에 반드시:  super(name, age);
//            * super(...)는 "부모 Person의 생성자를 호출"하는 문법입니다.
//            * 부모의 name/age는 private이라서 Student 안에서
//              this.name = name; 이라고 쓰면 컴파일 에러가 납니다.
//              그래서 부모 생성자에게 값을 전달해 부모가 대신 저장하게 합니다.
//            * super(...)는 생성자의 "첫 줄"에만 쓸 수 있습니다.
//          - 두번째 줄:  this.major = major;   <- major는 내(Student) 것이므로 직접 저장
		public Student(String name, int age, String major) {
			super(name, age);   
			this.major = major; // major는 내(Student) 소유의 변수이므로 직접 저장 가능.
		}
		
		
//      (3) showInfo 메소드 오버라이딩
//          * 오버라이딩: 부모에 이미 있는 메소드와 "완전히 같은 이름/형태"로
//            자식이 다시 만들어서 내용을 바꾸는 것
//          - 메소드 선언 윗줄에 @Override 를 붙이세요 (오타 검사 기능)
//          - 문법:  public void showInfo() { 출력코드 }
//          - 출력 코드 1줄:
//            System.out.println("이름: " + getName() + ", 나이: " + getAge() + ", 전공: " + this.major);
//            * name/age는 private이라 this.name 사용 불가!
//              부모에게 물려받은 getName(), getAge() 메소드를 호출해서 값을 얻습니다.
			@Override
			public void showInfo() {
				 System.out.println("이름: " + super.getName() + 
						 			 ", 나이: " + super.getAge() +
						 			 ", 전공: " + this.major);
			}
}
	
	
	
//==========================================================================
// 문제2. Animal(부모) -> Dog, Cat(자식)
//        핵심: protected 변수는 자식 클래스에서 this.변수명 으로 직접 접근 가능.
//              (문제1의 private과 무엇이 다른지 비교하며 작성할 것)
//==========================================================================

class Animal {

	//[2-1] 객체 변수 1개 선언
	//      protected 접근제어자, String 자료형, 변수명 name
	//      ※ protected: 같은 패키지 + 자식 클래스에서 접근을 허용하는 접근제어자.
	//        이렇게 선언하면 자식 클래스(Dog, Cat)가 this.name 으로
	//        직접 읽고 쓸 수 있게 됩니다.

	//>>> 여기에 변수를 선언하세요.
	protected String name;


	//[2-2] 생성자 작성
	//      - 생성자 이름: Animal (클래스명과 동일, 반환타입 없음)
	//      - 매개변수 1개: String name
	//      - 내부 코드 1줄:  this.name = name;

	//>>> 여기에 생성자를 작성하세요.
	public Animal(String name){
		this.name = name;
	}

	//[2-3] sound 메소드 작성
	//      문법:  public void sound() { 출력코드 }
	//      - 출력 코드 1줄:
	//        System.out.println(this.name + "이(가) 동물 소리를 냅니다");

	//>>> 여기에 sound 메소드를 작성하세요.
	public void sound() {
		 System.out.println(this.name + "이(가) 동물 소리를 냅니다");
	}
}
//=======================================================================================================


//[2-4] Dog 클래스 전체 작성
//      class Dog extends Animal {  으로 시작하세요.
class Dog extends Animal{
//      Dog 내부에 작성할 것 2가지:
//
//      (1) 생성자
//          - 매개변수 1개: String name
//          - 내부 코드 1줄:  super(name);
//            * 부모 Animal의 생성자에게 name을 전달해서 초기화를 맡깁니다.
		public Dog(String name) {
			super(name); // 매개변수로 받은 이름을 객체 변수에 저장.
		}


//      (2) sound 메소드 오버라이딩
//          - 윗줄에 @Override
//          - 출력 코드 1줄:
//            System.out.println(this.name + "이(가) 멍멍 짖습니다");
//            * name이 protected이므로 자식인 Dog 안에서 this.name을
//              직접 읽을 수 있습니다. (문제1과 달리 getter가 필요 없음!)
		@Override
		public void sound() {
			 System.out.println(super.name + "이(가) 멍멍 짖습니다");
		}
	
}

//[2-5] Cat 클래스 전체 작성
//      Dog와 완전히 같은 구조로 작성하되, 출력 문장만 다릅니다.
//      class Cat extends Animal {  으로 시작
//      (1) 생성자: 매개변수 String name  ->  내부에서 super(name);
//      (2) sound 오버라이딩(@Override): 
//          System.out.println(this.name + "이(가) 야옹 웁니다");

//>>> 여기에 Cat 클래스 전체를 작성하세요.
class Cat extends Animal {

	//고양이 이름 저장할 생성자 
	public Cat(String name) {
		super(name);
	}
	//sound 메소드 오버라이딩 
	@Override
	public void sound() {
		System.out.println(super.name + "이(가) 야옹 웁니다");
	}
}

//==========================================================================
// 문제3. Employee(부모) -> Manager(자식)
//        핵심: 한 클래스 안에서 private과 protected를 섞어 쓰면
//              자식 클래스에서 "직접 접근 가능한 변수"와
//              "getter로만 접근 가능한 변수"가 나뉜다.
//==========================================================================

class Employee {

	// [3-1] 객체 변수 2개 선언 - 접근제어자를 서로 다르게!
	// ① private 접근제어자, String 자료형, 변수명 name
	// -> 자식(Manager)도 직접 접근 불가하게 됨
	// ② protected 접근제어자, int 자료형, 변수명 salary
	// -> 자식(Manager)은 직접 접근 가능하게 됨

	// >>> 여기에 변수 2개를 선언하세요.
	private String name;
	protected int salary;

	// [3-2] 생성자 작성
	// - 생성자 이름: Employee
	// - 매개변수 2개: String name, int salary
	// - 내부 코드 2줄:
	// this.name = name;
	// this.salary = salary;

	// >>> 여기에 생성자를 작성하세요.
	public Employee(String name, int salary) {
		super();
		this.name = name;
		this.salary = salary;
	}

	// [3-3] getter 작성
	// 문법: public String getName() { return this.name; }
	// ※ private인 name을 자식이나 바깥에서 "읽을 수 있게" 열어주는 통로

	// >>> 여기에 getName 메소드를 작성하세요.
	public String getName() {
		return name;
	}

	// [3-4] showSalary 메소드 작성
	// 문법: public void showSalary() { 출력코드 }
	// - 출력 코드 1줄:
	// System.out.println(this.name + "의 급여: " + this.salary + "원");

	// >>> 여기에 showSalary 메소드를 작성하세요.
	public void showSalary() {
		System.out.println(this.name + "의 급여: " + this.salary + "원");
	}

}//================>  Employee 부모 클래스 끝 

//[3-5] Manager 클래스 전체 작성
//      class Manager extends Employee {  으로 시작하세요.
class Manager extends Employee { 
 //      Manager 내부에 작성할 것 4가지:
//
//      (1) 객체 변수 1개
//          private int bonus;    <- 보너스 저장용
		 private int bonus;
	
//      (2) 생성자
//          - 매개변수 3개: String name, int salary, int bonus
//          - 첫 줄:  super(name, salary);   <- 부모 생성자에게 2개 값 전달
//          - 둘째 줄:  this.bonus = bonus;
		 public Manager(String name, int salary, int bouns) {
				super(name, salary);
				this.bonus = bouns;
		 }
		 		 
//      (3) getTotalPay 메소드
//          문법:  public int getTotalPay() { return 계산식; }
//          - 반환 코드 1줄:  return this.salary + this.bonus;
//            * salary: protected라서 자식인 여기서 this.salary 직접 사용 가능
//            * bonus : 내(Manager) 변수이므로 당연히 직접 사용 가능
//            * 참고: 만약 name이 필요했다면 private이라 this.name은 에러이고
//              getName()을 호출해야 합니다. (salary와의 차이를 기억!)
		 public int getTotalPay() {
			 return this.salary + this.bonus;
		 }
		 	 
//      (4) showSalary 메소드 오버라이딩
//          - 윗줄에 @Override
//          - 출력 코드 1줄:
//            System.out.println(getName() + "의 총급여(보너스포함): " + getTotalPay() + "원");
//            * 이름은 getName()으로(private이므로), 금액은 위에서 만든
//              getTotalPay()를 호출해서 사용합니다.
		 @Override
			public void showSalary() {
			 	System.out.println(super.getName() + "의 총급여(보너스포함): " + this.getTotalPay() + "원");
				// 이름: private이므로 getName() 호출로 획득.
				// 금액: 위에서 만든 getTotalPay() 호출 -> 3000000 + 500000 = 3500000
			}
}

//==========================================================================
// 문제4. Shape(부모) -> Circle, Rectangle(자식)
//        핵심: 부모는 getArea()의 "틀"만 제공하고(0.0 반환),
//              자식마다 오버라이딩으로 자기만의 계산식을 넣는다.
//==========================================================================

class Shape {

	//[4-1] 객체 변수 1개 선언
	//      private 접근제어자, String 자료형, 변수명 shapeName  (도형 이름 저장용)
	//>>> 여기에 변수를 선언하세요.
    private String shapeName;

	//[4-2] 생성자 작성
	//      - 생성자 이름: Shape
	//      - 매개변수 1개: String shapeName
	//      - 내부 코드 1줄:  this.shapeName = shapeName;
	//>>> 여기에 생성자를 작성하세요.
	public Shape(String shapeName) {
		this.shapeName = shapeName;
	}

	//[4-3] getter 작성
	//      문법:  public String getShapeName() { return this.shapeName; }
	//>>> 여기에 getShapeName 메소드를 작성하세요.
	public String getShapeName() {
		return this.shapeName;
	}

	//[4-4] getArea 메소드 작성
	//      문법:  public double getArea() { return 0.0; }
	//      - 반환타입이 double(실수)임에 주의!
	//      - 부모 Shape은 자기가 원인지 사각형인지 모르므로
	//        일단 0.0만 반환하는 "기본 틀"을 제공합니다.
	//        (자식들이 이 메소드를 오버라이딩해서 진짜 계산식으로 교체할 예정)

	//>>> 여기에 getArea 메소드를 작성하세요.
	public double getArea() {
		return 0.0;
	}
}

//[4-5] Circle 클래스 전체 작성
//      class Circle extends Shape {  으로 시작하세요.
class Circle extends Shape {
//
//      (1) 객체 변수:  private double radius;    <- 반지름 저장용
		private double radius;
//
//      (2) 생성자
//          - 매개변수 1개: double radius
//          - 첫 줄:  super("원");
//            * 매개변수가 아니라 "원" 이라는 글자를 직접 전달합니다.
//              Circle이라는 클래스 자체가 항상 "원"이므로 이름을 고정하는 것.
//          - 둘째 줄:  this.radius = radius;
		public Circle(double radius) {
			super("원");
			this.radius = radius;
		}
		
//      (3) getArea 오버라이딩
//          - 윗줄에 @Override
//          - 반환 코드 1줄:  return Math.PI * this.radius * this.radius;
//            * Math.PI : 자바가 미리 준비해 둔 원주율 값(3.141592...)
		@Override
		public double getArea() {
			
			return Math.PI * this.radius * this.radius;
		}	
		//원넓이 구하는 공식 = 원주율 X 반지름 X 반지름
	
}

//[4-6] Rectangle 클래스 전체 작성
//      class Rectangle extends Shape {  으로 시작하세요.
class Rectangle extends Shape{
//      (1) 객체 변수 2개:
//          private double width;     <- 가로 저장용
//          private double height;    <- 세로 저장용
		private double width;
		private double height;
//      (2) 생성자
//          - 매개변수 2개: double width, double height
//          - 첫 줄:  super("사각형");
//          - 이어서:  this.width = width;   this.height = height;
		public Rectangle(double width, double height) {
			super("사각형");
			this.width = width;
			this.height = height;
		}
		
//      (3) getArea 오버라이딩
//          - 윗줄에 @Override
//          - 반환 코드 1줄:  return this.width * this.height;
		@Override
		public double getArea() {
			
			return this.width * this.height;
		}
		//사각형 넓이  = 가로 X 세로
}

//==========================================================================
// 문제5(종합). BankAccount(부모) -> SavingsAccount(자식)
//        핵심: 부모의 private 변수(balance)는 자식이 직접 계산에 못 쓰고,
//              부모가 열어준 public 메소드(getBalance, deposit)를
//              "호출"하는 방식으로만 읽고 변경할 수 있다.
//==========================================================================

class BankAccount {

	//[5-1] 객체 변수 1개 선언
	//      private 접근제어자, int 자료형, 변수명 balance  (잔액 저장용)
	private int balance;
	
	//[5-2] 생성자 작성
	//      - 생성자 이름: BankAccount
	//      - 매개변수 1개: int balance
	//      - 내부 코드 1줄:  this.balance = balance;
	public BankAccount(int balance) {
		this.balance = balance;
	}

	//[5-3] deposit 메소드 작성 (입금 기능, 출력은 없음)
	//      문법:  public void deposit(int amount) { 코드 }
	//      - 내부 코드 1줄:  this.balance += amount;
	//        * this.balance = this.balance + amount; 를 줄여 쓴 것

	//>>> 여기에 deposit 메소드를 작성하세요.
	public void deposit(int amount) {
		this.balance += amount;
	}

	//[5-4] getter 작성
	//      문법:  public int getBalance() { return this.balance; }
	//>>> 여기에 getBalance 메소드를 작성하세요.
	public int getBalance() {
		return this.balance;
	}
}

//[5-5] SavingsAccount 클래스 전체 작성
//      class SavingsAccount extends BankAccount {  으로 시작하세요.

class SavingsAccount extends BankAccount { 
//      (1) 객체 변수:  private double interestRate;   <- 이자율 저장용 (0.05 = 5%)
		private double interestRate;
		
//
//      (2) 생성자
//          - 매개변수 2개: int balance, double interestRate
//          - 첫 줄:  super(balance);      <- 잔액 초기화는 부모에게 위임
//          - 둘째 줄:  this.interestRate = interestRate;
		public SavingsAccount(int balance, double interestRate) {
			super(balance);
			// 잔액 초기화는 부모 BankAccount 생성자에게 위임.
			// (balance가 private이라 여기서 직접 저장 불가능하기 때문)
			
			this.interestRate = interestRate;
		}
			
//      (3) addInterest 메소드 (이자 입금 기능)
//          문법:  public void addInterest() { 코드 3줄 }
		public void addInterest() {
		//          - 1줄째(이자 계산):
		//          int interest = (int)(getBalance() * this.interestRate);
		//          * balance는 private이라 this.balance 로 직접 읽으면 컴파일 에러!
		//            -> 대신 부모의 getBalance() 메소드를 호출해서 현재 잔액을 얻습니다.
		//          * (int)(...) : 계산 결과가 실수(double)로 나오므로
		//            소수점을 잘라내고 정수(int)로 바꾸는 형변환 문법입니다.
		//            예) 100000 * 0.05 = 5000.0(실수) -> (int) 붙이면 5000(정수)
			 int interest = (int)(super.getBalance() * this.interestRate);
		//	          - 2줄째(이자 입금):
		//           deposit(interest);
		//           * this.balance += interest; 라고 쓰면 컴파일 에러가 납니다!
		//             (에러 문구: balance has private access in BankAccount)
		//           * 그래서 부모의 public 메소드 deposit()을 호출해서
		//             부모가 대신 balance를 늘리게 합니다.			 
			 super.deposit(interest);
		//	          - 3줄째(결과 출력):
		//           System.out.println("이자 " + interest + "원이 입금되었습니다. (현재 잔액: " + getBalance() + "원)");	 
			 System.out.println("이자 " + interest + "원이 입금되었습니다. (현재 잔액: " + super.getBalance() + "원)");
		
		}
}

//==========================================================================
// 실행 확인용 메인 클래스 (완성되어 있음 - 수정하지 마세요)
//==========================================================================
public class InheritanceAccessTest {
	public static void main(String[] args) {

		System.out.println("===== 문제1. Person -> Student =====");
		Student student = new Student("김민준", 20, "컴퓨터공학과");
		student.showInfo();
		System.out.println();

		System.out.println("===== 문제2. Animal -> Dog, Cat =====");
		Dog dog = new Dog("초코");
		Cat cat = new Cat("나비");
		dog.sound();
		cat.sound();
		System.out.println();

		System.out.println("===== 문제3. Employee -> Manager =====");
		Manager manager = new Manager("이수진", 3000000, 500000);
		manager.showSalary();
		System.out.println();

		System.out.println("===== 문제4. Shape -> Circle, Rectangle =====");
		Circle circle = new Circle(5);
		Rectangle rectangle = new Rectangle(4, 6);
		System.out.println(circle.getShapeName() + "의 넓이: " + circle.getArea());
		System.out.println(rectangle.getShapeName() + "의 넓이: " + rectangle.getArea());
		System.out.println();

		System.out.println("===== 문제5. BankAccount -> SavingsAccount =====");
		SavingsAccount savings = new SavingsAccount(100000, 0.05);
		savings.addInterest();
	}
}

/*
	===== 정답 코드 작성 시 예상 실행 결과 =====

	===== 문제1. Person -> Student =====
	이름: 김민준, 나이: 20, 전공: 컴퓨터공학과

	===== 문제2. Animal -> Dog, Cat =====
	초코이(가) 멍멍 짖습니다
	나비이(가) 야옹 웁니다

	===== 문제3. Employee -> Manager =====
	이수진의 총급여(보너스포함): 3500000원

	===== 문제4. Shape -> Circle, Rectangle =====
	원의 넓이: 78.53981633974483
	사각형의 넓이: 24.0

	===== 문제5. BankAccount -> SavingsAccount =====
	이자 5000원이 입금되었습니다. (현재 잔액: 105000원)


	===== 스스로 채점 포인트 =====
	1. 문제1: Student 생성자에서 super(name, age); 대신
	          this.name = name; 을 써보고 실제로 컴파일 에러가 나는지 확인
	          -> 에러 문구: name has private access in Person
	2. 문제2: Dog 안에서 this.name이 에러 없이 되는 이유를
	          "protected이기 때문"이라고 설명할 수 있으면 통과
	3. 문제3: getTotalPay에서 this.salary는 되는데
	          this.name은 왜 안 되는지 구분해서 말할 수 있으면 통과
	4. 문제5: deposit(interest); 대신 this.balance += interest; 를 써보고
	          에러 문구(balance has private access in BankAccount)를
	          직접 확인한 뒤 다시 고쳐볼 것
*/
