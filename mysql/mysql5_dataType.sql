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