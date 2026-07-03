// 주제: 구구단 2단 ~ 9단까지 가로 출력
public class for04 {

	public static void main(String[] args) {
		for ( int i = 0 ; i <= 9 ; i++ ) {
			for ( int j = 2 ; j <= 9 ; j++ ) {
				
				if ( i == 0 ) {
					System.out.print( "<<-- " + j + " 단 -->> \t" );
				} else {
					System.out.print( j + " X " + i + " = " + ( j * i ) + "\t" );
				}
			}
			System.out.println();
		}
	}

}
