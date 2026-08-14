
//주제 : 람다식을 Stream API에서 활용하여  데이터 필터링(추출) 및 변환 예  (짧게 작성 하는 코드 방식)

import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;


public class Ex2_1 {

	public static void main(String[] args) {

		/*
		  Arrays.asList()메소드는 제공된 배열의 요소들을 고정 크기의 ArrayList배열을 생성해서 반환해줌 
		  반환된 ArrayList는 배열이지만, 고정 크기의 리스트로 다룰수 있으며,
		  요소(객체)의 추가나 삭제는 허용되지 않습니다.
		*/		

		//1. 데이터 준비 (점수들 배열에 담아 준비)
		List<Integer>  scores = Arrays.asList(65, 70, 80, 90, 85, 50, 40);
		
		//[new Integer(65), new Integer(70), new Integer(80), new Integer(90), new Integer(85), new Integer(50), new Integer(40) ]
		//        0                1                2               3                4                 5                6         index		
		
		//2. 60점 이상의 성적만 필터링(걸러내고)하고 평균을 계산해서 변수에 저장
		//순서2.   중간 연산  - 필터링(걸러내기)
		//순서2-1. 변환 연산  - IntegerStream 스트림 통로를 IntStream스트림 통로로 변환
		//순서3.   최종 연산  - 60점 이상인 점수들의 평균 값 산출!
		double average = scores.stream()                       //IntegerStream 입력 스트림 통로 반환
							   .filter( score -> score >= 60 ) //60점 이상 필터링 한 IntegerStream 입력스트림 반환
							   .mapToInt( score -> score     ) //IntegerStream을 IntStream입력스트림으로 변환 해서 반환 
							   .average()     //78.0 평균을 계산해서 담은 OptionalDouble 객체를 반환하는데
							   .orElse(0.0);  //IntStream통로에 점수들이 없으면 0.0값으로 설정해 0.0이 반환되어 사용하게 된다.
											  //IntStream통로에 점수들이 있으면 .average()로 계산한 평균 78.0 이 반환되어 사용하게 된다.
		//3. 60점 이상의 평균 출력
		System.out.println("60점 이상인 점수들의 평균 : " + average);
		//					60점 이상인 점수들의 평균 : 78.0

		
	}

}
