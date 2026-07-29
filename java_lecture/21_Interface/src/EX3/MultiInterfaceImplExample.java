package EX3;
/*
===============================================================
 MultiInterfaceImplExample 클래스 (실행 시작점)
---------------------------------------------------------------
 이 파일이 확인하려는 내용

  [1] SmartTelevision 객체 하나를 두 가지 자료형으로 참조할 수 있다.
      RemoteControl 자료형으로도 받고, Searchable 자료형으로도 받는다.

  [2] 참조변수의 자료형에 따라 호출할 수 있는 메소드가 달라진다.
      참조변수는 자기 자료형에 선언된 메소드만 호출할 수 있다.
      객체가 그 메소드를 가지고 있어도, 자료형에 없으면 호출할 수 없다.

  [3] 실제로 실행되는 코드는 SmartTelevision 에 오버라이딩한 코드다.
      이것을 동적 바인딩이라고 한다.

 [실행 결과]
 SmartTelevision의 전원을 켭니다.
 SmartTelevision의 전원을 끕니다.
 http://www.naver.com을 검색합니다.
===============================================================
*/
public class MultiInterfaceImplExample {

	public static void main(String[] args) {
		//=============================================================
		//[1] RemoteControl 부모인터페이스 자료형의 참조변수로 참조하기(업캐스팅)
		//============================================================
		// 왼쪽 자료형 : RemoteControl (부모 인터페이스)
		// 오른쪽 객체 : SmartTelevision (자식 클래스)
		RemoteControl  rc = new SmartTelevision();
		
		//RemoteControl 부모인터페이스에 선언된 메소드이므로 호출할수 있다.
		rc.turnOn();   // 출력 : "SmartTelevision의 전원을 켭니다."
		rc.turnOff();  // 출력 : "SmartTelevision의 전원을 끕니다."
		
		// search() 는 Searchable 부모인터페이스에만 선언되어 있고
		// RemoteControl부모인터페이스 에는 없으므로 rc 로는 호출할 수 없다.
//      rc.search("http://www.naver.com");
		//
		// 실제 오류 메시지
		// error: cannot find symbol
		//   symbol:   method search(String)
		//   location: variable rc of type RemoteControl
		
		//=======================================================================
		//[2] Searchable 부모 인터페이스 자료형의 참조변수로 생성된 자식객체 참조하기(업캐스팅)
		//=========================================================================
		// [1] 과 같은 SmartTelevision 클래스지만, 객체는 새로 하나 더 생성된다.
		Searchable  searchable = new SmartTelevision();
		
		//Searchable 부모인터페이스에 선언된 추상메소드이므로 호출할수 있다
		searchable.search("http://www.naver.com");
		//출력  :  http://www.naver.com을 검색합니다.
		
		// turnOn(), turnOff() 는 RemoteControl 부모인터페이스 에만 선언되어 있고
		// Searchable 부모인터페이스 에는 없으므로 searchable 참조변수로는 호출할 수 없다.
//		searchable.turnOn();
//		searchable.turnOff();
		/*
		실제 오류 메세지 
		The method turnOn() is undefined for the type Searchable
		The method turnOff() is undefined for the type Searchable
		*/
		//-------------------------------------------------------
		// [3] 세 메소드를 모두 호출하려면
		//-------------------------------------------------------
		// 방법 1 : 자식 클래스 자료형으로 참조한다.
		   SmartTelevision tv = new SmartTelevision();
		   tv.turnOn(); tv.turnOff(); tv.search("...");
		//
		// 방법 2 : 다운캐스팅한다.	   
		  Searchable sc  = (Searchable)rc;	   
		  sc.search("http://www.naver.com");
		
		/*
		 -------------------------------------------------------
		  참조변수별 호출 가능 범위

		  참조변수        자료형            호출 가능한 메소드
		  rc             RemoteControl     turnOn(), turnOff()
		  searchable     Searchable        search(String)
		  tv             SmartTelevision   turnOn(), turnOff(), search(String)
		 -------------------------------------------------------
		*/		
		
		
	}

}
