//=====================================================================
// [종합문제] 스타크래프트 유닛 전투 시스템
//  학습 목표 : 상속 · 생성자 오버로딩 · 메소드 오버라이딩 · 업캐스팅 · 다운캐스팅
//---------------------------------------------------------------------
// 작성 방법 : 각 주석 바로 아래 줄에 코드를 직접 작성한다.
// 저장 방법 : 이 파일을 UnitTest.java 라는 이름으로 저장한 뒤 실행한다.
// 채점 방법 : 맨 아래 [실행 결과] 와 한 글자도 다르지 않게 출력되어야 한다.
//=====================================================================

//[1] Unit 클래스 - 모든 유닛의 부모 클래스 역할을 함
class Unit {

	//문제 1. 유닛 이름을 저장할 String 자료형 인스턴스변수 name 을 선언하시오.
	String name;

	//문제 2. 유닛 체력을 저장할 int 자료형 인스턴스변수 hp 를 선언하시오.
	int hp;

	//문제 3. 유닛 공격력을 저장할 int 자료형 인스턴스변수 damage 를 선언하시오.
	int damage;

	//문제 4. 이름 · 체력 · 공격력 3개를 매개변수로 전달받아
	//        인스턴스변수 name, hp, damage 를 각각 초기화하는 생성자를 선언하시오.
	//        (매개변수명과 인스턴스변수명이 같으므로 this 키워드를 사용할 것)
	public Unit(String name, int hp, int damage) {
		 		 //"시저탱크",    150,     35
		super();
		this.name = name;
		this.hp = hp;
		this.damage = damage;
	}

	//문제 5. 이름 · 체력 2개만 매개변수로 전달받는 생성자를 선언하시오. (생성자 오버로딩)
	//        name 과 hp 는 전달받은 값으로 초기화하고, damage 는 기본값 10 으로 초기화한다.
	public Unit(String name, int hp) {
					//"저글링", 80
		super();
		this.name = name;
		this.hp = hp;
		this.damage = 10;
	}


	//문제 6. 이름 1개만 매개변수로 전달받는 생성자를 선언하시오. (생성자 오버로딩)
	//        name 은 전달받은 값으로 초기화하고, hp 는 기본값 100, damage 는 기본값 10 으로 초기화한다.
	public Unit(String name) {
		super();
		this.name = name;
		this.hp = 100;
		this.damage = 10;
	}

	//문제 7. 상대 유닛을 공격하는 메소드를 선언하시오.
	//        메소드 선언부 : public void attack(Unit target)
	//        매개변수 자료형이 부모 Unit 이므로 Marin, Zergling, Tank 자식 객체가
	//        업캐스팅되어 전달될 수 있다.
	//        아래 7-1 ~ 7-6 순서 그대로 구현할 것.
	public void attack(Unit target) { //<======== new Zergling("저글링",80);
		
		//   7-1. 공격하는 자신(this)의 hp 가 0 이하이면
		//        "OOO은(는) 이미 파괴되어 공격할수 없습니다." 를 출력하고 메소드를 종료(return)한다.
		if(this.hp <= 0) {
			System.out.println(this.name + "은(는) 이미 파괴되어 공격할수 없습니다.");
			return;
		}
		//   7-2. 공격 대상(target)의 hp 가 0 이하이면
		//        "OOO은(는) 이미 파괴되었습니다." 를 출력하고 메소드를 종료(return)한다.
		if(target.hp <= 0) {
			System.out.println(target.name + "은(는) 이미 파괴되었습니다.");
			return;
		}
		//   7-3. 공격 대상의 hp 를 자신의 damage 만큼 감소시킨다.
		target.hp -= this.damage;
	
		//   7-4. "OOO이(가) XXX을(를) 공격합니다! (공격력: 15)" 형식으로 출력한다.
		System.out.println(this.name + "이(가) " + target.name + "을(를) 공격합니다! (공격력: " + this.damage +")");
		
		//   7-5. 공격 후 대상의 hp 가 0 이하가 되었다면
		//        hp 를 0 으로 고정한 뒤 "XXX이(가) 파괴되었습니다!" 를 출력한다.
		if(target.hp <= 0) {
			target.hp = 0;
			System.out.println(target.name + "이(가) 파괴되었습니다!");
		}
		//
		//   7-6. [다운캐스팅 구간]
		//        target 이 Tank 객체이면 Tank 자료형으로 다운캐스팅한 뒤
		//        "> 시저탱크는  두꺼운 장갑을 가지고 있다!" 를 출력한다.
		//        target 이 Zergling 객체이면 Zergling 자료형으로 다운캐스팅한 뒤
		//        "> 저글링은 빠르게 움직인다!" 를 출력한다.
		//        (다운캐스팅 전에 instanceof 로 반드시 확인할 것)
		if( target  instanceof Tank ) {
			//다운캐스팅
			Tank t = (Tank)target;
			System.out.println("> 시저탱크는  두꺼운 장갑을 가지고 있다!");
		}
		if(target instanceof Zergling) {
			//다운캐스팅
			Zergling z = (Zergling)target;
			System.out.println("> 저글링은 빠르게 움직인다!");
		}
	}//==== accack() 메소드 끝

