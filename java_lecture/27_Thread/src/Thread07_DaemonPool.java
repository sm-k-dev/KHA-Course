/*
 * [Thread07] 데몬 스레드 / 스레드 풀 - 톰캣의 실제 동작 방식
 *
 * ■ 데몬 스레드
 *   - 다른 스레드를 보조하는 배경 작업용 스레드.
 *   - 일반 스레드가 모두 종료되면 데몬 스레드는 실행 중이어도 자동 종료된다.
 *   - setDaemon(true)를 start() 전에 호출해서 지정한다.
 *   - 예) 자동 저장, 세션 만료 검사, 가비지 컬렉터(GC)
 *
 * ■ 스레드 풀 (Thread Pool)
 *   - 스레드를 미리 여러 개 만들어 두고 재사용하는 방식.
 *   - 왜 필요한가: 스레드 생성/삭제는 비용이 크다.
 *     요청마다 new Thread()를 하면 서버가 느려지고,
 *     요청이 폭주하면 스레드가 무한정 생성되어 서버가 죽는다.
 *   - 자바에서는 ExecutorService로 스레드 풀을 만든다.
 *
 * ■ 톰캣 연결 (매우 중요)
 *   - 톰캣은 시작할 때 스레드 풀을 만든다. (기본 최대 200개)
 *   - 요청이 오면 풀에서 스레드를 꺼내 배정하고, 처리가 끝나면 반납받는다.
 *   - 200개가 전부 사용 중이면 다음 요청은 대기한다.
 *     → 느린 코드(긴 DB 조회 등)가 스레드를 오래 점유하면
 *       전체 사이트가 느려지는 이유가 이것이다.
 */


//1. 데몬 스레드용 작업 :   1초 마다 자동 저장을 반복한다.

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class AutoSaveTask  implements Runnable{	
	@Override
	public void run() {
		
		//무한 반복 : 데몬 스레드는 main스레드의 작업이 끝나면 자동으로 함께 종료되어 무한반복 작업해도 괞찬다.
		while(true) {		
			try {
				//1초 대기 후  저장 메세지를 출력한다
				Thread.sleep(1000);
				System.out.println("(데몬스레드) 자동 저장 실행....");
			} catch (InterruptedException e) {
				break; //데몬스레드가 중단되반 반복 종료
			}
		} // while

	} // run()
}

//2. 스레드 풀용 작업  : 요청 1건 처리를 흉내 낸다
class HttpRequestTask  implements Runnable {
	
	int requestNo; //요청 번호

	//생성자 : 요청 번호 초기화
	public HttpRequestTask(int requestNo) {
		this.requestNo = requestNo;
	}
	
	@Override
	public void run() {
		//어떤 스레드가 이 요청을 처리하는지 출력 하기
		System.out.println("요청" + requestNo + " 처리 시작 - 담당: " 
		                   + Thread.currentThread().getName() );
		
		//요청 처리에 0.5초가 걸린다고 가정
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//처리 완료 메세지 출력 한다
		System.out.println("요청" + requestNo + " 처리 완료");	
	}
	
}

public class Thread07_DaemonPool {

	//main 스레드 역할 : 다른 일반작업스레드를 생성 하고 작업시작후 끝내는 역할
	public static void main(String[] args) throws InterruptedException {

		//--- 1 데몬 스레드 생성 ---
		//데몬스레드 만드는 방법
		//순서1. 일반작업스레드 생성
		//순서2. 일반작업스레드를 데몬스레드로 변경	
		Thread autoSave = new Thread(new AutoSaveTask(), "자동저장스레드");  //<-- 일반적인 작업스레드
		autoSave.setDaemon(true); //<--- 데몬스레드로 변경
		autoSave.start();
		
		//--- 2. 스레드 풀 생성 ---
		//스레드 2개 보관 할수 있는 스레드 풀을 만든다.
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		//요청 5건을 풀에 제출한다. (사용자 5명이 거의 동시에 접속)
		for(int i=1;  i<=5;  i++) {
			
			// execute(): 작업을 풀에 넘긴다. 빈 스레드가 있으면 즉시, 없으면 대기 후 실행
			pool.execute( new HttpRequestTask(i) );
			
		}
		//풀 종료 예약 : 제출된 작업이 다 끝나면 풀의 스레드들을 정리한다
		pool.shutdown();
		
		//데몬 스레드 동작을 관찰하기 위해 main 스레드가 3.5초 유지한다
		Thread.sleep(3500);
		System.out.println("main 스레드 종료 -> 데몬 스레드도 자동 종료 된다.");
		
        /*
         * ■ 실행 결과 예시
         *   요청1 처리 시작 - 담당: pool-1-thread-1
         *   요청2 처리 시작 - 담당: pool-1-thread-2
         *   (데몬) 자동 저장 실행...
         *   요청1 처리 완료
         *   요청2 처리 완료
         *   요청3 처리 시작 - 담당: pool-1-thread-1   ← 스레드 재사용!
         *   요청4 처리 시작 - 담당: pool-1-thread-2   ← 스레드 재사용!
         *   ...
         *   main 종료 → 데몬 스레드도 자동 종료된다
         *
         * ■ 핵심 정리
         *   1. 요청은 5건인데 스레드는 2개뿐 → 스레드가 재사용되고, 요청은 순서를 기다린다.
         *   2. 톰캣이 요청을 처리하는 방식이 정확히 이 구조다. (풀 크기만 200)
         *   3. 스레드 2개가 모두 바쁘면 요청3,4,5는 대기한다.
         *      → 한 요청의 처리가 느리면 뒤 요청들이 밀리는 이유.
         *   4. 데몬 스레드는 main(일반 스레드)이 끝나면 자동 종료된다.
         */
		
	}

}








