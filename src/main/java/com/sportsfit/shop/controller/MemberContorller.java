package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.MemberService;
import com.sportsfit.shop.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberContorller {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 화면
     */
    @GetMapping(value = "/join")
    public String memberForm(Model model) {
        model.addAttribute("memberVo", new MemberVo());
        return "member/join";
    }

    /**
     * 로그인 화면
     */
    @GetMapping(value = "/login")
    public String loginForm(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/login";
    }

    // 로그인 성공 후 리다이렉트


}
