package io.github.rxcats.datasourceroutedemo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceConfig;
import io.github.rxcats.datasourceroutedemo.ds.DataSourceProperties;

@Slf4j
@SpringBootTest(classes = { DataSourceConfig.class })
class DataSourcePropertiesTest {

    @Autowired
    private DataSourceProperties properties;

    @Test
    void test() {
        log.info("{}", properties);
        assertThat(properties).isNotNull();
    }
}
