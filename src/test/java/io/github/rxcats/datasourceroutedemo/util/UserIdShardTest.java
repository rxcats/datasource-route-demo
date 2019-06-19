package io.github.rxcats.datasourceroutedemo.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserIdShardTest {
    @Test
    void shard() {
        Map<Integer, Integer> sum = new HashMap<>();
        LongStream.rangeClosed(1_000_001, 9_000_000).forEach(i -> {
            int shard = UserIdShard.get(i + "") % 2;

            Integer cnt = sum.get(shard);
            if (cnt == null) {
                cnt = 0;
            }
            cnt++;

            sum.put(shard, cnt);
        });
        log.info("sum:{}", sum);
        assertThat(sum).hasSize(2);
        assertThat(sum.get(0)).isEqualTo(4_000_000);
        assertThat(sum.get(1)).isEqualTo(4_000_000);
    }
}
