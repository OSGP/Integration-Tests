/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support.ws.tariffswitching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.platform.cucumber.support.ws.BaseClient;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceTemplateFactory;

@Component
public class TariffSwitchingAdHocManagementClient extends BaseClient {

    @Autowired
    private WebServiceTemplateFactory tariffSwitchingAdHocManagementWstf;

}