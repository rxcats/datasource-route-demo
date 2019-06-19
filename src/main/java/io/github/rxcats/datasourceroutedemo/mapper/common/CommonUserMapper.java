package io.github.rxcats.datasourceroutedemo.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import io.github.rxcats.datasourceroutedemo.entity.CommonUser;

@Mapper
public interface CommonUserMapper {

    CommonUser selectOne(String userId);

    int insert(CommonUser user);

    int delete(String userId);

    int deleteAll();

    int errorInsert(CommonUser user);

}
