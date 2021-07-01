package com.example.demo.config;

import org.apache.ibatis.datasource.DataSourceFactory;
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
@MapperScan(basePackages = "com.example.demo.dao.mgt", sqlSessionFactoryRef = "mgtSqlSessionFactory")
public class MgtDataSourceConfig {

    @Bean(name = "mgtDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mgt")
    public DataSourceProperties mgtDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("mgtDataSource")
    @Primary
    public DataSource mgtDataSource(@Qualifier("mgtDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "mgtTransactionManager")
    @Primary
    public DataSourceTransactionManager mgtTransactionManager(@Qualifier("mgtDataSource") DataSource mgtDataSource) {
        return new DataSourceTransactionManager(mgtDataSource);
    }

    @Bean(name = "mgtSqlSessionFactory")
    @Primary
    public SqlSessionFactory mgtSqlSessionFactory(@Qualifier("mgtDataSource") DataSource mgtDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mgtDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/mapper/mgt/*.xml"));
        return sessionFactory.getObject();
    }


    @Bean(name = "mgtSqlSessionTemplate")
    public SqlSessionTemplate koalaSqlSessionTemplate(@Qualifier("mgtDataSource") DataSource mgtDataSource) throws Exception {
        return new SqlSessionTemplate(mgtSqlSessionFactory(mgtDataSource));
    }

}
