package WrapperClass;

//주제 : 박싱 과 언박싱 연습 

public class Test1 {
	public static void main(String[] args) {
/*
=============================================================
📌 [기본 개념 정리]

✔ 박싱(Boxing, 수동 박싱)
   - 기본자료형(int, double 같은 값)을
     래퍼(포장) 클래스(Integer, Double 등)의 "래퍼객체" 안에 저장하는 작업
   - 즉, 기본자료형 값 → 객체의 변수에 저장 하는것.
   - 예) int 10 → new Integer(10);

✔ 언박싱(Unboxing, 수동 언박싱)
   - 래퍼 객체 내부에 저장된 기본자료형 값을 다시 꺼내는 작업. 
   - 예) new Integer(10).intValue() → 10
=============================================================			
*/	
		//기본 자료형 int의 데이터 10을 박싱(수동 박싱) 하자.
		
			//박싱 방법1.
			//Integer  i = new Integer(10);
			
			//박싱 방법2.
			 Integer   i = Integer.valueOf(10);
			 //	       i = new Integer(10);
		
			//박싱 된 int 데이터 10을 언박싱해서 꺼내와 얻어 출력 하자.
			//언박싱 방법.   intValue()메소드를 호출해서 기본자료형 10 데이터를 다시 얻습니다.	  
			System.out.println(  i.intValue()  );
//====================================================================
/*		  
		  포장클래스란(래퍼클래스란)?	
				  - 기본자료형의 데이터를 객체메모리의 변수에 저장시킬떄 사용되는 클래스들
				  - 기본자료형  8 개에 대응되는 래퍼클래스들을 제공 해줍니다.
				  
				    기본자료형					래퍼클래스
				    byte						Byte
				    char						Character
				    short						Short
				    int							Integer
				    long						Long
				    float						Float
				    double						Double   <--------------------포장(래퍼)클래스 이용 
				    boolean						Boolean		  
*/			
			
			//기본자료형 double의 데이터 3.14를 박싱(수동 박싱) 하자.
			
				//박싱방법1.
				//Double d = new Double(3.14);
				
				//박싱방법2.
				 Double d = Double.valueOf(3.14);
			   //Double d = new Double(3.14); 
			
			//언박싱 해서 3.14를 다시 얻어  출력 해보기.
				 double result = d.doubleValue(); //3.14 반환받아 저장
				 System.out.println(result);   //3.14 출력
						    
			/*
			      ✨ 핵심 개념 한 문장 암기

			      "valueOf()는 수동박싱! , intValue()/doubleValue()는 수동 언박싱! 

			*/  
	}

}











