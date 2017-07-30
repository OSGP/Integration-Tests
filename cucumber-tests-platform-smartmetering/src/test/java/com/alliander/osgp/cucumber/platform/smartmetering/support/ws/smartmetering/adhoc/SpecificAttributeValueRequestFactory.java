package com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.adhoc;

import java.math.BigInteger;
import java.util.Map;

import com.alliander.osgp.adapter.ws.schema.smartmetering.adhoc.GetSpecificAttributeValueAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.adhoc.GetSpecificAttributeValueRequest;
import com.alliander.osgp.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.RequestFactoryHelper;

public class SpecificAttributeValueRequestFactory {

    private SpecificAttributeValueRequestFactory() {
        // Private constructor for utility class
    }

    public static GetSpecificAttributeValueRequest fromParameterMap(final Map<String, String> parameters) {
        final GetSpecificAttributeValueRequest request = new GetSpecificAttributeValueRequest();
        request.setDeviceIdentification(parameters.get(PlatformSmartmeteringKeys.DEVICE_IDENTIFICATION));
        request.setClassId(new BigInteger(parameters.get(PlatformSmartmeteringKeys.CLASS_ID)));
        request.setObisCode(ObisCodeValuesFactory.fromParameterMap(parameters));
        request.setAttribute(new BigInteger(parameters.get(PlatformSmartmeteringKeys.ATTRIBUTE)));
        return request;
    }

    public static GetSpecificAttributeValueAsyncRequest fromScenarioContext() {
        final GetSpecificAttributeValueAsyncRequest asyncRequest = new GetSpecificAttributeValueAsyncRequest();
        asyncRequest.setCorrelationUid(RequestFactoryHelper.getCorrelationUidFromScenarioContext());
        asyncRequest.setDeviceIdentification(RequestFactoryHelper.getDeviceIdentificationFromScenarioContext());
        return asyncRequest;
    }
}
