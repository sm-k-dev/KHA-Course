

//주제 : 부모클래스 A와 자식클래스 B가 다른 패키지에 각각 만들어져 있는 경우 
//		접근제어자의 접근 허용 범위가 어떻게 되는지 알아보자.


//부모클래스 A는 PA패키지 경로에 만들어져 있다~ 라고 컴파일러에게 알려 주는 패키지선언문
package PA;

public class A {  //A 부모클래스 
	
	//1. 접근제어자 종류에 따른 변수 선언
				int i;   // default 접근제어자 : 같은 패키지에 만들어 놓은 모든 클래스에서만! 변수에 접근 가능
    protected   int pro; // protected 접근제어자 : 같은 패키지의 일반, 자식클래스 또는 다른 패키지 자식클래스에서만 변수에 접근 가능
    private     int pri; // private 접근제어자  :  같은 클래스 내부에서만 변수에 접근가능
    public      int pub; // public 접근제어자  :  어디서든지 변수에 접근가능 (모든 클래스에서 접근 허용) 
	
	
    //2. A 클래스 내부에서 객체변수 값을 얻어 출력하는 기능의 public 메소드 선언
    public void print() {
    	
    	System.out.print("i = " + this.i + ", pro = " + this.pro + ", pri = " + this.pri  + ", pub = " + this.pub);
    	
    }
}








