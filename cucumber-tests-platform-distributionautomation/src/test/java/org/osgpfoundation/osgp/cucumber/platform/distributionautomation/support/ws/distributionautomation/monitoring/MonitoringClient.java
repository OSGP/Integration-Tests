/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring;

import com.alliander.osgp.cucumber.platform.support.ws.BaseClient;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;
import org.osgpfoundation.osgp.adapter.ws.da.domain.repositories.RtuResponseDataRepository;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class MonitoringClient extends BaseClient {

    @Autowired
    @Qualifier("webServiceTemplateFactoryDistributionAutomationMonitoring")
    private DefaultWebServiceTemplateFactory webServiceTemplateFactoryDistributionAutomationMonitoring;

    @Autowired
    private RtuResponseDataRepository rtuResponseDataRepository;

    @Value("${iec61850.rtu.response.wait.check.interval:1000}")
    private int waitCheckIntervalMillis;
    @Value("${iec61850.rtu.response.wait.fail.duration:15000}")
    private int waitFailMillis;

    /**
     * Send an Async PQValues request and return the response
     * @param request
     * @return
     * @throws WebServiceSecurityException
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public GetPQValuesAsyncResponse getPQValuesAsyncResponse(final GetPQValuesRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationMonitoring
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetPQValuesAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    /**
     * Request the PQValues response of an Async PQValues request and return the response
     * @param request
     * @return
     * @throws WebServiceSecurityException
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public GetPQValuesResponse getPQValuesResponse(final GetPQValuesAsyncRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final String correlationUid = request.getAsyncRequest().getCorrelationUid();
        this.waitForRtuResponseData(correlationUid);

        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationMonitoring
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetPQValuesResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    /**
     * Send an Async PQValuesPeriodic request and return the response
     * @param request
     * @return
     * @throws WebServiceSecurityException
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public GetPQValuesPeriodicAsyncResponse getPQValuesPeriodicAsyncResponse(final GetPQValuesPeriodicRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationMonitoring
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetPQValuesPeriodicAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    /**
     * Request the PQValuesPeriodic response of an Async PQValuesPeriodic request and return the response
     * @param request
     * @return
     * @throws WebServiceSecurityException
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public GetPQValuesPeriodicResponse getPQValuesPeriodicResponse(final GetPQValuesPeriodicAsyncRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final String correlationUid = request.getAsyncRequest().getCorrelationUid();
        this.waitForRtuResponseData(correlationUid);

        final WebServiceTemplate webServiceTemplate = this.webServiceTemplateFactoryDistributionAutomationMonitoring
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetPQValuesPeriodicResponse) webServiceTemplate.marshalSendAndReceive(request);
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
