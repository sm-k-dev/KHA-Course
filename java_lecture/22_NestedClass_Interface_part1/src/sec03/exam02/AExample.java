package sec03.exam02;

/*
 정적 멤버 중첩 클래스에 대한 예 
 
 예제. 정적 멤버 중첩 클래스 B 내부에는 일반클래스와 똑같이 
      변수,  생성자, 메소드 를 선언할 수 있다.
*/
class A {//바깥 외부 클래스 
	
	//정적 멤버 중첩 클래스 B 선언
	static class B {
		int field1 = 1; //인스턴스 변수 선언 가능
		
		static int field2 = 2; //정적(클래스) 변수 선언 가능
		
		B(){  System.out.println("B-생성자 실행");  } //생성자 선언 가능
		
		void method1() { System.out.println("B-method1 실행"); } //인스턴스 메소드 선언 가능
		
		static void method2() { System.out.println("B-method2 실행"); } //정적(클래스) 메소드 선언 가능
	}	
}


public class AExample {

	public static void main(String[] args) {
		
		//B 정적 멤버 중첩 클래스의 객체 생성
		A.B  b = new A.B();
		
		//B 객체의 인스턴스 변수 및 인스턴스 메소드 사용 가능!
		System.out.println( b.field1 );//1
		b.method1(); //"B-method1 실행"
		
		//-------------------------------------
		
		//B 정적 멤버 중첩 클래스 내부에 만들어 놓은  정적변수 사용!
		//작성문법
		//->     바깥클래스명.정적멤버중첩클래스명.정적변수명;
		System.out.println( A.B.field2 ); //2
		
		//B 정적 멤버 중첩 클래스 내부에 만들어 놓은 정적메소드 사용!
		//작성문법
		//->     바깥클래스명.정적멤버중첩클래스명.정적메소드명();
						 A.B.method2();  //B-method2 실행

	}

}










