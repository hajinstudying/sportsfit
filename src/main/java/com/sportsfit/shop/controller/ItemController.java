package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.ItemService;
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

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 구분별 상품 목록 화면
     */
    @GetMapping("/list.do/{itemGubun}")
    public String showItemList(Model model,
                               @PathVariable("itemGubun") String itemGubun,
                               Criteria cri) {

        List<ItemVo> itemList = itemService.findItemByItemGubun(itemGubun, cri);
        int total = itemService.countItemByItemGubun(itemGubun, cri);
        PageDto pageDto = new PageDto(cri, total);

        model.addAttribute("itemList", itemList);
        model.addAttribute("pageDto", pageDto);
        return "item/itemList";
    }

    /**
     * 카테고리별 상품 목록 화면
     */
    @GetMapping("/list.do/{categoryId}")
    public String showItemList(Model model,
                               @PathVariable("categoryId") Long categoryId,
                               Criteria cri) {

        List<ItemVo> itemList = itemService.findItemByCategoryId(categoryId, cri);
        int total = itemService.countItemByCategoryId(categoryId, cri);
        PageDto pageDto = new PageDto(cri, total);

        model.addAttribute("itemList", itemList);
        model.addAttribute("pageDto", pageDto);
        return "item/itemList";
    }
}
