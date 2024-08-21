package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.repository.ItemMapper;
import com.sportsfit.shop.vo.Criteria;
import com.sportsfit.shop.vo.ItemImgVo;
import com.sportsfit.shop.vo.ItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 상품 담당 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemMapper itemMapper;
    private ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    /**
     * 상품과 상품 이미지 함께 저장
     */
    @Override
    public void saveItemWithImages(ItemFormDto itemFormDto) throws Exception {

        // 상품 등록
        ItemVo item = ItemVo.builder()
                .categoryId(itemFormDto.getCategoryId())
                .itemName(itemFormDto.getItemName())
                .price(itemFormDto.getPrice())
                .stockNumber(itemFormDto.getStockNumber())
                .itemDetail(itemFormDto.getItemDetail())
                .dcRate(itemFormDto.getDcRate())
                .itemSellStatus(itemFormDto.getItemSellStatus())
                .itemGubun(itemFormDto.getItemGubun())
                .build();

        itemMapper.saveItem(item); // 상품 DB 저장

        // 상품 이미지 등록
        Long itemId = item.getItemId(); // 상품코드
        List<ItemImgVo> itemImgs = new ArrayList<>();
        int repImgIndex = Integer.parseInt(itemFormDto.getRepImgIndex()); // 대표이미지 인덱스

        for(int i = 0; i < itemFormDto.getItemImgFiles().size(); i++) {
            MultipartFile file = itemFormDto.getItemImgFiles().get(i);
            if(!file.isEmpty()) {

                String uuid = UUID.randomUUID().toString();
                String fileName = file.getOriginalFilename();

                ItemImgVo itemImg = new ItemImgVo();
                itemImg.setItemId(itemId);
                itemImg.setFileName(fileName);
                itemImg.setUuid(uuid);
                itemImg.setOrd(i);
                itemImg.setRepImg(i == repImgIndex);

                itemMapper.saveItemImg(itemImg); // 상품이미지 DB 저장
                itemImgs.add(itemImg);

                // 파일 저장
                file.transferTo(new File(uploadDir + "\\" + uuid + "_" + fileName));
            }
        }
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
     * 카테고리별 상품목록 조회
     */
    @Override
    public List<ItemVo> findItemByCategoryId(Long categoryId, Criteria cri) {
        int offset = (cri.getPageNum() -1) * cri.getAmount();
        List<ItemVo> items = itemMapper.findItemByCategoryId(categoryId, offset, cri.getAmount(), cri.getSearchText());
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 카테고리별 총 상품 갯수 가져오기
     */
    @Override
    public int countItemByCategoryId(Long categoryId, Criteria cri){
        return itemMapper.countItemByCategoryId(categoryId, cri.getSearchText());
    }

    /**
     * 상품 구분별 상품 목록 조회
     */
    @Override
    public List<ItemVo> findItemByItemGubun(String itemGubun, Criteria cri){
        int offset = (cri.getPageNum() -1) * cri.getAmount();
        List<ItemVo> items = itemMapper.findItemByItemGubun(itemGubun, offset, cri.getAmount(), cri.getSearchText());
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    };

    /**
     * 상품 구분별 총 상품 갯수 가져오기
     */
    @Override
    public int countItemByItemGubun(String itemGubun, Criteria cri){
        return itemMapper.countItemByItemGubun(itemGubun, cri.getSearchText());
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

    /**
     * 검색어별 상품 목록 조회 (통합검색)
     */
    @Override
    public List<ItemVo> findItemByItemDetail(Criteria cri) {
        int offset = (cri.getPageNum() -1) * cri.getAmount();
        List<ItemVo> items = itemMapper.findItemByItemDetail(cri.getSearchText(), offset, cri.getAmount());
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 검색어 검색결과의 총 상품 갯수 가져오기
     */
    @Override
    public int countItemByItemDetail(Criteria cri){
        return itemMapper.countItemByItemDetail(cri.getSearchText());
    }
}
