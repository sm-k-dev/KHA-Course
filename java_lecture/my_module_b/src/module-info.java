// 모듈 기술자 파일 => module-info.java

module my_module_b {
	exports pack3; // 외부 프로젝트(응용프로그램)에 공유
	exports pack4; // 외부 프로젝트(응용프로그램)에 패키지 공유
}