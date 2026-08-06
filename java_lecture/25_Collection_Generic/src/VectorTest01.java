/*
	주제 :  Vector클래스를 이용해 가변으로 칸이 생성되어 저장 되는 배열메모리 생성.

	■ 이 파일에서 확인할 것
	   ① capacity(칸 수)와 size(저장 개수)의 차이를 메소드로 직접 확인
	   ② 칸이 가득 찬 상태에서 add하는 "확장의 순간"에 무슨 일이 일어나는지 추적
	   ③ Vector에만 있는 전용 메소드 firstElement(), lastElement() 사용

	■ Vector 생성자 3종류 비교 (칸 수와 증가 방식이 달라진다)

	   생성 코드                  처음 칸 수      칸이 가득 찼을 때 증가 방식
	   ─────────────────────────────────────────────────────────────
	   new Vector()               10칸           2배로 증가 (10 -> 20)
	   new Vector(4)               4칸           2배로 증가 (4 -> 8)
	   new Vector(4, 3)            4칸           3칸씩 증가 (4 -> 7)   <- 이 파일에서 사용
	                                  └ 두 번째 숫자(capacityIncrement)를 지정하면
	                                    2배 방식 대신 그 값만큼씩 증가한다
*/

import java.util.Vector;

public class VectorTest01 {

