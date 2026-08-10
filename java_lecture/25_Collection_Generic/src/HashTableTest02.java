


import java.util.Hashtable;

public class HashTableTest02 {

	public static void main(String[] args) {
		
		Hashtable<String, String>  hashtable = new Hashtable<String, String>();
		
		hashtable.put("사과", "Apple");
		hashtable.put("딸기", "Strawberry");
		hashtable.put("포도", "Grapes");
		/*	
		┌─────────────────────────┐
		│  HashTable (표 구조)      │
		│                         │
		│  key        | value     │
		│ ────────────┼───────────│
		│  "사과",     |  "Apple"  │           행(row) 3 개 
		│  "딸기",     | "Strawberry" 
		│   "포도",    |  "Grapes" │                   
		└─────────────────────────┘	
	*/		
	    //============== HashTable 클래스의 get 메소드 ========================================	
	//	E      get(Object key) 메소드를 이용하자!
	//	String get(Object key) 메소드 형태로 변경 된다.
		
		String value = hashtable.get("포도"); //업캐스팅 안해도 value -> "Grapes" 문자열 객체를 value로 얻어 저장 가능
		//    "Grapes"						//그리고 다운 캐스팅도 하지 않고 String클래스의 모든 메소드 사용 가능!
		
		if(value != null) {
			System.out.println("포도 key와 함께 연결되어 저장됬던 vlaue -> " +  value.toString());
			//					포도 key와 함께 연결되어 저장됬던 vlaue -> Grapes
		}
		
	}

}






