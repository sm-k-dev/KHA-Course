/*
	import
		다른 패키지에 만들어 놓은 .java 파일 내부에 작성된 class를
		현재 파일에서 사용하기 위해 외부에 만들어둔 class를 불러와서 적용하는 예약어
	
	사용법
		import 패키기명.클래스명;
*/

// 1. 다른 패키지에 만들어 놓은 Calculator.java 파일 내부의 Calculator 클래스를 사용하기 위해 import 구문 사용
import com.example.utils.Calculator;

// 2. 다른 패키지 안에 만들어 놓은 외부 모든 클래스 가져오기.
// 사용법:
// import 패키지명.*;
import java.util.*;

/*
	3. 주의 사항  :  *는 해당 util패키지 안에 있는 클래스만 가져오고,
               하위 패키지 않에 있는 클래스들은 가져오지 않습니다.
	
		import java.util.*;
	
		
	4. import 구문 작성 없이도 사용할 수 있는 경우
	 	경우1. 같은 패키지 안의 클래스 가져오는 경우 
	  	경우2. java.lang 패키지안에 만들어서 제공하는 클래스 가져오는 경우
	  		예 : String,  System, Math, Object 클래스 등은 모두 java.lang 패키지 안에 있기때문에
	  		    import 구문 작성 생략 가능!!	    
*/

public class Main2 {

	public static void main(String[] args) {
		// Calculator 클래스 설계도 하나를 이용해 객체 메모리 생성 해봅시다. => 안됨!
		Calculator calculator = new Calculator();
		
		// 두 정수의 합을 반환 받기 위해 add 메소드 사용
		int reuslt = calculator.add(10, 5);

		System.out.println("덤셈 결과 : " + reuslt);
	}

}
