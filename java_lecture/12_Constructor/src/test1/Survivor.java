package test1;

//==========================================================================
// ★ 문제3. 좀비 서바이벌(Survivor) 클래스 만들기 ★  (문제1·2보다 심화)
//--------------------------------------------------------------------------
// 문제1·2에서 배운 것: 생성자 오버로딩, this, attack/heal 메소드
// 문제3에서 새로 연습할 것 (★static은 사용하지 않습니다★):
//   ① 객체를 "배열"에 담아서 여러 개를 한 번에 관리하기
//   ② for 반복문으로 배열 속 객체 전부를 상대로 전투하기
//   ③ 메소드가 boolean(true/false)을 "반환"하게 만들기
//   ④ 메소드 안에서 또 다른 메소드를 호출하기 (총알 0 -> 재장전)
//
// [설정] 생존자 1명(레온) vs 좀비 12마리(배열)의 웨이브 전투.
//        총알은 10발뿐이라 도중에 반드시 "재장전"이 필요하다!
//==========================================================================

public class Survivor { // 생존자 설계도

	// 1. 객체 변수 선언
	String name;	// 생존자 이름 (예: "레온")
	int hp;		// 생존자 체력
	int ammo;	// 총알 개수 ★공격할 때마다 1발씩 소모★
	int power;	// 총 공격력

	// 2. 생성자 (2개만 만듭니다)

	// 첫번째 생성자 : 이름만 받는 생성자
	// (체력100, 총알10발, 공격력30 으로 고정)
	// ★ this() 생성자 호출구문으로 두번째 생성자에게 넘기세요.

	public Survivor ( String name ) {
		this( name, 100, 10, 30);
	}

	// 두번째 생성자 : 이름, 체력, 총알, 공격력 모두 받는 생성자
	// ★ this 키워드로 객체변수 4개를 초기화하세요.

	public Survivor ( String name, int hp, int ammo, int power ) {
		this.name = name;
		this.hp = hp;
		this.ammo = ammo;
		this.power = power;
	}

	// 3. 메소드 정의

	// 첫번째 메소드
	// 메소드명 : isAlive
	// 반환타입 : boolean ★void가 아니라 boolean을 반환합니다★
	// 매개변수 : 없음
	// 기능 : 이 생존자가 살아있으면 true, 죽었으면 false 반환
	// 힌트: hp가 0보다 크면 살아있는 것.
	// return (this.hp > 0); <- 이 비교결과(true/false)를 그대로 반환
	// ※ boolean 반환 메소드는 if문 조건 자리에 바로 넣어 쓸 수 있어 편리합니다.

	public boolean isAlive () {
		return ( this.hp > 0 );
	}

	// 두번째 메소드
	// 메소드명 : shoot
	// 반환타입 : void
	// 매개변수 : Zombie target (공격할 좀비 객체 1마리)
	// 기능 순서:
	// 조건문1 : 총알이 없으면(ammo <= 0)
	// "OOO의 총알이 떨어졌습니다! 재장전 필요!" 출력 후 return
	// 조건문2 : target 좀비가 이미 죽었으면(hp <= 0)
	// 아무 출력 없이 그냥 return (죽은 좀비엔 총알 낭비 X)
	// 공격처리 : ammo를 1 감소, target.hp를 this.power만큼 감소
	// 출력 : "OOO이(가) 좀비를 공격! (공격력:XX, 남은총알:X발)"
	// 마무리 : target.hp가 0이하가 되면 0으로 고정하고
	// "좀비 처치!" 출력

	public void shoot ( Zombie target ) {
		if ( ammo <= 0 ) {
			System.out.println( this.name + "의 총알이 떨어졌습니다! 재장전 필요!");
			return;
		}
		
		if ( target.hp <= 0 ) return;
		
		ammo--;
		target.hp -= this.power;
		System.out.println( this.name + "이(가) 좀비를 공격! (공격력: " + this.power + ", 남은 총알: " + this.ammo + "발)");
		
		if ( target.hp <= 0 ) {
			target.hp = 0;
			System.out.println("좀비 처치!");
		}
	}

	// 세번째 메소드
	// 메소드명 : reload
	// 반환타입 : void
	// 매개변수 : 없음
	// 기능 : 총알을 10발로 다시 채우고(ammo = 10)
	// "OOO 재장전 완료! (총알:10발)" 출력

	public void reload () {
		ammo = 10;
		System.out.println();
		System.out.println("----------------------" + this.name + " 재장전 완료! (총알: 10발)");
		System.out.println();
	}

