package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 문의게시판 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaVo {

    private Long qnaId; // 문의코드
    private String qnaContent; // 문의내용
    private Boolean secret; // 비밀글여부
    private Long memberId; // 문의하는 회원코드
    private Long itemId; // 문의하는 상품코드

    // 날짜 바인딩 패턴 : yyyy-MM-dd
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate; // 등록일자

}
