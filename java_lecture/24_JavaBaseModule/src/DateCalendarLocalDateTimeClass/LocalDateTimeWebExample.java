package DateCalendarLocalDateTimeClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/*
웹 개발 응용 예제 (하나의 파일)

상황 설명
- 게시글을 작성하면 작성 시간이 저장된다.
- 게시글은 작성 후 24시간 이내에만 수정할 수 있다.
- LocalDateTime을 이용해 현재 시간과 작성 시간을 비교한다.

이 예제에서 배우는 것
1. LocalDateTime.now() 사용법
2. plus / minus 날짜 계산
3. isAfter(), isBefore() 날짜 비교
4. until() 시간 차이 계산
*/

public class LocalDateTimeWebExample {

	//게시글 하나의 정보가 저장되는 역할을 하는 내부(중첩) 클래스 선언 (DTO 역할)
	static class Board{
		
		int boardNo;   				//글번호
		String title;  				//글제목
		String content;				//글내용
		LocalDateTime createdAt;    //글작성 날짜 및 시간 
		
		//글 하나의 정보를 초기화 시킬 생성자
		Board(int boardNo,  String title, String content){
			this.boardNo = boardNo;
			this.title = title;
			this.content = content;
			this.createdAt = LocalDateTime.now(); //글작성 날짜 및 시간 정보를 현재 컴퓨터에 설정된 정보가 들어가게 저장
		}
		
	} //---- Board 내부(중첩) 클래스 끝 
	
	//게시 글 관련 기능의 메소드들을 포함하고 있는 내부(중첩) 클래스 선언 (서비스 역할)
	static class BoardService {
		
		//게시글 수정 가능 여부( 24시간 이내 수정 가능 여부) 판단하는 기능의 메소드 선언
		boolean canEdit(Board board) {  //<-------  new Board(1,  "LocalDateTime 예제", "웹 개발 응용");
			
			//현재 날짜 및 시간 정보가 저장된 LocalDateTime 객체 얻기
			LocalDateTime  now = LocalDateTime.now();
			
			//글 작성 날짜 및 시간 정보가 저장된 LocalDateTime 객체 얻기
			LocalDateTime createdTime = board.createdAt;
			
			//글 작성 날짜 및 시간 정보로부터 현재 날짜 및 시간이 지난 시간을 구해 (시간 단위로 구해) 저장
			long hoursBeteen = createdTime.until(now, ChronoUnit.HOURS);
			
			//24 시간 이내면 수정 가능 
			return hoursBeteen < 24;			
		}
		
	} // ---- BoardService 내부(중첩) 클래스 끝 
	

	public static void main(String[] args) {
		
		//1. 게시글 하나 작성
		Board board = new Board(1, "LocalDateTime 예제", "웹 개발 응용");
		
		//테스트를 위해 글 작성 시간을 23시간 전으로 변경
		board.createdAt = board.createdAt.minusHours(23);
		
		BoardService service = new BoardService();
		
		//2. 글 수정 가능 여부 확인
		boolean result = service.canEdit(board);
		
		//3. 결과 출력
		if(result) {
			System.out.println("게시글 수정 가능(게시글 하나 작성 후 24시간 이내)");
		}else {
			System.out.println("게시글 수정 불가능(게시글 하나 작성 후 24시간 초과)");
		}
		
		//4. 날짜 비교  메소드 예제
		LocalDateTime now = LocalDateTime.now(); //현재 컴퓨터에 설정된 날짜 정보가 저장된 LocalDateTime 객체 반환
		LocalDateTime future = now.plusDays(1);  //현재 컴퓨터에 설정된 날짜에서  + 1을 더한 날짜를 구한 정보가 저장된 LocalDateTime 객체 반환
		LocalDateTime past = now.minusDays(1);//현재 컴퓨터에 설정된 날짜의 정보에서 -1일을 한 날짜 정보가 저장된 LocalDateTime 객체 반환
		
		System.out.println("future 가 now 이후 인가? " + future.isAfter(now)); //true
		System.out.println("past 가 now 이전 인가? " +  past.isBefore(now) ); //true
		System.out.println("now 가 now 와 같은가? " +  now.isEqual(now) );    //true

		//5. 날짜 차이 계산
		long daysBetween = now.until(future, ChronoUnit.DAYS);
		System.out.println("현재와 미래 날짜 차이 : " + daysBetween + "일");  //1일 
		
	}

}

