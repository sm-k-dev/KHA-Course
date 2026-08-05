package DateCalendarLocalDateTimeClass;

/*
Calendar 클래스

- java.util 패키지에 만들어져있는 추상클래스

- Calendar 클래스는 달력을 표현하는 추상클래스이다.

- 날짜와 시간을 계산하는 방법이 지역과 문화에 따라 다르기 떄문에 특정 역법(날짜와 시간을 매기는 방법)에 따르는
  달력은 자식클래스에서 구현하도록 되어 있다.
  
- 특별한 역법을 사용하는 경우가 아니라면 직접 하위 클래스를 만들 필요는 없고,
  Calender 추상클래스의 정적메소드인 getInstance()메소드를 이용하면
  컴퓨터에 설정되어 있는 시간대(TimeZone)을 기준으로 Calender 추상부모클래스의 하위자식객체를 반환받아 얻을수 있다
  
  예)  Caldener now = Calender.getInstance();

Calender 클래스가 제공하는 날짜와 시간에 대한 정보를 얻기 위해 get() 메소드를 이용한다.
get()메소드 호출시 전달되는 인자값으로 Calender 추상클래스에 정의된 상수메모리를 넣어주면 상수에 저장된 의미값을 반환한다

 예)  
 		int year = now.get(Calendar.YEAR); //현재 년도를 리턴
 		int month = now.get(Calendar.MONTH) + 1; //현재 월을 리턴
 		int day =   now.get(Calendar.DAY_OF_MONTH); //현재 일을 리턴
 		int week = now.get(Calendar.DAY_OF_WEEK); //현재 요일을 리턴
 		int amPm = now.get(Calendar.AM_PM); //현재 오전 또는 오후 리턴
       int hour = now.get(Calendar.HOUR); //현재 시  리턴
       int minute = now.get(Calendar.MINUTE); //현재 분  리턴
       int second = now.get(Calendar.SECOND); //현재 초  리턴
     
       
*/
import java.util.Calendar;

public class CalendarExample {

	public static void main(String[] args) {
		
		//1. Calender 추상클래스로는 new 로 객체 생성이 불가능 하기 떄문에
		//	 Calender 추상클래스를 구현 한 하위 자식 GregorianCalendar객체를 얻어 사용
		Calendar now = Calendar.getInstance();
	//  Calendar now = new 	GregorianCalendar();  <----- 업캐스팅 해서 저장 
		
		//2. 현재 날짜와 시간 정보를 구해 변수에 각각 저장
		int year = now.get( Calendar.YEAR  );  		//현재 년도 정보를 반환 받아 저장 
		int month = now.get( Calendar.MONTH ) + 1;  //현재 월 정보를 get메소드로 1적게 얻어  + 1 해서 월정보 저장
		int day   = now.get( Calendar.DAY_OF_MONTH);//현재 일 정보를 반환 받아 저장
		int week  = now.get( Calendar.DAY_OF_WEEK );//현재 요일 정보를 반환 받아 저장
		
		String strWeek = null;
		
		//3. 현재 요일 정수값에 따라 한글로 요일을 저장 
		switch (week) {  // 현재 요일 정수값이~~~~
		
			case Calendar.MONDAY: //월요일 정수값과 같다면?
				strWeek = "월";
				break;
				
			case Calendar.TUESDAY: //화요일 정수값과 같다면?
				strWeek = "화";
				break;
				
			case Calendar.WEDNESDAY: //수요일 정수값과 같다면?
				strWeek = "수";
				break;		
				
			case Calendar.THURSDAY: //목요일 정수값과 같다면?
				strWeek = "목";
				break;		
				
			case Calendar.FRIDAY: //금요일 정수값과 같다면?
				strWeek = "금";
				break;				
	
			case Calendar.SATURDAY: //토요일 정수값과 같다면?
				strWeek = "토";
				break;
				
			default:	//일요일 정수값과 같다면?
				strWeek = "일";
				break;
		}
		
		//4. 현재 오전 또는 오후 인지에 대한 정보 반환 받아 저장
		int amPm = now.get(Calendar.AM_PM);
		
		String strAmPm = null;
		
		if(amPm == Calendar.AM) { //현재 시간이 오전이라면?
			strAmPm = "오전";
		}else {
			strAmPm = "오후";
		}
		
		//5. 현재 시, 분 , 초 정보 구하기 
		int hour = now.get( Calendar.HOUR ); 		//현재 시간 정보 반환
		int minute = now.get( Calendar.MINUTE );	//현재 분 정보 반환
		int second = now.get( Calendar.SECOND );    //현재 초 정보 반환
		
		//6. 현재 날짜와 시간 정보 모두 출력
		System.out.println(year + "년 " + month + "월 " + day + "일");
		System.out.println(strWeek + "요일 " + strAmPm + " " + hour + "시 " + minute + "분 " + second + "초");

	}

}

