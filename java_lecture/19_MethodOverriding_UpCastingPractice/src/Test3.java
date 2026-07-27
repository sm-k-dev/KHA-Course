

/* 주제 : 업캐스팅과  다운캐스팅을 하는 이유  응용  */

//부모클래스
class Character {
	
	//모든 케릭터의 공격하는 행동을 메소드로 정의
	public void attack() {
		System.out.println("공격한다");
	}
}
//자식클래스1 : 전사 클래스
class Warrior extends Character {
	
	//부모 Character클래스의 attaack메소드 오버라이딩해서 전사의 기능에 맞게 작성
	@Override
	public void attack() {
		System.out.println("검으로 공격한다");
	}
	
	//방어기능의 메소드로 정의
	public void raiseShield() {
		System.out.println("방패를 올린다");
	}	
}
//자식클래스 2 : 마법사 클래스
class Mage extends Character{
	
	@Override
	public void attack() {
		System.out.println("마법으로 공격한다");
	}
	
	public void castSpell() {
		System.out.println("마법을 시전한다");
	}
}

//모든 케릭터의 행동을 처리하는 기능의 구현된 클래스 
class GameSystem {
					//매개변수는 업캐스팅이 일어나 자식객체 메모리 주소를 전달 받음 
	public void play(Character  character) {  //<=  new Warrior();  전사 
											  //<=  new Mage(); 마법사 
		
		character.attack();       //호출가능  이유 : 매개변수로 전달받은 자식객체메모리영역일 지라도 메소드 오버라이딩된 메소드 이므로 
	//	character.raiseShield();  //호출해서 사용할 수 없다. 이유 : 매개변수 Character character 부모클래스자료형으로 만들어 놓았기떄문에
								  //                          부모 Character 클래스 내부에 작성된 attack메소드만 호출가능
	//  character.castSpell();    //호출해서 사용할 수 없다.
		
		
	  // instancedof 예약어  작성문법
	  //    객체   instanceof   클래스명
	  //     해설 ->  앞에 작성한 객체가  뒤에 작성한 클래스로 만들어진 객치 이냐? 라고 물어보는 instanceof 예약어로
	  //             맞으면 true 반환하고, 틀리면 false를 반환합니다.
		
      // ↓↓↓ 필요한 경우만 다운캐스팅 ↓↓↓
	  //조건 : Character character매개변수로 전달받은 객체가 Warrior자식클래스로 만들어진게 맞아?
	  if(character  instanceof  Warrior) {
		  
		  //다운캐스팅 
		  //장점 :  자식객체인? 전사 new Warrior() 객체의 모든 멤버를 사용가능 하게 됩니다.
		  Warrior     w   =   (Warrior)character;
		  
		  //전사 전용 기능 사용
		  w.raiseShield();  
	  }
	  
	  if(character instanceof  Mage) {
		  
		  Mage m = (Mage)character;		  //맞을 때만 다운캐스팅 하자

		  m.castSpell();     //마법사 전용 기능 사용
	  }
	
	}
}

public class Test3 {
	public static void main(String[] args) {
		
		GameSystem  game = new GameSystem();
		
		//업캐스팅을 하여  Character부모클래스의 참조변수를 만들고  new Warrior();전사 객체를 생성해서 저장
		Character  character = new Warrior();
        /*
							[ Warrior 객체 ]	0x12		
							┌───────────────────────────────┐
							│ 부모(Character) 영역           	│
							│ ----------------------------- │
							│ attack()  ← 오버라이딩 대상   	│
							│                               │
							│ 자식(Warrior) 영역             	│
							│ ----------------------------- │
							│ attack()  ← 재정의(override)  	│
							│ raiseShield()                 │
							└───────────────────────────────┘
         */		
		game.play(character);
		
		//업캐스팅을 하여  Character부모클래스의 참조변수에  new Mage(); 마법사 자식객체를 생성해서 저장
		character = new Mage();
		game.play(character);
			        /*
							[ Mage 객체 ] 0x16
						┌───────────────────────────────┐
						│ 부모(Character) 영역           	│
						│ ----------------------------- │
						│ attack()  ← 오버라이딩 대상   	│
						│                               │
						│ 자식(Mage) 영역                	│
						│ ----------------------------- │
						│ attack()  ← 재정의(override)  	│
						│ castSpell()                   │
						└───────────────────────────────┘
			
			         */	
		

	}

}






