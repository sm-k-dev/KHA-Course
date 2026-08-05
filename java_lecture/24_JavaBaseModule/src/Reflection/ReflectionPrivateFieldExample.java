package Reflection;

//Field 클래스 
//- 리플렉션을 통해 특정클래스의 변수 정보를 다루기 위한 클래스
import java.lang.reflect.Field;


class Member{  //리플렉션 기법으로 정보를 조사할 특정 클래스 
	
	private String name;  //회원 이름
	private int  age;     //회원 나이
	
	public Member() {}    //기본 생성자 
}

public class ReflectionPrivateFieldExample {
	
	public static void main(String[] args) throws Exception {
	
		//1. 리플렉션 기법으로 정보를 조사할 Member 클래스의 객체 생성
		Member member = new Member();
		//new Member(); <-기본생성자를 호출해 객체 생성하면  String name=null, int age=0; 으로 저장될 것임

		//2. class Class 의  new Class() 객체 얻기
		//Member.class 문법을 사용하여 Member 클래스의 설계도 정보를 담고 있는 Class 객체 획득 
		//이 Class 객체 내부에  모든 변수, 생성자, 메소드 정보(메타 정보)들이 저장되어 있습니다.
		Class claszz = Member.class;
		
		//3. Member 클래스에  만들어 놓은 변수 정보 얻기
		Field nameField = claszz.getDeclaredField("name");
		//Class 라는 객체 통해서  "name"라는 이름을 가진 변수 정보 검색!
		//private 으로 선언된 name 변수도 포함하여 검색됩니다!
		//만약! name 변수를 찾지 못하면 NoSuchFieldException예외 가 발생 됩니다.
		
		//4. private 접근 제한 해제 
		nameField.setAccessible(true);
		//JVM에게 접근 제어 검사를 하지 않도록 요청
		//private -> 접근 가능 상태로 변경
		//없으면 IllegalAccessException 발생
		
		//5. name 변수 값 강제로 설정
		nameField.set(member, "홍길동");
		//member 객체의 name 인스턴스변수에 "홍길동" 문자열 값을 강제로 저장
		//setter 메소드 호출 없이 강제로 "홍길동" 문자열 저장 가능
		
		//6. Member 클래스에 age 변수 정보 얻기
		Field ageField = claszz.getDeclaredField("age");
		
		//7. private 접근 제한 해제 
		ageField.setAccessible(true);
		
		//8. age 변수값 강제 설정
		ageField.set(member, 30);
		//member 객체의 age 인스턴스변수에 정수 값 30을 강제로 저장
		
		//9. 결과 확인 (리플렉션으로 다시 읽어오기)
		System.out.println("이름 : " + nameField.get(member)  );
		//					이름 :     홍길동
		
		//nameField.get(member);
		//-> member 객체의 name 인스턴스변수에 저장된 값을 읽어옴
		//   출력결과 : 홍길동
		
		System.out.println("나이 : " + ageField.get(member)  );
		//					나이 : 30
		
		//ageField.get(member);
		//-> member 객체의 age 인스턴스변수에 저장된 값을 반환 받음 
		//   출력결과 : 30
		
		/*
		이 예제에서 반드시 이해해야 하는 핵심
		
			- 일반 자바 접근 방식
			
				private 변수  -> 직접 외부 클래스에서 접근해서 사용할수 없음
				public setter 메소드를 통해 변경 변수값 변경 하거나 getter 메소드를 통해서 변수값을 얻을 수 있었음
	
			- 리플렉션 접근 방식
			
				Class 객체 생성 -> Field 객체 생성 -> setAccessible(true); -> 변수에 값 변경 후 얻을 수 있음
	
		 */
		
	}

}
