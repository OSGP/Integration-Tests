/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.publiclighting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alliander.osgp.cucumber.platform.config.CoreDeviceConfiguration;
import com.alliander.osgp.cucumber.platform.publiclighting.mocks.oslpdevice.MockOslpServer;

@Configuration
public class OslpMockServerConfig {

    @Autowired
    private CoreDeviceConfiguration configuration;

    @Value("${oslp.port.server}")
    private int oslpPortServer;

    @Value("${oslp.elster.port.server}")
    private int oslpElsterPortServer;

    @Value("${oslp.security.signature}")
    private String oslpSignature;

    @Value("${oslp.security.provider}")
    private String oslpSignatureProvider;

    @Value("${oslp.timeout.connect}")
    private int connectionTimeout;

    @Value("${oslp.security.signkey.path}")
    private String signKeyPath;

    @Value("${oslp.security.verifykey.path}")
    private String verifyKeyPath;

    @Value("${oslp.security.keytype}")
    private String keytype;

    @Value("${oslp.sequence.number.window}")
    private Integer sequenceNumberWindow;

    @Value("${oslp.sequence.number.maximum}")
    private Integer sequenceNumberMaximum;

    @Value("${response.delay.time}")
    private Long responseDelayTime;

    @Value("${response.delay.random.range}")
    private Long reponseDelayRandomRange;

    @Bean(destroyMethod = "stop", initMethod = "start")
    public MockOslpServer mockOslpServer() {
        return new MockOslpServer(this.configuration, this.oslpPortServer, this.oslpElsterPortServer,
                this.oslpSignature, this.oslpSignatureProvider, this.connectionTimeout, this.signKeyPath,
                this.verifyKeyPath, this.keytype, this.sequenceNumberWindow, this.sequenceNumberMaximum,
                this.responseDelayTime, this.reponseDelayRandomRange);
    }
}
