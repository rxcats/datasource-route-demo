package io.github.rxcats.datasourceroutedemo.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import io.github.rxcats.datasourceroutedemo.entity.UserShardNo;

@Mapper
public interface UserShardNoMapper {

    @Select("SELECT userId, shardNo FROM tb_common_user WHERE userId = #{userId}")
    UserShardNo selectOne(String userId);

}
