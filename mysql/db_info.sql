/*
	MySQL DBMS 에서
		1. 사용자 계정 만들기
        2. 데이터베이스 만들기
        3. 만든 사용자 계정에 특정 데이터베이스를 사용할 권한 부여
        4. 권한 정보 새로 고침
        5. 권한 정보 확인
        6. 사용자 계정 삭제
        7. 데이터베이스 백업
        8. 데이터베이스 복구
*/

# 1. 사용자 계정 만들기 ##################################################
/*
	create user '만들사용자계정명'@'localhost' identified by '비밀번호';
    
    create			: 만든다
    user			: 사용자 계정
    '만들사용자계정명'	: 만들 사용자 아이디
    @				: "어디에서 접속하는 사용자"인지 구분
    'localhost'		: MySQL DBMS소프트웨어가 설치된 서버(컴퓨터)에서만 접속 가능
    identified		: 비밀번호를 지정한다.
    '비밀번호'		: 계정 비밀번호
*/
create user 'springuser'@'localhost' identified by '1234';

# 1.1. 외부 접속 가능한 사용자 생성
create user 'springuser2'@'%' identified by '1234';
-- '%' 모든 아이피를 사용하는 컴퓨터에서 springuser2라는 계정으로 MySQL 접속이 가능하다

# 2. 데이터베이스 생성 ##################################################
create database springdb;

# 3. 만든 사용자 계정에 특정 데이터베이스를 사용할 권한 부여 ##################################################
/*
	localhost 에서 접속하는 springuser 사용자에게
    springdb 데이터베이스 안의 모든 테이블을 다룰 수 있는 모든 권한 (SELECT, INSERT, DELETE, UPDATE ...)을 준다.
    
    grant all privileges on DB명.* to '계정명'@'localhost';
    
    grant 			: 권한을 준다
    all privileges	: 모든 권한
    on				: 어느 대상에 대해
    DB명.*			: DB명 안에 있는 모든 테이블의
    to				: 누구에게
*/
grant all privileges on springdb.* to 'springuser'@'localhost';

# 4. 권한 적용 ##################################################
flush privileges;
/*
	flush		: 새로고침한다. 다시 읽는다.
    privileges	: 권한 정보
*/

# 5. 권한 부여 확인 ##################################################
/*
	show grants for '계정명'@'localhost';
*/
show grants for 'springuser'@'localhost';

# 6. 사용자 계정 삭제 ##################################################
/*
	drop user '계정명'@'localhost';
*/
drop user 'springuser'@'localhost';

# 7. 데이터베이스 백업 ##################################################
/*
	워크벤치에선 안 됨.
    
	mysql.exe 		- mysql 실행프로그램
	mysqldump.exe	- mysql 백업하는 프로그램
    
    mysqldump -u 계정명 -p 백업할db명 > 실행결과를_백업할_파일명.sql
    
    mysqldump -u root -p market_db > market_db.sql
    root 계정으로 mysql에 접속해서
    market_db 데이터베이스 내용을
    market_db.sql 파일로 저장한다.
    
    혹은 워크벤치 Navigator 창에서 Administration 탭으로 이동한 뒤 Data Export 누른다.
    Export from Self-Contained File (= .sql 파일)로 백업하는 것을 권장
    완료하기 전에 Include Create Schema를 체크 해주는것을 권장한다.
*/

# 8. 데이터베이스 복구 ##################################################
/*	
	mysql -u root -p 복구할db명 < 백업해둔db파일명.sql
    
	워크벤치 Navigator 창에서 Administration 탭으로 이동한 뒤 Data Import/Restore 누른다.
    Export from Self-Contained File 로 백업했으니 Import from Self-Contained File 를 선택한다.
    그리고 Default Target Schema에 복구할 데이터베이스 틀을 선택해야 하는데 없으니 옆에 New를 눌러서 데이터베이스를 하나 생성 해 준다. 
    Start Import 누르면 끝
*/