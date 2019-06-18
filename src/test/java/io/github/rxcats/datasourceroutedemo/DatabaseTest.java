package io.github.rxcats.datasourceroutedemo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.entity.User;
import io.github.rxcats.datasourceroutedemo.mapper.common.UserMapper;
import io.github.rxcats.datasourceroutedemo.service.QueryService;

@Slf4j
@SpringBootTest
class DatabaseTest {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private QueryService queryService;

    private User create() {
        var u = new User();
        u.setUserId("1000001");
        u.setNickname("Guest1000001");
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }

    @BeforeEach
    void before() {
        queryService.execute("common", () -> {
            userMapper.deleteAll();
            return null;
        });
    }

    @Test
    void contextHolderIfNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DataSourceContextHolder.set(null));
    }

    @Test
    void crud() {
        queryService.execute("common", () -> {
            int insertCnt = userMapper.insert(create());
            assertThat(insertCnt).isEqualTo(1);

            User select = userMapper.selectOne("1000001");
            log.info("user:{}", select);
            assertThat(select).isNotNull();
            assertThat(select.getNickname()).isEqualTo("Guest1000001");

            int deleteCnt = userMapper.delete("1000001");
            assertThat(deleteCnt).isEqualTo(1);

            return null;
        });
    }

    @Test
    void txInsert() {
        queryService.executeTx("common", () -> {
            int insertCnt = userMapper.insert(create());
            assertThat(insertCnt).isEqualTo(1);
            return null;
        });

        queryService.execute("common", () -> {
            User user = userMapper.selectOne("1000001");
            log.info("{}", user);
            assertThat(user).isNotNull();
            return null;
        });
    }

    @Test
    void txRollback() {
        queryService.executeRollback("common", () -> {
            int insertCnt = userMapper.insert(create());
            log.info("{}", insertCnt);
            return null;
        });

        queryService.execute("common", () -> {
            User user = userMapper.selectOne("1000001");
            log.info("{}", user);
            assertThat(user).isNull();
            return null;
        });
    }

    @Test
    void invalidInsertSql() {
        Assertions.assertThrows(BadSqlGrammarException.class, () ->
            queryService.execute("common", () -> {
                int insertCnt = userMapper.errorInsert(create());
                log.info("{}", insertCnt);
                return null;
            })
        );
    }

    private int getHashCode(@NonNull String userId) {
        int hash = userId.hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }

    @Test
    void shard() {
        Map<Integer, Integer> sum = new HashMap<>();
        LongStream.rangeClosed(1_000_001, 9_000_000).forEach(i -> {
            int shard = getHashCode(i + "") % 2;

            Integer cnt = sum.get(shard);
            if (cnt == null) {
                cnt = 0;
            }
            cnt++;

            sum.put(shard, cnt);
        });
        log.info("sum:{}", sum);
        assertThat(sum).hasSize(2);
        assertThat(sum.get(0)).isEqualTo(4_000_000);
        assertThat(sum.get(1)).isEqualTo(4_000_000);
    }

}
