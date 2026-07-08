/*
	문제 파일: 겨울철 기온 분석기 
	
	주석의 지시사항을 꼼꼼히 읽고, 바로 아래 빈 줄에 알맞은 코드를 직접 작성 해 주세요.
*/
public class TemperatureAnalyzer {

	public static void main(String[] args) {
		
		// 1. 5일 동안의 아침 최저 기온을 정수로 저장하는 배열 'temps'를 생성하고,
		// 초기값으로 -2, 3, -5, 1, 0 을 차례대로 한 번에 저장하세요.
		int[] temps = { -2, 3, -5, 1, 0 };
		
		// 2. 가장 낮은 기온(최솟값)을 찾기 위해, 최솟값을 임시로 저장할 변수 'minTemp'를 선언하고
		// temps 배열의 첫 번째 값(0번 인덱스)으로 초기화하세요.
		int minTemp = temps[0];
		
		// 3. 영하권(0도 미만)인 날이 며칠인지 개수를 셀 변수 'subZeroCount'를 선언하고 
		// 0으로 초기화하세요.
		int subZeroCount = 0;
		
		// 4. for 반복문을 작성하세요. 
		// 배열의 길이(length)를 이용하여 0번 인덱스부터 끝까지 반복하도록 조건식을 설정하세요.
		for ( int i = 0 ; i < temps.length ; i++ ) {
			
			// 5. (for문 블록 안) 만약 현재 꺼내온 기온이 minTemp보다 작다면?
			// minTemp의 값을 현재 기온으로 변경(갱신)하는 if문을 작성하세요.
			/*
			if ( minTemp > temps[i] ) {
				minTemp = temps[i];
			}
			*/
			
			minTemp = minTemp > temps[i] ? temps[i] : minTemp;
			
			// 6. (for문 블록 안) 만약 현재 꺼내온 기온이 0보다 작다면(영하권이라면)?
			// subZeroCount를 1 증가시키는 if문을 작성하세요.
			/*
			if ( temps[i] < 0 ) {
				subZeroCount++;
			}
			*/
			
			subZeroCount = temps[i] < 0 ? ++subZeroCount : subZeroCount;
			
		} // for문 종료 (괄호 닫기)
		
		// 7. (for문이 완전히 끝난 후) 아래 양식에 맞게 결과를 출력하세요.
		// 출력 예시:
		// 가장 추웠던 날의 기온: -5도
		// 영하권이었던 날의 수: 2일
		System.out.println("가장 추웠던 날의 기온: " + minTemp +"도");
		System.out.println("영하권이었던 날의 수: " + subZeroCount + "일");
	}

}