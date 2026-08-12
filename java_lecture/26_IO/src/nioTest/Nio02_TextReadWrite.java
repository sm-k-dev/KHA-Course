package nioTest;

import java.nio.charset.StandardCharsets;   // 문자 인코딩 상수를 모아 둔 클래스. UTF_8 을 사용한다
import java.nio.file.Files;                 // 파일 읽기/쓰기를 실행하는 클래스. 메서드가 전부 static
import java.nio.file.Path;                  // 파일 경로를 담는 객체의 타입
import java.nio.file.StandardOpenOption;    // 파일을 열 때 지정하는 옵션 상수 모음 (APPEND 등)
import java.io.IOException;                 // 파일 작업 실패 시 발생하는 예외 클래스
import java.util.ArrayList;                 // 요소를 순서대로 담는 목록 클래스
import java.util.List;                      // 목록의 타입. ArrayList 를 담는 변수의 타입으로 쓴다

/*
 * ==================================================================
 * [NIO 예제 2] 텍스트 파일 쓰기와 읽기
 * ==================================================================
 *
 * ------------------------------------------------------------------
 * 1. 용어 정리 (이 예제에 나오는 자바 용어)
 * ------------------------------------------------------------------
 *
 *   스트림(통로) : 프로그램과 파일 사이에서 데이터가 지나가는 연결.
 *                  NIO 에서는 Files 가 내부에서 열고 닫으므로
 *                  내 코드에는 보이지 않는다.
 *   바이트       : 파일에 실제로 저장되는 숫자 단위. 파일 안에는
 *                  문자가 아니라 바이트가 들어 있다.
 *   인코딩       : 문자를 바이트로 바꾸는 규칙. 우리는 UTF-8 을 쓴다.
 *                  UTF-8 에서 영어/숫자/기호는 1바이트, 한글은 3바이트다.
 *   String       : 문자열을 담는 객체. Heap 에 만들어진다.
 *   List<String> : String 만 담는 목록. <String> 은 요소의 타입 지정이다.
 *   ArrayList    : List 의 실제 구현 클래스. 요소를 넣은 순서대로 보관한다.
 *
 *
 * ------------------------------------------------------------------
 * 2. 이 예제에서 배우는 Files 메서드 4개
 * ------------------------------------------------------------------
 *
 *   쓰기
 *     Files.writeString(경로, String, 인코딩)   String 을 파일로 저장
 *     Files.write(경로, List, 인코딩)           List 를 한 줄씩 파일로 저장
 *
 *   읽기
 *     Files.readString(경로, 인코딩)            파일 전체를 String 하나로 반환
 *     Files.readAllLines(경로, 인코딩)          파일을 줄 단위로 잘라 List 로 반환
 *
 *   저장할 때와 읽을 때 인코딩이 다르면 한글이 깨진다.
 *   그래서 항상 StandardCharsets.UTF_8 로 통일한다.
 *
 *
 * ------------------------------------------------------------------
 * 2-1. IO(java.io) 와의 비교 - 같은 일을 하는 옛날 방식
 * ------------------------------------------------------------------
 *
 *   [이 예제의 NIO 코드와 IO 대응 코드]
 *
 *     NIO (이 예제, 각 1줄)   | IO (java.io 방식, 여러 줄 직접 구현)
 *     ------------------------+------------------------------------------
 *     Files.writeString       | FileWriter 통로 열기 + write + close
 *     Files.readString        | FileReader 통로 열기 + 끝까지 읽는 반복 + close
 *     Files.readAllLines      | BufferedReader 로 readLine 반복 + List.add + close
 *     APPEND 옵션             | new FileWriter(경로, true) 의 true 인자
 *
 *   [통로 관점의 차이]
 *
 *     IO  : 통로(스트림) 객체를 내 코드로 만들고, 흘리고, 닫는다
 *
 *       [Heap String] --> [FileWriter 쓰기 통로 ==>] --> [디스크]
 *        내 코드가 함 :     열기      write      close 전부
 *
 *     NIO : 같은 통로 작업을 Files 가 내부에서 대신한다
 *
 *       [Heap String] ==(Files 내부: 열기+이동+닫기 자동)==> [디스크]
 *        내 코드가 함 :  Files.writeString(...) 호출 1줄
 *
 *     통로가 사라진 것이 아니라, 통로 관리를 Files 에게 맡긴 것이다.
 *     각 구간([1][3][4])에서 IO 로 짜면 어떤 코드였는지 다시 비교한다.
 *
 *
 * ------------------------------------------------------------------
 * 3. 실행 순서
 * ------------------------------------------------------------------
 *
 *   [1] String 을 파일로 저장한다 (스트림에 바이트가 흐르는 과정 모델링)
 *   [2] 파일 전체를 String 으로 읽는다
 *   [3] 줄 단위로 읽어 List 에 담고, 계산까지 한다 (반복 추적표)
 *   [4] APPEND 옵션으로 파일 끝에 이어 쓴다
 *   [5] List 를 만들어 파일로 저장한다
 *   [6] 저장 결과를 다시 읽어 확인한다
 * ==================================================================
 */
