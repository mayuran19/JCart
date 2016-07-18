package com.mayuran19.jcart.core.config;

import com.mayuran19.jcart.core.constant.Constants;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by mayuran on 16/7/16.
 */
@Configuration
public class DataSourceConfiguration {
    private static final Logger DATASOURCE_CONFIGURATION_LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);
    @Autowired
    private Environment environment;

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        System.out.println(environment.getClass());
        String jdbcURL = environment.getProperty(Constants.DataSource.KEY_JDBC_URL);
        String driverClassName = environment.getProperty(Constants.DataSource.KEY_JDBC_DRIVER_CLASS_NAME);
        String username = environment.getProperty(Constants.DataSource.KEY_JDBC_USERNAME);
        String password = environment.getProperty(Constants.DataSource.KEY_JDBC_PASSWORD);

        DATASOURCE_CONFIGURATION_LOGGER.info("JDBC.URL: " + jdbcURL);
        DATASOURCE_CONFIGURATION_LOGGER.info("jdbc.DriverClassName: " + driverClassName);
        DATASOURCE_CONFIGURATION_LOGGER.info("JDBC.USERNAME: " + username);
        DATASOURCE_CONFIGURATION_LOGGER.info("JDBC.PASSWORD: " + password);

        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setUrl(jdbcURL);
        poolProperties.setDriverClassName(driverClassName);
        poolProperties.setUsername(username);
        poolProperties.setPassword(password);
        poolProperties.setJmxEnabled(false);
        poolProperties.setTestWhileIdle(true);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1 FROM DUAL");
        poolProperties.setTestOnReturn(true);
        poolProperties.setValidationInterval(30000);
        poolProperties.setTimeBetweenEvictionRunsMillis(30000);
        poolProperties.setMaxActive(100);
        poolProperties.setInitialSize(10);
        poolProperties.setMaxWait(10000);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setMinEvictableIdleTimeMillis(30000);
        poolProperties.setMinIdle(10);
        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);

        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setPoolProperties(poolProperties);

        return dataSource;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());

        return dataSourceTransactionManager;
    }
}
