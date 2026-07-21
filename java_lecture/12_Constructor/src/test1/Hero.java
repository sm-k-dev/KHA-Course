package test1;

//==========================================================================
// ★ 문제2. 던전 용사(Hero) 전투 클래스 만들기 ★  (문제1보다 조금 심화)
//--------------------------------------------------------------------------
// 스타크래프트 Unit 예제 구조 + 회복(heal) 메소드가 추가된 버전입니다.
// 아래 주석의 지시대로 코드를 직접 작성하세요.
//==========================================================================

public class Hero { // 용사(전사/마법사/몬스터 모두 이 설계도로 생성)

	// 1. 객체 변수 선언 - 캐릭터의 데이터 정보 저장할 용도
	String name;	// 캐릭터 이름 (예: "전사", "마법사", "드래곤") 저장
	int hp; 		// 캐릭터 기본체력 저장
	int damage;		// 캐릭터 기본공격력 저장
	int potion;		// 회복물약 개수 저장 ★문제1에 없던 새 변수★

	// 2. 첫번째 생성자 : 이름만 매개변수로 받는 생성자
	// (체력 : 100, 공격력 : 20, 물약 : 3개 로 고정)
	// ★ this() 생성자 호출구문으로 세번째 생성자에게 넘기세요.

	public Hero ( String name ) {
		this(name, 100, 20, 3);
	}

	// 두번째 생성자 : 이름과 체력을 매개변수로 받는 생성자
	// (공격력 : 20, 물약 : 3개 로 고정)
	// ★ this() 생성자 호출구문을 사용하세요.

	public Hero ( String name, int hp ) {
		this(name, hp, 20, 3);
	}

	// 세번째 생성자 : 이름, 체력, 공격력, 물약개수 모두 매개변수로 받는 생성자
	// ★ this 키워드를 사용하여 모든 객체변수값(4개)을 초기화하세요.

	public Hero ( String name, int hp, int damage, int potion ) {
		this.name = name;
		this.hp = hp;
		this.damage = damage;
		this.potion = potion;
	}

	// 3. 캐릭터의 행동을 메소드로 표현하기 위해 메소드 선언

	// 첫번째 메소드
	// 메소드명 : attack
	// 매개변수 : Hero target (공격 당할 상대 캐릭터 객체)
	// 기능 순서: (Unit 예제의 attack과 동일한 구조)
	// 조건문1 : 현재 캐릭터(this)의 hp가 0보다 작거나 같으면
	// "OOO은(는) 쓰러져서 공격할 수 없습니다!" 출력 후 return
	// 조건문2 : target 캐릭터의 hp가 0보다 작거나 같으면
	// "OOO은(는) 이미 쓰러져 있습니다!" 출력 후 return
	// 공격처리 : target의 hp를 this의 damage만큼 차감
	// 출력 : "OOO이(가) OOO을(를) 공격합니다!(공격력:XX)"
	// 마무리 : target의 hp가 0이하가 되면 hp를 0으로 고정하고
	// "OOO이(가) 쓰러졌습니다!!" 출력

	public void attack (Hero target) {
		if ( this.hp <= 0 ) {
			System.out.println(this.name + "은(는) 쓰러져서 공격할 수 없습니다!");
			return;
		}
		
		if ( target.hp <= 0 ) {
			System.out.println(target.name + "은(는) 이미 쓰러져 있습니다!");
			return;
		}
		
		target.hp -= this.damage;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		if ( target.hp <= 0 ) {
			target.hp = 0;
			System.out.println( target.name + "이(가) 쓰러졌습니다!!" );
		}
	}

	// 두번째 메소드 ★문제1에 없던 새 메소드★
	// 메소드명 : heal
	// 매개변수 : 없음 (자기 자신을 회복하는 메소드)
	// 기능 순서:
	// 조건문1 : 현재 캐릭터(this)의 hp가 0보다 작거나 같으면
	// "OOO은(는) 쓰러져서 물약을 마실 수 없습니다!" 출력 후 return
	// 조건문2 : this의 potion이 0보다 작거나 같으면 (물약이 없으면)
	// "OOO의 물약이 없습니다!" 출력 후 return
	// 회복처리 : this의 hp를 30 증가시키고, potion을 1개 차감
	// (힌트: this.hp += 30; this.potion -= 1; 또는 this.potion--;)
	// 출력 : "OOO이(가) 물약을 마셨습니다! (체력+30, 남은물약:X개)"

