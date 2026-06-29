/*
    1. 
    <button>Click< /button> 요소를 선택해서
    button 상수에 저장
*/
const button = document.querySelector("button");

/*
    참고. 선택한 요소에 특정이벤트 등록 후 이벤트 처리 문법

        선택한요소.on등록할이벤트종류명 = function(){ // function은 이벤트 핸들러라고 부른다
            등록한 이벤트 발생시 처리할 코드;
        };
    
    =========================================================

    2. 1 에서 선택한 button 요소에 click 이벤트 등록 후 이벤트 처리
*/
button.onclick = function(){
    // <body>< /body> 요소를 선택해서 가져와 배경색을 녹색으로 설정
    document.body.style.backgroundColor = "green";
};