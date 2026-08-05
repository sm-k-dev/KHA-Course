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

		//전체 수강생 명단 (순서 있는 명단이므로 List)
		List<String> allStudents = new ArrayList<String>();
		allStudents.add("kim");
		allStudents.add("lee");
		allStudents.add("park");
		allStudents.add("choi");
		allStudents.add("hong");

		//출석 체크기 태그 기록 (여러 번 태그한 사람이 있어 중복 포함)
		List<String> tagList = new ArrayList<String>();
		tagList.add("lee");
		tagList.add("kim");
		tagList.add("lee");   //lee 중복 태그
		tagList.add("choi");
		tagList.add("kim");   //kim 중복 태그

		//TODO 1-1 : 출석자 확인용 HashSet(변수명 attendSet)을 <String> 제네릭으로 생성하고,
		//           향상된 for문으로 tagList의 아이디들을 전부 add하시오.
		//           (중복 태그는 Set이 알아서 걸러주므로 반환값 확인은 필요 없다)
		Set<String> attendSet = new HashSet<String>();
		
		for ( String id : tagList ) {
			attendSet.add(id);
		}

		//TODO 1-2 : 향상된 for문으로 allStudents를 순서대로 돌면서
		//           attendSet에 없는(contains가 false인) 수강생을
		//           "결석 : 아이디" 형태로 출력하시오.
		//           힌트 : if(  attendSet.contains(id) == false  )
		for ( String id : allStudents ) {
			if ( attendSet.contains(id) == false ) {
				System.out.println("결석: " + id);
			}
		}


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
		Set<String> banSet = new HashSet<String>();
		banSet.add("바보");
		banSet.add("멍청이");
		banSet.add("최악");

		//댓글을 단어 단위로 잘라 순서대로 담은 List (순서가 중요하므로 List)
		List<String> comment = new ArrayList<String>();
		comment.add("이");
		comment.add("강의");
		comment.add("바보");    //금지어!
		comment.add("진짜");
		comment.add("최악");    //금지어!
		comment.add("같아요");

		//교체한 개수를 저장할 변수
		int filteredCount = 0;

		//TODO 2 : 일반 for문(index 사용)으로 comment의 단어를 0번부터 차례로 get하여
		//         그 단어가 banSet에 있으면(contains가 true)
		//         ① comment의 해당 index 칸을 "**" 로 교체하고   힌트: set(i, "**")
		//         ② filteredCount를 1 증가시키시오.
		//         ※ 교체(set)를 하려면 index 번호가 필요하므로
		//           향상된 for문이 아니라 일반 for문을 써야 한다! (Collections04 선택 기준)
		for ( int i = 0 ; i < comment.size() ; i++ ) {
			
			if ( banSet.contains( comment.get(i) ) ) {
				comment.set(i, "**");
				filteredCount++;
			}
		}


		//문제 2 결과 출력 (수정하지 말 것)
		System.out.println("필터링된 단어 수 : " + filteredCount + "개");
		System.out.println("필터링 후 댓글 : " + comment);

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

		//자바반 명단
		List<String> javaClass = new ArrayList<String>();
		javaClass.add("kim");
		javaClass.add("lee");
		javaClass.add("park");
		javaClass.add("choi");

		//파이썬반 명단
		List<String> pythonClass = new ArrayList<String>();
		pythonClass.add("lee");
		pythonClass.add("jung");
		pythonClass.add("choi");
		pythonClass.add("hong");

		//TODO 3-1 : 파이썬반 명단을 담은 HashSet(변수명 pythonSet)을 만들어
		//           pythonClass의 아이디들을 전부 add하시오.
		//           (교집합 확인 시 contains를 쓰기 위한 준비)
		Set<String>	pythonSet = new HashSet<String>();
		for ( String n : pythonClass ) {
			pythonSet.add(n);
		}


		//TODO 3-2 : 향상된 for문으로 javaClass를 순서대로 돌면서
		//           pythonSet에도 있는(contains가 true인) 사람을
		//           "두 반 모두 수강 : 아이디" 형태로 출력하시오. (= 교집합)
		for ( String n : javaClass ) {
			if ( pythonSet.contains(n) ) {
				System.out.println("두 반 모두 수강: " + n );
			}
		}


		//TODO 3-3 : 합집합용 HashSet(변수명 unionSet)을 만들어
		//           javaClass와 pythonClass의 아이디들을 전부 add한 후
		//           "전체 수강 인원(중복 제외) : N명" 형태로 출력하시오.
		//           (두 반 모두 듣는 lee, choi는 Set이 알아서 한 번만 센다)
		Set<String> unionSet = new HashSet<String>();
		
		

	}
}
