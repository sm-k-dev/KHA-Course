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
	-- select	조회할_데이터가_저장된_열_명
	-- from	조회할_테이블_명
	-- where	조건에서_사용할_데이터들이_저장된_열_명 = 비교할_조건값;
select	*
from	member
where	mem_name = '블랙핑크';

-- 실습5. member 테이블에서 회원 그룹인원(mem_number)이 4명인 그룹의 모든 열의 데이터 조회
select	*
from	member
where	mem_number = 4;

-- 실습6. 관계(비교) 연산자 기호 <=, >=, <, >, =
	-- member 테이블에서 회원 그룹 평균키들 중에서
	-- 데이터가 162 이상인 회원 그룹의 아이디들, 회원그룹명들 조회
select	mem_id, mem_name
from	member
where	height >= 162;

-- 실습7-1. 관계(비교) 연산자 기호 <=, >=, <, >, =
-- 논리 연산자 기호 AND OR
	-- member 테이블에서 회원그룹 평균키 (height 열에 저장된 데이터들)가 165 이상이면서
	-- 그룹 인원(mem_number)이 6명 초과인 회원 그룹의
	-- mem_name, height, mem_number
	select	mem_name, height, mem_number
	from	member
	where	height >= 165
	and 	mem_number > 6;

	-- member 테이블에서 회원그룹 평균키 (height 열에 저장된 데이터들)가 165 이상 이거나
	-- 그룹 인원(mem_number)이 6명 초과인 회원 그룹의
	-- mem_name, height, mem_number
	select	mem_name, height, mem_number
	from	member
	where	height >= 165
	or		mem_number > 6;
    
    -- 실습7-1-1. BETWEEN AND 절 미사용
    -- 회원그룹 평균키 163 이상 이면서 165 이하인 회원 그룹의 그룹명, 평균키, 그룹인원수 조회
    select	mem_name, height, mem_number
    from	member
    where	height >= 163
    and		height <= 165;
    
    -- BETWEEN AND 절 작성 문법
    /*
		select 열명 from 테이블명
        where 비교할_값들이_저장된_열명 between 범위의_최소값 and 범위의 최대값;
    */
    -- 실습7-1-2. BETWEEN AND 절 미사용
    -- 회원그룹 평균키 163 이상 이면서 165 이하인 회원 그룹의 그룹명, 평균키, 그룹인원수 조회
    select	mem_name, height, mem_number
    from	member
    where	height between 163 and 165;

-- 실습8. 회원그룹의 평균키가 165이상이거나 또는 그룹인원이 6명 초과인
-- 			회원그룹들의 그룹명, 그룹평균키, 그룹인원수 조회
select	mem_name, height, mem_number
from	member
where	height >= 165 or mem_number > 6;

-- 실습8-1. 회원그룹이 사는 지역이 경기 또는 전남 또는 경남 중 한 곳이라도 해당되는 그룹의 이름, 주소 조회
-- IN() 절 사용하지 않고
select	mem_name, addr
from	member
where	addr = '경기' or addr = '전남' or addr = '경남';

-- 실습8-2. 회원그룹이 사는 지역이 경기 또는 전남 또는 경남 중 한 곳이라도 해당되는 그룹의 이름, 주소 조회
-- IN() 절 사용
select	mem_name, addr
from	member
where	addr in ('경기', '전남', '경남');

/*
	LIKE
    - 문자열 데이터의 일부 글자가 옆의 데이터로 포함되어 있는 행에 대한 열의 값 조회 하는 예약어.
		예를 들어 회원그룹명의 첫 글자가 '우'문자로 시작하는 단어를 포함하는 데이터가 저장된 행에
        관한 열의 데이터를 조회할 수 있다.
	- 문법
		where 비교할_데이터가_저장된_열명 LIKE '문자%'
*/
-- 실습9. member 테이블에서 회원그룹명 중에서 '우'문자로 시작하는 단어가 포함된 데이터가 있으면
-- 그 행에 관한 모든 열의 데이터들 조회
select	*
from	member
where	mem_name like '우%';

-- 실습9-1. LIKE절에 _ 언더바 기호 사용 가능
-- member 테이블에서 회원그룹명 중에서 앞 두글자는 상관 없고 뒷 단어가 '핑크'인 => '__핑크' 언더바 두개, 언더바 하나당 한 글자
-- 회원그룹의 이름이 저장되어 있으면? 이름이 저장된 행에 관한 모든 열의 데이터를 조회
select	*
from	member
where 	mem_name like '__핑크';

-- 실습9-2. LIKE 절에 %단어% 사용
-- member 테이블에서 회원그룹면 중에서 '마' 라는 문자가 포함되어 있는 그룹명이 저장되어 있으면?
-- 그 그룹의 행에 관한 모든 열의 데이터를 조회
select	*
from	member
where	mem_name like '%마%';

-- 실습9-2-1. LIKE 절에 '%단어' 사용
-- member 테이블에서 회원그룹명 중에서 '친구' 단어로 끝나는 그룹명이 저장되어 있으면?
-- 그 그룹의 행에 관한 모든 열의 데이터를 조회
select	*
from	member
where	mem_name like '%친구';

