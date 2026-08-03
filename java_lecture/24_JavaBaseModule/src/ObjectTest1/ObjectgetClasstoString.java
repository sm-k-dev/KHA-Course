package ObjectTest1;
/* ============================================================================
 *  주제 : Object 클래스의 getClass() 메소드와 toString() 메소드
 * ----------------------------------------------------------------------------
 *  [1] getClass() 메소드
 *
 *          @IntrinsicCandidate
 *          public final native Class<?> getClass();
 *
 *      - 하는 일 : 이 객체가 어떤 클래스로 만들어졌는지에 대한 정보를 담은
 *                 Class 타입의 객체를 반환한다.
 *      - final   : final 이 붙은 메소드는 자식 클래스에서 재정의할 수 없다.
 *                 객체가 자신의 실제 클래스를 속이지 못하게 하기 위해서다.
 *      - native  : 메소드의 실행 내용이 자바 코드가 아니라 C, C++ 같은 다른
 *                 언어로 작성되어 있다는 표시다.
 *
 *      Class 객체에서 자주 사용하는 메소드
 *          getName()        : 패키지 이름을 포함한 클래스 이름.  예) exam03.Point
 *          getSimpleName()  : 패키지 이름을 뺀 클래스 이름만.    예) Point
 *
 *      참고. System.out.println(객체.getClass()) 를 실행하면 앞에 "class " 가
 *           붙어서 출력된다.  예) class exam03.Point
 *           이것은 Class 객체의 toString() 이 그렇게 만들어져 있기 때문이다.
 *
 *  [2] toString() 메소드
 *
 *      Object 클래스에 작성되어 있는 원래 내용은 다음과 같다.
 *
 *          public String toString() {
 *              return getClass().getName() + "@" + Integer.toHexString(hashCode());
 *          }                 
 *
 *      - getClass().getName()             : 패키지를 포함한 클래스 이름
 *      - "@"                              : 두 정보를 구분하는 문자
 *      - Integer.toHexString(hashCode())  : 해시코드 정수값을 16진수 문자열로 바꾼 것
 *
 *      실행 예)  exam03.Point2@2ff4acd0
 *
 *      뒤쪽 16진수 값은 프로그램을 실행할 때마다 달라진다. 고정된 값이 아니다.
 *
 *  [3] toString() 이 자동으로 호출되는 상황
 *
 *      아래 세 가지 경우, toString() 을 직접 쓰지 않아도 자바가 대신 호출한다.
 *
 *          System.out.println(객체);
 *          System.out.println("문자열" + 객체);
 *          String s = String.valueOf(객체);
 *
 *      그래서 toString() 을 재정의해 두면, 객체를 그대로 출력했을 때
 *      원하는 형태의 문자열이 나오게 만들 수 있다.
 *
 *  [4] 이미 재정의되어 있는 클래스들
 *
 *      String, Integer, Date, ArrayList 등 자바가 제공하는 많은 클래스는
 *      toString() 이 이미 재정의되어 있다. 그래서 그 객체를 출력하면
 *      주소 형태가 아니라 내용이 출력된다.
 * ========================================================================== */

/* 비교용 클래스.
 * toString() 을 재정의하지 않았으므로 Object 의 원래 toString() 이 그대로 동작한다.
 * 출력 형태 : 패키지포함클래스이름@16진수해시코드 */
class Point2 {
	
	int x, y;
	
	public Point2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/*
	Object로 부터 상속 받은 toString
	 
	 public String toString() {              
		 return getClass().getName() + "@" + Integer.toHexString(hashCode());
		 		ObjectTest1.Point2@1f32e575
	 } 
	*/
}

/* 본 예제 클래스.
 * extends Object 는 생략해도 되지만, 상속 관계를 보이기 위해 그대로 둔다. */
class Point extends Object {
	
	//인스턴스 변수.  한점의 가로 좌표와 세로좌표를 저장한다.
	int x, y;
	
	//기본 생성자.  값을 넣지 않았으므로 x, y 는 int 의 기본값인 0 이 된다.
	public Point() { }
	
	//매개변수 생성자. 전달받은 값으로 x,y를 초기화한다.
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
    /* ------------------------------------------------------------------
     * toString() 재정의
     * ------------------------------------------------------------------
     * 재정의하지 않으면 상속받은 아래 내용이 실행된다.
     *
     *     public String toString() {
     *         return getClass().getName() + "@" + Integer.toHexString(hashCode());
     *     }
     *
     * 이 결과는 좌표값을 확인하는 데 아무 도움이 되지 않는다.
     * 그래서 Point 객체의 목적에 맞게 "(x,y)" 형태의 문자열을 반환하도록 다시 작성한다.
     *
     * @Override : 부모의 메소드를 다시 작성한다는 표시다.
     *             메소드 이름이나 반환형을 잘못 쓰면 컴파일 오류로 알려 준다.
     * ------------------------------------------------------------------ */
	 @Override
	public String toString() {
		 
		 // x 가 10, y 가 20 이면 결과는 "(10,20)" 이 된다.
		return "(" + this.x + "," + this.y + ")";
	}
	
}

