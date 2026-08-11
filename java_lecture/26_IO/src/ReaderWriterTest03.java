
import java.io.*;
/*
	io 패키지
	- Input / Output (입력 / 출력) 관련 클래스들이 모여 있는 패키지
	- BufferedReader, BufferedWriter, FileWriter, InputSreamReader 클래스등이 포함되어 있는 패키지
*/
public class ReaderWriterTest03 {

	public static void main(String[] args) throws IOException {
		// 1. 변수 선언
		// - 사용자가 키보드로 입력한 "파일이름"을 저장할 변수
		String fileName = null;
		
		// - 파일에 저장할 내용을 "한 줄씩"읽어서 임시로 담아둘 변수
		String buf = null;
		
		// 2. 키보드로 부터 입력받은 데이터를 한줄 씩 읽어서 저장할 BufferedReader 입력 스트림 생성
		BufferedReader keyBr = new BufferedReader( new InputStreamReader( System.in ) );
		
		// 3. 파일 이름 입력 유도
		System.out.print("파일 이름을 입력하세요 -> ");
		
		// 사용자가 키보드로 입력하고 엔터를 누를 때 까지 기다린 후 입력한 내용을 한줄(String)로 읽어옴
		fileName = keyBr.readLine();
		
		// 4. 입력 받은 파일에 저장할 '내용' 입력을 유도
		System.out.println("파일에 기록할 내용을 입력하세요.");
		
		// 5. 파일에 저장(출력, 기록)용 출력스트림 통로 -> BufferedWriter 객체 생성
		BufferedWriter fileBw = new BufferedWriter( new FileWriter(fileName) );
		
		// 6. "키보드로 부터 입력 받은 데이터 읽어서 -> 파일에 저장" 이걸 반복 처리
		
		while ( (buf = keyBr.readLine()) != null ) {
			// 7. 파일에 한 줄 씩 기록
			fileBw.write(buf);
			fileBw.newLine();
		}
		
		// 8. 사용이 끝난 스트림 통로 닫기 (아주 중요)
		keyBr.close();
		fileBw.close();
	}

}
