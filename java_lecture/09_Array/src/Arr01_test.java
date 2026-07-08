/*
	빈칸 채우기 문제: 스마트폰 사용 시간 분석

	아래 코드의 설명과 문맥을 잘 읽고, [ 빈칸 1 ] 부터 [ 빈칸 5 ] 까지 들어갈 알맞은 코드를 생각해 보세요.
*/
public class Arr01_test {
	public static void main(String[] args) {
		
		// 1. 배열 선언 및 초기화
		// 최근 5일간 스마트폰 사용 시간(분)을 차례대로 배열에 저장합니다.
		int[] usageTime = {120, 90, 150, 100, 140};
		
		// 총합을 누적해서 저장할 변수를 선언하고, 초기값을 설정합니다.
		// 처음에는 더해진 값이 없어야 하므로 어떤 숫자로 시작해야 할까요?
		int total = 0;
		
		// 2. for문과 배열의 길이를 이용하여 총합 구하기
		// 조건식에 숫자 5를 직접 적지 않고, 배열의 크기를 자동으로 알려주는 '속성'을 사용해 보세요!
		for(int i = 0 ; i < usageTime.length ; i++) {
			
			// usageTime 배열의 방 번호(인덱스)에 맞게 값을 차례대로 꺼내어 total에 누적합니다.
			total += usageTime[i];
		}
		
		// 3. 평균 구하기 (총합 / 총개수)
		// 총합(total)을 배열의 총개수로 나누어 평균을 구합니다.
		// ⚠️주의: 정수 나누기 정수는 소수점이 버려집니다. 소수점까지 정확하게 계산하기 위해
		// 나누는 값을 실수형으로 바꾸는 '형변환' 코드를 괄호 안에 적어주세요.
		double avg = total / ( double )usageTime.length;
		
		System.out.println("=== 스마트폰 사용 시간 결과 ===");
		System.out.println("총 사용 시간 = " + total + "분");
		System.out.println("일일 평균 사용 시간 = " + avg + "분");
	}
}