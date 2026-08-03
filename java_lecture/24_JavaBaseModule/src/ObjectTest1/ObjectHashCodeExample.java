package ObjectTest1;
/* ============================================================================
 *  주제 : Object 클래스의 hashCode() 메소드와 equals() 메소드의 재정의
 * ----------------------------------------------------------------------------
 *  [1] Object 클래스에 선언되어 있는 hashCode 메소드
 *
 *          @IntrinsicCandidate
 *          public native int hashCode();
 *
 *      - 반환값 : 객체를 구분하기 위해 JVM 이 만들어 주는 정수값이다.
 *                이 정수값을 해시코드(hash code)라고 부른다.
 *      - 재정의하지 않으면, 서로 다른 객체는 대체로 서로 다른 정수값을 갖는다.
 *        (같은 값이 나올 수도 있으나 일반적인 경우는 아니다.)
 *
 *      - native 키워드
 *        메소드의 실행 내용이 자바 코드가 아니라 C, C++ 같은 다른 언어로
 *        작성되어 있다는 표시다. 그래서 { } 안의 내용 없이 세미콜론으로 끝난다.
 *
 *      - @IntrinsicCandidate
 *        JVM 에게 "이 메소드는 내부적으로 더 빠른 방식으로 바꿔 실행해도 된다"
 *        고 알려 주는 표시다. 자주 호출되면 JIT 컴파일러가 이 메소드를 더 빠른
 *        기계어 명령으로 바꿔 실행한다. 프로그래머가 직접 신경 쓸 부분은 아니다.
 *
 *  [2] 동일 객체와 동등 객체의 구분
 *
 *      동일 객체 : 두 참조변수가 가리키는 객체가 메모리상 같은 하나의 객체.
 *                 판단 기준은 주소값 비교( == )이다.
 *      동등 객체 : 서로 다른 객체지만, 객체 안에 들어 있는 값이 같아서
 *                 같은 것으로 취급하기로 정한 경우.
 *
 *      Object 가 제공하는 equals 와 hashCode 는 "동일 객체" 기준으로만 동작한다.
 *      값이 같으면 같은 객체로 취급하고 싶다면, 그 클래스에서
 *      equals 와 hashCode 를 직접 다시 작성해야 한다. 이것을 재정의(오버라이딩)라고 한다.
 *
 *  [3] equals 와 hashCode 를 함께 재정의해야 하는 이유
 *
 *      자바가 정한 규칙은 다음과 같다.
 *          equals 로 비교해서 true 인 두 객체는 hashCode 값도 반드시 같아야 한다.
 *
 *      HashMap, HashSet 같은 클래스는 값을 저장하고 찾을 때
 *      먼저 hashCode 로 저장 위치를 정하고, 그 위치에서 equals 로 최종 확인한다.
 *      따라서 equals 만 재정의하고 hashCode 를 재정의하지 않으면,
 *      값이 같은 객체인데도 서로 다른 위치에 저장되어 찾지 못하는 문제가 생긴다.
 * ========================================================================== */


/* Student 클래스.
 * extends 를 쓰지 않았으므로 컴파일러가 "extends Object" 를 자동으로 붙인다.
 * 따라서 Object 의 hashCode 와 equals 를 상속받고, 아래에서 그 둘을 재정의한다. */
class Student {

    // private : 클래스 밖에서 직접 접근할 수 없다. 접근하려면 아래 getter 메소드를 사용한다.
    private int no;        // 학생 번호
    private String name;   // 학생 이름

    // 생성자. 객체를 만들 때 학생 번호와 이름을 받아 인스턴스 변수를 초기화한다.
    public Student(int no, String name) {
        // super();  <- 컴파일러가 자동으로 넣어 주는 부모(Object) 생성자 호출문
        this.no = no;       // this.no : 이 객체의 인스턴스 변수 / no : 매개변수
        this.name = name;
    }

    // getter 메소드 : private 인 인스턴스 변수의 값을 외부에서 읽을 수 있게 해 준다.
    public int getNo() {
        return this.no;
    }

    public String getName() {
        return this.name;
    }

    /* ------------------------------------------------------------------
     * hashCode 재정의
     * ------------------------------------------------------------------
     * @Override : 부모 클래스에 있는 메소드를 다시 작성한다는 표시다.
     *             메소드 이름이나 매개변수를 잘못 쓰면 컴파일 오류가 발생하므로
     *             실수를 막아 준다.
     *
     * 재정의 내용 : 학생 번호와 학생 이름을 조합해 정수 하나를 만들어 반환한다.
     *             번호와 이름이 같은 두 객체는 항상 같은 정수를 반환하게 된다.
     *
     * String 클래스의 hashCode 는 문자 내용으로 정수를 계산하도록 이미
     * 재정의되어 있다. 그래서 내용이 같은 문자열은 항상 같은 값을 돌려준다.
     * ------------------------------------------------------------------ */
    @Override
    public int hashCode() {
        // 예) no 가 1, name 이 "홍길동" 인 경우
        //     "홍길동".hashCode() 의 값에 1 을 더한 값이 반환된다.
        return this.no + this.name.hashCode();
    }

