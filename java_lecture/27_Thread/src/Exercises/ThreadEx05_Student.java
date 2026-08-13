package Exercises;

/*
 * ==================================================================
 * [연습문제 5] 좋아요 수 사고 해결 - synchronized       난이도 ★★☆
 * (Thread05_Synchronized 학습 내용)  학생용 - TODO 를 완성하시오
 * ==================================================================
 *
 * ■ 상황
 *   연습문제 4에서 좋아요 수가 200만이 안 되는 사고를 확인했다.
 *   이번에는 synchronized 를 사용해서 그 사고를 없앤다.
 *   두 가지 형태를 모두 만들어 결과가 같은지 확인한다.
 *     형태 1 : synchronized 메소드  -> 메소드 전체를 보호
 *     형태 2 : synchronized 블록    -> 필요한 부분만 보호
 *
 * ■ synchronized 동작 모델
 *
 *   보호 장치가 없을 때 (연습문제 4)
 *     사용자A --> [ 읽기 - 더하기 - 저장 ] <-- 사용자B 가 중간에 끼어듦
 *                 끼어들기 때문에 값이 사라진다
 *
 *   synchronized 가 있을 때
 *     사용자A --> [ 읽기 - 더하기 - 저장 ]  (통과 중)
 *     사용자B --> 문 앞에서 대기 ...        (A가 끝나야 들어간다)
 *
 *   즉 "한 번에 한 스레드만" 그 구역을 실행하게 만든다.
 *   대기가 생기므로 안전해지는 대신 속도는 느려진다.
 *
 * ■ 두 형태의 보호 범위 비교 모델
 *
 *   형태 1 : public synchronized void increase() {
 *              like++;              <- 메소드 전체가 보호 구역
 *            }
 *
 *   형태 2 : public void increaseBlock() {
 *              String name = ...;   <- 이 부분은 보호 안 됨 (동시 실행 가능)
 *              synchronized (this) {
 *                  like++;          <- 이 블록 안만 보호 구역
 *              }
 *            }
 *
 *   지역변수(name)는 스레드마다 따로 만들어져 원래 안전하므로
 *   보호할 필요가 없다. 꼭 필요한 부분만 묶는 것이 성능에 유리하다.
 *
 * ■ 예상 출력 (몇 번을 실행해도 항상 같다)
 *   기대값 : 2000000
 *   메소드 방식 결과 : 2000000
 *   블록   방식 결과 : 2000000
 *
 * ■ 서술형 문제 (완성 후 주석으로 답을 적으시오)
 *   문제 A. synchronized 를 모든 메소드에 붙이지 않는 이유는 무엇인가?
 *           답:
 * ==================================================================
 */

//==================================================================
// [클래스 1] 보호 장치가 적용된 좋아요 서비스
//==================================================================
class SafeLikeService {

    //모든 스레드가 공유하는 멤버 변수 (보호 대상)
    int like = 0;

    //----------------------------------------------------------
    // TODO 1 : [형태 1] synchronized 메소드를 완성하시오
    //
    //   선언부 조각을 순서대로 적는다
    //     public       : 밖에서 호출하므로 공개
    //     synchronized : 이 메소드 전체를 보호 구역으로 만든다는 표시.
    //                    한 스레드가 실행하는 동안 다른 스레드는 앞에서 대기한다
    //     void         : 돌려줄 값 없음
    //     increase     : 메소드 이름
    //     ()           : 전달받을 재료 없음
    //     --> public synchronized void increase() {
    //
    //   몸통 : like++;
    //     이제 읽기, 더하기, 저장 3단계 사이에 다른 스레드가 끼어들 수 없다
    //
    //   닫기 : }
    //----------------------------------------------------------

    //여기에 TODO 1 의 메소드를 작성하시오
    public synchronized void increase() {
		like++;
	}



    //----------------------------------------------------------
    // TODO 2 : [형태 2] synchronized 블록을 완성하시오
    //
    //   선언부 : public void increaseBlock() {     <- synchronized 없음
    //
    //   몸통
    //     줄 1 : 지역변수 하나를 만든다 (보호가 필요 없는 부분의 예시)
    //            --> String name = Thread.currentThread().getName();
    //            지역변수는 스레드마다 따로 만들어지므로 원래 안전하다
    //     줄 2 : 보호 구역 시작
    //            --> synchronized (this) {
    //            (this) : 무엇을 기준으로 잠글지 지정하는 자리.
    //                     this 는 "지금 이 객체" 라는 뜻이다.
    //                     같은 객체를 기준으로 잠그므로 형태 1과 효과가 같다
    //     줄 3 :     like++;      <- 이 안만 한 번에 한 스레드씩 실행된다
    //     줄 4 : }                <- 보호 구역 끝
    //
    //   닫기 : }
    //----------------------------------------------------------

    //여기에 TODO 2 의 메소드를 작성하시오
    public void increaseBlock() {
    	
    	//지역변수 : 메소드가 호출될 때 마다 스레드의 Stack에 새로 만들어진다
    	//         스레드마다 자기 것을 따로 가지므로 안전하다 ---> 보호 불필요
    	String name = Thread.currentThread().getName();
    	
    	synchronized (this) {
    		like++;
		}
    
    }
    

}   //SafeLikeService 클래스의 끝

