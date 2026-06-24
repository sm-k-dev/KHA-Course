-- --------------------------------------------------------------------------
-- 05-3절. 가상의 테이블 : 뷰 
-- ---------------------------------------------------------------------------

/*
	=====================================================
	🎯 주제 : 뷰(View)의 개념과 사용법

	📌 1. 뷰(View)란?
	   - **하나 이상의 테이블에서 가져온 결과(SELECT문)를 마치 가상의 테이블처럼** 사용할 수 있게 해주는 것
	   - 실제 데이터를 저장하지 않고, 
		 **SELECT문의 결과를 저장해 놓은 "가상 테이블"**이라 생각하면 됨

	📌 2. 뷰를 사용하는 이유
	   - 복잡한 SELECT문을 매번 쓰지 않고, 뷰로 만들어 놓고 간단히 조회 가능
	   - 민감한 정보(예: 급여, 주민번호 등)를 감추고 보여줄 열만 선택 가능
	   - 유지보수나 재사용에 유리함

	📌 3. 뷰의 장점 요약
	   ✅ 복잡한 SQL을 단순하게 재사용
	   ✅ 보안성을 높여 민감한 열을 숨길 수 있음
	   ✅ 테이블 구조가 바뀌어도 뷰를 통해 일관된 방식으로 접근 가능

	=====================================================

	📚 뷰를 만드는 형식(문법)

	CREATE VIEW 뷰_이름
	AS
	   SELECT 열1, 열2, ... FROM 테이블명
	   WHERE 조건;

	📚 뷰를 사용하는 형식(문법)

	SELECT 열1, 열2
	FROM 뷰_이름
	WHERE 조건;

	=====================================================
*/
# 1. market_db 데이터베이스 사용을 위해 선택
use market_db;

# 예제. 회원 테이블의 아이디, 이름,주소에 접근하는 뷰를 생성해 봅시다.

#2. 실제 테이블인 member에서 필요한 열(mem_id, mem_name, addr)값만 골라서 조회 해 오기 
#  -> 뷰로 만들기 전에 어떤 열의 값이 필요한지 먼저 SELECT 해봄
select mem_id, mem_name, addr from member;

#3. 뷰(VIEW)를 생성
-- ▶ v_member 라는 이름의 "가상 테이블(뷰)"을 만든다
-- ▶ 이 뷰는 SELECT mem_id, mem_name, addr FROM member 쿼리의 결과를 저장해둔 가상 테이블
-- ▶ 즉, v_member 라는 이름으로 회원의 아이디, 이름, 주소 정보를 항상 조회할 수 있게 됨
create view v_member
as
	select mem_id, mem_name, addr from member;

#4. 방금 만든 뷰 v_member를 이용해 member테이블의 mem_id, mem_name, addr 열의 값만 조회 가능
-- ▶ 실제 member 테이블을 직접 조회하지 않고도, 필요한 열만 필터링해서 확인 가능
select * from v_member;

#5. 뷰를 이용한 조건 조회
-- > v_member 뷰에서 주소(addr)가 '서울' 또는 '경기'인 회원 만 조회 
-- > 원본 테이블 member에 직접 접근해서 조회하지 않고 쉽게 필터링 기능 
select mem_name, addr from v_member
where  addr  IN('서울','경기');
/*
	=========================================================
	🎯 목적 : 복잡한 SQL문을 단순하게 만들기 위한 "뷰(View)" 사용
	---------------------------------------------------------
	회원(member) 테이블과 구매(buy) 테이블을 조인하여,
	회원이 구매한 물건 목록과 회원의 연락처까지 함께 출력하는 예제입니다.
	=========================================================
*/
-- 1. 두 테이블 조인해서 물건 구매 정보를 조회 결과로 출력
-- > buy 테이블 과 member 테이블의 mem_id열값을 기준으로 inner join
-- > 회원 ID, 회원 이름, 구매한 상품 이름, 주소 , 전화번호를 함께 조회 결과로 출력
select B.mem_id, M.mem_name, B.prod_name, M.addr,
       CONCAT(M.phone1, M.phone2) '연락처'   -- > 전화번호 앞자리 + 뒷자리 합쳐서 보여주기 
from buy B inner join member M
on B.mem_id =  M.mem_id;
-- ✅ 위 SELECT 문은 길고 복잡하며 자주 사용된다면 매번 쓰기 불편함
-- ▶ 따라서 이 복잡한 SELECT 문을 "뷰(View)"로 만들어두면 간단히 사용 가능함

