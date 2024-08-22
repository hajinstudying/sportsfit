package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.Criteria;
import com.sportsfit.shop.vo.ItemImgVo;
import com.sportsfit.shop.vo.ItemVo;
import com.sportsfit.shop.vo.OptionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {

    // 상품 추가
    void saveItem(ItemVo itemVo);

    // 상품 이미지 추가
    void saveItemImg(ItemImgVo itemImgVo);

    // 상품 옵션 추가
    void saveOption(OptionVo optionVo);

    // 상품 정보 수정
    void updateItem(ItemVo itemVo);

    // 상품 이미지 수정
    void updateItemImg(ItemImgVo itemImgVo);

    // 상품 옵션 수정
    void updateOption(OptionVo optionVo);

    // 상품 삭제
    void deleteItem(long itemId);

    // 상품 이미지 삭제
    void deleteItemImg(ItemImgVo itemImgVo);

    // 상품 옵션 삭제
    void deleteOption(OptionVo optionVo);

    // 상품코드로 상세 조회
    Optional<ItemVo> findItemById(Long itemId);

    // 카테고리로 상품 목록 조회
    List<ItemVo> findItemByCategoryId(@Param("categoryId") Long categoryId,
                                      @Param("offset") int offset,
                                      @Param("amount") int amount,
                                      @Param("searchText") String searchText);

    // 카테고리별 상품 총갯수 구하기
    int countItemByCategoryId(@Param("categoryId") Long categoryId, @Param("searchText") String searchText);

    // 상품구분별 상품목록 조회 (ITGU)
    List<ItemVo> findItemByItemGubun(@Param("itemGubun") String itemGubun,
                                     @Param("offset") int offset,
                                     @Param("amount") int amount,
                                     @Param("searchText") String searchText);

    // 상품구분별 상품 총갯수 구하기
    int countItemByItemGubun(@Param("itemGubun") String itemGubun, @Param("searchText") String searchText);

    // 상품구분별 상품목록 4개 조회 (인덱스페이지 용도)
    List<ItemVo> findItem4ByItemGubun(String itemGubun);

    // 상품명, 상세설명으로 상품 목록 조회
    List<ItemVo> findItemByItemDetail(@Param("searchText") String searchText,
                                      @Param("offset") int offset,
                                      @Param("amount") int amount);

    // 상품명, 상세설명으로 가져온 상품 총 갯수 구하기
    int countItemByItemDetail(@Param("searchText") String searchText);

    // ========================= 사용하지 않음 ======================

    // 상품명으로 상품 목록 조회
    List<ItemVo> findItemByItemName(String itemName);



    // 상품 가격으로 상품 목록 조회
    List<ItemVo> findItemByPrice(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    // 판매상태별 상품목록 조회 (SELL/SOLD_OUT)
    List<ItemVo> findItemBySellStatus(String itemSellStatus);

    // 전체 상품 목록 조회
    List<ItemVo> findAllItems();

}
