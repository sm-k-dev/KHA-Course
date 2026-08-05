package Reflection;

/*
[메타정보란?]
- 클래스가 만들어져 있는 패키지 정보, 타입 정보, 멤버(생성자, 변수, 메소드) 정보 등을 말한다.
- 쉽게 말해 "클래스 자체에 대한 정보"이다.
  (객체에 저장된 데이터가 아니라, 그 객체를 만든 설계도(클래스)의 정보)

[리플렉션(Reflection)이란?]
- 자바는 클래스와 인터페이스의 메타정보를 Class 라는 이름의 클래스의 객체로 관리한다.
  (클래스 이름이 하필 "Class"라서 처음엔 헷갈리지만, java.lang 패키지에 실제로 존재하는 클래스이다.)
- 이 메타정보를 자바 프로그램 실행 중에 읽고 수정하는 행위를 리플렉션이라고 한다.
- 활용처: 지금 당장은 정보를 읽는 연습만 하지만,
  나중에 배울 스프링(Spring) 프레임워크가 내부에서 객체를 자동 생성해 주는 원리가 바로 이 리플렉션이다.

[중요한 사실]
- 하나의 클래스당 Class 객체는 JVM 안에 "딱 1개만" 만들어진다.
- 아래 3가지 방법은 "얻는 경로"만 다를 뿐, 전부 같은 1개의 Class 객체 주소를 반환한다.

[Class 객체를 얻는 방법 3가지]

	// 클래스 이름만 가지고 Class 객체를 얻는 방법
	방법1.  Class claszz = 클래스이름.class;
	예)    Class claszz = String.class;

	방법2.  Class claszz = Class.forName("패키지.클래스이름");
	예)    Class claszz = Class.forName("java.lang.String");
	       ※ 클래스 경로를 "문자열"로 전달하는 것이 특징 (실행 중에 문자열로 클래스를 찾음)
	       ※ 해당 경로에 클래스가 없으면 ClassNotFoundException 예외가 발생한다.

	// 이미 생성된 객체로부터 얻는 방법
	방법3.  Class claszz = 객체참조변수.getClass();
	예)    String str = "김자바";
	       Class claszz = str.getClass();

[기능 : 패키지와 타입 정보 얻기]
패키지와 타입(클래스, 인터페이스) 이름 정보는 다음 메소드로 얻을 수 있다.

 메소드                      용도
 String getPackageName()     패키지 이름 읽기
 String getSimpleName()      패키지를 제외한 타입 이름(클래스명 또는 인터페이스명)
 String getName()            패키지를 포함한 전체 타입 이름(클래스명 또는 인터페이스명)
*/


//예제. Car 클래스의 Class 객체를 얻고, 패키지와 클래스의 이름을 얻어 출력.

//메타정보를 읽어볼 대상 클래스. 멤버가 하나도 없는 빈 클래스이다.
//(멤버가 없어도 "리플렉션.Car 라는 클래스가 존재한다"는 메타정보는 존재한다.)
class Car{

}
public class GetClassExample {
	public static void main(String[] args) throws Exception   {
						// throws Exception : 방법2의 Class.forName()이 던질 수 있는
						//                    ClassNotFoundException 예외 처리를 위해 붙였다.
		
	//클래스 명을 가지고 Class 객체를 얻는 방법
			//방법1.  Class 참조변수 = 클래스명.class;
			
			//		 Class claszz = Car.class;
					 //만들어져 있는 class Car{}의 메타정보가 저장된 Class 객체 얻기
			
			//방법2. Class 참조변수 = Class.forName("패키지.클래스명");
			
			//		Class  claszz = Class.forName("리플렉션.Car");
					// 만들어져 있는 class Car{}의 메타정보가 저장된 Class 객체 얻기
	
	// 객체로부터 Class 객체를 얻는 방법
			
			//방법3. Class 참조변수  =  생성된_객체_ 참조변수명.getClass();
			
					//순서1. 먼저 class Car 의 객체를 생성한다.
					Car  car = new Car();
					
					//순서2. getClass() : 이 객체를 만들 때 사용된 클래스의 메타정보가 담긴
					//					  Class 객체의 주소를 반환하는 메소드 
					Class claszz = car.getClass();
					// 만들어져 있는 class Car{}의 메타정보가 저장된 Class 객체 얻기
					/*
					 [위 두 줄 실행 후 메모리 상태]

					  📍메소드 영역(Method Area)
					  ┌───────────────────────────────────────────────┐
					  │  Car 클래스 정보 (클래스 로딩 시 저장됨)              │
					  │  - 패키지: 리플렉션                               │
					  │  - 클래스명: Car                                │
					  │  - 멤버(생성자/변수/메소드) 정보                     │
					  └──────────────────────▲────────────────────────┘
					                         │ ③ Class 객체는 메소드 영역의
					                         │    Car 메타정보를 읽어서 알려주는 창구 역할
					  📍스택(Stack)           │        📍힙(Heap)
					  ┌────────────────┐     │     ┌──────────────────────────┐
					  │ car ───────────────▶ ① Car 객체               │
					  │ claszz ─────┐  │     │     ├──────────────────────────┤
					  └──────────────┘ │     └────── ② Class 객체 (Car 메타정보용)│
					                   └────────▶  ※ Car 클래스당 딱 1개! │
					                               └──────────────────────────┘

					 🔹 ① new Car()      : 힙에 Car 객체 생성, car에 주소 저장
					 🔹 ② car.getClass() : Car의 메타정보를 관리하는 Class 객체의 주소를 반환, claszz에 저장
					 🔹 ③ claszz로 메소드를 호출하면 메소드 영역에 있는 Car 클래스의 메타정보를 읽어온다.

					 [방법1, 2, 3의 관계]

					  Car.class ──────────────┐
					  Class.forName("리플렉션.Car") ──┼───▶ 전부 같은 "Class 객체(Car용) 1개"의 주소를 반환
					  car.getClass() ─────────┘      (얻는 경로만 다르고 결과는 동일)
					*/					
		
					
		//class Class 에 만들어져 있는 getPackgeName() 메소드 
		// ======>  class Car{} 의 메타 정보 중 "패키지 이름" 을 읽어 반환 해줍니다.
		System.out.println("Car 클래스가 만들어져 있는 패키지 경로(메타 정보) : " +  claszz.getPackageName());
		 //                  Car클래스가 만들어져 있는 패키지 경로(메타 정보) : 리플렉션
		
		//class Class 에 만들어져 있는 getSimpleName() 메소드
		//=======>  class Car{} 의 메타 정보 중 패키지 이름을 제외한 "클래스 명"만 읽어 반환 해줍니다.
		System.out.println("new Car(); 객체를 만들떄 사용된 클래스 명(메타 정보) : " + claszz.getSimpleName());
		 //                  new Car(); 객체를 만들때 사용된 클래스 명(메타 정보) : Car
		
		//class Class 에 만들어져 있는 getName() 메소드
	
		//========> class Car{} 의  메타 정보 중  "패키지명.클래스명" 을 읽어 반환해줍니다.
		System.out.println("Car 클래스가 만들어져 있는 패키지 경로를 포함한 클래스 전체 경로(메타정보) : " + claszz.getName() );		
		//      Car클래스가 만들어져 있는 패키지 경로를 포함한 클래스 전체 경로(메타 정보) : 리플렉션.Car			
					
		//getName() 메소드 호출 결과 는 ???????????????
		//(getPackageName() + "." + getSimpleName() 과 같은 결과)

	} // main 메소드 

}
