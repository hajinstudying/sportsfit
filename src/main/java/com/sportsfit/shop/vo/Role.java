package com.sportsfit.shop.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private Long id;        // role_id
    private String roleName;
}
