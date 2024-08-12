package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 회원권한 Vo
 * - MmeberVo와 RoleVo의 다대다 관계 해소를 위한 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRoleVo {

    private Long memberId;  // member 테이블의 FK
    private Long roleId;    // role 테이블의 FK

}
