/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.dlms.cucumber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.alliander.osgp.shared.application.config.AbstractConfig;

/**
 * Base class for the application configuration.
 */
@Configuration
@PropertySources({ @PropertySource("classpath:cucumber-platform-dlms.properties"),
        @PropertySource(value = "file:/etc/osp/test/global-cucumber.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:/etc/osp/test/cucumber-platform-dlms.properties", ignoreResourceNotFound = true), })
public class ApplicationConfiguration extends AbstractConfig {
    
    @Value("${web.service.template.default.uri.smartmetering.adhocmanagement}")
    public String webserviceTemplateDefaultUriSmartMeteringAdHocManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.adhocmanagement}")
    public String contextPathSmartMeteringAdHocManagement;
    
    @Value("${web.service.template.default.uri.smartmetering.bundlemanagement}")
    public String webserviceTemplateDefaultUriSmartMeteringBundleManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.bundlemanagement}")
    public String contextPathSmartMeteringBundleManagement;

    @Value("${web.service.template.default.uri.smartmetering.configurationmanagement}")
    public String webserviceTemplateDefaultUriSmartMeteringConfigurationManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.configurationmanagement}")
    public String contextPathSmartMeteringConfigurationManagement;

    @Value("${web.service.template.default.uri.smartmetering.installationmanagement}")
    public String webserviceTemplateDefaultUriSmartMeteringInstallationManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.installationmanagement}")
    public String contextPathSmartMeteringInstallationManagement;

    @Value("${web.service.template.default.uri.smartmetering.management}")
    public String webserviceTemplateDefaultUriSmartMeteringManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.management}")
    public String contextPathSmartMeteringManagement;

    @Value("${web.service.template.default.uri.smartmetering.monitoringmanagement}")
    public String webserviceTemplateDefaultUriSmartMeteringMonitoringManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.monitoringmanagement}")
    public String contextPathSmartMeteringMonitoringManagement;
}