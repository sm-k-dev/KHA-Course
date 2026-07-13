/*
1.객체지향 프로그래밍(Object-Oriented Programming)
->현실세계에 존재하는 사물 과 대상, 그리고 그에따른 동작(행위)을 
  있는그대로 실체화시키는 형태의 프로그래밍 임.

2.객체(Object)
->현실세계 의 속성(데이터,상태) + 동작(기능,행동)을 가진 
  모든 물건 또는 대상(사람)을 의미.
  예)컴퓨터,책상,책,과일장수,철수,영희 등등...

3.객체(Object)의 구성 
->속성(데이터) + 동작(기능,행동)
*/

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
*/

//(2단계) 클래스(설계도) 정의 : 변수 + 메소드
//과일장수 FruitSeller라는 이름의 틀(클래스)을 정의합니다.
class FruitSeller{ 
	
	/*변수*/
	int appleQuantity = 20;
	int applePrice = 500;
	int orangeQuantity = 10;
	int orangePrice = 1000;
	int todayTotal = 0;

	/*메소드*/
	void sellFruit(String fruit, int count) {
		if (fruit == "apple" && appleQuantity >= count) {
			todayTotal += count * applePrice;
			appleQuantity -= count;
		} else if (fruit == "apple" && appleQuantity < count){
			todayTotal += count * appleQuantity;
			appleQuantity = 0;
		} else if (fruit == "orange" && orangeQuantity >= count) {
			todayTotal += count * orangePrice;
			orangeQuantity -= count;
		} else if (fruit == "orange" && orangeQuantity < count) {
			todayTotal += count * orangeQuantity;
			orangeQuantity = 0;
		} else {
			System.out.println("오늘 재고가 다 떨어졌어요");
		}
	}

}// FruitSeller 클래스 끝

//(2단계) 클래스(설계도) 정의 : 변수 + 메소드
//나(과일구매자) FruitBuyer라는 이름의 틀(클래스)을 정의합니다.
class FruitBuyer {
	/*변수*/
	int money = 10000;
	int appleQuantity = 0;
	int orangeQuantity = 0;

	/*메소드*/
	void buyFruit(String fruit, int count, int price) {
		if ( fruit == "apple" && money > count * price ) {
			appleQuantity += count;
			money -= count * price;
		} else if ( fruit == "orange" && money > count * price ) {
			orangeQuantity += count;
			money -= count * price;
		} else {
			System.out.println("돈이 부족해서 못 살거 같아요, 죄송합니다.");
		}
	}
}

public class Ex1 {
	public static void main(String[] args) {
		//(3단계) 객체(인스턴스) 생성과 사용
		//-객체생성시 new 연산자 사용 
		//-클래스 (설계도,틀) 을 통해 -> 객체생성
		
		//과일판매자 객체생성
		FruitSeller seller = new FruitSeller();
		
		//과일구매자 객체생성
		FruitBuyer buyer = new FruitBuyer();
		
		//과일구매자는 과일판매자에게 2000원어치 사과 구매하기
		buyer.buyFruit("apple", 2000/seller.applePrice, seller.applePrice);
		seller.sellFruit("apple", 2000/seller.applePrice);
		
		//과일 판매자의 현재상황
		System.out.println("--과일 판매자의 현재상황--");
		System.out.println("사과: " + seller.appleQuantity + "개, 오렌지: " + seller.orangeQuantity + " 개");
		System.out.println("오늘 수익: " + seller.todayTotal + " 원");
		
		System.out.println();
		//과일 구매자의 현재상황
		System.out.println("--과일 구매자의 현재상황--");
		System.out.println("남은돈: " + buyer.money + " 원");
		System.out.println("구매한 사과: " + buyer.appleQuantity + " 개");
	}
}
