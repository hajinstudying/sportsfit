package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 문의게시판 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaVo  extends BaseVo{

    private Long qnaId; // 문의코드
    private String qnaContent; // 문의내용
    private Boolean secret; // 비밀글여부
    private Long memberId; // 문의하는 회원코드
    private Long itemId; // 문의하는 상품코드

}
