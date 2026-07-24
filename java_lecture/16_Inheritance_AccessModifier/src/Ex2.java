
/*
 주제 : 상속 기능을 사용하지 않고, Point2D, Point3D클래스 설계시
       각 클래스내부의 코드가 중복되는 예를 상속으로 해결하자 
*/

//============================================================
//  [Before] 상속을 하지 않은 경우  -  두 클래스에 같은 코드가 중복 작성됨
//===========================================================

//2차원 평면의 좌푯값을 저장하는  Point2D_Before클래스 설계
class Point2D_Before{
	
	private int x;
	private int y;
		
	//get단어로 시작되는 메소드들을 -> getter라고 부르고
	//set단어로 시작되는 메소드들을 -> seeter라고 부릅니다.
	
	public int getX() {  return this.x;   }    //getter 메소드  : 변수값을 반환 하는메소드
	public int getY() {  return this.y;   }	   //getter 메소드  
	
	public void setX(int x) {  this.x = x; }   //setter 메소드  : 변수값을 변경 하는 메소드 
	public void setY(int y) {  this.y = y; }   //setter 메소드  
	
}
// 3차원 공간의 좌표값을 저장하는 클래스 
// x, y 변수  +  getter/ setter 메소드가  Point2D_Before 클래스와 완전히 똑같이 "중복" 작성됨
class Point3D_Before{
	
	private int x;    // <- Point2D_Before클래스의 x와 중복코드 	
	private int y;	  // <- Point2D_Before클래스의 y와 중복코드
	
	private int z;    // <- Point3D_Before클래스에 새롭게 정의한 변수 중복 되지 않음 

	/*  getter / setter 역할메소드들  자동으로 만들기 단축키   alt  + shift  + s    r    */
	public int getX() { return  this.x; }     // 중복코드
	public void setX(int x) { this.x = x; }   // 중복코드	
	public int getY() { return y; }           // 중복코드
	public void setY(int y) { this.y = y; }   // 중복코드
	
	public int getZ() { return z; }
	public void setZ(int z) { this.z = z; }	
}

//===================================================================================
// [After] 상속을 사용한 경우  - Point3D 클래스가  Point2D클래스를 상속받아  중복 코드 제거후 만들었다.
//======================================================================================
// 상속 문법 :  class 자식클래스명  extends  부모클래스명 {    }
//=========================================================================

class Point2D{
	
	private int x;
	private int y;
		
	//메소드 이름이 get단어로 시작하는 getter역할을 하는 메소드 와 
	//메소드 이름이 set단어로 시작하는 setter역할을 하는 메소드 모두 만들기 
	//getter/ setter 메소드들 만들기 단축 키 ->  alt + shift + s  r
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}	
}

//3차원 공간의 좌표값을 저장하는 클래스 (자식클래스)
//Point2D의 x, y, getX(), setX(), getY(), setY()를 상속받아 그대로 사용하고
//z 변수 관련 코드만 새로 추가하면 되므로 중복코드작성이 없어진다.

//상속 문법 :  class 자식클래스명  extends  부모클래스명 {    }
class Point3D  extends  Point2D {
	
	private int z;

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
		
}

public class Ex2 {
	public static void main(String[] args) {

		System.out.println("------- Before (상속 미사용) --------");
		Point3D_Before  before = new Point3D_Before();
		before.setX(10);
		before.setY(20);
		before.setZ(30);
		System.out.println(before.getX() + "," +  before.getY() + "," + before.getZ());
		
		System.out.println("-------- After (상속 사용) ----------");
		Point3D      point3d  =  new Point3D();
		
		point3d.setX(10); // 상속받은 부모(Point2D) 소유의 메소드로, 부모 소유의 x변수에 저장
		point3d.setY(20); // 상속받은 부모(Point2D) 소유의 메소드로, 부모 소유의 y변수에 저장 
		point3d.setZ(30); // Point3D 자신의 메소드로, 자신의 z변수에 저장 
		System.out.println(point3d.getX() + "," + point3d.getY() + "," + point3d.getZ());	

/*												 0x12
			=================================================================
							Point2D 부모 클래스의 객체 메모리 
			
			상속받은 부모의 객체변수(x, y) + 객체메소드(getX(), setX(), getY(), setY())


			------------------------------------------------------------------
							Point3D 자식 클래스의 객체 메모리 

			자식의 객체변수(z) + 객체메소드 (getZ(), setZ()) 
			

			====================================================================
*/		
		
		
	}

}
















