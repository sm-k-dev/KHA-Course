// 1단계: 현실의 소나타 객체를 모델링 (데이터와 기능 추출)
//	데이터: 브랜드, 색상, 속도
//	기능: 가속, 감속

// 2단계: 자바코드로 class 설계 (멤버변수 + 메소드)
public class Car {
	
	// 멤버변수 선언
	String brand;
	String color;
	int speed;
	
	// 멤버메소드 선언
	void accelerate() {
		speed += 10;
	}
	
	void breaks() {
		speed -= 10;
	}
	
	public static void main(String[] args) {
		// 3단계: 2단계에서 만든 Car 클래스(설계도) 하나를 이용해 "Hyundai" 자동차 객체 생성 및 사용
		Car Hyundai = new Car();
		
		Hyundai.brand = "Hyundai";
		Hyundai.color = "Red";
		Hyundai.speed = 0;
		
		Car Kia = new Car();
		
		Kia.brand = "Kia";
		Kia.color = "Black";
		Kia.speed = 0;
		
		Hyundai.accelerate();
		Hyundai.breaks();
		
		Kia.accelerate();
		Kia.breaks();
	}

}
