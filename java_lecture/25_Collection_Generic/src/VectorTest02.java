

//주제 :  Vector 배열 메모리에 저장된 객체 얻기(객체 검색) 및 객체 삭제
//
//	■ 이 파일에서 확인할 것
//	   ① indexOf / contains 로 배열에서 객체를 검색하는 방법
//	   ② 검색이 "주소 비교"가 아니라 "equals(내용) 비교"로 동작한다는 원리 ★가장 중요
//	   ③ remove(Object)로 객체를 삭제한 후 배열에 일어나는 변화 (앞당김, capacity 유지)

import java.util.Enumeration;
import java.util.Vector;

public class VectorTest02 {

	public static void main(String[] args) {
		
		Vector  vector = new Vector();
	    /*
	     [1] 객체 생성 단계

	     [Heap 영역]
	     new Vector() 실행 결과
	     ┌────────────────────────────────────────────┐
	     │ Vector 객체                                 │
	     │--------------------------------------------│
	     │ elementCount = 0      ← 저장된 요소 개수       │
	     │ capacityIncrement = 0 ← 증가 단위            │
	     │ elementData = Object[10]                   │
	     │                                            │
	     │ index :   0  1  2  3  4  5  6   7  8    9  │
	     │ value : null null null null null null null │
	     └────────────────────────────────────────────┘

	     ※ capacityIncrement = 0
	       → 공간이 꽉 차면 기존 크기의 2배로 증가 (VectorTest01에서 배운 규칙)
	     */		
		
		System.out.println(vector.capacity());  //총 10 칸 
	    /*
	     capacity()
	     → elementData.length 반환
	     → 현재 Object[10]
	     → 출력값 : 10
	     */
		
		double[]  arr = new double[] { 38.6,  9.2,   45.3,   6.1,  4.7,  1.6 };
		//								0      1       2      3     4     5     index
	    /*
	     [Heap 영역]
	     double 배열 생성
	     ┌──────┬─────┬──────┬─────┬─────┬─────┐
	     │ 38.6 │ 9.2 │ 45.3 │ 6.1 │ 4.7 │ 1.6 │
	     └──────┴─────┴──────┴─────┴─────┴─────┘
	        0      1     2      3     4     5

	     ※ 기본자료형 double 배열 (객체가 아닌 실수값이 칸에 직접 저장됨)
	     ※ Vector와는 아직 아무 관계 없음
	     */		
		
		//arr 배열에 저장된 모든 실수 데이터들을 차례대로 반복해서 얻어
		//위 new Vector(); 로 생성한 가변길이 배열의 각 칸에 차례대로 오토박싱 해서 저장
		for(int i=0;  i<arr.length;   i++) {
			
			//기본자료형 double은 객체가 아니므로 Vector 배열에 바로 저장할수 없다.
			//컴파일러가 Double.valueOf(꺼내 온 실수값); 을 자동 호출해  Double 래퍼 객체로 포장한 후 저장한다.(= 오토박싱)
			vector.add(         arr[i]    			);
							 // arr[0] -> 38.6 꺼내옴 -> Vector배열의 0 index위치 칸에 추가
						 	 // arr[1] -> 9.2 꺼내옴  -> Vector배열의 1 index위치 칸에 추가 
						 	 //......
						 
		}
	    /*
	     [for문 종료 후 Vector 내부 상태]

	     elementData (Object[])
	     ┌──────┬──────┬──────┬──────┬──────┬──────┬──────┐
	     │38.6  │ 9.2  │45.3  │ 6.1  │ 4.7  │ 1.6  │ null │
	     └──────┴──────┴──────┴──────┴──────┴──────┴──────┘
	       0      1      2      3      4      5      6    ...... 3칸 더 존재

	     ★ 각 칸에는 실수값이 직접 저장된 것이 아니다!
	       → 힙에 만들어진 Double 래퍼 객체의 "주소"가 저장되어 있다
	       (그림에는 보기 쉽게 값을 적었지만 실제로는 주소)

	     elementCount = 저장된 객체수 6 개
	     capacity = 10 칸
	    */
		
		//일반 for 반복문을 활용하여  Vector 배열의 각 칸에 저장된 객체 갯수만큼 반복해서 얻어 출력
		for(int i=0;  i<vector.size();   i++) {
			
			//	38.6	9.2   45.3    6.1    4.7    1.6
			System.out.print("\t" + vector.get(i));
	        /*
	         vector.get(i)
	         → elementData[i] 에 저장된 주소 반환
	         → Double 객체
	         → print 시 Double 객체의 toString()이 자동 호출되어 "38.6" 형태의 문자열로 출력됨
           */
		}
		
		System.out.println(); //한줄 줄바꿈 출력 후 아래의 코드 실행
		
		//============================================
		// 검색 : indexOf 메소드
		//===========================================
		
		//1. Vector 배열 메모리에서 검색할 기본자료형 실수값을 변수에 저장
		double searchData = 6.1;
/*
 Vector 배열 모습 		
	     ┌──────┬──────┬──────┬──────┬──────┬──────┬──────┐
	     │38.6  │ 9.2  │45.3  │ 6.1  │ 4.7  │ 1.6  │ null │
	     └──────┴──────┴──────┴──────┴──────┴──────┴──────┘
	       0      1      2      3      4      5      6    ...... 3칸 더 존재
*/		
		//2. searchData변수에 저장된 6.1을 오토 박싱하여 indexOf메소드 호출할때! 매개변수로 전달하면
		//   Vector 배열의 가장 앞칸 부터 차례로 비교해서 6.1이 저장된 칸을 찾으면
		//   저장된 칸의 index 위치 번호 하나를 반환 해 줍니다.
		//   만약 저장되어 있는 않으면? indexOf 메소드는 -1 을 반환합니다.
		int index = vector.indexOf(searchData);
		//    3
		
		 /*
		  ★ 검색의 원리 : 주소 비교가 아니라 equals(내용) 비교다! (정확하게 이해하기)

		    지금 전달한 것은 방금 오토 박싱으로 새로 만들어진 Double 객체이고,
		    배열 3번 칸에 있는 것은 for문에서 저장할 때 만들어진 다른 Double 객체다.
		    -> 두 객체는 힙의 서로 다른 위치에 있으므로 "주소"는 전혀 다르다!

		    그런데도 indexOf가 3번 칸을 찾아내는 이유:
		    indexOf는 칸을 하나씩 돌면서 주소가 아니라
		    equals() 메소드로 "내용(값)이 같은가"를 비교하기 때문이다.
		        전달한 Double(6.1).equals( 각 칸의 객체 )  -> 3번 칸에서 true!

		    ※ Object 단원에서 배운 내용과 연결 : ==는 주소 비교, equals()는 내용 비교.
		      indexOf, contains, remove(Object)는 전부 equals() 비교로 동작한다.
		 */
		//3. 검색 결과 index 가  -1 이 아니면  =  찾았다면 이라는 의믜
		if(index  !=  -1) {
			System.out.println("검색 성공! : " + index + " index 위치 칸에 검색할 데이터 " + searchData + "가 존재");
		}else { //검색 실패 했다면?
			System.out.println("검색 실패! : " + index);
											 // -1
		}
	 /*
	    참고. Vector클래스가 제공하는 메소드

		 	int indexOf(Object o)
		 	-> 전달인자로 준 객체를 배열의 앞 index위치에서 부터
		 	   equals() 비교로 검색해서 찾습니다. 만약 찾으면 해당 index번호 반환
		 	   만약 찾지 못하면 -1을 반환

		참고. Vector 클래스에서 제공하는 메소드

			 boolean contains(Object obj)

			 -> Vector 배열메모리에 매개변수 obj로 전달한 객체와 내용이 같은(equals) 객체가
			    저장되어 있느냐? 라고 물어보는 메소드로
			    저장되어 있으면 true 반환, 저장되어 있지 않으면 false를 반환 하는 메소드.

			 -> indexOf와의 차이 : indexOf는 "몇 번 칸인지(위치)"까지 알려주고,
			                      contains는 "있는지 없는지(true/false)"만 알려준다.
	 */		
	 //=====================================================================
	 // 삭제 : contains로 확인 후 remove(Object)로 삭제
	 //=====================================================================
		
		//1. Vector 배열 메모리에 저장된 데이터 중에서 삭제할 실수값을 변수에 저장
		double delData = 45.3;
		
		//2. Vector 배열 메모리에 45.3과 같은 내용이 포함된 Double레퍼 객체가 저장되어 있는가? 물어보고! 저장되어 있으면? 삭제
		if(  vector.contains(delData)  ) {
				  // true 반환 받으면? if 조건식 참 
			
			 //저장되어 있기 떄문에 삭제하자.
			 //참고.  boolean remove(Object obj) 메소드
			 //		-> 매개변수 obj로 전달한 객체와 내용이 같은(equals) 객체를
			 //		   배열의 앞에서부터 찾아 첫 번째 것을 삭제 한 후
			 //		   삭제에 성공하면 true반환, 삭제에 실패 하면 false 반환 하는 메소드
			 boolean result = vector.remove(delData);
			 
			 if(result) System.out.println("삭제 성공");
			 else       System.out.println("삭제 실패");
		}
		 /*
		  ⚠ remove 오버로딩 주의 (시험 단골)
		    remove는 같은 이름의 메소드가 2개 있다. 매개변수 자료형으로 구분된다!

		       vector.remove(delData); // double을 주면 -> Double로 오토박싱 -> remove(Object) 호출
		                               //   "45.3이라는 내용의 객체"를 찾아서 삭제
		                               //
		       vector.remove(2);       // int를 주면 -> 박싱 없이 -> remove(int index) 호출
		                               //   "2번 index 칸의 객체"를 삭제 (45.3을 찾는 게 아님!)

		    -> 정수값을 객체로 찾아 지우고 싶다면 remove(Integer.valueOf(2)) 처럼
		       직접 박싱해서 전달해야 remove(Object obj)가 호출된다.
		 */		
		
//삭제전 -> Vector배열
//		38.6	9.2	  45.3	 6.1  4.7	1.6
//	   	 0       1      2     3    4     5     6   7    8   9 index

//삭제후 -> Vector배열
//		 38.6	9.2	   6.1	  4.7  1.6
//         0       1      2     3    4     5     6   7    8   9 index
		
//	 ★ 45.3이 있던 2번 칸이 비면, 뒤에 있던 객체들(6.1, 4.7, 1.6)이
//	   전부 앞으로 한 칸씩 당겨져 저장된다. (중간에 빈 칸을 남기지 않는다)		
		
		System.out.println("Vector 배열 메모리의 전체 칸의 갯수 : " + vector.capacity()); //10  칸 
		System.out.println("Vector 배열 메모리의 각칸에 저장된 객체 전체 갯수 : " +  vector.size());  //5  개 
		 //★ 삭제하면 size는 6 -> 5로 줄지만, capacity는 10칸 그대로다!
		 //  (칸은 한 번 늘어나면 삭제한다고 해서 자동으로 줄어들지 않는다)
		
		
//삭제후 -> Vector배열
//		 38.6	9.2	   6.1	  4.7  1.6
//        0       1      2     3    4     5     6   7    8   9 index		
		
		Enumeration enu = vector.elements();
		
		while(enu.hasMoreElements()) {
			
			System.out.println( enu.nextElement() );
//										38.6
//										9.2
//										6.1
//										4.7
//										1.6

			

			/*
				결론 1 : indexOf(위치 반환, 없으면 -1) / contains(있는지 true/false) /
				         remove(Object)(첫 번째 것 삭제 후 성공 여부 반환)는
				         전부 주소가 아니라 equals(내용) 비교로 객체를 찾는다.

				결론 2 : remove는 오버로딩되어 있다.
				         실수/객체를 주면 remove(Object) = 내용으로 찾아 삭제,
				         정수를 주면 remove(int) = 그 index 칸을 삭제. 혼동 주의!

				결론 3 : 객체를 삭제하면 뒤의 객체들이 앞으로 한 칸씩 당겨지고 size는 줄지만,
				         capacity(칸 수)는 그대로 유지된다.
			*/			
			
		}
		
	}

}






