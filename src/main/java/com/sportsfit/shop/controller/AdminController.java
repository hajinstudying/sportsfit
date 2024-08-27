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
import org.apache.ibatis.annotations.Param;
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
     * 관리자 메인 페이지
     */
    @GetMapping("/main")
    public String showAdminMain(Model model, Criteria cri) {
        List<ItemVo> itemList = itemService.findAllItems(cri);
        int total = itemService.countAllItems();
        PageDto pageDto = new PageDto(cri, total);

        model.addAttribute("itemList", itemList);
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("cri", cri);
        return "admin/Index";
    }

    /**
     * 상품 등록폼
     */
    @GetMapping("/item/create.do")
    public String itemCreateForm(Model model) {

        model.addAttribute("itemFormDto", new ItemFormDto());

        // 카테고리 목록 전달
        List<CategoryVo> categories = categoryService.findParentCategory();
        model.addAttribute("categories", categories);

        // 상품 상태 목록 전달
        List<GubunSubVo> statusList = gubunService.findStatusByGubunCode("ITSS");
        model.addAttribute("statusList", statusList);

        // 하위 상품 구분 목록 전달
        List<GubunSubVo> itemGubunList = gubunService.findStatusByGubunCode("ITGU");
        model.addAttribute("itemGubunList", itemGubunList);

        return "admin/itemCreateForm";

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
        return "redirect:/admin/main"; // 상품 목록 페이지로 리다이렉트

    }

    /**
     * 상품 수정 폼
     */
    @GetMapping("/item/edit.do/{itemId}")
    public String itemEditForm(Model model, @PathVariable("itemId") Long itemId) {

        // itemId를 Model에 추가
        model.addAttribute("itemId", itemId);

        // itemFormDto 전달
        ItemFormDto itemFormDto  = itemService.findItemById(itemId);
        model.addAttribute("itemFormDto", itemFormDto);

        // 카테고리 목록 전달
        List<CategoryVo> categories = categoryService.findParentCategory();
        model.addAttribute("categories", categories);

        // 상품 상태 목록 전달
        List<GubunSubVo> statusList = gubunService.findStatusByGubunCode("ITSS");
        model.addAttribute("statusList", statusList);

        // 상품구분 목록 전달
        List<GubunSubVo> itemGubunList = gubunService.findStatusByGubunCode("ITGU");
        model.addAttribute("itemGubunList", itemGubunList);

        // 상품 이미지 목록 전달
        List<ItemImgVo> itemImgVoList = itemService.findItemImgByItemId(itemId);
        model.addAttribute("itemImgVoList", itemImgVoList);
        return "admin/itemEditForm";
    }

    /**
     * 상품 수정 처리
     */
    @PostMapping(value = "/item/edit.do/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateItemFormDto(
            @PathVariable("itemId") Long itemId,
            @ModelAttribute("itemFormDto") ItemFormDto itemFormDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws Exception {


        log.info("수정하려고 전달받은 ItemFormDto: {}", itemFormDto);

        if (bindingResult.hasErrors()) {
            log.error("상품수정 데이터 검증 오류: {}", bindingResult.getAllErrors());

            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMap.put(error.getField(), error.getDefaultMessage())
            );
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            redirectAttributes.addFlashAttribute("itemFormDto", itemFormDto);
            return "redirect:/admin/item/edit.do/" + itemId;
        }

        // 상품 수정
        itemService.updateItemWithImages(itemFormDto);
        redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
        return "redirect:/admin/main"; // 상품 목록 페이지로 리다이렉트
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



}
