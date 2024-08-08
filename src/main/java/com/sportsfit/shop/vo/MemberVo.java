package com.sportsfit.shop.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVo extends BaseVo {

    private Long memberId; // 회원코드
    private String name; // 이름
    private String email; // 이메일
    private String password; // 비밀번호
    private String role; // 역할구분 default : 'USER'
    private boolean social; // 소셜로그인 여부 default : 0
    private boolean del; // 탈퇴여부 default : 0

}
