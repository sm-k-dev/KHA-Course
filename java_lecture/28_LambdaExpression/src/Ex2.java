
// 주제: 람다식을 Stream API에서 활용하여  데이터 필터링(추출) 및 변환 예 

import java.util.Arrays;
import java.util.List;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ex2 {

	public static void main(String[] args) {
		
		/*
			=================================================
			[1] 데이터 준비 단계
			=================================================
				Arrays.asList( 값1, 값2, 값3, 값4, ...... )
				----------------------------------------
				- asList 메소드 호출시 매개변수로 전달 한 값들을 포함하는 고정 크기의 List 배열을 생성해서 반환 해줌
				- 반환 된 고정 크기의 List 배열에 새로운 값을 추가하거나 기존 값을 삭제 할 수 없다
				- 값 추가 (add()) / 값 삭제 (remove()) 불가능
				- 값 조회 (get(index)) / 값 수정 (set(변경값)) 가능
		*/
		
		List<Integer> scores = Arrays.asList( 65, 70, 80, 90, 85, 50, 40 );
        /*
	        scores 내부 구조 (개념적 표현)
	        -------------------------------------------------------------------------
	        value :[65, 70, 80, 90, 85, 50, 40]
	        index :  0   1   2   3   4   5   6
	       
	        ⚠ 모든 값은 int가 아니라 Integer 객체
	        → 오토박싱(auto-boxing)에 의해 int → Integer 변환됨
        */ 
		
		/*
			=================================================
			[2] 문제 목표
			=================================================
			- 전체 점수 중 60점 이상만 걸러내고 그 점수들의 평균을 계산 한다.
			- 위 고정 크기 List 배열에 저장된 점수 들 중에서 60점 이상의 성적만 필터링(추출, 걸래냄)하고 평균을 계산 한다.
		*/
		// 순서1. 컬렉션 배열이나 고정 크기 배열에 저장된 데이터가 흘러가 읽어들일 스트림 통로 열기
		// scores.stream(); 메소드를 호출하면 생성되는 스트림 통로는 고정크기 배열에 저장된 Integer객체(점수)들이
		//					순서대로 흘러가는 IntegerStream 입력스트림 통로 를 생성해서 반환 해줌
		Stream<Integer> stream = scores.stream();
		/*
			IntegerStream통로 
			--------------------------------------------------------------------------------------------------------------------------
			new Integer(40), new Integer(50), new Integer(85), new Integer(90), new Integer(80),  new Integer(70),  new Integer(65)    >>>>>>>>   
			---------------------------------------------------------------------------------------------------------------------------
		*/	
		
		// 순서2. 중간 연산 - 필터링(데이터를 배열에서 추출)
		// IntegerStream 클래스가 제공해주는 .filter( 람다식을 사용한 익명 메소드 작성 ); 라는 메소드
		//	메소드를 사용해 각 성적을 검사하고, 60점 이상인 성적만 IntegerStream 통로에 남길 수 있다
		
		stream = stream.filter( ( Integer score ) -> score >= 60 );
		/*
			IntegerStream통로 
			--------------------------------------------------------------------------------------------------------------------------
			new Integer(85), new Integer(90), new Integer(80),  new Integer(70),  new Integer(65)    >>>>>>>>   
			---------------------------------------------------------------------------------------------------------------------------
		*/
		
		// 순서2-1. 변환 연산 - IntegerStream 에서 제공해줌, 스트림통로를 IntStream 스트림 통로로 변환
		/*
			mapToInt(), mapToDouble(), mapToLong() 메소드
			- IntegerStream 스트림에 있는 Integer 객체들을 모두 정수형(int)으로 변환하여 IntStream 통로에 담아 IntSream 통로 주소 자체를 반환
			- 일반적으로 객체가 저장된 스트림을 처리할때, 숫자 계산이나 통계처리를 하기 위해
				객체들을 기본자료형 스트림(IntStream, DoubleStream, LongStream) 으로 변환할때 사용되는 메소드 
				
				람다식  (score) -> { return score; }
						score  ->   score;
				이경우,  스트림의 각 Integer객체(요소)들을 매개변수 score로 받아
				스트림에 있는 Integer객체들을 정수형으로 변환해서 반환할 뿐입니다.   
		   
			- 이메소드 이유: 스트림의 요소가 객체타입(Integer)이라면
				자바에서 기본형 연산을 효율적으로 처리할수 있도록 
				IntStream으로 변환해 주는것이 좋습니다
				IntStream통로는 추가적인 average(), sum(), min(), max()등의
				숫자연산을 더 쉽게 처리할수 있습니다.
		
			IntegerStream통로 (Integer객체들이 저장된 통로)
			----------------------------------------------------------------------------------------	  
			new Integer(85), new Integer(90), new Integer(80),  new Integer(70),  new Integer(65)   >>>>>>>>   
			----------------------------------------------------------------------------------------
		*/
		
		IntStream stream2 = stream.mapToInt( score -> score.intValue() );
		/*
			 IntStream통로 (int들이 저장된 통로)
			-------------------------------------------	  
			            85, 90, 80,  70,  65   >>>>>>>>   
			-------------------------------------------
		*/			

		//순서3. 최종연산 결과 값 산출  - 60점이상인 점수들의 평균 값 산출!
		double average = stream2.average().orElse(0.0);
		
		System.out.println("Average Scope : " + average);
		//					Average Scope : 78.0
		
		/*
			참고.
			average() 메서드는 숫자형 스트림에서 평균값을 계산하는 역할을 합니다. 
			Stream<Integer>가 아니라 IntStream, DoubleStream, LongStream과 같은
			기본형 스트림에서만 사용 가능합니다. 
			즉, .mapToInt()로 IntStream으로 변환한 후 호출하는 메서드입니다.
		 
			.average() 메서드의 특징:
				반환 타입: OptionalDouble입니다. 
					이는 계산된 평균값을 담을 수 있는 컨테이너입니다. 
					평균값이 없을 경우(예를 들어, 스트림이 비어있는 경우)를 처리하기 위해 사용됩니다.
					비어있는 스트림의 경우 OptionalDouble.empty()가 반환되며, 
					평균값이 있을 경우 그 값을 포함합니다.
		    
			평균값이 없을 때 받을값 추출    
			.orElse(0.0)의 역할:
				.average()는 OptionalDouble을 반환하므로,
				만약 스트림에 값이 없을 경우 empty 상태가 됩니다. 
				이때 .orElse(0.0)을 사용하면 평균값이 없을 때 기본값을 설정하는 것이며,
				0.0을 반환합니다.
		*/
	}

}
