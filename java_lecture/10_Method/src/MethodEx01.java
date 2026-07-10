// [예제] Hello World 출력하는 기능의 메서드 만들기
// MethodEx01 이라는 class 안에 main 메서드
public class MethodEx01 {
	
	// hello_func 메서드
	// 기능: Hello World 라고 출력하는 기능
	public static void hello_func() {
		System.out.println("Hello World");
	}
	
	// main 메서드
	// 기능: 자바프로그램의 시작점, 자바프로그램 처음 가동 시키는 기능
	public static void main(String[] args) {
		
		// hello_func() 메서드를 호출한다.
		hello_func();
	}

}
