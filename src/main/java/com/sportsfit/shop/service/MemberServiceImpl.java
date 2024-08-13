package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.MemberMapper;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import lombok.RequiredArgsConstructor;
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
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    /**
     * 회원 저장
     * - 회원 중복 검사를 수행하고 중복이 아닐 경우에 회원 저장
     */
    @Override
    public MemberVo saveMember(MemberVo memberVo) {
        validateDuplicateMember(memberVo);
        memberMapper.saveMember(memberVo);
        // 회원 등록 시 부여받은 권한 저장
        for (RoleVo roleVo : memberVo.getRoles()) {
            memberMapper.insertMemberRole(new MemberRoleVo(memberVo.getMemberId(), roleVo.getRoleId()));
        }
        return memberVo;
    }

    /**
     * 회원 이메일 중복 검사
     */
    private void validateDuplicateMember(MemberVo memberVo) {
        Optional<MemberVo> findMember = memberMapper.findMemberByEmail(memberVo.getEmail());
        if(findMember.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    /**
     * 회원 권한 관계 저장
     */
    public int insertMemberRole(MemberRoleVo memberRoleVo){
        return memberMapper.insertMemberRole(memberRoleVo);
    };

    /**
     * 인증 메소드 (DB에서 이메일로 사용자 정보 조회)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일로 회원 조회
        MemberVo memberVo = memberMapper.findMemberByEmail(email).orElseThrow();
        if (memberVo == null) {
            throw new UsernameNotFoundException(email + " : 사용자 정보가 올바르지 않습니다.");
        }

        return new MemberSecurityDTO(memberVo);
    }

    /**
     * 아이디로 회원정보 조회
     */
    @Override
    public MemberVo findMemberById(Long memberId) {
        return memberMapper.findMemberById(memberId).orElseThrow();
    }

    /**
     * 이메일로 회원정보 조회
     */
    @Override
    public MemberVo findMemberByEmail(String email){
        return memberMapper.findMemberByEmail(email).orElseThrow();
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

