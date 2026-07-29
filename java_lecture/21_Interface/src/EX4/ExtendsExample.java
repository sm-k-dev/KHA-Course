package EX4;
/*
===============================================================
 인터페이스끼리의 상속
===============================================================

[1] 규칙

  인터페이스도 다른 인터페이스를 상속할 수 있다.
  상속에는 extends 를 사용한다. (implements 가 아니다)

  클래스는 extends 로 부모를 하나만 지정할 수 있지만,
  인터페이스는 콤마로 여러 개를 지정할 수 있다. (다중 상속)

    interface 자식인터페이스 extends 부모인터페이스1, 부모인터페이스2 {
        // 부모 인터페이스들의 추상메소드를 모두 물려받는다.
    }

[2] 구현 클래스가 해야 할 일

  자식 인터페이스를 implements 한 클래스는
  자식 인터페이스의 추상메소드뿐 아니라
  부모 인터페이스들의 추상메소드까지 전부 오버라이딩해야 한다.
  하나라도 빠뜨리면 컴파일 오류가 난다.

[3] 이 예제가 확인하려는 내용

  객체는 InterfaceCImpl 하나만 만든다.
  그 객체 하나를 4가지 자료형의 참조변수로 가리켜 보고,
  자료형에 따라 호출할 수 있는 메소드가 어떻게 달라지는지 확인한다.

  참조변수는 자기 자료형에 선언된 메소드만 호출할 수 있다.
  객체가 그 메소드를 실제로 가지고 있어도, 자료형에 없으면 호출할 수 없다.

[실행 결과]
InterfaceCImpl - methodA() 실행
---------------
InterfaceCImpl - methodB() 실행
----------------------
InterfaceCImpl - methodA() 실행
InterfaceCImpl - methodB() 실행
InterfaceCImpl - methodC() 실행
===============================================================
*/

//부모인터페이스1
interface InterfaceA{
	void methodA();  //추상메소드 
}

//부모인터페이스2
interface InterfaceB{
	void methodB(); //추상메소드 
}

/*
자식 인터페이스
- InterfaceA 와 InterfaceB 를 동시에 상속받는다.
- 상속받은 methodA(), methodB() 는 다시 적지 않아도 InterfaceC 의 멤버가 된다.
- 여기에 methodC() 를 추가로 선언한다.

따라서 InterfaceC 가 가진 추상메소드는 methodA(), methodB(), methodC() 3개다.
*/
interface InterfaceC  extends InterfaceA, InterfaceB {
	
	//void methodA(); //상속받음
	//void methodB(); //상속받음
	
	  void methodC(); //추상메소드 추가
}


/*
구현 클래스
- implements InterfaceC 하나만 적었지만,
  상속받은 것까지 포함해 추상메소드 3개를 모두 오버라이딩해야 한다.
- 오버라이딩할 때 public 을 반드시 붙인다.
  인터페이스의 추상메소드는 public 이므로 더 좁은 범위로 줄일 수 없다.

하나라도 빠뜨리면 나오는 오류
  error: InterfaceCImpl is not abstract and does not override
         abstract method methodB() in InterfaceB
*/
class InterfaceCImpl  implements InterfaceC {

	@Override
	public void methodB() {  //<=================== InterfaceB에서 물려받은 추상메소드 오버라이딩 
		System.out.println("InterfaceCImpl 클래스 - methodB() 실행");	
	}
	@Override
	public void methodA() { //<=================== InterfaceA에서 물려받은 추상메소드 오버라이딩 
		System.out.println("InterfaceCImpl 클래스 - methodA() 실행");	
	}

	@Override
	public void methodC() { //<=================== InterfaceC에서 물려받은 추상메소드 오버라이딩 
		System.out.println("InterfaceCImpl 클래스 - methodC() 실행");	
	}
}

public class ExtendsExample {

