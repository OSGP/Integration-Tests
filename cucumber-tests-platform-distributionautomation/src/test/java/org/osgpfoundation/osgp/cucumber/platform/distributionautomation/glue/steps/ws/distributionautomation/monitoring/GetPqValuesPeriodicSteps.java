/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.ws.distributionautomation.monitoring;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.*;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationKeys;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring.GetPqValuesPeriodicRequestBuilder;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring.MonitoringClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetPqValuesPeriodicSteps extends GlueBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPqValuesPeriodicSteps.class);

    @Autowired
    private MonitoringClient client;

    @When("^I send a getPQValuesPeriodic request$")
    public void iSendGetPQValuesPeriodicRequest(final Map<String, String> requestParameters) throws Throwable {

        final GetPQValuesPeriodicRequest getPQValuesPeriodicRequest = GetPqValuesPeriodicRequestBuilder.fromParameterMap(requestParameters);
        final GetPQValuesPeriodicAsyncResponse response = this.client.getPQValuesPeriodicAsyncResponse(getPQValuesPeriodicRequest);

        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID,
                response.getAsyncResponse().getCorrelationUid());
        ScenarioContext.current().put(PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                response.getAsyncResponse().getDeviceId());
    }

    @Then("^I should recieve a response for a getPQValuesPeriodicResponse request$")
    public void iShouldRecieveResponseForGetPQValuesPeriodicResponseRequest(final Map<String, String> responseParameters) throws Throwable {

        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        final Map<String, String> extendedParameters = SettingsHelper.addDefault(responseParameters,
                PlatformKeys.KEY_CORRELATION_UID, correlationUid);

        final GetPQValuesPeriodicAsyncRequest getPQValuesPeriodicAsyncRequest = GetPqValuesPeriodicRequestBuilder.fromParameterMapAsync(extendedParameters);
        final GetPQValuesPeriodicResponse response = this.client.getPQValuesPeriodicResponse(getPQValuesPeriodicAsyncRequest);

        final String expectedResult = responseParameters.get(PlatformKeys.KEY_RESULT);
        assertNotNull("Result", response.getResult());
        assertEquals("Result", expectedResult, response.getResult().name());

        if (!responseParameters.containsKey(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_DEVICES)) {
            throw new AssertionError("The Step DataTable must contain the expected number of Logical devices with key \""
                    + PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_DEVICES + "\" when confirming a returned get PQ Value response.");
        }
        final int expectedNumberOfLogicalDevices = Integer
                .parseInt(responseParameters.get(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_DEVICES));

        final List<LogicalDeviceType> logicalDevices = response.getLogicalDevice();
        assertEquals("Number of logical devices", expectedNumberOfLogicalDevices, logicalDevices.size());

        /**
         * parse node(s) for each device
         */
        for (int i = 0; i < expectedNumberOfLogicalDevices; i++) {
            this.assertDeviceResponse(responseParameters, logicalDevices, i);
        }
    }

    private void assertDeviceResponse(final Map<String, String> responseParameters,
                                      final List<LogicalDeviceType> logicalDevices,
                                      final int systemIndex) {

        final String deviceIndexPostfix = "_" + (systemIndex + 1);
        final LogicalDeviceType logicalDevice = logicalDevices.get(systemIndex);

        if (!responseParameters.containsKey(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_NODES.concat(deviceIndexPostfix))) {
            throw new AssertionError("The Step DataTable must contain the expected number of Logical nodes with key \""
                    + PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_NODES.concat(deviceIndexPostfix)
                    + "\" ( " + deviceIndexPostfix + " = the device ) when confirming a returned get PQ Value response.");
        }
        final int expectedNumberOfLogicalNodes = Integer
                .parseInt(responseParameters.get(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_NODES.concat(deviceIndexPostfix)));

        final List<LogicalNodeType> logicalNodes = logicalDevice.getLogicalNode();
        assertEquals("Number of logical nodes", expectedNumberOfLogicalNodes, logicalNodes.size());

        /**
         * Todo:
         * assert node names!!
         */

        /**
         * Parse node content (DataSamples) for each node
         */
        for (int i = 0; i < expectedNumberOfLogicalNodes; i++) {
            this.assertNodeResponse(responseParameters, logicalNodes, deviceIndexPostfix, i);
        }
    }

    private void assertNodeResponse(final Map<String, String> responseParameters,
                                    final List<LogicalNodeType> logicalNodes,
                                    final String deviceIndexPostfix,
                                    final int nodeIndex) {

        final String nodeIndexPostfix = deviceIndexPostfix + "_" + (nodeIndex + 1);
        final LogicalNodeType logicalNode = logicalNodes.get(nodeIndex);
        final List<DataSampleType> dataSamples = logicalNode.getDataSample();

        /**
         * Make the Map mutable with only DataSample Parameters from the selected node
         */
        Map<String, String> nodeResponseParameters = new HashMap<String, String>();
        for (String key : responseParameters.keySet()) {
            String value = responseParameters.get(key);
            if (key.endsWith(nodeIndexPostfix)){
                LOGGER.debug("Key = " + key + ", Value = " + value);
                nodeResponseParameters.put(key, value);
            }
        }

        if (nodeResponseParameters.containsKey(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_NODE_NAME.concat(nodeIndexPostfix))) {
            nodeResponseParameters.remove(PlatformDistributionAutomationKeys.KEY_NUMBER_OF_LOGICAL_NODE_NAME.concat(nodeIndexPostfix));
        }

        /**
         * Fail if dataSamples size does not equal nodeResponseParameters size.
         */
        assertEquals("Number node dataSamples for node" + nodeIndexPostfix + ": ",
                nodeResponseParameters.size(),
                dataSamples.size()
        );

        /**
         * assertEquals key + value for dataSamples (response) and nodeResponseParameters (from feature file)
         * Fail if key does not exist or values are not equal.
         */
        for(DataSampleType dataSample : dataSamples) {
            /**
             * need to check for existing keys! Otherwise nullpointer exceptions will occur.
             */
            try {
                assertEquals("Checking key : "+ dataSample.getType().concat(nodeIndexPostfix) ,
                        nodeResponseParameters.get(dataSample.getType().concat(nodeIndexPostfix)),
                        dataSample.getValue().toString());
            } catch (Exception e) {
                LOGGER.debug("Response key " + dataSample.getType().concat(nodeIndexPostfix) + " not found in feature file.");
                fail("Missing parameter in featue file : "+ dataSample.getType().concat(nodeIndexPostfix));
            }
        }
    }
}
