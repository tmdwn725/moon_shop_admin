package com.shop.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.Coupon;
import com.shop.domain.Member;
import com.shop.domain.QMember;
import com.shop.dto.enums.Role;
import com.shop.repository.custom.MemberConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberConfig {
    private final JPAQueryFactory queryFactory;
    QMember qMember = QMember.member;
    /**
     * ID로 사용자 조회
     * @param memberId
     * @return
     */
    public Member findByMemberId(String memberId) {
        return queryFactory.selectFrom(qMember)
                .where(qMember.memberId.eq(memberId).and(qMember.role.eq(Role.ADMIN)))
                .fetchOne();
    }

    /**
     * 사용자 목록 조회
     * @param pageable
     * @param role
     * @param searchStr
     * @return
     */
    public Page<Member> selectMemberPage(Pageable pageable, Role role, String searchStr){
        QueryResults<Member> memberList = queryFactory
                .selectFrom(qMember)
                .where(qMember.role.eq(role).and(searchMemberName(searchStr)))
                .orderBy(qMember.joinDate.desc()) // 최신순으로 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // 최대 8개만 조회
                .fetchResults();
        List<Member> content = memberList.getResults();
        long total = memberList.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    /**
     * 이름 검색
     * @param searchStr
     * @return
     */
    private BooleanExpression searchMemberName(String searchStr) {
        if (searchStr == null) {
            return null;
        }
        return qMember.name.contains(searchStr);
    }

    /**
     * 비밀번호 변경
     * @param memberId
     * @param newPassword
     */
    public void updatePassword(String memberId, String newPassword) {
        queryFactory.update(qMember)
                .set(qMember.password,newPassword)
                .where(qMember.memberId.eq(memberId))
                .execute();
    }
    /**
     * 사용자 정보 수정
     * @param member
     */
    public void updateMember(Member member){
        queryFactory.update(qMember)
                .set(qMember.name,member.getName())
                .set(qMember.nickName, member.getNickName())
                .set(qMember.email, member.getEmail())
                .set(qMember.password, member.getPassword())
                .where(qMember.memberSeq.eq(member.getMemberSeq()))
                .execute();
    }
}
