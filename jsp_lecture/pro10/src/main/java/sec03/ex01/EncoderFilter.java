package sec03.ex01;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

/*
	사용자 정의 EncoderFilter 클래스를 만들 때
	반드시 제공되는 Filter 인터페이스 내부에 작성된 추상메소드들 (init, doFilter, destroy)을 강제로 메소드 오버라이딩 해서 만든다.
*/
@WebFilter("/*")
public class EncoderFilter extends HttpFilter implements Filter {
	
	ServletContext	servletContext;
	
	/*
		init 메소드
		- 클라이언트가 웹브라우저를 이용해 LoginTest 서블릿 요청시
			톰캣 서버가 웹프로젝트 하나당 만들어 주는 ServletContext 서블릿 관련 객체 메모리를 하나 얻어
			인스턴스변수에 저장하는 역할
	*/
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("UTF-8 방식으로 인코딩.........");
		this.servletContext	=	fConfig.getServletContext();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("doFilter 메소드가 호출되어 실행중!");
		/* 요청이 들어 올때 마다 이 줄이 콘솔에 찍힌다. (요청 횟수 확인용) */
		
		// ------------------------------------------------------
		// [요청 필터 구간 시작] - 서블릿 실행 전 공통 작업
		// ------------------------------------------------------
		
		/*
			공통작업1 : 한글처리 3종 세트
				모든 서블릿에서 반복하던 코드를 여기 한 곳으로 모아둘 수 있다.
				이제 각 서블릿에서는 이 3줄을 지워도 한글이 깨지지 않는다.
		*/
		request.setCharacterEncoding("utf-8");					// 요청한 데이터 한글 처리
		response.setContentType("text/html; charset=utf-8");	// 응답할 데이터 유형 HTML문서의 데이터이고, 응답할 데이터 한글 처리
		response.setCharacterEncoding("utf-8");					// 톰캣서버가 응답할 한글 글자를 만들때 사용한 문자방식을 UTF-8로 설정 (응답할 데이터 한글처리)
		
		/*
			공통작업2 : 요청 주소 정보 3가지 확인
				ServletRequest 부모 인터페이스 타입에는 주소 관련 추상메소드가 없어서
				(HttpServletRequest)로 형변환 한 뒤 메소드 호출
		*/
		String	contextPath	=	((HttpServletRequest)request).getContextPath();	// 전체 URL 중에서 "/pro10" 컨텐스트 주소 얻기
		String	pathInfo	=	((HttpServletRequest)request).getRequestURI();	// 전체 URL 중에서 "/pro10/login" URI 주소 얻기
		String	realPath	=	request.getRealPath(pathInfo);
		
		/*
			공통작업3 : 처리 시간 측정 - 시작 시각 기록
		*/
		long	begin	=	System.currentTimeMillis();
		/* 현재 시각을 1/1000 초(밀리초) 단위 숫자로 얻는다.
		 	LoginTest 서블릿 실행 "전"의 시각이다. */
		
		/*	시간 차이를 눈으로 확인하기 위한 부하 (일부러 만든 작업) :
		 	*/
		
		chain.doFilter(request, response);
		
		// ---------------------------------------------------------------------------
		// [응답 필터 구간 시작] - LoginTest 서블릿 클래스의 코드 모두 실행 후 공통작업 코드 작성
		// ---------------------------------------------------------------------------
		
		long	end	=	System.currentTimeMillis();
		/* LoginTest 서블릿 실행이 "끝난 후"의 시각을 기록한다. */
		
		System.out.println("작업 수행시간: " + (end - begin) + "ms");
		/* (끝시각 - 시작시각) = 요청 하나를 처리하는 데 걸린 시간.
		 	LoginTest 서블릿 실행 시간을 작동 측정하는 성능 확인 기법이다.*/
	}

	@Override
	public void destroy() {
		
	}

}
