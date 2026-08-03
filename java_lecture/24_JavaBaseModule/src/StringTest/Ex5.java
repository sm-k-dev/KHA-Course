package StringTest;
/* ============================================================================
 *  주제 : replace() 메소드로 특정 문자를 한 번에 제거하기
 * ----------------------------------------------------------------------------
 *  Ex4 에서는 반복문으로 한 글자씩 확인해서 l 을 제거했다.
 *  같은 결과를 replace() 메소드 한 줄로 얻을 수 있다.
 *
 *      String replace(찾을문자열, 바꿀문자열)
 *
 *  바꿀 문자열 자리에 빈 문자열("")을 넣으면, 찾은 부분이 아무것도 아닌 것으로
 *  바뀌므로 결과적으로 제거된다.
 *  찾은 부분이 여러 개면 전부 바뀐다.
 *
 *  주의. 빈 문자열 "" 와 공백 문자열 " " 는 다르다.
 *       "" 는 길이가 0 인 문자열이고, " " 는 공백 문자 하나가 들어 있는
 *       길이 1 인 문자열이다.
 *
 *  주의. String 은 불변이므로 원본은 바뀌지 않는다.
 *       반환값을 변수에 저장해야 결과를 사용할 수 있다.
 * ========================================================================== */
public class Ex5 {

	public static void main(String[] args) {
		
		//원본 문자열
		String originalString = "Hello, World!";  // 변경 ->    "Heo, Word!"
		
		//제거할 대상 문자열 
		String deleteString = "l";
		
		//결과를 저장할 변수 
		String modifiedString = "";   //"Heo, Word!"
		
        /* replace 가 하는 일
         *      "Hello, World!" 안에서 "l" 을 찾아 모두 "" 로 바꾼다.
         *      찾은 위치는 인덱스 2, 3, 10 세 곳이다.
         *      결과로 "Heo, Word!" 를 담은 새 String 객체가 만들어져 반환된다. */
		modifiedString = originalString.replace(deleteString, "");
		
		System.out.println("원본 문자열 : " + originalString); 		   //"Hello, World!"
		System.out.println("modifiedString : " + modifiedString);  //"Heo, Word!"
		
		System.out.println("-----------------------------------------------");
		
		String test = "Hello, World!";
		
		test.replace("l", "");     //결과를 버리는 코드 
		System.out.println("변수에 저장하지 않는 경우 " + test); // Hello, World! (그대로)
		
		test = test.replace("l",""); //올바른 사용
		System.out.println("변수에 저장한 경우 " + test);     // Heo, Word!
		
		//여러 글자를 한번에 치환 할수 있다
		System.out.println(originalString.replace("lo", "")); // Hel, World!
		
		//전체 문자열에서 대문자 "L"을  "" 로 치환시  대문자와 소문자는 구분 하기 떄문에 아무런 영향이 없다.
		System.out.println(originalString.replace("L", "")); // Hello, World!
		
	}

}









