-- ----------------------------------------------------------------------------------------------------------
############# 05 - 1 테이블 만들기 책 내용 ##############
 
-- ----------------------------------------------
-- 1단계. 데이터베이스 생성
-- ----------------------------------------------
-- naver_db라는 이름의 새로운 데이터베이스 생성
create database naver_db;
 
-- ----------------------------------------------
-- 2단계. 데이터베이스 삭제 후 생성
-- (초기화를 위해 기존 데이터베이스를 지우고 다시 만든다)
-- ----------------------------------------------
drop database if exists naver_db;
 
-- 삭제 후 다시 naver_db 생성
create database naver_db;
 
-- ----------------------------------------------
-- 3단계. 사용할 데이터베이스 선택
-- ----------------------------------------------
use naver_db;
 
-- ----------------------------------------------
-- 4단계. 회원 가입 정보가 저장되는 member 테이블 생성
-- ----------------------------------------------
-- member 테이블이 이미 생성되어 있다면 삭제
drop table if exists member;
-- member 테이블 생성
create table member(
	 mem_id		char(8) not null primary key
   , mem_name		varchar(10) not null
   , mem_number	tinyint not null 		-- 회원그룹 인원수 (최대 255명)
   , addr			char(2) not null 	-- 회원 주소 (예: 서울, 경기 등 2글자)
   , phone1		char(3) null			-- 전화번호 앞자리 (예: 02, 031 등, 생략가능)
   , phone2		char(8) null			-- 전화번호 뒷자리 (예: 12341234 하이픈제외, 생략가능)
   , height		tinyint unsigned null	-- 키 (양수만 허용, 생략가능)
   , debut_date	date null				-- 데뷔일자 (형식: YYYY-MM-DD, 생략가능)
);

-- ----------------------------------------------
-- 5단계. 구매 테이블(buy) 생성
-- ----------------------------------------------
-- buy 테이블이 이미 생성되어 있다면 삭제
drop table if exists buy;
-- buy 테이블 생성
create table buy(
	 num		int auto_increment not null primary key -- 구매 순번
   , mem_id		char(8) not null						-- 회원 그룹 고유 ID (member테이블의 mem_id열 값과 연결, 외래키로 설정)
   , prod_name	char(6) not null						-- 구매한 제품 이름 (예: 아이폰, 맥북 등 최대 6글자)
   , group_name	char(4) null							-- 구매한 제품의 제품분류 (예: 디지털, 식품 등 생략가능)
   , price		int unsigned not null					-- 구매한 제품 가격 (양수만 허용)
   , amount		smallint unsigned not null				-- 구매한 제품 수량 (양수만 허용)
   
   -- 외래키 설정: buy 테이블의 mem_id 열 값은 반드시 member 테이블의 mem_id 열의 값과 일치해야 함
   , foreign key ( mem_id ) references member ( mem_id )
);

-- ----------------------------------------------
-- 6단계. 회원(member) 테이블에 행(레코드) 추가
-- ----------------------------------------------
-- 트와이스 회원 행 추가
INSERT INTO member VALUES('TWC', '트와이스', 9, '서울', '02', '11111111', 167, '2015-10-19');
INSERT INTO member VALUES('BLK', '블랙핑크', 4, '경남', '055', '22222222', 163, '2016-8-8');
INSERT INTO member VALUES('WMN', '여자친구', 6, '경기', '031', '33333333', 166, '2015-1-15');

-- ----------------------------------------------
-- 7단계. 구매(buy) 테이블에 행(레코드) 추가
-- ----------------------------------------------
INSERT INTO buy VALUES( NULL, 'BLK', '지갑', NULL, 30, 2);
INSERT INTO buy VALUES( NULL, 'BLK', '맥북프로', '디지털', 1000, 1);
-- 존재하지 않는 회원그룹(APN)이 아이폰을 구매하려 시도 (실행 시 오류 발생)
INSERT INTO buy VALUES( NULL, 'APN', '아이폰', '디지털', 200, 1);

-- --------------------------------------------------------------------------------------------
-- 05-2절. 제약조건으로 테이블을 견고하게
-- --------------------------------------------------------------------------------------------

-- 사용할 데이터베이스 선택
use naver_db;

