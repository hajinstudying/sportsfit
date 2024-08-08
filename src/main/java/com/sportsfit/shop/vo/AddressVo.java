package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 주소 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressVo {

    private Long addressId; // 주소코드
    private String deliName; // 수령인
    private String deliPhone; // 수령인 전화번호
    private String postalCode; // 우편번호
    private String deliAddr; // 기본주소
    private String deliAddr2; // 상세주소 (선택)
    private String deliMemo; // 배송메모

}