	public void heal () {
		if ( this.hp <= 0 ) {
			System.out.println( this.name + "은(는) 쓰러져서 물약을 마실 수 없습니다!");
			return;
		}
		
		if ( this.potion <= 0 ) {
			System.out.println( this.name + "의 물약이 없습니다!");
			return;
		}
		
		this.hp += 30;
		this.potion--;
		
		System.out.println( this.name + "이(가) 물약을 마셨습니다! (체력 + 30, 남은 물약: " + this.potion + "개)");
	}

	// 세번째 메소드
	// 메소드명 : status
	// 기능 : 현재 캐릭터 상태 출력
	// 출력형식 : [캐릭터 상태] 이름 - 체력: XX, 공격력: XX, 물약: X개

	public void status () {
		System.out.println("[캐릭터 상태] " + this.name + " - 체력: " + this.hp + ", 공격력: " + this.damage + ", 물약: " + this.potion + "개");
	}

	public static void main(String[] args) {

		// Hero클래스의 첫번째 객체 생성 : 이름만 받는 생성자 호출
		// 변수명 warrior, 이름 "전사" -> 체력 100, 공격력 20, 물약 3개 자동 설정

		Hero warrior = new Hero("전사");

		// Hero클래스의 두번째 객체 생성 : 이름과 체력을 받는 생성자 호출
		// 변수명 mage, 이름 "마법사", 체력 70

		Hero mage = new Hero("마법사", 70);

		// Hero클래스의 세번째 객체 생성 : 이름, 체력, 공격력, 물약 모두 받는 생성자 호출
		// 변수명 dragon, 이름 "드래곤", 체력 200, 공격력 45, 물약 0개

		Hero dragon = new Hero("드래곤", 200, 45, 0);

		// 위 생성된 3개의 Hero객체 상태 출력 (status 메소드 호출)

		warrior.status();
		mage.status();
		dragon.status();

		System.out.println("\n==== 던전 전투 시작 ====");

		// 전투 시뮬레이션 - 아래 순서대로 메소드를 호출하세요.
		// ① 전사가 드래곤 공격
		// ② 드래곤이 전사 공격
		// ③ 드래곤이 마법사 공격
		// ④ 전사가 물약 마시기 (heal 호출)
		// ⑤ 드래곤이 마법사 공격 -> 마법사 쓰러짐!
		// ⑥ 마법사가 드래곤 공격 -> 이미 쓰러져서 공격 불가 메시지 확인
		// ⑦ 드래곤이 물약 마시기 (heal 호출) -> 물약 없음 메시지 확인

		warrior.attack(dragon);
		dragon.attack(warrior);
		dragon.attack(mage);
		warrior.heal();
		dragon.attack(mage);
		mage.attack(dragon);
		dragon.heal();

		System.out.println("\n==== 전투 종료 후 상태 ====");

		warrior.status();
		mage.status();
		dragon.status();

	}

}

/*
 * ===== 정답 코드 작성 시 예상 실행 결과 =====
 * 
 * [캐릭터 상태] 전사 - 체력: 100, 공격력: 20, 물약: 3개 [캐릭터 상태] 마법사 - 체력: 70, 공격력: 20, 물약: 3개
 * [캐릭터 상태] 드래곤 - 체력: 200, 공격력: 45, 물약: 0개
 * 
 * ==== 던전 전투 시작 ==== 전사이(가) 드래곤을(를) 공격합니다!(공격력:20) 드래곤이(가) 전사을(를)
 * 공격합니다!(공격력:45) 드래곤이(가) 마법사을(를) 공격합니다!(공격력:45) 전사이(가) 물약을 마셨습니다! (체력+30,
 * 남은물약:2개) 드래곤이(가) 마법사을(를) 공격합니다!(공격력:45) 마법사이(가) 쓰러졌습니다!! 마법사은(는) 쓰러져서 공격할 수
 * 없습니다! 드래곤의 물약이 없습니다!
 * 
 * ==== 전투 종료 후 상태 ==== [캐릭터 상태] 전사 - 체력: 85, 공격력: 20, 물약: 2개 [캐릭터 상태] 마법사 - 체력:
 * 0, 공격력: 20, 물약: 3개 [캐릭터 상태] 드래곤 - 체력: 180, 공격력: 45, 물약: 0개
 */
