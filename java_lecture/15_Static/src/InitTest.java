



//주제 : 변수 초기화와 지역변수에 대해 

public class InitTest {
	
	//객체 변수 만들기
	int x;         //<- int의 기본값 0으로 자동초기화 
	int y = x;	   //<- x변수는 이미 0으로 초기화 되어 있어  y도 0으로 명시적 초기화가 이루어짐 
	
	//객체 메소드 만들기
	public void method1(int y) {  //<-- int y; 매개변수 이자~ 지역변수 
		
		//지역변수
		// - 메소드 {}블럭 내부에 선언된 변수 
		// - JVM 의  Stack 영역에  포함되며, 해당 method1 메소드실행 중에만 지역변수 i가 유효 
		// - 지역변수 i는  반드시 개발자가 직접 명시적 초기화 한후 사용 해야 합니다.
		int i;   // 지역변수 i 의 값을 다른곳에서 사용시 에러 발생!
				 //이유 : 지역변수 i의 값을 초기화 하지 않았기 떄문 
		
		//int j = i;  <=========== X 컴파일 에러 : 지역변수 i의 값은 초기화 되지 않음 	
	}
	
	//클래스 변수 만들기
	//- 생성된 모든 객체 메모리가  공유하는 클래스변수 이다.
	//- 명시적 초기화 가능 
	static int[]  arr = new int[10];  //<---- 개발자가 직접 배열메모리 만들어 명시적 초기화 
	
	//static 클래스 초기화 블럭 이용해 클래스 변수 arr값 초기화 가능
	static {
		
		//arr 배열의 각 칸에  1 ~ 10 사이의 랜덤 정수 값 초기화 
		for(int i=0;  i<arr.length;  i++) {
				
			arr[i] =  (int)(Math.random() * 10) + 1;
		}
			/*
			📌 실행 결과 예시 (Heap 영역에서 업데이트)
			arr[0] : 1
			arr[1] : 1
			arr[2] : 3
			arr[3] : 9
			arr[4] : 6
			arr[5] : 6
			arr[6] : 9
			arr[7] : 7
			arr[8] : 1
			arr[9] : 5
			*/
	} //<------------------------------------------------- static  {    }  클래스 초기화 블럭 끝 
		
	public static void main(String[] args) {
		
		//객체 생성 없이  클래스변수 arr에 저장된 배열을 가져와 사용해보자
		
			//arr에 저장된 배열 메모리의 각칸의 랜덤값을 반복해서 얻어 출력
		
				for(int i=0;  i<InitTest.arr.length;      i++) {
					
					System.out.println("arr[" + i + "] : " +  InitTest.arr[i] );
					
				}
				/*
												arr[0] : 7
												arr[1] : 9
												arr[2] : 9
												arr[3] : 1
												arr[4] : 3
												arr[5] : 3
												arr[6] : 10
												arr[7] : 10
												arr[8] : 2
												arr[9] : 2
				*/

	}

}







