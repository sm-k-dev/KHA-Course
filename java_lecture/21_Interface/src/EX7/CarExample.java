package EX7;

interface Vehicle {
	void run();
}

class Bus implements Vehicle {
	@Override
	public void run() {
		System.out.println("버스가 달립니다.");
	}
}

class Taxi implements Vehicle {
	@Override
	public void run() {
		System.out.println("택시가 달립니다.");
	}
}

// 운전자 설계도 (클래스)
class Driver {
	
	//운전하는 동작을 메소드로 표현
	void drive(Vehicle vehicle) { // new Bus(); 혹은 new Taxi(); 둘 중 하나의 자식 객체를 매개변수로 전달 받음
		// new Bus().run();
		// new Taxi().run();
		vehicle.run(); // 부모 Vehicle 인터페이스를 구현한 자식 객체에서 오버라이딩 한 run() 메소드 최종 실행
	}
}

public class CarExample {

	public static void main(String[] args) {
		// Driver 운전자 클래스의 객체 생성
		// Object object = new Driver(); // Object가 부모클래스이기 때문에 Object로 업캐스팅 가능
		Driver driver = new Driver();
		
		driver.drive( new Bus() );
	}

}
