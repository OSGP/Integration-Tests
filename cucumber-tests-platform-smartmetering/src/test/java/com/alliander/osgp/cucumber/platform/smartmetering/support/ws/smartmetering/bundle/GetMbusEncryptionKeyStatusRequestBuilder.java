/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.bundle;

import static com.alliander.osgp.cucumber.core.ReadSettingsHelper.getString;

import java.util.Collections;
import java.util.Map;

import com.alliander.osgp.adapter.ws.schema.smartmetering.bundle.GetMbusEncryptionKeyStatusRequest;
import com.alliander.osgp.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;

public class GetMbusEncryptionKeyStatusRequestBuilder {

    private static final String DEFAULT_MBUS_DEVICE_IDENTIFICATION = "TESTG102400000001";

    private String mbusDeviceIdentification;

    public GetMbusEncryptionKeyStatusRequestBuilder withDefaults() {
        return this.fromParameterMap(Collections.emptyMap());
    }

    public GetMbusEncryptionKeyStatusRequestBuilder fromParameterMap(final Map<String, String> parameters) {
        this.mbusDeviceIdentification = this.getMbusDeviceIdentification(parameters);
        return this;
    }

    public GetMbusEncryptionKeyStatusRequest build() {
        final GetMbusEncryptionKeyStatusRequest request = new GetMbusEncryptionKeyStatusRequest();
        request.setMbusDeviceIdentification(this.mbusDeviceIdentification);
        return request;
    }

    private String getMbusDeviceIdentification(final Map<String, String> parameters) {
        return getString(parameters, PlatformSmartmeteringKeys.MBUS_DEVICE_IDENTIFICATION,
                DEFAULT_MBUS_DEVICE_IDENTIFICATION);
    }
}
