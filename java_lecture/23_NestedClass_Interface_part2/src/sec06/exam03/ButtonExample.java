package sec06.exam03;

// (코드 흐름: 버튼 설계 => 클릭 이벤트 규칙 만들기 => 버튼 객체 생성 => 클릭 이벤트처리 => 실행)

// 버튼 설계도(클래스) 만들기
class Button { // 외부 바깥 클래스 역할
	
	// 정적 중첩 인터페이스 만들기
	// 만드는 이유 - Button 클래스 전용 리스너라는 것을 명확히 묶어두기 위해 내부에 만듦!
	//			public static이라서 외부에서 Button 객체를 굳이 'new' 하지 않고도 'Button.ClickListener'로 바로 가져다 쓸 수 있음!
	public static interface ClickListener {
		
		void onClick(); // 클릭하는 동작을 등록 시키는 메소드
	}
	
	// 외부 바깥 Button 클래스의 인스턴스변수 만들기
	// 	참고. ClickListener 부모인터페이스 내부에 만들어 놓은 규칙(추상메소드명)을 따르는 자식객체를 저장할 공간
	private ClickListener	clickListener;	// new OKListener(); 자식객체 저장
											// new CancelListener(); 자식객체 저장
	
	// 외부 바깥 Button 클래스의 인스턴스메소드를 setter로 만들기
	public void setClicklistener (ClickListener clickListener) {
												// new OKListener(); 자식객체 저장
												// new CancelListener(); 자식객체 저장
		this.clickListener = clickListener;
	}
	
	// 외부 바깥 Button 클래스의 인스턴스메소드 click 만들기
	//	기능: Button객체가 click 이벤트가 발생 했을 때 click 이벤트를 처리할 기능
	public void click() {
		this.clickListener.onClick();
	}
}

public class ButtonExample { // 외부 바깥 클래스 역할
	
	public static void main(String[] args) { // 외부 바깥 클래스 내부의 정적 메소드 역할
		
		// Button 클래스의 객체 생성
		Button btnOK = new Button(); // <button>OK</button> 버튼 역할
		
		/*
			위 Button 객체에 click 이벤트가 발생했을 때, click 이벤트를 처리할 코드가 작성되는 OKListener 자식클래스를
				ButtonExample 로컬 중첩 클래스로 만들기
			
			만드는 방법 ==========================
				Button 클래스 내부에 만들어 놓은 중첩 인터페이스 ClickListener 내부의 추상메소드 강제로 오버라이딩 해서 만든다.
		*/
		
		class OKListener implements Button.ClickListener {

			@Override
			public void onClick() {
				// click 이벤트 처리할 코드 작성
				System.out.println("Button btnOK = new Button(); 버튼 클릭");
			}
			
		}
		
		// 위 Button btnOK 
		btnOK.setClicklistener( new OKListener() );
		btnOK.click();
	}

}
