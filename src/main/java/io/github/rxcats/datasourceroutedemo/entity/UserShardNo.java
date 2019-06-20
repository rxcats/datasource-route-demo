package io.github.rxcats.datasourceroutedemo.entity;

import lombok.Data;

@Data
public class UserShardNo {

    private String userId;

    private Integer shardNo;

    public static UserShardNo of(String userId, Integer shardNo) {
        var value = new UserShardNo();
        value.userId = userId;
        value.shardNo = shardNo;
        return value;
    }

}
