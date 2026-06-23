-- ------------------------------
-- 05-1절 테이블 만들기
-- ------------------------------
/*
 주제 : 실무 기준 데이터베이스 설계와 테이블 만들기

  ============================================================
  1. 데이터베이스 설계란?
  ============================================================

    데이터베이스 설계는 업무에서 발생하는 데이터를
    안전하게 저장하고, 정확하게 조회하고, 쉽게 수정할 수 있도록
    테이블 구조와 데이터 규칙을 정하는 작업입니다.

    실무에서 데이터베이스 설계를 잘못하면 다음 문제가 생깁니다.

      - 같은 데이터가 여러 곳에 중복 저장됩니다.
      - 한 곳만 수정하고 다른 곳은 수정하지 않아 데이터가 서로 달라집니다.
      - 없는 고객의 주문처럼 잘못된 데이터가 저장됩니다.
      - 데이터를 조회할 때 SQL이 복잡해집니다.
      - 데이터가 많아질수록 조회 속도가 느려집니다.
      - 나중에 기능을 추가할 때 테이블을 크게 고쳐야 합니다.

    따라서 테이블을 만들기 전에 반드시 다음을 정해야 합니다.

      1. 어떤 업무 데이터를 저장할 것인가?
      2. 데이터는 어떤 단위로 나눌 것인가?
      3. 각 테이블은 어떤 컬럼을 가져야 하는가?
      4. 각 테이블에서 한 행을 구분하는 기준은 무엇인가?
      5. 테이블끼리는 어떤 관계인가?
      6. 어떤 값은 반드시 입력되어야 하는가?
      7. 어떤 값은 중복되면 안 되는가?
      8. 어떤 값은 특정 범위를 벗어나면 안 되는가?
      9. 데이터를 삭제할 때 연결된 데이터는 어떻게 처리할 것인가?
      10. 자주 조회하는 조건에는 인덱스가 필요한가?


  ============================================================
  2. 실무에서 테이블 설계하는 순서
  ============================================================

    1단계. 업무 기능을 확인합니다.

      예:
        회원가입
        로그인
        상품 등록
        상품 조회
        주문 생성
        주문 취소
        결제
        배송 조회

      기능을 먼저 확인해야 어떤 데이터가 필요한지 알 수 있습니다.


    2단계. 저장해야 할 데이터를 찾습니다.

      예:
        회원가입 기능에는 회원 데이터가 필요합니다.
        상품 등록 기능에는 상품 데이터가 필요합니다.
        주문 생성 기능에는 주문 데이터와 주문 상품 데이터가 필요합니다.

      이 단계에서는 아직 SQL을 작성하지 않습니다.
      먼저 업무에서 필요한 데이터를 빠짐없이 찾습니다.


    3단계. 데이터를 테이블 단위로 나눕니다.

      한 테이블에는 하나의 주제에 대한 데이터만 저장하는 것이 원칙입니다.

      예:
        고객 정보는 Customers 테이블
        상품 정보는 Products 테이블
        주문 정보는 Orders 테이블
        주문에 포함된 상품 정보는 OrderItems 테이블

      고객 정보와 주문 정보를 한 테이블에 섞으면 안 됩니다.
      고객과 주문은 저장 목적, 생성 시점, 수정 시점이 다르기 때문입니다.


    4단계. 각 테이블의 컬럼을 정합니다.

      컬럼은 해당 테이블에서 반드시 관리해야 하는 정보만 넣습니다.

      Customers 테이블 예:
        customer_id
        name
        email
        phone
        address
        created_at

      Products 테이블 예:
        product_id
        product_name
        price
        stock_quantity
        created_at

      Orders 테이블 예:
        order_id
        customer_id
        order_date
        order_status
        total_price

      OrderItems 테이블 예:
        order_item_id
        order_id
        product_id
        quantity
        order_price


    5단계. 기본키를 정합니다.

      기본키는 테이블에서 한 행을 정확히 구분하는 값입니다.

      기본키는 반드시 다음 조건을 만족해야 합니다.

        - 중복되면 안 됩니다.
        - NULL이면 안 됩니다.
        - 시간이 지나도 바뀌지 않는 값이 좋습니다.
        - 한 행을 찾을 때 기준으로 사용할 수 있어야 합니다.

      실무에서는 보통 다음처럼 ID 컬럼을 기본키로 많이 사용합니다.

        customer_id
        product_id
        order_id
        order_item_id

      이름, 전화번호, 주소처럼 바뀔 수 있는 값은 기본키로 적합하지 않습니다.


    6단계. 외래키를 정합니다.

      외래키는 다른 테이블의 기본키를 참조하는 컬럼입니다.
      외래키는 테이블 간 연결 관계를 만듭니다.

      예:
        Orders 테이블의 customer_id는
        Customers 테이블의 customer_id를 참조합니다.

      의미:
        주문 데이터는 반드시 실제 존재하는 고객과 연결되어야 합니다.

      예:
        OrderItems 테이블의 order_id는
        Orders 테이블의 order_id를 참조합니다.

      의미:
        주문상세 데이터는 반드시 실제 존재하는 주문과 연결되어야 합니다.


  ============================================================
  3. 관계 설계
  ============================================================

    1:1 관계

      한 행이 다른 테이블의 한 행과만 연결되는 관계입니다.
      실무에서는 자주 사용하지 않습니다.
      보안 정보, 추가 정보처럼 분리할 이유가 있을 때 사용합니다.


    1:N 관계

      한 테이블의 한 행이 다른 테이블의 여러 행과 연결되는 관계입니다.
      실무에서 가장 많이 사용합니다.

      예:
        고객 1명은 주문을 여러 번 할 수 있습니다.

      설계 방법:
        N쪽 테이블에 1쪽 테이블의 기본키를 외래키로 넣습니다.

      결과:
        Customers.customer_id
        Orders.customer_id


    N:M 관계

      양쪽 데이터가 서로 여러 개씩 연결되는 관계입니다.
      N:M 관계는 직접 연결하지 않고 중간 테이블을 만듭니다.

      예:
        주문 하나에는 여러 상품이 포함될 수 있습니다.
        상품 하나는 여러 주문에 포함될 수 있습니다.

      설계 방법:
        Orders와 Products 사이에 OrderItems 테이블을 만듭니다.

      결과:
        Orders
        Products
        OrderItems


  ============================================================
  4. 제약조건 설계
  ============================================================

    제약조건은 잘못된 데이터가 저장되지 않도록 막는 규칙입니다.

    PRIMARY KEY:
      기본키를 지정합니다.
      중복과 NULL을 허용하지 않습니다.

    FOREIGN KEY:
      다른 테이블과의 연결을 강제합니다.
      존재하지 않는 고객의 주문이 저장되는 것을 막습니다.

    NOT NULL:
      반드시 입력해야 하는 컬럼에 사용합니다.
      예: 이름, 상품명, 주문일자

    UNIQUE:
      중복되면 안 되는 값에 사용합니다.
      예: 이메일, 로그인 아이디

    CHECK:
      값의 조건을 검사합니다.
      예: 가격은 0보다 커야 함, 수량은 1 이상이어야 함

    DEFAULT:
      값을 입력하지 않았을 때 자동으로 들어갈 기본값을 정합니다.
      예: 주문상태 기본값을 '주문완료'로 설정


  ============================================================
  5. 실무형 CREATE TABLE 예시
  ============================================================

    CREATE TABLE Customers (
      customer_id INT PRIMARY KEY,
      name VARCHAR(50) NOT NULL,
      email VARCHAR(100) NOT NULL UNIQUE,
      phone VARCHAR(20),
      address VARCHAR(200),
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE Products (
      product_id INT PRIMARY KEY,
      product_name VARCHAR(100) NOT NULL,
      price INT NOT NULL CHECK (price > 0),
      stock_quantity INT NOT NULL DEFAULT 0,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE Orders (
      order_id INT PRIMARY KEY,
      customer_id INT NOT NULL,
      order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
      order_status VARCHAR(20) NOT NULL DEFAULT '주문완료',
      total_price INT NOT NULL CHECK (total_price >= 0),
      FOREIGN KEY (customer_id)
      REFERENCES Customers(customer_id)
    );

    CREATE TABLE OrderItems (
      order_item_id INT PRIMARY KEY,
      order_id INT NOT NULL,
      product_id INT NOT NULL,
      quantity INT NOT NULL CHECK (quantity > 0),
      order_price INT NOT NULL CHECK (order_price > 0),
      FOREIGN KEY (order_id)
      REFERENCES Orders(order_id),
      FOREIGN KEY (product_id)
      REFERENCES Products(product_id)
    );


  ============================================================
  6. 위 테이블 구조를 실무적으로 해석
  ============================================================

    Customers 테이블:
      고객 기본 정보를 저장합니다.
      email은 로그인이나 고객 식별에 사용될 수 있으므로 UNIQUE를 설정했습니다.
      name과 email은 필수 정보이므로 NOT NULL을 설정했습니다.

    Products 테이블:
      판매할 상품 정보를 저장합니다.
      price는 0보다 커야 하므로 CHECK 조건을 설정했습니다.
      stock_quantity는 입력하지 않으면 0으로 저장되도록 DEFAULT를 설정했습니다.

    Orders 테이블:
      주문의 대표 정보를 저장합니다.
      customer_id를 통해 어떤 고객의 주문인지 연결합니다.
      customer_id는 Customers 테이블에 실제 존재하는 값이어야 합니다.

    OrderItems 테이블:
      주문 안에 포함된 상품 목록을 저장합니다.
      order_id를 통해 어떤 주문에 포함된 상품인지 연결합니다.
      product_id를 통해 어떤 상품이 주문되었는지 연결합니다.
      quantity는 반드시 1 이상이어야 합니다.
      order_price는 주문 당시의 상품 가격을 저장합니다.


  ============================================================
  7. 정보처리기사 실기 문제 풀이 기준
  ============================================================

    문제에서 명사를 찾습니다.
      고객, 상품, 주문, 주문상세처럼 관리 대상이 되는 단어를 찾습니다.
      이 단어들은 테이블 후보입니다.

    각 명사의 정보를 찾습니다.
      고객명, 전화번호, 상품명, 가격, 주문일자처럼 세부 정보가 되는 단어를 찾습니다.
      이 단어들은 컬럼 후보입니다.

    각 테이블의 대표 번호를 정합니다.
      고객번호, 상품번호, 주문번호처럼 한 행을 구분할 수 있는 컬럼을 기본키로 정합니다.

    테이블 간 관계를 찾습니다.
      고객은 주문을 한다.
      주문은 상품을 포함한다.
      이런 문장에서 외래키가 필요한 위치를 찾습니다.

    1:N 관계이면 N쪽에 외래키를 둡니다.
      고객 1명은 주문 여러 개를 가질 수 있으므로
      Orders 테이블에 customer_id를 둡니다.

    N:M 관계이면 중간 테이블을 만듭니다.
      주문과 상품은 N:M 관계이므로
      OrderItems 테이블을 만듭니다.


  ============================================================
  8. 실무에서 반드시 확인하는 항목
  ============================================================

    컬럼명은 의미가 명확해야 합니다.
      name보다 customer_name처럼 쓰면 더 명확한 경우가 있습니다.

    금액 컬럼은 음수가 들어가지 않도록 해야 합니다.
      CHECK 조건을 사용합니다.

    필수 입력값은 NOT NULL로 막아야 합니다.

    중복되면 안 되는 값은 UNIQUE로 막아야 합니다.

    다른 테이블과 연결되는 값은 FOREIGN KEY로 관리해야 합니다.

    상태값은 가능한 값의 범위를 정해야 합니다.
      예: 주문완료, 결제완료, 배송중, 배송완료, 취소

    생성일, 수정일 컬럼을 두면 데이터 추적이 쉬워집니다.
      created_at
      updated_at

    자주 검색하는 컬럼에는 인덱스를 검토합니다.
      예: email, customer_id, order_date

    삭제 정책을 미리 정해야 합니다.
      고객을 삭제할 때 주문도 삭제할지,
      주문은 남기고 고객만 비활성 처리할지 결정해야 합니다.


  ============================================================
  9. 핵심 정리
  ============================================================

    데이터베이스 설계는 업무 데이터를 테이블과 컬럼으로 바꾸는 작업입니다.

    테이블은 하나의 관리 대상을 저장합니다.

    컬럼은 그 대상의 세부 정보를 저장합니다.

    기본키는 한 행을 구분합니다.

    외래키는 테이블과 테이블을 연결합니다.

    제약조건은 잘못된 데이터 입력을 막습니다.

    1:N 관계에서는 N쪽에 외래키를 둡니다.

    N:M 관계에서는 중간 테이블을 만듭니다.

    실무에서는 테이블을 만들기 전에
    기능, 데이터, 관계, 제약조건, 조회 방식, 삭제 정책을 먼저 확인합니다.
*/

