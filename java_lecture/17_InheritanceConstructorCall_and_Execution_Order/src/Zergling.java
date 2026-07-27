

//[3] Zergling 클래스  - Unit부모클래스를 상속받아 자식클래스로 만들기 
public class Zergling  extends Unit{

	
	public Zergling(String name, int hp) {
		//부모 Unit클래스에 만들어 놓은 생성자를 통해서
		// - "저글링" 이름을 부모Unit객체 메모리 영역안의 name객체변수에 초기화 
		// -  기본 체력 80을 부모Unit객체 메모리 영역안의 hp 객체변수에  초기화
		// -  기본 공격력 10을 부모Unit객체 메모리 영역안의 damage객체변수에 초기화
		super(name, hp);
	}
	
	
}



  ////////////  new Zergling("저글링", 80);