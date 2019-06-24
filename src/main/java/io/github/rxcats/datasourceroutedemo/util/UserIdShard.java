package io.github.rxcats.datasourceroutedemo.util;

import java.util.List;

import lombok.NonNull;

public class UserIdShard {

    private UserIdShard() {

    }

    public static int get(@NonNull String userId, @NonNull List<Integer> targets) {
        return targets.get(Math.abs(userId.hashCode()) % targets.size());
    }
}
