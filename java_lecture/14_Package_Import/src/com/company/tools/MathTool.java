// 패키지 선언문
//	- 이 MathTool 클래스 설계도는 com.company.tools 패키지에 포함됨을
//	자바 컴파일러 (javac.exe)에게 명시한다.
package com.company.tools;

// public 클래스로 선언해야 다른 패키지에서도 이 MathTool 클래스 내부에 접근해서 사용 할 수 있다.
public class MathTool {

	// 두 정수를 더해서 겨로가값을 반환하는 기능의 메소드
	public int add ( int a, int b ) {
		return a + b;
	}
	
	// 두 정수를 곱해서 결과값을 반환하는 기능의 메소드
	public int multiply ( int a, int b ) {
		return a * b;
	}

}
