package StringBuilderTest;
/* ============================================================================
 *  주제 : StringBuilder 클래스 - 메모리 변화를 그림으로 따라가기
 * ----------------------------------------------------------------------------
 *  [ 그림을 읽는 방법 ]
 *
 *      [ 참조변수 ]---▶ [ 객체 : 내용 ]
 *
 *      왼쪽의 대괄호는 참조변수다. 안에는 객체의 주소값이 들어 있다.
 *      화살표는 그 주소값이 가리키는 객체를 뜻한다.
 *      주소값 0x100 등은 설명을 위해 붙인 가짜 번호다.
 *      실제 주소값은 실행할 때마다 달라지며 우리가 직접 볼 수는 없다.
 *
 *  [ StringBuilder 의 핵심 성질 ]
 *
 *      String 은 불변이라, 내용을 바꾸면 새 객체가 만들어졌다.
 *      StringBuilder 는 가변이라, 객체 하나의 내용을 직접 고친다.
 *      그래서 아무리 여러 번 고쳐도 객체는 계속 하나다.
 *
 *      또 하나 중요한 점은, append, delete, insert 메소드가
 *      고친 뒤에 "자기 자신의 주소값"을 그대로 반환한다는 것이다.
 *      새 객체의 주소가 아니다.
 *
 *          public StringBuilder append(String str) {
 *              ... 내용을 고친다 ...
 *              return this;      <- 자기 자신을 반환한다
 *          }
 *
 *      이 성질 때문에, 반환값을 다른 변수에 담아도 두 변수는
 *      결국 같은 객체 하나를 가리키게 된다.
 * ========================================================================== */

public class StringBuilderExample {

