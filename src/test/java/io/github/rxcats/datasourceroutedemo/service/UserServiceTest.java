package io.github.rxcats.datasourceroutedemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.github.rxcats.datasourceroutedemo.TestData;
import io.github.rxcats.datasourceroutedemo.entity.CommonUser;
import io.github.rxcats.datasourceroutedemo.fn.DbOperation;
import io.github.rxcats.datasourceroutedemo.mapper.common.CommonUserMapper;
import io.github.rxcats.datasourceroutedemo.mapper.common.IdGeneratorMapper;
import io.github.rxcats.datasourceroutedemo.mapper.user.UserMapper;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheKey;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheService;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;

@SpringBootTest(classes = { UserService.class })
class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private CommonUserMapper commonUserMapper;

    @MockBean
    private IdGeneratorMapper idGeneratorMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private QueryHelper queryHelper;

    @MockBean
    private CacheService cacheService;

    @Test
    void getCommonUserViaCache() {
        when(cacheService.getViaCache(isA(CacheKey.class), anyString(), isA(DbOperation.class), anyBoolean()))
            .thenReturn(TestData.commonUser());

        CommonUser viaCache = service.getCommonUserViaCache("1000001");

        assertThat(viaCache).isNotNull();
        assertThat(viaCache.getUserId()).isEqualTo("1000001");
        assertThat(viaCache.getNickname()).isEqualTo("Guest1000001");
    }

    @Test
    void getCommonUser() {
        when(cacheService.getViaCache(isA(CacheKey.class), anyString(), isA(DbOperation.class), anyBoolean()))
            .thenReturn(TestData.commonUser());

        CommonUser user = service.getCommonUser("1000001");

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("1000001");
        assertThat(user.getNickname()).isEqualTo("Guest1000001");
    }
}