	// 네번째 메소드 ★가장 어려운 부분 - 배열 + 반복문 + 메소드 안 메소드호출★
	// 메소드명 : fightWave
	// 반환타입 : void
	// 매개변수 : Zombie[] zombies (좀비 여러 마리가 담긴 "배열")
	// 기능 순서:
	// 1) "===== OOO의 웨이브 전투 시작! =====" 출력
	// 2) for 반복문으로 zombies 배열을 처음부터 끝까지 돈다
	// (힌트: for(int i = 0; i < zombies.length; i++) )
	// 3) 반복 안에서:
	// - 만약 내(this) 총알이 0이면 reload() 메소드를 호출해 재장전
	// (★메소드 안에서 다른 메소드를 호출하는 연습★)
	// - 그 다음 shoot(zombies[i]) 를 호출해 i번째 좀비를 공격
	// 4) 반복이 끝나면 "===== 웨이브 종료! =====" 출력

	public void fightWave ( Zombie[] zombies ) {
		System.out.println("===== " + this.name + "의 웨이브 전투 시작! =====");
		System.out.println();
		
		for ( int i = 0 ; i < zombies.length ; i++ ) {
			if ( this.ammo <= 0 ) this.reload();
			
			this.shoot(zombies[i]);
		}
		
		System.out.println();
		System.out.println("===== 웨이브 종료! =====");
	}

	// 다섯번째 메소드
	// 메소드명 : status
	// 기능 : 생존자 상태 출력
	// 출력형식 : [생존자] 이름 - 체력:XX, 총알:X발, 공격력:XX

	public void status () {
		System.out.println("[생존자] " + this.name + " - 체력: " + this.hp + ", 총알: " + this.ammo + "발, 공격력: " + this.power );
	}

	public static void main(String[] args) {

		// 생존자 객체 생성 : 이름만 받는 생성자 호출
		// 변수명 leon, 이름 "레온" -> 체력100, 총알10, 공격력30

		Survivor leon = new Survivor("레온");

		// 좀비 12마리를 "배열"에 담기
		// ★배열 만드는 문법: Zombie[] 배열명 = new Zombie[12];
		// ★12마리를 일일이 만들기 번거로우니 for 반복문으로 생성하세요.
		// - 이름은 "좀비1", "좀비2" ... 처럼 번호를 붙이고 (힌트: "좀비" + (i+1))
		// - 체력은 모두 30으로 (총알 1발 공격력이 30이므로 한 발에 처치됨)
		// (Zombie 클래스는 아래에 이미 만들어져 있습니다)
		
		Zombie[] zombies = new Zombie[12];
		
		for ( int i = 0 ; i < 12 ; i++ ) {
			zombies[i] = new Zombie ( "좀비" + ( i + 1 ), 30 );
		}

		// 전투 시작 전 생존자 상태 출력

		leon.status();
		
		System.out.println();
		
		// 웨이브 전투 실행! (배열을 통째로 넘긴다)
		// 힌트: leon.fightWave(zombies);
		// ※ 좀비 12마리 > 총알 10발 이므로 도중에 reload가 자동 발동됩니다!

		leon.fightWave(zombies);

		System.out.println();
		
		// 전투 종료 후 생존자 상태 + 각 좀비 상태 출력
		// (좀비 상태는 for문으로 배열을 돌며 zombies[i].status() 호출)
		
		leon.status();
		
		System.out.println();
		
		for ( int i = 0 ; i < zombies.length ; i++ ) {
			zombies[i].status();
		}

	}
}

//==========================================================================
// Zombie 클래스 - 이미 완성되어 있습니다 (수정하지 마세요, 참고용)
//==========================================================================
class Zombie {
	String name; // 좀비 이름
	int hp; // 좀비 체력

	// 이름과 체력을 받는 생성자
	public Zombie(String name, int hp) {
		this.name = name;
		this.hp = hp;
	}

	// 좀비 상태 출력 메소드
	public void status() {
		if (this.hp <= 0) {
			System.out.println("[좀비] " + this.name + " - 처치됨");
		} else {
			System.out.println("[좀비] " + this.name + " - 체력:" + this.hp);
		}
	}
}

/*
 * ===== 정답 코드 작성 시 예상 실행 결과 =====
 * 
 * [생존자] 레온 - 체력:100, 총알:10발, 공격력:30
 * 
 * ===== 레온의 웨이브 전투 시작! ===== 레온이(가) 좀비를 공격! (공격력:30, 남은총알:9발) 좀비 처치! ...
 * (좀비1~10 처치, 총알이 0발까지 줄어듦) ... 레온이(가) 좀비를 공격! (공격력:30, 남은총알:0발) 좀비 처치! 레온 재장전
 * 완료! (총알:10발) <- ★총알 0 -> 자동 재장전 발동!★ 레온이(가) 좀비를 공격! (공격력:30, 남은총알:9발) 좀비 처치!
 * 레온이(가) 좀비를 공격! (공격력:30, 남은총알:8발) 좀비 처치! ===== 웨이브 종료! =====
 * 
 * ==== 전투 종료 후 상태 ==== [생존자] 레온 - 체력:100, 총알:8발, 공격력:30 [좀비] 좀비1 - 처치됨 ... (좀비2
 * ~ 좀비12 모두 처치됨) ... [좀비] 좀비12 - 처치됨
 */
