package io.github.rxcats.datasourceroutedemo;

import java.time.LocalDateTime;

import io.github.rxcats.datasourceroutedemo.entity.CommonUser;
import io.github.rxcats.datasourceroutedemo.entity.User;

public class TestData {

    private TestData() {

    }

    public static CommonUser commonUser() {
        var u = new CommonUser();
        u.setUserId("1000001");
        u.setNickname("Guest1000001");
        u.setShardNo(0);
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }

    public static User user() {
        var u = new User();
        u.setUserId("1000001");
        u.setNickname("Guest1000001");
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }

}
