/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.ws.distributionautomation.adhocmanagement;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationKeys;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.adhocmanagement.GetDeviceModelRequestBuilder;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.adhocmanagement.AdHocManagementClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetDeviceModelSteps extends GlueBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDeviceModelSteps.class);

    @Autowired
    private AdHocManagementClient client;

    @When("^I send a getDeviceModel request$")
    public void iSendGetDeviceModelRequest(final Map<String, String> requestParameters) throws Throwable {

        final GetDeviceModelRequest getDeviceModelRequest = GetDeviceModelRequestBuilder.fromParameterMap(requestParameters);
        final GetDeviceModelAsyncResponse response = this.client.getDeviceModelAsyncResponse(getDeviceModelRequest);

        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID,
                response.getAsyncResponse().getCorrelationUid());
        ScenarioContext.current().put(PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                response.getAsyncResponse().getDeviceId());

    }

    @Then("^I should recieve a response for a getDeviceModel request$")
    public void iShouldRecieveResponseForGetDeviceModelResponseRequest(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetDeviceModelAsyncRequest getDeviceModelAsyncRequest = GetDeviceModelRequestBuilder.fromParameterMapAsync(extendedParameters);
        final GetDeviceModelResponse response = this.client.getDeviceModelResponse(getDeviceModelAsyncRequest);

        final String expectedResult = responseParameters.get(PlatformKeys.KEY_RESULT);
        assertNotNull(PlatformKeys.KEY_RESULT, response.getResult());
        assertEquals(PlatformKeys.KEY_RESULT, expectedResult, response.getResult().name());

        final String expectedDevice = responseParameters.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION);
        assertNotNull(PlatformKeys.KEY_DEVICE_IDENTIFICATION, response.getDeviceIdentification());
        assertEquals(PlatformKeys.KEY_DEVICE_IDENTIFICATION, expectedDevice, response.getDeviceIdentification());

        /**
         *       Todo:
         *       increase field validation on fields!
         *       <ns2:GetDeviceModelResponse xmlns:ns2="http://www.osgpfoundation.org/schemas/osgp/distributionautomation/defs/2017/04" xmlns:ns3="http://www.alliander.com/schemas/osgp/common/2014/10">
         *         <ns2:Result>OK</ns2:Result>
         *         <ns2:DeviceIdentification>WAGO61850ServerRTU1</ns2:DeviceIdentification>
         *         <ns2:PhysicalServer id="WAGO61850ServerRTU1">
         *           <ns2:LogicalDevice id="WAGO61850ServerRTU1">
         *             <ns2:LogicalNode name="LLN0"/>
         *             <ns2:LogicalNode name="LPHD1"/>
         *             <ns2:LogicalNode name="MMXU1"/>
         *             <ns2:LogicalNode name="MMXU2"/>
         *           </ns2:LogicalDevice>
         *         </ns2:PhysicalServer>
         *       </ns2:GetDeviceModelResponse>
         */

    }

}
