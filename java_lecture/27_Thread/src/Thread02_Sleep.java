/*
 * [Thread02] sleep() - 스레드 일시정지
 *
 * ■ Thread.sleep(밀리초)
 *   - 현재 실행 중인 스레드를 지정한 시간 동안 일시정지시킨다.
 *   - 1000밀리초 = 1초
 *   - 정지된 스레드는 CPU를 사용하지 않는다. (다른 스레드가 그동안 실행됨)
 *
 * ■ InterruptedException
 *   - sleep 중인 스레드는 외부에서 interrupt()로 깨울 수 있는데,
 *     그때 InterruptedException이 발생한다.
 *   - 체크 예외이므로 try-catch 또는 throws 처리가 강제된다.
 *
 * ■ JSP/Spring 연결
 *   - 외부 API 호출, DB 조회 등 시간이 걸리는 작업을 흉내 낼 때 사용한다.
 *   - 요청 처리 스레드가 sleep(대기) 상태이면 그 사용자의 응답도 멈춘다.
 *     "느린 코드 = 스레드 점유 = 서버 성능 저하"라는 감각을 여기서 익힌다.
 */

// 1초 간격으로 진행 상황을 출력하는 작업을 하는 스레드를 만들기 위한 일반 클래스
class DownloadTask implements Runnable {

	@Override
	public void run() {
		
		try {
			for ( int i = 1 ; i <= 3 ; i++ ) {
				// 현재 작업 중인 스레드 이름과 진행률을 출력한다
				System.out.println( Thread.currentThread().getName() + " 진행중... " + i + "/3" );
				
				// 현재 작업중인 스레드를 1초 (1000 밀리초) 정지(휴식) 시킨다
				Thread.sleep(1000);
			}
			// 반복이 끝나면 완료 메세지를 출력한다
			System.out.println( Thread.currentThread().getName() + " 작업 완료");
		} catch (InterruptedException e) {
			
			// sleep 메소드 실행 중에 interrupt 되면 여기로 온다
			System.out.println("작업이 중단되었습니다.");
		}
	}
	
}

public class Thread02_Sleep {

	public static void main(String[] args) {
		
		// 1. 스레드가 작업할 코드가 작성된 run() 메소드가 작성된 일반 클래스의 객체 생성
		DownloadTask task = new DownloadTask();
		
		// 2. 실제 스레드 2개를 만든다
		Thread t1 = new Thread(task, "파일다운로드-A");
		Thread t2 = new Thread(task, "파일다운로드-B");
		
		// 3. 스레드 작업 시키기
		// start(): 새 스레드를 만들고, 그 스레드가 new DownloadTask(); 내부의 run()을 실행하게 된다.
		t1.start();
		t2.start();
		
		// 4. main 스레드는 두 다운로드를 기다리지 않으므로 이 줄이 대게 가장 먼저 출력 된다.
		System.out.println("main 스레드: 다운로드하는 두 스레드를 시작시켰다.");
	}

}
