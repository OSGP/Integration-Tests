/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.publiclighting.glue.steps.ws.publiclighting.adhocmanagement;

import static org.opensmartgridplatform.cucumber.core.ReadSettingsHelper.getEnum;
import static org.opensmartgridplatform.cucumber.core.ReadSettingsHelper.getString;
import static org.opensmartgridplatform.cucumber.platform.core.CorrelationUidHelper.saveCorrelationUidInScenarioContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.DeviceStatus;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.EventNotificationType;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusRequest;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusResponse;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.LightType;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.LightValue;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.adhocmanagement.LinkType;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.common.AsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.publiclighting.common.OsgpResultType;
import org.opensmartgridplatform.cucumber.core.ScenarioContext;
import org.opensmartgridplatform.cucumber.core.Wait;
import org.opensmartgridplatform.cucumber.platform.glue.steps.ws.GenericResponseSteps;
import org.opensmartgridplatform.cucumber.platform.publiclighting.PlatformPubliclightingDefaults;
import org.opensmartgridplatform.cucumber.platform.publiclighting.PlatformPubliclightingKeys;
import org.opensmartgridplatform.cucumber.platform.publiclighting.support.ws.publiclighting.PublicLightingAdHocManagementClient;
import org.opensmartgridplatform.cucumber.platform.publiclighting.support.ws.tariffswitching.TariffSwitchingAdHocManagementClient;
import org.opensmartgridplatform.shared.exceptionhandling.WebServiceSecurityException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class with all the set light requests steps
 */
public class GetStatusSteps {

    @Autowired
    private PublicLightingAdHocManagementClient publicLightingClient;

