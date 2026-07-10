// [예제] 매개변수로 전달받은 하나의 문자열을 화면에 출력한 후 한줄 줄바꿈 하는 기능의 메서드 만들기
public class Example2 {
	
	// 사용자 정의 메소드 printMessage 만들기
	// 기능: 매개변수 message로 전달받은 문자열을 화면에 출력후 한줄 줄바꿈 하는 기능
	public static void printMessage (String message) {
		System.out.println(message);
	}
	
	public static void main(String[] args) {
		printMessage("안녕하세요");
		printMessage("집에 가고싶다.");
	}

}
