package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 상품 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVo extends BaseVo {

    private Long itemId; // 상품코드
    private Long categoryId; // 카테고리 코드
    private String itemName; // 상품이름
    private int price; // 가격
    private int stockNumber; // 재고수량
    private String itemDetail; // 상품상세설명
    private double dcRate; // 할인율
    private String sellStatus; // 상품판매상태 (판매중, 품절)
    private String itemGubun; // 상품구분 (전체상품, 신상품, 베스트셀러)

}
