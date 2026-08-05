package Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*

A.java (소스 파일) --- 컴파일 ---> A.class( 실행 파일 )


[리플렉션(Reflection) 기본 개념]

- 리플렉션이란?
 .class 파일 실행 중(Runtime 중)에  class 특정클래스명{ } 의 구조 정보를 분석하고 사용할 수 있는 기능이다.

- 일반적인 경우
 → 우리는 소스 코드(.java 파일의 코드)를 직접 보고 생성자, 변수, 메소드를 눈으로 확인한다.

- 리플렉션을 사용하면
 → 소스 코드(.java 파일의 코드)를 열지 않아도
    class가 가지고 있는 정보(설계도)를
    자바 코드(.java 파일의 코드)로 직접 조회할 수 있다.

- 스프링(Spring), JPA, MyBatis 같은 프레임워크는
 내부적으로 리플렉션을 사용하여
 객체를 생성하고, 값을 주입하고, 메소드를 실행한다.
 
 
- class Class {...} : 타입(클래스,인터페이스)가 가지고 있는 멤버(생성자,변수,메소드)정보 얻기
	
	메소드														용도
	Constructor[]  getDeclaredConstructors()              생성자 정보 읽기
	Field[]		   getDeclaredFields()					  변수(필드) 정보 읽기
	Method[]       getDeclaredMethods()					  메소드 정보 읽기
 
*/

//=============================================
// 리플렉션으로 분석할 대상 특정 클래스 만들기
//============================================
class Car2 {
	
	//---------변수(필드)-------------
	private String model;  //자동차 모델명을 저장할 변수(필드)
	private String owner;  //자동차 소유자 명을 저장할 변수(필드)
	
	//---------생성자(Constructor)------
	public Car2() { }     //기본 생성자
	public Car2(String model) { this.model = model; }  //model 인스턴스변수 초기화할 생성자 
	
	//---------메소드(Method)---------
	public String getModel() {  return this.model;  } //model 인스턴스변수 값 외부 클래스로 반환할 getter 메소드 	
	public void setModel(String model) {  this.model = model; } // model 인스턴스 변수값 변경할 setter 메소드 	
	public String getOwner() {  return this.owner;   } //owner 인스턴스변수 값 외부 클래스로 반환할 getter 메소드 	
	public void setOwner(String owner) {  this.owner = owner; } // owner 인스턴스 변수값 변경할 setter 메소드 
	
}

//==================================================
//  리플렉션 기법을 실행할 클래스 
//=================================================
public class ReflectionExample {
	
	/*
	[매개변수 타입 출력 메소드]	
	- 생성자 또는 메소드가 어떤 매개변수를 가지고 있는지 출력하기 위한 보조 메소드 
	- Class 라는 이름의 new Class()객체 들이 저장된 배열을 전달 받아
	  배열에 저장된 new Class()객체가 어떤 클래스로 만들어져 있는지 ~~~ 클래스명을 출력한다.
   */
	private static void printParameters(Class[] parameters) { // [       , class java.lang.String] 배열 
												  //생성자일 경우 		
													 //[] 빈 배열 첫번쨰로 받음 
													 //[class java.lang.String] 배열 두번쨰로 받음
							
												  //메소드일 경우
												     //[] 빈 배열 첫번째로 받음 
													 //[class java.lang.String] Class객체 배열 두번쨰로 받음 
			
		
		//매개변수 Class[] parameters 로 전달 받은 배열에 저장된 new Class(); 객체 갯수 만큼 반복
		for(int i=0;  i<parameters.length;   i++) {
			
			//매개변수 이름(자료형 이름)이 작성된 생성자의 클래스경로 전체 출력
			//첫번쨰 반복)  
			//두번쨰 반복)  java.lang.String
			System.out.print(parameters[i].getName());
			
			//생성자의 마지막 매개변수가 아닐 경우  매개변수 사이를 구분하기 위한 , 콤마 출력
			if(i < parameters.length - 1) {
				System.out.print(",");
			}
			
		} // for
				
	}//-------------printParameters메소드 
	

