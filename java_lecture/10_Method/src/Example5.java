// [예제] 가변길이 매개변수를 사용해서 합계를 구하는 메소드 만들기
public class Example5 {
	/*
	 * [가변길이 매개변수란]
	 * 	메소드에서 전달받은 값의 개수를 미리 정하지 않고
	 * 	메소드를 호출할 때 원하는 값을 넘길 수 있게 해주는 변수
	 * 
	 * 	자바에서는 ...(점 3개) 를 붙여서 가변길이 매개변수를 표현
	 * 
	 * [가변길이 매개변수 선언 문법]
	 * public static 반환타입 메소드명(자료형... 매개변수명){
	 * 		
	 * }
	 * */
	
	// [메소드 선언]
	// 메소드명: sum
	// 기능: 여러개의 정수(int)를 가변길이 매개변수 numbers로 받아서 모두 더한값을 반환해주는 기능
	public static int sum(int... numbers) {
		
		int total = 0;
		
		for ( int number : numbers ) {
			total += number;
		}
		
		return total;
	}
	
	public static void main(String[] args) {
		System.out.println( "sum(1, 2, 3, 4, 5) = " + sum(1, 2, 3, 4, 5) );
		System.out.println( "sum(1, 2, 3, 4, 5, 6, 7, 8, 9) = " + sum(1, 2, 3, 4, 5, 6, 7, 8, 9) );
		System.out.println( "sum() = " + sum() );
	}

}
