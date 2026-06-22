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

/*
	실습1. 구매(buy)테이블과, 회원(member)테이블을 사용하자.
		구매 테이블에는 구매한 상품 정보만 저장되어 있고,
        회원 테이블에는 가입한 그룹의 회원 정보만 저장되어 있다.
        회원에게 구매한 상품정보를 배송하려면 회원의 주소, 연락처가 있는 member 테이블과
        구매 상품정보 (buy 테이블)를 결합해서 조회해서 가져와야 한다.
	
    구매(buy) 테이블에서 GRL 이라는 그룹 아이디를 가진 회원그룹이 구매한 물건을 배송하기 위해
    inner join을 통해 회원그룹이름/회원그룹주소/연락처/구매한상품명 등을 조회할 수 있다.
*/

select	m.mem_name, m.addr, m.phone1, m.phone2, b.prod_name
from	member as m inner join buy as b
on		m.mem_id = b.mem_id
where	b.mem_id = 'GRL';

/*
	buy 테이블과 member 테이블을 inner join하여
    전체 그룹회원의 아이디, 이름, 구매한 제품명, 주소정보 조회
    전체 그룹회원 아이디를 기준으로 오름차순 정렬
*/

select		m.mem_id, m.mem_name, b.prod_name, m.addr
from		member as m inner join buy as b
on			m.mem_id = b.mem_id
order by	m.mem_id;

/*
	우리 사이트에서 한번이라도 구매한 기록이 있는 회원그룹들에게 감사의 안내문을 발송해야 한다면
    회원 그룹 아이디, 회원 그룹 이름, 주소 를 중복없이
    buy 테이블과 member테이블의 inner join을 하여 열을 조회
*/
select		distinct m.mem_id, m.mem_name, m.addr, b.prod_name
from		member m inner join buy b
on			m.mem_id = b.mem_id
order by	m.mem_id;

/*
	2. 외부 조인 (OUTER JOIN)
		- 두 테이블 중에 한쪽 기준이 되는 테이블의 열에만 데이터가 저장되어 있어도
			두 테이블의 행에 관한 비어있는 열값도 같이 조회하기 위한 조인
		
        외부 조인 종류
		- 1. LEFT OUTER JOIN ( LEFT JOIN )
        - 2. RIGHT OUTER JOIN ( RIGHT JOIN )
        - 3. FULL OUTER JOIN ( FULL JOIN )
        
        1. LEFT OUTER JOIN
			기준이 되는 왼쪽 테이블의 열 데이터를 모두 조회 하되,
            오른쪽에 테이블의 열값이 NULL로 비어있는 값이 있더라도 같이 조회 결과로 보여주는 조인
*/
-- 실습1. 전체 그룹 회원 중에서 구매기록이 없는 그룹회원의 정보도 함께 모두 조회 하기 위해 
-- 		LEFT OUTER JOIN 사용
select		m.mem_id, m.mem_name, b.prod_name, m.addr
from		member m left outer join buy b
on			m.mem_id = b.mem_id
order by	m.mem_id;

/*
		2. RIGHT OUTER JOIN
			오른쪽에 작성한 테이블을 기준으로 열에 모든 데이터가 저장되어 있으면
			왼쪽 테이블 열의 데이터가 저장되어 있지 않아도 모든 열값을 NULL로 채워서 조회
*/
-- 실습2. 전체 그룹 회원 중에서 구매기록이 없는 그룹회원의 열 정보도 함께 모두 조회 하기위해
-- 		RIGHT OUTER JOIN 사용
select		m.mem_id, m.mem_name, b.prod_name, m.addr
from		buy b right outer join member m
on			b.mem_id = m.mem_id
order by	m.mem_id;

-- 실습3. 전체 회원 그룹중에서 한번도 구매한 기록이 없는 그룹회원 목록을 조회 하기 위해
-- 		LEFT OUTER JOIN 사용
-- 		단, 열의 데이터가 중복되면 하나로 합쳐서 하나의 데이터로 조회
select	distinct m.mem_id, b.prod_name, m.mem_name, m.addr
from	member m left outer join buy b
on		m.mem_id = b.mem_id
where	b.prod_name is null;

-- ------------------------------------------------------------------------------------------------
-- 주제: LEFT OUTER JOIN, RIGHT OUTER JOIN 예
-- ------------------------------------------------------------------------------------------------
-- a 테이블 생성
create table a (
	  id	int primary key -- 회원 id 
    , name	varchar(50)
);

-- a 테이블에 새로운 3개 행 데이터 다중 삽입 해서 저장
insert into a ( id, name )
		values	  ( 1, 'Alice' )
				, ( 2, 'Bob' )
                , ( 3, 'Charlie' );

