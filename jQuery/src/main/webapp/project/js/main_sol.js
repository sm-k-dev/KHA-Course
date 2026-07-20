$(function() {
/*
	콘텐츠 영역 개발하기
	
		-콘텐츠 영역은 크게 
		  비주얼배너, 
		  알림판, 
		  최근게시물, 
		  알림배너, 
		  베스트Book, 
		  페이스북,
		  마케팅, 
		  온라인서점
		 으로 나뉩니다.
		 
		-레이아웃은 비주얼 배너가 들어가는 visual영역과
		  나머지 주제 소스들이 들어가는 content영역으로 나뉘었음.  
 */

//-----------------------------------------------------------
/*
 주제 : 비주얼 배너 터치 슬라이드 만들기
 비주얼 배너 영역은 배너 중 한개만 노출되어 이루어져 있으며,
 [이전/다음]버튼을 누르면 배너가 이동되어 바뀌게 됨.
 스마트폰에서는 손가락으로 터치 했을때 도 배너가 바뀔수 있도록 제작 하자

 //https://github.com/bradbirdsall/Swipe
 */


  /* 터치 슬라이드 비주얼 영역  */
  /*
	    <div id="mySwipe" class='swipe'>
			<ul class="touch_banner swipe-wrap">
				<!-- 배너 목록 -->
				<li><a href="#"><img src="images/visual_img_1.jpg" alt="" /></a></li>
				<li><a href="#"><img src="images/visual_img_2.jpg" alt="" /></a></li>
				<li><a href="#"><img src="images/visual_img_3.jpg" alt="" /></a></li>
			</ul>
		</div>
  */
	window.mySwipe = $("#mySwipe").Swipe({
		
		//auto속성 - 자동슬라이드 전환을 위한 시간간격을 설정하는 속성
		//			설정하지 않으면 자동전환 비활성화 됨
		auto : 3000,
		
		// continuous속성 - 반복해서 슬라이드를 보여줄건지 설정하는속성
		//                 true 반복함, false 반복안함
		continuous : true,
		
		// callback 속성 - 슬라이드 전환 후 자동으로 호출되는 함수를 설정하는 속성
		// 				  배너의 하나의 슬라이드 이동이 완료 될때마다 function(){}이 호출되어
		//                {} 중괄호 내부의 실행문이 실행됩니다.
		//이때 index매개변수에는 현재 노출된 배너 이미지를 포함한 <li>태그의 index위치 값이 할당 됩니다.
		//그리고 element매개변수에는 현재 노출된 <li>요소 자체가 할당 됩니다.
		callback : function(index, element){
			
			//슬라이드가 전환 되면  두번째 슬라이드의 화면이 나올 것이니  첫번째 슬라이드 화면의 위치를 표시 하는 
			//동그라미 빨간색 이미지를 하얀색이미지로 변경 한다.
			$(".touch_bullet .active").attr("src","images/visual_bullet_off.png");
			
			//<im>태그의 class="active" 의 active값 삭제
			$(".touch_bullet .active").removeClass("active");
			
			//==========================================================
			
			//전환된 슬라이드 화면의 동그라미 버튼 이미지가 하얀색에서 빨간색으로 표시되게 하자.
			//그리고 선택한 <img>태그의 class속성에 "active"값 생성!
			$(".touch_bullet img").eq(index).attr("src", $(".touch_bullet img")
														.eq(index)
														.attr("src")
														.replace("off.png","on.png") 
												  ).addClass("active");				
		}		
		
	}).data('Swipe');
	 /*
		 .data('Swipe');
		- Swipe메소드로 생성된 슬라이드 객체를 가져오는데 사용됩니다.
		        이 객체는 나중에 다른곳에서 사용할수 있도록 저장되며,
		        예를 들어 슬라이드 조작( > 다음,  < 이전 으로 이동 등)을 위해 필요합니다.
		- 즉 .data('Swipe') 는 Swipe객체의 메서드로 생성된 객체를 반환하여
		        이후에 해당 슬라이드를 제어하는데 사용할수 있게 합니다.
	  */	
		
	 /*  이전  <   ,    다음 >  각각 클릭했을때 슬라이드 전환 처리 */	
	 
	 //요약 : 이전 < <a>를 선택해 click이벤트 등록 후 이전 배너 사진 화면으로 이동 되게 하자 
	 $(".touch_left_btn a").on("click", function(){
		
		mySwipe.prev(); //이전 배너 사진 화면으로 이동 
		
		return false;
	 });
	 
	 //요약 : 다음 > <a>를 선택해서 click이벤트 등록 후 다음 배너 사진 화면으로 이동 되게 하자
	 $(".touch_right_btn a").on("click", function(){
		
		mySwipe.next(); //다음 배너 사진 화면으로 이동(전환)
		
		return false;
	 });
	 
	
//-----------------------------------------------------------

  /*
   주제 : 자동 롤링 배너와 제어 버튼을 활용한 알림판 만들기
  
   알림판은 일정 시간 간격으로 자동으로 배너 이미지가 바뀌면서 해당하는 배너에 버튼이 활성화 됨.
   이때 버튼을 마우스로 클릭하면 버튼에 해당하는 배너로 이동 됨.
   그리고 정지 ■ 버튼을 누르면 자동으로 넘어가던 배너가 정지되고, 
   재생 ▶ 버튼을 누르면 다시 배너가 넘어가게 됨 

   
   
    1. index.html을 웹브라우저로 처음 요청 했을때 첫 화면은
       [1] 버튼이미지에 관한  배너1 이미지만 보이게 하고
       [2] [3] [4] 버튼이미지에 관한 배너2, 배너3, 배너4 이미지는 숨긴다.
   */
   $("#roll_banner_wrap dd").not(":first").hide(); //숨김 
   
   //첫번째 [1]버튼 <img>태그를 감싸고 있는 <a>를 최종선택해 onBtn변수에 저장
   let onBtn = $("#roll_banner_wrap dt a:first");
   
   /*
    2. [1] ~ [4] <img>태그를 감싸고 있는 모든 <a>들을 선택해서 click이벤트등록  
   
		숫자 버튼 <img>를 감싸고 있는 <a>를 클릭하면
		클릭하지 않은 <a>의 <img> 이미지는 하얀색 이미지로 변경 해야 하고
		클릭한  숫자의 <img>이미지는 빨간색이미지로  변경 시켜야 함 
   */
    $("#roll_banner_wrap dt a").on("click", function(){
		
		//현재 화면에 노출된  배너 사진 이미지 영역<dd> 을 숨깁니다.
		$("#roll_banner_wrap dd:visible").hide();
		
		//onBtn변수에 저장되어 있는 <a>의 하위요소 <img>를 최종 선택해
		//src속성의 이미지 주소 값을  하얀색 [1] 이미지 주소 경로로 변경 
		//참고.  attr("속성","변경할 새 주소값");
		$("img",onBtn).attr("src",  $("img",onBtn).attr("src").replace("over.gif","out.gif") );
		
		//[1] ~ [4] <a> 중 클릭한 <a>를 선택해  <a>의 index위치 번호를 구해 변수에 저장
		let num =  $("#roll_banner_wrap dt a").index(this);

		//클릭한 <a>의 index 위치번호값과 일치하는 <dd>태그영역(배너이미지영역)만 화면에 나타나게 해야 합니다
		//자세히 : 변수 num에 저장된 클릭한 <a>의  index위치 번호값을 이용해
		//		 그에 해당하는 배너를 포함하는 <dd>태그를 선택해서 show()메소드로 보여줌		
		$("#roll_banner_wrap dd").eq(num).show();
		
		//클릭한 <a>의 하위 <img>에 "src"속성의 이미지 주소값을 빨간색이미지 경로로 변경
		$("img",this).attr("src", $("img",this).attr("src").replace("out.gif","over.gif"));                   
		
		//[1] ~ [4] 중 click이벤트가 발생한 <a>요소를 선택해서 onBtn변수에 저장
		onBtn = $(this);
		
		//4쌍의 <a>중 click이벤트가 발생하면  <a> href기본이벤트 가 먼저 실행되니 차단
		return false;
	});
	/*
	 3.
	
	*/
	
	
	/*  4.  재생 버튼 ▶ <a> 또는 정지 버튼 ■ 클릭시 이벤트처리  */
	
	// 재생 버튼 ▶ <a>를 선택해서 가져와 click이벤트 등록
	$(".playBtn").on("click",function(){
		
		alert('>재생 클릭함');
		
		return false;
	});
	

	 	 
	  
	 
 //-----------------------------------------------------------
  /*
   주제 : 탭 메뉴를 이용해 최근 게시물 리스트 만들기
  
  - 탭메뉴의 경우 최초 탭버튼인[공지사항]이 활성화되어 보입니다.
    만일 방문자가 [질문과답변]탭을 클릭했을 때는 [공지사항]은 숨겨져야 하고,
    [질문과 답변]의 내용은 활성화되어 보여야 합니다.
    
  - 먼저 탭버튼에 <a>에 on()메서드를 사용하여 mouseover,focus,click이벤트를         등록하였고,
  	이벤트 핸들러에는 이벤트가 발생 했을때 마우스를 올린 탭 버튼과 탭에 해당하는 게시물 목록이 활성화되어 보이도록 만들자. 
   */
	 	
	  
 //-----------------------------------------------------------
	  
/*  
주제 : 자동 슬라이드 배너 를 이용한 베스트 Book영역   
	  https://bxslider.com/ 접속하여 사용법 보기 

  bxSlider 플러그인 이란?
    - 여러개의 배너에 슬라이드 기능을 간편하게 적용할수 있는 플러그인 종류중 하나

  bxSlider 플러그인 사용 문법
   
    	참조변수 = $("슬라이드 기능을 설정할 요소영역 선택").bxSlider({속성명:값, 속성명2:값2});
		
	 베스트 book 목록 태그 영역인 <ul>태그를 선택하여 
	 bxSlider() 메소드를 호출하여 적용하고  CSS옵션을 지정하자.
*/	
	let mySlider = $("#best_bg ul").bxSlider({
	 
		 mode:"horizontal", //수평 방향으로 이동시키기 
		 speed: 500, //슬라이드 이동 속도 (500: 0.5초)
		 pager: false, // 페이징 표시를 제어 (false:숨김, true:노출)
		 moveSlides: 2, //이동슬라이드 수 설정
		 slideWidth: 125, //슬라이드 폭 설정
		 minSlides: 1, //최소 노출될 슬라이드 수 설정
		 maxSlides: 2,  //최대 노출될 슬라이드 수 설정
		 slideMargin:30, //슬라이드 간의 간격 설정 
		 auto: true,     //자동 슬라이드 전환 여부 (true: 자동, false: 수동) 설정 
		 autoHover: true, // 마우스 오버시 슬라이드 전환 여부(true:정지, false:정지X) 설정 
		 controls : false // 이전prev  다음Next 버튼을 숨김 설정 (true:노출, false:숨김) 
	});

	// < 이전 <p>요소 영역을 클릭할때마다  슬라이드 한단계 이전으로 이동되게 하여 보여주자
	$(".prev_btn").on("click", function(){
		
		mySlider.goToPrevSlide(); //이전 슬라이드 전환
		
		return false;
	});

	// > 다음 <p>요소 영역을 클릭할때마다 슬라이드 한단계 다음으로 이동되게 하여 보여주자
	$(".next_btn").on("click", function(){
		
		mySlider.goToNextSlide(); //다음 슬라이드로 전환
		
		return false;
	});
	
	


 //-----------------------------------------------------------

  /*  
  주제 : 제이쿼리 UI플러그인과 쿠키 플러그인 사용 하기
  - 팝업창을 드래그 하여 이동시키려면, 제이쿼리 UI플러그인을 사용함.
  - [하루동안 이창 열지 않기]버튼 기능을 하용하기 위해서는 쿠키 플러그인을 사용함
  
  참고 : 쿠키 플러그인 사용법
  	       
  	    <쿠키를 생성 하는 기본 사용법>
  	    
  	  	 $.cookie("쿠키명","쿠키값",{expires:만료일});
  	  	 설명 : 쿠키명은 나중에 저장된 쿠키의 값을 불러올때 구분하기 위한 이름임.
  	  	            생성된 쿠키는 현재 부터 며칠동안 
                            클라이언트 컴퓨터의 웹브라우저에 보관할건지 만료일(expires)을 지정할수 있음.

			예)
	 	     $.cookie("pop","no",{expires:1}));
	 	         설명: 브라우저에는 "pop"라는 이름으로 "no"라는 값이 
                   1일 동안 쿠키가 보관 됩니다.
 	         
 	    <쿠키 플러그인을 이용하여  브라우저에 저장된 쿠키를 불러오는 기본 사용법>
 	    	$.cookie("쿠키명");
 	    
        	저장된 쿠키값인 "no" 불러오는 방법의 예)
	 	    $.cookie("pop");
 	    
 	    <쿠키 플러그인을 이용하여 브라우저에 저장된 쿠키를 삭제하는 기본 사용법>
 	    $.cookie("쿠키명",null);
 	    
 	    "pop"에 저장된 쿠키값 삭제의 예)
         $.cookie("pop",null);
  	  		
  */
	  

	


});

