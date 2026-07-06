// [예제] 1부터 10사이의 짝수의 합과 홀수의 합 구하기
public class E01 {

	public static void main(String[] args) {
		int n;					// 제어변수 선언
		int odd_sum, even_sum;	// 홀수의 합과 짝수의 합을 누적해서 저장할 변수 선언
		
		// 제어변수 값이 홀수가 구해지도록 하여 그 제어변수 값을 누적해서 홀수의 합을 구한다.
		// 자바는 초기식 자리에 두개의 변수를 넣을 수 있다. 1개 이상의 변수에 초기삾을 지정할때 , 로 연결
		for ( odd_sum = 0, n = 1 ; n <= 10 ; n += 2 ) {
			odd_sum += n;
		}
		System.out.println("odd_sum: " + odd_sum);
		
		for ( even_sum = 0, n = 2 ; n <= 10 ; n += 2 ) {
			even_sum += n;
		}
		System.out.println("odd_sum: " + even_sum);
	}

}
