package com.example.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.demo.dao.crm", sqlSessionFactoryRef = "crmSqlSessionFactory")
public class CrmDataSourceConfig {

    private static final String MAPPER_LOCATION = "classpath:mybatis/mapper/crm/*.xml";

    @Bean(name = "crmDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.crm")
    public DataSourceProperties crmDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource crmDataSource(@Qualifier("crmDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "crmTransactionManager")
    public DataSourceTransactionManager crmTransactionManager(@Qualifier("crmDataSource") DataSource crmDataSource) {
        return new DataSourceTransactionManager(crmDataSource);
    }

    @Bean(name = "crmSqlSessionFactory")
    public SqlSessionFactory crmSqlSessionFactory(@Qualifier("crmDataSource") DataSource crmDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(crmDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean(name = "crmSqlSessionTemplate")
    public SqlSessionTemplate koalaSqlSessionTemplate(@Qualifier("crmDataSource") DataSource crmDataSource) throws Exception {
        return new SqlSessionTemplate(crmSqlSessionFactory(crmDataSource));
    }
}
