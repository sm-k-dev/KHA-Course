package ObjectTest1;


// Lombok 이 자동으로 만들어 준  메소드들이 실제로 동작하는지 확인 하는 실행용 클래스 
public class LombokTest {

	public static void main(String[] args) {

		// @NoArgsConstructor 가 만들어 준 기본 생성자
		Member3  m1 = new Member3();
		
		// @Setter 가 만들어 준  setter 메소드로 값을 변경해서 저장
		m1.setMemberNo(1);
		m1.setMemberEmail("winter@test.com");
		m1.setMemberNickName("눈송이"); 
		
		// @Getter 가 만들어 준  getter 메소드로 값을 읽는다.
		System.out.println("getMemberNo()  =  "  +  m1.getMemberNo());
		System.out.println("getMemberNickName() = " + m1.getMemberNickName());
		
		// @ToString 이 만들어준 toString() 메소드 자동으로 호출
		System.out.println("m1 = " +  m1.toString());
		
		System.out.println("------------------------------------------------------------------");
		
		// @AllArgsContructor  가  만들어 주는 생성자 
		Member3  m2 = new Member3(2, "admin@test.com", "1234", "철수", "010-1111-2222", "서울시", "img.png");
		Member3  m3 = new Member3(2, "admin@test.com", "1234", "철수", "010-1111-2222", "서울시", "img.png");
		
		System.out.println("m2 = " + m2);
		
		// new 를  두번 했으므로  각 객체의 주소 번지는 다르다.
		System.out.println("m2 == m3  = " +  (m2 == m3)); //false
		
		//@EqualsAndHashCode  가  만들어준  equals 메소드는 모든 인스턴스변수값이 같은지 비교하게 되어 있다
		System.out.println("m2.equals(m3) = " +  m2.equals(m3));  //true
		System.out.println("m2.hashCode() = " +  m2.hashCode());
		System.out.println("m3.hashCode() = " +  m3.hashCode());  //m2 와 같은 10진수 값 
		
		//인스턴스 변수값 하나만 바꾸면 equlas 메소드의 결과도 hashCode 메소드 호출 결과도 달라진다
		m3.setMemberNickName("영희");
		System.out.println("닉네임 변경 후 m2.equals(m3) = " + m2.equals(m3)); //false
		
	}

}








