/*
 * 스토리: 헬스장 회원은 남은 PT(개인레슨) 횟수를 가진다.
 * 		PT를 사용할 때 마다 1회씩 차감되고, 0회가 되면 사용 할 수 없다.
 * */

// 1단계: 현실의 헬스장 회원 객체 모델링 (데이터와 기능 추출)
// - 데이터: 이름(name), 회원권 종류(membership), 잔여 PT 횟수(ptCount)
// - 기능: PT 사용(usePT), PT 충전(chargePT), 상태 출력(printStatus)

// 2단계: 헬스장 회원 설계도(class) 만들기
public class GymMemberTest {
	
	// 클래스 변수
	String name;
	String membership;
	int ptCount;
	
	// 클래스 메소드
	
	/*
	 * 메소드명: usePT
	 * 기능: ptCount가 0보다 크면 1회 차감하고 진행 메세지 출력,
	 * 		0 이면 "PT 횟수가 없습니다. 추가 결제가 필요합니다" 출력
	 * */
	 void usePT () {
		 
		 if ( ptCount > 0 ) {
			 ptCount--;
			 System.out.println(name + "님 PT 진행! 남은 횟수: " + ptCount + "회");
		 } else {
			 System.out.println("PT 횟수가 없습니다. 추가 결제가 필요합니다.");
		 }
	 }
	 
	 /*
	  * 메소드명: chargePT
	  * 기능: PT 횟수 충전 - ptCount에 매개변수 n을 더하고, 충전 완료 메세지 출력 
	  * */
	void chargePT (int n) {
		ptCount += n;
		System.out.println(name + "님 PT " + n + "회 충전 완료. 총 잔여: " + ptCount + "회");
	}
	
	/*
	 * 메소드명: printStatus
	 * 기능: 회원 상태 출력 - 이름, 회원권, 잔여 PT 횟수를 한줄로 출력
	 * */
	void printStatus() {
		System.out.println("이름: " + name + " / 회원권: " + membership + " / 남은 PT 횟수: " + ptCount + "회");
	}
	
	public static void main(String[] args) {
		// 3단계: 객체 생성후 데이터 저장 및 사용
		
		// 순서1. 객체 생성, 참조변수 m
		GymMemberTest m = new GymMemberTest();
		
		// 순서2. 객체 데이터 저장
		m.name = "정우성";
		m.membership = "3개월권";
		m.ptCount = 1;
		
		// 순서3. 객체 메소드 호출해서 기능 사용
		m.usePT();
		m.usePT();
		m.chargePT(5);
		m.printStatus();
	}

}
