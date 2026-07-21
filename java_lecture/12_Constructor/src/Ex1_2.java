//과일장수 이야기  스토리~
/*
	[ 현실 세계 상황 재연 1 ]
	"나는 과일장수에게 두개의 사과를 구매했다!"
	
	 객체 찾기-> 나 , 과일장수, 사과 

	[ 현실 세계 상황 재연 2 ]
	"나는 과일 장수에게  2000원을 주고 두개의 사과를 구매했다!"
	
	나(객체) 라는  객체가 ? 과일장수(객체)로부터  과일(객체)를 구매하는 동작(행동)를 표현 

	[ 현실 세계 상황 재연 3 ]	
	과일장수는 사과20개, 오렌지10개를 보유하고 있습니다. 	
	
	과일장수는 과일을 팝니다    	
	
*/		
		

//3. 객체 지향 프로그래밍 3단계
/*
1단계 :　현실세계의 객체를 모델링 하여 속성(데이터,상태)와 동작(기능,행동)을 추출 하는 단계
        요약 -> 현실 세계의 객체를 모델링 하는 단계

		속성(데이터,상태) -> 변수에 속성(데이터,상태)정보를 저장
		동작(기능,행동) -> 메소드로 표현

(1단계) 과일장수 객체 관점 으로 봤을때의 속성(데이터,상태) + 동작(기능,행동) 추출

	---과일장수의 속성(데이터,상태) 정보를 저장할 변수---
		1.사과 한 개당 가격을 저장할 변수 
		2.보유하고 있는 사과의 수를 저장할 변수
		3.판매수익을 저장할 변수
	
	---과일장수의 동작(기능,행동)을 메소드로 만들기---
		1.과일장수의 과일판매 동작(기능,행동)을 할 메소드
		2.사과판매후 남은 사과수,판매수익 출력 하는 기능의 메소드
		
(1단계) 과일구매자 객체 관점 으로 봤을때의 속성(데이터,상태) + 동작(기능,행동) 추출

	---과일구매자의 속성(데이터,상태) 정보를 저장할 변수---    
	    1.현재 소유하고 있는 현금을 저장할 변수
		2.현재 소유하고 있는 사과수를 저장할 변수
	
	---과일구매자의 동작(기능,행동)을 메소드로 만들기---
		1.과일구매자의 과일구매 동작(기능,행동)을 할 메소드
		2.과일구매후 현재 남은 현금과 사과의수 출력 기능의 메소드 


---------------------생성자를 이용하는 방법으로 업그레이드 시키자-----------------

FruitSeller 과일장수 클래스 내부 수정

	순서1. 사과 한개 당 가격 변수를 final을 붙여 상수메모리로 다시 만들어주자.
		  단! 사과 한개 당 가격은 FruitSeller클래스의 객체 메모리 생성시 호출되는 생성자 내부에서 초기화하자.
		  
	순서2. 과일장수 객체의 사과 한개당 가격, 사과보유수, 판매수익을 각각 객체변수들에 초기화 시킬 생성자 추가!!!	  

	순서3. 추가한 initMembers 메소드 삭제.
*/

//(2단계) 클래스(설계도) 정의 : 클래스변수 + 클래스메소드
//과일장수 FruitSeller라는 이름의 틀(클래스)을 정의합니다.
class FruitSeller{ 
	
/*클래스변수*/
	//---과일장수의 속성(데이터,상태) 정보를 저장할 변수---	
	
	
	final int APPLE_PRICE; 	      //1.사과 한 개당 가격을 저장할 변수 
	int numOfApple; 	         //2.보유하고 있는 사과의 수를 저장할 변수
	int myMoney;                 //3.판매수익을 저장할 변수

/*생성자*/
	 // new FruitSeller(1500,                  30,               0);

	// new FruitSeller(1000,                  20,             5000);
	//				   사과 한개 당 가격 ,       보유 사과 수 ,      판매수익
	public FruitSeller(int apple_price,  int numOfApple_, int myMoney_) {    
		
		APPLE_PRICE = apple_price; //1500;
		numOfApple  = numOfApple_; //30;
		myMoney     = myMoney_;    //0;
	}	
	
/*클래스메소드*/
	//---과일장수의 동작(기능,행동)을 메소드로 만들기---
	
