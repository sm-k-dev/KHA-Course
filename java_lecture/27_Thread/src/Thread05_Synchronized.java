
// =========================================
// [클래스 1] synchronized 로 보호된 서비스 클래스
// =========================================
class SafeCountService {
	
	// 모든 스레드 (홈페이지에 접속하는 클라이언트)가 공유하는 변수
	int count = 0;
	
	// ---------- [형태 1] synchronized 키워드가 작성된 메소드 ----------
	
	// synchronized : ★이 한 단어가 Thread04 와의 유일한 차이★
	//				동작: 이 메소드에 들어오려면 "이 객체의 메소드 문 잠금" 을
	//				얻어야 한다. 잠금은 1개 뿐이므로
	//				한 스레드가 실행하는 동안 다른 스레드는
	//				메소드 입구에서 대기한다
	
	// 사이트 방문자 수를 1 올리는 기능의 메소드
	public synchronized void increase() {
		
		// 이 코드는 하나의 스레드가 실행하면 다른 스레드는 increase() 메소드 밖에서 대기 하게 된다.
		// 이 코드를 실행 중인 스레드가 코드 실행을 마치면 대기중인 스레드가 이 코드를 실행하게 된다.
		count++;
	}
	
	// ---------- [형태 2] synchronized 블록 ----------
	// 메소드 선언부에는 synchronized 가 "없다"는 점에 주의
	// 보호 구역을 메소드 안의 일부분으로 좁힌 형태
	public void increaseBlock() {
		
		// 이 줄 코드는 synchronized 보호 구역 밖의 코드다 => 여러 스레드가 동시에 이 줄의 코드를 실행 해도 된다.
		// threadName 은 메소드 내에 만든 지역 변수이므로, 스레드마다 JVM의 Stack에 따로 만들어진다. 따라서 공유가 일어나지 않는다. ( 그래서 보호가 필요 없다)
		String	threadName = Thread.currentThread().getName();	// 현재 작업중인 스레드 이름 저장
		
        //synchronized (this) {  
        //  synchronized : 보호 구역을 만든다는 예약어
        //  (this)       : "무엇의 잠금을 쓸 것인가" 를 지정하는 자리.
        //                 this 는 "지금 이 객체" 라는 뜻이다.
        //                 형태 1도 이 객체의 잠금을 쓰므로
        //                
		synchronized (this) {
			
			// 이 블록 안에선 한번에 한 스레드만 이 코드를 실행 한다.
			count++;
		}
		
	}
}

// ===========================================================
// [클래스 2] increase() 메소드를 반복 호출하는 작업 ( 사용자 1명의 요청을 흉내 냄)
// ===========================================================
class SafeIncreaseTask implements Runnable {
	
	// 어떤 서비스 객체를 사용할지 전달 받아 저장한다.
	SafeCountService service;
	
	// 생성자: 사용할 서비스 객체를 전달 받아 초기화
	public SafeIncreaseTask ( SafeCountService service ) {
		this.service = service;
	}
	
	@Override
	public void run() {
		// synchronized 예약어로 보호된 increase() 메소드를 1,000,000 번 호출 한다
		for ( int i = 0 ; i < 1000000 ; i++ ) {
			// synchronized 예약어로 보호된 increase() 메소드 호출.
			// service.increase();
		
			// synchronized 블록으로 보호된 	increaseBlock() 메소드 호출.
			service.increaseBlock();
		}
	}
}

public class Thread05_Synchronized {
	
	public static void main(String[] args) throws InterruptedException {
		
		// 동기화가 적용된 서비스 객체를 "딱 1개" 만든다.
		SafeCountService	service = new SafeCountService();	// <== 모든 스레드가 공유해서 사용할 count 변수가 포함되어 있음
		
		// 같은 서비스를 사용하는 run() 메소드가 작성된 작업 객체
		SafeIncreaseTask	task = new SafeIncreaseTask( service );	// <== 스레드가 할 작업의 코드 run()이 적힌 일반 클래스의 작업객체
		
		// 홈페이지에 접속하는 사람 2명 생성
		Thread t1 = new Thread (task, "사용자-A");
		Thread t2 = new Thread (task, "사용자-B");
		
		// 홈페이지에 동시에 접속 ( 사용자-A, 사용자-B 스레드 동시에 시작 시키자 )
		t1.start();	t2.start();
		
		// 사용자-A, 사용자-B 가 접속할때까지 main 스레드에게 대기 요청
		t1.join();	t2.join();
		
		// 사용자-A 100만번 접속, 사용자-B 100만번 접속 이 끝날때까지 main 스레드는 대기 하고 있다가
		//	모든 접속이 끝나면 아래의 작업을 출력
		System.out.println("기대 총 접속 수 : 2000000");
		System.out.println("실제 총 접속 수 : " + service.count );
	}
}