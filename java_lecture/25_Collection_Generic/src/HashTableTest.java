import java.util.Hashtable;
import java.util.Map;
import java.util.Enumeration;

// 주제: Map 부모인터페이스를 구현 받은 자식 HashTable 클래스의 배열 메모리 만들어 사용해 보기
public class HashTableTest {

	public static void main(String[] args) {
		
		Hashtable<String, String>	hashtable	= new	Hashtable<String, String>();
		
		// HashTable 표 형태의 배열 메모리에 데이터 (key-value) 를 저장할 때 put 메소드 사용
		// key 또한 객체로 넣고, value 또한 객체로 넣는다.
		hashtable.put("사과", "Apple");
		hashtable.put("딸기", "Strawberry");
		hashtable.put("포도", "Grape");
		
		// ===================== HashTable 클래스의 get 메소드 =============================================
		// Object get (Object key) 메소드를 이용하자
		//	- get 메소드는 key를 매개변수로 집어넣으면
		//		HashTable 표 메모리 전체에 저장된 ( key-value ) 중 value를 Object obj에 저장할 자식 객체로 얻는 메소드
		
		// HashTable 전체 표 구조의 배열 메모리 안에 저장되어 있는 value 중에서 "Grape" 객체를 얻고 싶다.
		String	value	= hashtable.get("포도");	// 업캐스팅 안해도 value -> "Grape" 문자열 객체를 value로 얻어 저장 가능
												// 그리고 다운 캐스팅도 하지 않고 String 클래스에 만들어 놓은 메소드 호출 가능
		
		// key - "vheh"를 이요해서 value - "Grape"를 HashTable표 메모리에서 꺼내 올 수 있는지 확인
		if ( value != null ) {
			System.out.println( value.toString() );
		}
		
		/*
			HashTable 배열에 저장된 모든 Key(객체)들을 일일이 기억하지 못하므로
			모든 key(객체)들만 뽑아내서 Enumeration배열에 담아 Enumeration배열 주소 자체를 반환
			-> keys() 메소드
		*/
		Enumeration	enumeration	= hashtable.keys();
		
		// keys() 메소드를 호출해서 반환받은 [ "딸기", "사과", "포도" ] Enumeration 배열에 저장된 key들이 있으면 반복
		while ( enumeration.hasMoreElements() ) {
			
			/*
				// 1. Enumeration 배열에 저장되어 있는 key들을 차례대로 얻어 저장
				Object obj = enumeration.nextElement(); // 업캐스팅
				// 다운캐스팅
				String key = (String)obj;
			*/
			String key = (String)enumeration.nextElement(); // 위의 두 줄을 한번에 처리해 한줄로 표현
			
			// 2. key를 이요해 HashTable 배열 전체에 저장된 value를 차례대로 얻어 저장
			String val = (String)hashtable.get(key); // (String) 다운캐스팅은 생략 가능, HashTable 생성시 제네릭 <String>으로 설정했기 때문
			
			// 3. HashTable에 저장되어 있는 key - value 형태의 문자열을 반복해서 출력
			System.out.println( key + " - " + val );
		}
	}

}
