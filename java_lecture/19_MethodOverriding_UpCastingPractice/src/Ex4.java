
//주제 : 부모클래스의 변수를 상속받아  사용하는 예

class Point2D { //부모클래스 
	protected int x = 10;
	protected int y = 20;
}
class Point3D extends Point2D { //자식 클래스	
	//부모클래스의 변수명과 똑같은 변수명으로 자식클래스에 변수를 만들어 놓으면
	//부모클래스의 변수들은 자식클래스에 상속되지 않고, 은닉 보호되어 사용할수 없게 된다.
	protected int x = 40;
	protected int y = 50;
	protected int z = 30;
	
	//메소드기능 : x, y, z 변수에 저장된 값 각각 불러와 하나의 문자열로 출력
	//참고. 부모클래스의 변수명과 자식클래스의 변수명이 같을때 아래와 같이 다르게 출력하자
	public void print() {
		//					   10              20             30 
		System.out.println(super.x  + "," + super.y + "," + this.z);
		
		//					  40            50             30
		//System.out.println( this.x + "," + this.y + "," + this.z);
	}	
}
public class Ex4 {
	public static void main(String[] args) {
		new Point3D().print();
		//================================
		//부모 메모리 영역
		// int  x = 10;   y = 20;    
		//=================================
		//자식 메모리 영역
		// int x = 40;   y = 50;   z = 30;
		//
		// print(){  System.out.println( this.x + ","  +  this.y + "," + this.z);  }
		//================================
		/*
		결론
		메소드 오버라이딩시~~~
		상속되지 않고 은닉된 부모클래스의 메소드와 같이
		부모클래스에 정의된 변수와 같은이름을 가진 변수를 자식클래스에서 선언하는데
		이러한 경우에도 부모클래스의 변수는 자식클래스에서 상속받을 수 없게 된다.
		*/		
		
		
		

	}

}


