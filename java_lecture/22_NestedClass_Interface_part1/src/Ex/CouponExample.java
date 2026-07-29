package Ex;

/*
[문제 1] 인스턴스 멤버 중첩 클래스 - 카페 회원과 쿠폰
 회원 1명에게 쿠폰을 발급할 때마다 그 회원의 누적 발급 수가 1씩 늘어나야 한다.
 쿠폰은 특정 회원에게 소속된 정보이므로 static 없는 인스턴스 멤버 중첩 클래스로 만든다.

 [완성 후 실행 결과]
 [쿠폰] 회원=김철수 / 메뉴=아메리카노 / 누적발급=1
 [쿠폰] 회원=김철수 / 메뉴=카페라떼 / 누적발급=2
*/
class Member {

	private String name;
	private int couponCount;

	Member(String name) {
		this.name = name;
	}

	class Coupon {

		private String menu;

		Coupon(String menu) {
			// TODO 1-1.
			//  (1) 매개변수 menu 를 이 객체의 인스턴스 변수에 저장하세요.
			//  (2) 바깥 Member 객체의 couponCount 를 1 증가시키세요.
			//      힌트 : 바깥클래스명.this.변수명
			this.menu = menu;
			Member.this.couponCount++;
		}

		void print() {
			// TODO 1-2.
			//  실행 결과 형식대로 한 줄 출력하세요.
			//  회원 이름과 누적 발급 수는 바깥 객체의 변수입니다.
			System.out.println( "[쿠폰] 회원=" + Member.this.name + " / 메뉴=" + this.menu + " / 누적발급=" + Member.this.couponCount );
		}
	}
}

public class CouponExample {
	public static void main(String[] args) {

		Member m = new Member("김철수");

		// TODO 1-3.
		//  m 을 이용해 "아메리카노" 쿠폰과 "카페라떼" 쿠폰을 생성하고
		//  각각 print() 를 호출하세요.
		//  힌트 : Member.Coupon c1 = 바깥객체참조변수.new Coupon("...");
		Member.Coupon c1 = m.new Coupon("아메리카노");
		c1.print();
		
		Member.Coupon c2 = m.new Coupon("카페라떼");
		c2.print();
	}
}
