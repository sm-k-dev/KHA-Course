/*
 * 주제: 해당 문자가 영문자 모음인지를 확인하는 예제
 * */
public class switch04 {

	public static void main(String[] args) {
		char ch = 'i'; // i 아스키코드 105
		
		switch ( ch ) {
			case 'a':
				System.out.println("해당 문자는 'a'입니다.");
				break;
				
			case 'e':
				System.out.println("해당 문자는 'e'입니다.");
				break;
			
			case 'i':
				System.out.println("해당 문자는 'i'입니다.");
				break;
			
			case 'o':
				System.out.println("해당 문자는 'o'입니다.");
				break;
			
			case 'u':
				System.out.println("해당 문자는 'u'입니다.");
				break;
			
			default: // a, e, i, o, u 모두에 해당하지 않는다면 실행
				System.out.println("해당 문자는 모음이 아닙니다.");
				break;
		}
	}

}
