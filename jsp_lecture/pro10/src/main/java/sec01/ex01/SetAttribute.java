package sec01.ex01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// GET 요청 주소: http://localhost:8181/pro10/set

@WebServlet("/set")
public class SetAttribute extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 재료
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter	out	=	response.getWriter();
		
		// 실제작업: 각각의 서블릿 관련 객체 메모리들에 특정 문자열 값을 속성과 함께 바인딩
		String	ctxMesg	=	"ServletContext 객체 메모리에 바인딩할 특정 문자열 값";
		String	sesMesg	=	"HttpSession 객체 메모리에 바인딩할 특정 문자열 값";
		String	reqMesg	=	"HttpServletRequest 객체 메모리에 바인딩할 특정 문자열 값";
		
		// 1. ServletContext 객체 메모리 (웹 프로젝트 pro10 하나당 생성되는 하나의 메모리) 얻기
		ServletContext	servletContext	=	this.getServletContext();
		
		// 2. HttpSession 객체 메모리 (요청한 클라이언트 브라우저창 하나당 생성되는 하나의 메모리) 얻기
		HttpSession	httpSession	=	request.getSession();
		
		// 3. HttpServletRequest 객체 메모리 (클라이언트가 서블릿을 톰캣에 요청하는 순간 톰캣이 생성 해 주는 하나의 메모리) 얻기
		//	얻는 방법: doGet 메소드의 매개변수 request 변수로 전달 받는다.
		
		// 각각의 서블릿관련 객체 메모리 영역들에 바인딩(key와 value를 한쌍의 형태로 묶어서 저장) 하는 방법
		//	==> setAttribute("key", "value");
		
		// 1.1. ServletContext에 바인딩
		servletContext.setAttribute("context", ctxMesg);
		
		// 2.1. HttpSession에 바인딩
		httpSession.setAttribute("session", sesMesg);
		
		// 3.1. HttpServletRequest에 바인딩
		request.setAttribute("request", reqMesg);
	}
}
