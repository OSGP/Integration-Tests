/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config.ws.distributionautomation;

import com.alliander.osgp.cucumber.core.config.ws.BaseWebServiceConfig;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;

@Configuration
public class DistributionAutomationDeviceManagementWebServiceConfig extends BaseWebServiceConfig {

    @Value("${web.service.template.default.uri.distributionautomation.devicemanagement}")
    private String webserviceTemplateDefaultUriDistributionAutomationDeviceManagement;

    @Value("${jaxb2.marshaller.context.path.distributionautomation.devicemanagement}")
    private String contextPathDistributionAutomationDeviceManagement;

    @Bean
    public DefaultWebServiceTemplateFactory webServiceTemplateFactoryDistributionAutomationDeviceManagement() {
        return new DefaultWebServiceTemplateFactory.Builder().setMarshaller(this.distributionautomationDeviceManagementMarshaller())
                .setMessageFactory(this.messageFactory())
                .setTargetUri(this.baseUri.concat(this.webserviceTemplateDefaultUriDistributionAutomationDeviceManagement))
                .setKeyStoreType(this.webserviceKeystoreType).setKeyStoreLocation(this.webserviceKeystoreLocation)
                .setKeyStorePassword(this.webserviceKeystorePassword)
                .setTrustStoreFactory(this.webServiceTrustStoreFactory()).setApplicationName(this.applicationName)
                .build();
    }

    /**
     * Method for creating the Marshaller for DistributionAutomation DeviceManagement.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller distributionautomationDeviceManagementMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.contextPathDistributionAutomationDeviceManagement);

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for
     * DistributionAutomation DeviceManagement.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor distributionautomationDeviceManagementMarshallingPayloadMethodProcessor() {
        return new MarshallingPayloadMethodProcessor(this.distributionautomationDeviceManagementMarshaller(),
                this.distributionautomationDeviceManagementMarshaller());
    }

}