/*
	===========================
	기본키 (Primary Key) 제약조건
	 - MySQL에서 기본키 제약조건은 특정 열(컬럼) 또는 열들의 조합에 
	   중복된 값이나 NULL(빈값)을 허용하지 않도록 제한하는 규칙입니다.
	 - 즉, "이 컬럼은 각 행(row)마다 고유한 값이고 반드시 있어야 해!" 라고 강제하는 기능입니다.
    ---------------------------
*/

-- 기존 buy, member 테이블이 존재하면 삭제 (초기화 목적)
drop table if exists buy, member;

-- [방법1] 테이블 생성 시, 열 옆에 바로 primary key 제약조건 지정 ----------------------------------------
create table member (
	  mem_id	char(8)		not null primary key	-- mem_id열: 기본키 설정 (중복 x, null x)
    , mem_name	varchar(10)	not null				-- 회원 이름 (반드시 입력)
    , height	tinyint		unsigned null			-- 회원 평균키 (양수만 허용)
);
-- 테이블 열 구조 확인 (열 명 정보 + 제약 조건 등 확인 가능)
describe member; -- desc member; 와 같다

-- [방법2] 테이블 생성 시 마지막 줄에 primary key(열 이름) 지정 가능 ----------------------------------------
drop table if exists member; -- 기존 member 테이블 삭제

create table member (
	  mem_id	char(8)		not null		-- mem_id열: 기본키 설정 (중복 x, null x)
    , mem_name	varchar(10)	not null		-- 회원 이름 (반드시 입력)
    , height	tinyint		unsigned null	-- 회원 평균키 (양수만 허용)
    
    -- 아랫 줄 영역에서 mem_id 열 (컬럼)을 기본키로 지정
    , primary key ( mem_id )				-- 테이블 맨 아래에 따로 선언하는 방식
);

-- [방법3] 테이블을 먼저 만들고, ALTER TABLE 구문으로 기본키 제약 조건 추가 ----------------------------------------
drop table if exists member; -- 기존 member 테이블 삭제

create table member (
	  mem_id	char(8)		not null		-- mem_id열: 기본키 설정 (중복 x, null x)
    , mem_name	varchar(10)	not null		-- 회원 이름 (반드시 입력)
    , height	tinyint		unsigned null	-- 회원 평균키 (양수만 허용)
);

-- 이 단계에서 기본키 제약조건을 따로 추가 (기존 테이블을 변경)
alter table member	-- 변경할 테이블 선택
add constraint	-- 제약 조건 추가 명령어
primary key (mem_id);	-- 기본 키 제약 조건을 mem_id 열에 설정

-- [방법4] CONSTRAINT 이름 붙이면서 기본키 지정 (가독성, 관리 편리) ----------------------------------------
-- 		요약: 기본키에 이름 지정 하기
drop table if exists member; -- 기존 member 테이블 삭제

create table member (
	  mem_id	char(8)		not null		-- mem_id열: 기본키 설정 (중복 x, null x)
    , mem_name	varchar(10)	not null		-- 회원 이름 (반드시 입력)
    , height	tinyint		unsigned null	-- 회원 평균키 (양수만 허용)
    
    -- CONSTRAINT 키워드를 통해 제약조건에 이름 부여
    -- 제약조건 이름 만드는 방법: 제약조건축약어_테이블명_열명
    , constraint PK_member_mem_id primary key ( mem_id )
);

/*
	===========================
	외래키 (Foreign Key) 제약조건
    ---------------------------
		- 두 테이블 간의 관계를 설정하는 제약 조건
        - 자식 테이블의 열이 부모 테이블의 '기본 키'를 참조하게 함으로써
			두 테이블의 연결(관계)을 유지하고, 논리적으로 맞지 않는 데이터가 열에 저장되는 것을 막을 수 있다.
*/

-- 기존 buy, member 테이블이 존재하면 삭제 (초기화 목적)
drop table if exists buy, member;

-- [방법1] 테이블 생성 시 마지막 줄에 foreign key (열 이름) references 기준테이블 (열 이름) 지정 가능 ----------------------------------------
-- 1단계. 회원 정보를 저장할 member 테이블 만들기 (부모 테이블)
create table member (
	  mem_id	char(8)		not null primary key	-- mem_id열: 기본키 설정 (중복 x, null x)
    , mem_name	varchar(10)	not null				-- 회원 이름 (반드시 입력)
    , height	tinyint		unsigned null			-- 회원 평균키 (양수만 허용)
);

