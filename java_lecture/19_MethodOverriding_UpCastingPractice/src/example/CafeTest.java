
package example;
//==========================================================================
// ★ 응용문제. 카페 메뉴판 만들기 - 상속 + 접근제어자 ★
//--------------------------------------------------------------------------
// [만들 프로그램 소개]
//   카페의 일반 메뉴(Menu)를 부모 클래스로 만들고,
//   커피(Coffee)를 자식 클래스로 만듭니다.
//   커피는 "아이스 여부"라는 정보가 추가로 필요하고,
//   아이스로 주문하면 가격이 500원 추가되는 규칙이 있습니다.
//
// [이 문제로 연습하는 것]
//   1. private 변수  : 자식도 직접 접근 불가 -> getter로만 읽기
//   2. protected 변수: 자식이 this.변수명 으로 직접 접근 가능
//   3. super(...)    : 부모 생성자에게 초기화를 맡기기
//   4. 오버라이딩     : 부모의 showMenu()를 자식이 다르게 다시 만들기
//   5. boolean 변수  : true/false 두 가지 값으로 if 분기하기
//
// 주석의 지시를 위에서부터 순서대로 코드로 옮기면 완성됩니다.
// 파일 맨 아래 [예상 실행 결과]와 출력이 같으면 정답입니다.
//==========================================================================


//==========================================================================
// [부모 클래스] Menu - 카페의 일반 메뉴 (케이크, 쿠키 등)
//==========================================================================
class Menu {

	//[1] 객체 변수 2개 선언 - 접근제어자를 서로 다르게!
	//
	//    ① private 접근제어자, String 자료형, 변수명 menuName  (메뉴 이름 저장용)
	//       -> private이므로 자식 클래스 Coffee에서 this.menuName 은 컴파일 에러!
	//
	//    ② protected 접근제어자, int 자료형, 변수명 price  (가격 저장용)
	//       -> protected이므로 자식 클래스 Coffee에서 this.price 직접 사용 가능!
	//
	//    선언 문법:  접근제어자  자료형  변수명;

	//>>> 여기에 변수 2개를 선언하세요.
	private String menuName;
	// private: 이 변수는 Menu 클래스의 { } 안에서만 직접 접근 가능.
	//          자식 클래스 Coffee에서 this.menuName 이라고 쓰면
	//          "menuName has private access in Menu" 컴파일 에러가 난다.
	protected int price;
	// protected: 같은 패키지 + 자식 클래스에서 접근 허용.
	//            그래서 자식 Coffee가 this.price 로 직접 계산에 사용할 수 있다.
	//            (menuName과의 이 차이가 이 문제의 핵심!)
		


	//[2] 생성자 작성
	//    - 생성자 이름은 클래스 이름과 똑같이 Menu (반환타입 없음!)
	//    - 매개변수 2개: String menuName, int price
	//    - 내부 코드 2줄:
	//        this.menuName = menuName;   <- 객체의 변수에 매개변수 값 저장
	//        this.price = price;
	//    - this.menuName = "객체가 소유한 변수" / menuName = "전달받은 값"
	//      이름이 같아서 왼쪽에 this. 를 붙여 구분합니다.

	//>>> 여기에 생성자를 작성하세요.
	public  Menu(String menuName, int price) {
		// 생성자: 클래스명과 이름이 같고 반환타입이 없다.
		// new Menu("치즈케이크", 6000) 처럼 객체를 만들 때 자동으로 1번 실행된다.
		
		this.menuName = menuName;
		this.price = price;	
	}


	//[3] getter 작성
	//    문법:  public String getMenuName() { return this.menuName; }
	//    - private인 menuName을 자식/바깥에서 "읽을 수 있게" 열어주는 통로입니다.
	//    - price는 protected라서 자식이 직접 읽을 수 있으므로
	//      이 문제에서는 price용 getter를 만들지 않습니다. (차이를 기억!)

	//>>> 여기에 getMenuName 메소드를 작성하세요.
	public String getMenuName() { return this.menuName; }
	 

