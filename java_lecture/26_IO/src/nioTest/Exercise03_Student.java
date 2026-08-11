package nioTest;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

/*
 * ================================================================
 * [연습문제 3] 첨부파일 자동 분류 프로그램            난이도 ★★★
 * ================================================================
 *
 * ■ 상황
 *   게시판 첨부파일이 files 폴더에 뒤섞여 쌓여 있다.
 *   확장자에 따라 문서용/기타 폴더로 자동 분류하는 프로그램을 작성하시오.
 *
 * ■ 요구사항
 *   (1) files / doc / etc / backup2 폴더 4개를 만드시오.
 *       ※ 이미 있어도 예외가 나지 않는 메서드를 쓸 것
 *
 *   (2) files 폴더 안에 아래 5개 파일을 만드시오. (내용은 자유)
 *         report.txt / notice.txt / photo.jpg / data.csv / logo.png
 *
 *   (3) files 폴더의 파일 목록을 하나씩 꺼내며 아래 규칙으로 분류하시오.
 *         .txt 로 끝나면  → doc 폴더로 이동(move)
 *         그 외          → etc 폴더로 이동(move)
 *
 *   (4) 이동할 때마다 아래 형식으로 출력하시오.
 *         report.txt → doc 폴더로 이동
 *
 *   (5) 분류가 끝나면 doc 폴더와 etc 폴더의 목록을 각각 출력하고,
 *       각 폴더의 파일 개수를 출력하시오.
 *
 *   (6) doc 폴더의 모든 파일을 backup2 폴더로 복사(copy)하시오.
 *       ※ 이미 같은 이름이 있어도 덮어쓰도록 옵션을 줄 것
 *
 * ■ 사용할 메서드
 *   Files.createDirectories() / writeString()
 *   Files.newDirectoryStream() / move() / copy() / size()
 *   Path.resolve() / getFileName()
 *   String.endsWith(".txt")
 *   StandardCopyOption.REPLACE_EXISTING
 *
 * ■ 반드시 지킬 것
 *   - DirectoryStream 은 통로이므로 try-with-resources 로 열어 자동으로 닫을 것
 *   - 파일명 비교는 p.getFileName().toString() 으로 문자열로 바꾼 뒤 할 것
 *     (Path 끼리는 endsWith 결과가 다르게 나온다)
 *
 * ■ 파일 이동 전체 모델 (이 흐름을 코드로 만드는 문제다)
 *
 *   [디스크 files 폴더]  5개가 뒤섞여 있음
 *     report.txt  notice.txt  photo.jpg  data.csv  logo.png
 *          |
 *          | DirectoryStream 통로로 1개씩 꺼냄 (5회 반복)
 *          v
 *     파일명이 ".txt" 로 끝나는가?
 *          |
 *     +----+----- true ----------+----- false --------------
 *     |                          |
 *     v  Files.move              v  Files.move
 *   [doc 폴더]                 [etc 폴더]
 *     report.txt notice.txt      photo.jpg data.csv logo.png
 *     |
 *     |  Files.copy  (원본은 doc 에 그대로 남는다)
 *     v
 *   [backup2 폴더]
 *     report.txt notice.txt
 *
 *   끝난 뒤 files 폴더는 비어 있어야 한다 (move 이므로)
 *
 * ■ 분류 회차 추적표 - 코드를 짜기 전에 빈칸을 손으로 먼저 채우자
 *   (꺼내는 순서는 운영체제마다 다르다. 아래는 이름순 가정)
 *
 *   회차 | fileName      | endsWith(".txt") | 이동 목적지
 *   -----+---------------+------------------+----------------
 *   1    | "data.csv"    | false            | etc/data.csv     <- 견본
 *   2    | "logo.png"    | (      )         | (           )
 *   3    | "notice.txt"  | (      )         | (           )
 *   4    | "photo.jpg"   | (      )         | (           )
 *   5    | "report.txt"  | (      )         | (           )
 *
 * ■ move 와 copy 선택 기준 모델 (실행 후 파일 개수로 구분)
 *
 *   분류(3) : files 가 비워져야 한다      --> move (원본 소멸, 개수 그대로)
 *   백업(6) : doc 에 원본이 남아야 한다   --> copy (원본 유지, 개수 증가)
 *
 * ■ 예상 출력 결과
 *   ===== 분류 시작 =====
 *   data.csv → etc 폴더로 이동
 *   logo.png → etc 폴더로 이동
 *   notice.txt → doc 폴더로 이동
 *   photo.jpg → etc 폴더로 이동
 *   report.txt → doc 폴더로 이동
 *   ※ 순서는 운영체제에 따라 다를 수 있음
 *
 *   ===== doc 폴더 =====
 *   notice.txt (크기)
 *   report.txt (크기)
 *   doc 파일 수 : 2
 *
 *   ===== etc 폴더 =====
 *   data.csv (크기)
 *   logo.png (크기)
 *   photo.jpg (크기)
 *   etc 파일 수 : 3
 *
 *   ===== 백업 =====
 *   백업 완료 : notice.txt
 *   백업 완료 : report.txt
 *   총 2개 백업
 * ================================================================
 */
