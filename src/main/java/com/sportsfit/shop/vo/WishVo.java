package com.sportsfit.shop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 찜 Vo
 * - 회원이 마음에 드는 상품에 북마크한 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishVo extends BaseVo {

    private Long wishId; // 찜코드
    private Long memberId; // 회원코드
    private Long itemId;  // 상품코드

}
