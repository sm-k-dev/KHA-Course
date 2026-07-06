// 예제: 1부터 10사이의 짝수의 합 구해서 출력
public class While02 {

	public static void main(String[] args) {
		
		int sum = 0; // 1부터 10 사이의 짝수 합 저장할 누적 변수
		
		int i = 0; // 초기식
		
		while ( i <= 10 ) { // 조건식
			sum += i;
			i += 2; // 증감식
			
			System.out.println(sum);
		}
		
		System.out.println("final sum: " + sum);
	}

}
