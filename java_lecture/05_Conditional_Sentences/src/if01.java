/*
 * 	절대값: 음수이면 부호를 변경
 * */

public class if01 {

	public static void main(String[] args) {
		int num;
		
		num = -5;
		
		// 조건문: 변수 num에 저장된 값이 0보다 작은 음수인가
		//		컴퓨터에게 묻는 조건식을 조건문으로 작성
		if ( num < 0 ) {
			// num = -(-5);
			num = -num;
		}
		
		System.out.println("absolute num = " + num);
		
		num = 5;
		
		if ( num < 0 ) {
			num = -num;
		}
		
		System.out.println("absolute num = " + num);
	}

}