-- 2단계. 구매 정보를 저장할 buy 테이블 만들기 (자식 테이블)
create table buy (
	  num		int auto_increment not null primary key
    , mem_id	char(8)	not null
    , prod_name	char(6) not null
    
    , foreign key ( mem_id ) references member ( mem_id )
    /*
		외래키 제약조건 설정
        문법: foreign key (열 이름) references 기준 테이블 (열 이름)
        자식 테이블의 열 이름에 저장된 데이터는 반드시 기준 테이블의 열이름에 존재해야 한다 (저장 되어 있어야 함)
        데이터의 무결성을 보장한다 -> 논리적으로 말이 안 되는 데이터를 차단
    */
);
/*
	참고. 외래키의 열 이름이 기준 테이블의 열 이름과 꼭 같아야 하는 것은 아니다.
    열에 저장되는 데이터만 같으면 참조 가능하다.
*/

-- 기존 buy, member 테이블이 존재하면 삭제 (초기화 목적)
drop table if exists buy;

-- [방법2] 테이블을 먼저 만들고, ALTER TABLE 구문으로 기본키 제약 조건 추가 ----------------------------------------
-- 1단계. 구매 정보를 저장할 buy 테이블 만들기 (자식 테이블)
create table buy (
	  num		int auto_increment not null primary key
    , user_id	char(8)	not null
    , prod_name	char(6) not null
);

-- 2단계. buy 구매 테이블에 외래 키 제약 조건을 추가
alter table buy
add constraint
foreign key ( user_id ) references member ( mem_id );

/*
	===========================
	CASCADE
		- ON UPDATE CASCADE
        - ON DELETE CASCADE
    ---------------------------
*/
-- 1단계. member 테이블에 회원 한명 추가 (BLK)
-- 블랙핑크 회원 정보 입력
insert into member value ('BLK', '블랙핑크', 163);

-- 2단계. 참조 buy 테이블에 구매 정보 추가
-- 회원 BLK가 상품 '지갑'구매 입력
insert into buy values (null, 'BLK', '지갑');
-- 회원 BLK가 상품 '맥북'구매 입력
insert into buy values (null, 'BLK', '맥북');

-- 3단계. 두 테이블 join 조회
select	*
from	buy b join member m
on		m.mem_id = b.user_id;

-- 4단계. member 테이블의 기본키 제약조건이 설정된 mem_id 열 값 'BLK'를 'PINK'로 변경 시도
update	member
set		mem_id = 'PINK'
where	mem_id = 'BLK';
-- ❗ 문제 발생 가능:
--    - buy 테이블은 여전히 'BLK'를 참조하고 있음
--    - 외래 키가 걸려 있으나 ON UPDATE CASCADE가 설정되지 않은 경우
--    - 두 테이블의 연결 관계가 끊어짐 (buy에는 여전히 'BLK', member에는 'PINK')
-- ❌ 결과:
-- 💥 일부 MySQL 버전에서는 에러가 발생하거나,
-- 💥 에러 없이 member 테이블만 바뀌고 buy 테이블은 'BLK' 상태로 남음 (불일치 발생)
-- 🔴 외래 키 제약조건 위반 에러 예시:
-- ERROR 1451 (23000): Cannot update or delete a parent row: a foreign key constraint fails (`buy`, CONSTRAINT `buy_ibfk_1` FOREIGN KEY (`mem_id`) REFERENCES `member` (`mem_id`))

-- 5단계. member 테이블의 BLK 행 삭제 시도
delete from member
where mem_id = 'BLK';
-- ❗ 문제:
--    - buy 테이블이 아직 'BLK'를 참조 중이라 외래 키 충돌 발생
-- ❌ 결과:
-- 💥 외래 키 제약조건 위반으로 삭제 실패
-- 🔴 발생 가능한 오류 메시지:
-- ERROR 1451 (23000): Cannot delete or update a parent row: a foreign key constraint fails (`buy`, CONSTRAINT `buy_ibfk_1` FOREIGN KEY (`mem_id`) REFERENCES `member` (`mem_id`))