/*
	테이블 생성 기본 문법
    
		CREATE TABLE 생성할_테이블_명 (
			  열_명1	데이터_형식 [하나 이상의 제약조건들을 설정하는 자리]
            , 열_명2	데이터_형식 [하나 이상의 제약조건들을 설정하는 자리]
            ...
            
            [추가로 테이블에 제약조건을 설정하는 자리]
        );
        
        -----------------------------------------------------------------
        테이블 주요 구성 요소
			1. 데이터_형식
				MySQL에서 사용할 수 있는 데이터 형식 종류
                숫자형:		INT, FLOAT, DOUBLE, DECIMAL 등
                문자열형:		CHAR, BARCHAR, TEXT, BLOB 등
                날짜/시간형:	DATE, TIME, DATETIME, TIMESTAMP 등
                논리형:		BOOLEAN
			
            2. 열 제약조건
				열에 추가하여 데이터의 유효성을 보장하는 제약조건들
                종류
					PRIMARY KEY: 	테이블 내에서 고유하고 NOT NULL인 기본키 제약조건 설정
                    FORENING KEY: 	다른 테이블의 기본키 제약조건이 설정되어 있는 열의 데이터를 참조하여 테이블간의 관계 설정
                    UNIQUE: 		열의 모든 값이 고유해야 한다.
                    DEFAULT: 		데이터가 INSERT하지 않을 경우 INSERT될 기본값을 설정하는 제약조건
                    CHECK: 			열의 값이 특정조건을 만족해야 열에 값을 저장 할 수 있는 제약 조건 (MySQL 8 이상)
                    AUTO_INCREMENT: 열의 값을 자동으로 증가해서 저장시키는 제약조건 (주로 INT형 열에 사용)
			
            3. 테이블 제약조건
				테이블 수준에서 정의할 수 있는 제약 조건
                종류
					PRIMARY KEY:	하나 이상의 열을 기본키로 설정
                    FORENING KEY:	다른 테이블과의 관계를 설정
                    UNIQUE (열 목록):	여러 열의 조합이 고유해야 함
                    CHECK:			특정 조건을 만족해야 함
*/
/*
	FOREIGN KEY 제약조건을 열에 설정 하면? 어떤 일이 일어 나는가?

		1. 고객(Customers)테이블의 customer_id열과  주문(Orders)테이블의 customer_id열의 관계 이해하기
		Customers테이블에 각 고객에게는 고유한 customer_id열의 아이디가 있습니다. 
		이 customer_id열의 아이디는 고객을 유일하게 식별하는 데 사용됩니다. 
		이 customer_id열에는 **기본 키(Primary Key)** 제약 조건을 설정 했으며,
		현재 생성하고 있는 주문(Orders)테이블에서는 customer_id라는 열에 고객의 아이디가 저장되게 하여
		주문(Orders)테이블에서는 customer_id열은 **외래 키(Foreign Key)** 제약 조건을 설정 했습니다.

		2. 연결고리
		주문(Orders)테이블에서도 고객(Customers)테이블에 저장된 고객 정보를 참조하고 싶습니다. 
		예를 들어, 어떤 주문이 어떤 고객에 의해 이루어졌는지를 알기 위해, 
		주문(Orders)테이블에서도 customer_id라는 열을 만들 수 있습니다.
		여기서 주문(Orders)테이블의 customer_id열의 아이디는 
		고객(Customers) 테이블의 customer_id열의 아이디 데이터를 참조하는 역할을 하게 됩니다. 
		이때 주문 테이블의 customer_id를 **외래 키(Foreign Key)**라고 부릅니다.

		3. 어떻게 동작하나요?
		외래 키로 설정된 주문(Orders)테이블의 customer_id라는 열에 저장된 아이디는 
		"이 주문은 고객(Customers)테이블의 customer_id열의 아이디 3이 만든 주문입니다"라는 정보를 제공합니다.
		즉, 외래 키는 주문정보과 고객정보 간의 연결을 만들어 줍니다.
		주문 테이블의 customer_id가 고객 테이블에 있는 customer_id와 일치해야만 
		해당 주문이 유효하다고 볼 수 있습니다. 이렇게 함으로써 두 테이블 간의 관계가 형성됩니다.

		4. 데이터 무결성 유지
		외래 키를 사용하면 데이터의 일관성을 유지할 수 있습니다. 
		예를 들어, 고객(Customers)테이블에 고객 정보(하나의 행 데이터)가 삭제되면, 
		그 고객의 모든 주문(Orders테이블에 저장된 모든 주문정보 행들)도 함께 자동으로 삭제 처리할 수 있습니다. 
		이처럼 외래 키는 데이터베이스에서 정보를 안전하고 정확하게 관리하는 데 중요한 역할을 합니다.
*/    	

