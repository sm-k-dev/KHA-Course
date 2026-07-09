package array_quiz;

/*
	 문제. 배열 arr에 담긴 모든 값을 더하는 프로그램을 완성하시오.
	      즉   (1)영역에 들어 갈 코드를 작성 하시오.
	
	출력결과
	sum=150
*/
public class test7 {
	public static void main(String[] args) {
		
		int[] arr = {10,20,30,40,50};
		//			 0   1  2  3  4   index	
		int sum = 0;
		
		for ( int i = 0 ; i < arr.length ; i++ ) {
			sum += arr[i];
		}
		
		System.out.println("sum= " + sum);	
		
		// 향상된 for 반복문
		sum = 0;
		for ( int num : arr ) {
			sum += num;
		}
		
		System.out.println("sum= " + sum);	
	}

}