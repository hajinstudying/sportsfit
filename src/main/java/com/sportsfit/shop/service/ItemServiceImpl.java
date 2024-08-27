package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.repository.ItemMapper;
import com.sportsfit.shop.vo.Criteria;
import com.sportsfit.shop.vo.ItemImgVo;
import com.sportsfit.shop.vo.ItemVo;
import com.sportsfit.shop.vo.OptionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 상품 담당 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemMapper itemMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @Override
    public void saveItemWithImages(ItemFormDto itemFormDto) throws Exception {
        try {
            // 상품 등록
            ItemVo item = ItemVo.builder()
                    .categoryId(itemFormDto.getCategoryId())
                    .itemName(itemFormDto.getItemName())
                    .price(itemFormDto.getPrice())
                    .itemDetail(itemFormDto.getItemDetail())
                    .dcRate(itemFormDto.getDcRate())
                    .itemSellStatus(itemFormDto.getItemSellStatus())
                    .itemGubun(itemFormDto.getItemGubun())
                    .build();

            itemMapper.saveItem(item); // 상품 DB 저장
            Long itemId = item.getItemId(); // 상품코드

            // 상품 옵션 등록
            List<OptionVo> options = new ArrayList<>();
            for (OptionVo option : itemFormDto.getOptions()) {
                OptionVo newOption = OptionVo.builder()
                        .itemId(itemId)
                        .optionName(option.getOptionName())
                        .optionValue(option.getOptionValue())
                        .additionalPrice(option.getAdditionalPrice())
                        .stockNumber(option.getStockNumber())
                        .build();
                itemMapper.saveOption(newOption); // 옵션 DB 저장
                options.add(newOption);
                log.info("DB에 저장된 옵션의 수 : " + options.size());
            }

            // 상품 이미지 등록
            List<String> fileNames = itemFormDto.getFileNames();
            log.info("가져온 fileNames", itemFormDto.getFileNames());
            if (fileNames != null && !fileNames.isEmpty()) {
                for (int i = 0; i < fileNames.size(); i++) {

                    String fileName = fileNames.get(i);
                    String[] arr = fileName.split("_",2);

                    // 잘못된 형식의 파일명 처리
                    if (arr.length < 2) {
                        log.info("잘못된 형식의 파일명 : {}", fileName);
                        continue;
                    }

                    ItemImgVo itemImgVo = ItemImgVo.builder()
                            .uuid(arr[0])
                            .itemId(itemId)
                            .fileName(arr[1])
                            .ord(i)
                            .repImg(i == 0)
                            .build();
                    log.info("가져온 fileName을 itemImgVo로 담는 중.... ", itemImgVo);
                    itemMapper.saveItemImg(itemImgVo);
                    log.info("이미지 저장 완료. 저장된 이미지 수: {}", fileNames.size());
                }
            }
        } catch (Exception e) {
            log.error("상품 등록 중 오류 발생", e);
            throw new Exception("!!상품 저장 중 오류가 발생했습니다!!", e);
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
    public void deleteItem(Long itemId) {
        itemMapper.deleteItem(itemId);
    }

    /**
     * 상품 조회
     */
    @Override
    public ItemFormDto findItemById(Long itemId) {

        // itemId로 정보 조회
        ItemVo itemVo = itemMapper.findItemById(itemId).orElseThrow();
        List<ItemImgVo> itemImgs = itemMapper.findItemImgByItemId(itemId);
        List<OptionVo> options = itemMapper.findOptionByItemId(itemId);

        // ItemImgVo 리스트에서 파일 이름 목록 추출
        List<String> fileNames = itemImgs.stream()
                .map(img -> img.getUuid() + "_" + img.getFileName())
                .collect(Collectors.toList());

        // itemFormDto 생성
        ItemFormDto itemFormDto = ItemFormDto.builder()
                .itemId(itemVo.getItemId())
                .itemName(itemVo.getItemName())
                .categoryId(itemVo.getCategoryId())
                .price(itemVo.getPrice())
                .dcRate(itemVo.getDcRate())
                .itemDetail(itemVo.getItemDetail())
                .itemSellStatus(itemVo.getItemSellStatus())
                .itemGubun(itemVo.getItemGubun())
                .fileNames(fileNames)
                .options(options)
                .build();
        return itemFormDto;
    }

    /**
     * 옵션 Id로 옵션 조회
     */
    @Override
    public OptionVo findOptionByOptionId(Long optionId) {
        return itemMapper.findOptionByOptionId(optionId);
    }

    /**
     * 상품 Id로 상품 이미지 목록 조회
     */
    @Override
    public List<ItemImgVo> findItemImgByItemId(Long itemId) {
        return itemMapper.findItemImgByItemId(itemId);
    }

    /**
     * ItemFormDto로 상품, 이미지, 옵션 전부 수정
     */
    @Override
    public void updateItemWithImages(ItemFormDto itemFormDto) {

        // 상품 ID 가져오기
        Long itemId = itemFormDto.getItemId();

        // 1. 상품 정보 업데이트
        ItemVo itemVo = convertToItemVo(itemFormDto);
        itemMapper.updateItem(itemVo);

        // 2. 옵션 정보 업데이트
        List<OptionVo> currentOptions = itemMapper.findOptionByItemId(itemFormDto.getItemId());
        List<OptionVo> newOptions = itemFormDto.getOptions();
        // 기존 옵션 삭제
        for(OptionVo currentOption : currentOptions) {
            if(!newOptions.contains(currentOption)) {
                itemMapper.deleteOption(currentOption.getOptionId());
            }
        }
        // 새 옵션 추가 또는 업데이트
        for(OptionVo newOption : newOptions) {
            newOption.setItemId(itemId);
            if(currentOptions.contains(newOption)) {
                itemMapper.updateOption(newOption);
            } else {
                itemMapper.saveOption(newOption);
            }
        }

        // 3. 이미지 정보 업데이트
        List<ItemImgVo> currentImages = itemMapper.findItemImgByItemId(itemVo.getItemId());
        List<ItemImgVo> newImages = convertToImgVoList(itemFormDto);
        // 기존 이미지 삭제
        for (ItemImgVo currentImg : currentImages) {
            if (!newImages.contains(currentImg)) {
                itemMapper.deleteItemImg(currentImg.getUuid());
            }
        }
        // 새 이미지 추가 또는 업데이트
        for(ItemImgVo newImg : newImages) {
            if(currentImages.contains(newImg)) {
                itemMapper.updateItemImg(newImg);
            } else {
                itemMapper.saveItemImg(newImg);
            }
        }
    }


    /**
     * 전체 상품 목록 조회
     */
    @Override
    public List<ItemVo> findAllItems(Criteria cri) {
        int offset = (cri.getPageNum() -1) * cri.getAmount();
        List<ItemVo> items = itemMapper.findAllItems(offset, cri.getAmount());
        items.forEach(ItemVo::setRepImgFileName);
        return items;
    }

    /**
     * 전체 상품 갯수 가져오기
     */
    @Override
    public int countAllItems(){
        return itemMapper.countAllItems();
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

    // ItemFormDto -> ItemVo로 변환하는 메소드(repImgFileName, itemImgs 제외)
    private ItemVo convertToItemVo(ItemFormDto itemFormDto) {
        ItemVo itemVo = ItemVo.builder()
                .itemId(itemFormDto.getItemId())
                .categoryId(itemFormDto.getCategoryId())
                .itemName(itemFormDto.getItemName())
                .itemDetail(itemFormDto.getItemDetail())
                .price(itemFormDto.getPrice())
                .dcRate(itemFormDto.getDcRate())
                .itemSellStatus(itemFormDto.getItemSellStatus())
                .itemGubun(itemFormDto.getItemGubun())
                .options(itemFormDto.getOptions())
                .build();
        return itemVo;
    }

    // ItemFormDto -> ItemImgVoList로 변환하는 메소드
    private List<ItemImgVo> convertToImgVoList (ItemFormDto itemFormDto) {
        List<String> fileNames = itemFormDto.getFileNames();
        List<ItemImgVo> itemImgVoList = new ArrayList<>();

        if (fileNames != null && !fileNames.isEmpty()) {
            for (int i = 0; i < fileNames.size(); i++) {

                String fileName = fileNames.get(i);
                String[] arr = fileName.split("_", 2);

                // 잘못된 형식의 파일명 처리
                if (arr.length < 2) {
                    log.info("잘못된 형식의 파일명 : {}", fileName);
                    continue;
                }

                ItemImgVo newItemImg = ItemImgVo.builder()
                        .uuid(arr[0])
                        .itemId(itemFormDto.getItemId())
                        .fileName(arr[1])
                        .ord(i)
                        .repImg(i == 0)
                        .build();
                itemImgVoList.add(newItemImg);
            }
        }
        return itemImgVoList;
    }

}
