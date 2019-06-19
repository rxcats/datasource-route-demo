package io.github.rxcats.datasourceroutedemo.service.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DbType {
    common("common"),
    user("user")
    ;

    @Getter
    private final String name;

    public String shard(int no) {
        return user.name() + no;
    }

}
