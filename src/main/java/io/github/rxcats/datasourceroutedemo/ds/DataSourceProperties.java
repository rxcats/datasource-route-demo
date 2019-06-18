package io.github.rxcats.datasourceroutedemo.ds;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("app.database")
public class DataSourceProperties {
    private String username;
    private String password;
    private String driverClassName;
    private String commonUrl;
    private List<String> userUrl;
}