    /* ------------------------------------------------------------------
     * equals 재정의
     * ------------------------------------------------------------------
     * Object 의 원래 equals 는 아래와 같이 주소값만 비교했다.
     *
     *     public boolean equals(Object obj) {
     *         return (this == obj);
     *     }
     *
     * 여기서는 학생 번호와 이름이 모두 같으면 true 를 반환하도록 다시 작성한다.
     * ------------------------------------------------------------------ */
    @Override					//new Student(1, "홍길동")
    public boolean equals(Object obj) {

        if (obj instanceof Student target) {

            // 기본형 int 는 == 로 값을 비교한다.
            // 참조형 String 은 == 를 쓰면 주소값 비교가 되므로,
            // 문자 내용을 비교하는 equals 를 사용해야 한다.
            if (this.no == target.getNo() && this.name.equals(target.getName())) {
                return true;   // 번호와 이름이 모두 같으므로 동등 객체로 판단한다.
            }
        }

        // 아래 두 경우에는 false 를 반환한다.
        //  1) 전달받은 obj 가 Student 로 만들어진 객체가 아닌 경우
        //  2) Student 는 맞지만 번호나 이름 중 하나라도 다른 경우
        return false;
    }
}


 /* obj instanceof Student target
 *
 * instanceof 연산자 : 왼쪽 객체가 오른쪽 클래스로 만들어진 객체인지 검사한다.
 *                    맞으면 true, 아니면 false 를 반환한다.
 *
 * 뒤에 붙은 target 은 Java 16 부터 정식으로 사용할 수 있는 문법이다.
 * 검사가 true 인 경우, obj 를 Student 타입으로 변환한 결과를 자동으로
 * target 변수에 담아 준다. 아래 두 코드는 같은 의미다.
 *
 *     if (obj instanceof Student target) { ... }
 *
 *     if (obj instanceof Student) {
 *         Student target = (Student) obj;   // 다운캐스팅
 *         ...
 *     }
 *
 * 매개변수 타입이 Object 인 이유는, 어떤 종류의 객체가 전달되어도
 * 받을 수 있어야 하기 때문이다. 그래서 Student 인지 먼저 검사한다. */		


public class ObjectHashCodeExample {

	public static void main(String[] args) {
		//================================================================================
		//1. 메소드오버라이딩 하지 않은 경우  :   Object 의 객체를 생성해서 hashCode 메소드를 직접 호출
		//=================================================================================
		Object object1 = new Object();
		Object object2 = new Object();
		
		//서로 다른 Object객체 이므로 서로 다른 정수값이 출력된다.
		//이 값은 프로그램을 실행할 떄마다 달라진다. 고정된 값이 아니다.
		System.out.println("object1.hashCode() = " +  object1.hashCode() ); //object1.hashCode() = 1392838282
		System.out.println("object2.hashCode() = " +  object2.hashCode() ); //object2.hashCode() = 989110044
		
		//한번더 object1 참조변수가 사용하고 있는 new Object()객체의 hashCode() 메소드를 호출해보자
		System.out.println("object1.hashCode() 재호출 = " + object1.hashCode() ); //object1.hashCode() 재호출 = 1392838282
		
		System.out.println("---------------------------------------------------------------");
		
		//==========================================================================
		//2. 메소드 오버라이딩 한 경우  : Student 자식 클래스의 hashCode 메소드가 동작 한다.
		//==========================================================================
		
		//번호와 이름이 같은 두 개의 Student 객체를 만든다.
		//new 를 두번 실행했으므로 메모리상  객체는 두개 이고 객체주소값은 서로 다르다
		Student s1  = new Student(1, "홍길동");
		Student s2  = new Student(1, "홍길동");
		
		//메소드 오버라이딩 한  hashCode 메소드는  번호와 이름으로 값을 계산하므로,
		//두 객체 주소값이 달라도  두 결과는 항상 같은 정수가 됩니다.
		System.out.println("\"홍길동\".hashCode() = " + "홍길동".hashCode()  );
		System.out.println("s1.hashCode() = " + s1.hashCode()); // 54150063
		System.out.println("s2.hashCode() = " + s2.hashCode()); // 54150063
		
		//주소값 자체는 다르다는 사실을  == 로 확인한다
		System.out.println("s1 == s2 (주소값 비교) = " +  (s1 == s2) ); //false
				
		System.out.println("---------------------------------------------------------");
		
		//==============================================================
		//3. hashCode 메소드와  equals 메소드를 함꼐 사용해 동등 객체인지 판단 한다.
		//==============================================================
		if( s1.hashCode() == s2.hashCode()) {
			//54150063    ==  54150063
			
			// 해시코드가 같다는 것만으로는 값이 같다고 단정할 수 없다.
            // 서로 다른 값이 우연히 같은 정수를 만들어 내는 경우도 있기 때문이다.
			// 그래서 equals 로 인스턴스 변수 값을 최종 확인한다.
			if(s1.equals(s2)) {
				System.out.println("해시코드가 같고 인스턴스변수 값도 같으므로 동등 객체 입니다.");
			}else {
				System.out.println("해시코드는 같지만 인스턴스변수 값이 다르므로 동등 객체가 아닙니다.");
			}
		}else {
			
			System.out.println("해시코드가 다르므로 동등 객체가 아닙니다.");
		}
		
		System.out.println("--------------------------------------------------");
		
		//====================================================================================
		//4. 인스턴스 변수 값이 다른 경우의  Student 객체를 equals 메소드의 매개변수로 전달 한 경우 
		//====================================================================================
		Student  s3  = new Student(2, "김철수");
		
		System.out.println("s3.hashCode() = " +  s3.hashCode());
		System.out.println("s1.equals(s3) = " +  s1.equals(s3) );  //번호와 이름이 다르므로 false
		
	    // Student 가 아닌 String 객체를 전달하면 instanceof 검사에서 false 가 되어
        // equals 는 false 를 반환한다. 오류가 발생하지는 않는다.
		System.out.println("s1.equals(\"홍길동\") = " +   s1.equals("홍길동"));  //fasle
		
		//자기 자신 Student객체 끼리 비교하면  번호와 이름이 당연히 같으므로  true
		System.out.println("s1.equals(s1) = " + s1.equals(s1));
		
						//	new Student(1, "홍길동").equals(new Student(1, "홍길동"))
		

	}

}