//==================================================================
// [클래스 2] 좋아요를 반복해서 누르는 작업
//
//   이 작업은 useBlock 값에 따라 두 방식 중 하나를 호출한다
//   (같은 작업 클래스로 두 방식을 모두 실험하기 위해서다)
//==================================================================
class SafeLikeTask implements Runnable {

    //사용할 서비스 객체
    SafeLikeService service;

    //true 면 블록 방식, false 면 메소드 방식을 호출한다
    boolean useBlock;

    //생성자 : 서비스 객체와 방식 선택 값을 전달받는다
    public SafeLikeTask(SafeLikeService service, boolean useBlock) {
        this.service = service;
        this.useBlock = useBlock;
    }   //생성자의 끝

    //----------------------------------------------------------
    // TODO 3 : run 메소드의 몸통을 완성하시오
    //
    //   줄 1 : for 문으로 100만 번 반복
    //          --> for (int i = 0; i < 1000000; i++) {
    //   줄 2 :   방식에 따라 다른 메소드를 호출한다
    //          --> if (useBlock) {
    //   줄 3 :       service.increaseBlock();
    //   줄 4 :   } else {
    //   줄 5 :       service.increase();
    //   줄 6 :   }
    //   줄 7 : }
    //----------------------------------------------------------

    @Override
    public void run() {

        //여기에 TODO 3 을 작성하시오
        //100만 번 반복 (0 부터 999999 까지)
        for (int i = 0; i < 1000000; i++) {

            //if (useBlock) : 괄호 안이 true 면 아래 줄을 실행한다.
            //  boolean 변수는 == true 를 붙이지 않고 그대로 쓰는 것이 보통이다
            if (useBlock) {

                service.increaseBlock();   //블록 방식 호출

            } else {

                service.increase();        //메소드 방식 호출

            }   //if ~ else 의 끝

        }   //for 반복의 끝

    }   //run 메소드의 끝

}   //SafeLikeTask 클래스의 끝

//==================================================================
// [클래스 3] 실행 클래스
//==================================================================
public class ThreadEx05_Student {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("기대값 : 2000000");

        //------------------------------------------------------
        // TODO 4 : 메소드 방식(형태 1)을 실험하시오
        //
        //   줄 1 : 서비스 객체 1개 생성
        //          --> SafeLikeService s1 = new SafeLikeService();
        //   줄 2 : 작업 객체 생성 (false = 메소드 방식)
        //          --> SafeLikeTask task1 = new SafeLikeTask(s1, false);
        //   줄 3 : 스레드 2개 생성
        //          --> Thread a1 = new Thread(task1, "사용자A");
        //   줄 4 : --> Thread a2 = new Thread(task1, "사용자B");
        //   줄 5,6 : 두 스레드 시작   --> a1.start();  a2.start();
        //   줄 7,8 : 두 스레드 대기   --> a1.join();   a2.join();
        //   줄 9 : 결과 출력
        //          --> System.out.println("메소드 방식 결과 : " + s1.like);
        //------------------------------------------------------

        //여기에 TODO 4 의 아홉 줄을 작성하시오
        SafeLikeService s1 = new SafeLikeService();
        SafeLikeTask task1 = new SafeLikeTask(s1, false);


        Thread a1 = new Thread(task1, "사용자A");
        Thread a2 = new Thread(task1, "사용자B");

        a1.start();  a2.start();

        a1.join();   a2.join();
        //------------------------------------------------------
        // TODO 5 : 블록 방식(형태 2)을 같은 순서로 실험하시오
        //
        //   위 TODO 4 와 똑같이 하되 두 곳만 다르다
        //     1. 작업 객체를 만들 때 true 를 전달한다 (블록 방식 선택)
        //        --> SafeLikeTask task2 = new SafeLikeTask(s2, true);
        //     2. 출력 문구를 "블록   방식 결과 : " 로 한다
        //   서비스 객체와 스레드 변수 이름은 s2, b1, b2 처럼 새로 지어야 한다
        //   (같은 이름을 다시 쓰면 이름 중복 오류가 난다)
        //------------------------------------------------------

        //여기에 TODO 5 의 아홉 줄을 작성하시오
        //서비스 객체를 "새로" 만든다.
        //  s1 을 재사용하면 like 가 200만에서 이어져 400만이 되어 버린다
        SafeLikeService s2 = new SafeLikeService();

        //두 번째 재료 true = 블록 방식 선택
        SafeLikeTask task2 = new SafeLikeTask(s2, true);

        //스레드 이름 변수도 새로 지어야 한다 (같은 이름은 중복 오류)
        Thread b1 = new Thread(task2, "사용자C");
        Thread b2 = new Thread(task2, "사용자D");
        b1.start();
        b2.start();

        b1.join();
        b2.join();

        //블록 방식도 결과가 같다
        System.out.println("블록   방식 결과 : " + s2.like);
    }   //main 의 끝

}   //ThreadEx05_Student 클래스의 끝




