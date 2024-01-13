// 사용자 정보 삭제
function removeMember(memberSeq) {
    if(confirm("사용자 정보를 삭제하시겠습니까?")){
        $.ajax({
            url: "/member/removeMember",
            type: "delete",

            data: {memberSeq : memberSeq},
            success: function(response){
                alert("삭제되었습니다.");
                window.location.href = "http://localhost:8082/member/adminList";
            },
            error: function(xhr, status, error) {
                alert("삭제에 실패했습니다.");
            }
        });
    }
}