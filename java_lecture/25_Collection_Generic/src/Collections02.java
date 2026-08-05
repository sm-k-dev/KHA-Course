/*

ArrayList 클래스
- 부모 List인터페이스를 구현한 자식클래스로 가변으로 늘어나는 배열메모리를 만들때 사용.

- 내부적으로는 Object[] 배열을 사용하여 요소(객체)를 저장하며,
  배열의 칸이 부족할 때마다 자동으로 칸을 늘립니다.

- 아래는 ArrayList의 메모리 구조를 그림으로 표현한 것입니다.

+------------------------+
|    ArrayList      	 |
|------------------------|
|  elementData: Object[]    -> +---+---+---+---+---+
|  size: int         	 |     |   |   |   |   |   |
|  ...               	 |     +---+---+---+---+---+
+------------------------+       0   1   2   3   4   index

설명:
	1. ArrayList 객체 내부에는 두 가지가 들어 있습니다:

		- elementData: 실제 데이터를 저장하는 Object[] 배열입니다.

		- size: 현재 ArrayList에 저장된 요소(객체)의 개수를 나타냅니다.

	2. ★ 반드시 구분해야 하는 두 단어 : "칸 수"와 "저장 개수"

		- 칸 수(capacity)  : elementData 배열이 가지고 있는 전체 칸의 개수.
		                     (비어 있는 칸도 포함해서 센 것)

		- 저장 개수(size)  : 그 칸들 중에서 실제로 객체가 들어있는 개수.
		                     size() 메소드가 반환하는 값이 바로 이것.

		- 따라서 항상  size <= capacity  관계가 성립합니다.
		  예) 칸은 10칸인데 객체를 3개만 넣었다면 capacity=10, size=3

	3. 칸이 모자라면 어떻게 되는가? (자동 확장 규칙)

		- 칸이 가득 찬 상태에서 add하면, ArrayList가 기존의 1.5배 크기의
		  새 배열을 만들어 기존 객체들을 전부 복사해 옮긴 후 새 객체를 저장합니다.
		- 예) 2칸이 가득 찬 상태에서 add -> 2 x 1.5 = 3칸으로 확장
		      10칸이 가득 찬 상태에서 add -> 10 x 1.5 = 15칸으로 확장
		- 개발자는 이 과정을 직접 볼 수 없고, ArrayList가 자동으로 처리합니다.
		  그래서 "가변 길이 배열"이라고 부르는 것입니다.

*/
//주제 :  List 인터페이스를 구현한 자식ArrayList클래스의 배열메모리 만들기

import java.util.ArrayList;
import java.util.List;


