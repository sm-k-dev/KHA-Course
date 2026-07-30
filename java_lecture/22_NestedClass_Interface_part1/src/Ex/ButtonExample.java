package Ex;

/*
================================================================================
 [문제] 로컬 중첩 클래스 -> 익명 구현 객체로 변경하기
================================================================================

 1. 지금 이 파일의 상태
    - 버튼 클릭 이벤트 처리를 "로컬 중첩 클래스" 방식으로 만든 원본 코드가
      main 안에 [변경 전] 주석 블록으로 들어 있다.
    - OKListener, CancelListener 라는 이름 있는 클래스를 만들어
      new OKListener() 로 객체를 생성해 등록하는 방식이었다.

 2. 해야 할 일
    - 같은 실행 결과가 나오도록, 로컬 중첩 클래스 없이
      "익명 구현 객체" 방식으로 바꿔서 TODO 위치에 작성하라.
    - 클래스 이름(OKListener, CancelListener)이 코드에서 사라져야 한다.

 3. 변경에 사용할 작성 문법

        참조변수.setClickListner(new 바깥클래스명.중첩인터페이스명() {

            @Override
            public void onClick() {
                // 클릭 이벤트 처리 코드
            }
        });

    의미1. Button.ClickListener 인터페이스를 구현한 이름 없는 자식 클래스를
          만드는 동시에
    의미2. 그 자식 객체 1개를 생성해서 setClickListner() 매개변수로 바로 전달한다.

 4. 변경 전후 비교 (무엇이 줄어드는가)

    변경 전 (로컬 중첩 클래스)              변경 후 (익명 구현 객체)
    ------------------------------------   ------------------------------------
    ① class OKListener ... { } 선언        (선언 없음)
    ② new OKListener() 로 객체 생성        ①+②를 한 문장으로 :
    ③ setClickListner( ) 로 전달              setClickListner(new ...(){ });
    -> 3단계, 클래스 이름 필요              -> 1단계, 클래스 이름 불필요

    로컬 중첩 클래스는 "한 번만 쓰고 버릴 클래스"인데도 이름을 짓고
    선언을 따로 해야 했다. 한 번만 쓸 것이라면 익명 구현 객체가 더 짧다.

 [완성 후 실행 결과 - 변경 전 원본과 완전히 같아야 한다]
 Button btnOK = new Button(); 버튼을 클릭했으니 선물을 줄게요!!
 <Button>Cancel</Button> 취소 버튼을 클릭했으니 집으로 가겠다.

 채점 기준
 | 항목                                        | 배점 |
 |---------------------------------------------|------|
 | TODO 1 : btnOK 에 익명 구현 객체 등록       | 40   |
 | TODO 2 : btnCancel 에 익명 구현 객체 등록   | 40   |
 | 로컬 중첩 클래스 선언이 남아 있지 않은가    | 10   |
 | 실행 결과가 원본과 완전히 일치하는가        | 10   |
================================================================================
*/

//------------------------------------------------------------------------------
// [제공됨 - 수정 금지] 버튼 설계도 클래스
//------------------------------------------------------------------------------
class Button {

	// 정적 중첩 인터페이스 : 클릭 규칙
	// public : 외부 클래스(ButtonExample)에서 구현해야 하므로 밖에서 접근 가능해야 한다.
	// static : Button 객체 없이 Button.ClickListener 이름으로 바로 사용하기 위해서다.
	public static interface ClickListener {

		// 추상메소드 : 클릭되었을 때 실행할 동작
		void onClick();
	}

	// 등록된 클릭 동작(객체)의 주소를 저장하는 인스턴스 변수
	private ClickListener clickListner;

	// setter : 클릭 동작 등록
	public void setClickListner(ClickListener clickListner) {
		this.clickListner = clickListner;
	}

	// 클릭 이벤트 발생 : 등록된 객체의 오버라이딩된 onClick() 이 최종 실행된다.
	public void click() {
		this.clickListner.onClick();
	}
}

//------------------------------------------------------------------------------
// 실행 클래스
//------------------------------------------------------------------------------
public class ButtonExample {

	public static void main(String[] args) {

		// ==================================================================
		// [1] 확인(OK) 버튼
		// ==================================================================
		Button btnOK = new Button();

		/* -------------------- [변경 전 원본 : 로컬 중첩 클래스 방식] --------------------

		// ① 로컬 중첩 클래스 선언 : 이름을 짓고, implements 로 구현하고, 오버라이딩까지
		class OKListener implements Button.ClickListener {

			@Override
			public void onClick() {
				System.out.println("Button btnOK = new Button(); 버튼을 클릭했으니 선물을 줄게요!!");
			}
		}

		// ② 객체 생성 + ③ 등록
		btnOK.setClickListner(new OKListener());

		--------------------------------------------------------------------------------- */

		// TODO 1.
		//  위 [변경 전 원본]과 같은 동작을 익명 구현 객체 한 문장으로 작성하세요.
		//  - class OKListener 선언 없이
		//  - btnOK.setClickListner(new Button.ClickListener() { ... }); 형태로
		//  - onClick() 오버라이딩 내용(출력 문장)은 원본 그대로
		btnOK.setClickListner(new Button.ClickListener() {
			
			@Override
			public void onClick() {
				System.out.println("Button btnOK = new Button(); 버튼을 클릭했으니 선물을 줄게요!!");
			}
		});


		// TODO 1 완성 후 아래 주석을 해제하고 실행하세요.
		// (등록 전에 해제하면 clickListner 가 null 이라 NullPointerException 이 발생합니다)
		btnOK.click();

		// ==================================================================
		// [2] 취소(Cancel) 버튼
		// ==================================================================
		Button btnCancel = new Button();

		/* -------------------- [변경 전 원본 : 로컬 중첩 클래스 방식] --------------------

		class CancelListener implements Button.ClickListener {

			@Override
			public void onClick() {
				System.out.println("<Button>Cancel</Button> 취소 버튼을 클릭했으니 집으로 가겠다.");
			}
		}

		btnCancel.setClickListner(new CancelListener());

		--------------------------------------------------------------------------------- */

		// TODO 2.
		//  위 [변경 전 원본]과 같은 동작을 익명 구현 객체 한 문장으로 작성하세요.
		//  - class CancelListener 선언 없이
		//  - 출력 문장은 원본 그대로
		btnCancel.setClickListner(new Button.ClickListener() {
			
			@Override
			public void onClick() {
				System.out.println("<Button>Cancel</Button> 취소 버튼을 클릭했으니 집으로 가겠다.");
			}
		});


		// TODO 2 완성 후 아래 주석을 해제하고 실행하세요.
		btnCancel.click();

		System.out.println("(TODO 1, 2 를 완성하고 click() 주석을 해제하세요)");
	}
}
