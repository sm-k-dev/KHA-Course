package EX7;


interface Vehicle{
	void run(); //달리는기능을 의미하는 run 추상메소드 	
}

class Bus implements Vehicle{
	@Override
	public void run() { System.out.println("버스가 달립니다.");  }
}

class Taxi implements Vehicle{
	@Override
	public void run() { System.out.println("텍시가 달립니다.");  }
}

//운전자 설계도(클래스)
class Driver {
	
	//운전하는 동작을 메소드로 표현
	void drive(Vehicle vehicle) { //<- new Bus(); 또는  new Taxi(); 두개 중 하나의 자식객체를 매개변수로 전달 받음
		
	//new Bus().run();
		vehicle.run(); //<- 부모 Vehicle 인터페이스를 구현한 자식 객체의 오버라이딩 한 run()메소드 최종 실행!
				
	}	
}

public class CarExample {
	public static void main(String[] args) {
		//Driver 운전자 클래스의 객체 생성
		Driver  driver = new Driver();
   /*
						[ Heap 영역 ]
						┌──────────────────────────────┐
						│ Driver 객체                   │
						│ drive(Vehicle) 메소드          │
						└──────────────────────────────┘
*/
				driver.drive(new Bus()); //<- new Bus(); 객체 내부의 메소드오버라이딩 된 run메소드가 실행됨
				driver.drive(new Taxi()); //<- new Taxi(); 객체 내부의 메소드오버라이딩 된 run메소드가 최종 실행됨
		
	}

}






