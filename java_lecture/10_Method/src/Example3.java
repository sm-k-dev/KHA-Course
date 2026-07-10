// [예제] 반환할 값이 없고 매개변수도 없는 메소드 정의 및 호출해서 사용
public class Example3 {
	
	/*
	 * 	반환할 값이 없고 매개변수도 없는 메소드 작성
	 * 		
	 * 		public static void 메도스명() {
	 * 			메소드가 해야 할 기능 코드;
	 * 		}
	 * 
	 * 	void: 반환할 값이 없다.
	 * */
	
	public static void greet() {
		System.out.println("안녕하세요");
	}
	
	public static void main(String[] args) {
		
		// greet 메소드 호출
		greet();
		
		// greet 10번 호출
		for ( int i = 1 ; i <= 10 ; i++ ) {
			greet();
		}
	}

}
