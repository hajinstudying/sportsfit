package com.sportsfit.shop.security.dto;


import com.sportsfit.shop.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.jar.Attributes;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private Long memberId;
    private String name;
    private String email;
    private String password;
    private boolean del;
    private boolean social;

    private Map<String, Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(Long memberId,
                             String name,
                             String email,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        // User 클래스의 생성자 호출
        super(email, password, authorities);

        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return props;
    }
}
