// 예제1. 1차원 배열 메모리를 생성하고 값 저장 후 저장된 값을 얻어 출력
public class G01 {

	public static void main(String[] args) {
		/*
		 * 배열 메모리 생성 문법 1
		 * 		new - 새로운 메모리를 하나 만들건데, 라는 키워드
		 * 
		 * 		자료형[] 변수명 = new 자료형[원소개수]; 
		 * */
		
		// 1. 5명의 학생 점수를 정수로 저장하기 위한 배열 메모리 생성
		int []score = new int[5];
		
		// 2. 생성한 score 배열 메모리의 각칸(각원소)에 접근해서 값을 저장하는 문법
		// 배열명[index] = 저장할 값;
		score[0]	= 95;
		score[1]	= 70;
		score[2]	= 80;
		score[3]	= 75;
		score[4]	= 100;
		
		// 3. 배열의 각 원소(각 칸) 에 저장된 값을 꺼내오는 문법
		// 배열명[index]
		System.out.println(score[0]);
		
		// for 반복문을 이용하여 score 배열 메모리에 각 칸에 저장된 값을 차례대로 꺼내와 출력
		for ( int i = 0 ; i < score.length ; i++ ) {
			System.out.println("score[" + i + "] = " + score[i]);
		}
	}

}
