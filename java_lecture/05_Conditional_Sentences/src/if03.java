/*
 * 주제: if ~ else if ~ else 문을 사용하여
 * 		해당 문자가 영문 소문자 이거나 영문 대문자인지 검사
 * */
public class if03 {

	public static void main(String[] args) {
		char ch = 'J'; // J 아스키코드 74
		
		/*
		 * 소문자 a 97
		 * 소문자 z 122
		 * 
		 * 대문자 A 65
		 * 대문자 Z 90
		 * */
		
		if ( ch >= 97 && ch <= 122 ) {
			System.out.println("소문자 입니다.");
		} else if ( ch >= 65 && ch <= 90 ) {
			System.out.println("대문자 입니다.");
		} else {
			System.out.println("영문이 아닙니다.");
		}
	}

}
