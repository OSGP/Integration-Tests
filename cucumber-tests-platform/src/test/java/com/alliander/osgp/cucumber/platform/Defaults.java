/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform;

import org.joda.time.DateTime;

import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LongTermIntervalType;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.TransitionType;
import com.alliander.osgp.adapter.ws.schema.publiclighting.common.OsgpResultType;
import com.alliander.osgp.adapter.ws.schema.publiclighting.devicemonitoring.MeterType;
import com.alliander.osgp.domain.core.valueobjects.RelayType;
import com.alliander.osgp.oslp.Oslp.LightType;
import com.alliander.osgp.oslp.Oslp.LinkType;
import com.alliander.osgp.oslp.Oslp.Status;

/**
 * Defaults within the database.
 */
public class Defaults {

    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LightType CONFIGURATION_LIGHTTYPE = null;
    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.MeterType CONFIGURATION_METER_TYPE = null;
    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LinkType CONFIGURATION_PREFERRED_LINKTYPE = null;
    public static final Boolean DEFAULT_ACTIVATED = true;
    public static final Boolean DEFAULT_ACTIVE = true;
    public static final Integer DEFAULT_ACTUAL_CONSUMED_ENERGY = 96;
    public static final Integer DEFAULT_ACTUAL_CONSUMED_POWER = 48;
    public static final Integer DEFAULT_ACTUAL_CURRENT1 = 1;
    public static final Integer DEFAULT_ACTUAL_CURRENT2 = 2;
    public static final Integer DEFAULT_ACTUAL_CURRENT3 = 3;
    public static final LinkType DEFAULT_ACTUAL_LINKTYPE = LinkType.LINK_NOT_SET;
    public static final Integer DEFAULT_ACTUAL_POWER1 = 1;
    public static final Integer DEFAULT_ACTUAL_POWER2 = 2;
    public static final Integer DEFAULT_ACTUAL_POWER3 = 3;
    public static final String DEFAULT_ALIAS = "";
    public static final Boolean DEFAULT_ALLOWED = false;

    public static final Integer DEFAULT_AVERAGE_POWER_FACTOR1 = 1;

    public static final Integer DEFAULT_AVERAGE_POWER_FACTOR2 = 2;
    public static final Integer DEFAULT_AVERAGE_POWER_FACTOR3 = 3;
    public static final String DEFAULT_BEGIN_DATE = "";
    public static final Short DEFAULT_CHANNEL = new Short((short) 1);
    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LightType DEFAULT_CONFIGURATION_LIGHTTYPE = com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LightType.RELAY;
    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.MeterType DEFAULT_CONFIGURATION_METER_TYPE = com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.MeterType.AUX;

    public static final com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LinkType DEFAULT_CONFIGURATION_PREFERRED_LINKTYPE = com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.LinkType.ETHERNET;
    public static final String DEFAULT_CONTAINER_CITY = "";
    public static final String DEFAULT_CONTAINER_MUNICIPALITY = "";
    public static final String DEFAULT_CONTAINER_NUMBER = "";
    public static final String DEFAULT_CONTAINER_POSTALCODE = "";
    public static final String DEFAULT_CONTAINER_STREET = "";
    public static final String DEFAULT_DC_LIGHTS = "0";
    public static final Long DEFAULT_DEVICE_ID = new java.util.Random().nextLong();
    public static final String DEFAULT_DEVICE_IDENTIFICATION = "TD01";
    public static final String DEFAULT_DEVICE_MODEL_DESCRIPTION = "Test Model";
    public static final String DEFAULT_DEVICE_MODEL_MANUFACTURER = "Kaif";
    public static final Boolean DEFAULT_DEVICE_MODEL_METERED = true;
    public static final String DEFAULT_DEVICE_MODEL_MODEL_CODE = "Test";
    public static final String DEFAULT_DEVICE_MODEL_NAME = "Test Model";
    public static final String DEFAULT_DEVICE_OUTPUT_SETTING_ALIAS = "Continues burner";
    public static final Integer DEFAULT_DEVICE_OUTPUT_SETTING_EXTERNALID = 1;
    public static final Integer DEFAULT_DEVICE_OUTPUT_SETTING_INTERNALID = 1;