-- b 테이블 (a 테이블에 저장된 회원이 주문한 상품 정보가 행 단위로 저장 되는 테이블) 생성
create table b (
	  id			int primary key -- 주문 id
    , order_item	varchar(50)		-- 주문한 상품 이름
);

-- b 테이블에 3개의 행 데이터(주문한 상품 정보) 삽입
insert into b ( id, order_item )
		values	  ( 2, 'Laptop')
				, ( 3, 'Smartphone' )
                , ( 4, 'Tablet' );

-- LEFT OUTER JOIN
select	a.id, b.id, a.name, b.order_item
from	a left outer join b
on		a.id = b.id;

-- RIGHT OUTER JOIN
select	a.id, b.id, a.name, b.order_item
from	a right outer join b
on		a.id = b.id;

-- ------------------------------------------------------------------------------------------------
/*
	상호 조인 (CROSS JOIN)
		한쪽 테이블의 모든 행과 다른쪽 테이블의 모든 행을 조인해서 조회 하는 구문
        그래서 상호 조인 조회 결과의 전체 행 개수는 각 테이블의 행의 개수를 곱한 값이 된다.
        대용량 데이터를 조회 해서 만들어 테이블을 새로 생성할때 사용된다.
	
    상호 조인의 특징
		- on 구문을 사용할 수 없다.
        - 조회 결과 행의 열 데이터들에 의미가 없다.
        - 상호 조인의 주 용도는 대용량 데이터를 테스트하기 위해 데이터를 생성할때 조회하게 된다.
*/
-- ------------------------------------------------------------------------------------------------
-- buy 테이블과 member테이블 상호(cross) 조인 해서 조회
select *
from member cross join buy;

select count(*)
from member cross join buy;

-- 샘플 데이터베이스인 sakila 데이터베이스의 inventory 테이블에 저장된 전체 행 개수 조회
select count(*) from sakila.inventory; -- 4581

-- 샘플 데이터베이스인 world 데이터베이스의 city 테이블에 저장된 전체 행 개수 조회
select count(*) from world.city; -- 4079

-- sakila 의 inventory와 world의 city cross join
-- 4582 * 4079 = 18,685,899
select count(*) as '행(데이터)의 개수'
from sakila.inventory cross join world.city;

-- ------------------------------------------------------------------------------------------------
/*
	대용량 데이터가 저장되는 테이블을 만들고 싶으면?
		방법: create table 만들테이블명
				select * from 합칠테이블명1 cross join 합칠테이블명2;
*/
create table cross_table
	select		*
    from		sakila.actor -- 200건 
    cross join 	world.country; -- 239건

-- cross_table 테이블에 저장된 총 행(레코드) 개수 조회 -- 47800행(레코드) 조회
select 	count(*)
from	cross_table;

-- ------------------------------------------------------------------------------------------------
/*
	자체 조인 (SELF JOIN)
		- 자신의 테이블의 두개의 열 데이터를 이용해 조회 하기 위한 조인 구문
        자체 조인은 하나의 테이블에 서로 다른 별칭을 설정해서 조인하는 구문
	
    자체 조인 문법
		SELECT	조회할_열_명
        FROM	테이블명 별칭A INNER JOIN 테이블명 별칭B
        ON		조인이 될 조건식
        WHERE	검색 조건식;
*/
-- ------------------------------------------------------------------------------------------------
-- 실습1. market_db 데이터베이스 내부에 emp_table 이라는 이름의 테이블 생성 후 각 데이터 저장
create table emp_table (
	  emp		char(4) 	-- 회사 직급 정보 저장 ('대표', '영업이사', '인사부장' 등등)
    , manager	char(4) 	-- 직속 상관
    , phone		varchar(8) 	-- 사내 연락처
);

INSERT INTO emp_table VALUES('대표', NULL, '0000');

INSERT INTO emp_table VALUES('영업이사', '대표', '1111');
INSERT INTO emp_table VALUES('관리이사', '대표', '2222');
INSERT INTO emp_table VALUES('정보이사', '대표', '3333');

INSERT INTO emp_table VALUES('영업과장', '영업이사', '1111-1');
INSERT INTO emp_table VALUES('경리부장', '관리이사', '2222-1');
INSERT INTO emp_table VALUES('인사부장', '관리이사', '2222-2');
INSERT INTO emp_table VALUES('개발팀장', '정보이사', '3333-1');

INSERT INTO emp_table VALUES('개발주임', '정보이사', '3333-1-1');

select * from emp_table;

-- 실습2. 경리부장의 직속상관의 연락처
select	a.emp '직원', b.emp '직속상관', b.phone '직속상관 연락처'
from	emp_table a inner join emp_table b
on		a.manager = b.emp
where	a.emp = '경리부장';
