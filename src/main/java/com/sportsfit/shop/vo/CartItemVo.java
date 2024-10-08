package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 장바구니 상품 Vo
 * - 장바구니와 상품의 다대다 관계 해소를 위한 Vo
 * - 장바구니에 담긴 개별상품에 대한 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemVo extends BaseVo {

    private Long cartItemId; // 장바구니 상품 코드
    private Long cartId; // 장바구니 코드
    private Long itemId; // 상품코드
    private int count; // 상품 수량
    private Long optionId; // 상품 옵션

}
