package com.top.mini.happy.down;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * @author xugang on 18/1/25.
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.top.mini.happy.down.dal", annotationClass = Repository.class)
public class DataSourceConfig implements TransactionManagementConfigurer {

    private final static String LOCATION = "classpath*:dal/mapper/*.xml";
    private final static String CONFIG = "classpath:ibatisCfg.xml";

    @Bean(name = "dataSource",initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource getDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(getDataSource());
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(LOCATION));
        sqlSessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIG));
        return sqlSessionFactory.getObject();
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }
}
