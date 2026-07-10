/*
 * 메서드 - 어떤 '일'을 대신 처리해주는 작은 프로그램 조각
 * 
 * 예: 두 숫자를 더하기, 글자를 화면에 보여주기 등...
 * 
 * 메서드는 '반복되는 일'이나 '특정 기능'을 코드로 묶어서 필요할때마다 부를 수 있다.
 * */

// [예제] 기본적인 반환값(되돌려줄값)을 가지는 메소드 만들기 및 사용
public class Example {

	/*
	 * 	[사용자 정의 메서드]
	 * 		- 개발자가 필요에 따라 직접 만드는 메서드
	 * 		- 필요한 작업을 미리 만들어 두고 필요할때마다 가져다 쓸 수 있다.
	 * 
	 * 	[사용자 정의 메서드 만드는 방법]
	 * 
	 * 		접근제어자 수정자 반환자료형 메소드명 ( 자료형 매개변수명1, 자료형 매개변수명2 ) {
	 * 			메소드가 해야할 기능을 코드로 작성;
	 * 
	 * 			return 결과값;
	 * 		}
	 * 
	 *	1. 메서드 정의
	 * 		메서드 명 : add
	 * 		기능 : 두 개의 중수를 a, b 매개변수로 전달 받아 더한 결과값 sum을 되돌려 주는 기능
	 * 
	 * 		public static int add ( int a, int b ) {
	 * 			int sum = a + b;
	 * 
	 * 			return sum;
	 * 		} 
	 * */
	
	public static int add ( int a, int b ) {
		int sum = a + b;
		
		return sum;
	}
	
	public static void main(String[] args) {
		
		// 2. 메서드 호출
		System.out.println("5 + 10 = " + add( 5, 10 ));
		System.out.println("6 + 2 = " + add( 12, 10 ));
	}

}
