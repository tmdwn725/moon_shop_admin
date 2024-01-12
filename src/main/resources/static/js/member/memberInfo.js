// 비밀번호 변경
document.getElementById('btn-change-password').addEventListener('click', function(e){
    const password = document.getElementById('new-password').value
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password === '') {
        alert('현재 비밀번호를 입력해주세요.');
        return false;
    }

    if (password.length < 4) {
        alert('비밀번호 4자 이상이여야합니다.');
        return false;
    }

    if (password !== confirmPassword) {
        alert('신규 비밀번호와 재입력 비밀번호가 같지 않습니다.');
        document.getElementById('confirm-password').value = '';
        document.getElementById('new-password').value = '';
        return false;
    }

    $.ajax({
        type: "post",
        url: "/member/changePassword",
        data:{"password" : password },
        success: function(response){
            alert("저장되었습니다.");
            window.location.href = "http://localhost:8082/member/memberInfo?memeberSeq=" + memberSeq;
        },
        error: function(xhr, status, error) {
            alert("저장에 실패했습니다.");
        }
    });

});
// 사용자 정보 저장
document.getElementById('btn-save').addEventListener('click', function(e){
    const param = {
        memberSeq : document.getElementById('member-seq').value,
        email : document.getElementById("email").value,
        name : document.getElementById("member-name").value,
        nickName : document.getElementById("nick-name").value
    }

    if (!isValidEmail(param.email)) {
        alert('이메일 주소가 올바르지 않습니다.');
        return false;
    }

    $.ajax({
        type: "post",
        url: "/member/saveMember",
        cdata: param,
        success: function(response){
            alert("저장되었습니다.");
            window.location.href = "http://localhost:8082/member/memberInfo?memeberSeq=" + memberSeq;
        },
        error: function(xhr, status, error) {
            alert("저장에 실패했습니다.");
        }
    });
});

// 이메일 체크
function isValidEmail(email) {
    const emailRegex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return emailRegex.test(email);
}