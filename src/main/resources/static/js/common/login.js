function login(){
    const id = document.getElementById("memberId").value.trim();
    const password = document.getElementById("password").value.trim();
    if(id == '') {
        alert('ID를 입력해주세요');
    }

    if(password == '') {
        alert('비밀번호를 입력해주세요');
    }
    document.getElementById('member-form').submit();
}