	public static void main(String[] args) {
		
		/*
		  1.  4개의 객체(요소)를 저장할 수 있는 가변으로 칸이 생성되어 저장되는 Vector 클래스의 객체 생성
		  2.  그리고 Vector 가변 배멸메모리의 각 칸에  객체들이 모두 저장된 상태에서
		      add 메소드를 한번 호출해서 새로운 객체를 추가 하면 3칸씩 가변으로 생성되게 하는 Vector 클래스의 객체 생성
		*/
		Vector  vector = new Vector(4, 3);
					   //           │  └ capacityIncrement : 가득 찼을 때 늘릴 칸 수
					   //           └ 처음 칸 수(용량)
						//[    ][    ][    ][    ]
						//   0     1     2     3    index
						//   capacity=4, size=0
		/*
		   Vector 가변 배열 메모리 전체의 칸 갯수(용량) 얻기
		   -> int capacity() 메소드 사용
		   ★ capacity()는 Vector 에만 있는 메소드다. (ArrayList에는 칸 수를 알려주는 메소드가 없다)	  
		 */
		System.out.println("Vector 배열 칸 갯수(용량) : " + vector.capacity() + "칸");
		//                  Vector 배열 칸 갯수(용량) : 4칸
		
		/*
		 	Vector 가변 배열 메모리 전체 칸에 저장된 객체의 갯수 얻기 
		 	->  int size() 메소드 사용
		 	
 			   ★ 아직 add를 한 번도 안 했으므로 칸은 4칸이지만 저장 개수는 0개다.
		       (칸 수와 저장 개수는 다른 것이다!)
		 */
		System.out.println("Vector 배열 각 칸에 저장된 객체 수 : " + vector.size() + "개");
		//				   Vector 배열 각 칸에 저장된 객체 수  : 0개
		
		//Vector 배열 메모리 현재 모습 
		//[    ][    ][    ][    ]
		//   0     1     2     3    index
		//   capacity=4, size=0
		
		//일반 for 반복문을 이용하여 Vector 배열 메모리의 각칸에 객체를 반복해서 저장
		for(int i=0;  i<5;  i++) {
			
			vector.add(  i  *  10);  //i*10은 int이므로 오토 박싱되어
									 //Integer.valueOf(i*10) 래퍼 객체로 저장된다
									 //0 * 10 -> 0
									 //1 * 10 -> 10
									 //2 * 10 -> 20
									 //3 * 10 -> 30
									 //4 * 10 -> 40
									 //i가 5가 되는 순간 for 종료
		}
		/*
		 ★ 위 for문이 도는 동안 배열에서 일어난 일 추적 (확장의 순간을 잡아라!)

		   add 회차   저장한 값     저장 직전 상태          무슨 일이 일어났나
		   ──────────────────────────────────────────────────────────────────
		    1회        0          capacity4, size0       0번 칸에 저장 -> size1
		    2회       10          capacity4, size1       1번 칸에 저장 -> size2
		    3회       20          capacity4, size2       2번 칸에 저장 -> size3
		    4회       30          capacity4, size3       3번 칸에 저장 -> size4  ★4칸이 가득 참!
		    5회       40          capacity4, size4       빈 칸이 없다!
		                                                 -> 4+3=7칸짜리 새 배열을 만들고
		                                                    기존 객체 4개를 전부 복사해 옮긴 후
		                                                    4번 칸에 40 저장 -> capacity7, size5

		   확장 후의 배열 모습:
		   [ 0 ]  [ 10 ]  [ 20 ]  [ 30 ]  [ 40 ]  [    ]  [    ]
		     0      1       2       3       4       5       6    index
		                                  └──── 3칸씩 증가 설정으로 늘어난 칸 ────┘
		   capacity=7, size=5
		*/		
		/*
			Vector 가변 배열 메모리 전체의 칸 갯수(용량) 얻기
			-> 확장이 일어 났으므로  4칸이 아니라 7칸이 된다.  ( 4  +  3  = 7)
		*/
		System.out.println("Vector 배열 칸 갯수(용량) : " + vector.capacity() + "칸");
		//                 Vector 배열 칸 갯수(용량) : 7칸
		
		/*
			Vector 가변 배열 메모리 전체의 칸에 저장된 객체의 갯수 얻기
			-> 위 for 를 이용해  add 메소드로 추가를 5번 했으므로 5개가 반환된다. (빈칸 2개는 세지 않는다)
		*/
		System.out.println("Vector 배열 각 칸에 저장된 객체 수 : " + vector.size() + "개");
		//                 Vector 배열 각 칸에 저장된 객체 수 : 5개
		
		
		/*
		   확장 후의 Vector 배열 모습:
		   [ 0 ]  [ 10 ]  [ 20 ]  [ 30 ]  [ 40 ]  [    ]  [    ]
		     0      1       2       3       4       5       6    index
		                                  └──── 3칸씩 증가 설정으로 늘어난 칸 ────┘
		   capacity=7, size=5
		*/
		
		//★ 반복 조건에 capacity()가 아니라 size()를 쓰는 이유:
		//  capacity()인 7로 반복하면 비어 있는 5, 6번 칸까지 get하게 되어
		//  IndexOutOfBoundsException이 발생하기 때문이다. 반복은 항상 size() 기준!
		for(int i=0;  i<vector.size();  i++) {
			//i<5;  -> i가 5가 되는 순간 for 반복 종료
			
			System.out.print("   " + vector.get(i) );
								  // vector.get(0) 일때  0  얻기
								  // vector.get(1) 일때  10 얻기
								  // vector.get(2) 일때  20 얻기
								  // vector.get(3) 일때  30 얻기
								  // vector.get(4) 일때  40 얻기
		   //   0   10   20   30   40
		}
		
		System.out.println();  //한줄 줄바꿈 출력

//Vector 배열 메모리 
//		 [ 0 ]  [ 10 ]  [ 20 ]  [ 30 ]  [ 40 ]  [    ]  [    ]
//		   0      1       2       3       4       5       6    index	
		
		/*
		Vector 배열 메모리의 마지막으로 저장된 40 Integer객체 얻기(반환 받기, 리턴 받기)
		-> Object lastElement() 메소드 사용
		★ "마지막"의 기준은 칸(6번)이 아니라 저장된 객체(size-1 = 4번 칸)다.
		★ firstElement, lastElement는 Vector 전용 메소드다. (ArrayList에는 없다.
		  ArrayList에서는 get(0), get(list.size()-1) 로 같은 결과를 얻는다)
		*/		
		System.out.println( vector.lastElement()  );  //40
							//4번 칸의 Integer.valueOf(40) 객체가 반환됨
		
		/*
		Vector 배열 메모리의 첫번째로 저장된 0 Integer객체 얻기(반환 받기, 리턴받기)
		-> Object firstElement() 메소드 사용
		*/
		System.out.println( vector.firstElement() ); //0
							//0번 칸의 Integer.valueOf(0) 객체가 반환됨
		
		/*
		 ⚠ 주의 : 배열이 비어 있을 때(size가 0일 때)
		   firstElement()나 lastElement()를 호출하면
		   NoSuchElementException 예외가 발생하며 프로그램이 종료된다.
		   -> 호출 전에 size()가 0인지 확인하는 습관이 필요하다.
		*/

		/*
			결론 1 : new Vector(처음칸수, 증가량) 으로 생성하면 칸이 가득 찼을 때
			         2배가 아니라 지정한 증가량만큼씩 늘어난다. (4칸 -> 4+3=7칸 확인)

			결론 2 : capacity()는 전체 칸 수, size()는 실제 저장 개수를 반환한다.
			         반복문의 조건은 반드시 size() 기준으로 작성한다.
			         (capacity 기준으로 돌리면 빈 칸 get으로 IndexOutOfBoundsException)

			결론 3 : firstElement(), lastElement(), capacity()는 Vector 전용 메소드다.
			         빈 배열에서 firstElement/lastElement 호출 시 NoSuchElementException이 난다.
		*/		
		
	}

}
/*
참고.
		java에서 Vector가변길이배열은 동적으로 크기가 조정되는 배열입니다
		즉 배열 처럼 사용되지만, ArrayList와 달리 내부 메소드들이 동기화(synchronized)되어 있어
		멀티 스레딩 환경에서 안전합니다. (스레드 단원에서 배운 synchronized가 붙어 있는 것)
		그러나 스레드들이 줄을 서며 생기는 성능 문제로 요즘은 주로 ArrayList를 사용하는 경우가 많습니다

*/









