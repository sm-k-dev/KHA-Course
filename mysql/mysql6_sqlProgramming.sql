-- -----------------------------------------------------
-- 04-3절 SQL 프로그래밍 
-- ---------------------------------------------------------------------------
/*
	스토어드 프로시저란?
		데이터베이스 서버에 저장되어 있는 일련 SQL문장들로 구성된 프로그램(개체)
        
	스토어드 프로시저 개체 작성 문법
		-- 여러줄의 SQL 구문을 작성 해 놓을 스토어드 프로시저를 생성하기 위해
        -- MySQL의 기본 구분자(문장의 끝을 나타내는 기호 ;) 를 변경하기 위해 $$로 변경 시키는 구문
			DELIMITER $$
            
            CREATE procedure 스토어드_프로시저_이름()
            begin
				이 영역에 SQL 프로그래밍을 위해 코딩 합니다.
			end $$

		-- 기본 구분자 기호 $$를 세미콜론 ; 기호로 변경 시키는 구문
			DELIMITER ;
		
        -- 위 생성한 스토어드 프로시저 개체를 호출하여 실행시키는 문법
			CALL 스토어드_프로시저_이름();
		
	---------------------------------------------------------------------------
    IF 문: 10 > 100 조건식의 결과 참이면 SQL문을 실행하고, 거짓이면 빠져나가는 조건문
    IF 문 작성 문법
		IF 조건식 THEN
			조건식의 결과가 참일 때 실행될 SQL문의 코드 작성
        END IF;
*/
-- 실습1. 만약 ifProc1이라는 이름의 스토어드 프로시저 개체가 이미 만들어져 있으면 삭제 시키자
DROP procedure if exists ifProc1;

DELIMITER $$

CREATE procedure ifProc1()
begin
	-- 이 영역에 SQL 프로그래밍 코딩 한다.
	IF 100 = 100 THEN
		select '100은 100과 같습니다' as '조회결과';
	END IF;
end $$

DELIMITER ;

-- 바로 위에서 만든 ifProc1() 라는 이름의 스토어드 프로시저 개체를 호출하여 if 문을 실행시킨다.
CALL ifProc1();

/*
	IF ~ ELSE  조건문
		- IF 조건식이 참이면 SQL 문장1 을 수행하고 IF 조건식이 거짓이면 SQL 문장2 를 수행하는 조건문
        - 문법
			IF 조건식 THEN
				SQL 문장 1;
			ELSE
				SQL 문장 2;
			END IF;
*/
-- 실습2. 만약 ifProc2라는 이름의 스토어드 프로시저 개체가 이미 만들어져 있으면 삭제 시키자
DROP procedure if exists ifProc2;

DELIMITER $$
CREATE procedure ifProc2()
begin
	declare myNum int; -- declare 예약어를 사용해 myNum 이라는 이름의 변수 메모리 선언
    set myNum = 200; -- set 예약어를 사용해 myNum이라는 이름의 변수 메모리에 200을 대입해서 저장
		
	-- MyNum 변수 메모리에 저장되어 있는 값이 100과 같은지 묻는 조건식
    IF myNum = 100 THEN 
		select '100입니다';
    ELSE -- myNum 변수 메모리에 저장되어 있는 값이 100이 아니라면
		select '100이 아닙니다';
    END IF;
end $$
DELIMITER ;

-- ifProc2라는 이름의 스토어드 프로시저 개체를 호출하여 SQL코드 실행
CALL ifProc2();

-- ------------------------------------------------------------------------------
/*
	market_db 데이터베이스 내부에 만들어져 있는 member 테이블의 정보를 활용해서
    스토어드 프로시저 개체 생성 후 사용
*/
/*
	실습. 회원 그룹 아이디가 APN(에이핑크)인 회원 그룹의 데뷔일자가 5년이 넘었는지 확인해보고
		5년이 넘었으면 축하 메세지를 만들어서 출력 해 보자.
*/
-- 만약 이미 ifProc3 이라는 스토어드프로시저가 만들어져 있으면 삭제시키자
DROP procedure if exists ifProc3;

