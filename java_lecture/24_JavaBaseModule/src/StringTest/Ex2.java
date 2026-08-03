package StringTest;
/* ============================================================================
 *  주제 : String 클래스가 제공하는 메소드들
 * ----------------------------------------------------------------------------
 *  아래 메소드들은 모두 공통점이 하나 있다.
 *  원래 문자열을 바꾸지 않고, 처리 결과를 담은 새 문자열을 만들어 반환한다.
 *  String 은 불변 클래스이기 때문이다.
 *
 *  따라서 결과를 사용하려면 반드시 반환값을 변수에 저장해야 한다.
 *
 *      str.trim();          // 결과를 버리는 코드. 아무 효과가 없다.
 *      str = str.trim();    // 올바른 사용
 *
 *  [ 인덱스(index) 규칙 ]
 *      문자열의 각 문자에는 위치 번호가 붙어 있다. 이 번호를 인덱스라고 한다.
 *      인덱스는 0 부터 시작한다.
 *
 *          J  A  V  A  _  S  t  u  d  y
 *          0  1  2  3  4  5  6  7  8  9
 *
 *      마지막 문자의 인덱스는 (문자 개수 - 1) 이다.
 * ========================================================================== */
public class Ex2 {
	public static void main(String[] args) {
		
		String str5 = "JAVA_Study";
		//			   0123456789  <- 인덱스
		
        // ================================================================
		//1. length()  :  전체 문자열의 총 문자 갯수 반환한다.
        // ================================================================
		System.out.println("length() = " + str5.length()); // 10
		
        // ================================================================
		//2. charAt(인덱스) :  전달한 인덱스 위치의 문자 하나를 반환한다.  반환 타입은 char 다.
        // ================================================================
		System.out.println("charAt(0) = " +  str5.charAt(0)); // J
		System.out.println("charAt(9) = " +  str5.charAt(9)); // y
		
		//없는 인덱스를 매개변로 넣으면 실행 중 예외가 발생한다.
		//str5.charAt(10);
		//   -> StringIndexOutOfBoundsException
		
		System.out.println("--------------------------------------------");
		
        // ================================================================
		//3. equals(Object obj) :  String 객체 메모리에 보관된 문자열 값이 같은지 비교 한다.
        // ================================================================
		String str1 = new String("Coffee");
		String str2 = new String("House");
		
		//두 String 객체 메모리에 저장된 문자열 값이 같으냐?
		if(str1.equals(str2)) {
			System.out.println("두 문자열이 같다.");  // 실행되지 않는다.
		}else {
			System.out.println("두 문자열이 다르다."); // 실행된다.
		}
		
		String str3 = new String("House");
		
		//str2 참조변수의 String객체 메모리 내부의 "House"와 
		//str3 참조변수의 String객체 메모리 내부의 "House"가  같으냐?
		if(str2.equals(str3)) {
			System.out.println("str2와 str3이 참조 하고 있는 String객체 메모리 안의 문자열은 같다.");  //실행된다
		}
		
		// ! 논리 부정연산자 는 결과를 반대로 뒤집는다. !true 는 false가 되고  !false는  true 가 된다
		String str6 = "저장할문자열1";
		String str7 = "저장할문자열2";
		
		//두 String 객체 메모리 안의 문자열이 다르냐? 라고 물어 봅시다
		if(!str6.equals(str7)) {
			System.out.println("str6과 str7이 사용하고 있는 String객체 메모리 안의 문자열은 다르다."); //실행된다
		}
		
		//리터럴 문자열에 직접 메소드를 호출해도 된다. 리터럴 문자열 자체가! String객체를 표현 하기 떄문이다.
		System.out.println( "A".equals("B")  );  //false
		
		//equals 메소드는  대문자와 소문자를 구분해서 같은지 비교 한다.
		System.out.println( "JAVA".equals("java")  );  //false
		
		//equalsIgnoreCase 메소드는 대문자와 소문자를 구분하지 않고 같은지 비교 한다.
		System.out.println( "JAVA".equalsIgnoreCase("java")  );  //true
		
		System.out.println("--------------------------------------------------------");
        // ================================================================
        /*4. subString() :  전체 문자열의 일부를 잘라서  새 문자열로 만들어 반환한다.
         *  substring 은 오버로딩되어 있다. 오버로딩이란 이름은 같고
         *  매개변수의 개수나 타입이 다른 메소드를 여러 개 만들어 두는 것을 말한다.
         *
         *      String substring(int begin, int end)
         *          begin 위치의 문자부터 end 바로 앞 위치의 문자까지 잘라낸다.
         *          end 위치의 문자는 포함되지 않는다.
         *          잘라낸 문자 개수는 (end - begin) 이 된다.
         *
         *      String substring(int begin)
         *          begin 위치의 문자부터 마지막 문자까지 잘라낸다.
         */	
        // ================================================================
		String a = new String("AndroidJSPJAVA");
		//					   0123456789		<- 인덱스
		
		String temp1, temp2;
		
		// 인덱스 7위치의 문자부터  9인덱스 위치의 문자까지 잘라서 하나의 문자열로 얻자
		temp2 = a.substring(7, 10);
		System.out.println("substring(7, 10) = " + temp2); //"JSP"
		
		// 엔덱스 7위치의 문자부터 끝까지 잘라서 하나의 문자열로 얻자
		temp1 = a.substring(7);
		System.out.println("substring(7) = " + temp1); //"JSPJAVA"
		
		// a 참조변수의 String 객체 메모리 안의 전체 문자열은 변경되지 않는다.
		System.out.println("원본 a = " +  a); //"AndroidJSPJAVA"
		
		System.out.println("-----------------------------------------------------");
		
        // ================================================================
		//5. trim() :  전체 문자열에서 앞뒤 공백만 제거한 새 문자열을 만들어 반환한다.
        // ================================================================
		String  b = new String("     JA   VA    ");
		String temp3 = b.trim();
		
		System.out.println("trim() 결과 : [" + temp3 + "]" ); //[JA   VA]
		
		//strip() : trim() 과 비슷하지만 여러 나라의 공백 문자까지 처리한다.(Java 11 이상)
		System.out.println("strip() 결과 : [" + b.strip() + "]"); //[JA   VA]
		
	
		 // ================================================================
         // 6. concat() : 두 문자열을 이어 붙인 새 문자열을 반환한다.
         // ================================================================
        String c = "JAVA";
        String d = new String("PG");
		
        String temp4 = c.concat(d);
        System.out.println("concat() 결과 : " + temp4);   // "JAVAPG"
        
        // + 연산자로 이어 붙이는 것과 결과는 같다.
        System.out.println("+ 연산자 결과  : " + (c + d));   // "JAVAPG"

        System.out.println("------------------------------------------------");
        
        // ================================================================
        // 7. contains() : 특정 문자열이 포함되어 있는지 검사한다.
        // ================================================================
        // 포함되어 있으면 true, 없으면 false 를 반환한다.
        
        String e = new String("필요없는문자열해당문자열필요없는문자열");
        
        boolean result = e.contains("해당문자열");
        System.out.println("\"해당문자열\" 포함여부 = " + result); //true
        
        result = e.contains("문자열");
        System.out.println("\"문자열\" 포함여부 = " + result); //true
        
        result = e.contains("JAVA");
        System.out.println("\"JAVA\" 포함여부 = " + result); //false
  
        // ================================================================
        // 8. replace() : 찾은 부분을 다른 것으로 바꾼 새 문자열을 반환한다.
        // ================================================================
        /*
         *  replace 도 오버로딩되어 있다.
         *      replace(char 옛문자, char 새문자)          문자 하나 단위로 바꾼다.
         *      replace(CharSequence 옛문자열, CharSequence 새문자열)  문자열 단위로 바꾼다.
         *
         *  CharSequence 는 String 의 부모 인터페이스다.
         *  그래서 String 을 그대로 전달할 수 있다.
         *
         *  찾은 부분이 여러 개면 전부 바뀐다.
         */
        String f = new String("JAVAJSPC");

        // 작은따옴표는 문자 하나(char), 큰따옴표는 문자열(String) 이다.
        String result2 = f.replace('C', '!');
        System.out.println("문자 단위 replace   => " + result2);   // JAVAJSP!

        result2 = f.replace("JSPC", "PROGRAMMING");
        System.out.println("문자열 단위 replace => " + result2);   // JAVAPROGRAMMING

        // 빈 문자열로 바꾸면 해당 부분이 제거되는 효과가 된다.
        System.out.println("A 제거              => " + f.replace("A", ""));   // JVJSPC

        System.out.println("---------------------------------------------------");   
        
        // ================================================================
        // 9. toLowerCase() / toUpperCase() : 소문자, 대문자로 바꾼다.
        // ================================================================

        String lowerStr = "Hello World".toLowerCase();
        System.out.println("toLowerCase() = " + lowerStr);              // hello world
        System.out.println("toUpperCase() = " + "hello world".toUpperCase());   // HELLO WORLD

        System.out.println("------------------------------------------");       
        
        
        // ================================================================
        // 10. String.valueOf() : 다른 타입의 값을 문자열로 바꾼다.
        // ================================================================
        /*
         *  이 메소드는 static 메소드다. 객체를 만들지 않고
         *  클래스 이름으로 바로 호출한다.  ->  String.valueOf(값)
         *  앞에서 본 length(), charAt() 등은 객체를 통해 호출하는 인스턴스 메소드였다.
         */
        
        //=====================>  특정 값 을 문자열로 변환 
        String newStr = String.valueOf(10);
        System.out.println("valueOf(10) = " + newStr); //"10"
        
        //문자열 "10"이 되었는지 확인한다. length() 메소드 호출해보자
        //                         숫자 10이라면 length() 메소드 호출 불가능 할것이다.
        System.out.println("newStr.length() = " + newStr.length()); //2
        
        //3.14 실수를 문자열 "3.14"로 변경해서 얻고 싶다
        System.out.println(String.valueOf(3.14));  //"3.14"
        
        //true 블린을 문자열 "true"로 변경해서 얻고 싶다.
        System.out.println(String.valueOf(true));  //"true"
        
        //=====================> 문자열을 정수 숫자로 변환
        int num = Integer.parseInt("10");
        System.out.println(num + 5); // 15
        
        
	}

}












