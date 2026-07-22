package com.myapp.models;

public class User {
	
	// 변수 - 사람이름 
	private String name;
	
	// 생성자 - 사람이름 초기화
	public User (String name) {
		this.name = name;
	}
	
	// 메소드 - 사람 정보 출력
	public void printInfo() {
		System.out.println("내 앱 사용자: " + name);
	}

}
