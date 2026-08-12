
// 1초 간격으로 진행 상황을 출력하는 작업을 하는 스레드를 만들기 위한 일반 클래스
class DownloadTast implements Runnable {

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
		
	}

}
