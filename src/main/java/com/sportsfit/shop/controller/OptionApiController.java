package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.ItemService;
import com.sportsfit.shop.vo.OptionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품 상세 페이지에서 옵션 선택시
 * 옵션목록에 추가시킬 옵션 정보를 전달하는 컨트롤러
 */
@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
@Slf4j
public class OptionApiController {

    private final ItemService itemService;

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionVo> getOptionDetails(@PathVariable("optionId") Long optionId) {
        OptionVo optionVo = itemService.findOptionByOptionId(optionId);
        if(optionVo != null) {
            //log.info("찾은 optionVo: {}", optionVo);
            return ResponseEntity.ok(optionVo);
        } else {
            //log.info("옵션Id로 옵션 못찾음: {}", optionId);
            return ResponseEntity.notFound().build();
        }
    }
}
