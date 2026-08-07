import java.util.Map;      //Map인터페이스
import java.util.Set;      //Set인터페이스
import java.util.HashMap;  //Map부모인터페이스를 구현한 자식 HashMap클래스
import java.util.Iterator; //Iterator인터페이스

//===================================================================
//[응용 문제 2] HashMap 에 같은 key 로 다시 저장하면 어떻게 될까? + 전체 출력
//===================================================================
//과목 이름을 key, 시험 점수를 value 로 저장하는 문제입니다.
//
//[요구사항]
// 1단계 : key 는 String, value 는 Integer 를 저장하는
//         HashMap 객체를 생성해서 Map 인터페이스 타입 참조변수 map 에 저장하세요.
//
// 2단계 : put 메소드를 사용해서 아래 4번의 저장을 순서대로 실행하세요.
//           key          value
//          "자바"    ,   70
//          "DB"      ,   85
//          "HTML"    ,   90
//          "자바"    ,   95    <- 같은 key "자바" 로 다시 저장!
//
// 3단계 : size 메소드를 사용해서 아래 문장을 출력하세요.
//         HashMap에 저장된 key 총 갯수 얻기 : 3
//         (4번 put 했는데 왜 3개인지 주석으로 이유를 적어보세요.)
//
// 4단계 : get 메소드를 사용해서 key "자바" 와 연결되어 저장된
//         value 를 꺼내와 아래 문장을 출력하세요.
//         자바 : 95
//         (70 이 아니라 95 가 출력되는 이유를 주석으로 적어보세요.)
//
// 5단계 : keySet 메소드로 Set 배열을 반환 받고
//         iterator 메소드로 Iterator 객체를 반환 받은 후
//         while 반복문 + hasNext + next 메소드를 사용해서
//         저장된 모든 (key : value) 를 아래 형태로 출력하세요.
//         자바 : 95
//         DB : 85
//         HTML : 90
//         (출력 순서는 저장한 순서와 다를 수 있습니다.)
//===================================================================

public class HashMapEx2 {
	public static void main(String[] args) {

		//1단계 : Map부모 인터페이스를 구현한 자식 HashMap클래스의 배열객체 메모리 생성
		Map<String, Integer> map = new HashMap<String, Integer>();
		    //key ,  value

		//2단계 : put 메소드로 4번 저장 (마지막은 같은 key "자바" 로 저장)
		//			  key   ,  value
		map.put("자바", 70);
		map.put("DB", 85);
		map.put("HTML", 90);
		map.put("자바", 95); //<- 같은 key "자바" 에 대한 value를 추가하면
		                     //   기존에 map.put("자바", 70); 이용해서 저장했던 행이 제거 되고
		                     //   map.put("자바", 95); 이용해서 저장한 행이 HashMap에 추가되어 저장됨
		/*
		key는 중복저장할수 없지만 value는 중복 저장할수 있다.
		기존에 저장된 key와 동일한 key로 값을 저장하면
		기존의 값은 없어지고 새로운 값으로 대치된다.

		[Heap]
		┌──────────────────────────────────────────────┐
		│ HashMap                                      │
		│──────────────────────────────────────────────│
		│ table                                        │
		│   ┌────────┐ ┌────────┐ ┌────────┐           │
		│   │ Entry  │ │ Entry  │ │ Entry  │           │
		│   │────────│ │────────│ │────────│           │
		│   │ key    │ │ key    │ │ key    │           │
		│   │"자바"   │ │"DB"    │ │"HTML"  │           │
		│   │ value  │ │ value  │ │ value  │           │
		│   │  95    │ │  85    │ │  90    │           │
		│   └────────┘ └────────┘ └────────┘           │
		│      ▲                                       │
		│      └ 70 이 아니라 95 (덮어쓰기 됨)              │
		│ size = 3                                     │
		└──────────────────────────────────────────────┘
		*/

		//3단계 : size 메소드로 저장된 key 총 갯수 출력
		System.out.println("HashMap에 저장된 key 총 갯수 얻기 : " + map.size());
		//				   HashMap에 저장된 key 총 갯수 얻기 : 3
		/*
		put 메소드를 4번 호출했지만 size 는 3 입니다.
		이유 : 4번째 map.put("자바", 95); 는
		       새로운 행을 추가한 것이 아니라
		       이미 저장되어 있던 key "자바" 행의 value 를
		       70 → 95 로 바꾼 것이기 때문입니다.
		*/

		//4단계 : get 메소드로 key "자바" 의 value 꺼내와 출력
		String key = "자바";

		int value = map.get(key);
		    //95
		/*
		70 이 아니라 95 가 저장되는 이유 :
		map.put("자바", 95); 를 실행한 순간
		key "자바" 와 연결된 value 는 95 로 대치되었고
		70 은 HashMap 안에 더 이상 존재하지 않기 때문입니다.
		*/

		System.out.println(key + " : " + value);
		//				   자바   :   95

		//5단계 : keySet 메소드 + Iterator 객체로 전체 (key : value) 출력
		/*
		Set<K>	keySet() : HashMap배열에 저장된 모든 key객체들을 꺼내서
		                   Set부모인터페이스를 구현한 자식배열HashSet에 담아 리턴
		*/
		Set<String> keySet = map.keySet();
		/*
				자식배열HashSet
							-----         ------
								|"자바"    | <- key
								|"DB"     | <- key
								|"HTML"   | <- key
								|_________|
		*/

		//HashSet 배열에 저장되어 있던 모든 key들을 꺼내어서
		//Iterator부모인터페이스를 구현한 자식 Itr배열메모리에 담아 Itr배열 주소 자체 반환
		Iterator<String> keyIterator = keySet.iterator();
		//["자바", "DB", "HTML"] <- Itr배열 모습 (순서는 다를 수 있음)

		//hasNext() → 아직 꺼내지 않은 key 가 남아 있으면 true 반환
		//          → 전부 다 꺼냈으면 false 반환 → 반복 종료
		while( keyIterator.hasNext() ) {

			//key 들만 차례로 반복해서 Itr배열에서 얻습니다.
			String k = keyIterator.next(); //"자바"
			                               //"DB"
			                               //"HTML"

			//key를 이용해 HashMap 표의 공간에 key와 같이 저장된 value 얻기
			Integer v = map.get(k); //new Integer(95) 얻음
			                        //new Integer(85) 얻음
			                        //new Integer(90) 얻음

			System.out.println(k + " : " + v);
			/*
							자바 : 95
							DB : 85
							HTML : 90
							(출력 순서는 저장한 순서와 다를 수 있습니다.)
			*/
		}//------- while

		/*
		[전체 실행 결과 예시]
		HashMap에 저장된 key 총 갯수 얻기 : 3
		자바 : 95
		자바 : 95
		DB : 85
		HTML : 90
		*/


	}//----- main
}//--- class







