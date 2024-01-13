package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Member;
import com.shop.dto.MemberDTO;
import com.shop.dto.enums.Role;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username);
        return User.builder()
                .username(member.getMemberId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }

    /**
     * 사용자 상세 정보 조회
     * @param memberSeq
     * @return
     */
    public MemberDTO selectMember(Long memberSeq) {
        MemberDTO dto = ModelMapperUtil.map(memberRepository.findById(memberSeq).get(), MemberDTO.class);
        return dto;
    }
    /**
     * id로 member 정보 조회
     * @param id
     * @return
     */
    public MemberDTO selectMemberById(String id) {
        MemberDTO dto = ModelMapperUtil.map(memberRepository.findByMemberId(id), MemberDTO.class);
        return dto;
    }

    /**
     * 사용자 목록 조회
     * @param start
     * @param limit
     * @param role
     * @param searchStr
     * @return
     */
    public Page<MemberDTO> selectMemberList(int start, int limit, Role role, String searchStr) {
        PageRequest pageRequest = PageRequest.of(start-1, limit);
        Page<Member> result = memberRepository.selectMemberPage(pageRequest, role, searchStr);
        int total = result.getTotalPages();
        pageRequest = PageRequest.of((total-1), limit);
        List<MemberDTO> list = ModelMapperUtil.mapAll(result.getContent(), MemberDTO.class);
        return new PageImpl<>(list, pageRequest, total);
    }

    /**
     * 비밀번호 변경
     * @param memberDTO
     */
    public void changeMyPassword(MemberDTO memberDTO) {
        memberRepository.updatePassword(SecurityContextHolder.getContext().getAuthentication().getName(), memberDTO.getNewPassword());
    }
    /**
     * 사용자 정보 수정
     * @param memberDTO
     */
    public void saveMember(MemberDTO memberDTO){
        LocalDateTime nowDate = LocalDateTime.now();
        Member member = new Member();
        member.createMember(nowDate, "", memberDTO.getName(), memberDTO.getPassword(), memberDTO.getNickName(), memberDTO.getEmail());
        if(memberDTO.getMemberSeq() > 0){
            memberRepository.updateMember(member);
        }else {
            memberRepository.save(member);
        }
    }
}
