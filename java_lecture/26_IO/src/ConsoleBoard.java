/*
 * ==================================================================
 * [응용 예제] 콘솔 게시판 v3 - 모든 코드 줄을 하나하나 설명한 판
 *             (자바를 처음 배우는 사람 기준. IO + NIO 조합 실무 구조)
 * ==================================================================
 *
 * ------------------------------------------------------------------
 * 0. 코드를 읽기 전에 : 자바 문장의 기본 모양 3가지
 * ------------------------------------------------------------------
 *
 *   모양 1. 변수 선언 + 값 저장 문장
 *
 *     String   menu   =   keyBr.readLine()  ;
 *     ------   ----   -   ----------------  -
 *     자료형   변수명  대입  저장할 값(계산됨)  문장 끝 표시
 *
 *     읽는 순서 : = 의 "오른쪽을 먼저 실행" 하고, 그 결과를 왼쪽 변수에 담는다
 *
 *   모양 2. 메소드 호출 문장 (일을 시키기만 하고 결과는 안 받는 경우)
 *
 *     Files.createDirectories( uploadDir ) ;
 *     -----  -----------------  ---------
 *     클래스   메소드 이름        괄호 안 = 전달하는 재료(매개값)
 *
 *   모양 3. 블록 문장 (여러 문장을 중괄호 { } 로 묶은 것)
 *
 *     if (조건) {            <- 조건이 true 면
 *         문장1;                이 중괄호 안의 문장들을 실행
 *         문장2;
 *     }                      <- if 블록의 끝
 *
 *   중괄호 { 가 열리면 반드시 짝이 되는 } 가 있다.
 *   이 파일에서는 닫는 } 마다 "무엇의 끝인지" 주석을 달아 두었다.
 *
 * ------------------------------------------------------------------
 * 1. 이 파일에서 처음 나오는 문법 8가지 (본문에서 다시 자세히 설명)
 * ------------------------------------------------------------------
 *
 * (문법 1) static 변수 - 메소드 밖, 클래스 바로 안에 선언한 변수
 *
 *   이 파일에는 main 말고도 printList, writePost, viewPost 메소드가 있고
 *   넷 다 같은 경로와 같은 키보드 통로를 써야 한다.
 *   그래서 "모든 메소드가 함께 쓰는 변수" 를 클래스 바로 안에 static 으로 선언했다.
 *
 *   [JVM 메모리 수직 모델] static 변수가 놓이는 위치
 *
 *   [Method Area]  클래스 정보가 올라가는 구역 (프로그램 시작 시 1번 준비됨)
 *     boardDir, uploadDir, downloadDir, postsFile, keyBr  <- static 변수들
 *          ^
 *          | main 도, printList 도, viewPost 도 전부 여기를 같이 본다
 *   [Stack]        각 메소드가 실행될 때 지역변수가 쌓이는 구역
 *   [Heap]         new 로 만든 객체들이 놓이는 구역
 *
 * (문법 2) while(true) 와 break
 *
 *   while(true)  : 조건이 항상 true 이므로 무한 반복
 *   break        : 반복문을 그 즉시 빠져나가는 명령
 *
 *     while(true) --> 메뉴 출력 --> 입력 --> "0" 인가? --아니오--> 다시 위로
 *                                               |
 *                                               예 --> break --> 반복 밖으로
 *
 * (문법 3) 문자열 비교는 == 가 아니라 equals
 *
 *   [Stack]              [Heap]
 *   menu (참조) ------>  String 객체 "1"   <- 사용자 입력으로 만들어진 객체
 *                        String 객체 "1"   <- 코드에 적은 "1" (별개 객체일 수 있음)
 *
 *   ==     : 두 참조가 "같은 객체를 가리키는가" 비교 --> 내용이 같아도 false 가능
 *   equals : 두 객체의 "내용(글자)이 같은가" 비교    --> 우리가 원하는 비교
 *
 * (문법 4) split(",", 4) 의 두 번째 숫자 = 최대 조각 수
 *
 *   "1,첫 글,-,안녕하세요, 반갑습니다".split(",", 4)
 *
 *   +----+--------+----+--------------------------
 *   | "1"| "첫 글"| "-"| "안녕하세요, 반갑습니다"
 *   +----+--------+----+--------------------------
 *    [0]   [1]     [2]   [3] <- 4조각이 되면 그 뒤 쉼표는 더 안 자른다
 *
 * (문법 5) void 메소드 안의 return;
 *   돌려줄 값 없이 "이 메소드 실행을 여기서 즉시 끝내라" 는 뜻이다.
 *
 * (문법 6) ! 연산자 (논리 부정)
 *   ! 는 true 를 false 로, false 를 true 로 뒤집는다.
 *   Files.exists(p) 가 true(있음)일 때  !Files.exists(p) 는 false 가 된다.
 *
 * (문법 7) 숫자/객체 + 문자열 = 자동 문자열 변환
 *   no + "_" + fileName 에서 int 인 no 는 + 를 만나면 문자열로 바뀌어 이어진다.
 *
 * (문법 8) 한 줄에 new 가 두 번 있는 코드는 "안쪽 괄호부터" 실행된다
 *   new BufferedReader( new InputStreamReader( System.in ) )
 *        <-- 2번째 실행        <-- 1번째 실행
 *
 * ------------------------------------------------------------------
 * 2. 실무 웹 게시판과의 대응 모델 (이 예제가 연습하는 것)
 * ------------------------------------------------------------------
 *
 *   실제 웹 게시판                     이 콘솔 게시판
 *   ---------------------------------+--------------------------------
 *   브라우저에서 글 입력              | 키보드로 글 입력 (IO 통로)
 *   서버가 요청을 한 줄씩 읽음        | BufferedReader.readLine()
 *   DB 에 글 저장                     | board/posts.txt 에 한 줄 저장 (NIO)
 *   서버 폴더에 첨부파일 저장(업로드) | board/upload 폴더로 copy (NIO)
 *   첨부파일 내려받기(다운로드)       | download 폴더로 copy (NIO)
 *   화면(HTML)으로 목록 응답          | 콘솔에 목록 출력
 *
 * ------------------------------------------------------------------
 * 3. 전체 데이터 흐름 모델
 * ------------------------------------------------------------------
 *
 *   [키보드]
 *     |  IO 3중 입력 통로 (System.in -> InputStreamReader -> BufferedReader)
 *     v
 *   [이 프로그램 (메모리)]
 *     |                     |
 *     | 글 저장 (NIO)       | 첨부 복사 (NIO)
 *     v                     v
 *   [디스크 board/posts.txt]  [디스크 board/upload/번호_파일명]
 *     ^
 *     | 목록/상세 읽기 (NIO)
 *     v
 *   [콘솔 화면 출력]
 *
 * ------------------------------------------------------------------
 * 4. posts.txt 디스크 배치 모델 (글 1개 = 한 줄)
 * ------------------------------------------------------------------
 *
 *   한 줄 형식 : 번호,제목,첨부파일명,내용
 *
 *   1,첫 글,-,안녕하세요, 반갑습니다
 *   ^  ^     ^  ^
 *   |  |     |  +-- 내용 (쉼표 포함 가능. 문법 4 참고)
 *   |  |     +-- 첨부 저장명. 없으면 - 기호
 *   |  +-- 제목
 *   +-- 글 번호 (1부터 자동 증가)
 * ==================================================================
 */

