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
public class ExceptionHandlingExample2 {

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
		
		//1. 실행 예외가 발생할 것이 예상되는 코드 줄의 영역을 
		//   자바문법에서 제공해준 예외처리 하는 문법 코드 중에서
		//   try {  }  블럭으로 묶어 감싸 주자.
			
		//2. 그리고 try {  코드 } 블럭으로 묶어준 코드줄 에서 만약 예외가 발생하는 코드 줄이 생기면
		//   개발자가 직접 발생하는 예외 종류에 따라 예외처리할 코드를 작성하는
		//   catch(예상해서_발생할_종류의_예외를_처리할_클래스자료형명     매개변수명){}블럭을 이어서 작성 해주자.
		
		//3. 마지막으로 finally {  } 블럭을 작성하여  발생한 예외와 상관없이 무조건 한번은 실행되야할 코드를 넣어 실행 시킨다.	
		
		try {
			  int result  = data.length();  //<--- NullPointerException 실행 예외 발생!
			  System.out.println("매개변수 data로 전달 받은  전체 문자열의 문자 총 갯수 : " + result);
			  
		}catch(NullPointerException e) {
			
		//예외 처리 하는 코드 작성 하는 영역
		
			//작성할 수 있는 코드1. 예외가 발생하면 예외가 발생한 이유만 짧게 문자열로 반환받아 출력
			//예외 이유 메세지 -> Cannot invoke "String.length()" because "data" is null
		//	System.out.println( e.getMessage() );
			
			//작성할 수 있는 코드2. 발생한 예외 이유 메세지  +  발생한 예외 종류명 을 하나로 합친 문자열을 반환받아 출력
		//	System.out.println( e.toString()   );
			
			//작성할 수 있는 코드3. 발생한 예외 이유 메세지 + 발생한 예외 종류명 + 예외 발생 코드 위치 줄번호 까지 하나의 문자열로 출력
			e.printStackTrace();
			/*
				java.lang.NullPointerException: Cannot invoke "String.length()" because "data" is null
				at ex1.ExceptionHandlingExample2.printLength(ExceptionHandlingExample2.java:36)
				at ex1.ExceptionHandlingExample2.main(ExceptionHandlingExample2.java:62)
			*/
			
			//작성할  수 있는 코드4.  예외 처리할 코드 작성 
			System.out.println("printLength메소드를 호출할때 문자열을 매개변수 data로 전달 해야 예외가 발생하지 않음");

		} finally {			
			//try {} 와  catch(){} 과 상관 없이  무조건 한번은 실행 해야 할 코드 작성
			System.out.println("[마무리 코드 실행]");
		}		
		  
	} //<=================== printLength 메소드 끝 
	
	public static void main(String[] args) {		
		System.out.println("[자바 프로그램 시작 코드]");
	
		// ExceptionHandlingExample2.printLength("ThisIsJava");
		
		ExceptionHandlingExample2.printLength(null);
			
		System.out.println("[자바 프로그램 마지막 코드 실행 후 종료]");
		
	}

}
