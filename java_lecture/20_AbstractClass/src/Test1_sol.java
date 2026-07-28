//=====================================================================
// [추상클래스 응용문제] 사원 급여 계산 시스템
//---------------------------------------------------------------------
// 저장 방법 : 이 파일을 Test1.java 라는 이름으로 저장한 뒤 실행한다.
//
// [요구사항]
//  1. 추상클래스 Employee 를 완성한다.
//     - protected 변수 3개 : name(이름), empNo(사번), dept(부서)
//     - 생성자 : 위 3개를 매개변수로 전달받아 초기화 (this 사용)
//     - 일반메소드 printInfo() : 사번, 이름, 부서를 출력
//     - 추상메소드 calculateSalary() : 급여를 계산해 int 값을 반환
//     - 추상메소드 printPayslip(String month) : 급여명세서를 출력
//
//  2. 자식클래스 FullTimeEmployee(정규직) 를 완성한다.
//     - private 변수 2개 : baseSalary(기본급), bonus(성과급)
//     - 급여 계산식 : 기본급 + 성과급
//
//  3. 자식클래스 ContractEmployee(계약직) 를 완성한다.
//     - private 변수 2개 : hourlyWage(시급), workHours(근무시간)
//     - 급여 계산식 : 시급 * 근무시간
//
//  4. main 에서 Employee[] 배열 하나에 정규직 2명, 계약직 1명을 담고
//     for 문으로 명세서를 모두 출력한 뒤, 총 지급액을 합산해 출력한다.
//
// [서술형 질문 - 주석으로 답을 작성할 것]
//  질문1. 추상클래스에도 인스턴스 변수와 생성자를 만들 수 있나요?
//  답변1.
//
//  질문2. 반환자료형이 있는 추상메소드도 선언할 수 있나요?
//  답변2.
//
//  질문3. super(...) 는 왜 생성자의 첫 줄에 와야 하나요?
//  답변3.
//
//  질문4. 부모 자료형 배열 하나에 서로 다른 자식 객체를 담을 수 있나요?
//  답변4.
//=====================================================================

//  1. 추상클래스 Employee 를 완성한다.
//     - protected 변수 3개 : name(이름), empNo(사번), dept(부서)
//     - 생성자 : 위 3개를 매개변수로 전달받아 초기화 (this 사용)
//     - 일반메소드 printInfo() : 사번, 이름, 부서를 출력
//     - 추상메소드 calculateSalary() : 급여를 계산해 int 값을 반환
//     - 추상메소드 printPayslip(String month) : 급여명세서를 출력
abstract class Employee_sol {

	//TODO 1. protected 변수 3개 선언 (name, empNo, dept)
	protected String name, empNo, dept;

	//TODO 2. 생성자 선언 (name, empNo, dept 를 전달받아 초기화)
	public Employee_sol(String name, String empNo, String dept) {
		this.name = name;
		this.empNo = empNo;
		this.dept = dept;
	}

	//TODO 3. 일반메소드 printInfo() 구현
	//        출력 형식
	//        사번   : E001
	//        이름   : 홍길동
	//        부서   : 개발팀
	public void printInfo() {
		System.out.println("사번   : " + this.empNo);
		System.out.println("이름   : " + this.name);
		System.out.println("부서   : " + this.dept);
	}

	//추상메소드1 : 급여를 계산해서 그 결과값을 돌려주는 메소드
	//TODO 4. 추상메소드 calculateSalary() 선언 (반환형 int)
	public abstract int calculateSalary();

	
	//추상메소드2 : 급여명세서를 출력하는 메소드 (지급월을 매개변수로 전달받음)
	//TODO 5. 추상메소드 printPayslip(String month) 선언
	public abstract void printPayslip(String month);
	
}
//---------------------------------------------------------------------
//2. 자식클래스 FullTimeEmployee(정규직) 를 완성한다.
//- private 변수 2개 : baseSalary(기본급), bonus(성과급)
//- 급여 계산식 : 기본급 + 성과급

//TODO 6. Employee 를 상속받는 정규직 클래스 FullTimeEmployee 를 선언하고
//        baseSalary, bonus 를 private 으로 선언한 뒤 생성자에서 super() 호출
class FullTimeEmployee_sol extends Employee_sol{
	
	private int baseSalary; //기본급
	private int bonus;      //성과급
	
	public FullTimeEmployee_sol(String name, String empNo, String dept, int baseSalary, int bonus) {
		// super(...)는 왜 첫 줄에 와야 하나요?
		//  - 부모 변수의 초기화가 먼저 끝나야 자식 변수 초기화가 안전하기 때문에
		//    자바 문법상 생성자의 첫 문장에서만 호출할 수 있습니다.
		super(name, empNo, dept);
		this.baseSalary = baseSalary;
		this.bonus = bonus;
	}

