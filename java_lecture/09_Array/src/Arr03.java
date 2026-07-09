
public class Arr03 {

	public static void main(String[] args) {
		/*
		 * 2차원 배열 전체 메모리를 선언한 후
		 * 배열의 칸에 값을 대입하고,
		 * 값을 얻어 출력하는 자바프로그램 작성
		 * */
		
		// 2차원 배열 메모리 생성 문법 1
		// 자료형[][] 변수명 = new 자료형[행개수][행에 대한 열 개수];
		
		// 정수값을 저장시킬 5행 3열 (15칸) 표형태의 2차원 배열 메모리 생성
		int[][] score = new int[5][3];
		
		// 2차원 배열의 각 행에 대한 열의 위치 칸에 값을 저장하는 방법 (배열 초기화 문법)
		// 배열명[행번호위치][행 위치에 대한 열번호위치] = 값;
		
		//		0열				1열					2열
		score[0][0] = 10;	score[0][1] = 90;	score[0][2] = 70; // 0행
		score[1][0] = 60;	score[1][1] = 80;	score[1][2] = 65; // 1행
		score[2][0] = 55;	score[2][1] = 60;	score[2][2] = 85; // 2행
		score[3][0] = 90;	score[3][1] = 75;	score[3][2] = 95; // 3행
		score[4][0] = 60;	score[4][1] = 30;	score[4][2] = 80; // 4행
		
		// for 반복문을 이용해 2차원 배열에 각 칸에 저장된 값을 반복해서 꺼내와 출력
		for ( int i = 0 ; i < score.length ; i++ ) {
			for ( int j = 0 ; j < score[i].length ; j++ ) {
				System.out.print( "score[" + i + "][" + j + "] = " + score[i][j] + "\t" );
			}
			System.out.println();
		}
		
		/*
 			향상된 for문(Enhanced for loop)
 			
				// int[][] 배열명 = new int[][]; 이라고 가정합니다.
				for (int[] row : 배열명) { // 1. 2차원 배열에서 '행(1차원 배열)'을 하나씩 꺼냄
				    for (int value : row) { // 2. 꺼내온 '행'에서 '요소(int)'를 하나씩 꺼냄
				        System.out.print(value + "\t");
				    }
				    System.out.println();
				}
			
			향상된 for문은 매우 편리하지만 아래 상황에서는 사용할 수 없습니다.
				인덱스 번호가 필요할 때: score[i][j]처럼 현재 위치의 방 번호(i, j)를 함께 출력해야 한다면 기존 for문을 써야 합니다.
				배열의 값을 변경할 때: 꺼내온 변수(value)를 수정해도 실제 배열 내부의 값은 바뀌지 않습니다. (읽기 전용)
		*/
	}

}
