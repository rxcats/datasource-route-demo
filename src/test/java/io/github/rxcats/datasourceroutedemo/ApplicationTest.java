package io.github.rxcats.datasourceroutedemo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.entity.User;
import io.github.rxcats.datasourceroutedemo.mapper.common.UserMapper;

@Slf4j
@SpringBootTest
class ApplicationTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void crud() {
        DataSourceContextHolder.set("common");

        var u = new User();
        u.setUserId("user#1");
        u.setNickname("nickname#1");
        u.setCreatedAt(LocalDateTime.now());
        int insertCnt = userMapper.insert(u);
        assertThat(insertCnt).isEqualTo(1);

        User select = userMapper.selectOne("user#1");
        log.info("user:{}", select);
        assertThat(select).isNotNull();
        assertThat(select.getNickname()).isEqualTo("nickname#1");

        int deleteCnt = userMapper.delete("user#1");
        assertThat(deleteCnt).isEqualTo(1);

        DataSourceContextHolder.clear();
    }

}
