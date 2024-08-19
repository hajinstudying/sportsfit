package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 상품 이미지 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImgVo {

    private String uuid; // uuid
    private Long itemId; // 상품코드
    private String fileName; // 파일이름
    private int ord; // 순서
    private boolean repImg; // 대표이미지여부

}