DELIMITER $$
CREATE procedure ifProc3()
begin
	declare debutDate 	DATE; -- 데뷔 날짜 저장할 변수 메모리 선언 YYYY-MM-DD 형식의 데이터
	declare curDate   	DATE; -- 현재 날짜 정보를 저장할 변수 메모리 선언
    declare days      	INT; -- 데뷔일로부터 현재 날짜 까지 활동한 일수를 저장할 변수 메모리 선언
    
    -- 에이핑크 그룹의 데뷔일을 조회해서 debutDate변수에 저장하기 위해 into 절을 작성
    -- 문법 select 조회할열명 into debutDate
    select	debut_date into debutDate
    from	market_db.member
    where	mem_id = 'APN';
    
    -- 참고. MySQL서버 컴퓨터의 시스템 날짜 정보를 반환하는 함수 -> current_date() 함수 // 오라클 sys_date() // now()
    set curDate = current_date();
    
    -- 참고. datediff(날짜2, 날짜1) 함수
    -- 		날짜2 부터 날짜1 까지 일수를 계산해서 반환하는 함수
    -- 	에이핑크 데뷔 날짜로부터 현재까지 활동한 일수를 일단위로 구해서 days변수에 저장
    set days = datediff(curDate, debutDate);
    
    -- 에이핑크 데뷔년도가 5년이 지났다면? (조건식)
    if ( days/365 ) >= 5 then
        select concat('데뷔한지 ', days, ' 일이 지났습니다. 핑순이들 축하합니다!');
	else -- 에이핑크 데뷔년도가 5년이 지나지 않았다면?
		select concat('데뷔한지 ', days, ' 일 밖에 안되었네요. 핑순이들 화이팅!');
    end if;
end $$
DELIMITER ;

call ifProc3();

-- 참고. 현재 오늘날짜 정보와 시간정보를 함께 얻고 싶을때
--      ->  current_tiemstamp() 함수를 호출하면  2026-06-19 16:27:59 반환 받을수 있다.

/*
	CASE 문
		- 2가지 이상의 조건식 중 선택해서 실행해야 할 경우 사용하는 선택문
        
        - 문법
			CASE
				WHEN 조건식1 THEN
					조건식1 이 참일때 SQL1 실행;
                    
				WHEN 조건식2 THEN
					조건식2 가 참일때 SQL2 실행;
				
                WHEN 조건식3 THEN
					조건식3 이 참일때 SQL3 실행;
                    
				...
                ELSE
					조건식1, 조건식2, 조건식3... 모두 거짓일때 실행할 SQL;
            END CASE;
*/
/*
	실습1. 시험점수와 학점을 생각 해 봅시다.
		90점이상은 A학점, 
		80점이상은 B학점, 
		70점이상은 C학점, 
		60점이상은 D학점, 
		60점 미만은 F학점 
*/
drop procedure if exists caseProc;

delimiter $$
create procedure caseProc()
begin
	declare point int; -- 시험 점수를 저장할 point라는 이름의 지역변수 선언
    declare credit char(1); -- A 또는 B 또는 C등의 학점을 저장할 credit라는 이름의 지역변수 선언
    set point = 88; -- 시험점수 88을 point지역변수 저장 
    
    case
		when point >= 90 then
			set credit = 'A';
		when point >= 80 then
			set credit = 'B';
		when point >= 70 then
			set credit = 'C';
		when point >= 60 then
			set credit = 'D';
		else
			set credit = 'F';
    end case;
    select concat('취득 점수: ', point) as '취득점수', concat('취득 학점: ', credit) as '학점';
end $$
delimiter ;

call caseProc();

-- -----------------------------------------------------------------------

/*
	실습2. market_db에서 buy 테이블에 구매한 상품정보가 한줄 단위로 저장되어 있다.
		회원 그룹 아이디별 총 구매액을 계산해서 회원 등급을 4단계로 나눈다.
*/
select		m.mem_id as '회원 아이디', m.mem_name as '회원 이름', sum(price * amount) '총 구매액',
	case
			when ( sum(price * amount) >= 1500 ) then '최우수고객'
            when ( sum(price * amount) >= 1000 ) then '우수고객'
            when ( sum(price * amount) >= 1 ) then '일반고객'
            else '유령고객'
	end '회원 등급'
from		buy b
			right join
            member m
            on b.mem_id = m.mem_id
group by	m.mem_id
order by	sum(price * amount) desc;

-- --------------------------------------
-- 이미 만들어져 있는 totalLevel이라는 이름의 스토어드 프로시저 개체가 있으면 삭제
drop procedure if exists totalLevel;

delimiter $$
create procedure totalLevel() -- 회원 그룹 아이디별로 총 구매한 구매액에 따라 회원등급을 조회해서 보여주는 프로시저
begin
	select		m.mem_id as '회원 아이디', m.mem_name as '회원 이름', sum(price * amount) '총 구매액',
		case
				when ( sum(price * amount) >= 1500 ) then '최우수고객'
				when ( sum(price * amount) >= 1000 ) then '우수고객'
				when ( sum(price * amount) >= 1 ) then '일반고객'
				else '유령고객'
		end '회원 등급'
	from		buy b
				right join
				member m
				on b.mem_id = m.mem_id
	group by	m.mem_id
	order by	sum(price * amount) desc;
end $$
delimiter ;

call totalLevel();
-- -------------------------------------------------------------------------------------
/*
	WHILE 반복문
		조건식이 참일 경우 반복실행할 SQL문을 작성하는 구문
	
    WHILE 반복문 작성 문법
		WHILE (조건식) DO
			반복실행할 SQL문장
        END WHILE
*/
-- 이미 만들어져 있는 whileProc이라는 스토어드 프로시저가 만들어져 있으면 삭제
drop procedure if exists whileProc;

