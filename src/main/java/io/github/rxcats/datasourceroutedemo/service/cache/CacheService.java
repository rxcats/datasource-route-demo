package io.github.rxcats.datasourceroutedemo.service.cache;

import io.github.rxcats.datasourceroutedemo.fn.DbOperation;

public interface CacheService {
    <T> T getCache(CacheKey cacheKey, String userId);

    <T> void setCache(CacheKey cacheKey, String userId, T data, long ttl);

    void removeCache(CacheKey cacheKey, String userId);

    <T> T getViaCache(CacheKey cacheKey, String userId, DbOperation<T> dbOperation, boolean useCache);

    <T> T getViaCache(CacheKey cacheKey, String userId, DbOperation<T> dbOperation);
}
