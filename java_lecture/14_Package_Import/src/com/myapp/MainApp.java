package com.myapp;
/*
	com.external.models 패키지에 포함된 User 클래스 와 
	com.myapp.models 패키지에 포함된 User 다른 클래스 

	즉! 두 User클래스의 이름이 같기 때문에 둘다 한번에 import구문을 작성하면
	충돌이 일어나 자바컴파일러가 어떤 패키지 안의 User클래스를 사용하는지 모름!

	충돌 해결 방법.  하나의 User클래스만 import구문을 작성하고,
				  다른 하나의 User클래스는  패키지 경로를 포함한 클래스이름 전체경로를 코드 사용 직접 기술
*/
//1. 하나의 User클래스만 import구문을 작성하고,
import com.myapp.models.User;

public class MainApp {
	public static void main(String[] args) {		
		//1.위 import com.myapp.models.User; 이렇게 작성한 User클래스 사용해 객체 생성 가능
		User  user1 = new User("홍길동");
			  user1.printInfo();   //내 앱 사용자 : 홍길동
		
		//2. 다른 하나의 User클래스는 패키지 경로를 포함한 클래스이름 전체경로를 코드 사용 직접 기술
		     //패키지명.클래스명    참조변수   =  new  패키지명.생성자명(1234);
  com.external.models.User     user2    =  new  com.external.models.User(1234);
  							   user2.printInfo(); //외부 시스템 사용자 ID : 1234
	}

}