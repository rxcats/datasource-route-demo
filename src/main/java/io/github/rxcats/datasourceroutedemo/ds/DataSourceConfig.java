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
        var config = new HikariConfig();
        config.setAutoCommit(properties.getAutoCommit());
        config.setDriverClassName(properties.getDriverClassName());
        config.setPoolName(properties.getCommon().getPoolName());
        config.setConnectionTimeout(properties.getCommon().getConnectionTimeout());
        config.setIdleTimeout(properties.getCommon().getIdleTimeout());
        config.setMaximumPoolSize(properties.getCommon().getMaximumPoolSize());
        config.setUsername(properties.getCommon().getUsername());
        config.setPassword(properties.getCommon().getPassword());
        config.setJdbcUrl(properties.getCommon().getJdbcUrl());
        return new HikariDataSource(config);
    }

    private List<DataSource> userDatasourceList() {
        var dataSources = new ArrayList<DataSource>();
        for (var user : properties.getUser()) {
            var config = new HikariConfig();
            config.setAutoCommit(properties.getAutoCommit());
            config.setDriverClassName(properties.getDriverClassName());
            config.setPoolName(user.getPoolName());
            config.setConnectionTimeout(user.getConnectionTimeout());
            config.setIdleTimeout(user.getIdleTimeout());
            config.setMaximumPoolSize(user.getMaximumPoolSize());
            config.setUsername(user.getUsername());
            config.setPassword(user.getPassword());
            config.setJdbcUrl(user.getJdbcUrl());
            dataSources.add(new HikariDataSource(config));
        }
        return dataSources;
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
        var router = new DataSourceRouter();

        // default datasource
        router.setDefaultTargetDataSource(map.get("common"));
        router.setTargetDataSources(map);
        return router;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
        var bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(DefaultConfigEntity.class.getPackageName());
        bean.setMapperLocations(context.getResources(properties.getMapperPath()));

        // guide : http://www.mybatis.org/mybatis-3/ko/configuration.html#settings
        var cnf = new org.apache.ibatis.session.Configuration();
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
        var transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

}
