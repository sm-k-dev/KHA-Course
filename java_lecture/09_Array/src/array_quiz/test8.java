package array_quiz;
/*
	문제. 2차원 배열 arr에 담긴 모든 값의 총합과 평균을 구하는
	      프로그램을 완성 하시오.
	      즉! (1) 과 (2) 영역에 들어갈 코드를 완성 해서 넣으시오.
	      
	      참고. 1. int형(4byte)보다 크기가 작은 자료형은 int형으로 형변환 후에 연산을 수행함
	           2. 두 개의 피연산자 중 자료형의 표현범위가 큰 쪽에 맞춰서 형변환 된 후 연산을 수행함
	           3. 정수형 간의 나눗셈에서 0으로 나누는 것은 금지 되어 있다.
	      
	출력결과
	total=325
	average=16.25      
*/
public class test8 {
	public static void main(String[] args) {
		int[][] arr = {
				{5,  5, 5, 5, 5},	//첫번? 행 : 모든 값을 더하면 25
				{10,10,10,10,10},	//두번째 행 : 모든 값을 더하면 50
				{20,20,20,20,20},	//세번? 행 : 모든 값을 더하면 100
				{30,30,30,30,30}	//네번? 행 : 모든 값을 더하면 150
		};
		
		int total = 0; //전체 합계를 저장할 변수. 
		float average = 0;//평균 값을 저장할 변수.
		int count = 0;
		
		//바깥쪽 for문 : 배열의 각 행(1차원 배열)을 하나씩 접근합니다.
		for(int i=0 ; i<arr.length ; i++) {
			
			//안쪽 for문 : 각 행의 열(각 숫자들)을 하나씩 접근합니다.
			for(int j=0 ; j<arr[i].length ; j++) {	
				
				total += arr[i][j];
				count++;
			}
		}
		
		//(2) 평균계산 코드 작성
		average = total / (float)count;
		
		
		System.out.println("total= " + total);
		System.out.println("average= " + average);
		
	}

}









