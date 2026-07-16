// 1단계. 현실에 있는 보라색원, 빨간색원, 파란색원 객체들을 모델링해서 데이터와 기능 추출
// 데이터: 원의 지름
// 기능: 원의 면적을 구하는 기능

// 2단계. 원 설계도(class) 만들기

class Circle {
	// 클래스 변수
	double radius; // 원의 반지름
	
	// 클래스 메소드
	// 메소드명: setRadius
	// 기능: 매개변수로 전달받은 반지름의 값을 radius 변수에 저장
	void setRadius(double r) {
		radius = r;
	}
	
	// 메소드명: calculateArea
	// 기능: 원의 면적을 구해서 반환하는 기능
	double calculateArea () {
		return radius * radius * Math.PI;
	}
}
 
public class Ex2_1 {

	public static void main(String[] args) {
		// 3단계. 2단계에서 만든 class Circle 설계도 하나를 이용해 객체 메모리 생성 후 사용
		
		// 순서1. Circle 설계도(클래스)를 이용해 객체 메모리 생성후 참조변수에 객체 메모리 주소 번지(JVM 메모리가 붙여주는 16진수 주소번지) 저장
		Circle circle = new Circle();
		
		// 순서2. 반지름 값 5를 double radius 객체 변수에 저장
		circle.setRadius(5);
		
		// 순서3. 원 면적을 구하기 위해 new Circle(); 객체 메모리 내부에 만들어져 있는 calculateArea() 메소드 호출해서 기능을 사용
		System.out.println( circle.calculateArea() );
		
		Circle circle2 = new Circle();
		circle2.setRadius(3);
		System.out.println( circle2.calculateArea() );
	}

}
