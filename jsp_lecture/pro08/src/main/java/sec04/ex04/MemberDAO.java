package sec04.ex04;

/*
================================================================
MemberDAO 클래스 (Data Access Object : 데이터 접근 전담 객체)
================================================================

[1] 이 클래스의 역할
- 오라클 DBMS 서버의 XE 데이터베이스 안에 있는 t_member 테이블에 접근해서
  데이터베이스 접근 작업(SELECT, INSERT, UPDATE, DELETE)만 전담하는 클래스.
- 판단/계산 같은 비즈니스 로직은 이 클래스가 아니라 Service 계층이 담당한다.
  (Spring 구조 연결 지점 : Controller -> Service -> Repository(DAO) )

[2] 전체 요청/응답 흐름 (이 파일의 위치)

[브라우저] --요청--> [MemberServlet] --listMembers() 호출--> [MemberDAO] --SQL 전송--> [오라클 DBMS(t_member)]
[브라우저] <--응답-- [MemberServlet] <--ArrayList 반환------ [MemberDAO] <--조회 결과-- [오라클 DBMS(t_member)]

[3] JDBC 작업 전체 순서 (이 파일에서 번호로 표시됨)
순서1. DB 연결 정보 4가지를 상수로 준비 (DRIVER, URL, USER, PWD)
순서2. 드라이버 클래스 로딩          -> Class.forName(DRIVER)
순서3. DB 접속(연결 통로 객체 얻기)   -> DriverManager.getConnection(...)
순서4. SQL 실행 객체 얻기            -> con.createStatement()
순서5. SQL 문장 작성                 -> "select * from t_member"
순서6. SQL 전송·실행, 커서 얻기       -> stmt.executeQuery(query)
순서7. 커서를 이동시키며 한 행씩 읽기  -> while(rs.next()) { rs.getString(...) }
순서8. DB 자원 반납                  -> rs.close() -> stmt.close() -> con.close()
*/

import java.sql.*;
//java.sql 패키지의 모든 클래스/인터페이스를 사용하겠다는 선언.
//이 파일에서 실제로 사용하는 것 : Connection, Statement, ResultSet,
//                             DriverManager, Date, SQLException

