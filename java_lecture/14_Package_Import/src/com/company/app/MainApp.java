// 패키지 선언문
//	- MainApp 클래스는 com.company.app 패키지에 포함됨을 자바 컴파일러(javac.exe)에게 알려주는 구문
package com.company.app;

// import 문
// 	- 다른 패키지(com.company.tools)에 포함된 외부 MathTool 클래스를 현재 파일에서 사용하기 위해 불러오는 구문
import com.company.tools.MathTool;

public class MainApp {
	
	// main 메서드: 자바코드를 가동시키는 시작점
	public static void main(String[] args) {
		// 다른 패키지(com.company.tools)에 만들어져 있는 MathTool 클래스의 객체를 생성한다.
		// 생성하기 전에 위에 import 해야 한다.
		MathTool tool = new MathTool();
/*		===============================================
		//두 정수를 더해서 결과값을 반환하는 기능의 메소드 
		public int add(int a, int b) {
			return a + b;
		}
		
		//두 정수를 곱해서 결과값을 반환하는 기능의 메소드 
		public int multiply(int a, int b) {
			return a * b;
		}
		==============================================
*/	
		// 두 정수의 합을 구하기 위해 MathTool 객체 메모리에 포함된 add 메소드 호출
		int sum = tool.add(7, 3);
		System.out.println("덧셈 결과: " + sum);
		
		// 두 정수의 곱의 결과를 얻기 위해 MathTool 객체 메모리에 포함된 multiply 메소드 호출
		int product = tool.multiply(7, 3);
		System.out.println("곱셈 결과: " + product);
	}

}
