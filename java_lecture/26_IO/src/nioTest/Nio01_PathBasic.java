package nioTest;



/*
 * ==================================================================
 * [NIO 예제 1] Path 객체 - 경로 객체를 만들고 파일을 생성한다
 * ==================================================================
 *
 * ------------------------------------------------------------------
 * 1. 용어 정리 (이 예제에 나오는 자바 용어)
 * ------------------------------------------------------------------
 *
 *   객체        : new 등으로 만들어져 Heap 메모리에 올라간 데이터 덩어리
 *   참조        : 객체가 Heap 의 어디에 있는지 가리키는 값. 변수에 담긴다
 *   Stack       : 메서드 안의 변수(참조 포함)가 쌓이는 메모리 구역
 *   Heap        : 객체가 실제로 만들어져 저장되는 메모리 구역
 *   static 메서드 : 객체를 만들지 않고 클래스이름.메서드() 로 바로 호출하는 메서드
 *   예외        : 실행 중 발생하는 오류. 처리하지 않으면 프로그램이 멈춘다
 *
 *
 * ------------------------------------------------------------------
 * 2. Path 클래스와 Files 클래스의 역할 구분
 * ------------------------------------------------------------------
 *
 *   Path 객체
 *     경로 문자열을 담고 있는 객체다. Heap 에 만들어진다.
 *     경로를 담고 있을 뿐, 디스크에 파일을 만들지는 않는다.
 *
 *   Files 클래스
 *     디스크에 실제 작업(확인, 생성, 삭제)을 실행하는 클래스다.
 *     메서드가 전부 static 이라서 객체를 만들지 않고
 *     Files.exists(...) 처럼 클래스 이름으로 바로 호출한다.
 *     그래서 Files 는 Heap 에 객체가 생기지 않는다.
 *
 *
 * ------------------------------------------------------------------
 * 2-1. IO(java.io.File) 와의 비교 - 같은 일을 하는 옛날 방식
 * ------------------------------------------------------------------
 *
 *   IO 패키지에는 File 이라는 클래스가 있고,
 *   NIO 의 Path + Files 가 하는 일을 File 객체 하나가 전부 했다.
 *
 *   [역할 분담 구조 비교]
 *
 *     IO  방식 : File 객체 하나가 두 역할을 겸한다
 *
 *       [Heap]
 *       File 객체 "data"  ---- 경로 보관       (Path 의 역할)
 *                         ---- 디스크 확인/생성 (Files 의 역할)
 *
 *     NIO 방식 : 역할이 두 클래스로 나뉜다
 *
 *       [Heap]
 *       Path 객체 "data"  ---- 경로 보관만 한다
 *       (Files 는 static) ---- 디스크 작업만 한다. Heap 에 객체 없음
 *
 *   [이 예제의 NIO 코드와 IO 대응 코드]
 *
 *     NIO (이 예제)              | IO (java.io.File 방식)
 *     ---------------------------+----------------------------
 *     Path.of("data")            | new File("data")
 *     Files.exists(p)            | f.exists()
 *     Files.createDirectory(p)   | f.mkdir()
 *     Files.createFile(p)        | f.createNewFile()
 *     Files.isDirectory(p)       | f.isDirectory()
 *     Files.isRegularFile(p)     | f.isFile()
 *     Files.size(p)              | f.length()
 *
 *   [실패를 알려 주는 방식의 차이 - NIO 로 바뀐 가장 큰 이유]
 *
 *     IO  : f.mkdir() 이 실패하면 false 만 반환한다.
 *           왜 실패했는지(권한 부족? 상위 폴더 없음?)는 알 수 없다.
 *           
 *           
 *     NIO : Files.createDirectory(p) 가 실패하면 예외가 발생하고
 *           예외 종류가 원인을 알려 준다.
 *             FileAlreadyExistsException --> 이미 있어서 실패
 *             NoSuchFileException        --> 상위 폴더가 없어서 실패
 *
 *
 * ------------------------------------------------------------------
 * 3. 시작 시점의 메모리 텍스트 모델링
 * ------------------------------------------------------------------
 *
 *   [Stack]                  [Heap]                  [디스크]
 *   main() 영역              (비어 있음)             (data 폴더 없음)
 *     (변수 아직 없음)
 *
 *   프로그램이 진행되면서 이 세 구역이 어떻게 변하는지
 *   각 단계마다 모델링으로 보여 준다.
 *
 *
 * ------------------------------------------------------------------
 * 4. 실행 순서
 * ------------------------------------------------------------------
 *
 *   [1] Path 객체 2개를 Heap 에 만든다
 *   [2] Path 객체에서 경로 정보를 꺼낸다
 *   [3] Files.exists 로 디스크를 확인한다      --> 처음에는 false
 *   [4] Files.create... 로 디스크에 실제 생성한다
 *   [5] 다시 확인한다                          --> 이번에는 true
 * ==================================================================
 */


