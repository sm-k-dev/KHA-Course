package WrapperClass;
//주제 : String 객체 메모리에 저장된 문자열을 해당 기본자료형의 데이터로 변환하는 연습
//      그리고 반대로 기본자료형의 데이터를 문자열로 변환하는 연습
/*
[이 예제에서 사용하는 래퍼클래스의 static 메소드 2종류]

1. parseXxx("문자열")  : 문자열 -> 기본자료형 값 으로 변환
예) Integer.parseInt("123")      -> int 123
   Boolean.parseBoolean("true") -> boolean true
	 Float.parseFloat("3.14")     -> float 3.14f
※ 메소드 이름 규칙: parse + 기본자료형이름 (parseInt, parseDouble, parseBoolean ...)

2. toString(기본자료형값) : 기본자료형 값 -> 문자열 로 변환
예) Integer.toString(123)   -> "123"
   Boolean.toString(true)  -> "true"

[왜 필요한가?]
- 키보드 입력, 파일, 화면의 입력창 등에서 들어오는 데이터는 전부 "문자열"이다.
- 문자열 "123"은 숫자처럼 보여도 산술연산(+ - * /)을 할 수 없다.
("123" + 1 은 124가 아니라 문자열 결합 "1231"이 된다.)
- 그래서 계산이 필요하면 문자열을 기본자료형으로 변환해야 하고,
화면에 붙여서 보여주거나 저장할 때는 반대로 문자열로 변환한다.
*/
public class Test2 {
	public static void main(String[] args) {
//===================================================
// 1부.  문자열 -> 기본자료형  변환  (parseXxx 메소드 사용)
//===================================================
		
		//"123" 문자열을 기본자료형 값 123으로 변환하여 반환받아  int i 변수에 저장
		int i = Integer.parseInt("123");  //"123" ---변환 해서 반환--> 123
		//123
		//주의 : "123a" 처럼 숫자로 바꿀 수 없는 문자열을 넣으면 NumberFormatException 예외가 발생한다.
		
		//"true" 문자열을 기본자료형 값 true 로 변환하여 반환받아 boolean 변수에 저장
		boolean b = Boolean.parseBoolean("true");//"true" ---변환 해서 반환--> true
		//true
		// 참고: 대소문자는 구분하지 않는다. "TRUE", "True"도 true 로 변환된다.
		//      "true"가 아닌 모든 문자열("yes", "1" 포함)은 false 로 변환된다.
		
		//"3.141592" 문자열을 기본자료형 값 3.141592f로 변환하여 반환받아 float f 변수에 저장
		float f =   Float.parseFloat("3.141592");//"3.141592" ---변환 해서 반환--> 3.141592f
		//3.141592f
		
		// 참고: 변환 대상 자료형만 다를 뿐, 동작 원리는 parseInt와 완전히 같다.
		//      (Double.parseDouble, Long.parseLong, Byte.parseByte 등도 전부 동일한 규칙)
		
//================================================================
//2부.  기본자료형  ->  문자열로 변환 (toString() 메소드 사용)
//================================================================
		
		//정수 123을  문자열 "123"으로 변환 후, 저장된 문자열 객체 메모리의 주소를 반환받아 String s1참조변수에 저장
		String s1 = Integer.toString(123); //123 -----변환해서 반환----> "123"
//      String s1 = "123";

		//정수 123을 16진수 형태의 문자열 "7b"로 변환후, 저장된 문자열 개체 메모리의 주소를 반환받아 String s2참조변수에 저장
		String s2 = Integer.toHexString(123); //123 ----변환해서 반환---> "7b"
//      String s2 = "7b";
		// 참고: 16진수는 0~9 다음을 a~f로 표현하는 진법이다.
		//      123 = (16 × 7) + 11 이고, 11을 16진수로 쓰면 b이므로 결과가 "7b"가 된다.
		//      2진수 변환은 toBinaryString(123) -> "1111011", 8진수는 toOctalString(123) -> "173"		
		
		
		//문자 'a'를 문자열 "a"로 변환 후, 저장된 문자열 객체 메모리의 주소번지를 반환받아 String s3참조변수에 저장
		String s3 = Character.toString('a');//'a' ----변환해서 반환----> "a"
//      String s3 = "a";
		
		//boolean 자료형의 값 true를 문자열 "true"로 변환 후, 저장된 문자열 객체 메모리의 주소를 반환받아 String s4참조변수에 저장
		String s4 = Boolean.toString(true);//true ---변환해서 반환---->"true"
//      String s4 = "true";
		
//============================================================
// 반환 결과 확인용 출력
//============================================================
		System.out.println("==== 1부. 문자열 -> 기본자료형 으로 변환 ====");
		System.out.println("i = " + i);   //123
		System.out.println("b = " + b);   //true
		System.out.println("f = " + f);   //3.141592
		//기본자료형으로 변환되었으므로 산술연산이 가능함을 확인
		System.out.println("i + 1 = " + (i + 1)); //124 <- 진짜 숫자라서 계산이 된다!
		
		System.out.println("==== 2부. 기본자료형 -> 문자열 로 변환 ====");
		System.out.println("s1 = " + s1); //"123"
		System.out.println("s2 = " + s2); //"7b"
		System.out.println("s3 = " + s3); //"a"
		System.out.println("s4 = " + s4); //"true"
		//문자열로 변환되었으므로 +  는 산술아 아니라 문자열 결합이 됨을 확인
		System.out.println("s1 + 1 = " + (s1 + 1)); //"1231" <- 문자열이라서 뒤에 "1"이 이어 붙는다!
	

	}

}







