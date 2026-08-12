package nioTest;


//==================================================================
// import 구문 구역
//   다른 패키지의 클래스를 짧은 이름으로 쓰겠다고 미리 알려 두는 문장.
//   문장 끝에는 항상 ; (세미콜론) 을 붙인다
//==================================================================
import java.nio.charset.StandardCharsets;      // 문자 인코딩 상수 모음. UTF_8 사용
import java.nio.file.DirectoryStream;          // 폴더 안 항목을 하나씩 꺼내는 스트림 타입
import java.nio.file.Files;                    // 파일 작업을 실행하는 클래스. 전부 static 메서드
import java.nio.file.Path;                     // 파일 경로를 담는 객체의 타입
import java.nio.file.StandardCopyOption;       // 복사/이동 옵션 상수 모음 (REPLACE_EXISTING 등)
import java.io.IOException;                    // 파일 작업 실패 시 발생하는 예외 클래스
import java.util.List;                         // 목록 타입

/*
 * ==================================================================
 * [NIO 예제 3] 파일 복사, 이동, 삭제, 폴더 목록
 *              (v3 : 모든 실행문을 조각 단위로 분해 설명한 판)
 * ==================================================================
 *
 * ------------------------------------------------------------------
 * 0. 코드를 읽기 전에 : 이 파일의 모든 문장에 적용되는 공통 규칙 3가지
 * ------------------------------------------------------------------
 *
 *   규칙 1. 대입문은 = 의 "오른쪽을 먼저 실행" 하고 결과를 왼쪽 변수에 담는다
 *
 *     Path tempDir = Path.of("temp");
 *     ----  ------   ---------------
 *     자료형 변수명    먼저 실행되는 부분 (결과 = Heap 객체의 주소)
 *
 *   규칙 2. 괄호 안에 또 호출이 있으면 "안쪽(오른쪽 끝) 호출부터" 실행된다
 *
 *     System.out.println( Files.exists(tempReport) );
 *                         ---------------- 1번째 : true/false 가 나온다
 *     ------------------- 2번째 : 그 결과를 출력한다
 *
 *   규칙 3. 점(.)이 이어지면 왼쪽부터 한 단계씩 실행된다
 *
 *     p.getFileName()          1단계 : 파일명 조각(Path)을 꺼낸다
 *     backupDir.resolve(...)   2단계 : 그 조각을 이어 붙인 새 경로를 만든다
 *
 * ------------------------------------------------------------------
 * 1. 용어 정리 (이 예제에 나오는 자바 용어)
 * ------------------------------------------------------------------
 *
 *   버퍼            : 데이터를 옮길 때 중간에 잠시 담아 두는 byte 배열.
 *                     Heap 에 만들어진다. 한 바이트씩 옮기는 것보다
 *                     묶어서 옮기는 것이 훨씬 빠르기 때문에 사용한다.
 *   DirectoryStream : 폴더 안의 항목(Path 객체)을 하나씩 꺼내 주는 스트림.
 *                     NIO 에서 유일하게 내 코드로 닫아야 하는 스트림이다.
 *   try-with-resources : try (스트림 생성) { 사용 } 형태의 문법.
 *                     try 블록이 끝나면 스트림의 close() 가 자동 호출된다.
 *   예외            : 실행 중 오류. 아래 세 가지가 이 예제와 관련 있다.
 *                     FileAlreadyExistsException  이미 있는데 또 만들 때
 *                     NoSuchFileException         없는 것을 지우거나 읽을 때
 *                     DirectoryNotEmptyException  안 비운 폴더를 지울 때
 *
 * ------------------------------------------------------------------
 * 2. copy 와 move 의 차이 (이 예제의 핵심)
 * ------------------------------------------------------------------
 *
 *   Files.copy(원본, 대상)  실행 후
 *     원본 위치 : 파일 그대로 있음
 *     대상 위치 : 파일 새로 생김
 *     디스크의 파일 개수 : 2개
 *
 *   Files.move(원본, 대상)  실행 후
 *     원본 위치 : 파일 없어짐
 *     대상 위치 : 파일 생김
 *     디스크의 파일 개수 : 1개
 *
 *   백업 작업은 원본이 남아야 하므로 copy 를 쓰고,
 *   분류/정리 작업은 원본 위치가 비워져야 하므로 move 를 쓴다.
 *
 * ------------------------------------------------------------------
 * 2-1. IO(java.io) 와의 비교 - 같은 일을 하는 옛날 방식
 * ------------------------------------------------------------------
 *
 *   [이 예제의 NIO 코드와 IO 대응 코드]
 *
 *     NIO (이 예제, 각 1줄)     | IO (java.io 방식)
 *     --------------------------+------------------------------------------
 *     Files.copy                | 전용 메서드 없음. 스트림 2개 + 버퍼 반복을
 *                               | 직접 구현해야 했다 ([2] 에서 상세 비교)
 *     Files.move                | f.renameTo(대상) - 실패 시 false 만 반환
 *     Files.deleteIfExists      | f.delete()      - 실패 시 false 만 반환
 *     Files.newDirectoryStream  | f.listFiles()   - 전체를 배열로 한 번에
 *
 *   요약 : IO 는 "복사 기능이 아예 없고", 나머지도 실패 원인을
 *          알려 주지 않는다. 이 두 가지가 NIO 로 바뀐 핵심 이유다.
 *
 * ------------------------------------------------------------------
 * 3. 실행 순서 (게시판 첨부파일 처리 흐름)
 * ------------------------------------------------------------------
 *
 *   temp 폴더    : 업로드 직후 파일을 임시 보관하는 폴더
 *   upload 폴더  : 검사 후 정식으로 저장하는 폴더
 *   backup 폴더  : 복사본을 보관하는 폴더
 *
 *   [1] 폴더 3개 생성, 파일 2개 준비
 *   [2] temp 에서 upload 로 복사   --> 원본 유지 확인
 *   [3] temp 에서 upload 로 이동   --> 원본 소멸 확인
 *   [4] 파일 이름 변경 (같은 폴더 안 move)
 *   [5] upload 폴더 목록 출력 (DirectoryStream)
 *   [6] upload 전체를 backup 으로 복사
 *   [7] temp 의 파일 삭제
 *   [8] 백업된 파일 내용 확인
 * ==================================================================
 */

