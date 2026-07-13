/*
 * [예제] 정수들이 저장되어 있는 배열을 매개변수로 전달 받아 
 * 배열안의 모든 값을 더한 후 그 결과를 반환하는 sumArray메소드 정의
 * main 메소드에서 호출해서 사용
 * */
public class Example6 {
	
	// sumArray 메소드
	// 기능: 배열에 저장된 모든 값을 더해서 출력
	public static void sumArray(int[] numbers) {
		
		int sum = 0; // 합계를 저장할 변수
		
		// 매개변수로 받은 numbers 배열에 저장된 숫자의 갯수만큼 반복하면서 모든 숫자를 sum에 누적
		for ( int i = 0 ; i < numbers.length ; i++ ) {
			sum += numbers[i];
		}
	}
	
	public static void main(String[] args) {
		
	}
}
