

//[1] Unit 클래스 - 유닛 설계도 
public class Unit {		
	
//객체변수들 
	String name;    //유닛 이름 (예: "마린", "저글링", "탱그")
	int    hp;      //유닛 체력  100
	int   damage;   //유닛 기본 공격력
		
//생성자들
	//1. 유닛 이름, 체력, 공격력을 초기화 할 생성자
	public Unit(String name, int hp, int damage) {
		super();  					//<< 부모 Object 클래스의 기본생성자 호출!
		this.name = name;			//<< 유닛 이름 저장
		this.hp = hp;				//<< 유닛 체력 저장
		this.damage = damage;		//<< 유닛 기본공격력 저장 
	}
	
	//2. 유닛 이름, 체력 만 초기화 할 생성자
	public Unit(String name, int hp) {
		super();
		this.name = name;
		this.hp = hp;
		this.damage = 10;  //기본공격력 10
	}

	//3. 유닛 이름 만 초기화할 생성자 
	public Unit(String name) {
		super();
		this.name = name;
		this.hp = 100;    //기본체력 100
		this.damage = 10; //기본공격력 10
	}
		
//메소드들
	//1. 상대 유닛 Marine객체를 공격하는 행동의 메소드 
	public void attackMarine(Marin target) {   //매개변수로  new Marin(); 객체 주소 전달 받는다.
		
		if(this.hp <= 0) { //new Zergling("저글링", 80).hp <= 0
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할수 없습니다.");
			return;
		}
			
		if(target.hp <= 0) { 	//new Marin("마린").hp <= 0
			System.out.println(target.name + "은(는)이미 파괴되어 공격할수 없습니다.");
		}
		
		//저글링이 마린을 공격!
		//Marin상대 유닛의 체력을  차감 하기 위해 공격하는 유닛객체의 공격력 만큼 감소 시키자
		target.hp  -=  this.damage;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		//Marin상대 유닛의 체력이 0이 되면
		if(target.hp <= 0) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 파괴되었습니다.");
		}
	}
	
	//2. 상대 유닛 Zergling객체를 공격하는 행동의 메소드 
	public void attackZergling(Zergling target) {   //매개변수로  new Zergling("저글링", 80); 객체 주소 전달 받는다.
		
		if(this.hp <= 0) { //new Tank("시저탱크", 150, 35).hp <= 0
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할수 없습니다.");
			return;
		}
			
		if(target.hp <= 0) { //new Zergling("저글링",80).hp <= 0
			System.out.println(target.name + "은(는)이미 파괴되어 공격할수 없습니다.");
		}
		
	//Tank가 Zergling을 공격!!!!!!
		//Zergling상대 유닛의 체력을  차감 하기 위해 공격하는 유닛객체의 공격력 만큼 감소 시키자
		target.hp  -=  this.damage;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		//Zergling상대 유닛의 체력이 0이 되면
		if(target.hp <= 0) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 파괴되었습니다.");
		}
	}
	
	//3. 상대 유닛 Tank객체를 공격하는 행동의 메소드 
	public void attackTank(Tank target) {   //매개변수로  new Tank("시저탱크", 150, 35); 객체 주소 전달 받는다.
		
		if(this.hp <= 0) { //new Zergling("저글링",80).hp <= 0
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할수 없습니다.");
			return;
		}
			
		if(target.hp <= 0) { //new Tank("시저탱크", 150, 35).hp <= 0
			System.out.println(target.name + "은(는)이미 파괴되어 공격할수 없습니다.");
		}
		
	//Zergling가 Tank을 공격!!!!!!
		//Tank상대 유닛의 체력을  차감 하기 위해 공격하는 Zergling유닛객체의 공격력 만큼 감소 시키자
		target.hp  -=  this.damage;
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage + ")");
		
		//Tank상대 유닛의 체력이 0이 되면
		if(target.hp <= 0) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 파괴되었습니다.");
		}
	}
	
	//4. 현재 유닛 상태(체력 그리고 공격력)를 출력 하는 기능의 메소드
	public void status() {
		System.out.println("[유닛 상태] " + this.name + " - 체력 : " + this.hp + ", 공격력 : " + this.damage );
	}
}










