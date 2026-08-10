
/*
주제 : 현재 작성하고 있는 FileTest02.java 파일이 저장된 디렉터리(폴더) 전체 경로 내부에 있는
      파일 및 폴더 목록 정보 얻어 출력
*/
import java.io.File;
import java.io.IOException;

public class FileTest02 {
	public static void main(String[] args) throws IOException {
		
		//참고
		// .
		// -> 현재 작성하고 있는 FileTest02.java 파일이 저장된 폴더 전체 절대 경로 
		
		//현재 26IO 디렉터리 정보를 얻기 위한 File 클래스의 객체 메모리 생성
		File  fileDir = new File(".");
//		File fileDir = new File("C:\Users\KHBS_D_0\Desktop\workspace_java\26IO\");
		
		//현재 26IO 디렉터리 내부에 만들어져 있는  파일명/디렉터리 정보 문자열들을 모두 String[] 배열에 담아 얻어오기 
		//-> File 클래스의 list() 메소드 사용
		String[] strs = fileDir.list();
		
		for(int i=0;  i<strs.length;  i++) {
			
			System.out.println(strs[i]);
			/*
		    .classpath
			.project
			.settings
			bin
			src
			IOTest00.java
			자바 입출력 IO.pdf
		 */
		} //for
		
		//d.txt 파일에 접근해서 정보를 보기 위해 File 클래스의 객체 메모리 생성
		File  file = new File("C:\\a\\d.txt");
		
		/*
		 *  File 클래스의 delete() 메소드를 호출해서 사용하면 d.txt파일 삭제 후 
		 *  삭제에 성공하면 true 반환, 삭제에 실패하면 false 반환하게 됩니다.
		 */
		if( file.delete() ) { //d.txt 파일 삭제 후 삭제에 성공 했느냐? 라고 조건식
			
			System.out.println("d.txt 파일 삭제 완료");
			
		}else { //d.txt 파일 삭제에 실패 했느냐?
			
			System.out.println("d.txt파일 삭제 실패");
		}
		
		System.out.println("---------------------------------------");
		
		//실제 만들어져 있지 않은 b 디렉터리를 a디렉터리 내부에 새로 만들어 접근하기 위해 File 클래스의 객체 메모리 생성!
		//작성 방법 :  새로 만들 b 디렉터리의 경로를 생성자로 전달해서 File 클래스의 객체 생성
		File file2 = new File("C:\\a\\b");
		/*
		File 클래스에서 제공 해주는 mkdir()메소드를 호출하면
		File 클래스의 객체 생성시 생성자로 전달한 a 디렉터리 내부에 b 디렉터리를 새로 생성해 줍니다.
		
		단! 부모 a 디렉터리가 존재 해야 ~ 생성자로 전달한 새로운 b 디렉터리를 생성 할수 있습니다.
	  */		
		file2.mkdir();  //<-- 디렉터리 생성 
		//file2.delete(); //<-- 디렉터리 또는 파일 삭제 
		  
	   /*	  
		  File 클래스에서 제공 해주는 isDirectory() 메소드는
		  File 클래스의 객체 생성시 전달한 전체경로의 주소가 실제 디렉터리이냐? 라고 물러보는 메소드로
		  디렉터리 이면 ? true 반환하고  디렉터리가 아니면? false 반환 하는 메소드 입니다.
		*/
		if(file2.isDirectory()) {
			//     true
			System.out.println("b는 디렉터리 입니다."); //<--- 출력
		}else {
			System.out.println("b는 파일 입니다.");
		}
		
		file2 = new File("C:\\a\\c");
		
		file2.createNewFile();   // c 파일 새로 생성 후 생성에 성공하면 true 반환 !  실패하면 false 반환 !
		
		/*
		 File 클래스에서 제공 해 주는 isFile() 메소드는
		 File 클래스의 객체 생성시 생성자로 전달한 전체경로의 주소가 실제 파일이냐? 라고 물어보는 메소드로
		 실제 파일이면 ? true 반환하고 파일이 아니면? false를 반환 하는 메소드 입니다.
		*/
		if(file2.isFile()) {
			System.out.println("c는 파일입니다.");
		}else {
			System.out.println("c는 디렉터리이다.");
		}
		
		System.out.println("---------------------------------------------------");
		
		File file3 = new File("C:\\c");
		 /*
		 [Before]
				 C:\
				  ├─ a
				  └─ (c 없음)
		 */		
		//File 클래스에서 제공해 주는 mkdirs() 메소드를 호출하면
		//File 클래스의 생성자로 전달한 디렉터리가 존재하지 않으면? c디렉터리를 생성합니다.
		file3.mkdirs();
		/*
		 mkdirs() 특징 정리
		 --------------------------------------------------
		 1. C:\c 디렉터리가 이미 존재하면
		    → 아무 작업도 하지 않고 true 반환 가능

		 2. C:\c 디렉터리가 존재하지 않으면
		    → 새 디렉터리 생성

		 3. 중간 경로가 없어도 자동 생성 가능
		    예) new File("C:\\x\\y\\z").mkdirs();
		 --------------------------------------------------
		*/
		File file4 = new File("C:\\x\\y\\z");
			 file4.mkdirs();
			 
			 
		

	}

}







