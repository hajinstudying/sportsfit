package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.CategoryService;
import com.sportsfit.shop.service.GubunService;
import com.sportsfit.shop.vo.CategoryVo;
import com.sportsfit.shop.vo.GubunSubVo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 모든 컨트롤러에 navbar에 표시할 categories를 보내는 클래스
 * - categories 라는 이름으로 categoryMap을 보냄
 * - categoryMap은 <상위카테고리Vo, 그 하위카테고리Vo리스트> 형식으로 저장되어있음
 */

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final CategoryService categoryService;
    private final GubunService gubunService;

    @ModelAttribute("categories")
    public Map<CategoryVo, List<CategoryVo>> populateCategories() {

        // 대분류 카테고리 목록 생성
        List<CategoryVo> parentCategories = categoryService.findParentCategory();
        Map<CategoryVo, List<CategoryVo>> categoryMap = new HashMap<>();

        // 대분류 카테고리에 따른 하위 카테고리 목록 생성
        for (CategoryVo parentCategory : parentCategories) {
            List<CategoryVo> subCategories = categoryService.findCategoryByParentId(parentCategory.getCategoryId());
            if (subCategories == null) {
                subCategories = new ArrayList<>();
            }
            categoryMap.put(parentCategory, subCategories);
        }

        return categoryMap;
    }

    @ModelAttribute("itemGubuns")
    public List<GubunSubVo> populateItemGubuns() {

        // 상품 구분 목록 생성
        return gubunService.findStatusByGubunCode("ITGU");

    }
}
