package io.github.rxcats.datasourceroutedemo.service;

import java.util.function.Supplier;

public interface QueryService {
    <T> T execute(String db, Supplier<T> fn);

    <T> T executeTx(String db, Supplier<T> fn);

    <T> T executeRollback(String db, Supplier<T> fn);
}
