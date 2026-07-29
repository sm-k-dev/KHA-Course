package Ex_sol;

/*
[문제 2] 정적 멤버 중첩 클래스 - 주문과 배송지
 배송지는 주문보다 먼저 만들어질 수 있어야 하므로
 Order 객체 없이 생성 가능한 static 멤버 중첩 클래스로 작성한다.

 [완성 후 실행 결과]
 1001번 주문 배송지 : 부산 해운대구 센텀로 1
 1002번 주문 배송지 : 부산 해운대구 센텀로 1
*/
class Order { //바깥 외부 클래스 

	// TODO 2-1.
	//  정적 멤버 중첩 클래스 Address 를 선언하세요.
	public static class Address{
		
		//  - 인스턴스 변수 : String city, String detail (둘 다 private)
		private String city;  //도시
		private String detail;//상세 주소
		
		//  - 생성자 : 두 값을 받아 저장
		public Address(String city, String detail) {
							//"부산", "해운대구 센텀로 1"
			//super();
			this.city = city;
			this.detail = detail;
		}
		
		//  - 메소드 : public String full()  ->  "도시 상세주소" 형태로 돌려주기
		public String full() {
			return this.city + " " + this.detail;
		}
		
	}
	
	private int orderId; //주문 번호 
	private Address address; //배송지   // TODO 2-2. Address 자료형 인스턴스 변수 address 를 선언하세요. (private)

	Order(int orderId, Address address) { // TODO 2-3. Address 매개변수를 추가하세요.
		this.orderId = orderId;
		this.address = address;          // TODO 2-3. 전달받은 배송지를 인스턴스 변수에 저장하세요.
	}

	void printInfo() {
		// TODO 2-4. "1001번 주문 배송지 : 부산 해운대구 센텀로 1" 형식으로 출력하세요.
		System.out.println(this.orderId + "번 주문 배송지 : " + this.address.full());
	}
}

public class OrderExample {
	public static void main(String[] args) {

		// TODO 2-5.
		//  (1) new 바깥클래스명.Address("부산", "해운대구 센텀로 1") 로 배송지 생성
		
		//정적 멤버 중첩 Address클래스의 객체 생성 
		// -> 바깥클래스명.정적중첩클래스명 참조변수 = new 바깥클래스명.생성자();
				  Order.Address       addr = new      Order.Address("부산", "해운대구 센텀로 1");
		
		
		//  (2) 1001번, 1002번 주문을 같은 배송지로 생성하고 printInfo() 호출
		//  ※ TODO 2-3 완성 전에는 아래 두 줄이 컴파일되도록 임시로 배송지 없이 생성해 둠
		Order  order = new Order(1001, addr);	
		order.printInfo(); 				    //1001번 주문 배송지 : 부산 해운대구 센텀로 1
				    
		new Order(1002, addr).printInfo();  //1002번 주문 배송지 : 부산 해운대구 센텀로 1

	}
}






