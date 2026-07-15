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
public class GameCharacterTest {

//클래스 변수 선언		
	String name; // 캐릭터 이름 저장할 변수
	int level; // 레벨 저장할 변수
	int hp; // 체력 저장할 변수
	int exp; // 경험치 저장할 변수

//클래스 메소드 선언
	/*
	 * 메소드명 : takeDamage 기 능 : hp를 dmg만큼 줄이고, 0 이하가 되면 0으로 고정 후 사망 메시지 출력, 아니면 남은 체력
	 * 출력
	 */
	void takeDamage(int dmg) {
		if ( hp - dmg <= 0 ) {
			System.out.println(dmg + " 만큼의 데미지를 입어, 당신의 캐릭터 " + name + "(이)가 사망하였습니다.");
		} else {
			hp -= dmg;
			System.out.println(dmg + " 만큼의 데미지를 입었습니다. 남은 체력: " +  hp );
		}
	}

	/*
	 * 메소드명 : gainExp 기 능 : exp에 amount를 더하고, 100 이상이면 레벨업(exp 초기화), 아니면 경험치 획득 메시지
	 * 출력
	 */
	void gainExp(int amount) {
		if ( exp + amount >= 100 ) {
			level++;
			exp = 0;
			System.out.println("축하합니다! " + amount + "의 경험치를 얻어 " + name + "(이)가 " + level + "로 레벨업 하였습니다.");
		} else {
			exp += amount;
			System.out.println("경험치 " + amount + " 를 획득하셨습니다. 레벨 업 까지 남은 경험치 " + (100 - exp));
		}
	}

	/*
	 * 메소드명 : printStatus 기 능 : 이름, 레벨, 체력, 경험치를 한 줄로 출력한다
	 */
	void printStatus() {
		System.out.println(name + "의 현재 레벨: " + level + ", 체력: " + hp + ", 경험치: " + exp);
	}

	public static void main(String[] args) {
		// 3단계 : GameCharacter 객체 생성 후 데이터 저장 및 사용
		// 1. 객체 생성
		GameCharacterTest c = new GameCharacterTest();

		// 2. 데이터 저장
		c.name = "용사";
		c.level = 1;
		c.hp = 100;
		c.exp = 0;

		// 3. 기능 호출
		c.takeDamage(30); // hp 100 -> 70
		c.gainExp(80); // exp 0 -> 80
		c.gainExp(50); // exp 130 -> 레벨업! level 1->2, exp 0
		c.printStatus();
		/*
		 * 출력 : 용사 피해를 입었다! 남은 체력: 70 경험치 획득! 현재 경험치: 80 레벨업! 현재 레벨: 2 이름: 용사 / 레벨: 2 /
		 * 체력: 70 / 경험치: 0
		 */
	}

}
