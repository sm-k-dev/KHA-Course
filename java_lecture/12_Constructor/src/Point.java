import java.awt.TrayIcon.MessageType;

public class Point { // x좌표, y좌표  점 설계도 

	//[1] 객체 변수 선언
	//x , y 좌표값을 각각 저장시킬 int 객체변수 2개 선언
	int x, y;
	
	//-----------------------------------------------------
	
	//[2] 기본생성자 선언
	public Point() {	
		// x객체 변수 값 2로 초기화,   
		x = 2;
		// y객체 변수 값 2로 초기화 
		y = 2;
		//"[기본 생성자] Point 기본생성자 호출됨"    <----- 기본생성자가 호출됨을 확인할 용도로 출력
		System.out.println("[기본 생성자] Point 기본생성자 호출됨");
	}

	//[3] 위 X객체 변수값만 초기화 하는 생성자 선언
	public Point(int x_) {
		x = x_;
	}
		
	//[4] 위 x객체 변수, y객체 변수값 모두 초기화 하는 생성자 선언 
	public Point(int x_, int y_) {   //<----- new Point(300, 400);
		x = x_;
		y = y_;
	}
	
	
	//---------------------------------------------------
	//[5] 메소드 선언
	//1. 위 y객체 변수값 매개변수로 받아 변경시키는 setY메소드 만들기 
	public void setY(int y_) {
		y = y_;
	}
	
	//2. 위 y객체 변수값을 외부로 반환하는 getY메소드 만들기 
	public int getY() {
		return y;
	}
	
	//3. 위 x객체 변수값 매개변수로 받아 변경시키는 setX메소드 만들기 
	public void setX(int x_) {
		x = x_;
	}
	
	//4. 위 X객체 변수값을 외부로 반환하는 getX메소드 만들기  
	public int getX() {
		return x;
	}
	
	//5. 현재 x객체 변수값 그리고 y객체 변수값 을 얻어  "출력 x, y : 100, 200" 문자열로 합쳐 출력하는 기능의  printCoordinates메소드 만들기
	public void printCoordinates() {
		System.out.println("출력 x, y : "+ x + ", "+ y);
	}
	
	public static void main(String[] args) {
	
		//1. 기본생성자를 이용하여 Point클래스의 객체 생성
		Point  point1 = new Point();
		/*						  0x12
		 * 	   [0x12 ]= -------------------------------
		 * 					int x;   [2 => 10]
		 * 					int y;   [2 => 20]
		 * 			
		 * 				------------------------------
		 */
		//1.2. point1 참조변수에 저장된 객체메모리의 객체 변수 x의 값 얻어와 출력 
		//System.out.println("객체변수 x = " +  point1.x );      //2
		  System.out.println("객체변수 x = " +  point1.getX());  //2
		
		//1.2. point1 참조변수에 저장된 객체메모리의 객체 변수 y의 값 얻어와 출력
		//System.out.println("객체변수 y = " + point1.y );       //2
		  System.out.println("객체변수 y = " + point1.getY());   //2
		
		//1.3. point1 참조변수에 저장된 객체메모리의 전체 좌표 정보 출력
		  point1.printCoordinates();
		  
		//1.4. point1 참조변수에 저장된 객체메모리에 포함된 x객체 변수값  2 -> 10으로 변경  
		  point1.setX(10);
		 
		//1.5. point1 참조변수에 저장된 객체메모리에 포함된 y객체 변수값 2-> 20으로 변경
		  point1.setY(20);
		  
		//1.6. point1 참조변수에 저장된 객체메모리의 전체 좌표 정보 출력
		  point1.printCoordinates();  

		 System.out.println("==================================================");
		 
		 //2.  x좌표값만 초기화 하는 생성자를 사용해 Point클래스에 대한 다른 객체 생성
		 Point  point2 = new Point(300);
			/*						  0x13
			 * 	   [0x13]= -------------------------------
			 * 					int x;   [300]
			 * 					int y;   [0]
			 * 			
			 * 				------------------------------
			 */		 
		 //2.1. point2 참조변수에 저장된 객체 메모리의 전체 좌표 정보 출력
		   point2.printCoordinates();
		 
		 System.out.println("=================================================");
		 
		 //3. x, y 좌표값을 모두 초기화 하는 생성자를 사용해 Point클래스에 대한 다른 객체 생성
		 Point  point3 = new Point(300, 400);
			/*						  0x14
			 * 	   [0x14]= -------------------------------
			 * 					int x;   [300]
			 * 					int y;   [400]
			 * 			
			 * 				------------------------------
			 */		 		 
		 //3.1. point3 참조변수에 저장된 객체 메모리의 전체 좌표 정보 출력
		 point3.printCoordinates(); 
		 
		 //다른 출력 방법
		 //System.out.println("출력 x, y : " + point3.getX() + ", " + point3.getY());
	}
}