-- 실습 순서1. 인터넷 마켓 (InternetMarket) 데이터베이스 만들기
-- 데이터베이스 만들기
-- 방법	CREATE DATABASE 데이터베이스명;
CREATE DATABASE InternetMarket;

USE InternetMarket;
/*
	고객 정보					-> customers
    제품 정보					-> products
    고객이 주문한 정보			-> orders
    각 주문에 포함된 제품 정보	-> orderItems
*/
-- 실습 순서2. customers 테이블: 고객 정보를 저장
CREATE TABLE Customers(
	-- 고객 ID 저장
    -- int 타입으로 설정하고 자동으로(AUTO_INCREMENT)기능을 사용
    -- 기본 키 (primary key)로 설정하여 고객 ID가 유일하게 저장될 수 있도록 제약 조건 설정
      customer_id	int auto_increment primary key
      
    -- 고객 이름 저장
    -- varchar 타입으로 최대 100자 까지 저장할 수 있게 제약 조건 설정
    -- not null 제약 조건을 통해 이 열은 필수로 값을 추가해서 저장해야 한다는 제약 조건 설정
    , name			varchar(100) not null
    
    -- 고객 이메일 저장
    -- varchar 타입으로 최대 100자 까지 저장할 수 있게 제약 조건 설정
    -- unique 제약조건을 통해 중복을 허용하지 않는 제약 조건 설정
    -- not null 제약 조건을 설정해야 이 열은 필수로 값을 추가해서 저장해야 한다.
    , email			varchar(100) unique not null
    
    -- 고객 전화번호
    -- varchar 타입으로 최대 20자 까지 열에 저장할 수 있게 설정
    -- 이 열의 값은 필수로 추가해서 저장하지 않아도 되기때문에 null 값을 허용하게 not null 제약 조건을 지정하지 않았음
    , phone			varchar(20)
    
    -- 고객 주소
    -- varchar 타입으로 최대 255자까지 열에 저장할 수 있게 설정,
    -- 이 열은 필수로 저장되어야 하는 열의 칸이 아니므로 null값을 허용 한다.
    , address		varchar(255)
    
    -- 고객 등록 날짜와 시간
    -- timestamp 데이터유형('YYYY-MM-DD hh:mm:ss) 으로 열에 저장되게 데이터 유형 설정
    -- insert문에 쓰지 않아도 current_timestamp를 통해 현재 컴퓨터의 시스템 날짜 시간이 기본으로 저장되게 default 설정
    , created_at	timestamp default current_timestamp
);

-- 실습 순서 2-1. Customers 고객 테이블 열 구조 보기
-- 방법 DESC 테이블명
DESC Customers;

-- 실습 순서 2-2. Products 테이블: 제품 정보 저장
CREATE TABLE Products (
	/*
		제품 아이디 저장 -> product_id
        데이터 유형 -> int
        auto_increment
        primary key
    */
      product_id		int auto_increment primary key
	
    /*
		제품의 이름 저장 -> name
        데이터 유형 -> varchar(100)
        필수로 데이터 저장 not null
    */
    , name				varchar(100) not null 
    
    /*
		제품에 대한 설명내용을 저장 -> description
        긴 텍스트를 저장하는 데이터 유형 -> text
        이 열의 값은 필수는 아니므로 null 값 허용
    */
    , description		text
    
    /*
		제품의 가격을 저장 -> price
        최대 10자리 숫자 중 소수점 아래 둘째자리 -> decimal(10,2) 
        필수로 데이터 저장 not null
    */
	, price				decimal(10, 2) not null
    
    /*
		제품의 재고 수량 -> stock
        정수 숫자로 저장 -> int
        필수로 데이터 저장
    */
	, stock				int not null
    
    /*
		제품의 등록 날짜와 시간 -> create_at
        timestamp 데이터 유형으로 설정
        default 제약 조건, current_timestamp로 설정
    */
    , create_at			timestamp default current_timestamp
);

DESC products;

-- 실습 순서2-3. 고객이 주문한 주문정보가 저장되는 주문(Orders)테이블 생성
CREATE TABLE Orders (
	-- 주문 ID 저장
      order_id		int auto_increment primary key
    
    -- 주문한 고객 ID 
    , customer_id	int not null
    
    -- 주문 날짜와 시간 정보를 저장
    , order_date	timestamp default current_timestamp
    
    -- 주문상태 
    -- 보류중, 발송됨, 배송완료, 취소됨 중에 무조건 하나만 저장되게 (열거형 타입-> enum)
    , status		enum('Pending', 'Shipped', 'Delivered', 'Cancelled') default 'Pending'
    
    /*
		참고. 외래 키 (Foreign key)란?
				외래 키는 한 테이블의 열의 데이터가 다른 테이블의 기본키로 설정된 특정 열의 데이터를 참조하기 위해
                이를 통해 두 테이블 간의 관계를 설정하고, 데이터를 연결할 수 있게 하는 키
    */
    -- 현재 Orders 테이블의 customer_id 열에 외래 키 제약 조건을 설정한다.
    -- Orders 테이블의 customer_id열의 값은 Customers 테이블의 customer_id 열의 값을 참조한다.
    -- 이를 통해서 Orders 테이블과 Customers 테이블 간의 1대다 관계를 설정 할 수 있다.
    , foreign key (customer_id) references Customers(customer_id)
);

-- 실습 순서2-4. 각 주문에 포함된 제품 정보를 저장할 OrderItems테이블 생성
CREATE TABLE OrderItems (
	-- 주문 항목 ID를 저장할 열
      order_item_id		int auto_increment primary key
    
    -- 관련된 주문의 ID를 저장할 열
    , order_id			int not null
    
    -- 주문 항목에 해당하는 제품의 ID를 저장하는 열
    , product_id		int not null
    
    -- 주문 한 제품의 수량을 저장하는 열
    , quantity			int not null
    
    -- 주문 한 제품의 가격을 저장하는 열
    , price				decimal(10, 2) not null
    
    /*
		order_id 열에 대한 외래키 제약조건 설정
        OrderItems 테이블(주문 항목)의 order_id 열 값은 Orders 테이블(주문)의 order_id열 값을 참조한다.
        이는 주문 항목이 어떤 주문정보에 속하는지를 나타내는 중요한 연결고리 이다.
        즉, OrderItem테이블의 order_id 열값은 반드시 Orders 테이블에 존재하는 유효한 order_id열 값과 같아야 한다.
        이렇게 함으로, 데이터베이스에서 잘못된 정보가 저장되는 것을 미리 방지 할 수 있다.
        만약 Orders 테이블에서 특정 주문 정보가 삭제 된다면, 그 주문 정보에 관련된 모든 주문 항목도 함께 삭제 처리 할 수 있도록 설정 되어 있어
        데이터의 일관성을 유지 할 수 있다.
    */
    , foreign key (order_id) references Orders(order_id)
    
    /*
		OrderItems 테이블에 만들어져 있는 product_id 열에 외래키 (foreign key) 제약조건 설정
        이렇게 설정하면 OrderItems 테이블에 product_id 열에 대한 값은
        Products 테이블에 PK로 설정된 product_id 열에 대한 값을 참조한다.
        이렇게 함으로, 잘못된 제품 정보가 입력되는 것을 방지 할 수 있다.
        만약 Products 테이블에서 특정 제품이 삭제된다면 그 제품에 해당되는 모든 주문항목 정보도
        OrderItems 테이블에서 행단위로 함께 삭제 되어 데이터의 일관성을 유지할 수 있다.
    */
    , foreign key (product_id) references Products(product_id)
);

-- 실습 순서3. 더미 데이터 추가 (테스트할 행단위의 데이터 추가)

-- 실습 순서3-1. Customers(고객)테이블에 더미 데이터 추가 
-- 고객정보 (이름, 이메일, 전화번호, 주소)를 포함하여 5명의 고객 추가 
INSERT INTO Customers (name, email, phone, address) VALUES
('홍길동', 'hong@example.com', '010-1234-5678', '서울시 강남구'),
('김철수', 'kim@example.com', '010-2345-6789',  '서울시 송파구'),
('이영희', 'lee@example.com', '010-3456-7890',  '부산시 해운대구'),
('박준형', 'park@example.com', '010-4567-8901', '대구시 중구'),
('최지우', 'choi@example.com', '010-5678-9012', '인천시 남구');

-- 실습 순서3-2. Products(제품) 테이블에 더미 데이터 추가
-- Products (제품): 5가지 제품의 이름, 설명, 가격 및 재고 수량을 추가했습니다.
INSERT INTO Products (name, description, price, stock) VALUES
('스마트폰', '최신형 스마트폰, 128GB', 799000.00, 50),
('노트북', '고성능 게이밍 노트북, 16GB RAM', 1999000.00, 30),
('무선 이어폰', '액티브 노이즈 캔슬링 기능', 150000.00, 100),
('스마트워치', '건강 모니터링 기능이 탑재된 스마트워치', 299000.00, 70),
('태블릿', '10인치 태블릿, 64GB', 450000.00, 40);

-- 실습 순서3-3. Orders(주문)테이블에 더미 데이터 추가 
-- Order(주문) : 각 주문에 대해 고객ID, 주문 날짜, 상태를 포함하여 주문정보 추가
INSERT INTO Orders(customer_id, order_date, status) VALUES
(1, '2024-10-01 10:30:00','Pending'),
(2, '2024-10-02 11:00:00', 'Shipped'),
(1, '2024-10-03 12:15:00', 'Delivered'),
(3, '2024-10-04 14:45:00',  'Cancelled'),
(2, '2024-10-05 16:00:00',  'Pending');



-- 실습 순서3-4. OrderItems(주문 항목) 테이블에 더미 데이터 추가
-- OrderItems (주문 항목): 각 주문에 포함된 제품 정보를 입력했습니다. 
-- 각 주문과 관련된 제품 ID, 수량, 가격을 설정했습니다. 
-- 주문 order_id는 Orders 테이블에서의 order_id열의 값과 연결되도록 설정했습니다.
INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 799000.00),  -- 홍길동이 스마트폰 1개 주문
(1, 3, 2, 150000.00),  -- 홍길동이 무선 이어폰 2개 주문
(2, 2, 1, 1999000.00), -- 김철수가 노트북 1개 주문
(3, 4, 1, 299000.00),  -- 홍길동이 스마트워치 1개 주문
(4, 5, 1, 450000.00),  -- 이영희가 태블릿 1개 주문
(5, 2, 2, 1999000.00); -- 김철수가 노트북 2개 주문

