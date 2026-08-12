/*
	[스레드 연습문제] 예제 파일 1개당 문제 1개, 총 7문제

	   문제 1  <->  Thread01_Create      (생성 2가지 방법, start vs run)
	   문제 2  <->  Thread02_Sleep       (sleep과 예외 처리)
	   문제 3  <->  Thread03_Join        (join으로 결과 기다리기)
	   문제 4  <->  Thread04_SharedField (공유 변수 문제 - 서술형)
	   문제 5  <->  Thread05_Synchronized(synchronized로 해결)
	   문제 6  <->  Thread06_ServletSim  (지역변수로 해결)
	   문제 7  <->  Thread07_DaemonPool  (스레드 풀)

	각 문제의 TODO를 완성하시오. 실행용 main은 맨 아래에 작성되어 있다. (수정 금지)
*/
package Exercises;

import java.util.concurrent.ExecutorService; //문제 7에서 사용
import java.util.concurrent.Executors;       //문제 7에서 사용

//─────────────────────────────────────────────────────────────
// [문제 1 - Thread01 연계] 같은 작업을 두 가지 방식으로 만들기
//
// "스레드이름 >> 안내방송 i" 를 1부터 2까지 출력하는 스레드를
// (1) Thread 상속 방식과 (2) Runnable 구현 방식 둘 다로 완성하시오.
//
// 예상 출력 예) 방송-상속 >> 안내방송 1
//
// 추가 서술 : main에서 t.start() 대신 t.run()을 호출하면
//            출력 내용은 같더라도 무엇이 달라지는가? 주석으로 답하시오.
//            답:
//─────────────────────────────────────────────────────────────
class AnnounceThread extends Thread {

	@Override
	public void run() {
		// TODO 1-1 : 1부터 2까지 반복하며 "스레드이름 >> 안내방송 i" 출력
		//            힌트 : 상속 방식이므로 getName() 바로 사용 가능

	}
}

class AnnounceTask implements Runnable {

	@Override
	public void run() {
		// TODO 1-2 : 위와 똑같은 출력을 Runnable 방식으로 작성
		//            힌트 : Thread.currentThread().getName()

	}
}

//─────────────────────────────────────────────────────────────
// [문제 2 - Thread02 연계] sleep으로 점검 안내 만들기
//
// "서버 점검 중... n초 경과" 를 1초 간격으로 n=1부터 3까지 출력한 후
// 마지막에 "점검 완료!" 를 출력하는 작업을 완성하시오.
//
// 채점 포인트 : sleep의 예외 처리(try-catch)가 없으면 컴파일 자체가 안 된다!
//─────────────────────────────────────────────────────────────
class CheckTask implements Runnable {

	@Override
	public void run() {
		// TODO 2 : 1초(1000밀리초) 간격으로 3회 출력 후 "점검 완료!" 출력

	}
}

//─────────────────────────────────────────────────────────────
// [문제 3 - Thread03 연계] join으로 두 계산 결과 합치기
//
// RangeSum은 start부터 end까지의 합을 구하는 작업이다. (이미 완성됨)
// main의 [문제 3 실행] 부분에서 TODO 3을 완성하시오.
//
// 예상 출력 : 1~100 합 + 101~200 합 = 20100
//─────────────────────────────────────────────────────────────
class RangeSum implements Runnable {

	int start, end;   //계산 범위
	long result = 0;  //계산 결과 (스레드 종료 후 꺼내 쓴다)

	public RangeSum(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public void run() {
		//start부터 end까지 반복하며 합계를 누적한다
		for (int i = start; i <= end; i++) {
			result = result + i;
		}
	}
}

//─────────────────────────────────────────────────────────────
// [문제 4 - Thread04 연계] 왜 조회수가 깨지는가 (서술형)
//
// 아래 ViewCounter를 스레드 2개가 각각 100만 번 호출하면
// 조회수가 2,000,000이 되지 않는다. (main에서 직접 실행해 확인해 볼 것)
//
// (1) count++ 가 실제로 실행되는 3단계를 순서대로 쓰시오.
//     답: 1단계 :
//         2단계 :
//         3단계 :
//
// (2) 스레드 2개가 동시에 실행될 때 증가 1회가 사라지는 과정을
//     "스레드A가 ~ 하는 사이에 스레드B가 ~" 형태로 서술하시오.
//     답:
//
// (3) 이런 현상을 부르는 용어는?
//     답:
//─────────────────────────────────────────────────────────────
class ViewCounter {

	int count = 0; //모든 스레드가 공유하는 멤버 변수

	public void increase() {
		count++;
	}
}

//─────────────────────────────────────────────────────────────
// [문제 5 - Thread05 연계] synchronized로 조회수 지키기
//
// 문제 4의 ViewCounter와 같은 구조인 SafeViewCounter를
// 결과가 항상 2,000,000이 나오도록 완성하시오. (한 단어 추가)
//─────────────────────────────────────────────────────────────
class SafeViewCounter {

	int count = 0;

	// TODO 5 : 이 메소드를 스레드 안전하게 수정하시오
	public void increase() {
		count++;
	}
}

//문제 4, 5 실행용 작업 (수정 금지)
class CountUpTask implements Runnable {

	ViewCounter vc;         //문제 4용 (둘 중 하나만 전달됨)
	SafeViewCounter svc;    //문제 5용

