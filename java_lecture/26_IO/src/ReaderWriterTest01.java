

/*
 주제 : 키보드로 입력한 전체 데이터 중에서 "한 줄 단위"로 읽어서 그 결과를 모니터에 출력하는 예제 

 이 예제의 핵심 목적
  - 왜 BufferedReader 를 쓰는지 이해
  - readLine() 이 정확히 무엇을 읽는지 이해 
  - 문자 스트림의 실제 사용 방법 이해 

*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderWriterTest01 {
	public static void main(String[] args) throws IOException {
		
		//1. 키보드로 파일명을 입력받아 저장할 변수 선언
		String fileName = null;

		//2. 문자 입력 스트림 통로 BufferedReader 객체 생성
		// 이 한 줄에는 3단계 작업이 동시에 들어 있다.
		//
		//[1단계] System.in
		//-  키보드와 연결된 입력 스트림 BufferedInputStream 객체 얻기 
		//   1바이트(byte) 단위로 데이터를 읽는 입력 스트림 통로 입니다.
		//
		//[2단계] InputStreamReader
		//-  바이트 스트림(System.in->BufferedInputStream객체)을  문자 스트림(InputStreamReader객체)로 만든다.
		//-  2바이트(byte)  한 문자 단위로 데이터를 읽는 입력 스트림 통로 입니다.
		//
		//[3단계] BufferedReader
		//- 문자 스트림(InputStreamReader객체)를 BufferedReader 입력 스트림으로 만든다.
		//- BufferedReader 입력 스트림 통로 객체는 한 줄 단위로 읽을수 있게 해준다.
		//  내부 버퍼(임시 저장 공간)를 사용하여 읽기 속도를 빠르게 해준다.
		//
		//  즉, 이 통로의 역할은:
		//		-> 키보드 입력을 문자로 바꾸고, 엔터 기준으로 한 줄씩 읽어오는 통로"
		BufferedReader br = new BufferedReader(new InputStreamReader( System.in ));
		// --------------------------------------------------------------
		// [3중 통로 구조 모델] 위 한 줄이 만든 포장 구조 (안쪽부터 바깥쪽으로)
		//
		//   [키보드]
		//     |  바이트 단위 (숫자)
		//     v
		//   System.in ------------------ 1단계 : 바이트 통로
		//     |
		//     v
		//   InputStreamReader ---------- 2단계 : 바이트 -> 문자 변환 통로
		//     |  문자 단위 (char)
		//     v
		//   BufferedReader ------------- 3단계 : 문자를 내부 버퍼에 모아
		//     |  줄 단위 (String)                 엔터 기준 한 줄로 완성
		//     v
		//   br.readLine() 이 String 반환
		//
		//   [Stack]           [Heap]
		//   br (참조) ----->  BufferedReader 객체
		//                       > 내부에 InputStreamReader 를 품고
		//                         그 안에 System.in 을 품은 3중 구조
		//
		//   단계가 올라갈수록 다루는 단위가 커진다 : 바이트 -> 문자 -> 한 줄
		// --------------------------------------------------------------		
		
		//3. 사용자에게 입력 안내 메세지 출력해서 보여주자
		System.out.print("파일 이름을 입력하세요 : ");
		
		//4. 한 줄 단위로 입력한 데이터를 BufferedReader 입력스트림 통로를 통해 읽어 들이자
		//
		// readLine() 메소드의 동작방식
		// 
		// 1. 사용자가 키보드로 문자(파일명)를 입력한다.
		// 2. 엔터(Enter)를 누를떄까지 기다린다.
		// 3. 엔터 전까지 입력한 모든 문자를  하나의 문자열로 만들어 반환한다.
		//
		// 예)
		//    입력 : hello.txt + Enter
		//    반환 : "hello.txt"	
		//
		// [readLine 동작 모델] "test.txt" 입력 + 엔터를 눌렀을 때
		//
		//   BufferedReader 내부 버퍼 (문자가 차례로 쌓임)
		//     t  e  s  t  .  t  x  t  (엔터)
		//   +--------------------------+
		//   | 엔터를 만나는 순간           |
		//   | 앞의 문자들을 묶어           |  --> [Heap] String "test.txt" 생성
		//   | String 으로 완성          |  --> [Stack] fileName 이 참조
		//   +--------------------------+
		//
		//   엔터 문자 자체는 버려지고 문자열에는 포함되지 않는다
		fileName = br.readLine();
		/*
		키보드 입력
		 → 바이트
		 → 문자로 변환
		 → 버퍼에 임시 저장
		 → 엔터를 만나면 한 줄 완성
		 → String으로 반환
		*/
		//5. 입력한 결과 확인 용도 출력
		//사용자가 키보드로 입력한 파일 이름을 화면에 다시 출력하여 확인
		System.out.println("입력한 파일명 : " + fileName);
		
		//6. 스트림 통로 닫기 (매우 중요!)
		//
		//BufferedReader 스트림 통로 사용이 끝났으므로 반드시 !!! close() 메소드를 호출하여 메모리 자원 제거
		//
		//close()를 호출하면  내부에 연결된 InputStreamReader와 System.in까지 함께 정리 된다.
		br.close();		
	}

}
/*
이 예제에서 꼭 가져가야할 핵심 3가지

BufferedReader에서 제공 해주는 메소드 
1. read()  -> 문자 1개씩 읽음
2. readLine() ->  한 줄 전체를 문자열로 얻음 
3. BufferedReader가 있어야 readLine() 사용 가능 

한 줄 최종 요약
 - BufferedReader는 키보드 입력을 “엔터 기준 한 줄”로 읽기 위해 사용한다.

*/





