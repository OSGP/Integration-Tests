/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config.ws.distributionautomation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;

import com.alliander.osgp.cucumber.core.config.ws.BaseWebServiceConfig;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;

@Configuration
public class DistributionAutomationAdhocManagementWebServiceConfig extends BaseWebServiceConfig {

    @Value("${web.service.template.default.uri.distributionautomation.generic}")
    private String webserviceTemplateDefaultUriDistributionAutomationAdHocManagement;

    @Value("${jaxb2.marshaller.context.path.distributionautomation.generic}")
    private String contextPathDistributionAutomationAdHocManagement;

    @Bean
    public DefaultWebServiceTemplateFactory webServiceTemplateFactoryDistributionAutomationAdHocManagement() {
        return new DefaultWebServiceTemplateFactory.Builder().setMarshaller(this.distributionautomationAdHocManagementMarshaller())
                .setMessageFactory(this.messageFactory())
                .setTargetUri(this.baseUri.concat(this.webserviceTemplateDefaultUriDistributionAutomationAdHocManagement))
                .setKeyStoreType(this.webserviceKeystoreType).setKeyStoreLocation(this.webserviceKeystoreLocation)
                .setKeyStorePassword(this.webserviceKeystorePassword)
                .setTrustStoreFactory(this.webServiceTrustStoreFactory()).setApplicationName(this.applicationName)
                .build();
    }

    /**
     * Method for creating the Marshaller for DistributionAutomation AdHocManagement.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller distributionautomationAdHocManagementMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.contextPathDistributionAutomationAdHocManagement);

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for
     * DistributionAutomation AdHocManagement.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor distributionautomationAdHocManagementMarshallingPayloadMethodProcessor() {
        return new MarshallingPayloadMethodProcessor(this.distributionautomationAdHocManagementMarshaller(),
                this.distributionautomationAdHocManagementMarshaller());
    }

}
