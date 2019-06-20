package io.github.rxcats.datasourceroutedemo.entity;

import lombok.Data;

@Data
public class UserShardNo {

    private String userId;

    private Integer shardNo;

}
