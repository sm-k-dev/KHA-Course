package Ex3;

/*
===============================================================
 SmartTelevision 클래스
---------------------------------------------------------------
 인터페이스 2개를 동시에 구현한 클래스다.

   class SmartTelevision implements RemoteControl, Searchable

 클래스 상속(extends)은 부모를 하나만 지정할 수 있지만,
 인터페이스 구현(implements)은 콤마로 여러 개를 지정할 수 있다.

 두 인터페이스에 선언된 추상메소드 3개
 (turnOn, turnOff, search)를 모두 오버라이딩해야 한다.
 하나라도 빠뜨리면 다음 오류가 난다.

   error: SmartTelevision is not abstract and does not override
          abstract method search(String) in Searchable

 오버라이딩할 때 규칙
  - 메소드 이름, 매개변수, 리턴타입을 그대로 맞춘다.
  - 접근제어자에 public 을 반드시 붙인다.
    인터페이스의 추상메소드는 public 이므로, 더 좁은 범위로 줄일 수 없다.
  - 이클립스 자동 생성 단축키 : Alt + Shift + S 를 누른 뒤 V
===============================================================


   전원을 켜고 끄는 기능  +  검색 기능 이 구성된 클래스 완성 
*/
public class SmartTelevision implements RemoteControl, Searchable {

	//Searchable 부모인터페이스에 만들어 놓은 추상메소드 search를 강제로 오버라이딩
	@Override
	public void search(String url) {
		System.out.println(url + "을 검색합니다.");
	}

	//RemoteControl 부모인터페이스에 만들어 놓은 추상메소드 turnOn 과 turnOff 를 강제로 오버라이딩 
	@Override
	public void turnOn() {
		System.out.println("SmartTelevision의 전원을 켭니다.");
	}
	@Override
	public void turnOff() {
		System.out.println("SmartTelevision의 전원을 끕니다.");
	}

}








