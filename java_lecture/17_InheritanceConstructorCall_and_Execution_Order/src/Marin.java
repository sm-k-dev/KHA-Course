

//[2] Marin 클래스  - Unit부모클래스를 상속받아 자식클래스로 만들기 
public class Marin extends Unit{
	
	public Marin(String name) {		
		//부모 Unit클래스에 만들어 놓은 생성자를 통해서 
		// - "마린" 이름을 부모Unit객체 메모리 영역안의 name객체변수 초기화 
		// -  기본 체력 100  초기화 
		// -  기본 공격력 10  초기화 		
		super(name);
	}	
	
}


//new Marin("마린");