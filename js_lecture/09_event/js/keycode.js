/* 
    1. <body>< /body> 요소 선택해서 저장
*/
const body = document.body;

/*
    2. <div id="result">< /div> 요소 선택해서 저장
*/
const result = document.querySelector("#result");

/*
    3. 선택한 <body>< /body> 요소에 키보드의 키를 누르고 있는 동작 (keydown 이벤트)을 등록해
        사용자가 키를 누르고 있는 동작을 실행할때마다 이벤트핸들러 함수가 호출되어 실해오디게 작성
*/
body.addEventListener("keydown", (e) => {
    /* 3.1 <body>< /body> 영역을 보면서 키보드의 어떤 키를 누르고 있는 동작을 하는지 얻어 출력 */
    // 선택한 <div id="result">콘텐츠영역< /div>의 콘텐츠영역에 누른 키 코드값 정보를 넣어 보여주자
    result.innerText = ` code: ${e.code}, key: ${e.key}`;
});

/*
    이벤트 핸들러함수 (e) 를 보면 e매개변수는 
    이벤트 핸들러 함수가 자동으로 호출될때 이벤트 분배스레드에 의해
    자동으로 전달되는 이벤트 객체입니다.
    이 이벤트(Event) 객체는 이벤트가 발생했을때의 html요소에 관한 다양한 정보를 지닌 객체 입니다

    1. e.type  -> 이벤트의 유형을 제공해줌. 예를 들어 'keydown', 'keyup', 'click'등
    2. e.target -> 이벤트가 발생한 DOM요소를 제공해줌. 예를 들어 사용자가 클릭한 <button> 등
    3. e.code -> 눌린 키의 물리적 키 코드를 제공해줌
                            키의 위치를 기준으로 하며, 예를 들어 "KeyA" 는 A키를 나타냅니다.
    4. e.key  -> 눌린 키의 실제 값을 제공해줌
                            예를 들어, shift키를 누른상태에서 A키를 누르면
                e.key는 "A"가 됩니다. 
                
    5. e.altKey , e.ctrKey, e.shiftKey ->
        각각 Alt,     Ctrl,     Shift 키 가 눌렀는지를 나타내는 true또는 false반환 

    6. e.preventDefault()메소드 ->
        
    <a href="http://" onclick="alert('d');"> 
    href속성은 클릭을 유도하는 기본이벤트 동작을 차단하는 메소드

    요약 : 링크 클릭시 페이지 이동을 막을수 있습니다.

    7. e.stopPropagation()메소드 -> 
    
    이벤트가 상위요소로 전파되는것을 막는 메소드입니다.
    이벤트 버블링을 제어할때 사용됩니다.
*/


/*
    이벤트 등록 방식 3. addEventListener 메소드를 이용하여 선택한 요소에 이벤트 종류 하나 이상 등록
    문법) 요소선택.addEventListener( "등록할이벤트종류1 등록할이벤트종류2 ...", 이벤트핸들러함수, 옵션작성 );

    작성문법 참고. 옵션작성 자리에 작성할 코드 종류
        { once : true } => 선택한 요소에 이벤트종류 등록하면 이벤트핸들러 함수가 여러번의 이벤트가 발생해도
                            이벤트 처리를 단 한번만 수행함
                        
        { capture : true } => 이벤트 캡처링 단계에서 이벤트처리 실행하는 값

        { capture : false } => 옵션작성 자리에 작성을 생략하면 적용되는 기본값 false 이다. 이벤트 버블링 단계에서 이벤트처리 실행하는 값
*/