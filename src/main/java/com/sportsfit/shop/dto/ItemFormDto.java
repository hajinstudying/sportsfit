package com.sportsfit.shop.dto;

import com.sportsfit.shop.vo.ItemVo;
import com.sportsfit.shop.vo.OptionVo;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ItemFormDto 데이터 전송 및 유효성 검사용 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ItemFormDto {

    private Long itemId; // 상품 코드
    private String itemName; // 상품 이름
    private Long parentId; // 부모 카테고리 ID
    private Long categoryId; // 하위 카테고리 ID
    private int price; // 가격
    private double dcRate; // 할인율
    private String itemDetail; // 상품 상세 설명
    private String itemSellStatus; // 판매 상태
    private String itemGubun; // 상품 구분

    private List<String> fileNames; // 업로드된 이미지 파일 목록
    private List<OptionVo> options; // 상품 옵션 목록

}
