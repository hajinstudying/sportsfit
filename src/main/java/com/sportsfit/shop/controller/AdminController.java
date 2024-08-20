package com.sportsfit.shop.controller;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.service.CategoryService;
import com.sportsfit.shop.service.GubunService;
import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;
    private final GubunService gubunService;
    private final CategoryService categoryService;

    /**
     * 상품 등록폼
     */
    @GetMapping("/item/create.do")
    public String itemCreateForm(Model model) {

        model.addAttribute("itemFormDto", new ItemFormDto());

        List<CategoryVo> categories = categoryService.findParentCategory();
        model.addAttribute("categories", categories);

        List<GubunSubVo> statusList = gubunService.findStatusByGubunCode("ITSS");
        model.addAttribute("statusList", statusList);

        List<GubunSubVo> itemGubunList = gubunService.findStatusByGubunCode("ITGU");
        model.addAttribute("itemGubunList", itemGubunList);

        return "admin/itemCreateForm";

    }

    /**
     * 소분류 카테고리 목록 가져오기
     */
    @GetMapping("/categories/{categoryId}")
    @ResponseBody
    public List<CategoryVo> getCategories(@PathVariable("categoryId") long categoryId) {
        List<CategoryVo> categories = categoryService.findCategoryByParentId(categoryId);
        log.info("가져온 하위카테 목록 : {}", categories);
        return categories;
    }

    /**
     * 상품 등록 처리
     */
    @PostMapping("/item/create.do")
    public String itemCreate(@ModelAttribute("itemFormDto") ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("상품 등록 데이터 검증 오류 있음");

            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMap.put(error.getField(), error.getDefaultMessage())
            );
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            redirectAttributes.addFlashAttribute("itemFormDto", itemFormDto);
            return "redirect:/admin/item/create.do";
        }

        try {
            // 상품 등록
            itemService.saveItemWithImages(itemFormDto);
            log.info("상품 등록 : {}", itemFormDto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            log.info("예외발생!! " + e.getMessage());
            return "redirect:/admin/item/create.do";
        }

        return "redirect:/admin/item/list.do";
    }
}
