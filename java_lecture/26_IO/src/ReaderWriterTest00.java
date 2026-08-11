import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * [문자 스트림(Character Stream) 정리]
 *
 * 1. 문자 스트림이란?
 * - 자바에서 텍스트(문자)를 다루기 위한 입출력 방식
 * - 내부적으로는 바이트 스트림을 사용하지만,
 *   개발자는 "문자 단위(char, 2byte)"로 편하게 입출력 가능
 * - 유니코드 기반 → 한글 처리에 안전
 *
 * 2. Reader / Writer
 * - Reader  : 문자 입력용 추상 클래스 (읽기)
 * - Writer  : 문자 출력용 추상 클래스 (쓰기)
 * - 바이트 스트림(InputStream / OutputStream)의 문자 버전
 *
 * 3. 주요 Reader 계열
 * - FileReader           : 파일에서 문자 단위로 읽기
 * - InputStreamReader    : 바이트 스트림 → 문자 스트림 변환
 * - BufferedReader       : 버퍼 사용 → 속도 향상, readLine() 제공
 *
 * 4. 주요 Writer 계열
 * - FileWriter            : 파일에 문자 단위로 쓰기
 * - OutputStreamWriter    : 문자 스트림 → 바이트 스트림 변환
 * - BufferedWriter        : 버퍼 사용 → 속도 향상
 * - PrintWriter           : println() 사용 가능 (편의성 ↑)
 *
 * 5. InputStreamReader / OutputStreamWriter의 역할
 * - 바이트 스트림과 문자 스트림을 연결해주는 "다리 역할"
 * - encoding(문자 인코딩 방식)을 지정할 수 있음 (UTF-8 등)
 *
 *   예)
 *   InputStreamReader  : InputStream → Reader
 *   OutputStreamWriter : Writer → OutputStream
 *
 * 6. encoding(인코딩)
 * - 바이트 ↔ 문자 변환 규칙
 * - 잘못 지정하면 한글 깨짐 발생
 * - 실무에서는 UTF-8 사용이 일반적
 *
 * 7. 전체 흐름 요약
 * - 읽기:
 *   파일 → InputStream → InputStreamReader → BufferedReader → 문자 처리
 *
 * - 쓰기:
 *   문자 데이터 → BufferedWriter → OutputStreamWriter → OutputStream → 파일
 *
 * 8. 언제 문자 스트림을 쓰나?
 * - 텍스트 파일(txt, csv, json 등)
 * - 한글/다국어 처리 필요할 때
 * - 웹(Spring MVC, Spring Boot)에서 요청/응답 데이터 처리 시


✅ 바이트 스트림 vs 문자 스트림 한눈 정리

| 구분     | 바이트 스트림 (Byte Stream)        | 문자 스트림 (Character Stream) 
| ------  | ----------------------------   | ------------------------- 
| 처리 단위  | **바이트(byte, 1byte)**          | **문자(char, 2byte)**       
| 기준 클래스 | `InputStream / OutputStream`   | `Reader / Writer`        
| 한글 처리  | ❌ 직접 처리해야 함                 | ✅ 자동 처리 (유니코드)           
| 인코딩 개념 | 직접 신경 써야 함                  | 내부적으로 처리                  
| 주 용도   | 이진 데이터                       | 텍스트 데이터                   
| 예시 파일  | 이미지, 동영상, pdf                | txt, csv, json, html      
| 개발 난이도 | 상대적으로 어려움                    | 상대적으로 쉬움                  
| 실무 사용  | 파일 전송, 업로드                   | 파일 읽기/쓰기, 로그              


📌 대표 클래스 비교 (상속 구조)

바이트 스트림
InputStream
  +-- FileInputStream
  +-- BufferedInputStream
  +-- ObjectInputStream

OutputStream
  +-- FileOutputStream
  +-- BufferedOutputStream
  +-- ObjectOutputStream


문자 스트림
Reader
  +-- FileReader
  +-- InputStreamReader
  +-- BufferedReader

Writer
  +-- FileWriter
  +-- OutputStreamWriter
  +-- BufferedWriter

 */
/*
주제 :  
키보드(System.in) -> 바이트 스트림 -> 문자 스트림으로 변환       -> 문자 단위로 읽어들여서 모니터에 출력하는 예.
	  new BufferedInputstream()-> new InputStreamReader()->
*/
public class ReaderWriterTest00 {

