/*
 * 메서드 명: findSecondMax
 * 매개변수: int[] numbers
 * 기능: 1.numbers 배열에서 가장 큰 값과 두번째로 큰 값을 찾기
 * 		2. 두번째로 큰 값 출력
 * 
 * 조건: 1. numbers 배열에 같은 값이 있어도 두 번째로 큰 "다른 값"을 찾아야 한다.
 * 		2. 최대값과 두 번째 최대값이 같은 경우는 두 번째 최대값으로 한번만 출력
 * */
public class Example8 {
	public static int findSecondMax(int[] numbers) {
		// 매개변수 numbers로 받은 배열의 값들을 모두 검사해서 가장 큰 최대값 하나 저장
		int max = numbers[0];
		
		// 매개변수 numbers로 받은 배열의 값들을 모두 검사해서 두번째료 큰 값 하나 저장
		int secondMax = Integer.MIN_VALUE; // Integer.MIN_VALUE 는 int 형이 가질 수 있는 가장 작은 수 ( -2147483648 ) 이다.
		/*
		 * 자세한 이유
		 * - 만약 secondMax를 0이나 임의의 값으로 정하면,
		 * 		배열에 음수가 많거나 그 임의값보다 작은 값들이 오면 잘못된 결과가 나올 수 있다.
		 * - 그래서 "어떤 값이 오든 우선은 교체될 수 있도록" 가능한 최소값으로 시작시키는 것
		 * */
		
		for ( int i = 1 ; i < numbers.length ; i++ ) {
			
			// current 변수에 현재 검사 중인 값을 저장
			int current = numbers[i];
			
			// ----- 경우 1 ---------------------------
			// 만약 현재 검사 값 (current)이 지금까지의 최대값 (max)보다 크면?
			// -> current 가 새로 최대값(max)이 되며,
			// -> 기존 max값은 두번째로 큰 값 (secondMax)이 된다.
			if ( current > max ) {
				secondMax = max;
				max = current;
			
			// ----- 경우 2 ---------------------------
			// current가 max 보다 크지 않다면, 두번째로 큰 값 후보인지 확인
			// 조건: current가 secondMax보다 크고, 동시에 max와 같지 않아야 한다.
			} else if ( current > secondMax && current != max ) {
				secondMax = current;
			}
		}
		
		return secondMax;
	}
	
	public static void main(String[] args) {
		// arr 배열 생성
		int[] arr = { 5, 1, 9, 2, 9, 7, 3 };
		
		// findSecondMax 메소드를 호출할때 int[] numbers 매개변수에 arr 배열 주소 번지를 전달
		// 배열에서 두번째로 큰 값을 찾는다.
		System.out.println("두번째로 큰 값은? = " + findSecondMax(arr));
	}

}
