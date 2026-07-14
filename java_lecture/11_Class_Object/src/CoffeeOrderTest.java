/*
 * 스토리: 카페에서 음료 한잔을 주문한다.
 * 		사이즈 (TALL / GRANDE)에 따라 가격이 달라지고, 결제 후 영수증을 출력
 * */

// 1단계: 현실의 카페 주문 객체 모델링
//	- 데이터: 메뉴(menu), 사이즈(size), 기본가격(basePrice)
//	- 기능: 최종가격 계산(calcPrice), 영수증 출력(printReceipt)

// 2단계: class(설계도) 만들기
public class CoffeeOrderTest {
	// 클래스 변수
	String menu;
	String size;
	int basePrice;
	
	// 클래스 메소드 선언
	/*
	 * 메소드명: calcPrice
	 * 기능: size가 "GRANDE"면 기본가격 + 500원, 아니면 기본가격 그대로
	 * */
	int calcPrice () {
		
		if ( "GRANDE".equals(size) ) {
			return basePrice + 500;
		}
		return basePrice;
	}
	
	/*
	 * 메소드명: printReceipt
	 * 기능: calcPrice()를 호출해 "[영수증] 메뉴: 사이즈 / 가격: 원" 출력
	 * */
	void printReceipt () {
		int price = calcPrice();
		
		System.out.println("[영수증] 메뉴: " + menu + "(" + size + ") / 가격: " + price + " 원");
	}
	
	public static void main(String[] args) {
		CoffeeOrderTest order = new CoffeeOrderTest();
		
		order.menu = "아메리카노";
		order.size = "GRANDE";
		order.basePrice = 4000;
		
		order.printReceipt();
	}

}