	//[4] showMenu 메소드 작성
	//    문법:  public void showMenu() { 출력코드 }
	//    - 출력 코드 1줄:
	//      System.out.println("[메뉴] " + this.menuName + " - " + this.price + "원");
	//    - 여기는 Menu 클래스 내부이므로 private 변수도 this.menuName 으로
	//      직접 읽을 수 있습니다.

	//>>> 여기에 showMenu 메소드를 작성하세요.
	 public void showMenu() { 
		 
		 System.out.println("[메뉴] " + this.menuName + " - " + this.price + "원");
			// 여기는 Menu 클래스 내부이므로 private 변수도 this.menuName 으로
			// 직접 읽을 수 있다. 문자열과 변수를 + 로 이어붙여 한 줄 출력.
			// 예) "[메뉴] 치즈케이크 - 6000원"
	 }
}

//==========================================================================
// [자식 클래스] Coffee - 커피 메뉴 (아이스 여부와 추가요금 규칙이 있음)
//==========================================================================
//[5] Coffee 클래스 전체 작성
//    상속 문법:  class Coffee extends Menu {  으로 시작하세요.
class Coffee extends Menu {
	//  Coffee 내부에 작성할 것 4가지:
	//
	//  (1) 객체 변수 1개
	//      private boolean iced;
	//      - boolean : true(참) 또는 false(거짓) 딱 두 가지만 저장하는 자료형
	//      - iced가 true면 "아이스 주문", false면 "따뜻한 주문"이라는 뜻으로 사용	
	private boolean iced;

	//  (2) 생성자
	//      - 매개변수 3개: String menuName, int price, boolean iced
	//      - 첫 줄에 반드시:  super(menuName, price);
	//        * menuName/price 초기화는 부모 Menu의 생성자에게 맡깁니다.
	//        * 부모의 menuName은 private이라 여기서 this.menuName = menuName;
	//          이라고 쓰면 컴파일 에러가 나기 때문입니다.
	//          (에러 문구: menuName has private access in Menu)
	//      - 둘째 줄:  this.iced = iced;   <- iced는 내(Coffee) 것이므로 직접 저장
    public Coffee(String menuName, int price, boolean iced) {
    			//      "카페라떼",      5000,      true      <- 예시로 이런 값이 들어옴
    	
    	super(menuName, price);
		// super(...) : 부모 Menu의 생성자를 호출하는 문법. 반드시 첫 줄!
		// 부모의 menuName은 private이라 여기서 this.menuName = menuName; 을 쓰면
		// "menuName has private access in Menu" 컴파일 에러가 난다.
		// 그래서 값 2개를 부모 생성자에게 전달해 부모가 대신 저장하게 한다.
    	
    	this.iced = iced;
		// iced는 내(Coffee) 소유의 변수이므로 직접 저장 가능.
		// 예) this.iced = true;  (아이스 주문으로 기록됨)
    	
    }
    
	//  (3) getFinalPrice 메소드 (최종 가격 계산)
	//  문법:  public int getFinalPrice() { 코드 }
	//  - 코드 순서:
	//      if (this.iced) {              <- iced가 true이면 (아이스이면)
	//          return this.price + 500;  <- 기본 가격에 500원을 더해 반환
	//      }
	//      return this.price;            <- 아이스가 아니면 기본 가격 그대로 반환
	//  - ★this.price가 에러 없이 되는 이유: 부모에서 protected로
	//    선언했기 때문! (private이었다면 여기서 컴파일 에러)★
	//  - if (this.iced) 는 if (this.iced == true) 와 같은 뜻입니다.
	//    boolean 변수는 그 자체가 true/false이므로 == true를 생략합니다. 
    public int getFinalPrice() {
    	if(this.iced) {
    		return this.price + 500;
    	}
    	return this.price;
    	// 위 if를 통과했다 = iced가 false(따뜻한) 라는 뜻.
		// 추가요금 없이 기본 가격 그대로 반환. 예) 4000 반환
    }

