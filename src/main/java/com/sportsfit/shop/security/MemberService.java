package com.sportsfit.shop.security;

import com.sportsfit.shop.repository.MemberRoleMapper;
import com.sportsfit.shop.repository.MemberRepository;
import com.sportsfit.shop.security.dto.MemberSecurityDTO;
import com.sportsfit.shop.vo.Member;
import com.sportsfit.shop.vo.MemberRole;
import com.sportsfit.shop.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 시큐리티 인증 담당 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberRoleMapper memberRoleMapper; // 역할 정보를 가져오기 위한 매퍼 추가

    /**
     * 회원 저장
     * - 중복 회원 검사를 수행하고, 중복이 아닐 경우 회원을 저장하며 기본 역할을 부여합니다.
     */
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        // 회원 등록 시 기본 역할 부여
        for (Role role : member.getRoles()) {
            memberRoleMapper.insertMemberRole(new MemberRole(member.getId(), role.getId()));
        }
        return member;
    }

    /**
     * 중복 회원 검사
     * - 이미 가입된 회원이 있는지 검사합니다.
     */
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /**
     * 인증 메소드
     * - 데이터베이스에 사용자의 ID(username)으로 조회해서 비밀번호와 기타 정보 얻음.
     * - 얻은 정보를 MemberSecurityDTO라는 시큐리티 정보 보관 객체에 담아서 반환.
     * - 이렇게 반환된 객체는 UserDetailsService를 호출한 AuthenticationProvider
     * 를 거쳐서 AuthenticationManager를 거쳐서 AuthenticationFilter 거쳐서
     * SecurityContext에 저장된다.
     * - UserDetailsService -> AuthenticationProvider -> AuthenticationManager
     * SecurityContext -> HTTPSession에 보관
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        // 권한 저장용 컬렉션
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 역할 정보를 데이터베이스에서 가져옴
        List<Role> roles = memberRoleMapper.getRolesByMemberId(member.getId());
        member.setRoles(roles); // Member 객체에 역할 설정

        // 권한 컬렉션에 실제 디비에서 조회한 권한 세팅
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())); // 각 역할을 권한으로 추가
        }

        // 인증 객체 생성
        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getEmail(),
                        member.getPassword(),
                        authorities,
                        member.getName()
                );
        // 생성된 인증 객체에 추가 정보 세팅(인증 보다는 덜 중요한 정보들)
        memberSecurityDTO.setDel(member.isDel());
        memberSecurityDTO.setSocial(member.isSocial()); // 소셜 로그인 여부에 따른 설정

        return memberSecurityDTO;
    }

    /**
     * 카카오 소셜 로그인 사용자 비밀번호 변경.
     * 카카오 소셜 로그인 사용자의 social 컬럼값을 0(false)로 변경
     * 즉, 일반사용자로 전환되서 아이디/비밀번호로 로그인 가능.
     */
    @Transactional
    public void modifyPasswordAndSocialStatus(String email, String encodedPassword) {
        // 비밀번호 변경
        memberRepository.updatePasswordAndSocial(email, encodedPassword);
    }

    /**
     * 멤버 조회
     * - CartController에서 장바구니 아이템 삭제시 사용
     * - MyBatis를 이용하여 회원 정보를 조회합니다.
     */
//    public Member getMember(Long id) throws Exception {
//        return Optional.ofNullable(memberRepository.findById(id))
//                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
//    }
}
