package StringTest;

/* ============================================================================
 *  주제 : String 클래스와 객체 생성 방법 3가지
 * ----------------------------------------------------------------------------
 *  [1] String 클래스란?
 *
 *      java.lang 패키지에 들어 있는 클래스다. 그래서 import 없이 바로 쓸 수 있다.
 *      문자 여러 개가 순서대로 모여 있는 것을 문자열이라고 하며,
 *      String 은 그 문자열을 저장하고 다루기 위한 클래스다.
 *
 *  [2] String 은 불변(immutable) 클래스다
 *
 *      한 번 만들어진 String 객체 안의 문자열은 절대 바뀌지 않는다.
 *
 *          String s = "JAVA";
 *                
 *          s = s + "SCRIPT";
 *
 *      위 코드는 "JAVA" 를 "JAVASCRIPT" 로 고친 것이 아니다.
 *      "JAVASCRIPT" 라는 새 String 객체를 만들어서 그 주소를 s 에 다시 저장한 것이다.
 *      원래의 "JAVA" 객체는 그대로 남아 있다가 나중에 메모리에서 정리된다.
 *
 *      따라서 문자열을 반복해서 이어 붙이는 작업이 많으면
 *      StringBuilder 클래스를 쓰는 것이 낫다.
 *
 *  [3] String 객체를 만드는 방법 3가지
 *
 *      방법1. 문자열 리터럴을 그대로 대입한다.
 *             String s = "리터럴문자열";
 *
 *      방법2. new 연산자와 생성자를 사용한다.
 *             String s = new String("문자열");
 *
 *      방법3. char 배열을 생성자에 전달한다.
 *             char[] arr = {'1','2','3'};
 *             String s = new String(arr);
 *
 *  [4] 방법1 과 방법2 의 결정적 차이 : 문자열 상수 풀
 *
 *      큰따옴표로 직접 쓴 문자열(리터럴)은 힙 메모리 안의 특별한 공간에 저장된다.
 *      이 공간을 문자열 상수 풀(String Constant Pool)이라고 부른다.
 *
 *      리터럴을 대입하면 JVM 은 먼저 풀 안에 같은 내용의 문자열이 있는지 확인한다.
 *          있으면  : 그 객체의 주소값을 그대로 돌려준다. (새 객체를 만들지 않는다)
 *          없으면  : 풀에 새로 만들고 그 주소값을 돌려준다.
 *
 *      그래서 같은 리터럴을 두 변수에 대입하면 두 변수는 같은 객체를 가리킨다.
 *
 *      반면 new String("문자열") 은 풀을 확인하지 않고 무조건 새 객체를 만든다.
 *      그래서 내용이 같아도 주소값은 항상 다르다.
 *
 *  [5] == 와 equals 의 구분
 *
 *      ==      : 두 참조변수에 저장된 주소값이 같은지 비교한다.
 *      equals  : String 클래스가 재정의해 둔 메소드로, 문자 내용이 같은지 비교한다.
 *
 *      문자열의 내용을 비교할 때는 반드시 equals 를 사용해야 한다.
 * ========================================================================== */
