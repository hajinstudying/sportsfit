package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 하위 구분 Vo
 * - EUNM 타입의 상태코드들을 관리하기 쉽게 모아놓은 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GubunSubVo {

    private String gubunSubCode; // 하위구분코드
    private String gubunCode; // 상위 구분코드
    private String gubunSubName; // 하위구분명
    private String gubunSubDesc; // 하위구분 상세설명
    private int gubunSubOrder; // 하위구분 순서
    private boolean useFlag; // 사용상태

}
