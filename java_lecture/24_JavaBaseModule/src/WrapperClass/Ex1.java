package WrapperClass;
/*
자바의 자료형의 종류 2가지 
	1. 기본자료형 : byte, char, short, int, long, float, double, boolean
	2. 참조자료형 : 참조변수를 만들기 위한 클래스자료형, 인터페이스자료형
	
포장클래스란(래퍼클래스란)?	
  - 기본자료형의 데이터를 객체메모리의 변수에 저장시킬떄 사용되는 클래스들
  - 기본자료형  8 개에 대응되는 래퍼클래스들을 제공 해줍니다.
  
    기본자료형					래퍼클래스
    byte						Byte
    char						Character
    short						Short
    int							Integer
    long						Long
    float						Float
    double						Double
    boolean						Boolean


  기본자료형의 데이터를 객체메모리의 변수에 저장시키는 이유?

	예)  showData(Object   obj) 메소드를 반드시 사용해야 할 상황인 경우
	
	 
	 	public static void showData(Object    obj){
	 	
	 		System.out.println(obj.toString());
	 	}
	 	
	  위 showData메소드를 통해서 출력해야하는 데이터가 정수3과 정수7이다.
	  이러한 상황에서 정수3과 정수7이 Object클래스를 상속하는 자식객체형태가 되어야만..
	  showData(Object obj) 메소드 호출시 전달인자로 전달될수 있습니다.
	  객체의 주소값을 전달받는 변수자리에 기본자료형 데이터를 전달해야할 경우
	  기본자료형의 데이터를 포장클래스(래퍼클래스)의 객체 메모리를 생성해서 변수에 저장한 후 
	  포장클래스(래퍼클래스)의 객체 자체의 주소를 매개변수 obj로 전달 해야합니다.
*/

//자바 문법에서 제공해주는 Integer 라는 이름의 래퍼 클래스를 직접 흉내내어 만들어보자.
class IntWrapper  extends Object {
	
	private int num;  //기본 자료형의 정수 데이터를 저장시킬 변수 
					  // 3 또는 7을 저장할 수 있음
	
	//래퍼 Integer클래스 역할을 하는 IntWrapper클래스의 객체 생성시 호출되는 생성자
	//역할 : 위 private int num; 인스턴스 변수값 초기화
	public IntWrapper(int num) {
		this.num = num;
	}
	
	//Object 부모클래스에 만들어져 있는 toString() 메소드 오버라이딩
	@Override
	public String toString() {
		//기능 재구현 : num 인스턴스변수에 저장된 기본자료형값 3을 가져와 하나의 문자열로 만들어 반환
		return "" + this.num;  //"3"
	}
		
}
public class Ex1 {
	
	//정수 3 또는 7을 매개변수로 전달 받아 문자열 형태로 출력해주는 기능의 클래스 메소드 만들기
	public static void showData(Object obj) { //<---- new IntWrapper(3);
											  //<---- new IntWrapper(7);
		
		//Object obj 매개변수로 전달받은 자식객체가 new IntWrapper(3); 일경우 
		//-> obj.toString()메소드를 호출하면 "3" 반환받아 출력
		
		//Object obj 매개변수로 전달받은 자식객체가 new IntWrapper(7); 일경우 
		//-> obj.toString()메소드를 호출하면 "7" 반환받아 출력
		System.out.println(obj.toString());
	}
	
	public static void main(String[] args) {
		
		//1. 래퍼 Integer 클래스를 흉내내어 만든 IntWrapper클래스의 객체 생성시 생성자로 3을 전달해 저장시킨다.
		IntWrapper intWrapper = new IntWrapper(3);
		
		//2. showData 메소드 호출할때 Object부모의 자식 래퍼객체인 new IntWrapper(3); 객체 주소를 매개변수로 전달
		Ex1.showData(intWrapper);
			
		//1.2. IntWrapper클래스의 객체 생성시 생성자로 7을 전달해 저장시킨후 showData메소드 호출시 매개변수로 전달
		Ex1.showData(new IntWrapper(7));
	}

}