	@Override
	public int calculateSalary() { //급여를 계산해서 그 결과값을 돌려주는 메소드
		return this.baseSalary + this.bonus;
	}

	@Override
	public void printPayslip(String month) { //급여명세서를 출력하는 메소드 (지급월을 매개변수로 전달받음)
		System.out.println("[정규직] "+ month +"급여명세서");
		printInfo();
		System.out.println("기본급 : " + this.baseSalary);
		System.out.println("성과급 : " + this.bonus);
		System.out.println("실지급 : " + this.calculateSalary());
		System.out.println();
	}	
}

//---------------------------------------------------------------------

//3. 자식클래스 ContractEmployee(계약직) 를 완성한다.
//- private 변수 2개 : hourlyWage(시급), workHours(근무시간)
//- 급여 계산식 : 시급 * 근무시간

//TODO 7. Employee 를 상속받는 계약직 클래스 ContractEmployee 를 선언하고
//        hourlyWage, workHours 를 private 으로 선언한 뒤 생성자에서 super() 호출
class ContractEmployee_sol extends Employee_sol{
	
	private int hourlyWage; //시급
	private int  workHours; //근무시간
	
	public ContractEmployee_sol(String name, String empNo, String dept, int hourlyWage, int workHours) {
		super(name, empNo,dept);
		this.hourlyWage = hourlyWage;
		this.workHours = workHours;
	}
	
	//급여를 계산해서 그 결과값을 돌려주는 메소드 오버리이딩 
	public int calculateSalary() {
		return this.hourlyWage * this.workHours;
	}

	
	//급여명세서를 출력하는 메소드 (지급월을 매개변수로 전달받음) 오버라이딩
	public  void printPayslip(String month) {
		System.out.println("[계약직] " + month + " 급여명세서");
		this.printInfo();
		System.out.println("시급   : " + this.hourlyWage);
		System.out.println("근무   : " + this.workHours  + "시간");
		System.out.println("실지급 : " + this.calculateSalary());
		System.out.println();
	}
}


//---------------------------------------------------------------------

public class Test1_sol {
	
//  4. main 에서 Employee[] 배열 하나에 정규직 2명, 계약직 1명을 담고
//    for 문으로 명세서를 모두 출력한 뒤, 총 지급액을 합산해 출력한다.	
	public static void main(String[] args) {
	
		//TODO 8. Employee 자료형 배열(길이 3)을 만들고 아래 사원을 담는다.
		//  [0] 정규직 : 홍길동 / E001 / 개발팀   / 기본급 3000000 / 성과급 500000
		//  [1] 계약직 : 김철수 / E002 / 디자인팀 / 시급 15000     / 근무 120시간
		//  [2] 정규직 : 이영희 / E003 / 기획팀   / 기본급 2800000 / 성과급 300000
		Employee_sol[]  empList = new Employee_sol[3];
		
				    empList[0] = new FullTimeEmployee_sol("홍길동", "E001", "개발팀", 3000000, 500000);
				    empList[1] = new ContractEmployee_sol("김철수", "E002", "디자인팀", 15000, 120);
				    empList[2] = new FullTimeEmployee_sol("이영희", "E003", "기획팀", 2800000, 300000);
		
		int total = 0;
		
		//TODO 9. for 문으로 각 사원의 printPayslip("2026년 7월") 호출
		//        동시에 calculateSalary() 결과를 total 변수에 누적
		for(int i=0;  i<empList.length;   i++) {
			empList[i].printPayslip("2026년 7월");
			total  = total + empList[i].calculateSalary();
		}


		//TODO 10. 총 인원 수와 총 지급액 출력
		System.out.println("총 인원   : " + empList.length + "명");
		System.out.println("총 지급액 : " + total);

	}
}

/*=====================================================================
[실행 결과 - 이 출력과 똑같이 나와야 정답]

[정규직] 2026년 7월 급여명세서
사번   : E001
이름   : 홍길동
부서   : 개발팀
기본급 : 3000000
성과급 : 500000
실지급 : 3500000

[계약직] 2026년 7월 급여명세서
사번   : E002
이름   : 김철수
부서   : 디자인팀
시급   : 15000
근무   : 120시간
실지급 : 1800000

[정규직] 2026년 7월 급여명세서
사번   : E003
이름   : 이영희
부서   : 기획팀
기본급 : 2800000
성과급 : 300000
실지급 : 3100000

총 인원   : 3명
총 지급액 : 8400000

=====================================================================*/
