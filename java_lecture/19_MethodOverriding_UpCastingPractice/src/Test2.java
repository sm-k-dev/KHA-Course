
//주제 :  업캐스팅을 하는 이유 응용2


//부모클래스 :  알파벳 설계도 
class Alphabet{
	
/*	
	//안좋은 방법의 메소드 정의 예 	
		//A1 자식 객체 메모리 주소 하나를 매개변수로 전달 받아서 처리하는 메소드 
		public static void printA(A1   a1) { //<--- new A1();
			
			a1.display();//A1클래스 내부에 만들어진 오버라이딩된 display 메소드 최종 실행!
		}
		
		//B1 자식 객체 메모리 주소 하나를 매개변수로 전달 받아서 처리하는 메소드 
		public static void printB(B1   b1) { //<--- new B1();
			
			b1.display();//B1클래스 내부에 만들어진 오버라이딩된 display 메소드 최종 실행!
		}	
		
		//C1 자식 객체 메모리 주소 하나를 매개변수로 전달 받아서 처리하는 메소드 
		public static void printC(C1   c1) { //<--- new C1();
			
			c1.display();//C1클래스 내부에 만들어진 오버라이딩된 display 메소드 최종 실행!
		}		
		
*/
	
	
	//코드 가독성 있게 메소드 정의 예
		//Alphabet 부모클래스의 모든 자식객체의 주소를 하나의 매개변수로 모두 전달 받을 수 있게 
		//부모클래스자료형  참조변수   로  매개변수 선언 	
	public static void printAlphabet(Alphabet  alphabet) {  //<=  new A1();  업캐스팅으로 자식객체 전달받음
															//<=  new B1();
															//<=  new C1();
		     alphabet.display();
	 	//	new A1().display(); //-> 객체 메모리 내부의 오버라이딩 된 객체 메소드 최종 실행!
	 	//	new B1().display(); //-> 객체 메모리 내부의 오버라이딩 된 객체 메소드 최종 실행!
	 	//	new C1().display(); //-> 객체 메모리 내부의 오버라이딩 된 객체 메소드 최종 실행!	
	}

	public void display() {
		System.out.println("I am the parent class:Alphabet");
	}
}

//자식클래스1  : A1 알파벳 설계도
class A1  extends Alphabet {	
	
	@Override    //메소드 오버라이딩
	public void display() {
		System.out.println("I am class A1");
	}
}

//자식클래스2  : B1 알파벳 설계도
class B1  extends Alphabet {	
	
	@Override    //메소드 오버라이딩
	public void display() {
		System.out.println("I am class B1");
	}
}

//자식클래스3  : C1 알파벳 설계도
class C1  extends Alphabet {	
	
	@Override    //메소드 오버라이딩
	public void display() {
		System.out.println("I am class C1");
	}
}
public class Test2 {
	public static void main(String[] args) {
		//추천하는 코드예 
		Alphabet.printAlphabet( new A1()  );
		Alphabet.printAlphabet( new B1()  );
		Alphabet.printAlphabet( new C1()  );
	}

}




