/*
	Enumeration 인터페이스

	 - Enumeration 인터페이스는
	   Vector, Hashtable 같은 구식(JDK 1.0) 컬렉션 클래스의
	   가변길이 배열안에 저장된 객체들을 처음부터 끝까지 순서대로
	   쉽게 꺼낼수 있는 메소드들을 제공합니다.
	   (요즘 컬렉션에서 같은 역할을 하는 것이 Collections03에서 배운 Iterator입니다)

	   boolean  hasMoreElements()
	   : 배열안에 아직 꺼내지 않은 객체가 남아 있으면?
	     true반환, 끝까지 다 꺼냈으면 false를 반환

	   Object  nextElement()
	   : 배열안에 저장된 객체들을 순서대로 하나씩 꺼내올때 사용
	     (cursor 위치의 객체를 반환하고 cursor를 다음 칸으로 1 이동)

	 - ★ 지금 이것을 배우는 이유 (JSP/Spring 연결)
	   Enumeration은 옛날 인터페이스이지만, 곧 배울 서블릿 API가
	   Enumeration을 반환하는 메소드를 지금도 제공합니다.
	   예) request.getParameterNames()
	       -> 브라우저가 전송한 모든 파라미터 이름들을 Enumeration으로 반환
	   즉 JSP 수업에서 반드시 다시 만나게 되므로 사용법을 알아 두어야 합니다.

	 - Iterator와 메소드 이름 대응표 (외우기 쉽게)

	       역할                    Iterator (신형)      Enumeration (구형)
	       ──────────────────────────────────────────────────────────
	       꺼낼 것이 남았는가?      hasNext()            hasMoreElements()
	       하나 꺼내고 다음 칸으로   next()               nextElement()

	   -> 메소드 이름만 다르고 사용 공식은 완전히 같다.
	      while( ○○.hasMoreElements() ) { ○○.nextElement() }
*/

import java.util.Vector;   //<- 가변으로 칸이 늘어나는 배열 을 생성 할떄 사용되는 클래스 
						   //<- ArrayList 와 친구
						   //<- List 인터페이스가 부모 인터페이스 입니다.

import java.util.Enumeration;  //<- Iterator인터페이스 와 친구


public class EnumerationTest01 {

