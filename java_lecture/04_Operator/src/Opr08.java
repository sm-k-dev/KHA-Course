// 예제) 증가연산자 ++ 감소연산자 -- 의 선행처리와 후행처리
public class Opr08 {

	public static void main(String[] args) {
		int a = 10, b = 10;
		
		System.out.println( "++a: " + ++a );
		System.out.println( "a: " + a );
		System.out.println( "===============" );
		
		System.out.println( "b++: " + b++ );
		System.out.println( "b: " + b );
		
		a = b = 10 ;
		
		int c;
		
		c = ++a;
		
		System.out.println("c: " + c + ", a: " + a);
	}

}