//==================================================================
// import 구문 구역
//
//   import 란?
//   - 다른 폴더(패키지)에 들어 있는 클래스를 이 파일에서 쓰겠다고
//     미리 알려 두는 문장이다.
//   - import java.io.BufferedReader; 를 적어 두면
//     본문에서 java.io.BufferedReader 라고 길게 안 쓰고
//     BufferedReader 라고 짧게 쓸 수 있다.
//   - 문장 끝에는 항상 ; (세미콜론) 을 붙인다.
//==================================================================

import java.io.BufferedReader;              //IO : 키보드 입력을 "한 줄 단위"로 읽는 문자 스트림 클래스
import java.io.InputStreamReader;           //IO : 바이트(System.in)를 문자로 바꾸는 변환 통로 클래스
import java.io.IOException;                 //입출력 작업이 실패했을 때 발생하는 예외 클래스

import java.nio.charset.StandardCharsets;   //NIO : 문자 인코딩 상수 모음 클래스 (UTF_8 사용)
import java.nio.file.Files;                 //NIO : 파일 저장/읽기/복사 등 디스크 작업 클래스 (전부 static 메소드)
import java.nio.file.Path;                  //NIO : 파일 경로를 담는 객체의 자료형
import java.nio.file.StandardCopyOption;    //NIO : 복사할 때 주는 옵션 상수 모음 (REPLACE_EXISTING)
import java.nio.file.StandardOpenOption;    //NIO : 파일 쓸 때 주는 옵션 상수 모음 (APPEND)

import java.util.List;                      //여러 개의 값을 순서대로 담는 목록 자료형


public class ConsoleBoard {

    //==============================================================
    // static 변수 선언 구역 (문법 1 참고)
    //
    //   여기는 메소드 "밖", 클래스 "바로 안" 이다.
    //   여기 선언한 static 변수는 이 클래스의 모든 메소드가 같이 쓴다.
    //==============================================================

