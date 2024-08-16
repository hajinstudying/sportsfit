package com.sportsfit.shop.controller;

import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.service.MemberServiceImpl;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberContorller {

    private final MemberServiceImpl memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 화면
     */
    @GetMapping(value = "/join.do")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/join";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/join.do")
    public String registerMember(@Valid @ModelAttribute("memberFormDto") MemberFormDto memberFormDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("회원가입 데이터 검증 오류 있음");

            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMap.put(error.getField(), error.getDefaultMessage())
            );
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            redirectAttributes.addFlashAttribute("memberFormDto", memberFormDto);
            return "redirect:/member/join.do";
        }

        try {

            RoleVo role = new RoleVo();
            role.setRoleId(2L); // 기본 권한 ROLE_USER의 role_id를 설정
            role.setRoleName("USER"); // 기본 권한 설정

            MemberVo member = MemberVo.builder()
                    .password(passwordEncoder.encode(memberFormDto.getPassword()))
                    .name(memberFormDto.getName())
                    .email(memberFormDto.getEmail())
                    // 권한은 기본 권한으로 빌더패턴으로 생성
                    .roles(List.of(role))
                    .build();

            memberService.saveMember(member);

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/member/join.do";
        }

        return "redirect:/member/login.do";
    }

    /**
     * 로그인 화면
     */
    @GetMapping(value = "/login.do")
    public String loginForm(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/login";
    }

    // 로그인 성공 후 리다이렉트


}