	//1.과일장수의 과일판매 동작(기능,행동)을 할 메소드
	int saleApple(int money) {  //<- 사과 구매 금액을 과일 구매자(FruitBuyer)객체로 부터 매개변수로 전달 받는다.
		
		//판매한 사과 갯수 구하기
		//방법-> 과일 구매자(FruitBuyer)객체가 지불한  금액에 한 개당 금액으로 나누면  판매한 사과 갯수를 구할수 있다.
		int num = money / APPLE_PRICE;
			
		numOfApple -= num;  //사과 판매 후 과일장수 객체가 보유한 사과 갯수 차감
		
		myMoney += money;  	//과일장수 객체가 판매한 판매수익금액을 누적
		
		return num; // 실제 과일장수 객체가 판매한 사과 갯수를 과일 구매자 객체에게  반환 
	}
		
	//2.사과판매후 남은 사과수,판매수익 출력 하는 기능의 메소드	
	public void showSaleResult() {
		System.out.println("과일장수객체의 현재 남은 사과수 : " + numOfApple);
		System.out.println("과일장수객체의 현재 잔고 : " + myMoney);
	}
	
}// FruitSeller 클래스 끝

/*
FruitBuyer(과일 구매자) 클래스 내부 수정

순서1. 현재 과일 구매자가 소유하고 있는 현금  0원,  현재 소유하고 있는 사과 수 0으로 초기화 

순서2. 과일 구매자가 초기에 소유하고 있는 현금, 현재 소유하고 있는 사과 수 를 초기화 할 생성자 추가 
*/

//(2단계) 클래스(설계도) 정의 : 클래스 변수 + 클래스 메소드
//나(과일구매자) FruitBuyer라는 이름의 틀(클래스)을 정의합니다.
class FruitBuyer {
		
/*클래스 변수*/
//---과일구매자의 속성(데이터,상태) 정보를 저장할 변수---   	 
	int myMoney;     //1.현재 소유하고 있는 현금을 저장할 변수
	int numOfApple;  //2.현재 소유하고 있는 사과수를 저장할 변수
	
/*생성자*/	
	//생성자 만드는 단축 키  alt  + shift + s  o	
	public FruitBuyer(int myMoney_, int numOfApple_) {
		myMoney = myMoney_;
		numOfApple = numOfApple_;
	}

/*클래스 메소드*/
//---과일구매자의 동작(기능,행동)을 메소드로 만들기---
	//1.과일구매자의 과일구매 동작(기능,행동)을 할 메소드
	//  매개변수 -> 과일구매를 위한 과일장수(판매자)객체,  구매시 지불한 금액
	public void buyApple(FruitSeller seller,  int money) {
									    
			//과일장수(판매자)객체에게 과일을 구매하기 위해
	    	//FruitSeller seller매개변수로 전달 받은 과일판매자객체(new FruitSeller())의 salApple객체메소 호출!
	    	//호출시~~ 매개변수로 구매시 지불한 금액을 전달 하면 
	    	//과일구매자 객체는 구매한 과일(사과)갯수를 반환 받는다.
	    	//과일구매자객체(FruitBuyer객체)의 객체변수  numofApple에 누적!	
		  numOfApple  += seller.saleApple(money);
		  
		  //과일 판매자 객체(FruitSeller객체)에게 과일구매자객체가 지불한 2000원 차감 
		  myMoney -= money;
	}
		
	//2.과일구매후 현재 남은 현금과 사과의수 출력 기능의 메소드
	public void showBuyResult() {
		System.out.println("현재 잔액 : " + myMoney);       //현재 남은 돈 출력
		System.out.println("보유 사과 수 : " + numOfApple); //현재 보유한 사과 수 출력
	}
		
}//====>  과일구매자 (FruitBuyer class)정의 끝 

