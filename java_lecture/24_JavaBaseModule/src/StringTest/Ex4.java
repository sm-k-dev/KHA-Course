package StringTest;
/* ============================================================================
 *  주제 : 반복문과 substring() 으로 특정 문자를 모두 제거하기
 * ----------------------------------------------------------------------------
 *  "Hello, World!" 에서 소문자 l 을 모두 빼서 "Heo, Word!" 를 만든다.
 *
 *  처리 방법
 *      1) 문자열의 처음부터 끝까지 한 글자씩 확인한다.
 *      2) 그 글자가 제거 대상이 아니면 결과 문자열에 이어 붙인다.
 *      3) 제거 대상이면 이어 붙이지 않고 넘어간다.
 *
 *  substring(i, i+1) 은 인덱스 i 위치의 글자 하나만 잘라낸 문자열을 반환한다.
 *  charAt(i) 도 같은 위치의 글자를 가져오지만, 반환 타입이 char 라는 점이 다르다.
 *      substring(i, i+1) -> String 타입이므로 equals 로 비교한다.
 *      charAt(i)         -> char 타입이므로 == 로 비교한다.
 *
 *  참고. 이 방식은 문자열을 이어 붙일 때마다 새 String 객체가 만들어진다.
 *       String 이 불변이기 때문이다. 글자 수가 많으면 StringBuilder 를 쓰는 것이 낫다.
 * ========================================================================== */
public class Ex4 {
	
	public static void main(String[] args) {
		
		//검사할 원본 전체 문자열
		String originalString = "Hello, World!";
		//						 0123456789...    <-  인덱스 0 ~ 12
		
		//제거할 대상 문자열
		String deleteString = "l";
		
		//제거 한 결과 문자열을 누적할 변수 
		String modifiedString = "";
		
        /* 반복문
         *   i 는 0 부터 시작해서 (문자 개수 - 1) 까지 1씩 증가한다.
         *   조건이 i < length() 이므로 마지막 인덱스까지만 실행된다.
         *   i <= length() 로 쓰면 없는 위치에 접근해 예외가 발생한다. */
		for(int i=0;  i<originalString.length();  i++) {
			
			//인덱스 i 위치의 글자 하나를 잘라낸다.
			String oneChar = originalString.substring(i, i+1);
			
			//그 글자가 제거 대상 l 문자열과 다르면 modifiedString변수에 이어붙여 저장
			if( !oneChar.equals(deleteString)  ) {
				
				modifiedString += oneChar;
		       /* 누적 과정
		        *   i=0  "H"  ->  "H"
		        *   i=1  "e"  ->  "He"
		        *   i=2  "l"  -> 건너띔 
		        *   i=3  "l"  -> 건너띔
		        *   i=4  "o"  ->  "Heo"
                *   i=5  ","  ->  "Heo,"
		        *   i=6  " "  ->  "Heo, "
		        *   i=7  "W"  ->  "Heo, W"
		        *   i=8  "o"  ->  "Heo, Wo"
		        *   i=9  "r"  ->  "Heo, Wor"
		        *   i=10 "l"  ->  건너뜀
		        *   i=11 "d"  ->  "Heo, Word"
		        *   i=12 "!"  ->  "Heo, Word!" */
			}// if	
			
		}//for
		
		System.out.println("원본 전체 문자열 : " + originalString);       //"Hello, World!"
		System.out.println("modifiedString 변수 : " + modifiedString); //"Heo, Word!"
		
		System.out.println("----------------------------------------------------");
		
		//charAt() 메소드 사용 방식. 결과는 위와 같다.
		String result2 = "";
		
		//String originalString = "Hello, World!";
		//						   0123456789...    <-  인덱스 0 ~ 12
		
		for(int i=0;  i<originalString.length();  i++) {
			
			char oneChar = originalString.charAt(i);
			
			if(oneChar != 'l') {
				result2 += oneChar;
			}
		}
		System.out.println("charAt(index) 메소드 방식 : " + result2); //"Heo, Word!"
		
	

	}

}













