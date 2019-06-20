package io.github.rxcats.datasourceroutedemo.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import io.github.rxcats.datasourceroutedemo.entity.User;

@Mapper
public interface UserMapper {

    User selectOne(String userId);

    int insert(User user);

    int delete(String userId);

    int deleteAll();

}