    public static final RelayType DEFAULT_DEVICE_OUTPUT_SETTING_RELAY_TYPE = RelayType.LIGHT;

    public static final String DEFAULT_DEVICE_TYPE = "OSLP";

    public static final Integer DEFAULT_DIMVALUE = 100;
    public static final String DEFAULT_DOMAINS = "COMMON;PUBLIC_LIGHTING;TARIFF_SWITCHING";

    public static final String DEFAULT_END_DATE = "";
    public static final String DEFAULT_EVENTNOTIFICATIONS = "";
    public static final String DEFAULT_EVENTNOTIFICATIONTYPES = "";
    public static final Integer DEFAULT_EXTERNALID = 0;
    public static final Boolean DEFAULT_FILESTORAGE = true;

    public static final String DEFAULT_FIRMWARE_IDENTIFICATION = "";
    public static final Boolean DEFAULT_HASSCHEDULE = false;
    public static final com.alliander.osgp.adapter.ws.schema.publiclighting.devicemonitoring.HistoryTermType DEFAULT_HISTORY_TERM_TYPE = com.alliander.osgp.adapter.ws.schema.publiclighting.devicemonitoring.HistoryTermType.SHORT;
    public static final Boolean DEFAULT_INDEBUGMODE = false;
    public static final Integer DEFAULT_INDEX = 0;
    public static final Integer DEFAULT_INTERNALID = 0;
    public static final LongTermIntervalType DEFAULT_INTERVAL_TYPE = LongTermIntervalType.DAYS;

    public static final Boolean DEFAULT_IS_ACTIVATED = true;
    public static final Boolean DEFAULT_ISIMMEDIATE = false;
    public static final Float DEFAULT_LATITUDE = new Float(0);
    public static final LightType DEFAULT_LIGHTTYPE = LightType.LT_NOT_SET;
    public static final String DEFAULT_LIGHTVALUES = "";

    public static final Integer DEFAULT_LONG_INTERVAL = 1;
    public static final Float DEFAULT_LONGITUDE = new Float(0);
    public static final String DEFAULT_MANUFACTURER_CODE = "Test";
    public static final String DEFAULT_MANUFACTURER_ID = "Test";
    public static final String DEFAULT_MANUFACTURER_NAME = "Test Manufacturer";
    public static final Boolean DEFAULT_MANUFACTURER_USE_PREFIX = false;
    public static final MeterType DEFAULT_METER_TYPE = MeterType.AUX;
    public static final String DEFAULT_NEW_ORGANIZATION_IDENTIFICATION = "NewOrganization";
    public static final String DEFAULT_NEW_ORGANIZATION_NAME = "New Organization";
    public static final com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.PlatformFunctionGroup DEFAULT_NEW_ORGANIZATION_PLATFORMFUNCTIONGROUP = com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.PlatformFunctionGroup.ADMIN;
    public static final Boolean DEFAULT_ON = true;
    public static final String DEFAULT_ORGANIZATION_DESCRIPTION = "Test Organization";
    public static final String DEFAULT_ORGANIZATION_DOMAINS = "COMMON;PUBLIC_LIGHTING;TARIFF_SWITCHING";
    public static final Boolean DEFAULT_ORGANIZATION_ENABLED = true;
    public static final String DEFAULT_ORGANIZATION_IDENTIFICATION = "test-org";
    public static final String DEFAULT_ORGANIZATION_NAME = "Test organization";
    public static final String DEFAULT_ORGANIZATION_PREFIX = "cgi";

