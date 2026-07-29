package sec04.exam03;
/*
로컬 중첩 클래스 B  { }중괄호 내부에는
일반 클래스 처럼 변수, 생성자, 메소드 선언이 올수 있다.
단! 정적변수와 정적메소드는 JAVA 17버전 부터 선언할수 있다.
*/

class A { //외부 바깥 클래스 
	
	//A의 인스턴스 메소드 선언
	void useB() {		
		
		class B {//로컬 중첩 클래스 B 선언
			
			int field1 = 1; //인스턴스 변수 선언 가능
			
			static int field2 = 2; //정적(클래스) 변수 선언 가능
			
			B(){  System.out.println("B-생성자 실행");  } //생성자 선언 가능
			
			void method1() { System.out.println("B-method1 실행"); } //인스턴스 메소드 선언 가능
			
			static void method2() { System.out.println("B-method2 실행"); } //정적(클래스) 메소드 선언 가능			
			
		}//--- class B 	
		
		//로컬 중첩 클래스 B에 대한 객체 생성? 가능
		B  b = new B();
		
		//로컬 중첩 B객체의 인스턴스변수와 인스턴스메소드 호출해서 실행가능? 가능
		System.out.println( b.field1 );
		b.method1();
		
		//로컬 중첩 B클래스의 정적변수와 정적메소드 호출해서 실행가능? 가능
		System.out.println( b.field2  );
		b.method2();
		
	}//--------------------외부 A클래스의  void useB() 메소드 
	
}//---------외부 A클래스 닫는 중괄 호 


public class AExample {

	public static void main(String[] args) {
		
		//외부 A클래스의 객체 생성
		A  a   = new A();
		   a.useB(); //<- 이메소드를 호출 해야만 내부에 작성된 B중첩클래스를 사용할 수 있고 객체도 생성해서 사용할수 있을 것임.

	}

}