//public class Nio03_FileManage2 {  분해
//  public            : 어디서든 쓸 수 있는 공개 클래스
//  class             : 클래스(설계도)를 만든다는 예약어
//  Nio03_FileManage2 : 클래스 이름. 파일 이름(Nio03_FileManage2.java)과 반드시 같아야 한다
//  {                 : 클래스 내용의 시작 (파일 맨 아래의 } 와 짝)
public class Nio03_FileManage {

    //public static void main(String[] args) throws IOException {  분해
    //  public   : JVM 이 밖에서 호출해야 하므로 공개
    //  static   : 객체를 만들지 않고 JVM 이 바로 호출할 수 있게 함
    //  void     : 이 메소드는 돌려줄 값이 없다는 표시
    //  main     : JVM 이 프로그램 시작 시 찾는 약속된 메소드 이름
    //  (String[] args)    : 실행 시 외부에서 전달되는 문자열 배열 (이 예제에선 안 씀)
    //  throws IOException : 파일 작업 실패 예외를 직접 처리하지 않고
    //                       JVM 에 떠넘기겠다는 선언 (실무는 try-catch 가 원칙)
    public static void main(String[] args) throws IOException {

        // ==============================================================
        // [1] 폴더 3개와 파일 2개 준비
        // ==============================================================

        //System.out.println("===== [1] ... =====");  분해
        //  System.out : 화면(콘솔)과 연결된 출력 통로 객체 (PrintStream 타입)
        //  .println   : 괄호 안 내용을 출력하고 줄을 바꾸는 메소드
        //  ("...")    : 출력할 문자열 재료
        System.out.println("===== [1] 폴더와 임시 파일 준비 =====");

        //Path tempDir = Path.of("temp");  분해  (규칙 1 : 오른쪽 먼저)
        //  Path.of("temp") : "temp" 경로 문자열을 담은 Path 객체를 Heap 에 만들고
        //                    그 주소를 돌려준다
        //  Path tempDir =  : 돌아온 주소를 tempDir 변수에 담는다
        //  실행 결과       : 디스크에는 아무 변화 없음 (주소만 적어 둔 상태)
        Path tempDir   = Path.of("temp");     // temp 폴더의 경로 객체
        Path uploadDir = Path.of("upload");   // upload 폴더의 경로 객체 (같은 원리)
        Path backupDir = Path.of("backup");   // backup 폴더의 경로 객체 (같은 원리)

        //Files.createDirectories(tempDir);  분해
        //  Files              : 파일 작업 클래스. 객체 생성 없이 "클래스이름.메소드" 로 호출
        //  .createDirectories : 경로에 적힌 폴더를 만들어라 (끝에 s 붙은 메서드)
        //  (tempDir)          : 어떤 경로를 만들지 재료로 전달
        //  실행 결과          : 디스크에 temp 폴더가 실제로 생긴다
        //
        //  예제 1의 createDirectory 와의 차이 두 가지
        //    차이1 : 폴더가 이미 있어도 예외를 발생시키지 않고 넘어간다
        //            그래서 exists 확인 없이 반복 실행해도 안전하다
        //    차이2 : 상위 폴더가 없으면 상위 폴더까지 순서대로 다 만들어 준다
        //  반복 실행이 기본인 실무에서는 이 메서드를 주로 쓴다
        Files.createDirectories(tempDir);
        Files.createDirectories(uploadDir);   //아래 2줄도 같은 원리 (경로만 다름)
        Files.createDirectories(backupDir);

        System.out.println("temp / upload / backup 폴더 준비 완료");   //안내 출력

        //Path tempReport = tempDir.resolve("report.txt");  분해
        //  tempDir.resolve("report.txt")
        //    : 호출한 tempDir("temp") 뒤에 조각을 이어 붙인 "새" Path 객체를
        //      Heap 에 만들어 돌려준다.  결과 --> "temp/report.txt"
        //      tempDir 자체는 바뀌지 않는다 (새 객체가 하나 더 생길 뿐)
        //      조각 사이 구분 문자는 운영체제에 맞게 자동으로 들어간다
        //  이 시점에도 디스크에는 아직 report.txt 가 없다 (경로만 준비됨)
        Path tempReport = tempDir.resolve("report.txt");
        Path tempNotice = tempDir.resolve("notice.txt");   //같은 원리

        //Files.writeString(tempReport, "8월 업무 보고서\n...", UTF_8);  분해
        //  writeString(경로, 문자열, 인코딩) : 재료 3개를 쉼표로 나눠 전달
        //    경로   : 어느 파일에 쓸지 (tempReport = temp/report.txt)
        //    문자열 : 무엇을 쓸지. \n 은 줄바꿈을 뜻하는 특수 문자
        //    인코딩 : 문자를 바이트로 바꿀 규칙 (UTF-8 : 한글 1자 = 3바이트)
        //  실행 결과 : 파일이 없으면 만들고, 있으면 덮어쓴다.
        //             이제 디스크에 temp/report.txt (42바이트) 가 생겼다
        //  (실제 웹에서는 브라우저가 보낸 데이터가 이 파일에 해당한다)
        Files.writeString(tempReport, "8월 업무 보고서\n작성자 홍길동\n", StandardCharsets.UTF_8);
        Files.writeString(tempNotice, "8월 공지사항\n휴무일 안내\n", StandardCharsets.UTF_8);

        System.out.println("임시 파일 2개 생성 완료");
        System.out.println();   //괄호가 비어 있으면 빈 줄 1개만 출력 (구역 구분용)

        // ==============================================================
        // [2] 복사 - Files.copy
        // ==============================================================
        System.out.println("===== [2] 복사(copy) =====");

        //복사본이 만들어질 위치의 경로 객체 (uploadDir + "report.txt" 결합)
        //결과 --> "upload/report.txt". 아직 디스크에는 없음
        Path uploadReport = uploadDir.resolve("report.txt");

        // --------------------------------------------------------------
        // [스트림 + 메모리 텍스트 모델링] copy 한 줄 안에서 벌어지는 일
        // --------------------------------------------------------------
        //
        //  copy 는 내부에서 "읽기 스트림 + 버퍼 + 쓰기 스트림" 으로 동작한다.
        //  버퍼는 byte 배열이며 Heap 에 만들어진다. (기본 크기 8192바이트)
        //
        //  단계1  두 스트림이 열리고 버퍼가 Heap 에 만들어진다
        //
        //   [디스크 temp/report.txt]  [읽기 스트림]  [Heap 의 버퍼]  [쓰기 스트림]  [디스크 upload/report.txt]
        //    42바이트                  (열림)         byte[8192]      (열림)         0바이트
        //                                            전부 비어 있음
        //
        //  단계2  읽기 : 원본의 바이트가 스트림을 지나 버퍼에 담긴다
        //
        //   [디스크 temp/report.txt]  --->  [Heap 의 버퍼]
        //    42바이트                        앞쪽 42칸이 채워짐
        //                                   (8192칸 중 42칸 사용. 파일이 버퍼보다
        //                                    작아서 1회에 전부 담겼다)
        //
        //  단계3  쓰기 : 버퍼의 바이트가 스트림을 지나 대상 파일에 기록된다
        //
        //   [Heap 의 버퍼]  --->  [디스크 upload/report.txt]
        //    42바이트              42바이트 기록됨
        //
        //  단계4  추가로 읽을 바이트가 없음을 확인하고 두 스트림이 닫힌다
        //         버퍼는 더 이상 참조되지 않으므로 나중에 JVM 이 자동 정리한다
        //
        //  [회차표] 파일이 버퍼보다 클 때는 여러 회차로 나뉜다 (예: 20000바이트)
        //
        //   회차 | 버퍼에 읽어 온 양 | 대상 파일 누적
        //   -----+-------------------+---------------
        //   1    | 8192              | 8192
        //   2    | 8192              | 16384
        //   3    | 3616              | 20000   <- 마지막 회차는 남은 만큼만
        //   4    | 없음(끝 신호)     | 완료
        //
        //  이 모든 단계를 아래 copy 한 줄이 내부에서 실행한다.
        // --------------------------------------------------------------
        //
        // --------------------------------------------------------------
        // [IO vs NIO 비교] 위 내부 동작이 IO 시절에는 전부 내 코드였다
        // --------------------------------------------------------------
        //
        //   IO 에는 복사 전용 메서드가 없어서 아래처럼 직접 짜야 했다
        //
        //     FileInputStream  in  = new FileInputStream("temp/report.txt");
        //     FileOutputStream out = new FileOutputStream("upload/report.txt");
        //     byte[] buf = new byte[8192];              // 버퍼도 직접 준비
        //     int n;
        //     while ((n = in.read(buf)) != -1) {        // 읽기 통로 -> 버퍼
        //         out.write(buf, 0, n);                 // 버퍼 -> 쓰기 통로
        //     }
        //     in.close();                               // 통로 2개를
        //     out.close();                              // 각각 닫는다
        //
        //   [통로 구조는 IO 나 NIO 나 같다]
        //
        //     [디스크 원본] --> [읽기 통로] --> [Heap 의 buf] --> [쓰기 통로] --> [디스크 대상]
        //
        //     IO  : 이 구조를 내 코드 9줄로 만들고 관리한다
        //           (통로 2개 열기, 반복, 통로 2개 닫기 전부 내 책임)
        //     NIO : Files.copy 한 줄. 같은 구조를 내부에서 만들고 관리한다
        //
        //   구조가 같으므로 위 [회차표]의 8192 단위 반복도 두 방식 모두 동일하다.
        //   차이는 "누가 통로를 관리하는가" 뿐이다.
        // --------------------------------------------------------------

        //Files.copy(tempReport, uploadReport, StandardCopyOption.REPLACE_EXISTING);  분해
        //  copy(원본 경로, 대상 경로, 옵션) : 재료 3개를 순서대로 전달
        //  StandardCopyOption.REPLACE_EXISTING
        //    : "옵션 상수 모음 클래스" 안의 REPLACE_EXISTING 상수.
        //      대상 위치에 같은 이름의 파일이 이미 있으면 덮어쓰라는 지정이다
        //
        //    이 옵션이 없으면
        //      첫 실행  : 대상이 비어 있으므로 정상 동작
        //      둘째 실행: 지난 실행의 복사본이 이미 있으므로
        //                 FileAlreadyExistsException 예외 발생, 프로그램 중단
        //    프로그램은 반복 실행이 기본이므로 사실상 항상 붙인다
        //  실행 결과 : 디스크에 upload/report.txt (42바이트 사본) 생성.
        //              원본 temp/report.txt 는 그대로 남는다
        Files.copy(tempReport, uploadReport, StandardCopyOption.REPLACE_EXISTING);

        //System.out.println("복사 완료 : " + tempReport + " -> " + uploadReport);  분해
        //  + 는 왼쪽부터 차례로 이어 붙인다.
        //  tempReport 는 Path "객체" 지만 문자열과 + 되면 자동으로
        //  경로 문자열("temp/report.txt")로 바뀌어 이어진다
        //  출력 예) 복사 완료 : temp/report.txt -> upload/report.txt
        System.out.println("복사 완료 : " + tempReport + " -> " + uploadReport);

        //System.out.println("원본 남아있나? : " + Files.exists(tempReport));  분해  (규칙 2)
        //  1번째 실행 : 괄호 안의 Files.exists(tempReport)
        //               --> 디스크에 그 파일이 있으면 true, 없으면 false
        //  2번째 실행 : "원본 남아있나? : " + true
        //               --> boolean 도 문자열과 + 되면 "true" 글자로 바뀐다
        //  3번째 실행 : 완성된 문자열을 println 이 출력
        System.out.println("원본 남아있나? : " + Files.exists(tempReport));    // true  <- copy 는 원본 유지
        System.out.println("사본 생겼나?   : " + Files.exists(uploadReport));  // true  <- 사본이 새로 생김
        System.out.println();

        // ==============================================================
        // [3] 이동 - Files.move
        // ==============================================================
        System.out.println("===== [3] 이동(move) =====");

        //이동 후 위치의 경로 객체 (upload + "notice.txt" 결합. 디스크엔 아직 없음)
        Path uploadNotice = uploadDir.resolve("notice.txt");

        // --------------------------------------------------------------
        // [디스크 상태 텍스트 모델링] copy 와 move 의 실행 전후 비교
        // --------------------------------------------------------------
        //
        //  copy 실행 전                        copy 실행 후
        //   temp/report.txt    42바이트         temp/report.txt    42바이트 (그대로)
        //   upload/            비어 있음        upload/report.txt  42바이트 (새로 생김)
        //                                       --> 파일 2개
        //
        //  move 실행 전                        move 실행 후
        //   temp/notice.txt    35바이트         temp/notice.txt    없어짐
        //   upload/            report 만        upload/notice.txt  35바이트 (생김)
        //                                       --> 파일 1개, 위치만 변경
        //
        //  같은 디스크 안에서의 move 는 바이트를 옮기지 않는다.
        //  디스크가 관리하는 파일 위치 정보만 바꾸므로 copy 보다 훨씬 빠르다.
        //  아래 exists 출력 두 줄이 이 표의 오른쪽 상태를 증명한다.
        // --------------------------------------------------------------
        //
        // --------------------------------------------------------------
        // [IO vs NIO 비교] 이동
        //
        //   IO  : boolean ok = f.renameTo(new File("upload/notice.txt"));
        //         실패하면 false 만 반환. 원인(권한? 대상 이미 있음?)을 모른다.
        //         운영체제에 따라 다른 드라이브로는 이동이 안 되기도 한다.
        //   NIO : Files.move 는 실패 시 원인이 담긴 예외를 던지고,
        //         드라이브가 달라 위치 정보만 못 바꾸는 경우에는
        //         내부에서 복사 후 삭제로 자동 전환해서라도 이동을 완수한다.
        // --------------------------------------------------------------

        //Files.move(원본 경로, 대상 경로, 옵션)  : copy 와 재료 구성이 같다.
        //차이는 실행 결과뿐 --> 원본이 대상으로 옮겨가고 원래 자리는 비워진다
        Files.move(tempNotice, uploadNotice, StandardCopyOption.REPLACE_EXISTING);

        //Path 2개가 문자열로 자동 변환되어 이어진다 ([2]의 출력과 같은 원리)
        System.out.println("이동 완료 : " + tempNotice + " -> " + uploadNotice);

        //move 의 성질 확인 : 원본 위치는 비었고 대상 위치에만 있다 (1개)
        //(규칙 2 : 괄호 안 exists 가 먼저 실행되어 true/false 가 나온 뒤 출력)
        System.out.println("원본 남아있나? : " + Files.exists(tempNotice));    // false <- move 는 원본 소멸
        System.out.println("목적지 있나?   : " + Files.exists(uploadNotice));  // true
        System.out.println();

        // ==============================================================
        // [4] 이름 변경
        //
        // 이름 변경 전용 메서드는 NIO 에 없다.
        // 같은 폴더 안에서 다른 이름으로 move 하면 결과가 이름 변경이 된다.
        // ==============================================================
        System.out.println("===== [4] 이름 바꾸기 =====");

        //같은 upload 폴더 안에서 "파일명만 다른" 경로 객체를 만든다
        //결과 --> "upload/notice_202608.txt"
        Path renamed = uploadDir.resolve("notice_202608.txt");

        //원본과 대상의 "폴더는 같고 이름만 다른" move --> 결과는 이름 변경
        //실무에서는 파일명 중복을 막기 위해 날짜를 붙여 저장할 때 이렇게 쓴다
        Files.move(uploadNotice, renamed, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("이름 변경 : notice.txt -> notice_202608.txt");
        System.out.println();

        // ==============================================================
        // [5] 폴더 목록 출력 - DirectoryStream
        //
        // 여기서만 스트림 객체를 내 코드로 직접 다룬다.
        // 그래서 이 예제에서 유일하게 try-with-resources 가 나온다.
        // ==============================================================
        System.out.println("===== [5] upload 폴더 목록 =====");

        //try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadDir)) {  분해
        //  try ( 자원 만들기 ) { 사용 }  형태 = try-with-resources 문법
        //    소괄호 ( ) 안 : "닫아야 하는 자원(스트림)" 을 만드는 문장을 적는 자리
        //    효과          : 중괄호 블록이 끝나는 순간, 중간에 예외가 나더라도
        //                    그 자원의 close() 가 반드시 자동 호출된다
        //  DirectoryStream<Path>
        //    : 폴더 항목을 꺼내 주는 스트림 자료형. < > 안의 Path 는
        //      "꺼내 주는 값의 자료형이 Path" 라는 지정이다
        //  Files.newDirectoryStream(uploadDir)
        //    : upload 폴더를 향해 그 스트림(통로)을 여는 메소드
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadDir)) {
            // Files.newDirectoryStream(폴더 경로)
            //   그 폴더 안의 항목을 하나씩 꺼내는 DirectoryStream 을 열어 반환한다
            //   폴더 안 전체를 한 번에 메모리에 올리지 않고 하나씩 꺼내므로
            //   파일이 수만 개인 폴더에서도 메모리 부담이 적다
            //
            // ----------------------------------------------------------
            // [IO vs NIO 비교] 폴더 목록을 메모리에 올리는 방식
            // ----------------------------------------------------------
            //
            //   IO 방식 : File[] arr = folder.listFiles();
            //
            //     [디스크 폴더]              [Heap]
            //     항목 10000개  --한번에-->  File 객체 10000개 + 배열 1개
            //                                (전부 만들어진 뒤에야 반복 시작)
            //
            //   NIO 방식 : DirectoryStream (아래 실제 코드)
            //
            //     [디스크 폴더]              [Heap]
            //     항목 10000개  --1개씩-->   이번 회차의 Path 객체 1개만
            //                                (꺼낼 때마다 하나씩 만들어짐)
            //
            //   항목이 2개인 이 예제에서는 차이가 없지만,
            //   항목이 수만 개면 IO 방식은 메모리를 크게 차지하고
            //   NIO 방식은 회차당 1개씩만 다루므로 부담이 거의 없다.
            //   대신 하나씩 꺼내는 통로이므로 close 가 필요해졌고,
            //   그래서 이 코드만 try-with-resources 로 감싼 것이다.
            // ----------------------------------------------------------

            // ----------------------------------------------------------
            // [스트림 + 메모리 텍스트 모델링] 반복 1회차가 실행되는 순간
            // ----------------------------------------------------------
            //
            //   [Stack]                [Heap]                            [디스크 upload 폴더]
            //   main() 영역
            //     uploadDir  ------->  Path 객체 "upload"                 report.txt        42바이트
            //     stream     ------->  DirectoryStream 객체 (열림)        notice_202608.txt 35바이트
            //     p          ------->  Path 객체 "upload/report.txt"
            //                          (이번 회차에 꺼내진 항목)
            //
            //   회차가 바뀌면 stream 이 다음 항목의 Path 객체를 만들어 주고
            //   p 는 그 새 객체를 가리키게 된다.
            //
            // [반복 회차별 추적표]
            //
            //   회차 | p 가 가리키는 Path         | getFileName() | Files.size(p)
            //   -----+----------------------------+---------------+--------------
            //   1    | upload/report.txt          | report.txt    | 42
            //   2    | upload/notice_202608.txt   | notice_...txt | 35
            //   끝   | 더 꺼낼 항목 없음. 반복 종료, close() 자동 호출
            //
            //   주의
            //     꺼내지는 순서는 운영체제가 정하므로 회차 1, 2 가 바뀔 수 있다
            //     p 에는 폴더명이 포함된 경로가 담긴다
            //     파일명만 쓰려면 getFileName() 을 거쳐야 한다
            // ----------------------------------------------------------

            //for (Path p : stream) {  분해  (향상된 for 문)
            //  stream 이 주는 항목을 1개씩 꺼내 Path p 변수에 담고,
            //  담을 때마다 중괄호 안을 1회 실행한다. 더 없으면 반복 종료
            for (Path p : stream) {

                //System.out.println("파일명 : " + p.getFileName() + ...);  분해
                //  문장이 길어 두 줄에 걸쳐 적었지만 ; 까지가 "한 문장" 이다
                //  (자바는 줄바꿈이 아니라 세미콜론으로 문장 끝을 판단한다)
                //  실행 순서 (규칙 2, 3)
                //    1번째 : p.getFileName()  --> 파일명 조각(Path) 꺼냄
                //    2번째 : Files.size(p)    --> 파일 크기를 정수(long)로 얻음
                //    3번째 : 문자열 + Path + 문자열 + 정수 를 왼쪽부터 이어 붙임
                //    4번째 : 완성된 한 줄을 출력
                //  출력 예) 파일명 : report.txt / 크기 : 42 bytes
                System.out.println("파일명 : " + p.getFileName()
                                 + " / 크기 : " + Files.size(p) + " bytes");

            }   //for 반복의 끝

        }   //try 블록의 끝. 이 중괄호에서 stream.close() 가 자동 호출된다

