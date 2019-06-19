package io.github.rxcats.datasourceroutedemo.service.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.ds.MyTransactionManager;
import io.github.rxcats.datasourceroutedemo.fn.DbOperation;

@Service
public class QueryHelperImpl implements QueryHelper {

    @Autowired
    private MyTransactionManager txManager;

    @Override
    public <T> T execute(String db, DbOperation<T> operation) {
        try {
            DataSourceContextHolder.set(db);
            return operation.run();
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    @Override
    public <T> T execute(DbType db, Integer shardNo, DbOperation<T> operation) {
        if (db == DbType.common) {
            return execute(db.name(), operation);
        } else {
            return execute(db.shard(shardNo), operation);
        }
    }

    @Override
    public <T> T executeTx(String db, DbOperation<T> operation) {
        try {
            DataSourceContextHolder.set(db);
            txManager.start();
            T result = operation.run();
            txManager.commit();
            return result;
        } finally {
            txManager.close();
            DataSourceContextHolder.clear();
        }
    }

    @Override
    public <T> T executeTx(DbType db, Integer shardNo, DbOperation<T> operation) {
        if (db == DbType.common) {
            return executeTx(db.name(), operation);
        } else {
            return executeTx(db.shard(shardNo), operation);
        }
    }

    @Override
    public <T> T executeRollback(String db, DbOperation<T> operation) {
        try {
            DataSourceContextHolder.set(db);
            txManager.start();
            return operation.run();
        } finally {
            txManager.rollback();
            DataSourceContextHolder.clear();
        }
    }

    @Override
    public <T> T executeRollback(DbType db, Integer shardNo, DbOperation<T> operation) {
        if (db == DbType.common) {
            return executeRollback(db.name(), operation);
        } else {
            return executeRollback(db.shard(shardNo), operation);
        }
    }

}
