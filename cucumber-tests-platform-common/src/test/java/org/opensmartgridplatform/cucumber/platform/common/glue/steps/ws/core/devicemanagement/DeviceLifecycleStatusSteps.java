/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.common.glue.steps.ws.core.devicemanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.opensmartgridplatform.adapter.ws.schema.core.common.OsgpResultType;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceLifecycleStatus;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.SetDeviceLifecycleStatusAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.SetDeviceLifecycleStatusAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.SetDeviceLifecycleStatusRequest;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.SetDeviceLifecycleStatusResponse;
import org.opensmartgridplatform.cucumber.core.ScenarioContext;
import org.opensmartgridplatform.cucumber.core.Wait;
import org.opensmartgridplatform.cucumber.platform.PlatformKeys;
import org.opensmartgridplatform.cucumber.platform.common.support.ws.core.CoreDeviceManagementClient;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.repositories.DeviceRepository;
import org.opensmartgridplatform.logging.domain.repositories.DeviceLogItemRepository;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceLifecycleStatusSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceLifecycleStatusSteps.class);

    @Autowired
    CoreDeviceManagementClient deviceManagementClient;

    @Autowired
    DeviceRepository DeviceRepository;

    @Autowired
    private DeviceLogItemRepository deviceLogItemRepository;

    @When("^the SetDeviceLifecycleStatus request is received$")
    public void theSetDeviceLifecycleStatusRequestIsReceived(final Map<String, String> settings) throws Throwable {

        final SetDeviceLifecycleStatusRequest request = new SetDeviceLifecycleStatusRequest();
        request.setDeviceIdentification(settings.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION));
        request.setDeviceLifecycleStatus(
                DeviceLifecycleStatus.valueOf(settings.get(PlatformKeys.KEY_DEVICE_LIFECYCLE_STATUS)));

        final SetDeviceLifecycleStatusAsyncResponse asyncResponse = this.deviceManagementClient
                .setDeviceLifecycleStatus(request);

        assertNotNull("AsyncResponse should not be null", asyncResponse);
        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID, asyncResponse.getCorrelationUid());
    }

    @Then("^the device lifecycle status in the database is$")
    public void theDeviceLifecycleStatusInTheDatabaseIs(final Map<String, String> settings) throws Throwable {
        final SetDeviceLifecycleStatusAsyncRequest asyncRequest = new SetDeviceLifecycleStatusAsyncRequest();
        asyncRequest.setCorrelationUid((String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID));
        asyncRequest.setDeviceId((String) ScenarioContext.current().get(PlatformKeys.KEY_DEVICE_IDENTIFICATION));

        Wait.until(() -> {
            SetDeviceLifecycleStatusResponse response = null;
            try {
                response = this.deviceManagementClient.getSetDeviceLifecycleStatusResponse(asyncRequest);
            } catch (final Exception e) {
                // do nothing
            }
            assertNotNull("No response found for Set Device Lifecycle Status", response);
            assertNotEquals(OsgpResultType.NOT_FOUND, response.getResult());
            assertEquals("Set Device Lifecycle Status result should be OK", OsgpResultType.OK, response.getResult());
        });

        final String deviceLifecycleStatus = settings.get(PlatformKeys.KEY_DEVICE_LIFECYCLE_STATUS);
        final Device device = this.DeviceRepository
                .findByDeviceIdentification(settings.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION));

        assertEquals("Device Lifecycle Status should be " + deviceLifecycleStatus,
                org.opensmartgridplatform.domain.core.valueobjects.DeviceLifecycleStatus.valueOf(deviceLifecycleStatus),
                device.getDeviceLifecycleStatus());
    }
}
