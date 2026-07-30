package app;

// my_module_a 모듈 프로젝트에서 가져옴
import pack1.A;
import pack2.B;

// my_module_b 모듈 프로젝트에서 가져옴
import pack3.C;
import pack4.D;

public class Main {

	public static void main(String[] args) {
		
		// 인스턴스	=> new 생성자().메소드명();
		// 객체 		=> 클래스자료형		참조변수 = new 생성자();
		//							참조변수.메소드명();
		
		new A().method(); // 인스턴스 생성후 인스턴스 메소드 호출
		new B().method();
		
		C	c = new C(); // 객체 생성후
			c.method(); // 객체 메소드 호출
			
		D	d = new D();
			d.method();
	}

}
