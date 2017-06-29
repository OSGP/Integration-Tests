/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.database.adapterprotocoliec61850;

import com.alliander.osgp.adapter.protocol.iec61850.domain.entities.Iec61850Device;
import com.alliander.osgp.adapter.protocol.iec61850.domain.repositories.Iec61850DeviceRepository;
import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import cucumber.api.java.en.Given;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationDefaults;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.PlatformDistributionAutomationKeys;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config.Iec61850MockServerConfig;
import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.glue.steps.database.core.RtuDeviceSteps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.alliander.osgp.cucumber.core.Helpers.getInteger;
import static com.alliander.osgp.cucumber.core.Helpers.getString;

/**
 * IEC61850 specific device steps.
 */
public class Iec61850DeviceSteps extends GlueBase {

    private static final String DEFAULT_DEVICE_TYPE = "RTU";
    private static final String DEFAULT_PROTOCOL = "IEC61850";
    private static final String DEFAULT_PROTOCOL_VERSION = "1.0";

    private static final Map<String, String> RTU_DEFAULT_SETTINGS = Collections
            .unmodifiableMap(new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;
                {
                    this.put(PlatformDistributionAutomationKeys.KEY_DEVICE_TYPE, DEFAULT_DEVICE_TYPE);
                    this.put(PlatformDistributionAutomationKeys.KEY_PROTOCOL, DEFAULT_PROTOCOL);
                    this.put(PlatformDistributionAutomationKeys.KEY_PROTOCOL_VERSION, DEFAULT_PROTOCOL_VERSION);
                }
            });

    @Autowired
    private Iec61850DeviceRepository iec61850DeviceRepository;

    @Autowired
    private Iec61850MockServerConfig iec61850MockServerConfig;

    @Autowired
    private RtuDeviceSteps rtuDeviceSteps;

    /**
     * Creates an IEC61850 device.
     *
     * @param settings
     * @throws Throwable
     */
    @Given("^an rtu iec61850 device$")
    public void anRtuIec61850Device(final Map<String, String> settings) throws Throwable {

        ScenarioContext.current().put(PlatformDistributionAutomationKeys.KEY_DEVICE_IDENTIFICATION, PlatformDistributionAutomationDefaults.DEFAULT_DEVICE_IDENTIFICATION);
        final Map<String, String> rtuSettings = SettingsHelper.addAsDefaults(settings, RTU_DEFAULT_SETTINGS);
        rtuSettings.put(PlatformDistributionAutomationKeys.KEY_NETWORKADDRESS, this.iec61850MockServerConfig.iec61850MockNetworkAddress());

        this.rtuDeviceSteps.anRtuDevice(rtuSettings);

        this.rtuDeviceSteps.updateRtuDevice(rtuSettings);

        this.createIec61850Device(rtuSettings);
    }

    @Transactional("txMgrIec61850")
    private void createIec61850Device(final Map<String, String> settings) {

        /*
         * Make sure an ICD filename, port and servername corresponding to the
         * mock server settings will be used from the application to connect to
         * the device. ICD filename, port and servername may be null
         */
        final Iec61850Device iec61850Device = new Iec61850Device(
                getString(settings, PlatformDistributionAutomationKeys.KEY_DEVICE_IDENTIFICATION, PlatformDistributionAutomationDefaults.DEFAULT_DEVICE_IDENTIFICATION));

        iec61850Device.setIcdFilename(getString(settings, PlatformDistributionAutomationKeys.KEY_IEC61850_ICD_FILENAME));
        iec61850Device.setPort(getInteger(settings, PlatformDistributionAutomationKeys.KEY_IEC61850_PORT));
        iec61850Device.setServerName(getString(settings, PlatformDistributionAutomationKeys.KEY_IEC61850_SERVERNAME));

        this.iec61850DeviceRepository.save(iec61850Device);
    }
}
