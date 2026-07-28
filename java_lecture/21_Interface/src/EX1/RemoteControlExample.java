package EX1;



//인터페이스 : 추상클래스보다 더 추상화된 미완성 설계도

interface RemoteControl{
	/*
	  1. 상수
	     참고 : interface 내부에 상수메모리를 선언할때 final 키워드를 생략해서 작성하면 
	           변수로 인식하지 않고 상수메모리로 인식합니다.
	*/
	final int MAX_VOLUME = 10;
		  int MIN_VOLUME = 0;
	/*
	  2. 추상메소드
	     참고 : 추상메소드를 작성할때 public abstract 을 생략해도 추상메소드로 작성한 것입니다.
	*/	  
	public abstract void turnOn();
		            void turnOff();
		            void setVolume(int volume);
		        	/*
		        	인터페이스는 구현 클래스(자식 클래스)가 재정의하는 public 추상메소드를
		        	멤버로 가질수 있다. 
		        	추상메소드는 리터타입, 메소드명, 매개변수만 기술되고 중괄호{}를 붙이지 않는
		        	메소드를 말한다.  public abstract를 생략을 하더라도
		        	컴파일 과정에서 자동으로 붙게 된다. 
		           */	
	/*	   
	 JDK 8 이상의 버전부터  interface 내부에 추가로 작성할수 있는 멤버 
	           
		3. default 메소드
		
		 - interface 에는 완전한 실행코드를 가진 default 메소드를 선언할수 있습니다.
		   추상메소드는 선언부만 존재한다면  defalut메소드는 선언부와 구현부가 같이 존재하는 메소드 입니다.
		   
		 - default 메소드 작성방법
		 
		   		public default  반환자료형  메소드명(매개변수,...) {
		   		
		   				구현부 코드;
		   		}	                      
    */		
		 public default void  setMute(boolean mute) {
			 if(mute) {
				 System.out.println("무음 처리 합니다.");			
				 setVolume(MIN_VOLUME); //추상메소드를 호출하면서 상수의 값 얻어 사용하는 것이 가능
			 }else {
				 System.out.println("무음을 해제 합니다.");
			 }
		 }
	/*	      
		 4. 정적,클래스(static) 메소드 
		 - interface 내부에 클래스 메소드 선언 가능 함.
		 
		 - 추상메소드와 default메소드는 구현 자식객체가 필요하지만, 클래스 메소드는  인터페이스명.클래스메소드명(); 으로 호출 가능.
		   단, public을 생략하더라도 자동으로 컴파일러 과정에서 붙는 것이 일반클래스 내부에 작성된 클래스메소드와의 차이점이다.
		   
		 - 클래스 메소드 작성 방법
		 	
		 		public 또는 private  static  반환자료형  클래스메소드명(매개변수,....){
		 				
		 				구현부 코드;
		 		}
	*/	 
		 //기능 : 베터리 교환 하는 기능을 가진 클래스 메소드 선언
		 static void changeBattery() {
			 
			 System.out.println("리모콘의 건전지를 교환합니다.");
			 
			 /*참고. 클래스 메소드 내부에서는 상수를 제외한 추상메소드, 디폴트메소드, private메소드등을 호출할수 없다.*/
		 }
		            
}// ----------------- interface  RemoteControl   끝 

/*
	특정 인터페이스 내부에 만들어 놓은 추상메소드를 강제로 오버라이딩해서(반드시 구현해서)
	새로운 자식클래스를 만드는 방법 
	
			class 자식클래스명  implements  부모인터페이스명 {
			
				부모인터페이스 내부에 작성된 추상메소드들을 강제로 메소드 오버라이딩 할 코드 작성;
				
				자식클래스의 일반메소드 선언 코드 작성;
			
			}

	참고.   implements 예약어 : 부모 인터페이스 내부에 만들어 놓은 추상메소드를 자식클래스 설계시 강제로 메소드 오버라이딩 한다.
			
*/

/*
   Television 자식클래스를 새롭게 만들때... RemoteControl부모인터페이스 안에 만들어 놓은 추상메소드들을
   강제로 메소드 오버라이딩 해서 만든다.
*/
class Television  implements  RemoteControl {
	
	 private int volume;    //변수

	 //RemoteControl 부모인터페이스 내부에 만들어 놓은 추상메소드들을 하나도 빠짐없이 강제로 메소드 오버라이딩 하자
	 
	 //메소드 오버라이딩 시 참고할 점
	 //- 부모인터페이스의 추상메소드는 기본적으로 public 접근제어자를 가지기떄문에
	 //  public보다 더낮은 접근범위를 가진 접근제어자로 메소드 오버라이딩을 할수 없다.
	 @Override
	 public void turnOn() {  System.out.println("텔레비전의 전원을 켭니다.");	}

