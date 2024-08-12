package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 권한 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleVo {

    private Long roleId;        // 권한코드
    private String roleName;    // 권한명

}
