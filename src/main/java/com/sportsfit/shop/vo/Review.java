package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 후기게시판 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseVo {

    private Long reviewId; // 후기코드
    private Long memberId; // 후기작성 회원코드
    private Long orderItemId; // 주문상품코드
    private String rTitle; // 후기 제목
    private String rContent; // 후기 내용

}
