package io.github.rxcats.datasourceroutedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;
import io.github.rxcats.datasourceroutedemo.entity.User;
import io.github.rxcats.datasourceroutedemo.mapper.common.UserMapper;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class DatasourceRouteDemoApplication {

    @Autowired
    UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(DatasourceRouteDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return (args -> {
            DataSourceContextHolder.set("common");

            var u = new User();
            u.setUserId("user#1");
            u.setPlatformId("platform#1");
            u.setShardNo(0);
            userMapper.insert(u);

            var user = userMapper.selectOne("user#1");
            log.info("user:{}", user);

            DataSourceContextHolder.clear();
        });
    }
}
