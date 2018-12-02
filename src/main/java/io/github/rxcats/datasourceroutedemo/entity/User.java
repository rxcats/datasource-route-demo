package io.github.rxcats.datasourceroutedemo.entity;

import lombok.Data;

@Data
public class User {

    private String userId;

    private String platformId;

    private int shardNo;

}
