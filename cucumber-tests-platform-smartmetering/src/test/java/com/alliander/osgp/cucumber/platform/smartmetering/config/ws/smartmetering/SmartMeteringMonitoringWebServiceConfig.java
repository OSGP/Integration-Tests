/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.config.ws.smartmetering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;

import com.alliander.osgp.cucumber.core.config.ws.BaseWebServiceConfig;
import com.alliander.osgp.cucumber.platform.smartmetering.config.ApplicationConfiguration;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;

@Configuration
public class SmartMeteringMonitoringWebServiceConfig extends BaseWebServiceConfig {

    @Autowired
    private ApplicationConfiguration configuration;

    @Bean
    public DefaultWebServiceTemplateFactory smartMeteringMonitoringWstf() {
        return new DefaultWebServiceTemplateFactory.Builder().setMarshaller(this.smartMeteringMonitoringMarshaller())
                .setMessageFactory(this.messageFactory())
                .setTargetUri(
                        this.baseUri.concat(this.configuration.webserviceTemplateDefaultUriSmartMeteringMonitoring))
                .setKeyStoreType(this.webserviceKeystoreType).setKeyStoreLocation(this.webserviceKeystoreLocation)
                .setKeyStorePassword(this.webserviceKeystorePassword)
                .setTrustStoreFactory(this.webServiceTrustStoreFactory()).setApplicationName(this.applicationName)
                .build();
    }

    /**
     * Method for creating the Marshaller for SmartMetering Monitoring
     * webservice.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller smartMeteringMonitoringMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.configuration.contextPathSmartMeteringMonitoring);

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for
     * SmartMetering Monitoring webservice.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor smartMeteringMonitoringMarshallingPayloadMethodProcessor() {
        return new MarshallingPayloadMethodProcessor(this.smartMeteringMonitoringMarshaller(),
                this.smartMeteringMonitoringMarshaller());
    }

}