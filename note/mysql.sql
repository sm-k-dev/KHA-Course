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

-- ---------------------------------------------------------------------------------------
-- 03-2절 조금 더 깊게 알아보는 SELECT 문
-- ---------------------------------------------------------------------------------------
/*
	ORDER BY 절
		- 최종 조회 시 특정 열의 값을 기준으로 해서 내림 차순 또는 오름 차순 정렬해서 조회하는 예약어
        - 문법
			SELECT * FROM 조회할 테이블명
            WHERE 조건식
            ORDER BY 정렬할_데이터가_저장된_열_명 ASC 또는 DESC;
            
            ASC - ascending, 오름차순
            DESC - descending, 내림차순
*/

select * from member;
-- 실습1. 그룹 회원의 데뷔일자(debut_date 열에 저장된 날짜들)를 기준으로
-- 		오름 차순 정렬 (데뷔 일자가 빠른 날짜순) 하여 조회시 ORDER BY 절을 사용.

select		*
from		member
order by	debut_date asc;

-- 실습2. 그룹 회원의 데뷔일자(debut_date)를 기준으로
-- 		내림 차순 정렬 (데뷔 일자가 늦은 날짜순) 하여 조회시 ORDER BY 절을 사용.
select		*
from		member
order by	debut_date desc;

-- 실습3. ORDER BY 절과 WHERE 조건절 함께 사용하기
-- 그룹 평균키(height 열에 저장된 데이터들)가 164 이상인 그룹 회원들의 키가 큰 순서대로(내림차순) 정렬해서
-- 그룹명(mem_name), 그룹아이디(mem_id), 그룹평균키(height), 데뷔일(debut_date)
select		mem_name, mem_id, height, debut_date
from		member
where		height >= 164
order by	height desc;

-- 실습4. ORDER BY 절과 WHERE 조건절 함께 사용하기2
-- 		(정렬 조건 하나이상 설정가능)
-- 그룹 평균키 (height)가 큰(내림차순) 순서대로 조회하되,
-- 같은 평균키를 가진 그룹들이 있으면, 데뷔일자가 빠른순서대로 (오름차순) 최종 정렬
select		mem_name, mem_id, height, debut_date
from		member
where		height >= 164
order by	height desc, debut_date asc;

-- ---------------------------------------------------------------------------------------
-- LIMIT 예약어: 테이블 저장된 전체 행 (row, 레코드) 중에서
-- 				원하는 행의 갯수를 정해서 조회할때 사용하는 예약어
/*
	문법
		select * from 조회할_테이블_명
        where 조건식
        order by 정렬_기준_데이터가_저장된_열_명 asc 또는 desc;
        limit 조회할_행의_개수를_숫자로_작성;
*/

-- 실습5. member 테이블에서 전체 행 데이터(레코드)들 중에서 3개의 행만 잘라서 조회
select * from member
limit 3;

-- 실습6. member 테이블에서 회원그룹평균키가 큰 순 (desc)으로 정렬해서 조회하되,
-- 		정렬해서 조회한 결과 데이터들 중에서
-- 		3 index 위치 행의 레코드 부터 2개의 행(레코드)만 잘라서 조회
select		*
from		member
order by	height desc
limit		3, 2;

-- ------------------------------------------------------------------------------
-- distinct 예약어: 조회할 열의 데이터들이 중복되서 같은 이름의 데이터로 조회되면?
-- 					중복된 데이터를 1개만 남기고 1개로만 조회시키는 예약어
-- 					요약: 중복된 열의 데이터가 저장되어 있으면 하나로 조회하는 예약어.
/*
	문법
		SELECT DISTINCT 조회할열명
        FROM 조회할테이블명
        WEHRE 조건식
        ORDER BY 정렬기준데이터의_열명 정렬방식
        LIMIT 숫자;
*/
-- 실습8-1. 모든 그룹회원의 사는 지역 조회
select		addr, mem_name
from		member
order by	addr asc;

-- 실습8-2. DISTINCT 사용해서 열에 중복된 데이터를 하나로 통일해서 하나의 데이터만 조회
select		distinct addr
from		member
order by	addr asc;

/*
	group by 절
		- group by절은 데이터베이스에서 데이터를 그룹으로 묶어서 조회하는데 사용되는 예약어.
        - 예를 들어, 같은 날자에 해당하는 데이터들을 하나의 그룹으로 묶어 관리 할 수 있다.
        - group by절은 보통 sum, count, avg 같은 집계함수와 함께 작성해서 사용해야 한다.
        - 예를 들어, 각 카테고리별로 판매량의 합계를 구할때 사용한다.
        - 문법
			SELECT 		열_명1, 집계함수명(열_명2)
            FROM		조회할테이블명
            GROUP BY	그룹으로_묶을_같은_데이터가_저장된_열_명
            HAVING 		조건식
            ORDER BY	정렬기준열명 정렬방식
            LIMIT		숫자;
		
        -- 제공해주는 집계함수들
		-- SUM(): 열명을 SUM(열명)으로 작성하면 열에 저장된 데이터들의 함계를 반환해준다.
        -- AVG(): 열명을 AVG(열명)으로 작성하면 열에 저장된 데이터들의 평균을 반환해준다.
        -- MIN(): 열명을 MIN(열명)으로 작성하면 열에 저장된 데이터들 중에서 최소 값을 반환해준다.
        -- MAX(): 열명을 MAX(열명)으로 작성하면 열에 저장된 데이터즐 중에서 최대 값을 반환해준다.
        -- COUNT(*): 모든 열에 관한 행 갯수 반환 해 준다.
        -- COUNT(DISTINCT): 행의 갯수를 반환 해 준다. (중복된 데이터는 1개만 인정)
*/

