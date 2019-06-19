package io.github.rxcats.datasourceroutedemo.mapper.common;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.entity.IdGenerator;
import io.github.rxcats.datasourceroutedemo.service.query.DbType;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;

@Slf4j
@SpringBootTest
class IdGeneratorMapperTest {

    @Resource
    private IdGeneratorMapper idGeneratorMapper;

    @Autowired
    private QueryHelper queryHelper;

    @Test
    void generate() {
        var input = IdGenerator.of("test");
        var insert = queryHelper.execute(DbType.common, null, () -> idGeneratorMapper.generate(input));
        log.info("insert:{} input:{}", insert, input);
        assertThat(insert).isEqualTo(1);
        assertThat(input.getIdValue()).isNotNull();
    }

}
