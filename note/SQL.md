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

    CREATE 문: 테이블 생성

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
            CHECK: 입력될 수 있느 ㄴ값의 범위나 조건을 제한 (예: 나이 >= 19)
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
                                ex) add in ('부산', '서울', '강원')
                            like '%' <- % 는 *와 같다. 글자수 제한 없음
                                        _ 는 한 문자를 대체한다.
                                ex) 우% = 우 뒤에 무슨 글자가 와도 괜찮다 -> 우주 or 우주소녀 등
                                    우_ = 우 뒤에 무슨 글자든 한글자만 와야 한다 -> 우주

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

