package ObjectTest1;
/* ============================================================================
 *  주제 : Object 클래스의 equals(Object obj) 메소드
 * ----------------------------------------------------------------------------
 *  [1] 모든 클래스는 Object 클래스의 자식이다.
 *      우리가 만든 클래스에 extends 를 쓰지 않으면, 컴파일러가 자동으로
 *      "extends Object" 를 붙인다. 따라서 아래 두 줄은 완전히 같은 코드다.
 *
 *          class Member { }
 *          class Member extends Object { }
 *
 *      그 결과, 우리가 만든 모든 클래스의 객체는 Object 클래스가 가진
 *      메소드(equals, toString, hashCode 등)를 상속받아 그대로 쓸 수 있다.
 *
 *  [2] Object 클래스에 실제로 작성되어 있는 equals 메소드의 내용
 *
 *          public boolean equals(Object obj) {
 *              return (this == obj);
 *          }
 *
 *      this      : equals 를 호출한 객체 자신의 주소값
 *      obj       : 매개변수로 전달된 객체의 주소값
 *      this==obj : 두 주소값이 같은지 비교한 결과 (true 또는 false)
 *
 *      즉 Object 의 equals 는 "두 참조변수가 같은 객체를 가리키고 있는가"를
 *      판단한다. 객체 안에 들어 있는 값(예: id 문자열)은 전혀 보지 않는다.
 *
 *  [3] 참조변수와 객체의 관계
 *
 *          Member member1 = new Member();
 *
 *      new Member() 는 힙(Heap) 메모리에 객체를 만들고, 그 객체가 저장된
 *      메모리 주소값을 결과로 돌려준다.
 *      member1 이라는 참조변수에는 객체 자체가 아니라 그 "주소값"이 저장된다.
 *
 *      new 를 두 번 실행하면 객체도 두 개 만들어지고, 주소값도 서로 다르다.
 *      따라서 두 참조변수를 equals 로 비교하면 결과는 false 가 된다.
 *
 *  [4] == 연산자와 equals 메소드의 관계
 *
 *      Object 의 equals 를 그대로 상속받아 쓰는 경우, 아래 두 코드의
 *      결과는 항상 같다.
 *
 *          member1 == member2          // 주소값 직접 비교
 *          member1.equals(member2)     // 내부에서 this == obj 를 수행
 *
 *      단, String 클래스처럼 equals 를 재정의(오버라이딩)한 클래스에서는
 *      결과가 달라진다. 이 파일 마지막 부분에서 확인한다.
 * ========================================================================== */

/* Member 클래스 선언.
 * extends 를 쓰지 않았으므로 컴파일러가 "extends Object" 를 자동으로 붙인다.
 * 그 결과 Member 는 Object 의 equals 메소드를 상속받는다. */
class Member {
	
	//인스턴스변수 :  각 Member객체 마다  따로 만들어지는 변수. 회원 아이디를 저장한다
	public String id;
	
	//기본생성자 : 매개변수 없이 만든 생성자 이므로 id 인스턴스변수는 초기화 되지 않고  null 이 저장된 상태
	public Member() {  }
	
	//매개변수 생성자 : 매개변수 id로 전달 받은 아이디를 인스턴스변수 id에 초기화한다.	
	public Member(String id) {
		this.id = id;
		//this.id : 이 Member객체의 인스턴스변수 id
		//id : 매개변수로 전달된 값
	}
							
    /* Member 클래스에는 equals 메소드를 작성하지 않았다.
     * 그러므로 Object 클래스에 있는 아래 메소드가 그대로 상속되어 사용된다.
     *
     *     public boolean equals(Object obj) {
     *         return (this == obj);
     *     }
     */
}

public class ObjectEqualsExample {

