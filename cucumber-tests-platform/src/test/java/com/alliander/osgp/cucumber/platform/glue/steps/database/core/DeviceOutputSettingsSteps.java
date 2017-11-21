/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.glue.steps.database.core;

import static com.alliander.osgp.cucumber.core.Helpers.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.platform.PlatformDefaults;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.core.converters.DeviceOutputSettingConverter;
import com.alliander.osgp.domain.core.entities.DeviceOutputSetting;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.repositories.SsldRepository;
import com.alliander.osgp.domain.core.valueobjects.RelayType;

import cucumber.api.java.en.Given;

public class DeviceOutputSettingsSteps extends GlueBase {

    @Autowired
    private SsldRepository ssldRepository;

    /**
     * Adds a single device output setting to the device.
     */
    @Given("^a device output setting$")
    public void aDeviceOutputSetting(final Map<String, String> settings) throws Throwable {
        this.deviceOutputSettings(settings);
    }

    /**
     * Adds zero or more device output settings to the device.
     */
    @Given("^device output settings$")
    public void deviceOutputSettings(final Map<String, String> settings) throws Throwable {
        final String deviceIdentification = getString(settings, PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                PlatformDefaults.DEFAULT_DEVICE_IDENTIFICATION);

        final Ssld device = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
        device.updateOutputSettings(DeviceOutputSettingConverter.toDeviceOutputSettings(settings));

        this.ssldRepository.save(device);
    }

    @Given("^device output settings for lightvalues$")
    public void deviceOutputSettingsForLightValues(final Map<String, String> settings) throws Throwable {
        final String deviceIdentification = getString(settings, PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                PlatformDefaults.DEFAULT_DEVICE_IDENTIFICATION);

        final Ssld device = this.ssldRepository.findByDeviceIdentification(deviceIdentification);

        final String[] lightValues = getString(settings, PlatformKeys.KEY_LIGHTVALUES,
                PlatformDefaults.DEFAULT_DEVICE_IDENTIFICATION).split(PlatformKeys.SEPARATOR_SEMICOLON);

        final String[] deviceOutputSettings = getString(settings, PlatformKeys.DEVICE_OUTPUT_SETTINGS, "")
                .split(PlatformKeys.SEPARATOR_SEMICOLON);

        final List<DeviceOutputSetting> outputSettings = new ArrayList<>();
        for (int i = 0; i < lightValues.length; i++) {

            final String[] lightValueParts = lightValues[i].split(PlatformKeys.SEPARATOR_COMMA);

            final String[] deviceOutputSettingsPart = deviceOutputSettings[i].split(PlatformKeys.SEPARATOR_COMMA);

            final DeviceOutputSetting deviceOutputSettingsForLightValue = new DeviceOutputSetting(
                    Integer.parseInt(deviceOutputSettingsPart[0]), Integer.parseInt(lightValueParts[0]),
                    Enum.valueOf(RelayType.class, deviceOutputSettingsPart[1]), deviceOutputSettingsPart[2]);
            outputSettings.add(deviceOutputSettingsForLightValue);
        }

        device.updateOutputSettings(outputSettings);

        this.ssldRepository.save(device);
    }
}
