package io.github.rxcats.datasourceroutedemo.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.rxcats.datasourceroutedemo.entity.DefaultConfigEntity;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    @Autowired
    private DataSourceProperties properties;

    private DataSource commonDatasource() {
        var config = hikariConfig();
        config.setJdbcUrl(properties.getCommonUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        return new HikariDataSource(config);
    }

    private List<DataSource> userDatasourceList() {
        var list = new ArrayList<DataSource>();
        for (var url : properties.getUserUrl()) {
            var config = hikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(properties.getUsername());
            config.setPassword(properties.getPassword());
            list.add(new HikariDataSource(config));
        }
        return list;
    }

    private HikariConfig hikariConfig() {
        var config = new HikariConfig();
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(300000);
        config.setAutoCommit(true);
        config.setDriverClassName(properties.getDriverClassName());
        return config;
    }

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> map = new HashMap<>();

        // common db datasource
        map.put("common", commonDatasource());

        // user shard db datasource
        List<DataSource> dataSources = userDatasourceList();
        for (int i = 0; i < dataSources.size(); i++) {
            map.put("user" + i, dataSources.get(i));
        }

        // datasource routing configuration
        DataSourceRouter router = new DataSourceRouter();

        // default datasource
        router.setDefaultTargetDataSource(map.get("common"));
        router.setTargetDataSources(map);
        return router;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(DefaultConfigEntity.class.getPackageName());
        bean.setMapperLocations(context.getResources("classpath:mybatis/mapper/**/*.xml"));

        // guide : http://www.mybatis.org/mybatis-3/ko/configuration.html#settings
        org.apache.ibatis.session.Configuration cnf = new org.apache.ibatis.session.Configuration();
        cnf.setDatabaseId("mysql");
        cnf.setMapUnderscoreToCamelCase(true);
        cnf.setDefaultExecutorType(ExecutorType.REUSE);
        cnf.setCacheEnabled(false);
        cnf.setUseGeneratedKeys(true);
        cnf.setAggressiveLazyLoading(false);
        bean.setConfiguration(cnf);

        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

}
