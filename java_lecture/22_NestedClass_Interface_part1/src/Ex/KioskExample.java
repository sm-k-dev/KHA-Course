package Ex;

/*
	[문제 5] 익명 자식 객체 - 키오스크 인사말 교체
	 자식 클래스 파일을 새로 만들지 않고, 익명 자식 객체로
	 say() 를 오버라이딩해서 인사말을 교체하라.
	
	 [완성 후 실행 결과]
	 안녕하세요.
	 어서 오세요! 주문을 도와드릴게요.
*/

// [제공됨] 부모 클래스
class Greeting {
	public void say() {
		System.out.println("안녕하세요.");
	}
}

// [제공됨] 키오스크: 인사말 객체를 가지고 있다가 시작할 때 출력한다.
class Kiosk {

	// 부모 자료형 변수 => 부모 객체도, 익명자식객체도 저장 가능 (다형성)
	private Greeting greeting = new Greeting();

	public void setGreeting(Greeting greeting) {
		this.greeting = greeting;
	}

	public void start() {
		this.greeting.say();
	}
}

public class KioskExample {
	public static void main(String[] args) {

		Kiosk kiosk = new Kiosk();

		kiosk.start(); // 기본 인사말 출력

		// TODO 5-1.
		//  익명 자식 객체를 만들어 say() 를
		//  "어서 오세요! 주문을 도와드릴게요." 출력으로 오버라이딩하고
		//  setGreeting() 으로 교체하세요.
		//  작성 문법 : kiosk.setGreeting(new Greeting() { ... });
		kiosk.setGreeting(new Greeting() {
			@Override
			public void say() {
				super.say();
				System.out.println("어서 오세요! 주문을 도와드릴게요.");
			}
		});

		// TODO 5-2. 다시 start() 를 호출해 교체된 인사말을 확인하세요.
		kiosk.start();
	}
}