select * from buy;
-- 실습9. 'buy' 테이블에서 각 mem_id별로 총 구매 수량을 계산해서
-- 			계산한 총 구매 수량과 각회원 그룹아이디 같이 조회

-- 순서1. 각 회원 그룹단위로 한번 상품 구매시 구매한 수량 조회
select	mem_id as '그룹아이디', amount as '한번 구매시 구매한 수량'
from	buy;
-- 순서2. 각 회원 그룹의 아이디 단위로 묶어서 한번만 조회된 그룹아이디로 표시 하되
-- 			(group by 그룹으로_묶을_같은_데이터가_저장된_열명) 을 이용해서
-- 			그룹아이디 단위로 조회되게 묶어서 조회
select 		mem_id as '그룹아이디'
from		buy;

-- 순서3. group by mem_id; 를 끝에 작성해
-- 		mem_id열에 세로 방향으로 저장된 아이디를 하나의 그룹으로 묶어서 하나만 조회되게 해보자
select 		mem_id as '그룹아이디'
from		buy
group by	mem_id; -- <-- 이 한줄을 추가 하니 아래의 조회 결과가 달라진다.

-- 순서4. 'buy' 테이블에서 각 mem_id별로 총 구매 수량을 계산해서
-- 			계산한 총 구매 수량과 같이 조회하기 위해 추가
-- 			이때 SUM이라는 집계함수를 작성하여 amount 열에 조회되는 모든 행 위치의 열값들을 추출해
-- 			+ 모두 합계 한 회원 그룹 아이디 별 총 구매 수량을 조회하면 같이 보여줄 수 있음.
select 		mem_id as '그룹아이디', SUM(amount) as '총 구매 수량'
from		buy
group by	mem_id;

-- 실습11. 전체 회원그룹이 구매한 총 구매 수량의 평균을 구해서 조회된 결과를 보여주자.
select	avg(amount)
from	buy;

-- 실습12. 각 회원들이 한번 구매할 때마다 몇개의 상품을 구매했는지 평균 구매 개수 조회
-- 		참고. 각 회원그룹들을 식별할 유일한 고유값은 mem_id열에 저장된 그룹id를 그룹으로 묶어주자
select		mem_id, avg(amount)
from		buy
group by	mem_id;

-- 실습13. member 테이블에 저장된 그룹회원의 전체 행(레코드, row)의 갯수 조회
select	count(*)
from	member;

-- 실습13-1. member 테이블에서 연락처(phone1, phone2)가 저장되어 있는 그룹회원의 레코드(행)갯수만 조회
select	count(phone1)
from	member;

-- ------------------------------------------------------------------------------
-- having 조건절
-- 			where 조건절 대신에 그룹으로 묶어준 데이터의 조건을 검사하는 구문

-- 문법
-- 		select 열명1, 집계함수(열명2)
-- 		from 테이블명
-- 		group by 그룹으로_묶을_같은_데이터가_저장된_열명
-- 		having 조건식
-- 		order by 정렬기준데이터가_저장된_열명 asc 또는 desc;

-- 실습14. buy 테이블에서 조회
-- 			회원그룹 아이디를 그룹으로 묶어서, 회원 그룹 아이디별로 각각 총 구매 금액과 그룹아이디열의 데이터 조회
select		mem_id, sum(price*amount) as '총 구매 금액'
from		buy
group by	mem_id;

-- 실습14-1. 위 실습14 에서, 그룹 아이디별로 총 구매 금액이 1000 이상이면 사은품을 증정하려고 한다.
-- 			그룹 아이디별로 총 구매 금액이 1000이상인 그룹의 총 구매금액, 그룹아이디를 조회
select		mem_id, sum(price*amount) as '총 구매 금액'
from		buy
group by	mem_id
having		sum(price*amount) >= 1000;

-- 실습14-2. 위 실습14-1의 결과에서, 총 구매 금액이 큰 순서대로 (내림차순) 정렬 하여 최종 조회해서 보여줌
select		mem_id, sum(price*amount) as '총 구매 금액'
from		buy
group by	mem_id
having		sum(price*amount) >= 1000
order by	sum(price*amount) desc;

-- -----------------------------------------------------------------------------------------
-- 연습 ******************************************************

-- 1. 회원그룹수 (count)
-- 		회원 그룹 테이블의 총 그룹회원(행, 레코드) 수를 계산해서 조회
select 	count(*)
from 	member;

-- 2. 평균 인원수 (avg)
-- 		회원 그룹테이블의 그룹 평균 인원수 계산 조회
select	avg(mem_number)
from	member;

-- 3. 최대 평균키(max)
-- 		회원그룹 테이블에서 가장 키가 큰 그룹회원의 평균키 조회
select	max(height)
from	member;

-- 4. 최소 평균키(min)
-- 		회원그룹 테이블에서 가장 키가 작은 회원그룹의 평균키 조회
select	min(height)
from	member;

-- 5. 구매 수량의 총합(sum)
-- 		그매 테이블에서 모든 구매 수량의 총합 조회
select	sum(amount)
from	buy;

-- 6. 각 회원별 구매 수량의 총합
-- 		각 회원별로 구매한 총 수량을 계산합니다. mem_id로 그룹화 하여 각 회원의 총 구매 수량 조회
select		mem_id, sum(amount)
from		buy
group by	mem_id;

