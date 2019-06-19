package io.github.rxcats.datasourceroutedemo.entity;

import lombok.Data;

@Data
public class IdGenerator {

    private String idType;

    private String idValue;

    public static IdGenerator of(String idType) {
        var id = new IdGenerator();
        id.idType = idType;
        return id;
    }

}