    //static Path boardDir = Path.of("board");  분해
    //  static      : 모든 메소드가 공유하는 변수라는 표시 (Method Area 에 놓임)
    //  Path        : 이 변수에 담을 값의 자료형 (경로 객체를 가리키는 참조)
    //  boardDir    : 변수 이름 (개발자가 지은 이름)
    //  =           : 오른쪽 실행 결과를 왼쪽 변수에 담으라는 대입 기호
    //  Path.of("board") : Path 클래스의 of 메소드를 호출.
    //                     "board" 라는 경로 문자열을 담은 Path 객체를
    //                     Heap 에 만들고 그 주소를 돌려준다
    //  ;           : 문장의 끝
    //  실행 결과   : boardDir 이 Heap 의 Path 객체 "board" 를 가리킨다
    //                (이 시점에 디스크에는 아무 변화 없음. 주소만 적어 둔 상태)
    static Path boardDir = Path.of("board");

    //첨부파일이 올라갈(업로드될) 폴더의 경로 객체
    //  Path.of("board", "upload") : 재료(매개값)를 2개 넘기는 호출.
    //  조각 2개가 "board/upload" 로 조립된 Path 객체가 만들어진다
    //  (조각 사이 구분 문자는 운영체제에 맞게 자동으로 들어간다)
    static Path uploadDir = Path.of("board", "upload");

    //첨부파일을 내려받을(다운로드할) 폴더의 경로 객체 (원리는 boardDir 과 동일)
    static Path downloadDir = Path.of("download");

    //글이 저장될 파일의 경로 객체. 이 텍스트 파일 하나가 DB 역할을 한다
    static Path postsFile = Path.of("board", "posts.txt");

    //static BufferedReader keyBr = new BufferedReader(new InputStreamReader(System.in));  분해
    //
    //  실행 순서는 "안쪽 괄호부터" 다 (문법 8)
    //
    //  1번째 실행 : new InputStreamReader( System.in )
    //    new            : Heap 에 객체를 만들라는 예약어
    //    System.in      : 키보드와 연결된 바이트 입력 통로 (이미 만들어져 있는 객체)
    //    --> System.in 을 감싸서 "바이트를 문자로 바꿔 주는 통로" 객체가 Heap 에 생긴다
    //
    //  2번째 실행 : new BufferedReader( 1번째 결과 )
    //    --> 문자 통로를 다시 감싸서 "한 줄 단위로 읽는 통로" 객체가 Heap 에 생긴다
    //
    //  3번째 실행 : = 대입
    //    --> 완성된 겉 통로 객체의 주소가 keyBr 변수에 담긴다
    //
    //  [완성된 통로 구조]
    //    [키보드] -> System.in(바이트) -> InputStreamReader(문자) -> BufferedReader(한 줄)
    //                                                                  ^
    //                                                        keyBr 는 여기를 가리킨다
    static BufferedReader keyBr = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        //----------------------------------------------------------
        // [준비 1] 폴더 만들기 (웹 서버가 시작될 때의 초기화에 해당)
        //----------------------------------------------------------

        //Files.createDirectories(uploadDir);  분해
        //  Files              : NIO 의 파일 작업 클래스 (객체 생성 없이 클래스 이름으로 호출)
        //  .createDirectories : "경로에 적힌 폴더를 중간 폴더까지 전부 만들어라" 메소드
        //  (uploadDir)        : 어떤 경로를 만들지 재료로 전달
        //  실행 결과 : uploadDir 이 "board/upload" 이므로
        //              board 폴더와 그 안의 upload 폴더가 디스크에 함께 생긴다.
        //              이미 있으면 아무 일도 하지 않는다 (예외 없음. 반복 실행 안전)
        Files.createDirectories(uploadDir);

        //download 폴더 생성 (위와 같은 원리)
        Files.createDirectories(downloadDir);

        //----------------------------------------------------------
        // [준비 2] 글 파일이 없으면 빈 파일로 만들기
        //----------------------------------------------------------

        //boolean postsFileExists = Files.exists(postsFile);  분해
        //  boolean          : true 또는 false 두 값만 담는 자료형
        //  postsFileExists  : 변수 이름
        //  Files.exists(postsFile) : postsFile 경로가 디스크에 실제로 있으면 true,
        //                            없으면 false 를 돌려주는 메소드
        //  실행 결과 : 첫 실행이면 파일이 없으므로 false 가 담긴다
        boolean postsFileExists = Files.exists(postsFile);

