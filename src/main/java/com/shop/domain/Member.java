package com.shop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.dto.enums.Role;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
public class Member {
    @Id
    @Column(name ="member_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "email")
    private String email;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "address")
    private String address;
    @Column(name = "detail_address")
    private String detailAddress;
    @Column(name = "tel_no")
    private String telNo;
    @Column(name = "join_date")
    private LocalDateTime joinDate;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonManagedReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberCoupon> memberCouponList;
    public void createMember(Long memberSeq, LocalDateTime nowDate, String ... member){
        if(memberSeq > 0L) {
            this.memberSeq = memberSeq;
            this.joinDate = nowDate;
        }
        this.memberId = member[0];
        this.name = member[1];
        this.password = member[2];
        this.nickName = member[3];
        this.email = member[4];
        this.role = Role.ADMIN;
    }
}
