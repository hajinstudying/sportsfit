package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.GubunMapper;
import com.sportsfit.shop.vo.GubunSubVo;
import com.sportsfit.shop.vo.GubunVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GubunServiceImpl implements GubunService{

    private final GubunMapper gubunMapper;

    /**
     * 상위 구분 목록 가져오기
     */
    @Override
    public List<GubunVo> findAllGubuns() {
        return gubunMapper.findAllGubuns();
    }

    /**
     * 상위 구분코드로 하위 구분 목록 가져오기
     */
    @Override
    public List<GubunSubVo> findStatusByGubunCode(String gubunCode) {
        return gubunMapper.findStatusByGubunCode(gubunCode);
    }

}
