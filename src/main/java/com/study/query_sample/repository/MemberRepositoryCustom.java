package com.study.query_sample.repository;

import com.study.query_sample.dto.MemberDto;
import com.study.query_sample.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findByAgeOrderByUsername(Integer age);
    List<Member> findByAge(Integer age);
    List<MemberDto> findByTeam(String teamName);
    List<Member> findMemberBuilder(String username, Integer age);
    List<Member> findMemberParam(String username, Integer age);
    List<Member> findByAgeGreaterThanTeamAvgAge();

}
