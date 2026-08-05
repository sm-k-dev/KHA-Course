/*
	[연습문제] HashSet + ArrayList 응용 : 설문 제출 기록에서 중복 제출 걸러내기

	■ 상황
	   수업 만족도 설문을 받았는데, 같은 수강생이 실수로 여러 번 제출할 수 있었다.
	   제출된 순서 그대로의 기록이 ArrayList에 저장되어 있다. (중복 포함)

	   제출 기록 : "kim", "lee", "park", "kim", "choi", "lee", "kim"
	                (kim은 3번, lee는 2번 제출했다)

	■ 요구사항
	   1) 제출 기록(ArrayList)을 순서대로 꺼내 HashSet에 옮겨 담는다.
	   2) 옮기는 과정에서 add메소드가 false를 반환하는 순간이 "중복 제출"이므로
	      그때마다  "중복 제출 발견 : 아이디"  를 출력한다.
	   3) 다 옮긴 후 실제 참여 인원 수(중복 제거된 수)를 출력한다.
	   4) 최종 참여자 명단을 향상된 for문으로 출력한다.

	■ 예상 실행 결과 (참여자 명단의 출력 순서는 실행 환경에 따라 다를 수 있다 - HashSet 특성!)

	   총 제출 건수 : 7건
	   중복 제출 발견 : kim
	   중복 제출 발견 : lee
	   중복 제출 발견 : kim
	   실제 참여 인원 : 4명
	   ===== 참여자 명단 =====
	   kim
	   choi
	   park
	   lee

	■ 힌트
	   - Collections01 : HashSet의 add는 성공 시 true, 중복이라 거부되면 false를 반환한다.
	   - Collections06 : 제네릭 <String>을 쓰면 꺼낼 때 다운캐스팅이 필요 없다.
	   - 향상된 for문 : for( String id : 배열 ) { ... }
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetListPractice {
	public static void main(String[] args) {

		//제출 기록 : 제출된 순서 그대로, 중복을 허용해서 저장해야 하므로 ArrayList를 사용 (List 특성)
		List<String> submitList = new ArrayList<String>();
		submitList.add("kim");
		submitList.add("lee");
		submitList.add("park");
		submitList.add("kim");   //kim 2번째 제출 (중복)
		submitList.add("choi");
		submitList.add("lee");   //lee 2번째 제출 (중복)
		submitList.add("kim");   //kim 3번째 제출 (중복)

		//총 제출 건수 출력 (중복 포함이므로 7건)
		System.out.println("총 제출 건수 : " + submitList.size() + "건");

		//참여자 명단 : 같은 아이디는 한 번만 세어야 하므로 HashSet을 사용 (Set 특성)
		Set<String> memberSet = new HashSet<String>();

		//TODO 1 : 향상된 for문으로 submitList의 아이디를 순서대로 하나씩 꺼내
		//         memberSet에 add하시오.
		
		for ( String id : submitList ) {
			boolean result = memberSet.add(id);
		
		//TODO 2 : add의 반환값(boolean)을 변수에 받아서, false이면
		//         "중복 제출 발견 : 아이디" 를 출력하시오.
			if ( result ) {
				System.out.println("중복 제출 발견: " + id);
			}

		}

		//TODO 3 : memberSet의 size()를 이용해
		//         "실제 참여 인원 : N명" 형태로 출력하시오.

		System.out.println("실제 참여 인원: " + memberSet.size() + "명");

		System.out.println("===== 참여자 명단 =====");

		//TODO 4 : 향상된 for문으로 memberSet의 아이디들을 한 줄에 하나씩 출력하시오.

		for ( String id : memberSet ) {
			System.out.println( id );
		}

	}
}