/*
	=========================================================
	🛠️ 뷰(View) 생성 : 복잡한 SELECT 문을 하나의 이름(v_memberbuy)으로 저장
	---------------------------------------------------------
	뷰는 마치 "가상 테이블"처럼 행동하며,
	복잡한 SQL을 숨기고 간단한 이름만으로 데이터 조회 가능하게 만들어 줌
	=========================================================
*/
CREATE VIEW v_memberbuy
AS
	select B.mem_id, M.mem_name, B.prod_name, M.addr,
           CONCAT(M.phone1, M.phone2) '연락처'   -- > 전화번호 앞자리 + 뒷자리 합쳐서 보여주기 
	from buy B inner join member M
	on B.mem_id =  M.mem_id;
    
-- ✅ 이제부터는 복잡한 SELECT 문 대신 v_memberbuy 뷰를 사용하여 데이터 조회 가능
-- ▶ 뷰는 진짜 테이블은 아니지만 SELECT 문의 결과를 테이블처럼 보여주는 "가상 테이블"

-- 2. 뷰 (v_memberbuy)명을 이용해서 위 조인한 select전체의 조회 결과 얻기 
SELECT * FROM v_memberbuy;

-- 3 뷰에서 조건을 걸어도 조회도 가능 (ex. 블랙핑크가 구매한 상품 목록 조회 )
SELECT * FROM v_memberbuy
WHERE  mem_name = '블랙핑크';

/*
	====================================================================
	🎯 주제 : 뷰(View)의 생성과 열 이름에 별칭(Alias)을 줄 때의 주의점
	====================================================================

	🧠 뷰(View)란?
	  - 자주 사용하는 SELECT문을 마치 테이블처럼 저장해놓고 사용하는 가상의 테이블
	  - 복잡한 SQL문을 단순하게 만들기 위해 사용함

	📝 이번 예제에서 배울 것
	  ✅ 1. 뷰 생성 시 열 이름에 별칭(Alias) 주는 방법
	  ✅ 2. 열 이름에 띄어쓰기 포함 시 주의할 점
	  ✅ 3. 뷰를 SELECT 할 때 백틱(`)을 사용하는 이유
*/
-- 뷰를 만들기 전, 사용할 데이터베이스 선택 
USE market_db;

-- 뷰 생성  : buy 테이블 과 member 테이블을 조인해서 새로운 뷰(v_viewtest1)를 생성
--  > select 절에서 각 열에 별칭을 붙이고 있음
--  > 별칭(Alias)은  작은 따옴표 ' '  큰 따옴표 " " , 또는 AS 키워드를 사용해서 지정 가능 

CREATE VIEW v_viewtest1
AS
	SELECT 
		B.mem_id       'Member ID',      -- > buy 테이블의 회원 ID에 'Member ID'별칭 부여
        M.mem_name  AS 'Member Name',    -- > member 테이블의 이름에 'Member Name'별칭 부여 
        B.prod_name    "Product Name",   -- > 구매한 상품명에도 "Product Name"이라는 별칭 부여 
        CONCAT(M.phone1, M.phone2)  AS "Office Phone"  -- > 연락처를 결합해서 "Office Phone" 별칭 부여         
    FROM buy B INNER JOIN member M
    ON B.mem_id = M.mem_id;
		
-- -> v_viewtest1 뷰에서 중복 없이(Member ID, Member Name)열 값만 조회하는 쿼리(SQL문)
SELECT distinct `Member ID`, `Member Name`
FROM v_viewtest1;
/*
	조회 결과 
	---------------------
	Member ID   Member Name
	---------------------
	APN			에이핑크
	BLK			블랙핑크
	GRL			소녀시대
	MMU			마마무
*/

-- ✅ 기존에 만들어둔 뷰(v_viewtest1)의 내용을 수정합니다.
--     → ALTER VIEW : 기존 뷰의 SELECT 구문을 새롭게 바꿀 때 사용합니다.
--     → 열 이름에 한글을 사용할 수 있으며, 
--       한글 열 이름은 나중에 SELECT할 때 반드시 `백틱`으로 감싸줘야 합니다.

