// 예제: 2단부터 9단까지 구구단 출력하기
public class E03 {

	public static void main(String[] args) {
		for ( int i = 0 ; i <= 9 ; i++ ) {
			for ( int j = 2 ; j <= 9 ; j++ ) {
				if ( i == 0 ) {
					System.out.print("=== " + j + " 단 ===\t");
				} else {
					System.out.print( j + " X " + i + " = " + ( j * i ) + "\t");
				}
			}
			System.out.print("\n");
		}
	}

}
