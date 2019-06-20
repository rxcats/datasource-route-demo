package io.github.rxcats.datasourceroutedemo.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceProperties;
import io.github.rxcats.datasourceroutedemo.entity.UserShardNo;
import io.github.rxcats.datasourceroutedemo.exception.NotFoundUserException;
import io.github.rxcats.datasourceroutedemo.fn.DbOperation;
import io.github.rxcats.datasourceroutedemo.mapper.common.UserShardNoMapper;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheKey;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheService;

@Slf4j
@SpringBootTest(classes = { ShardHelperImpl.class })
class ShardHelperTest {

    @Autowired
    private ShardHelper shardHelper;

    @MockBean
    private DataSourceProperties properties;

    @MockBean
    private UserShardNoMapper userShardNoMapper;

    @MockBean
    private CacheService cacheService;

    @MockBean
    private QueryHelper queryHelper;

    @Test
    void newShardNo() {
        when(properties.getShardTargets())
            .thenReturn(List.of(0, 1));

        var sum = new int[]{ 0, 0 };

        IntStream.rangeClosed(100001, 200000)
            .forEach(i -> {
                int shardNo = shardHelper.newShardNo(i + "");
                sum[shardNo]++;
            });
        log.info("{}", sum);
        assertThat(sum[0]).isEqualTo(50001);
        assertThat(sum[1]).isEqualTo(49999);
    }

    @Test
    void getShardNo() {
        when(cacheService.getViaCache(any(CacheKey.class), anyString(), any(DbOperation.class)))
            .thenReturn(UserShardNo.of("100001", 0));

        int shardNo0 = shardHelper.getShardNo("100001");
        assertThat(shardNo0).isEqualTo(0);

        when(cacheService.getViaCache(any(CacheKey.class), anyString(), any(DbOperation.class)))
            .thenReturn(UserShardNo.of("100002", 1));

        int shardNo1 = shardHelper.getShardNo("100002");
        assertThat(shardNo1).isEqualTo(1);
    }

    @Test
    void getShardNoNotFoundUserException() {
        when(cacheService.getViaCache(any(CacheKey.class), anyString(), any(DbOperation.class)))
            .thenReturn(null);

        Assertions.assertThrows(NotFoundUserException.class, () -> {
            int shardNo0 = shardHelper.getShardNo("100001");
            assertThat(shardNo0).isEqualTo(0);
        });
    }

}