ALTER VIEW v_viewtest1
AS
	SELECT 
	B.mem_id    AS  '회원 아이디',      -- > buy 테이블의 회원 ID에 'Member ID'별칭 부여
	M.mem_name  AS '회원 이름',    -- > member 테이블의 이름에 'Member Name'별칭 부여 
	B.prod_name AS   "제품 이름",   -- > 구매한 상품명에도 "Product Name"이라는 별칭 부여 
	CONCAT(M.phone1, M.phone2)  AS "연락처"  -- > 연락처를 결합해서 "Office Phone" 별칭 부여         
    FROM buy B INNER JOIN member M
    ON B.mem_id = M.mem_id;

-- ✅ 뷰(v_viewtest1)에서 중복 없이 회원 정보만 조회
--     → `백틱`으로 한글 열 이름을 감싸줘야 정확히 조회됩니다.
--     → DISTINCT는 중복된 행은 한 번만 표시하겠다는 뜻입니다.
SELECT DISTINCT `회원 아이디`, `회원 이름`
FROM v_viewtest1;   -- 뷰를 마치 테이블 처럼 사용하여 간단하게 조회 가능 
/*
	조회결과
			회원아이디 회원이름
			APN		에이핑크
			BLK		블랙핑크
			GRL		소녀시대
			MMU		마마무
*/


/*
	📌 뷰 삭제
	- 더 이상 사용하지 않는 뷰(가상 테이블)는 삭제할 수 있습니다.
	- 형식: DROP VIEW 뷰이름;
*/
DROP VIEW v_viewtest1;

/*
	🧠 주제 : 뷰(View)의 정보 확인 방법

	📌 목적
	- 뷰는 가상 테이블이기 때문에, 실제로 어떤 열(컬럼)을 가지고 있는지 확인하고 싶을 때가 있습니다.
	- 이때 사용하는 명령어가 DESCRIBE 또는 DESC 입니다.

	📌 뷰란?
	- SELECT 문을 미리 저장해 둔 가상의 테이블입니다.
	- 복잡한 SQL을 간단하게 재사용할 수 있도록 도와줍니다.
*/
-- 💾 1단계: 사용할 데이터베이스 선택
USE market_db;  

-- ✅ 2단계: 뷰 생성 또는 재생성
-- 뷰의 이름: v_viewtest2
-- 이 뷰는 member 테이블에서 mem_id, mem_name, addr 열만 선택하여 구성합니다.
-- CREATE OR REPLACE VIEW 구문은:
--   - 뷰가 존재하지 않으면 새로 생성
--   - 뷰가 이미 있으면 내용을 덮어쓰기(재정의)합니다.
CREATE OR REPLACE VIEW v_viewtest2
AS
    SELECT mem_id, mem_name, addr FROM member;

-- 🔍 3단계: 뷰의 구조 확인
-- 이 명령은 v_viewtest2 뷰가 어떤 열(컬럼)을 가지고 있는지 알려줍니다.
-- 실제 테이블처럼 열 이름, 데이터 타입, Null 허용 여부 등을 보여줍니다.
DESCRIBE v_viewtest2;

-- 🔍 4단계: 원본 테이블의 구조도 비교해보기
-- 원본 테이블인 member 테이블에는 더 많은 열이 있을 수 있습니다.
-- 예를 들어 phone1, phone2, height 등의 열이 있을 수 있음.
-- 이 명령은 member 테이블의 전체 열 정보를 보여줍니다.
DESCRIBE member;

/*
	뷰가 어떻게 만들어졌는지 정확한 SQL문을 확인할 수 있는 명령 문법

		SHOW CREATE VIEW 뷰_이름;
*/
SHOW CREATE VIEW v_viewtest2;

/* 
	🟡 [뷰를 통한 테이블 데이터 수정과 삭제 실습 예제]

	  ▶ 실제 테이블(member)의 특정 컬럼(mem_id, mem_name, addr)만 접근할 수 있도록
		 만들어진 뷰(v_member)를 이용해 데이터를 수정(Update), 삽입(Insert), 삭제(Delete) 해봅니다.
*/
-- ✅ 1. 뷰(v_member)를 통해 기존 회원(BLK)의 주소를 '부산'으로 수정하는 예제
UPDATE v_member 
SET addr = '부산'       -- 주소(addr) 값을 '부산'으로 변경
WHERE mem_id = 'BLK';   -- 회원 아이디가 'BLK'인 경우만 대상

