package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 상태 코드들의 상위 카테고리
 * - EUNM 타입의 상태코드들을 관리하기 쉽게 모아놓은 클래스의 카테고리 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GubunVo {

    private String gubunCode; // 구분코드
    private String gubunDesc; // 구분카테고리 설명
    private String gubunName; // 구분 카테고리명
    private boolean useFlag; // 사용상태

}
