package com.sportsfit.shop.security.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * 사용자 정보로 시큐리티 인증 정보를 만드는 역할.
 *  - 스프링 시큐리티 제공  User 클래스 상속
 *  - 소셜 로그인을 위해서 OAuth2User 구현
 * 즉, 시큐리티 로그인과 소셜로그인 양쪽 모두에 사용됨.
 * User 상속으로 @Builder, @NoArgs, @AllArgs 사용불가.
 */
@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String id;
    private String password;
    private String email;
    private String name;
    private String realName;
    private boolean del;
    private boolean social;

    private Map<String, Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             String name) {
        // User 클래스의 생성자 호출
        super(username, password, authorities);

        this.id = username;
        this.password = password;
        this.email = username;
        this.name = name;
        this.realName = name;
    }

    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    /**
     * id 즉, 이메일을 반환한다.
     *  - 컨트롤러에서 principal.getName()하면 이 메소드가 호출됨.
     */
    @Override
    public String getUsername() {
        return this.id;
    }

    public String getRealName() {
        return this.name; // 실제 사용자의 이름 필드
    }

}
