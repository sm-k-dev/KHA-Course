
public class Arr04 {

	public static void main(String[] args) {
		/*
		 * 예제. 2차원 배열에 초기값을 저장하면서 배열 생성 후
		 * 		행 단위와 열 단위의 데이터들의 합을 구하는 프로그램
		 * */
		
		/*
			2차원 배열 메모리 생성 문법 2
				초기값을 칸에 저장하는 동시에 2차원 배열 메모리 생성
			
			문법: 자료형[][] 변수명 = { // 0열  1열  2열
									{ 값1, 값2, 값3, ... }, // 0 행
									{ 값4, 값5, 값6, ... }, // 1 행
									{ 값7, 값8, 값9, ... }, // 2 행
									...
								};
		*/
		
		// 학생들의 과목별 점수를 초기값으로 갖는 2차원 배열 메모리 생성
		int[][] score = { //  국,	영,	수
							{ 85,	60,	70 },	// 첫번째 학생
							{ 90,	95,	80 },	// 두번째 학생
							{ 75,	80,	100 },	// 세번째 학생
							{ 80,	70,	95 },	// 네번째 학생
							{ 100,	65,	80 }	// 다섯번째 학생
						};
		
		
		// 각 과목별 총점을 저장시킬 1차원 배열 메모리 생성
		int[] subject = new int[3];
		
		String[] subName = { "국어", "영어", "수학" };
		
		for ( int row = 0 ; row < score.length ; row++ ) {
			for ( int col = 0 ; col < score[row].length ; col++ ) {
				subject[col] += score[row][col];
			}
		}
		
		for ( int i = 0 ; i < subject.length ; i++ ) {
			System.out.println(subName[i] + " 과목 점수 총합은: " + subject[i] + " 입니다.");
			
		}
		
		System.out.println();
		
		// 각 학생별 총점을 저장하는 1차원 배열 메모리 생성
		int[] student = new int[5];
		
		for ( int row = 0 ; row < score.length ; row++ ) {
			for ( int col = 0 ; col < score[row].length ; col++ ) {
				student[row] += score[row][col];
			}
		}
		
		for ( int i = 0 ; i < student.length ; i++ ) {
			System.out.println((i + 1) + " 번 학생 점수 합: " + student[i] );
		}
	}

}