/*
======================================================
🎯 외래 키(Foreign Key) + CASCADE 옵션 실습

📌 목표:
   - 외래 키 제약조건 + ON UPDATE / DELETE CASCADE 옵션의 동작 확인
   - 기준 테이블(member)의 ID가 바뀌거나 삭제되면 참조 테이블(buy)도 자동 반영되는지 실습

📘 사전 준비:
   - member 테이블에 mem_id = 'BLK'인 회원이 있어야 함
     예시: INSERT INTO member VALUES('BLK', '블랙핑크', 163);

======================================================
*/
 -- ✅ 1단계: 기존의 buy 테이블이 있다면 삭제 (초기화)
DROP TABLE IF EXISTS buy;

-- ✅ 2단계: 새로운 buy 테이블 생성
CREATE TABLE buy (
   num         INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 구매 번호 (자동 증가, 기본키)
   mem_id      CHAR(8) NOT NULL,                        -- 회원 아이디 (member 테이블의 mem_id를 참조)
   prod_name   CHAR(6) NOT NULL                         -- 구매한 상품명
);

-- ✅ 3단계: 외래 키(FK) 제약조건 추가
--          - mem_id는 member 테이블의 mem_id 값을 참조함
--          - ON UPDATE CASCADE: 기준 테이블의 ID가 변경되면 buy 테이블도 자동 반영
--          - ON DELETE CASCADE: 기준 테이블에서 회원 삭제 시, 해당 회원의 구매 기록도 같이 삭제됨
ALTER TABLE buy
    ADD CONSTRAINT 
    FOREIGN KEY(mem_id) REFERENCES member(mem_id)
    ON UPDATE CASCADE     -- 기준 테이블(member)의 mem_id가 바뀌면 buy 테이블의 mem_id도 자동으로 바뀜
    ON DELETE CASCADE;    -- 기준 테이블에서 회원 삭제 시, 연결된 구매 기록도 자동 삭제됨

-- ✅ 4단계: 참조 테이블인 buy에 구매 데이터 추가
--          - mem_id = 'BLK'인 회원이 '지갑'과 '맥북'을 구매한 기록을 추가
INSERT INTO buy VALUES(NULL, 'BLK', '지갑');   -- 구매 번호는 자동 증가, 회원 아이디 'BLK', 상품명 '지갑'
INSERT INTO buy VALUES(NULL, 'BLK', '맥북');   -- 같은 회원이 '맥북'도 구매

-- ✅ 5단계: 기준 테이블(member)의 회원 아이디를 변경
--          - 'BLK' → 'PINK'로 바꾸면
--          - buy 테이블의 mem_id도 자동으로 'PINK'로 바뀜 (ON UPDATE CASCADE 작동)
UPDATE member 
SET mem_id = 'PINK'
WHERE mem_id='BLK';

-- ✅ 6단계: 두 테이블을 조인하여 결과 확인
--          - buy 테이블과 member 테이블을 mem_id 기준으로 INNER JOIN
--          - 결과는 mem_id = 'PINK', 이름 = '블랙핑크', 구매상품 = '지갑', '맥북'
SELECT M.mem_id, M.mem_name, B.prod_name 
FROM buy B INNER JOIN member M
ON B.mem_id = M.mem_id;

select * from buy;

-- ✅ 7단계: 기준 테이블(member)에서 'PINK' 회원을 삭제
--          - 이 회원이 구매했던 buy 테이블의 데이터도 자동으로 삭제됨 (ON DELETE CASCADE 작동)
DELETE FROM member 
WHERE mem_id='PINK';

-- ✅ 8단계: buy 테이블 조회
--          - 위에서 회원이 삭제되었기 때문에, 그와 관련된 구매 정보도 자동으로 사라짐
--          - 결과는 아무 행도 출력되지 않음 (빈 테이블)
SELECT * FROM buy;

/*
	====================================================
	실습 주제 : 고유 키 제약조건(UNIQUE constraint)
	 
	핵심 개념
	- UNIQUE 제약조건은 해당 열의 값이 중복되지 않도록 막아주는 규칙입니다.
	- 즉, 중복된 값을 가진 레코드는 입력할수 없습니다.
	 
	실습 배경
	-  회원 테이블에 이메일(email) 열을 추가하고, 이 열에 UNIQUE 제약조건을 설정 합니다.
	-  이메일 주소는 원래 사용자 마다 다르므로, 일반적으로 이메일 중복 되면 안됩니다.
	====================================================
*/
-- 1단계 :  기존 member, buy 테이블이 만들어져 있다면 삭제
drop table if exists member,buy;

