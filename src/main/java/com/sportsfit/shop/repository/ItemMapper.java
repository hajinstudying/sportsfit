package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.ItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {

    // 상품 추가
    void saveItem(ItemVo itemVo);

    // 상품 정보 수정
    void updateItem(ItemVo itemVo);

    // 상품 삭제
    void deleteItem(ItemVo itemVo);

    // 상품코드로 상세 조회
    Optional<ItemVo> findItemById(Long itemId);

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
}
