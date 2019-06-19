package io.github.rxcats.datasourceroutedemo.service.query;

import io.github.rxcats.datasourceroutedemo.fn.DbOperation;

public interface QueryHelper {
    <T> T execute(DbType db, Integer shardNo, DbOperation<T> operation);

    <T> T execute(String db, DbOperation<T> operation);

    <T> T executeTx(DbType db, Integer shardNo, DbOperation<T> operation);

    <T> T executeTx(String db, DbOperation<T> operation);

    <T> T executeRollback(DbType db, Integer shardNo, DbOperation<T> operation);

    <T> T executeRollback(String db, DbOperation<T> operation);
}
