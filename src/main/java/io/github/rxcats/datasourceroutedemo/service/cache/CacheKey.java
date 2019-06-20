package io.github.rxcats.datasourceroutedemo.service.cache;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CacheKey {
    user("user", 300, ""),
    usershard("usershard", 86400, "")
    ;

    private final String prefix;
    private final long ttl;
    private final String desc;

    private static final String delimiter = ":";

    public String name(String userId) {
        return prefix + delimiter + userId;
    }

    public long ttl() {
        return ttl;
    }

    public String desc() {
        return desc;
    }
}