-- 2단계  : member 테이블 생성
create table member(
	mem_id  char(8)         not null primary key,  -- 회원 ID : 중복 불가 , 무조건 열에 값 저장
	mem_name varchar(10)    not null,
	height   tinyint  unsigned  null,
	email    char(30)           null  unique -- 이메일 다른 열에 중복 저장 불가 
);
-- 💡 POINT!
-- email 컬럼에 UNIQUE 제약조건이 걸려 있어서
-- → '같은 이메일 주소'를 여러 회원이 사용할 수 없습니다.
-- → 단, NULL(입력 안함)은 중복해도 허용됩니다 (MySQL은 NULL != NULL 로 취급)


-- 3단계  : 회원 데이터 삽입

-- 첫번째 회원  -> 정상 입력
insert into member values('BLK', '블랙핑크', 163, 'pink@gmail.com');

-- 두번째 회원   -> 이메일 입력안함 : UNIQUE 조건 위반은 아님 !
insert into member values('TWC', '트와이스', 167,  null);

-- 세번쨰 회원   -> 이메일 입력 : 'pink@gmail.com'로 중복됨 -> UNIQUE 조건 위반 -> 오류발생!
insert into member values('APN', '에이핑크', 164, 'pink@gmail.com');

-- 예상 오류 메시지 예시 (DBMS에 따라 다름):
-- ERROR 1062 (23000): Duplicate entry 'pink@gmail.com' for key 'member.email'


-- 위의 INSERT 중 오류가 발생한 3번째 INSERT는 실행되지 않으므로,
-- SELECT 결과에는 '에이핑크' 데이터는 보이지 않음.
select * from member;

/*
	===========================================================
	🎯 실습 주제 : CHECK 제약조건 (값의 유효성 검사 조건)

	📌 핵심 개념
	- CHECK 제약조건은 "이 컬럼(열)에는 반드시 이런 조건을 만족하는 값만 저장할 수 있다!" 는 규칙을 설정하는 것.
	- 예를 들어 키(height)는 반드시 100 이상이어야 하고,
	 전화번호 앞자리(phone1)는 특정 지역 번호만 가능하게 제한할 수 있음.
	===========================================================
*/ 
-- 1단계 :  기존 member테이블이 만들어져 있다면 삭제
drop table if exists member;

-- 2단계  : member 테이블 생성
create table member(
	mem_id  char(8)         not null primary key,  -- 회원 ID : 중복 불가 , 무조건 열에 값 저장
	mem_name varchar(10)    not null,
	height   tinyint  unsigned  null  check(height >= 100),  -- height열에는 100이상의 키만 저장 
	phone    char(3)        null
);

-- 3단계 : check(height >= 100) 체크 제약 조건 테스트 

-- [성공] 키가 163이므로 조건 만족 
insert into member values('BLK', '블랙핑크', 163, null);

-- [실패] 키가 99이므로 조건 불만족 -> check 제약조건 위반 
insert into member values('TWC', '트와이스', 99, null);
-- 오류 메시지 예시 (MySQL 8.0 이상에서만 적용됨)
-- ERROR 3819 (HY000): Check constraint 'member_chk_1' is violated.

-- 💡 일부 MySQL 버전(특히 5.x)은 CHECK 제약조건을 무시합니다.
--    MySQL 8.0 이상에서는 체크가 실제 동작합니다.

-- 4단계  :   phone 열에도 check 제약조건 설정을 추가 할수 있음 (특저 지역번호만 허용)
alter table member
add constraint
check( phone IN('02', '031', '032', '054', '055', '061') );
-- >>>> phone 열에는 반드시 IN 절 안에 작성한 데이터들만 저장할수 있다. 

-- 5단계 : check(phone IN(......)) 조건 테스트 
delete from member
where mem_id = 'BLK';

-- [성공] phone 열에 null 이 설정 되어 있어 빈값 허용!
insert into member(mem_id, mem_name, height, phone) values('BLK', '블랙핑크', 163, null);

-- [성공] phone 열에 추가되는 값이 '02'이므로 허용함 -> check제약조건대로 열에 추가 
insert into member values('OML', '어린친구', 168, '02');

-- [실패] phone 열에 추가될값이 '010'이므로 허용되지 않음 -> check제약조건 위반 
insert into member                                  values('OMY', '오마이걸', 167, '010');
-- 오류 메시지 예시:
-- ERROR 3819 (HY000): Check constraint 'member_chk_2' is violated.