        System.out.println();

        // ==============================================================
        // [6] 전체 백업 - upload 의 모든 파일을 backup 으로 복사
        // ==============================================================
        System.out.println("===== [6] 전체 백업 =====");

        //int count = 0;  분해
        //  int   : 정수 자료형. 값이 Stack 에 직접 담긴다 (참조 아님)
        //  count : 백업한 파일 수를 세는 변수. 0 에서 시작한다
        //  반복문 "밖" 에 선언하는 이유 : 안에 선언하면 회차가 끝날 때마다
        //  변수가 사라져서 개수가 누적되지 않는다
        int count = 0;

        //upload 폴더를 향해 통로를 다시 연다 ([5]와 같은 문법)
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadDir)) {

            for (Path p : stream) {   //upload 안 항목을 1개씩 꺼내 반복

                //Path target = backupDir.resolve(p.getFileName());  분해  (규칙 3)
                //  1단계 : p.getFileName()
                //          p("upload/report.txt")에서 파일명 조각만 꺼냄 --> "report.txt"
                //  2단계 : backupDir.resolve(1단계 결과)
                //          backup 뒤에 이어 붙인 새 경로 --> "backup/report.txt"
                //  3단계 : 그 새 Path 객체의 주소를 target 에 담는다
                Path target = backupDir.resolve(p.getFileName());

                //백업이므로 copy 를 쓴다 (원본 유지가 백업의 목적)
                //move 를 쓰면 upload 폴더의 파일이 전부 없어져 버린다
                Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);

                //이번에 백업한 파일명을 출력  예) 백업 : report.txt
                System.out.println("백업 : " + p.getFileName());

                //count++;  분해
                //  ++ : 변수의 값을 1 늘리는 연산.
                //       count = count + 1; 과 완전히 같은 의미다
                count++;

            }   //for 반복의 끝

        }   //try 블록의 끝 (stream 자동 close)

        //int 인 count 가 문자열과 + 되어 자동으로 문자열로 바뀐다
        System.out.println("총 " + count + "개 백업 완료");   //예) 총 2개 백업 완료
        System.out.println();

        // ==============================================================
        // [7] 삭제 - Files.deleteIfExists
        // ==============================================================
        System.out.println("===== [7] 삭제 =====");

        //삭제할 파일의 경로 객체 (temp + "report.txt" 결합)
        Path tempTarget = tempDir.resolve("report.txt");

        //boolean deleted = Files.deleteIfExists(tempTarget);  분해  (규칙 1)
        //  1번째 실행 : Files.deleteIfExists(tempTarget)
        //    파일이 있으면 --> 지우고 true 를 돌려준다
        //    파일이 없으면 --> 아무 일 없이 false 만 돌려준다
        //  2번째 실행 : 돌아온 true/false 가 boolean 변수 deleted 에 담긴다
        //
        //  Files.delete(Path 객체) 와의 차이
        //    delete 는 파일이 없으면 NoSuchFileException 예외로 프로그램이 멈춘다
        //    deleteIfExists 는 없어도 예외 없이 false 만 반환한다
        //  반복 실행 시 이미 지워진 상태일 수 있으므로 deleteIfExists 가 안전하다
        //
        //  폴더 삭제 시 주의
        //    폴더 안에 파일이 하나라도 있으면 DirectoryNotEmptyException 발생
        //    폴더를 지우려면 안의 파일을 먼저 전부 지워야 한다
        //
        //  [IO vs NIO 비교] 삭제
        //    IO  : f.delete() - 없어서 실패해도, 권한이 없어 실패해도,
        //          폴더가 안 비어 실패해도 전부 똑같이 false 하나만 반환한다
        //    NIO : delete 는 원인별 예외를 던지고,
        //          deleteIfExists 는 "없음" 만 false 로 처리하고
        //          나머지 실패(권한, 안 빈 폴더)는 여전히 예외로 알려 준다
        boolean deleted = Files.deleteIfExists(tempTarget);

        //boolean 값도 문자열과 + 되면 "true"/"false" 글자로 바뀌어 출력된다
        System.out.println("temp/report.txt 삭제됨? : " + deleted);                    // true
        System.out.println("삭제 후 존재?           : " + Files.exists(tempTarget));   // false <- 정말 지워졌는지 재확인
        System.out.println();

        // ==============================================================
        // [8] 백업 파일 내용 확인
        //
        // 파일 개수만으로는 백업 성공을 확신할 수 없다.
        // 내용까지 같아야 진짜 성공이므로 읽어서 확인한다.
        // ==============================================================
        System.out.println("===== [8] 백업 파일 내용 확인 =====");

        //백업본의 경로 객체 (backup + "report.txt" 결합)
        Path backupReport = backupDir.resolve("report.txt");

        //List<String> lines = Files.readAllLines(backupReport, UTF_8);  분해
        //  List<String>  : String 만 담는 목록 자료형 (< > 안 = 칸의 자료형 지정)
        //  readAllLines  : 파일 전체를 읽어 "한 줄 = 한 칸" 인 List 로 만들어 돌려준다
        //                  줄바꿈은 자르는 기준으로만 쓰이고 칸에는 안 들어간다
        //  실행 결과     : lines 는 2칸 목록
        //                  0번 "8월 업무 보고서" / 1번 "작성자 홍길동"
        List<String> lines = Files.readAllLines(backupReport, StandardCharsets.UTF_8);

        //List 의 칸을 0번부터 하나씩 꺼내 String line 에 담아 반복
        for (String line : lines) {

            System.out.println("내용 : " + line);   //예) 내용 : 8월 업무 보고서

        }   //for 반복의 끝

        System.out.println();

        // ==============================================================
        // [정리]
        // ==============================================================
        System.out.println("===== [정리] =====");
        System.out.println("1. copy메소드 호출 후에는 파일 2개, move메소드 호출 후에는 파일 1개다.");
        System.out.println("2. copy메소드 내부는 읽기 스트림 - Heap 의 버퍼 - 쓰기 스트림 구조다.");
        System.out.println("3. REPLACE_EXISTING 이 없으면 두 번째 실행에서 예외로 멈춘다.");
        System.out.println("4. 같은 폴더 안에서의 파일 이동이  곧 이름 변경이다.");
        System.out.println("5. DirectoryStream 만 try-with-resources 로 직접닫는다.");

    }   //main 메소드의 끝

}   //Nio03_FileManage 클래스의 끝




