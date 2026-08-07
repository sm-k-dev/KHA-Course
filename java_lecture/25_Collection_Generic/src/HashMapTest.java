/*

	HashMap 클래스 
	
	- Map 인터페이스의 자식클래스로 
	  key-value한쌍의 형태로 묶어서 저장하는 배열을 만들때
	  사용하는 클래스 
	  
	- 여기서 key와 value는 모두 객체로 저장된다.
	
	- key는 중복저장할수 없지만  value는 중복 저장할수 있다.
	
	- 기존에 저장된 key와 동일한 key로 값을 저장하면 
	  기존에 값은 없어지고 새로운 값으로 대치된다.

	
	메소드 							설명
	V	put(K key, V value)			주어진 키와 값을 추가, 저장이 되면 값을 리턴 (객체 추가)
	
	
	boolean  containsKey(Object key)  주어진 키가 있는여부  true 또는 false리턴
	
	boolean  containsValue(Object value) 주어진 값이 있는여부 true 또는 false리턴 
	
	V   get(Object key)				주어진 키의 값을 리턴 
	
	boolean		isEmpty()			HashMap배열에 데이터가 비어있는지 물어보는 메소드로 
									비어 있으면? true리턴  저장되어있으면 false리턴
									
	Set<K>	keySet()				HashMap배열에 저장된 모든 key객체들을 꺼내서
									Set부모인터페이스를 구현한 자식배열HashSet에 담아 리턴 
	
	int  size()						HashMap배열에 저장된 key의 총개수를 리턴
	
	Collection<V>	values()		HashMap배열에 저장된 모든 value객체들을 꺼내어서
									Collection부모인터페이스를 구현한 자식배열에 담아
									자식배열 자체를 리턴 
	
	void   clear()					HashMap배열에 저장된 모든 데이터 삭제
	
	V	   remove(Object key)	    주어진 key와 일치하는 key-value한쌍의 데이터 삭제
									삭제되면 삭제된 value를 리턴
	
	Set<Map.Entry<K,V>>   entrySet()   
	
	  								키와 값의 쌍으로 구성된 모든 Map.Entry객체를 
	  								Set인터페이스를 구현한 자식 HashSet에 담아 리턴 
	
*/

import java.util.HashMap; //Map 부모인터페이스를 구현한 자식 HashMap 클래스 
import java.util.Iterator;
import java.util.Map;        //Map 인터페이스 
import java.util.Map.Entry; //Map인터페이스 내부에 만들어져 있는 중첩(내부) Entry인터페이스 
import java.util.Set;

public class HashMapTest {

