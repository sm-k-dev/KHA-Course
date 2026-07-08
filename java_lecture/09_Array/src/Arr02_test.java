/*
	빈칸 채우기 문제: 최고 달리기 기록 찾기
	
	아래 코드의 설명과 문맥을 잘 읽고, [ 빈칸 1 ] 부터 [ 빈칸 5 ] 까지 들어갈 알맞은 기호나 코드를 생각해 보세요.
*/

public class Arr02_test {
	public static void main(String[] args) {
		
		// 1. 배열 생성 및 초기화
		// 5일간의 달리기 기록(km)을 실수(double) 형태로 배열에 저장합니다.
		double[] runDistance = { 3.5, 5.2, 4.8, 7.1, 6.0 };
		
		// 2. 최댓값을 저장할 변수 선언 및 초기 기준값 설정
		// 처음에는 무조건 첫 번째 날의 기록을 '현재까지의 1등'으로 임시 지정합니다.
		double max = runDistance[0]; 
		
		// 3. for문을 이용하여 나머지 기록들과 하나씩 비교하기
		// 이미 첫 번째 칸의 값은 max에 넣었으니, 그다음 칸부터 끝까지 반복하며 비교합니다.
		for(int i = 1 ; i < runDistance.length ; i++) {
			
			// 4. 최댓값 갱신하기
			// 만약 지금 꺼내온 달리기 기록(runDistance[i])이 
			// 현재까지의 1등 기록(max)보다 크다면? (기록 경신!)
			/*
			if(runDistance[i] > max) {
				// 1등 자리(max)에 방금 찾은 더 큰 기록을 새롭게 덮어씌워 줍니다.
				max = runDistance[i]; 
			}
			*/
			
			max = runDistance[i] > max ? runDistance[i] : max;
		}
		
		System.out.println("이번 주 가장 멀리 달린 거리 = " + max + "km");
		
	}
}