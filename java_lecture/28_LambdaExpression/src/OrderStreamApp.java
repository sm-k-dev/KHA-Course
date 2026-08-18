
//주제  람다식 + Stream API 응용 - 주문 데이터 조회 (SQL 과 나란히 비교)

import java.util.stream.Collectors;   //클래스. 스트림 결과를 수집할 때 사용
import java.util.ArrayList;           //클래스. 가변 크기 배열
import java.util.Arrays;              //클래스. asList 로 고정 배열을 만들 때 사용
import java.util.List;                //인터페이스. 목록의 공통 규격
import java.util.OptionalDouble;

/*
 ==================================================================
 [이 예제의 목표]
   이미 배운 MySQL 의 SELECT 문과 자바 Stream 을 나란히 놓고 비교한다.
   Stream 은 "자바 코드로 쓰는 SQL" 이라고 보면 된다.

     MySQL                     |  자바 Stream
     --------------------------+--------------------------------
     FROM 테이블                 |  목록.stream()
     WHERE 조건                 |  .filter( o -> 조건 )
     SELECT 컬럼 하나             |  .mapToInt( o -> o.컬럼 )
     ORDER BY 컬럼 DESC         |  .sorted( (a,b) -> b.컬럼 - a.컬럼 )
     DISTINCT                  |  .distinct()
     SUM(컬럼)                  |  .sum()
     AVG(컬럼)                  |  .average()
     COUNT(*)                  |  .size()
     결과를 목록으로 받기            |  .collect( Collectors.toList() )

 [람다식 읽는 법]
     o  ->  o.price
     -      -------
     (1)      (2)

     (1) o : 통로에서 꺼내진 객체 1개가 담기는 자리.
             내가 넣는 것이 아니라 filter/mapToInt 메소드가 하나씩 넣어 준다.
     (2) 하고 싶은 일. 화살표 오른쪽이 한 문장이면 return 과 중괄호를 생략한다.

     sorted 만 값을 2개 받는다 --> (a, b) 처럼 괄호로 묶는다.

 [데이터 = MySQL 테이블 orders 라고 생각하면 된다]

     order_id | member_id | product | price   | status
     ---------+-----------+---------+---------+----------
     1        | kim       | 노트북  | 1500000 | 배송완료
     2        | lee       | 마우스  |  100000 | 배송중
     3        | kim       | 키보드  |   80000 | 배송중
     4        | park      | 모니터  |  350000 | 배송완료
     5        | kim       | 이어폰  |  120000 | 주문접수
     6        | lee       | 노트북  | 1500000 | 배송완료
     7        | park      | 마우스  |  130000 | 배송중

     테이블 1행 = 자바 Order 객체 1개 / 테이블 전체 = 자바 List 하나
 ==================================================================
*/

//==================================================================
// 테이블 1행을 담는 클래스 (컬럼 5개 = 멤버 변수 5개)
//==================================================================

class Order { // DTO 역할을 하는 클래스
	
	int		orderId;	// order_id 컬럼(열)
	String	memberId;	// member_id 컬럼(열)
	String	product;	// product 컬럼(열)
	int		price;		// price 컬럼(열)
	String	status; 	// status 컬럼(열)
	
	//생성자 : Order 객체를 만드는 순간  Order 테이블의 한 행의 값 5개를 전달받아 초기화(저장)
	public Order(int orderId, String memberId, String product, int price, String status) {
		super();
		this.orderId = orderId;
		this.memberId = memberId;
		this.product = product;
		this.price = price;
		this.status = status;
	}
	
}

public class OrderStreamApp {

