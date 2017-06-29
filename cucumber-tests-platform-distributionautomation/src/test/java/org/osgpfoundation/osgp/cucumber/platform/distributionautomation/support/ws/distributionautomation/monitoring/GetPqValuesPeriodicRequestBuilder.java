/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.support.ws.distributionautomation.monitoring;

import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.common.AsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesPeriodicAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetPQValuesPeriodicRequest;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationDefaults;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationKeys;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import static javax.xml.datatype.DatatypeFactory.newInstance;

public class GetPqValuesPeriodicRequestBuilder {

    private GetPqValuesPeriodicRequestBuilder() {
        // Private constructor for utility class.
    }

    public static GetPQValuesPeriodicRequest fromParameterMap(final Map<String, String> requestParameters) throws ParseException, DatatypeConfigurationException {
        final GetPQValuesPeriodicRequest getPQValuesPeriodicRequest = new GetPQValuesPeriodicRequest();
        getPQValuesPeriodicRequest.setDeviceIdentification(requestParameters.get(PlatformKeys.KEY_DEVICE_IDENTIFICATION));

//        GregorianCalendar dateFrom = GregorianCalendar.from((LocalDate.parse(requestParameters.get(PlatformDistributionAutomationKeys.KEY_FROM_DATE))).atStartOfDay(ZoneId.systemDefault()));
//        GregorianCalendar dateTo = GregorianCalendar.from((LocalDate.parse(requestParameters.get(PlatformDistributionAutomationKeys.KEY_TO_DATE))).atStartOfDay(ZoneId.systemDefault()));

        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
        //                         ^-^-----check these
        // don't pay attention to the smiley generated above, they're arrows ;)
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new SimpleDateFormat(format).parse(requestParameters.get(PlatformDistributionAutomationKeys.KEY_FROM_DATE)));
        XMLGregorianCalendar dateFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        cal.setTime(new SimpleDateFormat(format).parse(requestParameters.get(PlatformDistributionAutomationKeys.KEY_TO_DATE)));
        XMLGregorianCalendar dateTo = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        DatatypeFactory dtf = DatatypeFactory.newInstance();
        XMLGregorianCalendar xgc1 = dtf.newXMLGregorianCalendar(String.valueOf(dateFrom));
        XMLGregorianCalendar xgc2 = dtf.newXMLGregorianCalendar(String.valueOf(dateTo));

        getPQValuesPeriodicRequest.setFrom(xgc1);
        getPQValuesPeriodicRequest.setTo(xgc2);
        return getPQValuesPeriodicRequest;
    }

    public static GetPQValuesPeriodicAsyncRequest fromParameterMapAsync(final Map<String, String> requestParameters) {
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
        final GetPQValuesPeriodicAsyncRequest getPQValuesPeriodicAsyncRequest = new GetPQValuesPeriodicAsyncRequest();
        final AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.setCorrelationUid(correlationUid);
        asyncRequest.setDeviceId(deviceIdentification);
        getPQValuesPeriodicAsyncRequest.setAsyncRequest(asyncRequest);
        return getPQValuesPeriodicAsyncRequest;
    }

}
