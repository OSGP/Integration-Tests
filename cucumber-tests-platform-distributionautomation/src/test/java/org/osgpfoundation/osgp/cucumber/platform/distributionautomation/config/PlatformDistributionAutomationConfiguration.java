/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.alliander.osgp.cucumber.platform.config.AbstractPlatformApplicationConfiguration;

@Configuration
@PropertySources({ @PropertySource("classpath:cucumber-tests-platform-distributionautomation.properties"),
        @PropertySource(value = "file:/etc/osp/test/global-cucumber.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:/etc/osp/test/cucumber-tests-platform-distributionautomation.properties", ignoreResourceNotFound = true), })
public class PlatformDistributionAutomationConfiguration extends AbstractPlatformApplicationConfiguration {

    @Value("${jaxb2.marshaller.context.path.distributionautomation.notification}")
    private String contextPathDistributionAutomationNotification;

    @Value("${web.service.notification.context}")
    private String notificationsContextPath;

    @Value("${web.service.notification.port}")
    private int notificationsPort;

    public String getContextPathDistributionAutomationNotification() {
        return this.contextPathDistributionAutomationNotification;
    }

    public String getNotificationsContextPath() {
        return this.notificationsContextPath;
    }

    public int getNotificationsPort() {
        return this.notificationsPort;
    }
}