-- 실습1. 1에서 100까지의 값을 모두 더하는 간단한 기능의 WHILE 구문 구현
delimiter $$
create procedure whileProc()
begin
	-- 1에서 100까지 1씩 증가 된 값이 저장될 지역 변수 선언
	declare i int;
    -- 1부터 100까지 누적한 값 저장될 지역변수 선언
    declare totalSum int;
    
    set i = 1; -- i 지역 변수에 처음 저장되는 값 (초기값) 1로 저장
    set totalSum = 0; -- totalSum 지역 변수에 초기값으로 0 저장
    WHILE ( i <= 100 ) DO
		SET totalSum = totalSum + i;
        SET i = i + 1;
    END WHILE;
    
    SELECT '1부터 100까지의 합 ==> ', totalSum;
end $$
delimiter ;

call whileProc();

-- 실습2. 1에서 100까지의 합계에서 4의 배수를 제외한 숫자들의 합을 계산
-- 		숫자들의 합이 1000이 넘으면 더 이상 합치지 않고 while 반복문을 빠져나간다.
drop procedure if exists whileProc2;

DELIMITER $$
create procedure whileProc2()
BEGIN
	declare i int;
    declare totalSum int;
    set i = 1;
    set totalSum = 0;
    
    myWhile:
    WHILE ( i <= 100 ) DO
		IF ( i % 4 = 0 ) THEN
			SET i = i + 1;
            ITERATE myWhile; -- 지정한 label문으로 가서 계속 진행
		END IF;
        
        SET totalSum = totalSum + i;
        
        IF ( totalSum > 1000 ) THEN
			LEAVE myWhile; -- 지정한 label문을 떠남. 즉 while 종료
		END IF;
        
        SET i = i + 1;
    END WHILE;
    
    select '1부터 100까지의 합(4의 배수 제외). 1000 넘으면 종료 ==>', totalSum;
END $$
DELIMITER ;

call whileProc2();

-- ----------------------------------------------------------------------
/*
	동적 SQL문 이란?
		- SQL문이 고정된 것이 아니라 상황에 따라 where 조건열의 값을 변경해서
			필요할때 마다 추가해서 완성되는 SQL구문
            
		- 참고. prepare 예약어를 사용한 SQL구문은 SQL문을 미리 준비 해 놓는 구문.
        - 참고. execute 예약어를 사용한 SQL구문은 prepare 예약어를 사용해 준비 해 놓은 SQL문을 실행하는 구문
*/

-- 실습1. member 테이블에 저장된 'BLK'라는 그룹 아이디를 가진 그룹회원의 정보를 조회 하기 위해
-- 		prepare 예약어를 사용해 SQL준비 해 두고
-- 		excute 예약어를 사용해 준비 해 놓은 SQL을 실행해서 조회.

-- 미리 준비 해 놓을 SQL문 작성
-- PREPARE myQuery FROM '미리_준비_해_놓을_SQL문';
PREPARE myQuery FROM 'SELECT * FROM MEMBER WHERE mem_id = "BLK"';

-- prepare 예약어로 미리 준비해서 만들어 놓은 myQuery라는 이름의 SQL을 불러와 execute 예약어로 실행
execute myQuery;

-- 실습2. 출입한 날짜와 시간정보를 저장해 두는 테이블을 생성하고
-- 		출입한 날짜와 시간정보는 동적으로 바뀌는 데이터이므로 INSEDRT 문장을 만들어 놓고 실행

-- 출입한 날짜와 시간정보를 저장 해 두고 관리하는 gate_table 테이블 생성
CREATE TABLE gate_table (
	  id			int auto_increment primary key -- 출입한 회원 아이디가 저장될 열
    , entry_time	datetime -- 출입한 날짜와 시간 정보가 함께 저장될 열 'YYYY-MM-DD hh:mm:ss'
);

-- 현재 출입한 현재 날짜와 시간 정보를 얻어서 변수에 저장
set @curDate = current_timestamp();

select @curDate;

-- prepare 구문을 이용해 출입한 현재 날짜와 시간정보를 동적으로 gate_table 테이블에 추가 하기 위해서 
-- ? 기호를 insert 문장에 포함해 향후 추가될 값(출입한 날짜와 시간정보)을 아직 결정하지 않는 것이다.
prepare mySQL FROM 'insert into gate_table(id, entry_time) value(null, ?)';

-- execute 구문 내부에 using 예약어를 사용해 @curDate 변수에 저장된 현재 날짜와 시간정보를
-- ? 기호 대신 투입해서 insert 문을 실행하게 된다.
-- execute 예약어 구문을 실행하는 시점이 insert 문장이 실행해서 그 시점의 날짜와 시간 정보가 테이블에 추가 저장하게 된다.
execute mySQL using @curDate;