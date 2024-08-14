package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    // 회원 저장
    void saveMember(MemberVo memberVo);

    // 회원 권한 관계 저장
    int insertMemberRole(MemberRoleVo memberRoleVo);

    // 이메일로 회원 조회
    MemberVo findMemberByEmail(String email);

    // memberId로 회원 조회
    Optional<MemberVo> findMemberById(Long memberId);

    // 회원정보 수정
    int updateMember(MemberVo memberVo);

    // 회원정보 삭제 (del 컬럼을 수정)
    int deleteMemberById(Long memberId);

    // 회원 역할 관계 삭제
    int deleteMemberRole(MemberRoleVo memberRoleVo);

    // 전체 회원 목록 조회
    List<MemberVo> findAllMembers();

    // 탈퇴하지 않은 회원 목록 조회
    List<MemberVo> findAllActiveMembers();

    // 비밀번호와 소셜 상태 업데이트
    int updatePasswordAndSocial(@Param("email") String email, @Param("password") String encodedPassword);

}
