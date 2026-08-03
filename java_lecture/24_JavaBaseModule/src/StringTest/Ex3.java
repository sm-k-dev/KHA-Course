package StringTest;
/* ============================================================================
 *  주제 : lastIndexOf() 와 substring() 을 사용해 주소에서 상세주소만 잘라내기
 * ----------------------------------------------------------------------------
 *  [1] indexOf() 와 lastIndexOf()
 *
 *      int indexOf(찾을 문자 또는 문자열)
 *          문자열의 앞에서부터 찾는다. 처음 발견된 위치의 인덱스를 반환한다.
 *
 *      int lastIndexOf(찾을 문자 또는 문자열)
 *          문자열의 뒤에서부터 찾는다. 마지막으로 발견된 위치의 인덱스를 반환한다.
 *
 *      두 메소드 모두 찾지 못하면 -1 을 반환한다.
 *      -1 은 인덱스로 존재할 수 없는 값이므로, "없음" 을 나타내는 표시로 쓰인다.
 *
 *      찾은 대상이 문자열이면, 그 문자열의 첫 글자 위치를 반환한다.
 *
 *      예)  String text = "apple, banana, cherry, banana, date";
 *           //            0123456789...
 *           
 *           text.indexOf("banana")      ->  7
 *           text.lastIndexOf("banana")  ->  23
 *           text.lastIndexOf("melon")   ->  -1
 *
 *  [2] 이 예제에서 하려는 일
 *
 *      회원의 전체 주소가 "광주시 북구 북문로 112/505호" 형태로 저장되어 있다.
 *      슬래시(/) 뒤쪽이 상세주소다. 이 부분만 잘라내려면 두 단계가 필요하다.
 *
 *          1단계 : lastIndexOf('/') 로 슬래시의 위치를 찾는다.
 *          2단계 : substring(위치 + 1) 로 슬래시 다음부터 끝까지 잘라낸다.
 *
 *      슬래시가 없는 주소도 있을 수 있으므로, 찾지 못했을 때(-1)의 처리도 함께 작성한다.
 * ========================================================================== */

/*
  데이터베이스의 member 테이블에서 조회한 회원 한 사람의 정보를 담아 둘 DTO 클래스다.
  이렇게 데이터를 담아서 전달하는 용도로만 쓰는 클래스를 DTO 라고 부른다.
*/
class MemberDTO {
	
	//private 으로 인스턴스 변수 선언
	private String name;   		//회원 이름 
	private String address;		//회원 전체주소.  예) "광주시 북구 북문로 112/505호"
	
	
	//생성자. MemberDTO 객체를 만들때 이름과 주소를 전달받아 인스턴스변수를 초기화한다.
	public MemberDTO(String name, String address) {
		this.name = name;
		this.address = address;
	}
	
	//getter 메소드. private 인스턴스변수의 값을 외부 클래스에서 읽을 수 있게 해준다
	public String getName() {
		return this.name;
	}
	public String getAddress() {
		return this.address;
	}
	
	//회원 전체 주소( "광주시 북구 북문로 112/505호")에서 상세주소("505호") 부분만 잘라내어 반환하는 메소드 
	public String getDetailAddress() {
		
        /* address 에 저장된 전체 주소에서 슬래시의 위치를 뒤에서부터 찾는다.
        *
        *      광 주 시 _ 북 구 _ 북 문 로 _  1  1  2  /  5  0  5  호
        *      0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18
        *
        * 위 주소에서는 14 가 반환된다.
        * 슬래시가 없으면 -1 이 반환된다.
        *
        * 뒤에서부터 찾는 lastIndexOf 를 쓰는 이유는, 주소 안에 슬래시가
        * 여러 개 있어도 마지막 슬래시를 기준으로 잘라야 하기 때문이다. */
		int lastIndex = this.address.lastIndexOf('/');
		
        /* 두 조건을 모두 만족할 때만 잘라낸다. && 는 양쪽이 모두 true 여야 true 다.
        *
        *   lastIndex != -1
        *       슬래시를 찾았다는 뜻이다.
        *
        *   lastIndex + 1 < address.length()
        *       슬래시 다음에 글자가 하나라도 더 있다는 뜻이다.
        *       주소가 "광주시.../" 처럼 슬래시로 끝나면 이 조건이 false 가 되어
        *       빈 문자열을 반환하는 상황을 막아 준다. */
		if(lastIndex != -1  && lastIndex + 1 < this.address.length()) {
			
			//슬래시 다음 위치부터 끝 문자 까지 잘라서 반환한다. 결과는 "505호" 가 된다.
			//상세주소 "505호" 반환 
			return address.substring(lastIndex + 1);
		}
		//슬래시가 없거나(상세주소가 없거나)   슬래시 뒤에 글자가 없으면(상세주소가 없으면?) 전체주소를 그대로 반환하자
		return this.address;	
	}
	
	
} //<=======================================  class  MemberDTO    


