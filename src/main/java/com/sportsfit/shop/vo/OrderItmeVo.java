package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 주문항목 Vo
 * - 주문한 각 상품 항목에 대한 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItmeVo {

    private Long orderItemId; // 주문항목코드
    private Long orderId; // 주문코드
    private Long itemId; // 상품코드
    private int unitPrice; // 할인율이 반영된 상품단가
    private String quantity; // 주문수량

}
