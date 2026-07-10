
public class java_method_exercises {
	
	/*
		문제 1. 두 수의 합 구하기
			- 메서드 이름: sum
			- 매개변수: int a, int b
			- 반환 타입: int
			- 설명: 두 개의 정수를 입력받아 그 합을 반환하는 메서드를 작성하세요.
	*/
	
	public static int sum ( int a, int b ) {
		return a + b;
	}
	
	/*
		문제 2. 문자열 반복 출력하기
			- 메서드 이름: printLoop
			- 매개변수: String str, int n
			- 반환 타입: void
			- 설명: 입력받은 문자열(str)을 n번 반복해서 화면에 출력하는 메서드를 작성하세요. (결과를 반환하지 않고 출력만 합니다.)
	*/
	public static void printLoop ( String str, int n ) {
		for ( int i = 1 ; i <= n ; i++ ) {
			System.out.println(str);
		}
	}
	
	/*
		문제 3. 짝수 판별하기
			- 메서드 이름: isEven
			- 매개변수: int number
			- 반환 타입: boolean
			- 설명: 입력받은 정수가 짝수면 true, 홀수면 false를 반환하는 메서드를 작성하세요.
	*/
	public static boolean isEven(int number) {
		return ( number % 2 == 0 );
	}
	
	/*
		문제 4. 세 정수 중 최댓값 구하기
			- 메서드 이름: maxOfThree
			- 매개변수: int a, int b, int c
			- 반환 타입: int
			- 설명: 세 개의 정수를 입력받아 그중 가장 큰 값을 찾아 반환하는 메서드를 작성하세요.
	*/
	public static int maxOfThree ( int a, int b, int c ) {
		return Math.max(a, Math.max(b, c));
	}

	/*
		문제 5. 절대값 구하기
			- 메서드 이름: absoluteValue
			- 매개변수: int num
			- 반환 타입: int
			- 설명: 정수를 입력받아 그 수의 절대값을 반환하는 메서드를 작성하세요. (예: -5 입력 시 5 반환)
	*/
	public static int absoluteValue(int num) {
		return num >= 0 ? num : -num;
	}
	
	/*
		문제 6. 1부터 N까지의 합 구하기
			- 메서드 이름: sumUpTo
			- 매개변수: int n
			- 반환 타입: int
			- 설명: 1부터 입력받은 정수 n까지의 총합을 구하여 반환하는 메서드를 작성하세요.
	*/
	public static int sumUpTo (int n) {
		int sum = 0;
		
		for ( int i = 1 ; i <= n ; i++ ) {
			sum += i;
		}
		
		return sum;
	}
	
	/*
		문제 7. 특정 글자 개수 세기
			- 메서드 이름: countChar
			- 매개변수: String text, char target
			- 반환 타입: int
			- 설명: 문자열(text) 안에 특정 문자(target)가 몇 번 들어있는지 개수를 세어서 반환하는 메서드를 작성하세요.
	*/
	public static int countChar ( String text, char target ) {
		int count = 0;
		
		for ( int i = 0 ; i < text.length() ; i++ ) {
			
			if ( text.charAt(i) == target ) {
				count++;
			}
		}
		return count;
	}
	
	/*
		문제 8. 배열의 평균값 구하기
			- 메서드 이름: calculateAverage
			- 매개변수: int[] arr
			- 반환 타입: double
			- 설명: 정수형 배열을 입력받아 모든 요소의 평균을 실수형(double)으로 반환하는 메서드를 작성하세요.
	*/
	public static double calculateAverage ( int[] arr ) {
		int sum = 0;
		
		for ( int i = 0 ; i < arr.length ; i++ ) {
			sum += arr[i];
		}
		
		double average = sum / (double)arr.length;
		
		return average;
	}
	
	/*
		문제 9. 팩토리얼(Factorial) 구하기
			- 메서드 이름: factorial
			- 매개변수: int n
			- 반환 타입: long
			- 설명: 정수 n을 입력받아 n! (1부터 n까지의 곱)을 반환하는 메서드를 작성하세요. (곱셈 결과가 매우 커질 수 있으므로 반환 타입을 long으로 설정하세요.)
	*/
	public static long factorial ( int n ) {
		long factorial = 1;
		
		for ( int i = 1 ; i <= n ; i++ ) {
			factorial *= i;
		}
		
		return factorial;
	}
	
	/*
		문제 10. 소수(Prime Number) 판별하기
			- 메서드 이름: isPrime
			- 매개변수: int num
			- 반환 타입: boolean
			- 설명: 정수를 입력받아 그 수가 소수(1과 자기 자신으로만 나누어지는 1보다 큰 자연수)이면 true, 아니면 false를 반환하는 메서드를 작성하세요.
	*/
	public static boolean isPrime ( int num ) {
		
		if ( num <= 1 ) return false;
		
		for ( int i = 2 ; i < num ; i++ ) {
			
			if ( num % i == 0 ) {
				
				
				return false;
			} 
		}
		return true;
	}
	
	public static void main(String[] args) {
		
	}

}
