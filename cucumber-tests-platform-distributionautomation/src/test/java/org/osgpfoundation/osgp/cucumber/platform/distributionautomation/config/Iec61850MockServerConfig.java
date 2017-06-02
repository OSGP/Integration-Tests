/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.cucumber.platform.distributionautomation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.osgpfoundation.osgp.cucumber.platform.distributionautomation.mocks.iec61850.Iec61850MockServer;

@Configuration
public class Iec61850MockServerConfig {

    @Value("${iec61850.mock.networkaddress}")
    private String iec61850MockNetworkAddress;

    @Bean
    public String iec61850MockNetworkAddress() {
        return this.iec61850MockNetworkAddress;
    }

    @Bean(destroyMethod = "stop", initMethod = "start")
    public Iec61850MockServer iec61850MockServerWAGO61850ServerRTU1() {
        return new Iec61850MockServer("WAGO61850ServerRTU1", "Simple_substation_v0.13 (incl. 2 bays and edition 2.0).icd", 62105, "WAGO61850ServerRTU1");
    }
}
