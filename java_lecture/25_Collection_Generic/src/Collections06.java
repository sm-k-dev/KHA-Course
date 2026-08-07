/*

	주제 : Vector 배열 객체 생성시 <> 기호와 함께 String 제네릭 타입을 지정하여 -> <String>
		  Vector 배열 객체를 생성 함으로써 Vector에 저장되는 객체가 String객체만 저장되기 때문에
		  Vector의 get 메소드를 호출하면 반환타입이 String으로 변경되어
		  다운캐스팅 코드를 작성하지 않아도 되는 예

	■ 용어 정리
	   - < >  : 제네릭 타입을 지정하는 기호. (정식 명칭은 다이아몬드 기호)
	   - <String> : "이 배열에는 String 클래스의 객체만 저장한다"라고 자료형을 지정하는 문법.
	   - 이렇게 자료형을 지정하는 기법을 "제네릭(Generic) 기법"이라고 한다.

	■ 제네릭의 동작 원리 : E가 String으로 치환(변경)된다

	   Vector 클래스의 설계도 내부에는 자료형이 결정되지 않은 자리에 E라고 적혀 있다.

	       [Vector 클래스 내부 원본]              [new Vector<String>() 생성 순간]
	       boolean add( E e )          ──▶       boolean add( String e )
	       E       get( int index )    ──▶       String  get( int index )
	       E       remove( int index ) ──▶       String  remove( int index )

	   즉 <String>을 지정하는 순간 E라고 적혀 있던 모든 자리가 String으로 바뀐 것처럼 동작한다.
	   그래서 ① add는 String만 받게 되고  ② get은 String으로 반환하게 된다.

	■ Collections05의 두 가지 문제가 해결되는 방식 (앞 파일과 연결)

	   문제① 꺼낼 때마다 (String) 다운캐스팅을 매번 써야 한다.
	       -> 해결 : get의 반환타입 자체가 String이 되므로 다운캐스팅이 필요 없다.

	   문제② 잘못된 자료형을 add해도 컴파일러가 못 잡고 실행 중 ClassCastException으로 터진다.
	       -> 해결 : add의 매개변수 타입이 String이 되므로
	                 String이 아닌 객체를 add하는 코드는 그 즉시 컴파일 에러로 잡아낸다.
	                 (실행 전에 실수를 발견 = 제네릭의 가장 큰 장점)
*/

import java.util.List;
import java.util.Vector;

public class Collections06 { //<- Collections05.java 파일에 작성된 class Collections05{} 를 업그레이드 한 클래스
	public static void main(String[] args) {

		//업캐스팅을 하여 Vector 배열 메모리 생성
		//- Vector 배열 메모리를 생성 할 때 <> 기호와 함께 String 클래스의 객체만 저장될수 있도록 제네릭타입을 String 으로 작성하면
		//	Vector 클래스 내부에 E라고 적혀 있던 아직 결정되지 않은 반환타입 그리고 메소드의 매개변수 타입(자료형)이 String 으로 변경되어 바뀌므로
		//  항상 무조건 Vector 배열 내부에는 String 클래스의 객체만 저장됨을 보장합니다.
		//- 주의 : 참조변수 쪽(List<String>)과 객체 생성 쪽(new Vector<String>()) 양쪽 모두 <String>을 붙인다.
		List<String>   vector =  new Vector<String>();
		//참고 : 자바 7부터는 생성 쪽의 자료형을 생략하고 List<String> vector = new Vector<>(); 로 줄여 쓸 수 있다.
		//       (왼쪽에 이미 <String>이 있으므로 컴파일러가 오른쪽 <>를 String으로 알아서 채워 준다)

					   vector.add("Apple");   //String 객체이므로 저장 가능
					   vector.add("banana");  //String 객체이므로 저장 가능
					   vector.add("oRANGE");  //String 객체이므로 저장 가능

					   //vector.add(100);  <- 컴파일 에러가 발생하는 코드! (주석을 풀어 직접 확인해 볼 것)
					   /*
					      에러가 나는 이유 : <String>을 지정했으므로 add의 매개변수 타입은
					      boolean add(String e) 로 변경되어 있다. 100은 Integer로 오토 박싱되는
					      숫자이지 String이 아니므로 저장을 시도하는 즉시 컴파일 에러가 난다.
					      -> Collections05의 문제②(실행 중 ClassCastException)가
					         "실행 전 컴파일 에러"로 바뀌어 실수를 미리 잡아준다!
					   */

						//["Apple","banana","oRANGE"] <- Vector배열 (String 객체만 저장됨이 보장된 상태)
						//   0        1        2      index

		String temp; //<String> 제네릭 기법을 이용해서 생성한 new Vector<String>(); 배열 내부에 저장된 문자열객체를 얻어 저장

		//=====================================================================
		// 방법 1. 향상된(업그레이드된) for 반복문으로 반복해서 얻어 사용
		//=====================================================================
		//★ 제네릭 덕분에 달라진 점 : 꺼낸 객체를 담는 변수를 Object obj 가 아니라
		//  String str 로 바로 선언할 수 있다. (Collections04에서는 Object로만 받을 수 있었다)
		for(  String str   :  vector  ) {

			//str이 처음부터 String 자료형이므로 다운캐스팅 없이 toUpperCase()를 바로 호출할 수 있다
			System.out.println( str.toUpperCase()  );
			/*
			 APPLE
			 BANANA
			 ORANGE
			 */
		}

		System.out.println();

		//=====================================================================
		// 방법 2. 일반 for 반복문 + get(index)로 반복해서 얻어 사용
		//=====================================================================
		for(int i=0;  i<vector.size();  i++) {

				temp = vector.get(i);  //위 <String> 제네릭으로 Vector 배열을 생성했기 때문에
						               //Vector 클래스 내부에 get 메소드의 반환타입 또한 String 으로 변경됨
									   //-> 다운캐스팅을 하지 않아도 String temp 변수에 꺼낸 문자열객체 저장가능
									   //(Collections05에서는 Object obj = get(i); 후 temp = (String)obj; 두 줄이 필요했다)

				System.out.println( temp.toUpperCase() );
				/*
				 APPLE
				 BANANA
				 ORANGE
				 */
		}//for

		/*
		 ■ Collections05(제네릭 없음) 와 Collections06(제네릭 사용) 코드 비교

		    구분              Collections05 (제네릭 X)                Collections06 (제네릭 O)
		    ─────────────────────────────────────────────────────────────────────────────
		    배열 생성          List vector = new Vector();             List<String> vector = new Vector<String>();
		    꺼내기            Object obj = vector.get(i);             temp = vector.get(i);   <- 한 줄로 끝!
		                      temp = (String)obj;
		    잘못된 add        vector.add(100); 저장됨(에러 없음)       vector.add(100); 즉시 컴파일 에러
		                      -> 실행 중 ClassCastException 위험
		    향상된 for        for(Object obj : vector)                for(String str : vector)
		*/

		/*
			결론 1 : 배열 생성 시 <String> 을 지정하면 클래스 내부의 E가 전부 String으로
			         변경된 것처럼 동작한다. (add는 String만 받고, get은 String으로 반환)

			결론 2 : 그 결과 다운캐스팅이 필요 없어지고(문제① 해결),
			         잘못된 자료형의 add는 실행 전에 컴파일 에러로 잡힌다(문제② 해결).

			결론 3 : 참조변수와 생성 양쪽에 제네릭을 쓰며, 자바 7부터 생성 쪽은 <>로 축약 가능하다.
			         실무에서 컬렉션은 항상 제네릭과 함께 사용한다.
		*/

	}//main 메소드

}//클래스