	public static void main(String[] args) {
		/*
		제공 받는 Vector 클래스로 가변 배열 객체 메모리 생성
		-> 기본생성자로 생성하면 10칸의 Vector 배열 메모리가 만들어 집니다.
		-> 10칸이 가득 찬 상태에서 add 메소드로 객체를 더 추가하면?
		   기존의 2배 크기(10칸 -> 20칸)의 새 배열을 만들어 옮긴 후 저장합니다.
		   ★ ArrayList는 1.5배씩 늘어나지만 Vector는 2배씩 늘어난다는 차이가 있다.
		   (capacityIncrement 값을 지정해서 생성하면 그 값만큼씩 늘어나고,
		    지정하지 않아 0이면 위처럼 2배 증가 방식으로 동작한다)
		*/		
		Vector  vector = new Vector();
		/*
		 Vector 객체 생성됨
		 ┌────────────────────────────────────────┐
		 │ elementCount = 0                       │  ← 현재 저장된 객체 수 (ArrayList의 size와 같은 역할)
		 │ capacity = 10                          │  ← 기본 배열 칸 수
		 │ capacityIncrement = 0                  │  ← 증가 단위 (0이면 2배 증가 방식으로 동작)
		 │ elementData                            │
		 │ [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]         │
		 │  0  1  2  3  4  5  6  7  8  9 (index)  │
		 └────────────────────────────────────────┘
		*/
		
		//Integer 레퍼 객체 5개를 반복해서 만들어서 Vector 배열 메모리의 각 칸에 차례대로 저장!
		//추가할 객체 데이터 ->  0     10    20     30     40 
		for(int i=0;   i<5;    i++) {
			
			vector.add(  i  * 10 );
//				i-> 0     0 * 10  = 0  ->  Integer.valueOf(0) =  new Integer(0) 래퍼 객체 형태로 추가 됨 
//			    i-> 1     1 * 10  = 10 ->  Integer.valueOf(10) = new Integer(10) 래퍼 객체 형태로 추가 됨
//			    i-> 2     2 * 10  = 20 ->  Integer.valueOf(20) = new Integer(20) 래퍼 객체 형태로 추가 됨
//			    i-> 3     3 * 10  = 30 ->  Integer.valueOf(30) = new Integer(30) 래퍼 객체 형태로 추가 됨
//			    i-> 4     4 * 10  = 40 ->  Integer.valueOf(40) = new Integer(40) 래퍼 객체 형태로 추가 됨
//			    i-> 5  조건식 거짓  while 종료 
			
		}
		/*
		 [Heap 영역 - Vector 내부]

		 elementData 배열 상태

		 index :    0      1      2      3      4     5    6    7    8    9
		         ┌──────┬──────┬──────┬──────┬──────┬────┬────┬────┬────┬────┐
		 value : │  0   │  10  │  20  │  30  │  40  │null│null│null│null│null│
		         └──────┴──────┴──────┴──────┴──────┴────┴────┴────┴────┴────┘
		            ↑      ↑      ↑      ↑      ↑
		          각 칸에는 오토 박싱된 Integer 래퍼 객체 5개가 저장되어 있음

		 elementCount = 5   <- add를 5번 했으므로 5
		 capacity     = 10  <- 칸은 아직 10칸 그대로 (5칸이 비어 있는 상태)
		*/
		
		//위 Vector 배열 메모리 전체의 각 칸에 저장된 Integer 래퍼객체들을 for 일반 반복문을 이용해 얻어 출력
		for(int i=0;  i<vector.size();   i++) {
			
			System.out.println(vector.get(i));
										/*
										0
										10
										20
										30
										40
										*/
		}
	
		
		/*
		★ Vector 배열의 elements 메소드를 호출하면? (정확하게 이해하기)

		  객체들을 꺼내서 다른 배열에 복사해 담는 것이 아니다!

		  Enumeration 부모인터페이스를 구현한 자식 객체가 하나 만들어져 반환되는데,
		  이 객체는 딱 두 가지만 가지고 있다. (Collections03의 Iterator 인터페이스 와 같은 구조)

		      ① 원본 Vector 배열이 어디 있는지 가리키는 주소
		      ② cursor : "다음에 꺼낼 칸의 index 번호"를 저장하는 int 변수 (처음엔 0)

		  nextElement()를 호출할 때마다 원본 배열의 cursor 위치 객체를 반환하고
		  cursor 를 1 증가시킨다. 즉 원본 배열 위를 cursor 가 이동하며 읽는 구조다.
		*/
		Enumeration  enu  = vector.elements();		
		/*
		 [Heap]
		 Enumeration 구현 객체                          원본 Vector 배열
		 ┌──────────────────┐                  [ 0, 10, 20, 30, 40, null...null ]
		 │ 원본 배열의 주소  ─────────▶     0   1   2   3   4
		 │ cursor = 0       │                    ▲
		 └──────────────────┘                   cursor가 0번 칸을 가리키는 중
		 
	   */		
		//hasMoreElements()가 true를 반환하는 동안(= 아직 안 꺼낸 객체가 남아있는 동안) 반복한다
		while(enu.hasMoreElements()) {
			
			System.out.println( enu.nextElement() ); //cursor 위치의 객체 반환 후 cursor 1 증가
										/*
										0
										10
										20
										30
										40
										*/
		}
		/*
		   ★ while문이 도는 동안 cursor의 이동 과정 (전부 원본 배열 위에서 일어난다)

		   회차   hasMoreElements() 판단              nextElement() 반환   실행 후 cursor
		   ─────────────────────────────────────────────────────────────────────
		    1    cursor0 < elementCount5 → true            0                  1
		    2    cursor1 < elementCount5 → true           10                  2
		    3    cursor2 < elementCount5 → true           20                  3
		    4    cursor3 < elementCount5 → true           30                  4
		    5    cursor4 < elementCount5 → true           40                  5
		    6    cursor5 < elementCount5 → false → while반복문 종료 (더 꺼낼 것이 없다)

		   ★ 주의 : Enumeration 도 Iterator 처럼 1회용이다.
		     위 while 문이 끝난 시점에 cursor 는 이미 끝(5)에 가 있으므로
		     처음부터 다시 꺼내려면 vector.elements()를 다시 호출해
		     cursor=0인 새 Enumeration 객체를 만들어야 한다.

		   ★ 참고 : nextElement()의 반환 자료형은 Object 다.
		     제네릭 없이 만들었으므로 꺼낸 객체로 Integer 고유 메소드를 쓰려면
		     다운캐스팅이 필요하다. 제네릭을 쓰면 해결된다.
		     예) Vector<Integer> vector = new Vector<Integer>();
		         Enumeration<Integer> enu = vector.elements();
		         -> nextElement()가 Integer 로 반환되어 다운캐스팅이 필요 없다.
		
		
			결론 1 : Enumeration 은 Vector, Hashtable 등 구식 컬렉션용 반복 인터페이스이며
			         사용 공식은 while( hasMoreElements() ) { nextElement() } 이다.

			결론 2 : elements()는 객체를 복사하는 것이 아니라, 원본 배열의 주소와
			         cursor(다음에 꺼낼 index)를 가진 반복용 객체를 만들어 반환한다.
			         Enumeration 도 1회용이므로 다시 반복하려면 elements()를 다시 호출한다.

			결론 3 : Iterator 와의 대응 - hasNext()↔hasMoreElements(), next()↔nextElement().
			         새 코드에는 Iterator(또는 향상된 for 문)를 쓰고,
			         Enumeration 은 서블릿의 request.getParameterNames()처럼
			         구식 API가 반환해 줄 때 받아서 사용한다.

			결론 4 : Vector 는 칸이 가득 차면 2배씩 늘어난다. (ArrayList는 1.5배)
		*/	
		
	}

}
/*
참고.  자바에서 Iterator 인터페이스와 Enumeration 인터페이스는
      모두 컬렉션라이브러리에서 제공하는 가변길이 배열에 저장된 객체들을 반복해서 쉽게 꺼내올때 사용되는
      추상메소드들을 제공하는 인터페이스 들입니다

      차이점을 살펴보면?

      1. 만들어진 시기
      	- Enumeration 인터페이스는 JDK 1.0버전 부터 도입되었습니다.
      	- Iterator 인터페이스는 JDK 1.2버전에서 추가 도입되었습니다.

	  2. 컬렉션 배열의 타입
	    - Enumeration 인터페이스는 주로
	      Vector 나 Hashtable 과 같은 구식 컬렉션 클래스의 배열 메모리에서 사용됩니다.

	    - Iterator 인터페이스는 모든 컬렉션 클래스의 배열 메모리에서 사용될수 있습니다.

	  3. 컬렉션 배열에 저장된 객체 제거
	  	- Enumeration 인터페이스는 객체를 제거하는 기능의 메소드를 제공하지 않습니다.
	  	- Iterator 인터페이스는 remove()메소드를 통해 현재 반복해서 꺼내온 객체를 배열에서 제거하는 기능을 제공합니다.

	  4. 메소드 추가지원
	  	- Iterator 인터페이스는 추가로
	  	  forEachRemaining()메소드를 지원 합니다.
	  	  이 메소드는 아직 꺼내지 않은 나머지 모든 객체를 하나씩 꺼내면서
	  	  특정 동작을 수행할수 있습니다.
	  	- Enumeration 인터페이스는 해당 기능을 제공하지 않습니다.

	  -> 요약 : 새로 작성하는 코드에는 Iterator 를 사용하고,
	            Enumeration 은 구식 API(서블릿 등)가 반환할 때 받아서 사용한다.
*/



















