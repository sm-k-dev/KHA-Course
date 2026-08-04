package DateCalendarLocalDateTimeClass;

//Date 클래스를 사용하기 위해 import
//날짜와 시간을 다루기 위한 자바 기본 클래스
import java.util.Date;

// 주제 :  Date 클래스를 이용한 경과 시간 측정 

public class DateExample2 {

	public static void main(String[] args) {
		
		//현재 컴퓨터에 설정된 날짜 및 시간을 제공하는 Date 클래스의 객체 생성
		Date  startTime = new Date(); //작업 시작 날짜 및 시간 정보 저장되어있음
									  //예) 2026-01-02 17:05:20
		
		//작업 시작 시간 먼저 출력 해 놓자
		//출력 예) 작업 시작 시간 : Fri Jan 02 17:05:20 KST 2026
		System.out.println("작업 시작 시간 : " +  startTime);
		
		
		for(int i=1;  i<1000000000; i++) {
			System.out.println("작업 하자");
		}
		
		
		
		//작업 시작 시간 이후에 작업한 날짜와 시간을 제공하는 Date 클래스의 객체 생성 
		Date  endTime = new Date(); 
		
		//작업 시작 시간 이후에 작업한 날짜와 시간 출력 해 놓자
		//출력 예) 작업 종료된 시간 : Fri Jan 02 17:07:53 KST 2026
		System.out.println("작업 종료된 시간 : " + endTime);
		
		
		//startTime참조변수에 저장된 new Date() 객체의 작업 시작 날짜 및 시간을 숫자로 변환 해서 반환
		long startMs = startTime.getTime();
		/*
		   getTime() 메소드는 
		   1970년 1월 1일 00:00:00 초를 기준으로 
		   지금 작업을 시작한 날짜 및 시간 까지 흐른 시간을 밀리초(ms) 단위의 long 값으로 반환 !
		 */
		System.out.println(startMs);//작업 시작  날짜 및 시간을 초단위로 구해서 출력
						  //1767341533755
		
		
		//endTime참조변수에 저장된 new Date() 객체의 작업 종료  날짜 및 시간을 숫자로 변환해서 반환
		long endMs = endTime.getTime();
		/*
		   getTime() 메소드는 
		   1970년 1월 1일 00:00:00 초를 기준으로 
		   지금 작업을 종료한 날짜 및 시간 까지 흐른 시간을 밀리초(ms) 단위의 long 값으로 반환 !
		 */
		System.out.println(endMs);
						 //1767341658327
		
		//----------------------------------------------------------------------
		//  작업 종료 시간  - 작업 시작 시간 =  두 시간의 차이 계산 (작업에 걸린 시간) 할 수 있음
		//-----------------------------------------------------------------------
		
		//종료 시간에서 시작 시간을 뺴면   두 시간 사이에 실제로 흐른 시간이 계산됨
		long diffMs = endMs - startMs;
		
		
		//계산된 시간 차이를 밀리초 단위로 출력
		System.out.println("두 시간의 차이(ms) : " + diffMs);
		
		//밀리초 ? 1000/1 초 단위의 값
		
		//밀리초 -> 초  변경
		long diffSeconds = diffMs / 1000;
		
		//초 -> 분 
		long diffMinutes = diffSeconds / 60;
		
		//분 -> 시간
		long diffHours = diffMinutes / 60;
		
		//남은 초 계산
		long remainSeconds = diffSeconds % 60;
		
		//남은 분 계산
		long remainMinutes = diffMinutes % 60;
		
		//결과 출력(사람이 보기 쉬운 형태)
		System.out.println("작업에 걸린 시간 : " +  diffHours + "시간  " + remainMinutes + "분  " +  remainSeconds + "초");
		
/*
 
    [시작시간(ms)]        [종료시간(ms)]
      │                     │
      └─────── 빼기 ────────┘
              ↓
        diffMs (총 밀리초)
              ↓
        초 / 분 / 시간 분해
              ↓
     	"X시간 Y분 Z초"
 
 */
	}

}











