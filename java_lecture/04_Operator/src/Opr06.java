
public class Opr06 {

	public static void main(String[] args) {
		char ch = 'b';	// 변수 ch에 소문자 'b' (숫자로는 98) 저장
		
		String s;		// 결과를 담을 문자열 변수
		
		boolean result = ch >= 'A' && ch <= 'Z';
		//			     98 >= 65  && 98 <= 90;
		
		s = result ? "대문자" : "소문자";
		
		System.out.println(ch + " 는 " + s);
	}

}
