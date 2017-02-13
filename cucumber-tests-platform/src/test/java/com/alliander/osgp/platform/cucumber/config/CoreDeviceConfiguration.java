/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Base core device configuration.
 */
@Configuration
public class CoreDeviceConfiguration extends ApplicationConfiguration {

	@Value("${device.networkaddress}")
	private String deviceNetworkaddress;

	public String getDeviceNetworkAddress() {
	    return this.deviceNetworkaddress;
	}
	
}
