/*
	주제: 상속 관계에서 서브(자식)클래스의 객체 메모리가 생성될 때
		생성자가 호출되는데 자동으로 어떤 부모생성자가 호출되고 실행 되는가?
		
		참고. 조합1.
			서브(자식)클래스의 객체 메모리가 생성될 때
			서브(자식)클래스의 기본생성자를 호출하면
			슈퍼(부모)클래스의 기본생성자가 작성되어 있으면? 자동(super();)으로 호출되는 조합.
		
		참고. super();	: 부모클래스의 기본 생성자 호출 하는 구문
			super		: 부모 객체 메모리의 주소 번지를 기억하는 예약어.
			
		참고. 부모 객체 메모리 내부의 객체변수 또는 객체메소드 호출해서 사용하는 작성 방법.
			super.객체변수명 = 저장할값;
			super.객체메소드명();
*/

class AA /* extends Object 가 생략된 것 */ { // 부모 클래스
	
	// 기본생성자
	public AA() {
		// super(); // 부모 Object 클래스의 기본 생성자 호출 하는 구문 생략 된 것
		System.out.println("AA 부모 클래스의 기본생성자");
	}
	
	// 매개변수가 1개 작성된 생성자
	public AA(int x) {
		// super(); // 부모 Object 클래스의 기본 생성자 호출 하는 구문 생략 된 것
		System.out.println("AA 부모 클래스의 매개변수가 1개 작성된 생성자");
	}
}

// 자식 클래스
class BB extends AA {
	
	// 기본생성자: 매개변수를 작성하지 않는 생성자
	public BB () {
		// super(); // 부모 A 클래스의 기본생성자 AA()를 호출하는 구문 생략되어 있음
		System.out.println("BB 자식 클래스의 기본생성자");
	}
}

public class Ex2 {

	public static void main(String[] args) {
		
	}

}
