/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.devicemanagement;

import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.common.AsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusRequest;

import java.util.Map;

public class GetHealthStatusRequestBuilder {

    private GetHealthStatusRequestBuilder() {
        // Private constructor for utility class.
    }

    public static GetHealthStatusRequest fromParameterMap(final Map<String, String> requestParameters) {
        final GetHealthStatusRequest getHealthStatusRequest = new GetHealthStatusRequest();
        getHealthStatusRequest.setDeviceIdentification(requestParameters.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION));
        return getHealthStatusRequest;
    }

    public static GetHealthStatusAsyncRequest fromParameterMapAsync(final Map<String, String> requestParameters) {
        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);
        if (correlationUid == null) {
            throw new AssertionError("ScenarioContext must contain the correlation UID for key \""
                    + PlatformKeys.KEY_CORRELATION_UID + "\" before creating an async request.");
        }
        final String deviceIdentification = requestParameters.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION);
        if (deviceIdentification == null) {
            throw new AssertionError("The Step DataTable must contain the device identification for key \""
                    + PlatformKeys.KEY_DEVICE_IDENTIFICATION + "\" when creating an async request.");
        }
        final GetHealthStatusAsyncRequest getHealthStatusAsyncRequest = new GetHealthStatusAsyncRequest();
        final AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.setCorrelationUid(correlationUid);
        asyncRequest.setDeviceId(deviceIdentification);
        getHealthStatusAsyncRequest.setAsyncRequest(asyncRequest);
        return getHealthStatusAsyncRequest;
    }

}
