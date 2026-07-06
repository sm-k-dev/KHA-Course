// 사용자가 키보드를 사용해 입력한 내용을 받기 위한 Scanner 라는 이름의 클래스 사용을 위해 불러오기 위한 구문 
import java.util.Random;
import java.util.Scanner;

public class GuesslingGame {

	public static void main(String[] args) {
		
		// Scanner 객체 생성: 사용자가 키보드로 입력한 값을 읽어오려면 Scanner 객체가 필요하다.
		Scanner sc = new Scanner(System.in);
		
		// Random 객체 생성: 랜덤(무작위) 숫자를 생성할때 사용하는 도구
		Random rd = new Random();
		
		// rd.nextInt(10) 은 숫자 0 부터 9까지 중 하나의 정수를 만든다.
		// + 1 을 하면 범위가 1부터 10이 된다.
		int targetNumber = rd.nextInt(10) + 1; // 사용자가 맞춰야 할 정답 숫자 (랜덤 숫자)
		
		int userGuess; // 사용자가 입력한 추측 숫자를 저장할 변수 선언
		
		// 사용자에게 게임 설명 출력
		System.out.println("숫자 추측 게임에 오신 것을 환영합니다.");
		System.out.println("제가 생각한 1 부터 10 사이의 숫자를 맞춰 보세요.");
		
		// do-while 반복문: 최소 한번은 반복문 내부의 실행코드가 실행되고, 조건식을 검사하여 계속 반복할지 결정.
		do {
			System.out.print("예상하시는 숫자를 입력해 주세요: "); // 입력 안내 메세지 출력
			// sc.nextInt() 는 사용자가 입력한 정수를 읽어서 반환한다.
			// 예: 사용자가 7을 입력하고 엔터를 누르면 userGuess 변수값은 7이 저장된다.
			userGuess = sc.nextInt();
			
			// 사용자의 입력값이 정답보다 작으면 힌트 메세지 출력
			if (userGuess < targetNumber) {
				System.out.println("입력한 수가 작습니다. 다시 입력 해 주세요");
			} else if (userGuess > targetNumber) {
				System.out.println("입력한 수가 큽니다. 다시 입력 해 주세요");
			}
			// 만약 컴퓨터가 내는 난수와 사용자가 입력한 수와 같다면? 메세지 출력하지 않고 반복문을 빠져나온다.
		} while ( targetNumber != userGuess );
		// 사용자가 정답을 맞출 때 까지 반복
		
		// 정답을 맞췄을 때 출력되는 메세지
		System.out.println("정답입니다. 숫자는 " + targetNumber + " 였습니다.");
		
		// 키보드와 연결된 Scanner 객체 메모리 해제
		sc.close();
	}

}
