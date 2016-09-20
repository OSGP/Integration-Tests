/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.dbsupport;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.osgp.adapter.protocol.dlms.domain.repositories.DlmsDeviceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(entityManagerFactoryRef = "entityMgrFactDlms", 
    transactionManagerRef = "txMgrDlms",
    basePackageClasses = { DlmsDeviceRepository.class })
@Configuration
@EnableTransactionManagement()
@Primary
@PropertySource("file:/etc/osp/osgp-cucumber-response-data-smart-metering.properties")
public class PersistenceConfigResponseDlms extends AbstractPersistenceConfig {

    public PersistenceConfigResponseDlms() {
    }

    @Value("${cucumber.osgpadapterprotocoldlmsdbs.url}")
    private String databaseUrl;

    @Value("${entitymanager.packages.to.scan.dlms}")
    private String entitymanagerPackagesToScan;
  
    @Override
    protected String getDatabaseUrl() {
        return databaseUrl;
    }

    @Override
    protected String getEntitymanagerPackagesToScan() {
        return entitymanagerPackagesToScan;
    }

    /**
     * Method for creating the Data Source.
     *
     * @return DataSource
     */
    @Bean(name = "dsDlms")    
    public DataSource dataSource() {
        return makeDataSource();
    }

    /**
     * Method for creating the Entity Manager Factory Bean.
     *
     * @return LocalContainerEntityManagerFactoryBean
     * @throws ClassNotFoundException
     *             when class not found
     */
    @Bean(name = "entityMgrFactDlms")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dsDlms") DataSource dataSource) throws ClassNotFoundException {

        return makeEntityManager("OSGP_CUCUMBER_DLMS", dataSource);
    }

    /**
     * Method for creating the Transaction Manager.
     *
     * @return JpaTransactionManager
     * @throws ClassNotFoundException
     *             when class not found
     */
    @Bean(name = "txMgrDlms")    
    public JpaTransactionManager transactionManager(
            @Qualifier("entityMgrFactDlms") EntityManagerFactory barEntityManagerFactory) {
        return new JpaTransactionManager(barEntityManagerFactory);    }

}
