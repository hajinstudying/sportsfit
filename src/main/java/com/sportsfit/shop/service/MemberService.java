package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.MemberRepository;
import com.sportsfit.shop.repository.MemberRoleMapper;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import com.sportsfit.shop.vo.RoleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 시큐리티 인증 담당 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberRoleMapper memberRoleMapper;

    /**
     * 회원 저장
     * - 회원 중복 검사를 수행하고 중복이 아닐 경우에 회원 저장
     */
    public MemberVo saveMember(MemberVo memberVo) {
        validateDuplicateMember(memberVo);
        memberRepository.saveMember(memberVo);
        // 회원 등록 시 기본 역할 부여
        for (RoleVo roleVo : memberVo.getRoles()) {
            memberRoleMapper.insertMemberRole(new MemberRoleVo(memberVo.getMemberId(), roleVo.getRoleId()));
        }
        return memberVo;
    }

    /**
     * 회원 이메일 중복 검사
     */
    private void validateDuplicateMember(MemberVo memberVo) {
        MemberVo foundMember = memberRepository.findMemberByEmail(memberVo.getEmail());
        if (foundMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    /**
     * 인증 메소드 (DB에서 이메일로 사용자 정보 조회)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 이메일로 회원 조회
        MemberVo memberVo = memberRepository.findMemberByEmail(username);
        if (memberVo == null) {
            throw new UsernameNotFoundException(username + " : 사용자 정보가 올바르지 않습니다.");
        }

        // 권한 저장용 컬렉션
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 역할 정보를 데이터베이스에서 가져옴
        List<RoleVo> roles = memberRoleMapper.getRolesByMemberId(memberVo.getMemberId());
        memberVo.setRoles(roles); // Member 객체에 역할 설정

        // 권한 컬렉션에 실제 디비에서 조회한 권한 세팅
        for (RoleVo roleVo : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleVo.getRoleName())); // 각 역할을 권한으로 추가
        }

        // 인증 객체 생성
        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        memberVo.getMemberId(),
                        memberVo.getName(),
                        memberVo.getEmail(),
                        memberVo.getPassword(),
                        authorities
                );
        // 인증 객체에 추가 정보 세팅
        memberSecurityDTO.setDel(memberVo.isDel());
        memberSecurityDTO.setSocial(memberVo.isSocial());
        return memberSecurityDTO;
    }

    /**
     * 회원정보 조회
     */
    private MemberVo findMemberById(Long memberId) {
        return memberRepository.findMemberById(memberId).orElseThrow();
    }

    /**
     * 회원정보 수정
     */
    private int updateMember(MemberVo memberVo) {
        return memberRepository.updateMember(memberVo);
    }

    /**
     * 회원정보 삭제
     */
    private int deleteMemberById(Long memberId) {
        return memberRepository.deleteMemberById(memberId);
    }

    /**
     * 회원 목록 조회
     */
    private List<MemberVo> findAllMember() {
        return memberRepository.findAllMember();
    }

    /**
     * 소셜 로그인 사용자의 일반 사용자 전환 및 비밀번호 변경
     */
    @Transactional
    int updatePasswordAndSocial(String email, String encodedPassword) {
        return memberRepository.updatePasswordAndSocial(email, encodedPassword);
    }

}