import java.util.ArrayList;
//조회된 회원 객체들을 담을 가변길이 배열 클래스 ArrayList를 사용하겠다는 선언.

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class MemberDAO {
	
	//톰캣이 context.xml의 <Resource .../> 코드를 읽어 들여  미리만들어둔 커넥션풀(DataSource) 객체의 주소를 저장할 변수 
	private DataSource dataSource;

	// 생성자: 톰캣 JNDI 저장소에서 커넥션풀 (DataSource)을 찾아와 저장
	public MemberDAO() {// <==  new MemberDAO();
		try {
			// JNDI 탐색 시작점 InitialContext객체 생성 후 톰캣 자원구역(java:/comp/env)으로 이동
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:/comp/env");
			
			// context.xml 의 <Resource /> 태그에 작성한 name="jdbc/oracle" 키 이름으로 커넥션풀 꺼내오기
			dataSource = (DataSource) envCtx.lookup("jdbc/oracle");
			
		} catch (Exception e) {
			System.out.println("DataSource 커넥션풀 객체 얻기 실패  : " + e.toString());
		}	
	}
	
	//============================================================================
	// 2. 수정할 회원 한명 조회 (SELECT 한 행, ? 1개)
	//============================================================================
	public MemberVO  modMember(String modId) {
		
		// 조회한 회원 1명의 정보를 담아 반환할 객체
		MemberVO vo = new MemberVO();
		
		// String modId 매개변수로 전달받은 수정할 회원아이디에 해당하는 회원레코드 조회 SELECT문 작성해서 query변수에 저장
		String query = "select * from t_member where id=?";		
		
		try ( Connection con = dataSource.getConnection(); 
				PreparedStatement pstmt = con.prepareStatement(query); ) {
			
			// 첫번재 ? 에 삭제할 회원의 아이디 채우기
			pstmt.setString( 1, modId );
			
			 try ( ResultSet rs = pstmt.executeQuery() ) {
				 // 조회 결과는 최대 1행 -> if로 커서를 1번만 이동
				 if ( rs.next() ) {
					 vo.setId(rs.getString("id"));
					 vo.setPwd(rs.getString("pwd"));
					 vo.setName(rs.getString("name"));
					 vo.setEmail(rs.getString("email"));
					 vo.setJoinDate(rs.getDate("date"));
				 }
			 }
			
		} catch (Exception e) {
			System.out.println("MemberDAO의 modeMember메소드에서 select문 실행 오류 : " + e);
		}	
		
		return vo;	
	}
	
	//========================================================================
	// 3. 새 회원 추가 (INSERT, ? 4개)
	//=========================================================================
	public int addMember(MemberVO  vo) {
		
		int result = 0; //회원가입(insert)에 성공하면 1을 저장시키고, 회원가입(insert)에 실패하면 0을 저장시킬 변수 선언
		
		// 가입날짜는 오라클의 현재날짜 sysdate 예약어 사용해서 insert 문 만들기
		String query = "insert into t_member ( id, pwd, name, email, joindate ) values ( ?, ?, ?, ?, sysdate );";
		
		// INSERT는 조회 결과 (rs)가 없으므로 자원 2개만 소괄호에 생성
		try ( Connection con = dataSource.getConnection(); 
				PreparedStatement pstmt = con.prepareStatement(query); ) {
			
			// ? 4개를 왼쪽부터 순서대로 ( 1 ~ 4 ) 가입값으로 채우기
			pstmt.setString( 1, vo.getId() );
			pstmt.setString( 2, vo.getPwd() );
			pstmt.setString( 3, vo.getName() );
			pstmt.setString( 4, vo.getEmail() );
			
			// 완성된 INSERT 실행 -> 추가된 행 개수(성공 1) 반환
			result = pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			System.out.println("MemberDAO의 addMember 메소드에서 insert문 실행오류: " + e );
		}
		
		return result;
	}
	
	//=============================================================================
	// 5. 회원 한명 삭제 (DELETE, ? 1개)
	//==============================================================================
	public void delMember(String id) {
		
		String query = "delete from t_member where id=?";
		
		try ( Connection con = dataSource.getConnection(); 
				PreparedStatement pstmt = con.prepareStatement(query); ) {			
			
			// 첫번재 ? 에 삭제할 회원의 아이디 채우기
			pstmt.setString( 1, id );
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("MemberDAO의 delMember 메소드에서 delete문 실행 오류: " + e);
		}
	}

	//=========================================================================
	// listMembers() 메소드 정의  :  t_member 테이블의 전체 회원을 조회해 반환하는 메소드
	//=========================================================================
	public ArrayList<MemberVO>  listMembers(){		
		
		ArrayList<MemberVO> list = new ArrayList<MemberVO>(); //조회 결과를 담을 비어 있는 ArrayList 를 생성		
		
		try {		
			/*
		    [핵심 변화] 접속 생성이 아니라 커넥션 풀(DataSource 객체 공간)에서 "빌려오기"		  
		    - dataSource.getConnection()       -> 이미 접속된 연결을 즉시 대여

		    [텍스트 모델링] 빌려오는 순간 모습
		    ==================================================================================
		    [커넥션풀] 연결1 연결2 연결3 ...   -> 연결1을 꺼내 con에 저장
		    [커넥션풀] (빈자리) 연결2 연결3 ...   con --> [연결1 : 사용중]
		*/
			con = dataSource.getConnection();
			
			
			// 실행할 SQL 문장 작성 (전체 회원 조회)
			String query = "select * from t_member";
			
			// SQL(select)을 미리 전달해서 보관 중인  PreparedStatement 실행객체 얻기 
			pstmt = con.prepareStatement(query);
			/*
		    PreparedStatemnt select 실행 객체 메모리 안에 저장된 모습
		    +--------------------------------+
		    | select * from t_member         |
		    +--------------------------------+ 
			 */
			
			//PreparedStatemnt 객체에 보관된 select * from t_member 문장을 Oralce DBMS의 t_member테이블로 전송해서 실행후
			//조회된 표 형태의 결과를 ResultSet 임시 객체 메모리에 담아 ResultSet 객체 자체주소 반환
			//참고. ResultSet 객체 메모리 영역에서 커서 위치는 열 행 줄을 가리키고 있다.
			rs = pstmt.executeQuery(); 
					
			// next() : 커서를 한 행 이동 + 조회된 행이 있으면 true 반환. 조회된 행이 없으면 false로 반복 종료.
			while(rs.next()) {
				
				// 커서가 현재 가리키는 행에서 열(컬럼) 이름으로 조회 값을 꺼낸다.
				// getString("열이름") : 해당 열의 조회 값을 String으로 꺼내는 메소드.
				String id = rs.getString("ID");   		// 1행 예 : "hong"
				String pwd = rs.getString("PWD"); 		// 1행 예 : "1212"
				String name = rs.getString("NAME");		// 1행 예 : "홍길동"
				String email = rs.getString("EMAIL");	// 1행 예 : "hong@gamil.com"				
				// 날짜 열은 getDate()로 꺼내며, 반환 타입은 java.sql.Date 이다.
				// (import java.sql.*; 이므로 여기의 Date는 java.sql.Date)
				Date  joinDate = rs.getDate("JOINDATE"); //1행 예 : new Date("2026-08-19");
				
				//-------------------------------------------------------------------
				//꺼낸 한 행의 조회값 5개를  MemberVO 객체 1개에 저장
				//-------------------------------------------------------------------
				// MemberVO  : 회원 1명의 데이터를 담는 클래스 (DTO/VO 역할)
				// new MemberVO() : 회원 1명 정보 담을 객체 생성
				MemberVO vo  = new MemberVO();
				vo.setId(id); 					//MemberVO객체의 id 인스턴스 변수에 "hong" 저장
				vo.setPwd(pwd);					//MemberVO객체의 pwd 인스턴스 변수에 "1212" 저장
				vo.setName(name);               //MemberVO객체의 name 인스턴스 변수에 "홍길동" 저장
				vo.setEmail(email);             //MemberVO객체의 email 인스턴스 변수에 "hong@gamil.com" 저장
				vo.setJoinDate(joinDate);		//MemberVO객체의 joinDate 인스턴스 변수에 new Date("2026-08-19"); 저장 
				
				//완성된  MemberVO 객체를 ArrayList 배열 끝 칸에 추가 
				list.add(vo);
				
				// 반복이 3회 끝난 후 ArrayList 내부 모습 (주소를 담고 있음)
				// [ new MemberVO(hong), new MemberVO(lee), new MemberVO(kim) ]
				//        0                      1              2         index
				
			} //while 반복문 
			
					
		}catch(Exception e) {			
			e.printStackTrace(); //SQL 오타,  테이블 없음,  접속 끊김 등 실행 중 모든 예외를 받아 출력
		}finally {
			// ----------------------------------------------------
			// 순서8. finally : 성공/예외 발생과 관계없이 "무조건" 실행되는 영역
			// ----------------------------------------------------
			// DB 연결 자원은 예외가 나도 반드시 반납해야 하므로 finally에 작성한다.
			// (반납하지 않으면 DB 서버의 연결 수가 계속 쌓여 톰캣 서버가 느려진다.)
			ResourceClose();
			
		}
		
		// 조회 결과가 담긴 ArrayList배열 주소를 호출한 쪽(MemberServlet 사장님)으로 반환.
		return list; 	// [ new MemberVO(hong), new MemberVO(lee), new MemberVO(kim) ]  
	}

	//==============================================================================
	// updateMember()  : 수정을 위해 입력한 회원 정보를 DB의 테이블에 UPDATE 
	//
	//- memberModForm.html(회원 수정 화면)에서 수정한 값들이 저장된 MemberVO객체를 매개변수로 전달 받아
	//  UPDATE SQL문을 완성한 후~~ DB의 t_member테이블에 저장된 하나의 회원레코드 정보를 수정 시키는 기능의 메소드
	//- 아이디(PK)는 수정 대상이 아니라 "어떤 회원을 수정할지 찾는 조건(where)"으로만 사용한다.
	//==============================================================================
	public int updateMember(MemberVO vo) {		
		//수정(update)에 성공하면 1을 저장하고, 실패하면 0을 저장시킬 result 변수 선언 후 0 초기화 
		int result = 0;
		
		try {
			//순서1. 커넥션풀(DataSource)에서 미리 DB와 연결을 맺은 Connection 객체 빌려오기
			con = dataSource.getConnection();
			
			//순서2. 수정 UPDATE SQL문 만들기 => update 테이블명 set 수정할열명=수정할값,  수정할열명=수정할값, ... where 조건식열명=조건값;
			String query = "update t_member set pwd=?,  name=?, email=? where id=?";
					
			//순서3. query 변수의 update 문자열을 미리 로드한 PreparedStatement 실행 객체 얻기	
			pstmt = con.prepareStatement(query);
					
			//순서3-1. ? 기호 4개를 왼쪽부터 순서대로(1,2,3,4) 수정값과 조건값으로 설정		
			pstmt.setString(1, vo.getPwd()); //첫번째 ?  :  수정시 입력한 비밀번호로 설정
			pstmt.setString(2, vo.getName());//두번째 ?  :  수정시 입력한 이름으로 설정
			pstmt.setString(3, vo.getEmail());//세번째 ? :  수정시 입력한 이메일로 설정
			pstmt.setString(4, vo.getId());   //네번째 ? : where 조건의 회원 아이디로 설정 
							
			//순서4. 완성된 update 문장을 DB의 t_member테이블에 전송해서 실행! 후 결과값을 result 변수에 저장 
			result = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println("MemberDAO의 updateMember메소드 내부의 코드에서 update문 실행 오류 : " + e);
		} finally {
			//순서5. 사용한 메모리들(PreparedStatement객체, Connection객체) 자원해제
			ResourceClose();
		}
		
		//순서6. 수정 성공 1 또는 실패 0을 MemberServlet으로 반환
		return result;
	}
	
	//===============================================================================
	//ResourceClose() :  DB 연결 자원 반납 메소드 (순서8.에서 호출)
	public void ResourceClose() {
		
		try {
			// 닫는 순서 규칙 : 연 순서(con->pstmt->rs)의 "반대"로 닫는다.
			// 이유 : rs는 pstmt를 통해, pstmt는 con을 통해 만들어진
			//        의존 관계이므로 안쪽 자원부터 닫는 것이 안전하다.
			// if(변수 != null) : connDB() 실패로 객체가 안 만들어졌을 수 있으므로
			//                    null 검사 후 닫아야 NullPointerException을 막는다.
			if( rs  != null ) rs.close();   //1) 조회 결과 임시 공간 반납
			if( pstmt != null) pstmt.close(); //2) SQL 실행 객체 반납
			if( con  != null) con.close();  //3) DB 연결 통로 객체 Connection 를 DataSource 커넥션풀 공간으로 반납
		} catch (SQLException e) {			
			e.printStackTrace(); // close() 실패 시(이미 끊긴 연결 등) 발생하는 예외 처리
		}
	}

} //<==== class MemberDAO


/*
    ================================================================
    [예상 동작] MemberServlet에서 dao.listMembers() 호출 시
    ================================================================
    t_member에 3명이 저장되어 있다면 아래 배열이 반환된다.
    list = [ MemberVO(hong, 홍길동, ...), MemberVO(lee, 이순신, ...), MemberVO(kim, 김유신, ...) ]

    [메모리 구조 요약]
    [Stack] list변수 --주소--> [Heap] ArrayList --각 칸의 주소--> [Heap] MemberVO 객체 3개

    ================================================================
    [핵심 정리 3줄]
    ================================================================
    1. DAO는 DB 접근 작업만 전담하고, 비즈니스 로직은 Service 계층이 담당한다.
    2. JDBC 순서 : 드라이버 로딩 -> Connection -> Statement -> executeQuery
                  -> ResultSet(next로 한 행씩) -> close(연 순서의 반대로).
    3. close()는 메모리 제거가 아니라 DB 자원 반납이며, 메모리는 GC가 회수한다.
*/













