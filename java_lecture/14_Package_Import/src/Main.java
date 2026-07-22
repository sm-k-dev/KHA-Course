/*
	import
		다른 패키지에 만들어 놓은 .java 파일 내부에 작성된 class를
		현재 파일에서 사용하기 위해 외부에 만들어둔 class를 불러와서 적용하는 예약어
	
	사용법
		import 패키기명.클래스명;
*/

// 다른 패키지에 만들어 놓은 Calculator.java 파일 내부의 Calculator 클래스를 사용하기 위해 import 구문 사용
import com.example.utils.Calculator;

public class Main {

	public static void main(String[] args) {
		// Calculator 클래스 설계도 하나를 이용해 객체 메모리 생성 해봅시다. => 안됨!
		Calculator calculator = new Calculator();
		
		// 두 정수의 합을 반환 받기 위해 add 메소드 사용
		calculator.add(10, 5);
	}

}
