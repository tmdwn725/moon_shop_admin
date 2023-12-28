package com.shop.dto;

import com.shop.dto.enums.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDTO {
    private long memberSeq;
    private String memberId;
    private String name;
    private String nickName;
    private String email;
    private String address;
    private String detailAddress;
    private String telNo;
    private Role role;
    private String newPassword;
    private String password;
    private String authority;
    private List<MemberCouponDTO> memberCouponList = new ArrayList<>();
    private String myCouponYn;
}
