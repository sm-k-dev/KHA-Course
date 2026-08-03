package ex2;

public class ThrowsExample {
										  
	public static void main(String[] args) {
		
		//findClass메소드를 호출한 코드 장소! 여기서 try{}블럭으로 감싸서 catch(){}블럭으로 예외처리 대신 함.		
		try {
			findClass();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}// - main() 메소드 
	
	//클래스 메소드 선언
	//		                        해석:  findCLass메소드를 호출한 코드 줄로 가서 ClassNotFoundException예외처리하라!
	//							    throws 발생한처리할종류의예외클래스명
	public static void findClass() throws ClassNotFoundException {
		
		//Class.forName(".class파일이 저장된 경로를 하나의 문자열로 전달");
		//-> 전달한 문자열 경로에 .class파일이 만들어져 있는지 찾습니다.
			
		//ClassNotFoundException예외가 발생할 예상 코드 작성
		Class.forName("java.lang.String2");
	
		
	}
	
	
	

}
