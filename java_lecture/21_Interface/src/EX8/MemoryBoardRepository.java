package EX8;

//[3] MemoryBoardRepository 클래스 
//- 배열(DB공간으로사용)에 저장하는 저장소. 추상메소드 5개를 오버라이딩 한다.
public class MemoryBoardRepository implements BoardRepository {

	//글 여러 건을 담아 둘 배열.  크기는 인터페이스 의 상수값 100을 사용한다.
	private Board[] boards = new Board[MAX_SIZE];

	//실제로 채워진 칸의 갯수 저장할 변수 만들기 
	private int count = 0;

	//위 boards배열에  새 글 한건의 정보를 추가 하는 기능 
	@Override
	public boolean insert(Board board) {  // <- new Board(1, "첫 글", "내용 입니다", "홍길동" );
										  // <- new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수")
		//boards배열 칸이 최대치에 도달했는지 확인한다.
		if(count >= MAX_SIZE) {
			
			//새글 추가할 boards배열의 칸이 없으므로 새글 추가 실패를 알린다.
			return false;
		}
		
		//위 boards배열의 비어있는 첫칸에 새 글 한건의 정보를 넣는다
		//그 index 위치가 count변수의 값이다
		boards[count] = board;
		
		//위 boards배열에 채워진 칸의 그다음 위치칸으로 이동하기 위해 index 위치 변경
		count++;
		
		//저장 성공을 알립니다.
		return true;
	}

	//boards 배열에 추가된 모든 글을 가져와 제공하는 메소드 
	@Override
	public Board[] selectAll() {
		
		//실제 글 개수만큼만  새로운 배열을 만듭니다.
		//이유 : 100칸 배열을 그대로 넘기면 뒤쪽 칸에 null이 모두 저장되기 때문에 나중에 오류가 난다.
		Board[]  result = new Board[count];
		
		//0번 칸부터 count - 1번 index 칸까지 반복한다
		for(int i=0;   i<count;  i++) {
		
			//새글 이 각 칸에 저장된 배열의 각칸의 new Board(1, "첫 글", "내용 입니다", "홍길동" ); 와 
			//							   new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수") 을 반복해서 얻어
			//result배열의 각칸에 차례대로 반복해서 담는다.
			  result[i] = boards[i];
			  
		}
		//추가된 글들이 저장된 복사가 끝난 배열을 돌려준다.
		return result;
	}

	//위 boards배열에 추가된 모든 글 중에서 글번호에 해당하는 글 한건을 제공하는 메소드 
	@Override
	public Board selectOne(int boardId) {  //<== 2
		
		//boards배열에 저장된 글만 확인한다
		for(int i=0;  i<count;  i++) {
			
			//각 글의 번호와 찾는 번호를 비교한다
			if(boards[i].getId() == boardId) {
				
				//찾으면 그 객체를 돌려주고  반복을 즉시 끝낸다
				return boards[i];  //return  new Board(2, "인터페이스 질문", "default 메소드가 궁금합니다", "김철수");
			}
		}
		//끝까지 못찾으면 null 을 돌려준다
		return null;
	}

	//위 boards배열에 추가되어 있는 글 중에서 매개변수로 받은 글번호에 해당하는 글의 내용을 수정해서 그결과를 제공하는 메소드 
	@Override
	public boolean update(int boardId, String newContent) {
		
		//바로 위에 만든  글번호에 해당하는 글 한건을 제공하는 메소드 재활용
		Board found = selectOne(boardId);
		//			=  new Board(1, "첫 글", "내용 입니다", "홍길동" );
		
		// 못 찾았으면 null 이 들어 있다.
		if(found == null) {
			return false; //글내용 수정하지 않고 수정 실패를 알리자
		}
		
		//찾은 글 객체의 내용을 새 값으로 바꾼다
		// found는 boards배열 안의 Board객체를 가리키므로 boards배열의 칸에 저장된 Board객체의 정보도 바뀐다
		found.setContent(newContent);
		
		//글 내용 수정 성공을 알리자
		return true;
	}

	//위 boards배열에 추가되어 있는 모든 글 중에서 글번호에 해당하는 글 한건을 정보 삭제 해  그 결과를 제공하는 메소드 
	@Override
	public boolean delete(int boardId) {
		
		for(int i=0;  i<count;  i++) {
			
			//삭제할 글을 찾았는지 확인한다
			if(boards[i].getId() == boardId) {
				
				//찾은 위치부터 마지막 직전 칸 까지 반복한다
				for(int j=i; j<count-1; j++) {
					
					//뒤 칸의 값을 앞 칸으로 당겨 빈 칸을 매운다
					boards[j] = boards[j+1];
				}//--------------- 안쪽 for
				
				//맨 뒷 칸에 남아 있는 중복 값을 삭제한다
				boards[count - 1] = null;
				
				//채워진 칸 수를 1줄인다.
				count--;
				
				//삭제 성공을 알린다
				return true;
			}//----------------if
		}//------------------------- for
		
		//삭제할 글번호의 글이 boards배열에 존재하지 않으면? 글 삭제 실패를 알린다
		return false;
	}//-------------------------------------------------------------------- delete메소드 끝

}
