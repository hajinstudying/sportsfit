$(document).ready(function() {

    //================== 하위 카테고리 불러오기=========================//

    $('#parentCategorySelect').change(function() {
        var categoryId = $(this).val();
        var subCategorySelect = $('#subCategorySelect');
        subCategorySelect.empty(); // 기존 옵션 제거
        subCategorySelect.append($('<option>', {
            value: '',
            text: '선택하세요'
        }));

        if (categoryId) {
            $.ajax({
                url: '/admin/categories/' + categoryId,
                type: 'GET',
                success: function(data) {
                    $.each(data, function(index, category) {
                        subCategorySelect.append($('<option>', {
                            value: category.categoryId,
                            text: category.categoryName
                        }));
                    });
                },
                error: function(xhr, status, error) {
                    console.error('하위 카테고리 불러오기 실패', error);
                }
            });
        }
    });

     // ================== 옵션 추가 관련 설정 =============================//

     let optionCount = 0;
     const addedOptionsContainer = document.getElementById('addedOptionsContainer');
     const addOptionBtn = document.getElementById('addOptionBtn');

     addOptionBtn.addEventListener('click', function() {
         // 첫 번째 옵션 폼의 값들을 가져오기
         const optionName = document.getElementById('optionName').value;
         const optionValue = document.getElementById('optionValue').value;
         const additionalPrice = document.getElementById('additionalPrice').value;
         const stockNumber = document.getElementById('stockNumber').value;

         // 모든 필드가 채워져 있는지 확인
         if (!optionName || !optionValue || !additionalPrice || !stockNumber) {
             alert('모든 옵션 필드를 채워주세요.');
             return;
         }

        // 입력한 옵션값들을 새로운 폼으로 만들어 표시
         const newOptionGroup = document.createElement('div');
         newOptionGroup.className = 'option-group mb-2 border p-3 rounded';
         newOptionGroup.innerHTML = `
             <div class="row mb-2">
                 <label class="col-sm-2 col-form-label">옵션명:</label>
                 <div class="col-sm-4">
                     <input type="text" class="form-control" name="options[${optionCount}].optionName"
                     value="${optionName}" readonly>
                 </div>
                 <label class="col-sm-2 col-form-label">옵션값:</label>
                 <div class="col-sm-4">
                     <input type="text" class="form-control" name="options[${optionCount}].optionValue"
                     value="${optionValue}" readonly>
                 </div>
             </div>
             <div class="row mb-2">
                 <label class="col-sm-2 col-form-label">추가가격:</label>
                 <div class="col-sm-4">
                     <input type="number" class="form-control" name="options[${optionCount}].additionalPrice"
                            value="${additionalPrice}" readonly>
                 </div>
                 <label class="col-sm-2 col-form-label">재고 수량:</label>
                 <div class="col-sm-4">
                     <input type="number" class="form-control" name="options[${optionCount}].stock_number"
                            value="${stockNumber}" readonly>
                 </div>
             </div>
             <button type="button" class="btn btn-danger btn-sm remove-option">삭제</button>
         `;
         addedOptionsContainer.appendChild(newOptionGroup);
         optionCount++;

         // 옵션 삭제 버튼에 이벤트 리스너 추가
         newOptionGroup.querySelector('.remove-option').addEventListener('click', function() {
             addedOptionsContainer.removeChild(newOptionGroup);
         });

         // 첫번째 옵션 폼의 옵션값 초기화
         document.getElementById('optionValue').value = '';
     });

    //======================= 업로드된 이미지 목록 보기 ==============================//

    const itemImagesInput = document.getElementById('itemImages');
    const imageFileList = document.getElementById('imageFileList');

    itemImagesInput.addEventListener('change', function(event) {
        const files = event.target.files;
        imageFileList.innerHTML = ''; // 기존 목록 초기화

        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.textContent = file.name;
            imageFileList.appendChild(li);
        }
    });

    // 폼 제출 시
    const form = document.querySelector('form');
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        let formData = new FormData(form);

        // 이미 FormData에 파일들이 포함되어 있으므로 추가 작업 불필요

        $.ajax({
            url: form.action,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    alert(response.message);
                    window.location.href = '/admin/item/list.do';
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('상품 등록 실패:', error);
                alert('상품 등록에 실패했습니다.');
            }
        });
    });
});