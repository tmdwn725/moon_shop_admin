package com.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.dto.CouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.dto.enums.Role;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    /**
     * 사용자 목록 조회
     * @param page
     * @param searchStr
     * @param model
     * @return
     */
    @GetMapping("/memberList")
    public String memberList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        Page<MemberDTO> memberList = memberService.selectMemberList(page,6, Role.USER,searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("memberList", memberList);
        return "member/memberList";
    }
    /**
     * 사용자 상세정보 조회
     * @param memberDTO
     * @param model
     * @return
     */
    @GetMapping("/memberInfo")
    public String memberInfo(MemberDTO memberDTO, Model model) {
        MemberDTO member = new MemberDTO();
        if(memberDTO.getMemberSeq() > 0){
            member = memberService.selectMember(memberDTO.getMemberSeq());
        }
        model.addAttribute("member", member);
        return "member/memberInfo";
    }

    /**
     * 비밀번호 변경
     * @param request
     * @param response
     * @param memberDTO
     * @return
     * @throws IOException
     */
    @PostMapping("/changePassword")
    public ResponseEntity<Void> changePassword(HttpServletRequest request, HttpServletResponse response, MemberDTO memberDTO) throws IOException {
        memberDTO.setNewPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberService.changeMyPassword(memberDTO);
        return ResponseEntity.ok().build();
    }
    /**
     * 사용자 정보 저장
     * @param memberDTO
     * @return
     */
    @PostMapping("/saveMember")
    public ResponseEntity<Void> saveMember(MemberDTO memberDTO) {
        if(memberDTO.getPassword() != null){
            memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        }
        memberService.saveMember(memberDTO);
        return ResponseEntity.ok().build();
    }
    /**
     * 관리자 목록 조회
     * @param page
     * @param searchStr
     * @param model
     * @return
     */
    @GetMapping("/adminList")
    public String adminList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        Page<MemberDTO> adminList = memberService.selectMemberList(page,6, Role.ADMIN,searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("adminList", adminList);
        return "member/adminList";
    }
}
