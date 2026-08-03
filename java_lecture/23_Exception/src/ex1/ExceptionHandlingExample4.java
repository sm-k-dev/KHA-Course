package ex1;

/*
  주제. 배열메모리 전체칸에서 존재하지 않은 칸에 접근해서 값을 사용하려고 할때
       ArrayIndexOfBoundsException 예외 종류가 발생하고,
       
       숫자형태의 문자열이 아닌 데이터를 숫자로 변환 하려고 할때 
       NumberFormatException 예외 종류가 발생하는 각각 다르게 예외 처리하는 코드
       
       참고. 숫자형태의 문자열 "100"을 숫자 100으로 변환해서 반환 하는 메소드 
    		-> Integer 클래스 내부에 만들어져 있는  static int parseInt(String value) 메소드 
    		   예)  "100" 을 숫자 100으로 변환 해야한다.
    		   	    Integer.parseInt("100");  호출하면   "100"을 숫자 100으로 변환후 반환 
*/
public class ExceptionHandlingExample4 {
	public static void main(String[] args) {
	
		//문자열들을 저장한 배열 메모리 생성
		String[]  array = {"100",  "1oo"};
		//					 0       1     index
		
		//array 배열에 저장된 문자열 2개를 차례로 반복해서 얻어  숫자로 변환 후 출력
		for(int i=0;  i<=array.length;  i++) {
											
			try {
				int value = Integer.parseInt( array[i] );   //<--- NumberFormatException 예외 먼저 발생!
				
				System.out.println("array[" + i + "] = " + value); //<--- ArrayIndexOutOfBoundsException 예외 발생!
				
			} catch (NumberFormatException e) {
	
				//1. 숫자로 변환하지 못할때 예외가 발생하면 그 예외정보 출력
				e.printStackTrace();
				
				//2. 예외처할 코드 작성
				System.out.println("array[1] 위치 칸에 저장된 '1oo' 문자열을 숫자로 변환 할수 없음");
	
			} catch (ArrayIndexOutOfBoundsException  e) {
				
				//1. 배열 전체중 없는 칸에 코드로 접근해서 사용하려고 할떄의 예외가 발생하면 예외정보출력
				e.printStackTrace();
				//2. 예외 처리 코드 작성
				System.out.println("array배열에 존재하지 않는 array[2] 칸에 접근해서 사용을 시도 했다");
			}
			
		} // for
		
		System.out.println("자바 프로그램 종료 코드 출력되라~~~~~~~~~~");
		
	} // main()

} // class ExceptionHandlingExample4