-- 7. 각 제품의 평균 가격 (avg)
-- 		구매한 각 제품별 단가(가격)의 평균을 계산해서 계산한 평균값 조회되게 하기.
-- 		참고. 제품 이름으로 그룹화하여 평균 가격을 구합니다.
-- 			구매한 각 제품의 단가(가격)의 평균을 구하는 것.
-- 			즉, 특정 제품이 여러 번 구매되었을 때, 그 제품의 가격을 모두 더한 후 구매 횟수로 나누어 평균 가격을 계산.
select		prod_name, avg(price)
from		buy
group by	prod_name;

-- 8. 특정 지역의 그룹 회원 수(count)
-- 		각 사는 지역별 그룹명, 그룹회원 수 조회.
-- 		참고. 지역 주소로 그룹화하여 각 지역의 회원그룹 수를 구합니다.
select		addr, mem_name, count(*)
from		member
group by	addr;

-- 9. 구매한 제품의 종류 수 (count distinct)
-- 		구매테이블에서 구매한 제품의 종류 수를 계산합니다. 구매 제품 이름에 중복을 제거하여 고유한 제품 수를 구합니다.
-- 		전체 흐름 참고.
-- 		1. 구매 데이터 조회: buy 테이블에서 모든 구매 정보 가져오기
-- 		2. 중복 제거: prod_name 열에서 중복된 제품 이름을 제거하여 고유한 제품 이름만 남긴다.
-- 		3. 고유 제품 수 계산: 남은 고유한 제품 이름의 수를 세어 unique_products라는 이름으로 결과 반환.
select		count(distinct prod_name)
from		buy;

-- 10. 구매 테이블에서 상품 분류별 총 구매수량
-- 		예: 디지털 분류 전체 구매 수량, 패션 분류 전체 구매 수량
select 		group_name, sum(amount)
from		buy
group by	group_name;

-- 11. 구매 테이블에서 상품 분류별 평균가격을 조회 하시오.
-- 	단, 상품 분류가 NULL인 데이터는 제외하시오.
select		group_name, avg(price)
from		buy
group by	group_name
having		group_name is not null;

-- 12. 구매 테이블에서 회원별 구매 건수를 조회 하시오.
-- 		구매 건수란 buy 테이블에 저장된 구매 기록의 갯수를 의마 한다.
select		mem_id, count(*)
from		buy
group by	mem_id;

-- 13. 구매 테이블에서 회원별 총 구매 금액을 조회 하시오.
-- 		총 구매 금액은 가격(price) * 수량(amount)으로 계산하시오.
select		mem_id, sum(price * amount)
from		buy
group by	mem_id;

-- 14. 구매 테이블에서 상품별 총 판매 금액을 조회 하시오.
-- 		총 판매 금액은 가격(price) * 수량(amount)으로 계산하시오.
select		prod_name, sum(price * amount)
from		buy
group by	prod_name;

SELECT mem_id, prod_name, SUM(amount) AS total_amount
FROM buy 
GROUP BY mem_id, prod_name;

-- -----------------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------------------
-- 03-3절. 데이터 변경을 위한 SQL문
-- -----------------------------------------------------------------------------------------
/*
	주제: 데이터베이스 내부에 만든 특정 테이블에 데이터를 추가(입력) / 수정 / 삭제 하는 SQL문
    
		INSERT문 : 테이블에 새로운 행 데이터를 추가(입력)해서 저장

			INSERT문 문법
				insert into 테이블명 ( 열명1, 열명2, 열명3)
							vlaues  ( 값1,  값2,   값3);
*/
-- market_db 데이터베이스 사용하기 위해 선택
use market_db;

/*
	테이블 생성 문법
		create table 생성할_테이블_명(
			생성할_열_명1 열1에_저장할_데이터_유형,
            생성할_열_명2 열2에_저장할_데이터_유형,
			생성할_열_명3 열4에_저장할_데이터_유형
            ... ...
        );
*/
-- hongong1 이라는 이름의 테이블 생성
create table hongong1(
	toy_id		INT, 		-- 장난감 ID
    toy_name	CHAR(4), 	-- 장난감 이름
    age			INT			-- 장난감 나이
);

-- hongong1 테이블에 저장된 모든 열의 데이터 조회
select	*
from	hongong1;

-- hongong1 테이블에 하나의 행(row, 레코드)을 추가하여 저장
insert into hongong1	( toy_id, toy_name, age )
			values		( 1,	  '우디',	25);

-- hongong1 테이블에 toy_id열과 toy_name열에만 데이터를 추가하여 저장할 값 넣어보자
insert into hongong1	( toy_id, toy_name )
			values		( 2,	  '버즈');

-- hongong1 테이블에 열명의 순서를 바꿔서 저장할 값 넣어보자
-- 주의 할점은 테이블명() 사이에 작성한 열명의 순서에 맞게 values() 사이에 저장할 값 넣어야 한다.
insert into hongong1	( toy_name, age, toy_id )
			values		( '제시', 	20,  3 );

-- hongong1 테이블에 ( 열명1, 열명2, 열명3 ) 생략 하고
-- values (열 추가값1, 열 추가값2, 열 추가값3 ) 구문만 작성해 새로운 행 데이터를 추가할 수 있다.
-- 주의할점. 테이블 생성시 작성한 열명 순서에 맞게 추가할 값 들을 작성해야 한다.
insert into hongong1 values ( 4, '영구', 30 );

