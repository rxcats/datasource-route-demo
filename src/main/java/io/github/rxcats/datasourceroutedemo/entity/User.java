package io.github.rxcats.datasourceroutedemo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {

    private String userId;

    private String nickname;

    private LocalDateTime createdAt;

}
