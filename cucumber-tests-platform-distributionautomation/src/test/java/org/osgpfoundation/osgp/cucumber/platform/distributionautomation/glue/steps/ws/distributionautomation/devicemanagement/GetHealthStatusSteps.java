/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.ws.distributionautomation.devicemanagement;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationKeys;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.devicemanagement.DeviceManagementClient;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.devicemanagement.GetHealthStatusRequestBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

public class GetHealthStatusSteps extends GlueBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetHealthStatusSteps.class);

    @Autowired
    private DeviceManagementClient client;

    @When("^I send a getHealthStatus request$")
    public void iSendGetHealthStatusRequest(final Map<String, String> requestParameters) throws Throwable {

        final GetHealthStatusRequest getHealthStatusRequest = GetHealthStatusRequestBuilder.fromParameterMap(requestParameters);
        final GetHealthStatusAsyncResponse response = this.client.getHealthStatusAsyncResponse(getHealthStatusRequest);

        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID,
                response.getAsyncResponse().getCorrelationUid());
        ScenarioContext.current().put(PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                response.getAsyncResponse().getDeviceId());

    }

    @Then("^I should recieve a response for a getHealthStatus request$")
    public void iShouldRecieveResponseForGetHealthStatusResponseRequest(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetHealthStatusAsyncRequest getHealthStatusAsyncRequest = GetHealthStatusRequestBuilder.fromParameterMapAsync(extendedParameters);
        final GetHealthStatusResponse response = this.client.getHealthStatusResponse(getHealthStatusAsyncRequest);

        final String expectedResult = responseParameters.get(PlatformKeys.KEY_RESULT);
        assertNotNull(PlatformKeys.KEY_RESULT, response.getResult());
        assertEquals(PlatformKeys.KEY_RESULT, expectedResult, response.getResult().name());

        final String expectedDevice = responseParameters.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION);
        assertNotNull(PlatformKeys.KEY_DEVICE_IDENTIFICATION, response.getDeviceIdentification());
        assertEquals(PlatformKeys.KEY_DEVICE_IDENTIFICATION, expectedDevice, response.getDeviceIdentification());

        final String expectedStatus = responseParameters.get(PlatformDistributionAutomationKeys.KEY_HEALTH_STATUS);
        assertNotNull(PlatformDistributionAutomationKeys.KEY_HEALTH_STATUS, response.getHealthStatus());
        assertEquals(PlatformDistributionAutomationKeys.KEY_HEALTH_STATUS, expectedStatus, response.getHealthStatus().value());

    }

}
