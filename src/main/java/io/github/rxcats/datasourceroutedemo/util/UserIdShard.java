package io.github.rxcats.datasourceroutedemo.util;

import java.util.List;

import lombok.NonNull;

public class UserIdShard {

    private UserIdShard() {

    }

    public static int get(@NonNull String userId, @NonNull List<Integer> targets) {
        int hash = userId.hashCode();
        return (hash < 0) ? targets.get((hash * -1) % targets.size()) : targets.get(hash % targets.size());
    }
}
