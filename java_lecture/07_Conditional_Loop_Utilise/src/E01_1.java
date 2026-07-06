// [예제] 1부터 10사이의 짝수의 합과 홀수의 합 구하기
public class E01_1 {

	public static void main(String[] args) {
		int n;					// 제어변수 선언
		int odd_sum, even_sum;	// 홀수의 합과 짝수의 합을 누적해서 저장할 변수 선언
		
		// 제어변수 값이 홀수가 구해지도록 하여 그 제어변수 값을 누적해서 홀수의 합을 구한다.
		for ( n = 1, odd_sum = 0, even_sum = 0 ; n <= 10 ; n++ ) {
			if ( n % 2 != 0 ) {
				odd_sum += n;
			} else {
				even_sum += n;
			}
		}
		
		System.out.println("odd_sum: " + odd_sum);
		System.out.println("even_sum: " + even_sum);
	}

}
