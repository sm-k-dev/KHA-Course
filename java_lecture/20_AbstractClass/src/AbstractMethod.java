/*

	추상메소드?
	-  자식클래스들이 가지고 있는 공통 메소드를 뽑아내어
	   추상클래스로 작성할때, 메소드 선언부만 동일하고 구현부를 작성해 놓지 않는 메소드.

	- 추상메소드 작성 방법
	  접근제어자 abstract 반환자료형 추상메소드명(매개변수,.....);

	- 일반메소드와 차이점은 abstract 키워드가 붙고, 메소드의 실행코드가 작성되는 구현부 중괄호{}가 없다.
	
		예)   
			  public abstract class Animal {     // <= 추상클래스 (미완성 설계도)
			  
			  		//추상메소드 선언
			  		abstract  void  sound();
			  
			  }
*/
//추상클래스 만들기
abstract class Animal { //동물 미완성 설계도
	
	//상수, 변수,  일반메소드,  추상메소드 등을 만들어 놓을수 있다.
	
	//1. 일반 메소드  : 메소드의 선언부와 구현부가 모두 작성된 메소드
	public void breatch() {				   // <= 메소드의 선언부 영역
		System.out.println("숨을 쉽니다.");  // <= 메소드의 구현부 영역
	}
	
	//2. 추상 메소드 : 메소드의 선언부만 작성된 메소드명으로 메소드의 기능을 보여주는 메소드
	//기능 : 동물이 소리를 내는 기능
	public abstract  void  sound();       //  <= 메소드의 선언부 영역 만 작성 
	
}

//Animal 추상클래스를 상속받아 새로운 자식 클래스 Dog만들기
class Dog  extends Animal  {
	/*
	 * 추상클래스 Animal을 상속받아 자식 Dog 클래스를 만들때
	 * 무조건! 추상메소드를 강제로 메소드 오버라이딩 해서 만들어야 한다.
	 * 이유는! 추상클래스 Animal에는 아직 기능 구현코드가 완성되지 않은 추상메소드 sound(); 가 존재하기 떄문에 
	 * 강제로 메소드 오버라이딩 해서 Dog클래스를 완성시켜야 한다
	 */
	@Override
	public void sound() {
		System.out.println("멍멍");
	}		
}
//Animal 추상클래스를 상속받아 새로운 자식 클래스 Cat만들기
class Cat extends Animal {

	//메소드 오버라이딩 
	@Override
	public void sound() {  			//<= 메소드 선언부 
		System.out.println("야옹");  //<= 메소드 구현부 
	}
		
}

public class AbstractMethod {
	
	//클래스 메소드 만들기 - 매개변수는 업캐스팅이 일어나게  부모추상클래스자료형  매개변수명  작성 
	public static void animalSound(Animal  animal) {
									//    new Dog();  또는  new Cat(); 둘중 하나를 업캐스팅에 의해 받음
		
		animal.sound(); //매개변수 animal로 전달받은 자식객체의 종류에 따라
						//자식 객체 내부에 메소드오버라이딩된 sound메소드의 기능이 실행!
	}
	
	public static void main(String[] args) {
		
		//클래스 메소드 animalSound을 호출해서 사용하기 위해
		//방법1. 클래스명.클래스메소드명(인자);
		//방법2.        클래스메소드명(인자);   
		//둘중 하나의 방법을 이용하자
		
				AbstractMethod.animalSound(new Dog());
							   animalSound(new Cat());
	}

}
