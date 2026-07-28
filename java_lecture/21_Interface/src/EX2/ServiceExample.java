package EX2;

/*
===============================================================
 인터페이스 안에 만들 수 있는 private 메소드
===============================================================

[1] 인터페이스 멤버의 접근제어자

  인터페이스 안에 선언하는 상수, 추상메소드, default 메소드, static 메소드는
  public 을 적지 않아도 컴파일할 때 자동으로 public 이 붙는다.
  따라서 외부 클래스에서 항상 접근할 수 있다.

  반대로, 인터페이스 밖에서 접근하지 못하게 막고 싶은 메소드는
  private 을 직접 붙여서 선언한다.

[2] private 메소드를 만드는 이유

  default 메소드끼리 똑같은 코드가 반복되거나,
  static 메소드끼리 똑같은 코드가 반복될 때
  그 반복 코드를 private 메소드 하나로 모아 두고 호출하기 위해서다.

[3] 두 가지 private 메소드의 차이

  private 메소드          : default 메소드 안에서만 호출할 수 있다.
  private static 메소드    : default 메소드와 static 메소드 양쪽에서 호출할 수 있다.

  이유 : static 메소드는 객체 없이 실행되는 메소드라서
         객체가 있어야 실행되는 private 메소드를 호출할 수 없다.

[4] 이 예제가 하는 일

  Service 인터페이스에 default 메소드 2개와 static 메소드 2개를 만들고,
  각각의 중복 코드를 private 메소드와 private static 메소드로 분리해서 호출한다.
===============================================================
*/

interface Service {
	/*
	 ---------------------------------------------------------
	  인터페이스 메소드 4종 정리표

	  메소드 종류            접근제어자                    구현 클래스 상속 여부
	  default              public (생략 가능)            상속됨
	  static               public (인터페이스명으로 호출)   상속되지 않음
	  private              private (인터페이스 내부 전용)   상속되지 않음
	  private static       private (인터페이스 내부 전용)   상속되지 않음
	 ---------------------------------------------------------
	*/
	//[default 메소드] 실행부(중괄호)를 가질 수 있고, 구현 클래스에 상속된다.
	default void dafaultMethod1() {
		System.out.println("defaultMethod1 종속코드");
		defaultCommon(); //private 메소드 호출 - 가능
	}
	
	//[default 메소드 2] 위와 같은 중복코드를 private 메소드로 처리 한다.
	default void defaultMethod2() {
		System.out.println("defaultMethod2 종속코드");
		defaultCommon(); //private 메소드 호출 - 가능
		
	}
	//[ private 메소드 ]
	// default 메소드 2개의 공통 코드를 모아 두는 곳이다.
	// 인터페이스 밖에서는 이 private 메소드 호출이 불가능하다.
	private void  defaultCommon() {
		System.out.println("defaultMethod 중복코드 A");
		System.out.println("defaultMethod 중복코드 B");
	}
	
	//[static 메소드 1]
	//객체를 만들지 않고  "인터페이스명.메소드명()" 형태로 호출한다
	static  void  staticMethod1() {
		System.out.println("staticMethod1 종속코드");
		
		//defaultCommon();    //private 메소드 호출 - 불가능
		//이유 : static 메소드는 객체 없이 실행되므로 객체용 메소드를 부를수 없다.
			
	    Service.staticCommon(); //private static 메소드  호출  - 가능
	}
	//[static 메소드 2]
	static void staticMethod2() {
		
		System.out.println("staticMethod2 종속코드");
		
		//defaultCommon();    //private 메소드 호출 - 불가능
		
	  //Service.staticCommon();  //private static 메소드 호출 - 가능 
		        staticCommon();  //참고. 같은 인터페이스 안이므로 인터페이스명.  생략가능 
	}
	
	//[private static 메소드]
	//static 메소드 2개의 공통 코드를 모아 둔 곳이다.
	private static void staticCommon() {
		System.out.println("staticMethod 중복코드C");
		System.out.println("staticMethod 중복코드D");
	}
	
}
/*
 Service 인터페이스를 구현하는 자식 클래스 

   implements 를 쓰면
  - default 메소드는 자동으로 상속받는다. (필요하면 오버라이딩도 가능)
  - 추상메소드는 반드시 오버라이딩해야 한다. (이 예제에는 추상메소드가 없다)
  - private, static 메소드는 상속되지 않는다.
*/
class ServiceImpl  implements  Service{
	
	//추상메소드가 없으므로 오버라이딩 할 것이 없다. 중괄호를 비워 두어도 된다.
/*	
	//[default 메소드] 실행부(중괄호)를 가질 수 있고, 구현 클래스에 상속된다.
	default void dafaultMethod1() {
		System.out.println("defaultMethod1 종속코드");
		defaultCommon(); //private 메소드 호출 - 가능
	}
	
	//[default 메소드 2] 위와 같은 중복코드를 private 메소드로 처리 한다.
	default void defaultMethod2() {
		System.out.println("defaultMethod2 종속코드");
		defaultCommon(); //private 메소드 호출 - 가능
		
	}
*/	
}

public class ServiceExample {
	public static void main(String[] args) {
		//[업캐스팅]
		//부모 인터페이스 자료형 변수에 자식 클래스 객체의 주소를 저장한다.
		Service service = new ServiceImpl();
		/*
		 ---------------------------------------------------------
		  ServiceImpl 객체가 실제로 가지고 있는 것 (주소 0x100 가정)

		  ┌──────────────────────────────┐
		  │ ServiceImpl 객체              │
		  │                              │
		  │ defaultMethod1()             │  <- Service 의 default 메소드 (상속됨)
		  │ defaultMethod2()             │  <- Service 의 default 메소드 (상속됨)
		  │                              │
		  │ static 메소드 없음             │
		  │ private 메소드 없음            │
		  └──────────────────────────────┘
		 ---------------------------------------------------------
		*/
		//[1] default 메소드 호출
		//ServiceImpl 자식클래스가  상속받았기 떄문에 호출할 수 있다.
		service.dafaultMethod1();
		//출력
		//defaultMethod1 종속코드
		//defaultMethod 중복코드 A
		//defaultMethod 중복코드 B
		
		service.defaultMethod2();
		//출력
		//defaultMethod2 종속코드
		//defaultMethod 중복코드 A
		//defaultMethod 중복코드 B
		
		//[2] private 메소드는 호출할수 없다.
		//상속되지 않고 인터페이스 내부에서만 호출해서 사용할수 있기 때문입니다.
//		service.defaultCommon();  //컴파일 오류 
//		service.staticCommon();   //컴파일 오류
		// 실제 오류 메시지
		// error: defaultCommon() has private access in Service
		
		//[3] static 메소드는 참조변수가 아니라 인터페이스명으로 호출해서 사용 가능하다
		Service.staticMethod1();
		// 출력
		// staticMethod2 종속코드
		// staticMethod 중복코드C
		// staticMethod 중복코드D
		
		Service.staticMethod2();
		// 출력
		// staticMethod2 종속코드
		// staticMethod 중복코드C
		// staticMethod 중복코드D
		
//		service.staticMethod1();   //컴파일 오류 발생 
		// 실제 오류 메시지
		// error: illegal static interface method call
		//   the receiver expression should be replaced with the type qualifier 'Service'
		
	}

}
 








