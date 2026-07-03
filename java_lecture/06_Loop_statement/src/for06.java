/*
 * 예제 주제: 1부터 100까지 사이의 소수를 모두 출력하고, 그 총 합 구하기
 * 
 * 	※ 소수(prime): 1보다 큰 자연수 중에서 1과 자기 자신만을 약수로 가지는 수
 * */
public class for06 {

	public static void main(String[] args) {
		
		int primeSum = 0; // 소수의 합을 누적할 변수
		int count = 0; // 소수의 개수를 저장할 변수
		
		System.out.println("<< 1부터 100 사이의 소수 목록 >>");
		
		for ( int i = 2 ; i <= 100 ; i++ ) {
			boolean isPrime = true; // true: i 변수 값이 소수라고 가정하고 시작
			
			// 2부터 자기 자신 (i 변수값) 보다 1 작은수 까지 나누어 본다.
			for ( int j = 2 ; j < i ; j++ ) {
				if ( i % j == 0 ) {
					isPrime = false;
					break;
				}
			}
			
			if ( isPrime ) {
				System.out.print(i + "\t");
				primeSum += i;
				count++;
				
				if ( count % 5 == 0 ) {
					System.out.println();
				}
			}
		}
		System.out.println("소수의 총 합: " + primeSum);
		System.out.println("소수의 총 개수: " + count);
	}

}
