// join.html의 자바스크립트

document.addEventListener('DOMContentLoaded', function() {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    //console.log('CSRF Token:', csrfToken);
    //console.log('CSRF Header:', csrfHeader);

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


    /**********************************************************
                    `    비밀번호 동일설 검증
    ***********************************************************/
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

    /************************************************************
                 각 요소에 입력이 들어가는 경우 에러 메세지 제거
    *************************************************************/
     if (nameInput && nameError) {
        nameInput.addEventListener('input', function() {
            nameError.textContent = '';
        });
    }
    if (emailInput && emailError) {
        emailInput.addEventListener('input', function() {
                emailError.textContent = '';
        });
    }
    if (emailInput && emailDupleError) {
        emailInput.addEventListener('input', function() {
                emailDupleError.textContent = '';
        });
    }
    if (passwordInput && passwordError) {
        passwordInput.addEventListener('input', function() {
                passwordError.textContent = '';
        });
    }
    if (pwConfirmInput && pwConfirmError) {
        pwConfirmInput.addEventListener('input', function() {
                pwConfirmError.textContent = '';
        });
    }

    /************************************************************
                        이메일 중복 검사
    *************************************************************/
    $('#emailDupleCheckBtn').click(function() {
        checkEmailDuplicate();
    });

    // 중복 검사 메소드 (제이쿼리 사용)
    function checkEmailDuplicate() {
        var email = $('#emailInput').val();
        //console.log("이메일 중복검사 시작", email);

        $.ajax({
            url: '/mail/check.do/' + email,
            type: 'GET',
            data: {email: email},
            success: function(isDuplicate) {
                //console.log("이메일 중복검사 결과 : ", isDuplicate);
                if (isDuplicate) {
                    $('#emailDupleError').removeClass('text-success');
                    $('#emailDupleError').addClass('text-danger');
                    $('#emailDupleError').text('이미 사용 중인 이메일입니다.');
                } else {
                    $('#emailDupleError').addClass('text-success');
                    $('#emailDupleError').removeClass('text-danger');
                    $('#emailDupleError').text('이 이메일은 사용 가능합니다.');
                }
            },
            error: function() {
                $('#emailError').text('오류가 발생했습니다. 다시 시도해주세요.')
            }
        });
    }

    /************************************************************
                            이메일 구글 인증
    *************************************************************/
    function sendVerificationCode() {
        const email = emailInput.value;
        const verificationCode = document.getElementById("verificationCode");
        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        // CSRF 토큰 값을 가져옴
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
        //console.log('csrfToken : ', csrfToken);
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

        $.ajax({
            url: "/mail/send.do/" + email,
            type: "POST",
            //data: { email: email },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 헤더에 포함
            },
            success: function(response) {
                alert("인증번호가 전송되었습니다.");
                verificationCode.disabled = false;
                verificationCode.dataset.code = response; // 서버에서 받은 인증번호 저장
            },
            error: function() {
                alert("인증번호 전송에 실패했습니다. 다시 시도해주세요.");
            }
        });
    }

    // 인증번호 확인
    function verifyCode() {
        const userCode = document.getElementById("verificationCode").value;

        console.log('유저가 입력한 코드 : ', userCode);

        const actualCode = document.getElementById("verificationCode").dataset.code;

        console.log('실제 인증코드 : ', actualCode);

        if (userCode === actualCode) {
            alert("이메일 인증이 완료되었습니다.");
            document.getElementById("verification-error").style.display = "none";
            document.getElementById("submitBtn").disabled = false; // 가입 버튼 활성화
        } else {
            document.getElementById("verification-error").style.display = "block";
        }
    }

    // 함수를 전역으로 사용할 수 있도록 window 객체에 할당
    window.sendVerificationCode = sendVerificationCode;
    window.verifyCode = verifyCode;

});