/*
	AUTO_INCREMENT 예약어
		- 테이블을 새로 생성할때 열이름 뒤에 설정하는 예약어로
		열에 대한 값을 INSERT 문장으로 추가하지 않아도
		자동으로 1씩 중가되면서 추가가 되게 하는 예약어
*/
create table hongong2 (
	toy_id 		int auto_increment primary key,
    toy_name 	char(4),
    age			int
);

-- hongong2 테이블에 자동으로 toy_id열에 대한값을 1 증가해서 들어가는 데이터를 null 값으로 채워 놓고 데이터 추가
insert into hongong2 ( toy_id, toy_name, age ) values ( null, '보핍', 25 );

-- hongong2 테이블에 자동으로 toy_id 열에 대한 값을 1증가해서 들어가는 데이터를 null 값으로 채워 놓고 데이터 추가.
insert into hongong2 ( toy_id, toy_name, age )
			values	( null, '슬링키', 22 );

insert into hongong2 ( toy_id, toy_name, age )
			values	( null, '렉스', 21);

/*
	toy_id 열에 추가할 값을 작성하지 않고, 다른 열의 값만 추가 시키면
    auto_increment 제약조건 예약어를 설정 해 놓은 toy_id 열의 값은 자동으로 1 증가하면서 값이 추가된다.
*/
insert into hongong2 ( toy_name, age )
			values	( '맹구', 100 );
            
/*
	hongong2 테이블의 toy_id열에는 auto_increment 제약조건 예약어를 설정 해 놓았기 때문에
    자동 증가값이 4까지 설정되어 있다는 것을 확인 할 수 있지만
    자동으로 증가된 값이 얼마만큼 되었는지 확인하는 조회 구문
*/
select last_insert_id();

/*
	auto_increment 제약조건을 지정한 열은 1부터 insert(추가)가 되기 때문에
    특정 값 부터 insert(추가)되게 하기 위해 auto_increment 제약조건의 속성의 값을 설정 해야 한다.
*/
alter table hongong2 auto_increment = 100;
					-- 초기값은 100으로 설정
					-- 초기 100 부터 1씩 증가 되어 추가 되도록 설정

insert into hongong2 (toy_name, age) values ('재남', 35);

/*
	auto_increment 제약조건을 지정한 열은 100부터 1씩 증가되면서 insert가 된다.
    하지만 3씩 증가 즉! 103, 106, 109 형태로 증가 시킬 수 있게
    @@auto_increment_increment 변수의 값을 변경 시키면 된다.
*/
-- hongong3 테이블 새로 만들기
create table hongong3 (
	toy_id		int auto_increment primary key, -- 장난감 아이디 저장할 toy_id 열을 만들고 숫자아이디로 저장
    toy_name	char(4), -- 장난감 이름을 저장할 toy_name 열을 만들고 최대 4글자 까지 저장
    toy_age		int -- 장난감 나이를 지정할 age 열을 만들고 숫자로 나이를 저장
);

-- auto_increment 자동 증가 시작되는 값을 1000으로 설정
alter table hongong3 auto_increment = 1000;

-- auto_increment는 1000 부터 열의 데이터가 추가되어 1씩 증가 되어 추가되지만
-- 만약 3씩 증가하여 추가를 시키려면? 다음과 같이 시스템변수의 값을 설정 하면 됨
set @@auto_increment_increment = 3; -- 자동으로 증가되는 값을 3으로 설정

insert into hongong3 values ( null, '토마스', 20 );
insert into hongong3 values ( null, '제임스', 23 );
insert into hongong3 values ( null, '고든', 25 );

insert into hongong3 ( toy_name, age ) values ( '개똥이', 100);
insert into hongong3 ( toy_name, age ) values ( '똘똘이', 5);

/*
	insert into ~ select 전체 구문
		- 특정 테이블 select 구문을 이용해 조회한 표 형태의 결과 데이터들을
			insert into 문장을 이용해 테이블에 행의 데이터들을 한번에 추가시키는 구문
		- 문법
			insert into 테이블명 (열명1, 열명2, 열명3)
				select	열명1, 열명2, 열명3
				from	테이블명;
*/

-- world 데이터베이스 사용을 위한 선택
use world;

-- world 데이터베이스 내부에 만들어져 있는 테이블 목록 조회
show tables;

-- world 데이터베이스 내부에 만들어져 있는 city 테이블 레코드(행)의 총 갯수 조회
select count(*) as '총 행 갯수' from city; -- 4079 행 데이터들

-- city 테이블에 저장된 전체 레코드(행) 조회
select * from city;

-- city 테이블에 어떤 열이 어떤 구조로 설정되어 만들어져 있는지 열의 구성 확인
-- desc 테이블명
desc city;

-- city 테이블에 저장된 전체 4079행 데이터 중에서
-- 5개의 행데이터만 조회
select * from city
limit 0, 5;

/*
	테이블 생성 문법
		create table 생성할_테이블명 (
			생성할_열명1	열명1에_저장할_데이터유형,
            생성할_열명2	열명2에_저장할_데이터유형,
            생성할_열명3	열명3에_저장할_데이터유형
        );
*/
-- city 테이블에 저장된 도시명과 인구수를 조회해서 저장할 city_popul 테이블 생성
create table city_popul(
	city_name	char(35), -- 도시명
    population	int	-- 인구수
);

insert into city_popul ( city_name, population )
	select name, population
	  from city;
      
-- city_popul 테이블에 city 테이블에서 조회한 4079행의 정보가 제대로
-- city_name열과 population열에 추가되어 저장되는지 확인
select * from city_popul;

