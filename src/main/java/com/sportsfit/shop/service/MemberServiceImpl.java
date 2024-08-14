package com.sportsfit.shop.service;

import com.sportsfit.shop.dto.MemberFormDto;
import com.sportsfit.shop.repository.MemberMapper;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 시큐리티 인증 담당 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;

    /**
     * 회원 저장
     * - 회원 중복 검사를 수행하고 중복이 아닐 경우에 회원 저장
     */
    @Override
    public void saveMember(MemberVo memberVo) {

        log.info("MemberServiceImple의 saveMember 실행됨");
        // 이메일 중복검사
        // validateDuplicateMember(memberVo);
        // 회원 저장
        memberMapper.saveMember(memberVo);
        log.info("saveMember메소드의 memberMapper.saveMember 실행됨");
        // 회원 역할 저장
        memberMapper.insertMemberRole(new MemberRoleVo(memberVo.getMemberId(), 2L)); // 2L = "USER"
        log.info("saveMember 메소드의 memberMapper.insertMemberRole 실행됨");

    }

    /**
     * 회원 이메일 중복 검사
     */
    private void validateDuplicateMember(MemberVo memberVo) {
        MemberVo findMember = memberMapper.findMemberByEmail(memberVo.getEmail());
    }

    /**
     * 회원 권한 관계 저장
     */
    @Override
    public int insertMemberRole(MemberRoleVo memberRoleVo){
        return memberMapper.insertMemberRole(memberRoleVo);
    };

    /**
     * 인증 메소드 (DB에서 이메일로 사용자 정보 조회)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일로 회원 조회
        MemberVo member = memberMapper.findMemberByEmail(email);
        return new MemberSecurityDTO(member);
    }

    /**
     * 아이디로 회원정보 조회
     */
    @Override
    public MemberVo findMemberById(Long memberId) {

        return memberMapper.findMemberById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId + " : 사용자 정보가 올바르지 않습니다."));
    }

    /**
     * 이메일로 회원정보 조회
     */
    @Override
    public MemberVo findMemberByEmail(String email){
        return memberMapper.findMemberByEmail(email);
    };

    /**
     * 회원정보 수정
     */
    @Override
    public int updateMember(MemberVo memberVo) {
        return memberMapper.updateMember(memberVo);
    }

    /**
     * 회원정보 삭제
     */
    @Override
    public int deleteMemberById(Long memberId) {
        return memberMapper.deleteMemberById(memberId);
    }

    /**
     * 전체 회원 목록 조회
     */
    @Override
    public List<MemberVo> findAllMember() {
        return memberMapper.findAllMembers();
    }

    /**
     * 탈퇴하지 않은 회원 목록 조회
     */
    @Override
    public List<MemberVo> findAllActiveMember() {
        return memberMapper.findAllMembers();
    }

    /**
     * 소셜 로그인 사용자의 일반 사용자 전환 및 비밀번호 변경
     */
    @Override
    @Transactional
    public int updatePasswordAndSocial(String email, String encodedPassword) {
        return memberMapper.updatePasswordAndSocial(email, encodedPassword);
    }

}

