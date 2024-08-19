package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.GubunSubVo;
import com.sportsfit.shop.vo.GubunVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GubunMapper {

    // 상위 구분코드 목록 조회
    List<GubunVo> findAllGubuns();

    // 구분코드별 하위 구분목록 조회
    List<GubunSubVo> findStatusByGubunCode(String gubunCode);

}
