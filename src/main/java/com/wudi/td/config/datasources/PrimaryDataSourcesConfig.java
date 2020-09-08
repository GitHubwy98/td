package com.wudi.td.config.datasources;


import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.wudi.td.util.DbPasswordCallbackUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(value = "com.wudi.td.dao.primary",sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDataSourcesConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.primary")
    public  DataSource primaryDruidDataSources(){
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setPasswordCallback(new DbPasswordCallbackUtil());
        return druidXADataSource;
    }

    @Bean("primaryDataSource")
    @DependsOn({"primaryDruidDataSources"})
    @Primary
    public DataSource primaryDataSource(@Qualifier("primaryDruidDataSources") DataSource druidDataSources) throws SQLException {
        //注册到全局事务
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource((DruidXADataSource)druidDataSources);
        atomikosDataSourceBean.setUniqueResourceName("td");
        return atomikosDataSourceBean;
    }

    /**
     * 创建sqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/primary/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.wudi.td.entity.primary");
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 创建sqlSession模板
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
