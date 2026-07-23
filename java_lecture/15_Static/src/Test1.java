
/*
	연습.  클래스 내부에 멤버변수(객체변수, 클래스변수) 만들기 

Student 클래스 만들기
	
		정수 하나를 저장할 객체변수 num 만들기
		
		정수 하나를 저장할 클래스변수 staticNum 만들기
		
		기본생성자 만들기 
			아무런일도 하지 않도록 작성 X
		
		객체 메소드 만들기
			메소드명 : add
			매개변수 : 정수하나 전달받을 매개변수
			기능 :   매개변수로 전달받은 정수하나를 객체변수num과 클래스변수staticNum에 각각 누적		
*/
class Student {	
			int num;       //객체 변수 num
	static  int staticNum; //클래스 변수 staticNum
	
	public Student() { }  //기본생성자 
		
	public void add(int value) { //객체메소드 
		
				 this.num += value; //객체 변수 num에  매개변수 value의 값을 누적
		Student.staticNum += value; //클래스 변수 staticNum에 메게변수 value의 값을 누적
	}	
}
public class Test1 {
	public static void main(String[] args) {
		
		//1. class Student 를 이용해 객체를 생성하는데.. 기본생성자 호출해서 생성
		Student a      = new Student();
		
		//2.  a 객체 메모리에 포함된 add메소드 호출할때 매개변수 value로 5를 전달해 
		//	  객체변수 num과  클래스변수 staticNum 에 각각 누적 시키자 
		a.add(5);
		
		//3. a 객체 메모리에 포함된 int num 객체변수값 얻어 출력
		//   JVM메모리의 Method area 영역에 올라가 있는 static int staticNum 클래스변수값 얻어 출력
		System.out.println("------ a 객체 --------");
		System.out.println("객체변수 num = " +  a.num  );        //참조변수명.객체변수명
		System.out.println("클래스변수 staticNum = " +  Student.staticNum     );  
															   //참조변수명.클래스변수명
															   //or
															   //클래스명.클래스변수명
/*		
		------ a 객체 --------
		객체변수 num = 5
		클래스변수 staticNum = 5	

											0x16
			  [ox16]   = ----------------------------------------------
							int num; [ 0 => 5 ]  <--- 객체 변수 
							
							public void add(int value){  <-- 객체 메소드
								
								//객체변수
								this.num += value;  
								
								//클래스변수
								Student.staticNum += value;
							}
						 ---------------------------------------------
*/
		
		//1.1.  class Student 를 이용해 객체를 생성하는데.. 기본생성자 호출해서 생성 
		Student   b  = new  Student();
		
		//2.2. b객체 메모리에 포함된 add메소드 호출할때 매개변수 value로 5를 전달해 
		//	  객체변수 num과  클래스변수 staticNum 에 각각 누적 시키자 
		b.add(5);
		
		//3. b 객체 메모리에 포함된 int num 객체변수값 얻어 출력
		//   JVM메모리의 Method area 영역에 올라가 있는 static int staticNum 클래스변수값 얻어 출력
		System.out.println("------ b 객체 --------");
		System.out.println("객체변수 num = " +  b.num  );        //참조변수명.객체변수명
		System.out.println("클래스변수 staticNum = " +  Student.staticNum     );  
/*															   //참조변수명.클래스변수명   or  클래스명.클래스변수명
		------ b 객체 --------
		객체변수 num = 5
		클래스변수 staticNum = 10													   
															  
		
											0x17
			  [ox17]   = ----------------------------------------------
							int num; [ 0 => 5 ]  <--- 객체 변수 
							
							public void add(int value){  <-- 객체 메소드
								
								//객체변수
								this.num += value;  
								
								//클래스변수
								Student.staticNum += value;
							}
						 ---------------------------------------------	
	
		
		결론  :   객체 변수와  클래스 변수 차이점 적어보기 
		
				-> 객체 변수는  객체 메모리를 생성할때 마다  객체 메모리내부에 개별적으로 생성되는 메모리 공간
				
				-> 클래스변수는  생성된 각 객체 메모리들이 공용으로 사용하는 공용변수메모리이기때문에
				   자바프로그램이 시작되면 가장 처음  class Student가  JVM의 Methoed area 영역에 올라갈때 
				   class Student에 포함되어 같이 올라가는 변수 메모리 공간 		
*/		
		
	}

}







