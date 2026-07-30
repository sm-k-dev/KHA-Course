package sec06.exam03;
/*
	자바 중첩 인터페이스(Nested Interface)**란 
	- 하나의 클래스 또는 인터페이스 내부에 또 다른 인터페이스를 선언하는 것을 말합니다.
	- 일반적으로 특정 클래스와 강하게 연관된 규약(규칙)을 내부에 정의할 때 사용됩니다.
	
	
	📌 중첩 인터페이스 작성 문법
		
		✔ 클래스 내부에 인터페이스 선언
	
				public class OuterClass {
	
				    // 중첩 인터페이스
				    public interface NestedInterface {
				        void doSomething();
				    }
				}
	
		✔ 인터페이스 내부에 인터페이스 선언
		
				public interface OuterInterface {
	
				    interface NestedInterface {
				        void printMessage();
				    }
				}
	
	
		📌자식 클래스에서  재구현(implements) 예시
		
			public class MyClass implements OuterClass.NestedInterface {
												
			    @Override
			    public void doSomething() {
			        System.out.println("중첩 인터페이스 구현!");
			    }
			}
	
		
		📌 중첩 인터페이스를 사용하는 이유
		
			| 목적          | 설명                             
			| -------------|--------------------------------
			| 구조적 연관성 표현| 특정 클래스/기능과 밀접한 규약을 내부에 포함     
			| 코드 가독성 향상 | "이 클래스용 인터페이스"라는 의미가 명확해짐      
			| 캡슐화        | 외부에 필요 없으면 `private`로 감춰 사용 가능
	
		
		✔ 접근 제어자 가능 여부
			클래스 내부 선언 시: public, private, protected, default 모두 가능		
			인터페이스 내부 선언 시: 기본적으로 public (생략 가능)
*/

//(코드 흐름: 버튼 설계 → 클릭 이벤트 규칙 만들기 → 버튼 객체 생성 → 클릭 이벤트처리 → 실행)


//버튼 설계도(클래스) 만들기
class Button {  //<==============  외부 바깥 클래스 역할
	
	//정적 중첩 인터페이스 만들기
	//만드는 이유 -  외부 바깥 클래스에서 접근이 불가능 하도록 막고  
	//			  public 이면서 Button객체 생성없이 사용할수 있게 하기 위해 만든다
	public static interface ClickListener {
		
		void onClick(); //클릭하는 동작(이벤트)을 등록 시키는 메소드 
	}
	
	//외부 바깥 Button클래스의 인스턴스변수 만들기
	//참고. ClickListener 부모인터페이스 내부에 만들어 놓은 규칙(추상메소드명)을 따르는 자식객체를 저장할 공간
	private ClickListener   clickListener; //<-----  new OKListener(); 자식객체 저장 
										   //<-----  new CancelListener(); 자식객체 저장
  	
	//외부 바깥 Button클래스의 인스턴스메소드를 setter로  만들기 
	public  void  setClickListener(ClickListener  clickListener) {
															//<-----  new OKListener(); 자식객체 저장 
															//<-----  new CancelListener(); 자식객체 저장
		this.clickListener = clickListener;
	}
	
	//외부 바깥 Button클래스의 인스턴스메소드 click 만들기 
	//기능 : Button객체가 click이벤트가 발생했을때  click이벤트를 처리할 기능 
	public void click() {
		this.clickListener.onClick();
		// new OKListener().onClick();
	}
}
//-------------------------- class Button  외부 바깥 클래스 끝

public class ButtonExample {  //<----- 외부 바깥 클래스 역할 
	public static void main(String[] args) { //<---- 외부 바깥 클래스 내부의 정적메소드 역할
		
		//Button 클래스의 객체 생성
		Button btnOK = new Button();  //<button>OK</button>  버튼 역할
		
		/*
		위 Button객체에 click이벤트가 발생했을때 click이벤트를 처리할 코드가 작성되는 OkListener자식클래스를 로컬 중첩 클래스로 만들기 
		만드는 방법================>>>>>
			 	 Button클래스 내부에 만들어 놓은 중첩 인터페이스 ClickListener 내부의 추상메소드 강제로 오버라이딩해서 만든다
		*/
		class OkListener implements Button.ClickListener {
			
			@Override
			    public void onClick() {
					//click이벤트 처리할 코드 작성
					System.out.println("Button btnOK = new Button(); 버튼을 클릭했으니 선물을 줄게요!");				
			    }	
		} // class OkListener 로컬 중첩 클래스 끝
		
		//위 Button btnOk = new Button(); 객체에 click이벤트처리할 OkListener로컬중첩클래스의 객체 등록
		btnOK.setClickListener( new OkListener()  );
		
		//위 Button btnOk = new Button(); 객체를 click하는 동작 강제로 하기 
		btnOK.click();
		
		//---------------------------------------------------------------------------------------------------
		
		//Cancel 역할을 하는 Button클래스의 객체 생성
		Button  btnCancel = new Button();       // <button>Cancel</button>
		
		//Button  btnCancel = new Button() 버튼 객체 에 click이벤트를 처리할 클래스를 로컬중첩 클래스로 만든다
		class CancelListener implements Button.ClickListener{			
			@Override
			public void onClick() {
				//click 이벤트 처리할 코드 작성
				System.out.println("Button  btnCancel = new Button() 취소 버튼을 클릭했으니 집으로 가겠다.");
			}		
		} 
		
	   //Cancel역할을 하는 Button btnCancel = new Button(); 객체의 setClickListener메소드 호출 시..
	   //매개변수로 new CancelListener(); 객체를 생성해서 주소번지를 전달 합니다.
	   //이유 :  Button 클래스 내부에 만들어 놓은  private ClickListener  clickListener; 인스턴스변수에 저장 해야 하기 떄문		
		btnCancel.setClickListener( new  CancelListener() );
		
//		Button btnCancel = new Button(); 객체의 click메소드 호출해서  강제로 Click이벤트 동작 발생하게 하기 
		btnCancel.click();
		
		
	} // main() 메소드 끝

}









