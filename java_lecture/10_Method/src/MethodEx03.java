// [예제] 정수 하나를 매개변수로 전달 받아서 절대값을 구해 되돌려주는 메서드
public class MethodEx03 {
	
	public static int abs (int n) {
		
		if ( n < 0) {
			n = -n;
		}
		
		return n;
	}
	
	// main 메서드
	// 기능: MethodEx03 클래스를 시작시키는 시작점
	public static void main(String[] args) {
		System.out.println("abs(-10): " + abs(-10));
		System.out.println("abs(-5): " + abs(-5));
		System.out.println("abs(6): " + abs(6));
	}

}
