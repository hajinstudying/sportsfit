package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.ItemMapper;
import com.sportsfit.shop.vo.ItemImgVo;
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
        itemMapper.updateItem(itemVo);
    }

    /**
     * 상품 삭제
     */
    @Override
    public void deleteItem(long itemId) {
        itemMapper.deleteItem(itemId);
    }

    /**
     * 상품 조회
     */
    @Override
    public ItemVo findItemById(Long itemId) {
        ItemVo itemVo = itemMapper.findItemById(itemId).orElseThrow();
        itemVo.setRepImgFileName();
        return itemVo;
    }

    /**
     * 전체 상품 목록 조회
     */
    @Override
    public List<ItemVo> findAllItems() {
        List<ItemVo> items = itemMapper.findAllItems();
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 카테고리별 상품목록 조회
     */
    @Override
    public List<ItemVo> findItemByCategoryId(Long categoryId) {
        List<ItemVo> items = itemMapper.findItemByCategoryId(categoryId);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 상품 이름으로 상품 목록 검색
     */
    @Override
    public List<ItemVo> findItemByItemName(String itemName) {
        List<ItemVo> items = itemMapper.findItemByItemName(itemName);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 키워드로 상품 설명과 상품 이름 검색
     */
    @Override
    public List<ItemVo> findItemByItemDetail(String searchTerm) {
        List<ItemVo> items = itemMapper.findItemByItemDetail(searchTerm);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 상품 가격별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemByPrice(int minPrice, int maxPrice) {
        List<ItemVo> items = itemMapper.findItemByPrice(minPrice, maxPrice);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 상품 상태별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemBySellStatus(String itemSellStatus) {
        List<ItemVo> items = itemMapper.findItemBySellStatus(itemSellStatus);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 상품 구분별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemByItemGubun(String itemGubun) {
        List<ItemVo> items = itemMapper.findItemByItemGubun(itemGubun);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 상품 구분별 상품 목록 4개만 조회 (인덱스페이지 용도)
     * itemGubun에 "NEW"/ "BEST"
     */
    @Override
    public List<ItemVo> findItem4ByItemGubun(String itemGubun) {
        List<ItemVo> items = itemMapper.findItem4ByItemGubun(itemGubun);
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

}
