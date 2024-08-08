package com.sportsfit.shop.vo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Vo 클래스에서 중복되는 속성을 모은 추상클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseVo {

    private LocalDateTime regdate; // 등록일
    private LocalDateTime moddate; // 수정일
}
