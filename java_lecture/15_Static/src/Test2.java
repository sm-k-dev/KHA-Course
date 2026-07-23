//==========================================================================
// ★ 종합문제. 은행계좌(BankAccount) - 계좌번호 자동생성 + 은행 전체 자산 관리 ★
//--------------------------------------------------------------------------
// 지금까지 배운 내용을 모두 결합하는 문제입니다:
//   - 생성자 오버로딩 + this() 체이닝
//   - 인스턴스변수 vs 클래스변수(static)
//   - 인스턴스메소드 vs 클래스메소드(static)
//   - 배열 + 반복문
//   - boolean을 반환하는 메소드
//
// [설정] 계좌를 만들 때마다 계좌번호가 1001, 1002, 1003... 처럼 자동으로
//        하나씩 증가하며 부여되고, 은행 전체의 "총 계좌 수"와 "총자산"을
//        모든 계좌가 공유하는 클래스변수로 관리한다.
//==========================================================================

class BankAccount {

	//[1] 객체 변수 선언 (계좌 하나마다 개별로 가지는 값)
	//    int accountNumber - 이 계좌만의 고유 계좌번호
	//    String owner      - 예금주 이름
	//    int balance       - 현재 잔액

	//>>> 여기에 객체 변수 3개를 선언하세요.
	int accountNumber;   // 이 계좌만의 고유번호 - 계좌마다 값이 다를수 있음
	String owner;		 // 예금주 이름  - 계좌마다 값이 다를수 있음
	int balance;		 // 현재 잔액  - 계좌마다 값이 다를수 있음
	

	//[2] 클래스 변수 선언 (모든 계좌가 공유하는 값)
	//    static int accountCount = 0  - 지금까지 생성된 계좌 총 개수
	//    static int totalAsset   = 0  - 은행 전체 총자산 (모든 계좌 잔액의 합)

	//>>> 여기에 클래스 변수 2개를 선언하세요. (0으로 명시적 초기화)
	static int accountCount = 0;   
	static int totalAsset = 0;
	//----------------------------------------------------------------
	//[풀이] static 변수를 왜 2개나 두었나?
	//  - accountCount : "지금까지 계좌가 몇 개 만들어졌는지"는 계좌 하나의
	//                   정보가 아니라 "은행 전체"의 정보이므로 공유해야 한다.
	//  - totalAsset   : "은행 전체 자산"도 마찬가지로 특정 계좌 하나가
	//                   아니라 모든 계좌를 합친 값이므로 공유 변수여야 한다.
	//  - static이 붙으면 Method Area에 "딱 1개"만 만들어지고, new로 계좌를
	//    몇 개를 생성하든 그 계좌들이 전부 이 변수 하나를 함께 사용한다.
	//----------------------------------------------------------------


	//[3] 생성자 ① - 예금주 이름만 매개변수로 받는 생성자 (초기잔액 0)
	//    ★ this() 생성자 호출구문으로 아래 생성자 ②에게 위임하세요.

	//>>> 여기에 생성자 ①을 작성하세요.
	public BankAccount(String owner) {
						// "김철수"     
		
		this(owner, 0);
	}

	
	//----------------------------------------------------------------
	//[풀이] ★계좌번호 자동생성의 핵심★
	//  1) accountCount += 1;
	//     → 이 줄이 실행될 때마다 "은행 전체 공용 카운터"가 1씩 늘어난다.
	//       new BankAccount(...)가 몇 번째로 호출되는지와 정확히 같은 값이 된다.
	//  2) this.accountNumber = 1000 + accountCount;
	//     → 첫 계좌면 accountCount=1이므로 1001,
	//       두번째 계좌면 accountCount=2이므로 1002 ...
	//       "1000 + 순번"이라는 단순한 공식으로 자동 번호가 나온다.
	//  3) totalAsset += balance;
	//     → 새 계좌가 들고 들어오는 초기잔액만큼, 은행 전체 총자산에도
	//       "같이" 더해준다. (계좌 개별 balance와 전체 totalAsset을
	//        동시에 갱신하는 것이 포인트)
	//----------------------------------------------------------------
	
	
	//[4] 생성자 ② - 예금주 이름 + 초기잔액을 매개변수로 받는 생성자
	//    기능 순서:
	//      1) accountCount를 1 증가시킨다 (accountCount += 1;)
	//      2) 이 계좌의 accountNumber를 "1000 + accountCount" 값으로 저장한다
	//         (계좌가 하나씩 생길 때마다 1001, 1002, 1003...으로 자동 부여됨)
	//      3) owner, balance를 매개변수로 받은 값으로 저장한다
	//      4) totalAsset에 이번 계좌의 balance만큼 더한다 (은행 전체 총자산 갱신)