/*
	서브쿼리 구문
		- 안쪽 SELECT 구문을 이용하여 조회한 결과 데이터들을
			바깥쪽 SELECT 구문을 이용하여 다시 조회하는 전체 구문을 말함.
		- 문법
			SELECT * FROM 테이블명
			WHERE 조건열명 > (SELECT * FROM 테이블명
							WHERE 조건열명 = 조건열의 값들과 비교할 값);
*/
-- 실습10-1. 서브쿼리를 사용하지 않고 두개의 SELECT 문장 사용 예
-- 문제. 회원 그룹명이 에이핑크인 회원그룹의 평균키보다 큰 그룹회원의 그룹이름과 그룹평균키 조회
	-- 순서1. 에이핑크 그룹의 평균키 조회
	select	height
	from	member
	where	mem_name = '에이핑크';

	-- 순서2. 에이핑크 그룹의 평균키는 순서1.에서 조회 했으므로
	-- 		where 조건절의 조건값 자리에 164를 대입해서 164보다 큰 그룹의 이름과 평균키를 조회
	select	mem_name, height
	from	member
	where	height > 164;

-- 실습10-2. 서브쿼리 사용
-- 문제. 회원 그룹명이 에이핑크인 회원그룹의 평균키보다 큰 그룹회원의 그룹이름과 그룹평균키 조회
select	mem_name, height
from	member
where	height > (select	height
				    from	member
				   where	mem_name = '에이핑크');
                   
-- -----------------------------------------------------------------------------------
-- 연습문제
-- 1번. 회원 테이블에서 모든 회원의 ID와 그룹이름을 조회 해라.
select	mem_id, mem_name
from	member;

-- 2번. 회원 테이블에서 그룹회원의 평균키가 167이상인 그룹회원의 모든 열의 정보를 조회 해라
select	*
from	member
where	height > 167;

-- 3번. 회원 테이블에서 그룹인원수가 5명 이하인 그룹의 이름과 인원수 조회
select	mem_name, mem_number
from	member
where	mem_number <= 5;

-- 4번. 구매 테이블(buy)에서 상품가격이 100 이상인 구매한 상품의 이름과 가격을 조회 해라
select	prod_name, price
from	buy
where	price >= 100;

-- 5번. 회원 테이블에서 주소가 '경기'인 회원그룹의 모든 열 정보를 조회
select	*
from	member
where	addr like '%경기%';

-- 6번. 구매 테이블(buy)에서 '패션'분류의 상품 이름과 구매 수량을 조회 해라.
select	prod_name, amount
from	buy
where	group_name like '%패션%';

-- 7번. 회원 테이블에서 '서울'에 사는 그룹회원 이름과 전화번호 (국번, 뒷번호 모두 포함)를 조회
select	mem_name, phone1, phone2
from	member
where	addr like '%서울%';

-- 8번. 회원 테이블에서 그룹명이 '트와이스'인 그룹 회원의 모든 열 정보 조회
select	*
from	member
where	mem_name = '트와이스';

-- 9번. '블랙핑크'라는 이름을 가진 그룹회원이 구매한 모든 제품의 정보(모든 열값)를 조회 (서브쿼리)
select	*
from	buy
where	mem_id = (select 	mem_id
					from	member
				   where	mem_name = '블랙핑크');
                   
-- 10번. 회원 테이블에서 그룹 인원수가 8명인 그룹의 모든 열정보 조회
select	*
from	memeber
where	mem_number = 8;

-- 11번. 구매 테이블에서 구매한 상품이름에 '지갑' 단어가 포함된 상품의 모든 열정보 조회
select	*
from	buy
where	prod_name like '%지갑%';

-- 12번. 회원 테이블에서 평균키가 165cm 이하인 그룹의 이름과 평균키를 조회
select	mem_name, mem_height
from	member
where	height <= 165;

-- 13번. 회원 테이블에서 '여자친구' 또는 '트와이스' 그룹이름 가진 모든 열정보 조회
select	*
from	member
where	mem_name in ('여자친구', '트와이스');

-- 14번. 구매 테이블에서 구매한 제품 수량이 3이상 구매한 그룹의 그룹아이디와 상품의 이름과 가격을 조회
select	mem_id, prod_name, price
from	buy
where	amount >= 3;

-- 15번. 회원 테이블에서 사는 지역이 '강남'인 회원의 이름과 주소를 조회 -- 안 됨
select	mem_name, addr
from	member
where	addr like '%강남%';

-- 16번. 구매 테이블에서 '디지털' 분류의 상품 중 가격이 200 이하인 구매한 상품의 이름을 조회 하라.
select	prod_name
from	buy
where	group_name like '%디지털%' and price <= 200;

-- 17번. 구매 테이블에서 그룹 평균키가 162cm 이상인 그룹의 이름을 조회하라
select	mem_name
from	member
where	height >= 162;

-- 18번. 구매 테이블에서 특정그룹 ('블랙핑크')의 구매내역에서 가격이 50 이상인 구매한 상품의 모든열 정보를 조회하라 
select	*
from	buy
where	price >= 50 and mem_id = ( select	mem_id
									from	member
                                    where	mem_name = '블랙핑크' );