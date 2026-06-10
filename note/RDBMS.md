DB (Database, 데이터베이스) - 대량 데이터의 집합

DBMS (Database Management System) - DB 관리 시스템 또는 소프트웨어
    MySQL (Oracle), MaraiDB (MariaDB), PostgreSQL (PostgreSQL), Oracle (Oracle)
    SQL Server (Microsoft), DB2 (IBM), Access (Microsoft), SQLite (SQLite)
    Snowflake - SQL / 클라우드 데이터 웨어하우스
    Databricks - 멀티모델 / 데이터 레이크하우스

SQL (Structured Query Language) / 표준 SQL (국제표준화 기구에서 SQL에 대한 표준을 정해서 발표한 것)
    구조화된 질의 언어
    DBMS에 데이터를 구축, 관리하고 활용하기 위해 사용되는 언어
    SQLD (자격증)

NoSQL (Not Only SQL) 비관계형 데이터베이스, 표 형태가 아닌 다양한 방식으로 데이터를 저장
    문서형 (Document): 데이터를 JSON/BSON형태로 저장 (MongoDB, CouchDB)
    키-값형 (Key-Value): 가장 단순한 형태로 키와 값 쌍으로 저장 (Redis, Amazon, DynamoDB)
    와이드 컬럼형 (Wide Column): 행마다 다른 컬럼을 가질 수 있어 대용량 처리에 강하다 (Cassandra, HBase)
    그래프형 (Graph): 노드와 간선으로 데이터 간의 관계를 시각화하여 저장 (Neo4j)
    Elasticsearch - NoSQL (검색 엔진)

DBMS의 유형
    계층형 (Hierarchical)
    망형 (Network)
    관계형 (Relational) - 테이블이라는 최소 단위로 구성, 테이블은 열(column)과 행(row)으로 이루어짐
    객체지향형 (Object-Oriented)
    객체관계형 (Object-Relational)

MySQL의 포트번호는 3306

mysql DBMS 프로그램 - 우리 컴퓨터가 데이터베이스를 운용하는 서버
    운용 - 특정 대상이나 자산을 목적에 맞게 활용
    운영 - 조직, 시스템, 사업체 등 전체적인 체게를 관리하고 경영하는 것

워크벤치 클라이언트 전용 프로그램 - mysql 프로그램에 접속하고 명령할 GUI 환경 프로그램

connector J - java 언어로 개발한 페이지와 데이터베이스 서버를 연결할 중간 프로그램

C:\Program Files\MySQL\MySQL Server 8.0\bin 아래에 있는 mysql 파일은 sql 명령어 실행 파일이다.
    따라서 이 명령어를 쉽게 사용하기 위해 환경변수에 저장해둔다.
    환경 변수 의 시스템 변수 세션에서 Path를 찾아서 추가한다

    bin 폴더는 명령어 실행파일이 모여있는 곳이다.

Java는 시스템 환경 변수에 JAVA_HOME 변수를 만들고 bin 폴더가 위치한 디렉토리까지의 경로를 저장한다.
그리고 Path에 %JAVA_HOME%\bin 을 등록한다.
    JAVA의 설치 위치를 알아야 하는 외부 개발 도구들이 JAVA_HOME을 참조로 하기 때문에 이렇게 사용한다.
    1. 외부 소프트웨어의 표준 약속
    2. 유지보수의 편리함 (중복 감소)
            java는 업데이트가 잦아 설치 경로 (버전 폴더명)가 자주 바뀐다
            후에 JAVA_HOME만 바꾸면 된다.
    3. 라이브러리(기능) 참조
            path 변수는 오직 실행 파일만 찾는 용도
            다른 프로그램들이 java의 내부 라이브러리 폴더에 접근하려면 최상위 경로가 반드시 필요

-----------------------------------------------------------------------------------------------

Database Modeling
    테이블의 구조를 미리 설계하는 개념, 건축 설계도를 그리는 과정과 비슷하다.
    우리가 살고 있는 세상에서 사용되는 사물이나 작업을 DBMS의 데이터베이스 개체로 옮기기 위한 과정
    이 단계를 거치면 가장 중요한 데이터베이스 개체인 테이블 구조가 결정된다.
        소프트웨어 개발 생명주기 (SDLC)
            Waterfall model (폭포수 모델)
            Agile (애자일 모델)
            Prototyping (프로토타입 모델)
            Spiral (나선형 모델)
            V-Shaped (V 모델)

프로젝트 진행 단계
    Project - 현실 세계에서 일어나는 업무를 컴퓨터 시스템으로 옮겨놓는 과정
                대규모 software를 작성하기 위한 전체 과정

Data: 하나하나의 단편적인 정보
Table: 데이터를 입력하기 위해 표 형태로 표현한 것
Database: 테이블이 저장되는 저장소
DBMS: 데이터베이스 관리 시스템 또는 소프트웨어
column: 열, 테이블의 세로
열 이름: 각 열을 구분하기 위한 이름
데이터 형식: 열에 저장될 데이터의 형식
row: 행, 실질적인 진짜 데이터
Primary Key: PK, 기본 키, 각 행을 구분하는 유일한 열
SQL: Structured Query Language, 구조화된 질의 언어. DBMS가 알아듣는 언어
스키마 - 개발에선 데이터베이스 구조. 도식, 형태, 틀 또는 구조 
            외부 스키마, 개념 스키마, 내부 스키마로 나뉜다
예약어 - 정해진 단어
localhost - 내 컴퓨터의 IP 주소를 기억하지 못 할때 쓰는 단어, 127.0.0.1 과 같다

-----------------------------------------------------------------------------------------------
데이터베이스 시작부터 끝까지
    데이터베이스 구축 절차
        1 데이터베이스 만들기
        2 테이블 만들기
        3 데이터 입력/수정/삭제
        4 데이터 조회/활용
    
    DBMS 설치하기