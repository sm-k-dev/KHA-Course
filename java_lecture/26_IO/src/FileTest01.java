/*
  File 클래스
  - java.io패키지 내부에 만들어져 있는 클래스
  - 파일이나 디렉터리(폴더)의 정보를 조작하는데 사용되는 클래스
  - 이 File 클래스는 실제로 파일에 데이터를 읽거나 쓰지는 않지만
    파일 및 디렉터리 경로를 적용하여 파일 또는 디렉터리 정보를 얻을수 있다.

  ■ ★ 가장 먼저 알아야 하는 사실 : File 객체 생성 = 실제 파일 생성이 아니다!

     File file = new File("abc.txt");

     - 이 코드는 abc.txt 파일을 만드는 것도, 여는 것도 아니다.
     - "abc.txt라는 경로를 다루기 위한 정보 객체"를 힙에 만들 뿐이다.
     - 그래서 실제로는 존재하지 않는 파일의 경로를 넣어도 객체는 문제없이 만들어진다!

     [텍스트 모델링]

       new File("abc.txt")
             │
             ▼
       [Heap]  File 객체                          실제 하드디스크
       ┌──────────────────┐    존재하는지는     ┌──────────────┐
       │ 경로: "abc.txt"   │ ---알 수 없음!---▶ │ abc.txt 있음? │
       └──────────────────┘                    └──────────────┘
       (경로 문자열을 품은                       (진짜 있는지는
        정보 조작용 객체일 뿐)                     exists()로 물어봐야 안다)

     -> 따라서 파일 정보를 출력하기 전에 exists()로 실제 존재 여부를 확인하는 것이 안전하다.
        확인 없이 없는 파일의 length()를 부르면 0, lastModified()를 부르면 0이 반환되어
        new Date(0) = 1970년 1월 1일이라는 이상한 날짜가 출력되는 사고가 난다!

  - 주요 기능 및 메소드

     1. 파일 및 디렉토리 생성
        createNewFile() : 파일을 생성합니다
        mkdir() : 디렉터리(폴더)를 생성합니다
        mkdirs() : 필요한 상위 디렉터리를 포함하여 여러 디렉터리를 생성합니다

     2. 파일 및 디렉토리 정보 확인
     	exists() : 파일이나 디렉터리가 존재하는지 여부를 확인할수 있습니다.
     	isFile() : 파일인지 확인합니다.
     	isDirectory() : 디렉터리 인지 확인합니다.
     	getName() : 파일 또는 디렉토리의 이름을 반환합니다.
     	length() : 파일의 크기를 바이트 단위로 반환합니다.

     3. 파일 및 디렉토리 삭제
        delete() : 파일이나 디렉터리를 삭제합니다

     4. 파일 경로 관련
        getPath() : 파일의 경로를 문자열로 반환합니다
        getAbsolutePath() : 파일의 절대 경로를 반환합니다.

     5. 파일 및 디렉터리 내부에 존재하는 목록 정보
        list()  : 디렉터리에 있는 파일 및 디렉터리 이름을 배열에 담아 배열을 반환
        listFiles() : 디렉터리에 있는 파일 및 디렉토리를 File객체 배열로 반환


   		 //boolean canRead() 메소드
		 //-> File file = new File("파일경로"); 객체를 생성해서 저장한 "파일경로"의 파일을 읽기 모드로 열어 읽어들일수 있느냐?라고 물어보는메소드
		 //   읽어 들일수 있으면? true 반환, 읽어 들일수 없으면 false 를 반환 하는 메소드

  ■ 경로 입력 참고
  
     - 절대 경로 : 드라이브부터 전부 적는 경로
       예) C:\Users\KHBS_D_0\Desktop\workspace_java\24IO\src\FileTest01.java
       
     - 상대 경로 : 파일명만 적으면 "프로그램을 실행한 작업 폴더" 기준으로 찾는다.
       이클립스에서는 프로젝트 폴더가 기준이다! (src 폴더가 아님 - 단골 질문)
       그래서 파일명만 입력했을 때 절대 경로가 어떻게 완성되는지를
       getAbsolutePath()로 직접 확인해 보는 것이 이 예제의 포인트 중 하나다.

테스트

입력

C:\Users\KHBS_D_0\Desktop\workspace_java\24IO\src\FileTest01.java

*/

