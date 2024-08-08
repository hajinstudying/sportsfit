package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 카테고리 Vo
 * - 하위카테고리가 함께 등록되며 상위카테고리는 카테고리코드로 참조한다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryVo {

    private Long categoryId; // 카테고리 코드
    private String categoryName; // 카테고리명
    private Long parentId; // 상위 카테고리코드

}
