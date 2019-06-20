package io.github.rxcats.datasourceroutedemo.service.query;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.rxcats.datasourceroutedemo.ds.DataSourceProperties;
import io.github.rxcats.datasourceroutedemo.entity.UserShardNo;
import io.github.rxcats.datasourceroutedemo.exception.NotFoundUserException;
import io.github.rxcats.datasourceroutedemo.mapper.common.UserShardNoMapper;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheKey;
import io.github.rxcats.datasourceroutedemo.service.cache.CacheService;
import io.github.rxcats.datasourceroutedemo.util.UserIdShard;

@Service
public class ShardHelperImpl implements ShardHelper {

    @Autowired
    private DataSourceProperties properties;

    @Resource
    private UserShardNoMapper userShardNoMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private QueryHelper queryHelper;

    @Override
    public int newShardNo(String userId) {
        return UserIdShard.get(userId, properties.getShardTargets());
    }

    @Override
    public int getShardNo(String userId) {
        UserShardNo info = cacheService.getViaCache(CacheKey.usershard, userId, () ->
            queryHelper.execute(DbType.common, null, () -> userShardNoMapper.selectOne(userId)));
        if (info == null) {
            throw new NotFoundUserException(String.format("UserShardNo is null (userId:%s)", userId));
        }
        return info.getShardNo();
    }
}
