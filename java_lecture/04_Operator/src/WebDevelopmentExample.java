

//비트 논리 연산자  &    |    ^  를 이용하여 
//웹 개발 시 관리자 페이지와 일반 사용자 페이지 권한에 대한 응용 예제

public class WebDevelopmentExample {
	
	 // 각각 게스트, 일반 사용자, 관리자 권한을 나타내는 정수를 상수에 저장 
	 public static final int GUEST_PERMISSION = 1;   //게스트 권한
	 public static final int MEMBER_PERMISSION = 2;  //일반 사용자 권한
	 public static final int ADMIN_PERMISSION = 4;   //관리자 권한 

	 // 로그인 시 필요한 권한을 설정하는 상수 선언합니다.
	 //-> 일반 사용자 권한과  관리자 권한을  비트 OR 연산자 | 를 사용하여 로그인 권한을 설정합니다.
	 public static final int LOGIN_PERMISSION = MEMBER_PERMISSION  |  ADMIN_PERMISSION;
//	 													2          |         4
//	 															   6
	    /*
		2는 0010입니다. (2진수로 2를 나타내기 위해 4번째 자리에 1을 놓고 나머지 자리는 0으로 채웁니다.)
		4는 0100입니다. (2진수로 4를 나타내기 위해 3번째 자리에 1을 놓고 나머지 자리는 0으로 채웁니다.)
		OR 연산은 각 자리의 비트를 비교하여 하나라도 1이면 결과를 1로 설정합니다.
		
		--OR연산-
		2는 0010
		4는 0100
		6  0110
		
		0110은 2진수로 표현된 숫자입니다. 2진수를 10진수로 계산하는 방법은 간단합니다. 
		각 자리의 2진수의 값을 2의 거듭제곱으로 곱한 후에 모두 더하는 것입니다. 
		0110을 10진수로 계산하는 방법은 다음과 같습니다:
			
			0 * 2^3 + 1 * 2^2 + 1 * 2^1 + 0 * 2^0
			= 0 + 4 + 2 + 0
			= 6
	         따라서, 0110은 10진수로 계산하면 6이 됩니다.
	    */	
	 
	//관리자 페이지 접근 권한을 설정하는 상수 선언 후 4 저장
	public static final int ADMIN_PAGE_PERMISSION =  ADMIN_PERMISSION; //관리자 페이지 접근 권한 4
	

	public static void main(String[] args) {
		
		//로그인 필요 권한을 가진 사용자의 권한 6 설정 
		int userPermission = LOGIN_PERMISSION;
		
		
		//isAdmin 변수를 선언하여 관리자 권한 여부를 저장합니다.
		//-> userPermission변수 값과  ADMIN_PERMISSION상수 값을 비트 AND 연산자 &를 사용하여 
		//   관리자 권한을 가지고 있는지 확인
		boolean isAdmin = (userPermission  & ADMIN_PERMISSION) != 0;
//						  (       6        &         4       ) != 0;
        /*
        6 & 4의 비트 연산 과정을 2진수로 나열하여 설명해드리겠습니다. 
               먼저, 6와 4를 2진수로 나타내면 다음과 같습니다:
    	6는 0110입니다. (2진수로 6를 나타내기 위해 2번째 자리와 3번째 자리에 1을 놓고 나머지 자리는 0으로 채웁니다.)
    	4는 0100입니다. (2진수로 4를 나타내기 위해 3번째 자리에 1을 놓고 나머지 자리는 0으로 채웁니다.)
    	이제, AND 연산을 수행해 보겠습니다. 
    	    AND 연산은 각 자리의 비트를 비교하여 모두 1이면 결과를 1로 설정합니다. 그렇지 않으면 결과를 0으로 설정합니다.
    	 6    0110
     AND 4    0100
    	    ------
    	 4    0100
    	위 계산을 거쳐, 6 & 4의 결과는 0100이 됩니다. 이는 10진수로 나타내면 4입니다.
        */

		System.out.println("로그인 필요권한을 가지며 관리자 페이지 접근 권한을 가진 관리자 권한 여부값 : " + isAdmin);
		
		
		if(isAdmin) {
			System.out.println("관리자 페이지에 접근합니다."); //<- 관리자 동작 수행 
		}else {
			System.out.println("일반 사용자 페이지에 접근합니다."); //<- 일반 사용자 관련 동작 수행 
		}
		
	}

}