public class Nio02_TextReadWrite {

    public static void main(String[] args) throws IOException {
        // throws IOException : Files 메서드가 발생시키는 예외를 넘긴다는 선언

        // member.txt 의 경로를 담은 Path 객체를 Heap 에 만든다. 파일은 아직 없다
        Path path = Path.of("member.txt");

        // ==============================================================
        // [1] String 을 파일로 저장하기 - writeString
        // ==============================================================
        System.out.println("===== [1] 문자열 통째로 쓰기 =====");

        // \n : 줄바꿈을 나타내는 특수 문자. 문자열 안에 넣으면 그 위치에서 행이 나뉜다
        // 아래는 3행짜리 내용을 String 객체 하나에 담은 것이다
        String content = "홍길동,20,서울\n"
                       + "김철수,25,부산\n"
                       + "이영희,23,대구\n";

        // --------------------------------------------------------------
        // [스트림 + 메모리 텍스트 모델링] writeString 실행 중의 전체 상태
        // --------------------------------------------------------------
        //
        //  단계1  실행 직전. String 객체는 Heap 에 있고, 파일은 아직 없다
        //
        //   [Stack]              [Heap]                      [스트림 통로]    [디스크 member.txt]
        //   main() 영역
        //     path    ------->   Path 객체 "member.txt"      (닫혀 있음)      (없음)
        //     content ------->   String 객체
        //                        "홍길동,20,서울\n..."
        //
        //  단계2  writeString 호출. Files 가 스트림을 열고,
        //         String 의 문자를 UTF-8 규칙으로 바이트로 바꿔 통로에 흘린다
        //
        //   [Heap 의 String]         [스트림 통로에 흐르는 바이트]
        //     "홍"          --->      ed 99 8d      (한글 1자 = 3바이트)
        //     "길"          --->      ea b8 b8
        //     "동"          --->      eb 8f 99
        //     ","           --->      2c            (기호 = 1바이트)
        //     "2"           --->      32            (숫자 = 1바이트)
        //     "0"           --->      30
        //     "서"          --->      ec 84 9c
        //     "울"          --->      ec 9a b8
        //     "\n"          --->      0a            (줄바꿈 = 1바이트)
        //
        //  단계3  1행이 전부 흐르면 몇 바이트인지 계산해 보자
        //
        //     한글 5자(홍길동서울) x 3바이트 = 15
        //     기호와 숫자(, 2 0 ,)          =  4
        //     줄바꿈(\n)                    =  1
        //     ------------------------------------
        //     1행 합계                        20바이트
        //
        //  단계4  3행이 모두 흐르고 스트림이 닫힌다 (Files 가 처리)
        //
        //   [Stack]              [Heap]                      [스트림 통로]    [디스크 member.txt]
        //     path    ------->   Path 객체 (그대로)          (닫힘)           60 bytes 생성됨
        //     content ------->   String 객체 (그대로)                         (20 x 3행)
        //
        //   확인할 것
        //     Heap 의 String 은 그대로 남아 있다. 저장했다고 사라지지 않는다.
        //     디스크에는 문자가 아니라 바이트 60개가 기록되었다.
        //     아래 출력에서 60 bytes 가 실제로 나오는지 확인하자.
        // --------------------------------------------------------------

        // --------------------------------------------------------------
        // [IO vs NIO 비교] 같은 저장을 IO 로 하면 통로가 코드에 드러난다
        // --------------------------------------------------------------
        //
        //   IO 방식 (java.io.FileWriter)
        //
        //     FileWriter fw = new FileWriter("member.txt");   // 1. 통로 열기
        //     fw.write(content);                              // 2. 통로에 흘리기
        //     fw.close();                                     // 3. 통로 닫기
        //
        //     [Stack]           [Heap]                 [쓰기 통로]      [디스크]
        //     fw (참조) ------> FileWriter 객체        문자 -> 바이트   member.txt
        //                       (통로의 입구)          변환되어 이동 ==>
        //
        //     close() 를 빠뜨리면 통로에 남은 데이터가 파일에 안 담길 수 있고,
        //     통로 자원이 계속 잡혀 있게 된다. 닫는 책임이 전부 내 코드에 있다.
        //
        //   NIO 방식 (아래 실제 코드)
        //
        //     Files.writeString(path, content, UTF_8);        // 1줄
        //
        //     [Heap String] ==(Files 내부: 열기 -> 변환 -> 이동 -> 닫기)==> [디스크]
        //
        //     하는 일은 IO 와 같다. 통로 열기/닫기를 Files 가 대신하므로
        //     close 누락 실수가 원천적으로 없다.
        // --------------------------------------------------------------

        // Files.writeString(경로, String, 인코딩)
        //   파일이 없으면 만들고, 이미 있으면 내용을 전부 지우고 새로 쓴다 (덮어쓰기)
        Files.writeString(path, content, StandardCharsets.UTF_8);

        System.out.println("member.txt 저장 완료");
        System.out.println("파일 크기 : " + Files.size(path) + " bytes");   // 출력: 60 bytes
        System.out.println();

        // ==============================================================
        // [2] 파일 전체를 String 으로 읽기 - readString
        // ==============================================================
        System.out.println("===== [2] 파일 통째로 읽기 =====");

        // Files.readString(경로, 인코딩)
        //   스트림이 열리고, 디스크의 바이트가 UTF-8 규칙으로 다시 문자로 바뀌어
        //   String 객체 하나로 Heap 에 만들어진 뒤, 그 참조가 반환된다
        //   저장할 때 UTF-8 이었으므로 읽을 때도 UTF-8. 다르면 한글이 깨진다
        String readAll = Files.readString(path, StandardCharsets.UTF_8);

        System.out.println("읽은 내용 전체 아래에 출력");

        // print 와 println 의 차이
        //   println : 출력 후 줄을 바꾼다 / print : 출력만 하고 줄을 안 바꾼다
        // readAll 의 끝에 이미 \n 이 있으므로 print 를 써야 빈 줄이 안 생긴다
        System.out.print(readAll);
        System.out.println();

        // ==============================================================
        // [3] 줄 단위로 읽어 List 에 담기 - readAllLines
        //
        // readString 은 내용을 보여 주는 용도까지만 가능하다.
        // 값을 계산하려면 줄 단위로 자르고, 쉼표 단위로 또 잘라야 한다.
        // ==============================================================
        System.out.println("===== [3] 줄 단위로 읽어서 가공하기 =====");

        // --------------------------------------------------------------
        // [스트림 + 메모리 텍스트 모델링] readAllLines 실행 중의 전체 상태
        // --------------------------------------------------------------
        //
        //  단계1  스트림이 열리고 디스크의 바이트가 올라온다
        //
        //   [디스크 member.txt]        [스트림 통로]            [Heap]
        //   ed 99 8d ea b8 b8 ...  --> UTF-8 로 문자 복원 -->   (만드는 중)
        //   (80바이트, 4행)            \n(0a) 위치에서 자름
        //
        //  단계2  잘린 줄들이 String 객체로 만들어지고 ArrayList 에 담긴다
        //         \n 문자 자체는 버려지고 목록에 들어가지 않는다
        //
        //  단계3  스트림이 닫히고, ArrayList 의 참조가 lines 변수로 반환된다
        //
        //   [Stack]              [Heap]
        //   main() 영역
        //     lines   ------->   ArrayList 객체
        //                          0번 ---> String 객체 "홍길동,20,서울"
        //                          1번 ---> String 객체 "김철수,25,부산"
        //                          2번 ---> String 객체 "이영희,23,대구"
        //
        //   확인할 것
        //     ArrayList 는 String 객체 자체가 아니라
        //     각 String 객체를 가리키는 참조를 칸마다 담고 있다.
        // --------------------------------------------------------------
        //
        // --------------------------------------------------------------
        // [IO vs NIO 비교] 같은 줄 단위 읽기를 IO 로 하면
        // --------------------------------------------------------------
        //
        //   IO 방식 (java.io.BufferedReader)
        //
        //     BufferedReader br = new BufferedReader(
        //             new FileReader("member.txt"));     // 통로 열기 (2중 포장)
        //     List<String> lines = new ArrayList<>();    // 담을 List 직접 준비
        //     String line;
        //     while ((line = br.readLine()) != null) {   // 통로에서 한 줄씩 꺼냄
        //         lines.add(line);                       // List 에 직접 추가
        //     }
        //     br.close();                                // 통로 닫기
        //
        //     [디스크] --> [읽기 통로] --> readLine() 1회 = 1줄  --> lines.add
        //     끝나면 readLine() 이 null 을 반환한다 (바이트 read 의 -1 에 해당)
        //
        //   NIO 방식 (아래 실제 코드)
        //
        //     Files.readAllLines(path, UTF_8);           // 1줄
        //
        //     위 IO 코드의 "통로 열기, readLine 반복, add, 닫기" 전체를
        //     Files 가 내부에서 똑같이 실행하고 완성된 List 만 돌려준다.
        //
        //   [종료 신호 비교]
        //     IO  바이트 읽기 read()     : 끝나면 -1
        //     IO  줄 읽기   readLine()   : 끝나면 null
        //     NIO readAllLines           : 끝 처리를 내부에서 하므로 신호를 볼 일 없음
        // --------------------------------------------------------------
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        // size() : List 에 담긴 요소의 개수를 int 로 반환한다
        System.out.println("총 줄 수 : " + lines.size());   // 출력: 3
        System.out.println();

        // --------------------------------------------------------------
        // [반복 회차별 추적표] 아래 for 문이 도는 동안 각 변수의 값
        // --------------------------------------------------------------
        //
        //  회차 | line 이 가리키는 String | arr[0]   arr[1]  arr[2]  | age | 내년나이 출력
        //  -----+--------------------------+--------------------------+-----+--------------
        //  1    | "홍길동,20,서울"         | "홍길동" "20"    "서울"  | 20  | 21
        //  2    | "김철수,25,부산"         | "김철수" "25"    "부산"  | 25  | 26
        //  3    | "이영희,23,대구"         | "이영희" "23"    "대구"  | 23  | 24
        //  끝   | List 의 요소를 다 꺼냈으므로 반복 종료
        //
        //  표를 읽을 때 반드시 볼 것
        //    arr[1] 의 "20" 은 따옴표가 있다 --> String 타입. 아직 계산할 수 없다
        //    age 의 20 은 따옴표가 없다     --> int 타입. Integer.parseInt 를 거친 결과다
        //
        //  [메모리 모델링] 반복 1회차가 실행되는 순간
        //
        //   [Stack]              [Heap]
        //   main() 영역
        //     lines  --------->  ArrayList (0번, 1번, 2번 참조 보관)
        //     line   --------->  String "홍길동,20,서울"   (지금 회차의 요소)
        //     arr    --------->  String[] 배열
        //                          [0] ---> String "홍길동"
        //                          [1] ---> String "20"
        //                          [2] ---> String "서울"
        //     name   --------->  String "홍길동"  (arr[0] 과 같은 객체를 가리킴)
        //     age    = 20        (int 는 참조가 아니라 값이 Stack 에 직접 담긴다)
        //     city   --------->  String "서울"
        //
        //   회차가 바뀌면 line 과 arr 가 가리키는 대상이 다음 요소로 바뀐다
        // --------------------------------------------------------------

        // 향상된 for 문 : lines 의 요소를 앞에서부터 하나씩 꺼내
        //                line 변수에 담고 블록을 실행한다. 요소가 3개이므로 3회 반복
        for (String line : lines) {

            // split(",") : 문자열을 쉼표 위치에서 잘라 String 배열로 반환한다
            //   "홍길동,20,서울" --> 길이 3짜리 배열
            //   인덱스는 0 부터 시작한다
            String[] arr = line.split(",");

            String name = arr[0];   // 0번 요소를 이름으로 사용

            // Integer.parseInt(String)
            //   숫자 모양의 String 을 int 값으로 변환한다
            //
            //   변환이 필요한 이유
            //     String "20" + 1  --> "201"  (문자열 뒤에 1이 이어 붙는다)
            //     int    20  + 1  --> 21     (실제 덧셈이 된다)
            //   숫자가 아닌 문자열을 넣으면 NumberFormatException 예외가 발생한다
            int age = Integer.parseInt(arr[1]);

            String city = arr[2];   // 2번 요소를 지역으로 사용

            // (age + 1) 의 괄호가 필수인 이유
            //   + 는 왼쪽부터 차례로 계산된다
            //   괄호가 없으면 "내년나이:" + age 가 먼저 문자열로 붙어
            //   결과가 "내년나이:201" 이 된다
            //   괄호가 있으면 int 덧셈이 먼저 계산되어 21 이 된다
            System.out.println("이름:" + name + " / 내년나이:" + (age + 1) + " / 지역:" + city);
        }
        System.out.println();

        // ==============================================================
        // [4] 파일 끝에 이어 쓰기 - APPEND 옵션
        // ==============================================================
        System.out.println("===== [4] 이어쓰기(APPEND) =====");

        // StandardOpenOption.APPEND
        //   기존 내용을 지우지 말고 파일의 맨 뒤에 이어 쓰라는 옵션이다
        //   이 옵션이 없으면 기본 동작인 덮어쓰기가 되어
        //   기존 3행이 사라지고 박민수 1행만 남는다. 직접 빼고 실행해 확인해 보자
        //
        //   [IO vs NIO 비교] 이어쓰기 지정 방법
        //     IO  : new FileWriter("member.txt", true)
        //           두 번째 인자 true 가 이어쓰기라는 뜻. 이름이 없어서
        //           true 가 무엇인지 코드만 봐서는 알기 어렵다
        //     NIO : StandardOpenOption.APPEND
        //           옵션 이름 자체가 뜻을 말해 준다. 읽기 쉬운 쪽이 NIO 다
        //
        //   [디스크 상태 변화]
        //     이어쓰기 전 : 60 bytes (3행)
        //     이어쓰기 후 : 80 bytes (4행)   <- 뒤에 20바이트 1행이 붙었다
        Files.writeString(path, "박민수,30,인천\n", StandardCharsets.UTF_8,
                          StandardOpenOption.APPEND);

        System.out.println("이어쓰기 후 크기 : " + Files.size(path) + " bytes");   // 출력: 80 bytes

        // 다시 읽어 줄 수가 3에서 4로 늘었는지 확인한다
        List<String> after = Files.readAllLines(path, StandardCharsets.UTF_8);
        System.out.println("이어쓰기 후 줄 수 : " + after.size());   // 출력: 4
        System.out.println();

        // ==============================================================
        // [5] List 를 만들어 파일로 저장하기 - Files.write
        //
        // 이번에는 반대 방향이다.
        // 프로그램 안에서 List 를 먼저 만들고, 그것을 파일로 내보낸다.
        // ==============================================================
        System.out.println("===== [5] List를 파일로 저장 =====");

        // new ArrayList<>()
        //   비어 있는 ArrayList 객체를 Heap 에 만들고 참조를 newList 에 담는다
        //   <> 안을 비워 두면 왼쪽의 List<String> 을 보고 String 으로 자동 결정된다
        List<String> newList = new ArrayList<>();

        // add(요소) : List 의 맨 뒤에 요소를 추가한다
        // 넣은 순서 그대로 저장되므로 제목 행을 가장 먼저 넣는다
        newList.add("상품코드,상품명,가격");
        newList.add("P001,키보드,35000");
        newList.add("P002,마우스,18000");

        // --------------------------------------------------------------
        // [메모리 텍스트 모델링] add 3회가 끝난 직후
        //
        //   [Stack]                [Heap]
        //   main() 영역
        //     newList  --------->  ArrayList 객체
        //                            0번 ---> String "상품코드,상품명,가격"
        //                            1번 ---> String "P001,키보드,35000"
        //                            2번 ---> String "P002,마우스,18000"
        //
        //   아직 디스크에는 아무것도 없다. Heap 에만 존재하는 상태다.
        //   아래 Files.write 가 실행되어야 디스크로 나간다.
        // --------------------------------------------------------------
        Path productPath = Path.of("product.txt");   // 저장할 파일의 경로 객체

        // Files.write(경로, List, 인코딩)
        //   List 의 요소를 앞에서부터 한 줄씩 파일에 쓴다
        //   각 요소 뒤에 줄바꿈을 자동으로 넣어 준다
        //   그래서 add 할 때 \n 을 붙이면 줄바꿈이 두 번 되어 빈 줄이 생긴다
        Files.write(productPath, newList, StandardCharsets.UTF_8);

        System.out.println("product.txt 저장 완료 (" + Files.size(productPath) + " bytes)");
        System.out.println();

        // ==============================================================
        // [6] 저장 결과를 다시 읽어 확인
        // ==============================================================
        System.out.println("===== [6] 저장 결과 확인 =====");

        List<String> productLines = Files.readAllLines(productPath, StandardCharsets.UTF_8);

        // 인덱스 for 문 : 행 번호를 출력해야 하므로 번호 변수 i 가 있는 반복문을 쓴다
        //   i 는 0 부터, List 개수 직전까지, 1씩 증가
        for (int i = 0; i < productLines.size(); i++) {

            // get(i) : List 의 i번 요소를 꺼낸다
            // 사람은 1행부터 세므로 화면에는 i + 1 을 출력한다
            System.out.println((i + 1) + "행 : " + productLines.get(i));
        }
        System.out.println();

        // ==============================================================
        // [정리]
        // ==============================================================
        System.out.println("===== [정리] =====");
        System.out.println("1. 파일에는 문자가 아니라 인코딩된 바이트가 저장된다.");
        System.out.println("2. 한글 1자는 UTF-8 에서 3바이트다. 그래서 1행이 20바이트였다.");
        System.out.println("3. writeString 은 덮어쓰기가 기본. 이어 쓰려면 APPEND 옵션.");
        System.out.println("4. readAllLines 는 줄 단위 String 참조를 ArrayList 에 담아 준다.");
        System.out.println("5. String 숫자는 Integer.parseInt 로 int 변환 후 계산한다.");

    }   // main 끝

}   // 클래스 끝
