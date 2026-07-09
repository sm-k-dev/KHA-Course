
/*
	빈칸 채우기 문제: 아파트 거주 인원수 파악하기
	
	아래 코드의 설명과 문맥을 잘 읽고, [ 빈칸 1 ] 부터 [ 빈칸 6 ] 까지 들어갈 알맞은 기호나 코드를 생각해 보세요.
*/

public class Array2DBlankTest {
	public static void main(String[] args) {
		
		// 1. 2차원 배열 메모리 생성
		// 3층(행)짜리 건물이고, 각 층마다 2개의 호실(열)이 있는 아파트입니다. (총 6칸)
		// 2차원 배열임을 나타내는 대괄호 기호와, 층수(행)에 해당하는 숫자를 적어주세요.
		int[][] residents = new int[ 3 ][ 2 ];
		
		// 2. 각 층/호실에 값 저장하기 (행과 열의 인덱스는 모두 0부터 시작합니다!)
		residents[0][0] = 3; // 1층 1호에 3명 거주
		residents[0][1] = 4; // 1층 2호에 4명 거주
		
		// ⚠️주의: 2층 1호에 2명이 살고 있다는 데이터를 저장하려고 합니다. 
		// 2층을 의미하는 알맞은 행(row) 인덱스 번호는 무엇일까요?
		residents[1][0] = 2; 
		residents[1][1] = 5; // 2층 2호에 5명 거주
		
		residents[2][0] = 1; // 3층 1호에 1명 거주
		residents[2][1] = 3; // 3층 2호에 3명 거주
		
		System.out.println("=== 아파트 층별 거주 인원 ===");
		
		// 3. 다중 for문을 이용하여 2차원 배열의 전체 데이터 꺼내기
		
		// 바깥쪽 for문: 행(층수)을 담당합니다. 총 3층이므로 0, 1, 2까지 3번 반복합니다.
		for(int row = 0 ; row < residents.length ; row++) {
			System.out.print("      ");
			// 안쪽 for문: 열(호실)을 담당합니다. 각 층마다 2개의 호실이 있으므로 몇 번 반복해야 할까요?
			for(int col = 0 ; col < residents[row].length ; col++) {
				
				// 배열명[행위치][열위치]에 맞게 반복문의 변수들을 넣어주세요.
				System.out.print(residents[row][col] + "명\t" );
				
			}
			
			// 한 층(행)의 출력이 끝나면 다음 층 출력을 위해 줄바꿈을 해줍니다.
			System.out.println(); 
		}

	}
}