        //if (!postsFileExists) {  분해
        //  if       : 괄호 안이 true 일 때만 중괄호 안을 실행하는 예약어
        //  !        : true/false 를 뒤집는 기호 (문법 6)
        //  조건 계산 : postsFileExists 가 false --> ! 로 뒤집혀 true --> 블록 실행
        //             (즉 "파일이 없으면" 이라는 조건이 된다)
        if (!postsFileExists) {

            //Files.createFile(postsFile);
            //  전달한 경로에 내용이 비어 있는 파일(0바이트)을 만든다.
            //  실행 결과 : 디스크에 board/posts.txt 생성
            Files.createFile(postsFile);

        }   //if (!postsFileExists) 블록의 끝

        //----------------------------------------------------------
        // [메뉴 반복] 0 을 입력할 때까지 계속 돈다 (문법 2)
        //----------------------------------------------------------

        //while (true) {  분해
        //  while : 괄호 안이 true 인 동안 중괄호 안을 반복 실행하는 예약어
        //  true  : 항상 참 --> 무한 반복. 탈출은 아래 break 가 담당한다
        while (true) {

            //System.out.println();  분해
            //  System.out : 화면(콘솔)과 연결된 출력 통로 (PrintStream 타입의 객체)
            //  .println() : 괄호 안 내용을 출력하고 줄을 바꾸는 메소드.
            //               괄호가 비어 있으면 빈 줄 1개만 출력한다
            System.out.println();

            //메뉴 제목과 항목을 화면에 출력 (아래 5줄 모두 println 같은 원리)
            System.out.println("===== 콘솔 게시판 =====");   //제목 출력 후 줄바꿈
            System.out.println("1. 글 목록");                //메뉴 1 출력 후 줄바꿈
            System.out.println("2. 글 쓰기");                //메뉴 2 출력 후 줄바꿈
            System.out.println("3. 글 상세보기");            //메뉴 3 출력 후 줄바꿈
            System.out.println("0. 종료");                   //메뉴 0 출력 후 줄바꿈

            //System.out.print("메뉴 번호 입력 > ");  분해
            //  print 는 println 과 달리 출력 후 줄을 바꾸지 않는다.
            //  --> 사용자가 같은 줄에서 바로 이어서 입력하게 하기 위해서다
            System.out.print("메뉴 번호 입력 > ");

            //String menu = keyBr.readLine();  분해
            //  String          : 문자열 자료형
            //  menu            : 변수 이름
            //  keyBr           : 위에서 만든 키보드 3중 통로를 가리키는 참조변수
            //  .readLine()     : 사용자가 엔터를 누를 때까지 기다렸다가
            //                    엔터 직전까지 입력한 글자들을 String 하나로 돌려주는 메소드
            //  실행 결과 예    : 사용자가 2 를 치고 엔터 --> Heap 에 String "2" 가 생기고
            //                    menu 가 그 객체를 가리킨다
            String menu = keyBr.readLine();

            //if (menu.equals("1")) {  분해
            //  menu.equals("1") : menu 가 가리키는 문자열의 "내용" 이
            //                     "1" 과 같은지 비교해서 true/false 반환 (문법 3)
            //  같으면 --> 아래 printList(); 실행
            if (menu.equals("1")) {

                //printList();  분해
                //  이 파일 아래쪽에 만들어 둔 printList 메소드를 호출(실행)하는 문장.
                //  실행 순서 : main 이 잠시 멈춤 --> printList 가 끝까지 실행됨
                //              --> main 의 다음 줄로 돌아옴
                printList();

            } else if (menu.equals("2")) {   //위 if 가 false 였고, menu 가 "2" 면

                writePost();                 //글 쓰기 메소드 호출

            } else if (menu.equals("3")) {   //menu 가 "3" 이면

                viewPost();                  //글 상세보기 메소드 호출

            } else if (menu.equals("0")) {   //menu 가 "0" 이면 (종료 선택)

                System.out.println("게시판을 종료합니다.");   //안내 출력

                //break;  분해
                //  지금 돌고 있는 while 반복을 그 즉시 빠져나가는 명령 (문법 2).
                //  실행 결과 : while 블록 아래(keyBr.close())로 이동한다
                break;

            } else {   //위의 어떤 조건에도 해당하지 않으면 (1,2,3,0 이 아닌 입력)

                System.out.println("없는 메뉴입니다. 다시 입력하세요.");
                //이 뒤에는 문장이 없으므로 while 의 처음으로 돌아가 메뉴가 다시 뜬다

            }   //if ~ else if ~ else 전체의 끝

        }   //while (true) 반복의 끝

