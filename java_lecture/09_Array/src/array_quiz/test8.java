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
		
		//바깥쪽 for문 : 배열의 각 행(1차원 배열)을 하나씩 접근합니다.
		for(int i=0 ; i<arr.length ; i++) {
			
			//안쪽 for문 : 각 행의 열(각 숫자들)을 하나씩 접근합니다.
			for(int j=0 ; j<arr[i].length ; j++) {	
				
				total += arr[i][j];
			}
		}
		
		//(2) 평균계산 코드 작성
		//    평균을 구할때는 배열의 모든 요소(값)의 총합을 개수로 나누면되는데
		//    int로 나누면  int나누기 int이기때문에 결과를 int로 얻으므로
		//    만일 float형으로 형변환하지 않으면 average변수는 16.25가 아닌 16.0 될것입니다.
		//    (average변수의 자료형(타입)은 float이므로 16을 저장하면 16.0이 된다.)
		average = total / (float)( arr.length * arr[0].length );
		/*
		   1.  int형(4byte)보다 크기가 작은 자료형은 int형으로 형변환 후 에 연산을 수행한다
		       
		       byte / short  ->  int /  int  = int
		
		   2.  두 개의 피연산자 중 자료형의 표현범위가 큰 쪽에 맞춰서 형변환 된 후 연산을 수행한다
		   
		       int  / float ->  float / float = float
		  
		   3. 정수형 간의 나눗셈에서  0으로 나누는것은 금지 되어 있다.
		       
		         5 / 0     X
		         10 / 0    X
		
		
		   1. int형(4byte)보다 작은 자료형(byte, short 등)은 int형으로 변환되어 연산이 수행됩니다.
	       2. 두 피연산자 중 표현범위가 큰 자료형을 기준으로 계산이 이루어집니다.
	          예를 들어, int와 float을 함께 계산하면 float 결과가 나옵니다.
	       3. 정수형 나눗셈에서 0으로 나누면 안됩니다.
	          예) 5 / 0 은 에러가 발생하며, 컴파일 혹은 실행 단계에서 문제가 됩니다.
		*/
		
		System.out.println("total= " + total);
		System.out.println("average= " + average);
		
	}

}









