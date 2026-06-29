
public class Opr07 {

	public static void main(String[] args) {
		int a = 10, b = 10;
		
		// 변수 a의 값을 1 올리기
		++a;
		System.out.println("a의 값: " + a);
		
		// 변수 b의 값을 1 내리기
		--b;
		System.out.println("b의 값: " + b);
		
		/*
		 * 중요 - 만일 증가 연산자가 선행 처리 형태로 사용 되었을 때와
		 * 		후행 처리 형태로 사용 되었을 때의 차이점을 살펴 보려면
		 * 		증가 연산자가 다른 문장과 함께 사용되어야 한다. 
		 */
		int c = 1;
		System.out.println(c++);
		System.out.println(c);
	}

}
