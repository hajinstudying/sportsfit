package com.sportsfit.shop.repository;

import com.sportsfit.shop.vo.MemberRoleVo;
import com.sportsfit.shop.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberRoleMapper {

    // 회원 ID로 역할 정보 조회
    List<RoleVo> getRolesByMemberId(Long memberId);

    // 회원-역할 관계 삽입
    int insertMemberRole(MemberRoleVo memberRoleVo);

    // 회원-역할 관계 삭제
    int deleteMemberRole(MemberRoleVo memberRoleVo);
}
