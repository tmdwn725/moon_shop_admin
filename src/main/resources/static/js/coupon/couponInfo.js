// 쿠폰 정보 저장
document.getElementById('coupon-save-btn').addEventListener('click', function(){

    const param = {
        couponSeq : document.getElementById('coupon-seq').value,
        couponName : document.getElementById('coupon-name').value,
        discRate : document.getElementById('disc-rate').value,
        useStDateStr : document.getElementById('coupon-use-st-date').value,
        useEdDateStr : document.getElementById('coupon-use-ed-date').value,
        minPrice : document.getElementById('min-price').value,
        maxDiscPrice : document.getElementById('max_disc_price').value
    }

     $.ajax({
        type: "POST",
        url: "/coupon/saveCouponInfo",
        data: param,
        success: function(response){
            console.log("등록");
            alert("저장되었습니다.");
            window.location.href = "http://localhost:8082/coupon/getCouponInfo?couponSeq" + couponSeq;
        },
        error: function(xhr, status, error) {
            alert("저장에 실패했습니다.");
        }
    });
});

// 쿠폰 사용자 지정
document.getElementById('member-coupon-save-btn').addEventListener('click', function(){
    const memberSeqArray = [];
    const checkedMemberSeq = document.getElementsByName("member-seq");

    let unCheckedCnt = 0;
    // 취득한 속성 만큼 루프
    for (let i = 0; i < checkedMemberSeq.length; i++) {
      // 속성중에 체크 된 항목이 있을 경우
      if (checkedMemberSeq[i].checked == true) {
        const memberSeq = checkedMemberSeq[i].value;
        if(!document.getElementById("member-seq-"+memberSeq)){
            memberSeqArray.push(memberSeq);
        } else {
            checkedMemberSeq[i].checked = false;
            unCheckedCnt++;
        }
      }
    }

    if(unCheckedCnt > 0) {
        alert("쿠폰이 이미 발급된 "+ unCheckedCnt +"명의 사용자는 체크박스가 취소 됩니다.");
    }

    const param = {
        couponSeq : document.getElementById('coupon-seq').value,
        memberSeqArray : memberSeqArray
    }

    $.ajax({
        type: "POST",
        url: "/coupon/saveMemberCoupon",
        data: param,
        success: function(response){
            alert("발급되었습니다.");
            window.location.href = "http://localhost:8082/coupon/getCouponInfo?couponSeq" + couponSeq;
        },
        error: function(xhr, status, error) {
            alert("발급에 실패했습니다. 다시 시도해주세요");
        }
    });
});

document.getElementById('member-coupon-remove-btn').addEventListener('click', function() {
    let memberCouponSeqList = [];
    const checkedMemberSeq = document.getElementsByName("member-seq");
    let unCheckedCnt = 0;
    // 취득한 속성 만큼 루프
    for (let i = 0; i < checkedMemberSeq.length; i++) {
      // 속성중에 체크 된 항목이 있을 경우
      if (checkedMemberSeq[i].checked == true) {
        const memberSeq = checkedMemberSeq[i].value;
        if(document.getElementById("member-seq-"+memberSeq)){
            memberCouponSeqList.push(document.getElementById("member-seq-"+memberSeq).value);
        } else {
            checkedMemberSeq[i].checked = false;
            unCheckedCnt++;
        }
      }
    }

    $.ajax({
        type: "DELETE",
        url: "/coupon/removeMemberCoupon",
        contentType: "application/json",
        data: JSON.stringify(memberCouponSeqList),
        success: function(response){
            alert("발급취소되었습니다.");
            window.location.href = "http://localhost:8082/coupon/getCouponInfo?couponSeq" + couponSeq;
        },
        error: function(xhr, status, error) {
            alert("발급취소에 실패했습니다. 다시 시도해주세요");
        }
    });
});


// 각 탭 아이템에 클릭 이벤트 추가
document.querySelectorAll('#tabs .tab-link').forEach(function(tab) {
  tab.addEventListener('click', function() {
    // 모든 탭에서 'tab-on' 클래스 제거
    tabs.forEach(function(tab) {
      tab.classList.remove('current');
    });

    // 클릭한 탭에만 'tab-on' 클래스 추가
    this.classList.add('current');
  });
});
