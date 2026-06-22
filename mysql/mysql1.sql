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