package io.github.rxcats.datasourceroutedemo.service.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.rxcats.datasourceroutedemo.fn.DbOperation;

@Service
public class CacheServiceImpl implements CacheService {
    private final ConcurrentHashMap<String, Object> cacheTable = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> cacheTimeInfo = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCache(CacheKey cacheKey, String userId) {
        return (T) cacheTable.get(cacheKey.name(userId));
    }

    @Override
    public <T> void setCache(CacheKey cacheKey, String userId, T data, long ttl) {
        cacheTable.put(cacheKey.name(userId), data);
        cacheTimeInfo.put(cacheKey.name(userId), System.currentTimeMillis() + ttl);
    }

    @Override
    public void removeCache(CacheKey cacheKey, String userId) {
        cacheTable.remove(cacheKey.name(userId));
        cacheTimeInfo.remove(cacheKey.name(userId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getViaCache(CacheKey cacheKey, String userId, DbOperation<T> dbOperation, boolean useCache) {
        var data = (useCache) ? (T) getCache(cacheKey, userId) : null;
        if (data == null) {
            data = dbOperation.run();
            if (useCache) {
                setCache(cacheKey, userId, data, cacheKey.ttl());
            }
        }
        return data;
    }

    @Override
    public <T> T getViaCache(CacheKey cacheKey, String userId, DbOperation<T> dbOperation) {
        return getViaCache(cacheKey, userId, dbOperation, true);
    }

    @Scheduled(fixedDelay = 1000)
    private void clean() {
        long now = System.currentTimeMillis();

        List<String> targets = cacheTimeInfo.entrySet().stream()
            .filter(v -> v.getValue() <= now)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        for (String key : targets) {
            cacheTimeInfo.remove(key);
            cacheTable.remove(key);
        }
    }

}
