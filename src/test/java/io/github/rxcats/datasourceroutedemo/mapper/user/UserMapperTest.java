package io.github.rxcats.datasourceroutedemo.mapper.user;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.TestData;
import io.github.rxcats.datasourceroutedemo.ds.MyTransactionManager;
import io.github.rxcats.datasourceroutedemo.entity.User;
import io.github.rxcats.datasourceroutedemo.service.query.DbType;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelperImpl;

@Slf4j
@EnableAutoConfiguration
@SpringBootTest(classes = { UserMapper.class, QueryHelperImpl.class, MyTransactionManager.class })
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private QueryHelper queryHelper;

    @BeforeEach
    void setUp() {
        queryHelper.execute(DbType.user, 0, () -> userMapper.deleteAll());
        queryHelper.execute(DbType.user, 1, () -> userMapper.deleteAll());
    }

    @Test
    void insert() {
        int r0 = queryHelper.execute(DbType.user, 0, () -> userMapper.insert(TestData.user()));
        int r1 = queryHelper.execute(DbType.user, 1, () -> userMapper.insert(TestData.user()));
        log.info("r0:{}, r1:{}", r0, r1);

        assertThat(r0).isEqualTo(1);
        assertThat(r1).isEqualTo(1);
    }

    @Test
    void insertDuplicateKeyException() {
        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            queryHelper.execute(DbType.user, 0, () -> userMapper.insertOnly(TestData.user()));
            queryHelper.execute(DbType.user, 0, () -> userMapper.insertOnly(TestData.user()));
        });
    }

    @Test
    void insertDuplicateKeyExceptionTryCatch() {
        try {
            queryHelper.execute(DbType.user, 0, () -> userMapper.insertOnly(TestData.user()));
            queryHelper.execute(DbType.user, 0, () -> userMapper.insertOnly(TestData.user()));
        } catch (DuplicateKeyException e) {
            log.info(e.getMessage(), e);
            assertThat(e).hasMessageContaining("Duplicate entry");
        }
    }

    @Test
    void selectOne() {
        queryHelper.execute(DbType.user, 0, () -> userMapper.insert(TestData.user()));
        queryHelper.execute(DbType.user, 1, () -> userMapper.insert(TestData.user()));

        User r0 = queryHelper.execute(DbType.user, 0, () -> userMapper.selectOne("1000001"));
        User r1 = queryHelper.execute(DbType.user, 1, () -> userMapper.selectOne("1000001"));

        assertThat(r0).isNotNull();
        assertThat(r0.getUserId()).isEqualTo("1000001");

        assertThat(r1).isNotNull();
        assertThat(r1.getUserId()).isEqualTo("1000001");
    }

    @Test
    void delete() {
        queryHelper.execute(DbType.user, 0, () -> userMapper.insert(TestData.user()));
        queryHelper.execute(DbType.user, 1, () -> userMapper.insert(TestData.user()));

        queryHelper.execute(DbType.user, 0, () -> userMapper.delete("1000001"));
        queryHelper.execute(DbType.user, 1, () -> userMapper.delete("1000001"));

        User r0 = queryHelper.execute(DbType.user, 0, () -> userMapper.selectOne("1000001"));
        User r1 = queryHelper.execute(DbType.user, 1, () -> userMapper.selectOne("1000001"));

        assertThat(r0).isNull();
        assertThat(r1).isNull();
    }
}
