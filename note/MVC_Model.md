MVC 패턴 *"역할에 따라 코드를 나누어 관리하자"*

    MVC 모델은 쉽게 말해 "하나의 앱이나 웹사이트를 만들 때, 역할을 깔끔하게 3개의 구역으로 나누어 분업하는 구조"예요.
    우리가 자주 가는 식당(음식점)을 떠올리면 가장 이해하기 쉬워요!
    🍴 식당으로 비유하는 MVC
    구역                    자바 / 웹 개발 역할                                 식당에서의 역할
    Model (모델)            데이터 & 비즈니스 로직(DB 처리, 계산, 데이터 관리)      주방 (요리사)
    View (뷰)               화면 (UI)(HTML/CSS, 사용자에게 보여지는 창)           메뉴판 & 음식 접시
    Controller (컨트롤러)   중재자 / 데이터 흐름 관리(사용자 요청 받기, 명령 전달)    지배인 (서빙 직원)
    
    🔄 손님이 주문해서 음식이 나오기까지의 과정
    
        사용자(손님)가 화면(View)에서 버튼을 클릭해 어떤 요청을 보냅니다. (예: "메뉴판 보고 짜장면 주문할게요!")
        Controller(지배인)가 이 요청을 받아서 판단합니다. "아, 짜장면 주문이 들어왔군."
        Controller(지배인)가 Model(주방)에 "짜장면 하나 만들어주세요"라고 명령합니다.
        Model(주방)은 냉장고(DB)에서 재료를 꺼내 열심히 요리(데이터 처리)를 완성합니다.
        완성된 음식을 받은 Controller(지배인)가 이를 View(접시)에 예쁘게 담아 사용자에게 보여줍니다!
    
    💡 왜 굳이 3개로 나누어서 만들까요?
        서로 방해하지 않아요 (분업화):
            디자이너가 화면 디자인(View)을 싹 바꿔도, 뒤에서 복잡한 계산을 하는 코드(Model)는 건드릴 필요가 없어요.
        유지보수가 정말 편해요:
            계산 방식이나 DB 저장 로직에 버그가 생기면 Model 쪽만 고치면 되고, 버튼 위치나 색상을 바꿀 땐 View만 고치면 되니까 코드가 엉키지 않아요!
        
        📌 한 줄 요약MVC는 화면(View), 데이터 처리(Model), 그리고 이 둘을 **연결해 주는 매니저(Controller)**로 역할을 딱 나눠서 개발하는 똑똑한 방식입니다!

    1. 세 개체의 역할 비교
        [View / Controller]  <--- (DTO) --->  [Service / Business Layer]  <--- (VO / DTO) --->  [DAO]  <--- (SQL) --->  [Database]

        ① DTO (Data Transfer Object: 데이터 전송 객체)
            한 줄 요약: 레이어(Layer) 간에 데이터를 주고받기 위한 "택배 상자"

            역할:
                화면(View)에서 입력받은 데이터를 컨트롤러나 서비스로 보낼 때, 혹은 서버에서 조회한 결과를 화면으로 보낼 때 사용해요.
                순수하게 데이터 이동만을 목적으로 하기 때문에, 비즈니스 로직(복잡한 계산이나 기능)을 포함하지 않고 getter / setter만 가집니다.

            특징:
                setter가 있어 안에 담긴 데이터 값을 도중에 바꿀 수 있습니다 (Mutable).

        ② VO (Value Object: 값 객체)
            한 줄 요약: 데이터의 값 그 자체를 나타내는 "불변(Immutable) 객체"

            역할:
                우리 일상에서의 '만원짜리 지폐'나 '좌표값 (X, Y)'처럼 내부 값이 같으면 같은 객체로 취급하는 데이터 단위예요.
                비즈니스 로직에서 변하지 않는 고정된 데이터 값을 안전하게 다룰 때 사용합니다.

            특징:
                한 번 생성되면 값을 변경할 수 없도록 setter가 없고 getter만 제공합니다 (Read-Only / Immutable).
                두 VO의 값이 같은지 비교하기 위해 equals()와 hashCode() 메서드를 오버라이딩하여 사용해요.

            💡 DTO vs VO 한눈에 차이점 파악하기

            DTO: 목적이 '데이터 이동'. 내용물이 바뀔 수 있음. (택배 상자)
            VO: 목적이 '데이터의 값 자체를 표현'. 내용물을 절대 바꿀 수 없음. (수표/신분증)

        ③ DAO (Data Access Object: 데이터 접근 객체)
            한 줄 요약: 데이터베이스(DB)에 직접 접근해서 CRUD를 수행하는 "DB 전담 일꾼"

            역할:
                DB와 연결하여 데이터를 추가(Create), 조회(Read), 수정(Update), 삭제(Delete) 하는 전용 객체예요.
                Java 코드(Service)와 Database 사이에서 중재자 역할을 해주므로, 비즈니스 로직 쪽에서는 DB 연결 방식이나 SQL문을 몰라도 DAO의 메서드만 호출해서 편리하게 데이터를 가져올 수 있어요.

            특징:
                MyBatis의 Mapper 인터페이스나, JPA의 Repository가 바로 이 DAO 역할을 담당합니다.

    2. MVC 흐름으로 보는 실제 작동 과정
        사용자가 "회원 가입"을 하는 과정을 예로 들어볼게요.
        View => Controller: 사용자가 회원가입 폼에 정보를 입력하고 제출하면, 데이터가 UserDTO라는 상자에 담겨 Controller로 전달됩니다.
        Controller => Service: Controller는 UserDTO를 그대로 Service(비즈니스 로직)로 넘깁니다.
        Service => DAO: Service에서 유효성 검사 등을 마친 후, DB에 저장하기 위해 UserDAO의 insertUser(userDTO) 메서드를 호출합니다.
        DAO => DB: UserDAO가 SQL문을 실행하여 데이터베이스에 사용자를 최종 저장합니다.
        
        📌 요약
            DTO: 계층 사이를 계란 판처럼 데이터를 싸서 안전하게 전달하는 운반체
            VO: 신분증처럼 신뢰할 수 있고 값이 변하지 않는 값 객체
            DAO: DB에 들어가서 직접 데이터를 넣고 빼오는 DB 전담 객체자바의 인터페이스나 클래스 

