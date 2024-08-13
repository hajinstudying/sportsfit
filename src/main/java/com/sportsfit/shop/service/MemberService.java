package com.sportsfit.shop.service;

import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface MemberService extends UserDetailsService {

    MemberVo saveMember(MemberVo memberVo);

    int insertMemberRole(MemberRoleVo memberRoleVo);

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    MemberVo findMemberById(Long memberId);

    MemberVo findMemberByEmail(String email);

    int updateMember(MemberVo memberVo);

    int deleteMemberById(Long memberId);

    List<MemberVo> findAllMember();

    List<MemberVo> findAllActiveMember();

    int updatePasswordAndSocial(String email, String encodedPassword);

}

