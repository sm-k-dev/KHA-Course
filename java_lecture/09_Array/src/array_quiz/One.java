package array_quiz;
/*
	[배열문제1]
	다음은 거스름돈을 몇 개의 동전으로 지불할 수 있는지를 계산하는 문제이다 변.
	수 의 금액을 동전으로 바꾸었을 때 각각 몇 개의 동전이 필요한지 계산해서 출력하 money
	라 단 가능한 한 적은 수의 동전으로 거슬러 주어야한다   (1)에 알맞은 코드를 넣어서 
	프로그램을 완성하시오.
	[Hint] 나눗셈 연산자와 나머지 연산자를 사용해야 한다.
*/

public class One {
	public static void main(String args[]) {
		
		//큰 금액의 동전을 우선적으로 거슬러 줘야한다
		int[] coinUnit = { 500, 100, 50, 10 };
		
		int money = 2680;
		
		System.out.println("money= " + money);
		
		for (int i = 0; i < coinUnit.length; i++) {
	  
	         System.out.println( coinUnit[i] + "원 짜리\t" + (money / coinUnit[i]) + " 개");
	         
	         money = money % coinUnit[i];
		}
	} // main
}
/*
 실행결과
		money=2680
		500 : 5 원
		100 : 1 원
		50 : 1 원
		10 : 3 
*/