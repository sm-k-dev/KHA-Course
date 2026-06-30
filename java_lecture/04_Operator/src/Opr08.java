// 예제) 증가연산자 ++ 감소연산자 -- 의 선행처리와 후행처리
public class Opr08 {

	public static void main(String[] args) {
		int a = 10, b = 10;
		
		System.out.println( "++a: " + ++a );
		System.out.println( "a: " + a );
		System.out.println( "===============" );
		
		System.out.println( "b++: " + b++ );
		System.out.println( "b: " + b );

		/*
			연산 순서
			1. b변수에 10을 저장
			2. a변수에 b변수에 저장된 10을 다시 저장
			결론 : a, b변수에 저장된 값은 모두 10이 됨 
			
			a = 10,  b = 10
		*/
		a = b = 10 ;
		
		int c;
		
		c = ++a;
		
		System.out.println("c: " + c + ", a: " + a);
	}

}
