// 반복 처리할 문장을 한번 수행한 후 조건식을 나중에 검사해 반복하는 do while 반복문 예제
public class While03 {

	public static void main(String[] args) {
		
		int i = 1;
		do {
			// 뮤조건 한번 실행 한 후 다시 조건식이 참이면 실행될 코드
			System.out.println(i);
			i++;
		} while ( i <= 5 ); // 조건식을 검사하고, 조건식이 참이면 다시 do 구문을 실행
	}

}
