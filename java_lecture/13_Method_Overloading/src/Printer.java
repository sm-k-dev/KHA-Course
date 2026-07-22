/*
	✅ 메소드 오버로딩이란?
	
		같은 이름을 가진 메소드를 매개변수의 자료형이나 개수에 따라 여러 개 정의하는 것입니다.

	✅ 메소드 오버로딩 조건
	
	1. 매개변수의 갯수를 달리해서 동일한 이름의 메소드 여러개 작성가능
	
		void display(){}
		
		void display(int a){}
	
		void display(int a, int b){}

	2. 매개변수의 타입(자료형) 종류를 달리해서 동일한 이름의 메소드 여러개 작성가능
	
		void display(int a)(){}
		
		void display(double a){}

		void display(String a){}

    3. 매개변수의 갯수와 타입이 동일하지만  매개변수 작성 순서만 다른 경우에도 메소드 오버로딩 가능
    	
    	void display(int a, String b){}
    
    	void display(String a, int b){}


	주의 : 매개변수의 갯수와 타입이 동일하지만 순서만 다른 경우에만 오버로딩이 가능함
	      매개변수가 모두 동일한 경우(개수, 타입, 순서 모두 동일)는 오버로딩 불가능합니다
	      
	참고.  반환타입(자료형)만 다르고 매개변수가 동일한 메소드들은 오버로딩이 불가능함
		
		  public int getValue(int a){
		  		return a;
		  }
		  
		  public double getValue(int a){
		  		return (double)a;
		  }	  
*/
public class Printer {

/*
 개발자가  하나이상의 생성자를 만들어 놓지 않으면?  자바 컴파일러는 기본생성자 하나를 자동 생성해 놓는다.
 	
	public Printer() {  }	
*/	
	
	//주제 : 메소드 오버로딩 예 
	
	//메소드오버로딩?  같은 클래스 내부에 동일한 이름의 메소드를 여러개 정의하는것을 말함
	
	/*	
		메소드명 : print
	    매개변수 :  문자열 하나를 전달받을 매개변수
	    기능    :  문자열 하나를 매개변수로 받아 출력
	*/
	public void print(String value) {
		System.out.println("문자열 출력 : " + value);
	}
	
	/*	
	메소드명 : print
    매개변수 :  정수 숫자 하나를 전달받을 매개변수
    기능    :  정수 숫자 하나를 매개변수로 받아 출력
	*/
	public void print(int value) {
		System.out.println("정수 숫자 출력 : " + value);
	}
		
	/*	
	메소드명 : print
    매개변수 :  실수 숫자 하나를 전달받을 매개변수
    기능    :  실수 숫자 하나를 매개변수로 받아 출력
	*/
	public void print(double value) {
		System.out.println("실수 숫자 출력 : " + value);
	}	
	
	/*
	메소드명  : print
	매개변수  :  문자열 하나와 정수 숫자 하나를 전달받을 매개변수
	기능     :  문자열 과 숫자를 매개변수로 받아 출력
	*/
	public void print(String value1, int value2) {
		System.out.println("문자열과 정수 출력 : " + value1 + ", " + value2);
	}
	
	public static void main(String[] args) {
		
		//Printer 클래스의 객체 메모리 생성
		Printer  out = new Printer();
		
				 out.print(100); //정수 숫자 출력 : 100
				 
				 out.print(99.99); //실수 숫자 출력 : 99.99
				 
				 out.print("문자열", 85); //문자열과 정수 출력 : 문자열, 85

	}

}
