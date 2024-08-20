package com.sportsfit.shop.service;

import com.sportsfit.shop.vo.GubunSubVo;
import com.sportsfit.shop.vo.GubunVo;

import java.util.List;

public interface GubunService {

    // 상위 구분코드 목록 조회
    List<GubunVo> findAllGubuns();

    // 구분코드별 하위 구분목록 조회
    List<GubunSubVo> findStatusByGubunCode(String gubunCode);

}
