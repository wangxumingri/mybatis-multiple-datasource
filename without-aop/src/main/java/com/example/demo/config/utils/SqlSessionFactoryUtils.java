//package com.example.demo.config.utils;
//
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//public class SqlSessionFactoryUtils {
//        public SqlSessionFactoryUtils() {
//        }
//
//        public static SqlSessionFactory create(DataSource dataSource, String mapperLocation) throws Exception {
//            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//            SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
//            factory.setDataSource(dataSource);
//            factory.setTypeAliasesPackage(ProjectInfoUtils.BASE_PACKAGE + ".entity,cn.seed.log.entity");
//            factory.setConfigLocation(resolver.getResources("classpath*:mybatis/mybatis-config.xml")[0]);
//            SqlPlugins.INSTANCE.addSqlIntercepter(pageHelper());
//            factory.setPlugins((Interceptor[])SqlPlugins.INSTANCE.getSqlIntercepter().toArray(new Interceptor[SqlPlugins.INSTANCE.getSqlIntercepter().size()]));
//            factory.setMapperLocations(resolver.getResources(mapperLocation));
//            return factory.getObject();
//        }
//
//        private static PageHelper pageHelper() {
//            PageHelper pageHelper = new PageHelper();
//            Properties properties = new Properties();
//            properties.setProperty("pageSizeZero", "true");
//            properties.setProperty("reasonable", "false");
//            properties.setProperty("supportMethodsArguments", "true");
//            pageHelper.setProperties(properties);
//            return pageHelper;
//        }
//    }
//}
