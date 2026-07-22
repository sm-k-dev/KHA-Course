/*
 	주제: 메소드 오버로딩 연습
*/

// add 라는 동일한 이름의 메소드 4개 작성 해 놓은 Calculator 설계도(클래스) 만들기
class Calculator {
	// add 메소드 오버로딩 하자
	
	// 1. 두 정수를 매개변수로 받아, 합을 구해 반환하는 기능의 add 메소드 만들기
	public int add ( int a, int b ) {
		return a + b ;
	}
	
	// 2. 세개의 정수를 매개 변수로 전달 받아, 합을 구하는 기능의 add 메소드 만들기
	public int add ( int a, int b, int c ) {
		return a + b + c ;
	}
	
	// 3. 두 실수를 매개변수로 전달 받아 두 실수의 합을 구해 반환하는 기능의 add 메소드 만들기
	public double add ( double a, double b ) {
		return a + b ;
	}
	
	// 4. 하나의 정수와, 하나의 실수를 순서대로 각각 매개변수로 전달 받아 합을 구해 반환하는 기능의 add 메소드 만들기
	public double add ( int a , double b ) {
		return a + b ;
	}
}

public class Main {

	public static void main(String[] args) {
		/*
			class Calculator 클래스 하나를 이용해 객체 메모리 생성시 기본 생성자 호출
		*/
		Calculator calculator = new Calculator();
		
		// 정수 5와 정수 10의 합을 구해 이 자리에 15 출력
		System.out.println( calculator.add( 5, 10 ) );
		
		// 정수 5, 정수 10, 정수 15의 합을 구해 이 자리에 30 출력
		System.out.println( calculator.add( 5, 10, 15 ) );
		
		// 실수 5.5, 실수 10.5의 합을 구해 이 자리에 16.0 출력
		System.out.println( calculator.add( 5.5, 10.5 ) );
		
		// 정수 5와 실수 10.5의 합을 구해 이 자리에 15.5 출력
		System.out.println( calculator.add( 5, 10.5 ) );
	}

}
