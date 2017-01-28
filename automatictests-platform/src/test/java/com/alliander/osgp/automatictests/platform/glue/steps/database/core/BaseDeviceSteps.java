/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.automatictests.platform.glue.steps.database.core;

import static com.alliander.osgp.automatictests.platform.core.Helpers.getBoolean;
import static com.alliander.osgp.automatictests.platform.core.Helpers.getDate;
import static com.alliander.osgp.automatictests.platform.core.Helpers.getEnum;
import static com.alliander.osgp.automatictests.platform.core.Helpers.getFloat;
import static com.alliander.osgp.automatictests.platform.core.Helpers.getLong;
import static com.alliander.osgp.automatictests.platform.core.Helpers.getString;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.automatictests.platform.Defaults;
import com.alliander.osgp.automatictests.platform.Keys;
import com.alliander.osgp.automatictests.platform.StepsBase;
import com.alliander.osgp.automatictests.platform.config.CoreDeviceConfiguration;
import com.alliander.osgp.automatictests.platform.core.ScenarioContext;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceAuthorization;
import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.DeviceModelRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.OrganisationRepository;
import com.alliander.osgp.domain.core.repositories.ProtocolInfoRepository;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;

public abstract class BaseDeviceSteps extends StepsBase {

    @Autowired
    private CoreDeviceConfiguration configuration;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OrganisationRepository organizationRepository;

    @Autowired
    private DeviceAuthorizationRepository deviceAuthorizationRepository;

    @Autowired
    private ProtocolInfoRepository protocolInfoRepository;

    /**
     * Update a device entity given its device identification.
     *
     * @param deviceIdentification
     *            The deviceIdentification.
     * @param settings
     *            The settings.
     */
    public Device updateDevice(final String deviceIdentification, final Map<String, String> settings) {
        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        return this.updateDevice(device, settings);
    }

    /**
     * Update an existing device with the given settings.
     *
     * @param device
     * @param settings
     */
    public Device updateDevice(Device device, final Map<String, String> settings) {

        // Now set the optional stuff
        if (settings.containsKey(Keys.TECHNICAL_INSTALLATION_DATE)
                && !settings.get(Keys.TECHNICAL_INSTALLATION_DATE).isEmpty()) {
            device.setTechnicalInstallationDate(getDate(settings, Keys.TECHNICAL_INSTALLATION_DATE).toDate());
        }

        final DeviceModel deviceModel = this.deviceModelRepository
                .findByModelCode(getString(settings, Keys.DEVICE_MODEL, Defaults.DEVICE_MODEL_MODEL_CODE));
        device.setDeviceModel(deviceModel);

        device.updateProtocol(this.protocolInfoRepository.findByProtocolAndProtocolVersion(
                getString(settings, Keys.PROTOCOL, Defaults.PROTOCOL),
                getString(settings, Keys.PROTOCOL_VERSION, Defaults.PROTOCOL_VERSION)));

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(getString(settings, Keys.NETWORKADDRESS, this.configuration.getDeviceNetworkAddress()));
        } catch (final UnknownHostException e) {
            inetAddress = InetAddress.getLoopbackAddress();
        }
        device.updateRegistrationData(inetAddress, getString(settings, Keys.DEVICE_TYPE, Defaults.DEVICE_TYPE));

        device.setVersion(getLong(settings, Keys.VERSION));
        device.setActive(getBoolean(settings, Keys.ACTIVE, Defaults.ACTIVE));
        if (getString(settings, Keys.ORGANIZATION_IDENTIFICATION, Defaults.ORGANIZATION_IDENTIFICATION)
                .toLowerCase() != "null") {
            device.addOrganisation(
                    getString(settings, Keys.ORGANIZATION_IDENTIFICATION, Defaults.ORGANIZATION_IDENTIFICATION));
        }
        device.updateMetaData(getString(settings, Keys.ALIAS, Defaults.ALIAS),
                getString(settings, Keys.CITY, Defaults.CONTAINER_CITY),
                getString(settings, Keys.POSTCODE, Defaults.CONTAINER_POSTALCODE),
                getString(settings, Keys.STREET, Defaults.CONTAINER_STREET),
                getString(settings, Keys.NUMBER, Defaults.CONTAINER_NUMBER),
                getString(settings, Keys.MUNICIPALITY, Defaults.CONTAINER_MUNICIPALITY),
                getFloat(settings, Keys.LATITUDE, Defaults.LATITUDE),
                getFloat(settings, Keys.LONGITUDE, Defaults.LONGITUDE));

        device.setActivated(getBoolean(settings, Keys.IS_ACTIVATED, Defaults.IS_ACTIVATED));
        device = this.deviceRepository.save(device);

        if (getString(settings, Keys.ORGANIZATION_IDENTIFICATION, Defaults.ORGANIZATION_IDENTIFICATION)
                .toLowerCase() != "null") {
            final Organisation organization = this.organizationRepository.findByOrganisationIdentification(
                    getString(settings, Keys.ORGANIZATION_IDENTIFICATION, Defaults.ORGANIZATION_IDENTIFICATION));
            final DeviceFunctionGroup functionGroup = getEnum(settings, Keys.DEVICEFUNCTIONGROUP,
                    DeviceFunctionGroup.class, DeviceFunctionGroup.OWNER);
            final DeviceAuthorization authorization = device.addAuthorization(organization, functionGroup);
            final Device savedDevice = this.deviceRepository.save(device);
            this.deviceAuthorizationRepository.save(authorization);
            ScenarioContext.Current().put(Keys.DEVICE_IDENTIFICATION, savedDevice.getDeviceIdentification());

            device = savedDevice;
        }

        return device;
    }
}
