package io.github.rxcats.datasourceroutedemo.service;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.ds.MyTransactionManager;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private MyTransactionManager txManager;

    @Override
    public <T> T execute(String db, Supplier<T> fn) {
        try {
            DataSourceContextHolder.set(db);
            return fn.get();
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    @Override
    public <T> T executeTx(String db, Supplier<T> fn) {
        try {
            DataSourceContextHolder.set(db);
            txManager.start();
            T result = fn.get();
            txManager.commit();
            return result;
        } finally {
            txManager.close();
            DataSourceContextHolder.clear();
        }
    }

    @Override
    public <T> T executeRollback(String db, Supplier<T> fn) {
        try {
            DataSourceContextHolder.set(db);
            txManager.start();
            return fn.get();
        } finally {
            txManager.rollback();
            DataSourceContextHolder.clear();
        }
    }

}
