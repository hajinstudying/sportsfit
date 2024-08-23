// csrf 토큰
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');


// Axios 인스턴스 생성 및 기본 설정
const axiosInstance = axios.create({
    headers: {
        [csrfHeader]: csrfToken,
        'Content-Type': 'application/json'
    }
});

async function uploadToServer (formObj) {
    console.log("upload to server......")
    try {
        const response = await axiosInstance({
            method: 'post',
            url: '/upload',
            data: formObj,
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        console.log('여기는 upload.js', response.data)
        return response.data;
    } catch (error) {
        console.error('업로드 실패 :', error);
        throw error;
    }
}

async function removeFileToServer(uuid, fileName){
    try {
        const response = await axiosInstance.delete(`/remove/${uuid}_${fileName}`);
        return response.data;
    } catch (error) {
        console.error('파일 제거 실패 :', error);
        throw error;
    }
}