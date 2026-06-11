-- 한 줄 주석

# 한 줄 주석

/*
	여러 줄 주석
*/

/*
	실행하고 싶은 SQL 한줄만 실행하고 싶을때
		- 실행할 SQL 라인에 커서를 두고 ctrl + 엔터
		- 실행할 SQL 을 드래그로 선택해서 위의 번개 아이콘 클릭
*/

/*
	mysql -u root -p
    엔터를 누르면 아래에 패스워드를 입력할 곳이 생긴다
    -u 는 유저를 선택
    -p 는 패스워드를 입력하겠다라는 뜻
*/

-- 현재 MySQL DBMS 서버에 만들어져 있는 데이터베이스 목록 보기 명령
show databases;

/*
	sql 쿼리
		윈도우 에선 대소문자 구분 안함
        리눅스 에선 대소문자 구분 함
*/

/*
	DB명을 적을때
    MySQL/MariaDB 에선 백틱(``)으로 감싸거나 아무것도 쓰지 않는다.
	PostgreSQL/Oracle 에선 더블쿼트("")로 감싸거나 아무것도 쓰지 않는다.
	싱글쿼트('')는 절대 금지
*/
/*
	where 절에서 문자열 데이터를 감싸기 위해서는 싱글 쿼트 ('', 홑따옴표)를 사용하는것이 표준
    싱글 쿼트(''): 문자열 값을 표현할 때 사용
    더블 쿼트(""): 데이터베이스 객체 이름 (DB명, 테이블명, 컬럼명)을 지정할 때 사용
*/

-- DB이름(여기선 shop_db) 으로 데이터베이스 만들기
create schema shop_db;

-- 사용할 데이터베이스 선택 명령
-- 작성 문법: use 사용할데이터베이스명;
use world;

-- 데이터베이스에 있는 모든 테이블을 보겠다
show tables;

/*
	특정 테이블에 저장된 모든 열의 데이터들을 조회(검색)
    
    문법
		select 조회할데이터가_저장된_열명1, 열명2, 열명3
        from 조회할_테이블명;
	
    문법
		select * 
        from 조회할_테이블명;
	
    문법
		select *
        from 조회할_테이블명
        where 조건값을_비교할_데이터가_저장된_열명 = 조건값;
*/

-- 요약: member 테이블(표)에 저장된 member_id, member_addr 열 세로 방향의 각칸에 저장된 값 조회
select member_id, member_addr
  from member;

-- 풀이: member 테이블에 저장된 모든 열의 데이터들 조회
select *
  from member;

-- 풀이: member 테이블의 member_name 열 방향(세로 방향) 데이터가 '아이유' 인 저장된 행 위치의 모든 열의 값 조회 
-- 요약: 이름이 아이유인 회원의 모든 열 데이터 조회
select *
  from member
 where member_name = '아이유';

-- 맛보기 끝

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

-- ---------------------------------------------------------------------------------------------------

-- 주제 : 기본 조회문 SELECT ~ FROM 절 배우기
/*
	USE 문
		- SELECT 문으로 테이블에 저장된 데이터를 조회하기 전에 먼저 사용할 데이터베이스를 선택할때 이용하는 예약어
        - 사용 문법
			USE 사용할_데이터베이스_명;
*/
use market_db;

/*
	SELECT 문?
		- 특정 테이블 표에 저장되어 있는 데이터를 조회하여 가져올때 사용하는 SQL 구문.
        
	SELECT 문 전체 작성 문법
    
		SELECT 		조회할_데이터가_저장되어_있는_열_명
        FROM		조회할_데이터가_저장되어_있는_테이블_명
        WHERE		조건열명 = 조건값
        GROUP BY	그룹으로_묶을_데이터들이_저장된_열_명
        HAVING		조건식
        ORDER BY	정렬할_데이터가_저장된_열_명 (ASC 오름차순 or DESC 내림차순)
        LIMIT		숫자;
*/

/*
    SELECT 핵심 문법 1.
		SELECT 		조회할_데이터가_저장되어_있는_열_명
        FROM		조회할_데이터가_저장되어_있는_테이블_명
        WHERE		조건열명 = 조건값;
*/
-- 실습0. memeber 테이블에 저장된 모든 열의 값 조회

desc memeber;

select 	mem_id, mem_name, mem_number, addr, phone1, phone2, height, debut_date
from	member;

-- select -> 테이블에서 데이터를 조회 해서 가져올 때 사용하는 예약어
-- * -> 조회해 올 데이터가 저장된 모든 열 명
-- from -> 테이블에서 데이터를 조회해 온다는 의미의 예약어.
-- member -> 조회할 데이터가 저장된 테이블 명
select	*
from	memeber;
-- 풀어서 해석하면? member 테이블에 저장된 모든 열의 데이터들을 조회해서 가져오라는 의미

-- 실습1. 회원테이블(member)에 그룹이름이 저장된 mem_name열의 데이터들만 조회
select	mem_name
from	member;

-- 실습2. 회원테이블(member)에 주소 addr, 입사년도 debut_date, 그룹이름 mem_name 열의 데이터들만 조회
select	addr, debut_date, mem_name
from	member;

-- 실습3. 회원테이블(member)에 조회할 열명 대신 별칭을 지어서 조회된 결과창에 보여주기 위해서는 아래의 문법을 사용하자
-- 		조회할_열명1 as 별칭1, 조회할_열명2 as 별칭2
-- 		from 조회할_테이블_명
-- 또는
-- 		select 조회할_열명1 별칭1, 조회할_열명2 별칭2
-- 		from 조회할_테이블_명
select	addr as '주소', debut_date '데뷔일자', mem_name '그룹명'
from	member;

-- 실습4. 회원테이블(member)에서 회원그룹이름(mem_name)이 '블랙핑크'가 저장되어 있는 위치의 행이 있는 모든 열의 데이터를 조회 = 레코드 조회
-- 문법
--		select	조회할_데이터가_저장된_열_명
--		from	조회할_테이블_명
--		where	조건에서_사용할_데이터들이_저장된_열_명 = 비교할_조건값;
select	*
from	member
where	mem_name = '블랙핑크';

-- 실습5. member 테이블에서 회원 그룹인원(mem_number)이 4명인 그룹의 모든 열의 데이터 조회
select	*
from	member
where	mem_number = 4;