	//문제 8. 현재 유닛의 상태를 출력하는 메소드를 선언하시오.
	//        메소드 선언부 : public void status()
	//        출력 형식 : [유닛 상태] 마린 - 체력 : 90, 공격력 : 15
	public void status() {
		System.out.println("[유닛 상태] " + this.name + " - 체력 : " +  this.hp + ", 공격력 : " + this.damage);
	}


}//---- Unit 클래스 끝


//[2] Marin 클래스 - Unit 을 상속받은 자식 클래스
class Marin extends Unit {

	//문제 9. 이름 1개만 매개변수로 전달받는 생성자를 선언하시오.
	//        부모 Unit 의 생성자 중 이름 1개만 받는 생성자를 super() 로 호출해
	//        체력 100, 공격력 10 으로 초기화되게 한다.
	public Marin(String name) {
		super(name);
	}

	//문제 10. 마린 전용 기능 스팀팩 메소드를 선언하시오.
	//         메소드 선언부 : public void stimPack()
	//
	public void stimPack() {
		//   10-1. 체력이 10 이하이면
		//         "OOO은(는) 체력이 부족해 스팀팩을 사용할 수 없습니다." 출력 후 return.
		if(super.hp <= 10) {
			System.out.println(super.name + "은(는) 체력이 부족해 스탬팩을 사용할 수 없습니다.");
			return;
		}
		//   10-2. 그렇지 않으면 체력을 10 감소, 공격력을 5 증가시킨다. (super 키워드 사용)
		super.hp -= 10;        super.damage += 5;
		
		//   10-3. "OOO이(가) 스팀팩 기능 사용!" 과 ">>> 체력 -10,  공격력 + 5" 를 차례로 출력한다.
		System.out.println(super.name + "이(가) 스탬팩 기능 사용!");
		System.out.println(">>> 체력 -10, 공격력 + 5");
	}
}


//[3] Zergling 클래스 - Unit 을 상속받은 자식 클래스
class Zergling extends Unit {

	//문제 11. 이름 · 체력 2개를 매개변수로 전달받는 생성자를 선언하시오.
	//         부모 Unit 의 생성자 중 이름 · 체력 2개를 받는 생성자를 super() 로 호출한다.
	//         (공격력은 부모 생성자에서 기본값 10 이 들어간다)
	public Zergling(String name, int hp) {
						//"저글링",   80
		super(name, hp);
	}
    
	//문제 12. 저글링 전용 기능 돌진 메소드를 선언하시오.
	//         메소드 선언부 : public void rush()
	public void rush() {
		//   12-1. 공격력을 3 증가시킨다. (super 키워드 사용)
		super.damage += 3;
		//   12-2. "OOO이(가) 돌진한다!" 와 ">>> 다음 공격  공격력 +3" 을 차례로 출력한다.
		System.out.println(super.name + "이(가) 돌진한다!");
	}
	
}


//[4] Tank 클래스 - Unit 을 상속받은 자식 클래스
class Tank extends Unit {

	//문제 13. 시즈 모드 상태를 저장할 boolean 자료형 인스턴스변수 siegeMode 를
	//         초기값 false 로 선언하시오.
	boolean siegeMode = false;

	//문제 14. 이름 · 체력 · 공격력 3개를 매개변수로 전달받는 생성자를 선언하시오.
	//         부모 Unit 의 생성자 중 3개를 받는 생성자를 super() 로 호출한다.
	public Tank(String name, int hp, int damage) {
				 //"시저탱크",    150,     35
		
		super(name,  hp, damage);
	}

	//문제 15. 시저탱크 전용 기능 시즈 모드 메소드를 선언하시오.
	//         메소드 선언부 : public void siegeMode()	
	public void siegeMode() {
		//   15-1. 이미 siegeMode 가 true 이면
		//         "OOO은(는) 이미 시즈 모드입니다." 출력 후 return.
		if(siegeMode) {
			System.out.println(super.name + "은(는) 이미 시즈 모드입니다.");
			return;
		}		
		//   15-2. 그렇지 않으면 siegeMode 를 true 로 바꾸고 공격력을 15 증가시킨다.
		this.siegeMode = true;      super.damage += 15;
		
		//   15-3. "OOO이(가) 시즈 모드로 전환!" 과 ">>> 공격력 +15 (이동 불가)" 를 차례로 출력한다.
		System.out.println(super.name + "이(가) 시즈 모드로 전환!");
		System.out.println(">>> 공격력 +15 (이동 불가)");
	}
}//==================> class Tank


