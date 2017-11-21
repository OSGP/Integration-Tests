package com.alliander.osgp.cucumber.platform.core.converters;

import static com.alliander.osgp.cucumber.core.Helpers.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.domain.core.entities.DeviceOutputSetting;
import com.alliander.osgp.domain.core.valueobjects.RelayType;

public class DeviceOutputSettingConverter {

    public static DeviceOutputSetting toDeviceOutputSetting(final String deviceOutputSettingString) {
        final String[] deviceOutputSettingParts = deviceOutputSettingString.split(PlatformKeys.SEPARATOR_COMMA);

        final DeviceOutputSetting deviceOutputSetting = new DeviceOutputSetting(
                Integer.parseInt(deviceOutputSettingParts[0]), Integer.parseInt(deviceOutputSettingParts[1]),
                Enum.valueOf(RelayType.class, deviceOutputSettingParts[2]));

        if (deviceOutputSettingParts.length == 4) {
            deviceOutputSetting.setAlias(deviceOutputSettingParts[3]);
        }

        return deviceOutputSetting;
    }

    public static List<DeviceOutputSetting> toDeviceOutputSettings(final Map<String, String> inputSettings) {
        final String[] deviceOutputSettingsStrings = getString(inputSettings, PlatformKeys.DEVICE_OUTPUT_SETTINGS, "")
                .split(PlatformKeys.SEPARATOR_SEMICOLON);

        final List<DeviceOutputSetting> outputSettings = new ArrayList<>();
        for (final String deviceOutputSettingString : deviceOutputSettingsStrings) {
            final DeviceOutputSetting dos = DeviceOutputSettingConverter
                    .toDeviceOutputSetting(deviceOutputSettingString);
            outputSettings.add(dos);
        }

        return outputSettings;
    }
}
