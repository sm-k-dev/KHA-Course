
/*
 
 	1.추상클래스?  완벽히 설계되지 않은 미완성 설계도 

	2.추상클래스를 만드는 작성 문법
	
			접근제어자  abstract class 추상클래스명 {
			
				    static final int 상수명;     //상수 선언 
				    			 int 변수명;     //변수 선언 
				  				 기본생성자명(){} //생성자 선언
				  			 void 메소드명(){}   //일반메소드 선언
			  	    abstract void 추상메소드();  //추상메소드 선언
			}
			
			
			
 전화기 종류 : 스마트폰, 아이폰, 일반전화, 공중전화 
  
   추상화 (공통점 찾기) :  전화를 걸수있다.  
*/

//모든 전화기의 공통 데이터를 저장할 변수와 메소드만 뽑아내서 추상클래스 Phone으로 만들자
abstract class Phone {
		  
	String owner; //변수
	
	public Phone(String owner) { //생성자 
		this.owner = owner;  
	}
	
	//일반메소드 : 메소드의 선언부와 구현부가 모두 작성된 일반적인 메소드 
	public void turnOn() {  //<-- 메소드의 선언부
						
		System.out.println("폰 전원을 켭니다.");//<-- 메소드의 구현부
	}
	public void turnOff() {
		System.out.println("폰 전원을 끕니다.");
	}
	
	//추상메소드 : 메소드의 선언부만 작성되어 있고, 구현부가 작성되지 않는 메소드 
	public abstract void call();	
	
}

//추상클래스 Phone을 부모역할로 상속받아 새로운 자식클래스 SmartPhone 새롭게 만든다
class SmartPhone extends Phone {
	
	//생성자 - 스마트폰을 사용하는 사용자명 초기화 
	public SmartPhone(String owner) {
		super(owner);
	}
	//부모 Phone추상클래스에 만들어 놓은 추상메소드 call을 강제로 메소드 오버라이딩 해야 컴파일 에러를 피할수 있다!
	//이유 : SmartPhone 클래스 자체를 완성된 클래스로 만들기 위함
	@Override
	public void call() {
		System.out.println("스마트폰으로 전화를 건다");
	}
	
	//일반메소드 
	public void internetSearch() {
		System.out.println("인터넷 검색을 합니다");
	}
	
}

public class PhoneExample {

	public static void main(String[] args) {
		//1. 추상클래스 Phone을 이용하여 객체 메모리 생성가능? X
		//Phone phone = new Phone("철수");  X
		
		//2. 부모 추상클래스 Phone을 상속받아 만든! SmartPhone자식클래스의 객체 생성가능? O
		SmartPhone  smartPhone = new SmartPhone("홍길동");
		
					smartPhone.turnOn();
					smartPhone.internetSearch();
					smartPhone.turnOff();
				/*			 
					결론: 추상클래스 Phone 를 이용해 객체 생성 할수는 없었지만
						 자식클래스 SmartPhone 를 이용해 객체 생성이 가능하고,
						 추상클래스 Phone 으로 부터 상속 받은 turnOn()메소드와 turnOff()메소드를 호출 해서 사용할수 있다.		 
				*/
	}

}
