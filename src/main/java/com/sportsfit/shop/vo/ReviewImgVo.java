package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 후기게시판에 첨부되는 이미지 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImgVo {

    private String rUuid; // uuid
    private Long reviewId; // 후기코드
    private String rFileName; // 이미지 파일이름
    private int rOrder; // 이미지 순서
    private boolean rRepImg; // 대표이미지 여부

}
