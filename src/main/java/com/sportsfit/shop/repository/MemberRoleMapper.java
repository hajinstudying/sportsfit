package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.MemberRole;
import com.sportsfit.shop.vo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberRoleMapper {

    // 회원 ID로 역할 정보 조회
    List<Role> getRolesByMemberId(Long memberId);

    // 회원-역할 관계 삽입
    int insertMemberRole(MemberRole memberRole);

    // 회원-역할 관계 삭제
    int deleteMemberRole(MemberRole memberRole);
}
