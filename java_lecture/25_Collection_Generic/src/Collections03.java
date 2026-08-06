/*
 주제 :   부모 List 인터페이스를 구현한 자식 ArrayList 클래스
         그리고 Iterator 인터페이스를 구현한 자식 ArrayList$Itr 객체의 hasNext(), next()메소드를 이용해
         쉽게~  ArrayList 가변 배열에 저장된 객체를 반복해서 얻어 사용하는 예

 ■ Iterator(이터레이터, 반복기)란?
   - 가변 배열에 저장된 객체(요소)들을 "처음부터 끝까지 순서대로 하나씩 꺼내는 일"만
     전문으로 하는 객체이다.
   - Iterator는 인터페이스이고, ArrayList 내부에 그 구현 자식클래스인
     ArrayList$Itr 클래스가 만들어져 있다.
     (클래스 이름의 $ 는 "ArrayList 클래스 내부에 선언된 Itr 클래스"라는 뜻이다)

 ■ ArrayList$Itr 객체의 실제 구조 (중요! 배열을 복사하는 것이 아니다)
   - ArrayList$Itr 객체는 배열을 새로 만들거나 객체들을 복사해 담지 않는다.
   - 내부에 딱 두 가지만 가지고 있다.
       ① 원본 ArrayList 배열이 어디 있는지 가리키는 주소
       ② cursor : "다음에 꺼낼 칸의 index 번호"를 저장하는 int 변수 (처음엔 0)
   - next()를 호출할 때마다 원본 배열의 cursor 위치 객체를 반환하고 cursor 를 1 증가시킨다.
   - 즉, 원본 배열 위를 cursor가 0번 칸부터 끝 칸까지 한 칸씩 이동하며 읽는 구조이다.

 ■ Iterator 인터페이스에서 제공되는 메소드 2개
 
      boolean hasNext()
    -> cursor 가 가리키는 칸에 아직 꺼내지 않은 객체가 남아 있냐? 물어보는 메소드
       남아 있으면 true 반환, 끝까지 다 꺼냈으면 false 반환 하는 메소드
       (내부 동작: cursor < size 인지 비교한 결과를 반환)

      E next()
    -> cursor 위치에 저장된 객체를 반환하고, cursor를 다음 칸으로 1 이동시키는 메소드
       (E는 제네릭 표기. 아직 배우지 않았으므로 지금은 Object로 읽으면 된다)
*/

import java.util.ArrayList; //<- ArrayList 클래스 
import java.util.List;      //<- ArrayList 클래스의 부모 인터페이스 List
import java.util.Iterator;  //<- 가변 배열에 저장된 객체(요소)들을 쉽게 얻게 도와주는 메소드들을 제공하는 인터페이스

public class Collections03 {

