package com.shop.repository.custom;

import com.shop.domain.Member;
import com.shop.dto.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberConfig {
    Member findByMemberId(String memberId);
    Page<Member> selectMemberPage(Pageable pageable, Role role, String searchStr);
}
