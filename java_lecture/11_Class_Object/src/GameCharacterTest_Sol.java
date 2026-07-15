/*
  게임 캐릭터 — GameCharacter

  스토리 : 캐릭터는 레벨, 체력, 경험치를 가진다. 
         공격받으면 체력이 줄고 0 이하면 사망 처리, 
         경험치를 100 모으면 자동으로 레벨업하고 경험치가 초기화된다.
         
1단계 : 현실의 게임 캐릭터 객체 모델링
- 데이터 : 이름(name), 레벨(level), 체력(hp), 경험치(exp)
- 기능   : 피해받기(takeDamage), 경험치 획득(gainExp), 상태 출력(printStatus)

*/

// 2단계 : 자바 코드로 class 설계 (데이터=변수 + 기능=메소드)
public class GameCharacterTest_Sol {
	
//클래스 변수 선언		
	String name; // 캐릭터 이름 저장할 변수	
	int level; // 레벨 저장할 변수	
	int hp;   // 체력 저장할 변수
	int exp;  // 경험치 저장할 변수
	
//클래스 메소드 선언
	/*
	 메소드명 : takeDamage
	 기   능  : hp를 dmg만큼 줄이고, 0 이하가 되면 0으로 고정 후
	            사망 메시지 출력, 아니면 남은 체력 출력
	*/	
	void takeDamage(int dmg) {
		
		hp -= dmg;//현재 체력에서 받은 피해량 만큼 차감
		
		if(hp <= 0) {//체력이 0이하로 떨어 졌는지 검사 	
			
			hp = 0; //음수가 되지 않도록 0으로 고정		
			System.out.println(name + " 사망했습니다.");//사망 메시지 출력
			
		}else {//체력이 남아 있지 않는 경우 검사 
			
			System.out.println("용사 피해를 입었다! 남은 체력: " + hp);   //남은 체력 출력
		}	
	}
	
	/*
	 메소드명 : gainExp
	 기   능  : exp에 amount를 더하고, 100 이상이면 레벨업(exp 초기화),
	            아니면 경험치 획득 메시지 출력
	*/
	void gainExp(int amount) {
		
		exp += amount;//현재 경험치에 획득한 경험치를 더함
		
		if(exp >= 100) {//현재 경험치가 100이상 모였는지 검사 
					
			level++; //레벨업  레벨 1증가
			
			exp = 0; //레벨업후 경험치를 0으로 초기화 
			
			System.out.println("레벨업! 현재 레벨: " + level);
		
		}else {//현재 경험치가 100미만인 경우 
			
			System.out.println("경험치 획득! 현재 경험치: "  + exp); //경험치 획득 메세지 출력 
		}
		
	}

	/*
	 메소드명 : printStatus
	 기   능  : 이름, 레벨, 체력, 경험치를 한 줄로 출력한다
	*/
	void printStatus() {
	
		System.out.println("이름: " + name + "/ 레벨: " + level + "/ 체력: " + hp + " / 경험치: " + exp);
		
	}	
	
	public static void main(String[] args) {
		// 3단계 : GameCharacter 객체 생성 후 데이터 저장 및 사용
		//1. 객체 생성
		GameCharacterTest c = new GameCharacterTest();
		/*
		  				c                          0x16 주소값
					[0x16] =  ------------------------------------------------
						         |  String name; [nul => "용사"]
						         |  int level;   [0 =>  1 => 2]
						         |  int hp;      [0 =>  100 => 70]
						         |  int exp;     [0 =>   0 => 80 => 130 => 0]
						         |
						         |  takeDamage(){}, gainExp(){}, printStatus(){}
						         ------------------------------------------------
	*/
		//2. 데이터 저장
		c.name = "용사";
		c.level = 1; c.hp = 100; c.exp = 0;

		//3. 기능 호출
		c.takeDamage(30);     // hp 100 -> 70
		c.gainExp(80);        // exp 0 -> 80  
		c.gainExp(50); 		 // exp 130 -> 레벨업! level 1->2, exp 0
		c.printStatus();
		/*
 			 출력 :  용사 피해를 입었다! 남은 체력: 70
			        경험치 획득! 현재 경험치: 80
			        레벨업! 현재 레벨: 2
			        이름: 용사 / 레벨: 2 / 체력: 70 / 경험치: 0
		 */
	}

}
