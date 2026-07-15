/*

스토리:

장바구니 하나에는 상품 1종류의 이름·단가·수량이 담긴다(간단 버전). 
수량을 추가하고, 총 합계를 계산한다.

*/
// 1단계 : 현실의 쇼핑 장바구니 객체 모델링
// - 데이터 : 상품명(itemName), 단가(price), 수량(quantity)
// - 기능   : 수량 추가(addQuantity), 합계 계산(calcTotal), 장바구니 출력(printCart)

// 2단계 : 자바 코드로 class 설계 (데이터=변수 + 기능=메소드)
public class ShoppingCartTest {
	//클래스 변수 선언
		// 상품명 저장할 변수
		String itemName;
		// 단가 저장할 변수
		int price;
		// 수량 저장할 변수
		int quantity;

	//클래스 메소드 선언
		/*
		 메소드명 : addQuantity
		 기   능  : quantity에 n을 더하고 변경된 수량을 출력한다
		*/
		void addQuantity(int n) {
			// TODO: 수량 추가 처리
		}

		/*
		 메소드명 : calcTotal
		 기   능  : price * quantity 값을 반환한다
		*/
		int calcTotal() {
			// TODO: 합계 계산 후 반환
			return 0;
		}

		/*
		 메소드명 : printCart
		 기   능  : calcTotal()을 호출해 상품명, 수량, 합계를 출력한다
		*/
		void printCart() {
			// TODO: 장바구니 출력
		}

	public static void main(String[] args) {
		// 3단계 : ShoppingCart 객체 생성 후 데이터 저장 및 사용
		//1. 객체 생성
		ShoppingCartTest cart = new ShoppingCartTest();

		//2. 데이터 저장
		cart.itemName = "무선 마우스";
		cart.price = 15000;
		cart.quantity = 1;

		//3. 기능 호출
		cart.addQuantity(2);
		cart.printCart();
	}
}