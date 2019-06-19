package io.github.rxcats.datasourceroutedemo.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import io.github.rxcats.datasourceroutedemo.entity.IdGenerator;

@Mapper
public interface IdGeneratorMapper {

    int generate(IdGenerator idGenerator);

}