import java.io.File;          //파일/디렉터리 정보 조작용 클래스
import java.io.IOException;   //입 출력 예외 클래스
import java.util.Date;        //밀리초 정수를 사람이 읽는 날짜로 바꿔 주는 클래스 


public class FileTest01 {

	public static void main(String[] args) throws IOException { //main 메소드 내부에서 입출력 예외가 발생하면
																//main 메소드를 호출한 JVM아 니가 알아서 입출력 예외처리 하라!
																//예외 처리 떠넘기기  throws IOException
		//파일 명을 입력 받아 저장할 byte 배열 메모리 생성 (100칸, 각 칸의 초기값은 전부 0)
		byte[] byteFileName = new byte[100];
		
		System.out.print("정보를 확인할 파일명(또는 경로)을 입력하세요 : ");
		
		/*
		  키보드로 부터 입력받은 파일명을 입력스트림 통로를 통해  
		  위 선언된 byteFileName byte 배열의 크기 100바이트 만큼 "최대" 한번에 읽어들여
		  byteFileName byte 배열 메모리에 각칸에 저장 시킵니다.
		  요약 : 키보드에서 입력 받은 파일명을  new byte[100]; 배열에 저장  
		  작성 방법 : InputStream에 만들어져 있는  read(byte[] b) 메소드를 호출해서 사용 
		  
  		 ★ read()와 read(byte[] b)의 차이 (오버로딩!)
		    read()        : 1바이트만 읽어 정수로 반환                (IOTest00에서 사용)
		    read(byte[] b): 배열 크기만큼을 한도로 여러 바이트를 한번에 읽어
		                    배열에 채워 넣고, "실제로 읽은 바이트 수"를 정수로 반환
		    -> write의 오버로딩(1바이트 vs 배열)과 완전히 짝을 이루는 구조다! (IOTest0A 참고)
		*/
		System.in.read(byteFileName);
		/*
		 [텍스트 모델링] "a.txt" + Enter 를 입력했을 때 배열의 실제 내용 (윈도우 기준)

		 byteFileName 배열 (100칸)
		 ┌────┬────┬─────┬─────┬─────┬────┬────┬────┬────┬─────────┐
		 │ 97 │ 46 │ 116 │ 120 │ 116 │ 13 │ 10 │ 0  │ 0  │ ...0... │
		 └────┴────┴─────┴─────┴─────┴────┴────┴────┴────┴─────────┘
		   'a'   '.'   't'   'x'   't'   \r   \n   └── 안 쓴 93칸은 초기값 0 그대로 ──┘
		                                 └엔터 2바이트┘

		 ★ 입력한 파일명 뒤에 ①엔터 바이트(\r\n)와 ②비어 있는 칸의 0들이 함께 남아 있다!
		   이것들을 그대로 두면 "a.txt\r\n␀␀..." 라는 엉뚱한 파일명이 되어 파일을 못 찾는다.
		   -> 바로 아래에서 trim()으로 제거하는 이유가 이것이다.
		*/		
		
		//위 byteFileName byte 배열에 읽어온 파일명을 문자열로 변환해서 저장할 변수 선언
		String fileName = null;
		
		/*
		 키보드로 부터 입력받은 파일명 중 byteFileName byte 배열에 저장된 읽어들인 바이트정수들을
		 문자열로 변환시켜 문자열 객체 메모리에 보관 후 양쪽 공백을 제거 후 반환 받아 fileName변수에 저장

		 [실행 순서 분해]
		   1순위 : new String(byteFileName)
		           -> 배열 100칸의 바이트들을 문자로 묶어 복원한 문자열 객체 생성
		              "a.txt\r\n␀␀...␀"  (뒤에 지저분한 것들이 붙은 상태)
		           ※ String 생성자는 여러 바이트를 문자 단위로 "묶어서" 복원하므로
		             IOTest00에서 깨졌던 한글 파일명도 여기서는 안 깨진다!
		   2순위 : .trim()
		           -> 문자열 양쪽 끝의 공백과 제어문자(엔터 \r\n, 널문자 0 포함)를 잘라낸
		              새 문자열을 반환      "a.txt"  (드디어 깨끗한 파일명!)
		*/
		fileName = new String(byteFileName).trim();
		//"a.txt"
		
		System.out.println(fileName.toString());
		
		
		//키보드로 입력 받은 파일명 "a.txt"을 이용해 실제 만들어져 있는 "a.txt"파일에 접근하기 위해
		//파일명을 포함한 경로를 File클래스에서 제공해주는 생성자로 전달해 File클래스의 객체 메모리 생성!
		//작성문법.
		//File    참조변수 = new File("파일이 실제 저장되어 있는 파일명을 포함한 파일 전체 경로");
		File     file    = new File(fileName);
		//				   new File("a.txt");  또는  new File("FileTest01.java"); .....
		
		 //★ 다시 강조 : 이 줄은 파일을 만들거나 여는 것이 아니라
		 //             "경로 파일의 정보를 다루는 객체"를 만든 것뿐이다. 없는 경로여도 성공한다!
		
		// 파일 정보를 출력하기전에 실제로 존재하는 파일인지 먼저 확인한다! (사고 방지)
		//조건 : 실제 파일이 만들어져 있지 않느냐?
		if (  file.exists() == false ) {
			
			//파일이 만들어져 있지 않으면 안내 메세지만 출력
			System.out.println(fileName + " -> 존재하지 않는 파일(경로) 입니다!");
			
			System.out.println("(입력한 상대 경로가 절대 경로로 어떻게 해석되는지 확인 : " + file.getAbsolutePath() + ")");
			
			return; // main 메소드 강제 종료 
		}
		
		//실제 파일이 만들어져 있다면?
		System.out.println(fileName + " 파일 상세 내용 출력 *******************");
		
		//파일이 실제 위치한 전체 경로 얻어 출력
		System.out.println(fileName + " 파일 실제 만들어져 있는 절대 경로 전체  : " +  file.getAbsolutePath() ); //<== 중요
		
		//lastModified() : 파일이 마지막으로 수정된 시각을 "밀리초 정수"로 반환
		//-> 사람이 읽을 수 있는 날짜로 바꾸기 위해 new Date(밀리초) 생성자에 넣어 변환한다
		//   (정확히는 생성일 아니라 "마지막 수정일" 이다!)
		System.out.println(fileName + " 파일 마지막 수정일 : " +  new Date( file.lastModified() ) );
		
		//length()  :  파일 크기를 바이트 단위 정수로 반환
		System.out.println(fileName + "파일 크기 : " + file.length() + "byte");
		
		//getName() : 파일경로를 뺴고 순수한 파일명만 반환  예)  "C:\a\b.txt" -> "b.txt" 
		System.out.println(fileName + "파일 명(실제 만들어져 있는 파일명 만) : " +  file.getName() ); //<== 중요
		
		//canRead() : 이 파일을 읽기 모드로 열어 읽을 수 있는가 ? true / false
		System.out.println(fileName + "파일을 열어 파일의 내용을 읽을 수 있는지에 대한 반환 값 : " + file.canRead());
		
		//isHidden() : 숨김 속성이 설정되어 있는 파일인가? true / false
		System.out.println(fileName + "파일이 현재 숨겨져 있는 숨김 파일인지에 대한 반환 값 : " + file.isHidden());
		
		//getParent() : 이 파일이 들어 있는 부모(상위) 디렉터리 경로 반환
		System.out.println(fileName + "파일이  저장된 부모상위_디렉터리 이름 반환 : " + file.getParent());

	} // main 메소드 

}





