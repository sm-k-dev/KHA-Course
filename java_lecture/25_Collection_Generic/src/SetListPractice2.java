/*
	[연습문제 모음] HashSet + ArrayList 응용 3문제

	문제 1. 결석자 찾기          (핵심 : Set의 contains 메소드)
	문제 2. 댓글 금지어 필터      (핵심 : List의 get + set 메소드로 교체)
	문제 3. 두 반 수강생 비교     (핵심 : 교집합과 합집합 만들기)

	각 문제의 TODO를 완성하시오. 실행용 코드는 이미 작성되어 있다.
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetListPractice2 {
	public static void main(String[] args) {

		//=====================================================================
		// [문제 1] 결석자 찾기
		//
		// 전체 수강생 명단과, 오늘 출석 체크기에 태그된 기록(중복 태그 포함)이 있다.
		// 출석 기록에 없는 수강생 = 결석자를 찾아 출력하시오.
		//
		// ■ 예상 실행 결과
		//    ===== 문제 1 : 결석자 명단 =====
		//    결석 : park
		//    결석 : hong
		//=====================================================================
		System.out.println("===== 문제 1 : 결석자 명단 =====");

		//전체 수강생 명단을 저장할 가변 배열 생성
		//- 명단은 등록 "순서"가 유지되어야 하므로 List(ArrayList)를 선택한다
		//- <String> 제네릭 : <> 는 of 라고 한다. String 객체만 저장 가능 + 꺼낼 때 다운캐스팅 불필요
		//- 부모 List 자료형의 참조변수에 자식 ArrayList 객체 주소 저장 (업캐스팅)
		List<String> allStudents = new ArrayList<String>();
		allStudents.add("kim");   //0번 칸에 "kim" 저장  -> [kim]
		allStudents.add("lee");   //1번 칸에 "lee" 저장  -> [kim, lee]
		allStudents.add("park");  //2번 칸에 "park" 저장 -> [kim, lee, park]
		allStudents.add("choi");  //3번 칸에 "choi" 저장 -> [kim, lee, park, choi]
		allStudents.add("hong");  //4번 칸에 "hong" 저장 -> [kim, lee, park, choi, hong]
		/*
		 allStudents (ArrayList) 완성 모습 - add한 순서 그대로 저장됨 (List 특성)
		 [ "kim" ][ "lee" ][ "park" ][ "choi" ][ "hong" ]
		     0        1        2         3         4      index
		*/
		
		//출석 체크기 태그 기록 (여러 번 태그한 사람이 있어 중복 포함)
		//- "찍힌 순서 그대로 + 여러 번 찍은 것도 전부" 남아야 하는 기록이므로
		//  중복을 허용하는 List(ArrayList)를 선택한다
		List<String> tagList = new ArrayList<String>();
		tagList.add("lee");   //0번 칸 -> [lee]
		tagList.add("kim");   //1번 칸 -> [lee, kim]
		tagList.add("lee");   //2번 칸 -> [lee, kim, lee]        ※List라서 중복 저장이 허용됨!
		tagList.add("choi");  //3번 칸 -> [lee, kim, lee, choi]
		tagList.add("kim");   //4번 칸 -> [lee, kim, lee, choi, kim]  ※kim도 중복 저장됨

		//TODO 1-1 : 출석자 확인용 HashSet(변수명 attendSet)을 <String> 제네릭으로 생성하고,
		//           향상된 for문으로 tagList의 아이디들을 전부 add하시오.
		//           (중복 태그는 Set이 알아서 걸러주므로 반환값 확인은 필요 없다)
		
		//- "출석했는가?"를 물어보는 용도이므로 같은 사람이 두 번 있을 필요가 없다
		//  -> 중복 저장을 거부하는 Set(HashSet)을 선택한다
		Set<String>  attendSet = new HashSet<String>();
		
		//향상된 for문 : "tagList  ArrayList배열에서 객체를 0번 칸부터 하나씪 꺼내
		//			   String id 변수에 담아가며 반복해라" 는 뜻
		for(String  id : tagList) { // [lee, kim, lee, choi, kim] 
			
			//꺼낸 아이디 문자열객체를 출석자 확인용 HashSet 배열에 추가 시도
			//- 처음 보는 아이디면 -> 저장되고 true 반환
			//- 이미 있는 아이디면 -> 저장이 거부되고 false 반환
			//- 여기서는 "출석자 명단 완성"만 목적이므로 반환값을 변수에 받지 않아도 된다
			//  (중복 태그는 Set이 알아서 걸러 주기 때문)
			attendSet.add(id);
			/*
			 반복 추적 (5회) - attendSet의 변화
			   1회 "lee"  -> 없음   -> 저장     attendSet = {lee}
			   2회 "kim"  -> 없음   -> 저장     attendSet = {lee, kim}
			   3회 "lee"  -> 이미있음-> 거부!    attendSet = {lee, kim}        (변화 없음)
			   4회 "choi" -> 없음   -> 저장     attendSet = {lee, kim, choi}
			   5회 "kim"  -> 이미있음-> 거부!    attendSet = {lee, kim, choi}  (변화 없음)

			 -> 태그 기록은 5건이지만 attendSet에는 실제 출석자 3명만 남는다
			*/
		}


		//TODO 1-2 : 향상된 for문으로 allStudents를 순서대로 돌면서
		//           attendSet에 없는(contains가 false인) 수강생을
		//           "결석 : 아이디" 형태로 출력하시오.
		//           힌트 : if(  attendSet.contains(id) == false  )
		
		//★ 반복의 "기준"이 이 문제의 핵심이다
		//  - 찾는 것은 "결석자" = 전체 명단에는 있는데 출석 Set에는 없는 사람
		//  - 따라서 반복은 "전체 명단(allStudents)"을 기준으로 돌아야 하고
		//  - List를 기준으로 돌기 때문에 결석자가 명단 순서대로(park -> hong) 출력된다
		//    (attendSet을 기준으로 돌면 "출석자"만 나올 뿐 결석자는 영원히 못 찾는다!)
		for(String id   : allStudents  ) {
			
			//contains 메소드 : attendSet 안에 id와 내용이 같은(equals) 객체가
			//                 저장되어 있으면 true, 없으면 false를 반환
			//                 (저장된 객체 주소번지가 아니라 내용 비교!)
			if(attendSet.contains(id) == false) {
				
				//Set에 없다 = 오늘 한 번도 태그하지 않았다 = 결석
				System.out.println("결석 : " + id);
			}
			/*
			 반복 추적 (5회) - 전체 명단 순서대로 출석 여부 확인
			   1회 "kim"  -> attendSet.contains("kim")  -> true  -> 출석 (출력 없음)
			   2회 "lee"  -> attendSet.contains("lee")  -> true  -> 출석 (출력 없음)
			   3회 "park" -> attendSet.contains("park") -> false -> "결석 : park" 출력
			   4회 "choi" -> attendSet.contains("choi") -> true  -> 출석 (출력 없음)
			   5회 "hong" -> attendSet.contains("hong") -> false -> "결석 : hong" 출력
			*/
			
		}	
		/*
		 
		attendSet (HashSet) = {lee, kim, choi} 
		
	
		 allStudents (ArrayList) 완성 모습 - add한 순서 그대로 저장됨 (List 특성)
		 [ "kim" ][ "lee" ][ "park" ][ "choi" ][ "hong" ]
		     0        1        2         3         4      index
		*/

		//=====================================================================
		// [문제 2] 댓글 금지어 필터
		//
		// 금지어 목록과, 댓글을 단어 단위로 잘라 담은 List가 있다.
		// 댓글의 각 단어가 금지어이면 "**" 로 교체하고, 교체한 개수를 세시오.
		//
		// ■ 예상 실행 결과
		//    ===== 문제 2 : 금지어 필터 =====
		//    필터링된 단어 수 : 2개
		//    필터링 후 댓글 : [이, 강의, **, 진짜, **, 같아요]
		//=====================================================================
		System.out.println("===== 문제 2 : 금지어 필터 =====");

		//금지어 목록 (존재 여부만 빠르게 확인하면 되므로 Set)
		//- 금지어는 순서가 필요 없고 "이 단어가 금지어인가?"라는 존재 확인만 하면 되므로
		//  contains 확인에 특화된 Set(HashSet)을 선택한다
		Set<String> banSet = new HashSet<String>();
		banSet.add("바보");   //금지어 1 등록
		banSet.add("멍청이"); //금지어 2 등록 (이번 댓글에는 없지만 목록에는 존재)
		banSet.add("최악");  //금지어 3 등록
		//banSet = {바보, 멍청이, 최악}
		
		//댓글을 단어 단위로 잘라 순서대로 담은 List (순서가 중요하므로 List)
		//- 댓글은 단어의 "순서"가 곧 문장이므로 순서가 유지되는 List(ArrayList)를 선택한다
		List<String> comment = new ArrayList<String>();
		comment.add("이");     //0번 칸
		comment.add("강의");    //1번 칸
		comment.add("바보");    //2번 칸  <- 금지어!
		comment.add("진짜");    //3번 칸
		comment.add("최악");    //4번 칸  <- 금지어!
		comment.add("같아요");   //5번 칸
		/*
		 comment (ArrayList) 필터링 전 모습
		 [ "이" ][ "강의" ][ "바보" ][ "진짜" ][ "최악" ][ "같아요" ]
		    0        1         2         3      4     5      index
		*/
				
		//교체한 개수를 저장할 변수 (0에서 시작해 금지어를 교체할 때마다 1씩 증가)
		int filteredCount = 0;

		//TODO 2 : 일반 for문(index 사용)으로 comment의 단어를 0번부터 차례로 get하여
		//         그 단어가 banSet에 있으면(contains가 true)
		//         ① comment의 해당 index 칸을 "**" 로 교체하고   힌트: set(i, "**")
		//         ② filteredCount를 1 증가시키시오.
		//         ※ 교체(set)를 하려면 index 번호가 필요하므로
		//           향상된 for문이 아니라 일반 for문을 써야 한다! (Collections04 선택 기준)
		for(int i=0;  i<comment.size();  i++) {
			
			//i=0부터 시작해서 i가 size()인 6이 되는 순간 반복 종료 (i는 0~5까지)
			//조건에 =을 붙여 i <= size()로 쓰면 get(6) 호출 순간
			//IndexOutOfBoundsException 발생! 
			
			//① comment.get(i) : i번 칸에 저장된 단어를 꺼낸다
			//② banSet.contains(꺼낸 단어) : 그 단어가 금지어 목록에 있는지 확인 (내용 비교)
			if(banSet.contains( comment.get(i) )  ) {
				
				//금지어 발견! -> 방금 확인한 "같은 i번 칸"을 "**" 문자열로 교체한다
				//set(int index, E element) : index 칸의 기존 객체를 element로 교체하고
				//                            교체되기 전의 기존 객체를 반환하는 메소드
				//                            (여기서는 반환값을 쓸 일이 없어 받지 않았다)
				comment.set(i, "**");
				
				filteredCount++;  //교체 개수 1증가
			}
			
		}// for
		/*
		 반복 추적 (6회)
		   i=0  get(0)="이"     -> banSet에 없음 -> 통과
		   i=1  get(1)="강의"   -> banSet에 없음 -> 통과
		   i=2  get(2)="바보"   -> banSet에 있음! -> set(2,"**") 교체, filteredCount=1
		   i=3  get(3)="진짜"   -> banSet에 없음 -> 통과
		   i=4  get(4)="최악"   -> banSet에 있음! -> set(4,"**") 교체, filteredCount=2
		   i=5  get(5)="같아요" -> banSet에 없음 -> 통과
		*/
		/*
		 comment (ArrayList) 필터링 후 모습 - 2번, 4번 칸만 교체되고 순서는 그대로!
		 [ "이" ][ "강의" ][ "**" ][ "진짜" ][ "**" ][ "같아요" ]
		    0        1       2        3       4         5      index
		*/
		


		//문제 2 결과 출력 (수정하지 말 것)
		System.out.println("필터링된 단어 수 : " + filteredCount + "개");  //2개
		System.out.println("필터링 후 댓글 : " + comment);
						  //필터링 후 댓글 : [이, 강의, **, 진짜, **, 같아요]

		//=====================================================================
		// [문제 3] 두 반 수강생 비교
		//
		// 자바반과 파이썬반의 수강생 명단이 있다. (같은 사람이 두 반을 모두 들을 수 있다)
		// (1) 두 반을 모두 수강하는 사람(교집합)을 신청 순서대로 출력하시오.
		// (2) 한 반이라도 수강하는 전체 인원 수(합집합의 크기)를 출력하시오.
		//
		// ■ 예상 실행 결과
		//    ===== 문제 3 : 두 반 수강생 비교 =====
		//    두 반 모두 수강 : lee
		//    두 반 모두 수강 : choi
		//    전체 수강 인원(중복 제외) : 6명
		//=====================================================================
		System.out.println("===== 문제 3 : 두 반 수강생 비교 =====");

		//자바반 명단 (신청 순서 유지가 필요하므로 List)
		List<String> javaClass = new ArrayList<String>();
		javaClass.add("kim");  //0번 칸
		javaClass.add("lee");  //1번 칸  <- 파이썬반도 수강
		javaClass.add("park"); //2번 칸
		javaClass.add("choi"); //3번 칸  <- 파이썬반도 수강

		//파이썬반 명단 (신청 순서 유지가 필요하므로 List)
		List<String> pythonClass = new ArrayList<String>();
		pythonClass.add("lee");  //0번 칸  <- 자바반도 수강
		pythonClass.add("jung"); //1번 칸
		pythonClass.add("choi"); //2번 칸  <- 자바반도 수강
		pythonClass.add("hong"); //3번 칸

		//TODO 3-1 : 파이썬반 명단을 담은 HashSet(변수명 pythonSet)을 만들어
		//           pythonClass의 아이디들을 전부 add하시오.
		//           (교집합 확인 시 contains를 쓰기 위한 준비)
		Set<String> pythonSet = new HashSet<String>();
		
		//파이썬 반 ArrayList 배열에 저장된 아이디 문자열객체를 하나씩 꺼내어 HashSet에 전부 저장
		for(String id  : pythonClass  ) {
			pythonSet.add(id);   
		}
		//pythonSet = {lee, jung, choi, hong}


		//TODO 3-2 : 향상된 for문으로 javaClass를 순서대로 돌면서
		//           pythonSet에도 있는(contains가 true인) 사람을
		//           "두 반 모두 수강 : 아이디" 형태로 출력하시오. (= 교집합)
		for(String id  : javaClass ) {
			
			//자바반 수강생이 파이썬반 HashSet에도 있으면 == 두 반 모두 수강
			if( pythonSet.contains(id) ) {
				System.out.println("두 반 모두 수강 : " + id);
			}
			/*
			 반복 추적 (4회)
			   1회 "kim"  -> pythonSet에 없음 -> 통과
			   2회 "lee"  -> pythonSet에 있음! -> "두 반 모두 수강 : lee" 출력
			   3회 "park" -> pythonSet에 없음 -> 통과
			   4회 "choi" -> pythonSet에 있음! -> "두 반 모두 수강 : choi" 출력
			*/
		}

		//TODO 3-3 : 합집합용 HashSet(변수명 unionSet)을 만들어
		//           javaClass와 pythonClass의 아이디들을 전부 add한 후
		//           "전체 수강 인원(중복 제외) : N명" 형태로 출력하시오.
		//           (두 반 모두 듣는 lee, choi는 Set이 알아서 한 번만 센다)
		Set<String>  unionSet = new HashSet<String>();
		
		//1) 자바반 4명을 전부 add
		for(String id  : javaClass) {
			
			unionSet.add(id);
			//kim(저장), lee(저장), park(저장), choi(저장)
			//-> unionSet = {kim, lee, park, choi}
		}
		//2) 파이썬 반 4명을 전부 add
		for(String id  : pythonClass ) {
			
			unionSet.add(id);
			//lee(이미 있음->거부!), jung(저장), choi(이미 있음->거부!), hong(저장)
			//-> unionSet = {kim, lee, park, choi, jung, hong}
		}
		/*
		 unionSet 완성 모습 - 8건을 add했지만 중복 2건(lee, choi)이 거부되어 6명
		 { kim, lee, park, choi, jung, hong }
		*/
		
		//합집합 HashSet 에 저장된 개수 = 중복 제외한 전체 수강 인원
		System.out.println("전체 수강 인원(중복 제외) : " + unionSet.size()  + "명");
						  //전체 수강 인원(중복 제외) : 6명

		/*
		결론 : Set + List 조합의 3대 활용 공식
		  공식 1. 존재 확인   : 기록을 Set에 담아 두고 contains로 묻는다 (문제 1, 2)
		  공식 2. 순서 유지   : 출력 순서가 중요하면 반복의 기준은 List로 잡는다 (문제 1, 3)
		  공식 3. 교집합/합집합 : 교집합 = 한쪽을 돌며 다른 쪽 Set에 contains
		                      합집합 = 하나의 Set에 양쪽 전부 add (문제 3)
		 */
	}
}






