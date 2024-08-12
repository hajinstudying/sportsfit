package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 후기게시판 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    private Long reviewId; // 후기코드
    private Long memberId; // 후기작성 회원코드
    private Long orderItemId; // 주문상품코드
    private String rTitle; // 후기 제목
    private String rContent; // 후기 내용

    // 날짜 바인딩 패턴 : yyyy-MM-dd
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate; // 등록일자

}
