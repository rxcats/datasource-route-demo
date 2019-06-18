package io.github.rxcats.datasourceroutedemo.ds;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("app.database")
public class DataSourceProperties {
    private String driverClassName;
    private String mapperPath;
    private Boolean autoCommit;
    private List<Integer> shardTargets;

    private DatabaseProp common;

    private List<DatabaseProp> user;

    @Data
    public static class DatabaseProp {
        private String poolName;
        private Long connectionTimeout;
        private Long idleTimeout;
        private Integer maximumPoolSize;
        private String username;
        private String password;
        private String jdbcUrl;
    }
}
