표준 SQL (ANSI)

-- 한줄 주석 ( 표준 SQL )
# 한줄 주석 ( MySQL 전용 부가 기능 )
/* 여러줄 주석 */ ( 표준 SQL )

대소문자 및 식별자 (이름) 지정 규칙

    대소문자: 윈도우(구분 안 함) / 리눅스(구분 함). 
        ( 표준 SQL 권장사항: 가급적 키워드는 대문자, 테이블/컬럼명은 소문자로 통일하면 플랫폼 간 이식성이 좋아집니다.)

    식별자 감싸기:
        표준 SQL: 식별자에 공백이나 특수문자가 있을 때 더블쿼트("")로 감쌉니다.
        MySQL / MariaDB: 백틱(`)을 사용하거나 아무것도 쓰지 않습니다. (더블쿼트는 기본설정에서 문자열로 인식될 수 있음)
        PostgreSQL / Oracle: 더블쿼트("")로 감싸거나 아무것도 쓰지 않습니다.

        싱글쿼트(' '): 표준 및 모든 DB 공통으로 '문자열 값(데이터)'을 표현할 때만 사용합니다. 식별자 이름에는 절대 쓰지 않습니다.

실행하고 싶은 SQL 한줄만 실행하고 싶을때
    - 실행할 SQL 라인에 커서를 두고 ctrl + 엔터
    - 실행할 SQL 을 드래그로 선택해서 위의 번개 아이콘 클릭
    
mysql -u root -p
    엔터를 누르면 아래에 패스워드를 입력할 곳이 생긴다
    -u 는 유저를 선택
    -p 는 패스워드를 입력하겠다라는 뜻

commit;

rollback;

DDL (Data Definition Language): 데이터 정의어
    테이블 구조를 만들고 지우는 CREATE, ALTER, DROP

DML (Data Manipulation Language): 데이터 조작어
= CRUD (Create = INSERT, Read = SELECT, Update = UPDATE, Delete = DELETE)
= 액션 쿼리 / 쓰기 작업

--------------------------------------------------------------------------------------------

DB 및 테이블 탐색/생성 (DDL 기초)
    데이터를 조회하기 전에 DB를 선택하고 구조를 확인하는 명령

    show databases; (MySQL 전용)
        현재 DBMS 서버에 만들어져 있는 데이터베이스 목록 보기
        아니면 왼쪽 navigator 창에서 Schemas를 누르면 된다.

        표준 SQL
            SELECT schema_name FROM information_schema.schemata;
        ORACLE
            SELECT username FROM all_users; (오라클은 DB와 사용자가 유사한 개념으로 관리 됨)

    use 데이터베이스명; (MySQL 전용)
        사용할 데이터 베이스 선택

        -- use 명령어 없이 바로 조회하는 표준 방식
        표준 SQL
            SELECT * FROM my_databse.users;
        ORACLE: 접속한 사용자가 곧 기본 데이터베이스(스키마)가 되므로 use 개념이 없다.
            다른 사용자의 테이블을 볼 때는 SELECT * FROM 유저명.테이블명; 구조

    show tables; (MySQL 전용)
        데이터베이스에 있는 모든 테이블을 보겠다

        표준 SQL
            SELECT table_name FROM information_schema.tables
            WHERE table_schema = 'DB이름';
        
    desc 테이블명; -- desc (Describe)
    desc db명.table명; (MySQL 전용)
        테이블명 에 설정된 컬럼에 대한 정보를 보겠다
        원칙적으로 데이터베이스_이름.테이블_이름 형식을 사용해야 하지만
        대부분 데이터베이스_이름은 생략한다.

        표준 SQL로 테이블 구조(컬럼) 보기
            SELECT column_name, data_type FROM information_schema.columns
            WHERE table_name = '테이블명';

--------------------------------------------------------------------------------------------

DDL (Data Definition Language): 데이터 정의어
    테이블 구조를 만들고 지우는 CREATE, ALTER, DROP

    CREATE 문: 테이블 생성 - ./mysql/mysql8_database_and_table 참고

        CREATE TABLE 테이블명 (
            컬럼명1 데이터타입 [제약조건],
            컬럼명2 데이터타입 [제약조건],
            ...
            [테이블 레벨 제약조건]
        );

        핵심 제약조건 (Constraints) 종류
            NOT NULL: 빈 값 (NULL)을 허용하지 않음
            UNIQUE: 테이블 내에서 중복된 값을 허용하지 않음 (NULL은 중복 허용)
            PRIMARY KEY (기본키): 행을 식별하는 고유 키 (NOT NULL + UNIQUE 성격)
            FOREIGN KEY (외래키): 다른 테이블의 기본키를 참조하여 데이터 무결성을 유지
            CHECK: 입력될 수 있는 값의 범위나 조건을 제한 (예: 나이 >= 19)
            DEFAULT: 값을 입력하지 않았을 때 자동으로 들어갈 기본값 지정
        
        MySQL 작성 방법
            CREATE TABLE employees (
                emp_id INT AUTO_INCREMENT, -- MySQL 전용 자동 증가
                emp_name VARCHAR(50) NOT NULL,
                hire_date DATE DEFAULT (CURRENT_DATE), -- MySQL 8.0+ 기본값 괄호 필수
                salary DECIMAL(10, 2),
                dept_id INT,
                -- 컬럼 레벨이나 테이블 레벨 모두 자유롭게 믹스 가능
                PRIMARY KEY (emp_id), 
                UNIQUE KEY uk_emp_name (emp_name),
                CONSTRAINT fk_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
                CONSTRAINT chk_salary CHECK (salary > 0)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; -- MySQL 전용 스토리지 엔진 및 인코딩 설정

        ANSI (표준 SQL) 작성방법
            CREATE TABLE employees (
                emp_id INT,
                emp_name VARCHAR(50) NOT NULL,
                hire_date DATE DEFAULT CURRENT_DATE,
                salary NUMERIC(10, 2),
                dept_id INT,
                -- 테이블 레벨 제약조건 정의
                CONSTRAINT pk_employee PRIMARY KEY (emp_id),
                CONSTRAINT uk_emp_name UNIQUE (emp_name),
                CONSTRAINT fk_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
                CONSTRAINT chk_salary CHECK (salary > 0)
            );

        ANSI와 MySQL 차이
            자동 증가 (Auto Increment) 기능
                MySQL: AUTO_INCREMENT라는 매우 직관적인 키워드를 컬럼뒤에 붙여서 해결
                ANSI 표준: GENERATED ALWAYS AS IDENTITY 또는 SEQUENCE 객체를 따로 만들어서 사용
            
            스토어드 엔진 및 데이터 인코딩 설정
                MySQL: 테이블 괄호가 닫히는 끝부분에 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 처럼 물리적인 저장방식과 한글 깨짐 방지 설정을 명시 할 수 있다
                ANSI 표준: 하드웨어/소프트웨어 내적 제어 구문은 표준에 존재하지 않는다.
            
            날짜 및 시간 데이터 타입의 명칭
                MySQL: 가볍고 널리 쓰이는 DATETIME, TIMESTAMP 등을 주로 쓴다
                ANSI 표준: 시차가 포함된 TIMESTAMP WITH TIME ZONE 형식을 표준으로 규정
            
            문자열 타입 명칭
                MySQL: 가변 길이 문자로 VARCHAR 사용
                ANSI 표준: 공식 표준 명칭은 CHARACTER VARYING 이다.
    
    ALTER TABLE 문법 (구조 변경)
        테이블을 이미 만든 후 구조를 바꿀 때 사용

        1) 자동 증가 시작 값 변경 문법
            MySQL: ALTER TABLE 테이블명 AUTO_INCREMENT = 시작값;
        
        2) ALTER를 이용한 인덱스 생성 문법
            MySQL: ALTER TABLE 테이블명 ADD INDEX 인덱스명 (컬럼명);
                    ADD INDEX
            ANSI 표준: CREATE INDEX
    
    AUTO_INCREMENT 제어 및 확인
        데이터를 삽입 ( INSERT ) 할 때 자동 증가 값을 다루는 시스템 명령어
        
        1) SELECT LAST_INSERT_ID ();
            의미: 현재 접속한 세션에서 가장 최근에 성공적으로 삽입된 AUTO_INCREMENT 값을 반환
        
        2) SET @@auto_increment_increment = 숫자; (증가 폭 변경)
            의미: 자동 증가할 때 번호가 몇 씩 건너뛰맂 증가 간격 (폭)을 설정하는 시스템 변수
            앞에 @@가 붙는 이유: MySQL에서 시스템이 관리하는 전역/세션 변수를 의미
                    일반 사용자 정의 변수는 @ 를 쓴다.
                만약 전체 시스템 변수의 종류를 알고 싶다면 SHOW GLOBAL VARIABLES를 실행
    
    DROP
        DROP TABLE table명;

    CASCADE 옵션

--------------------------------------------------------------------------------------------

데이터베이스 객체 생성 - 고급 DDL 및 개체

DB이름 으로 데이터베이스 만들기
    create schema 'DB이름';

인덱스 (index)
    조회 속도를 올리기 위한 책의 '찾아보기' 같은 개념

    인덱스 생성 문법
        create index idx_인덱스를_만들_컬럼_명 
                on 인덱스를_만들_테이블명(인덱스를_만들_컬럼명);

뷰 (View)
    가상 테이블, 보안이나 복잡한 쿼리를 단순화 할 때 사용

    뷰 생성 문법
        create view 뷰를만들_테이블명_view
        as select *
            from 뷰를만들_테이블명;

스토어드 프로시저 (Stored Procedure)
    SQL 문을 하나로 묶어 프로그램 함수처럼 호출 (Call) 하는 기능

    스토어드 프로시저 생성 문법
        delimiter //
            create procedure 생성할_스토어드_프로시저_명()
            begin
                프로그래밍할 SQL문장1;
                프로그래밍할 SQL문장2;
                ...
            end // 
        delimiter ;
    
    DELIMITER // 를 쓰는 이유: SQL문은 세미콜론 (;)을 만나면 실행을 종료한다.
        프로시저 내부에는 많은 세미콜론이 들어가기 때문에, 프로시저 작성이 끝날 때까지
        문장의 끝을 알리는 기호를 임시로 // 로 바꾸기 위해 사용.
        작성이 끝나면 다시 DELIMITER;로 원상복구

    스토어드 프로시저 개체를 호출해서 실행하기 위한 문법
        call 호출할_프로시저_명();

--------------------------------------------------------------------------------------------

테이블명에 있는 모든 데이터 중 조건값에 맞는 행의 모든 열 데이터를 보겠다.
    대괄호에 묶인 부분은 생략 가능하다.

select      열_이름 (or *)
            , 열_이름 as 별칭
            , 열_이름 별칭
from        테이블명
[where      조건식]
[group by   열_이름
 having     조건식]
[order by   열_이름 ASC(기본값, 오름차순)/DESC(내림차순)]
[limit      숫자];
    
    범위 값을 구할때
        관계(비교) 연산자 기호  <=, >=, <, >, =
        논리 연산자 기호       and, or
        범위 값               between ~ and <- 숫자나 날짜로 구성된 데이터
                            in() <- 이산적인(떨어진) 문자/숫자 데이터 비교
                            not in()
                                ex) add in ('부산', '서울', '강원')
                            like '%' <- % 는 *와 같다. 글자수 제한 없음
                                        _ 는 한 문자를 대체한다.
                                ex) 우% = 우 뒤에 무슨 글자가 와도 괜찮다 -> 우주 or 우주소녀 등
                                    우_ = 우 뒤에 무슨 글자든 한글자만 와야 한다 -> 우주
                            not like
    비교 연산자 (특수 연산자)  is null, is not null 
                            exists (서브쿼리)
                                괄호안의 서브쿼리 결과가 단 1건이라도 존재하는지 확인
                                대용량 데이터에서 조건에 맞는 데이터가 있는지 존재 여부만 빠르게 확인

    group by 묶을데이터열
    having 조건식
        having의 조건에 따라 묶인 결과에 따라 재정렬
            group by와 주로 사용되는 집계 함수 (aggregate function)
            함수를쓰고 괄호에 열 명을 쓴다.
                sum() 합계
                avg() 평균
                min() 최소값
                max() 최대값
                count() 행의 개수
                count(distinct) 행의 개수 (중복 데이터는 1개만 인정)
        where 절은 group by와 쓰지 않지만 order by와 limit은 같이 쓸 수 있다.
        where 절은 집계 함수를 쓸 수 없다.

    order by 정렬기준열
        최종 조회 시 특정 열의 값을 기준으로 해서 내림 차순 또는 오름 차순 정렬 해서 조회하는 예약어
        WHERE 절 다음에 나와야 한다.
        정렬 조건은 하나 이상 설정이 가능하다
            ex) order by 정렬기준열1, 정렬기준열2
    
    limit 
        결과의 개수 제한해서 조회하는 예약어
        limit 시작(숫자 index행 위치), 개수
            ex) limit 3 = limit 0, 3 = limit 3 offset 0 과 동일 => 0 인덱스행 위치부터 3건
                limit 3, 2 = limit 2 offset 3 => 3 인덱스행 위치부터 2건
        표준 SQL (ANSI)
            OFFSET 3 ROWS FETCH NEXT 2 ROWS ONLY;
    
    distinct
        중복 데이터 제거
        조회된 결과에서 중복된 데이터를 1개만 남긴다.
        중복 데이터를 제거할 열 이름 앞에 DISTINCT를 써주면 된다.

--------------------------------------------------------------------------------------------

서브쿼리 (SubQuery)
    select 안에 또 다른 select 가 들어 갈 수 있다.
    이것을 서브쿼리 혹은 하위쿼리 라고 부른다.
    주의점: 서브쿼리의 결과가 '단 하나의 값(단일행)'이 나오느냐, '여러 개의 값(다중행)'이 나오느냐에 따라 WHERE 절의 연산자가 달라진다.

        select  컬럼명1, 컬럼명2 ...
        from    테이블명
        where   ( select    (비교할데이터 컬럼명)
                  from      테이블명
                  where     조건 );

--------------------------------------------------------------------------------------------

DML (Data Manipulation Language): 데이터 조작어
= CRUD (Create = INSERT, Read = SELECT, Update = UPDATE, Delete = DELETE)
= 액션 쿼리 / 쓰기 작업 

    테이블 조회: SELECT 구문
    테이블 새 행 데이터 추가: INSERT 구문
    테이블 열에 저장된 값만 수정: UPDATE 구문
    테이블에 행 데이터 삭제: DELETE 구문

    INSERT 문: 테이블에 새로운 행 데이터를 추가(입력)해서 저장할 때 사용되는 SQL문 종류 중 하나

        INSERT문 문법

            insert into 테이블명 ( 열명1, 열명2, 열명3 )
                        values  ( 값1,  값2,   값3 );
            
            insert into 테이블명 ( 열명1, 열명2, 열명3 )
                        values  ( 값1,  값2,   값3 )
                                ( 값4,  값5,   값6 ),
                                ...
                                ( 값n, 값n+1, 값n+2 );

			insert into 테이블명 (열명1, 열명2, 열명3)
				select	열명1, 열명2, 열명3
				from	테이블명;
    
    UPDATE 문: 

        UPDATE문 문법

            UPDATE  테이블명
            SET		열명1 = 수정값1,
                    열명2 = 수정값2
                    ...
            WHERE	조건식;

    DELETE 문: 행 단위 데이터 삭제, 빈 테이블은 남는다.
        where 조건식; 을 쓸 수 있다.

        DELETE문 문법
            DELETE FROM 테이블명
            WHERE       조건식;

    TRUNCATE 문: 행 단위 데이터 삭제, 빈 테이블은 남는다.
        where 조건식; 을 쓸 수 없다. 조건식 없이 모든 행 데이터를 삭제

    경고: UPDATE와 DELETE 문에 WHERE 절을 빼 먹으면 테이블 전체 데이터가 날아가거나 변경 되므로 주의

--------------------------------------------------------------------------------------------

MySQL의 데이터 형식

데이터 종류             SQL 표준 형식                 MySQL 구현 및 확장
정수 (Integer)          INT, SMALLINT               INT, TINYINT, MEDIUMINT, BIGINT
문자열 (String)          CHARACTER, VARCHAR          CHAR, VARCHAR, TEXT, BLOB
날짜/시간 (Date/Time)    DATE, TIME, TIMESTAMP       DATE, TIME, DATETIME, TIMESTAMP
순수 이진 데이터          BINARY LARGE OBJECT         BLOB, LONGBLOB

열거형 타입 (enum) - 하나의 값만 저장할 수 있도록
    사용법
        열_명 enum('값1', '값2', '값3'...) default '값1'

**************************************************

    TINYINT 
        1 바이트, -128 ~ 127
    
    SMALLINT
        2 바이트, 132,768 ~ 32,767

    INT
        4 바이트, 약 -21억 ~ +21억
    
    BIGINT
        8 바이트, 약 -900경 ~ +900경
    
    UNSIGNED 예약어
        1 바이트는 256개를 표현하므로 -128 ~ +127 로 표현하거나, 0 ~ 255로 표현하거나 모두 256개이므로 unsigned(부호가 없는 정수)를 사용하여 128 이상의 숫자를 표현 할 수 있다.
        테이블을 생성할때, 컬럼명 뒤에 데이터 타입을 적은 후 unsigned를 명시 해 준다.


        * 전화번호는 모두 숫자로 이루어져 있지만 전화번호는 숫자로서 의미가 없기 때문에 문자로 저장한다.
        * 숫자로서 의미를 가지려면
            - 더하기/빼기 등의 연산에 의미가 있다.
            - 크다/작다 또는 순서에 의미가 있다.
    
**************************************************

    CHAR (Character)
        고정길이 문자형, 자릿수가 고정
        MySQL 내부적으로 성능 (빠른 속도) 면에서는 CHAR로 설정하는 것이 좋다.
    
    VARCHAR (Variable Character)
        가변길이 문자형
        공간을 효율적으로 운영할 수 있다.

    TEXT 형식
        TEXT:       1 ~ 65,535 바이트
        LONGTEXT:   1 ~ 4,294,967,295 바이트, 대량의 텍스트
    BLOB 형식 (Binary Long Object)
        BLOB:       1 ~ 65,535 바이트
        LONGBLOB:   1 ~ 4,294,967,295 바이트, 대량의 이진 데이터

        쿼리에서 실제 파일 경로를 통해 파일의 정보를 불러오는 법
            load_file('실제_파일_경로')
            
            참고: LOAD_FILE()을 사용하려면 DB 서버의 secure_file_priv 설정이 허용된 경로에 파일이 위치해 있어야 한다.

            show variables like 'secure_file_priv'; 쿼리를 쓰면 경로 위치를 알 수 있다.
            
            워크벤치에서 경로 추가 하는 법
                administration 메뉴 > Options File > 오른쪽 위 Locate option에 secure-file-priv 써넣고 find > 체크 표시된 secure-file-priv 의 오른쪽에 경로를 바꾼뒤 apply를 누른다
                apply가 됐다면 Startup / Shutdown 을 눌러 서버를 껏다 켠다.

**************************************************
    
    FLOAT
        4 바이트, 소수점 아래 7자리까지 표현
    DOUBLE
        8 바이트, 소수점 아래 15자리까지 표현

**************************************************

    MySQL 8에서 제공하는 날짜형 데이터 타입은 총 5가지로, 각각의 용도와 저장 형식이 다릅니다. 
    아래는 MySQL 8에서 사용할 수 있는 날짜 및 시간 관련 데이터 타입입니다:

    1. DATE
        - 형식: `YYYY-MM-DD`
        - 설명: 날짜만 저장할 수 있으며, 시간 정보는 포함되지 않습니다. 
            예를 들어, `2024-10-02`와 같은 값을 저장할 수 있습니다.

    2. TIME
        - 형식: `HH:MM:SS`
        - 설명: 시간만 저장할 수 있으며, 날짜 정보는 포함되지 않습니다. 
            예를 들어, `12:30:45`와 같은 값을 저장할 수 있습니다.

    3. DATETIME
        - 형식: `YYYY-MM-DD HH:MM:SS`
        - 설명: 날짜와 시간을 함께 저장할 수 있습니다. 
            예를 들어, `2024-10-02 12:30:45`와 같은 값을 저장할 수 있습니다.

    4. TIMESTAMP
        - 형식: `YYYY-MM-DD HH:MM:SS`
        - 설명: `DATETIME`과 비슷하지만, 
            `TIMESTAMP`는 UTC 기준으로 저장된 후, 
            조회할 때 서버의 타임존에 맞게 변환됩니다. 
            서버의 시간대에 의존하는 데이터를 처리할 때 유용합니다.

    5. YEAR
        - 형식: `YYYY`
        - 설명: 연도만 저장할 수 있습니다. 
            예를 들어, `2024`와 같은 값을 저장할 수 있습니다.


        참고.
            TIMESTAMP 데이터 타입을 쉽게 설명하자면, MySQL에서 시간을 저장할 때 
            'UTC(세계 표준시)'라는 기준 시간을 사용해 저장하고, 
            나중에 데이터를 조회할 때는 서버가 위치한 곳의 시간대에 맞게 변환해주는 타입입니다.

            UTC(세계 표준시): 전 세계가 동일하게 사용하는 기준 시간이 있습니다. 
                            예를 들어, 한국은 이 UTC 시간보다 9시간 빠릅니다. 
                            그래서 만약 UTC 기준으로 2024-10-02 12:00:00가 있으면, 
                            한국 시간으로는 2024-10-02 21:00:00이 됩니다.

            TIMESTAMP 작동 방식:
                저장할 때: 데이터베이스는 시간을 UTC 기준으로 저장합니다. 
                        예를 들어, 한국에서 2024-10-02 21:00:00이라는 시간을 저장하면, 
                        데이터베이스에는 UTC 기준으로 2024-10-02 12:00:00으로 저장됩니다.
                조회할 때: 나중에 이 데이터를 조회하면, 
                        데이터베이스는 저장된 UTC 시간을 다시 한국 시간대로 변환해 2024-10-02 21:00:00으로 보여줍니다.

        즉, TIMESTAMP는 언제나 동일한 기준(UTC)으로 시간을 저장하고, 
        이를 각 나라나 서버의 시간대에 맞춰 보여주기 때문에, 
        여러 나라에서 동시에 사용하는 시스템에서는 아주 유용합니다.

**************************************************

    데이터 형 변환
        문자형을 정수형으로 바꾸거나, 정수형을 문자형으로 바꾸는 것을 형 변환 (type conversion)
        
        명시적인 변환 (explicit conversion): 직접 함수를 사용해서 변환
        암시적인 변환 (implicit conversion): 별도의 지시 없이 자연스럽게 변환

        명시적 형변환
            CAST() 함수
                문법
                    SELECT CAST( 형변환할_값 AS 변환할_데이터_유형 ) '열_별칭명'
                    FROM 조회할_테이블명;
                
                CAST 함수 내부에 작성할 데이터 유형은
                CHAR, SIGNED, UNSIGNED, DATE, TIME, DATETIME 등
                    SIGNED: 부호가 있는 정수, UNSIGNED: 부호가 없는 정수

            CONVERT() 함수
                문법
                    SELECT CONVERT( 형변환할_값, 변환할_데이터_유형 ) '열_별칭명'
                    FROM 조회할_테이블명;
        
            CONCAT() 함수
                여러개의 문자열이나 열의 값을 하나로 이어 붙일때 사용하는 문자열 함수이다.
                기본 사용 방법
                    CONCAT( 문자열1, 문자열2, 문자열3 );

**************************************************

    변수? 컴퓨터의 특정 RAM 메모리에 잠시 데이터(값)를 기억할 공간을 변수 메모리 공간 이라고 한다.
        사용자 변수를 생성해서 사용할 수 있다.
		시스템변수는 골뱅이 두개 @@
        사용자변수는 골뱅이 한개 @
    
    1. 변수를 생성하고 값을 저장시키는 문법
		SET @변수이름 = 변수에 저장할 값; -- MySQL 워크벤치를 끄기전까진 쓸 수 있다.
	
    2. 변수에 저장된 갓을 조회하는 문법
		SELECT @변수이름;

    SELECT 문에 전체 행 중에서 특정 행의 갯수를 제한해서 조회할때 LIMIT을 사용했다.
    제한 할 행의 갯수도 변수를 선언하여 저장해 놓고 변수명을 이용해서 값을 불러와서 사용할 수 있다.
    일반 SELECT 문에서 사용할 수 없다. 
    사용 방법은 prepare 와 execute예약어 구문을 사용하면 된다.

        prepare 프리페어 구문에 mySQL 이름에 'select'구문을 미리 준비 해 놓고 대기 한다.
        여기서 ? 기호는 아직 값이 정해져 있지 않아 나중에 값을 결정해서 ? 대신 넣겠다는 뜻이다.

            PREPARE 이름 FROM '?가 포함된 쿼리';
        
            SET @count = 3;
            PREPARE mySQL FROM 'select mem_name, height from member order by height asc limit ?';

        EXECUTE 구문으로 mySQL 이름으로 미리 준비 해 놓은 select 전체 문장을 실행하기 전에
        using 구문을 이용해 아직 결정되지 않은 ? 기호 자리에 들어갈 값을 @count 변수에 저장된 값으로 설정하고
        select 문장을 'select mem_name, height from member order by height asc limit 3' 완성하고 실행한 후 조회 하게 한다.

            EXECUTE 이름 USING ?_에_넣을_값

            EXECUTE mySQL USING @count;

--------------------------------------------------------------------------------------------

JOIN
    하나 이상의 테이블의 열에 저장된 행 데이터들을 묶어서 하나의 표형태의 결과 조회하는 구문

    INNER JOIN - 내부조인
        JOIN 조인 이라고도 합니다.
        INNER JOIN은 두 테이블의 교집합을 구하는 조인 방식입니다. 
        조인 조건에 일치하는 데이터가 두 테이블 모두에 존재하는 행만 결합하여 반환합니다.
        
        기본 개념: 
            양쪽 테이블 모두 조인 조건을 만족하는 데이터가 있을 때만 결과에 포함됩니다. 
            한쪽에만 데이터가 있고 다른 쪽에는 없다면 결과에서 제외됩니다.
        생략 가능: 
            가장 자주 사용되는 기본 조인으로, INNER 키워드를 생략하고 JOIN만 적어도 똑같이 동작합니다.
        1 대 多 관계: 
            주로 테이블 간의 기본키(PK, Primary Key)와 외래키(FK, Foreign Key) 관계를 기반으로 연결됩니다. 
            그래서 1대다 관계를 PK-FK 관계 라고 부른다.
        문법:
            SELECT   A.열이름1, A.열이름2, B.열이름3
            FROM     첫번째_테이블명 A INNER JOIN 두번째_테이블명 B
            ON       A.공통_기준열 = B.공통_기준열
            WHERE    필터_조건식;               

    OUTER JOIN - 외부조인
        외부 조인은 조인 조건을 만족하지 않는 행도 버리지 않고 결과 표에 포함하는 방식입니다. 
        기준이 되는 테이블의 모든 행(ROW)을 모두 보여주고, 데이터가 없는 반대쪽 테이블의 빈칸은 전부 NULL로 채워집니다.

        LEFT [OUTER] JOIN - 왼쪽 외부 조인
            기본 개념: 
                왼쪽(FROM 절) 테이블의 모든 데이터를 먼저 기준 삼아 가져온 뒤, 오른쪽(JOIN 절) 테이블에서 조건에 맞는 데이터를 결합합니다.
            특징: 
                오른쪽 테이블에 매칭되는 데이터가 없더라도 왼쪽 테이블 데이터는 무조건 출력되며, 이때 오른쪽 테이블의 열들은 NULL로 표시됩니다. OUTER 키워드는 생략 가능합니다.
            문법:
                SELECT   A.열이름, B.열이름
                FROM     왼쪽_테이블명 A LEFT JOIN 오른쪽_테이블명 B
                ON       A.공통_기준열 = B.공통_기준열
                WHERE    필터_조건식;

        RIGHT [OUTER] JOIN - 오른쪽 외부 조인
            기본 개념: 
                오른쪽(JOIN 절) 테이블의 모든 데이터를 먼저 기준 삼아 가져온 뒤, 왼쪽(FROM 절) 테이블에서 조건에 맞는 데이터를 결합합니다.
            특징: 
                LEFT JOIN과 작동 방향만 반대입니다. 실무에서는 가독성을 위해 주로 테이블 위치를 바꾸고 LEFT JOIN을 쓰는 편입니다. OUTER 키워드는 생략 가능합니다.
            문법:
                SELECT   A.열이름, B.열이름
                FROM     왼쪽_테이블명 A RIGHT JOIN 오른쪽_테이블명 B
                ON       A.공통_기준열 = B.공통_기준열
                WHERE    필터_조건식;

        FULL [OUTER] JOIN - 전체 외부 조인
            기본 개념: 
                두 테이블의 합집합을 구하는 조인 방식입니다. 양쪽 테이블의 모든 데이터를 반환합니다.
            특징: 
                서로 매칭되는 데이터는 결합하고, 한쪽에만 데이터가 존재하는 경우 반대쪽 테이블의 열들을 NULL로 채워서 모두 가져옵니다.
            주의 (DBMS 제약): 
                MySQL/MariaDB 등 일부 데이터베이스 시스템에서는 FULL OUTER JOIN 구문을 직접 지원하지 않습니다. 대신 LEFT JOIN 결과와 RIGHT JOIN 결과를 UNION 키워드로 합쳐서 구현해야 합니다.
            문법:
                SELECT   A.열이름, B.열이름
                FROM     테이블명A A FULL OUTER JOIN 테이블명B B
                ON       A.공통_기준열 = B.공통_기준열;

            -- MySQL에서 FULL OUTER JOIN을 구현하는 방법
                SELECT A.열이름, B.열이름 FROM 테이블A A LEFT JOIN 테이블B B ON A.id = B.id
                UNION
                SELECT A.열이름, B.열이름 FROM 테이블A A RIGHT JOIN 테이블B B ON A.id = B.id;

    CROSS JOIN - 상호조인 / 카테시안 곱
        기본 개념: 
            두 테이블의 모든 행을 무조건 하나씩 서로 연결하여 가능한 모든 행의 조합을 만드는 조인 방식입니다.
        특징: 
            두 테이블을 잇는 매개체(ON 조건절)가 필요 없습니다. 
            결과 행의 총 개수는 [A 테이블의 행 수 × B 테이블의 행 수]가 되므로, 대용량 테이블끼리 조인할 때 데이터가 폭발적으로 늘어나 주의해야 합니다. 
            주로 테스트용 대량 데이터를 생성할 때 씁니다.
            - 표준(ANSI) SQL: 
                CROSS JOIN은 무조건 조건절(ON)을 가질 수 없으며, 무조건 전체 조합(카테시안 곱)만 반환해야 합니다.
            - MySQL: 
                CROSS JOIN 뒤에 ON 조건식을 붙이는 것을 허용합니다. 만약 MySQL에서 CROSS JOIN에 ON 조건을 붙이면, 이름만 다를 뿐 INNER JOIN과 100% 동일하게 작동하므로 주의해야 합니다. (실무 혼선을 줄이기 위해 조건이 없을 때만 CROSS JOIN을 쓰는 것이 좋습니다.)
        문법: 
            SELECT   A.열이름, B.열이름
            FROM     테이블명A A CROSS JOIN 테이블명B B;

    SELF JOIN - 자체조인
        기본 개념: 
            별도의 테이블을 조인하는 것이 아니라, 하나의 동일한 테이블을 자기 자신과 조인하는 기법입니다.
        특징: 
            SQL에 SELF JOIN이라는 별도의 키워드가 존재하는 것이 아닙니다. INNER JOIN이나 LEFT JOIN 구문을 그대로 사용하되, 동일한 테이블에 서로 다른 별칭(Alias)을 부여하여 컴퓨터가 마치 두 개의 서로 다른 테이블인 것처럼 인식하게 만듭니다.
        예시: 
            사원 테이블에서 '사원의 매니저 사원 번호'를 가지고 동일한 테이블의 '매니저의 이름'을 조회할 때
        문법:
            SELECT   E.사원이름 AS 사원명, M.사원이름 AS 매니저명
            FROM     사원테이블 E INNER JOIN 사원테이블 M
            ON       E.매니저ID = M.사원ID;

-----------------------------------------------------------------------------------------------

SQL 프로그래밍
    DELIMITER가 필요한 프로시저 안의 IF/CASE: 
        쿼리문 전체의 흐름을 제어하는 '제어문'입니다. 문장 끝에 ;이 들어가므로 DELIMITER가 필수입니다.
    SELECT 안의 IF/CASE: 
        데이터를 가공해서 새로운 열(Column)로 보여주는 '표현식'입니다. 하나의 쿼리문 안에서 돌아가므로 DELIMITER가 필요 없습니다.
    
    --------------------------------------------------------------------------

    스토어드 프로시저는 MySQL에서 프로그래밍 기능이 필요할 때 사용하는 데이터베이스 개체이다.
    SQL 프로그래밍은 기본적으로 스토어드 프로시저 안에 만들어야 한다.

        DROP PROCEDURE IF EXISTS 스토어드_프로시저_이름;
        
        DELIMITER $$
        CREATE PROCEDURE 스토어드_프로시저_이름()
        BEGIN
            이 부분에 SQL 프로그래밍 코딩;
        END $$
        DELIMITER ;

        CALL 스토어드_프로시저_이름();

    --------------------------------------------------------------------------

        DECLARE 변수명 데이터타입;
            지역 변수를 만드는 예약어입니다. (BEGIN 바로 밑에 모아서 선언해야 합니다.)
        SET 변수명 = '값';
            변수에 값을 넣는 예약어입니다.
        SELECT 변수명;
            변수에 저장된 값을 조회(출력)하는 예약어입니다. (GET은 사용하지 않습니다.)

        DELIMITER $$
            SQL 문장 종결자를 ;에서 $$로 바꾸겠다는 선언입니다.
        DELIMITER ;
            SQL 문장 종결자를 다시 원래대로 ;로 바꾸겠다는 선언입니다
    
    --------------------------------------------------------------------------

    IF 문 (JavaScript의 IF else 와 동일)
        프로시저 제어문으로도 쓰고, SELECT 안에서 데이터 가공 함수로도 쓸 수 있음.
        
        스토어드 프로시저로 생성
            DROP PROCEDURE IF EXISTS 스토어드_프로시저_이름;

            DELIMITER $$
            CREATE PROCEDURE 스토어드_프로시저_이름()
            BEGIN
                IF 조건식 THEN
                    조건식이 참일때 실행할 SQL문;
                ELSE
                    조건식이 거짓일때 실행할 SQL문;
                END IF;
            END $$
            DELIMITER ;

            CALL 스토어드_프로시저_이름();
        
        --------------------------------------------------------------------------

        select 문에서 if 함수 사용 (단일 조건문)
            SELECT 
                이름, 
                점수,
                IF(점수 >= 60, '합격', '불합격') AS 결과 
            FROM 성적테이블;
        
        --------------------------------------------------------------------------

        select 문에서 if - else if - else 대체 (if 함수 중첩)
            SELECT 
                이름, 
                점수,
                IF(점수 >= 90, 'A', IF(점수 >= 80, 'B', 'C')) AS 학점 
            FROM 성적테이블;
    
    --------------------------------------------------------------------------

    CASE문 (JavaScript의 switch / if else if 와 동일) - 다중분기
        프로시저 제어문으로도 쓰고, SELECT 안에서 데이터 가공 함수로도 쓸 수 있음.

        스토어드 프로시저로 생성
            DROP PROCEDURE IF EXISTS 스토어드_프로시저_이름;

            DELIMITER $$
            CREATE PROCEDURE 스토어드_프로시저_이름()
            BEGIN
                CASE
                    WHEN 조건식1 THEN
                        조건식1 이 참일때 실행할 SQL;
                    WHEN 조건식2 THEN
                        조건식2 가 참일때 실행할 SQL;
                    WHEN 조건식3 THEN
                        조건식3 이 참일때 실행할 SQL;
                    ...
                    ELSE
                        조건식1, 조건식2, 조건식3 ... 모두 거짓일때 실행할 SQL;
                END CASE;
            END $$
            DELIMITER ;

            CALL 스토어드_프로시저_이름();
        
        --------------------------------------------------------------------------
        
        select 문에서 case문 사용 (다중 조건문)
            SELECT 
                이름, 
                점수,
                CASE 
                    WHEN 점수 >= 90 THEN 'A'
                    WHEN 점수 >= 80 THEN 'B'
                    WHEN 점수 >= 70 THEN 'C'
                    ELSE 'F'
                END AS 학점
            FROM 성적테이블;
    
    --------------------------------------------------------------------------

    WHILE 문 (반복문)
        SELECT 문 안에서 단독으로 사용할 수 없으며, 반드시 스토어드 프로시저 안에서만 사용해야 합니다.
        데이터를 단순히 가공하는 것이 아니라, 데이터베이스의 실행 흐름을 제어하고 쿼리문 자체를 실시간으로 만들어 실행하는 순수한 프로그래밍 문법이기 때문

        WHILE 반복문 작성 문법
            WHILE (조건식) DO
                반복실행할 SQL문장
            END WHILE

        스토어드 프로시저로 생성
            DROP PROCEDURE IF EXISTS 반복문_프로시저;

            DELIMITER $$
            CREATE PROCEDURE 반복문_프로시저()
            BEGIN
                DECLARE i INT;
                DECLARE 합계 INT;
                
                SET i = 1;
                SET 합계 = 0;
                
                -- i가 100 이하인 동안 반복 실행
                WHILE (i <= 100) DO
                    SET 합계 = 합계 + i;
                    SET i = i + 1;
                END WHILE; -- WHILE문 종료 표시 필수
                
                SELECT 합계; -- 결과 출력 (5050)
            END $$
            DELIMITER ;

            CALL 반복문_프로시저();

            --------------------------------------------------------------------------

            응용
                ITERATE [label] - 지정한 레이블로 가서 계속 진행 (자바스크립트의 continue)
                LEAVE [label] - 지정한 레이블을 빠져나간다. 즉 WHILE 문이 종료 (자바스크립트의 break)
            
            DROP PROCEDURE IF EXISTS 프로시저_이름;

            DELIMITER $$
            CREATE PROCEDURE 프로시저_이름();
            BEGIN
                DECLARE i INT;
                DECLARE sum INT;
                SET i = 1;
                SET sum = 0;

                myWhile: -- while 에 label 지정
                WHILE ( i <= 100 ) DO
                    IF (조건식) THEN
                        실행 SQL문;
                        ITERATE myWhile; -- 지정한 label 문으로 가서 계속 진행
                    END IF;

                    IF ( sum > 1000 ) THEN
                        실행 SQL문;
                        LEAVE myWhile; -- 지정한 label 문을 떠남. 즉 while 종료
                    END IF;
                END WHILE;

                SELECT 구문;
            END $$
            DELIMITER ;

    --------------------------------------------------------------------------

    동적 SQL
        SELECT 문 안에서 단독으로 사용할 수 없으며, 반드시 스토어드 프로시저 안에서만 사용해야 합니다.
        데이터를 단순히 가공하는 것이 아니라, 데이터베이스의 실행 흐름을 제어하고 쿼리문 자체를 실시간으로 만들어 실행하는 순수한 프로그래밍 문법이기 때문

        스토어드 프로시저 안에서의 사용법
            PREPARE: SQL 문자열을 실행할 준비를 합니다.
            EXECUTE: PREPARE를 사용해 준비한 SQL을 실행합니다.
            DEALLOCATE PREPARE: 사용한 SQL 자원을 해제합니다.

        스토어드 프로시저로 생성
            DROP PROCEDURE IF EXISTS 동적SQL_프로시저;

            DELIMITER $$
            CREATE PROCEDURE 동적SQL_프로시저(IN 테이블이름 VARCHAR(50))
            BEGIN
                -- 1. 실행할 SQL 문장을 문자열로 만듭니다. (변수 앞 @ 필수)
                SET @조립된쿼리 = CONCAT('SELECT * FROM ', 테이블이름, ' WHERE 나이 >= 20;');
                
                -- 2. SQL 문장을 실행할 수 있도록 컴파일(준비) 합니다.
                PREPARE 내쿼리 FROM @조립된쿼리;
                
                -- 3. 준비된 쿼리를 실행합니다.
                EXECUTE 내쿼리;
                
                -- 4. 메모리 해제를 위해 해제해 줍니다.
                DEALLOCATE PREPARE 내쿼리;
            END $$
            DELIMITER ;

            -- 호출할 때 테이블 이름을 인자값으로 넘겨 실행합니다.
            CALL 동적SQL_프로시저('회원테이블');

        execute using 사용
            -- 쿼리를 만들때 확정되지 않은 값을 ? 로 둔다.
            prepare 내쿼리 from 'select * from 테이블 where 컬럼명 = ?';
            prepate 내쿼리 from 'insert into 테이블 (컬럼명, 컬럼명) value (값, ?)';
            
            -- 쿼리를 실행할때 using을 사용해 ? 값에 들어갈 값을 넣는다.
            execute 내쿼리 using @조건값;

    --------------------------------------------------------------------------