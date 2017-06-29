/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.devicemanagement;

import com.alliander.osgp.cucumber.platform.support.ws.BaseClient;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;
import org.osgpfoundation.osgp.adapter.ws.da.domain.repositories.RtuResponseDataRepository;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class DeviceManagementClient extends BaseClient {

    @Autowired
    @Qualifier("webServiceTemplateFactoryDistributionAutomationDeviceManagement")
    private DefaultWebServiceTemplateFactory webServiceTemplateFactoryDistributionAutomationDeviceManagement;

    @Autowired
    private RtuResponseDataRepository rtuResponseDataRepository;

    @Value("${iec61850.rtu.response.wait.check.interval:1000}")
    private int waitCheckIntervalMillis;
    @Value("${iec61850.rtu.response.wait.fail.duration:15000}")
    private int waitFailMillis;

    public GetHealthStatusAsyncResponse getHealthStatusAsyncResponse(final GetHealthStatusRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationDeviceManagement
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetHealthStatusAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    public GetHealthStatusResponse getHealthStatusResponse(final GetHealthStatusAsyncRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final String correlationUid = request.getAsyncRequest().getCorrelationUid();
        this.waitForRtuResponseData(correlationUid);

        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationDeviceManagement
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetHealthStatusResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    private void waitForRtuResponseData(final String correlationUid) {
        try {
            for (int timeSpentWaiting = 0; timeSpentWaiting < this.waitFailMillis; timeSpentWaiting += this.waitCheckIntervalMillis) {
                Thread.sleep(this.waitCheckIntervalMillis);
                if (this.rtuResponseDataRepository.findSingleResultByCorrelationUid(correlationUid) != null) {
                    return;
                }
            }
            throw new AssertionError("RtuResponseData not available within " + this.waitFailMillis + " milliseconds.");
        } catch (final InterruptedException e) {
            throw new AssertionError("Waiting for RtuResponseData was interrupted.", e);
        }
    }
}
