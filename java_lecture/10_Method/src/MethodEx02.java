// [예제] 주어진 값까지의 자연수의 합 구하기
public class MethodEx02 {
	
	// sum 메서드
	// 기능: 숫자 1부터 n 매개변수로 전달 받은 값까지의 합을 구해 보여주는 기능
	// 메서드 내에서 선언된 변수 = 지역 변수
	// 지역변수는 초기화 시켜준 다음 사용해야 한다.
	public static int sum ( int n ) { // int n 은 매개변수라고 한다.
		int sum = 0; // 합을 누적할 변수
		// int i = 제어변수
		for ( int i = 1 ; i <= n ; i++ ) {
			sum += i;
		}
		System.out.println("sum(int " + n + ") 메서드 내부 실행 => sum = " + sum);
		return sum;
	}
	
	// main 메서드
	// 기능: 자바프로그램의 코드를 처음 실행 시키는 기능
	public static void main(String[] args) {
		
		// sum 메소드를 호출할 때 int n 매개변수로 5를 전달해서 숫자 1 부터 5까지의 합을 구해 출력
		System.out.println("main 메서드에서 호출한 sum(5) = " + sum(5));
		
		System.out.println("===================================================");
		
		// sum 메소드를 호출할 때 int n 매개변수로 10을 전달해서 숫자 1 부터 10까지의 합을 구해 출력
		System.out.println("main 메서드에서 호출한 sum(10) = " + sum(10));
	}

}