public class Ex1 {
	public static void main(String[] args) {
		
		//1. String 클래스의 객체 생성 방법 3가지 
		
		//방법1.  리터럴 문자열 대입.  문자열 상수 풀에 저장된다.
		String  s1 = "JAVA";
		
		//방법2. new 연산자 사용. 상수 풀과 별개로 새 객체가 만들어 진다.
		String s2 = new String("programming");
		
		//방법3. char 배열을 생성자에 전달한다.
		//순서1. 각 문자들이 들어 있는 배열을 만든다.
		char[]  charArr = {'S', 't', 'r', 'i', 'n', 'g'};
		
		//순서2.  배열을 생성자에 전달하면  문자들을 순서대로 이어 붙여  하나의 문자열 "String"을 저장한 객체가 만들어진다
		String  s3  = new String(charArr);
		
		//String 의 toString() 메소드는 저장된 문자열 자체를 반환하도록 재정의(메소드 오버라이딩) 되어 있다.
		//그래서 toString() 메소드 호출 구문을 작성 해 놓든 말든  출력결과는 같다.
		System.out.println(s1.toString()); //"JAVA"
		System.out.println(s1);            //"JAVA"
		System.out.println(s2.toString()); //"programming"
		System.out.println(s3.toString()); //"String"
		System.out.println(s3);            //"String"
		
		System.out.println("---------------------------------------------------------------------");
		
		String str3 = "JSP"; //"JSP" 문자열을 보관하는 String 객체 메모리 생성 후 주소번지 str3참조변수에 저장
		String str4 = "JSP";

        /* 메모리 상태
         *
         *      str3  [ 0x100 ] ─┐
         *                       ├──▶ 문자열 상수 풀 : "JSP"  (객체 1개)
         *      str4  [ 0x100 ] ─┘
         *
         * str4 에 "JSP" 를 대입할 때 JVM 이 풀에서 기존 객체를 찾아
         * 그 주소값을 그대로 넣었기 때문에 두 변수의 값이 같다. */
		
		if(str3 == str4) {
			System.out.println("[리터럴] 두 참조변수가 같은 String 객체 하나를 가리킨다. (== 결과 true)"); // << 출력됨
		}else {
			System.out.println("[리터럴] 서로 다른 String 객체다. (== 결과 false)");
		}
		
		System.out.println("-----------------------------------------------------------");
		
		//3. 방법2. 로 만든 두 String 객체 비교 ( new 연산자 )
		
		String str1 = new String("Java");
		String str2 = new String("Java");
        /* 메모리 상태
        *
        *      str1  [ 0x200 ] ──▶ String 객체 : "Java"
        *      str2  [ 0x300 ] ──▶ String 객체 : "Java"   (별개의 객체)
        *
        * new 는 풀을 확인하지 않으므로 객체가 두 개 만들어진다. */
		
		if(str1 == str2) {
			System.out.println("[new] 두 참조변수가 같은 String객체 메모리 하나를 가리킨다");	
		}else {
			System.out.println("[new] 문자열 값은 같지만 서로 다른 String 객체 메모리 이다.");  // 출력 됨
		}
		
		// 두  String 객체 메모리에 저장된  각각의 "Java" 문자열이 같은지 비교 하기 위해 equals 메소드 사용
		System.out.println("str1.equals(str2) = " + str1.equals(str2) ); // true
		
		System.out.println("------------------------------------------------------------------");
		
		//4. 리터럴 과 new 를 섞어서 비교하기 
		
		String a = "Java";
		String b = new String("Java");
		
		//두 String 객체 메모리의 주소번지가 같으냐? 라고 물어 봅시다
		System.out.println("a == b  = " +  (a == b) );  //false
		
		//두 String 객체 메모리 안에 저장된 "Java" 문자열 값이 같으냐? 라고 물어 봅시다
		System.out.println("a.equals(b) = " + a.equals(b)); //true    <------------- 문자열 끼리 비교 !!!!!!!!!
		
		System.out.println("-----------------------------------------------------------------");
		
		//5. 불변 확인
		
		String  origin = "JAVA";
		//				 ======================
		//					"JAVA"
		//				========================
		
		String  changed = origin.concat("_Study");   // 새 객체가 만들어진다.
		//				  ======================
		//					"JAVA_Study"
		//				  ======================
		
		//origin 참조변수가 가리키는 String객체의 내용은 바뀌지 않았다.
		System.out.println("origin = " + origin.toString());  //"JAVA"
		System.out.println("changed = " + changed.toString()); //"JAVA_Study"
		
		//대입 까지 해야 참조변수가 새 객체를 가리키게 된다.
		origin = origin.concat("_Study");
		System.out.println("대입 후 origin = " + origin.toString()); //"JAVA_Study"
	
	}

}

 












