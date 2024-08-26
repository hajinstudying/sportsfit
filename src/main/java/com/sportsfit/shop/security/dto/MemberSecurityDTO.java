package com.sportsfit.shop.security.dto;


import com.sportsfit.shop.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Slf4j
public class MemberSecurityDTO extends User implements OAuth2User, Serializable {

    private static final long serialVersionUID = 1L;

    private MemberVo memberVo; // 일반 시큐리티 로그인
    private Map<String, Object> attributes; //소셜 로그인 정보

    public MemberSecurityDTO(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        //log.info("MemberDto 생성자 호출 -1 ");
    }

    // 일반 시큐리티 사용
    public MemberSecurityDTO(MemberVo memberVo) {
        super(memberVo.getEmail(),
                memberVo.getPassword(),
                memberVo.getRoles().stream()
                        .map(g -> new SimpleGrantedAuthority(g.getRoleName()))
                        .collect(Collectors.toList()));

        //log.info("MemberDto 생성자 호출 - 2");

        this.memberVo = memberVo;
    }

    // 소셜 로그인 사용자용 생성자, 파라미터로 attributes 추가됨.
    public MemberSecurityDTO(MemberVo memberVo,
                      Map<String, Object> attributes) {

        super(memberVo.getEmail(),
                memberVo.getPassword(),
                memberVo.getRoles().stream()
                        .map(g -> new SimpleGrantedAuthority(g.getRoleName()))
                        .collect(Collectors.toList()));

        //log.info("CustomUser 생성자 호출 - 소셜 로그인");

        this.memberVo = memberVo;
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getUsername() {
        return this.memberVo.getEmail();
    }

    @Override
    public String getName() {
        return this.memberVo.getName();
    }

    public Long getMemberId() { return this.memberVo.getMemberId(); }

    public String getPassword() {
        return this.memberVo.getPassword();
    }

    public boolean isSocial() {
        return this.memberVo.isSocial();
    }
}
