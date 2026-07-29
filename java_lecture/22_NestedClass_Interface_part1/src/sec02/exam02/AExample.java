package sec02.exam02;
/*
중첩 클래스 종류 중에서 인스턴스 멤버 중첩 클래스 사용 예


예제. 인스턴스 멤버 중첩클래스 B 내부에는 
    일반적인 클래스와 똑같이  변수(필드), 생성자,  메소드를 작성 해 놓을 수 있다.
    그리고
    정적(static,클래스)변수 와 정적(staitc,클래스)메소드는  java JDK 17버전이상 설치해야 작성이 가능하다. 
*/
class A { //바깥 외부 클래스 
		
	/* public A() {}  자바컴파일러가 기본생성자 자동추가 */
	
	//인스턴스 멤버 중첩 클래스 B 만들기
	class B {
		
		int filed1 = 1;  //인스턴스 변수 선언 가능!
		
		static int filed2 = 2;  //클래스 변수 선언 가능! (JDK 17버전 부터 작성 가능)
		
		B(){ System.out.println("B-생성자 실행됨");}  //생성자 선언 가능!
		
		void method1() { System.out.println("B-method1 실행됨"); } //인스턴스 메소드 선언 가능!
		
		static void method2() { System.out.println("B-method2 실행됨"); }  //클래스 메소드 선언 가능! (JDK 17버전 부터 작성 가능)
	}
	
	//바깥 외부 A클래스의 인스턴스 메소드 선언
	void useB() {
		// B 인스턴스 멤버 중첩 클래스를 이용해 객체 생성 및  메소드 호출해서 사용가능 한가?
		B  b = new B();
		   b.method1();  					//인스턴스  메소드 호출 가능!
		   System.out.println( b.filed1 );  //인스턴스 변수 값 가져와 출력 가능!
	}
	
}

public class AExample {

	public static void main(String[] args) {
		//바깥 외부 A클래스의 객체 생성 
		A  a  = new A();
		
		//바깥 외부 A클래스의 객체 메모리에 포함된  useB() 인스터스 메소드 호출!
		a.useB();
							/*
									JVM 메모리 전체 메모리 영역 
							────────────────────────────────────────────────────────────────────────────────────
							
							Method Area
							└─ A, B 설계도(클래스)들
							
							Stack
							└─ a ─────────▶ 0x100
							
							Heap
							├─ A 객체 (0x100)
							└─ B 객체 (0x200)
							────────────────────────────────────────────────────────────────────────────────────
							
							*/		
	}

}









