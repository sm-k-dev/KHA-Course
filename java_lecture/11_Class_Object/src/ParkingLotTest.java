/*
 * 스토리: 주차장 관리
 * 		주차장은 총 자리 수와 현재 주차된 차량수를 가진다.
 * 		자리가 다 차면 입차 할 수 없고, 차가 없으면 출차 할 수 없다.
 * 
 * 객체 지향 프로그래밍 기법 3단계 중에서
 * 	1단계: 현실의 주차장 객체 모델링
 * 		데이터 - 총 자리수(totalSpots), 현재 주차된 차량수(currentCars)
 * 		기능 - 입차(enter), 출차(exit), 남은자리 확인(checkAvailable)
 * 
 * 	2단계: 주차장 설계도(class)만들기
 * */

public class ParkingLotTest {
	
	int totalSpots;
	int currentCars;
	
	void enter() {
		if ( totalSpots - currentCars <= 0 ) {
			System.out.println("현재 주차할 자리가 없습니다.");
		} else {
			currentCars++;
			System.out.println("차량이 입차 되었습니다. 남은 자리: " + (totalSpots - currentCars) + "자리");
		}
	}
	
	 void exit() {
		if ( currentCars < 0 ) {
			System.out.println("출차 할 차량이 없습니다.");
		} else {
			currentCars--;
			System.out.println("출차가 완료 되었습니다. 주차 가능한 자리: " + (totalSpots - currentCars) + "자리");
		}
	}
	
	void checkAvailable() {
		System.out.println("현재 주차 가능한 자리: " + (totalSpots - currentCars) + " 자리 / 총 " + totalSpots + "자리");
	}
	
	public static void main(String[] args) {
		ParkingLotTest p = new ParkingLotTest();
		
		p.totalSpots = 2;
		p.currentCars = 0;
		
		p.enter();
		p.enter();
		p.enter();
		p.checkAvailable();
		p.exit();
		p.checkAvailable();
	}

}