	public CountUpTask(ViewCounter vc)      { this.vc = vc; }
	public CountUpTask(SafeViewCounter svc) { this.svc = svc; }

	@Override
	public void run() {
		for (int i = 0; i < 1000000; i++) {
			if (vc != null) vc.increase();
			else            svc.increase();
		}
	}
}

//─────────────────────────────────────────────────────────────
// [문제 6 - Thread06 연계] 주문 서블릿을 지역변수로 고치기
//
// 아래 OrderServlet은 멤버 변수 때문에 동시 주문 시
// 다른 사람의 상품으로 응답이 나가는 문제가 있다.
// 멤버 변수를 제거하고 지역변수(매개변수)만 쓰도록 수정하시오.
//─────────────────────────────────────────────────────────────
class OrderServlet {

	String product; //TODO 6 : 이 멤버 변수를 제거하고 아래 메소드를 수정하시오

	public String order(String productName) {
		product = productName;              //1단계 : 멤버 변수에 저장
		try { Thread.sleep(5); } catch (InterruptedException e) { }
		return product + " 주문 접수";       //2단계 : 멤버 변수를 읽어 응답
	}
}

//─────────────────────────────────────────────────────────────
// [문제 7 - Thread07 연계] 스레드 풀로 주문 4건 처리하기
//
// 아래 OrderTask는 주문 1건 처리를 흉내 내는 작업이다. (이미 완성됨)
// makePool 메소드에서 TODO 7을 완성하시오.
//
// 추가 서술 : 스레드 2개짜리 풀에 작업 4건을 제출하면
//            3, 4번째 작업은 언제 실행되는가? 주석으로 답하시오.
//            답:
//─────────────────────────────────────────────────────────────
class OrderTask implements Runnable {

	int orderNo; //주문 번호

	public OrderTask(int orderNo) { this.orderNo = orderNo; }

	@Override
	public void run() {
		System.out.println("주문" + orderNo + " 처리 - 담당: "
		                   + Thread.currentThread().getName());
		try { Thread.sleep(300); } catch (InterruptedException e) { }
	}
}

//─────────────────────────────────────────────────────────────
// 실행용 main (수정하지 말 것 / 단, TODO 3만 완성할 것)
//─────────────────────────────────────────────────────────────
public class Thread_PerExample_Practice {

	//문제 7용 메소드 : 스레드 풀을 만들어 주문 4건을 처리한다
	public static void makePool() {
		// TODO 7 : ① 스레드 2개짜리 풀을 생성하시오 (Executors.newFixedThreadPool)
		//          ② 일반 for문으로 OrderTask 1~4번을 풀에 제출하시오 (execute)
		//          ③ 제출이 끝나면 풀을 종료 예약하시오 (shutdown)

	}

	public static void main(String[] args) throws InterruptedException {

		// [문제 1 실행]
		System.out.println("=== 문제 1 ===");
		AnnounceThread a1 = new AnnounceThread();
		a1.setName("방송-상속");
		a1.start();
		Thread a2 = new Thread(new AnnounceTask(), "방송-구현");
		a2.start();
		a1.join();  a2.join();

		// [문제 2 실행]
		System.out.println("=== 문제 2 ===");
		Thread c1 = new Thread(new CheckTask(), "점검");
		c1.start();
		c1.join();

		// [문제 3 실행]
		System.out.println("=== 문제 3 ===");
		RangeSum front = new RangeSum(1, 100);     //1~100 합 계산 작업
		RangeSum back  = new RangeSum(101, 200);   //101~200 합 계산 작업
		Thread s1 = new Thread(front, "앞구간");
		Thread s2 = new Thread(back,  "뒷구간");
		s1.start();
		s2.start();
		// TODO 3 : 아래 출력 전에 두 계산이 반드시 끝나 있도록 코드를 추가하시오

		System.out.println("전체 합 : " + (front.result + back.result)); //20100 이 나와야 정답

		// [문제 4 실행] - 결과가 2000000보다 작게 나오는 것을 눈으로 확인
		System.out.println("=== 문제 4 ===");
		ViewCounter vc = new ViewCounter();
		Thread v1 = new Thread(new CountUpTask(vc));
		Thread v2 = new Thread(new CountUpTask(vc));
		v1.start(); v2.start(); v1.join(); v2.join();
		System.out.println("조회수(깨짐 확인) : " + vc.count);

		// [문제 5 실행] - 항상 2000000이 나와야 정답
		System.out.println("=== 문제 5 ===");
		SafeViewCounter svc = new SafeViewCounter();
		Thread w1 = new Thread(new CountUpTask(svc));
		Thread w2 = new Thread(new CountUpTask(svc));
		w1.start(); w2.start(); w1.join(); w2.join();
		System.out.println("조회수(안전) : " + svc.count);

		// [문제 6 실행]
		System.out.println("=== 문제 6 ===");
		OrderServlet servlet = new OrderServlet();
		System.out.println(servlet.order("노트북"));
		System.out.println(servlet.order("키보드"));

		// [문제 7 실행]
		System.out.println("=== 문제 7 ===");
		makePool();
		Thread.sleep(1000); //풀의 작업이 끝나기를 잠시 기다린 후 종료
	}
}