public class Exercise03_Student {

    public static void main(String[] args) throws IOException {

        // ---------- (1) 폴더 4개 준비 ----------
        //   폴더 이름 : "files" / "doc" / "etc" / "backup2"
        //   createDirectories (끝에 s) : 이미 있어도 예외가 없다. 반복 실행 안전
        // TODO: filesDir / docDir / etcDir / backupDir Path 만들고 createDirectories


        // ---------- (2) 테스트 파일 5개 만들기 ----------
        //   resolve 결합 모델 : filesDir("files") + "report.txt" --> "files/report.txt"
        //   (filesDir 자체는 안 바뀌고 새 Path 객체가 반환된다)
        // TODO: files 폴더 안에 5개 파일을 writeString 으로 생성
        //       힌트: filesDir.resolve("report.txt") 형태로 경로를 만든다


        // ---------- (3)(4) 확장자로 분류하기 ----------
        System.out.println("===== 분류 시작 =====");

        //   함정 주의 모델 - 이 문제에서 가장 많이 틀리는 지점
        //
        //     p.getFileName()             --> 결과가 Path 타입 "report.txt"
        //     Path 의 endsWith(".txt")    --> 조각 단위 비교라서 항상 false
        //
        //     p.getFileName().toString()  --> 결과가 String "report.txt"
        //     String 의 endsWith(".txt")  --> true    <- 반드시 이 길로 갈 것
        //
        //   1회차 처리 절차 모델
        //     p 꺼냄 --> 파일명을 String 으로 --> endsWith 판단
        //       --> target = (doc 또는 etc).resolve(파일명)
        //       --> Files.move(p, target, REPLACE_EXISTING)
        //       --> "파일명 → 폴더로 이동" 출력
        //
        // TODO: try-with-resources 로 DirectoryStream 을 열고
        //       for (Path p : stream) 반복
        //       파일명을 String 으로 꺼내 endsWith(".txt") 로 판단
        //       doc 또는 etc 로 Files.move
        //       이동 결과 출력

        System.out.println();

        // ---------- (5) 분류 결과 확인 ----------
        //   같은 목록 출력 코드가 doc / etc 두 번 필요하다.
        //   그대로 두 번 써도 되고, 정답처럼 보조 메서드로 분리해도 된다.
        System.out.println("===== doc 폴더 =====");
        // TODO: doc 폴더 목록 출력 + 개수 출력

        System.out.println();

        System.out.println("===== etc 폴더 =====");
        // TODO: etc 폴더 목록 출력 + 개수 출력

        System.out.println();

        // ---------- (6) doc 폴더 백업 ----------
        //   백업 방향 모델 (copy 이므로 doc 은 그대로)
        //     [doc]  report.txt notice.txt  --copy--> [backup2]  report.txt notice.txt
        //   개수 세기 : int 변수를 반복문 "밖"에 선언하고 안에서 ++ 할 것
        System.out.println("===== 백업 =====");
        // TODO: doc 폴더 전체를 backup2 로 copy (REPLACE_EXISTING)
        //       백업 개수 출력

    }
}
