/*
 * 	스토리. 학교에서 학생 한명의 정보를 관리한다.
 * 			이름, 학번, 국어/영어/수학 점수를 저장, 평균을 계산
 * */

/*
 * 	1단계. 현실의 학생 객체 모델링
 * 		- 데이터: 이름(name), 학번(studentId), 국/영/수 점수 (kor, eng, math)
 * 		- 기능: 평균계산 (calcAverage), 학생 정보 출력(printInfo)
 * */

// 2단계. 자바코드로 설계도(class) 설계 (변수 = 데이터 + 메소드 = 기능)
public class StudentTest {
	
	// 클래스 변수 선언
	String name;			// 학생 이름
	String studentId;		// 학생 학번
	int kor, eng, math;		// 학생 국/영/수 점수
	
	// 메소드 선언
	/*
	 * 메소드명: calcAverage
	 * 기능:	국, 영, 수 점수의 평균 계산
	 * 		"학생이름님의 평균 점수 : XX.X점" 형식으로 출력
	 * */
	void calcAverage () {
		double avg = ( kor + eng + math ) / 3.0;
		
		System.out.println(name + "님의 평균 점수: " + avg + "점");
	}
	
	/*
	 * 메소드명: printInfo
	 * 기능: 학번과 이름을 "학번: XXX, 이름: XXX" 형식으로 출력
	 * */
	void printInfo () {
		System.out.println("학번: " + studentId + ", 이름: " + name);
	}

	public static void main(String[] args) {
		// 3단계. new 연산자로 객체 메모리 생성 후 사용
		
		// 순서1+2. 참조변수명 + new 연산자로 객체 메모리 생성
		// 방법. 클래스명 참조변수명 = new 클래스명();
		StudentTest s = new StudentTest();
		
		// 순서3. 객체 변수 값 설정
		s.name = "김민준";
		s.studentId = "2026001";
		s.kor = 90; s.eng = 85; s.math = 95;
		
		s.calcAverage();
		s.printInfo();
	}

}
