
//주제 : 람다식, Stream API 활용 - 스트림 통로를 변수에 담아 단계별로 처리하기

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;   // 스트림 통로의 결과 데이터를 컬렉션 배열에 수집할때 사용하는 클래스 
import java.util.stream.Stream;       // 데이터스트림 통로의 부모인터페이스 자료형 

public class Ex5 {

	public static void main(String[] args) {
/*
		자바 스트림 통로 종류 크게 2가지로 나뉩니다
		 	종류1. 일반스트림(InputStream, OutputStream ....) 과
		 	종류2. 데이터스트림(IntegerStream, DoubleStream ... -> 컬렉션 배열에 저장된 객체가 흘러가는 통로)로 나뉜다.

		 	차이 정리
		 	  종류1 일반스트림 : 디스크의 파일이나 키보드처럼 "프로그램 밖" 과 데이터를 주고받는 통로.
		 	                     IO 단원에서 배운 그 통로다.
		 	  종류2 데이터스트림 : 이미 메모리에 있는 배열/컬렉션의 객체들이 흘러가는 통로.
		 	                     읽고 쓰는 것이 목적이 아니라 "걸러 내고 가공" 하는 것이 목적이다.
*/		
	
		//1. 데이터 준비
		List<Integer>  numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
		/*
		  numbers 고정 크기 배열
		  [1, 2, 3, 4, 5, 6]  <- Integer 객체들이 각 칸에 저장된 고정 칸의 배열
		   0  1  2  3  4  5     index

		  참고 : Arrays.asList(1, 2, ...) 에 적은 숫자 1 은 기본자료형 int 인데
		         목록에는 객체만 담을 수 있어서 자동으로 Integer 객체로 바뀐다.
		         이것을 오토박싱이라고 한다.
		*/
		
		//2. 데이터들(Integer 객체들)을 읽어들일 데이터스트림 통로(IntegerStream 통로) 만들기
		Stream<Integer> stream = numbers.stream();
  /*
	    <----- IntegerStream 데이터스트림 통로
		------------------------------------------------------------------------------------------------------

		  new Integer(1),  new Integer(2), new Integer(3), new Integer(4),  new Integer(5),  new Integer(6)

		-------------------------------------------------------------------------------------------------------
*/		
		//3. 중간 연산에 사용할 filter 메소드를 이용하여 조건식의 결과 참인 데이터들을 추출한 IntegerStream 데이터스트림 통로 반환
		// -> 위 IntegerStream 데이터스트림 통로에 흘러가는 new Integer(..)객체들 중에서 2의 배수인 짝수 데이터만 
		//    새로운 IntegerStream에 담아 반환 받자
		//
		//   ※ 이 람다는 Predicate 인터페이스의 boolean test(T t) 를 오버라이딩한 것이다.
		//     몸통이 한 문장이면 { return ... ; } 을 생략하고
		//     (n) -> n % 2 == 0  이라고 짧게 써도 결과는 완전히 같다
		
		//   ★ 왼쪽의 stream 변수에 결과를 다시 담는 이유
		//     filter 는 원래 통로를 고치는 것이 아니라 "새 통로 객체" 를 만들어 반환한다.
		//     그 새 통로를 받아 두지 않으면 필터링 결과를 쓸 수 없으므로
		//     같은 변수에 다시 담아(덮어써) 다음 단계에서 이어 쓴다		
		stream = stream.filter( (n) -> { return n  %  2 == 0; }  );	  
			  /*
					    <----- IntegerStream 데이터스트림 "새 통로 객체"  
						------------------------------------------------------------------------------
						   
							new Integer(2) <-   new Integer(4) <- new Integer(6)
						-----------------------------------------------------------------------------
			*/	
		
		//4. 최종연산의 결과 데이터들이 저장된 산출물 얻기 collect 메소드 사용
		//   ★ 이 줄이 실행되는 순간 비로소 통로에 데이터가 흐르기 시작한다.
		//     3번의 filter 만 적어 두고 이 줄이 없으면 아무 일도 일어나지 않는다
		//   collect(Collectors.toList())
		//     - 통로 끝에 도착한 객체들을 순서대로 ArrayList 배열에 담아 반환한다
		//     - 종료 연산이므로 이  IntegerStream 데이터스트림 통로는 여기서 닫힌다. 다시 사용할 수 없다
		List<Integer> eventNumbers = stream.collect(Collectors.toList());
		
					 //[new Integer(2),  new Integer(4),   new Integer(6)] <- 컬렉션 가변길이 배열 ArrayList 반환
					 //         0                1               2             index
		
		//ArrayList 의  toString() : 배열에 담긴 내용을 사람이 쉽게 볼수 있도록 ArrayList 배열 자체모습을 문자열로 변환해서 반환
		System.out.println(  eventNumbers.toString()   );  //"[2, 4, 6]" 
		
		//----------------------------------------------------------------------
		//  최종 결과 상태
		//----------------------------------------------------------------------
		//  numbers      : [1, 2, 3, 4, 5, 6]  (변경 없음. 원본 고정배열은 그대로다)
		//  eventNumbers : [2, 4, 6]           (새로 생성된 ArrayList)
		//
		//  ※ 스트림은 원본을 절대 건드리지 않고 항상 새 배열을 만들어 돌려준다
		//----------------------------------------------------------------------	
		
		/*
		 [참고] 이 예제의 방식 vs 체이닝 방식 - 결과는 완전히 같다

		   방식 A. 이 예제처럼 변수에 담아 단계를 나누기

		     Stream<Integer> stream = numbers.stream();
		     stream = stream.filter( (n) -> n % 2 == 0 );
		     List<Integer> result = stream.collect(Collectors.toList());

		     장점 : 단계마다 통로가 어떻게 변하는지 눈으로 따라가기 쉽다 (배울 때 유리)

		   방식 B. 점(.)으로 이어 붙이기 (체이닝)

		     List<Integer> result = numbers.stream()
		                                   .filter( (n) -> n % 2 == 0 )
		                                   .collect(Collectors.toList());

		     장점 : 중간 변수가 없어 코드가 짧다 (실무에서 주로 쓰는 형태)

		   ★ 방식 A 에서 주의할 점
		     스트림 통로는 1회용이다.
		     collect 로 한 번 종료 연산을 하고 나면 그 stream 변수는 더 쓸 수 없다.
		     다시 조회하려면 numbers.stream() 을 새로 호출해야 한다.
		*/		
		
		
	}

}