        //keyBr.close();  분해
        //  키보드 입력 통로를 닫는 메소드 호출.
        //  다 쓴 통로는 닫아서 자원을 돌려주는 것이 원칙이다
        keyBr.close();

    }   //main 메소드의 끝

    //==============================================================
    // [기능 1] printList : 글 목록을 화면에 출력하는 메소드
    //
    //   public static void printList() throws IOException {  분해
    //     public  : main 등 다른 곳에서 호출할 수 있게 공개
    //     static  : static 인 main 에서 객체 생성 없이 호출하려면
    //               이 메소드도 static 이어야 한다
    //     void    : 돌려줄 값 없음 (화면 출력만 한다)
    //     printList : 메소드 이름
    //     ()      : 전달받을 재료(매개변수)가 없다는 뜻
    //     throws IOException : 파일 읽기 실패 시 예외를 호출한 쪽(main)으로 떠넘김
    //
    //   [데이터 흐름 모델]
    //   [디스크 posts.txt] ==(readAllLines)==> [Heap 의 List]
    //                                             |  한 줄씩 split
    //                                             v
    //                                         [콘솔 화면]  번호 : 제목
    //==============================================================
    public static void printList() throws IOException {

        //List<String> lines = Files.readAllLines(postsFile, StandardCharsets.UTF_8);  분해
        //  List<String>   : String 만 담는 목록 자료형.
        //                   < > 안의 String 은 "칸마다 담길 값의 자료형" 지정이다
        //  lines          : 변수 이름
        //  Files.readAllLines(경로, 인코딩)
        //                 : 파일 전체를 읽어 "한 줄 = 한 칸" 인 List 로 만들어 돌려주는 메소드.
        //                   저장할 때 UTF-8 이었으므로 읽을 때도 UTF_8 로 맞춘다
        //  실행 결과 예   : 글이 1개면 lines 는 1칸짜리 목록
        //                   0번 칸 --> "1,첫 글,1_sample.txt,안녕하세요, 반갑습니다"
        List<String> lines = Files.readAllLines(postsFile, StandardCharsets.UTF_8);

        //int postCount = lines.size();  분해
        //  int          : 정수 자료형
        //  lines.size() : 목록의 칸 수를 정수로 돌려주는 메소드
        //  한 줄 = 글 1개 이므로, 칸 수 = 글 수 가 된다
        int postCount = lines.size();

        //문자열 + 정수 : postCount 가 자동으로 문자열로 바뀌어 이어진다 (문법 7)
        System.out.println("----- 글 목록 (총 " + postCount + "건) -----");

        //if (postCount == 0) {  분해
        //  ==  : 숫자(기본자료형)끼리는 == 로 값을 비교한다.
        //        (equals 는 객체 내용 비교, == 는 숫자 값/참조 비교)
        if (postCount == 0) {

            System.out.println("(등록된 글이 없습니다)");   //안내 출력

            //return;  : 이 메소드 실행을 여기서 즉시 끝낸다 (문법 5).
            //출력할 글이 없으므로 아래 for 문까지 갈 필요가 없다.
            //실행 결과 : main 의 호출한 지점 다음으로 돌아간다
            return;

        }   //if (postCount == 0) 블록의 끝

        //for (String line : lines) {  분해  (향상된 for 문)
        //  lines 목록의 칸을 앞(0번)에서부터 하나씩 꺼내
        //  String line 변수에 담고, 담을 때마다 중괄호 안을 1회 실행한다.
        //  꺼낼 칸이 더 없으면 반복이 끝난다.
        //  회차 예 : 1회차 line = 0번 칸의 글, 2회차 line = 1번 칸의 글 ...
        for (String line : lines) {

            //String[] arr = line.split(",", 4);  분해
            //  String[]  : String 을 여러 칸에 담는 배열 자료형
            //  line.split(",", 4) : line 문자열을 쉼표 위치에서 잘라
            //                       "최대 4조각" 배열로 돌려준다 (문법 4)
            //  실행 결과 : arr[0]=번호 arr[1]=제목 arr[2]=첨부 arr[3]=내용
            String[] arr = line.split(",", 4);

            //배열 칸의 값을 이름 붙은 변수로 옮겨 담기 (읽기 쉽게 하기 위해)
            //  arr[0]  : 배열의 0번 칸을 꺼내는 표기. 배열 번호는 0부터 시작한다
            String no     = arr[0];   //0번 칸 = 글 번호
            String title  = arr[1];   //1번 칸 = 제목
            String attach = arr[2];   //2번 칸 = 첨부 저장명 (없으면 "-")

            //첨부 표시 문자열 준비. 일단 "빈 문자열(글자 0개)" 로 시작한다
            String attachMark = "";

            //attach 의 내용이 "-" 와 같은지 비교 --> 같으면 true (첨부 없음)
            boolean noAttach = attach.equals("-");

            //!noAttach : "첨부 없음" 을 뒤집으면 "첨부 있음" (문법 6)
            if (!noAttach) {

                attachMark = " [첨부]";   //표시 문자열을 " [첨부]" 로 교체

            }   //if (!noAttach) 블록의 끝

            //번호 + 제목 + 첨부표시 를 이어서 한 줄 출력
            //예) "1번 : 첫 글 [첨부]"
            System.out.println(no + "번 : " + title + attachMark);

        }   //for 반복의 끝

    }   //printList 메소드의 끝

    //==============================================================
    // [기능 2] writePost : 글을 저장하는 메소드 (+ 첨부파일 업로드)
    //
    //   [저장 통로 모델]
    //   [키보드] -> readLine (제목/내용/첨부여부)
    //           -> "번호,제목,첨부,내용" 한 줄로 조립
    //           -> writeString + APPEND : 파일 "끝" 에 이어쓰기
    //              (APPEND 가 없으면 덮어쓰기가 되어 기존 글이 전부 사라진다!)
    //
    //   [업로드 통로 모델] 첨부파일이 있을 때
    //
    //   [디스크 원본 sample.txt]              [디스크 board/upload/1_sample.txt]
    //         |                                          ^
    //         +===== Files.copy (통로 자동 관리) ========+
    //
    //     실무 웹의 "업로드" = 사용자 쪽 파일을 서버 폴더로 복사. 이 구조 그대로다.
    //==============================================================
    public static void writePost() throws IOException {

        //---------- 새 글 번호 정하기 : 지금까지의 글 수 + 1 ----------

        //파일 전체를 List 로 읽는다 (printList 의 첫 줄과 같은 원리)
        List<String> allLines = Files.readAllLines(postsFile, StandardCharsets.UTF_8);

        //목록의 칸 수 = 지금까지의 글 수
        int postCount = allLines.size();

        //int no = postCount + 1;  분해
        //  = 의 오른쪽 postCount + 1 이 먼저 계산되고, 결과가 no 에 담긴다.
        //  예) 글이 0개면 no 는 1 (첫 글의 번호)
        int no = postCount + 1;

        //---------- 제목 / 내용 / 첨부 여부를 키보드에서 읽기 ----------

        System.out.print("제목 입력 > ");        //줄바꿈 없이 안내 출력
        String title = keyBr.readLine();         //한 줄 입력 --> title 에 저장

        System.out.print("내용 입력 (한 줄) > ");
        String content = keyBr.readLine();       //내용 저장 (쉼표 있어도 됨. 문법 4)

        System.out.print("첨부파일이 있습니까? (y/n) > ");
        String hasFile = keyBr.readLine();       //"y" 또는 "n" 저장

        //첨부 저장명을 담을 변수. 첨부가 없으면 이 "-" 가 그대로 파일에 저장된다
        String attachName = "-";

        //---------- 첨부가 있다고 답한 경우 : 업로드(복사) 실행 ----------

        if (hasFile.equals("y")) {               //입력 내용이 "y" 와 같으면

            System.out.print("올릴 파일 경로 입력 > ");
            String srcInput = keyBr.readLine();  //예) "sample.txt" 저장

            //입력받은 문자열을 Path 객체로 바꾼다 (경로로 다루기 위해)
            Path srcPath = Path.of(srcInput);

            //그 경로에 파일이 실제로 있는지 확인 --> true/false
            boolean srcExists = Files.exists(srcPath);

            if (srcExists) {                     //원본이 있으면

                //Path srcFileName = srcPath.getFileName();  분해
                //  getFileName() : 경로의 마지막 조각(파일명)만 Path 로 돌려주는 메소드
                //  예) "sample.txt"
                Path srcFileName = srcPath.getFileName();

                //attachName = no + "_" + srcFileName;  분해
                //  int 인 no 와 Path 인 srcFileName 이 + 를 만나
                //  자동으로 문자열로 바뀌어 이어진다 (문법 7)
                //  예) 1 + "_" + sample.txt --> "1_sample.txt"
                //  번호를 붙이는 이유 : 같은 이름의 파일이 또 올라와도
                //  서로 덮어쓰지 않게 하는 실무 관행이다
                attachName = no + "_" + srcFileName;

                //Path targetPath = uploadDir.resolve(attachName);  분해
                //  resolve : 호출한 경로 뒤에 조각을 이어 붙인 "새" Path 를 돌려주는 메소드.
                //            uploadDir 자체는 바뀌지 않는다
                //  예) "board/upload" + "1_sample.txt" --> "board/upload/1_sample.txt"
                Path targetPath = uploadDir.resolve(attachName);

                //Files.copy(srcPath, targetPath, StandardCopyOption.REPLACE_EXISTING);  분해
                //  copy(원본, 목적지, 옵션) : 원본을 읽어 목적지에 같은 내용의
                //                             새 파일을 만든다 (원본은 그대로 남음)
                //  REPLACE_EXISTING : 목적지에 같은 이름이 이미 있으면 덮어쓰라는 옵션
                //  실행 결과 : 디스크에 board/upload/1_sample.txt 생성 = 업로드 완료
                Files.copy(srcPath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("업로드 완료 : " + attachName);

            } else {                             //원본이 없으면

                System.out.println("파일이 없어 첨부 없이 저장합니다.");
                //attachName 은 처음의 "-" 그대로 유지된다

            }   //if (srcExists) ~ else 의 끝

        }   //if (hasFile.equals("y")) 블록의 끝

        //---------- 저장할 한 줄 조립 후 파일 끝에 이어쓰기 ----------

        //String oneLine = no + "," + title + "," + attachName + "," + content + "\n";  분해
        //  + 는 왼쪽부터 차례로 이어 붙인다. no 는 자동으로 문자열이 된다 (문법 7)
        //  "\n" : 줄바꿈을 뜻하는 특수 문자.
        //         writeString 은 줄바꿈을 자동으로 넣지 않으므로 직접 붙인다.
        //         안 붙이면 다음 글이 같은 줄에 이어 붙는 사고가 난다
        //  조립 결과 예) "1,첫 글,1_sample.txt,안녕하세요, 반갑습니다\n"
        String oneLine = no + "," + title + "," + attachName + "," + content + "\n";

        //Files.writeString(postsFile, oneLine, StandardCharsets.UTF_8, StandardOpenOption.APPEND);  분해
        //  writeString(경로, 문자열, 인코딩, 옵션) : 문자열을 파일에 기록하는 메소드
        //  StandardOpenOption.APPEND : 쓰기 통로를 파일의 "끝" 에 연결하라는 옵션.
        //    APPEND 있음 --> 기존 글 뒤에 이어쓰기 (글이 쌓인다)
        //    APPEND 없음 --> 0번 위치부터 덮어쓰기 (기존 글 전부 소멸!)
        //  실행 결과 : posts.txt 끝에 새 글 한 줄이 추가된다
        Files.writeString(postsFile, oneLine,
                          StandardCharsets.UTF_8, StandardOpenOption.APPEND);

        //정수 no 가 문자열로 바뀌어 이어진 안내 출력. 예) "1번 글 저장 완료"
        System.out.println(no + "번 글 저장 완료");

    }   //writePost 메소드의 끝

    //==============================================================
    // [기능 3] viewPost : 글 1개를 찾아 보여 주는 메소드 (+ 첨부 다운로드)
    //
    //   [찾기 모델] 번호가 3인 글을 찾는 과정 (글이 3개일 때)
    //
    //     회차 | 이 줄의 번호 조각 | equals("3") | 동작
    //     -----+-------------------+-------------+---------------------
    //     1    | "1"               | false       | 다음 줄로
    //     2    | "2"               | false       | 다음 줄로
    //     3    | "3"               | true        | 출력 후 return 으로 끝
    //
    //   [다운로드 통로 모델] 업로드와 방향만 반대인 복사
    //
    //   [디스크 board/upload/1_sample.txt]      [디스크 download/1_sample.txt]
    //         |                                          ^
    //         +===== Files.copy (서버 -> 내 폴더) ======+
    //==============================================================
    public static void viewPost() throws IOException {

        System.out.print("볼 글 번호 입력 > ");     //안내 출력 (줄바꿈 없음)
        String inputNo = keyBr.readLine();          //찾을 번호를 문자열로 저장
                                                    //(파일 속 번호도 문자열이므로
                                                    // 숫자로 바꾸지 않고 그대로 비교한다)

        //글 전체를 List 로 읽는다 (앞의 두 메소드와 같은 원리)
        List<String> lines = Files.readAllLines(postsFile, StandardCharsets.UTF_8);

        //글을 한 줄씩 꺼내며 번호를 대조한다
        for (String line : lines) {

            //한 줄을 최대 4조각으로 자른다 : [0]번호 [1]제목 [2]첨부 [3]내용
            String[] arr = line.split(",", 4);

            //조각을 이름 붙은 변수로 옮겨 담기
            String no      = arr[0];   //글 번호
            String title   = arr[1];   //제목
            String attach  = arr[2];   //첨부 저장명 ("-" 면 없음)
            String content = arr[3];   //내용

            //이 줄의 번호가 사용자가 찾는 번호와 내용이 같은가? --> true/false
            boolean found = no.equals(inputNo);

            if (found) {   //찾는 글이면

                //상세 내용 3줄 출력
                System.out.println("----- " + no + "번 글 -----");
                System.out.println("제목 : " + title);
                System.out.println("내용 : " + content);

                //첨부가 없는 글인지 확인 ("-" 와 내용이 같으면 없음)
                boolean noAttach = attach.equals("-");

                if (!noAttach) {   //뒤집어서 "첨부가 있으면" (문법 6)

                    System.out.println("첨부 : " + attach);       //첨부명 출력
                    System.out.print("다운로드 하시겠습니까? (y/n) > ");
                    String down = keyBr.readLine();               //"y"/"n" 입력

                    if (down.equals("y")) {   //다운로드를 원하면

                        //다운로드 원본 = 업로드 폴더에 저장되어 있는 그 파일
                        //예) "board/upload" + "1_sample.txt"
                        //    --> "board/upload/1_sample.txt"
                        Path fromPath = uploadDir.resolve(attach);

                        //다운로드 목적지 = download 폴더 안의 같은 이름
                        //예) "download/1_sample.txt"
                        Path toPath = downloadDir.resolve(attach);

                        //복사 실행 = 다운로드 (업로드와 원리가 같고 방향만 반대)
                        Files.copy(fromPath, toPath,
                                   StandardCopyOption.REPLACE_EXISTING);

                        System.out.println("다운로드 완료 : download/" + attach);

                    }   //if (down.equals("y")) 블록의 끝

                }   //if (!noAttach) 블록의 끝

                //글을 찾아 다 보여 줬으므로 메소드를 즉시 끝낸다 (문법 5).
                //남은 줄을 더 검사할 필요가 없다
                return;

            }   //if (found) 블록의 끝

        }   //for 반복의 끝

        //for 를 끝까지 돌았는데 return 을 못 만났다 = 일치하는 번호가 없었다
        System.out.println("해당 번호의 글이 없습니다.");

    }   //viewPost 메소드의 끝

}   //ConsoleBoard 클래스의 끝
/*
 * ==================================================================
 * 실행 시나리오와 전체 예상 출력 (첨부용 sample.txt 를 미리 만들어 둔 경우)
 * ------------------------------------------------------------------
 * ===== 콘솔 게시판 =====          <- 메뉴가 뜬다
 * 메뉴 번호 입력 > 2               <- 글 쓰기 선택
 * 제목 입력 > 첫 글
 * 내용 입력 (한 줄) > 안녕하세요, 반갑습니다     <- 내용에 쉼표 포함!
 * 첨부파일이 있습니까? (y/n) > y
 * 올릴 파일 경로 입력 > sample.txt
 * 업로드 완료 : 1_sample.txt
 * 1번 글 저장 완료
 *
 * 메뉴 번호 입력 > 1               <- 목록 선택
 * ----- 글 목록 (총 1건) -----
 * 1번 : 첫 글 [첨부]
 *
 * 메뉴 번호 입력 > 3               <- 상세보기 선택
 * 볼 글 번호 입력 > 1
 * ----- 1번 글 -----
 * 제목 : 첫 글
 * 내용 : 안녕하세요, 반갑습니다     <- 쉼표가 살아 있다 (문법 4 덕분)
 * 첨부 : 1_sample.txt
 * 다운로드 하시겠습니까? (y/n) > y
 * 다운로드 완료 : download/1_sample.txt
 *
 * 메뉴 번호 입력 > 9               <- 없는 메뉴 입력 시
 * 없는 메뉴입니다. 다시 입력하세요.
 *
 * 메뉴 번호 입력 > 0
 * 게시판을 종료합니다.
 * ------------------------------------------------------------------
 * 종료 후 디스크 최종 상태
 *
 *   board/
 *     posts.txt                "1,첫 글,1_sample.txt,안녕하세요, 반갑습니다"
 *     upload/
 *       1_sample.txt           <- 업로드된 첨부 (원본의 복사본)
 *   download/
 *     1_sample.txt             <- 다운로드된 첨부 (upload 의 복사본)
 *   sample.txt                 <- 원본은 그대로 (copy 이므로)
 * ==================================================================
 * 핵심 정리
 * 1. 문장은 = 의 오른쪽부터 실행되고, 결과가 왼쪽 변수에 담긴다.
 * 2. 키보드 입력은 IO 의 3중 통로(BufferedReader)로 한 줄씩 읽는다.
 * 3. 글 저장은 NIO 의 writeString + APPEND (파일 끝 이어쓰기) 로 한다.
 * 4. 업로드/다운로드의 정체는 Files.copy 한 줄이다. 방향만 반대다.
 * 5. split(",", 4) 의 숫자 4 가 내용 속 쉼표를 지켜 준다.
 * 6. 문자열 내용 비교는 equals, 숫자 값 비교는 == 를 쓴다.
 * ==================================================================
 */
