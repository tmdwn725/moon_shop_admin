package com.shop.controller;

import com.shop.dto.CouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.dto.enums.Role;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
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
        return "member/memberInfo";
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
