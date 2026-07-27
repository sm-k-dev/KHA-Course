


//자바프로그램의 시작점인 main메소드 기능을 포함하고 있는 설계도(클래스)
public class UnitTest {

	public static void main(String[] args) {
		
		Marin marin = new Marin("마린");//이름만 전달 하면  이름:마린, 기본체력 100,  기본공격력 10 이 됨 
		
		Zergling zergling = new Zergling("저글링", 80); //이름과 기본체력만 전달하면 이름:저글링, 기본체력 80, 기본공격력10 이됨   
	
		Tank  tank = new Tank("시저탱크", 150, 35);//이름,          기본체력, 			기본공격력 모두 전달하면
												 //이름 : 시저탱크, 기본체력 : 150,      기본공격력 : 35 가 저장됨 
				
		//초기 인스턴스변수 값 확인 
		marin.status();  //[유닛 상태] 마린 - 체력 : 100, 공격력 : 10
		zergling.status();//[유닛 상태] 저글링 - 체력 : 80, 공격력 : 10
		tank.status();//[유닛 상태] 시저탱크 - 체력 : 150, 공격력 : 35
		
		System.out.println("\n===== 전 투 시 작 =====");
		
		//마린이 저글링 공격
		marin.attackZergling(zergling);
//		marin.attackZergling(new Zergling("저글링", 80));
	
		//저글링이 마린 공격
		zergling.attackMarine(marin);
		
		//시저탱크가 저글링 공격1
		tank.attackZergling(zergling);
		
		//시저탱크가 저글링 공격2
		tank.attackZergling(zergling);
		
		
		System.out.println("\n==== 전 투 종 료 ======");
		
		zergling.status(); //[유닛 상태] 저글링 - 체력 : 70->35->0, 공격력 : 10
		marin.status();    //[유닛 상태] 마린 - 체력 : 90, 공격력 : 10

	}

}
