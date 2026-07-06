
public class F02 {

	public static void main(String[] args) {
		int n;
		
		for ( n = 1 ; n <= 10 ; n++ ) {
			
			if ( n % 3 == 0 ) {
				continue;
			}
			System.out.print("\t" + n);
		}
		
		System.out.println("\n =====================================");
		
		int i = 1;
		
		while ( i <= 10 ) {
			if ( i % 3 == 0 ) {
				continue; // 1, 2만 출력된다. continue 명령어가 실행되면서 값을 증가시키는 증감식(i++)을 건너뛰기 때문
			}
			System.out.print("i: " + i + "\t");
			
			i++;
		}
	}

}
