package io.github.rxcats.datasourceroutedemo.service.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.datasourceroutedemo.TestData;
import io.github.rxcats.datasourceroutedemo.entity.CommonUser;

@Slf4j
@SpringBootTest(classes = CacheServiceImpl.class)
class CacheServiceTest {

    @Autowired
    private CacheServiceImpl cacheService;

    @BeforeEach
    void setUp() {
        var testData = new ConcurrentHashMap<String, Object>();
        testData.put(CacheKey.user.name("1000001"), TestData.commonUser());
        ReflectionTestUtils.setField(cacheService, "cacheTable", testData);

        var testDataTimeInfo = new ConcurrentHashMap<String, Long>();
        testDataTimeInfo.put(CacheKey.user.name("1000001"), System.currentTimeMillis() + CacheKey.user.ttl());
        ReflectionTestUtils.setField(cacheService, "cacheTimeInfo", testDataTimeInfo);
    }

    @Test
    void getCache() {
        var cache = (CommonUser) cacheService.getCache(CacheKey.user, "1000001");
        log.info("{}", cache);

        assertThat(cache).isNotNull();
        assertThat(cache.getUserId()).isEqualTo("1000001");
        assertThat(cache.getNickname()).isEqualTo("Guest1000001");
    }

    @Test
    void setCache() {
        cacheService.setCache(CacheKey.user, "1000001", TestData.commonUser(), CacheKey.user.ttl());

        var cache = (CommonUser) cacheService.getCache(CacheKey.user, "1000001");
        log.info("{}", cache);

        assertThat(cache).isNotNull();
        assertThat(cache.getUserId()).isEqualTo("1000001");
        assertThat(cache.getNickname()).isEqualTo("Guest1000001");
    }

    @Test
    void getViaCache() {
        var data = cacheService.getViaCache(CacheKey.user, "1000001", TestData::commonUser);
        log.info("{}", data);

        assertThat(data).isNotNull();
        assertThat(data.getUserId()).isEqualTo("1000001");
        assertThat(data.getNickname()).isEqualTo("Guest1000001");
    }

    @Test
    void getViaCacheNotUseCache() {
        cacheService.removeCache(CacheKey.user, "1000001");

        cacheService.getViaCache(CacheKey.user, "1000001", TestData::commonUser, false);

        var cache = (CommonUser) cacheService.getCache(CacheKey.user, "1000001");
        log.info("{}", cache);

        assertThat(cache).isNull();
    }

    @Test
    void removeCache() {
        cacheService.removeCache(CacheKey.user, "1000001");

        var cache = (CommonUser) cacheService.getCache(CacheKey.user, "1000001");
        log.info("{}", cache);

        assertThat(cache).isNull();
    }

}