    public static final com.alliander.osgp.oslp.Oslp.HistoryTermType DEFAULT_OSLP_HISTORY_TERM_TYPE = com.alliander.osgp.oslp.Oslp.HistoryTermType.Short;
    public static final String DEFAULT_OWNER = "test-org";
    public static final Short DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String DEFAULT_PERIOD_TYPE = "INTERVAL";
    public static final LinkType DEFAULT_PREFERRED_LINKTYPE = LinkType.LINK_NOT_SET;
    public static final String DEFAULT_PREFIX = "MAA";
    public static final String DEFAULT_PROTOCOL = "OSLP";
    public static final Long DEFAULT_PROTOCOL_INFO_ID = new java.util.Random().nextLong();
    public static final String DEFAULT_PROTOCOL_VERSION = "1.0";
    public static final String DEFAULT_PUBLIC_KEY = "123456abcdef";
    public static final Boolean DEFAULT_PUBLICKEYPRESENT = true;
    public static final String DEFAULT_PUBLICLIGHTING_DESCRIPTION = "";
    public static final OsgpResultType DEFAULT_PUBLICLIGHTING_STATUS = OsgpResultType.OK;
    public static final String DEFAULT_RECORD_TIME = "";
    public static final Integer DEFAULT_SHORT_INTERVAL = 15;
    public static final String DEFAULT_SMART_METER_DEVICE_IDENTIFICATION = "TEST1024000000001";
    public static final String DEFAULT_SMART_METER_GAS_DEVICE_IDENTIFICATION = "TESTG102400000001";
    public static final Status DEFAULT_STATUS = Status.OK;
    public static final String DEFAULT_SUPPLIER = "Kaifa";
    public static final DateTime DEFAULT_TECHNICAL_INSTALLATION_DATE = DateTime.now().minusDays(1);
    public static final Integer DEFAULT_TOTAL_LIGHTING_HOURS = 144;

    public static final TransitionType DEFAULT_TRANSITION_TYPE = TransitionType.DAY_NIGHT;
    public static final Boolean DEFAULT_USE_PAGES = true;
    public static final String DEFAULT_USER_NAME = "Cucumber";
    public static final String DEVICE_MODEL_MODEL_CODE = "Test";
    public static final String DEVICE_MODEL_NAME = "Testmodel";
    public static final String DLMS_DEFAULT_COMMUNICATION_METHOD = "GPRS";
    public static final String DLMS_DEFAULT_DEVICE_TYPE = "SMART_METER_E";

    public static final Boolean DLMS_DEFAULT_HSL3_ACTIVE = false;
    public static final Boolean DLMS_DEFAULT_HSL4_ACTIVE = false;
    public static final Boolean DLMS_DEFAULT_HSL5_ACTIVE = true;

    public static final Boolean DLMS_DEFAULT_IP_ADDRESS_IS_STATIC = true;
    public static final long DLMS_DEFAULT_LOGICAL_ID = 1L;
    public static final long DLMS_DEFAULT_PORT = 1024L;
    public static final String DLMS_DEFAULT_DEVICE_DELIVERY_DATE = "2016-05-11T00:00:00.000Z";
    public static final String EMAIL = "someone@somewhere.nl";
    public static final Boolean EVENTS_NODELIST_EXPECTED = false;
    public static final String EXPECTED_RESULT_OK = "OK";
    public static final DateTime EXPIRYDATECONTRACT = DateTime.now().plusWeeks(1);
    public static final LongTermIntervalType INTERVAL_TYPE = LongTermIntervalType.DAYS;
    public static final Integer LONG_INTERVAL = 0;
    public static final String MANUFACTURER_CODE = "Test";
    public static final String MANUFACTURER_NAME = "Test";
    public static final boolean MANUFACTURER_USE_PREFIX = false;
    public static final String PHONENUMBER = "+31 43 1234567";
    public static final com.alliander.osgp.domain.core.valueobjects.PlatformFunctionGroup PLATFORM_FUNCTION_GROUP = com.alliander.osgp.domain.core.valueobjects.PlatformFunctionGroup.ADMIN;
    public static final Integer SHORT_INTERVAL = 15;
    public static final String SMART_METER_E = "SMART_METER_E";
    public static final String SMART_METER_G = "SMART_METER_G";

    public static final String FIRMWARE_IDENTIFICATION = "F01";
    public static final Boolean FIRMWARE_PUSH_TO_NEW_DEVICE = false;
    public static final String DC_LIGHTS = "";
    public static final int FIRMWARE_ID = 0;
    public static final String FIRMWARE_DESCRIPTION = "Test Firmware";
}