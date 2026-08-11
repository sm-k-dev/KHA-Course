package nioTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/*
 * ================================================================
 * [연습문제 1] 사원 자료 폴더 점검 프로그램          난이도 ★☆☆
 * ================================================================
 *
 * ■ 상황
 *   회사 서버에 사원 자료를 저장할 폴더를 준비하려 한다.
 *   폴더와 파일이 있는지 먼저 점검하고, 없으면 만드는 프로그램을 작성하시오.
 *
 * ■ 요구사항
 *   (1) "emp" 폴더를 가리키는 Path 객체 empDir 을 만드시오.
 *   (2) "emp" 폴더 안의 "list.txt" 를 가리키는 Path 객체 listFile 을 만드시오.
 *       ※ Path.of("조각1", "조각2") 형태를 사용할 것
 *   (3) listFile 의 파일명 / 상위 폴더 / 절대 경로를 출력하시오.
 *   (4) empDir 폴더가 없으면 만드시오.
 *   (5) listFile 파일이 없으면 만드시오.
 *   (6) 생성 후 아래 4가지를 출력하시오.
 *       - listFile 존재 여부
 *       - empDir 이 폴더인지 여부
 *       - listFile 이 일반 파일인지 여부
 *       - listFile 의 크기(bytes)
 *
 * ■ 사용할 메서드
 *   Path.of() / getFileName() / getParent() / toAbsolutePath()
 *   Files.exists() / createDirectory() / createFile()
 *   Files.isDirectory() / isRegularFile() / size()
 *
 * ■ 완성 프로그램의 상태 변화 모델 (이 그림대로 되면 성공이다)
 *
 *   시작 시점
 *     [Stack]          [Heap]          [디스크]
 *     (변수 없음)      (객체 없음)     (emp 폴더 없음)
 *
 *   (1)(2) 끝난 시점 - Heap 에만 생긴다. 디스크는 그대로다
 *     [Stack]                [Heap]                     [디스크]
 *     empDir   (참조) ---->  Path 객체 "emp"            emp      : 아직 없음
 *     listFile (참조) ---->  Path 객체 "emp/list.txt"   list.txt : 아직 없음
 *
 *   (4)(5) 끝난 시점 - 이번에는 디스크만 바뀐다
 *     [Stack]                [Heap]                     [디스크]
 *     empDir   (참조) ---->  Path 객체 (그대로)         emp 폴더  : 생김
 *     listFile (참조) ---->  Path 객체 (그대로)         list.txt : 생김 (0 byte)
 *
 *   순서 요약
 *     (1)(2) Path 준비   : Heap 에 경로 객체 생성. 디스크 무변화
 *     (3)    경로 분석   : Heap 의 문자열만 분석. 디스크 무변화
 *     (4)(5) 실제 생성   : Files 가 디스크를 바꾸는 유일한 구간
 *     (6)    상태 확인   : Files 가 디스크를 확인만 한다
 *
 * ■ 통로(스트림) 관점
 *   이 문제에서는 통로가 한 번도 열리지 않는다.
 *   경로 준비(Path)와 존재 확인/생성(Files)만 있고
 *   내용을 읽고 쓰는 작업이 없기 때문이다. 통로는 연습문제 2부터 열린다.
 *
 * ■ 예상 출력 결과
 *   ===== 경로 정보 =====
 *   파일명    : list.txt
 *   상위 폴더 : emp
 *   절대 경로 : (실행 위치에 따라 다름)/emp/list.txt
 *
 *   ===== 생성 작업 =====
 *   emp 폴더 생성 완료
 *   list.txt 생성 완료
 *
 *   ===== 최종 확인 =====
 *   파일 존재?     : true
 *   emp는 폴더?    : true
 *   list는 파일?   : true
 *   파일 크기      : 0 bytes
 * ================================================================
 */
public class Exercise01_Student {

