package io.github.rxcats.datasourceroutedemo.service.query;

public interface ShardHelper {
    int newShardNo(String userId);

    int getShardNo(String userId);
}