public class Ex1_2 {
	public static void main(String[] args) {
	//(3단계) 객체(인스턴스) 생성과 사용
	//-객체생성시 new 연산자 사용 
	//-클래스 (설계도,틀) 을 통해 -> 객체생성
	
	//1. 과일 장수 1 객체 생성 시 생성자호출해서 객체 데이터 완성
	//  객체 데이터 :  사과 한개당 가격 1500원,   사과보유갯수 30개,   판매수익 및 잔고 : 0원
	FruitSeller   fruitSeller1 =  new FruitSeller(1500, 30, 0);
	/*		
				[  0x16     ] = ------------------0x16--------------------------------------------------
			
									final int APPLE_PRICE;   [ 1500 ]
									
									int numofApple;  [ 30 ]
									
									int myMoney; [ 0 ]
									
									int saleApple(int money){
									
										int num = money / APPLICE_PIRCE;
										
										numofApple -= num;
										
										myMoney += money;
										
										return num;
									}
									
									public void showSaleResult() {
										System.out.println("과일장수객체의 남은 사과 갯수 : " +  numofApple);
										System.out.println("과일장수객체의 현재 잔고 : " + myMoney);
									}
							   -----------------------------------------------------------------------
*/	
	
		//2. 과일 장수 2 객체 생성 시 생성자호출해서 객체 데이터 완성
		//  객체 데이터 :  사과 한개당 가격 1000원,   사과보유갯수 20개,   판매수익 및 잔고 : 5000원
		FruitSeller    fruitSeller2  =  new FruitSeller(1000, 20, 5000);
		/*		
						[ 0x17      ] = ------------------0x17--------------------------------------------------
				
											final int APPLE_PRICE;   [ 1000 ]
											
											int numofApple;  [ 20 ]
											
											int myMoney; [ 5000 ]
											
											int saleApple(int money){
											
												int num = money / APPLICE_PIRCE;
												
												numofApple -= num;
												
												myMoney += money;
												
												return num;
											}
											
											public void showSaleResult() {
												System.out.println("과일장수객체의 남은 사과 갯수 : " +  numofApple);
												System.out.println("과일장수객체의 현재 잔고 : " + myMoney);
											}
									   -----------------------------------------------------------------------
*/	
		
		  //new FruitBuyer            =>  FruitBuyer class설계도 하나 이용해 새로운 객체 메모리 만든다
		  //    FruitBuyer(10000, 0); =>  생성자 호출할때 객체 데이터들을 매개변수로 전달해 객체변수값들 모두 저장시킨 객체메모리 완성.

		//과일 구매자 1 객체 생성
		//객체 데이터  :  현재 소유 현금 10000원,    현재 구매한 사과 갯수 0개 
		FruitBuyer   fruitBuyer1 = new FruitBuyer(10000, 0);
		/*		
						 [  0x5 ]= ---------------- 0x5-----------------
					
										int myMoney; [ 5500 ]
										
										int numOfApple;  [ 3  ]
					
										public void buyApple(FruitSeller seller,  int money) {

											  numOfApple  += seller.saleApple(money);
											  
											  //과일 판매자 객체(FruitSeller객체)에게 과일구매자객체가 지불한 2000원 차감 
											  myMoney -= money;
										}	  
										
										 public void showBuyResult() {
											 System.out.println("현재 잔액 : " + myMoney); //현재 남은 돈 출력
											 System.out.println("보유 사과 수 : " + numOfApple);//현재 보유한 사과 수 출력
										 }
										
									--------------------------------------
		*/				
		
		// 스토리 :  과일 구매자 1 객체는 과일 장수 1 객체에게 4500원을 지불하고  과일(사과)를 구매 하는 동작 시키기
		fruitBuyer1.buyApple(fruitSeller1, 4500);
	
		System.out.println("------------- 과일 구매자 1 객체 ---------------");
		fruitBuyer1.showBuyResult();
		/*
		------------- 과일 구매자 1 객체 ---------------
		현재 잔액 : 5500
		보유 사과 수 : 3
		*/
		System.out.println("------------ 과일 장수 1객체 ----------------");
		fruitSeller1.showSaleResult();
		/*		
		------------ 과일 장수 1객체 ----------------
		과일장수객체의 현재 남은 사과수 : 27
		과일장수객체의 현재 잔고 : 4500
		*/
		
		//스토리 : 과일 구매자 1객체는 과일 장수 2 객체에게 2000원을 지불하고 과일(사과)를 구매 하는 동작 시키기
		fruitBuyer1.buyApple(fruitSeller2, 2000);
		
		System.out.println("-------------- 2번째로 구매한 과일 구매자1 객체 -----------");
		fruitBuyer1.showBuyResult();
		/*
		 -------------- 2번째로 구매한 과일 구매자1 객체 -----------
		현재 잔액 : 3500
		보유 사과 수 : 5
		 */
		System.out.println("-------------- 과일 장수 2객체 ---------------");
		fruitSeller2.showSaleResult();
		/*
		-------------- 과일 장수 2객체 ---------------
		과일장수객체의 현재 남은 사과수 : 18
		과일장수객체의 현재 잔고 : 7000
		*/
	}
}
