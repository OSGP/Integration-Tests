/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.publiclighting.glue.steps.ws.tariffswitching.schedulemanagement;

import static com.alliander.osgp.cucumber.core.Helpers.getDate;
import static com.alliander.osgp.cucumber.core.Helpers.getEnum;
import static com.alliander.osgp.cucumber.core.Helpers.getInteger;
import static com.alliander.osgp.cucumber.core.Helpers.getString;
import static com.alliander.osgp.cucumber.platform.core.Helpers.saveCorrelationUidInScenarioContext;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.tariffswitching.common.AsyncRequest;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.common.OsgpResultType;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.common.Page;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleRequest;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleResponse;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.TariffSchedule;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.TariffValue;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.WeekDayType;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.config.CoreDeviceConfiguration;
import com.alliander.osgp.cucumber.platform.glue.steps.ws.GenericResponseSteps;
import com.alliander.osgp.cucumber.platform.publiclighting.PlatformPubliclightingDefaults;
import com.alliander.osgp.cucumber.platform.publiclighting.PlatformPubliclightingKeys;
import com.alliander.osgp.cucumber.platform.publiclighting.support.ws.tariffswitching.TariffSwitchingScheduleManagementClient;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class with all the set requests steps
 */
public class SetTariffScheduleSteps {

    @Autowired
    private CoreDeviceConfiguration configuration;

    @Autowired
    private TariffSwitchingScheduleManagementClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(SetTariffScheduleSteps.class);

    /**
     * Sends a Set Tariff Schedule request to the platform for a given device
     * identification.
     *
     * @param requestParameters
     *            The table with the request parameters.
     * @throws Throwable
     */
    @When("^receiving a set tariff schedule request$")
    public void receivingASetTariffScheduleRequest(final Map<String, String> requestParameters) throws Throwable {

        this.callAddSchedule(requestParameters, 1);
    }

    /**
     * Sends a Set Tariff Schedule request to the platform for a given device
     * identification.
     *
     * @param requestParameters
     *            The table with the request parameters.
     * @throws Throwable
     */
    @When("^receiving a set tariff schedule request for (\\d+) schedules?$")
    public void receivingASetTariffScheduleRequestForSchedules(final Integer countSchedules,
            final Map<String, String> requestParameters) throws Throwable {

        this.callAddSchedule(requestParameters, countSchedules);
    }

