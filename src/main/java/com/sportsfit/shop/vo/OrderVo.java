package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 주문 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVo {

    private Long orderId; // 주문코드
    private Long memberId; // 주문한 회원코드
    private Long addressId; // 배송될 주소코드
    private int orderAmount; // 주문총액
    private String gubunSubCode; // 주문상태 (orderStatus)

    // 날짜 바인딩 패턴 : yyyy-MM-dd
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate; // 등록일자
}
