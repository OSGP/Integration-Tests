/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support.ws.publiclighting;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.ResumeScheduleAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.ResumeScheduleAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.ResumeScheduleRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.ResumeScheduleResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetLightAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetLightAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetLightRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetLightResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetTransitionAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetTransitionAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetTransitionRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetTransitionResponse;
import com.alliander.osgp.platform.cucumber.support.ws.BaseClient;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceSecurityException;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceTemplateFactory;

@Component
public class PublicLightingAdHocManagementClient extends BaseClient {

	@Autowired
    private WebServiceTemplateFactory publicLightingAdHocManagementWstf;

	public GetStatusAsyncResponse getStatus(GetStatusRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetStatusAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
	}   

	public GetStatusResponse getGetStatusResponse(GetStatusAsyncRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetStatusResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public ResumeScheduleAsyncResponse resumeScheduleStatus(ResumeScheduleRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (ResumeScheduleAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public ResumeScheduleResponse getResumeScheduleResponse(ResumeScheduleAsyncRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (ResumeScheduleResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public SetLightAsyncResponse setLight(SetLightRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetLightAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public SetLightResponse getSetLightResponse(SetLightAsyncRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
	    final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetLightResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public SetTransitionAsyncResponse setTransition(SetTransitionRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetTransitionAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

	public SetTransitionResponse getSetTransitionResponse(SetTransitionAsyncRequest request) throws WebServiceSecurityException, GeneralSecurityException, IOException {
	    final WebServiceTemplate webServiceTemplate = this.publicLightingAdHocManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetTransitionResponse) webServiceTemplate.marshalSendAndReceive(request);
	}

}