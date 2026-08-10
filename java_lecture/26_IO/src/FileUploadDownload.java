

import java.io.*;
//java.io 패키지
// - File, FileInputStream, FileOutputStream 클래스 사용을 위해 import

/*
=========================================================
자바 응용프로그램 (FileUploadDownloadExample.class)

- 파일업로드 기능 / 파일다운로드 기능
- try-with-resources 적용 버전
=========================================================

1. 이 프로그램의 본질
    - "업로드" 와 "다운로드" 라는 말은 웹 용어일 뿐
    - 실제로 하는 일은 "파일 복사" 이다.

2. 용어 정리
    - 업로드   : userFile.txt 원본파일을 -> upload 폴더로 복사
    - 다운로드 : upload 폴더의 파일을   -> download 폴더로 복사

=========================================================
3. 이번 버전의 핵심 : try-with-resources 란 무엇인가
=========================================================

    문법 모양
        try ( 스트림 객체 생성 ; 스트림 객체 생성 ) {
            사용하는 코드
        } catch (예외 타입 변수) {
            예외 처리 코드
        }

    동작 규칙 (딱 3가지만 기억)
        규칙1. try 의 소괄호 ( ) 안에서 만든 스트림은
               try 블록이 끝나는 순간 close() 가 자동으로 호출된다.

        규칙2. 중간에 예외가 발생해도 close() 는 반드시 호출된다.
               (기존 버전에서 finally 가 하던 일을 자바가 대신한다)

        규칙3. 소괄호 안에 여러 개를 만들었으면
               "만든 순서의 반대(역순)" 로 닫힌다.
               fis 먼저, fos 나중에 만들었다면 -> fos 먼저 닫히고 fis 가 닫힌다.

    소괄호 안에 넣을 수 있는 조건
        close() 메소드를 가진 클래스만 넣을 수 있다.
        (자바에서는 AutoCloseable 을 구현한 클래스라고 부른다)
        FileInputStream, FileOutputStream 은 이 조건을 만족한다.

=========================================================
4. 기존 버전과의 코드 비교
=========================================================

    [기존 버전 - 직접 닫기]

        FileInputStream fis = null;              <- (1) 미리 null 로 선언
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);   <- (2) try 안에서 생성
            fos = new FileOutputStream(to);
            ... 복사 코드 ...
        } catch (Exception e) {
            ...
        } finally {                              <- (3) finally 에서 직접 닫기
            try {
                if (fis != null) fis.close();    <- (4) null 검사까지 필요
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();             <- (5) close 자체의 예외 처리도 필요
            }
        }

    [새 버전 - 자동 닫기]

        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(to) ) {
            ... 복사 코드 ...
        } catch (Exception e) {
            ...
        }
        <- finally 블록 전체가 사라짐. (1)(3)(4)(5) 가 전부 불필요

    사라진 코드가 하는 일을 자바가 대신하는 것이지,
    닫는 일 자체가 없어진 것이 아니다.

=========================================================
5. 기존 버전에 숨어 있던 위험 (왜 바꾸는가)
=========================================================

    위험1. close() 를 깜빡하면
           스트림이 파일을 계속 붙잡고 있어서(파일 잠김)
           다른 프로그램이 그 파일을 지우거나 수정할 수 없게 된다.

    위험2. finally 안의 close() 순서 실수
           fis.close() 에서 예외가 나면 fos.close() 가 실행되지 않아
           fos 는 영영 닫히지 않는 문제가 생길 수 있다.

    위험3. 코드가 길어질수록 닫아야 할 스트림을 빠뜨리기 쉽다.

    try-with-resources 는 이 세 가지 위험을 문법 차원에서 전부 제거한다.
    그래서 자바 7 부터 도입된 이후 실무 표준 방식이 되었다.
=========================================================
*/
public class FileUploadDownload {

