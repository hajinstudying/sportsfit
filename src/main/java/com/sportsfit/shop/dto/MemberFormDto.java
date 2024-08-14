package com.sportsfit.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * MemberFormDto 데이터 전송 전용및 밸리데이션용 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(min = 2, max = 30, message = "이름은 2자 이상 30자 이내로 입력하세요.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력입니다.")
    private String passwordConfirm;

}
