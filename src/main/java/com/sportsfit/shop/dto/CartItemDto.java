package com.sportsfit.shop.dto;

import lombok.*;

/**
 * CartItemDto
 * 회원별 장바구니 조회시 상품 정보를 함께 전달하는 CartItemDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartItemDto {

    private Long cartItemId; // 장바구니 상품 항목코드
    private Long cartId; // 장바구니 코드
    private Long itemId; // 상품코드
    private int count; // 상품 수량

    private Long optionId; // 옵션코드
    private String optionName; // 옵션명
    private String optionValue; // 옵션값
    private int additionalPrice; // 추가 가격

    private int price; // 상품 가격
    private String itemName; // 상품 이름
    private double dcRate; // 할인율
    private String itemSellStatus; // 상품판매상태
    private String repImgFileName; // 대표 이미지 파일 이름

    private int totalPrice; // 장바구니 항목의 총가격

    // 장바구니 항목 총가격 구하는 메소드
    public void setTotalPrice() {
        int discountedPrice = calculateDiscountedPrice();
        int priceWithOption = discountedPrice + this.additionalPrice;
        this.totalPrice = Math.max(priceWithOption * this.count, 0);
    }

    // 상품 할인 가격 구하는 메소드
    private int calculateDiscountedPrice() {
        return Math.max((int)(this.price * (1 - this.dcRate)), 0);
    }

}
