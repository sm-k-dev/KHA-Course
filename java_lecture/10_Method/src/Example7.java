/*
✅ 문제:
정수 배열과 특정 숫자를 전달받아, 
그 숫자가 배열 안에서 몇 번 등장하는지 세어 출력하는 countOccurrences 메소드를 완성하세요.

 - 메소드 이름: countOccurrences
 
 - 매개변수:
			① int[] 배열
			② int target 값 (찾을 숫자)

 - 출력 형식: "target의 등장 횟수: N회" 
 */

public class Example7 {
    
    // countOccurrences 메소드를 완성하세요!
    public static void countOccurrences( int[] numbers, int target ) {
    	
        int count = 0;  // target 숫자가 몇 번 나오는지 세는 변수
        
        for ( int number : numbers ) {
            if ( number == target ) {
                count++;  // target 값이 발견되면 count 1 증가
            }
        }
        
        System.out.println(target + "의 등장 횟수: " + count + "회");
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 7, 3, 2, 3, 9, 3};
        
        // countOccurrences 메소드를 호출하여 배열 arr 안에서 3이 몇 번 등장하는지 확인해보세요.
        countOccurrences( arr, 3 );
    }
}
