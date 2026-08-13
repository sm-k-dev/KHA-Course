
/*
	함수형 인터페이스 란?
	- 추상메소드를 단 하나만 가진 인터페이스
	- 람다식으로 익명메소드를 만들 때 사용되는 인터페이스
	
	함수형 인터페이스를 개발자가 직접 만들어서 사용하려면?
	- @FunctionalInterface 어노테이션 기호를 인터페이스 위에 작성해서 만든다.
	
	@FunctionalInterface 어노테이션 기호 의미
	- 이 인터페이스가 함수형 인터페이스로 만들어져 있다고 자바컴파일러(javac.exe)에게 알려주는 기호
*/

// 주제: 개발자가 직접 함수형 인터페이스를 만들어서 사용해보자.
@FunctionalInterface
interface MathOperation {
	// 추상메소드
	// 기능: 두개의 정수를 매개변수로 각각 전달 받아 계산한 정수값을 반환하는 기능
	int operation ( int a, int b );
}

public class Main {

	public static void main(String[] args) {
		
		/*
			1. 람다식 작성 X :
				MathOperation 함수형 인터페이스 내부의 operation 추상메소드를 강제로 메소드 오버라이딩(구현)한
				이름이 없는 익명클래스와 익명구현객체를 만들어 add 참조변수에 저장
				
				메소드 오버라이딩 할 기능 내용
				-> operation 메소드의 매개변수로 두개의 정수를 각각 전달 받아서 더한(+) 결과 정수값을 반환하는 기능
		*/
		
		MathOperation add = new MathOperation() {
			@Override
			public int operation(int a, int b) {
				return a + b;
			}
		};
		System.out.println( add.operation(5, 3) );
		/*
			설명: 여기서 익명클래스는 MathOperation 부모인터페이스의 추상메소드 operation 을
				강제로 구현(메소드 오버라이딩) 하는 익명객체를 생성하며,
				익명객체 내부에 operation 추상메소드를 강제로 오버라이딩 해 놓은 것
		*/
		
		// ======================================================================================
		
		/*
			2. 람다식 작성 O :
				MathOperation 함수형 부모인터페이스 내부의 operation 추상메소드를 구현한 익명메소드의 매개변수로 두개의 정수를 각각 전달 받아서 더한
				결과 값 하나를 정수로 반환하는 기능의 익명메소드를 람다식으로 작성
		*/
		
		MathOperation lamAdd = (int a, int b) -> { return a + b; };
		// MathOperation lamAdd = ( a, b ) -> a + b;
		// int 생략 가능 이유 -> 인터페이스 내의 operation 메소드에 이미 명시 되어 있기 때문
		// 구현부 코드가 한줄이기때문에 중괄호 생략 가능, return 생략 가능
		// 지금은 매개변수가 두개라 소괄호 적어줘야 하지만, 만약 매개변수가 하나라면 소괄호 생략 가능
		// 만약 Generic <> 부분이 있다면 적어줘야 한다.
		
		System.out.println( lamAdd.operation(4, 2) );
	}

}