select count(*) from city_popul;

/*
insert 문 보충    
    1. 단일 행 삽입 
		-  단일 행 삽입은 한 번에 하나의 행을 테이블에 추가하는 방법.
        - 예시.  INSERT INTO 테이블명(열1, 열2, 열3) VALUES(값1, 값2, 값3);
    
    2. 다중 행 삽입 
		-  다중 행 삽입은 한번에 여러개의 행을 테이블에 추가하는 방법.
        - 예시. INSERT INTO 테이블명(열1, 열2, 열3) VALUES(값1, 값2, 값3),(값4, 값5, 값6),(값7,값8,값9);
    
    3. SELECT문을 활용한 삽입
		- SELECT문을 사용하여 다른 테이블의 데이터를 기반으로 데이터를 삽입하는 방법
        - INSERT INTO 문과 SELECT문을 함께 사용하여 SELECT문의 결과를 기반으로 테이블에 삽입합니다.
        - 예시. INSERT INTO 테이블명(열1, 열2, 열3)
               SELECT 열1, 열2, 열3 FROM 다른테이블명 WHERE 조건;
        
    4. ON DUPLICATE KEY UPDATE 사용 
        - MySQL 8버전에서 도입된 기능으로, 데이터 삽입시 중복된 키가 발생할 경우 업데이트 작업을 수행하는 방법
        - INSERT INTO문 뒤에 ON DUPLICATE KEY UPDATE 절을 추가고, 업데이트할 열과 값을 지정합니다.
        - 예시.  INSERT INTO 테이블명(열1, 열2, 열3) VALUES(값1, 값2, 값3)
                ON DUPLICATE KEY UPDATE 열1=값1, 열2=값2;

*/

-- 1. 테이블 생성
create table users(
	id		int auto_increment primary key, -- 고유한 ID
    name	varchar(50),					-- 사용자 이름
    age		int								-- 사용자 나이
);

-- 2. 단일 행 삽입 예제
-- 		users 라는 테이블에 한 명의 행 데이터를 삽입
insert into users ( name, age ) values ( 'Alice', 25);

-- 3. 다중 행 삽입 예제
-- users 테이블에 여러 명의 행 데이터를 동시에 삽입.
insert into users ( name, age )
			values ( 'Bob', 30 ),
					( 'Charlie', 35 ),
                    ( 'Diana', 28 );

-- 4. SELECT를 사용한 데이터 삽입
-- 		다른 테이블에서 데이터를 조회해서 users테이블에 삽입하는 예제
-- 		예를 들어 new_users 테이블이 있다고 가정하자
create table new_users (
	name	varchar(50), -- 사용자 이름
    age		int			-- 사용자 나이
);

-- 일단 new_users 테이블에 데이터가 하나도 없으므로 다중 행 추가 해 놓자.
insert into new_users ( name, age ) 
				values ( 'Eve', 26 ),
						( 'Frank', 29 );

-- new_users 테이블에서 select 구문을 이용해서 조회한 name열의 값과 age열에 대한 값을
-- users 테이블에 inserrt 구문을 이용해서 삽입.
insert into users ( name, age )
	select name, age 
    from new_users
    where age > 25;

-- 5. ON DUPLICATE KEY UPDATE 예제 (MySQL8 이상부터 적용되는 문법)
-- 		id 열에 저장된 값이 중복되는 경우 (같은 1을 넣으면) insert 되지 않는다.
-- 		그럴 경우 name 열과 age열의 값만 변경하고 싶을 때 ON DUPLICATE KEY UPDATE 구문을 사용
insert into users ( id, name, age )
			values ( 1, 'Alice', 27 )
on duplicate key update name = 'Alice Update', age = 27;

/*
	테이블 조회: SELECT 구문
    테이블 새 행 데이터 추가: INSERT 구문
    테이블 열에 저장된 값만 수정: UPDATE 구문
    테이블에 행 데이터 삭제: DELETE 구문
*/
/*
	UPDATE 구문?
		- 테이블에 이미 저장되어 있는 열의 데이터를 수정(변경)하는 SQL문 중 하나
        - 문법
			UPDATE 수정할_데이터가_저장된_테이블_명
            SET		수정할_데이터가_저장된_열_명1 = 수정할_값1,
					열_명2 = 수정할_값2,
                    ...
			WHERE	조건식;
            
            UPDATE 테이블명
            SET		열명 = 수정값;
            WHERE	조건식;
*/
-- city_popul 테이블에 저장된 도시 이름이 'Seoul'인 모든 열의 데이터 조회
select * from city_popul
where	city_name = 'Seoul';

-- city_popul 테이블의 도시 이름 중에서
-- 영문 'Seoul' 데이터를 한글 '서울'로 수정하자
update city_popul
set		city_name = '서울'
where	city_name = 'Seoul';

-- city_popul 테이블에 저장된 도시 이름이 '서울' 인 모든 열의 데이터 조회
select * from city_popul
where	city_name = '서울';

-- city_popul 테이블의 city_name열에 저장된 데이터가 'New York'을 '뉴욕'으로 수정하고
-- 			동시에 population 열에 저장된 인구수를 0으로 수정
-- 	조건 city_name 열에 저장된 데이터가 'New York'인 열의 값이면 위 2가지의 정보를 수정

-- 순서1. 수정할 데이터 조회
select * from city_popul
where city_name = 'New York';

