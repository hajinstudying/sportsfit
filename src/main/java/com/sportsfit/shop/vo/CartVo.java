package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
    private List<CartItemVo> cartItems; // 장바구니 상품항목

}
