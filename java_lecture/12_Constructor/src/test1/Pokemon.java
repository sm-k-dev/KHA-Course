package test1;

//==========================================================================
// ★ 문제1. 포켓몬(Pokemon) 배틀 클래스 만들기 ★
//--------------------------------------------------------------------------
// 스타크래프트 Unit 예제와 똑같은 구조로 포켓몬 클래스를 완성하세요.
// 아래 주석의 지시대로 코드를 직접 작성하면 됩니다.
//==========================================================================

public class Pokemon { // 포켓몬 설계도

	// 1. 객체 변수 선언 - 포켓몬의 데이터 정보 저장할 용도
	String name; 	// 포켓몬 이름 (예: "피카츄", "꼬부기", "리자몽") 저장
	int hp;			// 포켓몬 기본체력 저장
	int power;		// 포켓몬 기술공격력 저장

	// ------------------------------------------
	//	[풀이] 생성자 3개를 왜 이렇게 나눴나
	//		- "이름만 아는 경우" / "	
	// ------------------------------------------
	// 2. 첫번째 생성자 : 포켓몬 이름만 매개변수로 받는 생성자
	// (체력 : 100, 공격력 : 15 로 고정)
	// ★ this() 생성자 호출구문을 사용해서 세번째 생성자에게 넘기세요.
	public Pokemon ( String name ) {
		this(name, 100, 15);
	}

	// 두번째 생성자 : 포켓몬 이름과 체력을 매개변수로 받는 생성자
	// (공격력 : 15 로 고정)
	// ★ this() 생성자 호출구문을 사용하세요.

	public Pokemon ( String name, int hp ) {
		this(name, hp, 15);
	}

	// 세번째 생성자 : 이름, 체력, 공격력 모두 매개변수로 받는 생성자
	// ★ this 키워드를 사용하여 모든 객체변수값을 초기화하세요.
	// (힌트: this.name = name; 형식)

	public Pokemon ( String name, int hp, int power) {
		this.name = name;
		this.hp = hp;
		this.power = power;
	}

	// 3. 포켓몬의 행동을 메소드로 표현하기 위해 메소드 선언

	// 첫번째 메소드
	// 메소드명 : skill
	// 매개변수 : Pokemon target (공격 당할 상대 포켓몬 객체)
	// 기능 순서:
	// 조건문1 : 현재 포켓몬(this)의 hp가 0보다 작거나 같으면
	// "OOO은(는) 기절해서 기술을 쓸 수 없습니다!" 출력 후 return
	// 조건문2 : target 포켓몬의 hp가 0보다 작거나 같으면
	// "OOO은(는) 이미 기절했습니다!" 출력 후 return
	// 공격처리 : target의 hp를 this의 power만큼 차감
	// (힌트: target.hp -= this.power;)
	// 출력 : "OOO이(가) OOO을(를) 공격합니다!(공격력:XX)"
	// 마무리 : target의 hp가 0이하가 되면 hp를 0으로 고정하고
	// "OOO이(가) 기절했습니다!!" 출력

	public void skill ( Pokemon target ) {
		if ( this.hp <= 0 ) {
			System.out.println(this.name + "은(는) 기절해서 기술을 쓸 수 없습니다!");
			return;
		}
		
		if ( target.hp <= 0 ) {
			System.out.println( target.name + "은(는) 이미 기절했습니다!");
			return;
		}
		
		target.hp -= this.power;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.power + ")");
		
		if ( target.hp <= 0 ) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 기절했습니다!!");
		}
	}

	// 두번째 메소드
	// 메소드명 : status
	// 기능 : 현재 포켓몬 상태 출력
	// 출력형식 : [포켓몬 상태] 이름 - 체력: XX, 공격력: XX

	public void status () {
		System.out.println( "[포켓몬 상태] " + this.name + " - 체력: " + this.hp + ", 공격력: " + this.power );
	}

	public static void main(String[] args) {

		// Pokemon클래스의 첫번째 객체 생성 : 이름만 받는 생성자 호출
		// 변수명 pikachu, 이름 "피카츄" -> 체력 100, 공격력 15 자동 설정
		Pokemon pikachu = new Pokemon("피카츄");

		// Pokemon클래스의 두번째 객체 생성 : 이름과 체력을 받는 생성자 호출
		// 변수명 squirtle, 이름 "꼬부기", 체력 90
		Pokemon squirtle = new Pokemon("꼬부기", 90);

		// Pokemon클래스의 세번째 객체 생성 : 이름, 체력, 공격력 모두 받는 생성자 호출
		// 변수명 charizard, 이름 "리자몽", 체력 180, 공격력 40
		Pokemon charizard = new Pokemon("리자몽", 180, 40);

		// 위 생성된 3개의 Pokemon객체 상태 출력 (status 메소드 호출)
		pikachu.status();
		squirtle.status();
		charizard.status();

		System.out.println("\n==== 배틀 시작 ====");

		// 배틀 시뮬레이션 - 아래 순서대로 skill 메소드를 호출하세요.
		// ① 피카츄가 꼬부기 공격
		// ② 꼬부기가 피카츄 공격
		// ③ 리자몽이 꼬부기 공격 x 2번
		// ④ 리자몽이 피카츄 공격 x 3번

		pikachu.skill(squirtle);
		squirtle.skill(pikachu);
		charizard.skill(squirtle);
		charizard.skill(squirtle);
		charizard.skill(pikachu);
		charizard.skill(pikachu);
		charizard.skill(pikachu);

		System.out.println("\n==== 배틀 종료 후 상태 ====");

		pikachu.status();
		squirtle.status();
		charizard.status();

	}

}

/*
 * ===== 정답 코드 작성 시 예상 실행 결과 =====
 * 
 * [포켓몬 상태] 피카츄 - 체력: 100, 공격력: 15 [포켓몬 상태] 꼬부기 - 체력: 90, 공격력: 15 [포켓몬 상태] 리자몽 -
 * 체력: 180, 공격력: 40
 * 
 * ==== 배틀 시작 ==== 피카츄이(가)꼬부기을(를) 공격합니다!(공격력:15) 꼬부기이(가)피카츄을(를) 공격합니다!(공격력:15)
 * 리자몽이(가)꼬부기을(를) 공격합니다!(공격력:40) 리자몽이(가)꼬부기을(를) 공격합니다!(공격력:40) 꼬부기이(가) 기절했습니다!!
 * 리자몽이(가)피카츄을(를) 공격합니다!(공격력:40) 리자몽이(가)피카츄을(를) 공격합니다!(공격력:40) 리자몽이(가)피카츄을(를)
 * 공격합니다!(공격력:40) 피카츄이(가) 기절했습니다!!
 * 
 * ==== 배틀 종료 후 상태 ==== [포켓몬 상태] 피카츄 - 체력: 0, 공격력: 15 [포켓몬 상태] 꼬부기 - 체력: 0, 공격력:
 * 15 [포켓몬 상태] 리자몽 - 체력: 180, 공격력: 40
 */