	 @Override
	 public void turnOff() { System.out.println("텔레비전의 전원을 끕니다."); }

	 @Override
	 public void setVolume(int volume) {
		//RemoteControl 인터페이스 내부에 작성된 상수메모리를 이용해 volume 매개변수 값을 제한 할수 있다. 
		 
		//조건1. 현재 매개변수 volume으로 전달받은 값이 최대 볼륨값보다 크다면?
		if(volume > RemoteControl.MAX_VOLUME ) {
			
			//최대 볼륨값을 현재 Television객체 내부의 포함된 volume 객체 변수에 저장
			this.volume = RemoteControl.MAX_VOLUME;  //10
		
		//조건2. 현재 매개변수 volume으로 전달받은 값이 최소 볼륨값보다 크다면?
		}else if(volume > RemoteControl.MIN_VOLUME) {
			
			this.volume = RemoteControl.MIN_VOLUME; // 0
			
		}else {
			this.volume = volume;
		}
		 
		System.out.println("현재 Television 객체의 볼륨 : " + this.volume);
		
	 }//------ setVolume 메소드 오버라이딩 끝
	
}//-------------- Television 자식 클래스 끝


/*
Audio 자식클래스를 새롭게 만들때... RemoteControl부모인터페이스 안에 만들어 놓은 추상메소드들을
강제로 메소드 오버라이딩 해서 만든다.
*/
class Audio  implements RemoteControl{
	
	private int volume;
	private int memoryVolume;
	
	@Override
	public void turnOn() {  System.out.println("오디오 전원을 켭니다.");   }

	@Override
	public void turnOff() { System.out.println("오디오 전원을 끕니다.");  }

	@Override
	public void setVolume(int volume) {
		
		//조건1. 현재 매개변수 volume으로 전달받은 값이 최대 볼륨값보다 크다면?
		if(volume > RemoteControl.MAX_VOLUME ) {
			
			//최대 볼륨값을 현재 Television객체 내부의 포함된 volume 객체 변수에 저장
			this.volume = RemoteControl.MAX_VOLUME;  //10
		
		//조건2. 현재 매개변수 volume으로 전달받은 값이 최소 볼륨값보다 크다면?
		}else if(volume > RemoteControl.MIN_VOLUME) {
			
			this.volume = RemoteControl.MIN_VOLUME; // 0
			
		}else {
			this.volume = volume;
		}
		 
		System.out.println("현재 Audio 객체의 볼륨 : " + this.volume);
		
	}


	
	//RemoteControl부모 인터페이스 내부에 만들어 놓은 default 메소드 오버라이딩 하자.
	//- 메소드 오버라이딩시 주의할 점은 public 접근제어자는 반드시 작성해서 사용 해야 하고
	//  default 키워드는 생략되므로 작성하지 말아야 한다.
	@Override
	public /*default*/ void setMute(boolean mute) {
		
		if(mute) {
			this.memoryVolume = this.volume;
			System.out.println("무음 처리합니다.");
		}else {
			System.out.println("무음을 해제처리합니다.");
			this.setVolume(this.memoryVolume);
		}
			
	}
	
} //  class Audio 자식 클래스 끝


public class RemoteControlExample {
	public static void main(String[] args) {
		
		//부모인터페이스자료형을 사용해 업캐스팅 가능?  가능
		
		//업캐스팅 작성 문법
			//부모인터페이스자료형   참조변수선언  = new 자식클래스의생성자();
		
			RemoteControl       rc         = new Television();
			
							    rc.turnOn(); //Television 객체 메모리 내부의 오버라딩된 메소드를 호출해서 실행가능
							    			 //"텔레비전의 전원을 켭니다."
							    
							    rc.setVolume(5);//Television 객체 메모리 내부의 오버라딩된 메소드를 호출해서 실행가능
											    //"현재 Television 객체의 볼륨 :  0"

							    rc.turnOff(); //Television 객체 메모리 내부의 오버라딩된 메소드를 호출해서 실행가능
							    			  //"텔레비전의 전원을 끕니다."
		
							    rc.setMute(true);//무음 처리합니다.
												//현재 Television 객체의 볼륨 :  0
							    
							    rc.setMute(false);//무음을 해제 합니다.
							    
							    //업캐스팅 
							    rc          =    new Audio();
							    
							    rc.turnOn();  //메소드 오버라이딩 된 자식객체의 메소드 호출 가능
							    rc.setVolume(5);
							    rc.turnOff();
							    rc.setMute(true);
							    rc.setMute(false);
							    							    
							    //RemoteControl인터페이스 내부에 만들어져 있는 클래스메소드 호출
							    RemoteControl.changeBattery();
							    
							    
							    
							    

	}

}













