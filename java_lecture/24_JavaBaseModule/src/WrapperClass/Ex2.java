package WrapperClass;

public class Ex2 {

	public static void main(String[] args) {
/*	
	박싱 (수동 박싱) ?
	-> 기본자료형의 데이터를 래퍼클래스의 객체 메모리 내부의 인스턴스변수에 저장시키는 작업.
	
	
	박싱 (수동 박싱) 방법 2가지
	->  방법1. 해당 기본자료형 데이터에 대응되는 래퍼클래스의 생성자를 호출해 객체의 인스턴스변수에 저장.
		예)  new Integer(10);
	
	->  방법2. 해당 기본자료형 데이터에 대응되는 레퍼클래스 내부에 만들어진 메소드를 사용해서 저장
		예)  Integer.valuOf(10); -> valueOf메소드는 new Integer(10); 객체를 생성해서 반환해줌.

*/
		int  n = 10;   //<- 기본자료형 int 정수값 10
		
	//수동 박싱 작업 하자
		
		//해석 : 10 을 Integer 클래스의 객체 메모리에 저장하기 위해 
		//      Integer 클래스의 생성자를 호출해 객체 메모리 생성후 인스턴스변수에 10을 저장 (수동 박싱)
		Integer value = new Integer(10); //방법1.
		
		//해석 : 10 을 Integer 클래스의 객체 메모리에 저장하기 위해
		//		Integer 클래스의 valueOf()메소드를 호출할때 매개변수로 10을 전달하면
		//		10이 저장된 새로운 Integer 객체 메모리를 만들어서 저장 후 Integer 객체 메모리 주소 자체를 반환하게 됩니다.
		Integer num = Integer.valueOf(n); //방법2.

/*
	언 박싱(수동 언박싱)?
	- 레퍼 클래스의 객체 메모리 내부에 저장된 기본자료형 데이터를 반대로 꺼내어 얻는 작업.
	
	언 박싱(수동 언박싱) 하는 작성 방법
	- 해당 래퍼 객체 메모리 내부에 만들어진 인스턴스메소드를 호출해서 반환 데이터로 기본자료형의 데이터를 다시 꺼내어 얻습니다.
	
		예)  new Integer(10);  <- 10 수동박싱 된 상태에서!!!
		
			new Integer(10).intValue(); 메소드를 호출해서 기본자료형 10 데이터를 다시 얻습니다.  <- 수동 언박싱
		
*/		
		//박싱(수동 박싱) 작업 먼저 하자 - Integer객체 메모리 내부의 인스턴스변수에 10 저장
		Integer  integer = new Integer(10);
		
		//언박싱(수동 언박싱) 나중에 하자 - Integer객체 메모리 내부의 인스턴변수에 저장된 10을 다시 꺼내어 얻자
		int result	= integer.intValue(); // -> 다시 꺼내온 10을 반환 받을 수 있다.
		System.out.println(result); //언박싱 작업한 10 출력
		
		
	}

}