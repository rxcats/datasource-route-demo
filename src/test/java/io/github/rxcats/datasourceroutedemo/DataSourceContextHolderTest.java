package io.github.rxcats.datasourceroutedemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceContextHolder;

@Slf4j
@SpringBootTest
class DataSourceContextHolderTest {

    @Test
    void contextHolderIfNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DataSourceContextHolder.set(null));
    }

}