	public static void main(String[] args) {
		//[1] 구현 클래스 자료형으로 참조변수를 만들고 구현 클래스의 객체 생성해서 저장
		InterfaceCImpl  imCImpl = new InterfaceCImpl();
		/*									  0x16
						[  0x16]=  ======================================
										methodA(){}   <--- InterfaceA에 만들어 놓은 추상메소드를 오버라이딩 한것
										methodB(){}   <--- InterfaceB에 만들어 놓은 추상메소드를 오버라이딩 한것
										methodC(){}   <--- InterfaceC에 만들어 놓은 추상메소드를 오버라이딩 한것
								   =====================================
		*/		
						imCImpl.methodA();
						imCImpl.methodB();
						imCImpl.methodC();
						
		//[2] InterfaceA 부모 인터페이스 자료형으로  참조변수를 만들고 구현 클래스의 객체 생성해서 저장
		//요약 : 업캐스팅 하자
		InterfaceA   ia = imCImpl;
		/*									  0x16
				[  0x16]=  ======================================
								methodA(){}   <--- InterfaceA에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodB(){}   <--- InterfaceB에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodC(){}   <--- InterfaceC에 만들어 놓은 추상메소드를 오버라이딩 한것
						   =====================================
		 */			
					ia.methodA(); //최종 InterfaceCImpl클래스에 오버라이딩 해놓은 InterfaceA부모인터페이스 메소드만 호출 가능 	
		//			ia.methodB(); //methodB() 는 InterfaceA 에 없으므로 ia참조변수로는 호출할 수 없다.
		//			ia.methodC(); //methodC() 또한 InterfaceA 에 없으므로 ia참조변수로는 호출할 수 없다.
		
		//[3] InterfaceB 부모 인터페이스 자료형으로  참조변수를 만들고 구현 클래스의 객체 생성해서 저장
		//요약 : 업캐스팅 하자
		InterfaceB   ib = imCImpl;
		/*									  0x16
				[  0x16]=  ======================================
								methodA(){}   <--- InterfaceA에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodB(){}   <--- InterfaceB에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodC(){}   <--- InterfaceC에 만들어 놓은 추상메소드를 오버라이딩 한것
						   =====================================
		 */			
		//			ib.methodA(); //methodA() 는 InterfaceB 에 없으므로 ib참조변수로는 호출할 수 없다.
					ib.methodB();
		//			ib.methodC(); //methodC() 또한 InterfaceB 에 없으므로 ib참조변수로는 호출할 수 없다.					
		
		//[4] InterfaceC 부모 인터페이스 자료형으로  참조변수를 만들고 구현 클래스의 객체 생성해서 저장
		//요약 : 업캐스팅 하자
		InterfaceC   ic = imCImpl;
		/*									  0x16
				[  0x16]=  ======================================
								methodA(){}   <--- InterfaceA에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodB(){}   <--- InterfaceB에 만들어 놓은 추상메소드를 오버라이딩 한것
								methodC(){}   <--- InterfaceC에 만들어 놓은 추상메소드를 오버라이딩 한것
						   =====================================
		 */			
					ic.methodA();
					ic.methodB();
					ic.methodC(); 				
					 
					/*
					 -------------------------------------------------------
					  메모리 상태 : 객체는 1개, 참조변수는 4개

					  [ Stack 영역 ]
					  ┌──────────────────────────────────────┐
					  │ imCImpl  (InterfaceCImpl)  → 0x16    │
					  │ ia       (InterfaceA)      → 0x16    │
					  │ ib       (InterfaceB)      → 0x16    │
					  │ ic       (InterfaceC)      → 0x16    │
					  └──────────────────────────────────────┘
					                    │
					                    ▼
					  [ Heap 영역 ]  주소 0x16
					  ┌──────────────────────────────────────┐
					  │ InterfaceCImpl 객체                   │
					  │  methodA()                           │
					  │  methodB()                           │
					  │  methodC()                           │
					  └──────────────────────────────────────┘

					  네 참조변수 모두 같은 객체를 가리킨다.
					  실행되는 코드도 항상 InterfaceCImpl 에 오버라이딩한 코드다.
					  달라지는 것은 "호출할 수 있는 메소드의 범위"뿐이다.
					 -------------------------------------------------------

					  참조변수별 호출 가능 범위

					  참조변수    자료형             호출 가능한 메소드
					  ia         InterfaceA        methodA()
					  ib         InterfaceB        methodB()
					  ic         InterfaceC        methodA(), methodB(), methodC()
					  imCImpl    InterfaceCImpl    methodA(), methodB(), methodC()
					 -------------------------------------------------------
					*/		
	}

}










