package com.study.query_sample.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.study.query_sample.entity.Member;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private Integer age;

    @QueryProjection
    public MemberDto(Long id, String username, Integer age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }
}
