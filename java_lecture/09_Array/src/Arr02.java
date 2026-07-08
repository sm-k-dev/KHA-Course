
public class Arr02 {

	public static void main(String[] args) {
		// 예제. 5개의 실수값 중에서 최대 값 하나 구하기
		
		/*
		 * 실수 값 5개 저장 시킨 배열 만들기 (생성하기)
		 */
		double[] data = new double[] { 9.5, 7.0, 13.6, 7.5, 10.0 };
		
		/*
		 * data 배열의 최대값 하나를 얻어 저장할 max 변수 선언후 0.0 초기화 (변수에 값 처음 저장)
		 */
		double max = 0.0; // 접미사 d 생략
		
		/*
		 * for 반복문을 수행하기 전, data 배열에 0 index 위치 칸에 저장된 9.5를 꺼내서
		 * max 변수에 최대값으로 설정하기 위해 저장 
		 */
		max = data[0];
		
		/*
		 * for 반복문을 이용하여 5번 반복 처리 하면서 data배열의 1 index ~ 4 index 위치 칸에 저장된 값을
		 * 반복하여 얻은 값이 max 변수에 저장된 값보다 크면? max 변수에 다시 저장
		 * */
		for ( int i = 1 ; i < data.length ; i++ ) {
			max = max > data[i] ? max : data[i];
		}
		
		System.out.println("max = " + max);
	}

}
