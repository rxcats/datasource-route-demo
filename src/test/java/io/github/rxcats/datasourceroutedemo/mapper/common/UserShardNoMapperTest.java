package io.github.rxcats.datasourceroutedemo.mapper.common;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.TestData;
import io.github.rxcats.datasourceroutedemo.ds.MyTransactionManager;
import io.github.rxcats.datasourceroutedemo.entity.UserShardNo;
import io.github.rxcats.datasourceroutedemo.service.query.DbType;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelperImpl;

@Slf4j
@EnableAutoConfiguration
@SpringBootTest(classes = { UserShardNoMapper.class, CommonUserMapper.class, QueryHelperImpl.class, MyTransactionManager.class })
class UserShardNoMapperTest {

    @Resource
    private UserShardNoMapper userShardNoMapper;

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
    void selectOne() {
        queryHelper.execute(DbType.common, null, () -> commonUserMapper.insert(TestData.commonUser()));

        UserShardNo userShardNo = queryHelper.execute(DbType.common, null, () -> userShardNoMapper.selectOne("1000001"));
        log.info("{}", userShardNo);

        assertThat(userShardNo).isNotNull();
        assertThat(userShardNo.getShardNo()).isEqualTo(TestData.commonUser().getShardNo());
    }
}