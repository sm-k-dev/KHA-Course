package Ex_sol;

/*
[문제 3] 로컬 중첩 클래스 - 영수증 한 줄 출력
 영수증 줄을 만드는 기능은 print 메소드 안에서만 필요하다.
 메소드 { } 안에 로컬 중첩 클래스 Line 을 선언해서 완성하라.

 [완성 후 실행 결과]
 아메리카노 4500원 x 2개 = 9000원
 치즈케이크 6000원 x 1개 = 6000원

 [함께 생각해 볼 것]
 Line 클래스 아래에서 total = 0; 을 추가하면 어떤 컴파일 에러가 날까?
*/
class Receipt {
			   //"치즈케이크",     6000,        1
	void print(String menu, int price, int count) {
		
		//   6000 =  6000 X 1;
		int total = price * count; // 지역변수

		// TODO 3-1.
		//  이 위치에 로컬 중첩 클래스 Line 을 선언하세요.
		class Line {
			//  - 메소드 : void show()
			//  - 출력 형식 : 아메리카노 4500원 x 2개 = 9000원
			//  - 매개변수(menu, price, count)와 지역변수(total)를 그대로 읽어서 사용합니다.
			void show() {
				System.out.println(menu + " " + price +"원 x "+ count +"개 = " + total + "원");
			}
			
			//total = 0;  //<- 변경 하려고 할떄  int total 이  final int total 상수메모리로 변경되어 변경할수 없다.
			
		}		
		// TODO 3-2.
		//  Line 객체를 생성하고 show() 를 호출하세요.
		Line line = new Line();
		line.show();

	}
}

public class ReceiptExample {
	public static void main(String[] args) {

		Receipt receipt = new Receipt();
				receipt.print("아메리카노", 4500, 2);			
				receipt.print("치즈케이크", 6000, 1);
	}
}







