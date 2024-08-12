package com.sportsfit.shop.security;

import com.sportsfit.shop.repository.MemberRepository;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 소셜 로그인을 통해서 인증을 진행하는 클래스
 *  - 소셜이 아니면 CustomUserDetailsService 클래스가 인증 진행
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
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
        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                name = getKakaoUserName(paramMap);
                break;
        }
        log.info("카카오 이메일 : " + email);
        MemberSecurityDTO msd = generateSecurityDTO(email, name, paramMap);
        return msd;
    }

    /**
     * 소셜에서 갖고온 데이터로 시큐리티 인증 정보 생성
     */
    private MemberSecurityDTO generateSecurityDTO(String email, String name, Map<String, Object> params) {

        // 소셜(카카오)에서 갖고온 이메일로 데이터베이스 조회
        MemberVo result = memberRepository.findMemberByEmail(email);

        /**
         * 이전에 소셜 로그인을 한적이 있어서 데이터베이스에 정보가 있는 경우
         * 데이터베이스에서 조회한 정보로 스프링 시큐리티 인증 객체 생성
         */
        if (result != null) {
            // 시큐리티에 저장할 권한 authorities 컬렉션 변수 생성
            Collection<SimpleGrantedAuthority> authorities = result.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())) // Corrected part
                    .collect(Collectors.toList());

            // 디비에서 조회한 정보로 시큐리티 인증 객체 생성
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            result.getMemberId(),
                            result.getName(),
                            result.getEmail(),
                            result.getPassword(),
                            authorities
                    );

            log.info("memberSecurityDTO : " + memberSecurityDTO);
            return memberSecurityDTO;

        } else { // 최초 소셜 로그인 사용자

            /**
             * 데이터베이스에 해당 이메일 사용자가 없는 경우, 최초 소셜 로그인
             * - 사용자 정보 데이터베이스 저장
             * - 디비에 회원 추가(이메일주소/패스워드는 1111/권한은 USER/social flag true 하드코딩)
             */
            MemberVo memberVo = MemberVo.builder()
                    .password(passwordEncoder.encode("1111"))
                    .email(email) /* 소셜에서 받은 이메일 */
                    .name(name) /* 소셜에서 받은 이름 */
                    .social(true)
                    .roles(Arrays.asList(new RoleVo(null, "USER"))) // Assume Role has a constructor accepting role name
                    .del(false)
                    .build();
            memberRepository.saveMember(memberVo); // 영속화
            log.info("저장된 memberVo : {}", memberVo);

            /**
             * MemberSecurityDTO, 인증정보 생성
             * 카카오에서 조회한 이메일과 비밀번호(1111), 권한은 ROLE_USER로 인증객체 만들기.
             */
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            memberVo.getMemberId(),
                            name,
                            email,
                            "1111",
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            memberSecurityDTO.setProps(params);
            memberSecurityDTO.setSocial(true);

            log.info("소셜 로그인 memberSecurityDTO : " + memberSecurityDTO);
            return memberSecurityDTO;
        }
    }


    /**
     * paramMap에서 이메일 추출
     */
    private String getKakaoEmail(Map<String, Object> paramMap){
        Object value = paramMap.get("kakao_account");
        log.info(value);
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String)accountMap.get("email");
        // 만약 이메일이 없으면 기본 이메일로 세팅
        if("".equals(email)) {
            email = "default_email@kakao.com";
        }
        log.info("email..." + email);
        return email;
    }

    /**
     * 카카오 닉네임 추출
     */
    private String getKakaoUserName(Map<String, Object> paramMap) {
        LinkedHashMap kakaoAccount = (LinkedHashMap) paramMap.get("kakao_account");
        LinkedHashMap profile = (LinkedHashMap) kakaoAccount.get("profile");
        String name = (String) profile.get("nickname");
        // 만약 name 이 없으면 기본 이름으로 세팅
        if("".equals(name)) {
            name = "기본사용자";
        }
        log.info("카카오 사용자명 : " + name);
        return name;
    }

}

