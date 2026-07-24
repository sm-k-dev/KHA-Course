// [1] Unit 클래스 - 유닛 설계도
public class Unit {
	
	// 멤버 변수 - 클래스 변수 (static), 객체 변수로 나뉜다
	
	// 객체 변수
	String	name;	// 유닛 이름
	int		hp;		// 유닛 체력
	int		damage;	// 유닛 기본 공격력
	
	// 생성자
	// 1. 매개변수가 3개인 생성자
	public Unit(String name, int hp, int damage) {
		super();					// 부모 Object 클래스의 기본생성자 호출
		this.name = name;			// 유닛 이름 저장
		this.hp = hp;				// 유닛 체력 저장
		this.damage = damage;		// 유닛 기본 공격력 저장
	}

	// 2. name, hp 생성자
	public Unit(String name, int hp) {
		super();
		this.name = name;
		this.hp = hp;
	}
	
	// 3. name 생성자
	public Unit(String name) {
		super();
		this.name = name;
	}
	
	// 메소드
	// 1. 상대 유닛 Marine 객체를 공격하는 행동의 메소드
	public void attackMarine ( Marine target ) {
		
		if (this.hp <= 0) { // new Zergling ("저글링", 80).hp <= 0
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할 수 없습니다.");
			return;
		}
		
		if (target.hp <= 0) { // new Marin("마린").hp <= 0
			System.out.println(target.name + "은(는) 이미 파괴되어 공격할 수 없습니다.");
		}
		
		// 저글링이 마린을 공격!
		// Marin 상대 유닛의 체력을 차감 하기 위해 공격하는 유닛객체의 공격력 만큼 감소
		target.hp -= this.damage;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		// Marin 상대 유닛의 체력이 0이 되면
		if (target.hp <= 0) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 파괴되었습니다.");
		}
	}
	
	public void status () {
		System.out.println("");
	}
}
