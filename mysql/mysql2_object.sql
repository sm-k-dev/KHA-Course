-- ---------------------------------------------------------------------------------------------------

/*
	개체(객체) ? 데이터로 표현하고자 하는 데이터베이스의 구성요소
    
    개체 종류: 테이블, 인덱스, 뷰, 스토어드 프로시저, 트리거, 함수, 커서 등 ...
*/

-- ---------------------------------------------------------------------------------------------------

/*
    1. 인덱스(index) 개체
		- 데이터베이스 테이블에 저장된 데이터의 검색 속도를 향상시키기 위한 개체
*/
-- 인덱스 개체를 생성해서 사용하지 않고 member table에 저장되어 있는 이름이 아이유인 한 사람의 정보 조회
select *
  from member
 where member_name = '아이유';
 
 /*
	인덱스 객체 만들기 문법
		CREATE INDEX 생성할_인덱스_개체명 ON 테이블명(열_명);
 */
 -- member 테이블의 member_name열에 대한 빠른 속도로 데이터를 조회 하기 위해 인덱스 개체를 생성
 -- 보통 index는 idx_ 로 시작해서 컬럼이름을 적는다.
 CREATE INDEX idx_member_name ON member(member_name);
 
 -- 인덱스 개체를 idx_member_name 을 생성하고 나서 member 테이블에 저장된 이름이 '아이유'인 한 사람에 대한 정보 조회
select *
  from member
 where member_name = '아이유';
  
-- ---------------------------------------------------------------------------------------------------
  
  /*
	2. 뷰(view) 개체
		- 테이블과 상당히 동일한 성격의 데이터베이스 개체이다.
			가상 테이블 (가짜 테이블)
		- 실제 데이터를 가지고 있지 않으며, 진짜 테이블에 링크(link)된 개념 - 바로가기 같은 것
        - 뷰의 실체는 바로 select 문 이다.
  */
-- member 테이블에 저장된 모든 열 정보 조회
select *
  from member;
  
-- member 테이블과 연결되는 회원뷰 개체(member_view) 생성
/*
	뷰 개체 생성 문법
		CREATE VIEW 생성할_뷰명
		AS SELECT * FROM 조회할_실제_테이블명;
*/
create view member_view
  as select *
       from member;

-- member 테이블명이 아닌 회원뷰 개체(member_view)명으로
-- member 테이블의 정보를 조회 할 수 있다.
select *
  from member_view;

/*
	조회시 테이블을 사용하지 않고 굳이 뷰를 사용하여 조회한 이유는?
	1. member 테이블을 조작하면 데이터가 변경되거나 삭제 될 수 있어 보안에 좋지 않음
		그래서 뷰 명으로 조회 하면 member 테이블을 직접 만져서 조회하지 않기 때문에 보안에 좋음
	2. 긴 조회 SELECT SQL 문을 간략하게 만들수도 있다.
*/

-- ---------------------------------------------------------------------------------------------------
  
/*
	3. 스토어드 프로시저(stored procedure) 개체
		프로그램 코드를 묶어 놓은 함수와 같은 개체
        재활용 가능
*/
-- 회원 테이블(member)에 저장된 데이터들 중에서 member_name열에 저장된 값이 '나훈아'인 행에 관한 모든 열의 해당되는 값들을 조회
select *
  from member
 where member_name = '나훈아';
 
-- 상품테이블 (product)에 저장된 데이터들 중에서 product_name열에 저장된 값이 '삼각김밥'인 행에 관한 모든 열의 해당되는 값들을 조회
select *
  from product
 where product_name = '삼각김밥';
 
 /*
	스토어드 프로시저 개체 생성 문법
    
		DELIMITER //
			CREATE PROCEDURE 생성할_스토어드_프로시저_명()
			BEGIN
				프로그래밍할 SQL문장1;
				프로그래밍할 SQL문장2;
				...
			END 
        // DELIMITER ;
        
	참고. 위 첫 행과 마지막 행에 구분 문자라는 의미의 DELIMITER // 와 DELIMITER ; 문을 작성 하였는데
		이것은 스토어드 프로시저를 만들기 위해 묶어 주는 약속의 문법 이라고 생각하면 된다.
        // 는 {} 로 생각하면 된다.
 */

-- 위 두 SELECT 문을 하나의 기능인 스토어드 프로시저 개체로 만든다.
DELIMITER //
	CREATE PROCEDURE myProc()
		BEGIN
			SELECT * FROM member WHERE member_name = '나훈아';
			SELECT * FROM product WHERE product_name = '삼각김밥';
		END // 
DELIMITER ;

-- 바로 위에서 만든 myProc() 이라는 이름의 스토어드프로시저 개체를 호출해서 실행하기 위한 문법
-- CALL 호출할프로시저명();
CALL myProc();