public class ObjectgetClasstoString {

	public static void main(String[] args) {
		//================================================================
		//1. getClass() 로 클래스 정보 확인하기 
		//===============================================================
		Point  p1 = new Point(10, 20);
		
				
        // getClass() 는 Class 객체를 반환한다. 출력하면 앞에 "class " 가 붙는다.
		System.out.println("p1.getClass()  = " + p1.getClass() ); 
//					                             class ObjectTest1.Point
//												 class      패키지명.클래스명
		System.out.println("p1.getClass().getName() = " + p1.getClass().getName());
//													             ObjectTest1.Point
//																 패키지명.클래스명
		//클래스 이름만 얻자
		System.out.println("p1.getClass().getSimpleName() = " + p1.getClass().getSimpleName());
//																				Point
//																				클래스명
		System.out.println("-------------------------------------------------");
		
		//==========================================================================
		//2. toString() 메소드를 오버라이딩(재정의) 하지 않은 경우 (Point2 클래스의 객체일 경우)
		//=========================================================================
		Point2 q = new Point2(10, 20);
		
		System.out.println("q  = " + q.toString());  //"ObjectTest1.Point2@1f32e575"

		//위 출력이 어떻게 만들어지는지 직접 조립해 보면 결과가 같다
		String manual = q.getClass().getName()  + "@" + Integer.toHexString(q.hashCode());
		//					    "ObjectTest1.Point2@1f32e575"  
        System.out.println("직접 조립한 문자열 = " + manual);
	
        System.out.println("----------------------------------------------------");
        
        //========================================================================
        //3. toString 메소드를 오버라이딩(재정의) 한 경우 (Point 클래스의  객체일 경우)
        
        System.out.println("new Point().toString() = "  +  new Point().toString() );
        
        System.out.println("new Point(10,20).toString() = " +  new Point(10,20).toString() );
        
        Point point = new Point(30, 40);
            
        // 아래 두 줄은 완전히 같은 결과를 낸다.
        // println 에 객체를 넣으면 자바가 내부에서 toString() 을 자동으로 호출하기 때문이다.
        System.out.println("point  =   " +   point);              
        System.out.println("point.toString() =  " +   point.toString());
        
        System.out.println("---------------------------------------------------------------");
        // ================================================================
        // 4. toString() 이 자동 호출되는 다른 경우들
        // ================================================================
        
        //문자열 + 로 연결할 때 도  toString()이 자동으로 호출된다.
        String s1 = "좌표는 " + point + " 입니다.";
        System.out.println(s1); //좌표는 (30,40) 입니다.
        
        // String.valueOf(객체) 도 내부에서 toString() 을 호출한다.
        // 객체가 null 이면 오류를 내지 않고 "null" 이라는 문자열을 돌려준다.
        System.out.println("String.valueOf(point) = " + String.valueOf( point ));

        Point empty = null;
        System.out.println("String.valueOf(null)  = " + String.valueOf(empty));

        System.out.println("---------------------------------------------");

        // ================================================================
        // 5. 자바가 제공하는 클래스들의 toString()
        // ================================================================

        // String 클래스는 toString() 이 저장된 문자열 자체를 반환하도록 재정의되어 있다.
        String str = "hello";
        System.out.println("str.toString()   = " + str.toString());

        // 배열은 toString() 이 재정의되어 있지 않다.
        // 그래서 내용이 아니라 타입기호@16진수해시코드 형태가 출력된다.
        int[] arr = { 1, 2, 3 };
        System.out.println("arr              = " + arr);

        // 배열의 내용을 보려면 java.util.Arrays 클래스의 toString 메소드를 사용한다.
        System.out.println("Arrays.toString(arr) = " + java.util.Arrays.toString(arr));

        /* 정리
         * - getClass() 는 객체가 어떤 클래스로 만들어졌는지 알려 주며 재정의할 수 없다.
         * - Object 의 toString() 은 클래스이름@16진수해시코드 를 반환한다.
         * - 객체의 내용을 출력하고 싶다면 그 클래스에서 toString() 을 재정의해야 한다.
         * - println 이나 문자열 연결에서는 toString() 이 자동으로 호출된다.
         */
        
        
        
		
	}

}






