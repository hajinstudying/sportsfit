package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

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

    // 카테고리 id로 카테고리 정보 가져오기
    CategoryVo findCategoryByCategoryId(long categoryId);
}
