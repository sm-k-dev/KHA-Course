import java.util.Hashtable;
import java.util.Enumeration;

//===================================================================
//[응용 문제] Hashtable 배열 메모리에 나라-수도 데이터 저장하고 꺼내오기
//===================================================================
//수업 예제 HashTableTest.java (과일-영단어) 와 똑같은 순서로
//나라 이름을 key, 수도 이름을 value 로 저장하고 출력하는 문제입니다.
//
//[요구사항]
// 1단계 : key 도 String, value 도 String 을 저장하는 Hashtable 객체를 생성하세요.
//
// 2단계 : put 메소드를 사용해서 아래 3쌍의 (key-value) 데이터를 저장하세요.
//           key          value
//          "한국"    ,   "서울"
//          "일본"    ,   "도쿄"
//          "프랑스"  ,   "파리"
//
// 3단계 : get 메소드를 사용해서 key "한국" 과 연결되어 저장된
//         value "서울" 문자열 객체를 꺼내와서 String 변수 capital 에 저장하세요.
//
// 4단계 : capital 변수에 저장된 값이 null 이 아니면
//         아래 문장을 출력하세요.
//         한국 key와 함께 연결되어 저장된 value-> 서울
//
// 5단계 : get 메소드를 사용해서 Hashtable 에 저장한 적이 없는
//         key "미국" 으로 value 를 꺼내려고 시도해 보고,
//         반환된 값이 null 이면 아래 문장을 출력하세요.
//         미국 key는 HashTable에 저장되어 있지 않습니다.
//
// 6단계 : keys 메소드로 Enumeration 배열을 반환 받은 후
//         while 반복문 + hasMoreElements + nextElement 메소드를 사용해서
//         저장된 모든 (key - value) 를 아래 형태로 출력하세요.
//         일본 - 도쿄
//         한국 - 서울
//         프랑스 - 파리
//         (출력 순서는 저장한 순서와 다를 수 있습니다.)
//===================================================================

public class HashTableEx {
	public static void main(String[] args) {

		//1단계 : Hashtable 객체 생성
		Hashtable<String, String> hashtable = new Hashtable<String, String>();

		//2단계 : put 메소드로 (key-value) 3쌍 저장
		hashtable.put("한국", "서울");
		hashtable.put("일본", "도쿄");
		hashtable.put("프랑스", "파리");

		//3단계 : get 메소드로 key "한국" 의 value 꺼내와 저장
		String	value	= hashtable.get("한국");
		
		//4단계 : null 이 아니면 출력
		if ( value != null ) {
			System.out.println(value);
		}
		
		System.out.println();
		
		//5단계 : 저장한 적 없는 key "미국" 으로 꺼내기 시도 후 null 확인
		if ( hashtable.get("미국") != null ) {
			System.out.println( hashtable.get("미국") );
		}

		//6단계 : keys 메소드 + Enumeration 배열로 전체 (key - value) 출력
		Enumeration	enumeration = hashtable.keys();
		
		while ( enumeration.hasMoreElements() ) {
			String key = (String)enumeration.nextElement();
			String val = (String)hashtable.get(key);
			
			System.out.println( key + "\t - \t" + val );
		}

	}
}
