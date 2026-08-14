import java.util.Comparator;

public class Ex6 {

	public static void main(String[] args) {
/*	
		Comparator.reversOrder()는 Comparator<T>타입의 객체를 반환하며
		이객체는 두 값을 비교할때 사용하는 compare(값1,값2)메소드를 제공합니다.
		
		compare(A,B) 메소드에서
		   A.comare(B)의 결과를 뒤집어 반환합니다.
		   즉! 만약 A.compareTo(B)가 양수라면, 
		   reverseOrder()는 음수를 반환하여 내림차순으로 정렬합니다
		   
		   A.compare(B)가 음수라면,
		   reverseOrder()는 양수를 반환하여 내림차순으로 정렬합니다
		   
		   A.compare(B)가 0이면
		   동일하므로 0을 반환합니다.
		   
*/	
		Comparator<Integer>  recomparator = Comparator.reverseOrder();
		
		//10이 20보다 더 크냐? 물음에 20이 더 크므로 원래는 음수 -1 반환하지만
		//-> Comparator.reverseOrder(); 호출하여 정렬 거꾸로 판단하는 자식객체를 반환받아서 사용하기 떄문에
	    //   compare(10,20); 호출 결과는 거꾸로 판단 해 양수 1을 반환합니다.
		int result1 = recomparator.compare(10, 20);  //10이 20보다 더 작으냐? 작으면 양수 1반환
		System.out.println(result1); //양수 1 출력
		
		int result2	=  recomparator.compare(20, 10); //20이 10보다 더 작으냐? 작으면 양수 1반환 크면? 음수 -1 반환
		System.out.println(result2); //음수 -1 출력
			
		int reuslt3 = recomparator.compare(10, 10); //10과 10이 같으냐? -> 두값이 같으면 0 반환
		System.out.println(reuslt3); //두값이 같으므로 0 출력 
		
	}

}











