/** 
 *  1. 화면 요소 가져오기
*/

/* 상품명을 적는 입력칸 */
const nameInput = document.getElementById("nameInput");
/* 가격을 적는 입력칸 */
const priceInput = document.getElementById("priceInput");
/* 등록 버튼 */
const addBtn = document.getElementById("addBtn");
/* 상품을 검색하는 입력칸 */
const searchInput = document.getElementById("searchInput");
/* 정렬 방식을 고르는 드롭다운 */
const sortSelect = document.getElementById("sortSelect");
/* 상품 목록이 들어갈 표의 본문 (tbody) */
const productTable = document.getElementById("productTable");
/* 총합 금액을 보여주는 글자 영역 */
const total = document.getElementById("total");

/** 
 *  2. 상품 정보를 저장할 비어 있는 배열 생성
*/
let products = [];

/**
 *  3. 상품 등록: 버튼 클릭시 실행되는 로직
 *      - 입력 검증: 값이 비었거나 가격이 숫자가 아닌 경우 경고
 *      - 상품 객체를 만들고 배열(products)에 추가
 *      - 입력창을 비우고 전체 HTML을 다시 브라우저가 읽어들이게 하자
*/
addBtn.addEventListener("click", () => {
    // 1) 사용자가 입력칸에 적은 값을 꺼내온다.
    const name = nameInput.value.trim(); // 입력한 상품명
    const price = Number(priceInput.value); // 입력한 가격을 숫자로 전환

    // 2) 입력값 검사 (하나라도 이상하면 등록을 멈춤)
    // !name 상수에 저장된 값이 비어 있으면 true
    // !price -> 가격이 0, 빈칸, 숫자가 아님(NaN) 이면 true
    if ( !name || !price ) {
        window.alert("상품명과 가격을 올바르게 입력하세요"); // 화면에 경고메세지를 팝업으로 띄워준다.
        return; // () => {} 함수 즉시 종료
    }

    // 3) 이 상품만의 고유번호(id)를 만든다.
    //      Date.now()      = 지금 시각을 아주 큰 숫자(밀리초)로 알려줌 => 매번 값이 달라 거의 겹쳐지지 않음
    //      Math.random()   = 0.0 ~ 1 사이의 무작위 실수를 반환 => 만에 하나 겹치는 것까지 방지
    //      두 값을 더해 "겹치지 않는 번호"를 만든다
    const id = Date.now() + Math.random();

    // 4) 위에서 모은 모든 값들로 상품 객체 하나를 만들어 products 배열에 추가
    //  방법 push(추가할 상품정보의 객체); <= 배열의 맨 뒤에 새 항목을 추가
    products.push( { id: id, name: name, price: price } );

    // 5) 다음 상품을 편하게 입력하도록 입력칸을 비운다 (빈 문자열 ""을 넣는다)
    nameInput.value = "";
    priceInput.value = "";

    // 6) 화면을 다시 그린다.
    //      products 배열의 데이터가 바뀌었으니, 그 내용에 맞춰서 표와 총합을 새로 그려주는 render() 호출
    render();
});

/**
 *  4. render() 함수
 *      - 역할: 현재 products 배열 상태에 맞춰 화면 (테이블)과 총합을 다시 그리는 함수
 *      - 동작순서 요약:
 *          1) 테이블 초기화
 *          2) 검색어로 Products 배열에서 꺼내서 보여줄 목록을 고름 (filter)
 *          3) 정렬을 적용(sort 함수)
 *          4) 화면에 행을 하나씩 추가
 *          5) 각 행의 삭제 버튼에 이벤트 연결 (id 기반 삭제)
 *          6) 총합을 계산해 화면에 갱신
*/

function render() {
    // 1) 테이블 표 안 <tbody>< /tbody>의 기존 내용을 전부 지운다.
    productTable.innerHTML = ""; 

    // 2) 검색칸에 입력된 검색어를 읽어온다.
    const keyword = searchInput.value.trim().toLowerCase();

    // 3) 전체 상품 중 '검색어가 이름에 포함된 상품'만 골라서 보여줄 목록을 만든다.
    let displayList = products.filter( item => item.name.toLowerCase().includes(keyword) );

    // 4) 선택된 목록(displayList)에 정렬 적용
    /**
     * sortSelect.vlaue 는 <select> 태그에서 사용자가 선택한 옵션 값
     *      <option value="asc">가격 낮은 순< /option>
     *      <option value="desc">가격 높은 순< /option>
     * 
     * 즉, 사용자가 선택한 정렬 방식에 맞춰 배열(displayList)을 정렬하는 기능
    */
    if ( sortSelect.value === "asc" ) {
        displayList.sort( (a, b) => a.price - b.price );
    } else if ( sortSelect.value === "desc" ) {
        displayList.sort( (a, b) => b.price - a.price );
    } 

    /*
        5) 삭제 함수 (인자로 상품 id 받도록 구현)
            - render() 에서 각 행의 삭제 버튼에 이 함수를 연결함
            - 배열에서 해당 상품객체의 id를 찾아서 제거후 다시 렌더
    */
    function deleteProductById( id ){
        products = products.filter( item => item.id !== id );
        render(); // 목록이 바뀌었으니 화면을 새로 그림
    }

    // 6) 화면 표에 상품 한 줄씩 추가하면서, 동시에 가격을 더해서 총합을 구한다.
    let sum = 0; // 가격을 차곡차곡 더해 나갈 '누적통' (0에서 시작)

    displayList.forEach( item => { 
        // a) 표의 한 행 <tr>< /tr> 생성
        const tr = document.createElement("tr");
        // b) 상품명이 들어갈 칸<td>< /td> 생성
        const tdName = document.createElement("td");
        tdName.textContent = item.name;
        const tdPrice = document.createElement("td");
        tdPrice.textContent = item.price.toLocaleString()+"원";
        const tdDel = document.createElement("td");
        const btn = document.createElement("button");
        btn.textContent = "삭제"

        btn.addEventListener("click", () => {
            deleteProductById( item.id );
        });

        tdDel.appendChild(btn);

        // e) tr안에 td 들을 왼쪽부터 순서대로 추가한다.
        tr.appendChild(tdName);
        tr.appendChild(tdPrice);
        tr.appendChild(tdDel);

        // f) 완성된 한 줄 (tr)을 표 본문 (tbody)에 붙여 넣는다
        productTable.appendChild(tr);

        // g) 상품의 가격을 sum에 누적해서 저장
        sum += item.price;
    });

    // 7) 모든 상품 가격을 더한 최종 금액을 화면 아래에 표시
    total.textContent = "총합: " + sum.toLocaleString() + " 원";
}

/** 
 *  8) 웹브라우저 주소창에 URL http://ip:port/PMS.html 입력후 엔터 눌러서 서버에 요청하면
 *      처음 화면을 한번 브라우저에 보여주자
*/
render();