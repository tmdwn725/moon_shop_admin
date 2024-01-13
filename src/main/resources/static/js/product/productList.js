// 상품삭제
function removeProduct(productSeq) {
    if (confirm("상품 정보를 삭제하시겠습니까?")) {
        $.ajax({
            url: "/product/removeProduct",
            type: "DELETE",
            data:{ productSeq : productSeq },
            success: function(response) {
              alert("삭제에 되었습니다.");
              // 성공한 경우 해당 페이지로 리다이렉트
              window.location.href = "http://localhost:8082/product/productList";
            },
            error: function(xhr, status, error) {
                alert("삭제에 실패했습니다.");
            }
        });
    }
}