# MyBatis DataSourceRouter example

## application.properties 설정 
```
# MyBatis Query 로깅 설정
logging.level.io.github.rxcats.datasourceroutedemo.mapper=debug

# 공통 MySQL 설정
app.database.driver-class-name=com.mysql.cj.jdbc.Driver
app.database.mapper-path=classpath:mybatis/mapper/**/*.xml
app.database.auto-commit=true

# 유저 생성시 샤딩 타겟이 되는 DB index array 값
app.database.shard-targets=0,1

# commondb 설정
app.database.common.pool-name=commondb
app.database.common.connection-timeout=5000
app.database.common.idle-timeout=600000
app.database.common.maximum-pool-size=10
app.database.common.username=username
app.database.common.password=password
app.database.common.jdbc-url=jdbc:mysql://192.168.99.100:3306/commondb?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true

# userdb[0] 샤드0 설정
app.database.user[0].pool-name=user[0]
app.database.user[0].connection-timeout=5000
app.database.user[0].idle-timeout=600000
app.database.user[0].maximum-pool-size=10
app.database.user[0].username=username
app.database.user[0].password=password
app.database.user[0].jdbc-url=jdbc:mysql://192.168.99.100:3306/userdb0?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true

# userdb[1] 샤드1 설정
app.database.user[1].pool-name=user[1]
app.database.user[1].connection-timeout=5000
app.database.user[1].idle-timeout=600000
app.database.user[1].maximum-pool-size=10
app.database.user[1].username=username
app.database.user[1].password=password
app.database.user[1].jdbc-url=jdbc:mysql://192.168.99.100:3306/userdb1?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
```

## DB Query 예제
```java
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
import io.github.rxcats.datasourceroutedemo.service.query.ShardHelper;

@Service
public class UserService {
    
    @Resource
    private UserMapper userMapper;

    @Resource
    private CommonUserMapper commonUserMapper;
    
    @Autowired
    private QueryHelper queryHelper; // DB 정보와 shardNo 를 이용하여 Mapper 함수를 실행시키는 서비스
    
    @Autowired
    private ShardHelper shardHelper; // 유저의 샤드 정보 지정, 조회 서비스
    
    @Autowired
    private CacheService cacheService; // 캐시를 이용하여 DB 를 사용하기 위한 서비스
    
    /**
     * queryHelper.execute 파라미터 설명
     * queryHelper.execute(db 타입:[common or user], 샤드번호:[0~N], Mapper Method function:[DbOperation interface 를 이용]); 
     */
    public void createShardUser(String userId) {
        // 신규 유저의 샤드 번호 구하기
        int shardNo = shardHelper.newShardNo(userId);
        
        // 지정된 유저 샤드 DB 에 유저정보 insert
        queryHelper.execute(DbType.user, shardNo, () -> userMapper.insert(user()));
    }
    
    /**
     * commondb 에서 사용자 조회
     * 캐시를 먼저 조회하여 본 후 DB에 Query 를 한다., DB Query 결과는 캐시에 저장 
     */
    public CommonUser getCommonUser(String userId, boolean useCache) {
        return cacheService.getViaCache(CacheKey.user, userId, () ->
            queryHelper.execute(DbType.common, null, () -> commonUserMapper.selectOne(userId)), useCache);
    }
}

```