/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.ws.distributionautomation.monitoring;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.glue.steps.ws.GenericResponseSteps;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import cucumber.api.java.en.Then;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesPeriodicAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesPeriodicResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring.MonitoringClient;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring.GetPqValuesPeriodicRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.util.Map;

import static org.junit.Assert.fail;

public class FaultSteps extends GlueBase {

    @Autowired
    private MonitoringClient client;

    @Then("^I should recieve a SOAP Fault for a getPQValuesPeriodicResponse request$")
    public void shouldRecieveSoapFaultForGetPQValuesPeriodicResponseRequest(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetPQValuesPeriodicAsyncRequest getPQValuesPeriodicAsyncRequest = GetPqValuesPeriodicRequestBuilder.fromParameterMapAsync(extendedParameters);

        try {
            final GetPQValuesPeriodicResponse response = this.client.getPQValuesPeriodicResponse(getPQValuesPeriodicAsyncRequest);
            fail("Expected a SOAP fault, but got a GetDataResponse with result " + response.getResult().value() + ".");
        } catch (final SoapFaultClientException e) {
            ScenarioContext.current().put(PlatformKeys.RESPONSE, e);
        }

        GenericResponseSteps.verifySoapFault(responseParameters);
    }
}