	public static void main(String[] args) throws IOException {
	
		int data = -1;
		//data 변수 설명
		//- 키보드로 부터 입력된 "문자 1개"를 저장할 변수
		//- read() 메소드는   int 타입을 반환하므로 int로 선언
		//- -1은 "더 이상 읽을 데이터가 없다"는 특별한 의미로 사용됨
		
		//========================
		//1. 바이트 스트림 (System.in)
		//==========================
		// 컴퓨터는 문자를 바로 이해하지 못하고 모든 데이터를 숫자(바이트) 형태로 처리한다
		//
		// System.in 은
		// - 키보드와 연결된 입력 통로 이며  1바이트(byte)씩 데이터를 읽어 들이기 위한 바이트 스트림 통로 입니다.
		//예) 
		//   키보드에서 'A' 입력 ->  65(숫자)
		//   키보드에서 '가' 입력 ->  여러 개의 숫자(바이트)
		
		//================================
		//2. 문자 스트림 (InputStreamReader)
		//===============================
		//
		//InputStreamReader는 
		//- 바이트 스트림(System.in)을 문자 스트림으로 바꿔주는 변환기 역할을 하는 클래스 입니다.
		//
		//- new InputStreamReader(System.in); 내부 동작
		//  1. System.in으로 들어오는 바이트들을 읽고
		//  2. 인코딩 규칙(UTF-8 등)에 따라 바이트들을 해석해서 문자(char) 하나로 변환한다.
		//
		//  char란?
		//  - 자바에서 문자 1개를 의미
		//  - 크기는 2바이트
		//
		//  중요한 점!
		//  - char는 항상 2바이트 크기의 문자를 나타내지만
		//  - 실제로 읽은 바이트 수는 문자의 종류에 따라 다르다.
		//  예) UTF-8 기준
		//		'A' -> 1바이트
		//      '가' -> 3바이트 
		//  -> new InputStreamReader(System.in);  한 문자 단위로 데이터를 읽어들이기 위한 입력스트림 만들수 있음 
		//
		//==============================================================================================
		//3. Reader 부모 추상클래스 자료형의 참조변수 선언 후 업캐스팅을 해서 InputStreamReader자식클래스의 객체 저장 가능
		//================================================================================================
		//
		// Reader는 "문자 스트림"의 부모(추상) 클래스이다.
		// InputStreamReader는 Reader를 상속받은 자식클래스이다.
		//
		// 따라서
		// -InputStreamReader 객체를 Reader 타입 변수에 저장할 수 있다.
		//  
		// 이렇게 하면 장점:
		// - 코드가 유연해 짐
		// - 나중에 BufferedReader 등 다른 자식 문자 스트림 객체의 주소로 쉽게 교체 해서 저장할수 있다.				
		Reader myIn    =  new InputStreamReader(System.in);
		
		// 키보드로 입력된 데이터는
		// → 원래는 바이트(숫자) 형태인데
		// → InputStreamReader가 그걸 문자로 번역해 주고
		// → 우리는 문자(char) 단위로 편하게 읽을 수 있다.
		//
		// 즉, myIn 은
		// "키보드 입력을 문자로 읽기 위한 입력 스트림 통로"이다.
		
		while(true) {
			//==================================
			//1. myIn.read()가 호출되면 무슨 일이 일어날까요?
			//==================================
			//
			// 키보드로 입력된 데이터는
			// -> 원래는 바이트(숫자)형태인데
			//    InputStreamReader가 그걸 문자로 번역해 주고
			//    우리는 문자(char) 단위로 편하게 읽어들일수 있다.
			//
			// (1) 사람이 키보드로 글자를 입력한다
			//     예) A,  가, 1,  엔터 등
			//
			// (2) 키보드에서 입력된 값은
			//     "문자" 가 아니라 "숫자(바이트)" 형태로 컴퓨터 안으로 들어온다.
			//     예) A -> 65
			//		   가 -> 여러개 의 숫자(바이트)
			// (3) System.in은
			//	   이 숫자들을 1바이트씩 읽어들이는 통로이다.
			//     -> 이단계까지는 아직 "문자"가 아니다.
			//
			// (4) InputStreamReader는
			//     이 숫자 바이트 데이터들을 모아서 사람이 이해하는 "문자"로 하나씩 바꿔서 읽어들이기 위한 통로 역할을 한다.
			//	   이때 문자 꺠짐 방지를 위해 문자를 처리하는(인코딩)방식의 설정값을 UTF-8로 설정 해준다.
			//
			// (5) 문자 1개가 완성되면  그 문자를 자바의 char 형태로 만든다.
			//	   char는 자바에서 문자 1개를 의미 하며 데이터크기는 2바이트 크기 입니다.
			//
			// (6) read(); 한번 호출하면  그 문자(char) 단위로 데이터 한번 읽어 들입니다.
			//     하지만 read()메소드는 문자 데이터  + 입력종료(-1)를 구분해야 하므로 char가 아닌 int타입으로 값을 반환한다.			
			data = myIn.read();
			
			//=================================
			// 2. data == -1 은 무슨의미 일까요?
			//==================================
			// -1은  "더이상 읽어들일 데이터가 없다"는 신호값이다.
			//
			//키보드 입력에서는
			//- Window : 데이터 입력후 Enter 누르고  Ctrl+Z를 눌러 입력이 끝났다고 알려주면 read()메소드는 -1을 반환한다.
			//- MAC / LINUX : 데이터 입력후 Enter 누르고  Ctrl+D를 눌러 입력이 끝났다고 알려주면 read()메소드는 -1을 반환한다.				
			if(data == -1) {
				break;  //while 무한 반복 종료 해서 InputStreamReader 입력스트림 에서 읽어 들이지 말자!
			}
			
			//==============================
			//3. (char)data 는 왜 필요할까?
			//==============================
			//
			// read()의 결과는 int정수 데이터 이지만
			// 실제 의미는 "문자 1개"의 int정수 값이다.
			// 이 int정수 값(유니코드값)은  다시 사람이 보는 문자로 형변환 해서 출력해야 문자를 모니터 화면에서 볼수 있다.
			System.out.print( (char)data );
					
		}//while
	
	}
	
}
