

/*
 비트 시프트 연산을 사용하여 암호화하는 방법이외 다른 암호화 복호화 방법은  -> 간단한 XOR 암호화 방식을 구현하는 것입니다. 
 암호화는 XOR 연산을 사용하며, 비밀키로는 숫자를 사용합니다.
  암호화하려는 문자열의 각 문자를 비밀키로 XOR 연산을 수행하여 암호화한 문자열을 만듭니다. 
 복호화는 암호화된 문자열의 각 문자를 다시 비밀키로 XOR 연산을 수행하여 원래의 문자열을 복원합니다.
  아래는 비트 시프트 연산을 사용하여 XOR 암호화 방식을 구현한 예시입니다.
 */

public class BitwiseEncryptionExample {
    public static void main(String[] args) {
    	
        String password = "myPassword123"; //암호화 시킬 비밀번호
        int key = 42; // 비밀키

        // 비밀번호를 암호화
        String encryptedPassword = "";
        
        for (int i = 0; i < password.length(); i++) {
            // 각 문자의아스키코드 값의 2진수값을 비밀키int key = 42; 와 XOR 연산하여 암호화
        	//예
        	/*
        	(char)('m' ^ 42)는 Java에서 비트 XOR 연산을 수행하고 그 결과를 문자로 변환하는 식입니다.
 
            'm'이라는 문자를  10진수 아스키코드값의  2진수로 표현으로 변환하면 "01101101"입니다.

        	이제 42와 비트 XOR 연산을 수행합니다:

        	  01101101   ('m')
        	^ 00101010   (42)
        	-----------
        	  01000111   (결과)
        	
        	최종적으로, 이진 표현 "01000111"은 10진수로 변환하면 71이 됩니다. 또한, 
        	
        	이 값은 ASCII 코드의 범위 내에 있으므로, 문자로 변환하면 'G'가 됩니다.

        	따라서, 식 (char)('m' ^ 42)의 계산 결과는 'G'입니다.
        	*/
            char encryptedChar = (char) (password.charAt(i) ^ key);
            encryptedPassword += encryptedChar;
        }
        
        System.out.println("암호화된 비밀번호: " + encryptedPassword);
     
        //------------------------------------------------------------------------------
        
        // 암호화된 비밀번호를 복호화
        String decryptedPassword = "";
        
        for (int i = 0; i < encryptedPassword.length(); i++) {
            // 암호화된 문자열의 각 문자를 다시 키와 XOR 연산하여 복호화
            char decryptedChar = (char) (encryptedPassword.charAt(i) ^ key);
            decryptedPassword += decryptedChar;
        }
        System.out.println("복호화된 비밀번호: " + decryptedPassword);
    }

  
}
