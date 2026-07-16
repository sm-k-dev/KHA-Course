
// 날짜 정보 (년 월 일) 를 저장할 설계도(class) 만들기
class MyDate {
	int year = 2016;
	int month = 1;
	int day = 5;
}

public class Ex4 {
	public static void main(String[] args) {
		// 참조자료형 역할
		// => 생성된 객체 메모리에 접근하여 사용하기 위해, 참조변수의 타입을 결정해 주는 자료형.
		
		// 클래스자료형 참조변수명;
		MyDate d; // MyDate 데이터 타입의 참조변수명 d는 new MyDate(); 객체 메모리의 주소번지만 가질 수 있다
		
		d = new MyDate();
		// 해석1. new MyDate		MyDate 클래스를 사용해 새로운 객체메모리 생성
		// 해석2. 	 MyDate();	MyDate 라는 생성자를 호출해서 새로운 객체메모리를 완성 시킨다.
		
		MyDate t;
		t = d; // t한테 d가 가지고 있는 주소를 그대로 써준다.
		
		System.out.println( "참조 자료형 종류 (클래스 자료형)의 참조 변수로 접근한 객체 메모리의 객체 변수값을 얻어 출력 ");
		System.out.println(d.year + " / " + d.month + " / " + d.day);
		System.out.println(t.year + " / " + t.month + " / " + t.day);
		
		// 참조변수 t로 생성된 위 객체 메모리에 접근해서 객체변수값을 변경
		t.year = 2007;
		t.month = 7;
		t.day = 9;
		System.out.println();
		System.out.println(d.year + " / " + d.month + " / " + d.day);
		System.out.println(t.year + " / " + t.month + " / " + t.day);
	}

}
