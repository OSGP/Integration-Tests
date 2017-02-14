/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.glue.steps.mocks;

import static com.alliander.osgp.cucumber.platform.core.Helpers.getBoolean;
import static com.alliander.osgp.cucumber.platform.core.Helpers.getDate;
import static com.alliander.osgp.cucumber.platform.core.Helpers.getEnum;
import static com.alliander.osgp.cucumber.platform.core.Helpers.getInteger;
import static com.alliander.osgp.cucumber.platform.core.Helpers.getString;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.protocol.oslp.infra.messaging.DeviceRequestMessageType;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.EventNotificationType;
import com.alliander.osgp.cucumber.platform.Defaults;
import com.alliander.osgp.cucumber.platform.Keys;
import com.alliander.osgp.cucumber.platform.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.mocks.oslpdevice.DeviceSimulatorException;
import com.alliander.osgp.cucumber.platform.mocks.oslpdevice.MockOslpServer;
import com.alliander.osgp.dto.valueobjects.EventNotificationTypeDto;
import com.alliander.osgp.oslp.Oslp;
import com.alliander.osgp.oslp.Oslp.ActionTime;
import com.alliander.osgp.oslp.Oslp.Event;
import com.alliander.osgp.oslp.Oslp.EventNotification;
import com.alliander.osgp.oslp.Oslp.EventNotificationRequest;
import com.alliander.osgp.oslp.Oslp.EventNotificationResponse;
import com.alliander.osgp.oslp.Oslp.GetPowerUsageHistoryRequest;
import com.alliander.osgp.oslp.Oslp.HistoryTermType;
import com.alliander.osgp.oslp.Oslp.LightType;
import com.alliander.osgp.oslp.Oslp.LightValue;
import com.alliander.osgp.oslp.Oslp.LinkType;
import com.alliander.osgp.oslp.Oslp.LongTermIntervalType;
import com.alliander.osgp.oslp.Oslp.Message;
import com.alliander.osgp.oslp.Oslp.MeterType;
import com.alliander.osgp.oslp.Oslp.RelayType;
import com.alliander.osgp.oslp.Oslp.ResumeScheduleRequest;
import com.alliander.osgp.oslp.Oslp.Schedule;
import com.alliander.osgp.oslp.Oslp.SetScheduleRequest;
import com.alliander.osgp.oslp.Oslp.SetTransitionRequest;
import com.alliander.osgp.oslp.Oslp.Status;
import com.alliander.osgp.oslp.Oslp.TransitionType;
import com.alliander.osgp.oslp.Oslp.TriggerType;
import com.alliander.osgp.oslp.Oslp.Weekday;
import com.alliander.osgp.oslp.OslpUtils;
import com.google.protobuf.ByteString;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class which holds all the OSLP device mock steps in order to let the device
 * mock behave correctly for the automatic test.
 */
public class OslpDeviceSteps {

    @Autowired
    private MockOslpServer oslpMockServer;

