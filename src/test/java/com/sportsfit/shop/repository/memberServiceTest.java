package com.sportsfit.shop.repository;

import com.sportsfit.shop.service.MemberService;
import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootTest
public class memberServiceTest {

    @Autowired
    private MemberService memberService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 관리자 계정 만드는 테스트코드
     */
    @Test
    public void createAdminAccount() {
        MemberVo admin = new MemberVo();
        admin.setName("관리자");
        admin.setEmail("admin2@sportsfit.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setSocial(false);
        memberService.saveMember(admin);
        log.info("admin의 memberId : " + admin.getMemberId());

        // admin 계정에 ADMIN 권한 부여
        MemberRoleVo adminRole = new MemberRoleVo();
        adminRole.setMemberId(admin.getMemberId());
        adminRole.setRoleId(1L);
        memberService.insertMemberRole(adminRole);
    }

    @Test
    public void createUserAccount(){

        // 테스트 유저 계정 생성
        MemberVo user = new MemberVo();
        user.setName("홍길동");
        user.setEmail("tester@a.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setSocial(false);
        memberService.saveMember(user);

    }
}
