/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config;

import com.alliander.osgp.cucumber.platform.config.ApplicationPersistenceConfiguration;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import org.osgpfoundation.osgp.domain.da.repositories.RtuDeviceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityMgrCoreDistributionAutomation", transactionManagerRef = "txMgrCoreDistributionAutomation", basePackageClasses = {
        DeviceRepository.class, RtuDeviceRepository.class })
public class CoreDistributionAutomationPersistenceConfig extends ApplicationPersistenceConfiguration {

    @Value("${db.name.osgp_core}")
    private String databaseName;

    @Value("${entitymanager.packages.to.scan.core}")
    private String entitymanagerPackagesToScan;

    public CoreDistributionAutomationPersistenceConfig() {
    }

    /**
     * Method for creating the Data Source.
     *
     * @return DataSource
     */
    @Primary
    @Bean(name = "dsCoreDistributionAutomation")
    public DataSource dataSource() {
        return this.makeDataSource();
    }

    /**
     * Method for creating the Entity Manager Factory Bean.
     *
     * @return LocalContainerEntityManagerFactoryBean
     * @throws ClassNotFoundException
     *             when class not found
     */
    @Primary
    @Bean(name = "entityMgrCoreDistributionAutomation")
    public LocalContainerEntityManagerFactoryBean entityMgrCoreDistributionAutomation(
            @Qualifier("dsCoreDistributionAutomation") final DataSource dataSource) throws ClassNotFoundException {

        return this.makeEntityManager("OSGP_CUCUMBER_CORE_DISTRIBUTIONAUTOMATION", dataSource);
    }

    @Override
    protected String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    protected String getEntitymanagerPackagesToScan() {
        return this.entitymanagerPackagesToScan;
    }

    /**
     * Method for creating the Transaction Manager.
     *
     * @return JpaTransactionManager
     * @throws ClassNotFoundException
     *             when class not found
     */
    @Primary
    @Bean(name = "txMgrCoreDistributionAutomation")
    public JpaTransactionManager txMgrCoreDistributionAutomation(
            @Qualifier("entityMgrCoreDistributionAutomation") final EntityManagerFactory entityManagerFactory)
            throws ClassNotFoundException {

        return new JpaTransactionManager(entityManagerFactory);
    }

}
