package DateCalendarLocalDateTimeClass;

/*

날짜와 시간 정보를 제공하거나 조작할 수 있게 도와 주는 클래스들

	 Date 클래스 -  날짜와 시간정보를 제공 하거나 조작하는 클래스
	 
	 Calender 클래스 - 다양한 지역의 시간대별로 날짜와 시간을 얻을때 사용하는 클래스 
	 
	 LocalDateTime 클래스 - 날짜와 시간을 조작할때 사용하는 클래스 
	 
	 
Date 클래스
-  java.util 패키지에 만들어서 제공하는 클래스

-  날짜를 표현하는 클래스로 객체 간에 날짜 정보를 주고 받을때 사용된다.

-  Date 클래스에는 여러개의 생성자가 선언되어 있지만  대부분 Date() 기본생성자만 주로 사용된다.
   Date() 기본생자는 컴퓨터의 현재 날짜를 읽어 저장시킨 Date객체메모리를 생성할때 주로 호출해서 사용한다.   
  예) Date now = new Date();

*/
import java.util.Date;

import java.text.SimpleDateFormat;

public class DateExample {
	public static void main(String[] args) {
		
		Date now = new Date();  //Date() 기본생성자를 호출해  new Date(); 객체를 생성하면
								//현재 컴퓨터에 설정된 현재 날짜 및 시간정보를 읽어 저장시킨 Date 객체가 생성됨
		
		//현재 날짜 및 시간이 저장된 Date 객체의 정보를 문자열로 변환 해서 반환 받아 출력
		//	    			"Tue Aug 04 16:51:02 KST 2026"    <=== 현재 날짜 및 시간정보가 영문 형태로 출력 
		System.out.println(    now.toString()     			);
		
		//===================================================================================================
		//"Tue Aug 04 16:51:02 KST 2026"
		//위 영문의 현재 날짜와 시간정보를 우리 개발자가 원하는 포맷 형식으로 만들어서 출력하게 도와 주는 SimpleDateFormat클래스를 사용하자.
		
		//순서1. SimpleDateFormat 클래스의 생성자를 호출할때 원하는 포맷형식을 문자열 형태로 전달해서 저장후 객체 생성
		SimpleDateFormat   sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
														
		//순서2. SimpleDateFormat 클래스에 만들어져 있는 format( new Date() ); 호출하면 
		//      반환값으로 "yyyy.MM.dd HH:mm:ss" 포맷 형식으로 변경해서 반환
		String strNow2 = sdf.format(now);
//		String strNow2 = sdf.format(new Date());
		
		System.out.println(strNow2); //"2026.08.04 17:09:20"
									 //"yyyy.MM.dd HH:mm:ss"
		
	}

}
//패턴 문자 
//y   년도 의미 
//M   월을 의미
//d   일을 의미
//D   월구분이 없는 일(1 ~ 365)
//E   요일
//a   오전/오후
//w   년의 몇번 쨰 주
//W	  월의 몇번 째 주 
//H   시간을 의미 (0 ~ 23)
//h	  시간을 의미 (1 ~ 12)
//K   시간을 의미 (0 ~ 11)
//k   시간을 의미 (1 ~ 24)
//m   분을 의미
//s   초를 의미 
//S   밀리세컨드(1/1000초)