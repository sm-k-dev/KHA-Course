package app;

// my_module_a 모듈 프로젝트에서 pack1 패키지에 만들어 놓은 class A 불러오기
import pack1.A;
// import pack2.B;
import pack3.C;
import pack4.D;

public class Main {

	public static void main(String[] args) {
		A a = new A();
		a.method();
		
		/*
		B b = new B(); b.method();
		*/
		
		C c = new C();
		c.method();
		
		D d = new D();
		d.method();
	}

}
