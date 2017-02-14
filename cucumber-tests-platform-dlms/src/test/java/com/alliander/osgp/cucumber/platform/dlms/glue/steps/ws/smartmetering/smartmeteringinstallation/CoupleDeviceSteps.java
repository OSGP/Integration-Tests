/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.dlms.glue.steps.ws.smartmetering.smartmeteringinstallation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceResponse;
import com.alliander.osgp.cucumber.platform.Keys;
import com.alliander.osgp.cucumber.platform.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.dlms.glue.steps.ws.smartmetering.AbstractSmartMeteringSteps;
import com.alliander.osgp.cucumber.platform.dlms.support.ws.smartmetering.installation.CoupleMbusDeviceRequestFactory;
import com.alliander.osgp.cucumber.platform.dlms.support.ws.smartmetering.installation.SmartMeteringInstallationClient;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CoupleDeviceSteps extends AbstractSmartMeteringSteps {

    @Autowired
    private SmartMeteringInstallationClient smartMeteringInstallationClient;

    @When("^the Couple G-meter \"([^\"]*)\" request on channel (\\d+) is received$")
    public void theCoupleGMeterRequestIsReceived(final String gasMeter, final Short channel)
            throws WebServiceSecurityException {

        final CoupleMbusDeviceRequest request = CoupleMbusDeviceRequestFactory.forMbusDeviceAndChannel(gasMeter,
                channel);
        final CoupleMbusDeviceAsyncResponse asyncResponse = this.smartMeteringInstallationClient
                .coupleMbusDevice(request);

        this.checkAndSaveCorrelationId(asyncResponse.getCorrelationUid());
    }

    @When("^the Couple G-meter \"([^\"]*)\" to E-meter \"([^\"]*)\" request on channel (\\d+) is received for an unknown gateway$")
    public void theCoupleGMeterToEMeterRequestOnChannelIsReceivedForAnUnknownGateway(final String gasMeter,
            final String eMeter, final Short channel) throws WebServiceSecurityException {

        final CoupleMbusDeviceRequest request = CoupleMbusDeviceRequestFactory.forGatewayMbusDeviceAndChannel(eMeter,
                gasMeter, channel);

        try {
            this.smartMeteringInstallationClient.coupleMbusDevice(request);
            fail("A SoapFaultClientException should be thrown");
        } catch (final SoapFaultClientException e) {
            ScenarioContext.Current().put(Keys.RESPONSE, e);
        }
    }

    @When("^the Couple G-meter \"([^\"]*)\" request on channel (\\d+) is received for an inactive device$")
    public void theCoupleGMeterRequestOnChannelIsReceivedForAnInactiveDevice(final String gasMeter, final Short channel)
            throws WebServiceSecurityException {

        final CoupleMbusDeviceRequest request = CoupleMbusDeviceRequestFactory.forMbusDeviceAndChannel(gasMeter,
                channel);

        try {
            this.smartMeteringInstallationClient.coupleMbusDevice(request);
            fail("A SoapFaultClientException should be thrown");
        } catch (final SoapFaultClientException e) {
            ScenarioContext.Current().put(Keys.RESPONSE, e);
        }
    }

    @Then("^the Couple response is \"([^\"]*)\"$")
    public void theCoupleResponseIs(final String status) throws WebServiceSecurityException {

        final CoupleMbusDeviceAsyncRequest asyncRequest = CoupleMbusDeviceRequestFactory.fromScenarioContext();
        final CoupleMbusDeviceResponse response = this.smartMeteringInstallationClient
                .getCoupleMbusDeviceResponse(asyncRequest);

        assertNotNull("Result", response.getResult());
        assertEquals("Result", status, response.getResult().name());
    }

    @Then("^the Couple response is \"([^\"]*)\" and contains$")
    public void theCoupleResponseIsAndContains(final String status, final List<String> resultList)
            throws WebServiceSecurityException {

        final CoupleMbusDeviceAsyncRequest coupleMbusDeviceAsyncRequest = CoupleMbusDeviceRequestFactory
                .fromScenarioContext();
        final CoupleMbusDeviceResponse response = this.smartMeteringInstallationClient
                .getCoupleMbusDeviceResponse(coupleMbusDeviceAsyncRequest);

        assertNotNull("Result", response.getResult());
        assertEquals("Result", status, response.getResult().name());
        assertTrue("Description should contain all of " + resultList,
                this.checkDescription(response.getDescription(), resultList));
    }
}