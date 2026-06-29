/*
    1. <button>< /button> 요소를 선택해서 button 상수에 저장
*/
const button = document.querySelector("button");

/**
    2. 선택해온 <button>Click< /button> 요소에 click 이벤트 등록,
        그리고 click 이벤트가 발생하면 자동으로 호출될 이벤트 핸들러 함수 연결
*/
button.onclick = changeBackground; // 호출할 이벤트 핸들러 함수명 뒤에 () 소괄호를 작성하지 않고, 함수명만 작성

/**
    3. click이벤트가 발생하면 처리할 이벤트 핸들러 함수를 외부에 정의
*/
function changeBackground(){
    // <body>< /body> 요소 선택 후 배경색을 녹색으로 설정
    document.body.style.backgroundColor = "green";
}