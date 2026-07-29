package sec05.exam02;

/*
중첩클래스내부에서 바깥 외부 클래스에 대한 객체메모리에 접근방법

		바깥클래스명.this
		
중첩클래스내부에서 중첩클래스에대한 객체메모리에 접근 방법
  
  	     this
*/
//예제.  중첩클래스와 바깥클래스가 동일한 이름의 인스턴스변수와 메소드를 가지고 있을 경우
//		바깥 객체 소속의 변수와 메소드를 사용하는 방법

class A { //바깥 외부 클래스 
	
	String field = "A-Field";  //A 바깥 외부 클래스의 인스턴스변수 
	
	void method() {  System.out.println("A-method"); }  //A 바깥 외부 클래스의 인스턴스 메소드 
	
	//인스턴스 멤버 중첩 클래스 B 선언
	class B {
		
		String field = "B-Field"; //B 인스턴스 멤버 중첩클래스의 인스턴스변수 
		
		void method() { System.out.println("B-method"); } //B 인스턴스 멤버 중첩 클래스 내부의 인스턴스메소드 
				
		void print() { //B 인스턴스 멤버 중첩 클래스 내부의 인스턴스 메소드 하나더 추가
			
			//B인스턴스 멤버 중첩 클래스의 인스턴스변수와 인스턴스 메소드를 사용하기 위해 this 이용
			System.out.println(this.field);
			this.method();
			
			//바깥 외부 A클래스의 객체에 포함된 인스턴스변수와 인스턴스 메소드를 사용하기 위해 바깥클래스명.this 이용
			System.out.println(A.this.field);
			A.this.method();
			
		}
	}
	
	//바깥 외부 A의 인스턴스 메소드 선언
	void useB() {
		B b = new B();
		  b.print();
	}
	
} //===========================> 바깥 외부 A 클래스 끝


public class AExample {
	public static void main(String[] args) {
			//외부 바깥 A객체 메모리의 useB인스턴스메소드 호출
			new A().useB();
			/*
			    B-Field
				B-method
				A-Field
				A-method
			 */
	}

}




