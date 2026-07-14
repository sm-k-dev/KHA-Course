/*
 * 스토리: 반려 동물은 배고픔 수치 ( 0 ~ 100 )를 가진다.
 * 		먹이를 주면 배고픔이 줄고, 놀아주면 배고픔이 늘어난다.
 * */

// 1단계 : 현실의 반려동물 객체 모델링
//	- 데이터: 이름(name), 배고픔(hunger, 0 ~ 100)
//	- 기능: 먹이주기(feed), 놀아주기(play), 상태 확인 (checkStatus)

// 2단계 : 자바코드로 class 설계
// class 명: PetTest
class PetTest {
	// class 변수 선언
	String name;	// 반려동물 이름 저장
	int hunger;		// 배고픔 저장할 변수
	
	// class 메소드 선언
	// 메소드명: feed
	// 기능: hunger를 amount 만큼 줄인다 (0보다 작아지지 않게)
	void feed( int amount ) {
		
		// 현재 배고픔에서 먹인 양 만큼 차감
		if ( hunger >= amount ) {
			hunger -= amount;
		} else {
			hunger = 0;
		}
		System.out.println(name + "(이)에게 밥을 먹였다! 배고픔: " + hunger + "%");
	}
	
	// 메소드명: play
	// 기능: 배고픔 증가
	void play ( int amount ) {
		// 현재 배고픔에서 논 만큼 증가
		if ( amount + hunger <= 100 ) {
			hunger += amount;
		} else {
			hunger = 100;
		}
		System.out.println(name + "(이)와 놀아주었다. 현재 배고픔: " + hunger + "%");
	}
	
	// 메소드명: checkStatus
	// 기능: 배고픔 상태 출력
	void checkStatus () {
		System.out.println(name + "의 현재 배고픔 수치: " + hunger + "%");
	}
}

// Main 설계도(클래스) 역할: 자바 프로그램을 가동시키는 main 메소드를 포함하고 있는 설계도 일 뿐이다.
// .java 파일 명과 같은 이름을 가지고 있는 class는 public을 꼭 붙여야 한다.
public class Main {

	public static void main(String[] args) {
		PetTest p = new PetTest();
		
		p.name = "후추";
		p.hunger = 50;
		
		p.play(30);
		p.feed(20);
	}

}
