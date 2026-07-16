/*
	1단계: 현실의 부산은행 계좌, 국민은행 계좌 객체들을 모델링 하여 데이터 + 기능 추출
		데이터 - 계좌번호 (accountNumber), 예금주(owner), 잔액(balance)
		기능 - 입금(deposit), 출금(withdraw), 잔액확인(checkBalance)
*/

// 2단계: 계좌 설계도(class) 만들기
public class BankAccountTest {
	
	// 클래스 변수
	String accountNumber; 	// 1. 계좌번호 (예: "123-456-789") 저장할 클래스 변수
	String owner;			// 2. 예금주 (예: "이영희") 저장할 클래스 변수
	double balance;			// 3. 잔액 (예: 10000.0 원) 저장할 클래스 변수
	
	// 클래스 메소드
	// 1. deposit: 특정 금액 (amount)을 입금하면, 잔액(balance)이 증가
	void deposit ( double amount ) {
		balance += amount;
		
		System.out.println(owner + "님의 계좌에 " + amount + "원이 입금되었습니다.");
		System.out.println("현재 잔액: " + balance + "원");
		System.out.println();
	}
	
	// 2. withdraw: 특정 금액 (amount)을 출금하면, 잔액(balance)을 감소
	void withdraw ( double amount ) {
		
		if ( amount > balance ) {
			System.out.println("잔액이 부족합니다. 출금 실패");
			System.out.println();
		} else {
			balance -= amount;
			
			System.out.println(owner + "님의 계좌에서 " + amount + "원이 출금되었습니다.");
			System.out.println("현재 잔액: " + balance + "원");
			System.out.println();
		}
	}
	
	// 3. checkBalance: 현재 계좌의 잔액 (balance)을 출력
	void checkBalance () {
		System.out.println(owner + "님의 계좌번호: " + accountNumber + "의 현재 잔액: " + balance + "원");
		System.out.println();
	}
	
	// 4. main 메소드: 주 스레드, 자바코드를 실행 시키는 시작 위치
	public static void main(String[] args) {
		
		// 3단계. 객체 생성 후 사용
		
		// 순서1. '은행 계좌 객체 생성
		BankAccountTest account = new BankAccountTest();
		
		// 순서2. 객체 변수 초기화
		account.accountNumber = "123-456-789";
		account.owner = "이영희";
		account.balance = 100000;
		
		// 순서3. 계좌 기능 사용
		account.deposit(50000);
		account.withdraw(30000);
		account.checkBalance();
		account.withdraw(130000);
	}

}
