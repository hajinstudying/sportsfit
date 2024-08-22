package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 상품의 사이즈나 색상과 같은 옵션, 옵션별 재고를 담은 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionVo {

    private Long optionId; // 옵션코드
    private Long itemId; // 상품코드
    private String optionName; // 옵션이름
    private String optionValue; // 옵션값
    private int additionalPrice; // 옵션별 추가요금
    private int stockNumber; // 옵션별 재고수량

}
