package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.vo.Criteria;
import com.sportsfit.shop.vo.ItemImgVo;
import com.sportsfit.shop.vo.ItemVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    // 상품과 이미지 함께 등록
    void saveItemWithImages(ItemFormDto itemFormDto) throws Exception;

    // 상품 정보 수정
    void updateItem(ItemVo itemVo);

    // 상품 삭제
    void deleteItem(long itemId);

    // 상품코드로 상세 조회
    ItemVo findItemById(Long itemId);

    // 카테고리로 상품 목록 조회
    List<ItemVo> findItemByCategoryId(Long categoryId, Criteria cri);

    // 카테고리별 총 상품갯수 가져오기
    int countItemByCategoryId(Long categoryId, Criteria cri);

    // 상품구분별 상품목록 조회 (ITGU)
    List<ItemVo> findItemByItemGubun(String itemGubun, Criteria cri);

    // 상품구분별 총 상품갯수 가져오기
    int countItemByItemGubun(String itemGubun, Criteria cri);

    // 상품구분별 상품목록 4개만 조회 (메인페이지용)
    List<ItemVo> findItem4ByItemGubun(String itemGubun);

    // 검색어로 상품목록 조회
    List<ItemVo> findItemByItemDetail(Criteria cri);

    // 검색어 결과의 상품 총객수 가져오기
    int countItemByItemDetail(Criteria cri);

}
