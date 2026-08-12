package nioTest;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * ================================================================
 * [연습문제 2] 성적 처리 프로그램                     난이도 ★★☆
 * ================================================================
 *
 * ■ 상황
 *   수강생 성적 파일을 읽어 총점과 평균을 계산하고,
 *   결과를 새 파일로 저장하는 프로그램을 작성하시오.
 *
 * ■ 요구사항
 *   (1) score.txt 파일을 만들고 아래 내용을 UTF-8로 저장하시오.
 *         홍길동,90,85,95
 *         김철수,70,65,80
 *         이영희,100,95,90
 *       ※ 형식 = 이름,국어,영어,수학
 *
 *   (2) score.txt 를 줄 단위 목록(List)으로 읽으시오.
 *
 *   (3) 각 줄을 쉼표로 잘라 이름과 3과목 점수를 꺼내고,
 *       총점과 평균을 계산하시오. (평균은 소수점 없이 정수로 처리)
 *
 *   (4) 화면에 아래 형식으로 출력하시오.
 *         홍길동 / 총점:270 / 평균:90
 *
 *   (5) 계산 결과를 result.txt 로 저장하시오.
 *       - 첫 줄은 "이름,총점,평균" 이라는 제목 줄을 넣을 것
 *       - 이후 줄은 "홍길동,270,90" 형식
 *       ※ List 를 만들어 Files.write() 로 한 번에 저장할 것
 *
 *   (6) result.txt 를 다시 읽어 화면에 행 번호와 함께 출력하시오.
 *
 * ■ 사용할 메서드
 *   Files.writeString() / readAllLines() / write()
 *   String.split(",") / Integer.parseInt()
 *   ArrayList.add() / List.size() / List.get()
 *
 * ■ 힌트
 *   - 문자열을 숫자로 바꿔야 덧셈이 된다. "90" + "85" 는 "9085" 가 된다.
 *   - 결과를 List<String> 에 담을 때는 문자열로 다시 조립해야 한다.
 *     예) name + "," + total + "," + avg
 *
 * ■ 데이터 이동 전체 모델 (이 길을 코드로 만드는 문제다)
 *
 *   [디스크 score.txt]       [스트림 통로]         [Heap]
 *   바이트 상태          ->  readAllLines     ->  ArrayList (lines)
 *   (홍길동,90,85,95...)     UTF-8 로 문자 복원     0번 "홍길동,90,85,95"
 *                            줄바꿈에서 자름        1번 "김철수,70,65,80"
 *                                                  2번 "이영희,100,95,90"
 *                                                    |
 *                                                    | split(",") + parseInt
 *                                                    v
 *                                                  kor=90 eng=85 mat=95 (int)
 *                                                  total=270  avg=90
 *                                                    |
 *                                                    | 문자열로 재조립 + add
 *                                                    v
 *   [디스크 result.txt]      [스트림 통로]         ArrayList (resultList)
 *   바이트 상태          <-  Files.write      <-  0번 "이름,총점,평균"
 *                            요소마다 줄바꿈 자동   1번 "홍길동,270,90" ...
 *
 *   요약 : 파일의 글자는 통로를 지나 Heap 에서 숫자가 되어 계산되고,
 *          다시 문자열로 조립되어 통로를 지나 파일로 돌아간다.
 *
 * ■ 반복 회차별 추적표 - 코드를 짜기 전에 빈칸을 손으로 먼저 채우자
 *   (따옴표 있음 = String / 따옴표 없음 = int)
 *
 *   회차 | line               | arr[1] | kor  | total | avg
 *   -----+--------------------+--------+------+-------+------
 *   1    | "홍길동,90,85,95"  | "90"   | 90   | 270   | 90     <- 견본
 *   2    | "김철수,70,65,80"  | (    ) | (  ) | (   ) | (  )
 *   3    | "이영희,100,95,90" | (    ) | (  ) | (   ) | (  )
 *
 *   검산 포인트 : 2회차 avg 는 215/3 = 71.66... 이 아니라 71 이다.
 *                int 나눗셈은 소수점 아래를 버린다.
 *
 * ■ 예상 출력 결과
 *   ===== 성적 계산 =====
 *   홍길동 / 총점:270 / 평균:90
 *   김철수 / 총점:215 / 평균:71
 *   이영희 / 총점:285 / 평균:95
 *
 *   ===== result.txt 저장 완료 =====
 *
 *   ===== 저장 결과 확인 =====
 *   1행 : 이름,총점,평균
 *   2행 : 홍길동,270,90
 *   3행 : 김철수,215,71
 *   4행 : 이영희,285,95
 * ================================================================
 */
public class Exercise02_Student {

