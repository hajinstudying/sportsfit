package com.sportsfit.shop.service;

import com.sportsfit.shop.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    // 카테고리 추가
    void saveCategory(CategoryVo categoryVo);

    // 카테고리 수정
    void updateCategory(CategoryVo categoryVo);

    // 카테고리 삭제
    void deleteCategory(long categoryId);

    // 카테고리 대분류 목록 조회
    List<CategoryVo> findParentCategory();

    // 카테고리 대분류별 하위 카테고리 목록 조회
    List<CategoryVo> findCategoryByParentId(long parentId);

}
