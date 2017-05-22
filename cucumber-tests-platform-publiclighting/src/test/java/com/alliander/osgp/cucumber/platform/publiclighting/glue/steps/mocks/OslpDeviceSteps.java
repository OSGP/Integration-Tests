/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.publiclighting.glue.steps.mocks;

import static com.alliander.osgp.cucumber.core.Helpers.getBoolean;
import static com.alliander.osgp.cucumber.core.Helpers.getDate;
import static com.alliander.osgp.cucumber.core.Helpers.getEnum;
import static com.alliander.osgp.cucumber.core.Helpers.getInteger;
import static com.alliander.osgp.cucumber.core.Helpers.getString;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.protocol.oslp.infra.messaging.DeviceRequestMessageType;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.config.CoreDeviceConfiguration;
import com.alliander.osgp.cucumber.platform.publiclighting.PlatformPubliclightingDefaults;
import com.alliander.osgp.cucumber.platform.publiclighting.PlatformPubliclightingKeys;
import com.alliander.osgp.cucumber.platform.publiclighting.mocks.oslpdevice.DeviceSimulatorException;
import com.alliander.osgp.cucumber.platform.publiclighting.mocks.oslpdevice.MockOslpServer;
import com.alliander.osgp.domain.core.valueobjects.EventNotificationType;
import com.alliander.osgp.dto.valueobjects.EventNotificationTypeDto;
import com.alliander.osgp.oslp.Oslp;
import com.alliander.osgp.oslp.Oslp.ActionTime;
import com.alliander.osgp.oslp.Oslp.DeviceType;
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
import com.alliander.osgp.oslp.Oslp.RegisterDeviceResponse;
import com.alliander.osgp.oslp.Oslp.RelayType;
import com.alliander.osgp.oslp.Oslp.ResumeScheduleRequest;
import com.alliander.osgp.oslp.Oslp.Schedule;
import com.alliander.osgp.oslp.Oslp.SetRebootRequest;
import com.alliander.osgp.oslp.Oslp.SetScheduleRequest;
import com.alliander.osgp.oslp.Oslp.SetTransitionRequest;
import com.alliander.osgp.oslp.Oslp.Status;
import com.alliander.osgp.oslp.Oslp.TransitionType;
import com.alliander.osgp.oslp.Oslp.TriggerType;
import com.alliander.osgp.oslp.Oslp.Weekday;
import com.alliander.osgp.oslp.OslpEnvelope;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(OslpDeviceSteps.class);

    @Autowired
    private CoreDeviceConfiguration configuration;

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
    @Then("^a get configuration \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aGetConfigurationOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^a get firmware version \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aGetFirmwareVersionOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^a get power usage history \"([^\"]*)\" message is sent to the device$")
    public void aGetPowerUsageHistoryOslpMessageIsSentToTheDevice(final String protocol,
            final Map<String, String> expectedParameters) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.GET_POWER_USAGE_HISTORY);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasGetPowerUsageHistoryRequest());

        final GetPowerUsageHistoryRequest request = message.getGetPowerUsageHistoryRequest();
        Assert.assertEquals(getEnum(expectedParameters, PlatformPubliclightingKeys.HISTORY_TERM_TYPE, HistoryTermType.class,
                PlatformPubliclightingDefaults.DEFAULT_OSLP_HISTORY_TERM_TYPE), request.getTermType());
        if (expectedParameters.containsKey(PlatformPubliclightingKeys.KEY_PAGE) && !expectedParameters.get(PlatformPubliclightingKeys.KEY_PAGE).isEmpty()) {
            Assert.assertEquals((int) getInteger(expectedParameters, PlatformPubliclightingKeys.KEY_PAGE), request.getPage());
        }
        if (expectedParameters.containsKey(PlatformPubliclightingKeys.START_TIME) && !expectedParameters.get(PlatformPubliclightingKeys.START_TIME).isEmpty()
                && expectedParameters.get(PlatformPubliclightingKeys.START_TIME) != null) {
            Assert.assertEquals(getString(expectedParameters, PlatformPubliclightingKeys.START_TIME), request.getTimePeriod().getStartTime());
        }
        if (expectedParameters.containsKey(PlatformPubliclightingKeys.END_TIME) && !expectedParameters.get(PlatformPubliclightingKeys.END_TIME).isEmpty()
                && expectedParameters.get(PlatformPubliclightingKeys.END_TIME) != null) {
            Assert.assertEquals(getString(expectedParameters, PlatformPubliclightingKeys.END_TIME), request.getTimePeriod().getEndTime());
        }
    }

    /**
     * Verify that a get status OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a get status \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aGetStatusOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^an update firmware \"([^\"]*)\" message is sent to the device$")
    public void anUpdateFirmwareOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.UPDATE_FIRMWARE);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasUpdateFirmwareRequest());

        message.getUpdateFirmwareRequest();
    }

    @Then("^an update key \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void anUpdateKeyOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^a resume schedule \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aResumeScheduleOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification,
            final Map<String, String> expectedRequest) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.RESUME_SCHEDULE);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasResumeScheduleRequest());

        final ResumeScheduleRequest request = message.getResumeScheduleRequest();

        Assert.assertEquals(getBoolean(expectedRequest, PlatformPubliclightingKeys.KEY_ISIMMEDIATE), request.getImmediate());
        Assert.assertEquals(getInteger(expectedRequest, PlatformPubliclightingKeys.KEY_INDEX),
                OslpUtils.byteStringToInteger(request.getIndex()));
    }

    /**
     * Verify that a set configuration OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set configuration \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aSetConfigurationOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^a set event notification \"([^\"]*)\" message is sent to device \"([^\"]*)\"")
    public void aSetEventNotificationOslpMessageIsSentToDevice(final String protocol,
            final String deviceIdentification) {
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
    @Then("^a set light \"([^\"]*)\" message with \"([^\"]*)\" lightvalues is sent to the device$")
    public void aSetLightOslpMessageWithLightValuesIsSentToTheDevice(final String protocol, final int nofLightValues) {
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
    @Then("^a set light \"([^\"]*)\" message with one light value is sent to the device$")
    public void aSetLightOSLPMessageWithOneLightvalueIsSentToTheDevice(final String protocol,
            final Map<String, String> expectedParameters) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_LIGHT);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetLightRequest());

        final LightValue lightValue = message.getSetLightRequest().getValues(0);

        Assert.assertEquals(getInteger(expectedParameters, PlatformPubliclightingKeys.KEY_INDEX, PlatformPubliclightingDefaults.DEFAULT_INDEX),
                OslpUtils.byteStringToInteger(lightValue.getIndex()));
        if (expectedParameters.containsKey(PlatformPubliclightingKeys.KEY_DIMVALUE)
                && !StringUtils.isEmpty(expectedParameters.get(PlatformPubliclightingKeys.KEY_DIMVALUE))) {
            Assert.assertEquals(getInteger(expectedParameters, PlatformPubliclightingKeys.KEY_DIMVALUE, PlatformPubliclightingDefaults.DEFAULT_DIMVALUE),
                    OslpUtils.byteStringToInteger(lightValue.getDimValue()));
        }
        Assert.assertEquals(getBoolean(expectedParameters, PlatformPubliclightingKeys.KEY_ON, PlatformPubliclightingDefaults.DEFAULT_ON), lightValue.getOn());
    }

    /**
     * Verify that a set light schedule OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device. @
     */
    @Then("^a set light schedule \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aSetLightScheduleOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification,
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
    @Then("^a set reboot \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aSetRebootOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_REBOOT);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetRebootRequest());

        @SuppressWarnings("unused")
        final SetRebootRequest request = message.getSetRebootRequest();
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
    @Then("^a set reverse tariff schedule \"([^\"]*)\" message is sent to device \"(?:([^\"]*))\"$")
    public void aSetReverseTariffScheduleOSLPMessageIsSentToDevice(final String protocol,
            final String deviceIdentification, final Map<String, String> expectedRequest) {
        this.aSetTariffScheduleOSLPMessageIsSentToDevice(protocol, deviceIdentification, expectedRequest);
    }

    /**
     * Verify that a set tariff schedule OSLP message is sent to the device.
     *
     * @param deviceIdentification
     *            The device identification expected in the message to the
     *            device.
     */
    @Then("^a set tariff schedule \"([^\"]*)\" message is sent to device \"(?:([^\"]*))\"$")
    public void aSetTariffScheduleOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification,
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
    @Then("^a set transition \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aSetTransitionOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification,
            final Map<String, String> expectedResult) {
        final Message message = this.oslpMockServer.waitForRequest(DeviceRequestMessageType.SET_TRANSITION);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.hasSetTransitionRequest());

        final SetTransitionRequest request = message.getSetTransitionRequest();

        Assert.assertEquals(getEnum(expectedResult, PlatformPubliclightingKeys.KEY_TRANSITION_TYPE, TransitionType.class),
                request.getTransitionType());
        if (expectedResult.containsKey(PlatformPubliclightingKeys.KEY_TIME)) {
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
    @Then("^a start device \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aStartDeviceOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
    @Then("^a stop device \"([^\"]*)\" message is sent to device \"([^\"]*)\"$")
    public void aStopDeviceOSLPMessageIsSentToDevice(final String protocol, final String deviceIdentification) {
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
                Assert.assertEquals(getEnum(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_WEEKDAY, Weekday.class),
                        schedule.getWeekday());
            }
            if (!expectedRequest.get(PlatformPubliclightingKeys.SCHEDULE_STARTDAY).isEmpty()) {
                final String startDay = getDate(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_STARTDAY).toDateTime(DateTimeZone.UTC)
                        .toString("yyyyMMdd");

                Assert.assertEquals(startDay, schedule.getStartDay());
            }
            if (!expectedRequest.get(PlatformPubliclightingKeys.SCHEDULE_ENDDAY).isEmpty()) {
                final String endDay = getDate(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_ENDDAY).toDateTime(DateTimeZone.UTC)
                        .toString("yyyyMMdd");

                Assert.assertEquals(endDay, schedule.getEndDay());
            }

            if (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) {
                Assert.assertEquals(getEnum(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_ACTIONTIME, ActionTime.class),
                        schedule.getActionTime());
            }
            String expectedTime = getString(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_TIME).replace(":", "");
            if (expectedTime.contains(".")) {
                expectedTime = expectedTime.substring(0, expectedTime.indexOf("."));
            }
            Assert.assertEquals(expectedTime, schedule.getTime());
            final String scheduleLightValue = getString(expectedRequest,
                    (type == DeviceRequestMessageType.SET_LIGHT_SCHEDULE) ? PlatformPubliclightingKeys.SCHEDULE_LIGHTVALUES
                            : PlatformPubliclightingKeys.SCHEDULE_TARIFFVALUES);
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
                Assert.assertEquals((!getString(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_TRIGGERTYPE).isEmpty())
                        ? getEnum(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_TRIGGERTYPE, TriggerType.class)
                        : TriggerType.TT_NOT_SET, schedule.getTriggerType());

                final String[] windowTypeValues = getString(expectedRequest, PlatformPubliclightingKeys.SCHEDULE_TRIGGERWINDOW).split(",");
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
    @When("^receiving an \"([^\"]*)\" event notification message$")
    public void receivingAnOSLPEventNotificationMessage(final String protocol, final Map<String, String> settings)
            throws DeviceSimulatorException, IOException, ParseException {

        final EventNotification eventNotification = EventNotification.newBuilder()
                .setDescription(getString(settings, PlatformPubliclightingKeys.KEY_DESCRIPTION, ""))
                .setEvent(getEnum(settings, PlatformPubliclightingKeys.KEY_EVENT, Event.class)).build();

        final Message message = Oslp.Message.newBuilder()
                .setEventNotificationRequest(EventNotificationRequest.newBuilder().addNotifications(eventNotification))
                .build();

        // Save the OSLP response for later validation.
        ScenarioContext.current().put(PlatformPubliclightingKeys.RESPONSE, this.oslpMockServer.sendRequest(message));
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
        final String sMeterType = getString(responseData, PlatformPubliclightingKeys.METER_TYPE);
        if (!sMeterType.toString().contains("_") && sMeterType.equals(MeterType.P1_VALUE)) {
            final String[] sMeterTypeArray = sMeterType.toString().split("");
            meterType = MeterType.valueOf(sMeterTypeArray[0] + "_" + sMeterTypeArray[1]);
        } else {
            meterType = getEnum(responseData, PlatformPubliclightingKeys.METER_TYPE, MeterType.class);
        }

        this.oslpMockServer.mockGetActualPowerUsageResponse(Enum.valueOf(Status.class, result),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_CONSUMED_POWER, null), meterType,
                getDate(responseData, PlatformPubliclightingKeys.RECORD_TIME).toDateTime(DateTimeZone.UTC).toString("yyyyMMddHHmmss"),
                getInteger(responseData, PlatformPubliclightingKeys.TOTAL_CONSUMED_ENERGY, null),
                getInteger(responseData, PlatformPubliclightingKeys.TOTAL_LIGHTING_HOURS, null),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_CURRENT1, null),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_CURRENT2, null),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_CURRENT3, null),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_POWER1, null), getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_POWER2, null),
                getInteger(responseData, PlatformPubliclightingKeys.ACTUAL_POWER3, null),
                getInteger(responseData, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR1, null),
                getInteger(responseData, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR2, null),
                getInteger(responseData, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR3, null),
                getString(responseData, PlatformPubliclightingKeys.RELAY_DATA, null));
    }

    @Given("^the device returns a get configuration status over \"([^\"]*)\"$")
    public void theDeviceReturnsAGetConfigurationStatusOverOSLP(final String protocol,
            final Map<String, String> requestParameters) {
        this.theDeviceReturnsAGetConfigurationStatusWithResultOverOSLP(
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_STATUS, Status.class, Status.OK).name(), protocol,
                requestParameters);
    }

    /**
     * Setup method to set the configuration status which should be returned by
     * the mock.
     *
     * @param status
     *            The status to respond.
     */
    @Given("^the device returns a get configuration status \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAGetConfigurationStatusWithResultOverOSLP(final String result, final String protocol,
            final Map<String, String> requestParameters) {
        // Note: This piece of code has been made because there are multiple
        // enumerations with the name MeterType, but not all of them has all
        // values the same. Some with underscore and some without.
        MeterType meterType = MeterType.MT_NOT_SET;
        final String sMeterType = getString(requestParameters, PlatformPubliclightingKeys.METER_TYPE);
        if (!sMeterType.toString().contains("_") && sMeterType.equals(MeterType.P1_VALUE)) {
            final String[] sMeterTypeArray = sMeterType.toString().split("");
            meterType = MeterType.valueOf(sMeterTypeArray[0] + "_" + sMeterTypeArray[1]);
        } else {
            meterType = getEnum(requestParameters, PlatformPubliclightingKeys.METER_TYPE, MeterType.class);
        }

        this.oslpMockServer.mockGetConfigurationResponse(Enum.valueOf(Status.class, result),
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_LIGHTTYPE, LightType.class),
                getString(requestParameters, PlatformPubliclightingKeys.DC_LIGHTS, PlatformPubliclightingDefaults.DC_LIGHTS),
                getString(requestParameters, PlatformPubliclightingKeys.DC_MAP), getEnum(requestParameters, PlatformPubliclightingKeys.RC_TYPE, RelayType.class),
                getString(requestParameters, PlatformPubliclightingKeys.RC_MAP),
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_PREFERRED_LINKTYPE, LinkType.class), meterType,
                getInteger(requestParameters, PlatformPubliclightingKeys.SHORT_INTERVAL, PlatformPubliclightingDefaults.SHORT_INTERVAL),
                getInteger(requestParameters, PlatformPubliclightingKeys.LONG_INTERVAL, PlatformPubliclightingDefaults.LONG_INTERVAL),
                getEnum(requestParameters, PlatformPubliclightingKeys.INTERVAL_TYPE, LongTermIntervalType.class));
    }

    /**
     * Setup method to get the power usage history which should be returned by
     * the mock.
     *
     * @param requestParameters
     */
    @Given("^the device returns a get power usage history response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAGetPowerUsageHistoryOverOSLP(final String result, final String protocol,
            final Map<String, String> requestParameters) {
        this.oslpMockServer.mockGetPowerUsageHistoryResponse(Enum.valueOf(Status.class, result),
                getString(requestParameters, PlatformPubliclightingKeys.RECORD_TIME), getInteger(requestParameters, PlatformPubliclightingKeys.KEY_INDEX),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_CONSUMED_POWER, null),
                getEnum(requestParameters, PlatformPubliclightingKeys.METER_TYPE, MeterType.class),
                getInteger(requestParameters, PlatformPubliclightingKeys.TOTAL_CONSUMED_ENERGY, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.TOTAL_LIGHTING_HOURS, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_CURRENT1, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_CURRENT2, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_CURRENT3, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_POWER1, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_POWER2, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.ACTUAL_POWER3, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR1, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR2, null),
                getInteger(requestParameters, PlatformPubliclightingKeys.AVERAGE_POWER_FACTOR3, null),
                getString(requestParameters, PlatformPubliclightingKeys.RELAY_DATA, null));
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     *            The get status to respond.
     */
    @Given("^the device returns a get status response over \"([^\"]*)\"$")
    public void theDeviceReturnsAGetStatusResponseOverOSLP(final String protocol, final Map<String, String> result) {
        int eventNotificationTypes = 0;
        final String eventNotificationTypesString = getString(result, PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONTYPES,
                PlatformPubliclightingDefaults.DEFAULT_EVENTNOTIFICATIONTYPES);
        for (final String eventNotificationType : eventNotificationTypesString.split(PlatformPubliclightingKeys.SEPARATOR_COMMA)) {
            if (!eventNotificationType.isEmpty()) {
                eventNotificationTypes = eventNotificationTypes
                        + Enum.valueOf(EventNotificationTypeDto.class, eventNotificationType.trim()).getValue();
            }
        }

        final List<LightValue> lightValues = new ArrayList<>();
        if (!getString(result, PlatformPubliclightingKeys.KEY_LIGHTVALUES, PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES).isEmpty()
                && getString(result, PlatformPubliclightingKeys.KEY_LIGHTVALUES, PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES)
                        .split(PlatformPubliclightingKeys.SEPARATOR_SEMICOLON).length > 0) {

            for (final String lightValueString : getString(result, PlatformPubliclightingKeys.KEY_LIGHTVALUES, PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES)
                    .split(PlatformPubliclightingKeys.SEPARATOR_SEMICOLON)) {
                final String[] parts = lightValueString.split(PlatformPubliclightingKeys.SEPARATOR_COMMA);

                final LightValue lightValue = LightValue.newBuilder()
                        .setIndex(OslpUtils.integerToByteString(Integer.parseInt(parts[0])))
                        .setOn(parts[1].toLowerCase().equals("true"))
                        .setDimValue(OslpUtils.integerToByteString(Integer.parseInt(parts[2]))).build();

                lightValues.add(lightValue);
            }
        }

        this.oslpMockServer.mockGetStatusResponse(
                getEnum(result, PlatformPubliclightingKeys.KEY_PREFERRED_LINKTYPE, LinkType.class, PlatformPubliclightingDefaults.DEFAULT_PREFERRED_LINKTYPE),
                getEnum(result, PlatformPubliclightingKeys.KEY_ACTUAL_LINKTYPE, LinkType.class, PlatformPubliclightingDefaults.DEFAULT_ACTUAL_LINKTYPE),
                getEnum(result, PlatformPubliclightingKeys.KEY_LIGHTTYPE, LightType.class, PlatformPubliclightingDefaults.DEFAULT_LIGHTTYPE),
                eventNotificationTypes, getEnum(result, PlatformPubliclightingKeys.KEY_STATUS, Oslp.Status.class, PlatformPubliclightingDefaults.DEFAULT_STATUS),
                lightValues);
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a get status response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAGetStatusResponseWithResultOverOSLP(final String result, final String protocol,
            final Map<String, String> requestParameters) {

        int eventNotificationTypes = 0;
        if (getString(requestParameters, PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONTYPES, PlatformPubliclightingDefaults.DEFAULT_EVENTNOTIFICATIONTYPES)
                .trim().split(PlatformPubliclightingKeys.SEPARATOR_COMMA).length > 0) {
            for (final String eventNotificationType : getString(requestParameters, PlatformPubliclightingKeys.KEY_EVENTNOTIFICATIONTYPES,
                    PlatformPubliclightingDefaults.DEFAULT_EVENTNOTIFICATIONTYPES).trim().split(PlatformPubliclightingKeys.SEPARATOR_COMMA)) {
                if (!eventNotificationType.isEmpty()) {
                    eventNotificationTypes = eventNotificationTypes
                            + Enum.valueOf(EventNotificationType.class, eventNotificationType.trim()).getValue();
                }
            }
        }

        final List<LightValue> lightValues = new ArrayList<>();
        if (!getString(requestParameters, PlatformPubliclightingKeys.KEY_LIGHTVALUES, PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES).isEmpty()
                && getString(requestParameters, PlatformPubliclightingKeys.KEY_LIGHTVALUES, PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES)
                        .split(PlatformPubliclightingKeys.SEPARATOR_SEMICOLON).length > 0) {

            for (final String lightValueString : getString(requestParameters, PlatformPubliclightingKeys.KEY_LIGHTVALUES,
                    PlatformPubliclightingDefaults.DEFAULT_LIGHTVALUES).split(PlatformPubliclightingKeys.SEPARATOR_SEMICOLON)) {
                final String[] parts = lightValueString.split(PlatformPubliclightingKeys.SEPARATOR_COMMA);

                final LightValue lightValue = LightValue.newBuilder()
                        .setIndex(OslpUtils.integerToByteString(Integer.parseInt(parts[0])))
                        .setOn(parts[1].toLowerCase().equals("true"))
                        .setDimValue(OslpUtils.integerToByteString(Integer.parseInt(parts[2]))).build();

                lightValues.add(lightValue);
            }
        }

        this.oslpMockServer.mockGetStatusResponse(
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_PREFERRED_LINKTYPE, LinkType.class,
                        PlatformPubliclightingDefaults.DEFAULT_PREFERRED_LINKTYPE),
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_ACTUAL_LINKTYPE, LinkType.class, PlatformPubliclightingDefaults.DEFAULT_ACTUAL_LINKTYPE),
                getEnum(requestParameters, PlatformPubliclightingKeys.KEY_LIGHTTYPE, LightType.class, PlatformPubliclightingDefaults.DEFAULT_LIGHTTYPE),
                eventNotificationTypes, Enum.valueOf(Status.class, result), lightValues);
    }

    /**
     * Setup method to resume a schedule which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a resume schedule response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAResumeScheduleResponseOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockResumeScheduleResponse(Enum.valueOf(Status.class, result));
    }

    @Given("^the device returns a set configuration status over \"([^\"]*)\"$")
    public void theDeviceReturnsASetConfigurationStatusOverOSLP(final String protocol,
            final Map<String, String> settings) {
        this.theDeviceReturnsASetConfigurationStatusWithStatusOverOSLP(
                getEnum(settings, PlatformPubliclightingKeys.KEY_STATUS, Status.class).name(), protocol);
    }

    /**
     * Setup method to set the configuration status which should be returned by
     * the mock.
     *
     * @param status
     *            The status to respond.
     */
    @Given("^the device returns a set configuration status \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetConfigurationStatusWithStatusOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockSetConfigurationResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set the event notification which should be returned by
     * the mock.
     *
     * @param result
     */
    @Given("^the device returns a set event notification \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetEventNotificationOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockSetEventNotificationResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set a light which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set light response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetLightOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockSetLightResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to get a status which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set light schedule response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetLightScheduleResponseOverOSLP(final String result, final String protocol) {
        this.callMockSetScheduleResponse(result, DeviceRequestMessageType.SET_LIGHT_SCHEDULE);
    }

    /**
     * Setup method to set a reboot which should be returned by the mock.
     *
     * @param resultEnum.valueOf(Status.class,
     *            result) The stop device to respond.
     */
    @Given("^the device returns a set reboot response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetRebootResponseOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockSetRebootResponse(Enum.valueOf(Status.class, result));
    }

    @Given("^the device returns a set reverse tariff schedule response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetReverseTariffScheduleResponseOverOSLP(final String result, final String protocol) {
        this.theDeviceReturnsASetTariffScheduleResponseOverOSLP(result, protocol);
    }

    @Given("^the device returns a set tariff schedule response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetTariffScheduleResponseOverOSLP(final String result, final String protocol) {
        this.callMockSetScheduleResponse(result, DeviceRequestMessageType.SET_TARIFF_SCHEDULE);
    }

    /**
     * Setup method to set a transition which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a set transition response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsASetTransitionResponseOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockSetTransitionResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to start a device which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a start device response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAStartDeviceResponseOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockStartDeviceResponse(Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to stop a device which should be returned by the mock.
     *
     * @param result
     */
    @Given("^the device returns a stop device response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsAStopDeviceResponseOverOSLP(final String result, final String protocol) {
        // TODO: Check if ByteString.EMPTY must be something else
        this.oslpMockServer.mockStopDeviceResponse(ByteString.EMPTY, Enum.valueOf(Status.class, result));
    }

    /**
     * Setup method to set the firmware which should be returned by the mock.
     *
     * @param firmwareVersion
     *            The firmware to respond.
     */
    @Given("^the device returns firmware version \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsFirmwareVersionOverOSLP(final String firmwareVersion, final String protocol) {
        this.oslpMockServer.mockGetFirmwareVersionResponse(firmwareVersion);
    }

    /**
     * Setup method to set the firmware which should be returned by the mock.
     *
     * @param firmwareVersion
     *            The firmware to respond.
     */
    @Given("^the device returns update firmware response \"([^\"]*)\" over \"([^\"]*)\"$")
    public void theDeviceReturnsUpdateFirmwareResponseOverOSLP(final String result, final String protocol) {
        this.oslpMockServer.mockUpdateFirmwareResponse(Enum.valueOf(Status.class, result));
    }

    @Then("^the \"([^\"]*)\" event notification response contains$")
    public void theOSLPEventNotificationResponseContains(final String protocol,
            final Map<String, String> expectedResponse) {
        final Message responseMessage = (Message) ScenarioContext.current().get(PlatformPubliclightingKeys.RESPONSE);

        final EventNotificationResponse response = responseMessage.getEventNotificationResponse();

        Assert.assertEquals(getString(expectedResponse, PlatformPubliclightingKeys.KEY_STATUS), response.getStatus());
    }

    @Given("^the device sends a register device request to the platform over \"([^\"]*)\"$")
    public void theDeviceSendsARegisterDeviceRequestToThePlatform(final String protocol,
            final Map<String, String> settings) throws IOException, DeviceSimulatorException {

        LOGGER.info("IpAddress from feature: [{}]", getString(settings, PlatformPubliclightingKeys.IP_ADDRESS, PlatformPubliclightingDefaults.LOCALHOST));
        final InetAddress address = InetAddress.getByName(getString(settings, PlatformPubliclightingKeys.IP_ADDRESS, PlatformPubliclightingDefaults.LOCALHOST));
        final byte[] ba = address.getAddress();
        LOGGER.info("getAddress() from inetAddress: [{}.{}.{}.{}]", ba[0], ba[1], ba[2], ba[3]);
        LOGGER.info("getHostAddress() from inetAddress: [{}]", address.getHostAddress());
        LOGGER.info("getHostName() from inetAddress: [{}]", address.getHostName());

        try {
            final OslpEnvelope request = this
                    .createEnvelopeBuilder(getString(settings, PlatformPubliclightingKeys.KEY_DEVICE_UID, PlatformPubliclightingDefaults.DEVICE_UID),
                            this.oslpMockServer.getSequenceNumber())
                    .withPayloadMessage(Message.newBuilder().setRegisterDeviceRequest(Oslp.RegisterDeviceRequest
                            .newBuilder()
                            .setDeviceIdentification(getString(settings, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION,
                                    PlatformPubliclightingDefaults.DEFAULT_DEVICE_IDENTIFICATION))
                            .setIpAddress(ByteString.copyFrom(InetAddress
                                    .getByName(getString(settings, PlatformPubliclightingKeys.IP_ADDRESS, PlatformPubliclightingDefaults.LOCALHOST)).getAddress()))
                            .setDeviceType(getEnum(settings, PlatformPubliclightingKeys.KEY_DEVICE_TYPE, DeviceType.class, DeviceType.PSLD))
                            .setHasSchedule(getBoolean(settings, PlatformPubliclightingKeys.KEY_HAS_SCHEDULE, PlatformPubliclightingDefaults.DEFAULT_HASSCHEDULE))
                            .setRandomDevice(getInteger(settings, PlatformPubliclightingKeys.RANDOM_DEVICE, PlatformPubliclightingDefaults.RANDOM_DEVICE))).build())
                    .build();

            this.send(request, settings);
        } catch (final IOException ioe) {
            ScenarioContext.current().put("Error", ioe);
        } catch (final IllegalArgumentException iae) {
            ScenarioContext.current().put("Error", iae);
        }

    }

    @Given("^the device sends an event notification request to the platform over \"([^\"]*)\"$")
    public void theDeviceSendsAnEventNotificationRequestToThePlatform(final String protocol,
            final Map<String, String> settings) throws IOException, DeviceSimulatorException {

        this.oslpMockServer.doNextSequenceNumber();

        final Oslp.EventNotification.Builder builder = Oslp.EventNotification.newBuilder()
                .setEvent(getEnum(settings, PlatformPubliclightingKeys.KEY_EVENT, Event.class))
                .setDescription(getString(settings, PlatformPubliclightingKeys.KEY_DESCRIPTION));

        builder.setIndex((settings.containsKey(PlatformPubliclightingKeys.KEY_INDEX) && !settings.get(PlatformPubliclightingKeys.KEY_INDEX).equals("EMPTY"))
                ? ByteString.copyFrom(getString(settings, PlatformPubliclightingKeys.KEY_INDEX).getBytes())
                : ByteString.copyFrom("0".getBytes()));

        final OslpEnvelope request = this
                .createEnvelopeBuilder(getString(settings, PlatformPubliclightingKeys.KEY_DEVICE_UID, PlatformPubliclightingDefaults.DEVICE_UID),
                        this.oslpMockServer.getSequenceNumber())
                .withPayloadMessage(Message.newBuilder()
                        .setEventNotificationRequest(
                                Oslp.EventNotificationRequest.newBuilder().addNotifications(builder.build()))
                        .build())
                .build();

        this.send(request, settings);
    }

    @When("^the device sends multiple event notifications request to the platform over \"([^\"]*)\"$")
    public void theDeviceSendsMultipleEventNotificationsRequestToThePlatform(final String protocol,
            final Map<String, String> settings) throws IOException, DeviceSimulatorException {

        this.oslpMockServer.doNextSequenceNumber();

        final Oslp.EventNotificationRequest.Builder requestBuilder = Oslp.EventNotificationRequest.newBuilder();
        final Oslp.EventNotification.Builder builder = Oslp.EventNotification.newBuilder();

        final String[] events = getString(settings, PlatformPubliclightingKeys.KEY_EVENTS).split(PlatformPubliclightingKeys.SEPARATOR_COMMA),
                indexes = getString(settings, PlatformPubliclightingKeys.KEY_INDEXES).split(PlatformPubliclightingKeys.SEPARATOR_COMMA);

        for (int i = 0; i < events.length; i++) {
            if (!events[i].isEmpty() && !indexes[i].isEmpty()) {
                builder.setEvent(Event.valueOf(events[i].trim()));
                builder.setIndex((!indexes[i].equals("EMPTY")) ? ByteString.copyFrom(indexes[i].getBytes())
                        : ByteString.copyFrom("0".getBytes()));
                requestBuilder.addNotifications(builder.build());
            }
        }

        final OslpEnvelope request = this
                .createEnvelopeBuilder(getString(settings, PlatformPubliclightingKeys.KEY_DEVICE_UID, PlatformPubliclightingDefaults.DEVICE_UID),
                        this.oslpMockServer.getSequenceNumber())
                .withPayloadMessage(Message.newBuilder().setEventNotificationRequest(requestBuilder.build()).build())
                .build();

        this.send(request, settings);
    }

    /**
     * Verify that we have received a response over OSLP/OSLP ELSTER
     *
     * @param expectedResponse
     * @throws DeviceSimulatorException
     * @throws IOException
     */
    @Then("^the event notification response contains$")
    public void theEventNotificationResponseContains(final Map<String, String> expectedResponse) {
        final Message responseMessage = this.oslpMockServer.waitForResponse();

        final EventNotificationResponse response = responseMessage.getEventNotificationResponse();

        Assert.assertEquals(getString(expectedResponse, PlatformPubliclightingKeys.KEY_STATUS), response.getStatus().name());
    }

    @Given("^the device sends an event notification request with sequencenumber \"([^\"]*)\" to the platform over \"([^\"]*)\"$")
    public void theDeviceSendsAStartDeviceResponseOver(final Integer sequenceNumber, final String protocol,
            final Map<String, String> settings) throws IOException, DeviceSimulatorException {

        ScenarioContext.current().put(PlatformPubliclightingKeys.NUMBER_TO_ADD_TO_SEQUENCE_NUMBER, sequenceNumber);

        this.theDeviceSendsAnEventNotificationRequestToThePlatform(protocol, settings);
    }

    /**
     * Verify that we have received a response over OSLP/OSLP ELSTER
     *
     * @param expectedResponse
     */
    @Then("^the register device response contains$")
    public void theRegisterDeviceResponseContains(final Map<String, String> expectedResponse)
            throws IOException, DeviceSimulatorException {
        final Exception e = (Exception) ScenarioContext.current().get("Error");
        if (e == null || getString(expectedResponse, PlatformPubliclightingKeys.MESSAGE) == null) {
            final Message responseMessage = this.oslpMockServer.waitForResponse();

            final RegisterDeviceResponse response = responseMessage.getRegisterDeviceResponse();

            Assert.assertNotNull(response.getCurrentTime());
            Assert.assertNotNull(response.getLocationInfo().getLongitude());
            Assert.assertNotNull(response.getLocationInfo().getLatitude());
            Assert.assertNotNull(response.getLocationInfo().getTimeOffset());

            Assert.assertEquals(getString(expectedResponse, PlatformPubliclightingKeys.KEY_STATUS), response.getStatus().name());
        } else {
            Assert.assertEquals(getString(expectedResponse, PlatformPubliclightingKeys.MESSAGE), e.getMessage());
        }

    }

    public OslpEnvelope.Builder createEnvelopeBuilder(final String deviceUid, final Integer sequenceNumber) {
        final byte[] sequenceNumberBytes = new byte[2];
        sequenceNumberBytes[0] = (byte) (sequenceNumber >>> 8);
        sequenceNumberBytes[1] = (byte) (sequenceNumber >>> 0);

        return new OslpEnvelope.Builder().withSignature(this.oslpMockServer.getOslpSignature())
                .withProvider(this.oslpMockServer.getOslpSignatureProvider())
                .withPrimaryKey(this.oslpMockServer.privateKey()).withDeviceId(Base64.decodeBase64(deviceUid))
                .withSequenceNumber(sequenceNumberBytes);
    }

    private OslpEnvelope send(final OslpEnvelope request, final Map<String, String> settings)
            throws IOException, DeviceSimulatorException {
        final String deviceIdentification = getString(settings, PlatformPubliclightingKeys.KEY_DEVICE_IDENTIFICATION);
        final String hostname = this.configuration.getPlatform();
        final String protocol = getString(settings, PlatformPubliclightingKeys.KEY_PROTOCOL, PlatformPubliclightingDefaults.DEFAULT_PROTOCOL);

        InetSocketAddress address = null;

        switch (protocol) {
        case "OSLP ELSTER":
            address = new InetSocketAddress(hostname, PlatformPubliclightingDefaults.OSLP_ELSTER_SERVER_PORT);
            break;
        case "OSLP":
            address = new InetSocketAddress(hostname, PlatformPubliclightingDefaults.OSLP_SERVER_PORT);
            break;
        default:
            address = new InetSocketAddress(hostname, PlatformPubliclightingDefaults.OSLP_SERVER_PORT);
            break;
        }

        return this.oslpMockServer.send(address, request, deviceIdentification);
    }
}