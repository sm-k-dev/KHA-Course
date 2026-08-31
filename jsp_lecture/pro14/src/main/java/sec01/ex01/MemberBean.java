package sec01.ex01;

import java.sql.Date;

public class MemberBean {
	// 회원 테이블의 컬럼 이름과 동일하게 변수이름과 자료형을 선언
	private	String	id;
	private	String	pwd;
	private	String	name;
	private	String	email;
	private	Date	joinDate;
	
	// 아무일도 하지 않는 기본 생성사
	public MemberBean() {}
	
	// 인자가 4개인 생성자
	public MemberBean(String id, String pwd, String name, String email) {
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
	}
	
	// getter / setter 메소드 들
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
}
