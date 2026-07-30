package app;

/*
	classPath에 등록해둔 my_lib.jar 라이브러리 파일에 포함된
	pack1 패키지 경로에 만들어져 있는 class A를
	현재 Main.java파일의 class Main 내부에서 사용하기 위해
	import 구문으로 불러온다.
*/
import pack1.A;
import pack2.B;

public class Main {

	public static void main(String[] args) {
		// my_lib.jar 라이브러리 압축 파일에서 불러온 class A의 객체 생성 가능
		A a = new A();
		a.method();
		
		// my_lib.jar 라이브러리 압축 파일에서 불러온 class B의 객체 생성 가능
		B b = new B();
		b.method();
	}

}