	//>>> 여기에 생성자 ②를 작성하세요.
	public BankAccount(String owner, int balance) {
						//  "이영희",      50000     <-- 예시로 이런 값들이 들어온다고 가정
		
		accountCount += 1;
		//accountCount = accountCount + 1;

		this.accountNumber = 1000 + accountCount;
		//this.accountNumber = 1000 + 2;  //<--- 이 객체(이영희 계좌)만의 번호로 확정 저장
		
		this.owner = owner;
		this.balance = balance;
		
		totalAsset += balance;
		// totalAsset = totalAsset + 50000;
		// 이 계좌가 가져온 잔액(50000)만큼 은행 전체 총자산에도 더해줌
		
	}



	//[5] 인스턴스메소드 - deposit (입금)
	//    매개변수: int amount
	//    반환타입: void
	//    기능:
	//      - this.balance 에 amount만큼 더하기
	//      - totalAsset 에도 amount만큼 더하기 (은행 전체 총자산도 함께 갱신)
	//      - 출력: "[계좌번호]에 amount원 입금 완료 (잔액: balance원)"
	//        예) [1002]에 20000원 입금 완료 (잔액: 70000원)

	//>>> 여기에 deposit 메소드를 작성하세요.
	public void deposit(int amount) {
						//20000    <- 이금액이 들어 온다고 가정
		
		//계좌 현재 잔액이 20000 이 늘어남 
		this.balance += amount;
		
		//은행 전체 총 자산도 20000 이 늘어남
		totalAsset += amount;
		
		System.out.println("[" + accountNumber  + "]에 " + amount + "원 입금 완료 (잔액: " + this.balance + "원)");
		// 예) 			   "[1002]에 20000원 입금 완료 (잔액: 70000원)"
		
	}


	//[6] 인스턴스메소드 - withdraw (출금)   ★boolean을 반환하는 메소드★
	//    매개변수: int amount
	//    반환타입: boolean  (출금 성공하면 true, 실패하면 false)
	//    기능 순서:
	//      조건문 : amount가 this.balance보다 크면 (잔액이 부족하면)
	//               "[계좌번호] 잔액 부족으로 출금 실패" 출력 후 false 반환
	//      그 외  : this.balance 에서 amount만큼 빼고,
	//               totalAsset 에서도 amount만큼 뺀 뒤
	//               "[계좌번호]에서 amount원 출금 완료 (잔액: balance원)" 출력 후
	//               true 반환

	//>>> 여기에 withdraw 메소드를 작성하세요.
	public boolean withdraw(int amount) { //<- 50000출금 금액 매개변수로 전달 받기 
											
		if(amount > this.balance) {
		//만약 (출금요청금액 > 현재잔액) 이라면, 즉 돈이 모자르면?
			
			System.out.println("[" + this.accountNumber + "] 잔액 부족으로 출금 실패" );		
			return false;  //출금 실패로 인한 fasle를 호출한 쪽으로 돌려준다.			
		}
		
		 //this.balance 에서 amount만큼 빼고,
		 this.balance -= amount;
		
		 //totalAsset 에서도 amount만큼 뺀 뒤
		 totalAsset -= amount;
		 
		 //"[계좌번호]에서 amount원 출금 완료 (잔액: balance원)" 출력 후
		System.out.println("[" + this.accountNumber + "]에서" + amount +"원 출금 완료 (잔액: " + this.balance + "원)");
		
		 return true; //정상적으로 출금이 끝났으므로  true를 호출한 쪽으로 돌려준다.		
	}


	//[7] 인스턴스메소드 - showInfo (계좌 정보 출력)
	//    출력형식: "계좌번호: XXXX / 예금주: OOO / 잔액: XXXX원"

	//>>> 여기에 showInfo 메소드를 작성하세요.
	public void showInfo() {
		System.out.println("계좌번호: " + this.accountNumber + " / 예금주: " 
									  + this.owner + " / 잔액: " + this.balance + "원");
	}


	//[8] 클래스메소드(static) - showBankStatus (은행 전체 현황 출력)
	//    ★static 메소드이므로 this 사용 불가 - accountCount/totalAsset만 접근 가능★
	//    출력형식(세 줄):
	//      ===== 은행 전체 현황 =====
	//      총 계좌 수: X개
	//      총자산: XXXX원

	//>>> 여기에 showBankStatus 메소드를 작성하세요.
	public static void showBankStatus() {
		System.out.println("===== 은행 전체 현황 =====");
		
		System.out.println("총 계좌 수: " +  BankAccount.accountCount + "개");
		// static 변수 accountCount를 this 없이 바로 사용 (클래스 소속이므로 가능)
		// 예) "총 계좌 수: 3개"
		
		System.out.println("총자산: " + BankAccount.totalAsset + "원");
		// static 변수 totalAsset도 마찬가지로 this 없이 바로 사용
		// 예) "총자산: 100000원"
	}

}  //=========>  class BankAccount