	public static void main(String[] args) {
	/*
	   class Class{...} 의 객체 란?
	   
	   - Class 객체는 하나의 클래스 설계도 정보를 담아 제공 해주는 객체 이다.
	   
	   - 분석할 대상 특정 클래스 하나당 ~~  Class 객체는 단 하나만 만들어진다.
	   
	   - 이 Class 객체 내부에는  분석할 대상 특정클래스 안에 만들어 놓은 생성자,변수,메소드 이런 메타정보가 모두 저장되어 있다. 
	
	*/		
	//클래스 이름만 가지고  Class 객체를 얻는 방법
		//방법1. Class claszz = 클래스이름.class;
		//설명 : class Car2{....} 분석할 대상 클래스의 정보를 가진 new Class()객체 얻기 
		        Class claszz = Car2.class;
       
		//===============================================================
		//  class Car2 { ...... } 에 만들어 놓은 생성자 정보(메타 정보) 얻어 출력
		//===============================================================
		System.out.println("[생성자 정보]");
		
		//Class 객체의 getDeclaredConstructors()메소드를 호출하면!!
		//class Car2 {....} 에 만들어 놓은 생성자 2쌍을 각각 한쌍씩! Constructor 객체 에 저장후 
		//Constructor 객체 2쌍을 Constructor 배열에 담아 반환해 줍니다.
		Constructor[]  constructors = claszz.getDeclaredConstructors();
		
									  //[public 리플렉션.Car2(), public 리플렉션.Car2(java.lang.String)] 배열
		for(Constructor constructor  : constructors ) {
			//class Car2 {......}에 만들어져 있는 생성자 갯수 만큼 반복
			
			//생성자가 만들어져 있는 패키지경로를 포함한 클래스명 전체 출력 -> getName() 메소드 호출 -> 리플렉션.Car2
			System.out.print( constructor.getName() + "(" );
								       //리플렉션.Car2(          <- 첫번째 반복
									   //리플렉션.Car2(		   <- 두번째 반복
			//해당 생성자의 매개변수 타입 목록 조회
			Class[] parameters = constructor.getParameterTypes();
			//				   = [       ,class java.lang.String] 배열 
			
			//생성자의 매개변수 이름 목록 출력하기 위해 위에 만들어 놓은 메소드 호출!
			printParameters(parameters);
			
			System.out.println(")");
			//   )  출력   <- 첫번쨰 반복 
			//   )  출력   <- 두번쨰 반복 
			
		}// for
		
		System.out.println();  // 한줄 줄바꿈 출력
		
		//========================================================
		// class Car2{...} -> Car2.class의  변수(필드) 정보 얻어 출력
		//=========================================================
		System.out.println("[변수(필드) 정보]");
		
		//class Car2{...} 설계도 클래스에 만들어 놓은 모든 변수(필드) 정보 하나하나를 Field 객체에 담고,
		//Field 객체 2쌍을 Field[] 배열에 담아 반환 받습니다.
		//요약 : Car2 클래스 내부에 만들어 놓은 모든 변수(필드) 정보 얻기 
		Field[] fields = claszz.getDeclaredFields();
		
		for(Field field  :  fields  ) {
			
			//변수 자료형 타입   +  변수 이름     출력
			System.out.println( field.getType().getName()  +  "  " + field.getName() );
			/*
			[변수(필드) 정보]
			java.lang.String  model
			java.lang.String  owner
			*/
		}
		
		System.out.println(); //한줄 줄바꿈 출력
		
		//===========================================================
		// class Car2{ ....  }  ->  Car2.class 의  메소드 정보 얻어 출력
		//===========================================================
		System.out.println("[메소드 정보]");
		
		// class Car2{ .... } 에 만들어 놓은 모든 메소드들을 하나하나씩 각각 Method객체에 정보를 담은 후 
		// Method 배열에 최종 저장후 반환 받습니다.
		Method[]  methods = claszz.getDeclaredMethods();
		
	//Method 배열 메모리 모습 	
		//[public java.lang.String 리플렉션.Car2.getOwner(),     <- 0 index   Method객체
		// public void 리플렉션.Car2.setOwner(java.lang.String), <- 1 index   Method객체
		// public java.lang.String 리플렉션.Car2.getModel(),     <- 2 index   Method객체
		// public void 리플렉션.Car2.setModel(java.lang.String)] <- 3 index   Method객체		
		
		for(Method method  :  methods) {
			
			//만들어 놓은 메소드 이름 출력
			System.out.print(method.getName() + "(");
			
			//만들어 놓은 메소드의 매개변수 자료형 갯수만큼 저장된 new Class() 객체의 배열 얻기
			Class[]  parameters  = method.getParameterTypes();
			 		// getOwner 첫번째 메소드는 매개변수를 작성 하지 않은 메소드 이므로 -> [ ] Class 객체 배열 받음 
					// setModel(String model) 두번쨰 메소드는 매개변수가 작성한 메소드 이므로 -> [class java.lang.String] Class객체 배열 받음
			
			//위에 만들어 놓은 printParameters 메소드의 매개변수로 Class[] 배열을 전달해서
			//메소드의 매개변수 자료형 출력하기 위해 메소드를 호출합니다.
			printParameters(parameters);
			
			
			//class Car2{} 에 만들어 놓은 메소드 매개변수 자리 ) 출력
			System.out.println(")");
			
		}

	}

}