	public static void main(String[] args) {
		
		//ArrayList 클래스를 이용해 가변으로 칸이 늘어 나는 배열 메모리 생성 (업캐스팅 해서 만들기)
		List  list = new ArrayList(6);  //<-- 처음부터 배열 칸 수는 6칸으로 지정
	    /*
	     [Stack]                       [Heap]
	     list (List 타입 참조변수)      ArrayList 객체
	        │                          ┌──────────────────────────┐
	        └─────────────▶  size = 0                
	                                   │ capacity = 6             │
	                                   │ elementData              │
	                                   │ [    ][    ][    ][    ][    ][    ]
	                                   │   0     1     2     3     4     5    index
	                                   └──────────────────────────┘
	     */		
		
		 //0번 index 위치 칸에  "하나" 문자열 객체 주소 저장, size변수가 0 ->  1로 증가 
		 list.add("하나");
	    /*
	     [Heap]
	     ArrayList 객체
	     ┌──────────────────────────┐
	     │ size = 1   ★add할 때마다 size도 1씩 증가한다
	     │ capacity = 6             │
	     │ elementData              │
	     │ ["하나"][    ][    ][    ][    ][    ]
	     │    0      1     2     3     4     5   index
	     └──────────────────────────┘
	     */
		 list.add(2);   //오토박싱 : Integer.valueOf(2); Integer래퍼 객체로 저장됨
		 list.add(3.42); //오토박싱 : Double.valueOf(3.42); Double래퍼 객체로 저장됨
		 list.add("넷");
		 list.add("다섯");
		 list.add(6);   //오토박싱 : Integer.valueOf(6); Integer래퍼 객체로 저장됨
	    /*
	     [Heap]
	     ArrayList 객체
	     ┌──────────────────────────┐
	     │ size = 6                 │
	     │ capacity = 6             │
	     │ elementData              │
	     │ ["하나"][Integer.valueOf(2)][Double.valueOf(3.42)]["넷"]["다섯"][Integer.valueOf(6)]
	     │    0           1                    2               3      4          5          index
	     └──────────────────────────┘
	     */		
		 
		 //ArrayList 배열 모습 (간단히 표현)
		 //["하나",   2,   3.42,  "넷",  "다섯",    6]
		 //   0       1      2      3      4     5    index
		
		 //======================================================
		 //방법1. 기존 방식 : for문  + get(index) 로 반복해서 꺼내오기 
		 //======================================================
		 System.out.println("===== 방법1. for문 + get(index) ======");
		 
		 //i를 0부터 size-1까지 1씩 증가시키면서 get(i)로 각 칸의 객체를 꺼낸다.
		 for(int i=0;   i<list.size();   i++) {
			 
			 System.out.println( list.get(i)  );
		 }
		 //이 방식의 단점 : index가 없는 컬렉션(HashSet 등)에는 get(index)가 없어서 쓸 수 없다.
		 //               -> 어떤 컬렉션이든 똑같은 방법으로 꺼내기 위해 Iterator가 제공된다.
		 
		 //==========================================================
		 //방법2. Iterator(이터레이터 반복자)로 반복해서 배열의 객체값 꺼내오기
		 //=========================================================
		 System.out.println("====== 방법2. Iterator 인터페이스 ======");
		 
		 //ArrayList 배열 모습 (간단히 표현)
		 //["하나",   2,   3.42,  "넷",  "다섯",    6]
		 //   0       1      2      3      4     5    index
	 
		 /*
		 	list.iterator() 메소드를 호출하면
		 	Iterator부모 인터페이스를 구현한 자식 ArrayList$Itr 객체가 생성되고
		 	그 객체 메모리의 주소번지를 반환해 준다. (업캐스팅으로 부모 자료형에 저장)
		 	이 객체는 원본 배열을 가리키는 주소와 cursor(=0)만 가지고 태어난다.
		 */
		 Iterator iterator = list.iterator();
	    /*
	     [Heap]
	     ArrayList$Itr 객체                        원본 ArrayList 배열
	     ┌──────────────────┐                  ["하나", 2, 3.42, "넷", "다섯", 6]
	     │ 원본 배열의 주소  ──────────▶    0     1    2     3     4    5
	     │ cursor = 0       │                    ▲
	     └──────────────────┘                   cursor가 0번 칸을 가리키는 중
	     */		 
		 
		 //hasNext()가 true를 반환하는 동안(= 아직 안 꺼낸 객체가 남아있는 동안) 반복한다
		 while( iterator.hasNext() ) {
			 
			 System.out.println(  iterator.next()  );  //cursor 위치의 객체 반환 후 cursor 1 증가
//			 					  "하나"
//			 					   2
//			 					   3.42
//			 					   "넷"
//			 					   "다섯"
//			 					   6
		 }
		 /*
		    ★ while문이 도는 동안 cursor의 이동 과정 (전부 원본 배열 위에서 일어난다)

		    회차   hasNext() 판단        next() 반환   실행 후 cursor
		    ────────────────────────────────────────────────────────
		     1    cursor0 < size6 →true    "하나"            1
		     2    cursor1 < size6 →true      2              2
		     3    cursor2 < size6 →true    3.42             3
		     4    cursor3 < size6 →true     "넷"             4
		     5    cursor4 < size6 →true    "다섯"            5
		     6    cursor5 < size6 →true      6              6
		     7    cursor6 < size6 →false  → while문 종료 (더 꺼낼 것이 없다)
		 */
		 //===============================================
		 // Iterator 사용 시 주의 2가지
		 //===============================================
		 
		 //주의 1. Iterator 는 1회용이다.
		 //       위 while 문이 끝난 시점에  cusor는 이미 끝(6)에 가 있으므로
		 //		  같은 iterator로 다시 반복하면 hasNext() 가 false라 한번도 실행되지 않는다.
		 System.out.println("다 쓴 iterator의 hasNext() -> " + iterator.hasNext() ); //false
		 
		 // 	  처음 부터 다시 ArrayList 배열에서 꺼내고 싶으면?  list.iterator()를 다시 호출해
		 //	       cursor=0 인  새 ArrayList$Itr 객체를 새로 만들어야 한다.
		 Iterator iterator2 = list.iterator();
		 System.out.println("새로 만든 iteratror2 참조변수의 ArrayList$Itr 객체의 hasNext() -> " + iterator2.hasNext());
		 
		 //주의 2. hasNext() 확인 없이  next()만 계속 호출하면?
		 //		  ArrayList 원본배열에 꺼낼 객체가 없는 순간 NoSuchElementException 예외가 발생하여 자바프로그램이 강제 종료 된다.
		 //		  그래서  while( itr.hasNext()  ) {  itr.next();  } 형태를 공식처럼 사용한다.
		 
		 //=======================================================
		 //방법 3.   향상된 for문 (Iterator를 자동으로 써 주는 축약 문법)
		 //=======================================================
		 System.out.println("====== 방법3. 향상된 for문 =======");
		 
		 //작성법 : for( 꺼낸객체를담을변수선언   : 반복할대상배열 ) {   반복할코드;   }
		 
		 
		 // ArrayList 배열 모습 (간단히 표현)
		 // ["하나",   2,   3.42,  "넷",  "다섯",    6]
		 //   0       1      2      3      4     5    index
		 
		 
		 //읽는법 : "ArrayList 배열에서 객체를 하나씩 꺼내서  Object obj 변수에 담아가며 반복할 코드를 실행해라!
		 for(Object  obj  :   list  ) {
			 
			 System.out.println(obj);
		 }
		 
		 //향상된 for문은 컴파일하면 내부적으로 방법2(iterator/hasNext/next) 코드로 바뀐다.
		 //즉, 방법2를 짧게 쓰는 문법일 뿐 동작 원리는 완전히 같다.
		 //(원리를 알아야 하므로 방법2를 먼저 배우고, 실제 코딩에서는 방법3을 가장 많이 쓴다)
	
		/*
			결론 1 : iterator()는 객체들을 새 배열에 복사하는 것이 아니라,
			         원본 배열을 가리키는 주소와 cursor(다음에 꺼낼 index)를 가진
			         ArrayList$Itr 반복기 객체를 만들어 반환한다.

			결론 2 : hasNext() = "cursor 위치에 꺼낼 객체가 남았는가?(cursor < size)" true/false 반환
			         next()    = cursor 위치의 객체를 반환하고 cursor를 1 이동
			         공식 : while( it.hasNext() ) { it.next() }

			결론 3 : Iterator는 1회용이다. 처음부터 다시 반복하려면 iterator()를 다시 호출한다.
			         hasNext() 없이 next()를 남발하면 NoSuchElementException이 발생한다.

			결론 4 : 향상된 for문은 Iterator 방식을 짧게 쓰는 문법이며,
			         index가 없는 HashSet 등 모든 컬렉션에서 똑같이 사용할 수 있다.
		*/
		 
		 
		 

	}

}







