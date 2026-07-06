// 예제: 1부터 5사이의 자연수 반복해서 출력
public class While01 {

	public static void main(String[] args) {
		
		int i = 1; // 초기식
		while ( i <= 5 ) { // 조건식
			System.out.print(i + " "); // 반복 처리할 문장
			
			i++; // 증감식
		}
		
		System.out.println("\n==========================================");
		
		for ( int j = 1 ; j <= 5 ; j++ ) {
			System.out.print( j + " ");
		}
	}

}
