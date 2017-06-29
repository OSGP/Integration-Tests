/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.ws.distributionautomation.adhocmanagement;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.glue.steps.ws.GenericResponseSteps;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.adhocmanagement.AdHocManagementClient;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.adhocmanagement.GetDeviceModelRequestBuilder;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.util.Map;

import static org.junit.Assert.fail;

public class FaultSteps extends GlueBase {

    @Autowired
    private AdHocManagementClient client;

    @Then("^I should recieve a SOAP Fault for a getDeviceModelResponse request$")
    public void shouldRecieveSoapFaultForGetDeviceModelResponseRequest(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetDeviceModelAsyncRequest getDeviceModelAsyncRequest = GetDeviceModelRequestBuilder.fromParameterMapAsync(extendedParameters);

        try {
            final GetDeviceModelResponse response = this.client.getDeviceModelResponse(getDeviceModelAsyncRequest);
            fail("Expected a SOAP fault, but got a GetDataResponse with result " + response.getResult().value() + ".");
        } catch (final SoapFaultClientException e) {
            ScenarioContext.current().put(PlatformKeys.RESPONSE, e);
        }

        GenericResponseSteps.verifySoapFault(responseParameters);
    }
}
