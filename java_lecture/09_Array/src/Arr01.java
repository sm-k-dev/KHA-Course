/*
 * 예제. 5명의 학생 점수를 초기값으로 갖는 배열메모리를 2번째 생성 방법으로 만든 후
 * 		점수의 총합을 구하고 평균을 구하는 예
 * */
public class Arr01 {
	
	// main 메소드 기능: 자바프로그램을 처음 실행하는 기능
	public static void main(String[] args) {
		/*
		 * 5명의 학생 점수들을 차례대로 95, 70, 80, 75, 100 점수 들을 초기값을 갖는
		 * score 배열 메모리 생성
		 * */
		int[] score = { 95, 70, 80, 75, 100 };
		
		// 5명의 학생 점수 총합을 구해 저장할 변수 선언 후 0으로 초기화
		int total = 0;
		
		// for 반복문을 이용하여 score 배열에 각 점수를 차례 대로 얻어 total 변수에 누적
		for ( int i = 0 ; i < score.length ; i++ ) {
			// 배열 전체의 크기를 구하는 방법
			// 배열명.length
			total += score[i];
		}
		System.out.println("점수 총 합 = " + total);
		
		// 학생 5명의 점수들의 평균을 avg 변수에 저장
		double avg = total/ (double)score.length ;
		/*
			중요 포인트1)  정수 / 실수가 왜 실수가 되는가?

				자바(Java)는 자료형이 다를 때, 더 큰 표현 범위를 가진 타입으로 자동 변환합니다.
				이를 **자동 형변환(자동 타입 캐스팅, implicit casting)**이라고 부릅니다.

				int (정수)  / double (실수)

				double이 표현 가능한 값의 범위가 훨씬 넓기 때문에
				정수 / 실수 연산이 나오면 정수가 자동으로 double(실수)로 변환됩니다.
			
					
			중요 포인트2)  정수 / 정수 → 결과는 정수! (소수점은 버림)

				자바에서 int / int 나눗셈은  무조건 **정수 나눗셈**으로 처리됩니다.
				즉, 소수점 아래 값이 있더라도 그냥 버립니다(반올림 X).
		*/
		
		System.out.println("점수 평균 = " + avg);
	}

}
