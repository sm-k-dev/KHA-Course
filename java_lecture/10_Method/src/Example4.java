// [예제] 다양한 형태의 메소드 만들어 보기
public class Example4 {
	/* 메서드 정의 */
	
	/*
	 * 	메서드명: printStudentInfo
	 * 	기능:		학생명 하나를 문자열로 매개변수로 전달 받고, 학생나이 하나를 매개변수로 전달 받아
	 * 			이름: 홍길동, 나이: 20 < 출력후 한 줄 줄바꿈 하는 기능
	 * */
	public static void printStudentInfo (String name, int age ) {
		System.out.println("이름: " + name + ", 나이: " + age);
	}
	
	/*
	 * 	메서드명: add
	 * 	기능:		정수 2개를 매개변수로 각각 전달 받아 합을 구해 반환하는 기능
	 * */
	
	public static int add (int a, int b) {
		return a + b;
	}
	
	/*
	 * 	메서드명: add2
	 * 	기능:		실수 2개를 매개변수로 각각 전달 받아 합을 구해 반환하는 기능
	 * */
	public static double add2 (double a, double b) {
		return a + b;
	}
	
	public static void main(String[] args) {
		// 4. 위 각각의 메서드 호출 구문을 사용하여 아래와 같이 출력되게 하자
		/*
		 * 	이름: 홍길동, 나이: 30
		 * 	15
		 * 	16.0
		 * */
		
		printStudentInfo("홍길동", 30);
		System.out.println( add(10, 5) );
		System.out.println( add2(10.0, 6.0) );
	}

}
