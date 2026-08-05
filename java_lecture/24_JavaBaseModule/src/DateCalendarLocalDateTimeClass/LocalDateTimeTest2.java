package DateCalendarLocalDateTimeClass;

/*
LocalDateTime 클래스는 날짜와 시간을 비교할수 있는 메소드들도 제공합니다.

표
		리턴타입 			메소드(매개변수)			설명
						isAfter(other)		이후날짜인지?
		boolean			isBefore(other)     이전날짜인지?
						isEqual(other)		동일한 날짜인지?
		
		long			until(other,unit)   주어진 단위(unit) 차이를 리턴 
		
*/

import java.time.LocalDateTime;			// LocalDateTime 클래스를 사용하기 위한 import
import java.time.temporal.ChronoUnit;	// 시간단위를 계산하기 위한 ChronoUnit 클래스 (열거형 클래스)를 사용하기 위해 import 

public class LocalDateTimeTest2 {
	public static void main(String[] args) {
		
		//현재 컴퓨터에 설정된 날짜와 시간정보를 지닌  LocalDateTime 클래스의 객체 얻기
		LocalDateTime now = LocalDateTime.now(); //2026-08-05
		
		//현재 컴퓨터에 설정된 날짜와 시간 2026-08-05에서 5일을 더한 날짜를 구해 보관을 한 LocalDateTime 객체 얻기
		LocalDateTime futureDate = now.plusDays(5); //2026-08-10
		
		//현재 컴퓨터에 설정된 날짜와 시간 2026-08-05에서 3일을 뺀 날짜를 구해 보관을 한 LocalDateTime 객체 얻기
		LocalDateTime pastDate = now.minusDays(3);  //2026-08-02
		
		//until() : 두 날짜 사이의 차이 계산 해서 정수로 반환하는 메소드  (단위 : DAYS)
		long daysBetween = now.until(futureDate, ChronoUnit.DAYS);
//					2026-08-05.until(2026-08-10, 두 날짜 사이의 차이 일 단위 계산); 
		
		/*
		 now참조변수에 저장된 LocalDateTime객체 내부의 현재 날짜가!
		 futureDate참조변수에 저장된 LocalDateTime객체 내부의 날짜사이의 차이 = 5일
		 */
		System.out.println("now참조변수에 저장된 LocalDateTime객체 내부의 현재 날짜가!");
		System.out.print("futureDate참조변수에 저장된 LocalDateTime객체 내부의 날짜사이의 차이 = ");
		System.out.println(daysBetween + "일");
		
		//isBefore()  :  pastDate (2026-08-02) 가  now (2026-08-05) 보다 이전 날짜인지 검사하는 메소드 
		System.out.println("pastDate 가  now 보다  이전 날짜인가? " + pastDate.isBefore(now));
		
		//결과 : true (pastDate참조변수에 저장된 LocalDateTime객체 내부의 2026-08-02 날짜 정보가?
		//			  now 참조변수에 저장된 LocalDateTime객체 내부의 2026-08-05 날짜 정보보다 이전 날짜이므로 isBefore메소드는 true 반환)
		
		
		
		//isAfter()  :  futureDate (2026-08-10) 가  now (2026-08-05) 보다 이후 날짜인지 검사하는 메소드
		System.out.println("futureDate 가  now 보다 이후 날짜인가? " + futureDate.isAfter(now));
																  //         true
		//결과 : true  (futureDate참조변수에 저장된 LocalDateTime객체 내부의 2026-08-10 날짜 정보가?
		//			   now 참조변수에 저장된 LocalDateTime객체 내부의 2026-08-05 날짜 정보보다 이후 날짜이므로 )

	}

}