-- ----------------------------------------------------------------------------------------

-- 실습4. 'InternetMarket' 데이터베이스의 테이블에서 사용할 수 있는 'SELECT' 문제 10개

-- 문제1. 모든 고객 정보 조회
-- 고객(Customers) 테이블에서 고객의 정보를 조회 하는 SQL 쿼리 작성
select	*
from	Customers;

-- 문제2. 특정 제품 가격 조회
-- Products 테이블에서 '스마트폰'의 가격을 조회하는 SQL 쿼리 작성
select	name, price
from	Products
where	name = '스마트폰';

-- 문제3. 고객 이름과 이메일 조회
-- 고객(Customers) 테이블에서 고객의 이름과 이메일 주소정보만 조회하는 SQL 쿼리 작성
select	name, email
from	Customers;

-- 문제4. 주문 상태가 'Pending'인 주문 조회
-- Orders 테이블에서 주문 상태가 'Pending'인 주문의 정보(모든열값) 조회하는 SQL 쿼리 작성
select	*
from	Orders
where	status = 'Pending';

-- 문제5. 특정 고객이 주문한 모든 주문 정보 조회
-- 고객 ID가 1인 고객이 주문한 모든 주문의 정보를 조회하는 SQL쿼리를 작성하시오.
select	*
from	Orders
where	customer_id = 1;

