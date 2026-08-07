import java.util.Map;     //Map인터페이스
import java.util.HashMap; //Map부모인터페이스를 구현한 자식 HashMap클래스

//===================================================================
//[응용 문제 1] HashMap 배열 메모리에 상품-가격 데이터 저장하고 꺼내오기
//===================================================================
//수업 예제 HashMapTest 와 똑같은 순서로
//상품 이름을 key, 가격을 value 로 저장하고 출력하는 문제입니다.
//
//[요구사항]
// 1단계 : key 는 String, value 는 Integer 를 저장하는
//         HashMap 객체를 생성해서 Map 인터페이스 타입 참조변수 map 에 저장하세요.
//
// 2단계 : put 메소드를 사용해서 아래 3쌍의 (key-value) 데이터를 저장하세요.
//           key           value
//          "노트북"   ,   1500000
//          "마우스"   ,   30000
//          "키보드"   ,   80000
//
// 3단계 : size 메소드를 사용해서 아래 문장을 출력하세요.
//         HashMap에 저장된 key 총 갯수 얻기 : 3
//
// 4단계 : get 메소드를 사용해서 key "마우스" 와 연결되어 저장된
//         value 30000 을 int 변수 price 에 저장한 후 아래 문장을 출력하세요.
//         마우스 : 30000
//
// 5단계 : containsKey 메소드를 사용해서 key "모니터" 가
//         HashMap 에 저장되어 있는지 확인하고,
//         저장되어 있지 않으면 아래 문장을 출력하세요.
//         모니터 key는 HashMap에 저장되어 있지 않습니다.
//===================================================================

public class HashMapEx1 {
	public static void main(String[] args) {

		//1단계 : Map부모 인터페이스를 구현한 자식 HashMap클래스의 배열객체 메모리 생성
		Map<String, Integer> map = new HashMap<String, Integer>();
		    //key ,  value
		/*
		<String, Integer> 의 의미
		→ 앞의 String   : key 로 String(문자열) 객체만 저장하겠다.
		→ 뒤의 Integer  : value 로 Integer(정수를 감싼 객체)만 저장하겠다.

		 [Stack]
		┌─────────────────────────┐
		│ map                     │
		└───────────────┬─────────┘
		                │
		                ▼
		[Heap]
		┌─────────────────────────┐
		│ HashMap                 │
		│─────────────────────────│
		│ table      : null       │
		│ size       : 0          │
		└─────────────────────────┘
		- 아직 데이터 없음
		*/

		//2단계 : put 메소드로 (key-value) 3쌍 저장
		//HashMap 표형태의 메모리 구조에 데이터(key-value)를 한쌍의 형태로 묶어 한 행에 각각 추가로 저장
		//			  key      ,  value
		map.put("노트북", 1500000);
		map.put("마우스", 30000);
		map.put("키보드", 80000);
		/*
		value 자리에 30000 처럼 int 값을 적으면
		자바가 자동으로 new Integer(30000) 객체로 바꿔서 저장합니다. (오토박싱)

		[Heap]
		┌──────────────────────────────────────────────┐
		│ HashMap                                      │
		│──────────────────────────────────────────────│
		│ table                                        │
		│   ┌─────────┐ ┌─────────┐ ┌─────────┐        │
		│   │ Entry   │ │ Entry   │ │ Entry   │        │
		│   │─────────│ │─────────│ │─────────│        │
		│   │ key     │ │ key     │ │ key     │        │
		│   │"노트북"   │ │"마우스"  │ │"키보드"   │        │
		│   │ value   │ │ value   │ │ value   │        │
		│   │ 1500000 │ │ 30000   │ │ 80000   │        │
		│   └─────────┘ └─────────┘ └─────────┘        │
		│                                              │
		│ size = 3                                     │
		└──────────────────────────────────────────────┘
		*/

		//3단계 : size 메소드로 저장된 key 총 갯수 출력
		//int  size() : HashMap배열에 저장된 key의 총개수를 리턴
		System.out.println("HashMap에 저장된 key 총 갯수 얻기 : " + map.size());
		//				   HashMap에 저장된 key 총 갯수 얻기 : 3

		//4단계 : get 메소드로 key "마우스" 의 value 꺼내와 출력
		//V   get(Object key) : 주어진 키의 값을 리턴

		//1. "마우스" key 변수에 저장
		String key = "마우스";

		//2. "마우스" key를 이용해 30000 value를 얻어 저장
		int price = map.get(key);
		/*
		get 메소드는 value 를 Integer 객체로 반환합니다.
		반환된 Integer 객체를 int 변수 price 에 저장하면
		자바가 자동으로 Integer 객체 안의 정수 30000 을 꺼내서 저장합니다. (오토언박싱)
		*/

		System.out.println(key + " : " + price);
		//				   마우스   :   30000

		//5단계 : containsKey 메소드로 key "모니터" 저장 여부 확인 후 출력
		//boolean  containsKey(Object key) : 주어진 키가 있는여부 true 또는 false리턴
		if( map.containsKey("모니터") == false ) {
			/*
			HashMap 표 안의 key 들 : "노트북", "마우스", "키보드"
			"모니터" key 는 put 메소드로 저장한 적이 없음
			→ containsKey 메소드가 false 를 반환
			→ false == false 는 true → if 블록 실행됨
			*/
			System.out.println("모니터 key는 HashMap에 저장되어 있지 않습니다.");
			//				   모니터 key는 HashMap에 저장되어 있지 않습니다.
		}

		/*
		[전체 실행 결과]
		HashMap에 저장된 key 총 갯수 얻기 : 3
		마우스 : 30000
		모니터 key는 HashMap에 저장되어 있지 않습니다.
		*/
		

	}//----- main
}//--- class




