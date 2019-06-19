package io.github.rxcats.datasourceroutedemo.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.rxcats.datasourceroutedemo.entity.CommonUser;
import io.github.rxcats.datasourceroutedemo.mapper.common.CommonUserMapper;
import io.github.rxcats.datasourceroutedemo.mapper.common.IdGeneratorMapper;
import io.github.rxcats.datasourceroutedemo.mapper.user.UserMapper;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheKey;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheService;
import io.github.rxcats.datasourceroutedemo.service.query.DbType;
import io.github.rxcats.datasourceroutedemo.service.query.QueryHelper;

@Service
public class UserService {

    private static final String USER_ID = "userId";

    @Resource
    private IdGeneratorMapper idGeneratorMapper;

    @Resource
    private CommonUserMapper commonUserMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private QueryHelper queryHelper;

    @Autowired
    private CacheService cacheService;

    private CommonUser getCommonUser(String userId, boolean useCache) {
        return cacheService.getViaCache(CacheKey.user, userId, () ->
            queryHelper.execute(DbType.common, null, () -> commonUserMapper.selectOne(userId)), useCache);
    }

    public CommonUser getCommonUserViaCache(String userId) {
        return getCommonUser(userId, true);
    }

    public CommonUser getCommonUser(String userId) {
        return getCommonUser(userId, false);
    }

}