public class UnitTest {
	public static void main(String[] args) {
		//문제 16. Marin 객체를 생성해 부모 Unit 자료형 참조변수 marin 에 저장하시오. (업캐스팅)
		//         전달값 : "마린"  →  체력 100, 공격력 10 이 된다.
		Unit  marin = new Marin("마린");
		//			  =============================
		//                String name = ["마린"]
		//                int    hp   = [100]              <==== Unit부모 메모리 영역
		//                int   damage =  [10]
		//					
		//				   attack() 메소드 있음
		//				   static() 메소드 있음 
		//            ============================
		//				    변수는   없었다!				    <=== Marin자식 메모리 영역 
		//					stimPack()  메소드는 있다
		//			  ============================	

		//문제 17. Zergling 객체를 생성해 부모 Unit 자료형 참조변수 zergling 에 저장하시오. (업캐스팅)
		//         전달값 : "저글링", 80  →  공격력 10 이 된다.
		Unit  zergling = new Zergling("저글링", 80);
				

		//문제 18. Tank 객체를 생성해 부모 Unit 자료형 참조변수 tank 에 저장하시오. (업캐스팅)
		//         전달값 : "시저탱크", 150, 35
		Unit  tank   = new Tank("시저탱크", 150, 35);
		

		//문제 19. "\n======유닛의 고유 기능 사용========" 을 출력하시오.
		System.out.println("\n======유닛의 고유 기능 사용========");

		//문제 20. marin 을 Marin 으로 다운캐스팅해서 stimPack() 을 호출하시오.
		//         (참조변수 자료형이 Unit 이므로 다운캐스팅 없이는 호출할 수 없다)
		((Marin)marin).stimPack();

		//문제 21. zergling 을 Zergling 으로 다운캐스팅해서 rush() 를 호출하시오.
		((Zergling)zergling).rush();

		//문제 22. tank 를 Tank 로 다운캐스팅해서 siegeMode() 를 호출하시오.
		((Tank)tank).siegeMode();

		//문제 23. "\n======유닛 상태========" 를 출력하시오.
		System.out.println("\n======유닛 상태========");


		//문제 24. marin, zergling, tank 의 status() 를 차례로 호출하시오.
		marin.status();
		zergling.status();
		tank.status();


		//문제 25. "\n===== 전 투 시 작 =====" 을 출력하시오.
		System.out.println("\n===== 전 투 시 작 =====");


		//문제 26. marin 이 zergling 을 공격하게 하시오.
		marin.attack(zergling); 


		//문제 27. zergling 이 marin 을 공격하게 하시오.
		zergling.attack(marin); 


		//문제 28. tank 가 zergling 을 공격하게 하시오. (1차)
        tank.attack(zergling);

		//문제 29. tank 가 zergling 을 공격하게 하시오. (2차 - 저글링이 파괴된다)
        tank.attack(zergling);  

		//문제 30. "\n==== 전 투 종 료 ======" 를 출력한 뒤
		//         zergling 과 marin 의 status() 를 차례로 호출하시오.
		System.out.println("\n==== 전 투 종 료 ======");
		zergling.status();
		marin.status();
	}
}

/*=====================================================================
[실행 결과 - 이 출력과 똑같이 나와야 정답]


======유닛의 고유 기능 사용========
마린이(가) 스팀팩 기능 사용!
>>> 체력 -10,  공격력 + 5
저글링이(가) 돌진한다!
>>> 다음 공격  공격력 +3
시저탱크이(가) 시즈 모드로 전환!
>>> 공격력 +15 (이동 불가)

======유닛 상태========
[유닛 상태] 마린 - 체력 : 90, 공격력 : 15
[유닛 상태] 저글링 - 체력 : 80, 공격력 : 13
[유닛 상태] 시저탱크 - 체력 : 150, 공격력 : 50

===== 전 투 시 작 =====
마린이(가) 저글링을(를) 공격합니다! (공격력: 15)
> 저글링은 빠르게 움직인다!
저글링이(가) 마린을(를) 공격합니다! (공격력: 13)
시저탱크이(가) 저글링을(를) 공격합니다! (공격력: 50)
> 저글링은 빠르게 움직인다!
시저탱크이(가) 저글링을(를) 공격합니다! (공격력: 50)
저글링이(가) 파괴되었습니다!
> 저글링은 빠르게 움직인다!

==== 전 투 종 료 ======
[유닛 상태] 저글링 - 체력 : 0, 공격력 : 13
[유닛 상태] 마린 - 체력 : 77, 공격력 : 15

=====================================================================*/
