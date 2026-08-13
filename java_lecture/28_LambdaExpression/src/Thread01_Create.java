/*
 * [Thread01] 스레드란? / 스레드 만드는 2가지 방법
 *
 * ■ 프로세스와 스레드
 *   - 프로세스: 실행 중인 프로그램 하나. (예: 실행 중인 톰캣, 실행 중인 이클립스)
 *   - 스레드: 프로세스 안에서 코드를 실행하는 실행 흐름 하나.
 *   - 프로세스 하나 안에는 스레드가 여러 개 있을 수 있다.
 *   - main() 메서드도 "main"이라는 이름의 스레드 위에서 실행된다.
 *
 * ■ JSP/Spring에서 왜 배우는가?
 *   - 톰캣(WAS)은 프로세스 1개이고, 그 안에 스레드를 여러 개 미리 만들어 둔다.
 *   - 브라우저 요청이 들어올 때마다 스레드 1개를 배정해서
 *     서블릿/컨트롤러 코드를 실행시킨다.
 *   - 즉, 사용자 10명이 동시에 접속하면 같은 코드가 스레드 10개에서
 *     동시에 실행된다. 이 사실이 이번 단원 전체의 핵심이다.
 *
 * ■ 스레드를 만드는 2가지 방법
 *   방법 1: Thread 클래스를 상속하고 run()을 오버라이딩
 *   방법 2: Runnable 인터페이스를 구현하고 run()을 오버라이딩 (실무 권장)
 *   - 방법 2를 권장하는 이유: 자바는 단일 상속이라 Thread를 상속하면
 *     다른 클래스를 상속할 수 없다. Runnable은 인터페이스라 제약이 없다.
 */

//방법1. Thread 클래스 상속 하여 하나의 작업흐름을 처리하는 일꾼(개발자가 직접 만드는 스레드)을 만든다
class MyThread  extends Thread {
	
	//스레드가 시작되면 run() 안의 코드가 실행된다.
	//결론 : 스레드가 할일을 run()메소드 내부에 작성 해 놓자
	@Override
	public void run() { //할일 :  1부터 3까지 반복해서 출력
		
		for(int i=1;  i<=3; i++) {
			
			//this.getName() : 현재 일하고 있는 스레드의 이름을 반환한다.(Thread 에게 상속 받은 메소드)
			System.out.println( this.getName() + " 실행 : " + i );
		}
		
	}	
}

//방법2. Runnable 인터페이스안에 만들어져 있는 run 추상메소드를 오버라이딩 하여 하나의 작업을 처리하는 일꾼(개발자가 직접 만드는 스레드)을 만든다
//-----> 코드 삭제 이유  : 람다식으로 작성을 해서 사용하기 위해 


public class Thread01_Create {

	//main 스레드 : 다른 스레드에게 일을 시키고, 프로그램 시작과 끝을 담당하는 스레드 
	public static void main(String[] args) {

		//-- 방법 1 ---
		//Thread 클래스를 상속한 클래스는 그 자체가 스레드이므로 바로 start() 메소드 호출 가능
		MyThread t1 = new MyThread();  //<-- 스레드(일꾼)
		t1.setName("상속스레드");  // 스레드 이름 저장 (저장 안하면  Thread-0,  Thread-1...)
		t1.start();             // 새 스레드를 만들고 run() 메소드를 JVM에게 실행시키는 명령어 
		
		//--- 방법2 ---
		//참고. Runnable 인터페이스를 구현한 객체는 "run()" 메소드만 재작성 해 놓은 객체일 뿐이므로 
		//     Therad객체를 직접 생성해서 전달해서 보관해야함
		Runnable task =  () -> {    
			
			for(int i=1;  i<=3;  i++) {
				System.out.println(Thread.currentThread().getName() + " 실행 : " + i );
			}		
			
		};  //<--- 람다식 문장의 끝 
		
		new Thread(task, "구현스레드").start();

		//-- 방법2 더 축소-----		
		new Thread( () ->{
			
			for(int i=1;  i<=3;  i++) {
				System.out.println(Thread.currentThread().getName() + " 실행 : " + i );
			}	
		
		}, "구현스레드2").start();
		
	
		
		
		System.out.println("main 스레드의 코드 실행");
		
        /*
         * ■ 실행 결과 (실행할 때마다 순서가 다르다!)
         *   main 스레드의 코드 실행
         *   상속스레드 실행 : 1
         *   구현스레드 실행 : 1
         *   상속스레드 실행 : 2
         *   ... (섞여서 출력됨)
         *
         * ■ 핵심 정리
         *   1. 스레드 3개(main, 상속스레드, 구현스레드)가 동시에 실행됐다.
         *   2. 실행 순서는 OS가 정한다. 개발자가 순서를 보장할 수 없다.
         *   3. start() = 새 스레드 생성 후 run() 실행
         *      run()   = 새 스레드 없음, 단순 메서드 호출
         *   4. 실무에서는 Runnable 구현 방식을 쓴다.
         

import jdk.javadoc.internal.tool.Start;

*/
	}

}







