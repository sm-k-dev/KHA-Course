package sec02.ex01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
	실행 테스트 흐름
	1. 클라이언트가 login3.html 에서 아이디, 비밀번호 입력 후 로그인 버튼 클릭
	2. LoginServlet에서 아이디와 비밀번호 확인 후 로그인 성공 시 쿠키 생성
	3. 클라이언트가 WelcomeServlet에 접근하면 쿠키를 확인하여 로그인 상태 유지
	4. 클라이언트가 logout을 클릭하면 LogoutServlet에서 쿠키 삭제 후 로그아웃 처리
*/

// LoginServlet 서블릿 클래스 역할: 로그인 요청을 받았을 때 로그인 처리 후 쿠키 생성

@WebServlet("/login3")
public class LoginServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 재료
		// 1. 요청한 데이터 한글 처리
		request.setCharacterEncoding("utf-8");
		// 2. 브라우저로 응답할 데이터 유형 설정 및 한글 처리
		response.setContentType("text/html; charset=utf-8");
		// 3. 브라우저에 응답할 출력스트림 얻기
		PrintWriter	out	=	response.getWriter();
		
		// 실제 작업
		// 1. 사용자가 로그인을 하기 위해 입력한 아이디, 비밀번호 얻기
		//	요약: 요청한 데이터들 얻기
		String	user_id	=	request.getParameter("user_id");
		String	user_pw	=	request.getParameter("user_pw");
		
		// 스토리: 데이터베이스 서버의 테이블에 저장된 아이디, 비밀번호 -> "admin", "1234"가 저장되어 있다고 가정하자.
		
		// 2. 입력한 아이디, 비밀번호와 데이터베이스 서버의 테이블에 저장된 아이디 "admin"과 비밀번호"1234"가 같은지 비교
		if ( "admin".equals(user_id) && "1234".equals(user_pw) ) {
			
			// 2.1. 로그인 성공시키기 위해 Cookie 객체 메모리 생성 (나중엔 Session 메모리 생성) (쿠키명: user_id, 쿠키값: user_id변수에 저장된 입력한 아이디)
			Cookie	userCookie	=	new Cookie("user_id", user_id);
		}
	}

}
