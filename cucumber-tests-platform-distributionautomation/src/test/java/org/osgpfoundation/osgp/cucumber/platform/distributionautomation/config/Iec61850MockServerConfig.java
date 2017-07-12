/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config;

import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.mocks.iec61850.Iec61850MockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Iec61850MockServerConfig {

    @Value("${iec61850.mock.networkaddress}")
    private String iec61850MockNetworkAddress;

    @Value("${iec61850.mock.icdfilename}")
    private String iec61850MockIcdFilename;

    @Bean
    public String iec61850MockNetworkAddress() {
        return this.iec61850MockNetworkAddress;
    }

    @Bean(destroyMethod = "stop", initMethod = "start")
    public Iec61850MockServer iec61850MockServerWAGO61850ServerRTU1() {
        return new Iec61850MockServer("WAGO61850ServerRTU1", iec61850MockIcdFilename, 62100, "WAGO61850ServerRTU1");
    }
}