	public static void main(String[] args) {
		//==========================================
		//1. 파일 경로 설정 
		//==========================================
		
		//사용자가 가지고 있다고 가정한 업로드할 원본파일 경로
		String sourceFile = "userFile.txt";
		
		//서버 역할을 하는 업로드 폴더 경로
		String uploadDir = "upload/";
		
		//업로드 완료 후 서버(upload폴더)에 저장될 파일의 전체 경로
		String uploadFile = uploadDir + "userFile.txt";
//						  ="upload/userFile.txt";
		
		//사용자가 업로드된 파일을 내려받은 폴더 경로
		String downloadDir = "download/";
		
		//다운로드 후 사용자에게 보일 파일 전체 경로
		//원본파일명과 구분하기 위해 _copy 를 붙임
		String downloadFile = downloadDir + "userFile_copy.txt";
//							= "download/userFile_copy.txt";
		//=========================================================
		//2. 폴더 생성
		//=========================================================
		//파일은 반드시 폴더 안에 저장되므로 
		//폴더가 없으면 파일 생성 자체가 불가능하다. 그래서 폴더를 먼저 만들어 둔다.
		//
		//File클래스의 mkdirs()  :  폴더가 없으면 생성, 이미 생성되어 있으면? 아무일도 하지 않음
		new File(uploadDir).mkdirs();
		//       "upload/"
		new File(downloadDir).mkdirs();
		//        "download/"
		
		//==========================================================
		//3. 파일 업로드 처리 
		//==========================================================
		System.out.println("파일업로드 시작..........");
		
		//copyFile(원본파일,대상파일)
		//-> 원본 파일의 내용을 읽어서 대상 위치에 그대로 복사
		//-> 복사에 성공하면 true, 실패하면 false 반환
		if(copyFile(sourceFile, uploadFile)   ) {
			  //"userFile.txt", "upload/userFile.txt"
			
			System.out.println("파일 업로드 완료!");
		
		}else {
		
			System.out.println("파일 업로드 실패!");
			
			//업로드 실패 = 서버에 파일이 올라가지 않았다는 의미이므로
			//main 메소드 종료하여 다운로드 기능이 실행되지 않게 한다 
			return;
		}
		
		//=================================================
		//4.파일 다운로드 처리
		//===============================================		
		//...............
		
		

	} // main
	
	//=================================================================
	//파일 복사 메소드  -  try-with-resources 적용
	//=================================================================
	//-> from 매개변수로 전달 받은 경로의 파일의  to 매개변수로 전달받은 전체경로의 파일 그대로 복사 한다
	
	 						  //"userFile.txt", "upload/userFile.txt"
	public static boolean copyFile(String from,  String to) {
		
		//1. 원본 파일이 실제로 존재하는지 확인
		// -> 컴퓨터는 저장되어 있지 않은 파일의 정보를 읽어들일수 없다.
		//    그래서 복사 전에 반드시 파일이 있는지 확인해야 한다.
		
		//File객체  = 파일 정보에 접근하기 위한 객체 
		File source = new File(from);
		
		//exists() : 해당 위치에 파일이 실재로 존재하면 true, 없으면 false
		if(!source.exists()) {
			//업로드할 원본파일 "userFile.txt"이 존재 하지 않느냐?
			
			System.out.println("원본 업로드할 파일이 존재하지 않습니다 : " + from);
			
			//자바프로그램이 기준으로 삼는 실제 실행 위치를 출력
			System.out.println("실행 위치 : " + source.getAbsolutePath());
			
			//원본파일이 없으므로 복사 실패
			return false;
		}
		
		
		try(FileInputStream fis = new FileInputStream(source);  
			FileOutputStream fos = new FileOutputStream(to)) {
		
			//2. 원본 파일의 내용을 읽어 들여   새로운 파일에 기록 (실제 복사)	
			byte[] buffer = new byte[1024];  //원본 파일 데이터를 한 번 읽어들일때 담을 배열
											 //JVM의 heap 영역에 1024칸짜리 byte 배열이 만들어진다
			
			//반복문에서 실제로 읽어온 데이터 크기를 저장할 변수 
			int readCount;
			
            //fis.read(buffer)
            //  원본에서 바이트를 읽어 buffer 에 담고, 실제 읽은 개수를 반환
            //  더 읽을 것이 없으면 -1 을 반환
			while( (readCount = fis.read(buffer)) != -1 ) {
				
                // fos.write(buffer, 0, readCount)
                //   buffer 배열의 0번 인덱스부터 readCount 개수만큼만 파일에 쓴다
                //
                //   readCount 가 중요한 이유
                //     buffer 크기 1024, 마지막에 실제 읽은 크기 213 이라면
                //     1024를 다 쓰면 이전 회차의 찌꺼기 데이터까지 파일에 들어간다
				fos.write(buffer, 0, readCount);
				
			} //while
			
			//while 종류 후  = 파일 끝(-1)을 만남  = 전체 복사 완료를 알리기 위해 true 반환
			return true;
			
		}catch (Exception e) {
			
			//3. 복사 도중 예외 발생 시!
			//-> 파일이 갑자기 삭제 되거나, 파일 권한 문제 등이 발생할수 있다.
			//
            //  중요 : 예외가 나서 이 catch 로 넘어올 때도
            //         fis, fos 는 이미 자동으로 닫힌 상태다.
            //         (규칙2 : 예외가 발생해도 close 는 반드시 실행된다)
			System.out.println("파일 처리 중 오류 발생");
			
			//개발자가 원인을 확인할 수 있도록 예외 상세 정보 출력
			e.printStackTrace();
			
			return false; //원본파일 복사 실패
		}
		
	} //---------------- copyFile 메소드 끝
	

} //class ---------