    @Autowired
    private TariffSwitchingAdHocManagementClient tariffSwitchingClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStatusSteps.class);

    /**
     * Sends a Get Status request to the platform for a given device
     * identification.
     *
     * @param requestParameters
     *            The table with the request parameters.
     * @throws Throwable
     */
    @When("^receiving a get status request$")
    public void receivingAGetStatusRequest(final Map<String, String> requestParameters) throws Throwable {

        final GetStatusRequest request = new GetStatusRequest();
        request.setDeviceIdentification(
                getString(requestParameters, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION,
                        PlatformPubliclightingDefaults.DEFAULT_DEVICE_IDENTIFICATION));

        try {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE,
                    this.publicLightingClient.getStatus(request));
        } catch (final SoapFaultClientException ex) {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, ex);
        }
    }

    @When("^receiving a get tariff status request$")
    public void receivingAGetTariffStatusRequest(final Map<String, String> requestParameters) throws Throwable {

        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusRequest request = new org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusRequest();
        request.setDeviceIdentification(
                getString(requestParameters, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION,
                        PlatformPubliclightingDefaults.DEFAULT_DEVICE_IDENTIFICATION));

        try {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE,
                    this.tariffSwitchingClient.getStatus(request));
        } catch (final SoapFaultClientException ex) {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, ex);
        }
    }

    @When("^receiving a get status request by an unknown organization$")
    public void receivingAGetStatusRequestByAnUnknownOrganization(final Map<String, String> requestParameters)
            throws Throwable {
        // Force the request being send to the platform as a given organization.
        ScenarioContext.current().put(PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION,
                "unknown-organization");

        this.receivingAGetStatusRequest(requestParameters);
    }

    @When("^receiving a get tariff status request by an unknown organization$")
    public void receivingAGetTariffStatusRequestByAnUnknownOrganization(final Map<String, String> requestParameters)
            throws Throwable {
        // Force the request being sent to the platform as a given organization.
        ScenarioContext.current().put(PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION,
                "unknown-organization");

        this.receivingAGetTariffStatusRequest(requestParameters);
    }

    /**
     * The check for the response from the Platform.
     *
     * @param expectedResponseData
     *            The table with the expected fields in the response.
     * @apiNote The response will contain the correlation uid, so store that in the
     *       current scenario context for later use.
     * @throws Throwable
     */
    @Then("^the get status async response contains$")
    public void theGetStatusAsyncResponseContains(final Map<String, String> expectedResponseData) throws Throwable {

        final GetStatusAsyncResponse asyncResponse = (GetStatusAsyncResponse) ScenarioContext.current()
                .get(PlatformPubliclightingKeys.RESPONSE);

        Assert.assertNotNull(asyncResponse.getAsyncResponse().getCorrelationUid());
        Assert.assertEquals(getString(expectedResponseData, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION),
                asyncResponse.getAsyncResponse().getDeviceId());

        // Save the returned CorrelationUid in the Scenario related context for
        // further use.
        saveCorrelationUidInScenarioContext(asyncResponse.getAsyncResponse().getCorrelationUid(),
                getString(expectedResponseData, PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION,
                        PlatformPubliclightingDefaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        LOGGER.info("Got CorrelationUid: ["
                + ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID) + "]");
    }

    @Then("^the get tariff status async response contains$")
    public void theGetTariffStatusAsyncResponseContains(final Map<String, String> expectedResponseData)
            throws Throwable {

        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncResponse asyncResponse = (org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncResponse) ScenarioContext
                .current().get(PlatformPubliclightingKeys.RESPONSE);

        Assert.assertNotNull(asyncResponse.getAsyncResponse().getCorrelationUid());
        Assert.assertEquals(getString(expectedResponseData, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION),
                asyncResponse.getAsyncResponse().getDeviceId());

        // Save the returned CorrelationUid in the Scenario related context for
        // further use.
        saveCorrelationUidInScenarioContext(asyncResponse.getAsyncResponse().getCorrelationUid(),
                getString(expectedResponseData, PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION,
                        PlatformPubliclightingDefaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        LOGGER.info("Got CorrelationUid: ["
                + ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID) + "]");
    }

    @Then("^the get status response contains soap fault$")
    public void theGetStatusResponseContainsSoapFault(final Map<String, String> expectedResponseData) {
        GenericResponseSteps.verifySoapFault(expectedResponseData);
    }

    @Then("^the platform buffers a get status response message for device \"([^\"]*)\"$")
    public void thePlatformBuffersAGetStatusResponseMessageForDevice(final String deviceIdentification,
            final Map<String, String> expectedResult) throws Throwable {
        final GetStatusAsyncRequest request = this.getGetStatusAsyncRequest(deviceIdentification);
        final GetStatusResponse response = Wait.untilAndReturn(() -> {
            final GetStatusResponse retval = this.publicLightingClient.getGetStatusResponse(request);
            Assert.assertNotNull(retval);
            Assert.assertEquals(
                    Enum.valueOf(OsgpResultType.class, expectedResult.get(PlatformPubliclightingKeys.KEY_RESULT)),
                    retval.getResult());
            return retval;
        });

        final DeviceStatus deviceStatus = response.getDeviceStatus();

        Assert.assertEquals(getEnum(expectedResult, PlatformPubliclightingKeys.KEY_PREFERRED_LINKTYPE, LinkType.class),
                deviceStatus.getPreferredLinkType());
        Assert.assertEquals(getEnum(expectedResult, PlatformPubliclightingKeys.KEY_ACTUAL_LINKTYPE, LinkType.class),
                deviceStatus.getActualLinkType());
        Assert.assertEquals(getEnum(expectedResult, PlatformPubliclightingKeys.KEY_LIGHTTYPE, LightType.class),
                deviceStatus.getLightType());

        if (expectedResult.containsKey(PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONTYPES)
                && !expectedResult.get(PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONTYPES).isEmpty()) {
            Assert.assertEquals(
                    getString(expectedResult, PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONS,
                            PlatformPubliclightingDefaults.DEFAULT_EVENTNOTIFICATIONS)
                                    .split(PlatformPubliclightingKeys.SEPARATOR_COMMA).length,
                    deviceStatus.getEventNotifications().size());
            for (final String eventNotification : getString(expectedResult,
                    PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONS,
                    PlatformPubliclightingDefaults.DEFAULT_EVENTNOTIFICATIONS)
                            .split(PlatformPubliclightingKeys.SEPARATOR_COMMA)) {
                Assert.assertTrue(deviceStatus.getEventNotifications()
                        .contains(Enum.valueOf(EventNotificationType.class, eventNotification)));
            }
        }

        if (expectedResult.containsKey(PlatformPubliclightingKeys.KEY_LIGHTVALUES)
                && !expectedResult.get(PlatformPubliclightingKeys.KEY_LIGHTVALUES).isEmpty()) {
            Assert.assertEquals(
                    getString(expectedResult, PlatformPubliclightingKeys.KEY_LIGHTVALUES,
                            PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES)
                                    .split(PlatformPubliclightingKeys.SEPARATOR_COMMA).length,
                    deviceStatus.getLightValues().size());
            for (final String lightValues : getString(expectedResult, PlatformPubliclightingKeys.KEY_LIGHTVALUES,
                    PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES)
                            .split(PlatformPubliclightingKeys.SEPARATOR_COMMA)) {

                final String[] parts = lightValues.split(PlatformPubliclightingKeys.SEPARATOR_SEMICOLON);
                final Integer index = Integer.parseInt(parts[0]);
                final Boolean on = Boolean.parseBoolean(parts[1]);
                final Integer dimValue = Integer.parseInt(parts[2]);

                boolean found = false;
                for (final LightValue lightValue : deviceStatus.getLightValues()) {

                    if (Objects.equals(lightValue.getIndex(), index) && lightValue.isOn() == on
                            && Objects.equals(lightValue.getDimValue(), dimValue)) {
                        found = true;
                        break;
                    }
                }

                Assert.assertTrue(found);
            }
        }
    }

    @Then("^the platform buffers a get tariff status response message for device \"([^\"]*)\"$")
    public void thePlatformBuffersAGetTariffStatusResponseMessageForDevice(final String deviceIdentification,
            final Map<String, String> expectedResult) throws Throwable {
        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncRequest request = this
                .getGetTariffStatusAsyncRequest(deviceIdentification);
        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusResponse response = Wait
                .untilAndReturn(() -> {
                    final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusResponse retval = this.tariffSwitchingClient
                            .getGetStatusResponse(request);
                    Assert.assertNotNull(retval);
                    Assert.assertEquals(Enum.valueOf(
                            org.opensmartgridplatform.adapter.ws.schema.tariffswitching.common.OsgpResultType.class,
                            expectedResult.get(PlatformPubliclightingKeys.KEY_RESULT)), retval.getResult());
                    return retval;
                });

        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.DeviceStatus deviceStatus = response
                .getDeviceStatus();
        Assert.assertNotNull(deviceStatus);
        Assert.assertNotNull(deviceStatus.getTariffValues());
    }

    @Then("^the platform buffers a get status response message for device \"([^\"]*)\" which contains soap fault$")
    public void thePlatformBuffersAGetStatusResponseMessageForDeviceWhichContainsSoapFault(
            final String deviceIdentification, final Map<String, String> expectedResult)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        try {
            this.publicLightingClient.getGetStatusResponse(this.getGetStatusAsyncRequest(deviceIdentification));
        } catch (final SoapFaultClientException sfce) {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, sfce);
        }

        GenericResponseSteps.verifySoapFault(expectedResult);
    }

    private GetStatusAsyncRequest getGetStatusAsyncRequest(final String deviceIdentification) {
        final GetStatusAsyncRequest request = new GetStatusAsyncRequest();
        final AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.setDeviceId(deviceIdentification);
        asyncRequest.setCorrelationUid(
                (String) ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID));
        request.setAsyncRequest(asyncRequest);

        return request;
    }

    private org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncRequest getGetTariffStatusAsyncRequest(
            final String deviceIdentification) {
        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncRequest request = new org.opensmartgridplatform.adapter.ws.schema.tariffswitching.adhocmanagement.GetStatusAsyncRequest();
        final org.opensmartgridplatform.adapter.ws.schema.tariffswitching.common.AsyncRequest asyncRequest = new org.opensmartgridplatform.adapter.ws.schema.tariffswitching.common.AsyncRequest();
        asyncRequest.setDeviceId(deviceIdentification);
        asyncRequest.setCorrelationUid(
                (String) ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID));
        request.setAsyncRequest(asyncRequest);

        return request;
    }
}
