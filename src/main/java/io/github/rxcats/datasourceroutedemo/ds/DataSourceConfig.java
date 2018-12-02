package io.github.rxcats.datasourceroutedemo.ds;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Value("${app.database.username}")
    private String username;

    @Value("${app.database.password}")
    private String password;

    @Value("${app.database.driver-class-name}")
    private String driverClassName;

    @Value("${app.database.common.url}")
    private String commonUrl;

    @Value("${app.database.userdb0.url}")
    private String user0Url;

    private DataSource commonDs() {
        var config = hikariConfig();
        config.setJdbcUrl(commonUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    private DataSource userDs() {
        var config = hikariConfig();
        config.setJdbcUrl(user0Url);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    private HikariConfig hikariConfig() {
        var config = new HikariConfig();
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(10);
        config.setConnectionTestQuery("SELECT 1");
        config.setConnectionTimeout(300000);
        config.setAutoCommit(true);
        config.setDriverClassName(driverClassName);
        return config;
    }

    @Primary
    @Bean
    public DataSource dataSource() {
        Map<Object, Object> map = new HashMap<>();

        // common db datasource
        map.put("common", commonDs());

        // user shard db datasource
        map.put("user0", userDs());

        // datasource 를 사용할 때에는 router 를 이용하여 사용한다.
        DataSourceRouter router = new DataSourceRouter();
        router.setDefaultTargetDataSource(map.get("common")); // 기본 datasource 설정
        router.setTargetDataSources(map);
        return router;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("io.github.rxcats.datasourceroutedemo.entity");
        bean.setConfigLocation(context.getResource("classpath:mybatis/mybatis-config.xml"));
        bean.setMapperLocations(context.getResources("classpath:mybatis/mapper/**/*.xml"));

        // default TypeHandler
//        TypeHandlerRegistry typeHandlerRegistry = bean.getObject().getConfiguration().getTypeHandlerRegistry();
//        typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
//        typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
//        typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);

        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

}
