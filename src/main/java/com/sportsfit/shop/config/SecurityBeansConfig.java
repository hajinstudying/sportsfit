package com.sportsfit.shop.config;

import com.sportsfit.shop.security.handler.CustomAccessDeniedHandler;
import com.sportsfit.shop.security.handler.CustomSocialLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 순환참조로 인해서 SecurityConfig 에서 분리, Bean 들은 여기서 등록
 *  - @Configuration : 앱 구동시 먼저 로딩해서 처리
 *  - PasswordEncoder, CustomSocialLoginSuccessHandler,
 *    CustomAccessDeniedHandler 빈 생성 역할
 */
@Configuration
public class SecurityBeansConfig {

    // 패스워드 인코딩 관련 빈
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 소셜 로그인 성공후 처리 객체 Bean 생성, 명시적으로 이름을 부여함,
    // 이름 생략하면 자동으로 메소드명이 빈 이름이됨.
    @Bean(name = "customSocialLoginSuccessHandler")
    public AuthenticationSuccessHandler customSocialLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        return new CustomSocialLoginSuccessHandler(passwordEncoder);
    }


    // 권한이 없어서 특정 메뉴에 못들어갈 때 사용하는 객체 Bean 생성
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}