    private void callAddSchedule(final Map<String, String> requestParameters, final Integer countSchedules)
            throws Throwable {

        final SetScheduleRequest request = new SetScheduleRequest();
        request.setDeviceIdentification(
                getString(requestParameters, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION, PlatformPubliclightingDefaults.DEFAULT_DEVICE_IDENTIFICATION));
        if (requestParameters.containsKey(PlatformPubliclightingKeys.SCHEDULE_SCHEDULEDTIME)) {
            request.setScheduledTime(DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(((requestParameters.get(PlatformPubliclightingKeys.SCHEDULE_SCHEDULEDTIME).isEmpty())
                            ? DateTime.now() : getDate(requestParameters, PlatformPubliclightingKeys.SCHEDULE_SCHEDULEDTIME))
                                    .toDateTime(DateTimeZone.UTC).toGregorianCalendar()));
        }

        for (int i = 0; i < countSchedules; i++) {
            this.addScheduleForRequest(request, getEnum(requestParameters, PlatformPubliclightingKeys.SCHEDULE_WEEKDAY, WeekDayType.class),
                    getString(requestParameters, PlatformPubliclightingKeys.SCHEDULE_STARTDAY),
                    getString(requestParameters, PlatformPubliclightingKeys.SCHEDULE_ENDDAY),
                    getString(requestParameters, PlatformPubliclightingKeys.SCHEDULE_TIME),
                    getString(requestParameters, PlatformPubliclightingKeys.SCHEDULE_TARIFFVALUES));
        }

        if (requestParameters.containsKey(PlatformPubliclightingKeys.SCHEDULE_CURRENTPAGE)
                && requestParameters.containsKey(PlatformPubliclightingKeys.SCHEDULE_PAGESIZE)
                && requestParameters.containsKey(PlatformPubliclightingKeys.SCHEDULE_TOTALPAGES)) {
            final Page page = new Page();
            page.setCurrentPage(getInteger(requestParameters, PlatformPubliclightingKeys.SCHEDULE_CURRENTPAGE));
            page.setPageSize(getInteger(requestParameters, PlatformPubliclightingKeys.SCHEDULE_PAGESIZE));
            page.setTotalPages(getInteger(requestParameters, PlatformPubliclightingKeys.SCHEDULE_TOTALPAGES));
            request.setPage(page);
        }

        try {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, this.client.setSchedule(request));
        } catch (final SoapFaultClientException ex) {
            ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, ex);
        }
    }

    private void addScheduleForRequest(final SetScheduleRequest request, final WeekDayType weekDay,
            final String startDay, final String endDay, final String time, final String scheduleTariffValue)
            throws DatatypeConfigurationException {
        final TariffSchedule schedule = new TariffSchedule();
        schedule.setWeekDay(weekDay);
        if (!startDay.isEmpty()) {
            schedule.setStartDay(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    DateTime.parse(startDay).toDateTime(DateTimeZone.UTC).toGregorianCalendar()));
        }
        if (!endDay.isEmpty()) {
            schedule.setEndDay(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    DateTime.parse(endDay).toDateTime(DateTimeZone.UTC).toGregorianCalendar()));
        }
        schedule.setTime(time);

        for (final String tariffValue : scheduleTariffValue.split(";")) {
            final TariffValue lv = new TariffValue();
            final String[] tariffValues = tariffValue.split(",");
            lv.setIndex(Integer.parseInt(tariffValues[0]));
            lv.setHigh(Boolean.parseBoolean(tariffValues[1]));

            schedule.getTariffValue().add(lv);
        }

        request.getSchedules().add(schedule);
    }

    @When("^receiving a set tariff schedule request by an unknown organization$")
    public void receivingASetTariffScheduleRequestByAnUnknownOrganization(final Map<String, String> requestParameters)
            throws Throwable {
        // Force the request being send to the platform as a given organization.
        ScenarioContext.current().put(PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION, "unknown-organization");

        this.receivingASetTariffScheduleRequest(requestParameters);
    }

    /**
     * The check for the response from the Platform.
     *
     * @param expectedResponseData
     *            The table with the expected fields in the response.
     * @note The response will contain the correlation uid, so store that in the
     *       current scenario context for later use.
     * @throws Throwable
     */
    @Then("^the set tariff schedule async response contains$")
    public void theSetTariffScheduleAsyncResponseContains(final Map<String, String> expectedResponseData)
            throws Throwable {
        final SetScheduleAsyncResponse response = (SetScheduleAsyncResponse) ScenarioContext.current()
                .get(PlatformPubliclightingKeys.RESPONSE);

        Assert.assertNotNull(response.getAsyncResponse().getCorrelationUid());
        Assert.assertEquals(getString(expectedResponseData, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION),
                response.getAsyncResponse().getDeviceId());

        // Save the returned CorrelationUid in the Scenario related context for
        // further use.
        saveCorrelationUidInScenarioContext(response.getAsyncResponse().getCorrelationUid(),
                getString(expectedResponseData, PlatformPubliclightingKeys.KEY_ORGANIZATION_IDENTIFICATION,
                        PlatformPubliclightingDefaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        LOGGER.info("Got CorrelationUid: [" + ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID) + "]");
    }

    @Then("^the set tariff schedule response contains soap fault$")
    public void theSetTariffScheduleResponseContainsSoapFault(final Map<String, String> expectedResponseData) {
        GenericResponseSteps.verifySoapFault(expectedResponseData);
    }

    @Then("^the platform buffers a set tariff schedule response message for device \"([^\"]*)\"$")
    public void thePlatformBuffersASetTariffScheduleResponseMessageForDevice(final String deviceIdentification,
            final Map<String, String> expectedResult) throws Throwable {
        final SetScheduleAsyncRequest request = new SetScheduleAsyncRequest();
        final AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.setDeviceId(deviceIdentification);
        asyncRequest.setCorrelationUid((String) ScenarioContext.current().get(PlatformPubliclightingKeys.KEY_CORRELATION_UID));
        request.setAsyncRequest(asyncRequest);

        boolean success = false;
        int count = 0;
        while (!success) {
            if (count > this.configuration.getTimeout()) {
                throw new TimeoutException();
            }

            count++;
            Thread.sleep(1000);

            final SetScheduleResponse response = this.client.getSetSchedule(request);

            if (getEnum(expectedResult, PlatformPubliclightingKeys.KEY_RESULT, OsgpResultType.class) != response.getResult()) {
                continue;
            }

            if (expectedResult.containsKey(PlatformPubliclightingKeys.KEY_DESCRIPTION)
                    && !getString(expectedResult, PlatformPubliclightingKeys.KEY_DESCRIPTION).equals(response.getDescription())) {
                continue;
            }

            success = true;
        }
    }

    @Then("^the platform buffers a set tariff schedule response message for device \"([^\"]*)\" contains soap fault$")
    public void thePlatformBuffersASetTariffScheduleResponseMessageForDeviceContainsSoapFault(
            final String deviceIdentification, final Map<String, String> expectedResult) throws Throwable {
        try {
            this.thePlatformBuffersASetTariffScheduleResponseMessageForDevice(deviceIdentification, expectedResult);
        } catch (final SoapFaultClientException ex) {
            Assert.assertEquals(getString(expectedResult, PlatformPubliclightingKeys.KEY_MESSAGE), ex.getMessage());
        }
    }
}