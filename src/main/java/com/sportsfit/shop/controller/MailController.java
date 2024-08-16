package com.sportsfit.shop.controller;

import com.sportsfit.shop.service.MailService;
import com.sportsfit.shop.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Mail 컨트롤러
 * - 이메일 전송 기능을 제공하는 컨트롤러
 * - /mail/send 경로로 POST 요청을 받아 이메일 전송
 * - 이메일 전송 성공 시 인증번호를 반환
 */
@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private MemberService memberService;

    /**
     * 이메일 전송
     * - 사용자가 기재한 이메일로 인증번호를 전송
     * - 이메일 전송 성공 시 인증번호를 사용자가 회원가입작업을 하고 있는 화면으로  반환
     * - 화면으로 전송된 인증번호는 사용자가 확인하고 입력한 번호와 비교해서 인증 성공/실패 여부를 판단
     */
    @PostMapping("/send.do/{email}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email) {
        String confirmNumber = mailService.generateConfirmNumber(); // 랜덤 숫자 생성
        try {
            mailService.sendEmail(email, confirmNumber); // 이메일 전송(인증번호 포함)

            log.info("이메일 전송 성공 인증번호 {}", confirmNumber);

        } catch (MessagingException e) {
            return new ResponseEntity<>("이메일 전송 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(confirmNumber, HttpStatus.OK);
    }

    /**
     * 이메일 중복 검사 컨트롤러
     */
    @GetMapping("/check.do/{email}")
    public boolean checkEmailDuplicate(@PathVariable("email") String email) {
        return memberService.isEmailDuplicate(email);
    }




}