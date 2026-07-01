/*
	참고1.
	
	String 클래스  charAt(index) 메소드에 대한 설명
	
	예    
	String password = "myPassword123";  
	
	위와 같이 password변수 메모리를 String클래스 자료형으로 선언 후 문자열을 저장 해 놓았을때..
	
	문자열 "myPassword123" 을 저장한 String객체 메모리가 만들어집니다.
	
	이때  String클래스에 이미 만들어서 제공하는  메소드 중에서 charAt(index) 메소드는 하나의 기능을 가지고 있는데
	
	전체 문자열  "myPassword123"중에서   각 문자에는 인덱스 위치가 0부터 부여 되는데  
	
	charAt(index) 해당 인덱스에 위치한 문자를 반환하는 역할을 합니다.
	
	전체 문자열에서 특정 위치에 있는 문자를 가져올 수 있습니다.
	
	코드 예
	String password = "myPassword123"; 
	                   012
		   password.charAt(0);  ->  'm' 해당 문자 반환

*/

//주제 :   왼쪽  / 오른쪽 shift 연산자를 사용하여  비밀번호를 암호화하고  복호화하는 간단한 예제 입니다.

public class PasswordEncryptionExample {

	public static void main(String[] args) {
		
		char a = 'h';
		System.out.println(a); //h
		
		String password = "myPassword123"; //<- 암호화 할 비밀번호 
		
		int shift = 3; //shift 변수는 비트 시프트 연산에 사용될 값을 정합니다.
					   //여기에서는 3을 선택하여 왼쪽으로 3개의 비트를 시프트 할것입니다.
		
		String encryptedPassword = "";
		
		for(int i=0;   i < password.length();   i++) {
			
										//"myPassword123"
										// 0123456789     <- index
			
						//"myPassword123".charAt(0);   ->   'm'
			        	//"myPassword123".charAt(1);   ->   'y'
			        	//"myPassword123".charAt(2);   ->   'P'   ...... 이런식으로 전체 문자열 중에서 각 index 위치에 있는 해당 문자를 반환 받아 사용한다.
			System.out.println( password.charAt(i) );
			System.out.println();
			
            // 각 문자의ASCII 코드 값의 2진수 값을 왼쪽으로 시프트하여 암호화
        	/*
        	 예
        	"myPassword123".charAt(0)은 문자열 "myPassword123"의 첫 번째 문자인 'm'을 의미합니다.
        	
        	shift 연산은 비트를 지정된 수만큼 왼쪽으로 이동시키는 연산자입니다. 왼쪽으로 이동시키면서 비트는 지정된 수만큼 영향을 받아 값이 변경됩니다.
        	
        	여기서 char 'm'은 ASCII 코드로 109를 가지고 있습니다. 이를 이진수로 나타내면 01101101입니다.
        	
        	그러면 'm'의 ASCII 코드인 109을 왼쪽으로 3비트 shift 한 결과는 어떻게 될까요?
        	
        	3비트 shift하면 오른쪽에 추가되는 비트는 버려지고, 0으로 채워집니다. 
        	
        	따라서 01101101을 왼쪽으로 3비트 shift한 결과는 01101000가 됩니다.
        	
        	결과적으로 "myPassword123".charAt(0) << 3는 'm'의 ASCII 코드인 109를 왼쪽으로 3비트 shift한 것이므로, 
        	
        	이진수로 나타내면 01101000입니다.
        	
        	정수로 표현하면 104가 됩니다. 따라서 결과는 104입니다.
        	
        	(char)(104); 104 10진수를 문자로 강제 형변환 하면  'h'문자로 만들어 줍니다.
            */				
			char  encryptedChar  = (char)(password.charAt(i) << shift);
			encryptedPassword += encryptedChar;
							
		}
		System.out.println("암호화 된 비밀번호 : " + encryptedPassword);
        //암호화된 비밀번호: ?ψ??ΘΘθ??????  
        /*
			🎯 왜 ?ψ??ΘΘθ?????? 같은 이상한 문자가 나올까?
			
			여기 핵심은 단 하나!
			
				문자를 왼쪽 shift(<<) 하면 ASCII 범위를 넘어가는 값이 만들어지기 때문!
			
			
			✔ 예를 들어볼게요!
			
				'm'의 ASCII 값은 109
				이진수: 01101101
				
				여기서 shift 3 하면:
				
				01101101 << 3  →  01101101000
			
			
			이 값은 ASCII 1바이트(0~127) 범위를 벗어납니다.
			자바의 char는 2바이트라서 값은 담기지만…
			👉 콘솔에 출력할 때는 문자 테이블(Unicode)에서 해당 코드값에 해당하는 문자를 찍습니다.
				
			그래서 우리가 전혀 예상하지 못한
			?, ψ, Θ 같은 문자들이 튀어나오는 거예요!
       */		
     /*
        암호화된 비밀번호가 이상하게 출력되는 이유는 해당 코드에서 사용한 비트 시프트 연산이 잘못되었기 때문입니다. 
        비트 시프트 연산은 산술이나 논리적 연산을 수행할 때 사용하는 연산자인데, 비트를 왼쪽이나 오른쪽으로 이동시키는 연산입니다. 
        그러나 이 코드에서는 이 비트 시프트 연산을 이용하여 문자열을 암호화하고 복호화하고 있습니다. 
        이 코드에서 사용한 shift 값을 3으로 설정하면, 비트연산이 원하지 않는 방향으로 되어 원래의 문자열과 다른 형태로 암호화가 진행되는 것입니다.
         즉, 암호화된 문자열 출력이 이상하게 되는 것입니다. 
         비밀번호를 안전하게 암호화하기 위해서는 안전한 암호화 알고리즘을 적용하는 것이 필요합니다. 
         따라서 비트 시프트 연산을 사용해서 암호화하는 방법은 추천되지 않습니다.
        만약 그래도?  비트  연산자을 사용하여 암호화하려면 방법은, 
        간단한  비트 논리 연산자  XOR을 사용해  암호화 방식을 구현하는 것입니다     
    */
		
        //decryptPassword 변수를 선언하여 복호화 한 비밀번호를 저장 할것입니다. 
		String decryptedPassword = "";
		
		for(int i=0;  i<encryptedPassword.length();   i++) {
			
			char  decryptedChar  = (char)(encryptedPassword.charAt(i) >> shift);
			decryptedPassword += decryptedChar;
		}

		System.out.println("복호화된 비밀번호 : " + decryptedPassword);    	
	}

	


}






