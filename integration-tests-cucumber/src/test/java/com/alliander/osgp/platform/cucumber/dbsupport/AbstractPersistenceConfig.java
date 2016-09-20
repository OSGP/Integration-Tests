/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.dbsupport;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Base class for PersistenceConfig classes.
 * An application context Java JPA configuration class. The usage of Java
 * configuration requires Spring Framework 3.0
 */
@Configuration
@EnableTransactionManagement()
@Primary
@PropertySource("file:/etc/osp/osgp-cucumber-response-data-smart-metering.properties")
public abstract class AbstractPersistenceConfig {

    @Value("${cucumber.dbs.driver}")
    protected String databaseDriver;

    @Value("${cucumber.dbs.username}")
    protected String databaseUsername;

    @Value("${cucumber.dbs.password}")
    protected String databasePassword;

    protected static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    @Value("${hibernate.dialect}")
    protected String hibernateDialect;

    protected static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    @Value("${hibernate.format_sql}")
    protected String hibernateFormatSql;

    protected static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    @Value("${hibernate.ejb.naming_strategy}")
    protected String hibernateNamingStrategy;

    protected static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    @Value("${hibernate.show_sql}")
    protected String hibernateShowSql;

    protected abstract String getDatabaseUrl();

    protected abstract String getEntitymanagerPackagesToScan();


    @Resource
    protected Environment environment;

  
    public AbstractPersistenceConfig() {
    }

    
    /**
     * Method for creating the Data Source.
     *
     * @return DataSource
     */
    protected DataSource makeDataSource() {
        final SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setAutoCommit(false);
        final Properties properties = new Properties();
        properties.setProperty("socketTimeout", "0");
        properties.setProperty("tcpKeepAlive", "true");
        singleConnectionDataSource.setDriverClassName(this.databaseDriver);
        singleConnectionDataSource.setUrl(this.getDatabaseUrl());
        singleConnectionDataSource.setUsername(this.databaseUsername);
        singleConnectionDataSource.setPassword(this.databasePassword);
        singleConnectionDataSource.setSuppressClose(true);
        return singleConnectionDataSource;
    }

    /**
     * Method for creating the Entity Manager Factory Bean.
     *
     * @return LocalContainerEntityManagerFactoryBean
     * @throws ClassNotFoundException
     *             when class not found
     */
    protected LocalContainerEntityManagerFactoryBean makeEntityManager(final String unitName, final DataSource dataSource)
            throws ClassNotFoundException {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setPersistenceUnitName(unitName);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(this.getEntitymanagerPackagesToScan());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);

        final Properties jpaProperties = new Properties();
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, this.hibernateDialect);
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, this.hibernateFormatSql);
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, this.hibernateNamingStrategy);
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, this.hibernateShowSql);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }
    
 
}
