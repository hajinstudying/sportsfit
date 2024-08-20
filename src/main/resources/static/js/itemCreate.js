$(document).ready(function() {

    // 하위 카테고리 불러오기
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

    // 이미지 미리보기 및 대표이미지 설정
    document.getElementById('itemImages').addEventListener('change', function(event) {
        const imagePreviewContainer = document.getElementById('imagePreviewContainer');

        Array.from(event.target.files).forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(e) {
                // 이미지 요소 생성
                const img = document.createElement('img');
                img.src = e.target.result;
                img.alt = file.name;
                img.style.width = '100px';
                img.style.height = '100px';
                img.style.margin = '5px';

                // 파일 이름 요소 생성
                const fileName = document.createElement('div');
                fileName.style.textAlign = 'center';
                fileName.style.fontSize = '12px';
                fileName.style.marginTop = '5px';

                // 라디오 버튼 생성
                const radioButton = document.createElement('input');
                radioButton.type = 'radio';
                radioButton.name = 'repImgIndex'; // 같은 이름으로 묶어 하나만 선택 가능
                radioButton.value = index; // 인덱스를 값으로 사용
                radioButton.style.marginLeft = '5px';

                // 라벨 생성
                const label = document.createElement('label');
                label.textContent = file.name;
                label.style.marginLeft = '5px';

                // 삭제 버튼 생성
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'X';
                deleteButton.style.color = 'red';
                deleteButton.style.border = 'none';
                deleteButton.style.background = 'none';
                deleteButton.style.cursor = 'pointer';
                deleteButton.style.position = 'absolute'; // 절대 위치 지정
                deleteButton.style.top = '0'; // 상단 위치
                deleteButton.style.right = '0'; // 우측 위치

                // 삭제 버튼 클릭 시 이미지 제거
                deleteButton.addEventListener('click', function() {
                    imagePreviewContainer.removeChild(container); // 컨테이너 제거
                });

                // 이미지와 파일 이름, 라디오 버튼, 삭제 버튼을 포함할 컨테이너 생성
                const container = document.createElement('div');
                container.style.position = 'relative'; // 상대 위치 지정
                container.style.display = 'inline-block';
                container.style.textAlign = 'center';
                container.style.margin = '10px';

                // 파일 이름 요소에 라디오 버튼과 라벨 추가
                fileName.appendChild(radioButton);
                fileName.appendChild(label);

                // 컨테이너에 이미지, 파일 이름, 삭제 버튼 추가
                container.appendChild(deleteButton); // 삭제 버튼 먼저 추가
                container.appendChild(img);
                container.appendChild(fileName);

                // 미리보기 컨테이너에 추가
                imagePreviewContainer.appendChild(container);
            };
            reader.readAsDataURL(file);
        });
    });
});