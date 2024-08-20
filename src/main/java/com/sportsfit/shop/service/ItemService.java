package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.vo.ItemImgVo;
import com.sportsfit.shop.vo.ItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {

    // 상품과 이미지 함께 등록
    void saveItemWithImages(ItemFormDto itemFormDto) throws Exception;

    // 상품 추가
    void saveItem(ItemVo itemVo);

    // 상품 이미지 추가
    void saveItemImg(ItemImgVo itemImgVo);

    // 상품 정보 수정
    void updateItem(ItemVo itemVo);

    // 상품 삭제
    void deleteItem(long itemId);

    // 상품코드로 상세 조회
    ItemVo findItemById(Long itemId);

    // 전체 상품 목록 조회
    List<ItemVo> findAllItems();

    // 카테고리로 상품 목록 조회
    List<ItemVo> findItemByCategoryId(Long categoryId);

    // 상품명으로 상품 목록 조회
    List<ItemVo> findItemByItemName(String itemName);

    // 상품명, 상세설명으로 상품 목록 조회
    List<ItemVo> findItemByItemDetail(String searchTerm);

    // 상품 가격으로 상품 목록 조회
    List<ItemVo> findItemByPrice(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    // 판매상태별 상품목록 조회 (SELL/SOLD_OUT)
    List<ItemVo> findItemBySellStatus(String itemSellStatus);

    // 상품구분별 상품목록 조회 (ITGU)
    List<ItemVo> findItemByItemGubun(String itemGubun);

    // 상품구변별 상품목록 4개만 조회 (메인페이지용)
    List<ItemVo> findItem4ByItemGubun(String itemGubun);

}
