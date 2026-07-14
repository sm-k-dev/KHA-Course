/*
 * 스토리: 자동차는 현재 속도와 남은 연료를 가진다.
 * 		가속하면 속도가 오르고 연료가 줄며, 연료가 부족하면 가속할 수 없다.
 * */

// 1단계: 현실의 자동차 객체 모델링
//	- 데이터: 모델명(model), 속도(speed), 남은 연료(fuel)
//	- 기능: 가속하기(accelerate), 상태 확인(checkStatus)

// 2단계: class 설계
public class CarTest {
	
	// 클래스 변수 선언
	String model;	// 1. 모델명 저장할 변수
	int speed;		// 2. 현재 속도 저장할 변수
	double fuel;		// 3. 남은 연료(리터) 저장할 변수
	
	// 클래스 메소드 선언
	/*
	 * 메소드명: accelerate
	 * 기능: 연료가 amount 만큼 남아있으면 연료를 소모하고
	 * 		속도를 (amount*10)만큼 올린다.
	 * 		연료가 부족하면 "연료 부족! 가속 실패." 출력
	 * */
	
	void accelerate ( double amount ) {
		
		if ( fuel >= amount ) {
			speed += (int)(amount * 10);
			fuel -= amount;
		} else {
			System.out.println("연료 부족! 가속 실패.");
		}
	}
	
	/*
	 * 메소드명: checkStatus
	 * 기능: 모델명, 속도, 남은 연료를 한 줄로 출력
	 * 		"모델: XXX / 속도: XXkm/h / 남은 연료: xxL" 출력
	 * */
	void checkStatus() {
		System.out.println("모델: " + model + " / 속도: " + speed + "km/h / 남은 연료: " + fuel + "L");
	}
	
	public static void main(String[] args) {
		CarTest c = new CarTest();
		
		c.model = "아반떼";
		c.speed = 0;
		c.fuel = 5.0;
		
		// 3. 객체 메소드 호출해서 사용
		c.accelerate(0.7);
		c.checkStatus();
	}

}
