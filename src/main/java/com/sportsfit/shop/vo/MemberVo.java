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

    @Builder.Default
    private boolean social = false; // 소셜로그인 여부 default : 0
    private boolean del = false; // 탈퇴여부 default : 0

    @Builder.Default
    private List<RoleVo> roles = new ArrayList<>(); // 역할구분 (여러 권한 가능)
    @Builder.Default
    private Map<String, Object> attributes = Map.of(); // 소셜 로그인 정보 기본 값 설정

}
