package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.CategoryService;
import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.CategoryVo;
import com.sportsfit.shop.vo.Criteria;
import com.sportsfit.shop.vo.ItemVo;
import com.sportsfit.shop.vo.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.directory.SearchResult;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    /**
     * 상품 통합검색 목록 화면
     */
    @GetMapping("/search")
    public String showListByItemDetail(Criteria cri, Model model) {

        List<ItemVo> searchResults = itemService.findItemByItemDetail(cri);
        int total = itemService.countItemByItemDetail(cri);
        PageDto pageDto = new PageDto(cri, total);

        model.addAttribute("totalCount", total);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("cri", cri);

        return "item/searchItemList";

    }


    /**
     * 상품 구분별 상품 목록 화면
     */
    @GetMapping("/list.do/gubun/{itemGubun}")
    public String showListByItemGubun(Model model,
                               @PathVariable("itemGubun") String itemGubun,
                               Criteria cri) {

        List<ItemVo> itemList = itemService.findItemByItemGubun(itemGubun, cri);
        int total = itemService.countItemByItemGubun(itemGubun, cri);
        PageDto pageDto = new PageDto(cri, total);

        model.addAttribute("itemList", itemList);
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("cri", cri);
        model.addAttribute("itemGubun", itemGubun);

        return "item/itemList";

    }

    /**
     * 카테고리별 상품 목록 화면
     */
    @GetMapping("/list.do/category/{categoryId}")
    public String showListByCategory(Model model,
                               @PathVariable("categoryId") Long categoryId,
                               Criteria cri) {

        List<ItemVo> itemList = itemService.findItemByCategoryId(categoryId, cri);
        int total = itemService.countItemByCategoryId(categoryId, cri);
        PageDto pageDto = new PageDto(cri, total);

        // 브레드크럼브를 위한 카테고리 정보
        CategoryVo categoryVo = categoryService.findCategoryByCategoryId(categoryId);
        CategoryVo parentCategoryVo = null;
        if (categoryVo.getParentId() != null) {
            parentCategoryVo = categoryService.findCategoryByCategoryId(categoryVo.getParentId());
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("cri", cri);
        model.addAttribute("categoryVo", categoryVo);
        model.addAttribute("parentCategoryVo", parentCategoryVo);

        return "item/itemList";

    }
}