-- 순서2. city_popul 테이블의 city_name열에 저장된 데이터가
-- 		영문 'New York'을 한글 '뉴욕'으로 수정하는 동시에
-- 		population 열에 저장된 인구수를 0으로 수정하자
update city_popul
set		city_name = '뉴욕',
		population = 0
where	city_name = 'New York';

-- 순서3. 수정된 열의 데이터 확인을 위해 조회
select * from city_popul
where city_name = '뉴욕';

-- city_popul 테이블에 저장된 모든 행의 열 인구수를 10000으로 나눈 계산된 값들을
-- population 열의 값들로 수정
update city_popul
set population = population/10000;

select * from city_popul;

-- 영구반영 (commit) 안한 update 구문 취소 (되돌리기) 하는 구문
rollback;

-- -----------------------------------------------------------------------------------------
/*
	DELETE 문
		- 테이블에 저장된 행 단위로 데이터를 삭제하는 SQL문 중 하나
        - 문법
			DELETE FROM 삭제할_행이_저장된_테이블명
			WHERE 조건식;
*/
-- 도시 이름 (city_name열의 데이터들)이 New라는 단어로 시작하는 도시이름이 존재하는 행을 삭제

-- 순서1. 먼저 도시이름이 New라는 단어로 시작하는 도시이름이 저장되어 있는지 SELECT 조회해 보자
select * from city_popul
where	city_name like 'New%';

-- 순서2. city_name 테이블에 city_name 열에 저장된 데이터들 중에서
-- 		'New' 단어로 시작하는 도시이름의 행 11개를 삭제
delete from city_popul
where	city_name like 'New%';

-- 순서3. city_popul 테이블에 저장된 총 행의 갯수는 4079개 중에서
-- 		4068행의 갯수로 조회 되게 전체 행의 갯수 조회
select count(*) from city_popul;

-- -------------------------------------------------------------------------------------------

/*
	대용량의 데이터가 저장된 테이블을 삭제 하기 위해 먼저 실습 준비
    
	대용량 데이터를 저장하기 위해 일단 테이블 3개 준비
    방법: 대용량의 데이터들이 저장된 테이블을 SELECT 구문으로 조회해 와서
		CREATE 구문을 이용하여 총 3개의 테이블을 생성
*/
-- 		외부_데이터베이스_명.외부_데이터베이스에_생성할_테이블_명

select * from city;
select * from sakila.country;
select * from world.city, sakila.country;

create table market_db.big_table1 (select * from world.city, sakila.country); -- 444,611
create table market_db.big_table2 (select * from world.city, sakila.country);
create table market_db.big_table3 (select * from world.city, sakila.country);

desc market_db.big_table1;
desc market_db.big_table2;
desc market_db.big_table3;

select count(*) from market_db.big_table1;
select count(*) from market_db.big_table2;
select count(*) from market_db.big_table3;

-- DELETE : 테이블에 저장된 행 단위 데이터 삭제하는 SQL문
delete from market_db.big_table1; -- 4.359초

-- DROP : 테이블 자체를 삭제하는 SQL문
-- 문법
-- 	DROP TABLE 삭제할테이블명;
drop table market_db.big_table2; -- 0.016초

-- TRUNCATE : 테이블에 저장된 행 단위 데이터 삭제하는 SQL문
-- 			단, where 조건식; 을 쓸 수 없다.
-- 			조건식 없이 전체 행을 삭제 할 때 사용
-- 문법
--  truncate table 삭제할테이블명;
truncate table market_db.big_table3; -- 0.031초

-- -----------------------------------------------------------------------------------------
-- 04-1절 MySQL의 데이터 형식
-- -----------------------------------------------------------------------------------------
-- 주제 : 데이터 형식(유형)

use market_db;

create table hongong4 (
	  tinyint_col	tinyint 	-- 정수 127 까지 열에 저장할 수 있다.
    , smallint_col	smallint 	-- 정수 32,767 까지 열에 저장할 수 있다.
    , int_col		int 		-- 정수 2,147,483,647 까지 열에 저장할 수 있다.
    , bigint_col	bigint		-- 정수 약 900경 까지 열에 저장할 수 있다.
);

-- insert 구문을 이용하여 hongong4 테이블에 새로운 행(데이터) 추가
insert into hongong4 ( tinyint_col, smallint_col, int_col, bigint_col )
			values	 ( 127,			32767,		  2147483647, 90000000000000000 );

select * from hongong4;

-- 각 숫자에 1을 더해서 추가 해 봅시다.
insert into hongong4 ( tinyint_col, smallint_col, int_col, bigint_col )
			values	 ( 128,			32768,		  2147483648, 900000000000000000 );

-- netflix_db 데이터베이스 생성
-- 데이터베이스 생성 문법
-- CREATE DATABASE 생성할_데이터베이스_이름;            
create database netflix_db;

-- netflix_db 데이터베이스 선택
use netflix_db;

-- netflix_db 데이테버이스 내부에 영화정보 저장하기 위한 movie 테이블 생성
create table movie (
	  movie_id			int			-- 영화 구분 아이디를 정수로 저장
    , movie_title		varchar(30)	-- 영화 제목을 문자형태로 저장
    , movie_director	varchar(20) -- 감독명 문자형태로 저장
    , movie_star		varchar(20) -- 별점수 문자형태로 저장
	, movie_script		longtext	-- 영화자막 텍스트파일의 내용 저장 최대 4GB
    , movie_film		longblob	-- 영화 동영상 파일 저장 최대 4GB
);

