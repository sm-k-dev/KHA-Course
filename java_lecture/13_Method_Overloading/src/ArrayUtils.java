
public class ArrayUtils {
	// 메소드 오버로딩
	
	// [1] 정수들이 저장된 배열을 하나 매개변수로 전달 받아
	//		배열에 저장된 값들의 합을 구해 반환하는 기능의 sum 메소드 정의
	public int sum ( int[] array ) { // 예: { 1, 2, 3, 4, 5 } 배열을 매개변수 array로 전달 받을 수 있다.
		
		int total = 0; // 매개변수 array로 전달받은 배열의 정수값들의 합을 누적할 변수
		
		// 향상된 for 반복문 사용하여 매개변수 array로 전달 받은 배열의 각 값을 하나씩 얻어 total에 누적
		for ( int num : array ) {
			total += num;
		}
		
		return total;
	}
	
	// [2] 실수들이 저장된 배열을 하나 매개변수로 전달 받아
	//		배열에 저장된 값들의 합을 구해 반환하는 기능의 sum 메소드 정의
	public double sum ( double[] array ) {
		
		double total = 0;
		
		for ( double num : array ) {
			total += num;
		}
		
		return total;
	}
	
	// [3] 정수들이 저장된 배열 하나를 매개변수로 전달 받아 합계를 계산한 뒤 출력하는 기능의 printSum 메소드 정의
	public void printSum( int[] array ) {
		
		System.out.println("array 매개변수로 받은 배열안의 모든 정수들의 합: " + this.sum(array) );
	}
	
	// [4] 실수들이 저장된 배열 하나를 매개변수로 전달 받아 합계를 계산한 뒤 출력하는 기능의 printSum 메소드 정의
	public void printSum( double[] array ) {
		
		System.out.println("array 매개변수로 받은 배열안의 모든 정수들의 합: " + this.sum(array) );
	}
	
	public static void main(String[] args) {
		
		// 1. ArrayUtils 클래스 설계도로 객체 메모리 생성
		ArrayUtils arrayUtils = new ArrayUtils();
		
		// 2. 정수 들만 저장 된 배열 하나 생성
		int[] intArray = { 1, 2, 3, 4, 5 };
		
		// 3. intArray 배열에 저장된 정수들의 합을 구해서 출력한다.
		arrayUtils.printSum( intArray );
		
		// 4. 실수 들만 저장 된 배열 하나 생성
		double[] doubleArray = { 1.5, 2.5, 3.5 };
		
		// 5. doubleArray 배열에 저장된 정수들의 합을 구해서 출력한다.
		arrayUtils.printSum( doubleArray );
	}

}