-- ✅ 2. 뷰(v_member)를 통해 새로운 회원을 추가하는 예제
INSERT INTO v_member(mem_id, mem_name, addr) 
VALUES('BTS','방탄소년단','경기');  
-- ▶ 주의: v_member 뷰는 member 테이블을 기반으로 하며,
--         이 뷰를 통해 member 테이블에 새 행이 삽입됩니다.
/*13:09:16	INSERT INTO v_member(mem_id, mem_name, addr) VALUES('BTS','방탄소년단','경기')	
 Error Code: 1423. Field of view 'market_db.v_member' underlying table doesn't have a default value	0.000 sec
 
 오류 원인 : v_member(뷰)가 참조하는 member(테이블)의 열 중에서 mem_number 열은 NOT NULL로 설정되어서 반드시 입력해줘야 합니다.
		   하지만  현재의 v_member(뷰)에서는 member_number 열을 참조하고 있지 않으므로 값을 입력할 방법이 없습니다.
           
 해결방법 설명 : 만약 v_member 뷰를 통해서 member 테이블에 값을 입력하고 싶다면 v_member 뷰에 mem_number 열을 포함하도록 뷰를 재정의 하거나,
              아니면 member테이블에서 mem_number 열의 속성을 Null로 바꾸거나, 기본값(Default)을 지정해야 합니다.
          
🛠 해결 방법
			방법 ①: 뷰를 다시 만들 때 누락된 열들을 포함시키기
            
				-- 🔧 mem_number 컬럼도 포함해서 뷰를 다시 정의!
				CREATE OR REPLACE VIEW v_member
				AS
				SELECT mem_id, mem_name, addr, mem_number, phone1, phone2
				FROM member;
	
				➡️ 이렇게 하면 INSERT 구문에서 mem_number 등의 값을 직접 넣을 수 있음
                
                
           방법 ②: 테이블 구조 변경 → NOT NULL 제거 또는 기본값 설정
           
				-- 🔧 mem_number에 기본값 설정 또는 NULL 허용
				ALTER TABLE member 
				MODIFY mem_number INT DEFAULT 0;

				-- 또는
				ALTER TABLE member 
				MODIFY mem_number INT NULL;
                
                ➡️ 이렇게 하면 INSERT 시 생략해도 기본값이 들어가거나 NULL이 허용됨
*/



-- 지정한 범위로 뷰를 생성해 보겠습니다.
-- 평균 키가 167 이상인 뷰를 생성해 봅시다.

CREATE VIEW v_height167
AS
    SELECT * FROM member WHERE height >= 167 ;
    
-- 평균키가 167 이상만 조회되었습니다.
SELECT * FROM v_height167 ;

-- v_height167 뷰에서 키가 167 미만인 데이터를 삭제해봅시다.
DELETE FROM v_height167 WHERE height < 167;
-- 설명: 당연히 v_height167 뷰에는 167 미만인 데이터가 없습니다. 그러므로 삭제될 데이터도 없는 것입니다.



/*
	주제 : 아래는 **뷰(View)에서 INSERT가 되는 원리와 `WITH CHECK OPTION`의 필요성**을 정리한 예제.
*/

## ✅ \[STEP 1] 기본 뷰 생성 (조건: 키가 167 이상인 회원만 보이도록)
-- ✅ member 테이블에서 키(height)가 167 이상인 회원만 보여주는 뷰(view) 생성
CREATE OR REPLACE VIEW v_height167
AS
    SELECT * 
    FROM member 
    WHERE height >= 167;

## ✅ \[STEP 2] 뷰를 통해 새로운 회원 1명 추가하기
-- ✅ v_height167 뷰를 통해 새 회원을 추가하려고 시도! 추가됨! 이게 문제가 됨!
INSERT INTO v_height167 
VALUES('TRA','티아라', 6, '서울', NULL, NULL, 159, '2005-01-01');
/*
	### 🔍 왜 키가 159인데도 삽입이 되었을까? 이게 문제가 됨!

	💡 뷰(v_height167)는 member 테이블을 기준으로 "보여주는 창문 역할"만 합니다.
	👉 즉, INSERT가 일어나면 실제로는 member 테이블에 직접 삽입되는 것입니다.

	🟥 이 뷰는 키(height) ≥ 167 조건을 갖고 있지만,
	👉 그 조건을 강제할 방법은 아직 없음!

	➡ 그래서 조건을 만족하지 않는 height = 159도 삽입은 되지만,
	   v_height167 뷰에서는 **보이지 않게 됩니다.**
*/

## ✅ \[STEP 3] 현재 뷰를 통해 다시 조회해보기
-- ✅ height가 167 이상인 데이터만 보이므로, 방금 추가한 TRA(159)는 조회되지 않음
SELECT * FROM v_height167;


