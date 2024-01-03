// 이미지 클릭시 파일 버튼 클릭
const upload = document.querySelector('.upload');
const upload1 = document.querySelector('.upload1');
const upload2 = document.querySelector('.upload2');
const upload3 = document.querySelector('.upload3');

const realUpload = document.querySelector('.real-upload');
const realUpload1 = document.querySelector('.real-upload1');
const realUpload2 = document.querySelector('.real-upload2');
const realUpload3 = document.querySelector('.real-upload3');

realUpload.addEventListener('change', getImageFiles);
realUpload1.addEventListener('change', getImageFiles);
realUpload2.addEventListener('change', getImageFiles);
realUpload3.addEventListener('change', getImageFiles);

upload.addEventListener('click', () => realUpload.click());
upload1.addEventListener('click', () => realUpload1.click());
upload2.addEventListener('click', () => realUpload2.click());
upload3.addEventListener('click', () => realUpload3.click());

function getImageFiles(e) {
    const uploadFiles = [];
    const file = e.currentTarget.files[0];
    const imageId = this.id;

    // 파일 타입 검사
    if (!file.type.match("image/.*")) {
        alert('이미지 파일만 업로드가 가능합니다.');
        return
    }

    uploadFiles.push(file);
    const reader = new FileReader();
    reader.onload = (e) => {
        addImageFile(imageId.replace('file','upload'),e, file);
    };
    reader.readAsDataURL(file);
}

// 이미지 파일 추가
function addImageFile(id, e, file) {
    const div = document.getElementById(id);
    const imgElements = div.querySelector('img'); // div 요소 안의 모든 img 요소를 선택합니다.

    // 이미지가 이미 존재하는지 확인합니다.
    if (imgElements != null) {
        // 이미지의 src 속성을 변경합니다.
        $(imgElements).attr('src', e.target.result);
        $(imgElements).attr('data-file', file.name);
    } else {
        // 이미지가 없으면 새로 생성하여 추가합니다.
        const img = document.createElement('img');
        img.setAttribute('src', e.target.result);
        img.setAttribute('class', "product-image");
        img.setAttribute('width', "200px");
        img.setAttribute('data-file', file.name);
        div.appendChild(img);
    }
}

// Selectbox 이벤트 핸들러
function onProductTypeChange(value) {
    const options = $('#product-detail-type option');

    for(var i = 0; i < options.length; i++) {
        const option = options[i];
        const parentCategory = option.getAttribute('name');

        if (parentCategory === value) {
            option.style.display = 'block';
        }else {
            option.style.display = 'none';
        }
    }

    const selectBox = $('#product-detail-type'); // Selectbox의 ID를 선택

    // 가장 위에 있는 옵션을 선택
    selectBox.prop('selectedIndex', 0);
}
document.getElementById('btn-save').addEventListener('click', function(e){
    let seq = document.getElementById('product-seq').value;
    if(seq == ''){
       seq = 0;
    }
    const name = document.getElementById('product-name').value;
    const content = document.getElementById('product-content').value;
    const price = document.getElementById('product-price').value;
    const type = document.getElementById('product-type').value;
    const detailType = document.getElementById('product-detail-type').value;
    const imageForm = document.getElementById('product-image-form');
    let formData = new FormData(imageForm);

    let sizeType = {};

    formData.append("productSeq", seq);
    formData.append("productName", name);
    formData.append("productContent", content);
    formData.append("price", price);
    formData.append("productTypeCd", detailType);

    // document.getElementsByName은 컬렉션을 반환하므로 배열로 변환하여 사용
    const sizeList = Array.from(document.getElementsByName('product-size'));

    sizeList.forEach(function(e) {
        const id = e.id;
        const value = e.value;
        formData.append(`sizeTypes[${id}]`, value);
    });

    formData.append("sizeMap", JSON.stringify(sizeType)); // 객체를 문자열로 변환하여 FormData에 추가

    $.ajax({
        type: "POST",
        url: "/product/saveProductInfo",
        contentType: false, // 필수: FormData를 사용하기 때문에 false로 설정
        processData: false, // 필수: FormData를 사용하기 때문에 false로 설정
        data:formData,
        success: function(response){
            console.log("등록");
            alert("저장되었습니다.");
            window.location.href = "http://localhost:8082/product/productList";
        },
        error: function(xhr, status, error) {
            alert("저장에 실패했습니다.");
        }
    });


});

// 사이즈 추가
document.getElementById('add-size').addEventListener('click',function(e){
   const sizeNameInput = document.getElementById("size-name");
   const sizeName = sizeNameInput.value.trim();
   sizeNameInput.value = '';

   if (sizeName === '') {
       alert("사이즈명을 입력해주세요.");
       return;
   }

   if(document.getElementById(sizeName)){
       alert("동일한 사이즈가 존재합니다.");
       return;
   }

   const sizeListDiv = document.getElementById("sizeList");
   const newEntry = document.createElement("div");
   newEntry.style.marginLeft = "10px";
   newEntry.style.display = "inline-block";

   // 새로운 사이즈명 텍스트 생성
   const sizeNameText = document.createElement("span");
   sizeNameText.innerText = sizeName + ': ';
   newEntry.appendChild(sizeNameText);

   // 사이즈 개수를 입력할 수 있는 input 생성
   const quantityInput = document.createElement("input");
   quantityInput.type = "number";
   quantityInput.classList.add("product-size");
   quantityInput.name="product-size"
   quantityInput.id=sizeName
   quantityInput.min = "0";
   quantityInput.max = "100";
   newEntry.appendChild(quantityInput);

   // 삭제 버튼 생성
   const removeButton = document.createElement('button');
   removeButton.textContent = '삭제';
   removeButton.classList.add("btn");
   // 버튼에 onclick 이벤트 처리기 추가
   removeButton.onclick = function() {
     // 이벤트 핸들러에서 인자를 변수로 사용하려면 클로저를 이용한다.
     handleClick(sizeName);
   };

   // 버튼을 버튼 컨테이너에 추가
   newEntry.appendChild(removeButton);
   sizeListDiv.appendChild(newEntry);
});

// 상품 재고 삭제
const delSize = document.querySelectorAll('.del-size');
delSize.forEach((target) => target.addEventListener("click", function(e){
    if(confirm("재고를 삭제하시겠습니까?")){
        const productSize = e.target.getAttribute('data-parameter');
        const productSeq = document.getElementById('product-seq').value;
         $.ajax({
            url: "/product/removeProductStock",
            type: "DELETE",
            data:{
                productSeq: productSeq,
                sizeType: productSize
            },
            success: function(response) {
                alert("삭제하였습니다.");
                location.reload(true);
             },
            error: function(xhr, status, error) {
                alert("삭제에 실패했습니다.");
            }
        });
    }
}));
