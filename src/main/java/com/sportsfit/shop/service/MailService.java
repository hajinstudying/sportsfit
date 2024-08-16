package com.sportsfit.shop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail; // application.properties에서 발신자 이메일 주소를 읽어옴

    // 인증 번호 생성 메서드
    public String generateConfirmNumber() {
        int number = (int) (Math.random() * (900000)) + 100000; // 100000 ~ 999999 사이의 숫자
        log.info("MailService generateConfirmNumber 메소드 생성 번호 number : " + number);
        return String.valueOf(number); // 인증 번호 반환
    }

    // 이메일 생성 메서드
    public MimeMessage createMail(String mail, String confirmNumber) {

        log.info("createMail 메서드 실행됨");
        MimeMessage message = mailSender.createMimeMessage();

        try {

            // 발신자 이메일 주소 설정
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(mail);

            // 이메일 제목 설정
            helper.setSubject("SPORTSFIT에서 보낸 회원가입 이메일 인증입니다");

            // 이메일 본문 내용 설정 (HTML 형식)
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1 style='color:blue;'>" + confirmNumber + "</h1>"; // 인증 번호를 강조하여 표시
            body += "<p>" + "해당 인증 번호를 인증 창에 입력해 주세요." + "</p>";
            body += "<br>";
            body += "<p>" + "감사합니다." + "</p>";

            // 이메일 본문을 HTML로 설정
            helper.setText(body, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 생성된 MimeMessage 리턴
        return message;
    }

    // 이메일 전송 메서드
    public void sendEmail(String to, String confirmNumber) throws MessagingException {
        MimeMessage message = createMail(to, confirmNumber); // 이메일 생성
        mailSender.send(message); // 이메일 전송
    }
}