	public static void main(String[] args) {
		
		//Map 부모 인터페이스를 구현한 자식 HashMap클래스의 배열이 포함된 객체 생성
		Map<String, Integer>   map = new HashMap<String, Integer>();
		//	 key  ,  value
		/*
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
		│ modCount   : 0          │
		└─────────────────────────┘
		 */		
		//HashMap 표형의 메모리 구조에  데이터(key-value) 를 한쌍의 형태로 묶어서 한 행에 추가로 저장 
		//       key  , value
		map.put("신용권", 85);
		map.put("홍길동", 90);
		map.put("동장군", 80);
		map.put("홍길동", 95);//<- 같은 key "홍길동" 에 대한 value를 추가하면 
							 //   기존에 map.put("홍길동", 90); 이용해서 저장했던 행을 제거 되고
							 //   map.put("홍길동", 95); 이용해서 저장한 행이 HashMap에 추가되어 저장됨 		
		/*
		[Heap]
		┌──────────────────────────────────────────────┐
		│ HashMap                                      │
		│──────────────────────────────────────────────│
		│ table                                        │
		│ ┌─────┬─────┬─────┬─────┐                    │
		│ │  0  │  1  │  2  │  3  │                    │
		│ └─────┴─────┴─────┴─────┘                    │
		│     │     │     │                            │
		│     ▼     ▼     ▼                            │
		│   ┌────────┐ ┌────────┐ ┌────────┐           │
		│   │ Entry  │ │ Entry  │ │ Entry  │           │
		│   │────────│ │────────│ │────────│           │
		│   │ key    │ │ key    │ │ key    │           │
		│   │"신용권"	 │ │"홍길동"  │ │"동장군" │           │
		│   │ value  │ │ value  │ │ value  │           │
		│   │  85    │ │  95    │ │  80    │           │
		│   │ next   │ │ next   │ │ next   │           │
		│   │ null   │ │ null   │ │ null   │           │
		│   └────────┘ └────────┘ └────────┘           │
		│                                              │
		│ size = 3                                     │
		└──────────────────────────────────────────────┘
		*/		
		System.out.println("HashMap 에 저장된 key 총 갯수 얻기  : " +  map.size() );
		//				    HashMap 에 저장된 key 총 갯수 얻기  : 3

		System.out.println();		
		/*
		[Heap]
		┌──────────────────────────────────────────────┐
		│ HashMap                                      │
		│──────────────────────────────────────────────│
		│ table                                        │
		│ ┌─────┬─────┬─────┬─────┐                    │
		│ │  0  │  1  │  2  │  3  │                    │
		│ └─────┴─────┴─────┴─────┘                    │
		│     │     │     │                            │
		│     ▼     ▼     ▼                            │
		│   ┌────────┐ ┌────────┐ ┌────────┐           │
		│   │ Entry  │ │ Entry  │ │ Entry  │           │
		│   │────────│ │────────│ │────────│           │
		│   │ key    │ │ key    │ │ key    │           │
		│   │"신용권"  │ │"홍길동" │ │"동장군"  │           │
		│   │ value  │ │ value  │ │ value  │           │
		│   │  85    │ │  95    │ │  80    │           │
		│   │ next   │ │ next   │ │ next   │           │
		│   │ null   │ │ null   │ │ null   │           │
		│   └────────┘ └────────┘ └────────┘           │
		│                                              │
		│ size = 3                                     │
		└──────────────────────────────────────────────┘
		*/		
		//HashMap 에 저장된 특정 key를 이용해 value 얻기 
		// ->  V  get(Object  key)   :  매개변수로 전달하는 key의  value를 반환 합니다.
		
		//1. "홍길동" key 자체를 변수에 저장
		String key = "홍길동";
		
		//2. "홍길동" key를 이용해 95 value를 얻어 저장
		int value = map.get(key);
		
		System.out.println(key + "  :  " + value);
		//					  홍길동  :  95
/*
		[Heap]
		┌──────────────────────────────────────────────┐
		│ HashMap                                      │
		│──────────────────────────────────────────────│
		│ table                                        │
		│ ┌─────┬─────┬─────┬─────┐                    │
		│ │  0  │  1  │  2  │  3  │                    │
		│ └─────┴─────┴─────┴─────┘                    │
		│     │     │     │                            │
		│     ▼     ▼     ▼                            │
		│   ┌────────┐ ┌────────┐ ┌────────┐           │
		│   │ Entry  │ │ Entry  │ │ Entry  │           │
		│   │────────│ │────────│ │────────│           │
		│   │ key    │ │ key    │ │ key    │           │
		│   │"신용권"  │ │"홍길동" │ │"동장군"  │           │
		│   │ value  │ │ value  │ │ value  │           │
		│   │  85    │ │  95    │ │  80    │           │
		│   │ next   │ │ next   │ │ next   │           │
		│   │ null   │ │ null   │ │ null   │           │
		│   └────────┘ └────────┘ └────────┘           │
		│                                              │
		│ size = 3                                     │
		└──────────────────────────────────────────────┘
	
	    참고.
			Set<K>	keySet()				HashMap배열에 저장된 모든 key객체들을 꺼내서
											Set부모인터페이스를 구현한 자식배열HashSet에 담아 리턴 	
		*/		
		Set<String> keySet = map.keySet();
		/*
		자식배열 HashSet
					-----         ------
						|"동장군"   | <- key
						|"홍길동"   | <- key
						|"신용권"   | <- key
						|_________|
					
		HashSet 배열에 저장되어 있던 모든 key들을 꺼내어서 
		Iterator부모인터페이스를 구현한 자식 Itr배열메모리에 담아 Itr배열 주소 자체 반환
	   */
		Iterator<String> keyIterator = keySet.iterator();
		//["홍길동", "신용권", "동장군"] <-  모든 key 들만 저장된  자식 Itr 배열 모습
		
		//HashMap 공간에 저장된 key-value 를 모두 반복해서 얻어 출력 
		while(keyIterator.hasNext()) {
			
			//key 들만 차례로 반복해서 자식 Itr 배열에서 얻습니다.
			String k = keyIterator.next(); //"홍길동"  										   
										   //"신용권"
										   //"동장군"
			
			//key를 이용해 HashMap 표의 공간에 key-value 중 value를 얻습니다.
			Integer  v = map.get(k); //new Integer(95) 얻음 
			 						 //new Integer(85) 얻음
									 //new Integer(80) 얻음
												
			System.out.println(k   +  " : " +  v);
			 /*
									홍길동 : 95
									신용권 : 85
									동장군 : 80
			 */
			
		} // while
		
		System.out.println("-----------------------------------------");
		
		//Map 부모 인터페이스를 구현한 자식 HashMap클래스의 배열이 포함된 객체 생성
		
//		Map<String, Integer>   map = new HashMap<String, Integer>();
		
/*
		map.entrySet(); 코드를 작성 해서 HashMap 객체의 메소드를 호출하면
		HashMap 내부에 저장되어 있는 모든 Entry객체(key,value 한쌍의 정보가 저장된 객체)들을 
	    Map.Entry 객체 형태로 "그대로 꺼내어 볼 수 있는" 
		Set 부모인터페이스를 구현한 자식 HashMap배열 메모리에 Entry객체들 참조할수 있는 HashSet배열 주소 자체 반환
	
		 ※ Entry 객체는 새로 생성되지 않는다.
		 ※ HashMap 안에 이미 존재하던 Entry 객체들을 그대로 참조한다.
		 ※ 실제 데이터는 HashMap에 있고, HashSet배열은 데이터(key,value)를 소유하지 않는다.
		 ※ HashSet배열을 통해 값을 수정하면 HashMap의 데이터(key,value)도 함께 변경된다.
*/			
		Set<Entry<String,Integer>>    entrySet =  map.entrySet();
		/*
		[Stack]
		+---------------------------+
		| entrySet                  |
		+--------------+------------+
		               |
		               v
		[Heap]
		+---------------------------+
		| HashSet 배열               |
		+---------------------------+
		| map 참조변수 -----------------+--------------------+
		+---------------------------+                     |
		                                                  v
		                                  +---------------------------+
		                                  | HashMap                   |
		                                  +---------------------------+
		                                  | table                     |
		                                  |  +---------------------+  |
		                                  |  | Entry("신용권", 85)   |  |
		                                  |  +---------------------+  |
		                                  |  | Entry("홍길동", 95)   |  |
		                                  |  +---------------------+  |
		                                  |  | Entry("동장군", 80)   |  |
		                                  |  +---------------------+  |
                              			  +---------------------------+
*/		 	
		// HashSet배열 객체 주소번지가 저장된 entrySet참조변수를 이용해  HashMap배열에 저장된 Entry객체들을 
		// 앞에서 부터 하나씩 얻어 사용하기 위한  Iterator 부모인터페이스를 구현한 반복기 역할을 HashMap.Itr객체 주소 반환해 얻는다.
		//
		// ※ Iterator 부모인터페이스를 구현한 반복기 역할을 HashMap.Itr 객체는 Entry 객체를 저장하는 배열이 아니다.
		// ※ Entry객체의 "위치 정보"만 관리하는 순회 전용 객체이다.
		// ※ 실제 Entry 객체들은 여전히 HashMap 내부에 존재한다.		
		 Iterator<Entry<String, Integer>>    entryIterator  = entrySet.iterator();
/*
		 [Stack]
		+---------------------------+
		| entryIterator             |
		+--------------+------------+
		               |
		               v
		[Heap]
		+---------------------------+
		| Iterator (HashMap.Itr객체) |
		+---------------------------+
		| map 참조 -----------------+----------------------+
		| table 참조 ---------------+-------------------+  |
		| cursor  -> index 0        |                   |  |
		| current -> null           |                   |  |
		| expectedModCount = 3      |                   |  |
		+---------------------------+                   |  |
		                                                |  |
		                                                v  v
		                              +---------------------------+
		                              | HashMap                   |
		                              +---------------------------+
		                              | table                     |
		                              |  +---------------------+  |
		                              |  | Entry("신용권", 85)   |<-+
		                              |  +---------------------+  |
		                              |  | Entry("홍길동", 95)   |  |
		                              |  +---------------------+  |
		                              |  | Entry("동장군", 80)   |  |
		                              |  +---------------------+  |
		                              +---------------------------+	 
		 */		
		 // HashMap.Itr 자식객체는  그다음 HashMap배열 내부의 각 칸에 저장된 Entry객체를 하나식 반복해서 꺼내어 사용할수 있는 동안만 반복
		 // (HashMap 내부에 아직 순회 하지 않은 Entry 객체가 있는 동안만 반복)
		 while( entryIterator.hasNext() ) {
			 
			   //HashMap.Itr 객체가 현재 사용하고 있는 HashMap 내부의 각 칸에 저장된 Entry 객체를 하나씩 꺼낸다
			   Entry<String, Integer> entry  = entryIterator.next();
			   //new Entry("홍길동",95 ) 객체  <- 반복 1
			   //new Entry("신용권", 85) 객체  <- 반복 2 
			   //new Entry("동장군",80) 객체   <- 반복 3
			   
			   //Entry 객체에 저장된 key를 꺼낸다.
			   String k = entry.getKey();
			   //new Entry("홍길동",95 ) 객체에 저장된 "홍길동" <-key 꺼내기      <-- 반복 1
			   //new Entry("신용권", 85) 객체에 저장된 "신용권" <-key 꺼내기      <-- 반복 2
			   //new Entry("동장군", 80) 객체에 저장된 "동장군" <-key 꺼내기 	  <-- 반복 3
			   
			   //Entry 객체에 저장된 value를 꺼낸다.
			   Integer v = entry.getValue();
			    //new Entry("홍길동", 95) 객체에 저장된 95를 new Integer(95)로 <- value 꺼내기     <-- 반복 1
			    //new Entry("신용권", 85) 객체에 저장된 85를 new Integer(85)로 <- value 꺼내기     <-- 반복 2
			    //new Entry("동장군", 80) 객체에 저장된 80를 new Integer(80)로 <- value 꺼내기     <-- 반복 3
			   
			   //꺼낸 key 와  value를 같이 출력
			   System.out.println(k  + "  :  " +   v);
								   //홍길동 : 95  <-- 반복 1
								   //신용권 : 85  <-- 반복 2
								   //동장군 : 80  <-- 반복 3
		 } //-------- while
		
		System.out.println();
		
		//HashMap 객체 내부의 인스턴스변수의 배열 메모리에 저장된 Entry객체 정보 중 key를 이용해  Entry객체(key-value) 한쌍 삭제 
		map.remove("홍길동");
		
		/*	
        +---------------------------+
        | HashMap                   |
        +---------------------------+
        | table                     |
        |  +---------------------+  |
        |  | Entry("신용권", 85)   |+
        |  +---------------------+  |
        |  | Entry("홍길동", 95)   | <-  Entry객체(key-value) 객체  하나 삭제 
        |  +---------------------+  |
        |  | Entry("동장군", 80)   |  |
        |  +---------------------+  |
        +---------------------------+
	*/	
		
		//HashMap 객체 내부의 배열 메모리의 각칸에 저장된 Entry 객체 갯수 얻어 출력
		System.out.println(map.size() + " 개");  // "2 개"
		
		
	}

}







