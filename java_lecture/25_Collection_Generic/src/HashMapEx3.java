import java.util.Map;       //Map인터페이스
import java.util.Set;       //Set인터페이스
import java.util.HashMap;   //Map부모인터페이스를 구현한 자식 HashMap클래스
import java.util.Iterator;  //Iterator인터페이스
import java.util.Map.Entry; //Map인터페이스 내부에 만들어져 있는 중첩(내부) Entry인터페이스

//===================================================================
//[응용 문제 3] entrySet 으로 전체 출력 + remove 로 삭제 + clear 로 전체 삭제
//===================================================================
//회원 이름을 key, 적립 포인트를 value 로 저장하는 문제입니다.
//
//[요구사항]
// 1단계 : key 는 String, value 는 Integer 를 저장하는
//         HashMap 객체를 생성해서 Map 인터페이스 타입 참조변수 map 에 저장하세요.
//
// 2단계 : put 메소드를 사용해서 아래 3쌍의 (key-value) 데이터를 저장하세요.
//           key          value
//          "김회원"   ,   1000
//          "이회원"   ,   2000
//          "박회원"   ,   3000
//
// 3단계 : entrySet 메소드로 Set 배열을 반환 받고
//         iterator 메소드로 Iterator 객체를 반환 받은 후
//         while 반복문 안에서 Entry 객체를 하나씩 꺼내서
//         getKey 메소드와 getValue 메소드를 사용해
//         저장된 모든 (key : value) 를 아래 형태로 출력하세요.
//         김회원 : 1000
//         박회원 : 3000
//         이회원 : 2000
//         (출력 순서는 저장한 순서와 다를 수 있습니다.)
//
// 4단계 : remove 메소드를 사용해서 key "이회원" 과 일치하는
//         (key-value) 한쌍의 데이터를 삭제한 후
//         size 메소드를 사용해서 아래 문장을 출력하세요.
//         2 개
//
// 5단계 : clear 메소드를 사용해서 HashMap 에 저장된 모든 데이터를 삭제한 후
//         isEmpty 메소드를 사용해서 HashMap 이 비어 있으면
//         아래 문장을 출력하세요.
//         HashMap이 비어 있습니다.
//===================================================================

