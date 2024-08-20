package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.CategoryMapper;
import com.sportsfit.shop.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * 카테고리 담당 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private ModelMapper modelMapper;

    /**
     * 카테고리 추가
     */
    @Override
    public void saveCategory(CategoryVo categoryVo) {
        categoryMapper.saveCategory(categoryVo);
    }

    /**
     * 카테고리 수정
     */
    @Override
    public void updateCategory(CategoryVo categoryVo) {
        categoryMapper.updateCategory(categoryVo);
    }

    /**
     * 카테고리 삭제
     */
    @Override
    public void deleteCategory(long categoryId) {
        categoryMapper.deleteCategory(categoryId);
    }

    /**
     * 카테고리 대분류 목록 조회
     */
    @Override
    public List<CategoryVo> findParentCategory() {
        List<CategoryVo> categories = categoryMapper.findParentCategory();
        log.info("대분류 카테고리 : {}", categories);
        return categories;
    }

    /**
     * 카테고리 대분류별 하위 카테고리 조회
     */
    @Override
    public List<CategoryVo> findCategoryByParentId(long parentId) {
        List<CategoryVo> subCategories = categoryMapper.findCategoryByParentId(parentId);
        log.info("하위 카테고리 : {}", subCategories);
        return subCategories;
    }
}
