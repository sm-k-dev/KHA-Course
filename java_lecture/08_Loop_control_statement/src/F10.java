
public class F10 {
	
	public static void main(String[] args) {
		int a, i;
		
		for ( a = 1 ; a < 10 ; a++ ) {
			for ( i = 1 ; i <= 10 ; i++ ) {
				if ( i % 3 == 0) {
					break; // 안쪽 for 빠져나감
				}
				System.out.print("\t i -> " + i);
			}
			System.out.println("\n a -> " + a);
		}
		
		System.out.println(" ---------------------------------------- ");
		
		// 레이블명을 exit_for로 지정
		exit_for:
			for ( a = 1 ; a < 10 ; a++ ) {
				for ( i = 1 ; i <= 10 ; i++ ) {
					if ( i % 3 == 0 ) {
						break exit_for; // 레이블을 설정한 부분 (바깥쪽 for) 까지 빠져나감
					}
					System.out.print("\t i -> " + i);
				}
				System.out.println("\n a -> " + a);
			}
		
		System.out.println("\n --------- 바깥 for 다음 코드");
	}

}