	//  (4) showMenu 메소드 오버라이딩
	//  - 윗줄에 @Override 를 붙이세요.
	//  - 코드 순서:
	//      if (this.iced) {
	//          System.out.println("[커피] 아이스 " + getMenuName() + " - " + getFinalPrice() + "원 (아이스 +500원)");
	//      } else {
	//          System.out.println("[커피] 따뜻한 " + getMenuName() + " - " + getFinalPrice() + "원");
	//      }
	//  - ★메뉴 이름은 this.menuName이 아니라 getMenuName()으로!
	//    (private이라 직접 접근 불가, getter 경유)★
	//  - 가격은 this.price가 아니라 getFinalPrice()를 호출해서
	//    아이스 추가요금이 반영된 "최종 가격"을 출력합니다.
    @Override
    public void showMenu() {
    	if(this.iced) {
    		//아이스 주문인 경우의 출력
    		System.out.println("[커피] 아이스 " + super.getMenuName() + " - " + this.getFinalPrice() + "원 (아이스 + 500원)" );
    					// getMenuName()  : menuName이 private이라 this.menuName 불가 -> getter 경유
    					// getFinalPrice(): 위에서 만든 메소드를 호출 -> 추가요금 반영된 5500이 옴
    					// 예) "[커피] 아이스 카페라떼 - 5500원 (아이스 +500원)"
    	
    	}else {
    		//따뜻한 음료 주문인 경우의 출력
    		 System.out.println("[커피] 따뜻한 " + super.getMenuName() + " - " + this.getFinalPrice() + "원");
			    			// getFinalPrice()가 추가요금 없이 기본 가격 4000을 반환
			 				// 예) "[커피] 따뜻한 아메리카노 - 4000원"
    	}  	
    }//========== showMenu메소드 끝
    
}//========== Coffee 자식 클래스 끝


//==========================================================================
// 실행 확인용 메인 클래스 (완성되어 있음 - 수정하지 마세요)
//==========================================================================
public class CafeTest {
	public static void main(String[] args) {

		// 부모 클래스로 일반 메뉴 1개 생성
		Menu cake = new Menu("치즈케이크", 6000);
		cake.showMenu();
		// -> 부모의 showMenu()가 실행됨

		System.out.println();

		// 자식 클래스로 커피 2잔 생성
		Coffee hotAmericano = new Coffee("아메리카노", 4000, false);  // false = 따뜻한
		Coffee icedLatte = new Coffee("카페라떼", 5000, true);        // true  = 아이스

		hotAmericano.showMenu();
		// -> 오버라이딩된 자식의 showMenu()가 실행됨 (따뜻한, 추가요금 없음)

		icedLatte.showMenu();
		// -> 오버라이딩된 자식의 showMenu()가 실행됨 (아이스, +500원 반영)
	}
}

/*
	===== 정답 코드 작성 시 예상 실행 결과 =====

	[메뉴] 치즈케이크 - 6000원

	[커피] 따뜻한 아메리카노 - 4000원
	[커피] 아이스 카페라떼 - 5500원 (아이스 +500원)

	★스스로 채점 포인트★
	1. 치즈케이크 줄은 [메뉴]로, 커피 두 줄은 [커피]로 시작하는가?
	   -> 부모의 showMenu와 자식이 오버라이딩한 showMenu가
	      각각 따로 실행되고 있다는 증거!
	2. 카페라떼가 5000원이 아니라 5500원으로 나오는가?
	   -> getFinalPrice()의 if (this.iced) 분기가 동작한다는 증거!
	3. Coffee 생성자에서 super(menuName, price); 대신
	   this.menuName = menuName; 을 써보면 실제로
	   "menuName has private access in Menu" 에러가 나는지 확인해볼 것.
	4. 부모의 price를 private으로 바꿔보면 getFinalPrice()의
	   this.price 줄에서 에러가 나는지도 확인해볼 것.
	   (확인 후에는 다시 protected로 되돌리기!)
*/
