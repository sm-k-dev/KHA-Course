



/*
 주제 : 키보드에서 입력받은 데이터를 입력스트림 통로(FileInputStream)를 통해 읽어들여
 	   특정파일에 저장(쓰기, 기록, 출력) 하기 위해 
 	   출력 스트림 통로(FileOutputStream)를 사용한 예.
 	   
 주제 요약 : 키보드에서 입력한 데이터를 파일에 기록
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOTest06 {
	public static void main(String[] args) {

		int data = 0; //키보드에서 입력한 전체 데이터 크기 중에서 1바이트 크기의 데이터만 읽어 저장할 변수 

		File f = null;
		InputStream fis = null;
		FileOutputStream fos = null;
		
		System.out.println("파일에 기록할 내용을 입력하세요");
		System.out.println("지금 입력한 내용은 파일에 기록됩니다");
		
		try {
			//1. Test.txt 파일에 접근할수 있도록 File 클래스의 객체 생성
			f = new File("Test.txt");
			
			//2. 키보드로 부터 입력한 데이터를 1(한)바이트 단위로  Test.txt파일에 내보내어 기록하기 위한
			//   FileOutputStream 클래스의 객체(출력 스트림 통로) 생성
			fos = new FileOutputStream(f, true);  //true -> 만약 Test.txt파일에 이미 기록된 데이터가 있다면?
												  //        출력스트림 통로를 통해 Test.txt파일 기록된 내용 뒤에 바이트 단위로 내보내어 
												  //        추가로 기록!
			//3. 키보드로 부터 입력한 데이터를 1(한)바이트 단위로 읽어들일  System.in (BufferedInputStream 입력스트림 통로 객체) 얻기
			fis = System.in;
			
			//4. 키보드로 부터 입력 받은 전체 데이터 중에서 1(한)바이트 크기의 데이터를 반복해서 읽어들여 data 변수에 저장
			while( (data = fis.read())  != -1 ) {
				
				//BufferedInputStream 입력 스트림을 통해 한번 읽어들인 1바이트 크기의 데이터를 반복해서 
				//FileOutputStream 출력 스트림을 통해 Test.txt 파일에 1바이트 크기 단위로 데이터를 내보내어 출력(기록) 합니다.
				fos.write(data);
				
			}
					
		} catch (FileNotFoundException e) {
			
			e.printStackTrace(); //발생한 예외 메세지 얻어 출력
			
		} catch (IOException e) {
		
			e.printStackTrace(); //발생한 예외 메세지 얻어 출력 
	
		} finally {
			//무조건! 한번 실행될 코드가 있다면 여기 이자리에 코드 작성.
			//예) 입력스트림 통로(BufferedInputStream객체 메모리), 
			//   출력스트림 통로(FileOutputStream객체 메모리) 모두 사용했으면 JVM에서 제거
			//   요약 : 메모리 자원 제거(해제)
			
			try {
				//-> FileOutputStream 출력스트림 통로 역할의 객체 메모리를 모두 사용했다면? JVM 의 heap영역에서 제거
				if(fos != null) {  fos.close();  }
				
				//-> BufferedInputStream 입력스트림 통로 역할의 객체 메모리를 모두 사용 했다면? JVM의 heap영역에 제거 
				if(fis != null) { fis.close(); }
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} //--------------- finally 블럭 끝 
			
	} //----------------------- main 메소드 블럭 끝

} //----------------------------------- IOText06 class 블럭 끝 

/*
Windows 콘솔에서 Test.txt 파일에 기록이 되는 이유를 쉽게 설명

		이 자바 프로그램(IOTest06.class)은
		키보드 → 입력스트림 통로 → 파일 → 출력스트림 통로
		구조로 동작한다.
		
		여기서
		- 입력스트림 통로 : BufferedInputStream 객체 메모리 (System.in)
		- 출력스트림 통로 : FileOutputStream 객체 메모리
		역할을 한다.
		
		
		프로그램이 동작하는 전체 흐름
		------------------------------------------------
		1) 사용자가 키보드로 글자를 입력한다
		2) 입력한 내용은 바로 자바 프로그램(IOTest06)으로 들어오는 것이 아니라
		   입력스트림 통로(BufferedInputStream 객체 메모리)에
		   잠시 보관된다
		3) Enter 키를 누르면
		   입력스트림 통로에 모여 있던 데이터가
		   자바 프로그램(IOTest06)으로 전달된다
		4) 자바 프로그램(IOTest06)은 전달받은 데이터를
		   1바이트씩 읽어 data 변수에 저장한다
		5) 읽어들인 데이터는
		   출력스트림 통로(FileOutputStream 객체 메모리)를 통해
		   Test.txt 파일에 하나씩 기록된다
		
		
		Test.txt 파일에 기록이 안 되는 것처럼 보였던 이유
		------------------------------------------------
		while ( (data = fis.read()) != -1 )
		이 반복문은
		입력스트림 통로(BufferedInputStream 객체 메모리)에서 더 이상 읽을 데이터가 없을 때,
		즉 -1(입력 종료, EOF)을 받을 때까지
		절대 끝나지 않는다.
		
		Enter 키는 입력을 끝내는 것이 아니라,
		입력스트림 통로에 모여 있는 데이터를
		프로그램으로 보내는 역할만 한다.
		
		그래서 Enter만 누르면
		프로그램은 계속 다음 입력을 기다리게 된다.
		
		
		Ctrl + Z의 역할 (Windows 기준)
		------------------------------------------------
		Ctrl + Z는
		"이제 더 이상 입력할 데이터가 없습니다"
		라는 신호를 입력스트림 통로(BufferedInputStream 객체 메모리)에 전달하는 역할을 한다.
		
		이 신호가 전달되면
		입력스트림 통로(BufferedInputStream 객체 메모리)는
		더 이상 읽을 데이터가 없다고 판단하고
		fis.read()에 -1을 돌려준다.
		
		그 결과
		- while 반복문이 종료되고
		- 출력스트림 통로(FileOutputStream 객체 메모리)를 통한
		  파일 기록이 마무리되며
		- 프로그램이 정상적으로 종료된다.
		
		
		입력 순서 정리 (Windows)
		------------------------------------------------
		1) 키보드로 파일에 저장할 내용 입력
		2) Enter 키를 눌러 입력 내용을
		   입력스트림 통로(BufferedInputStream 객체 메모리)에
		   확정시켜 프로그램으로 전달
		3) Ctrl + Z를 눌러 입력 종료(EOF)를 알림
		
		
		한 줄 요약
		------------------------------------------------
		Test.txt 파일에 기록이 안 되는 것처럼 보였던 이유는
		입력스트림 통로(BufferedInputStream 객체 메모리)에
		입력 종료 신호가 전달되지 않았기 때문이며,
		Enter로 입력 내용을 먼저 전달한 뒤
		Ctrl + Z로 입력 종료를 알려야
		출력스트림 통로(FileOutputStream 객체 메모리)를 통해
		파일에 정상적으로 기록된다.
*/









