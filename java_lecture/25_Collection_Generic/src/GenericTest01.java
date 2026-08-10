

//주제 : 정해진 자료형(데이터)만 처리하는 클래스 

class TestClass {
	
	//인스턴스 변수 만들기
	private int member;
	
	//setter, getter 메소드 만들기
	//alt + shift  +  s   r
	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}	
}

public class GenericTest01 {
	public static void main(String[] args) {
	
		TestClass  obj01 = new TestClass();
		
		obj01.setMember(3);  //-> 인스턴스변수 private int member = 3;  저장됨
		
		System.out.println("인스턴스변수 member에 저장된 값을 얻어 출력 : " + obj01.getMember()); //3
		
		//obj01.setMember(3.4); //-> 실제 메소드 호출시 전달 되는 값 자체가 3.4 실수 자료형이면 에러 발생
		
		//obj01.setMember("이해 할수 있다?");
		
		System.out.println("인스턴스변수 member에 저장된 값을 얻어 출력 : " + obj01.getMember()); //3
		
		/*
		결론 : TestClass는 정수 데이터만 처리하는 클래스로 선언되었기 떄문에
		      실수형이나 문자열형은 처리할수 없다.
		      만일 실수형이나 문자열형 처리하도록하려면
		      setMember메소드를 오버로딩해서 2개더 만들어야 합니다.
		      하지만 Object 부모클래스 자료형의 매개변수로 클래스를 설계하면??
		      어떤 자료형도 매개변수로 받을수 있는 클래스가 됩니다.
	    */
	}

}