    public static void main(String[] args) {
    	
    	// 1. 데이터 준비 (SELECT * FROM orders 의 조회 결과를 자바 목록으로 옮겨 놓은 것)
    	List<Order>	orders	= Arrays.asList(
								    			new Order(1,"kim",		"노트북", 1500000,	"배송완료"),
								    		    new Order(2, "lee",		"마우스", 100000,		"배송중"),
								    		    new Order(3, "kim",		"키보드", 80000,		"배송중"),
								    		    new Order(4, "park",	"모니터", 350000,		"배송완료"),
								    		    new Order(5, "kim",		"이어폰", 120000,		"주문접수"),
								    		    new Order(6, "lee",		"노트북", 1500000,	"배송완료"),
								    		    new Order(7, "park",	"마우스", 130000,		"배송중")
    		    							);
        /*
			orders 고정 크기 배열
			[1행, 2행, 3행,   4행,  5행, 6행, 7행] <- Order 객체들이 각 칸에 저장된 고정 칸의 배열
			 0    1    2    3    4    5    6     index
		*/
    	
        //================================================================================
        // [조회 1]  SELECT * FROM orders WHERE member_id = 'kim';
        //
        //   중간 연산 filter(Predicate<T> predicate)
        //     - 스트림의 각 요소에 조건식을 평가하며 Predicate<T> 함수형 인터페이스를 사용한다.
        //     - Predicate<T> 에는 boolean 을 반환하는 test(T t) 추상메소드가 작성되어 있어서
        //       조건식을 만족한 요소들만 다음 연산으로 넘긴다.
        //================================================================================
    	
    	// 순서1. 준비된 orders 고정 배열의 Order 객체를 이용해 Stream 입력 스트림 통로를 만들어 얻자
    	//		요약: Stream<Order> 통로 준비
    	List<Order>	kimOrders = orders.stream()
    			/*
	                Stream<Order> 객체 입력스트림 통로  ------> 읽어들이는 방향
	                ----------------------------------------------------------
	orders 고정배열
	[1행 ~ 7행]                     -> 7park -> 6lee -> 5kim -> 4park -> 3kim -> 2lee -> 1kim
	                ----------------------------------------------------------
    			*/
    			
    	// 순서2. Stream 통로 객체의 filter 메소드를 호출하면
    	//		통로의 Order 객체가 하나씩 o 매개변수 자리에 담기고, o.memberId가 "kim"인지 조건식으로 확인한다.
    	//		조건식을 만족하지 않는 Order 객체들을 제외한 Order 객체들이 저장된 Stream 통로 객체를 반환
    	.filter ( o -> o.memberId.equals("kim") )

    		/*
				Stream<Order> 통로 객체 ->
				-------------------------------------
					->  5kim(120000) -> 3kim(80000) -> 1kim(1500000)
				-------------------------------------
				
					빠진 것 : 2lee, 4park, 6lee, 7park  (조건식이 false 라서 통로에서 제외)
    		 */    
    	
    	// 순서3. 최종 결과를 얻어 ArrayList 배열에 담아 얻기
    	.collect ( Collectors.toList() ); // [1행, 3행, 5행]
    	
    	// 순서4. 구매한 아이디(member_Id)가 "kim" 구매한 Order 객체들 정보만 출력
    	System.out.println("======= [조회1] kim의 주문 정보 =======");
    	
    	for ( Order o : kimOrders ) { //ArrayList 배열의 칸을 0index 부터 하나씩 꺼내 o변수에 담아 반복
    		System.out.println(  o.orderId + "번 | " + o.product + " | " + o.price + "원");
    	}
    	System.out.println();
    	
        //================================================================================
        // [조회 2]  SELECT * FROM orders WHERE status = '배송중';
        //
        //   조회 1과 구조가 완전히 같고 "람다 안에서 보는 컬럼" 만 다르다.
        //   SQL 에서 WHERE 뒤만 바꾸면 다른 조회가 되는 것과 똑같다.
        //================================================================================
    	
    	// 순서1. Stream<Order> 통로 준비
    	List<Order> shipping = orders.stream()
    		
    	// 순서2. status 컬럼이 "배송중"인 Order 객체만 Stream<Order> 통로에 남긴 새로운 Stream<Order>통로 객체 반환
    		.filter( o -> o.status.equals("배송중") )
    		
    	// 순서3. 최종 결과를 ArrayList 배열로 얻기
    		.collect( Collectors.toList() );
    		
    	// 순서4. 구매한 아이디(member_Id)가 "kim" 구매한 Order 객체들 정보만 출력
    	System.out.println("======= [조회2] 배송중 주문 정보 =======");
    	
    	for ( Order o : shipping ) { //ArrayList 배열의 칸을 0index 부터 하나씩 꺼내 o변수에 담아 반복
    		System.out.println(  o.orderId + "번 | " + o.memberId + " | " + o.product );
    	}
    	
    	System.out.println("건수: " + shipping.size() + "건"); // 3건
    	System.out.println();
    		
        //================================================================================
        // [조회 3]  SELECT SUM(price) FROM orders WHERE member_id = 'kim';
        //
        //   중간 연산 mapToInt(ToIntFunction<T> mapper)
        //     - 스트림의 각 객체를 int 값 하나로 변환할 때 사용한다.
        //     - 객체가 흐르던 Stream 통로가 int 만 흐르는 IntStream 통로로 바뀐다.
        //     - sum, average 는 숫자에만 쓸 수 있으므로 반드시 이 단계를 거쳐야 한다.
        //================================================================================

    	// 순서1. Stream<Order> 통로 준비
    	int kimTotal = orders.stream()
    	
    	// 순서2. memeber_id 가 "kim"인 Order 객체만 통로에 남긴다 (조회 1 과 같은 조건식)
    	.filter( o -> o.memberId.equals("kim") )
    	
    	// 순서3. 통로의 각 Order 객체에서 price 값만 꺼내어
    	//		int 숫자가 흐르는 IntStream 통로 객체로 바꿔서 반환
    	//		(이 줄을 지나면 통로에는 Order 객체가 아니라 숫자만 남는다)
    	.mapToInt( o -> o.price )
    	
    	// 순서4. IntStream 통로의 숫자를 전부 더한 결과를 int로 반환 (종료 연산)
    	.sum();
    	
    	System.out.println("======= [조회3] kim 총 구매액 =======");
    	System.out.println(kimTotal + "원");
    	
        //================================================================================
        // [조회 4]  SELECT AVG(price) FROM orders;
        //
        //   종료 연산 average()
        //     - IntStream 통로 숫자들의 평균을 계산한다.
        //     - 결과는 곧바로 숫자가 아니라 OptionalDouble 이라는 "상자" 로 반환된다.
        //       행이 0건이면 평균을 낼 수 없기 때문이다.
        //       (MySQL 에서도 빈 테이블의 AVG 는 NULL 이 나오는 것과 같은 이치)
        //     - orElse(기본값) 으로 그 상자를 열어 값을 꺼낸다. SQL 의 IFNULL 역할.
        //================================================================================

    	// 순서1. Stream<Order> 통로 준비 (WHERE 가 없는 SQL 이므로 filter 메소드를 쓰지 않는다)
    	double avg = orders.stream() // 여기의 Data type은 List<Order>
    	
    	// 순서2. 각 Order 객체에서 price 만 꺼내 IntStream 통로에 넣어 객체 반환
    	.mapToInt( o -> o.price ) // .mapToInt( (o) -> { return o.price; } ); // 여기의 Data type은 IntStream
    	
    	// 순서3. 통로 숫자들의 평균을 계산해서 OptionalDouble 상자에 담아서 반환 
    	.average() // 여기서의 Data type 은 OptionalDouble
    	
    	// 순서4. OptionalDouble 상자를 열어서 평균값을 꺼낸다. 만약 비어 있으면 0.0을 대신 받아 사용
    	.orElse(0.0D);
    	
    	System.out.println("=======[조회4] =======");
    	System.out.println(avg + "원");
    	System.out.println();
    	
        //================================================================================
        // [조회 5]  SELECT * FROM orders ORDER BY price DESC;
        //
        //   중간 연산 sorted(Comparator<T> comparator)
        //     - Comparator<T> 함수형 인터페이스를 사용하며,
        //       값 2개를 받아 int 를 반환하는 compare(T a, T b) 추상메소드가 작성되어 있다.
        //     - 반환값이 음수면 a 를 앞에, 양수면 b 를 앞에 놓는다. 0 이면 순서 유지.
        //
        //     정렬 공식 :  a - b  --> 작은 값이 앞 (ORDER BY ASC)
        //                  b - a  --> 큰 값이 앞   (ORDER BY DESC)
        //     외우는 법 : "앞에 오길 원하는 쪽을 뒤에 빼면 된다"
        //================================================================================
    	
    	// 순서1. Stream<Order> 통로 준비
    	orders.stream();
    	
    	// 순서2. 통로의 Order 객체를 두 개씩 (a, b) 자리에 담아 비교하며 순서를 정한 통로 객체를 반환
    	
        //================================================================================
        // [조회 6]  SELECT DISTINCT product FROM orders;
        //
        //   중간 연산 distinct()
        //     - 람다를 받지 않는다. "이미 지나간 값 명단" 을 스스로 기억하며
        //       equals 로 비교해서 처음 보는 값만 통과시킨다.
        //================================================================================

    }   //main 의 끝

}   //OrderStreamApp 클래스의 끝

