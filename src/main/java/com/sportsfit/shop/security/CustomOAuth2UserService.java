package com.sportsfit.shop.security;

import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.service.MemberService;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 소셜 로그인을 통해서 인증을 진행하는 클래스
 *  - 소셜이 아니면 CustomUserDetailsService 클래스가 인증 진행
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인 인증 진행 메소드
     *  - 일반 인증에서는 loadUserByUsername 메소드가 진행.
     *  파라미터인 OAuth2UserRequest 에 포함된 정보
     *   1. Registration ID : 여러 소셜 로그인 업체 중에서 어떤 업체를 사용할지 정보
     *   2. Client ID & Client Secret, Redirect URI 정보등
     *   3. 이 모든 정보는 application.properties 에 설정 해놓을것.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser 실행됨");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        /**
         * DefaultOAuth2UserService 클래스의 loadUser() 메소드 호출.
         * 소설로그인 처리를 진행하고 결과를 OAuth2User 객체에 담아서 보내줌.
         * OAuth2User 객체에는 카카오에서 제공하는 사용자의 이메일, 이름 등의 정보 포함
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);

        /**
         * oAuth2User 에서 정보를 Key-value 형태로 추출하여 map 형태로 보관.
         */
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = "";
        String name = "";

        // 클라이언트 추가 가능하도록 switch문
        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                name = getKakaoNickname(paramMap);
                log.info("카카오 이메일 : " + email);
                break;
        }

        OAuth2User oAuth2UserDto = generateSecurityDTO(email, name, paramMap);
        return oAuth2UserDto;
    }

    /**
     * 소셜에서 갖고온 데이터로 시큐리티 인증 정보 생성
     */
    @Transactional
    private MemberSecurityDTO generateSecurityDTO(String email, String name, Map<String, Object> params) {
        log.info("generateSecurityDTO 실행됨");
        // 소셜(카카오)에서 갖고온 이메일로 데이터베이스 조회
        MemberVo result = memberService.findMemberByEmail(email);

        /**
         * 이전에 소셜 로그인을 한적이 있어서 데이터베이스에 정보가 있는 경우
         * 데이터베이스에서 조회한 정보로 스프링 시큐리티 인증 객체 생성
         */
        if (result == null) {
            log.info("소셜로그인 사용자가 DB에 존재하지 않습니다.");

            /**
             * 데이터베이스에 해당 이메일 사용자가 없는 경우, 최초 소셜 로그인
             * - 사용자 정보 데이터베이스 저장
             * - 디비에 회원 추가(이메일주소/패스워드는 1111/권한은 USER/social flag true 하드코딩)
             */

            String encodedPassword = passwordEncoder.encode("1111");
            RoleVo userRole = new RoleVo(2L, "USER");
            List<RoleVo> roles = Collections.singletonList(userRole);

            MemberVo memberVo = MemberVo.builder()
                    .password(encodedPassword)
                    .name(name != null ? name : "social user") /* 소셜에서 받은 이름 */
                    .email(email) /* 소셜에서 받은 이메일 */
                    .social(true)
                    .del(false)
                    .roles(roles)
                    .attributes(params)
                    .build();

            memberService.saveMember(memberVo); // 영속화
            log.info("저장된 memberVo : {}", memberVo);
            System.out.println("저장된 memberVo : " + memberVo);
            return new MemberSecurityDTO(memberVo, params);

        } else {
            log.info("소셜로그인 사용자가 DB에 존재합니다. isSocialUser");
                return new MemberSecurityDTO(result, params);
        }
    }


    /**
     * paramMap에서 이메일 추출
     */
    private String getKakaoEmail(Map<String, Object> paramMap){
        log.info("KAKAO-----------------------------------------");
        Object kakaoAccount = paramMap.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) kakaoAccount;
        String email = (String)accountMap.get("email");
        log.info("email..." + email);
        return email;
    }

    /**
     * 카카오 닉네임 추출
     */
    private String getKakaoNickname(Map<String, Object> paramMap) {
        LinkedHashMap propertiesMap = (LinkedHashMap) paramMap.get("properties");
        return (String) propertiesMap.get("nickname");
    }

}

