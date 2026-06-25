기계어
    컴퓨터가 처리 할 수 있는 0과 1로 이루어진 코드

소스파일
    프로그래밍 언어로 작성한 파일

컴파일
    프로그래밍 언어로 작성된 소스파일을 기계어로 작성된 파일로 변환하는 작업

컴파일러
    소프 파일을 기계어 파일로 변환 시켜주는 프로그램
    자바의 경우 - javac.exe

JDK (Java Development Kit) 소프트웨어
    JDK 소프트웨어 종류
        1. Open JDK
            - 개발 학습용 및 상업용
        2. Oracle JDK
            - 개발, 학습용 무료
            - 상업용 목적으로 사용할 경우 년간 사용료
            - 장기 기술 지원(LTS: Long Term Support) 및 업데이트 제공으로 안정적

-------------------------------------------------------------------------------

Java 개발환경
    Java 개발환경
        자바 언어가 실행 할 수 있는 실행 환경
    
        자바 언어로 만들 수 있는 프로그램의 유형
            데스크톱 응용 SW: JavaSE 플랫폼 (SE: Standard Edition)
            웹 기반 응용 SW: JavaEE 플랫폼 (EE: EnterPrise Edition)
            모바일 기반 응용 SW: JavaME 플랫폼 (ME: Micro Edition)
        
        개발환경을 구성하기 위해서는 JDK를 설치하면 된다.
        JDK내에 JVM (Java Virtual Machine: 자바 가상 머신), Java API등 자바언어를 이용해 개발하는데 필요한 프로그램이 설치 되기 때문
    
    Java 파일을 실행하는 과정
        1. Java 언어로 코드를 작성한다 (.java)
        2. 해당 Java 파일을 컴파일 한다. (JAVA Compiler)
        3. 컴파일이 완료되면 class 파일이 생성된다. (.class)
        4. JVM이 class 파일을 실행한다.
        5. Java 언어로 작성한 코드가 실행된다.
    
    JDK
        Java Development Kit (자바 개발 키트): 자바로 개발하는데 필요한 프로그램이 설치된 키트
        자바 기반의 개발환경에서 가장 기본이 되는 소프트웨어
        java 문법 오류 검증 SW, 변환 SW (컴파일러) 가 필요한데, 이것을 JDK를 설치하면 실행 할 수 있다.
        JDK를 설치하면 JavaSE 개발환경이 구축된다.
            크게 JRE와 개발에 필요한 실행파일 (java.exe 등)의ㅡ java 개발환경 구성요소

    JRE
        Java Runtime Environment로 자바로 작성된 응용프로그램이 실행되기 위한 최소 환경
        JVM과 클래스라이브러리(Java API)를 포함
        JRE가 있어야 Java언어로 작성된 프로그램을 실행할 수 있다.
        Java 언어로 개발된 프로그램을 실행하기 위해선 JDK 전체가 아닌 JRE만 설치하면 된다.

    JVM
        Java Virtual Machine으로 자바를 실행하기 위한 가상 기계(컴퓨터)이다.
        JVM은 컴파일된 class 파일(bytecode로, JVM이 실행할 수 있는 언어로 변환된 파일)을 실행한다.
        일반 애플리케이션은 OS와 바로 붙어 있기 때문에 OS 종속적이다.
        하지만, Java 애플리케이션은 JVM을 한번 거쳐 OS로 전달된다.
        이를 통해 Java 애플리케이션은 JVM을 통해서 어떤 운영체제(윈도우, 맥, 리눅스)에서든지 Java를 실행할 수 있다.
        Java 애플리케이션은 운영체제나 하드웨어가 아닌 JVM 하고만 통신한다.
        JVM은 Java 애플리케이션으로부터 전달받은 명령(class 파일 = bytecode)을 해당 운영체제가 이해할 수 있도록 변환하여 전달한다.
        이때, JVM은 1차 컴파일된 bytecode(class) 파일을 현재 OS에 맞게 한번 더 컴파일하여 .exe 코드로 운영체제에 맞게 변환하여 메모리에 로딩하고 실행한다.
        따라서, Java로 개발하고 JVM을 통해 어떤 운영체제에서라도 독립적으로 Java를 실행할 수 있다.
        단, JVM은 운영체제 종속적이기 때문에 각 운영체제에 맞는 JVM을 설치해야 한다.
    
    Java 언어로 작성된 자바 프로그램의 구동방식 이해
        Java 코드 작성
        javac.exe가 .java 파일을 컴파일 하여 .class 파일 생성 (Java Byte Code: 중간어 코드)
        Run 클릭시 java.exe가 해당 java.class 파일 실행(명령어)을 JVM에 요청
        JVM이 .class 파일을 메모리에 로딩시키고 (Class Loader) OS에 맞게 .exe 코드로 변환하고 실행 (Execute)한다.
        이때 JVM은 4개의 메모리(메서드, 힙, 스택, 리터럴 풀)를 이용해서 클래스를 실행한다.

        Java Source File (.java)
        컴파일 (javac.exe)
        Java Byte Code (.class)
            ==> 여기까지는 IntelliJ에 연결딘 JDK에서 처리 해준다.
        -> Run을 하는 순간 (실행을 하는 순간) (java.exe)
        Class Loader
        Execute
            ==> JVM이 동작을 한다.
        Method Area
        Heap Area Generation
        Stack Area (Call Stack Frame Area) / PC register Native Method Area == Thread
        Runtime Constant Pool (Literal Pool)
            ==> JVM의 Memory model (Runtime Data Area)

    IDE (Intergrated Development Environment: 통합 개발환경)
        프로젝트 생성, 자동 코드 완성, 디버깅 등 개발에 필요한 다양한 기능을 제공 해 주는 프로그래밍 툴

-------------------------------------------------------------------------------

Java Project
    자바 프로젝트를 생성하면 왼쪽 네비게이터에 프로젝트가 생성되고, 이클립스가 참조하는 워크스페이스에 프로젝트 이름으로 디렉토리가 생성된다.
    프로젝트를 확장하면 JRE System Library와 src가 있다.
    src 안에 소스 파일을 생성한다.

    src를 우클릭해서 package를 생성한다.
        (상위 패키지와 하위 패키지를 구분짓는 기호는 도트(.) 이다. 파일 시스템에서는 상위 디렉토리와 하위 디렉토리로 생성된다.)
        패키지는 소스 파일과 바이트코드 파일을 관리하기 위한 디렉토리라고 생각하자.

    패키지 안에서 class를 생성하면 .java 파일 상단에 class파일이 속한 패키지 경로가 작성되어 있다.
    이를 패키지 선언, 패키지 정의 라고 한다.
    패키지를 정의 하는 이유는

    public class 클래스명 {}
        클래스 선언, 클래스 정의
        클래스명은 숫자로 시작 할 수 없고, 공백을 포함해서는 안 된다.
        소스 파일명과 클래스명은 완전히 일치해야 한다.
        클래스명 다음으로 나오는 중괄호 {}를 클래스 블록이라고 하고, 여기에 클래스 정의 내용이 작성된다.

    public static void main(String[] args) {}
        메소드 정의
        main 이라는 이름의 메소드라고 부른다.
        중괄호 {} 를 main 메소드 블록이라고 한다.
        main 메소드는 자바프로그램 실행 진입점 이라고 부른다.
    
    주석 기호 종류
        // 행 주석

        /* 
            범위 주석 
        */ 

        /** 
        * 도큐먼트 주석 
        * javadoc 명령어, API 도큐먼트를 생성
        */

        문자열 ("") 내부에 작성하면 주석 기호를 문자열 데이터로 인식한다.