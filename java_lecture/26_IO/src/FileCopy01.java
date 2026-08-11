
/*
 주제 :  파일복제프로그램(FileCopy.class)을 업그레이드 시키되
 		BufferedInputStream 입력 보조 스트림 통로와
 		BufferedOutputStream 출력 보조 스트림 통로의 내부 버퍼메모리를 사용한 예
*/
import java.io.BufferedInputStream;  //입력 보조 스트림
import java.io.BufferedOutputStream; //출력 보조 스트림 

import java.io.FileInputStream;  //파일 입력 스트림
import java.io.FileOutputStream; //파일 출력 스트림

import java.io.File;  //파일 정보 조작 클래스 


public class FileCopy01 {

	//입출력 할 데이터를 수용할 만큼의 버퍼메모리 크기 설정
	final static int BUFFER_SIZE = 256;
	
	public static void main(String[] args) {
		//윈도우 명령 프롬포터 창에 입력될 구문
		//java FileCopy01 "원본파일명" "복사될새파일명"
		//					  0           1       index
		
		int i = 0;  //"원본파일"에서 한번 읽어들일때마다 읽어들인 바이트 크기의 데이터를 저장할 변수 
		int len = 0; //"원본파일"에서  읽어 들인 총 바이트크기를 저장할 변수
		
		//"원본파일"에서 읽어들인 데이터를 저장하거나  내보낼 크기의 byte 배열 생성
		byte[] buffer = new byte[BUFFER_SIZE];  //256byte
		
		//원도우 명령 프롬포터 창에 "원본파일명" 만 입력하고 "복사될새파일명"을 입력하지 않은 경우
		if(args.length  < 2) {
			
			//입력될 구문을 사용자에게 알리자
			System.out.println("java FileCopy01 원본파일명 복살될새파일명");
			
			//FileCopy01.class 복사 프로그램 종료
			System.exit(0);
		}
		
		try {
		
		//BufferedInputStream 보조 입력 스트림 객체 생성 방법			
			//순서1. "원본파일"의 데이터를 1바이트 단위로 접근해서 읽어들일 입력 스트림 통로 생성
			FileInputStream fis = new FileInputStream( new File( args[0] ) );
			
			//순서2. FileInputStream 입력스트림을 업그레이드 해서
			//     "원본파일"의 데이터를 우리가 지정한 256byte 단위로 접근해서 한번에 읽어들이기 위한
			//     BufferedInputStream 보조 입력스트림 객체 생성
			//   참고. BufferedInputStream 객체 메모리 내부에는  내부 버퍼메모리(512byte공간)에 저장해 두었다가
			//		  512byte 크기의 데이터를 한번에 읽어들 읽수 있는 보조 입력스트림 입니다.	
			BufferedInputStream    bis = new BufferedInputStream(fis);
			
		//BufferedOutputStream 보조 출력 스트림 객체 생성 방법		
			//순서1. 순서2. "복사될새파일"에 BufferedInputStream 입력 보조 스트림 통로로 부터 읽어들인 데이터들을
			//			   1바이트 단위로 내보내서 출력하기 위한 FileOutputStream 출력 스트림 통로 객체 생성 후 
			//			   업그레이드 해서~ 내부 버퍼 메모리 공간 512바이트에 모아 두었다가 
			//			   512 바이트 크기의 데이터 단위로 출력하기 위한 출력 보조 스트림 통로 BufferedOutStream 객체 생성
			BufferedOutputStream  bos = new BufferedOutputStream(  new FileOutputStream(new File(args[1])) ); 
			
			// --------------------------------------------------------------
			// [통로 전체 구조 모델] 이 프로그램에서 데이터가 지나가는 길 (전부 연결하면)
			//
			//   [디스크 원본파일]                                  [디스크 복사될새파일]
			//     |                                                       ^
			//     v                                                       |
			//   FileInputStream (속 통로)                    FileOutputStream (속 통로)
			//     | 1바이트 단위 접근                                      ^
			//     v                                                       |
			//   BufferedInputStream (겉 통로)               BufferedOutputStream (겉 통로)
			//   내부 버퍼메모리 512byte (Heap)               내부 버퍼메모리 512byte (Heap)
			//     |                                                       ^
			//     |          [Heap]  byte[] buffer (256칸)                |
			//     +-- read(buffer) --> | 65 66 67 ... | -- write(buffer) -+
			//
			//   요약 : 원본 -> 입력 겉통로의 버퍼 -> buffer 배열 -> 출력 겉통로의 버퍼 -> 새파일		
			
			/*
			  read(byte[] b) 메소드 사용
			  
					위 작성 해 놓은 byte[] buffer = new byte[256]; 배열을 read 메소드를 호출 할때 매개변수로 전달하면
					byte[] buffer = new byte[256]; 배열 크기 만큼 "원본파일"의 전체 데이터에서 읽어와
					byte[] buffer = new byte[256]; 배열의 각 칸에 저장 시킵니다.
					그리고 한번 읽어 들인 256byte 크기 만큼의 데이터를 정수로 반환 해 줍니다
					BufferedInputStream보조 입력 스트림에서 더이상 읽어 들일 데이터가 없으면? read 메소드는 -1 을 반환합니다.			
			*/	
			while(  (i = bis.read(buffer))  != -1  ) {
				/*
			    buffer 배열에 들어 있는 데이터 중 "이번에 읽은 만큼(i바이트)만"
		        BufferedOutputStream 내부 버퍼메모리에 저장한다
		        
		        [주의! write(buffer) 가 아니라 write(buffer, 0, i) 인 이유]
		        
		          마지막 회차에는 buffer(256칸)가 다 채워지지 않는다.
		          예) 600바이트 파일의 3회차 : 88바이트만 새로 담김
		        
		          buffer 상태 : | 새로 읽은 88바이트 | 지난 회차의 찌꺼기 168바이트
		                        +--------------------+----------------------------
		                          0 ~ 87               88 ~ 255
		        
		          write(buffer)       : 256바이트 전부 내보냄 --> 찌꺼기까지 복사되어 파일 깨짐
		          write(buffer, 0, i) : 0번부터 i개만 내보냄  --> 정확히 원본과 같은 복사본
				*/				
				bos.write(buffer, 0, i);
				
				/*
				 BufferedOutputStream 내부 버퍼메모리에 모여 있는 데이터를
    			 강제로 파일로 내보낸다
				 */
				bos.flush();
				
				//256바이트 씩 읽어들인 바이트수를 len변수에 누적
				len += i;
				
				System.out.println("process : read[" + i + "," + len + "], avail[" + bis.available() + "]");
				
			} // while 반복문
			
			//자원해제 (스트림 통로 메모리들을 모두 사용하였으면 JVM메모리의 heap 영역에서 객체 제거)
			bis.close();   bos.close();
			
			System.out.println(len + " bytes are copied................");
			
		}catch (Exception e) {
			e.printStackTrace();  //예외가 발생하면 출력
		}
	
	} // main

} // class





