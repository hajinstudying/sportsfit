document.addEventListener('DOMContentLoaded', function() {

    /* csrf 토큰 */
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    /* 요소 가져오기 */
    const itemOption = document.getElementById('itemOption');
    const selectedOptions = document.getElementById('selectedOptions');
    const addToCartBtn = document.getElementById('addToCart');
    const addToOrderBtn = document.getElementById('addToOrder');
    const selectedOptionMap = new Map();

    /* CSRF 토큰을 포함한 fetch 요청 헬퍼 함수 */
    function fetchWithCSRF(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
        };
        return fetch(url, { ...defaultOptions, ...options });
    }

    /* 옵션 선택시 선택한 옵션 정보 가져오기 */
    itemOption.addEventListener('change', function() {
        const selectedOptionId = this.value;
        if (selectedOptionId) {
            fetchOptionDetails(selectedOptionId);
        }
        this.selectedIndex = 0;
    });

    /* 옵션 데이터 비동기로 불러오기 */
    function fetchOptionDetails(optionId) {
        fetchWithCSRF(`/api/options/${optionId}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`HTTP error! status: ${response.status}, body: ${text}`);
                    });
                }
                return response.json();
            })
            .then(optionVo => {
                if(!selectedOptionMap.has(optionVo.optionId)) {
                    addOptionToSelection(optionVo);
                } else {
                    alert('이미 선택된 옵션입니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('옵션 정보를 가져오는 데 실패했습니다. 다시 시도해 주세요.');
            });
    }

    // 선택목록에 선택한 옵션 정보 보여주기
    function addOptionToSelection(option) {
        const optionDiv = document.createElement('div');
        optionDiv.className = 'selected-option mb-2';
        optionDiv.innerHTML = `
            <div class="d-flex justify-content-between align-items-center">
                <span>${option.optionValue} (+${option.additionalPrice}원)</span>
                <div>
                    <input type="number" class="form-control form-control-sm d-inline-block w-auto" value="1" min="1" style="width: 60px;">
                    <button class="btn btn-sm btn-danger ms-2">삭제</button>
                </div>
            </div>
        `;

        const quantityInput = optionDiv.querySelector('input');
        const deleteBtn = optionDiv.querySelector('button');

        // 옵션 수량 변경시 updateTotalPrice 호출
        quantityInput.addEventListener('change', updateTotalPrice);

        // 옵션 삭제 클릭시
        deleteBtn.addEventListener('click', function() {
            selectedOptions.removeChild(optionDiv);
            selectedOptionMap.delete(option.optionId);
            updateTotalPrice();
        });

        // 선택옵션목록에 옵션 추가
        selectedOptions.appendChild(optionDiv);
        selectedOptionMap.set(option.optionId, { optionDiv, additionalPrice: option.additionalPrice });
        updateTotalPrice();
    }

    /* 가격 관련 요소 가져오기 */
   const basePrice = parseInt(document.getElementById('price').textContent.replace(/,/g, ''));
   const totalPriceElement = document.getElementById('totalPrice');

    // 총 가격 보여주는 메소드
    function updateTotalPrice() {
        let totalPrice = 0; // 초기 총합계 0원
        selectedOptionMap.forEach(({ optionDiv, additionalPrice }) => {
            const quantity = parseInt(optionDiv.querySelector('input').value);
            totalPrice += (basePrice + additionalPrice) * quantity;
        });
        totalPriceElement.textContent = totalPrice.toLocaleString(); // 천단위 콤마 포맷팅
        console.log('Total Price:', totalPrice);
    }

    // 페이지 로드 시 초기 가격 설정
    updateTotalPrice();

    // 장바구니 추가 로직
    addToCartBtn.addEventListener('click', function() {
        const itemId = document.getElementById('itemId').value;

        // 옵션을 선택하지 않고 버튼 클릭한 경우
        if(selectedOptionMap.size === 0) {
            alert('옵션을 선택해주세요.');
            return;
        }

        // 옵션 선택한 경우 장바구니 상품 목록 생성
       const cartItems = Array.from(selectedOptionMap.entries()).map(([optionId, value]) => ({
           itemId: parseInt(itemId),
           optionId: parseInt(optionId),
           count: parseInt(value.optionDiv.querySelector('input').value)
       }));

        // 장바구니에 추가 api 호출
        fetchWithCSRF('/api/cart/add', {
            method: 'POST',
            body: JSON.stringify(cartItems)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text)});
            }
            return response.text();
        })
        .then(message => {
            alert(message);
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message || '장바구니 추가에 실패했습니다. 다시 시도해주세요.');
        });
    });

    addToOrderBtn.addEventListener('click', function() {
        // 주문하기 로직
    });
});