    public static void main(String[] args) throws IOException {

        // ---------- (1) emp 폴더 Path 만들기 ----------
        //
        //   실행 후 목표 상태
        //     [Stack]              [Heap]
        //     empDir (참조) ---->  Path 객체 "emp"    (디스크는 아직 그대로)
        //
        // TODO: Path empDir = ?
    	Path empDir = Path.of("emp");  //"emp" 폴더를 가리키는 Path 객체 생성 


        // ---------- (2) emp/list.txt Path 만들기 ----------
        //
        //   조각 결합 모델 : Path.of("조각1", "조각2")
        //
        //     "emp"  +  "list.txt"  -->  "emp/list.txt"
        //                                (조각 사이 구분자는 운영체제가 자동 결정)
        //     +-------+----------
        //     | emp   | list.txt
        //     +-------+----------
        //       0번      1번 조각
        //
        // TODO: Path listFile = ?
    	Path listFile = Path.of("emp", "list.txt");
    	


        // ---------- (3) 경로 정보 출력 ----------
        //
        //   각 메서드가 꺼내는 조각 (전부 Heap 의 문자열 분석. 디스크 접근 없음)
        //     getFileName()    --> 마지막 조각            --> list.txt
        //     getParent()      --> 마지막을 뺀 나머지     --> emp
        //     toAbsolutePath() --> 루트부터 전체 경로     --> (실행 위치)/emp/list.txt
        //
        System.out.println("===== 경로 정보 =====");
        // TODO: 파일명 출력
        System.out.println("파일명 : " + listFile.getFileName()); //    list.txt
        
        // TODO: 상위 폴더 출력
        System.out.println("상위 폴더 : " + listFile.getParent());//     emp
        
        // TODO: 절대 경로 출력
        System.out.println("절대 경로 : " + listFile.toAbsolutePath());
        
        System.out.println();

        // ---------- (4)(5) 폴더와 파일 생성 ----------
        //
        //   판단 흐름 모델 (두 번 실행해도 안 죽는 코드의 핵심)
        //
        //     Files.exists(empDir) 결과는?
        //       |
        //       +-- false (없음) --> createDirectory 실행 --> "생성 완료" 출력
        //       |
        //       +-- true  (있음) --> 만들지 않고 넘어감    --> "이미 존재" 출력
        //
        //   확인 없이 바로 만들면?
        //     첫 실행은 성공하지만 두 번째 실행에서
        //     FileAlreadyExistsException 예외로 프로그램이 멈춘다
        //
        //   순서 주의 : 폴더(4)를 먼저, 파일(5)을 나중에.
        //   emp 폴더가 없는 상태에서 emp/list.txt 를 만들면 예외가 난다
        //
        System.out.println("===== 생성 작업 =====");
        
        // TODO: empDir 이 없으면 createDirectory 로 생성하고 안내 출력       
        if(!Files.exists(empDir)) {
        	Files.createDirectory(empDir); //폴더를 실제로 생성
        	System.out.println("emp 폴더 생성 완료");
        }else {
        	System.out.println("emp 폴더 이미 존재");
        }

        // TODO: listFile 이 없으면 createFile 로 생성하고 안내 출력
        if(!Files.exists(listFile)) {
        	Files.createFile(listFile);  //내용이 비어 있는 파일을 생성 
        	System.out.println("list.txt 생성 완료");
        }else {
        	System.out.println("list.txt 이미 존재");
        }
        System.out.println();

        // ---------- (6) 최종 확인 ----------
        //
        //   출력할 4가지와 예상 결과 (직접 예측해 보고 코드를 짜자)
        //
        //     호출                          | 예상 결과 | 이유
        //     ------------------------------+-----------+----------------------
        //     Files.exists(listFile)        | true      | (5)에서 만들었으므로
        //     Files.isDirectory(empDir)     | true      | 폴더이므로
        //     Files.isRegularFile(listFile) | true      | 일반 파일이므로
        //     Files.size(listFile)          | 0         | 내용 없는 빈 파일이므로
        //
        System.out.println("===== 최종 확인 =====");
        // TODO: 파일 존재 여부 출력
        System.out.println("파일 존재? : " + Files.exists(listFile));
        
        // TODO: empDir 이 폴더인지 출력
        System.out.println("emp는 폴더? : " + Files.isDirectory(empDir));
        
        // TODO: listFile 이 일반 파일인지 출력
        System.out.println("list는 파일? : " + Files.isReadable(listFile));
        
        // TODO: listFile 크기 출력
        System.out.println("파일 크기    :  " + Files.size(listFile) + " bytes");

    }
}



