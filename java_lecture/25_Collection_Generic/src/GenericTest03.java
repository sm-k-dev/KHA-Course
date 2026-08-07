
/*
	주제 :  Object 부모클래스 자료형으로 변수나 메소드의 반환타입을 작성하면
	 	   단점 (업캐스팅, 다운캐스팅) 코드를 작성 해야 한다.
*/

class TestClass2 /* extends Object */ {
	
	private  Object member;  //<--- "apple"자식 문자열 객체를 업캐스팅으로 인해 member변수에 저장가능!
	
	public void setMember(Object  member) { //<--- "apple"자식 문자열 객체를 업캐스팅으로 인해 
											//      member매개변수로 전달 받아 사용가능!
		this.member = member;
	}
	
//  public Object <- 부모 Object obj 참조변수에 저장될 자식 "apple"문자열 객체 주소 반환 가능!	
	public Object getMember() {
		
		return this.member;  	//  return "apple";		
	}
}

public class GenericTest03 {
	public static void main(String[] args) {
	
		TestClass2 obj01 = new TestClass2();
		
				   obj01.setMember("apple");
				   
				   //getMember() 인스턴스 메소드를 호출하면
				   //부모 Object 클래스자료형의 참조변수에 저장할 자식String클래스의 "apple"문자열 객체 메모리 주소반환
		Object	obj	   = obj01.getMember();
		//     "apple" = 
		//		obj.toUpperCase();   String클래스에 만들어 놓은 메소드 호출 불가능
		//						     이유는? Object 부모클래스 내부에는 toUpperCase()메소드가 만들어져 있지 않아
		//							       Object obj; 참조변수를 이용해 toUpperCase()메소드 호출이 불가능 하기 때문 
		
		//해결 방법  : 다운캐스팅  (자식 클래스 자료형의 참조변수를 만들어서 저장)
		String temp = (String)obj;  // 다운캐스팅을 하여 "apple"문자열 객체의 주소번지를 다시 temp변수에 대입해서 자장함 으로써
								    // "apple" 문자열 객체 내부에 포함된 인스턴스메소드 toUpperCase()메소드 호출 가능하게 됨
		   
		 //다운 캐스팅 후 temp 참조변수로 toUpperCase메소드 호출 가능 -> "APPLE" 출력
		System.out.println( temp.toUpperCase() );
		
		/*				new TestClass2();  객체 메모리 모습 
		---------------------------------------------------------------------------------------------
									private Object member = "apple"; 
								
									public void setMember(Object member) {
										this.member = member;
									}
											
									public Object getMember() {
										
										return this.member;
									}	
	   -----------------------------------------------------------------------------------------------------------------


	*/		
		

	}

}