    public static void main(String[] args) throws IOException {

        Path scorePath = Path.of("score.txt");     // 원본 성적 파일 경로
        Path resultPath = Path.of("result.txt");   // 결과 저장 파일 경로

        // ---------- (1) score.txt 만들기 ----------
        //
        //   만들 문자열 구조 (\n 위치에 주의)
        //     "홍길동,90,85,95\n" + "김철수,70,65,80\n" + "이영희,100,95,90\n"
        //   저장 방향 : [Heap 의 String] ==(통로: Files 가 자동 관리)==> [디스크]
        //
        // TODO: content 문자열을 만들고 Files.writeString 으로 저장
        String content = "홍길동,90,85,95\n"
        				+"김철수,70,65,80\n" 
        				+"이영희,100,95,90\n";

        //score.txt 파일에  위 학생 문자열들을 한번에 기록 
        Files.writeString(scorePath, content, StandardCharsets.UTF_8);
      /*  
        홍길동,90,85,95
        김철수,70,65,80
        이영희,100,95,90
      */
        // ---------- (2) 줄 단위로 읽기 ----------
        //
        //   읽기 방향 : [디스크] ==(통로)==> [Heap 의 ArrayList]
        //   결과 모델 :
        //     lines (참조) --> ArrayList
        //                        0번 --> String "홍길동,90,85,95"
        //                        1번 --> String "김철수,70,65,80"
        //                        2번 --> String "이영희,100,95,90"
        //   줄바꿈 \n 은 자르는 기준으로만 쓰이고 List 에는 안 들어간다
        //
        // TODO: List<String> lines = ?
        List<String> lines = Files.readAllLines(scorePath, StandardCharsets.UTF_8);


        // ---------- (3)(4) 계산하고 출력하기 ----------
        System.out.println("===== 성적 계산 =====");

        // 결과를 담을 목록 (첫 줄에 제목부터 넣어 둔다)
        List<String> resultList = new ArrayList<>();
        
        // TODO: resultList 에 "이름,총점,평균" 제목 줄 추가
        //       (반복문 밖에서 딱 1번만. 안에 넣으면 매 줄마다 제목이 들어간다)
        resultList.add("이름,총점,평균");
 


        //   1회차 처리 절차 모델 (이 순서를 그대로 코드로 옮기면 된다)
        //
        //     line = "홍길동,90,85,95"
        //       |
        //       | split(",")  <- 쉼표에서 자름. 쉼표는 버려진다
        //       v
        //     +----------+------+------+------
        //     | "홍길동"  | "90" | "85" | "95"     <- 전부 String (따옴표 있음)
        //     +----------+------+------+------
        //       arr[0]    arr[1]  arr[2]  arr[3]
        //       |           |
        //       | 그대로    | Integer.parseInt 3회  <- String 을 int 로
        //       v           v
        //     name        kor=90  eng=85  mat=95    <- int (따옴표 없음)
        //                   |
        //                   v
        //                 total = kor + eng + mat   --> 270
        //                 avg   = total / 3         --> 90 (int 나눗셈, 소수점 버림)
        //       |
        //       v  화면 출력 후
        //     resultList.add(name + "," + total + "," + avg)
        //       <- int 가 문자열에 + 되면 자동으로 문자열로 바뀐다 --> "홍길동,270,90"
        //
        // TODO: for 문으로 lines 를 반복하면서
        //       split(",") 으로 자르고
        //       Integer.parseInt() 로 점수 3개를 숫자로 바꾸고
        //       총점과 평균을 구해 화면에 출력하고
        //       resultList 에 "이름,총점,평균" 형식으로 추가
        //for (String line : lines) {  분해  (향상된 for 문)
        //  lines 목록의 칸을 0번부터 하나씩 꺼내 String line 변수에 담고,
        //  담을 때마다 중괄호 안을 1회 실행한다. 꺼낼 칸이 없으면 반복 종료.
        //  회차 예 : 1회차 line = "홍길동,90,85,95", 2회차 line = "김철수,70,65,80" ...]
        
        for(String line  : lines) {
        	
            //String[] arr = line.split(",");  분해
            //  String[]        : String 을 여러 칸에 담는 "배열" 자료형
            //  line.split(",") : line 문자열을 쉼표 위치에서 잘라 배열로 돌려준다
            //                    (쉼표 자체는 버려진다)
            //  실행 결과       : "홍길동,90,85,95" --> 길이 4짜리 배열
            //                    [0]"홍길동" [1]"90" [2]"85" [3]"95"
            //                    배열의 칸 번호(인덱스)는 0 부터 시작한다
        	String[]  arr  = line.split(",");
        	
        	String name = arr[0];  //"홍길동"
        	
        	int kor = Integer.parseInt(arr[1]);    // 국어 점수 90
        	int eng = Integer.parseInt(arr[2]);    // 영어 점수 85
        	int mat = Integer.parseInt(arr[3]);    // 수학 점수 95
        	
        	int total = kor + eng + mat; //총점 
        	
        	int  avg = total / 3;        //평균
        	
        	System.out.println(name + " / 총점:" + total + " / 평균:" + avg);
        	
        	resultList.add(name + "," + total + "," + avg);
        }

        System.out.println();

        // ---------- (5) result.txt 로 저장 ----------
        //   저장 방향 : [Heap resultList] ==(통로)==> [디스크 result.txt]
        //   요소마다 줄바꿈이 자동으로 붙는다 (add 할 때 \n 을 넣지 말 것)
        // TODO: Files.write 로 resultList 저장
        Files.write(resultPath, resultList, StandardCharsets.UTF_8);
        

        System.out.println("===== result.txt 저장 완료 =====");
        System.out.println();

        // ---------- (6) 저장 결과 다시 읽어 확인 ----------
        System.out.println("===== 저장 결과 확인 =====");
        //   행 번호가 필요하므로 향상된 for 가 아니라 인덱스 for 를 쓴다
        //     i = 0 부터 saved.size() 직전까지 / 화면에는 (i + 1) 로 출력
        // TODO: result.txt 를 readAllLines 로 읽고
        //       인덱스 for 문으로 (i+1)행 형식으로 출력
        
        List<String> saved = Files.readAllLines(resultPath, StandardCharsets.UTF_8);
        
        for(int i = 0;  i < saved.size();   i++) {
        	
        	System.out.println((i + 1) + "행 : " + saved.get(i));
        }
        
        

    }
}








