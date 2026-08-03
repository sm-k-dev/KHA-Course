package StringBuilderTest;

public class StringBuilderExample2 {

	public static void main(String[] args) {
		
		// 사이트 이용자가 입력창에서 입력했다고 가정한 데이터
		String city = "서울특별시";
		String district = "강남구";
		String street = "테헤란로";
		String buldingNo = "123";
		String detail = "501호 (ABC빌딩)";
		
        /*
         * 📌 StringBuilder 사용 이유
         * - 문자열에 추가(append) 작업이 여러 번 일어남
         * - String을 사용하면 매번 새로운 객체가 생성되어 비효율적 (성능 저하)
         * - StringBuilder는 내부에서 버퍼메모리를 사용해 문자열 수정에 최적화 → 속도 ↑ & 메모리 낭비 ↓
         */
		StringBuilder address = new StringBuilder();
		
		//순서대로 문자열을 StringBuilder 객체 메모리 하나에!!! 추가해서 저장 할수 있음 (메서드 체이닝 기법 이용)
					  address.append(city)       //"서울특별시"
					  		 .append(" ")        //"서울특별시 "
					  		 .append(district)   //"서울특별시 강남구"
					  		 .append(" ")        //"서울특별시 강남구 "
					  		 .append(street)     //"서울특별시 강남구 테헤란로"
					  		 .append(" ")        //"서울특별시 강남구 테헤란로 "
					  		 .append(buldingNo)  //"서울특별시 강남구 테헤란로 123"
					  		 .append(" ")        //"서울특별시 강남구 테헤란로 123 "
					  		 .append(detail);    //"서울특별시 강남구 테헤란로 123 501호 (ABC빌딩)"
				  			 //new StringBuilder("서울특별시 강남구 테헤란로 123 501호 (ABC빌딩)"); 
					  
        /*
         * 📌 toString() 으로 최종 문자열 변환
         * - StringBuilder는 "문자열을 조립하는 작업 전용"
         * - 완성 후 최종 결과가 필요할 때만 toString() 사용
         */					  
		String fullAddress	= address.toString();
							//새로 생성된? new String("서울특별시 강남구 테헤란로 123 501호 (ABC빌딩)"); 객체 반환 
		
		//new String("서울특별시 강남구 테헤란로 123 501호 (ABC빌딩)"); 객체 메모리 내부에 저장된 문자열 얻어(반환 받아) 출력
		System.out.println("최종 주소 : " + fullAddress.toString());
					//      최종 주소 : 서울특별시 강남구 테헤란로 123 501호 (ABC빌딩)
	}

}




