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
public class ItemVo {

    private Long itemId; // 상품코드
    private Long categoryId; // 카테고리 코드
    private String itemName; // 상품이름
    private int price; // 가격
    private int stockNumber; // 재고수량
    private String itemDetail; // 상품상세설명
    private double dcRate; // 할인율
    private String gubunSubCode; // 상품판매상태(sellStatus)

    // 날짜 바인딩 패턴 : yyyy-MM-dd
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate; // 등록일자

}
