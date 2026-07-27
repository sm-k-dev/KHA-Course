


//[4] Tank 클래스  - Unit부모클래스를 상속받아 자식클래스로 만들기 
public class Tank extends Unit{

	public Tank(String name, int hp, int damage) {
		//부모 Unit클래스에 만들어 놓은 생성자를 통해서 
		//- "시저탱크" 이름을 부모 Unit객체 메모리 영역안에 포함된 name객체변수에 저장 
		//- 기본 체력 150을  부모 Unit객체 메모리 영역안에 포함된 hp객체변수에 저장
		//- 기본 공격력 35를  부모 Unit객체 메모리 영역안에 포함된 damage객체변수에 저장 		
		super(name, hp, damage);
	}

}
