package ex1;
/*
예제에서 printLength()메소드는 문자열의 문자개수를 구해서 출력하는 메소드이다.
이때 문자열이 존재하지 않는데(문자열을 매개변수로 받지 않는데)..
문자열의 문자총갯수를 알아내기위해 .length() 메소드를 호출하려할때
NullPointerException이라는 이름의 예외가 발생 했습니다.
즉! 값이 없는데 값을 사용하려고 할떄 발생하는 예외입니다. 

  참고.  NullPointerException 예외 ?
        - 변수(매개변수 또는 멤버변수)에 값이 저장되어 있지 않은 상태에서
          변수의 값을 다른곳에서 사용하려고 할때 발생하는 예외종류중 하나.

*/
public class ExceptionHandlingExample1 {

	/*
	  클래스 메소드 
	  기능 : 매개변수 date로 하나의 전체 문자열을 전달 받아
	        전체 문자열의 총 문자 갯수를 구해서  변수에 저장하고
	        변수에 저장된 문자열의 총 문자 갯수 출력
	*/
	public static void printLength(String data) {
									//"ThisIsJava"  <- 정상적으로 넘겨 받은 문자열
									//또는
									//null          <- 비정상적으로 넘겨 받은 null
			
					//="ThisIsJava".length(); -> 총 문자 갯수 10 반환
					//= null.length();        -> NullPointerException 실행 예외 발생!
		  int result  = data.length();
		  
		  System.out.println("매개변수 data로 전달 받은  전체 문자열의 문자 총 갯수 : " + result);
	}
	
	public static void main(String[] args) {		
		System.out.println("[자바 프로그램 시작 코드]");
	
		ExceptionHandlingExample1.printLength("ThisIsJava");
		
		ExceptionHandlingExample1.printLength(null);
			
		System.out.println("[자바 프로그램 마지막 코드 실행 후 종료]");
		
	}

}