/*
	==========================================================
	🎯 실습 주제 : 기본값 (DEFAULT) 제약 조건 사용법

	📌 핵심 개념
	- 특정 컬럼에 값을 입력하지 않아도, 미리 정한 기본값이 자동으로 입력되도록 설정하는 방법
	- 예) 키(height) 값이 입력되지 않으면 자동으로 160이 들어가게 설정
	- 예) 전화번호 앞자리(phone1)가 입력되지 않으면 자동으로 '02'가 들어가게 설정
	==========================================================
*/
-- 1단계 : 이전에 만든 member 테이블 삭제
drop table if exists member;

-- 2단계 : member 테이블 새로 생성 (default 디폴트 제약조건을 height열에 160으로 설정)
create table member(

	mem_id     char(8)          not null  primary key,
	mem_name   varchar(10)      not null,
	height     tinyint unsigned     null  default 160, -- 입력이 없으면 기본값 160이 자동으로 insert추가됨
	phone1     char(3)              null
);
-- 🔍 height 컬럼에 DEFAULT 160 지정
-- ⇒ INSERT 시 height 생략하면 자동으로 160이 입력됨

/*
==========================================================
✅ 3단계: ALTER TABLE로 기존 컬럼(phone1)에 기본값 추가
- phone1이 비어 있으면 자동으로 '02'를 입력하게 설정
==========================================================
*/
alter table member
alter column phone1  set default '02';

/*
4단계: 데이터 입력(추가) 실습  
*/
-- > 첫번쨰 insert  : 열에 추가할 값 명시적으로 입력
--  height = 161,  phone1 = '054'  직접 입력 -> 기본값 사용 안함
insert into member values('RED', '레드벨벳', 161, '054');

-- > 두번째 insert : height과 phone1열에 추가할 값을 생략하고 기본값 사용 
-- height = default ->  height에 자동으로 160 입력됨 
-- phone1 = default ->  phone1에 자동으로 '02' 입력됨 
insert into member values('SPC', '우주소녀', default, default);

insert into member (mem_id, mem_name)   values('BLK', '블랙핑크');

select * from member;

describe member;
/*
	==========================================================
	🎯 실습 주제 : NULL 값과 NOT NULL 제약 조건의 의미와 사용법

	📌 핵심 개념 정리

	🔹 NULL이란?
	- 아무 값도 입력되지 않은 상태 (공백 ''이나 숫자 0과는 다름)
	- 예) 전화번호를 아직 모르면 NULL로 입력 가능

	🔹 NOT NULL이란?
	- 반드시 값을 입력해야 한다는 의미
	- NULL을 허용하지 않도록 설정
	- 즉, 비워둘 수 없다 ❌

	🔹 PRIMARY KEY(기본키)는?
	- 각 행(row)을 유일하게 식별하는 열(column)
	- 자동으로 NOT NULL이 적용됨 (즉, NULL 입력 불가!)

	==========================================================
*/
#  실습으로 확인해보기
-- 이전에 만든 테이블이 있다면 삭제
drop table if exists member;

-- 새로운 member 테이블 생성
create table member(

	mem_id  char(8)  primary key, -- 기본키 : 자동으로 not null 이 적힘 
								  -- 기본키 의미 :  열에 고유값  +  열에 빈값 허용 X
	mem_name varchar(10)       not null,
	height   tinyint unsigned,    -- 키는 열에 입력하지 않아도 됨 (NULL 허용)
	email   char(30)              -- 이메일도 열에 입력하지 않아도됨 (NULL 허용)
);
-- 예제 : 다양한 입력 케이스 
-- 1. 모든 값을 정상적으로 입력하는 경우 -> 추가됨 
insert into member values('BLK', '블랙핑크', 163,  'pink@gmail.com');

-- 2. 키와 이메일 생략(NULL 허용) -> 추가됨 
insert into member values('TWC',  '트와이스', NULL,  NULL);

-- 3. 이름(mem_name)입력을 생략한 경우  -> 오류가 발생함
insert into member values('APN',  NULL,  160,  'apink@gamil.com');

-- 4. 기본키 제약조건이 설정된 mem_id열값 입력을 생략한 경우 -> 오류가 발생함
insert into member values(NULL, '아이유', 160, 'iu@gamil.com');