-- 문제6. 제품 재고가 50개 이상인 제품 정보 조회
-- Products테이블에서 재고 수량이 50개 이상인 제품의 이름과 재고 수량을 조회하는 SQL쿼리를 작성하시오.
select	name, stock
from	Products
where	stock >= 50;

-- 문제7. 주문 항목의 총 가격 조회
-- OrderItems테이블에서 각 주문 항목의 총 가격(수량 X 가격)을 계산하여 조회하는 SQL쿼리를 작성하시오.
select	(quantity * price) as '총 가격'
from	OrderItems;

-- 문제8. 주문 날짜로 정렬하여 조회
-- Orders 테이블에서 모든 주문 정보를 주문 날짜 기준으로 내림차순 정렬하여 조회하는 SQL쿼리를 작성하시오.
select		*
from		Orders
order by	order_date desc; 

-- 문제9. 주문과 고객정보 함께 조회
-- Orders테이블과 Customers 테이블의 열을 조인하여 각 주문의 고객이름 과 주문상태를 조회하는 SQL 쿼리를 작성하시오.
select	o.order_id, c.name, o.status
from	Orders o inner join Customers c
on	o.customer_id = c.customer_id;

-- 문제10. 특정 제품이 포함된 모든 주문정보 조회
-- OrderItems테이블에서 '스마트폰'이 포함된 모든 주문정보를 조회하는 SQL쿼리를 작성하시오.
select	*
from		Orders o 
inner join 	OrderItems i
on			o.order_id = i.order_id
inner join 	products p
on			i.product_id = p.product_id
where		p.name = '스마트폰';

