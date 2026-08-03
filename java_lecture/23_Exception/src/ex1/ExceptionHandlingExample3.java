package ex1;

public class ExceptionHandlingExample3 {
	/*
	   ClassNotFoundException 예외 
	   
	   - Class.forName("특정 .class 파일의 전체경로 문자열로 전달");
	     : 이코드는 특정 .class 파일이 forName메소드의 매개변수로 전달한 위치에서 찾는 코드이다.
	       만약 찾지 못하면  ClassNotFoundException 예외가 발생합니다.
	       
	   - ClassNotFoundException 요약 : 그 경로에 .class 파일이 만들어져 있지 않아 찾을수 없다. 
	       
	   - 해결책 : 따라서 소스가 컴파일되려면 일반 예외처리코드를 반드시 작성 해야 한다.
	*/
	
	public static void main(String[] args) {
		/*
			이클립스 예외처리 코드 자동으로 작성 하는 방법
			순서1. 예외가 예상되는 코드 줄을 마우스로 드래그 한다.
			순서2.  alt  + shift  +  z     y 키를 눌러  true {  }catch(){  } 블럭을 자동으로 작성한다.

		*/							
			try {
				Class.forName("java.lang.String.class");  //String.class 파일이 만들어져 있는 파일의 전체 경로 전달 
				System.out.println("java.lang 패키지안에는 String.class 파일이 만들어져 있다.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
			try {
				Class.forName("java.lang.String2");    //String2.class 파일은 존재 하지 않은 파일이기때문에 
													   //ClassNotFoundException 일반예외가 발생합니다.
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace(); //발생한 종류의 예외 메세지 자세히 출력		
				System.out.println("java.lang 패키지에 String2.class 파일이 만들어져 있지 않다."); //예외 처리할 코드 작성 
			}
			
			
			System.out.println("자바 프로그램 코드 끝까지 코드 실행 되었습니다.");
	
		
	}

}










