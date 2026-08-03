package ObjectTest1;

/* ============================================================================
 *  주제 : DTO 클래스인 Member2 를 사용해 객체를 만들고 정보를 출력한다.
 * ----------------------------------------------------------------------------
 *  같은 패키지(ObjectTest1) 안에 있는 클래스는 import 없이 바로 사용할 수 있다.
 *  Member2 클래스는 Member2.java 파일에, 실행 코드는 이 파일에 나누어 두었다.
 *  하나의 .java 파일에는 public 클래스를 하나만 둘 수 있고,
 *  그 파일 이름은 public 클래스 이름과 같아야 하기 때문이다.
 * ========================================================================== */

public class RecordExample {

    // main 메소드 : 프로그램이 시작되는 지점이다. JVM 이 이 메소드를 가장 먼저 실행한다.
    public static void main(String[] args) {

        // ================================================================
        // 1. Member2 객체 만들기
        // ================================================================

        // new Member2(...) 가 실행되면 힙 메모리에 Member 객체가 만들어지고,
        // 생성자가 실행되어 id, name, age 필드가 초기화된다.
        // 만들어진 객체의 주소값이 참조변수 m1 에 저장된다.
        //
        // 생성자에 값을 넣는 순서는 생성자에 선언된 매개변수 순서와 같아야 한다.
        //     public Member(String id, String name, int age)
        Member2 m1 = new Member2("winter", "눈송이", 25);
        //                       id        name    age

        Member2 m2 = new Member2("admin", "철수", 30);
        //                      id       name   age

        // ================================================================
        // 2. toString 으로 정보 출력하기
        // ================================================================

        // Member 클래스에서 toString 을 재정의해 두었으므로
        // "Member[id=..., name=..., age=...]" 형태의 문자열이 반환된다.
        System.out.println("첫번째 Member객체의 정보 : " + m1.toString());
        System.out.println("두번째 Member객체의 정보 : " + m2.toString());

        // 아래처럼 toString() 을 생략해도 결과는 같다.
        // 문자열과 객체를 + 로 연결하면 자바가 toString() 을 자동으로 호출하기 때문이다.
        System.out.println("toString() 생략            : " + m1);

        System.out.println("---------------------------------------------");

        // ================================================================
        // 3. getter 메소드로 개별 값 꺼내기
        // ================================================================

        // 필드가 private 이므로 m1.id 처럼 직접 접근하면 컴파일 오류가 발생한다.
        //   -> error: id has private access in Member
        // 값을 읽으려면 getter 메소드를 사용해야 한다.
        System.out.println("m1.getId()   = " + m1.getId());
        System.out.println("m1.getName() = " + m1.getName());
        System.out.println("m1.getAge()  = " + m1.getAge());

        // 필드가 final 이므로 값을 바꾸는 메소드(setter)는 존재하지 않는다.
        // 값을 바꾸려면 새 객체를 만들어야 한다.

        System.out.println("---------------------------------------------");

        // ================================================================
        // 4. equals 와 hashCode 의 현재 동작 확인
        // ================================================================

        // m3 는 m1 과 필드값이 완전히 같지만, new 를 다시 실행했으므로
        // 메모리상 별개의 객체다. 따라서 주소값은 서로 다르다.
        Member2 m3 = new Member2("winter", "눈송이", 25);

        System.out.println("m1 == m3         = " + (m1 == m3));          // false (주소값이 다름)

        // Member 의 equals 는 내용이 채워져 있지 않아 항상 false 를 반환한다.
        // 따라서 필드값이 같아도 false 이고, 자기 자신과 비교해도 false 가 나온다.
        System.out.println("m1.equals(m3)    = " + m1.equals(m3));       // false
        System.out.println("m1.equals(m1)    = " + m1.equals(m1));       // false

        // Member 의 hashCode 는 항상 0 을 반환하도록 되어 있다.
        System.out.println("m1.hashCode()    = " + m1.hashCode());       // 0
        System.out.println("m2.hashCode()    = " + m2.hashCode());       // 0

        // 값을 직접 비교하면 같다는 것을 확인할 수 있다.
        // String 은 equals 가 문자 내용을 비교하도록 이미 재정의되어 있다.
        System.out.println("id 값 비교        = " + m1.getId().equals(m3.getId()));   // true

        /* 정리
         * - DTO 는 여러 데이터를 하나의 객체로 묶어 전달하기 위한 클래스다.
         * - 필드는 private 으로 감추고 getter 로 값을 읽는다.
         * - final 필드는 생성자에서만 값을 정할 수 있고 이후 변경할 수 없다.
         * - toString 을 재정의하면 객체의 값을 바로 확인할 수 있다.
         * - equals 와 hashCode 는 내용을 반드시 채워 넣어야 값 비교가 정상 동작한다.
         */
    }
}