	public static void main(String[] args) {
		//=======================================================
		//1. 서로 다른 두개의 new Member 객체의 주소번지가 같은지 비교 한다.
		//=======================================================
		
		Member member1 = new Member(); //<- 생성된 객체 주소번지 : 0x100

		Member member2 = new Member(); //<- 생성된 객체 주소번지 : 0x200
		
	    // member1.equals(member2) 가 실행되면 Object 클래스의 equals 가 동작한다.
        // 이때 this 는 member1 이 가리키는 객체(0x100),
        //      obj  는 member2 가 가리키는 객체(0x200) 이다.
        // 0x100 == 0x200 은 성립하지 않으므로 false 가 반환된다.
		boolean result = member1.equals(member2);
		
		System.out.println("member1.equals(member2) = " + result);   // false
	         
		//equlas 메소드 내부 코드 동작과 동일한  == 연산자로 확인해도 결과는 위와 같다
		System.out.println("member1 == member2  = " +  (member1 == member2)); //false
		
		System.out.println("--------------------------------------------------");
		
		//======================================================
		//2. 같은 객체를 가리키는 두 참조변수를 비교 한다.
		//=======================================================
		
		//new 를 실행하지 않고, member1참조변수에 저장된 첫번째 Member객체의 주소값을 그대로 복사해서 저장
		//member1 과 member3 참조변수는  같은 하나의 Member객체 메모리를 가리킨다.(사용한다.)
		Member member3 = member1;
		//			   =  0x100;
		//		0x100
		//두 참조변수에 저장된 Member객체 메모리의 주소값이 같으냐? 같으면? true를 출력하겠죠?
		System.out.println("member1.equals(member3) = " + member1.equals(member3) ); //true 같음을 의미 
														// 0x100 .        0x100
		
		//========================================================================
		//3. 인스턴스 변수 값이 같아도   member4.equals(member5)  결과는   false 이다.
		//========================================================================
		
		// 두 객체 모두 인스턴스 변수 값이 "hong"으로 같다.
		Member  member4 = new Member("hong");  // 0x300
		Member  member5 = new Member("hong");  // 0x400
		
		System.out.println("member4.id = " + member4.id + ", member5.id = " + member5.id);
		System.out.println("member4.equals(member5) = " + member4.equals(member5)); //false
		//												   0x300           0x400

		//인스턴스 변수 값 자체가 같은지 비교 할때  인스턴스변수 값 얻어 같은지 비교 한다
		//String 클래스는  equals 메소드가  메소드 오버라이딩 되어 있어  문자열 값이 같은지 비교한다. 
		System.out.println("member4.id.equals(member5.id) = " +  member4.id.equals(member5.id)); //true
		
		System.out.println("--------------------------------------------------------------");
		
		// =====================================================================
		// 4. 제공받는 class Object  의  객체를 직접 생성해서  객체의 주소가 같은지 비교 
		//======================================================================
		
		//Object 클래스는 자바가 제공하는 최상위 클래스 이며 직접 객체를 생성해서 사용할수도 있다
		Object  object1  = new Object();   // 0x600
		Object  object2  = new Object();   // 0x700
		
		System.out.println("object1.equals(object2) = " +  object1.equals(object2)  );  // false
														 //  0x600.equals(0x700)
		
		System.out.println("object1.equals(object1) = " +  object1.equals(object1)  );  // true
														 //  0x600.equals(0x600)
		
		System.out.println("---------------------------------------------------------------");
		
		//================================================================================
		//5.  equals 메소드를 오버리이딩 해 놓은 클래스와의 차이 (String 자식 클래스)
		//=================================================================================
		
		//new String("java") 를 두번 작성했으므로  두 String 객체 메모리는 두개이고  주소값도 각각 다르다.
		String str1 = new String("java"); //0x800
		String str2 = new String("java"); //0x900
		
		// == 연산자는 주소값을 비교하므로 false 이다.
		System.out.println("str1 == str2  = " +  (str1 == str2)  );  //false
		
		// equals 메소드는  String 객체메모리 내부의 인스턴스변수에 저장된 문자열값을 비교하므로 true 이다.
		System.out.println("str1.equals(str2) = " +  str1.equals(str2)  ); //"java" 가 같으므로 true
		
        // 정리
        // - Object 의 equals  : 객체 주소값 비교 (this == obj)
        // - 재정의한 equals   : 그 클래스가 정한 기준으로 값 비교
        // - Member 클래스는 재정의하지 않았으므로 주소값 비교로 동작한다.
		
	}

}



















