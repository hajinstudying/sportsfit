package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 장바구니 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartVo extends BaseVo {

    private Long cartId; // 장바구니 코드
    private Long memberId; // 회원코드

}
