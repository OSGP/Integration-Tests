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
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesResponse;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.adhocmanagement.AdHocManagementClient;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.getpqvalues.GetPqValueRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetPqValueSteps extends GlueBase {

    /**
     * Delta value for which two measurement values are considered equal if
     * their difference does not exceed it.
     */
    private static final double DELTA_FOR_MEASUREMENT_VALUE = 0.0001;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private AdHocManagementClient client;

    @When("^a get pq value request is received$")
    public void aGetPqValueRequestIsReceived(final Map<String, String> requestParameters) throws Throwable {

        final GetPQValuesRequest getPQValuesRequest = GetPqValueRequestBuilder.fromParameterMap(requestParameters);
        final GetPQValuesAsyncResponse response = this.client.getDataAsync(getPQValuesRequest);

        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID,
                response.getAsyncResponse().getCorrelationUid());
        ScenarioContext.current().put(PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                response.getAsyncResponse().getDeviceId());
    }

    @Then("^the get pq value response should be returned$")
    public void theGetPqValueResponseShouldBeReturned(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetPQValuesAsyncRequest getPQValuesAsyncRequest = GetPqValueRequestBuilder.fromParameterMapAsync(extendedParameters);
        final GetPQValuesResponse response = this.client.getData(getPQValuesAsyncRequest);

        final String expectedResult = responseParameters.get(PlatformKeys.KEY_RESULT);
        assertNotNull("Result", response.getResult());
        assertEquals("Result", expectedResult, response.getResult().name());

        if (!responseParameters.containsKey(PlatformKeys.KEY_NUMBER_OF_SYSTEMS)) {
            throw new AssertionError("The Step DataTable must contain the expected number of systems with key \""
                    + PlatformKeys.KEY_NUMBER_OF_SYSTEMS + "\" when confirming a returned get data response.");
        }
        final int expectedNumberOfSystems = Integer
                .parseInt(responseParameters.get(PlatformKeys.KEY_NUMBER_OF_SYSTEMS));

        System.out.println(response.toString());

//        final List<GetDataSystemIdentifier> systemIdentifiers = response.getSystem();
//        assertEquals("Number of Systems", expectedNumberOfSystems, systemIdentifiers.size());
//
//        for (int i = 0; i < expectedNumberOfSystems; i++) {
//            this.assertSystemResponse(responseParameters, systemIdentifiers, i);
//        }
    }

//    private void assertSystemResponse(final Map<String, String> responseParameters,
//            final List<GetDataSystemIdentifier> systemIdentifiers, final int systemIndex) {
//
//        final int numberOfSystems = systemIdentifiers.size();
//        final String indexPostfix = "_" + (systemIndex + 1);
//        final String systemDescription = "System[" + (systemIndex + 1) + "/" + numberOfSystems + "]";
//
//        final GetDataSystemIdentifier systemIdentifier = systemIdentifiers.get(systemIndex);
//
//        if (responseParameters.containsKey(PlatformKeys.KEY_SYSTEM_TYPE.concat(indexPostfix))) {
//            final String expectedType = responseParameters.get(PlatformKeys.KEY_SYSTEM_TYPE.concat(indexPostfix));
//            assertEquals(systemDescription + " type", expectedType, systemIdentifier.getType());
//        }
//
//        if (responseParameters.containsKey(PlatformKeys.KEY_NUMBER_OF_MEASUREMENTS.concat(indexPostfix))) {
//            this.assertMeasurements(responseParameters, systemIdentifier.getMeasurement(), numberOfSystems, systemIndex,
//                    systemDescription, indexPostfix);
//        }
//
//        if (responseParameters.containsKey(PlatformKeys.KEY_NUMBER_OF_PROFILES.concat(indexPostfix))) {
//            this.assertProfiles(responseParameters, systemIdentifier.getProfile(), numberOfSystems, systemIndex,
//                    systemDescription, indexPostfix);
//        }
//    }


}
