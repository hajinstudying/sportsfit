package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.ItemMapper;
import com.sportsfit.shop.vo.ItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 상품 담당 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemMapper itemMapper;
    private ModelMapper modelMapper;

    /**
     * 상품 저장
     */
    @Override
    public void saveItem(ItemVo itemVo) {

        itemMapper.saveItem(itemVo);
    }

    /**
     * 상품 수정
     */
    @Override
    public void updateItem(ItemVo itemVo) {

    }

    /**
     * 상품 삭제
     */
    @Override
    public void deleteItem(ItemVo itemVo) {

    }

    /**
     * 상품 조회
     */
    @Override
    public Optional<ItemVo> findItemById(Long itemId) {
        return Optional.empty();
    }

    /**
     * 전체 상품 목록 조회
     */
    @Override
    public List<ItemVo> findAllItems() {
        return null;
    }

    /**
     * 카테고리별 상품목록 조회
     */
    @Override
    public List<ItemVo> findItemByCategoryId(Long categoryId) {
        return null;
    }

    /**
     * 상품 이름으로 상품 목록 검색
     */
    @Override
    public List<ItemVo> findItemByItemName(String itemName) {
        return null;
    }

    /**
     * 키워드로 상품 설명과 상품 이름 검색
     */
    @Override
    public List<ItemVo> findItemByItemDetail(String searchTerm) {
        return null;
    }

    /**
     * 상품 가격별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemByPrice(int minPrice, int maxPrice) {
        return null;
    }

    /**
     * 상품 상태별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemBySellStatus(String itemSellStatus) {
        return null;
    }

    /**
     * 상품 구분별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemByItemGubun(String itemGubun) {
        return null;
    }

    /**
     * 상품 구분별 상품 목록 3개만 조회 (인덱스페이지 용도)
     * itemGubun에 "NEW"/ "BEST"
     */
    @Override
    public List<ItemVo> findItem3ByItemGubun(String itemGubun) {
        return itemMapper.findItem3ByItemGubun(itemGubun);
    }

}
