package io.github.rxcats.datasourceroutedemo.util;

import java.util.List;

import lombok.NonNull;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceProperties;

public class UserIdShard {

    private static List<Integer> shardTargets;
    private static int shardTargetsCount;

    public UserIdShard(DataSourceProperties properties) {
        shardTargets = properties.getShardTargets();
        shardTargetsCount = properties.getShardTargets().size();
    }

    private static int getShard(int hash) {
        return shardTargets.get(hash % shardTargetsCount);
    }

    public static int get(@NonNull String userId) {
        int hash = userId.hashCode();
        return (hash < 0) ? getShard(hash * -1) : getShard(hash);
    }
}
