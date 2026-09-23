-- BoardApp MVC 게시판 DB 스키마
CREATE DATABASE IF NOT EXISTS jspbeginner
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE jspbeginner;

CREATE TABLE IF NOT EXISTS tblBoard (
  num      INT AUTO_INCREMENT PRIMARY KEY,   -- 글번호
  name     VARCHAR(50)  NOT NULL,            -- 작성자
  email    VARCHAR(100),                     -- 이메일
  homepage VARCHAR(100),                     -- 홈페이지
  subject  VARCHAR(200) NOT NULL,            -- 제목
  content  TEXT,                             -- 내용
  pass     VARCHAR(50)  NOT NULL,            -- 비밀번호
  count    INT DEFAULT 0,                    -- 조회수
  ip       VARCHAR(50),                      -- 작성 IP
  regdate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성일시
  pos      INT DEFAULT 0,                    -- 정렬순서(답변)
  depth    INT DEFAULT 0                     -- 들여쓰기 깊이
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DB 계정 (필요 시)
-- CREATE USER 'jspid'@'localhost' IDENTIFIED BY 'jsppass';
-- GRANT ALL PRIVILEGES ON jspbeginner.* TO 'jspid'@'localhost';
-- FLUSH PRIVILEGES;
