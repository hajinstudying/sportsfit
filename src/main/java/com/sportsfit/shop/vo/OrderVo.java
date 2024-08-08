package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 주문 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVo extends BaseVo {

    private Long orderId; // 주문코드
    private Long memberId; // 주문한 회원코드
    private Long addressId; // 배송될 주소코드
    private int orderAmount; // 주문총액
    private String orderStatus; // 주문상태

}
