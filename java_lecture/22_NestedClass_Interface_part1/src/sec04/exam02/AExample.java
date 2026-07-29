package sec04.exam02;


/*
로컬(지역) 중첩 클래스란?

	- 바깥 외부 클래스 내부에 만들기는 하지만
	  바깥 외부 클래스의 생성자 또는 메소드 중괄호 {} 내부에 만들어지는 중첩클래스.
*/
class A { //바깥 외부 클래스 
	
	//생성자
	A() {
		//로컬 중첩 클래스 선언 가능
		class B {    }
		
		//로컬 중첩 클래스B를 이용해 객체 생성 가능
		B  b  = new B();
	}
	
	//메소드
	void method() {
		//로컬 중첩 클래스 선언 가능
		class B {   }
		
		//로컬 중첩 클래스B를 이용해 객체 생성 가능
		B  b  = new B();			
	}
	
	
}





public class AExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
