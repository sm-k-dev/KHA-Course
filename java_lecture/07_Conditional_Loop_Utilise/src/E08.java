// [예제] 다중 for문에서 제어변수의 변화 알아보기
public class E08 {

	public static void main(String[] args) {
		/*
			int i;	// 안쪽 for 문에서 사용할 제어변수
			int a;	// 바깥 for 문에서 사용할 제어변수
			
			System.out.println("시침 -------------------> 분침");
			System.out.println("a(바깥 쪽 제어변수) -------> i(안쪽제어변수)");
			
			for ( a = 1 ; a <= 12 ; a++ ) {
				
				for ( i = 1 ; i <= 60 ; i++ ) {
					System.out.println( a + "시 --------------------> " + i + "분");
				}
			}
		*/
		
		int hour, min, sec;
		
		System.out.println("시침 -----------------------> 분침 ----------------------> 초침");
		System.out.println("hour(제일 바깥 쪽 제어변수) ----> min(중간 제어변수) ----------> sec (제일 안쪽 제어변수)");
		
		for ( hour = 1 ; hour <= 12 ; hour++ ) {
			for ( min = 1 ; min <= 60 ; min+=10 ) {
				for ( sec = 1 ; sec <= 60 ; sec+=30 ) {
					System.out.println( hour + "시 -----------------------> " + min + "분 ----------------------> " + sec + "초");
				}
			}
		}
	}

}
