package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
    private String itemSellStatus; // 상품판매상태 (판매중, 품절)
    private String itemGubun; // 상품구분 (전체상품, 신상품, 베스트셀러)
    private List<ItemImgVo> itemImgs; // 상품 이미지 목록
    private String repImgFileName; // 대표 이미지 파일 이름 추가

    /**
     * 대표 이미지 파일 이름을 설정하는 메소드
     */
    public void setRepImgFileName() {
        if (itemImgs != null) {
            for (ItemImgVo img : itemImgs) {
                if (img.isRepImg()) {
                    this.repImgFileName = img.getFileName();
                    break;
                }
            }
        }
    }
}
