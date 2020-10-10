package com.jingbo.houtai.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
@Configuration
public class MybatisConfig {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage("com.jingbo.houtai.entity");
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        //MyBatis无法扫描Spring Boot别名的Bug 添加下面这行代码
        sessionFactory.setVfs(MySpringBootVFS.class);
        return sessionFactory.getObject();
    }
}
