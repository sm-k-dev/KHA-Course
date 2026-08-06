/*
	[연습문제 - 난이도 최상] HashSet + ArrayList 종합 : 전자 투표 개표 시스템

	■ 상황
	   반장 선거 전자 투표가 끝났다. 투표 기록은 두 개의 List에 나뉘어 저장되어 있다.

	   voterList  (누가 투표했나)   : "kim", "lee", "kim", "park", "choi", "lee", "hong", "jung"
	   choiceList (누구를 찍었나)   :  "A",   "B",   "B",   "B",    "A",    "A",   "A",    "A"

	   ★ 두 List는 같은 index끼리 한 장의 표다!
	     예) 0번 표 = kim이 A를 찍음 / 2번 표 = kim이 B를 찍음(두 번째 투표!)

	■ 개표 규칙
	   규칙 1. 같은 사람의 두 번째 이후 투표는 무효표다.
	           무효표는 "무효표(중복 투표) : 아이디" 를 출력하고 집계하지 않는다.
	   규칙 2. 유효표만 후보별로 집계한다.
	           단, 후보 명단은 미리 주어지지 않는다! 개표 중에 처음 나온 후보는
	           그때 후보 목록에 등록하면서 1표부터 세기 시작한다.
	   규칙 3. 후보별 득표수를 "후보 : N표" 형태로, 후보가 처음 등장한 순서대로 출력한다.
	   규칙 4. 최다 득표 후보를 "당선 : 후보 (N표)" 형태로 출력한다.

	■ 핵심 설계 : Map 없이 집계하는 방법 = "병렬 리스트" 기법
	   candidateList : [ "A" ][ "B" ]      <- 후보 이름
	   countList     : [  4  ][  2  ]      <- 같은 index 칸이 그 후보의 득표수!
	   -> "B의 득표수"가 궁금하면? candidateList.indexOf("B")로 위치(1)를 찾고
	      countList.get(1)을 보면 된다. 두 List를 같은 index로 짝지어 쓰는 기법이다.

	■ 예상 실행 결과

	   무효표(중복 투표) : kim
	   무효표(중복 투표) : lee
	   ===== 개표 결과 =====
	   A : 4표
	   B : 2표
	   당선 : A (4표)

	■ 힌트 모음
	   - 투표자와 선택을 "같은 i번째"로 꺼내야 하므로 1단계는 일반 for문이 필수다
	   - indexOf(객체) : 있으면 위치 반환, 없으면 -1 반환 (Collections02)
	   - countList.set( idx, countList.get(idx) + 1 )  <- 득표수 1 올리기
	     (get이 꺼낸 Integer가 오토 언박싱되어 +1 계산되고, 결과가 다시 오토 박싱되어 저장된다)
	   - 최댓값 찾기 : maxIdx를 0으로 시작해서, 더 큰 득표수를 만나면 maxIdx를 갱신
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VotePractice {
	public static void main(String[] args) {

		//병렬 리스트란?
		//- List 두 개를 만들어 놓고, "같은 번호 칸끼리 한 묶음"으로 약속하고 쓰는 방식이다.
		//- 아래에서는 voterList의 3번 칸(park)과 choiceList의 3번 칸(B)이 합쳐져
		//  "park가 B를 찍었다"라는 한 장의 표가 된다.
		//- 표는 "들어온 순서"가 유지되어야 하고 같은 후보(A, B)가 여러 번 나오므로
		//  중복을 거부하는 Set이 아니라 List를 사용해야 한다.
		
		//투표 기록 : 같은 index끼리 한 장의 표 (병렬 리스트)
		List<String> voterList = new ArrayList<String>();
		List<String> choiceList = new ArrayList<String>();

		voterList.add("kim");   choiceList.add("A");  //0번 표 : kim -> A
		voterList.add("lee");   choiceList.add("B");  //1번 표 : lee -> B
		voterList.add("kim");   choiceList.add("B");  //2번 표 : kim -> B  (kim의 2번째 투표 = 무효!)
		voterList.add("park");  choiceList.add("B");  //3번 표 : park -> B
		voterList.add("choi");  choiceList.add("A");  //4번 표 : choi -> A
		voterList.add("lee");   choiceList.add("A");  //5번 표 : lee -> A  (lee의 2번째 투표 = 무효!)
		voterList.add("hong");  choiceList.add("A");  //6번 표 : hong -> A
		voterList.add("jung");  choiceList.add("A");  //7번 표 : jung -> A

		/*
		 투표 기록 완성 모습 (같은 index = 한 장의 표)
		 voterList  : [ kim ][ lee ][ kim ][ park ][ choi ][ lee ][ hong ][ jung ]
		 choiceList : [  A  ][  B  ][  B  ][  B   ][  A   ][  A  ][  A   ][  A   ]
		                 0      1      2      3       4       5      6       7    index

		 -> 표가 8장이므로 voterList.size()도 8, choiceList.size()도 8이다.
		 -> 칸 번호는 0~7까지 존재한다. (8번 칸은 없다! get(8)을 하면 오류가 난다)
		*/
		
		//이미 투표한 사람인지 확인하기 위한 Set (중복 투표 판별용)
		//- 필요한 것은 "이 이름이 이미 들어 있는가?"라는 존재 확인뿐이다.
		//- Set은 같은 값을 두 번 저장할 수 없으므로, 이 성질을 그대로 중복 투표 검사에 이용한다.
		Set<String> votedSet = new HashSet<String>();

		//유효표의 선택(후보)만 순서대로 모아 둘 List
		//- 예 : 최종적으로 [A, B, B, A, A, A] 처럼 후보 이름이 표 개수만큼 쌓인다.
		//- 같은 후보가 여러 번 들어가야 하고(득표 1개 = 값 1개) 순서도 유지해야 하므로 List를 사용.
		List<String> validList = new ArrayList<String>();

		//=====================================================================
		// 1단계 : 무효표 걸러내기
		//=====================================================================

		//TODO 1 : 일반 for문으로 0번 표부터 마지막 표까지 반복하면서
		//         - voterList.get(i) 로 투표자를, choiceList.get(i) 로 선택을 꺼낸다
		//         - 투표자를 votedSet에 add한 반환값이 false이면 (= 이미 투표한 사람)
		//              "무효표(중복 투표) : 투표자" 출력
		//         - true이면 (= 첫 투표) 선택(후보)을 validList에 add한다
		//
		//         ※ 왜 향상된 for문을 못 쓰는가? -> 투표자와 선택을 "같은 i"로
		//           짝지어 꺼내야 하는데, 향상된 for문은 한 번에 한 List밖에 못 돌기 때문
		
		//for문 구조 : for( ①시작값 ; ②반복 조건 ; ③한 바퀴 끝날 때마다 실행 )
		//  ① int i = 0            -> i를 0으로 만들고 시작 (0번 표부터 읽는다)
		//  ② i < voterList.size() -> i가 표 개수(8)보다 작은 동안만 반복 (i가 0~7일 때만 실행)
		//  ③ i++                  -> 한 바퀴 돌 때마다 i를 1씩 증가 (다음 표로 이동)
		
		for ( int i = 0 ; i < voterList.size() ; i++ ) {
			
			//i번 표의 투표자와 선택을 "같은 index"로 꺼내 변수에 담는다
			String voter = voterList.get(i);   //i번 표를 낸 사람 (예: i=0이면 "kim")
			String choice = choiceList.get(i); //i번 표에 적힌 후보(예: i=0이면 "A")
			
			//투표자 이름을 명단 Set에 추가 "시도" 한다
			//★ 핵심 : Set의 add는 성공/실패 결과를 boolean(true/false)으로 알려 준다!
			//- 처음 투표하는 사람이면 -> Set에 없던 이름 -> 저장 성공 -> true 반환
			//- 이미 투표한 사람이면  -> Set에 있는 이름 -> 저장 거부 -> false 반환
			//즉 add 한 번으로 "저장"과 "중복 검사"가 동시에 끝난다.
			
			if ( votedSet.add( voterList.get(i) ) ) {
				validList.add( choiceList.get(i) );
			} else {
				System.out.println( "무효표(중복 투표): " + voterList.get(i) );
			}
			
			/*
			 반복 추적 (8회) : 표를 앞에서부터 한 장씩 검사한 결과
			   i  투표자   votedSet.add   처리                    validList 변화
			   ─────────────────────────────────────────────────────────────────
			   0  kim      true           유효 -> A 추가           [A]
			   1  lee      true           유효 -> B 추가           [A, B]
			   2  kim      false          무효! "무효표...kim"     [A, B]        (변화 없음)
			   3  park     true           유효 -> B 추가           [A, B, B]
			   4  choi     true           유효 -> A 추가           [A, B, B, A]
			   5  lee      false          무효! "무효표...lee"     [A, B, B, A]  (변화 없음)
			   6  hong     true           유효 -> A 추가           [A, B, B, A, A]
			   7  jung     true           유효 -> A 추가           [A, B, B, A, A, A]

			 -> 8표 중 무효 2표, 유효표 6장 : validList 안에 A가 4개, B가 2개 섞여 있는 상태
			 -> 이 시점의 votedSet : [kim, lee, park, choi, hong, jung] (순서는 보장되지 않음)
			*/
		}

		//=====================================================================
		// 2단계 : 후보별 득표 집계 (병렬 리스트)
		//=====================================================================
		
		//집계용 병렬 리스트 : 후보 이름 목록과 득표수 목록 (같은 index끼리 짝!)
		//- candidateList의 i번 칸 후보가 받은 표 수 = countList의 i번 칸 값
		//- 예 : candidateList가 [A, B]이고 countList가 [4, 2]이면 "A는 4표, B는 2표"라는 뜻
		//- countList의 타입이 <Integer>인 이유 : List의 <> 안에는 클래스만 쓸 수 있어서
		//  기본형 int 대신 int를 감싼 클래스인 Integer를 써야 한다. (박싱/언박싱과 연결되는 부분)
		//후보 이름 목록과 득표수 목록 (같은 index끼리 짝!)
		List<String>  candidateList = new ArrayList<String>();
		List<Integer> countList     = new ArrayList<Integer>();

		//TODO 2 : 향상된 for문으로 validList의 후보를 하나씩 꺼내
		//         - candidateList.indexOf(후보) 결과를 int 변수 idx에 받는다
		//         - idx가 -1이면  (= 처음 등장한 후보)
		//              candidateList에 후보를 add하고, countList에 1을 add한다 (1표부터 시작)
		//         - idx가 -1이 아니면 (= 이미 등록된 후보)
		//              countList의 idx 칸 값을 1 올린다
		//              힌트 : countList.set( idx, countList.get(idx) + 1 );
		for ( String c : validList ) {
			
			//지금 꺼낸 후보 c가 이미 후보 목록에 등록되어 있는지 "위치"를 검색한다
			//- indexOf : 0번 칸부터 차례로 내용을 비교(equals)해서
			//            찾으면 그 칸 번호를, 끝까지 못 찾으면 -1을 반환 (Collections02)
			//- 즉 idx가 -1이면 "처음 보는 후보", -1이 아니면 "idx번 칸에 이미 있는 후보"
			
			int idx = candidateList.indexOf(c);
			
			if ( idx == -1 ) { //처음 등장한 후보
				candidateList.add(c);
				countList.add(1);
				//같은 위치(끝)에 득표수 1 등록 <- 오토 박싱되어 Integer로 저장
                //(오토 박싱 : int 값 1을 자바가 자동으로 Integer 객체로 바꿔서 넣는 것)
                //★ 두 List에 "동시에" add하므로 index 짝이 유지된다!
                //  예 : A가 2번 칸에 등록되면 A의 득표수도 반드시 2번 칸에 생긴다.
			} else { //이미 등록된 후보 -> countList의 idx번 칸 값을 1올린다.
				//set( 칸 번호, 새 값) =  그 칸의 기존 값을 새값으로 교체하는 메서드 (add처럼 추가가 아님!)
				//결과적으로 "현재 득표수를 꺼내 1 더한 값으로 갈아끼운다"는 한 줄이 된다.
				countList.set(idx, countList.get(idx) + 1 );
			}
		}
		/*
		 반복 추적 (유효표 6장 : A, B, B, A, A, A 순서)
		   후보  indexOf   처리                        candidateList / countList
		   ──────────────────────────────────────────────────────────────────
		   A     -1        신규 등록 (A, 1표)            [A]    / [1]
		   B     -1        신규 등록 (B, 1표)            [A,B]  / [1,1]
		   B      1        countList.set(1, 1+1)       [A,B]  / [1,2]
		   A      0        countList.set(0, 1+1)       [A,B]  / [2,2]
		   A      0        countList.set(0, 2+1)       [A,B]  / [3,2]
		   A      0        countList.set(0, 3+1)       [A,B]  / [4,2]

		 -> 집계 완료 : candidateList=[A, B] / countList=[4, 2]
		    같은 index끼리 읽으면 : 0번 짝 = "A는 4표", 1번 짝 = "B는 2표"
		    (후보가 "처음 등장한 순서"대로 등록되므로 출력 순서도 A -> B 로 보장된다)
		*/


		//=====================================================================
		// 3단계 : 개표 결과 출력
		//=====================================================================
		System.out.println("===== 개표 결과 =====");

		//TODO 3 : 일반 for문으로 candidateList와 countList를 같은 i로 짝지어
		//         "후보 : N표" 형태로 출력하시오.

		for ( int i = 0 ; i < candidateList.size() ; i++ ) {
			System.out.println( candidateList.get(i) + " : " + countList.get(i) + "표");
		}


		//=====================================================================
		// 4단계 : 당선자 찾기 (최다 득표)
		//=====================================================================

		//TODO 4 : 최다 득표 후보의 "위치"를 저장할 변수 maxIdx를 0으로 시작해서
		//         일반 for문(i=1부터)으로 countList를 돌며
		//         countList.get(i)가 countList.get(maxIdx)보다 크면 maxIdx를 i로 갱신하고,
		//         반복이 끝나면 "당선 : 후보 (N표)" 형태로 출력하시오.

		int maxIdx = 0;
		//일단 0번 후보가 최다 득표라고 가정하고 시작
		//(비교 기준이 하나는 있어야 하므로 첫 번째 칸을 임시 1등으로 둔다)
		
		//1번 후보 부터 끝까지 임시 1등과 비교 
		for ( int i = 1 ; i < countList.size() ; i++ ) {
			
			//i번 후보의 득표수가 "지금까지의 1등(maxIdx번 칸)" 득표수보다 크면
			//(get이 반환한 Integer 두 개가 오토 언박싱되어 int끼리 > 비교된다)
			maxIdx = countList.get(i) > countList.get(maxIdx) ? i : maxIdx;
			
			/*
			 반복 추적 (후보 2명이므로 1회)
			   i=1 : countList.get(1)=2 > countList.get(maxIdx=0)=4 ? -> 거짓 -> maxIdx는 0 유지
			 -> for문 종료 시점의 maxIdx = 0 (후보 A의 칸 번호) = 진짜 1등의 위치 확정
			*/
		}
		
		System.out.println("당선: " + candidateList.get(maxIdx) + " (" + countList.get(maxIdx) + "표)");
		
		/*
		[전체 실행 결과]
		  무효표(중복 투표) : kim
		  무효표(중복 투표) : lee
		  ===== 개표 결과 =====
		  A : 4표
		  B : 2표
		  당선 : A (4표)

		결론 : 이 문제의 핵심 설계 3가지
		  1. 병렬 리스트 = 두 List를 "같은 index 한 세트"로 다루는 기법.
		     짝을 맞춰야 하는 구간(1단계 표 읽기, 3단계 출력, 4단계 발표)은
		     반드시 일반 for문 + get(i)를 쓴다. (향상된 for문은 index가 없어서 불가능)
		  2. 집계 = indexOf로 "등록 여부 분기" : -1이면 두 List에 동시 add(짝 생성),
		     아니면 set(idx, get(idx)+1)로 값만 교체(짝 유지).
		  3. 최댓값 탐색 = 값이 아니라 "위치(maxIdx)"를 기억하면
		     병렬 리스트의 모든 정보(이름+표수)를 한 번에 꺼낼 수 있다.

		  ※ List와 Set 선택 기준 요약
		     - 순서 유지 + 중복 허용이 필요하다  -> List  (투표 기록, 유효표, 집계)
		     - "이미 있는가?" 존재 확인만 필요하다 -> Set   (투표자 명단)

		  ※ 수업 연결 : 다음에 HashMap<String, Integer>를 배우면
		     2단계 병렬 리스트 전체가 몇 줄로 줄어든다.
		     "왜 Map이 필요한가"를 몸으로 느끼게 하는 것이 이 문제의 숨은 목적이다.
		 */

	}
}
