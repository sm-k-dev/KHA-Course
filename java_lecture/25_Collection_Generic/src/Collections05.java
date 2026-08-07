/*
	주제 :  제네릭 기법이 나오게 된 배경

		   Vector 배열에 저장된 객체들을 get 메소드를 이용하여 얻는데...

		   get 메소드가 무조건 부모 Object 클래스자료형으로 자식객체를 반환하기 때문에

		   (업캐스팅된 상태로 반환) 실제 자료형의 메소드를 쓰려면

		   매번 다운캐스팅을 하여 코드를 작성해야 하는 불편함이 있다.
*/

import java.util.List;
import java.util.Vector;

public class Collections05 {

	public static void main(String[] args) {
		
		//업캐스팅을 하여 Vector 배열 메모리 생성 후 저장
		List   vector = new Vector();
		
		//Vector 배열의 각 칸에 문자열 객체 추가
		vector.add("Apple");
		vector.add("banana");
		vector.add("oRANGE");
		
		//["Apple","banana","oRANGE"] <- Vector 배열
		//   0        1        2      index
		
		//다운 캐스팅을 하여 얻은 자식 문자열 객체메모리 주소를 저장할  String클래스 자료형 참조변수 선언
		String temp;
		
		//Vector 배열 메모리의 각칸에 저장된 문저열 객체 갯수만큼 반복해서 차례로 얻어 사용!
		for(int i=0;   i<vector.size();   i++) {
			
			//1단계. Vector 배열에 저장된 문자열 객체를 반환받기 위해  Object get(int index) 메소드 호출!
			//결과 -> get 메소드는 무조건!  부모 Object 클래스 자료형의 참조변수에 저장시킬 자식 문자열객체의 주소 반환해줌
			Object obj =  vector.get(i);  //i = 0 :  "Apple" 문자열 자식 객체 주소 저장
										  //i = 1 :  "banana" 문자열 자식 객체 주소 저장
										  //i = 2 :  "oRANGE" 문자열 자식 객체 주소 저장
			
			//2단계. 다운캐스팅을 하여 String 클래스 내부에 만들어져 있는 toUpperCase() 메소드를 사용할수 있게 함
			//(부모 Object 클래스 자료형의 참조변수 obj로는 Object클래스에 없는 toUpperCase() 메소드를 호출할수 없기때문)
			temp = (String)obj;
			     
			//3단계. 다운캐스팅 후에는 String 클래스의 메소드를 자유롭게 호출해서 사용할수 있다
			//toUpperCase() : 문자열의 모든 소문자를 대문자로 변경한 새 문자열을 반환하는 메소드
			//"Apple".toUpperCase()  -> "APPLE"  반환
			//"banana".toUpperCase() -> "BANANA" 반환
			//"oRANGE".toUpperCase() -> "ORANGE" 반환 (이미 대문자인 글자는 그대로 둔다)			
			System.out.println( temp.toUpperCase() );
									//APPLE
									//BANANA
									//ORANGE
			
			
			
			//temp = vector.get(i);  //<--- 컴파일 에러가 발생하는 코드!
			/*
			   컴파일 에러가 발생하는 이유 (정확하게 이해하기)

			   get 메소드의 반환 자료형은 Object다. 즉 이 대입문은
			        String temp  =  Object 자료형의 값;
			   의 모양인데, 이것은 "부모 자료형 -> 자식 자료형" 방향의 대입, 즉 다운캐스팅이다.

			   ★ 다운캐스팅은 자동으로 되지 않는다. 왜?
			      컴파일러는 코드를 검사할 때 "자료형"만 보고, 실행 중 그 칸에
			      실제로 무슨 객체가 들어있는지는 알지 못한다.
			      컴파일러 입장에서 Object 자료형이 반환된다는 것은
			      String일 수도, Integer일 수도, 어떤 자식 객체일 수도 있다는 뜻이므로
			      String 변수에 자동으로 넣어 주는 것을 거부(컴파일 에러)하는 것이다.

			   ★ 반대로 업캐스팅(자식 -> 부모)이 자동으로 되는 이유
			      String은 100% Object의 자식이라는 것이 항상 보장되기 때문이다.
			      (그래서 add("Apple")로 넣을 때와 get()이 Object로 반환할 때는 에러가 없다)

			   따라서 개발자가 (String) 을 직접 붙여
			   "이 칸의 실제 객체는 String이 맞다. 내가 책임진다"라고
			   컴파일러에게 알려주는 강제 다운캐스팅이 필요하다.
			*/			

			
			
			
		}
		
		/*
		 ⚠ 다운캐스팅 방식의 진짜 위험 : 실수해도 컴파일러가 못 잡아준다

		   자료형을 지정하지 않은 Vector에는 아무 자료형이나 저장할 수 있으므로
		   누군가 실수로 문자열이 아닌 객체를 추가했다고 하자.

		       vector.add(100);  // Integer.valueOf(100) 래퍼 객체가 저장됨 (에러 없음!)

		   이 상태로 위의 for문이 실행되면
		       temp = (String)obj;  // obj의 실제 객체가 Integer인 순간
		   컴파일은 통과하지만 ★실행 중★ ClassCastException 예외가 발생하며 프로그램이 죽는다.

		   즉 이 방식의 문제는 2가지다.
		       문제① 꺼낼 때마다 다운캐스팅 코드를 매번 써야 한다.        (불편함)
		       문제② 잘못된 자료형을 add해도 컴파일 시점에 잡지 못하고
		             실행 중에 ClassCastException으로 터진다.            (위험함)
		*/	
		
		
		
		

	}

}




