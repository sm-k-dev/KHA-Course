package file.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import file.dao.FileDAO;



/*
   다운로드 GET 요청 주소 :  http://loaclhost:8181/pro15/download.do?fileRealName=다운로드시킬파일명

*/
@WebServlet("/download.do")
public class FileDownloadServlet extends HttpServlet{

	/* 업로드된 파일이 저장된 폴더이름 */
	private static final String UPLOAD_DIR = "upload"; //<=== 다운로드할 파일이 저장되어 있는 폴더 이름
	
	/* DB작업을 대신 시킬 FileDAO객체의 주소를 저장할 변수 선언*/
	private FileDAO  fileDao;
	
	/* init 메소드 오버라이딩해서 구현할 내용 : FileDAO객체 생성해서 위 변수에 저장 */
	@Override
	public void init() throws ServletException {
		fileDao = new FileDAO();
	}
	
	/* doGet 메소드 오버라이딩해서 구현할 내용 : 다운로드 시키고 DB작업*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*1. 브라우저가 보낸 요청 데이터 중 한글 문자 인코딩 방식 UTF-8 설정 */
		request.setCharacterEncoding("UTF-8");

		/*2. 목록화면(fileList.jsp)에서  a 링크로 전달한 실제 다운로드할 파일명을 request 내장객체 메모리에서 얻기*/
		String fileRealName = request.getParameter("fileRealName");
		
		//참고. <a href="http://loaclhost:8181/pro15/download.do?fileRealName=다운로드시킬파일명">다운로드</a>
	
		/*3. 다운로드할 파일명을 다운로드 요청시 전달하지 않았거나 공백만 전달되었으면 목록 화면(fileList.jsp)으로 되돌려 보내기(재요청)*/
		if(fileRealName == null ||  fileRealName.trim().length() == 0) {	
			response.sendRedirect("list.do"); //FileListServlet 재요청  후 내부적으로  uploadResult.jsp로 포워딩해서 보여줌 
			return;
		}
		/*
		참고. 경로 조작 공격(Path Traversal)이란?

			공격자가 주소창에 아래처럼 입력하는 공격입니다.

				download.do?fileRealName=../WEB-INF/web.xml

			이대로 실행되면 upload폴더가 아니라 그 위 폴더의 web.xml 파일이 다운로드되어
			DB 접속 정보 같은 중요한 서버 설정이 그대로 유출됩니다.

			파일명에는 원래 .. 이나 / 나 \ 가 들어갈 이유가 전혀 없으므로
			이 세 가지가 포함되어 있으면 무조건 차단합니다.
	   */
		/*4. 경로 조작 공격 차단 */
		if(fileRealName.contains("..") || fileRealName.contains("/") || fileRealName.contains("\\") ) {
			response.sendRedirect("list.do");
			return;
		}
		
		/* 5.1. 다운로드할 파일이 저장되어 있는 실제 톰캣 서버 하드디스크 내부의 upload폴더의 절대 경로 얻기*/
		String savePath = request.getServletContext().getRealPath("/" + UPLOAD_DIR);
		
		/* 5.2. 다운로드할 파일의 폴더 경로와 다운로드할 파일명을 결합해서 다운로드할 파일에 접근할 File클래스의 객체 생성 */
		// 참고.  new File(다운로드할폴더경로, 다운로드할파일명); 형태로 만들면 운영체제에 맞는 구분자 기호 / 또는 \\ 가 자동으로 엮어서 경로 저장
		File downFile = new File(savePath, fileRealName);
							   //"c:\workspace\pro15\src\webapp\ upload\다운로드할파일명"
		
		/* 6. 실제 다운로드할 파일이 존재하지 않으면 목록화면(fileList.jsp)으로 되돌려 보내기(포워딩)*/
		if(!downFile.exists()) {
			response.sendRedirect("list.do");
			return; //doGet 메소드 종료 시켜  바로 아래 코드가 실행되지 않도록 하자.
		}
		/*
		참고. 원본 파일명을 다시 조회하는 이유

			같은 이름의 파일을 두 번 업로드하면 서버에는 아래처럼 저장됩니다.

				원본파일명 : 보고서.hwp        실제 업로드된 파일명 : 보고서.hwp
				원본파일명 : 보고서.hwp        실제 업로드된 파일명 : 보고서1.hwp

			사용자에게는 보고서1.hwp 가 아니라 원래 이름인 보고서.hwp 로 저장되어야 하므로
			실제 파일명으로 원본 파일명을 DB에서 다시 찾아옵니다.
	   */		
		/*7.1. file테이블에서 실제 파일명에 해당하는 원본파일명 조회*/
		String fileName = fileDao.selectOriginName(fileRealName);
		
	} //doGet 메소드 끝 
	
}
