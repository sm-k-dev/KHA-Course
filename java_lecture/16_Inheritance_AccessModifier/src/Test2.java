


//주제 : 부모클래스와 자식클래스가 같은 패키지에 만들어져 있는 경우
//      다양한 종류의 접근제어자 작성 연습

class Super{ //부모 
	
	//public 접근제어자 이용, 4바이트 크기의 정수를 저장시킬 num1변수 선언후 10저장
	public    int num1 = 10;
	
	//protected 접근제어자 이용, 4바이트 크기의 정수를 저장시킬 num2변수 선언후 20저장
	protected int num2 = 20;
	
	//default 접근제어자 이용, 4바이트 크기의 정수를 저장시킬 num3변수 선언후 30저장
			  int num3 = 30;
			  
	//private 접근제어자 이용, 4바이트 크기의 정수를 저장시킬 num4변수 선언후 40저장
	private   int num4 = 40;
	
	//num4변수는 private접근제어자로 은닉(보호)되고 있으므로
	//외부 클래스에서는 변수명이 보이지 않기떄문에
	//public  getter역할의 num4인스턴스변수 값을 외부클래스로 반환(공유)하게 getNum4메소드 선언
	public int getNum4() {
		return this.num4;
	}
	
}
//---------------------------------------------------

//Super클래스를 부모로 정하여 상속받아 새롭게 만드는 Sub자식클래스 만들기
class Sub extends Super {
	
	//private 접근제어자 이용, 4바이트 크기의 정수하나를 저장시킬 num5변수 선언
	private int num5;
	
	//Sub클래스의 객체 메모리를 생성하면 부모와 자식메모리 영역이 다 만들어진다는 가정하에 작성
	public void print() {
		 //안에서 부모 영역 접근 
		 System.out.println(super.num1); //10
		 System.out.println(super.num2); //20
		 System.out.println(super.num3); //30
		 System.out.println(super.getNum4());//40
		 //자식 영역 접근
		 System.out.println(this.num5);//0
	}
	
}
public class Test2 {
	public static void main(String[] args) {
		//자식 클래스 Sub의 객체 메모리 생성
		Sub sub = new Sub();
		sub.print();
/*		
		Heap 영역
		┌──────────────────────────────────────────┐
		│ Sub 객체 (객체는 1개)   ox12                │
		│                                          │
		│  ┌───── 부모 Super 영역 ─────────────────┐ │
		│  │ public    int num1 = 10             │ │
		│  │ protected int num2 = 20             │ │
		│  │ default   int num3 = 30             │ │
		│  │ private   int num4 = 40             │ │
		│  │ public int getNum4()                │ │
		│  └─────────────────────────────────────┘ │
		│                                          │
		│  ┌───── 자식 Sub 영역 ───────────────────┐ 
		│  │ private int num5 = 0                │ │
		│  │ public void print() {안에서 부모 영역 + 자식 영역 접근}                
		│  └─────────────────────────────────────┘ │
		└──────────────────────────────────────────┘
*/		
		

	}

}