/*
 ==================================================================
 전체 예상 출력
 ------------------------------------------------------------------
 ===== [조회1] kim 의 주문 =====
 1번 | 노트북 | 1500000원
 3번 | 키보드 | 80000원
 5번 | 이어폰 | 120000원

 ===== [조회2] 배송중 주문 =====
 2번 | lee | 마우스
 3번 | kim | 키보드
 7번 | park | 마우스
 건수 : 3건

 ===== [조회3] kim 총 구매액 =====
 1700000원

 ===== [조회4] 전체 평균 금액 =====
 540000.0원

 ===== [조회5] 금액 큰 순서 =====
 1500000원 | 노트북 (kim)
 1500000원 | 노트북 (lee)
 350000원 | 모니터 (park)
 130000원 | 마우스 (park)
 120000원 | 이어폰 (kim)
 100000원 | 마우스 (lee)
 80000원 | 키보드 (kim)

 ===== [조회6] 판매 상품 종류 =====
 - 노트북
 - 마우스
 - 키보드
 - 모니터
 - 이어폰
 ==================================================================
 최종 정리

 1. 모든 조회가 같은 3단계다.
      통로 만들기(stream) -> 중간 연산(filter/mapToInt/sorted/distinct)
      -> 종료 연산(collect/sum/average)

 2. 중간 연산은 통로 객체를 반환하므로 . 으로 계속 이어 붙일 수 있다(체이닝).
    종료 연산이 호출되어야 비로소 통로에 데이터가 흐른다.

 3. 원본 배열(orders)은 절대 변하지 않는다. 항상 새 ArrayList 가 반환된다.

 4. mapToInt 를 지나면 통로의 내용물이 객체에서 숫자로 바뀐다.
    sum, average 는 그 이후에만 쓸 수 있다.

 5. 자주 하는 실수
      - 문자열을 == 로 비교 --> 결과 0건. equals 를 써야 한다
      - mapToInt 없이 sum --> 컴파일 오류
      - collect 를 빠뜨림 --> 통로가 흐르지 않아 아무 일도 안 일어난다
 ==================================================================
*/
