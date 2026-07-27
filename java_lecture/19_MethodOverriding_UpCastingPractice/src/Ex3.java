

/*

주제. 자식클래스BB 내부에서 print메소드를 오버라이딩 해 놓았을때...
     상속되지 않고 은닉(보호)된 부모클래스 AA의 print메소드를 강제로 호출해서 실행시키는 방법
      
	방법.  자식클래스BB 에 오버라이딩한 print메소드 중괄호 내부에서 super.부모print();  메소드 호출해서 사용

*/
class AA { //부모클래스 
	public void print(int num) {
		System.out.println(num);
	}
}

class BB extends AA {  //자식클래스 	
	//AA부모클래스에 만들어 놓은 print메소드 오버라이딩 해  놓자
	//alt + shift + s v
	@Override
	public void print(int num) {		
		super.print(num); //super예약어를 이용해 상속되지 않고 보호된 부모AA객체 메모리 영역에 있는 print메소드를 강제로 호출해서 사용할수 있음	
		System.out.println(num + 1); //자식 BB클래스의 print메소드 기능에 맞게 구현부 코드 재정의 
	}
	
	//BB자식클래스에 작성한 메소드
	public void bInfo() {
		System.out.println("BB자식 클래스의 bInfo메소드 이다");
	}
}
public class Ex3 {
	public static void main(String[] args) {
		BB  bb = new BB();
			bb.print(10);
		
		
	}

}








