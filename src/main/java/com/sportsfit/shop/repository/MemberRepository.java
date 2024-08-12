package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface MemberRepository {

    // 회원 저장
    int saveMember(MemberVo memberVo);

    // 이메일로 회원 조회
    MemberVo findMemberByEmail(String email);

    // memberId로 회원 조회
    Optional<MemberVo> findMemberById(Long memberId);

    // 회원정보 수정
    int updateMember(MemberVo memberVo);

    // 회원정보 삭제 (del 컬럼을 수정)
    int deleteMemberById(Long memberId);

    // 회원 목록 조회
    List<MemberVo> findAllMember();

    // 비밀번호와 소셜 상태 업데이트
    int updatePasswordAndSocial(@Param("email") String email, @Param("password") String encodedPassword);

}
