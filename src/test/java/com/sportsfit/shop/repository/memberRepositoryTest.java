package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class memberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRoleMapper memberRoleMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 관리자 계정 만드는 테스트코드
     */
    @Test
    public void createAdminAndUserAccounts() {
        MemberVo admin = new MemberVo();
        admin.setName("Admin");
        admin.setEmail("admin@sportsfit.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setSocial(false);
        memberRepository.saveMember(admin);

        // Assign ROLE_ADMIN to admin
        MemberRoleVo adminRole = new MemberRoleVo();
        adminRole.setMemberId(admin.getMemberId());
        adminRole.setRoleId(1L); // Assuming 1 is the ID for ROLE_ADMIN
        memberRoleMapper.insertMemberRole(adminRole);

        // Create user account
        MemberVo user = new MemberVo();
        user.setName("User");
        user.setEmail("test@a.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setSocial(false);
        memberRepository.saveMember(user);

        // Assign ROLE_USER to user
        MemberRoleVo userRole = new MemberRoleVo();
        userRole.setMemberId(user.getMemberId());
        userRole.setRoleId(2L); // Assuming 2 is the ID for ROLE_USER
        memberRoleMapper.insertMemberRole(userRole);
    }
}
