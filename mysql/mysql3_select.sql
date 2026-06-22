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