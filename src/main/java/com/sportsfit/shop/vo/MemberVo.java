package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 회원 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVo {

    private Long memberId; // 회원코드
    private String name; // 이름
    private String email; // 이메일
    private String password; // 비밀번호
    private List<RoleVo> roles; // 역할구분 (여러 권한 가능)
    private boolean social; // 소셜로그인 여부 default : 0
    private boolean del; // 탈퇴여부 default : 0

    // 날짜 바인딩 패턴 : yyyy-MM-dd
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate; // 등록일자

    // 개별 Role 객체를 추가
    public void addRole(RoleVo roleVo) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();  // roles가 null인 경우 초기화
        }
        this.roles.add(roleVo);
    }

}
