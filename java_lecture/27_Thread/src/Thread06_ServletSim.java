
// ==============================================================
// [잘못된 서블릿] 요청 데이터를 멤버 변수 (인스턴스 변수 또는 클래스 변수)에 저장
// ==============================================================
class BadLoginServlet {
	
	// 멤버 변수 : 모든 요청(스레드)이 공유한다. <= 사고의 원인
	String loginId;
	
	// 로그인 요청을 처리하는 메소드
	public String service ( String userId ) {
		
		// 1단계: 요청받은 아이디를 멤버 변수에 저장
		loginId = userId;
		
		// 로그인 처리 시간을 흉내 낸다. (DB 조회 등으로 5밀리초 걸린다고 가정)
		// - 이 사이에 다른 스레드가 loginId를 덮어 쓸 수 있다!
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 2단계: 멤버 변수를 읽어서 응답을 만든다.
		return loginId + "님 환영합니다.";
	}
}

// ==============================================================
// [올바른 서블릿] 지역변수 / 매개변수만 사용
// ==============================================================
class GoodLoginServlet {
	
	// 멤버 변수 없음!
	
	// 로그인 요청을 처리하는 메소드
	public String service ( String userId ) {
		
		// 매개변수 userId는 지역변수 -> 스레드마다 자기 것을 따로 가진다.
		
		
		// 로그인 처리 시간을 흉내 낸다.
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 자기 스레드의 지역변수 값을 읽으므로 절대 섞이지 않는다.
		return userId + "님 환영합니다.";
	}
}

// ==============================================================
// 로그인 요청 1건을 표현하는 작업
// ==============================================================
class LoginRequest implements Runnable {
	
	BadLoginServlet		badServlet;		// 잘못된 서블릿
	GoodLoginServlet	goodServlet;	// 올바른 서블릿
	String				userId;			// 이 로그인 요청의 사용자 아이디
	
	// 생성자: 두 서블릿과 사용자 아이디를 초기화 하는 생성자
	public LoginRequest(BadLoginServlet badServlet, GoodLoginServlet goodServlet, String userId) {
		super();
		this.badServlet = badServlet;
		this.goodServlet = goodServlet;
		this.userId = userId;
	}
	
	@Override
	public void run() {
		// 잘못된 서블릿에 로그인 요청 -> 멤버변수 LoginId의 값이 섞일 수 있다.
		String badResult = badServlet.service(userId);
		
		// 올바른 서블릿에 로그인 요청 -> 지역변수 LoginId의 값은 정확한 값을 사용할 수 있다.
		String goodResult = goodServlet.service(userId);
		
		// 로그인 요청한 아이디와 응답할 데이터를 비교해서 출력한다.
		System.out.println("[" + userId + "의 로그인 요청]" );
		System.out.println("잘못된 서블릿: " + badResult + "올바른 서블릿: " + goodResult );
	}
}

public class Thread06_ServletSim {

	public static void main(String[] args) throws InterruptedException {
		
		// 서블릿은 각각 1개 만든다.
		BadLoginServlet	bad = new BadLoginServlet();	// 잘못된 서블릿 <= 멤버변수 LoginId 존재
		GoodLoginServlet good = new GoodLoginServlet();	// 올바른 서블릿 <= 지역변수 LoginId 존재
		
		// 로그인 요청하는 사용자 3명(스레드 3개)이 동시에 로그인 요청을 보낼 수 있도록 작성
		Thread t1 = new Thread( new LoginRequest(bad, good, "홍길동") );
		Thread t2 = new Thread( new LoginRequest(bad, good, "이순신") );
		Thread t3 = new Thread( new LoginRequest(bad, good, "김유신") );
		
		// 로그인 세 요청을 동시에 시작시킨다
		t1.start();
		t2.start();
		t3.start();
		
		// 로그인 세 요청이 모두 끝날때 까지 main 스레드 기다리게 하기
		t1.join();
		t2.join();
		t3.join();
	}

}
