


//함수형 인터페이스 예 실습 - Ex1.java

//람다식 ? 함수형 인터페이스를 구현한 익명객체의 익명메소드를 만드는 식.

//함수형 인터페이스란?  오직 하나의 추상메소드만 작성되어 있는 인터페이스로 람다식 작성시 사용됩니다.


//================ 자바에서 제공해 주는 java.util.function 패키지에 포함된 함수형 인터페이스들 불러오기 ===================

import java.util.function.Function; //Function 함수형 인터페이스 내부의 apply 추상메소드를 오버라이딩해서 익명메소드(람다식) 작성을 위해 불러온다.

import java.util.function.Predicate; //Predicate 함수형 인터페이스 내부의 test 추상메소드를 오버라이딩해서 익명메소드(람다식) 작성을 위해 불러옴 

import java.util.function.Consumer; // Consumer 함수형 인터페이스 내부의 accept 추상메소드를 오버라이딩해서 익명메소드(람다식) 작성을 위해 불러옴


public class Ex1 {

	public static void main(String[] args) {
	/*
		1. Function<T, R> 함수형 인터페이스 : 
		   - 매개변수로 값 하나를 받아서 -> 다른 값으로 바꿔서 반환 해주는 apply 추상메소드가 만들어져 있는 인터페이스
		   - T는 입력타입, R은 반환타입을 의미합니다.
		   여기서는  입력타입이 Integer 이고, 반환타입이 String 입니다.
		   inToString 람다식은 정수 값 하나를 매개변수로 받아서
		   "숫자: "라는 문자열과 i 매개변수 값을 결합하여 하나의 문자열로 반환하는 익명메소드식입니다.
				
			Function<Integer, String> intToString_ = new Function<Integer, String>() {
				
			    //부모인터페이스 Function 내부에 만들어져 있는 apply 추상메소드를 강제로 오버라이딩
				@Override
				public String apply(Integer i) {

					return "숫자: " + i;
				}			
			};
	*/			
		//예시로  5를 매개변수 i로 전달하면  "숫자:5"라는 문자열을 반환하는 람다식 작성										
		 Function<Integer, String>	intToString	 = 	(i) -> {  return  "숫자:"+i;    };
/*
		 설명
		 	 이름이 없는 익명 객체 주소번지를 intToString 참조변수에 Function 부모인터페이스의 자식익명객체로 저장되어 있다.
		 	 익명 객체 내부에는 Function 함수형부모인터페이스 내부에 apply 추상메소드를 강제로 오버라이딩한 이름이 없는 익명메소드를
		 	 람다식 (i) -> { return "숫자:"+i; }; 으로 작성하여 정의 해 놓았습니다.
			 
*/		System.out.println( intToString.apply(5)  );   //"숫자:5" 문자열을 반환받아  이 자리에 출력   

/*
		2. Predicate<T> 함수형 인터페이스 
		- 값을 매개변수로 받아서 boolean 값을 반환 하는 기능의 test 추상메소드가 작성된 함수형 인터페이스
		- T 은 매개변수로 전달받는 값의 타입을 의미하며, boolean 값을 반환 합니다.
	
		Predicate<String> isEmpty2 = new Predicate<String>(){
		
		
				//부모인터페이스 Predicate 에 작성된 추상메소드 test 를 강제로 오버라이딩 해서 작성
				//-> boolean test(String t) 추상메소드를 강제로 오버라이딩
				
				@Override
				public boolean test(String t){
					return  t.isEmpty();   //<--- 매개변수 Strint t로 전달받은 문자열객체 메모리 내부에 특정 문자열이 저장되어 있지 않느냐?
				}
		
		};

		System.out.println(  isEmpty2.test("") );
*/			
		
		
		//test 메소드의 매개변수로 전달하는 문자열이 없는 없는 경우에는 true 반환하고, 그렇지 않은면 false 반환하는 람다식으로 익명메소드 작성
		Predicate<String> isEmpty =    (String t) -> { return t.isEmpty();  };


		//메소드 오버라이딩 해서 작성 해놓은 이름이 없는 익명메소드를 test(""); 형태로 호출해서 반환받은 결과 출력
		System.out.println(  isEmpty.test("") );  // true

/*		
		3. Consumer<T> 함수형 인터페이스 : 
			-  매개변수로 값을 전달 받아 처리하지만, 값을 반환하지 않는 accept 추상메소드가 작성된 함수형 인터페이스.
			-  매개변수로 값을 하나 전달받아서 사용(처리)만 하고, 결과를 돌려주지 않는 accept 추상메소드가 작성된 함수형 인터페이스.
			-  T 는 accept 추상메소드의 매개변수로 전달 받는 클래스 타입을 의미하며,
			   accept 추상메소드는 매개변수로 전달 받는 값만 처리하고 처리된 값을 반환하지는 않습니다.	
			   
			   void accept(T t);
*/		
			/*
				Consumer<T> 함수형 인터페이스 내부에 작성된 void accept(T t);  추상메소드를 강제로 오버라이딩 시킨
				이름이 없는 자식 익명클래스를 만드는 동시에 이름이 없는 자식 익명객체 메모리 내부에 람다식을 이용해 익명메소드를 작성 한 
				자식 익명 객체를 생성해서 그 주소번지를 
				Consumer<T> 함수형 부모인터페이스 타입의 print 참조변수에 대입해서 저장.
			*/
		
//			                         매개변수 t 로 문자열을 하나 전달 받아 출력 후 줄바꿈 하는 람다식 		
			Consumer<String> print = (String t) -> { System.out.println(t);  };
//          Consumer<String> print =  String t  ->	 System.out.println(t);
//          Consumer<String> print =         t  ->   System.out.println(t);
			
			//위 익명메소드는 void accept(Strint t) 추상메소드를 강제로 오버라이딩 시킨 익명메소드 이므로
			//Consumer<String> 함수형 인터페이스 내부에 작성된 accept 추상메소드명으로 호출해서 사용해야 함.
			print.accept("Hello");   // "Hello"  출력

	}

}
