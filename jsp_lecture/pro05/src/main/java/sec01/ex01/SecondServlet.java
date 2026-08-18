package sec01.ex01;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SecondServlet
 */
@WebServlet("/second")
public class SecondServlet extends HttpServlet {

	// 호출 순위 1 : SecondServlet 객체가 톰캣 메모리에 처음 올라 갈때 딱 1회만 호출 되는 메소드로
	//				DB 연결, 변수 설정 값 로딩 같은 준비 작업을 여기에 작성한다.
	public void init(ServletConfig config) throws ServletException {
		System.out.println("init 메소드 호출>>>>>");
	}

	// 호출 순위 2 : 클라이언트의 요청 방식에 따라 doGet 또는 doPost 메소드 중 하나를 실행하는 역할을 하는 메소드
	// public void service() {}
	
	// 호출 순위 3: 클라이언트가 GET 요청 할때마다 매번 호출되는 메소드로, service() 메소드가 요청 방식을 보고 자동으로 호출해 준다.
	// request 매개변수 -> 브라우저 요청 정보를 담은 객체 (톰캣이 만들어 전달 해줌)
	// response 매개변수 -> 브라우저로 보낼 응답을 담은 객체 (톰캣이 만들어 전달 해줌)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet 메소드 호출>>>>>");
	}
	
	// 호출 순위 마지막 : 톰캣 서버 종료 등으로 SecondServlet 객체(스레드)가 톰캣에서 제거(소멸)될때 딱 1회 호출 된다.
	//					DB 연결 객체 닫기 같은 정리 작업을 여기에 작성한다.
	public void destroy() {
		System.out.println("destroy 메소드 호출>>>>>");
	}

}
