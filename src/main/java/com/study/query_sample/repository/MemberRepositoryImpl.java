package com.study.query_sample.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.query_sample.dto.MemberDto;
import com.study.query_sample.dto.QMemberDto;
import com.study.query_sample.entity.Member;
import com.study.query_sample.entity.QMember;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.study.query_sample.entity.QMember.member;
import static com.study.query_sample.entity.QTeam.team;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    /**
     *
     * @param age
     * @return
     *
     * sql
     * SELECT m.*
     * FROM member m
     * WHERE m.age = age
     * ORDER BY m.username DESC;
     */
    @Override
    public List<Member> findByAgeOrderByUsername(Integer age) {
        return queryFactory
                .selectFrom(member)
                .where(member.age.eq(age))
                .orderBy(member.username.desc())
                .fetch();
    }

    @Override
    public List<Member> findByAge(Integer age) {
        return List.of();
    }

    @Override
    public List<MemberDto> findByTeam(String teamName) {
        return queryFactory
                .select(
                        new QMemberDto(
                                member.id,
                                member.username,
                                member.age
                        ))
                .from(member)
                .join(member.team, team)
                .where(team.name.eq(teamName))
                .fetch();
    }

    @Override
    public List<Member> findMemberBuilder(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if (username != null) {
            builder.and(member.username.eq(username));
        }

        if (age != null) {
            builder.and(member.age.eq(age));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Override
    public List<Member> findMemberParam(String username, Integer age) {
        return queryFactory
                .selectFrom(member)
                .where(
                        usernameEq(username),
                        ageEq(age)
                )
                .fetch();
    }

    // m.age > (
    //  select sub.age AVG()
    //    from member sub
    // where sub.team = team
    // )
    @Override
    public List<Member> findByAgeGreaterThanTeamAvgAge() {
        QMember memberSub = new QMember("memberSub");

        return queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(member.age.gt(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                                .where(memberSub.team.eq(team))
                ))
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return username != null ? member.username.eq(username) : null;
    }

    private BooleanExpression ageEq(Integer age) {
        return age != null ? member.age.eq(age) : null;
    }
}
