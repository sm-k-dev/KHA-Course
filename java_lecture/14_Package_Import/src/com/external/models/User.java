package com.external.models;

public class User {

	//변수
	int id;
	
	//생성자
	public User(int id) {
		this.id = id;
	}
	
	//메소드 
	public void printInfo() {
		System.out.println("외부 시스템 사용자 ID : " + this.id);
	}
	
}