public class Collections02 {
	public static void main(String[] args) {
		//=======================================
		//1. 기본 생성 : new ArrayList();
		//=======================================
		
						//기본 칸 수(10칸)의 Object[] 배열이 준비됩니다.
						//(정확히는 자바 8부터 메모리 절약을 위해 첫 add() 시점에 10칸이 만들어집니다.
						// 동작 결과는 같으므로 "기본 10칸"으로 이해하면 됩니다.)
		ArrayList list = new ArrayList();
						//[null, null, null, null, null, null, null, null, null, null]
						//  0     1     2     3     4     5     6     7     8     9    index
						//  capacity=10, size=0
				
				  list.add("apple");  //비어 있는 가장 앞칸 (0번 index)저장된다.
			   		   //["apple",null,null,null,null,null,null,null,null,null]
				  	   //  0       1    2    3    4    5    6    7    8    9   index
					   //  capacity=10, size=1
				  
				  list.add("banana"); //그 다음 칸(1번 index)에 저장된다
				      //["apple","banana",null,null,null,null,null,null,null,null]
				  	  //  0        1       2    3    4    5    6    7    8    9   index
					  //  capacity=10, size=2
				  
				  list.add("cherry"); //그 다음 칸(2번 index)에 저장된다
				   	  //["apple","banana","cherry",null,null,null,null,null,null,null]
				  	  //  0        1        2        3    4    5    6    7    8    9  index
					  //  capacity=10, size=3
				  
				  //ArrayList 가변 배열에 저장된 객체의 갯수 얻기 : size() 메소드 이용
				  //참고. size() 는 "칸 수 10"이 아니라  "실제 저장 객체 갯수 3"을 반환 한다는 점에 주의!!
				  System.out.println("위 ArrayList 가변 배열에 저장된 객체 갯수 : " + list.size());
				  					//위 ArrayList 가변 배열에 저장된 객체 갯수 : 3
				  
				  /*
				    위 코드의 메모리 구조는 다음과 같습니다.

					+-------------------+
					|    ArrayList      |
					|-------------------|
					|  elementData: Object[] |  ->  +---------+---------+---------+------+
					|  size: 3          |           | "apple" | "banana"| "cherry"| null |....... 뒤로 6칸이 더 있음
					|  ...              |           +---------+---------+---------+------+        (capacity=10)
					+-------------------+               0         1         2        3    index
				*/
				  
				//================================================================
				// 2. 칸 수를 지정한 생성 : new ArrayList(2);   +   자동 칸 확장 관찰  
				//=================================================================
				  
				  ArrayList  list2 = new ArrayList(2); //처음 부터 칸 수를 2칸으로 지정해서 배열 생성
				  /*
				    위 코드의 메모리 구조는 다음과 같습니다.

					+-------------------+
					|    ArrayList      |
					|-------------------|
					|  elementData: Object[] |  ->  +---------+---------+
					|  size: 0          |           |   null  |   null  |
					|  ...              |           +---------+---------+
					+-------------------+				0         1        index
														capacity=2, size=0
				 */				  
				  			list2.add("apple");  //0번 index 칸에 저장.  size=1
				  			list2.add("banana"); //1번 index 칸에 저장.  size=2
				  			list2.add("cherry"); //칸이 가득찬 상태에서 add!
				  								 // 2 X 1.5 = 3칸짜리 새 배열을 만들어 
				  								 // 기존 객체들을 복사해 옮긴 후 "cherry"를 저장한다.
			       /*
						       위 코드의 메모리 구조는 다음과 같습니다.

						   	+-------------------+
						   	|    ArrayList      |
						   	|-------------------|                                    1.5배 확장으로 생긴 칸
						   	|  elementData: Object[] |  ->  +---------+---------+---------+
						   	|  size: 3          |           |  "apple"| "banana"| "cherry"|
						   	|  ...              |           +---------+---------+---------+
						   	+-------------------+		        0          1         2      index
						   	                                    capacity=3, size=3
				   */ 
				  			
				  		System.out.println("ArrayList list2 가변 배열에 저장된 객체의 갯수 : " + list2.size() ); // 3
				  		
				  		System.out.println("ArrayList list2 가변 배열의 모습을 문자열로 변환해서 반환받아 출력");
				  		System.out.println(list2.toString()); //" [apple, banana, cherry] "
				  		// 중요!  HashSet 배열과 비교  : add 추가한 순서 그대로 "apple"-> "banana" -> "cherry" 출력된다.
				  		//       ArrayList 배열은  "추가한 순서대로 각 칸에 저장"이 보장된다.
				  		
			//==================================================================================
			// 3. 업캐스팅 생성  :  List 부모 인터페이스의 참조변수에 자식 ArrayList 배열을 생성해서 저장 후 사용	  		
			//=================================================================================
				  		
					 //ArrayList 클래스의 부모 인터페이스는 List 입니다.
					 //업캐스팅을 통한 ArrayList배열 메모리 생성 가능
					 //(부모 자료형으로 받아 두면 나중에 ArrayList를 LinkedList 등
					 // 다른 자식으로 교체할 때 이 한 줄만 고치면 되므로 실무 표준 작성법이다)	
			  		 
				  	 //업캐스팅
				  	 List  list3 = new ArrayList(2);  //칸 2개짜리 배열이 처음에 만들어지고,
				  	 								  //칸이 가득 찬 상태에서 새로운 객체를 추가하면 1.5배로 칸이 늘어난다.
				  	 
					 //				  ----------배열 메모리 주소 0x16-------
					 //		[0x16] =  [  null    ][  null    ]
					 //						0          1       index
				  	 
				  	       list3.add(1);    // 1 -> Integer.valueOf(1) 오토 박싱이 일어나 Integer 래퍼 객체로 추가됨
				  	       list3.add("안녕");//"안녕" 문자열 객체 메모리 추가 
				  	       list3.add(true); //true -> Boolean.valueOf(true) 오토 박싱이 일어나 Boolean 래퍼 객체로 추가됨
				  	       
					 		//★ 이처럼 자료형을 지정하지 않은 ArrayList에는 아무 자료형의 객체나 섞어 담을 수 있다.
					 		//  (편해 보이지만 꺼낼 때 자료형 구분 문제가 생긴다. 해결책은 다음 단원 "제네릭"에서 배운다)
				  		
					 //				  ----------배열 메모리 주소 0x16 (2칸 -> 3칸으로 확장됨)-------
					 //		[0x16] =  [Integer.valueOf(1)][   "안녕"   ][Boolean.valueOf(true)]
					 //						0          	     1                2                 index	
		  			
				  	 System.out.print("위 List list3 참조변수에 저장된 new ArrayList(2); 가번 배열의 각칸에 저장된 객체 갯수 : ");
				  	 System.out.println(list3.size());  // 3개 
				  	 
					  /*
						List 부모인터페이스로 구현한 자식클래스배열의 객체(요소)는 위치(index)를 이용해서 접근합니다.

						List 부모인터페이스는 List의 부모인 Collection 인터페이스로 부터 상속되었으므로

						Collection 인터페이스에서 제공하는 메소드들(add, size, remove 등)을 모두 사용할수 있으며

						"index로 접근하는" 다음과 같은 메소드가 더 추가되었습니다.

						※ 표에 나오는 E는 "배열에 저장된 객체의 자료형"이라는 뜻의 표기입니다.
						   (제네릭 문법인데 아직 배우지 않았으므로, 지금은 자료형을 지정하지 않았기 때문에
						    E 자리를 전부 Object로 읽으면 됩니다. 예: E get(int index) -> Object get(int index))

							메소드 						설명
							int indexOf(Object o)      전달인자로 준 객체를 앞(0번 index)에서부터 찾아 해당 위치(index)를 반환함
													   찾지 못하면 -1을 반환함.

							int lastIndexOf(Object o)  저장된 객체를 배열의 마지막 index위치부터 거꾸로 찾음
													   찾으면 해당 위치(index)를 반환하고 찾지 못하면 -1을 반환함

							E   get(int index)		   매개변수 index로 전달한 index위치에 저장된 객체를 반환

							E	set(int index,  E  element)
													   매개변수 index로 전달한 index위치에 저장된 객체를
													   매개변수 E element로 전달한 객체로 대체(교체) 하는 메소드
													   대체되기 전의 기존 객체를 반환함

							void add(int index, E  element)
													   매개변수 index로 전달한 index위치에
													   매개변수 E element로 전달한 객체를 저장합니다.
													   매개변수 index로 전달한 index위치에 저장된 기존의 객체는
													   뒤로 밀려나 그다음 index위치에 저장되게 됩니다.

							E   remove(int index)      매개변수 index로 전달한 index위치에 저장된 객체를 삭제하고
													   삭제된 그 객체를 반환함.
													   뒤에 있던 객체들은 앞으로 한 칸씩 당겨진다.
													   (존재하지 않는 index를 주면 IndexOutOfBoundsException 발생)

							boolean remove(Object o)   매개변수로 전달하는 객체를 앞에서부터 찾아 첫 번째 것을 삭제하는 메소드
													   삭제에 성공하면 true반환, 배열에 없어서 실패하면 false반환
					*/				  	 
				  	 
				//=====================================================================
				// 4. 위 표의 메소드들을 실제로 하나씩 실행해서 눈으로 확인하기
				//=====================================================================	  	 
				  	 
				 List  list4 = new ArrayList();  
				 list4.add("사과");
				 list4.add("포도");
				 list4.add("수박");
				 list4.add("포도"); //List는 중복 저장 허용! ("포도"가 2개 저장된다. Set이었다면 거부됐다)
				 
				//  ["사과", "포도", "수박", "포도"]
				//    0       1       2      3     index				 
				 
				 System.out.println("\n====== List 인터페이스 추상메소드 실습 ========");
				 System.out.println("현재 배열 : " + list4);  // "[사과, 포도, 수박, 포도]"
				 //												  0    1    2    3   index
				 
				 //(1) 	int indexOf(Object o)      전달인자로 준 객체를 앞(0번 index)에서부터 찾아 해당 위치(index)를 반환함
				 //                                찾지 못하면 -1을 반환함.
				 System.out.println("indexOf(\"포도\")  -> " +  list4.indexOf("포도") ); //1 (앞에서부터 찾으므로 3번이 아닌 1번)
				 
				 
				//(2)  int lastIndexOf(Object o)  저장된 객체를 배열의 마지막 index위치부터 거꾸로 찾음
				//	   							  찾으면 해당 위치(index)를 반환하고 찾지 못하면 -1을 반환함
				System.out.println("lastIndexOf(\"포도\")  -> " + list4.lastIndexOf("포도") ); //3 (뒤에서 부터 찾으므로 3번)
				
				//(3)  int indexOf(Object o) 를 사용하여 ArrayList 배열에 저장되어 있지 않는 객체 찾기
				System.out.println("indexOf(\"멜론\")  -> "  + list4.indexOf("멜론")  ); //배열에서 찾지 못하면 -1 반환
				
				 // "[사과, 포도, 수박, 포도]"
				 //     0    1    2    3   index
				
				//(4)  E   get(int index)		   매개변수 index 로 전달한 index 위치에 저장된 객체를 반환
				System.out.println("get(2)           -> " + list4.get(2)); //"수박"
					  
				//(5) E	set(int index,  E  element)
				//   								매개변수 index로 전달한 index위치에 저장된 객체를
				//   								매개변수 E element로 전달한 객체로 대체(교체) 하는 메소드
				//   								대체되기 전의 기존 객체를 반환함
				Object old = list4.set(0, "딸기"); // 0번 index 위치 칸에 저장어 있는 "사과" 를 "딸기"로 교체 
												  //  교체 되기 전! "사과" 객체를 반환 합니다.
				System.out.println("set(0,\"딸기\") 의 반환 값(교체 전 객체) -> " + old); //"사과"
				System.out.println("set 메소드 실행 후 배열 모습 : " + list4);           //"[딸기, 포도, 수박, 포도]"
																					//   0    1   2    3   index
				
				//(6) void add(int index, E  element)
				//   									매개변수 index로 전달한 index위치에
				//   									매개변수 E element로 전달한 객체를 저장합니다.
				//   									매개변수 index로 전달한 index위치에 저장된 기존의 객체는
				//   									뒤로 밀려나 그다음 index위치에 저장되게 됩니다.
				list4.add(1, "귤");
				System.out.println("add(1, \"귤\") 실행 후 배열 모습 : " + list4); //"[딸기, 귤, 포도, 수박, 포도]"
				
				// "포도"(1번)부터 뒤에 있던 객체들이 전부 한 칸씩 뒤로 밀렸다		
				//  [딸기,  귤,   포도,  수박,  포도]
				//   0     1     2      3      4    index
				
				//(7)  E   remove(int index)      매개변수 index로 전달한 index위치에 저장된 객체를 삭제하고
				//   							  삭제된 그 객체를 반환함.
				//   							  뒤에 있던 객체들은 앞으로 한 칸씩 당겨진다.
				//  							  (존재하지 않는 index를 주면 IndexOutOfBoundsException 발생)
				Object removed = list4.remove(3); //3번 index 위치 칸에 저장된 "수박" 삭제 
												  //삭제 시도한 "수박" 객체를 반환 
				System.out.println("remove(3) 의 반환값(삭제된 객체) -> " + removed); //"수박"
				System.out.println("remove(3) 실행 후 배열 모습 -> " + list4); //"[딸기, 귤, 포도, 포도]"
				
				//참고. 4번 index 위치칸에 저장되어 있었던 포도 가  앞칸으로 당겨져서 3번 index 위치칸에 자동 저장된다. 그후 4번 index 위치칸 없어짐
				//  [딸기,  귤,   포도,  포도]
				//   0     1     2      3      index
				
				//(8) remove(Object o) : 배열에서 객체를 찾아 첫 번째 것만 삭제. 성공 true / 실패 false 반환  	 
				boolean ok1 = list4.remove("포도");  //앞쪽(2번 index 위치칸)에 저장된 "포도" 하나만 삭제 
				System.out.println("remove(\"포도\") 의 반환 값 -> " + ok1);//true  <- 삭제 성공 
				System.out.println("현재 ArrayList 배열 모습 : " + list4);  //"[딸기, 귤, 포도]"
				
				//[딸기, 귤, 포도]
				//  0   1   2    index
				
				boolean ok2 = list4.remove("멜론"); //"멜론" 은 배열에 없으므로 삭제 실패 값 false 를 반환 
				System.out.println("remove(\"멜론\") 의 반환 값 -> " + ok2); //false  <- 삭제 실패 
				System.out.println("현재 ArrayList 배열 모습 : " + list4);  //"[딸기, 귤, 포도]"

			/*
				★ remove 사용 시 주의 (시험 단골)
				- list4.remove(3)      : int를 주면    -> "3번 index 위치의 객체" 삭제
				- list4.remove("포도") : 객체를 주면   -> "포도라는 객체"를 찾아서 삭제
				  같은 이름의 메소드지만 매개변수 자료형에 따라 다르게 동작한다. (오버로딩)
			*/

			/*
				결론 1 : ArrayList는 내부의 Object[] 배열에 객체를 저장하며,
				         칸이 가득 차면 1.5배 크기의 새 배열로 자동 확장된다.
				         칸 수(capacity)와 저장 개수(size)는 다른 것이며 size()는 저장 개수를 반환한다.

				결론 2 : List 계열은 Set과 다르게
				         ① 중복 저장을 허용하고 ("포도" 2개 저장 확인)
				         ② add한 순서 그대로 저장되며
				         ③ index(위치 번호)로 객체에 접근한다. (get, set, add, remove)

				결론 3 : set과 remove(int index)는 "기존/삭제된 객체를 반환"하고,
				             remove(Object o)는 "성공 여부 true/false를 반환"한다.
				             indexOf는 앞에서부터, lastIndexOf는 뒤에서부터 찾으며 없으면 -1이다.
			*/		
				
				
	}

}