public class Ex3 {
	public static void main(String[] args) {
        /* 상황 설정
         * 데이터베이스에서 아래 SQL 을 실행해 회원 5명을 조회했다고 가정한다.
         *
         *      select * from member;
         *
         *      name     | address
         *      ---------+-----------------------------------
         *      홍길동    | 서울시 강남구 테헤란로 123/101호
         *      김청수    | 부산시 해운대구 해변로 456/202호
         *      이영희    | 대구시 수성구 수성로 789/303호
         *      박지민    | 인천시 남동구 남동대로 101/404호
         *      최준호    | 광주시 북구 북문로 112/505호
         *
         * 조회된 행 하나가 MemberDTO 객체 하나에 대응된다. */
		
		//final 을 붙이면 값을 바꿀수 없는 상수 메모리가 된다
		//상수의 이름은 모두 대문자로 쓰고 단어 사이는 밑줄로 구분하는 것이 관례이다.
		final int MEMBER_COUNT = 5;
		
        /* MemberDTO 객체 5개의 주소값을 저장할 배열을 만든다.
         * 이 시점에는 객체가 만들어진 것이 아니라 저장할 칸 5개만 준비된 상태다.
         * 각 칸의 값은 아직 아무것도 가리키지 않는 상태인 null 이다.
         *
         *      memberArray  [null][null][null][null][null]
         *                     0     1     2     3     4     <- 인덱스        */
		MemberDTO[] memberArray = new MemberDTO[MEMBER_COUNT];
		
		// 배열 각 칸에 MemberDTO 객체를 만들어 차례대로 넣는다.
		memberArray[0] = new MemberDTO("홍길동", "서울시 강남구 테헤란로 123/101호");
		memberArray[1] = new MemberDTO("김청수", "부산시 해운대구 해변로 456/202호");
		memberArray[2] = new MemberDTO("이영희", "대구시 수성구 수성로 789/303호");
        memberArray[3] = new MemberDTO("박지민", "인천시 남동구 남동대로 101/404호");
        memberArray[4] = new MemberDTO("최준호", "광주시 북구 북문로 112/505호");
        
        /* 향상된 for 문(for each 문)
        *
        *      for (타입 변수명 : 배열이름) { ... }
        *
        * 배열의 첫 칸부터 마지막 칸까지 값을 하나씩 꺼내 변수에 넣고 반복한다.
        * 인덱스를 직접 다루지 않으므로 범위를 벗어나는 실수가 생기지 않는다. */
        for(MemberDTO memberdto : memberArray) {
        	System.out.println("이름 : " + memberdto.getName() + ", 전체주소 : " + memberdto.getAddress());
        }
        
        System.out.println("------------------------------------------------");

        //슬래시가 없는(상세 주소가 없는) 주소의 회원일떄의 동작도 확인한다. => 상세 주소 없기떄문에 전체주소가 그대로 반환된다.
        MemberDTO noDetail = new MemberDTO("정하나", "제주시 연동 999");
        System.out.println("이름 : " + noDetail.getName() + ", 전체주소 : " +  noDetail.getDetailAddress());
		
        //indexOf 메소드와  lastIndexOf 메소드의 차이를 직접 확인 한다.
        String text = "apple, banana, cherry, banana, date";
        
        System.out.println("indexOf(\"banana\") = " + text.indexOf("banana") ); // 7
        System.out.println("lastIndexOf(\"banana\") = " + text.lastIndexOf("banana") ); //23
        System.out.println("lastIndexOf(\"melon\") = " +  text.lastIndexOf("melon")  ); //-1
        
        
		
	}

}






