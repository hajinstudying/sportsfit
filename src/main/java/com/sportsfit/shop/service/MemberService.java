package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MemberService extends UserDetailsService {

    // 회원가입폼을 통한 회원 저장
    void saveMember(MemberVo memberVo);

    // 회원권한 저장
    int insertMemberRole(MemberRoleVo memberRoleVo);

    // 인증메소드
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    // 회원 Id를 통한 회원 조회
    Optional<MemberVo> findMemberById(Long memberId);

    // 회원 email을 통한 회원 조회
    Optional<MemberVo> findMemberByEmail(String email);

    // 회원 정보 수정
    int updateMember(MemberVo memberVo);

    // 회원 삭제처리
    int deleteMemberById(Long memberId);

    // 모든 회원 목록 조회
    List<MemberVo> findAllMember();

    // 활성회원 목록 조회
    List<MemberVo> findAllActiveMember();

    // 소셜설정과 비밀번호 수정
    int updatePasswordAndSocial(String email, String encodedPassword);

}

