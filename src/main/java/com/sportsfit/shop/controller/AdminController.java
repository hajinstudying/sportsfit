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
        //log.info("가져온 하위카테 목록 : {}", categories);
        return categories;
    }

    /**
     * 상품 등록 처리
     */
    @PostMapping("/item/create.do")
    @ResponseBody
    public Map<String, Object> itemCreate(@ModelAttribute ItemFormDto itemFormDto,
                                          BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if(bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("errors", getErrors(bindingResult));
            return response;
        }

        try {
            itemService.saveItemWithImages(itemFormDto);
            response.put("success", true);
            response.put("message", "상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("상품 등록 중 예외 발생", e);
            response.put("success", false);
            response.put("message", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}