    /**
     * Verify that a get actual power usage OSLP message is sent to the device.
     *
     */
    @Then("^a get actual power usage OSLP message is sent to the device$")
    public void aGetActualPowerUsageOslpMessageIsSentToTheDevice() {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_ACTUAL_POWER_USAGE);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetActualPowerUsageRequest());

        message.getGetActualPowerUsageRequest();
    }

    /**
     * Verify that a get configuration OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a get configuration OSLP message is sent to device \"([^\"]*)\"$")
    public void aGetConfigurationOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_CONFIGURATION);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetConfigurationRequest());
    }

    /**
     * Verify that a get firmware version OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a get firmware version OSLP message is sent to device \"([^\"]*)\"$")
    public void aGetFirmwareVersionOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_FIRMWARE_VERSION);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetFirmwareVersionRequest());

        message.getGetFirmwareVersionRequest();
    }

    /**
     * Verify that a get power usage history OSLP message is sent to the device.
     *
     * @param expectedParameters
     *            The parameters expected in the message of the device.
     */
    @Then("^a get power usage history OSLP message is sent to the device$")
    public void aGetPowerUsageHistoryOslpMessageIsSentToTheDevice(final Map<String, String> expectedParameters) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_POWER_USAGE_HISTORY);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetPowerUsageHistoryRequest());

        final GetPowerUsageHistoryRequest request = message.getGetPowerUsageHistoryRequest();
        Assert.assertEquals(getEnum(expectedParameters, Keys.HISTORY_TERM_TYPE, HistoryTermType.class,
                Defaults.DEFAULT_OSLP_HISTORY_TERM_TYPE), request.getTermType());
        if (expectedParameters.containsKey(Keys.KEY_PAGE) && !expectedParameters.get(Keys.KEY_PAGE).isEmpty()) {
            Assert.assertEquals((int) getInteger(expectedParameters, Keys.KEY_PAGE), request.getPage());
        }
        if (expectedParameters.containsKey(Keys.START_TIME) && !expectedParameters.get(Keys.START_TIME).isEmpty()
                && expectedParameters.get(Keys.START_TIME) != null) {
            Assert.assertEquals(getString(expectedParameters, Keys.START_TIME), request.getTimePeriod().getStartTime());
        }
        if (expectedParameters.containsKey(Keys.END_TIME) && !expectedParameters.get(Keys.END_TIME).isEmpty()
                && expectedParameters.get(Keys.END_TIME) != null) {
            Assert.assertEquals(getString(expectedParameters, Keys.END_TIME), request.getTimePeriod().getEndTime());
        }
    }

    /**
     * Verify that a get status OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a get status OSLP message is sent to device \"([^\"]*)\"$")
    public void aGetStatusOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_STATUS);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetStatusRequest());
    }

    /**
     * Verify that a get firmware version OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^an update firmware OSLP message is sent to the device$")
    public void anUpdateFirmwareOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.UPDATE_FIRMWARE);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasUpdateFirmwareRequest());

        message.getUpdateFirmwareRequest();
    }

    @Then("^an update key OSLP message is sent to device \"([^\"]*)\"$")
    public void anUpdateKeyOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.UPDATE_KEY);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetDeviceVerificationKeyRequest());
    }

    /**
     * Verify that a resume schedule OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a resume schedule OSLP message is sent to device \"([^\"]*)\"$")
    public void aResumeScheduleOSLPMessageIsSentToDevice(final String deviceIdentification,
            final Map<String, String> expectedRequest) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.RESUME_SCHEDULE);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasResumeScheduleRequest());

        final ResumeScheduleRequest request = message.getResumeScheduleRequest();

        Assert.assertEquals(getBoolean(expectedRequest, Keys.KEY_ISIMMEDIATE), request.getImmediate());
        Assert.assertEquals(getInteger(expectedRequest, Keys.KEY_INDEX),
                OslpUtils.byteStringToInteger(request.getIndex()));
    }

    /**
     * Verify that a set configuration OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set configuration OSLP message is sent to device \"([^\"]*)\"$")
    public void aSetConfigurationOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_CONFIGURATION);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetConfigurationRequest());
    }

    /**
     * Verify that an event notification OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set event notification OSLP message is sent to device \"([^\"]*)\"")
    public void aSetEventNotificationOslpMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_EVENT_NOTIFICATIONS);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetEventNotificationsRequest());
    }

    /**
     * Verify that a set light OSLP message is sent to the device.
     *
     * @param nofLightValues
     *            The parameters expected in the message of the device.
     */
    @Then("^a set light OSLP message with \"([^\"]*)\" lightvalues is sent to the device$")
    public void aSetLightOslpMessageWithLightValuesIsSentToTheDevice(final int nofLightValues) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_LIGHT);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetLightRequest());

        Assert.assertEquals(nofLightValues, message.getSetLightRequest().getValuesList().size());
    }

    /**
     * Verify that a set light OSLP message is sent to the device.
     *
     * @param expectedParameters
     *            The parameters expected in the message of the device.
     */
    @Then("^a set light OSLP message with one light value is sent to the device$")
    public void aSetLightOSLPMessageWithOneLightvalueIsSentToTheDevice(final Map<String, String> expectedParameters) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_LIGHT);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetLightRequest());

        final LightValue lightValue = message.getSetLightRequest().getValues(0);

        Assert.assertEquals(getInteger(expectedParameters, Keys.KEY_INDEX, Defaults.DEFAULT_INDEX),
                OslpUtils.byteStringToInteger(lightValue.getIndex()));
        if (expectedParameters.containsKey(Keys.KEY_DIMVALUE)
                && !StringUtils.isEmpty(expectedParameters.get(Keys.KEY_DIMVALUE))) {
            Assert.assertEquals(getInteger(expectedParameters, Keys.KEY_DIMVALUE, Defaults.DEFAULT_DIMVALUE),
                    OslpUtils.byteStringToInteger(lightValue.getDimValue()));
        }
        Assert.assertEquals(getBoolean(expectedParameters, Keys.KEY_ON, Defaults.DEFAULT_ON), lightValue.getOn());
    }

    /**
     * Verify that a set light schedule OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device. @
     */
    @Then("^a set light schedule OSLP message is sent to device \"([^\"]*)\"$")
    public void aSetLightScheduleOSLPMessageIsSentToDevice(final String deviceIdentification,
            final Map<String, String> expectedRequest) {
        this.checkAndValidateRequest(DeviceRequestMessageType.SET_LIGHT_SCHEDULE, expectedRequest);
    }

    /**
     * Verify that a set reboot OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set reboot OSLP message is sent to device \"([^\"]*)\"$")
    public void aSetRebootOSLPMessageIsSentToDevice(final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_REBOOT);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetRebootRequest());
    }

    /**
     * Verify that a set reverse tariff schedule OSLP message is sent to the
     * device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     * @param expectedRequest
     *            The request parameters expected in the message to the device.
     */
    @Then("^a set reverse tariff schedule OSLP message is sent to device \"(?:([^\"]*))\"$")
    public void aSetReverseTariffScheduleOSLPMessageIsSentToDevice(final String deviceIdentification,
            final Map<String, String> expectedRequest) {
        this.aSetTariffScheduleOSLPMessageIsSentToDevice(deviceIdentification, expectedRequest);
    }

    /**
     * Verify that a set tariff schedule OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set tariff schedule OSLP message is sent to device \"(?:([^\"]*))\"$")
    public void aSetTariffScheduleOSLPMessageIsSentToDevice(final String deviceIdentification,
            final Map<String, String> expectedRequest) {
        this.checkAndValidateRequest(DeviceRequestMessageType.SET_TARIFF_SCHEDULE, expectedRequest);
    }

    /**
     * Verify that a set transition OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set transition OSLP message is sent to device \"([^\"]*)\"$")
    public void aSetTransitionOSLPMessageIsSentToDevice(final String deviceIdentification,
            final Map<String, String> expectedResult) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_TRANSITION);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetTransitionRequest());

        final SetTransitionRequest request = message.getSetTransitionRequest();

        Assert.assertEquals(getEnum(expectedResult, Keys.KEY_TRANSITION_TYPE, TransitionType.class),
                request.getTransitionType());
        if (expectedResult.containsKey(Keys.KEY_TIME)) {
            // TODO: How to check the time?
            // Assert.assertEquals(expectedResult.get(Keys.KEY_TIME),
            // request.getTime());
        }
    }

    /**
     * Verify that a start device OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a start device OSLP message is sent to device \"([^\"]*)\"$")
    public void aStartDeviceOSLPMessageIsSentToDevice(final String deviceIdentification) {
        // TODO: Sent an OSLP start device message to device
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.START_SELF_TEST);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasStartSelfTestRequest());
    }

    /**
     * Verify that a stop device OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a stop device OSLP message is sent to device \"([^\"]*)\"$")
    public void aStopDeviceOSLPMessageIsSentToDevice(final String deviceIdentification) {
        // TODO: Sent an OSLP start device message to device
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.STOP_SELF_TEST);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasStopSelfTestRequest());
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     */
    private void callMockSetScheduleResponse(final String result, final DeviceRequestMessageType type) {
        this.oslpMockServer.mockSetScheduleResponse(type, Enum.valueOf(Status.class, result));
    }

    private void checkAndValidateRequest(final DeviceRequestMessageType type,
            final Map<String, String> expectedRequest) {
        final Message message = this.oslpMockServer.waitForRequest(type);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetScheduleRequest());

        final SetScheduleRequest request = message.getSetScheduleRequest();

        for (final Schedule schedule : request.getSchedulesList()) {
            if (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) {
                Assert.assertEquals(getEnum(expectedRequest, Keys.SCHEDULE_WEEKDAY, Weekday.class),
                        schedule.getWeekday());
            }
            if (!expectedRequest.get(Keys.SCHEDULE_STARTDAY).isEmpty()) {
                final String startDay = getDate(expectedRequest, Keys.SCHEDULE_STARTDAY).toDateTime(DateTimeZone.UTC)
                        .toString("yyyyMMdd");

                Assert.assertEquals(startDay, schedule.getStartDay());
            }
            if (!expectedRequest.get(Keys.SCHEDULE_ENDDAY).isEmpty()) {
                final String endDay = getDate(expectedRequest, Keys.SCHEDULE_ENDDAY).toDateTime(DateTimeZone.UTC)
                        .toString("yyyyMMdd");

                Assert.assertEquals(endDay, schedule.getEndDay());
            }

            if (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) {
                Assert.assertEquals(getEnum(expectedRequest, Keys.SCHEDULE_ACTIONTIME, ActionTime.class),
                        schedule.getActionTime());
            }
            String expectedTime = getString(expectedRequest, Keys.SCHEDULE_TIME).replace(":", "");
            if (expectedTime.contains(".")) {
                expectedTime = expectedTime.substring(0, expectedTime.indexOf("."));
            }
            Assert.assertEquals(expectedTime, schedule.getTime());
            final String scheduleLightValue = getString(expectedRequest,
                    (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) ? Keys.SCHEDULE_LIGHTVALUES
                            : Keys.SCHEDULE_TARIFFVALUES);
            final String[] scheduleLightValues = scheduleLightValue.split(";");
            Assert.assertEquals(scheduleLightValues.length, schedule.getValueCount());
            for (int i = 0; i < scheduleLightValues.length; i++) {
                final Integer index = OslpUtils.byteStringToInteger(schedule.getValue(i).getIndex()),
                        dimValue = OslpUtils.byteStringToInteger(schedule.getValue(i).getDimValue());
                if (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) {
                    Assert.assertEquals(scheduleLightValues[i], String.format("%s,%s,%s", (index != null) ? index : "",
                            schedule.getValue(i).getOn(), (dimValue != null) ? dimValue : ""));
                } else if (type == DeviceRequestMessageType.SET_TARIFF_SCHEDULE) {
                    Assert.assertEquals(scheduleLightValues[i],
                            String.format("%s,%s", (index != null) ? index : "", !schedule.getValue(i).getOn()));
                }
            }

            if (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) {
                Assert.assertEquals((!getString(expectedRequest, Keys.SCHEDULE_TRIGGERTYPE).isEmpty())
                        ? getEnum(expectedRequest, Keys.SCHEDULE_TRIGGERTYPE, TriggerType.class)
                        : TriggerType.TT_NOT_SET, schedule.getTriggerType());

                final String[] windowTypeValues = getString(expectedRequest, Keys.SCHEDULE_TRIGGERWINDOW).split(",");
                if (windowTypeValues.length == 2) {
                    Assert.assertEquals(Integer.parseInt(windowTypeValues[0]), schedule.getWindow().getMinutesBefore());
                    Assert.assertEquals(Integer.parseInt(windowTypeValues[1]), schedule.getWindow().getMinutesAfter());
                }
            }
        }
    }

    /**
     * Simulates sending an OSLP EventNotification message to the OSLP Protocol
     * adapter.
     *
     * @param settings
     * @throws DeviceSimulatorException
     * @throws IOException
     * @throws ParseException
     */
    @When("^receiving an OSLP event notification message$")
    public void receivingAnOSLPEventNotificationMessage(final Map<String, String> settings)
            throws DeviceSimulatorException, IOException, ParseException {

        final EventNotification eventNotification = EventNotification.newBuilder()
                .setDescription(getString(settings, Keys.KEY_DESCRIPTION, ""))
                .setEvent(getEnum(settings, Keys.KEY_EVENT, Event.class)).build();

        final Message message = Oslp.Message.newBuilder()
                .setEventNotificationRequest(EventNotificationRequest.newBuilder().addNotifications(eventNotification))
                .build();

        // Save the OSLP response for later validation.
        ScenarioContext.Current().put(Keys.RESPONSE, this.oslpMockServer.sendRequest(message));
    }

    /**
     * Setup method to get an actual power usage which should be returned by the
     * mock.
     *
     * @param requestParameters
     *            The data to respond.
     */
    @Given("^the device returns a get actual power usage response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAGetActualPowerUsageOverOSLP(final String result,
            final Map<String, String> responseData) {

        // Note: This piece of code has been made because there are multiple
        // enumerations with the name MeterType, but not all of them has all
        // values the same. Some with underscore and some without.
        MeterType meterType = MeterType.MT_NOT_SET;
        final String sMeterType = getString(responseData, Keys.METER_TYPE);
        if (!sMeterType.toString().contains("_") && sMeterType.equals(MeterType.P1_VALUE)) {
            final String[] sMeterTypeArray = sMeterType.toString().split("");
            meterType = MeterType.valueOf(sMeterTypeArray[0] + "_" + sMeterTypeArray[1]);
        } else {
            meterType = getEnum(responseData, Keys.METER_TYPE, MeterType.class);
        }

        this.oslpMockServer.mockGetActualPowerUsageResponse(Enum.valueOf(Status.class, result),
                getInteger(responseData, Keys.ACTUAL_CONSUMED_POWER, null), meterType,
                getDate(responseData, Keys.RECORD_TIME).toDateTime(DateTimeZone.UTC).toString("yyyyMMddHHmmss"),
                getInteger(responseData, Keys.TOTAL_CONSUMED_ENERGY, null),
                getInteger(responseData, Keys.TOTAL_LIGHTING_HOURS, null),
                getInteger(responseData, Keys.ACTUAL_CURRENT1, null),
                getInteger(responseData, Keys.ACTUAL_CURRENT2, null),
                getInteger(responseData, Keys.ACTUAL_CURRENT3, null),
                getInteger(responseData, Keys.ACTUAL_POWER1, null), getInteger(responseData, Keys.ACTUAL_POWER2, null),
                getInteger(responseData, Keys.ACTUAL_POWER3, null),
                getInteger(responseData, Keys.AVERAGE_POWER_FACTOR1, null),
                getInteger(responseData, Keys.AVERAGE_POWER_FACTOR2, null),
                getInteger(responseData, Keys.AVERAGE_POWER_FACTOR3, null),
                getString(responseData, Keys.RELAY_DATA, null));
    }

    @Given("^the device returns a get configuration status over OSLP$")
    public void theDeviceReturnsAGetConfigurationStatusOverOSLP(final Map<String, String> requestParameters) {
        this.theDeviceReturnsAGetConfigurationStatusWithResultOverOSLP(
                getEnum(requestParameters, Keys.KEY_STATUS, Status.class, Status.OK).name(), requestParameters);
    }

    /**
     * Setup method to set the configuration status which should be returned by
     * the mock.
     *
     * @param status
     *            The status to respond.
     */
    @Given("^the device returns a get configuration status \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAGetConfigurationStatusWithResultOverOSLP(final String result,
            final Map<String, String> requestParameters) {
        // Note: This piece of code has been made because there are multiple
        // enumerations with the name MeterType, but not all of them has all
        // values the same. Some with underscore and some without.
        MeterType meterType = MeterType.MT_NOT_SET;
        final String sMeterType = getString(requestParameters, Keys.METER_TYPE);
        if (!sMeterType.toString().contains("_") && sMeterType.equals(MeterType.P1_VALUE)) {
            final String[] sMeterTypeArray = sMeterType.toString().split("");
            meterType = MeterType.valueOf(sMeterTypeArray[0] + "_" + sMeterTypeArray[1]);
        } else {
            meterType = getEnum(requestParameters, Keys.METER_TYPE, MeterType.class);
        }

        this.oslpMockServer.mockGetConfigurationResponse(Enum.valueOf(Status.class, result),
                getEnum(requestParameters, Keys.KEY_LIGHTTYPE, LightType.class),
                getString(requestParameters, Keys.DC_LIGHTS, Defaults.DC_LIGHTS),
                getString(requestParameters, Keys.DC_MAP), getEnum(requestParameters, Keys.RC_TYPE, RelayType.class),
                getString(requestParameters, Keys.RC_MAP),
                getEnum(requestParameters, Keys.KEY_PREFERRED_LINKTYPE, LinkType.class), meterType,
                getInteger(requestParameters, Keys.SHORT_INTERVAL, Defaults.SHORT_INTERVAL),
                getInteger(requestParameters, Keys.LONG_INTERVAL, Defaults.LONG_INTERVAL),
                getEnum(requestParameters, Keys.INTERVAL_TYPE, LongTermIntervalType.class));
    }

    /**
     * Setup method to get the power usage history which should be returned by
     * the mock.
     *
     * @param requestParameters
     */
    @Given("^the device returns a get power usage history response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAGetPowerUsageHistoryOverOSLP(final String result,
            final Map<String, String> requestParameters) {
        this.oslpMockServer.mockGetPowerUsageHistoryResponse(Enum.valueOf(Status.class, result),
                getString(requestParameters, Keys.RECORD_TIME), getInteger(requestParameters, Keys.KEY_INDEX),
                getInteger(requestParameters, Keys.ACTUAL_CONSUMED_POWER, null),
                getEnum(requestParameters, Keys.METER_TYPE, MeterType.class),
                getInteger(requestParameters, Keys.TOTAL_CONSUMED_ENERGY, null),
                getInteger(requestParameters, Keys.TOTAL_LIGHTING_HOURS, null),
                getInteger(requestParameters, Keys.ACTUAL_CURRENT1, null),
                getInteger(requestParameters, Keys.ACTUAL_CURRENT2, null),
                getInteger(requestParameters, Keys.ACTUAL_CURRENT3, null),
                getInteger(requestParameters, Keys.ACTUAL_POWER1, null),
                getInteger(requestParameters, Keys.ACTUAL_POWER2, null),
                getInteger(requestParameters, Keys.ACTUAL_POWER3, null),
                getInteger(requestParameters, Keys.AVERAGE_POWER_FACTOR1, null),
                getInteger(requestParameters, Keys.AVERAGE_POWER_FACTOR2, null),
                getInteger(requestParameters, Keys.AVERAGE_POWER_FACTOR3, null),
                getString(requestParameters, Keys.RELAY_DATA, null));
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     *            The get status to respond.
     */
    @Given("^the device returns a get status response over OSLP$")

    public void theDeviceReturnsAGetStatusResponseOverOSLP(final Map<String, String> result) {
        int eventNotificationTypes = 0;
        final String eventNotificationTypesString = getString(result, Keys.KEY_EVENTNOTIFICATIONTYPES,
                Defaults.DEFAULT_EVENTNOTIFICATIONTYPES);
        for (final String eventNotificationType : eventNotificationTypesString.split(Keys.SEPARATOR_COMMA)) {
            if (!eventNotificationType.isEmpty()) {
                eventNotificationTypes = eventNotificationTypes
                        + Enum.valueOf(EventNotificationTypeDto.class, eventNotificationType.trim()).getValue();
            }
        }

        final List<LightValue> lightValues = new ArrayList<>();
        if (!getString(result, Keys.KEY_LIGHTVALUES, Defaults.DEFAULT_LIGHTVALUES).isEmpty()
                && getString(result, Keys.KEY_LIGHTVALUES, Defaults.DEFAULT_LIGHTVALUES)
                        .split(Keys.SEPARATOR_SEMICOLON).length > 0) {

            for (final String lightValueString : getString(result, Keys.KEY_LIGHTVALUES, Defaults.DEFAULT_LIGHTVALUES)
                    .split(Keys.SEPARATOR_SEMICOLON)) {
                final String[] parts = lightValueString.split(Keys.SEPARATOR_COMMA);

                final LightValue lightValue = LightValue.newBuilder()
                        .setIndex(OslpUtils.integerToByteString(Integer.parseInt(parts[0])))
                        .setOn(parts[1].toLowerCase().equals("true"))
                        .setDimValue(OslpUtils.integerToByteString(Integer.parseInt(parts[2]))).build();

                lightValues.add(lightValue);
            }
        }

        this.oslpMockServer.mockGetStatusResponse(
                getEnum(result, Keys.KEY_PREFERRED_LINKTYPE, LinkType.class, Defaults.DEFAULT_PREFERRED_LINKTYPE),
                getEnum(result, Keys.KEY_ACTUAL_LINKTYPE, LinkType.class, Defaults.DEFAULT_ACTUAL_LINKTYPE),
                getEnum(result, Keys.KEY_LIGHTTYPE, LightType.class, Defaults.DEFAULT_LIGHTTYPE),
                eventNotificationTypes, getEnum(result, Keys.KEY_STATUS, Oslp.Status.class, Defaults.DEFAULT_STATUS),
                lightValues);
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a get status response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAGetStatusResponseWithResultOverOSLP(final String result,
            final Map<String, String> requestParameters) {

        int eventNotificationTypes = 0;
        if (getString(requestParameters, Keys.KEY_EVENTNOTIFICATIONTYPES, Defaults.DEFAULT_EVENTNOTIFICATIONTYPES)
                .trim().split(",").length > 0) {
            for (final String eventNotificationType : getString(requestParameters, Keys.KEY_EVENTNOTIFICATIONTYPES,
                    Defaults.DEFAULT_EVENTNOTIFICATIONTYPES).trim().split(",")) {
                if (!eventNotificationType.isEmpty()) {
                    eventNotificationTypes = eventNotificationTypes
                            + Enum.valueOf(EventNotificationType.class, eventNotificationType.trim()).ordinal();
                }
            }
        }

        final List<LightValue> lightValues = new ArrayList<>();
        if (!getString(requestParameters, Keys.KEY_LIGHTVALUES, Defaults.DEFAULT_LIGHTVALUES).isEmpty()
                && getString(requestParameters, Keys.KEY_LIGHTVALUES, Defaults.DEFAULT_LIGHTVALUES)
                        .split(Keys.SEPARATOR_SEMICOLON).length > 0) {

            for (final String lightValueString : getString(requestParameters, Keys.KEY_LIGHTVALUES,
                    Defaults.DEFAULT_LIGHTVALUES).split(Keys.SEPARATOR_SEMICOLON)) {
                final String[] parts = lightValueString.split(Keys.SEPARATOR_COMMA);

                final LightValue lightValue = LightValue.newBuilder()
                        .setIndex(OslpUtils.integerToByteString(Integer.parseInt(parts[0])))
                        .setOn(parts[1].toLowerCase().equals("true"))
                        .setDimValue(OslpUtils.integerToByteString(Integer.parseInt(parts[2]))).build();

                lightValues.add(lightValue);
            }
        }

        this.oslpMockServer.mockGetStatusResponse(
                getEnum(requestParameters, Keys.KEY_PREFERRED_LINKTYPE, LinkType.class,
                        Defaults.DEFAULT_PREFERRED_LINKTYPE),
                getEnum(requestParameters, Keys.KEY_ACTUAL_LINKTYPE, LinkType.class, Defaults.DEFAULT_ACTUAL_LINKTYPE),
                getEnum(requestParameters, Keys.KEY_LIGHTTYPE, LightType.class, Defaults.DEFAULT_LIGHTTYPE),
                eventNotificationTypes, Enum.valueOf(Status.class, result), lightValues);
    }

    /**
     * Setup method to resume a schedule which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a resume schedule response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAResumeScheduleResponseOverOSLP(final String result) {
        this.oslpMockServer.mockResumeScheduleResponse(Enum.valueOf(Status.class, result));
    }

    @Given("^the device returns a set configuration status over OSLP$")
    public void theDeviceReturnsASetConfigurationStatusOverOSLP(final Map<String, String> settings) {
        this.theDeviceReturnsASetConfigurationStatusWithStatusOverOSLP(
                getEnum(settings, Keys.KEY_STATUS, Status.class).name());
    }

    /**
     * Setup method to set the configuration status which should be returned by
     * the mock.
     *
     * @param status
     *            The status to respond.
     */
    @Given("^the device returns a set configuration status \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetConfigurationStatusWithStatusOverOSLP(final String result) {
        this.oslpMockServer.mockSetConfigurationResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set the event notification which should be returned by
     * the mock.
     *
     * @param result
     */
    @Given("^the device returns a set event notification \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetEventNotificationOverOSLP(final String result) {
        this.oslpMockServer.mockSetEventNotificationResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set a light which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set light response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetLightOverOSLP(final String result) {
        this.oslpMockServer.mockSetLightResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set light schedule response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetLightScheduleResponseOverOSLP(final String result) {
        this.callMockSetScheduleResponse(result, DeviceRequestMessageType.SET_LIGHT_SCHEDULE);
    }

    /**
     * Setup method to set a reboot which should be returned by the mock.
     *
     * @param resultEnum.valueOf(Status.class,
     *            result) The stop device to respond.
     */
    @Given("^the device returns a set reboot response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetRebootResponseOverOSLP(final String result) {
        this.oslpMockServer.mockSetRebootResponse(Enum.valueOf(Status.class, result));
    }

    @Given("^the device returns a set reverse tariff schedule response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetReverseTariffScheduleResponseOverOSLP(final String result) {
        this.theDeviceReturnsASetTariffScheduleResponseOverOSLP(result);
    }

    @Given("^the device returns a set tariff schedule response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetTariffScheduleResponseOverOSLP(final String result) {
        this.callMockSetScheduleResponse(result, DeviceRequestMessageType.SET_TARIFF_SCHEDULE);
    }

    /**
     * Setup method to set a transition which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set transition response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsASetTransitionResponseOverOSLP(final String result) {
        this.oslpMockServer.mockSetTransitionResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to start a device which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a start device response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAStartDeviceResponseOverOSLP(final String result) {
        this.oslpMockServer.mockStartDeviceResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to stop a device which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a stop device response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsAStopDeviceResponseOverOSLP(final String result) {
        // TODO: Check if ByteString.EMPTY must be something else
        this.oslpMockServer.mockStopDeviceResponse(ByteString.EMPTY, Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set the firmware which should be returned by the mock.
     *
     * @param firmwareVersion
     *            The firmware to respond.
     */
    @Given("^the device returns firmware version \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsFirmwareVersionOverOSLP(final String firmwareVersion) {
        this.oslpMockServer.mockGetFirmwareVersionResponse(firmwareVersion);
    }

    /**
     * Setup method to set the firmware which should be returned by the mock.
     *
     * @param firmwareVersion
     *            The firmware to respond.
     */
    @Given("^the device returns update firmware response \"([^\"]*)\" over OSLP$")
    public void theDeviceReturnsUpdateFirmwareResponseOverOSLP(final String result) {
        this.oslpMockServer.mockUpdateFirmwareResponse(Enum.valueOf(Status.class, result));
    }

    @Then("^the OSLP event notification response contains$")
    public void theOSLPEventNotificationResponseContains(final Map<String, String> expectedResponse) {
        final Message responseMessage = (Message) ScenarioContext.Current().get(Keys.RESPONSE);

        final EventNotificationResponse response = responseMessage.getEventNotificationResponse();

        Assert.assertEquals(getString(expectedResponse, Keys.KEY_STATUS), response.getStatus());
    }
}