import java.nio.file.Files;  //파일 작업(생성, 확인, 삭제)을 실행하는 클래스.  메서가 전부 static 이다.
import java.nio.file.Path;  //파일이나 폴더의 경로를 저장하는 객체의 부모인터페이스 타입 
import java.io.File;
import java.io.IOException; // 파일 입출력 작업 실패 시 발생하는 예외정보를 제공하는 클래스 

public class Nio01_PathBasic {

	public static void main(String[] args) throws IOException {
		//============================================
		//[1]  Path 객체 만들기 
		//============================================
		System.out.println("====== [1] Path 객체 만들기 ====== ");
		
        // Path.of("data")
        //   경로 문자열 "data" 를 담은 Path 객체를 Heap 에 만들고
        //   그 참조를 dirPath 변수(Stack)에 저장한다.
        //
        //   of() 는 static 메서드다. 그래서 Path.of() 로 바로 호출한다.
        //   (Path 는 new 로 만들 수 없도록 설계되어 있다)
        //
        //   중요 : 이 줄이 실행되어도 디스크에 data 폴더는 생기지 않는다.
        //          Heap 에 경로 객체가 생길 뿐이다.
		Path dirPath = Path.of("data");
		
		
        // Path.of("data", "memo.txt")
        //   경로를 조각으로 나눠 넘기면 조각 사이의 구분 문자를
        //   운영체제에 맞게 자동으로 넣어 준다.
        //     윈도우 : data\memo.txt
        //     리눅스 : data/memo.txt
        //   운영체제가 달라도 코드를 고칠 필요가 없다.
		Path filePath = Path.of("data", "memo.txt");
		
		//Path 객체를 println 에 넣으면 toString() 메소드가 자동으로 호출되어 보관하고 있는 경로를 문자열로 반환해 출력해줍니다
		System.out.println("폴더 경로 : " +  dirPath.toString() );  //출력 :   data
		System.out.println("파일 경로 : " +  filePath.toString() ); //출력 :   data\memo.txt
		
		System.out.println(); //빈 줄 출력 
		
		//==============================================
		//[2]  Path 객체에서 경로 정보 꺼내기 
		//
		//아래 메서드들은 전부 Path 객체의 메서드이다.
		//JVM의 HEAP 에 있는 경로 문자열만 분석해서 답을 준다.
		//하드디스크에 보관된 실제 파일이 없어도 동작 한다.
		//=============================================
		System.out.println("====== [2] Path객체에서 경로 정보 꺼내기 ======");
		
		//getFileName() : 경로의 마지막 조각을 반환한다.
		// "data/memo.txt" 의 마지막 조각은 "memo.txt"
		// 반환 타입이 String이 아니라 Path 부모인터페이스 라는 것에 주의한다
		System.out.println("파일명 : " + filePath.getFileName() );    // 출력 :   memo.txt
		
		//getParent() : 마지막 조각을 뺸 나머지 경로를 반환한다.
		//  "data/memo.txt" 에서 마지막을 빼면 "data"
		System.out.println("상위 폴더 : " + filePath.getParent() );    // 출력 :   data
		
		// toAbsolutePath() : 상대 경로를 절대 경로로 바꿔서 반환한다
	    //   상대 경로 : 프로그램 실행 위치를 기준으로 적은 경로. data/memo.txt
        //   절대 경로 : 드라이브(또는 루트)부터 전부 적은 경로.
        //               예) C:\work\myproject\data\memo.txt
		System.out.println("절대 경로 : " + filePath.toAbsolutePath());
		
		//getNameCount() : 경로의 조각 개수를 int 로 반환한다.
		//  data 조각  + memo.txt 조각  ==>>>>>> 2개 
		System.out.println("경로 조각 수 : " + filePath.getNameCount());  // 출력 : 2
		
		//getName(인덱스) : 해당 엔덱스의 조각을 반환한다
		// 인덱스는 배열과 같이 0 부터 시작한다
		//"data/memo.txt"
		//  0     1       index
		System.out.println("0번 조각 : " + filePath.getName(0));  //출력 : data
		System.out.println("1번 조각 : " + filePath.getName(1));  //출력 : memo.txt
		
		System.out.println();

		//==============================================
		//[3] 하드디스크에 실제로 파일 및 폴더가 존재하는지 확인 (생성 전)
		//
		//여기서 부터 Files 클래스를 사용한다.
		//Files 클래스의 메서드는 하드디스크에 접근해서 실제 상태를 확인한다.
		//=====================================================
		System.out.println("====== [3] 실제 존재 여부 확인 (생성 전) =======");
		
		//Files.exists(Path 객체)
		//-> 전달받은 Path 객체에 보관된 경로가 하드디스크에 실제로 존재하면 true, 없으면 false 를 반환 한다
        //
        //   첫 번째 실행 : 아직 안 만들었으므로 둘 다 false
		//	 두 번째 실행 : 이전 실행에서 만든 것이 디스크에 남아 있으므로 true
		System.out.println("data 폴더 존재?      :  " +  Files.exists(dirPath)  );
		System.out.println("memo.txt 존재?      :  " +  Files.exists(filePath) );
		System.out.println();
		
		//====================================================================
		//[4] 하드디스크에 실제로 파일 및 폴더 생성 하기
		//
		// 주의 : 이미 존재하는 것을 또 만들면 
		//       FileAlreadExistsException 예외가 발생하며  자바프로그램 전체는 멈춘다.
		//       그래서 만들기 전에 exists 메소드로 반드시 확인한다.
		//===================================================================
		
        // --------------------------------------------------------------
        // [IO vs NIO 비교] 생성 명령이 디스크로 가는 길과 실패 통보의 길
        // --------------------------------------------------------------
        //
        //   IO 방식 (java.io.File)
        //
        //     File f = new File("data");
        //     boolean ok = f.mkdir();
        //
        //     [프로그램] -- 생성 명령 --> [디스크]
        //     [프로그램] <-- true/false 만 돌아옴
        //                    ^
        //                    실패해도 false 하나뿐. 원인은 못 받는다
        //
        //   NIO 방식 (이 예제)
        //
        //     Files.createDirectory(dirPath);
        //
        //     [프로그램] -- 생성 명령 --> [디스크]
        //     [프로그램] <-- 성공 : 조용히 다음 줄로 진행
        //                <-- 실패 : 원인이 담긴 예외가 날아온다
        //                           FileAlreadyExistsException = 이미 있음
        //                           NoSuchFileException        = 상위 폴더 없음
        //
        //   그래서 IO 는 실패 시 if (ok == false) 로 짐작만 했지만,
        //   NIO 는 예외 종류를 보고 정확한 원인 대응 코드를 짤 수 있다.
        // --------------------------------------------------------------
		
		
        // ! 연산자 : boolean 값을 반대로 뒤집는다
        //   Files.exists(...) 가 false 이면 !Files.exists(...) 는 true
        //   그래서 if (!Files.exists(...)) 는 "존재하지 않으면" 이라는 조건이 된다
		if(!Files.exists(dirPath)) {
			
			//Files.createDirectory(Path 객체)
			//-  매개변수로 전달 받은 Path 객체의 경로에 폴더를 하드디스크에 실제로 만든다.
			//-  주의 : 상위 폴더가 먼저 만들어져 있어야 한다
			//         상위 폴더가 없으면 NoSuchFileException 예외가 발생한다.
			Files.createDirectory(dirPath);
			System.out.println("data 폴더 생성 완료");
			
		}else {
			System.out.println("data 폴더는 이미지 존재함 (생성 하지 말자)");
		}
		
		
		if(!Files.exists(filePath)) { //"data/memo.txt" 파일이 만들어져 있지 않느냐?
			
			//Files.createFile(Path 객체)
			//- 매개변수로 전달 받은 Path 객체의 경로에 크기 0byte인 빈 파일을 하드디스크에 실제로 만든다
			//- 주의 : data 폴더가 먼저 만들어져 있어야 한다
			//		  그래서 폴더 생성 코드가 이 코드 줄보다 위에 작성되어 있다.
			Files.createFile(filePath);
			System.out.println("memo.txt 생성 완료");
			
		}else {
			System.out.println("memo.txt 는 이미 존재함 (생성 하지 말자)");
		}
		
		System.out.println();
		
		//============================================================
		//[5] 생성 후 상태 다시 확인
		//===========================================================
		System.out.println("===== [5] 생성 후 상태 확인 ======");
		
		//[3]에서 false였던 값이 지금은 true 로 나온다
		System.out.println("memo.txt 존재?   : " + Files.exists(filePath)); //true
		
		//Files.isDirectory(Path 객체)  :  해당 경로가 폴더이면 true 를 반환한다.
		System.out.println("data는 폴더인가?  : " + Files.isDirectory(dirPath) );    //true
		System.out.println("memo.txt는 폴더인가? : " + Files.isDirectory(filePath)); //false
		
		//Files.isRegularFile(Path 객체) : 해당 경로가 일반 파일이면 true 를 반환한다.
		System.out.println("memo.txt는 파일인가? : " + Files.isRegularFile(filePath));//true
		
		//Files.size(Path 객체) :  파일 크기를 바이트 단위 long 값으로 반환한다
		// 바이트 : 컴퓨터가 데이터 크기를 세는 기본 단위
		// long :  int 보다 큰 정수를 담은 타입.  2GB 넘는 파일도 표현하기 위해서 이다.
		//방금 만든 빈 파일이므로 0 이 나온다
		System.out.println("memo.txt 크기     : " + Files.size(filePath) + " bytes");  // 0 bytes
		
		//Files.isReadable메소드  /  Files.isWritable 메소드
		//  이 프로그램이 해당 파일을 읽을 수 있는지  /  쓸 수 있는지 의 boolean 값 하나를 반환해 줍니다.
		//  운영체제의 권한 설정에 따라 false 가 나올 수 있다.
		System.out.println("읽기 가능?      : " + Files.isReadable(filePath));  //true
		System.out.println("쓰기 가능?      : " + Files.isWritable(filePath));  //true
		System.out.println();
		
		//===================================================================
		//[정리]
		//====================================================================
		System.out.println("====== [정리] =======");
		System.out.println("1. Path.of메소드는 JVM의 HEAP영역에 경로 객체를 만들 뿐,  하드디스크는 안 바뀐다.");
		System.out.println("2. 디스크를 확인하고 바꾸는 것은 전부 Files클래스의 static 메소드다.");
		System.out.println("3. 파일이나 폴더 생성전 Files클래스의 exists()메소드 실행하면 false, 생성 후에는 true 다.");
		System.out.println("4. exists() 확인 없이  create..()메소드를 호출하여 생성하면  두번째 실행에서 예외로 멈춘다.");
		
		
		
	}

}











