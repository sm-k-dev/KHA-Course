/*
 	조합3. 상속관게에서 자식 객체 메모리 생성시 매개변수가 1개 작성된 생성자를 호출하면
 			부모 클래스의 기본생성자가 super(); 구문에 의해 자동으로 호출되는 조합
 			
 			new 자식클래스생성자(값); => 부모클래스의 기본생성자 가 호출되어 실행됨
*/
class F { // 부모클래스
	
	public F () { System.out.println("부모 F 클래스 기본 생성자"); } // F의 기본 생성자
	
	public F ( int d ) { System.out.println("부모 F의 매개변수 1개 작성된 생성자"); } // F의 매개변수 1개인 생성자
}
	
class G extends F {
	public G () {
		System.out.println("자식 G의 기본생성자");
	}
	
	public G (int x) {
		System.out.println("자식 G의 매개변수가 1개 작성된 생성자");
	}
}

public class Ex3 {

	public static void main(String[] args) {
		
	}
}