-- 1. 영화 '기생충' 데이터 삽입
insert into movie ( movie_id, movie_title, movie_director, movie_star, movie_script, movie_film )
			values (
				  1
				, '기생충'
                , '봉준호'
                , '★★★★★'
                , '제시카 외동딸 일리노이 시카고, 과 선배는 김진모 그는 네사촌...'
                , 'binary_movie_data_1'
            );

-- 2. 영화 '부산행' 데이터 삽입
insert into movie ( movie_id, movie_title, movie_director, movie_star, movie_script, movie_film )
			values (
				  2
				, '부산행'
                , '연상호'
                , '★★★★☆'
                , '아빠, 가지마....(좀비 울음소리) 으아아악!'
                , 'binary_movie_data_2'
            );

-- 3. 영화 '올드보이' 데이터 삽입
insert into movie ( movie_id, movie_title, movie_director, movie_star, movie_script, movie_film )
			values (
				  3
				, '올드보이'
                , '박찬욱'
                , '★★★★★'
                , '웃어라, 온 세상이 너와 함께 웃을 것이다. 울어라, 너 혼자만 울 것이다.'
                , 'binary_movie_data_3'
            );

-- 4. 영화 '오징어 게임' 데이터 삽입
insert into movie ( movie_id, movie_title, movie_director, movie_star, movie_script, movie_film )
			values (
				  4
				, '오징어 게임'
                , '황동혁'
                , '★★★★★'
                , load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/netflix_data/squid_game_sub.txt')	-- load_file('실제파일경로') 로 자막 내용 불러와 추가
                , load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/netflix_data/squid_game.mp4') 		-- 실제 동영상 파일 경로 작성해서 영상 내용 불러와 추가
            );
-- (참고: LOAD_FILE()을 사용하려면 DB 서버의 secure_file_priv 설정이 허용된 경로에 파일이 위치해 있어야 한다.)
show variables like 'secure_file_priv';

/*

MySQL 8에서 제공하는 날짜형 데이터 타입은 총 5가지로,
각각의 용도와 저장 형식이 다릅니다. 
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


------------------------------------------------------------------------------------------------------------------------
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

*/
-- ----------------------------------------------------------------------------------------------
/*
	주제: 사용자 변수를 생성해서 사용할 수 있다.
		시스템변수는 골뱅이 두개 @@
        사용자변수는 골뱅이 한개 @
    
    변수? 컴퓨터의 특정 RAM 메모리에 잠시 데이터(값)를 기억할 공간을 변수 메모리 공간 이라고 한다.
    
    1. 변수를 생성하고 값을 저장시키는 문법
		SET @변수이름 = 변수에 저장할 값; -- MySQL 워크벤치를 끄기전까진 쓸 수 있다.
	
    2. 변수에 저장된 갓을 조회하는 문법
		SELECT @변수이름;
*/
use market_db;

-- 변수 생성 후 정수 하나 저장
SET @myVar1 = 5;

-- 변수 생성 후 실수 하나 저장
SET @myVar2 = 4.25;

-- 변수 생성하고 문자열과 정수 저장
SET @txt = '가수 이름 => ';
SET @height = 166;

-- market_db 데이터베이스의 member 테이블을 조회합니다.
-- 그룹의 평균키(height열에 저장된 데이터들)가 166보다 큰 그룹의 이름 조회
SELECT 	@txt as '가수이름', mem_name as '그룹이름'
from	member
where	height > @height;

/*
	SELECT 문에 전체 행 중에서 특정 행의 갯수를 제한해서 조회할때 LIMIT을 사용했다.
    제한 할 행의 갯수도 변수를 선언하여 저장해 놓고
    변수명을 이용해서 값을 불러와서 사용할 수 있다.
*/
SET @count = 3;

/*
	select 		mem_name, height
	from		member
	order by	height asc
	limit		@count;
*/
-- 위 SET으로 생성한 @count 변수 메모리에 저장된 값은 limit 구문의 값으로 사용하지 못 하였다.
-- 그러나 사용할 수 있는 해결 방법은 prepare 와 execute예약어 구문을 사용하면 된다.

-- prepare 프리페어 구문에 mySQL 이름에 'select'구문을 미리 준비 해 놓고 대기 한다.
-- 여기서 ? 기호는 아직 값이 정해져 있지 않아 나중에 값을 결정해서 ? 대신 넣겠다는 뜻이다.
PREPARE mySQL FROM 'select mem_name, height from member order by height asc limit ?';

-- EXECUTE 구문으로 mySQL 이름으로 미리 준비 해 놓은 select 전체 문장을 실행하기 전에
-- using 구문을 이용해 아직 결정되지 않은 ? 기호 자리에 들어갈 값을 @count 변수에 저장된 값으로 설정하고
-- select 문장을 'select mem_name, height from member order by height asc limit 3' 완성하고
-- 실행한 후 조회 하게 한다.
EXECUTE mySQL USING @count;

-- -------------------------------------------------------------------------------
/*
	데이터 형변환 이란?
    - 정수를 실수로 변환 한다거나, 문자를 정수로 변환 시키는 것을 데이터의 형태를 변환한다고 해서 데이터 형 변환 이라 한다.
    
    데이터 형변환 하는 방법 2가지
    1. 개발자가 직접 제공되는 함수를 이용해 강제로 형변환 (명시적 형변환)
    2. 자동형변환 (암시적 형변환)
    
    1. 명시적 형변환
		CAST() 함수
			문법
				SELECT CAST( 형변환할_값 AS 변환할_데이터_유형 ) '열_별칭명'
				FROM 조회할_테이블명;
		
        CONVERT() 함수
			문법
				SELECT CONVERT( 형변환할_값, 변환할_데이터_유형 ) '열_별칭명'
                FROM 조회할_테이블명;
*/
-- 실습1. market_db 데이터베이스 내부에 만들어져 있는 구매 (buy) 테이블에서
-- 		가수 그룹들이 구매한 평균가격 조회해서 가져오자
-- 		조회한 평균 가격은 142.9167. 즉, 실수값으로 조회되어 나온다.
select	avg(price) as '평균 가격'
from	buy;

