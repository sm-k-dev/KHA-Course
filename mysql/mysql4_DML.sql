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