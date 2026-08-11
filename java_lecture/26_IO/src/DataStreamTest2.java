

/**
 * DataInputStream 입력스트림 클래스 설명 
 *
 * ------------------------------------------------------------
 * 1. DataInputStream이란?
 * ------------------------------------------------------------
 *
 * DataInputStream은
 * 파일이나 입력 장치에 저장된 데이터를
 * 자바의 기본자료형 형태 그대로
 * 다시 읽어오기 위해 사용하는 입력스트림 클래스입니다.
 *
 * 쉽게 말해,
 * DataOutputStream으로 저장해 둔 데이터를
 * "원래 자료형 그대로 복원해 주는 역할"을 합니다.
 *
 *
 * ------------------------------------------------------------
 * 2. 왜 DataInputStream이 필요한가?
 * ------------------------------------------------------------
 *
 * 파일에 저장된 데이터는
 * 실제로는 모두 숫자(바이트) 형태입니다.
 *
 * FileInputStream으로도 읽을 수는 있지만,
 * 그러면 개발자가 직접
 * 바이트를 다시 조합해서
 * int, double, float 같은 값으로 만들어야 합니다.
 *
 * DataInputStream을 사용하면
 * ✔ readInt()  → int 값 그대로 읽기
 * ✔ readDouble() → double 값 그대로 읽기
 * ✔ readChar() → char 그대로 읽기
 *
 * 처럼 아주 쉽게 데이터를 복원할 수 있습니다.
 *
 *
 * ------------------------------------------------------------
 * 3. DataInputStream은 반드시 짝이 있다
 * ------------------------------------------------------------
 *
 * DataInputStream은
 * 반드시 DataOutputStream과 함께 사용해야 합니다.
 *
 * 그리고 아주 중요한 규칙이 있습니다.
 *
 * ✔ 데이터를 쓴 순서와
 * ✔ 데이터를 읽는 순서가
 * ✔ 자료형까지 완전히 같아야 합니다.
 *
 * 예)
 * writeChar → readChar
 * writeByte → readByte
 * writeShort → readShort
 * writeInt → readInt
 * writeFloat → readFloat
 * writeDouble → readDouble
 * writeUTF → readUTF
 *
 * 이 순서가 하나라도 틀리면
 * 값이 깨지거나
 * 전혀 엉뚱한 값이 읽히게 됩니다.
 *
 *
 * ------------------------------------------------------------
 * 4. 이 코드가 동작하는 이유
 * ------------------------------------------------------------
 *
 * 이 프로그램은
 * 이전 예제(DataOutputStream)에서
 * data.txt 파일에 저장해 둔 데이터를
 * 같은 순서, 같은 자료형으로 읽어옵니다.
 *
 * 그래서
 * char c = 'a'
 * byte b = 10
 * short s = 20
 * int i = 100
 * float f = 3.14f
 * double d = 1.5
 * String str = "hello"
 *
 * 이 값들이
 * 정확하게 그대로 복원되어 출력됩니다.
 *
 *
 * ------------------------------------------------------------
 * 5. 파일을 메모장으로 보면 깨지는 이유
 * ------------------------------------------------------------
 *
 * data.txt 파일은
 * 사람이 읽기 위한 파일이 아니라
 * 프로그램이 읽기 위한 이진 데이터 파일입니다.
 *
 * 그래서 메모장으로 열면
 * 글자가 깨져 보이는 것이 정상이며,
 * 오류가 아닙니다.
 *
 *
 * ------------------------------------------------------------
 * 6. 언제 사용하는가?
 * ------------------------------------------------------------
 *
 * DataInputStream은
 * ✔ 설정 파일 읽기
 * ✔ 점수, 수치 데이터 읽기
 * ✔ 프로그램 상태 복원
 *
 * 처럼
 * "프로그램이 저장해 둔 데이터를
 * 다시 정확하게 불러올 때" 사용합니다.
 *
 *
 * ------------------------------------------------------------
 * 한 줄 요약
 * ------------------------------------------------------------
 *
 * DataInputStream은
 * DataOutputStream으로 저장된 데이터를
 * 기본자료형 그대로
 * 안전하게 다시 읽어오기 위한
 * 입력스트림 통로 클래스입니다.
 */

//주제: FilterInputStream부모클래스의 하위 DataInputStream클래스를 사용한 예



import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataStreamTest2 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		//data.txt파일에 기록된 데이터들을 읽어와 저장할 변수들 선언
		char c;
		byte b;
		short s;
		int i;
		float f;
		double d;
		String str;
		
		//data.txt파일에 기록된 데이터들을 메소드를 통해서 읽어올 확장 입력 스트림 DataInputStream객체 생성
		//
		// --------------------------------------------------------------
		// [통로 구조 모델] 쓰기(DataStreamTest1)와 정확히 반대 방향의 2중 통로
		//
		//   [디스크 data.txt]         [Heap]                          [Stack]
		//   28바이트           ====>  FileInputStream (속 통로)
		//                               |  바이트를 순서대로 읽는 역할
		//                               v
		//                             DataInputStream (겉 통로)  --->  c = 'a'
		//                                바이트를 자료형으로            b = 10
		//                                다시 조립하는 역할             ...
		//
		// [읽기 순서 규칙표] 쓴 순서 + 자료형과 완전히 같아야 한다
		//   (DataStreamTest1 의 디스크 배치 모델과 짝을 이루는 표)
		//
		//   순서 | 파일 안 바이트 위치 | 읽는 메소드   | 조립 결과
		//   -----+---------------------+---------------+-----------
		//   1    | 0 ~ 1   (2바이트)   | readChar()    | 'a'
		//   2    | 2       (1바이트)   | readByte()    | 10
		//   3    | 3 ~ 4   (2바이트)   | readShort()   | 20
		//   4    | 5 ~ 8   (4바이트)   | readInt()     | 100
		//   5    | 9 ~ 12  (4바이트)   | readFloat()   | 3.14
		//   6    | 13 ~ 20 (8바이트)   | readDouble()  | 1.5
		//   7    | 21 ~ 27 (7바이트)   | readUTF()     | "hello"
		//
		//   순서를 하나라도 바꾸면?
		//     예를 들어 첫 호출을 readChar 대신 readInt 로 하면
		//     0~3번 바이트 4개를 잘못 묶어 조립하므로
		//     예외가 아니라 "전혀 엉뚱한 값" 이 나온다. 그래서 더 위험하다.
		// --------------------------------------------------------------
		DataInputStream dis  = new DataInputStream( new FileInputStream("data.txt") );   
		
		c = dis.readChar();//'a'
		b = dis.readByte();//10
		s = dis.readShort();//20
		i = dis.readInt();  //100
		f = dis.readFloat(); //3.14f
		d = dis.readDouble();//1.5d
		str = dis.readUTF(); //"hello"
		
		System.out.println(c);
		System.out.println(b);
		System.out.println(s);
		System.out.println(i);
		System.out.println(f);
		System.out.println(d);
		System.out.println(str.toString());
	
	}

}












