package com.sportsfit.shop.vo;

import lombok.*;

/**
 * 문의댓글 Vo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnsVo extends BaseVo {

    private Long ans_id; // 댓글 코드
    private Long memberId; // 댓글쓴 회원코드
    private Long qnaId; // 댓글이 쓰인 문의코드
    private String ans_Content; // 댓글내용
    private int ansOrder; // 댓글순서 (대댓글 고려)
    private int ansIndent; // 들여쓰기 (대댓글 여부에 따른 들여쓰기)

}
