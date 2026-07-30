package Ex;

/*
[문제 4] 중첩 인터페이스 + 익명 구현 객체 - 알람 소리 바꾸기
 알람이 울릴 때의 동작을 익명 구현 객체로 등록하고, 교체까지 해 본다.

 [완성 후 실행 결과]
 따르릉! 7시입니다. 일어나세요.
 음악 재생 : 좋은 아침입니다~
*/
class Alarm { // 바깥 외부 클래스

	// [제공됨] 중첩 인터페이스 : 알람 동작 규칙
	public static interface Ringer {
		void ring();  // 알림이 울릴 때 실행할 동작 
	}

	//부모 Ringer 인터페이스 자료형  인스턴스 변수 
	private Ringer ringer;

	//setter : 알람 동작을 등록(교체) 한다.
	public void setRinger(Ringer ringer) {
		this.ringer = ringer;
	}

	//알람 울리기 : 저장된 익명 객체의 오버라이딩된 ring() 이 최종 실행되도록 하는 메소드 
	public void wakeUp() {
		// TODO 4-1.
		//  인스턴스 변수 ringer 에 저장된 객체의 ring() 을 호출하세요.
		this.ringer.ring();
	}
}

public class AlarmExample {
	public static void main(String[] args) {

		Alarm alarm = new Alarm();

		// TODO 4-2.
		//  익명 구현 객체로 "따르릉! 7시입니다. 일어나세요." 를 출력하는
		//  동작을 등록하고 wakeUp() 을 호출하세요.
		//  작성 문법 : alarm.setRinger(new Alarm.Ringer() { ... });
		
		// 익명 구현 객체 생성 문법
		// -> new 바깥클래스명.중첩인터페이스명() { 추상메소드 강제 오버라이딩 }
		alarm.setRinger(new Alarm.Ringer() {
			
			@Override
			public void ring() {
				System.out.println("따르릉! 7시입니다. 일어나세요." );
			}
		});
		
		alarm.wakeUp();
		
		// TODO 4-3.
		//  익명 구현 객체를 새로 만들어 "음악 재생 : 좋은 아침입니다~" 로
		//  동작을 교체한 뒤 다시 wakeUp() 을 호출하세요.
		
		// 다른 익명 구현 객체로 교체하면 같은 wakeUp() 호출이 다른 동작을 한다.
		alarm.setRinger(new Alarm.Ringer() {
			
			@Override
			public void ring() {
				System.out.println("음악 재생 : 좋은 아침입니다~");
			}
		});
		
		alarm.wakeUp();
				
		
	}
}






