package Ex;

import Ex.Order.Address;

/*
[문제 2] 정적 멤버 중첩 클래스 - 주문과 배송지
 배송지는 주문보다 먼저 만들어질 수 있어야 하므로
 Order 객체 없이 생성 가능한 static 멤버 중첩 클래스로 작성한다.

 [완성 후 실행 결과]
 1001번 주문 배송지 : 부산 해운대구 센텀로 1
 1002번 주문 배송지 : 부산 해운대구 센텀로 1
*/
class Order {

	// TODO 2-1.
	//  정적 멤버 중첩 클래스 Address 를 선언하세요.
	//  - 인스턴스 변수 : String city, String detail (둘 다 private)
	//  - 생성자 : 두 값을 받아 저장
	//  - 메소드 : public String full()  ->  "도시 상세주소" 형태로 돌려주기
	static class Address {
		private String city, detail;
		
		public Address ( String city, String detail ) {
			this.city = city;
			this.detail = detail;
		}
		
		public String full() {
			return this.city + " " + this.detail;
		}
	}

	private int orderId;
	// TODO 2-2. Address 자료형 인스턴스 변수 address 를 선언하세요. (private)
	private String address;

	Order(int orderId, Address address) { // TODO 2-3. Address 매개변수를 추가하세요.
		this.orderId = orderId;
		// TODO 2-3. 전달받은 배송지를 인스턴스 변수에 저장하세요.
		this.address = address.full();
	}

	void printInfo() {
		// TODO 2-4. "1001번 주문 배송지 : 부산 해운대구 센텀로 1" 형식으로 출력하세요.
		System.out.println( this.orderId + "번 주문 배송지 : " + this.address );
	}
}

public class OrderExample {
	public static void main(String[] args) {

		// TODO 2-5.
		//  (1) new 바깥클래스명.Address("부산", "해운대구 센텀로 1") 로 배송지 생성
		//  (2) 1001번, 1002번 주문을 같은 배송지로 생성하고 printInfo() 호출
		//  ※ TODO 2-3 완성 전에는 아래 두 줄이 컴파일되도록 임시로 배송지 없이 생성해 둠
		Address address = new Order.Address("부산", "해운대구 센텀로 1");
		
		new Order(1001, address).printInfo();
		new Order(1002, address).printInfo();
	}
}
