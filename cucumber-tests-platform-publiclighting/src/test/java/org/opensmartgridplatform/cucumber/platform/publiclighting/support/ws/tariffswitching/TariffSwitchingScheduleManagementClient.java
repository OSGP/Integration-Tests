/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.publiclighting.support.ws.tariffswitching;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import org.opensmartgridplatform.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncRequest;
import org.opensmartgridplatform.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncResponse;
import org.opensmartgridplatform.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleRequest;
import org.opensmartgridplatform.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleResponse;
import org.opensmartgridplatform.cucumber.platform.support.ws.BaseClient;
import org.opensmartgridplatform.shared.exceptionhandling.WebServiceSecurityException;
import org.opensmartgridplatform.shared.infra.ws.DefaultWebServiceTemplateFactory;

@Component
public class TariffSwitchingScheduleManagementClient extends BaseClient {

    @Autowired
    private DefaultWebServiceTemplateFactory tariffSwitchingScheduleManagementWstf;

    public SetScheduleAsyncResponse setSchedule(final SetScheduleRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.tariffSwitchingScheduleManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());

        return (SetScheduleAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    public SetScheduleResponse getSetSchedule(final SetScheduleAsyncRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.tariffSwitchingScheduleManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());

        return (SetScheduleResponse) webServiceTemplate.marshalSendAndReceive(request);
    }
}
