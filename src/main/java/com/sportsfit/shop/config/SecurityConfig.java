package com.sportsfit.shop.config;

import com.sportsfit.shop.handler.AuthFailureHandler;
import com.sportsfit.shop.handler.AuthSucessHandler;
import com.sportsfit.shop.security.CustomOAuth2UserService;
import com.sportsfit.shop.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * [스프링 시큐리티 환경설정 파일]
 *  - @Configuration : 앱 구동시 먼저 로딩해서 처리되는 환경설정 파일임을 알림.
 *  - 일반 로그인과 소셜 로그인을 동시에 구현하기 때문에 로그인 성공후에
 *    처리를 담당할 의존성이 두 개가 주입됨. 둘다 AuthenticationSuccessHandler
 *    구현체이기 때문에 그 둘을 각각 구분해줘야 함.
 *
 *    명시적인 이름으로 의존성을 주입해줌(@Qualifier(이름))
 */
@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {
	/* 멤버 변수 */
	// 실제로 인증을 진행하는 객체, loadUserByUsername 메소드 보유
	private final MemberServiceImpl memberService;
	// 일반 로그인 성공 후처리 담당 객체
	private final AuthSucessHandler authSuccessHandler;
	// 로그인 실패 후처리 담당 객체
	private final AuthFailureHandler authFailureHandler;
	// 소셜 로그인 성공 후처리 담당객체
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	// 권한이 없는 페이지에 접근시 처리 객체
	private final AccessDeniedHandler accessDeniedHandler;
	// 비밀번호 암호화 담당 객체
	private final PasswordEncoder passwordEncoder;

	// 생성자 의존성 주입
	public SecurityConfig(
			MemberServiceImpl memberService,
			AuthSucessHandler authSuccessHandler,
			AuthFailureHandler authFailureHandler,
			AccessDeniedHandler accessDeniedHandler,
			PasswordEncoder passwordEncoder,
			@Qualifier("customSocialLoginSuccessHandler")
			AuthenticationSuccessHandler authenticationSuccessHandler) {

		this.memberService = memberService;
		this.authSuccessHandler = authSuccessHandler;
		this.authFailureHandler = authFailureHandler;
		this.accessDeniedHandler = accessDeniedHandler;
		this.passwordEncoder = passwordEncoder;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	/**
	 * [인증 관련 설정과 인가 관련 설정]
	 *  1. 인증 : 데이터베이스에 사용자 조회
	 *  2. 인가 : 인증된 사용자가 현재 페이지에 접속권한이 있는지 비교
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
		/**
		 * [인증을 어떻게 진행할지 설정]
		 *  - 스프링 시큐리티에게 사용자 정보를 어떻게 로드하고, 사용자 비밀번호를 어떻게 검증할지를 알려주는 설정
		 *  1. 인증 주체인 AuthenticationManager 객체를 불러오고
		 *  2. AuthenticationProvider 구현체인 DaoAuthenticationProvider를 통해서 인증 진행
		 *  3. DaoAuthenticationProvider 구현체는 userDetailsService 통해서 인증진행
		 *  4. 인증 진행시 비밀번호 검증은 passwordEncoder를 통해서 진행
		 */
		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);

		http	// 로그인/로그아웃 설정
				.formLogin(formLogin -> formLogin
						.loginPage("/member/login.do")
						.successHandler(authSuccessHandler) /* 로그인 성공시 처리 객체 */
						.failureHandler(authFailureHandler) /* 로그인 실패시 처리 객체 */
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout.do"))
						.logoutSuccessUrl("/member/login.do")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)	// 인가설정(권한 설정)
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/ckeditor2/**", "/vendor/**", "/assets/**").permitAll()
						.requestMatchers("/member/login.do","/member/logout.do", "/member/action", "/member/join.do/**", "/member/modify.do").permitAll()
						.requestMatchers( "/board/detail.do/*").hasRole("USER")
						.requestMatchers("/board/list.do/**", "/board/create.do/**", "/board/update.do/**", "/board/delete").hasRole("USER")
						.requestMatchers("/admin/item/create.do/**").permitAll()
						.requestMatchers("/api/track/**").hasAnyRole("USER","MANAGER", "ADMIN")
						.requestMatchers("/mail/**").permitAll()
						.requestMatchers("/").permitAll()
						.requestMatchers("/index").permitAll()
						.anyRequest().authenticated()
				) // 아이디 기억 관련 설정
				.rememberMe(rememberMe -> rememberMe
						.alwaysRemember(false)
						.tokenValiditySeconds(43200)
						.rememberMeParameter("remember-me")
				) // 권한 없는 페이지 요청 처리
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler)
				)
				/*
				 * 소셜 로그인 설정
				 *  - oauth2Login() 메소드 : 소셜(OAuth2) 로그인을 활성화하는 설정의 시작점.
				 *  - 이 메서드를 호출함으로써, 애플리케이션은 OAuth2 공급자(Google, Kakao 등)를
				 *    통해 사용자 인증을 수행할 수 있게 된다.
				 *  - loginPage() : 사용자가 인증되지 않은 상태에서 보호된 리소스에 접근시 여기로 리디렉트[생략가능]
				 *  - successHandler() : 인증 성공 핸들러 지정
				 */
				.oauth2Login(oauth2Login -> oauth2Login
						.loginPage("/member/login.do")
						.successHandler(authenticationSuccessHandler)
						.userInfoEndpoint(userInfo -> userInfo // Endpoint : API와 같은 의미(ex 은행의 텔러)
								.userService(customOAuth2UserService) // 소셜로그인 담당 주체
						)
				);

		/**
		 * 위에서 설정한 값으로 실제 AuthenticationManager 객체 생성
		 * 그리고 이 인스턴스를 HttpSecurity 객체에 등록
		 * 여기까지 HttpSecurity 구성에 관한 설정이 완료됨
		 */
		http.authenticationManager(auth.build());

		/**
		 * HttpSecurity 구성이 완료된 후 스프링 시큐리티 필터 체인을 초기화하고 구성하는 역할
		 *  1. 필터 체인 구성: HttpSecurity 객체에 정의된 모든 설정(인증, 인가, CSRF 보호, 세션 관리 등)을
		 *     바탕으로 실제 스프링 시큐리티 필터 체인이 구성. 이 필터 체인은 HTTP 요청을 처리할 때
		 *     각 필터가 순차적으로 요청을 검사하고 처리하게 됨.
		 *  2. http.build() : 최종적으로 SecurityFilterChain 객체를 반환
		 *     이 객체는 구성된 필터들의 체인을 나타내며, 스프링 시큐리티의 핵심 구성요소
		 *  3. 반환된 SecurityFilterChain 객체는 스프링 시큐리티의 인프라스트럭처와 통합되어,
		 *     어플리케이션에 대한 모든 HTTP 요청에 대해 보안 처리를 수행하게됨.
		 *
		 */
		return http.build();
	}

}
