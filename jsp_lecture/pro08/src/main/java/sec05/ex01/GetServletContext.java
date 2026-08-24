package sec05.ex01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
	주제: ServletContext 서블릿 관련 객체 메모리에 바인딩 실습
		(실습에 사용할 파일 2개 - SetServletContext.java, GetServletContext.java)
		
	실습순서
		순서3. 클라이언트가 다른 종류의 엣지 웹브라우저창으 령ㄹ어 주소창에 요청 URL을 입력하여
				GetServletContext 서블릿 클래스를 톰캣 서버에 요청 합니다.
				요청하는 URL -> http://localhost:8181/pro08/cget
		
		순서4. 요청을 받은 GetServletContext class 내부에 개발자가 ServletContext객체 메모리를 공유 받아 얻기 위해
				getServletContext() 메소드 호출 구문을 작성 해 놓는다.
				그리고 getAttribute("key"); 메소드를 호출 하는 코드를 작성 하여
				공유 받은 ServletContext객체 메모리 내부에 바인딩 됐던 ArrayList 배열을 얻어 사용 해 본다.
*/

@WebServlet("/cget")
public class GetServletContext extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 재료
		// 1. 요청한 클라이언트의 브라우저로 응답할 데이터 유형 설정 및 한글처리
		response.setContentType("text/html; charset=utf-8");
		
		// 2. 요청한 클라이언트의 브라우저와 연결된 출력스트림 통로 PrintWriter 얻기
		PrintWriter out = response.getWriter();
		
		// 본격적으로 작업
		// 1. 톰캣 (웹 어플리케이션) 서버가 생성 해 놓은 ServletContext 객체 메모리 주소를 얻어 저장
		//	설명: 현재 개발자가 작성하고 있는 SetServletContext class가 속한 웹프로젝트(pro08) 전체에서
		//		데이터를 공유할 공간 메모리인 ServletContext 객체 메모리를 톰캣으로 부터 얻어 저장
		ServletContext servletContext = this.getServletContext();
		//	참고. ServletContext 객체 메모리는
		//		웹프로젝트(pro08) 전체에서 공유되는 데이터들을 저장(key-value 한쌍 묶어 바인딩) 해 놓고
		//		모든 서버페이지(서블릿 또는 jsp)에서 공유하는 메모리
		
		// 2. getAttribute("key"); 메소드를 호출하는 코드를 작성해서 공유받은 ServletContext객체 메모리 내부에 바인딩됐던
		//		ArrayList 배열을 꺼내와 사용하자
		//	이유: ServletContext객체 메모리는 내부의 자원을 전체 프로젝트 다른 서블릿에게 공유할 수 있는지 보기 위해
		List member = (List)servletContext.getAttribute("member");
		//												key			=> value (ArrayList 배열)
		
		// 3. ArrayList 배열에 저장된 이름과 나이를 얻어 저장 후 브라우저로 응답
		String	name	= (String)member.get(0);	// "이순신" 문자열 얻기
		int		age		= (Integer)member.get(1);	// new Integer(30); 객체를 얻어 오토언박싱해서 30 얻기
		
		/*
			결론 - 이번 예제는 ServletContext 서블릿 관련 공유 객체 메모리 내부 영역을 사용해서
					하나의 웹어플리케이션(pro08) 내부에 만들어 놓은 모든 서블릿(서버페이지)들이 공유해서 사용해야할 데이터가 있을 때
					ServletContext 객체 메모리 내부에 공유해서 사용할 데이터들을 바인딩 해 놓고 얻어 사용하는 예제
					톰캣 서버가 정지되기 전까지 공유되는 메모리
		*/
		
		out.print("GetServletContext 서블릿 클래스가 ServletContext객체 메모리 내부의 ArrayList자원을 공유받아 사용했다. 이름: " + name + ", 나이: " + age);
	}
	
}
