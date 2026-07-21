package test1;

// 스타크래프트 게임 - 마린, 저글링, 질럿

public class Unit { // 유닛 설계도(클래스)
	
	// 1. 클래스 변수 - 유닛의 데이터 정보 저장할 용도
	String	name;	// 유닛 이름
	int		hp;		// 기본 체력
	int		damage;	// 유닛의 기본 공격력

	// 2. 생성자 - 유닛의 객체 데이터 정보를 초기화(처음 설정) - 생성자 단축키: alt shift s o
	// 첫번째 생성자: 유닛의 이름만 매개변수로 받아 체력: 100, 공격력: 10 으로 객체 변수들의 값을 초기화 하는 생성자
	public Unit(String name) {
		this(name, 100, 10);
	}
	
	// 두번째 생성자: 유닛의 이름과 체력만 매개변수로 받아 공격력: 10 으로 객체 변수들의 값을 초기화 하는 생성자
	public Unit(String name, int hp) {
		this(name, hp, 10);
	}
	
	// 세번째 생성자: 유닛의 이름, 체력, 공격력 모두 매개변수로 받아 모든 객체 변수들의 값을 초기화 하는 생성자
	public Unit(String name, int hp, int damage) {
		super(); // 상속부터
		this.name = name;
		this.hp = hp;
		this.damage = damage;
	}
	
	// 3. 메소드 - 유닛들의 행동을 표현
	// 첫번째 메소드 명: attack
	// 기능: 특정 유닛 객체가 다른 유닛 객체를 공격
	public void attack(Unit target) {
		// 조건문1. 현재 생성된 Unit 클래스에 대한객체 (마린객체) 의 hp 객체 변수의 값이 0보다 작거나 같으면?
		if ( this.hp < 0 ) {
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할 수 없습니다!");
			return; // attack 메소드 } 중괄화 빠져나감
		}
		
		// 조건문2. target 매개변수로 전달받은 new Unit ("저글링", 80); 객체의 hp 객체변수의 값이 0보다 작거나 같으면
		if ( target.hp <= 0 ) {
			System.out.println( target.name + "은(는) 이미 파괴되어 공격할 수 없습니다!");
		}
		
		// 매개변수 target으로 전달 받은 new Unit("저글링", 80); 객체의 hp 객체변수(기본체력)을
		// this.damage;로 작성한 new Unit("마린"); 객체의 damage 객체변수 (기본공격력)값으로 차감
		// 요약: 마린이 저글링을 공격
		target.hp -= this.damage;	// target.hp = target.hp - this.damage;
		
		System.out.println( this.name + "(이)가 " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		// 저글링 객체의 체력이 0이 되면 파괴된 상태 표시
		if ( target.hp <= 0 ) {
			target.hp = 0;
			System.out.println( target.name + "이(가) 파괴되었습니다!");
		}
	}
	
	// 두번째 메소드 명: status
	// 기능: 현재 유닛의 상태 출력
	public void status () {
		System.out.println( this.name + "의 현재 체력: " + this.hp + ", 기본 공격력: " + this.damage );
	}

	public static void main(String[] args) {
		
		// 마린 유닛 역할을 하는 객체 하나 생성
		// 유닛이름: 마린, 기본체력: 100, 기본공격력: 10
		Unit marine = new Unit("마린");
		
		// 저글링 유닛 역할을 하는 객체 하나 생성
		// 유닛이름: 저글링, 기본체력: 80, 기본공격력: 10
		Unit zergling = new Unit("저글링", 80);
		
		// 질럿 유닛 역할을 하는 객체 하나 생성
		// 유닛이름: 질럿, 기본체력: 90, 기본공격력: 10
		Unit zealot = new Unit("질럿", 90);
		
		// 시저 탱크 유닛 역할을 하는 객체 하나 생성
		// 유닛이름: 시저탱크, 기본체력: 150, 기본공격력: 35
		Unit tank = new Unit("시저탱크", 150, 35);
		
		// 위에 생성된 마린 역할을 하는 마린, 저글링, 질럿 객체 메모리의 객체 변수 값들을 출력
		marine.status();
		zergling.status();
		zealot.status();
		tank.status();
		
		System.out.println("\n========= 전투 시작 =========");
		
		// 전투 시뮬레이션 - 마린 유닛 객체가 저글링 유닛 객체를 공격한다.
		//마린 유닛 객체가  저글링을 공격한다.
		marine.attack(zergling);
//		marine.attack( new Unit("저글링", 80)   );
		
		//저글링이 마린을 공격한다.
		zergling.attack(marine);
//		zergling.attack( new Unit("마린")   );		
		
		//시저탱크가 저글링 공격한다.
		tank.attack(zergling);
		
		//시저탱크가 마린 공격1
		tank.attack(marine);
		
		//시저탱크가 마린 공격2
		tank.attack(marine);
		
		//시저탱크가 마린 공격3
		tank.attack(marine);
		
		System.out.println("\n========= 전투 종료 후 상태 =========");
		marine.status();
		zergling.status();
		tank.status();

	}

}