	public static void main(String[] args) {

        /* ┌────────────────────────────────────────────────────────────┐
         * │ 실행 전 : 아직 아무것도 없다                                  │
         * └────────────────────────────────────────────────────────────┘
         *
         * 실행 후
         *      data ────▶ 0x100 [ StringBuilder : "ABC" ]
         *                             인덱스 :          0 1 2
         */
		StringBuilder  data = new StringBuilder("ABC");
        /* ┌────────────────────────────────────────────────────────────┐
         * │ 실행 전                                                     │
         * │      data ────▶ 0x100 [ "ABC" ]                            │
         * └────────────────────────────────────────────────────────────┘
         *
         * append("DEF") 가 하는 일
         *      1) 0x100 객체의 내용을 "ABC" 에서 "ABCDEF" 로 직접 고친다.
         *      2) 새 객체를 만들지 않는다.
         *      3) 자기 자신의 주소값 0x100 을 반환한다.
         *
         * 실행 후
         *      data  ────▶ 0x100 [ StringBuilder : "ABCDEF" ]
         *      data2 ────▶ 0x100        (같은 객체를 가리킨다)
         *                            인덱스 :          0 1 2 3 4 5
         */
		 StringBuilder data2 = data.append("DEF");
		 
		 //두 참조변수 data 와 data2 는 같은 객체를 사용하고 있는지 == 로 확인한다
		 System.out.println("data == data2 : " + (data == data2));  //true
		 
	        /* ┌────────────────────────────────────────────────────────────┐
	         * │ 실행 전                                                     │
	         * │      data  ─┐                                              │
	         * │             ├─▶ 0x100 [ "ABCDEF" ]                        │
	         * │      data2 ─┘                                              │
	         * └────────────────────────────────────────────────────────────┘
	         *
	         * toString() 이 하는 일
	         *      StringBuilder 안의 문자열을 꺼내, 그 내용을 담은
	         *      새로운 String 객체를 만들어 그 주소값을 반환한다.
	         *      StringBuilder 와 String 은 서로 다른 타입이기 때문이다.
	         *
	         * 실행 중 메모리
	         *      data, data2 ────▶ 0x100 [ StringBuilder : "ABCDEF" ]
	         *                                       │ 내용을 복사
	         *                                       ▼
	         *                           0x200 [ String : "ABCDEF" ]  <- 새로 만들어짐
	         */
		      String newData  =  data2.toString();
		      System.out.println(newData.toString()); //"ABCDEF"
		      
		      
		      /* ┌────────────────────────────────────────────────────────────┐
		         * │ 실행 전                                                     │
		         * │      data, data2 ────▶ 0x100 [ "ABCDEF" ]                  │
		         * │                          인덱스 :   0 1 2 3 4 5              │
		         * │                                   A B C D E F              │
		         * └────────────────────────────────────────────────────────────┘
		         *
		         * delete(3, 4) 가 하는 일
		         *      인덱스 3 부터 인덱스 4 바로 앞까지 지운다.
		         *      끝 번호인 4 는 포함되지 않는다. 그래서 인덱스 3 의 D 하나만 지워진다.
		         *
		         *          지우기 전 :  A  B  C  D  E  F
		         *        인덱스     :  0  1  2  3  4  5
		         *                             ↑ 여기만 삭제
		         *          지운 후   :  A  B  C  E  F
		         *         인덱스     :  0  1  2  3  4      <- 뒤 문자들이 앞으로 당겨진다
		         *
		         * 실행 후
		         *      data, data2 ────▶ 0x100 [ StringBuilder : "ABCEF" ]
		         *                                       │
		         *                                       ▼ toString()
		         *                        0x300 [ String : "ABCEF" ]
		         */
		      System.out.println( data2.delete(3, 4).toString() ); //"ABCEF"
		      
		      
		      
		      /* ┌────────────────────────────────────────────────────────────┐
		         * │ 실행 전                                                     │
		         * │      data, data2 ────▶ 0x100 [ "ABCEF" ]                   │
		         * │                          인덱스 :   0 1 2 3 4                │
		         * │                                   A B C E F                │
		         * └────────────────────────────────────────────────────────────┘
		         *
		         * insert(0, "G") 가 하는 일
		         *      인덱스 0 자리에 "G" 를 끼워 넣는다.
		         *      원래 그 자리에 있던 문자들은 뒤로 한 칸씩 밀린다.
		         *
		         *          넣기 전 :        A  B  C  E  F
		         *          인덱스   :       0  1  2  3  4
		         *
		         *          G 를 0 자리에 끼워 넣음
		         *                    ↓
		         *          넣은 후 :   G  A  B  C  E  F
		         *          인덱스   :  0  1  2  3  4  5   <- 기존 문자들이 1씩 밀림
		         *
		         * 실행 후
		         *      data  ─┐
		         *      data2 ─┼─▶ 0x100 [ StringBuilder : "GABCEF" ]
		         *      data3 ─┘         (세 변수가 모두 같은 객체 하나를 가리킨다)
		         */
		      StringBuilder data3 = data2.insert(0, "G");
		      
		      System.out.println(data3.toString());  //"GABCEF"
		      
		      // 3개의 참조변수는 StringBuilder 객체 메모리 하나에 접근해서 사용하고 있다.
		      System.out.println("data = " + data);
		      System.out.println("data2 = " + data2);
		      System.out.println("data3 = " + data3);
		      
		      // 3개의 참조변수에 같은 하나의 StringBuilder 객체 메모리 주소가 저장되어 있는지 확인 
		      System.out.println(data == data2 && data2 == data3);
		      //					 true      &&      true
		      //						   true

		     System.out.println("------------------------------------------------------------------");
		     /*
		     	메소드 체이닝 기법?   . 도트연산자를 사용해서 메소드로 부터 반환받은 객체의 메소드를 다시 호출하는 기법  
		     	
		     															 // new StringBuilder("하세요 공부");
		     */									//new StringBuilder("하세요").append(" 공부");
		     StringBuilder sb2 = new StringBuilder("안녕하세요").delete(0, 2).append(" 공부");
		    		                             //0 1 2 3 4
		     
//		     StringBuilder  sb2 = new StringBuilder("하세요 공부");		
		     
		     System.out.println( sb2.toString() ); //"하세요 공부"
		      
	}

}