-- 실습2. 실수값을 정수 데이터로 형변환을 하여 조회된 결과로 보여주자
-- 참고. CAST 함수 내부에 작성할 데이터 유형은
-- 		CHAR, SIGNED, UNSIGNED, DATE, TIME, DATETIME 등
-- 		SIGNED: 부호가 있는 정수, UNSIGNED: 부호가 없는 정수
-- 		결과: 143
select	cast(avg(price) as signed ) as '평균 가격'
from	buy;

-- CAST 함수는? 데이터를 특정 데이터 형으로 형변환을 해주는 함수

-- 실습2-1. 숫자를 문자열로 변환
select	CAST(123 as CHAR); -- 123 -> '123'으로 변환되어 조회됨

-- 실습2-2. 문자열을 정수 숫자로 변환
select CAST( '456' as unsigned ); -- '456' -> 456 으로 변환되어 조회됨

-- 실습2-3. 숫자를 문자열로 변환
select convert( 123 , char ); -- 123 -> '123'으로 변환되어 조회됨

-- 실습2-4. 문자열을 숫자로 변환
select convert( '456', unsigned ); -- '456' -> 456 으로 변환되어 조회됨

-- 실습2-5. 문자처리방식(인코딩방식) 변환
select convert( '테스트' using utf8mb4 );

-- 실습3. 날짜 데이터를 YYYY-MM-DD 날짜형식을 만들기 위해 데이터 유형을 DATE 사용해 데이터 형변환해보자
-- CAST(형변환할값 as 형변환할데이터유형) as '별칭';

-- '2026$06$17' 문자열을 2026-06-17 DATE 유형의 데이터로 형변환
select cast( '2026$06$17' as DATE ) as '오늘 날짜';

-- '2026/06/17' 문자열을 2026-06-17 DATE 유형의 데이터로 형변환
select cast( '2026/06/17' as DATE ) as '오늘 날짜';

-- 실습4. 조회결과를 원하는 날짜형으로 형변환해서 조회된 결과를 출력해서 보여줄수 있다.
/*
	참고. CONCAT()함수: 여러개의 문자열이나 열의 값을 하나로 이어 붙일때 사용하는 문자열 함수이다.
    기본 사용 방법
		CONCAT( 문자열1, 문자열2, 문자열3 );
				문자열1 + 문자열2 + 문자열3 -> 하나의 문자열로 합쳐서 반환 해준다.
*/
select	  num
		, concat(	cast(price as char)
					, ' X '
					, cast(amount as char)
                    , ' = ') as '가격 X 수량'
		, price * amount as '구매 액'
from	buy;

/*
	2. 자동 형변환(암시적 형변환) 실습
*/
-- 실습1. '100' + '200'
-- 설명: + 연산을 할 때 문자열이 정수로 각각 자동변환된 후
-- 		+ 연산자 기호를 이용해 계산한다. 계산결과는 300이 조회됨
select '100' + '200';

-- 실습2. 문자열'100'과 문자열'200'을 하나로 합친 '100200'하나의 문자열로 만들어서 조회되게 하려면?
-- 		CONCAT() 함수를 사용
select concat('100', '200');

-- 실습3. 숫자와 문자열을 concat()함수를 호출할때 인수로 전달하면
-- 		숫자는 문자열 '100'으로 자동 형변환되고, 문자열 '200'과 합쳐진 '100200'으로 조회된다.
select concat(100, '200');

-- 실습4. + 연산자 기호를 사용해 숫자 + '문자열' 계산하면?
-- 		+ 연산자는 숫자를 기준으로 '문자열'을 숫자로 자동 형변환 하여 문자 '200'이 숫자 200으로 자동 형변환 된 다음에 100과 더한다.
select 100 + '200';

/*
	조인 (join): 하나 이상의 테이블의 열에 저장된 행 데이터들을 묶어서 하나의 표형태의 결과 조회하는 구문
    
    조인 종류
		1. 내부조인 (Inner Join) === 조인 (Join)
        2. 외부조인 (Outer Join) 
        3. 상호조인
        4. 자체조인
        
        1. 내부조인 (Inner join)
			- 두 테이블 양쪽 열에 저장된 데이터가 저장되어 있을때 사용되는 조인 종류 중 하나
            - 두 테이블의 교집합의 값을 조회해서 보여준다. 즉, 조인 조건을 충족하는 열들의 행 데이터만 조회해서 반환
            - 두 테이블에서 공통된 값이 같은 종류의 열에 저장되어 있는 행만 조회
				즉, 조인 조건을 만족하는 열의 행 데이터만 조회 결과에 포함시키는 조인
			- 문법
				SELECT	하나이상의 테이블에서 조회할 열명들 나열
                FROM	첫번째_테이블명 inner join 두번째_테이블명
                on		첫번째 테이블명.key = 두번째 테이블명.key <- 두 테이블을 연결할 조건식
                where	검색조건식;
*/
use market_db;

select	*
from	member as m inner join buy as b
on		m.mem_id = b.mem_id;