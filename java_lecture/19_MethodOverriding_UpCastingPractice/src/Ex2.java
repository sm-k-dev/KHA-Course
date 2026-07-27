
/*
	주제 : 메소드 오버라이딩 예제
		  부모클래스의 메소드를 자식클래스에서 재정의하는 개념을 설명하는 코드
*/
//======================== 부모 클래스 ==============================
//모든 도형의 공통 설계도 역할을 하는 부모 클래스 
class  DObject{
	
	//변수  : 접근제어자 부모클래스자료형  참조변수 선언
	public    DObject   dObject;     //<==========  DObject부모클래스를 상속받은 자식 객체 메모리 주소번지 저장 가능 
	
	//생성자 : 객체가 생성될때 자동으로 호출되는 기본생성자 선언
	public DObject() {	
		this.dObject = null; //dObject 참조변수를  null로 초기화 가능
	}

	//메소드 : 도형을 그리는 공통 기능을 표현한 메소드 
	public void draw() {
		System.out.println("도형을 그린다.");
	}
}
//======================== 자식 클래스 1 ==============================
//Line 자식클래스를 새롭게 만들때... DObject 부모클래스를 상속받아 만든다.
class Line extends DObject{
	
	//부모 DObject의 draw()메소드를 그대로 상속받아 사용하지 않고
	//자식 Line클래스의 기능에 맞게 재정의(메소드 오버라이딩) 해서 사용하자
	//요약 : 부모 DObject클래스에 만들어 놓은 draw()메소드 오버라이딩 하자 
	@Override
	public void draw() {   //<====== 부모 클래스의 메소드 선언부 그대로 작성 
		
		//선을 그리는 동작으로 변경
		System.out.println("선을 그린다.");  //<====== 부모 클래스의 메소드 구현부 재정의 
	}
	
  /*
    참고 설명:
    - Line 객체로 draw()를 호출하면 부모(DObject)의 draw()는 실행되지 않는다
    - 자식(Line)에서 오버라이딩한 draw()가  부모 메소드를 가려(은닉) 대신 실행된다
  */
	
}

//========================= 자식클래스 2 ===================================
//Circle자식클래스도 마찬가지로 DObject부모클래스를 상속받아 만든다.
class Circle extends DObject{
	
	//부모 DObject클래스의 draw()메소드 오버라이딩 하자
	@Override
	public void draw() {
		//원을 그리는 동작으로 재작성
		System.out.println("원 그리기");
	}
		
}

public class Ex2 {
	public static void main(String[] args) {
		/*
			부모클래스 => class DObject
			자식클래스들 =>  class Line  , class Circle
		*/
		//========= 1. 자식클래스의 객체 생성 ============
		Line  line = new Line();		
			  line.draw();  //  메소드 오버라이딩된  Line의 draw()메소드가 최종 실행되어 "선을 그린다" 출력됨
        /*
				        line 객체 메모리 구조 개념			
				        ------------------
				        부모 DObject의 멤버
				          - public DObject dObject
				          - public void draw()  ← 오버라이딩됨 (사용 불가)
				
				        Line 자식의 멤버
				          - @Override
				            public void draw() → "선을 그린다"
				        ------------------
       */
	   //=========== 2. 업캐스팅 기능을 사용하여 자식클래스의 객체 생성 ===============
	   //업캐스팅?  부모클래스자료형의 참조변수 하나만 만들어  하나이상의 자식객체 메모리의 주소번지 저장 
	   DObject   dObject = new Circle();
	   
	   			 dObject.draw();  //부모Dobject의 dObject참조변수로 draw()메소드를 호출 하면 
	   			 				 //최종 출력이?  "원 그리기" 된다.
					       /*
					       dObject가 참조하는 객체 메모리 구조
					
					       ------------------------------------
					       부모 DObject의 멤버
					         - public DObject dObject
					         - public void draw()  ← 오버라이딩됨
					
					       Circle 자식의 멤버
					         - @Override
					           public void draw() → "원 그리기"
					       -------------------------------------
					    */
	  /*
	             실행 과정 설명 (중요 ★):

	             1️. dObject는 부모 타입(DObject)이므로
	                컴파일 시 부모의 draw()를 호출하려고 함

	             2️. 실행 시 JVM이 실제 객체 타입을 확인
	                → Circle 객체임을 확인

	             3️. Circle 클래스에
	                부모와 동일한 이름의 draw()가 있는지 검사

	             4️. 존재하므로
	                자식(Circle)의 draw()로 동적 바인딩

	             5️. 최종적으로
	                "원 그리기"가 출력됨
	                
	                
	   동적 바인딩이란
	   👉 메소드를 실행하는 순간에
	   “어느 클래스의 메소드를 실행할지”를 결정하는 것입니다.

	   🔸 왜 “동적”이라는 말을 쓸까?
	   🔹 반대 개념: 정적 바인딩(Static Binding)

	   컴파일할 때(코드 검사 시)
	   → 실행할 메소드가 이미 정해짐

	   🔹 동적 바인딩(Dynamic Binding)
	   실행할 때(runtime)
	   → 실제 객체가 무엇인지 보고 메소드를 선택

	   👉 즉,
	   “나중에(실행 중에) 결정된다” → 동적   

	   ---------------------------------------- 
	    🧠 동적 바인딩이 일어나는 순서

	   1️. dObject.draw() 호출
	   → 부모 DObject 기준으로 draw()를 찾음

	   2️. JVM이 실제 객체가 Circle인지 확인

	   3️. Circle 클래스에
	   👉 draw()가 오버라이딩 되어 있는지 검사

	   4️. 있다면?
	   → 부모 메소드는 무시
	   → 자식(Circle)의 draw() 실행

	   ✔ 이 과정이 동적 바인딩
	    
	    */
			  
		

	}

}










