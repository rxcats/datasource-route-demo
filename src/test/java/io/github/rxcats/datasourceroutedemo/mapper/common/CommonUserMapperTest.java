package io.github.rxcats.datasourceroutedemo.mapper.common;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.TestData;
import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.entity.CommonUser;
import io.github.rxcats.datasourceroutedemo.service.query.DbType;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;

@Slf4j
@SpringBootTest
class CommonUserMapperTest {

    @Resource
    private CommonUserMapper commonUserMapper;

    @Autowired
    private QueryHelper queryHelper;

    @BeforeEach
    void before() {
        queryHelper.execute(DbType.common, null, () -> {
            commonUserMapper.deleteAll();
            return null;
        });
    }

    @Test
    void contextHolderIfNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DataSourceContextHolder.set(null));
    }

    @Test
    void crud() {
        queryHelper.execute(DbType.common, null, () -> {
            int insertCnt = commonUserMapper.insert(TestData.commonUser());
            assertThat(insertCnt).isEqualTo(1);

            CommonUser select = commonUserMapper.selectOne("1000001");
            log.info("user:{}", select);
            assertThat(select).isNotNull();
            assertThat(select.getNickname()).isEqualTo("Guest1000001");

            int deleteCnt = commonUserMapper.delete("1000001");
            assertThat(deleteCnt).isEqualTo(1);

            return null;
        });
    }

    @Test
    void txInsert() {
        queryHelper.executeTx(DbType.common, null, () -> {
            int insertCnt = commonUserMapper.insert(TestData.commonUser());
            assertThat(insertCnt).isEqualTo(1);
            return null;
        });

        queryHelper.execute(DbType.common, null, () -> {
            CommonUser user = commonUserMapper.selectOne("1000001");
            log.info("{}", user);
            assertThat(user).isNotNull();
            return null;
        });
    }

    @Test
    void txRollback() {
        queryHelper.executeRollback(DbType.common, null, () -> {
            int insertCnt = commonUserMapper.insert(TestData.commonUser());
            log.info("{}", insertCnt);
            return null;
        });

        queryHelper.execute(DbType.common, null, () -> {
            CommonUser user = commonUserMapper.selectOne("1000001");
            log.info("{}", user);
            assertThat(user).isNull();
            return null;
        });
    }

    @Test
    void invalidInsertSql() {
        Assertions.assertThrows(BadSqlGrammarException.class, () ->
            queryHelper.execute(DbType.common, null, () -> {
                int insertCnt = commonUserMapper.errorInsert(TestData.commonUser());
                log.info("{}", insertCnt);
                return null;
            })
        );
    }

}
