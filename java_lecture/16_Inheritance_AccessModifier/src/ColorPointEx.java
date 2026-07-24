

//주제 : (x, y)의 한 점을 표현하는 Point 클래스와 이를 상속받아 점에 색을 추가한 ColorPoint
//     클래스를 만들고 활용해보자.


//(x, y)의 한 점을 표현하는 Point 클래스  <==== 부모클래스 역할
class Point{
	private int x, y;  //한점을 구성하는 x, y 좌표
	
	public void set(int x, int y) { //x , y 객체변수값 을 매개변수 x,y 로 변경하는 메소드 
		this.x = x;
		this.y = y;
	}	
	public void showPoint() {
		System.out.println("(" + x + "," + y + ")");
	}
}

//Point 클래스와 이를 상속받아 점에 색을 추가한 새로운 자식 ColorPoint클래스를 만들고 활용해보자.
class ColorPoint extends Point{
	
	private String color; //점의 색
	
	//setter
	public void setColor(String color) {
		this.color = color;
	}
	
	public void showColorPoint() { //컬러 점의 좌표 출력
		System.out.print(color);
		showPoint(); //상속받은 부모 Point클래스의 showPoint()메소드 호출
	}
	
}

public class ColorPointEx {
	public static void main(String[] args) {
		
		Point p = new Point(); //Point 객체 생성
				/*	
					JVM의 Heap 영역
					┌───────────────────────────┐
					│ Point 객체                 │
					│                           │
					│  int x = 0                │
					│  int y = 0                │
					└───────────────────────────┘
			     */		
			 p.set(1, 2);
				 /*	
					JVM의 Heap 영역
					┌───────────────────────────┐
					│ Point 객체                 │
					│                           │
					│  int x = 1                │
					│  int y = 2                │
					└───────────────────────────┘
			     */		
			 p.showPoint(); // (1,2)
			 
			 ColorPoint  cp = new ColorPoint();  //자식 ColorPoint클래스의 객체 메모리 생성
				/*		
								JVM의 Heap 영역
								┌──────────────────────────────────────┐
								│ ColorPoint 객체                       │
								│                                      │
								│  ┌───────── 부모(Point)메모리 영역  ──┐  │
								│  │ int x = 0                      │  │
								│  │ int y = 0                      │  │
								│  └────────────────────────────────┘  │
								│                                      │
								│  ┌──────── 자식(ColorPoint)메모리 영역 ─┐│
								│  │ String color = null              ││
								│  └──────────────────────────────────┘│
								└──────────────────────────────────────┘
			 	*/	
			 			cp.set(3, 4);
				/*		
								JVM의 Heap 영역
								┌──────────────────────────────────────┐
								│ ColorPoint 객체                       │
								│                                      │
								│  ┌───────── 부모(Point)메모리 영역  ──┐  │
								│  │ int x = 3                      │  │
								│  │ int y = 4                      │  │
								│  └────────────────────────────────┘  │
								│                                      │
								│  ┌──────── 자식(ColorPoint)메모리 영역 ─┐│
								│  │ String color = null              ││
								│  └──────────────────────────────────┘│
								└──────────────────────────────────────┘
				*/	  
			 			cp.setColor("red");	
				/*		
								JVM의 Heap 영역
								┌──────────────────────────────────────┐
								│ ColorPoint 객체                       │
								│                                      │
								│  ┌───────── 부모(Point)메모리 영역  ──┐  │
								│  │ int x = 3                      │  │
								│  │ int y = 4                      │  │
								│  └────────────────────────────────┘  │
								│                                      │
								│  ┌──────── 자식(ColorPoint)메모리 영역 ─┐│
								│  │ String color = "red"             ││
								│  └──────────────────────────────────┘│
								└──────────────────────────────────────┘
				*/	   		
			 			
			 			cp.showColorPoint();
			 			// Point 클래스의 set() 호출
			 			// ColorPoint의 setColor() 호출
			 			// 컬러와 좌표 출력    
			 		   	//                 red(3,4)
			 			


	}

}





