package casia.isiteam.springbootshiro.properties.dbsource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Author wzy
 * Date 2017/7/26 14:08
 */
@Configuration
public class DataSourceConfigration {

    /***************************************************
     ****             user 库数据源配置              ****
     ***************************************************/
    @Primary
    @Bean(name = "sysUserProperties")
    @Qualifier("sysUserProperties")
    @ConfigurationProperties(prefix="datasource.sys-user")
    public DataSourceProperties sysUserProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "sysUserDataSource")
    @Qualifier("sysUserDataSource")
    public DataSource sysUserDataSource(@Qualifier("sysUserProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /***************************************************
     ****             common 库数据源配置              ****
     ***************************************************/
    @Bean(name = "commonProperties")
    @Qualifier("commonProperties")
    @ConfigurationProperties(prefix="datasource.common")
    public DataSourceProperties commonProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "commonDataSource")
    @Qualifier("commonDataSource")
    public DataSource commonDataSource(@Qualifier("commonProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

}
