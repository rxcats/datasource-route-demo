package io.github.rxcats.datasourceroutedemo.util;

import lombok.NonNull;

public class UserIdShard {
    private UserIdShard() {

    }

    public static int get(@NonNull String userId) {
        int hash = userId.hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }
}
