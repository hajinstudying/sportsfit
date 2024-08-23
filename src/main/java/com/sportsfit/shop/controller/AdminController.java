package com.sportsfit.shop.controller;

import com.sportsfit.shop.dto.ItemFormDto;
import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.service.CategoryService;
import com.sportsfit.shop.service.GubunService;
import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/item/create.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String itemCreate(@Valid @ModelAttribute ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws Exception {

        log.info("전달받은 ItemFormDto: {}", itemFormDto); // 로깅 추가

        if (bindingResult.hasErrors()) {
            log.error("상품등록 데이터 검증 오류: {}", bindingResult.getAllErrors());

            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMap.put(error.getField(), error.getDefaultMessage())
            );
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            redirectAttributes.addFlashAttribute("itemFormDto", itemFormDto);
            return "redirect:/admin/item/create.do";
        }

        itemService.saveItemWithImages(itemFormDto);
        redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 등록되었습니다.");
        return "redirect:/"; // 상품 목록 페이지로 리다이렉트

    }

}