public class HashMapEx3 {
	public static void main(String[] args) {

		//1단계 : Map부모 인터페이스를 구현한 자식 HashMap클래스의 배열객체 메모리 생성
		Map<String, Integer> map = new HashMap<String, Integer>();
		    //key ,  value

		//2단계 : put 메소드로 (key-value) 3쌍 저장
		//	   key    ,  value
		map.put("김회원", 1000);
		map.put("이회원", 2000);
		map.put("박회원", 3000);
		/*
		[Heap]
		┌───────────────────────────────┐
		│ HashMap                       │
		│───────────────────────────────│
		│ table                         │
		│  ┌─────────────────────────┐  │
		│  │ Entry("김회원", 1000)     │  │
		│  ├─────────────────────────┤  │
		│  │ Entry("이회원", 2000)     │  │
		│  ├─────────────────────────┤  │
		│  │ Entry("박회원", 3000)     │  │
		│  └─────────────────────────┘  │
		│ size = 3                      │
		└───────────────────────────────┘
		*/

		//3단계 : entrySet 메소드 + Iterator 객체 + getKey/getValue 로 전체 출력
		/*
		map.entrySet(); 코드를 작성 해서 HashMap 객체의 메소드를 호출하면
		HashMap 내부에 저장되어 있는 모든 Entry객체(key,value 한쌍의 정보가 저장된 객체)들을
		Map.Entry 객체 형태로 "그대로 꺼내어 볼 수 있는"
		Set 부모인터페이스를 구현한 자식 HashSet배열 주소 자체 반환
		*/
		Set<Entry<String, Integer>> entrySet = map.entrySet();

		//Entry 객체들을 앞에서 부터 하나씩 얻어 사용하기 위한
		//Iterator 부모인터페이스를 구현한 반복기 역할의 객체 주소 반환해 얻는다.
		Iterator<Entry<String, Integer>> entryIterator = entrySet.iterator();

		//HashMap 내부에 아직 순회하지 않은 Entry객체가 있는 동안만 반복
		while( entryIterator.hasNext() ) {

			//HashMap내부의 각칸에 저장된 Entry객체를 하나씩 꺼낸다
			//참고. 이 Entry는 HashMap 내부에 저장된 실제 Entry 객체이다.
			Entry<String, Integer> entry = entryIterator.next();
			//Entry("김회원", 1000) 객체  <- 반복 1
			//Entry("박회원", 3000) 객체  <- 반복 2
			//Entry("이회원", 2000) 객체  <- 반복 3

			//Entry객체에 저장된 key를 꺼낸다
			String k = entry.getKey();
			//Entry("김회원", 1000) 객체에 저장된 "김회원" <- key 꺼내기  <-- 반복 1
			//Entry("박회원", 3000) 객체에 저장된 "박회원" <- key 꺼내기  <-- 반복 2
			//Entry("이회원", 2000) 객체에 저장된 "이회원" <- key 꺼내기  <-- 반복 3

			//Entry객체에 저장된 value를 꺼낸다
			Integer v = entry.getValue();
			//Entry("김회원", 1000) 객체에 저장된 1000 <- value 꺼내기  <-- 반복 1
			//Entry("박회원", 3000) 객체에 저장된 3000 <- value 꺼내기  <-- 반복 2
			//Entry("이회원", 2000) 객체에 저장된 2000 <- value 꺼내기  <-- 반복 3

			//꺼낸 key와 value를 출력
			System.out.println(k + " : " + v);
			//김회원 : 1000  <-- 반복 1
			//박회원 : 3000  <-- 반복 2
			//이회원 : 2000  <-- 반복 3
			//(출력 순서는 저장한 순서와 다를 수 있습니다.)

		}//----while

		//4단계 : remove 메소드로 key "이회원" 삭제 후 size 출력
		//V  remove(Object key) : 주어진 key와 일치하는 key-value한쌍의 데이터 삭제
		//                        삭제되면 삭제된 value를 리턴
		map.remove("이회원");
		/*
		┌───────────────────────────────┐
		│ HashMap                       │
		│───────────────────────────────│
		│ table                         │
		│  ┌─────────────────────────┐  │
		│  │ Entry("김회원", 1000)     │  │
		│  ├─────────────────────────┤  │
		│  │ Entry("이회원", 2000)     │ <- (key-value) 한쌍 삭제됨
		│  ├─────────────────────────┤  │
		│  │ Entry("박회원", 3000)     │  │
		│  └─────────────────────────┘  │
		│ size = 2                      │
		└───────────────────────────────┘
		*/

		//HashMap 배열에 저장된 Entry객체 갯수 얻어 출력
		System.out.println(map.size() + " 개"); //2 개

		//5단계 : clear 메소드로 전체 삭제 후 isEmpty 메소드로 확인
		//void  clear() : HashMap배열에 저장된 모든 데이터 삭제
		map.clear();
		/*
		┌───────────────────────────────┐
		│ HashMap                       │
		│───────────────────────────────│
		│ table                         │
		│  (비어있음)                     │
		│ size = 0                      │
		└───────────────────────────────┘
		*/

		//boolean  isEmpty() : HashMap배열에 데이터가 비어있는지 물어보는 메소드로
		//                     비어 있으면? true 반환  저장되어있으면 false 반환
		if( map.isEmpty() ) {
			//clear 메소드 실행 후 데이터가 0개 → isEmpty 가 true 반환 → if 블록 실행됨
			System.out.println("HashMap이 비어 있습니다.");
			//				   HashMap이 비어 있습니다.
		}

		/*
		[전체 실행 결과 예시]
		김회원 : 1000
		박회원 : 3000
		이회원 : 2000
		2 개
		HashMap이 비어 있습니다.
		*/
	}//----- main
}//--- class
