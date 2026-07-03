/*
 * 주제: 자바에서 for 반복문을 사용하여 반복 작업 수행하는 예제
 * */
public class for02 {

	public static void main(String[] args) {
		// 1. 반복을 제어할 i 변수 선언
		int i;
		
		// 2. for 문을 사용하여 i = 1; 부터 4가 될때까지만 1씩 증가해서 4번 반복해서 i 변수값 출력
		for ( i = 1 ; i <= 4 ; i++ ) {
			System.out.println("i= " + i);
		}
		
		System.out.println("=============================> " + i );
	}

}
