package pack1;

import pack2.B; // 같은 my_module_a 모듈 프로젝트의 다른 pack2 패키지의 B 클래스 가져옴

public class A {
	// 메소드 선언
	public void method() {
		System.out.println("A-method 실행");
		
		// B 클래스 사용
		B	b = new B();
			b.method();
	}
}
