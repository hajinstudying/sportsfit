package com.sportsfit.shop.security.handler;


import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 소셜 로그인 성공 후처리 담당 클래스
 */
@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    // 비밀번호 인코딩 객체 의존성 주입
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        //log.info("CustomLoginSuccessHandler authentication : " + authentication);
        //log.info("CustomLoginSuccessHandler authentication.getPrincipal() : " + authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getPassword();

        //소셜로그인(true)이고 회원의 패스워드가 1111 이라면
        if (memberSecurityDTO.isSocial()
                &&
            (memberSecurityDTO.getPassword().equals("1111") || passwordEncoder.matches("1111", memberSecurityDTO.getPassword())
        )) {
            log.info("CustomLoginSuccessHandler authentication 소셜 로그인 성공 후처리");
            // 회원정보 변경화면으로 이동해서 일반 회원으로 전환 유도
            response.sendRedirect("/");
            return;
        } else {
            response.sendRedirect("/");
        }
    }
}