public class Test2 {
	public static void main(String[] args) {

		//[9] 계좌 3개를 배열에 담아 생성하기
		//    ★배열 문법: BankAccount[] accounts = new BankAccount[3];
		//    accounts[0] : 생성자① 사용 - 예금주 "김철수" (초기잔액 0, 계좌번호 자동 1001)
		//    accounts[1] : 생성자② 사용 - 예금주 "이영희", 초기잔액 50000 (계좌번호 자동 1002)
		//    accounts[2] : 생성자② 사용 - 예금주 "박민수", 초기잔액 30000 (계좌번호 자동 1003)

		//>>> 여기에 배열 생성 + 계좌 3개 담는 코드를 작성하세요.
		BankAccount[]  accounts = new BankAccount[3];
		
					   accounts[0] = new BankAccount("김철수");
						// 생성자① 호출 -> 내부에서 this("김철수", 0) 실행
						// -> accountCount 0에서 1로 증가, accountNumber = 1001, balance = 0
		
					   accounts[1] = new BankAccount("이영희", 50000);
						// 생성자② 바로 호출
						// -> accountCount 1에서 2로 증가, accountNumber = 1002, balance = 50000
						// -> totalAsset 0에서 50000으로 증가
					   
					   accounts[2] = new BankAccount("박민수", 30000);
						// 생성자② 바로 호출
						// -> accountCount 2에서 3으로 증가, accountNumber = 1003, balance = 30000
						// -> totalAsset 50000에서 80000으로 증가
					   



		//[10] for 반복문으로 배열을 처음부터 끝까지 돌며 각 계좌의 showInfo() 호출
					   
		//>>> 여기에 for문을 작성하세요.
		for(int i=0;  i<accounts.length;  i++) {			
			// i를 0부터 시작해서 accounts.length(=3)보다 작은 동안 반복
			// i=0 -> i=1 -> i=2  (총 3번 반복하고 i=3이 되면 조건 거짓으로 종료)
			
			accounts[i].showInfo();
			// i=0일 때 accounts[0](김철수) 정보 출력
			// i=1일 때 accounts[1](이영희) 정보 출력
			// i=2일 때 accounts[2](박민수) 정보 출력			
			
		}
					   
					 
		System.out.println();

		//[11] accounts[1] (이영희) 계좌가 20000원 입금

		//>>> 여기에 deposit 호출 코드를 작성하세요.
		accounts[1].deposit(20000);
		// accounts[1] = 이영희 계좌를 꺼내서 그 객체의 deposit 메소드 호출
		// -> 이영희의 balance: 50000 -> 70000
		// -> totalAsset: 80000 -> 100000


		//[12] accounts[2] (박민수) 계좌가 50000원 출금 시도
		//     (박민수 잔액은 30000원뿐이라 50000원 출금은 실패해야 정상!)
		//     ★withdraw의 반환값(boolean)을 변수에 저장한 뒤 if문으로 성공/실패를 나눠 출력★
		//     성공하면 "출금 처리 완료" 출력, 실패하면 "출금 처리 불가" 출력

		//>>> 여기에 withdraw 호출 + boolean 변수 저장 + if문을 작성하세요.
		boolean result = accounts[2].withdraw(50000);
						// accounts[2] = 박민수 계좌의 withdraw(50000) 호출
						// -> 메소드 내부: 50000 > 30000 이 참이므로 "잔액 부족" 출력 후 false 반환
						// -> 그 false 값이 result 변수에 저장됨

		if(result) { //result가 true라면 (즉 출금이 성공했다면 )
			
			System.out.println("출금 처리 완료");
			
		}else {//result가 false라면 (즉 출금에 실패했다면 )
			
			System.out.println("출금 처리 불가");
		}

		System.out.println();

		//[13] 은행 전체 현황 출력
		//     ★static 메소드이므로 객체가 아니라 "클래스명"으로 호출한다★

		//>>> 여기에 showBankStatus 호출 코드를 작성하세요.
		BankAccount.showBankStatus();
		// accounts[0] 같은 객체가 아니라 BankAccount라는 "클래스 이름"으로 직접 호출
		// -> "총 계좌 수: 3개", "총자산: 100000원" 출력

	}
}

/*
	===== 정답 코드 작성 시 예상 실행 결과 =====

	계좌번호: 1001 / 예금주: 김철수 / 잔액: 0원
	계좌번호: 1002 / 예금주: 이영희 / 잔액: 50000원
	계좌번호: 1003 / 예금주: 박민수 / 잔액: 30000원

	[1002]에 20000원 입금 완료 (잔액: 70000원)
	[1003] 잔액 부족으로 출금 실패
	출금 처리 불가

	===== 은행 전체 현황 =====
	총 계좌 수: 3개
	총자산: 100000원

	★확인 포인트★
	- 계좌번호가 1001, 1002, 1003으로 "자동으로" 하나씩 증가하는지
	  (accountCount라는 static 변수 하나를 모든 계좌가 공유하기 때문에 가능함)
	- 박민수는 입금 없이 출금만 시도했으므로 잔액 30000원 그대로 실패해야 함
	- 총자산 100000원 = 0(김철수) + 70000(이영희, 입금후) + 30000(박민수, 출금실패로 그대로)
	- 총 계좌 수는 accounts 배열 길이(3)와 accountCount 값이 일치해야 함
*/
