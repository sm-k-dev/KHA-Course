package ex1;

/*

예제. 두 개 이상 종류의 예외를  하나의 catch () {} 블록으로 동일한 예외처리 코드를 작성하고 싶을때
	 catch () {} 블록의 매개변수 작성하는 위치에   예외처리클래스자료형1  |  예외처리클래스자료형2  와 같이  기호 |  를 작성하여 연결하면 된다.
	
		
	 catch ( NumberFormatException  |   NullPointerException     매개변수명) {
	 
	 	  예외처리 코드 작성;
	 }
*/

public class ExceptionHandlingExample5 {
	public static void main(String[] args) {
		
		String[]  array = {"100", "1oo",  null,  "200"};
		
		for(int i = 0;  i <= array.length;   i++) {
			
			try {
				int value = Integer.parseInt(array[i]);   //array[1] 작성되면 "1oo"문자열을 숫자로 변환 안됨 NumberFormatException
														  //array[2] 작성되면 null 비어있는 값을 사용하려고 할때 NullPointerException
														  //array[4] 작성되면 존재하지 않은 배열의 칸에 접근 할때 ArrayIndexOfBoundSException
				System.out.println("array[" + i + "] = " + value);
				
			} catch (NumberFormatException |  NullPointerException | ArrayIndexOutOfBoundsException    e) {
				
				e.printStackTrace();
			}	
			
		}//for 
		
		System.out.println("자바 프로그램 종료 코드");
		
	}

}