📂 파일별 MVC 영역 및 역할 구분
    1. Model (모델) 영역
        데이터, 비즈니스 로직(핵심 기능), DB 접근을 담당하는 가장 핵심적인 영역입니다.
        작성하신 파일의 대부분이 이 Model 영역을 단단하게 받쳐주고 있어요!

        Board.java => 데이터 객체 (DTO / Entity / VO)
            게시글 데이터(제목, 내용, 작성자 등)를 담는 틀입니다.

        BoardService.java & BoardServiceImpl.java => 비즈니스 로직 (Service Layer)
            BoardService: 
                어떤 서비스(게시글 등록, 수정, 삭제 등)를 제공할지 정의한 인터페이스입니다.
            BoardServiceImpl: 
                실제 비즈니스 로직을 코드로 구현한 클래스입니다. (필요한 검증이나 계산 등을 처리합니다.)

        BoardRepository.java & MemoryBoardRepository.java => 데이터 접근 (DAO / Repository Layer)
            BoardRepository: 
                데이터를 저장하고 불러오는 규칙을 정의한 인터페이스입니다.
            MemoryBoardRepository: 
                DB 대신 메모리(List, Map 등)에 데이터를 저장하도록 구현한 클래스입니다.
    
    2. Controller (컨트롤러) 영역
        사용자의 요청을 가장 먼저 받아서 적절한 서비스(Model)를 호출하고, 그 결과를 다시 전달해 주는 중재자 역할을 합니다.
        
        BoardController.java (servlet)=> Controller
            게시글 작성 요청이 오면 BoardService를 불러서 일을 시키고, 처리 결과를 받아오는 흐름 제어(라우팅) 역할을 전담합니다.
            
    3. View (뷰) / 진입점 영역
        사용자와 직접 상호작용하는 영역입니다.
        
        BoardMain.java => 진입점 (Main) / 간단한 View 역할
            main() 메서드가 포함된 실행 파일입니다.
            웹 환경(JSP, Thymeleaf 등)이 아닌 콘솔 환경이라면, 이 파일이 메뉴를 보여주고 사용자 입력을 받는 View(화면) 겸 프로그램의 시작점 역할을 맡게 됩니다.
        
    
    💡 한눈에 보는 흐름 정리
        만약 "게시글 쓰기"를 실행한다면 데이터가 이렇게 흐르게 됩니다!
        BoardMain (View) ===(입력)===> BoardController ===(요청)===> BoardServiceImpl (Service) ===(저장)===> MemoryBoardRepository (DAO)
        
        인터페이스(BoardService, BoardRepository)와 구현체(ServiceImpl, MemoryRepository)를 꼼꼼하게 분리해서 다형성까지 잘 고려한 매우 훌륭한 백엔드 계층 구조

    =====================================================================================

    Board.java는 실제 어떠한 로직(계산, DB 저장, 요청 처리 등)을 실행하는 일꾼이 아니라, 게시글이라는 '데이터의 틀(모양)'을 정의하는 클래스예요.

    📦 Board.java는 식당으로 치면 '메뉴용 규격 접시'나 '서식지'예요
        어떤 모양인가요?
        게시글 하나에는 id(글번호), title(제목), content(내용), writer(작성자) 같은 정보가 들어간다고 규격을 정해두는 역할을 합니다.

    무슨 일을 하나요?
        아무런 비즈니스 로직을 수행하지 않고, 오직 데이터를 안전하게 담아두는 용도로만 쓰입니다.

        그래서 아까 다루었던 DTO(Data Transfer Object)나 Entity, 또는 VO의 역할을 수행하는 개체가 바로 이 Board.java랍니다.

    🔄 다른 파일들과의 관계를 보면:
        Board.java (데이터 틀):
            "게시글 데이터는 [글번호, 제목, 내용] 이렇게 생겼어! 나한테 담아서 옮겨!"

        BoardController / BoardService / BoardRepository (일꾼들):
            "오케이, 사용자한테 받은 입력값을 Board라는 박스에 담아서 service로 넘기고, repository에 보내서 저장하자!"

    📌 한 줄 요약
        맞습니다! Board.java는 일하는 애가 아니라, 데이터가 어떻게 생겼는지 모양만 잡아주고 데이터를 담아 나르는 '택배 상자 / 데이터 규격' 역할을 하는 클래스입니다.

    ------------------------------------------------------------------

    1. 사용자가 저장을 누르면 이동하는 순서
        질문: 사용자가 저장을 누르면 BoardMain에서 들어가는 건가요, 아니면 바로 BoardController를 부르나요?
        정답은 BoardMain에서 시작해서 BoardController를 호출합니다!
        
        콘솔 프로그램 기준(웹이 아닐 때)으로 설명해 드릴게요.
            BoardMain (View 역할): 
                화면에 1. 글쓰기  2. 글목록 같은 메뉴를 띄우고, 사용자가 1번을 누르고 제목과 내용을 입력합니다.
            입력받은 데이터를 가지고 BoardMain이 BoardController의 메서드를 호출합니다.(웹 환경으로 발전하면 HTML 페이지에서 버튼을 눌렀을 때 웹 브라우저가 인터넷 전송을 통해 바로 BoardController로 요청을 보내게 됩니다!)
    
    2. 전체 데이터 처리 흐름 (수정 반영)
        작성해 주신 흐름이 90% 이상 맞았어요! 딱 돌아오는 부분(리턴값의 전달)만 정확하게 교정해 볼게요.

        🔄 정확한 순서
            BoardMain => 사용자에게 입력을 받아 BoardController 호출
            BoardController => 비즈니스 로직을 처리하기 위해 BoardService 호출
            BoardService (실제로는 BoardServiceImpl) => DB 작업을 위해 BoardRepository 호출
            BoardRepository (실제로는 MemoryBoardRepository) => 데이터를 메모리(Map/List 등)에 저장하고, 저장 결과를 BoardServiceImpl에게 반환(return)
            BoardServiceImpl => 결과를 정리/가공하여 BoardController에게 전달(return)
            BoardController => 성공 여부나 결과 데이터를 BoardMain에게 전달(return)
            BoardMain => 화면에 "게시글이 성공적으로 저장되었습니다." 출력
            
        💡 교정할 부분:
            "나온 결과를 다시 BoardRepository에 담아서..."라고 하셨는데, Repository가 결과를 만들어내거나 DB에서 꺼내온 후, 그 결과 객체(예: Board 또는 저장 성공 여부 boolean)를 역순으로 상위 계층(Service => Controller => Main)에 return으로 전달해 주는 거예요!
            
    3. DB를 붙여도 MemoryBoardRepository를 쓰나요?
        질문: MemoryBoardRepository는 지금 DB연결을 안 했기 때문에 DB대신 쓰는 거고? 근데 나중에 DB 붙여도 쓰나요?
        진짜 DB를 붙이게 되면 MemoryBoardRepository는 더 이상 쓰지 않거나, 테스트용으로만 쓰게 됩니다!
        대신에 JpaBoardRepository나 JdbcBoardRepository처럼 진짜 DB와 통신하는 새로운 구현 클래스를 만들어서 사용합니다.
        
    🌟 여기서 인터페이스(BoardRepository)의 진가가 나타나요!
        나중에 DB를 도입하더라도, BoardService나 BoardController의 코드는 단 한 줄도 고칠 필요가 없어요.
            인터페이스: BoardRepository (규칙: "save(), findById() 기능이 있어야 해!")
            기존: BoardRepository repository = new MemoryBoardRepository(); (메모리에 저장)
            DB 도입 후: BoardRepository repository = new JpaBoardRepository(); (DB에 저장)
        단지 부품(구현체)을 Memory에서 Db용으로 갈아 끼우기만 하면 전체 시스템이 알아서 DB와 통신하게 돼요. 이렇게 언제든 부품을 쉽게 교체하려고 인터페이스와 구현체(Impl)를 나누어 설계한 것이랍니다!
        
    📌 요약
        BoardMain(입력) => BoardController => BoardService => BoardRepository(저장) 순으로 들어갑니다.
        결과는 역순(Repository => Service => Controller => Main)으로 return되며 전달됩니다.
        나중에 DB를 붙이면 MemoryBoardRepository 대신 DB용 Repository 클래스를 새로 만들어서 갈아 끼우게 됩니다!

 =====================================================

 MVC 디자인 패턴 개발 방식: 개발자들이 정함
    Model: 브라우저로 응답할 데이터 ( VO / DAO )
    View: 요청한 브라우저로 응답할 디자인 화면 ( .html / .jsp )
    Controller: Model과 View를 연결 해 주는 중간 관리자 ( .java / 서블릿 클래스 )

클라이언트의 파일첨부 후 업로드 요청하는 디자인 VIEW(WEB-INF/views/fileUpload.jsp) 요청 흐름

순서1. 브라우저 주소창에 URL: http://localhost:8181/pro15/upload.do 주소 입력 후 GET 방식으로 Controller(FileUploadServlet) 요청

순서2. FileUploadServlet 안의 doGet 메소드 호출 당함, doGet 메소드 안에서 VIEW fileUpload.jsp를 재요청 (포워딩)

순서3. VIEW (WEB-INF/views/fileUpload.jsp) 파일 첨부 후 업로드 요청하는 화면을 만들어서 브라우저에 보여준다