-- ----------------------------------------------------------------------------------
-- 실습5. `InternetMarket` 데이터베이스의 테이블에서 사용할 수 있는 `UPDATE` 문제 5개

### 문제 1: 고객 이메일 수정
-- 고객 ID가 1인 고객의 이메일 주소를 "newemail@example.com"으로 수정하는 SQL 쿼리를 작성하시오
update	Customers
set		email = 'newemail@example.com'
where	customer_id = 1;

### 문제 2: 제품 가격 인상
-- "스마트폰"의 가격을 50,000원 인상하는 SQL 쿼리를 작성하시오. (예: 현재 가격이 300,000원이면 350,000원으로 수정)
update	Products
set		price = price + 50000
where	name = '스마트폰';

### 문제 3: 주문 상태 변경
-- 주문 ID가 2인 주문의 상태를 'Shipped'로 변경하는 SQL 쿼리를 작성하시오.
update	Orders
set		status = 'Shipped'
where	order_id = 2;

### 문제 4: 고객 전화번호 업데이트
-- 고객 ID가 3인 고객의 전화번호를 "010-1234-5678"로 수정하는 SQL 쿼리를 작성하시오.
update	customers
set		phone = '010-1234-5678'
where	customer_id = 3;

### 문제 5: 제품 재고 수량 수정
-- "노트북" 제품의 재고 수량을 100으로 업데이트하는 SQL 쿼리를 작성하시오.
update	products
set		stock = 100
where	name = '노트북';

