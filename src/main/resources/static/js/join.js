// join.html의 자바스크립트

document.addEventListener('DOMContentLoaded', function() {

    // 요소 가져오기
    const form = document.querySelector('form');
    const nameInput = document.getElementById('nameInput');
    const emailInput = document.getElementById('emailInput');
    const passwordInput = document.getElementById('passwordInput');
    const pwConfirmInput = document.getElementById('pwConfirmInput');

    const nameError = document.getElementById('nameError');
    const emailError = document.getElementById('emailError');
    const passwordError = document.getElementById('passwordError');
    const pwConfirmError = document.getElementById('pwConfirmError');
    const passwordMatchError = document.getElementById('passwordMatchError');


    // 비밀번호 동일설 검증 메소드
    function checkPasswordMatch() {

        // 비밀번호를 입력하지 않은 경우 메세지 삭제
        if (pwConfirmInput.value === '') {
                    passwordMatchError.classList.remove('text-success', 'text-danger');
                    passwordMatchError.textContent = '';

        // 비밀번호 불일치의 경우
        } else if (passwordInput.value !== pwConfirmInput.value) {
            passwordMatchError.classList.remove('text-success');
            passwordMatchError.classList.add('text-danger');
            passwordMatchError.textContent = '비밀번호가 일치하지 않습니다.';

        // 비밀번호 일치 경우
        } else {
            passwordMatchError.classList.remove('text-danger');
            passwordMatchError.classList.add('text-success');
            passwordMatchError.textContent = '비밀번호가 일치합니다.';
        }
    }

    // 비밀번호 확인란 입력시 동일성 검증 메소드 실행
    pwConfirmInput.addEventListener ('input', checkPasswordMatch);

    // 비밀번호 불일치시 폼 제출 막기
    form.addEventListener('submit', function(event) {
        if (passwordInput.value !== pwConfirmInput.value) {
                    event.preventDefault();
        }
    });

    // 각 요소에 입력이 들어가는 경우 에러 메세지 제거
    nameInput.addEventListener('input', function() {
        nameError.textContent = '';
    });
    emailInput.addEventListener('input', function() {
            emailError.textContent = '';
    });
    passwordInput.addEventListener('input', function() {
            passwordError.textContent = '';
    });
    pwConfirmInput.addEventListener('input', function() {
            pwConfirmError.textContent = '';
    });

});