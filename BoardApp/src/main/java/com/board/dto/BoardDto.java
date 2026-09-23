package com.board.dto;
//  ↑ 패키지 = 클래스를 담는 폴더(분류함). com/board/dto 폴더 안에 있다는 뜻.

import java.sql.Timestamp;
//  ↑ 날짜+시간을 함께 저장하는 자료형. DB의 datetime/timestamp 와 짝을 이룬다.

/**
 * ============================================================
 *  BoardDto  (Data Transfer Object · 데이터 운반 그릇)
 * ============================================================
 *  [이 클래스가 하는 일]
 *   - 게시판 글 "1건"의 정보를 통째로 담아 계층 사이를 오간다.
 *     (DAO ↔ Service ↔ Controller ↔ JSP)
 *   - 즉, 데이터를 넣고/빼는 "택배 상자" 역할.
 *
 *  [자바빈(JavaBean) 규칙]  ★ 시험/실무 단골
 *   1) 필드(멤버변수)는 private 로 숨긴다. (캡슐화)
 *   2) 값을 넣는 setter, 꺼내는 getter 를 public 으로 제공한다.
 *   3) 기본 생성자(파라미터 없는 생성자)가 있어야 한다.
 *      → new BoardDto() 로 빈 상자를 만들 수 있어야 하기 때문.
 *
 *  [왜 필드명을 DB 컬럼명과 똑같이 맞추나?]
 *   - DAO에서 rs.getString("name") → dto.setName(...) 처럼
 *     "이름만 봐도 어느 컬럼인지" 헷갈리지 않게 하기 위함.
 *
 *  [JSP의 EL/스크립틀릿에서 값을 꺼낼 수 있는 이유]
 *   - dto.getSubject() 같은 getter 가 있어야 화면에서 값을 읽을 수 있다.
 * ============================================================
 */
public class BoardDto {

    // ─────────────── 필드(멤버변수) : DB tblBoard 테이블의 컬럼과 1:1 대응 ───────────────
    private int num;            // 글 번호 (PK, auto_increment → DB가 자동 부여)
    private String name;        // 작성자 이름
    private String email;       // 이메일
    private String homepage;    // 홈페이지 주소
    private String subject;     // 글 제목
    private String content;     // 글 내용
    private String pass;        // 비밀번호 (수정/삭제 시 본인 확인용)
    private int count;          // 조회수 (글을 읽을 때마다 +1)
    private String ip;          // 작성자 IP (서버가 자동 수집)
    private Timestamp regdate;  // 작성 일시

    // ↓↓↓ 아래 2개는 "답변형 게시판(계층형)"을 위한 핵심 필드 ↓↓↓
    private int pos;            // 정렬 순서값. 값이 작을수록 위쪽에 표시. (답변글 끼워넣기용)
    private int depth;          // 들여쓰기 깊이. 원글=0, 답변=1, 답변의답변=2 ...

    // ─────────────── getter / setter ───────────────
    // 규칙: get필드명() 은 값 반환, set필드명(값) 은 값 저장.
    //       변수명 첫 글자만 대문자로 (num → getNum, setNum)

    public int getNum() { return num; }              // num 값을 꺼낸다
    public void setNum(int num) { this.num = num; }  // this.num(내 필드) ← num(전달값)

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public Timestamp getRegdate() { return regdate; }
    public void setRegdate(Timestamp regdate) { this.regdate = regdate; }

    public int getPos() { return pos; }
    public void setPos(int pos) { this.pos = pos; }

    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }
}
