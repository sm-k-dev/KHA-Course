/*
	1단계: 현실의 영희 학생 객체 모델링
		데이터 - 이름, 나이, 학번
		동작 - 공부하기, 영희 정보 출력
 */

/*
 	2단계: 현실의 영희 객체의 클래스 설계도 만들기
 		클래스명: Student
 		
 			멤버변수 선언
 				1. 학생이름(문자열) 저장할 name 변수
 				2. 학생나이(정수) 저장할 age 변수
 				3. 학생학번(문자열) 저장할 studentId변수
 			
 			메소드 선언
 				1. 학생이 공부하는 동작 출력 기능의 study 메소드 선언
 					예: "영희 학생이 공부하고 있습니다"
 				
 				2. 학생 정보를 출력 하는 기능의 printInfo 메소드 선언
 					예: "이름: 영희, 나이: 20, 학번: 2025001"
 */
public class Student {
	
	String name;
	int age;
	String studentId;
	
	void study() {
		System.out.println( name + " 학생이 공부하고 있습니다." );
	}
	
	void printInfo() {
		System.out.println("이름: " + name + ", 나이: " + age + ", 학번: " + studentId);
	}

	public static void main(String[] args) {
		// 3단계: Student 클래스를 이용해 영희 객체 생성 후 데이터 저장 하고 사용
			// 순서1. Student 클래스 이용해 영희 객체 생성. 단, 참조변수명은 student1로 작성
		
		Student student1 = new Student();
		
			// 순서2. 순서1 에서 생성한 영희 객체에 이름"영희", 나이 20, 학번 "2025001"로 저장
		student1.name = "영희";
		student1.age = 20;
		student1.studentId = "2025001";
		
			// 순서3. 영희 학생 객체 정보 출력하기 위해 printInfo메소드 호출
		student1.printInfo();
		
			// 순서4. 영희 학생 객체가 공부하는 동작 출력하기 위해 study 메소드 호출
		student1.study();
	}

}
