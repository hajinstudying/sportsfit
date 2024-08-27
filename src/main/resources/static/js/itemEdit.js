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

     let optionCount = document.querySelectorAll('.option-group').length;;
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

    //======================= 이미지 업로드 관련 ==============================//

    // 업로드 모달창 띄우기
    const uploadModal = new bootstrap.Modal(document.querySelector(".uploadModal"))

    document.querySelector(".uploadFileBtn").addEventListener("click", function(e){
        e.stopPropagation();
        e.preventDefault();
        uploadModal.show();
    }, false);

    // 모달창에서 파일 업로드
    document.querySelector(".uploadBtn").addEventListener("click", function(e){
        const formObj = new FormData();

        const fileInput = document.querySelector("input[name='files']")

        console.log(fileInput.files)

        const files = fileInput.files
        for (let i = 0; i < files.length; i++) {
          formObj.append("files", files[i]);
        }

        console.log('formObj', formObj)

        uploadToServer(formObj).then(result => {
          console.log('result : ', result);
          for (const uploadResult of result) {
            console.log('uploadResult', uploadResult);
            showUploadFile(uploadResult);
          }
          uploadModal.hide();
        }).catch(e => {
          console.error('업로드 실패:', e);
          uploadModal.hide();
        })

    },false)

    const uploadResult = document.querySelector(".uploadResult");

    // 섬네일을 보여주는 함수
    function showUploadFile({uuid, fileName, link}){

        const str =`<div class="card col-4">
          <div class="card-header d-flex justify-content-center">
              ${fileName}
              <button class="btn-sm btn-danger" onclick="javascript:removeFile('${uuid}', '${fileName}', this)" >X</button>
          </div>
          <div class="card-body">
               <img src="/view/${link}" data-src="${uuid + "_" + fileName}" >
          </div>
        </div><!-- card -->`;

        uploadResult.innerHTML += str;
    }


    // 이미지 삭제 함수
    function removeFile(uuid,fileName, obj){

        console.log(uuid);
        console.log(fileName);
        console.log(obj);

        const targetDiv = obj.closest(".card");

        removeFileToServer(uuid, fileName).then(data => {
            targetDiv.remove();
        });
    }

    // 폼 제출시 fileNames 추가
    const itemForm = document.getElementById('itemForm');
    const submitBtn = document.getElementById('submitBtn');

    submitBtn.addEventListener("click", function(e){
        e.preventDefault();
        e.stopPropagation();

        // 파일 이름을 hidden 필드로 추가
        const target = document.querySelector(".uploadHidden");
        const uploadFiles = uploadResult.querySelectorAll("img");

        let str = '';
        for (let i = 0; i < uploadFiles.length ; i++) {
            const uploadFile = uploadFiles[i]
            const imgLink = uploadFile.getAttribute("data-src")
            str += `<input type='hidden' name='fileNames' value="${imgLink}">`
        }
        target.innerHTML = str;

       itemForm.submit();
    });

    // itemEditForm의 기존 섬네일 이미지 관련 자바스크립트
    // 이미지 삭제 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.remove-file').forEach(button => {
        button.addEventListener('click', function() {
            const uuid = this.getAttribute('data-uuid');
            const fileName = this.getAttribute('data-filename');
            removeFile(uuid, fileName, this);
        });
    });

    // 기존 옵션 삭제 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.remove-option').forEach(button => {
        button.addEventListener('click', function() {
            this.closest('.option-group').remove();
        });
    });
});




