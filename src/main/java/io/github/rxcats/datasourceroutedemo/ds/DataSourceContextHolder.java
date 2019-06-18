package io.github.rxcats.datasourceroutedemo.ds;

import lombok.NonNull;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(@NonNull String dbType) {
        CONTEXT.set(dbType);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
