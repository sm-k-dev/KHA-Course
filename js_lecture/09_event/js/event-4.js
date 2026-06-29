/*
    이벤트 등록 방법2.

    HTML요소 선택후
    onclick = function(){} 작성해 click 이벤트 등록 방법
*/
document.querySelector("button").onclick = function(){
    document.body.style.backgroundColor = "green";
}