## ✅ \[STEP 4] 조건을 반드시 지키도록 강제하기 (WITH CHECK OPTION)
-- ✅ 뷰를 수정하면서 "조건을 반드시 만족해야만 INSERT/UPDATE 허용"하도록 설정!
ALTER VIEW v_height167
AS
    SELECT * 
    FROM member 
    WHERE height >= 167
    WITH CHECK OPTION;
/*
	### 🔍 `WITH CHECK OPTION`이란?
	✔ 이 옵션을 뷰에 붙이면,
	   👉 뷰에 정의된 조건(height >= 167)을 만족하지 않으면
		  ➤ INSERT 또는 UPDATE를 **거부**합니다!

	✔ 즉, 뷰를 통해 입력/수정된 데이터는
	   항상 이 뷰에 **조회될 수 있는 데이터**여야만 한다는 원칙을 만듭니다.
*/


## ✅ \[STEP 5] 조건에 맞지 않는 INSERT를 다시 시도해보기
-- ❌ height가 140으로 뷰의 조건을 만족하지 않음
INSERT INTO v_height167 
VALUES('TOB','텔레토비', 4, '영국', NULL, NULL, 140, '1995-01-01');

/*
	### 🔥 결과: 오류 발생
	[오류 코드]
	Error Code: 1369. CHECK OPTION failed 'market_db.v_height167'


	### 🧠 이유:
	뷰의 조건(height >= 167)을 만족하지 않으므로
	👉 INSERT 자체가 아예 실패함!


	## ✅ 요약 정리표 (비전공자용)
	| 단계                    | 설명                                    
	| ---------------------- | -------------------------------------- 
	| `CREATE VIEW`          | 특정 조건에 맞는 데이터만 보이게 하는 가상 테이블 생성        
	| 조건에 안 맞는 INSERT      | 기본 상태에서는 그냥 member 테이블에 삽입됨 (뷰에는 안 보임) 
	| `WITH CHECK OPTION` 추가 | 뷰의 조건을 **반드시 지켜야만** INSERT/UPDATE 허용   
	| 조건 위반 시 INSERT       | ❌ 오류 발생, member 테이블에도 추가 안 됨          
*/




-- 여기서 잠깐 : 단순 뷰와 복합뷰
-- 하나의 테이블로 만든 뷰를 단순 뷰라고 하고,
-- 두 개 이상의 테이블로 만든 뷰를 복합 뷰라고 합니다.
-- 복합 뷰는 주로 두 테이블을 조인한 조회 결과를 뷰로 만들 때 사용합니다.
-- 복합 뷰 생성 예는 아래와 같습니다.
CREATE VIEW v_complex
AS
    SELECT B.mem_id, M.mem_name, B.prod_name, M.addr
        FROM buy B
            INNER JOIN member M
            ON B.mem_id = M.mem_id;
-- 참고.  복합 뷰는 읽기(조회) 전용입니다.  보합 뷰를 통해 테이블에 데이터를 입력/수정/삭제할 수 없습니다.            

-- 위 v_complex 뷰가 참조하는 회원 테이블과 구매 테이블을 모두 삭제하겠습니다.
DROP TABLE IF EXISTS buy, member;
-- 결과 : 현재 여러 개의 뷰가 두 테이블과 관련이 있는데고 테이블이 삭제되었습니다.

-- 두 테이블 중 아무거나 연돤되는 뷰를 다시 조회해봅시다. 
SELECT * FROM v_height167;
-- Error Code: 1356. View 'market_db.v_height167' references invalid table(s) or column(s) or function(s) or definer/invoker of view lack rights to use them
-- 결과 : 당연히 참조하는 테이블을 삭제 해서 없기 때문에 조회할 수 없다는 메세지가 나옵니다.
--       바람직하지는 않지만 관련 뷰가 있더라도 테이블은 쉽게 삭제됩니다.

-- v_height167 뷰가 조회되지 않으면 CHECK TABLE 문으로 뷰의 상태를 확인해 볼 수 있습니다.
-- 뷰가 참조하는 테이블이 삭제되어 없어서 오류가 발생하는 것을 확인할 수 있습니다.
CHECK TABLE v_height167;
/*
	실행 결과 

		Table				 Op   Msg_Type		Msg_text
	market_db.v_height167	check	Error	View 'market_db.v_height167' references invalid table(s) or column(s) or function(s) or definer/invoker of view lack rights to use them
	market_db.v_height167	check	error	Corrupt
*/ 