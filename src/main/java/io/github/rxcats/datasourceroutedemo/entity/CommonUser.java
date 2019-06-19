package io.github.rxcats.datasourceroutedemo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommonUser {

    private String userId;

    private String nickname;

    private Integer shardNo;

    private LocalDateTime createdAt;

}
