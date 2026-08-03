package ObjectTest1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* ============================================================================
 *  주제 : Lombok 라이브러리를 사용해 DTO 클래스를 작성한다.
 * ----------------------------------------------------------------------------
 *  [1] 라이브러리란?
 *
 *      다른 개발자가 미리 만들어 둔 클래스들을 하나로 묶어 놓은 파일(.jar)이다.
 *      프로젝트에 추가하면 그 안의 클래스와 기능을 가져다 쓸 수 있다.
 *      Lombok 은 lombok.jar 라는 파일 하나로 제공된다.
 *
 *  [2] Lombok 이 하는 일
 *
 *      DTO 클래스를 직접 작성하면 getter, setter, 생성자, toString,
 *      equals, hashCode 를 손으로 다 써야 한다.
 *      필드가 7개면 getter 7개, setter 7개를 포함해 수백 줄이 된다.
 *
 *      Lombok 은 클래스 위에 어노테이션을 붙이기만 하면,
 *      컴파일하는 시점에 그 메소드들을 자동으로 만들어 .class 파일에 넣어 준다.
 *
 *      중요한 점은 다음과 같다.
 *      - 우리가 보는 .java 파일에는 그 메소드들이 보이지 않는다.
 *      - 그러나 컴파일된 .class 파일 안에는 실제로 들어 있다.
 *      - 그래서 다른 클래스에서 member.getMemberNo() 를 호출할 수 있다.
 *
 *  [3] 어노테이션이란?
 *
 *      골뱅이 기호(@)로 시작하는 표시다. 실행되는 명령문이 아니라,
 *      컴파일러나 다른 프로그램에게 "이 클래스를 이렇게 처리하라"고
 *      알려 주는 표시 역할을 한다.
 *      @Override 도 어노테이션이며, "부모 메소드를 재정의한다"는 표시였다.
 *
 *  [4] 자주 쓰는 Lombok 어노테이션
 *
 *      @Getter                  : 모든 필드의 getter 메소드를 만든다.
 *      @Setter                  : 모든 필드의 setter 메소드를 만든다.
 *      @ToString                : toString() 메소드를 만든다.
 *      @EqualsAndHashCode       : equals() 와 hashCode() 메소드를 만든다.
 *      @NoArgsConstructor       : 매개변수가 없는 기본 생성자를 만든다.
 *      @AllArgsConstructor      : 모든 필드를 매개변수로 받는 생성자를 만든다.
 *      @RequiredArgsConstructor : final 필드만 매개변수로 받는 생성자를 만든다.
 *      @Data                    : 위 기능 중 여러 개를 한 번에 적용한다.
 *                                 (@Getter, @Setter, @ToString,
 *                                  @EqualsAndHashCode, @RequiredArgsConstructor)
 *
 *  [5] 사용하기 전 준비 사항
 *
 *      (1) lombok.jar 파일을 프로젝트의 빌드 경로(Build Path)에 추가한다.
 *      (2) 이클립스 같은 편집기에서는 lombok.jar 를 한 번 실행해 설치해야
 *          자동 생성된 메소드를 편집기가 인식한다.
 *          설치하지 않으면 컴파일은 되지만 편집기에 빨간 오류 표시가 남는다.
 *      (3) Spring Boot 프로젝트에서는 pom.xml 또는 build.gradle 에
 *          lombok 의존성을 추가한다.
 * ========================================================================== */


/* 아래 어노테이션들은 컴파일할 때 각각 다음 코드를 자동으로 만들어 넣는다.
 * 어노테이션을 붙이는 순서는 결과에 영향을 주지 않는다. */


//equals(Object obj) 와 hashCode() 를 만든다.
//만들어지는 기준 : 모든 필드값이 같으면 equals 결과가 true 가 된다.
//              (Object 의 원래 equals 는 주소값만 비교했다.)
//@EqualsAndHashCode

//toString() 을 만든다.
//만들어지는 형태 : Member3(memberNo=1, memberEmail=..., memberPw=..., ...)
//              클래스이름과 모든 필드의 이름=값 이 들어간다.
//@ToString

//모든 필드의 setter 메소드를 만든다.
//예) public void setMemberNo(int memberNo) { this.memberNo = memberNo; }
//주의 : final 필드에는 setter 가 만들어지지 않는다. 값을 바꿀 수 없기 때문이다.
//@Setter

//모든 필드의 getter 메소드를 만든다.
//예) public int getMemberNo() { return this.memberNo; }
//주의 : boolean 타입 필드는 get 이 아니라 is 로 시작하는 이름이 만들어진다.
//@Getter

//모든 필드를 매개변수로 받는 생성자를 만든다.
//매개변수 순서는 아래에 필드를 선언한 순서와 같다.
//예) new Member3(1, "a@b.com", "1234", "닉네임", "010-...", "주소", "img.png")
@AllArgsConstructor

//매개변수가 없는 기본 생성자를 만든다.
//예) new Member3()
//@AllArgsConstructor 만 붙이면 기본 생성자가 사라지므로,
//new Member3w() 를 쓰려면 이 어노테이션도 함께 붙여야 한다.
@NoArgsConstructor
 
//@Data 를 쓰면 위 어노테이션 중 여러 개를 한 번에 적용할 수 있다.
//다만 @Data 에는 @AllArgsConstructor 가 포함되어 있지 않다.
//모든 필드를 받는 생성자가 필요하면 @AllArgsConstructor 를 따로 붙여야 한다.
@Data
public class Member3 {  //<- 회원 한명의 정보가 저장되는 DTO 역할을 하는 클래스
    // ------------------------------------------------------------------
    // 필드 선언
    // private 이므로 다른 클래스에서 직접 접근할 수 없다.
    // 값을 읽고 쓰는 일은 Lombok 이 자동으로 만들어 준 getter, setter 로 한다.
    //
    // final 을 붙이지 않았으므로 객체를 만든 뒤에도 값을 바꿀 수 있다.
    // 그래서 @Setter 가 동작한다.
    // ------------------------------------------------------------------	
	private int memberNo;    		//회원 번호 -> getMemberNo(), setMemberNo(int)
	private String memberEmail;     //회원 이메일 -> getMemberEmail(), setMemberEamil(String)
	private String memberPw;		//회원 비밀번호
	private String memberNickName;  //회원 별명
	private String memberTel;		//회원 전화번호 
	private String memberAddress;   //회원 주소
	private String profileImage;    //프로필 사진 파일 이름 
	

    /* 이 클래스에는 메소드를 한 줄도 쓰지 않았지만,
     * 컴파일 후에는 아래 메소드들이 .class 파일 안에 만들어져 있다.
     *
     *   생성자 2개
     *       public Member()
     *       public Member(int, String, String, String, String, String, String)
     *   getter 7개
     *       getMemberNo, getMemberEmail, getMemberPw, getMemberNickName,
     *       getMemberTel, getMemberAddress, getProfileImage   
     *   setter 7개
     *       setMemberNo, setMemberEmail, setMemberPw, setMemberNickName,
     *       setMemberTel, setMemberAddress, setProfileImage 
     *   toString()  , equals(Object), hashCode()
	 */
	
		
}