-- --------------------------------------------------------------------------------------------------------------
-- 실습6. `InternetMarket` 데이터베이스의 테이블에서 사용할 수 있는 `DELETE` 문제 5개
-- 참고. 외래 키 제약 조건으로 인해 삭제가 불가능한 상황을 다루고 있습니다.

### 문제 1: 고객 삭제
-- 고객 ID가 4인 고객을 삭제하는 SQL 쿼리를 작성하시오.
delete from customers
where	customer_id = 4;

### 문제 2: 제품 삭제
-- 제품 ID가 2인 제품을 삭제하는 SQL 쿼리를 작성하시오.
delete from products
where	product_id = 2;
-- 실패 Error Code: 1451. Cannot delete or update a parent row: a foreign key constraint fails (`internetmarket`.`orderitems`, CONSTRAINT `orderitems_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`))
-- orderItems 테이블에서 product_id가 2인 항목을 먼저 삭제

### 문제 3: 주문 삭제
-- 주문 ID가 1인 주문을 삭제하는 SQL 쿼리를 작성하시오.
delete from orders
where	order_id = 1;
-- 실패 Error Code: 1451. Cannot delete or update a parent row: a foreign key constraint fails (`internetmarket`.`orderitems`, CONSTRAINT `orderitems_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`))
-- orderitems 테이블에서 order_id가 1인 항목을 먼저 삭제

### 문제 4: 주문 항목 삭제
-- 주문 항목 ID가 3인 주문 항목을 삭제하는 SQL 쿼리를 작성하시오.
delete from orderitems
where	order_item_id = 3;

### 문제 5: 외래 키 제약 조건으로 인한 삭제 실패
 -- 고객 ID가 1인 고객을 삭제하려고 시도하는 SQL 쿼리를 작성하시오. 
 -- 이 고객이 주문을 한 경우, 삭제가 실패하는 이유와 해결 방법을 설명하시오.
 delete from customers
 where	customer_id = 1;
 -- 실패 Error Code: 1451. Cannot delete or update a parent row: a foreign key constraint fails (`internetmarket`.`orders`, CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`))
 -- orders 테이블에서 customer_id가 1인 항목을 먼저 삭제